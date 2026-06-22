package ec.espoch.biblioteca.recursos;

import ec.espoch.biblioteca.domain.RecursoBibliografico;
import ec.espoch.biblioteca.servicios.PrestamoExtendido;
import ec.espoch.biblioteca.servicios.ServicioAdicional;

public class Revista extends RecursoBibliografico {

    private static final int MAX_DIAS_EXTENSION = 14;

    public Revista(String titulo, String autor, String codigo, double costoBase) {
        super(titulo, autor, codigo, costoBase);
    }

    @Override
    public void agregarServicio(ServicioAdicional servicio) {
        if (!(servicio instanceof PrestamoExtendido ext)) {
            throw new IllegalArgumentException(
                "Revista solo acepta el servicio PrestamoExtendido. Recibido: " + servicio.getTipo());
        }
        if (ext.getDiasExtension() > MAX_DIAS_EXTENSION) {
            throw new IllegalArgumentException(String.format(
                "Revista: PrestamoExtendido no puede superar %d días. Solicitado: %d días.",
                MAX_DIAS_EXTENSION, ext.getDiasExtension()));
        }
        super.agregarServicio(servicio);
    }

    @Override
    public String obtenerDescripcion() {
        return String.format("[Revista] %s — %s", titulo, autor);
    }

    @Override
    public String getTipo() { return "revista"; }
}
