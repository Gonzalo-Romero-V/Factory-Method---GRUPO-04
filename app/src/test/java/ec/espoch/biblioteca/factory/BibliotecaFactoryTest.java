package ec.espoch.biblioteca.factory;

import ec.espoch.biblioteca.domain.RecursoBibliografico;
import ec.espoch.biblioteca.recursos.*;
import ec.espoch.biblioteca.servicios.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BibliotecaFactory — Factory Method")
class BibliotecaFactoryTest {

    private BibliotecaFactory factory;

    @BeforeEach
    void setUp() {
        factory = new BibliotecaFactory();
    }

    @Test @DisplayName("Crea LibroFisico correctamente")
    void creaLibroFisico() {
        RecursoBibliografico r = factory.crearRecurso("libro_fisico");
        assertInstanceOf(LibroFisico.class, r);
        assertEquals(3.00, r.getCostoBase());
    }

    @Test @DisplayName("Crea LibroDigital correctamente")
    void creaLibroDigital() {
        RecursoBibliografico r = factory.crearRecurso("libro_digital");
        assertInstanceOf(LibroDigital.class, r);
        assertEquals(1.50, r.getCostoBase());
    }

    @Test @DisplayName("Crea Revista correctamente")
    void creaRevista() {
        RecursoBibliografico r = factory.crearRecurso("revista");
        assertInstanceOf(Revista.class, r);
        assertEquals(1.00, r.getCostoBase());
    }

    @Test @DisplayName("Crea Tesis correctamente")
    void creaTesis() {
        RecursoBibliografico r = factory.crearRecurso("tesis");
        assertInstanceOf(Tesis.class, r);
        assertEquals(2.00, r.getCostoBase());
    }

    @Test @DisplayName("Crea Audiolibro correctamente")
    void creaAudiolibro() {
        RecursoBibliografico r = factory.crearRecurso("audiolibro");
        assertInstanceOf(Audiolibro.class, r);
        assertEquals(2.50, r.getCostoBase());
    }

    @Test @DisplayName("Tipo inválido lanza IllegalArgumentException")
    void tipoInvalidoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> factory.crearRecurso("enciclopedia"));
    }

    @Test @DisplayName("Tipo nulo lanza IllegalArgumentException")
    void tipoNuloLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> factory.crearRecurso(null));
    }

    @Test @DisplayName("LibroFisico acepta EnvioDomicilio y acumula costo")
    void libroFisicoAceptaEnvioDomicilio() {
        RecursoBibliografico r = factory.crearRecurso("libro_fisico");
        r.agregarServicio(new EnvioDomicilio());
        assertEquals(3.00 + 2.50, r.calcularCosto(), 0.001);
    }

    @Test @DisplayName("LibroFisico rechaza servicio incorrecto")
    void libroFisicoRechazaServicioIncorrecto() {
        RecursoBibliografico r = factory.crearRecurso("libro_fisico");
        assertThrows(IllegalArgumentException.class, () -> r.agregarServicio(new AccesoPremium()));
    }

    @Test @DisplayName("Revista rechaza extensión mayor a 14 días")
    void revistaRechazaExtensionExcesiva() {
        RecursoBibliografico r = factory.crearRecurso("revista");
        assertThrows(IllegalArgumentException.class, () -> r.agregarServicio(new PrestamoExtendido(15)));
    }

    @Test @DisplayName("Revista acepta extensión de exactamente 14 días")
    void revistaAceptaExtension14Dias() {
        RecursoBibliografico r = factory.crearRecurso("revista");
        assertDoesNotThrow(() -> r.agregarServicio(new PrestamoExtendido(14)));
        assertEquals(1.00 + 14 * 0.50, r.calcularCosto(), 0.001);
    }

    @Test @DisplayName("Préstamo sin usuario lanza IllegalStateException")
    void prestamoSinUsuarioLanzaExcepcion() {
        assertThrows(IllegalStateException.class,
            () -> new ec.espoch.biblioteca.domain.Prestamo(null));
    }
}
