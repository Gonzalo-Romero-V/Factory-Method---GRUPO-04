# Sistema de Préstamos - Biblioteca Digital
### Patrón de Diseño: Factory Method
**Arquitectura de Software — ESPOCH, Facultad de Informática y Electrónica**

**Grupo 04:** Daniel Quizhpi · Joel Caiza · Erik Yumi · Gonzalo Romero · Mateo López

---

## Requisitos del sistema

| Herramienta | Versión mínima | Verificar con |
|---|---|---|
| **Java JDK** | 17+ | `java -version` |
| Git | 2.x | `git --version` |

> **Maven NO necesita estar instalado.** El proyecto incluye Maven Wrapper (`mvnw`) que lo descarga automáticamente.

---

## Reproducir el proyecto (3 pasos)

```bash
# 1. Clonar
git clone https://github.com/Gonzalo-Romero-V/Factory-Method---GRUPO-04.git
cd Factory-Method---GRUPO-04/app

# 2. Compilar (Windows)
mvnw.cmd clean package

# 2. Compilar (Linux / Mac)
./mvnw clean package

# 3. Ejecutar
java -jar target/biblioteca.jar
```

---

## Estructura del proyecto

```
Factory-Method---GRUPO-04/
├── app/                          # Código fuente Java (Maven)
│   ├── mvnw / mvnw.cmd           # Maven Wrapper (no requiere Maven instalado)
│   ├── pom.xml                   # Configuración del proyecto
│   └── src/main/java/ec/espoch/biblioteca/
│       ├── Main.java             # Punto de entrada — flujo funcional completo
│       ├── domain/               # Entidades del dominio
│       │   ├── Usuario.java
│       │   ├── RecursoBibliografico.java   (abstract)
│       │   ├── Prestamo.java
│       │   ├── DetallePrestamo.java
│       │   └── Notificacion.java
│       ├── recursos/             # Productos concretos del Factory Method
│       │   ├── LibroFisico.java
│       │   ├── LibroDigital.java
│       │   ├── Revista.java
│       │   ├── Tesis.java
│       │   └── Audiolibro.java
│       ├── factory/              # Patrón Factory Method
│       │   ├── RecursoFactory.java         (Creator — abstract)
│       │   └── BibliotecaFactory.java      (ConcreteCreator)
│       └── servicios/            # Servicios adicionales por composición
│           ├── ServicioAdicional.java      (interface)
│           ├── EnvioDomicilio.java
│           ├── DescargaOffline.java
│           ├── PrestamoExtendido.java
│           ├── CertificadoLectura.java
│           └── AccesoPremium.java
└── docs/                         # Vault de documentación (PlantUML + decisiones)
    ├── diagrams/                 # Diagramas C4 en PlantUML (.puml)
    ├── decisions/                # Architecture Decision Records (ADR)
    └── requirements/             # Requisitos y reglas de negocio
```

---

## Arquitectura — Patrón Factory Method

```
«abstract»                      «interface»
RecursoFactory                  RecursoBibliografico
+ crearRecurso(tipo): RB  ────► + titulo, autor, codigo, costoBase
        ▲                       + calcularCosto(): double
        │                       + obtenerDescripcion(): String
BibliotecaFactory                       ▲
+ crearRecurso(tipo): RB        ┌───────┼───────┬──────┬───────┐
  → switch(tipo)          LibroFisico  LibroDigital  Revista  Tesis  Audiolibro
  → lanza excepción       + servicios: List<ServicioAdicional>
    si tipo inválido
```

---

## Reglas de negocio

| Recurso | Servicio permitido | Restricción |
|---|---|---|
| `LibroFisico` | `EnvioDomicilio` | — |
| `LibroDigital` | `DescargaOffline` | — |
| `Revista` | `PrestamoExtendido` | Máximo **14 días** (estándar bibliográfico universitario) |
| `Tesis` | `CertificadoLectura` | — |
| `Audiolibro` | `AccesoPremium` | — |
| Cualquier recurso | — | No se puede prestar sin usuario asociado |

---

## Costos base por tipo de recurso

| Recurso | Costo base (USD) | Servicio adicional | Costo extra (USD) |
|---|---|---|---|
| Libro Físico | 3.00 | Envío a domicilio | 2.50 |
| Libro Digital | 1.50 | Descarga offline | 1.00 |
| Revista | 1.00 | Préstamo extendido | 0.50/día extra |
| Tesis | 2.00 | Certificado de lectura | 3.00 |
| Audiolibro | 2.50 | Acceso premium | 2.00 |

---

## Diagramas C4

Los diagramas están en `docs/diagrams/` en formato PlantUML (`.puml`).

Para generarlos como imagen:
```bash
# Requiere Java y PlantUML jar descargado
java -jar plantuml.jar docs/diagrams/*.puml
```

---

## Tecnologías

- **Java 17** (OpenJDK Temurin)
- **Maven 3.9+** (via Maven Wrapper — sin instalación manual)
- **PlantUML** — diagramas C4 arquitectónicos
