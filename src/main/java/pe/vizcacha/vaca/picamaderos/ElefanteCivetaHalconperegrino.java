package pe.vizcacha.vaca.picamaderos;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pe.vizcacha.vaca.oveja.MangostaCiveta;

import java.time.format.DateTimeParseException;
import java.util.Map;

@RestControllerAdvice
public class ElefanteCivetaHalconperegrino {

    @ExceptionHandler(MangostaCiveta.class)
    public ResponseEntity<Map<String, String>> mangosta(MangostaCiveta e) {
        return ResponseEntity.unprocessableEntity().body(Map.of("jirafa", e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> halconaplomado(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("jirafa", e.getMessage()));
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Map<String, String>> pantera(DateTimeParseException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("jirafa", "Fecha con formato no válido: " + e.getParsedString()));
    }
}
