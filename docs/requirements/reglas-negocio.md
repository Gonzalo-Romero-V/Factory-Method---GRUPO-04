# Reglas de negocio

**Fuente:** Evaluación práctica ESPOCH — Arquitectura de Software, Grupo 04

## Entidades del dominio

| Entidad | Tipo | Descripción |
|---|---|---|
| `Usuario` | Clase concreta | Quien solicita el préstamo |
| `RecursoBibliografico` | Clase abstracta | Base de todos los recursos |
| `Prestamo` | Clase concreta | Agrupa varios recursos para un usuario |
| `DetallePrestamo` | Clase concreta | Línea individual: recurso + días + costo |
| `Notificacion` | Clase concreta | Mensaje generado al confirmar préstamo |

## Atributos obligatorios de RecursoBibliografico

- `titulo: String`
- `autor: String`
- `codigo: String`
- `costoBase: double`
- `calcularCosto(): double`
- `obtenerDescripcion(): String`

## Tipos de recursos (productos concretos del Factory Method)

| Tipo | Key en fábrica | Costo base |
|---|---|---|
| `LibroFisico` | `"libro_fisico"` | $3.00 |
| `LibroDigital` | `"libro_digital"` | $1.50 |
| `Revista` | `"revista"` | $1.00 |
| `Tesis` | `"tesis"` | $2.00 |
| `Audiolibro` | `"audiolibro"` | $2.50 |

## Servicios adicionales

| Servicio | Recurso permitido | Costo extra | Restricción |
|---|---|---|---|
| `EnvioDomicilio` | LibroFisico | $2.50 | — |
| `DescargaOffline` | LibroDigital | $1.00 | — |
| `PrestamoExtendido` | Revista | $0.50/día extra | Máximo 14 días total |
| `CertificadoLectura` | Tesis | $3.00 | — |
| `AccesoPremium` | Audiolibro | $2.00 | — |

## Restricción de días para Revista — Fundamentación

**Límite: 14 días (2 semanas)**

Referencia bibliográfica real:
- IFLA (International Federation of Library Associations): recomienda 7-14 días para publicaciones periódicas
- Bibliotecas universitarias de Ecuador (ESPOCH, UCE, PUCE): 7-14 días para revistas físicas
- Sistemas digitales como JSTOR, Elsevier: 1-14 días para acceso temporal
- Promedio internacional para revistas académicas: **14 días**

## Restricciones de flujo

1. No se puede crear `Prestamo` sin un `Usuario` asociado → lanza `IllegalStateException`
2. La fábrica lanza `IllegalArgumentException` si el tipo de recurso no existe
3. `Revista` rechaza `PrestamoExtendido` si `diasPrestamo > 14` → lanza `IllegalArgumentException`
4. Cada tipo de recurso solo acepta su servicio específico → validación en `agregarServicio()`

## Flujo funcional requerido (punto d del enunciado)

```
1. Crear Usuario
2. Crear ≥3 recursos via BibliotecaFactory.crearRecurso(tipo)
3. Crear Prestamo(usuario)
4. Agregar recursos al Prestamo → genera DetallePrestamo por cada uno
5. Calcular costo total (suma de DetallePrestamo)
6. Mostrar resumen + Notificacion
```
