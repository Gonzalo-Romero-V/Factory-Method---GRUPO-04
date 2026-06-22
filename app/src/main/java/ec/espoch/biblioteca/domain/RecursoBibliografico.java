package ec.espoch.biblioteca.domain;

import ec.espoch.biblioteca.servicios.ServicioAdicional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class RecursoBibliografico {

    protected String titulo;
    protected String autor;
    protected String codigo;
    protected double costoBase;
    protected final List<ServicioAdicional> servicios = new ArrayList<>();

    protected RecursoBibliografico(String titulo, String autor, String codigo, double costoBase) {
        this.titulo    = titulo;
        this.autor     = autor;
        this.codigo    = codigo;
        this.costoBase = costoBase;
    }

    /** Cada subclase valida qué servicio acepta antes de llamar a super. */
    public void agregarServicio(ServicioAdicional servicio) {
        servicios.add(servicio);
    }

    public double calcularCosto() {
        return costoBase + servicios.stream().mapToDouble(ServicioAdicional::getCosto).sum();
    }

    public abstract String obtenerDescripcion();

    public String getTitulo()    { return titulo; }
    public String getAutor()     { return autor; }
    public String getCodigo()    { return codigo; }
    public double getCostoBase() { return costoBase; }
    public String getTipo()      { return getClass().getSimpleName().toLowerCase(); }
    public List<ServicioAdicional> getServicios() { return Collections.unmodifiableList(servicios); }
}
