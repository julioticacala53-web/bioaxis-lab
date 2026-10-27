package pe.vizcacha.laboratorio.service;

import org.springframework.stereotype.Service;
import pe.vizcacha.laboratorio.config.DemoClock;
import pe.vizcacha.laboratorio.domain.Alpaca;
import pe.vizcacha.laboratorio.domain.enums.AlpacaKind;

/**
 * Estado derivado de un recurso (§6.2). No se almacena: se calcula.
 *
 * El orden de las comprobaciones es parte de la regla: vencido gana sobre
 * agotado, y agotado gana sobre stock bajo.
 */
@Service
public class AlpacaStatusService {

    public static final String DISPONIBLE = "Disponible";
    public static final String STOCK_BAJO = "Stock bajo";
    public static final String AGOTADO = "Agotado";
    public static final String VENCIDO = "Vencido";

    private final DemoClock clock;

    public AlpacaStatusService(DemoClock clock) {
        this.clock = clock;
    }

    public String derived(Alpaca r) {
        if (r.getKind() == AlpacaKind.EQUIPOS) {
            return r.getStatus() == null ? "" : r.getStatus().label();
        }
        if (r.getKind() == AlpacaKind.REACTIVOS
                && r.getExpires() != null
                && r.getExpires().isBefore(clock.today())) {
            return VENCIDO;
        }
        if (r.getQty() == 0) {
            return AGOTADO;
        }
        if (r.getQty() <= r.getMinQty()) {
            return STOCK_BAJO;
        }
        return DISPONIBLE;
    }
}
