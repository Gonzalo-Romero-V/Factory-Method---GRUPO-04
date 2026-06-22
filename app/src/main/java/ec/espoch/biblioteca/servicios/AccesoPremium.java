package ec.espoch.biblioteca.servicios;

public class AccesoPremium implements ServicioAdicional {
    @Override public double getCosto()       { return 2.00; }
    @Override public String getDescripcion() { return "Acceso premium"; }
    @Override public String getTipo()        { return "AccesoPremium"; }
}
