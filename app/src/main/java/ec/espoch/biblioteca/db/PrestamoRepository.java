package ec.espoch.biblioteca.db;

import ec.espoch.biblioteca.domain.*;
import ec.espoch.biblioteca.servicios.ServicioAdicional;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoRepository {

    private final Connection conn;

    public PrestamoRepository(Connection conn) {
        this.conn = conn;
    }

    /** Guarda el préstamo completo y genera la notificación con el ID real. */
    public Notificacion save(Prestamo prestamo) throws SQLException {
        int usuarioId = asegurarUsuario(prestamo.getUsuario());

        // 1. Insertar cabecera de préstamo
        int prestamoId;
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO prestamos (usuario_id, fecha, estado, costo_total) VALUES (?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt   (1, usuarioId);
            ps.setDate  (2, Date.valueOf(prestamo.getFecha()));
            ps.setString(3, prestamo.getEstado());
            ps.setDouble(4, prestamo.calcularCostoTotal());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            keys.next();
            prestamoId = keys.getInt(1);
            prestamo.setId(prestamoId);
        }

        // 2. Insertar cada detalle
        for (DetallePrestamo detalle : prestamo.getDetalles()) {
            int recursoId = asegurarRecurso(detalle.getRecurso());
            int detalleId;
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO detalle_prestamo (prestamo_id, recurso_id, dias, costo) VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt   (1, prestamoId);
                ps.setInt   (2, recursoId);
                ps.setInt   (3, detalle.getDias());
                ps.setDouble(4, detalle.getCosto());
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                keys.next();
                detalleId = keys.getInt(1);
                detalle.setId(detalleId);
            }

            // 3. Insertar servicios del detalle
            for (ServicioAdicional s : detalle.getServicios()) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO detalle_servicios (detalle_id, tipo, descripcion, costo) VALUES (?,?,?,?)")) {
                    ps.setInt   (1, detalleId);
                    ps.setString(2, s.getTipo());
                    ps.setString(3, s.getDescripcion());
                    ps.setDouble(4, s.getCosto());
                    ps.executeUpdate();
                }
            }
        }

        // 4. Generar notificación con el ID real y persistirla
        Notificacion notificacion = prestamo.generarNotificacion();
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO notificaciones (prestamo_id, mensaje, fecha) VALUES (?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt   (1, prestamoId);
            ps.setString(2, notificacion.getMensaje());
            ps.setDate  (3, Date.valueOf(notificacion.getFecha()));
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) notificacion.setId(keys.getInt(1));
        }

        return notificacion;
    }

    /** Retorna todos los préstamos como resumen de texto para mostrar historial. */
    public List<String> obtenerHistorial() throws SQLException {
        List<String> historial = new ArrayList<>();
        String sql = """
            SELECT p.id, u.nombre, p.fecha, p.estado, p.costo_total,
                   COUNT(dp.id) AS recursos
            FROM prestamos p
            JOIN usuarios u ON u.id = p.usuario_id
            LEFT JOIN detalle_prestamo dp ON dp.prestamo_id = p.id
            GROUP BY p.id, u.nombre, p.fecha, p.estado, p.costo_total
            ORDER BY p.id
            """;
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                historial.add(String.format("  #%-3d %-20s %s  %-10s recursos: %d  $%.2f",
                    rs.getInt("id"), rs.getString("nombre"), rs.getDate("fecha"),
                    rs.getString("estado"), rs.getInt("recursos"), rs.getDouble("costo_total")));
            }
        }
        return historial;
    }

    /** Carga el primer usuario por ID (para el flujo demo). */
    public Usuario cargarUsuario(int id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT id, nombre, codigo, email FROM usuarios WHERE id = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) throw new SQLException("Usuario #" + id + " no encontrado.");
            Usuario u = new Usuario(rs.getString("nombre"), rs.getString("codigo"), rs.getString("email"));
            u.setId(rs.getInt("id"));
            return u;
        }
    }

    // ── helpers privados ─────────────────────────────────────────────────────

    private int asegurarUsuario(Usuario u) throws SQLException {
        if (u.getId() > 0) return u.getId();
        try (PreparedStatement ps = conn.prepareStatement(
                "MERGE INTO usuarios (nombre, codigo, email) KEY(codigo) VALUES (?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getCodigo());
            ps.setString(3, u.getEmail());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) { u.setId(keys.getInt(1)); }
        }
        return u.getId();
    }

    private int asegurarRecurso(ec.espoch.biblioteca.domain.RecursoBibliografico r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT id FROM recursos WHERE codigo = ?")) {
            ps.setString(1, r.getCodigo());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        }
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO recursos (tipo, titulo, autor, codigo, costo_base) VALUES (?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, r.getTipo());
            ps.setString(2, r.getTitulo());
            ps.setString(3, r.getAutor());
            ps.setString(4, r.getCodigo());
            ps.setDouble(5, r.getCostoBase());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            keys.next();
            return keys.getInt(1);
        }
    }
}
