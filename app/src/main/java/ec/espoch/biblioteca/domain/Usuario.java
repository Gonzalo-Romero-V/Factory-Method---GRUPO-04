package ec.espoch.biblioteca.domain;

public class Usuario {

    private int    id;
    private String nombre;
    private String codigo;
    private String email;

    public Usuario(String nombre, String codigo, String email) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.email  = email;
    }

    public int    getId()     { return id; }
    public String getNombre() { return nombre; }
    public String getCodigo() { return codigo; }
    public String getEmail()  { return email; }

    public void setId(int id) { this.id = id; }

    @Override
    public String toString() {
        return String.format("Usuario{id=%d, nombre='%s', codigo='%s'}", id, nombre, codigo);
    }
}
