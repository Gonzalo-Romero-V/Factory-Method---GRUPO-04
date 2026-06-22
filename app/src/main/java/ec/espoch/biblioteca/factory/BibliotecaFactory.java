package ec.espoch.biblioteca.factory;

import ec.espoch.biblioteca.domain.RecursoBibliografico;
import ec.espoch.biblioteca.recursos.*;

import java.util.concurrent.atomic.AtomicInteger;

/** ConcreteCreator: decide qué subclase de RecursoBibliografico instanciar. */
public class BibliotecaFactory extends RecursoFactory {

    private static final AtomicInteger counter = new AtomicInteger(1);

    /**
     * Crea un recurso por tipo con valores por defecto.
     * Tipos válidos: libro_fisico, libro_digital, revista, tesis, audiolibro.
     *
     * @throws IllegalArgumentException si el tipo no existe en el catálogo
     */
    @Override
    public RecursoBibliografico crearRecurso(String tipo) {
        if (tipo == null) throw new IllegalArgumentException("El tipo de recurso no puede ser nulo.");
        String n = String.format("%03d", counter.getAndIncrement());
        return switch (tipo.trim().toLowerCase()) {
            case "libro_fisico"  -> new LibroFisico ("Libro Físico",  "Autor",  "LF-" + n, 3.00);
            case "libro_digital" -> new LibroDigital("Libro Digital", "Autor",  "LD-" + n, 1.50);
            case "revista"       -> new Revista     ("Revista",       "Editor", "RV-" + n, 1.00);
            case "tesis"         -> new Tesis       ("Tesis",         "Autor",  "TS-" + n, 2.00);
            case "audiolibro"    -> new Audiolibro  ("Audiolibro",    "Autor",  "AL-" + n, 2.50);
            default -> throw new IllegalArgumentException(
                "Tipo de recurso no existe: '" + tipo + "'. " +
                "Tipos válidos: libro_fisico, libro_digital, revista, tesis, audiolibro.");
        };
    }

    /**
     * Crea un recurso con datos específicos — sobrecarga conveniente para el flujo principal.
     */
    public RecursoBibliografico crearRecurso(String tipo, String titulo, String autor, String codigo) {
        return switch (tipo.trim().toLowerCase()) {
            case "libro_fisico"  -> new LibroFisico (titulo, autor, codigo, 3.00);
            case "libro_digital" -> new LibroDigital(titulo, autor, codigo, 1.50);
            case "revista"       -> new Revista     (titulo, autor, codigo, 1.00);
            case "tesis"         -> new Tesis       (titulo, autor, codigo, 2.00);
            case "audiolibro"    -> new Audiolibro  (titulo, autor, codigo, 2.50);
            default -> throw new IllegalArgumentException(
                "Tipo de recurso no existe: '" + tipo + "'.");
        };
    }
}
