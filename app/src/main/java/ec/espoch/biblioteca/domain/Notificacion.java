package ec.espoch.biblioteca.domain;

import java.time.LocalDate;

public class Notificacion {

    private int       id;
    private Prestamo  prestamo;
    private String    mensaje;
    private LocalDate fecha;

    public Notificacion(Prestamo prestamo, String mensaje) {
        this.prestamo = prestamo;
        this.mensaje  = mensaje;
        this.fecha    = LocalDate.now();
    }

    public int       getId()      { return id; }
    public Prestamo  getPrestamo(){ return prestamo; }
    public String    getMensaje() { return mensaje; }
    public LocalDate getFecha()   { return fecha; }

    public void setId(int id)  { this.id = id; }

    @Override
    public String toString() {
        return String.format("[NOTIF %s] %s", fecha, mensaje);
    }
}
