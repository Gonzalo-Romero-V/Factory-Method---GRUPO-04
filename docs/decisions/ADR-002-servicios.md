# ADR-002: Implementación de servicios adicionales — Composición

**Estado:** Aceptado  
**Fecha:** 2026-06-22

## Contexto
Los recursos bibliográficos "pueden tener servicios adicionales" (EnvioDomicilio, DescargaOffline, etc.).
Opciones evaluadas:

### Opción A — Decorator Pattern
```java
RecursoBibliografico r = new EnvioDomicilio(new LibroFisico(...));
```
- Pro: Elegante, open/closed principle, cada capa modifica costo transparentemente
- Con: Introduce un segundo patrón de diseño (Decorator) encima de Factory Method, distrayendo del foco evaluado

### Opción B — Composición con interfaz (elegida)
```java
LibroFisico libro = (LibroFisico) factory.crearRecurso("libro_fisico");
libro.agregarServicio(new EnvioDomicilio());
// calcularCosto() = costoBase + sum(servicios)
```
- Pro: Simple, Factory Method queda como protagonista, servicios muestran uso de interfaces
- Con: Menos "puro" en términos de OCP estricto

## Decisión
**Composición (Opción B):** `RecursoBibliografico` tiene `List<ServicioAdicional>`.  
`ServicioAdicional` es una interfaz con `getCosto()` y `getDescripcion()`.  
`calcularCosto()` suma `costoBase + servicios.stream().mapToDouble(getCosto).sum()`.

## Razón principal
La evaluación puntúa Factory Method. Agregar Decorator complejiza sin beneficio evaluativo.
