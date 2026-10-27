package pe.vizcacha.laboratorio.domain.enums;

/**
 * Enum cuyo valor almacenado y mostrado es una etiqueta literal en espanol,
 * con tildes y espacios ("EN ANALISIS", "Medico / profesional responsable").
 * Esas etiquetas son parte del contrato: se guardan tal cual en SQLite y
 * viajan tal cual en el JSON del snapshot.
 */
public interface LabeledEnum {
    String label();
}
