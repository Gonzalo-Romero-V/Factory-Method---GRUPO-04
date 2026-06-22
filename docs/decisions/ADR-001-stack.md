# ADR-001: Stack tecnológico — Java 17 + Maven

**Estado:** Aceptado  
**Fecha:** 2026-06-22

## Contexto
Evaluación práctica sobre el Patrón Factory Method. Se evaluaron: Java, Python, Node.js + TypeScript, Laravel + Next.

## Decisión
**Java 17 (OpenJDK Temurin) + Maven 3.9 con Maven Wrapper.**

## Razones
- El enunciado usa sintaxis Java explícita (`RecursoBibliografico recurso = recursoFactory.crearRecurso("tesis")`)
- Java es el lenguaje canónico para patrones GoF en cursos de arquitectura
- Java 17 ya instalado en el entorno; Maven via Maven Wrapper (sin instalación manual)
- El evaluador espera tipado estático, interfaces y herencia explícita

## Reproducibilidad
```bash
git clone <repo>
cd Factory-Method---GRUPO-04/app
mvnw.cmd clean package   # Windows
java -jar target/biblioteca.jar
```
Solo requiere Java 17+ instalado. Maven Wrapper lo descarga automáticamente.

## Alternativas descartadas
- **Python**: más rápido de escribir, pero menos adecuado para el contexto académico
- **Node.js + TS**: complejidad de compilación sin beneficio real para consola
- **Laravel + Next**: dos stacks, overkill para un patrón de diseño
