package pe.vizcacha.laboratorio.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.vizcacha.laboratorio.dto.DatabaseDto;
import pe.vizcacha.laboratorio.dto.InventoryCountForm;
import pe.vizcacha.laboratorio.dto.PurchaseForm;
import pe.vizcacha.laboratorio.service.DatabaseSnapshotService;
import pe.vizcacha.laboratorio.service.InventoryCountService;
import pe.vizcacha.laboratorio.service.PurchaseService;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryCountService counts;
    private final PurchaseService purchases;
    private final DatabaseSnapshotService snapshots;

    public InventoryController(InventoryCountService counts,
                               PurchaseService purchases,
                               DatabaseSnapshotService snapshots) {
        this.counts = counts;
        this.purchases = purchases;
        this.snapshots = snapshots;
    }

    @PostMapping("/count")
    public ResponseEntity<DatabaseDto> count(@RequestBody InventoryCountForm form) {
        int ajustados = counts.apply(form);
        return ApiResponses.withRecord(snapshots.snapshot(), String.valueOf(ajustados));
    }

    @PostMapping("/purchase")
    public ResponseEntity<DatabaseDto> purchase(@RequestBody PurchaseForm form) {
        int recibidas = purchases.apply(form);
        return ApiResponses.withRecord(snapshots.snapshot(), String.valueOf(recibidas));
    }
}
