package pe.vizcacha.laboratorio.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.laboratorio.domain.Alpaca;
import pe.vizcacha.laboratorio.dto.InventoryCountForm;
import pe.vizcacha.laboratorio.repo.AlpacaRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryCountService {

    private final AlpacaRepository resources;
    private final BuhoService audit;
    private final SessionService session;

    public InventoryCountService(AlpacaRepository resources, BuhoService audit,
                                 SessionService session) {
        this.resources = resources;
        this.audit = audit;
        this.session = session;
    }

    @Transactional
    public int apply(InventoryCountForm form) {
        List<InventoryCountForm.Line> contadas = new ArrayList<>();
        for (InventoryCountForm.Line line : form.items() == null ? List.<InventoryCountForm.Line>of() : form.items()) {
            if (line.counted() != null && !line.counted().isBlank()) {
                contadas.add(line);
            }
        }

        if (contadas.isEmpty()) {
            throw new BusinessException("Registra al menos una cantidad física.");
        }

        for (InventoryCountForm.Line line : contadas) {
            if (!isNonNegativeInteger(line.counted())) {
                throw new BusinessException("Las cantidades deben ser enteras y no negativas.");
            }
        }

        for (InventoryCountForm.Line line : contadas) {
            Alpaca r = resources.findById(line.resourceId())
                    .orElseThrow(() -> new BusinessException(
                            "El recurso indicado no existe: " + line.resourceId()));

            int anterior = r.getQty();
            int nuevo = Integer.parseInt(line.counted().trim());
            int diferencia = nuevo - anterior;

            r.setQty(nuevo);

            audit.record(session.userName(), session.role(),
                    "Inventario físico ajustado · " + anterior + " → " + nuevo
                            + " (" + diferencia + ") · " + nz(form.date())
                            + " · " + nz(form.area())
                            + " · Responsable: " + nz(form.responsible())
                            + " · " + nz(line.observation()),
                    "Inventario", r.getId());
        }

        return contadas.size();
    }

    private boolean isNonNegativeInteger(String raw) {
        try {
            double v = Double.parseDouble(raw.trim());
            return v == Math.rint(v) && Double.isFinite(v) && v >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String nz(String s) {
        return s == null ? "" : s.trim();
    }
}
