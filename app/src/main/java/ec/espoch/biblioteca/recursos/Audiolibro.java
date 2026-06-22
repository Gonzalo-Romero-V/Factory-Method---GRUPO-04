package ec.espoch.biblioteca.recursos;

import ec.espoch.biblioteca.domain.RecursoBibliografico;
import ec.espoch.biblioteca.servicios.AccesoPremium;
import ec.espoch.biblioteca.servicios.ServicioAdicional;

public class Audiolibro extends RecursoBibliografico {

    public Audiolibro(String titulo, String autor, String codigo, double costoBase) {
        super(titulo, autor, codigo, costoBase);
    }

    @Override
    public void agregarServicio(ServicioAdicional servicio) {
        if (!(servicio instanceof AccesoPremium)) {
            throw new IllegalArgumentException(
                "Audiolibro solo acepta el servicio AccesoPremium. Recibido: " + servicio.getTipo());
        }
        super.agregarServicio(servicio);
    }

    @Override
    public String obtenerDescripcion() {
        return String.format("[Audiolibro] %s — %s", titulo, autor);
    }

    @Override
    public String getTipo() { return "audiolibro"; }
}
