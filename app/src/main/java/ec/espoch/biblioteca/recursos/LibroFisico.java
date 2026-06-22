package ec.espoch.biblioteca.recursos;

import ec.espoch.biblioteca.domain.RecursoBibliografico;
import ec.espoch.biblioteca.servicios.EnvioDomicilio;
import ec.espoch.biblioteca.servicios.ServicioAdicional;

public class LibroFisico extends RecursoBibliografico {

    public LibroFisico(String titulo, String autor, String codigo, double costoBase) {
        super(titulo, autor, codigo, costoBase);
    }

    @Override
    public void agregarServicio(ServicioAdicional servicio) {
        if (!(servicio instanceof EnvioDomicilio)) {
            throw new IllegalArgumentException(
                "LibroFisico solo acepta el servicio EnvioDomicilio. Recibido: " + servicio.getTipo());
        }
        super.agregarServicio(servicio);
    }

    @Override
    public String obtenerDescripcion() {
        return String.format("[Libro Físico] %s — %s", titulo, autor);
    }

    @Override
    public String getTipo() { return "libro_fisico"; }
}
