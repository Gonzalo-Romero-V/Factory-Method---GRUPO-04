# Sistema de Préstamos — Biblioteca Digital
**Patrón de Diseño: Factory Method**

Arquitectura de Software · ESPOCH · Facultad de Informática y Electrónica · Ingeniería de Software

**Grupo 04:** Daniel Quizhpi · Joel Caiza · Erik Yumi · Gonzalo Romero · Mateo López

---

## Descripción

Sistema de consola en Java que gestiona el préstamo de recursos académicos (libros físicos, digitales, revistas, tesis y audiolibros) aplicando el Patrón Factory Method para la creación desacoplada de recursos.

---

## Requisitos

| Herramienta | Versión | Verificar |
|---|---|---|
| **Java JDK** | **17+** | `java -version` |
| Git | cualquier | `git --version` |

> Maven **no necesita instalarse**. El repositorio incluye Maven Wrapper que lo descarga automáticamente.

---

## Ejecutar el proyecto

```bash
# 1. Clonar
git clone https://github.com/Gonzalo-Romero-V/Factory-Method---GRUPO-04.git
cd Factory-Method---GRUPO-04/app

# 2. Compilar
mvnw.cmd clean package        # Windows
./mvnw clean package          # Linux / Mac

# 3a. Modo consola (demo del patrón)
java -Dfile.encoding=UTF-8 -jar target/biblioteca.jar

# 3b. Modo web (interfaz gráfica)
java -Dfile.encoding=UTF-8 -jar target/biblioteca.jar --web
# → Abrir http://localhost:7000
```

> **Windows:** la flag `-Dfile.encoding=UTF-8` es necesaria para que tildes y ñ
> se muestren correctamente en la consola. Sin ella el JAR funciona igual,
> pero la salida de texto puede verse con caracteres extraños (`?` o `Ã©`).

## Re-renderizar diagramas PlantUML

```bash
# Descargar plantuml.jar (una sola vez) desde https://plantuml.com/download
# Colocar en: vault/diagrams/plantuml.jar  (ignorado por git)

java -Dfile.encoding=UTF-8 -jar vault/diagrams/plantuml.jar vault/diagrams/c4-context.puml
java -Dfile.encoding=UTF-8 -jar vault/diagrams/plantuml.jar vault/diagrams/c4-containers.puml
java -Dfile.encoding=UTF-8 -jar vault/diagrams/plantuml.jar vault/diagrams/c4-components-prestamos.puml

# Copiar los PNG generados a docs/diagrams/
```

---

## Estructura del repositorio

```
Factory-Method---GRUPO-04/
│
├── README.md                        ← este archivo
│
├── app/                             ← código fuente Java
│   ├── mvnw / mvnw.cmd              ← Maven Wrapper (no requiere Maven instalado)
│   ├── pom.xml
│   └── src/main/java/ec/espoch/biblioteca/
│       ├── Main.java                ← punto de entrada, flujo completo
│       ├── domain/                  ← entidades del dominio
│       │   ├── Usuario.java
│       │   ├── RecursoBibliografico.java   (abstract)
│       │   ├── Prestamo.java
│       │   ├── DetallePrestamo.java
│       │   └── Notificacion.java
│       ├── recursos/                ← productos concretos del Factory Method
│       │   ├── LibroFisico.java
│       │   ├── LibroDigital.java
│       │   ├── Revista.java
│       │   ├── Tesis.java
│       │   └── Audiolibro.java
│       ├── factory/                 ← implementación del patrón
│       │   ├── RecursoFactory.java  (Creator abstract)
│       │   └── BibliotecaFactory.java (ConcreteCreator)
│       └── servicios/               ← servicios adicionales por composición
│           ├── ServicioAdicional.java  (interface)
│           ├── EnvioDomicilio.java
│           ├── DescargaOffline.java
│           ├── PrestamoExtendido.java
│           ├── CertificadoLectura.java
│           └── AccesoPremium.java
│
└── docs/                            ← entregables de la práctica
    └── diagrams/                    ← diagramas C4 (PlantUML)
        ├── c4-context.puml
        ├── c4-containers.puml
        └── c4-components.puml
```

> Las carpetas `context/` y `vault/` son **locales** y están excluidas del repositorio.

---

## Diagramas C4 (PlantUML)

Los diagramas están en `docs/diagrams/`. Para renderizarlos:

```bash
# Descargar PlantUML (una sola vez)
# https://plantuml.com/download

java -jar plantuml.jar docs/diagrams/c4-context.puml
java -jar plantuml.jar docs/diagrams/c4-containers.puml
java -jar plantuml.jar docs/diagrams/c4-components.puml
```

---

## Tecnologías

- **Java 17** — lenguaje principal
- **Maven 3.9** — build tool (via Maven Wrapper, sin instalación manual)
- **PlantUML** — diagramas arquitectónicos C4
