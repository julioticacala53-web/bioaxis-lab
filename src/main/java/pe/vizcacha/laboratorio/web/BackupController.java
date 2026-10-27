package pe.vizcacha.laboratorio.web;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.vizcacha.laboratorio.service.BackupService;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/backup")
public class BackupController {

    private final BackupService respaldos;

    public BackupController(BackupService respaldos) {
        this.respaldos = respaldos;
    }

    @GetMapping("/patients.csv")
    public ResponseEntity<byte[]> pacientes() {
        byte[] contenido = respaldos.csv();
        String nombre = respaldos.fileName();
        respaldos.registrar(nombre);
        return ResponseEntity.ok()
                .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(nombre, StandardCharsets.UTF_8).build().toString())
                .body(contenido);
    }
}
