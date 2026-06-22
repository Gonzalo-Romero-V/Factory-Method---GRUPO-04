package ec.espoch.biblioteca.domain;

import ec.espoch.biblioteca.servicios.ServicioAdicional;

import java.util.List;

public class DetallePrestamo {

    private int                   id;
    private RecursoBibliografico  recurso;
    private int                   dias;
    private double                costo;

    public DetallePrestamo(RecursoBibliografico recurso, int dias) {
        this.recurso = recurso;
        this.dias    = dias;
        this.costo   = recurso.calcularCosto();
    }

    public int                  getId()      { return id; }
    public RecursoBibliografico getRecurso() { return recurso; }
    public int                  getDias()    { return dias; }
    public double               getCosto()   { return costo; }
    public List<ServicioAdicional> getServicios() { return recurso.getServicios(); }

    public void setId(int id) { this.id = id; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("  %-40s %2d días  $%5.2f", recurso.obtenerDescripcion(), dias, costo));
        for (ServicioAdicional s : recurso.getServicios()) {
            sb.append(String.format("\n    + %-37s        $%5.2f", s.getDescripcion(), s.getCosto()));
        }
        return sb.toString();
    }
}
