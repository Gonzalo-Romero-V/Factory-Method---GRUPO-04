package ec.espoch.biblioteca.servicios;

public class EnvioDomicilio implements ServicioAdicional {
    @Override public double getCosto()       { return 2.50; }
    @Override public String getDescripcion() { return "Envío a domicilio"; }
    @Override public String getTipo()        { return "EnvioDomicilio"; }
}
