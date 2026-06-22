package ec.espoch.biblioteca.factory;

import ec.espoch.biblioteca.domain.RecursoBibliografico;

/** Creator abstracto del patrón Factory Method. */
public abstract class RecursoFactory {

    /** Factory Method — las subclases deciden qué clase concreta instanciar. */
    public abstract RecursoBibliografico crearRecurso(String tipo);
}
