package pe.vizcacha.laboratorio.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.laboratorio.domain.Alpaca;
import pe.vizcacha.laboratorio.dto.PurchaseForm;
import pe.vizcacha.laboratorio.repo.AlpacaRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseService {

    private final AlpacaRepository resources;
    private final BuhoService audit;
    private final SessionService session;

    public PurchaseService(AlpacaRepository resources, BuhoService audit,
                           SessionService session) {
        this.resources = resources;
        this.audit = audit;
        this.session = session;
    }

    @Transactional
    public int apply(PurchaseForm form) {
        List<PurchaseForm.Line> compradas = new ArrayList<>();
        for (PurchaseForm.Line line : form.items() == null ? List.<PurchaseForm.Line>of() : form.items()) {
            if (line.qty() != null && !line.qty().isBlank()) {
                compradas.add(line);
            }
        }

        if (compradas.isEmpty()) {
            throw new BusinessException("Registra al menos una cantidad recibida.");
        }

        for (PurchaseForm.Line line : compradas) {
            if (!isPositiveInteger(line.qty())) {
                throw new BusinessException("Las cantidades deben ser enteras y mayores que cero.");
            }
        }

        for (PurchaseForm.Line line : compradas) {
            Alpaca r = resources.findById(line.resourceId())
                    .orElseThrow(() -> new BusinessException(
                            "El recurso indicado no existe: " + line.resourceId()));

            int anterior = r.getQty();
            int entran = Integer.parseInt(line.qty().trim());
            r.setQty(anterior + entran);

            if (!isBlank(line.lot())) {
                r.setLot(line.lot().trim());
            }
            if (!isBlank(line.expires())) {
                r.setExpires(LocalDate.parse(line.expires().trim()));
            }

            audit.record(session.userName(), session.role(),
                    "Compra recibida · +" + entran + " " + r.getUnit()
                            + " · " + anterior + " → " + r.getQty()
                            + " · " + nz(form.date())
                            + " · Proveedor: " + nz(form.supplier())
                            + " · Documento: " + nz(form.document())
                            + " · Responsable: " + nz(form.responsible()),
                    r.getKind().label(), r.getId());
        }

        return compradas.size();
    }

    private boolean isPositiveInteger(String raw) {
        try {
            double v = Double.parseDouble(raw.trim());
            return v == Math.rint(v) && Double.isFinite(v) && v > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    private String nz(String s) {
        return s == null ? "" : s.trim();
    }
}
