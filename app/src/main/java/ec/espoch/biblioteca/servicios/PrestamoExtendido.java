package ec.espoch.biblioteca.servicios;

public class PrestamoExtendido implements ServicioAdicional {

    private final int diasExtension;

    public PrestamoExtendido(int diasExtension) {
        this.diasExtension = diasExtension;
    }

    public int getDiasExtension() { return diasExtension; }

    @Override public double getCosto()       { return diasExtension * 0.50; }
    @Override public String getDescripcion() { return "Préstamo extendido +" + diasExtension + " días"; }
    @Override public String getTipo()        { return "PrestamoExtendido"; }
}
