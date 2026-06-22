package ec.espoch.biblioteca;

import ec.espoch.biblioteca.db.DatabaseManager;
import ec.espoch.biblioteca.db.PrestamoRepository;
import ec.espoch.biblioteca.web.WebServer;
import ec.espoch.biblioteca.domain.*;
import ec.espoch.biblioteca.factory.BibliotecaFactory;
import ec.espoch.biblioteca.servicios.*;

import java.io.PrintStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) throws Exception {
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
        System.setErr(new PrintStream(System.err, true, "UTF-8"));
        Logger.getLogger("org.flywaydb").setLevel(Level.SEVERE);

        if (args.length > 0 && args[0].equalsIgnoreCase("--web")) {
            DatabaseManager.getInstance(); // inicializa DB y migraciones
            WebServer.start();
            return;
        }

        // ── Inicialización ────────────────────────────────────────────────────
        DatabaseManager db = DatabaseManager.getInstance();
        PrestamoRepository repo = new PrestamoRepository(db.getConnection());

        linea('═', 54);
        System.out.println("   SISTEMA DE BIBLIOTECA DIGITAL — ESPOCH G04");
        System.out.println("   Patrón de Diseño: Factory Method");
        linea('═', 54);

        // ── a) Crear usuario (cargado desde DB — seed data) ──────────────────
        System.out.println("\n► 1. USUARIO");
        Usuario usuario = repo.cargarUsuario(2);   // Carlos López (seed)
        System.out.println("   " + usuario);

        // ── b) Crear recursos via Factory Method ─────────────────────────────
        System.out.println("\n► 2. CREAR RECURSOS VÍA FACTORY METHOD");
        BibliotecaFactory factory = new BibliotecaFactory();

        RecursoBibliografico libro = factory.crearRecurso(
            "libro_fisico", "Clean Code", "Robert C. Martin", "LF-NEW-01");
        libro.agregarServicio(new EnvioDomicilio());
        System.out.println("   [FACTORY] crearRecurso(\"libro_fisico\") → " + libro.obtenerDescripcion());
        System.out.println("            + " + libro.getServicios().get(0).getDescripcion());

        RecursoBibliografico digital = factory.crearRecurso(
            "libro_digital", "Head First Design Patterns", "Freeman & Freeman", "LD-NEW-01");
        digital.agregarServicio(new DescargaOffline());
        System.out.println("   [FACTORY] crearRecurso(\"libro_digital\") → " + digital.obtenerDescripcion());
        System.out.println("            + " + digital.getServicios().get(0).getDescripcion());

        RecursoBibliografico tesis = factory.crearRecurso(
            "tesis", "Patrones GoF en Sistemas Distribuidos", "M. Romero", "TS-NEW-01");
        tesis.agregarServicio(new CertificadoLectura());
        System.out.println("   [FACTORY] crearRecurso(\"tesis\") → " + tesis.obtenerDescripcion());
        System.out.println("            + " + tesis.getServicios().get(0).getDescripcion());

        RecursoBibliografico revista = factory.crearRecurso(
            "revista", "IEEE Software Vol.42", "IEEE", "RV-NEW-01");
        revista.agregarServicio(new PrestamoExtendido(7));
        System.out.println("   [FACTORY] crearRecurso(\"revista\") → " + revista.obtenerDescripcion());
        System.out.println("            + " + revista.getServicios().get(0).getDescripcion());

        RecursoBibliografico audio = factory.crearRecurso(
            "audiolibro", "Clean Architecture", "Robert C. Martin", "AL-NEW-01");
        audio.agregarServicio(new AccesoPremium());
        System.out.println("   [FACTORY] crearRecurso(\"audiolibro\") → " + audio.obtenerDescripcion());
        System.out.println("            + " + audio.getServicios().get(0).getDescripcion());

        // ── c) Crear préstamo y agregar recursos ──────────────────────────────
        System.out.println("\n► 3. CREAR PRÉSTAMO Y AGREGAR RECURSOS");
        Prestamo prestamo = new Prestamo(usuario);
        prestamo.agregarRecurso(libro,   7);
        prestamo.agregarRecurso(digital, 7);
        prestamo.agregarRecurso(tesis,   14);
        prestamo.agregarRecurso(revista, 14);
        prestamo.agregarRecurso(audio,   7);
        System.out.println("   Recursos agregados: " + prestamo.getDetalles().size());

        // ── d) Calcular costo total ───────────────────────────────────────────
        System.out.println("\n► 4. DESGLOSE DE COSTOS");
        linea('-', 54);
        for (DetallePrestamo d : prestamo.getDetalles()) {
            System.out.println(d);
        }
        linea('-', 54);
        System.out.printf("   COSTO TOTAL: $%.2f%n", prestamo.calcularCostoTotal());
        linea('-', 54);

        // ── e) Guardar en BD y mostrar resumen ───────────────────────────────
        System.out.println("\n► 5. PERSISTENCIA");
        Notificacion notif = repo.save(prestamo);
        System.out.println("   [DB] Préstamo guardado con ID #" + prestamo.getId());
        System.out.println("   " + notif);

        // ── f) Historial completo desde BD ───────────────────────────────────
        System.out.println("\n► 6. HISTORIAL DE PRÉSTAMOS (desde DB)");
        linea('-', 54);
        List<String> historial = repo.obtenerHistorial();
        historial.forEach(System.out::println);
        linea('-', 54);
        System.out.println("   Total registros: " + historial.size());

        // ── g) Demo restricción de negocio ───────────────────────────────────
        System.out.println("\n► 7. DEMO RESTRICCIONES DE NEGOCIO");
        try {
            RecursoBibliografico rv = factory.crearRecurso("revista");
            rv.agregarServicio(new PrestamoExtendido(20));
        } catch (IllegalArgumentException e) {
            System.out.println("   [REGLA] " + e.getMessage());
        }
        try {
            factory.crearRecurso("enciclopedia");
        } catch (IllegalArgumentException e) {
            System.out.println("   [REGLA] " + e.getMessage());
        }
        try {
            new Prestamo(null);
        } catch (IllegalStateException e) {
            System.out.println("   [REGLA] " + e.getMessage());
        }

        System.out.println();
        linea('═', 54);
        System.out.println("   Ejecución completada exitosamente.");
        linea('═', 54);
    }

    private static void linea(char c, int n) {
        System.out.println(String.valueOf(c).repeat(n));
    }
}
