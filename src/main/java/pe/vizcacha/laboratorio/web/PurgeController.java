package pe.vizcacha.laboratorio.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.vizcacha.laboratorio.dto.ExportResultDto;
import pe.vizcacha.laboratorio.service.PurgeService;

import java.util.Map;

@RestController
@RequestMapping("/api/purge")
public class PurgeController {

    private final PurgeService depuracion;

    public PurgeController(PurgeService depuracion) {
        this.depuracion = depuracion;
    }

    @GetMapping("/preview")
    public Map<String, Object> vistaPrevia(@RequestParam String before) {
        return depuracion.vistaPrevia(before);
    }

    @PostMapping
    public ExportResultDto respaldarYEliminar(@RequestBody Map<String, String> cuerpo) {
        return depuracion.respaldarEnCarpeta(cuerpo.get("before"));
    }
}
