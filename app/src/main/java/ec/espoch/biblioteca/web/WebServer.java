package ec.espoch.biblioteca.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ec.espoch.biblioteca.db.DatabaseManager;
import ec.espoch.biblioteca.db.PrestamoRepository;
import ec.espoch.biblioteca.domain.*;
import ec.espoch.biblioteca.factory.BibliotecaFactory;
import ec.espoch.biblioteca.servicios.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

public class WebServer {

    private static final int PORT = 7000;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void start() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/",           WebServer::serveIndex);
        server.createContext("/api/usuarios",  WebServer::handleUsuarios);
        server.createContext("/api/catalogo",  WebServer::handleCatalogo);
        server.createContext("/api/historial", WebServer::handleHistorial);
        server.createContext("/api/prestamos", WebServer::handlePrestamos);

        server.setExecutor(null);
        server.start();

        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║   BIBLIOTECA DIGITAL — Modo Web          ║");
        System.out.printf ("║   http://localhost:%d                   ║%n", PORT);
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println("   Presiona Ctrl+C para detener el servidor.");
    }

    // ── Sirve index.html ──────────────────────────────────────────────────────
    private static void serveIndex(HttpExchange ex) throws IOException {
        try (InputStream is = WebServer.class.getResourceAsStream("/public/index.html")) {
            if (is == null) { respond(ex, 404, "text/plain", "index.html no encontrado"); return; }
            byte[] body = is.readAllBytes();
            ex.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            ex.sendResponseHeaders(200, body.length);
            ex.getResponseBody().write(body);
        } finally { ex.close(); }
    }

    // ── GET /api/usuarios ─────────────────────────────────────────────────────
    private static void handleUsuarios(HttpExchange ex) throws IOException {
        if (!"GET".equals(ex.getRequestMethod())) { respond(ex, 405, "text/plain", "Method Not Allowed"); return; }
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            List<Map<String,Object>> list = new ArrayList<>();
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT id, nombre, codigo, email FROM usuarios ORDER BY id")) {
                while (rs.next()) {
                    Map<String,Object> m = new LinkedHashMap<>();
                    m.put("id",     rs.getInt("id"));
                    m.put("nombre", rs.getString("nombre"));
                    m.put("codigo", rs.getString("codigo"));
                    m.put("email",  rs.getString("email"));
                    list.add(m);
                }
            }
            respondJson(ex, 200, list);
        } catch (Exception e) { respondJson(ex, 500, Map.of("error", e.getMessage())); }
    }

    // ── GET /api/catalogo ─────────────────────────────────────────────────────
    private static void handleCatalogo(HttpExchange ex) throws IOException {
        if (!"GET".equals(ex.getRequestMethod())) { respond(ex, 405, "text/plain", "Method Not Allowed"); return; }
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            List<Map<String,Object>> list = new ArrayList<>();
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT id, tipo, titulo, autor, codigo, costo_base FROM recursos ORDER BY tipo, id")) {
                while (rs.next()) {
                    Map<String,Object> m = new LinkedHashMap<>();
                    m.put("id",        rs.getInt("id"));
                    m.put("tipo",      rs.getString("tipo"));
                    m.put("titulo",    rs.getString("titulo"));
                    m.put("autor",     rs.getString("autor"));
                    m.put("codigo",    rs.getString("codigo"));
                    m.put("costoBase", rs.getDouble("costo_base"));
                    list.add(m);
                }
            }
            respondJson(ex, 200, list);
        } catch (Exception e) { respondJson(ex, 500, Map.of("error", e.getMessage())); }
    }

    // ── GET /api/historial ────────────────────────────────────────────────────
    private static void handleHistorial(HttpExchange ex) throws IOException {
        if (!"GET".equals(ex.getRequestMethod())) { respond(ex, 405, "text/plain", "Method Not Allowed"); return; }
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            List<Map<String,Object>> list = new ArrayList<>();
            String sql = """
                SELECT p.id, u.nombre AS usuario, p.fecha, p.estado, p.costo_total,
                       COUNT(dp.id) AS recursos
                FROM prestamos p
                JOIN usuarios u ON u.id = p.usuario_id
                LEFT JOIN detalle_prestamo dp ON dp.prestamo_id = p.id
                GROUP BY p.id, u.nombre, p.fecha, p.estado, p.costo_total
                ORDER BY p.id DESC
                """;
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    Map<String,Object> m = new LinkedHashMap<>();
                    m.put("id",         rs.getInt("id"));
                    m.put("usuario",    rs.getString("usuario"));
                    m.put("fecha",      rs.getString("fecha"));
                    m.put("estado",     rs.getString("estado"));
                    m.put("costoTotal", rs.getDouble("costo_total"));
                    m.put("recursos",   rs.getInt("recursos"));
                    list.add(m);
                }
            }
            respondJson(ex, 200, list);
        } catch (Exception e) { respondJson(ex, 500, Map.of("error", e.getMessage())); }
    }

    // ── POST /api/prestamos ───────────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    private static void handlePrestamos(HttpExchange ex) throws IOException {
        if (!"POST".equals(ex.getRequestMethod())) { respond(ex, 405, "text/plain", "Method Not Allowed"); return; }
        try {
            String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Map<String,Object> req = GSON.fromJson(body, Map.class);

            int usuarioId = ((Double) req.get("usuarioId")).intValue();
            List<Map<String,Object>> recursos = (List<Map<String,Object>>) req.get("recursos");

            Connection conn  = DatabaseManager.getInstance().getConnection();
            PrestamoRepository repo = new PrestamoRepository(conn);
            BibliotecaFactory factory = new BibliotecaFactory();

            Usuario usuario = repo.cargarUsuario(usuarioId);
            Prestamo prestamo = new Prestamo(usuario);

            for (Map<String,Object> r : recursos) {
                String tipo    = (String) r.get("tipo");
                String titulo  = (String) r.getOrDefault("titulo", "Sin título");
                String autor   = (String) r.getOrDefault("autor",  "Desconocido");
                String codigo  = (String) r.getOrDefault("codigo", UUID.randomUUID().toString().substring(0,8).toUpperCase());
                String servicio= (String) r.getOrDefault("servicio", "");
                int    dias    = r.containsKey("dias") ? ((Double) r.get("dias")).intValue() : 7;

                RecursoBibliografico recurso = factory.crearRecurso(tipo, titulo, autor, codigo);
                agregarServicio(recurso, servicio, dias);
                prestamo.agregarRecurso(recurso, dias);
            }

            Notificacion notif = repo.save(prestamo);

            Map<String,Object> result = new LinkedHashMap<>();
            result.put("id",           prestamo.getId());
            result.put("usuario",      usuario.getNombre());
            result.put("costoTotal",   prestamo.calcularCostoTotal());
            result.put("notificacion", notif.getMensaje());
            result.put("recursos",     prestamo.getDetalles().size());

            respondJson(ex, 201, result);
        } catch (IllegalArgumentException | IllegalStateException e) {
            respondJson(ex, 400, Map.of("error", e.getMessage()));
        } catch (Exception e) {
            respondJson(ex, 500, Map.of("error", e.getMessage()));
        }
    }

    // ── helpers ───────────────────────────────────────────────────────────────
    private static void agregarServicio(RecursoBibliografico r, String servicio, int dias) {
        switch (servicio) {
            case "EnvioDomicilio"     -> r.agregarServicio(new EnvioDomicilio());
            case "DescargaOffline"    -> r.agregarServicio(new DescargaOffline());
            case "PrestamoExtendido"  -> r.agregarServicio(new PrestamoExtendido(Math.min(dias, 14)));
            case "CertificadoLectura" -> r.agregarServicio(new CertificadoLectura());
            case "AccesoPremium"      -> r.agregarServicio(new AccesoPremium());
        }
    }

    private static void respondJson(HttpExchange ex, int code, Object data) throws IOException {
        respond(ex, code, "application/json; charset=UTF-8", GSON.toJson(data));
    }

    private static void respond(HttpExchange ex, int code, String ct, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", ct);
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        ex.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(bytes); }
    }
}
