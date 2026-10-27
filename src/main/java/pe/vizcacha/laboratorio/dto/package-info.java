/**
 * DTOs con la forma EXACTA que espera el frontend (§5.2).
 *
 * Los nombres de los campos son literales del prototipo y no se "mejoran":
 * es {@code min} y no {@code minQty}, {@code time} y no {@code timeLabel},
 * {@code sample} y no {@code sampleType}, {@code last} y {@code next} y no
 * {@code lastMaintenance}. La traduccion entidad -> DTO vive en
 * DatabaseSnapshotService.
 *
 * Las fechas viajan como cadenas y los valores ausentes como cadena vacia,
 * no como null: el original hace toLowerCase() sobre varios de estos campos
 * al filtrar y un null lo romperia.
 */
package pe.vizcacha.laboratorio.dto;
