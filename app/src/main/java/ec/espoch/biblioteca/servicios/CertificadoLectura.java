package ec.espoch.biblioteca.servicios;

public class CertificadoLectura implements ServicioAdicional {
    @Override public double getCosto()       { return 3.00; }
    @Override public String getDescripcion() { return "Certificado de lectura"; }
    @Override public String getTipo()        { return "CertificadoLectura"; }
}
