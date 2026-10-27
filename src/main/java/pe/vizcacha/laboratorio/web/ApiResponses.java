package pe.vizcacha.laboratorio.web;

import org.springframework.http.ResponseEntity;
import pe.vizcacha.laboratorio.dto.DatabaseDto;

/**
 * Respuesta de una mutacion: el snapshot completo en el cuerpo (§5.3) y el
 * identificador del registro afectado en una cabecera.
 *
 * La cabecera cubre un hueco del contrato. El §5.3 dice que las mutaciones
 * devuelven solo el snapshot, pero el frontend necesita el identificador
 * recien generado para tres cosas que §6.4 exige: el toast
 * "Paciente registrado correctamente · PAC-000006", abrir automaticamente el
 * modal de trazabilidad de la muestra creada, y el boton
 * "Guardar y crear solicitud". Deducirlo buscando el id mas alto seria
 * fragil. Va en una cabecera y no en el cuerpo para no alterar la forma del
 * snapshot documentada en §5.2.
 */
final class ApiResponses {

    static final String HEADER_RECORD_ID = "X-Record-Id";

    private ApiResponses() {
    }

    static ResponseEntity<DatabaseDto> withRecord(DatabaseDto snapshot, String recordId) {
        return ResponseEntity.ok().header(HEADER_RECORD_ID, recordId).body(snapshot);
    }
}
