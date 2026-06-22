package ec.espoch.biblioteca.servicios;

public class DescargaOffline implements ServicioAdicional {
    @Override public double getCosto()       { return 1.00; }
    @Override public String getDescripcion() { return "Descarga offline"; }
    @Override public String getTipo()        { return "DescargaOffline"; }
}
