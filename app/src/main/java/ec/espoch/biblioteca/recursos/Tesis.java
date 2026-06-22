package ec.espoch.biblioteca.recursos;

import ec.espoch.biblioteca.domain.RecursoBibliografico;
import ec.espoch.biblioteca.servicios.CertificadoLectura;
import ec.espoch.biblioteca.servicios.ServicioAdicional;

public class Tesis extends RecursoBibliografico {

    public Tesis(String titulo, String autor, String codigo, double costoBase) {
        super(titulo, autor, codigo, costoBase);
    }

    @Override
    public void agregarServicio(ServicioAdicional servicio) {
        if (!(servicio instanceof CertificadoLectura)) {
            throw new IllegalArgumentException(
                "Tesis solo acepta el servicio CertificadoLectura. Recibido: " + servicio.getTipo());
        }
        super.agregarServicio(servicio);
    }

    @Override
    public String obtenerDescripcion() {
        return String.format("[Tesis] %s — %s", titulo, autor);
    }

    @Override
    public String getTipo() { return "tesis"; }
}
