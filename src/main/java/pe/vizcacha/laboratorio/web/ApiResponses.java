package pe.vizcacha.laboratorio.web;

import org.springframework.http.ResponseEntity;
import pe.vizcacha.laboratorio.dto.DatabaseDto;

final class ApiResponses {

    static final String HEADER_RECORD_ID = "X-Record-Id";

    private ApiResponses() {
    }

    static ResponseEntity<DatabaseDto> withRecord(DatabaseDto snapshot, String recordId) {
        return ResponseEntity.ok().header(HEADER_RECORD_ID, recordId).body(snapshot);
    }
}
