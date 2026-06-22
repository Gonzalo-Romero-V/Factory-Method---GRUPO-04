package ec.espoch.biblioteca.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Prestamo {

    private int               id;
    private final Usuario     usuario;
    private final LocalDate   fecha;
    private String            estado;
    private final List<DetallePrestamo> detalles = new ArrayList<>();

    public Prestamo(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalStateException("No se puede crear un préstamo sin usuario asociado.");
        }
        this.usuario = usuario;
        this.fecha   = LocalDate.now();
        this.estado  = "activo";
    }

    public void agregarRecurso(RecursoBibliografico recurso, int dias) {
        detalles.add(new DetallePrestamo(recurso, dias));
    }

    public double calcularCostoTotal() {
        return detalles.stream().mapToDouble(DetallePrestamo::getCosto).sum();
    }

    public Notificacion generarNotificacion() {
        String msg = String.format(
            "Préstamo #%d confirmado para %s. Recursos: %d. Costo total: $%.2f",
            id, usuario.getNombre(), detalles.size(), calcularCostoTotal()
        );
        return new Notificacion(this, msg);
    }

    public int                   getId()       { return id; }
    public Usuario               getUsuario()  { return usuario; }
    public LocalDate             getFecha()    { return fecha; }
    public String                getEstado()   { return estado; }
    public List<DetallePrestamo> getDetalles() { return Collections.unmodifiableList(detalles); }

    public void setId(int id) { this.id = id; }
}
