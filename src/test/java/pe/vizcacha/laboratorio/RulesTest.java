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

    @Test
    void elOrdenDelEstadoDerivadoEsVencidoAgotadoStockBajo() {
        Alpaca r = resource("REA-003");

        r.setQty(0);
        r.setExpires(LocalDate.parse("2026-09-01"));
        assertThat(statuses.derived(r)).isEqualTo(AlpacaStatusService.VENCIDO);

        r.setExpires(LocalDate.parse("2027-04-30"));
        assertThat(statuses.derived(r)).isEqualTo(AlpacaStatusService.AGOTADO);

        r.setQty(10);
        r.setMinQty(15);
        assertThat(statuses.derived(r)).isEqualTo(AlpacaStatusService.STOCK_BAJO);

        r.setQty(100);
        assertThat(statuses.derived(r)).isEqualTo(AlpacaStatusService.DISPONIBLE);
    }

    @Test
    void elEstadoDeUnEquipoEsElQueTieneGuardado() {
        assertThat(statuses.derived(resource("EQ-001"))).isEqualTo("OPERATIVO");
        assertThat(statuses.derived(resource("EQ-003"))).isEqualTo("EN MANTENIMIENTO");
    }

    @Test
    void losMaterialesNoVencen() {
        Alpaca material = resource("MAT-005");
        material.setExpires(LocalDate.parse("2020-01-01"));

        assertThat(statuses.derived(material)).isEqualTo(AlpacaStatusService.DISPONIBLE);
    }

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

    @Test
    void elAccesoEsGeneralYSeRegistraComoRegistroLocal() {
        assertThat(session.userName()).isEqualTo("Responsable del laboratorio");
        assertThat(session.role()).isEqualTo(Role.ACCESO_GENERAL);
        assertThat(session.role().label()).isEqualTo("Acceso general");
    }
}
