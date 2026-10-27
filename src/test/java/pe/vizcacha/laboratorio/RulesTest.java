package pe.vizcacha.laboratorio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.laboratorio.domain.Llama;
import pe.vizcacha.laboratorio.domain.Alpaca;
import pe.vizcacha.laboratorio.domain.enums.EquipmentStatus;
import pe.vizcacha.laboratorio.domain.enums.Role;
import pe.vizcacha.laboratorio.dto.AlertDto;
import pe.vizcacha.laboratorio.repo.LlamaRepository;
import pe.vizcacha.laboratorio.repo.AlpacaRepository;
import pe.vizcacha.laboratorio.service.AlertService;
import pe.vizcacha.laboratorio.service.AvailabilityService;
import pe.vizcacha.laboratorio.service.SessionService;
import pe.vizcacha.laboratorio.service.AlpacaStatusService;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Entregable de la Fase 3: la lista de comprobaciones que exige §13,
 * verificada una por una.
 *
 * Los metodos que tocan datos van en transaccion y revierten al terminar,
 * asi que la semilla queda intacta para el resto de los tests.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
class RulesTest {

    @Autowired private AvailabilityService availability;
    @Autowired private AlpacaStatusService statuses;
    @Autowired private AlertService alerts;
    @Autowired private SessionService session;
    @Autowired private LlamaRepository exams;
    @Autowired private AlpacaRepository resources;

    private Llama exam(String id) {
        return exams.findById(id).orElseThrow();
    }

    private Alpaca resource(String id) {
        return resources.findById(id).orElseThrow();
    }

    // --------------------------------------------- disponibilidad con la semilla

    @Test
    void cuatroExamenesDeLosOchoEstanDisponibles() {
        assertThat(availability.unavailableExams()).hasSize(4);

        for (String id : List.of("EX-001", "EX-002", "EX-003", "EX-004")) {
            assertThat(availability.reasons(exam(id)))
                    .as("%s deberia estar disponible", id)
                    .isEmpty();
        }
    }

    @Test
    void elEquipoEnMantenimientoBloqueaTshYT4() {
        String motivo = "Analizador de inmunología: equipo en mantenimiento";

        assertThat(availability.reasons(exam("EX-005"))).containsExactly(motivo);
        assertThat(availability.reasons(exam("EX-006"))).containsExactly(motivo);
    }

    @Test
    void elMaterialEnCeroBloqueaLosExamenesDeMicrobiologia() {
        String motivo = "Hisopos estériles: material insuficiente";

        assertThat(availability.reasons(exam("EX-007"))).containsExactly(motivo);
        assertThat(availability.reasons(exam("EX-008"))).containsExactly(motivo);
    }

    /**
     * El error mas facil de cometer: confundir el umbral de alerta con el de
     * bloqueo. 12 unidades con minimo 20 es stock bajo, y aun asi el examen
     * se procesa.
     */
    @Test
    void elStockBajoAvisaPeroNoBloquea() {
        Alpaca glucosa = resource("REA-002");
        assertThat(glucosa.getQty()).isEqualTo(12);
        assertThat(glucosa.getMinQty()).isEqualTo(20);

        assertThat(statuses.derived(glucosa)).isEqualTo(AlpacaStatusService.STOCK_BAJO);
        assertThat(availability.reasons(exam("EX-002"))).isEmpty();
    }

    @Test
    void elReactivoEnCeroSiBloqueaSuExamen() {
        resource("REA-002").setQty(0);

        assertThat(availability.reasons(exam("EX-002")))
                .containsExactly("Reactivo de glucosa: reactivo sin stock");
    }

    @Test
    void elReactivoVencidoSiBloqueaSuExamen() {
        resource("REA-001").setExpires(LocalDate.parse("2026-09-01"));

        assertThat(availability.reasons(exam("EX-001")))
                .containsExactly("Reactivo hematológico: producto vencido");
    }

    /** Vencer hoy no cuenta como vencido: la comparacion es estrictamente
     *  anterior a la fecha congelada. */
    @Test
    void unReactivoQueVenceHoyTodaviaSirve() {
        resource("REA-001").setExpires(LocalDate.parse("2026-09-24"));

        assertThat(availability.reasons(exam("EX-001"))).isEmpty();
    }

    @Test
    void elExamenDeshabilitadoSeBloqueaPorSiMismo() {
        exam("EX-001").setEnabled(false);

        assertThat(availability.reasons(exam("EX-001"))).containsExactly("Examen deshabilitado");
    }

    @Test
    void unEquipoFueraDeServicioDaUnMotivoDistintoAlDeMantenimiento() {
        resource("EQ-001").setStatus(EquipmentStatus.FUERA_DE_SERVICIO);

        assertThat(availability.reasons(exam("EX-001")))
                .containsExactly("Analizador hematológico: equipo no operativo");
    }

    // ------------------------------------------------------- estado derivado

    @Test
    void elOrdenDelEstadoDerivadoEsVencidoAgotadoStockBajo() {
        Alpaca r = resource("REA-003");

        // Vencido gana sobre agotado.
        r.setQty(0);
        r.setExpires(LocalDate.parse("2026-09-01"));
        assertThat(statuses.derived(r)).isEqualTo(AlpacaStatusService.VENCIDO);

        // Agotado gana sobre stock bajo.
        r.setExpires(LocalDate.parse("2027-04-30"));
        assertThat(statuses.derived(r)).isEqualTo(AlpacaStatusService.AGOTADO);

        // Stock bajo gana sobre disponible.
        r.setQty(10);
        r.setMinQty(15);
        assertThat(statuses.derived(r)).isEqualTo(AlpacaStatusService.STOCK_BAJO);

        r.setQty(100);
        assertThat(statuses.derived(r)).isEqualTo(AlpacaStatusService.DISPONIBLE);
    }

    /** Un equipo devuelve su estado almacenado, no uno calculado por stock. */
    @Test
    void elEstadoDeUnEquipoEsElQueTieneGuardado() {
        assertThat(statuses.derived(resource("EQ-001"))).isEqualTo("OPERATIVO");
        assertThat(statuses.derived(resource("EQ-003"))).isEqualTo("EN MANTENIMIENTO");
    }

    /** Un material nunca se evalua por vencimiento, aunque la semilla le
     *  ponga fecha por el default de la factoria. */
    @Test
    void losMaterialesNoVencen() {
        Alpaca material = resource("MAT-005");
        material.setExpires(LocalDate.parse("2020-01-01"));

        assertThat(statuses.derived(material)).isEqualTo(AlpacaStatusService.DISPONIBLE);
    }

    // -------------------------------------------------------------- alertas

    @Test
    void laSemillaProduceOchoAlertas() {
        assertThat(alerts.alerts()).hasSize(8);
    }

    @Test
    void elReactivoDeGlucosaGeneraDosAlertas() {
        List<AlertDto> deGlucosa = alerts.alerts().stream()
                .filter(a -> a.title().equals("Reactivo de glucosa"))
                .toList();

        assertThat(deGlucosa).hasSize(2);
        assertThat(deGlucosa.get(0).detail()).isEqualTo("Stock bajo · 12 pruebas disponibles");
        assertThat(deGlucosa.get(0).tone()).isEqualTo("amber");
        assertThat(deGlucosa.get(1).detail()).isEqualTo("Vence el 08/10/2026 · 14 días");
        assertThat(deGlucosa.get(1).icon()).isEqualTo("calendar");
    }

    @Test
    void elMaterialAgotadoAvisaEnRojo() {
        AlertDto hisopos = alerts.alerts().stream()
                .filter(a -> a.title().equals("Hisopos estériles"))
                .findFirst().orElseThrow();

        assertThat(hisopos.detail()).isEqualTo("Sin existencias · requiere reposición");
        assertThat(hisopos.tone()).isEqualTo("red");
        assertThat(hisopos.icon()).isEqualTo("box");
    }

    /** Las 4 primeras son las que caben en el dashboard, asi que su orden
     *  no es un detalle. */
    @Test
    void lasPrimerasAlertasSonLasDeLosRecursos() {
        List<AlertDto> lista = alerts.alerts();

        assertThat(lista.get(0).title()).isEqualTo("Analizador de inmunología");
        assertThat(lista.get(0).module()).isEqualTo("Equipos");
        assertThat(lista.subList(4, 8))
                .extracting(AlertDto::title)
                .containsExactly("Hisopos estériles", "Resultados por validar",
                        "Muestras pendientes", "Exámenes no disponibles");
    }

    @Test
    void lasAlertasDeMuestrasCuentanLoQueHayEnCadaEstado() {
        List<AlertDto> lista = alerts.alerts();

        assertThat(lista).extracting(AlertDto::detail)
                .contains("2 resultados requieren revisión", "5 muestras por procesar",
                        "4 exámenes con recursos afectados");
    }

    // -------------------------------------------------------- acceso general

    @Test
    void elAccesoEsGeneralYSeRegistraComoRegistroLocal() {
        assertThat(session.userName()).isEqualTo("Responsable del laboratorio");
        assertThat(session.role()).isEqualTo(Role.ACCESO_GENERAL);
        assertThat(session.role().label()).isEqualTo("Acceso general");
    }
}
