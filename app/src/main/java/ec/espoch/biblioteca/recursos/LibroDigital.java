package ec.espoch.biblioteca.recursos;

import ec.espoch.biblioteca.domain.RecursoBibliografico;
import ec.espoch.biblioteca.servicios.DescargaOffline;
import ec.espoch.biblioteca.servicios.ServicioAdicional;

public class LibroDigital extends RecursoBibliografico {

    public LibroDigital(String titulo, String autor, String codigo, double costoBase) {
        super(titulo, autor, codigo, costoBase);
    }

    @Override
    public void agregarServicio(ServicioAdicional servicio) {
        if (!(servicio instanceof DescargaOffline)) {
            throw new IllegalArgumentException(
                "LibroDigital solo acepta el servicio DescargaOffline. Recibido: " + servicio.getTipo());
        }
        super.agregarServicio(servicio);
    }

    @Override
    public String obtenerDescripcion() {
        return String.format("[Libro Digital] %s — %s", titulo, autor);
    }

    @Override
    public String getTipo() { return "libro_digital"; }
}
