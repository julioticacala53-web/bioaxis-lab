package pe.vizcacha.laboratorio;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * La compra con varias lineas: la entrada masiva al almacen.
 *
 * Nace de una observacion del docente -- registrar una guia de remision de
 * uno en uno no es razonable -- y se comporta como el inventario fisico
 * salvo en lo esencial: aqui la cantidad SUMA, no sustituye.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PurchaseTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper json;

    private JsonNode snapshot() throws Exception {
        byte[] cuerpo = mvc.perform(get("/api/database")).andReturn()
                .getResponse().getContentAsByteArray();
        return json.readTree(new String(cuerpo, StandardCharsets.UTF_8));
    }

    private JsonNode recurso(String id) throws Exception {
        for (JsonNode n : snapshot().get("resources")) {
            if (n.get("id").asText().equals(id)) {
                return n;
            }
        }
        throw new IllegalStateException("No existe el recurso " + id);
    }

    private void comprar(Object cuerpo, int estado) throws Exception {
        mvc.perform(post("/api/inventory/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(cuerpo)))
                .andExpect(status().is(estado));
    }

    private Map<String, Object> compra(List<Map<String, Object>> lineas) {
        return Map.of(
                "date", "2026-09-24",
                "supplier", "Droguería Andina",
                "document", "GR-000777",
                "responsible", "Puma Montés",
                "kind", "Materiales",
                "items", lineas);
    }

    private Map<String, Object> linea(String id, String qty) {
        return Map.of("resourceId", id, "qty", qty, "lot", "", "expires", "");
    }

    @Test
    void unaCompraSumaElStockDeVariosRecursosALaVez() throws Exception {
        int antesUno = recurso("MAT-001").get("qty").asInt();
        int antesDos = recurso("MAT-002").get("qty").asInt();

        comprar(compra(List.of(linea("MAT-001", "32"), linea("MAT-002", "10"))), 200);

        assertThat(recurso("MAT-001").get("qty").asInt()).isEqualTo(antesUno + 32);
        assertThat(recurso("MAT-002").get("qty").asInt()).isEqualTo(antesDos + 10);
    }

    @Test
    void laLineaEnBlancoNoToca            () throws Exception {
        int antes = recurso("MAT-003").get("qty").asInt();

        comprar(compra(List.of(linea("MAT-001", "5"), linea("MAT-003", ""))), 200);

        assertThat(recurso("MAT-003").get("qty").asInt()).isEqualTo(antes);
    }

    @Test
    void elLoteYElVencimientoDeLaLineaSustituyenALosDelRecurso() throws Exception {
        comprar(Map.of(
                "date", "2026-09-24",
                "supplier", "Droguería Andina",
                "document", "F-001",
                "responsible", "Puma Montés",
                "kind", "Reactivos",
                "items", List.of(Map.of(
                        "resourceId", "REA-002",
                        "qty", "40",
                        "lot", "LOT-2027-B",
                        "expires", "2027-12-31"))), 200);

        assertThat(recurso("REA-002").get("lot").asText()).isEqualTo("LOT-2027-B");
        assertThat(recurso("REA-002").get("expires").asText()).isEqualTo("2027-12-31");
    }

    /**
     * Comprar lo que faltaba tiene que devolver el recurso a Disponible: es
     * justo el acoplamiento que hace util la pantalla, porque de ahi depende
     * que los examenes se puedan procesar.
     */
    @Test
    void comprarPorEncimaDelMinimoDevuelveElRecursoADisponible() throws Exception {
        assertThat(recurso("MAT-004").get("derivedStatus").asText()).isEqualTo("Agotado");

        comprar(compra(List.of(linea("MAT-004", "40"))), 200);

        assertThat(recurso("MAT-004").get("derivedStatus").asText()).isEqualTo("Disponible");
    }

    @Test
    void cadaLineaDejaSuPropiaEntradaDeAuditoriaConProveedorYDocumento() throws Exception {
        comprar(compra(List.of(linea("MAT-001", "7"))), 200);

        // La auditoria del snapshot viaja con lo mas reciente delante: el
        // modulo la pinta en el orden del array y el original inserta con
        // unshift. Lo ultimo registrado es, por tanto, el indice 0.
        JsonNode ultima = snapshot().get("audit").get(0);
        assertThat(ultima.get("action").asText())
                .contains("Compra recibida")
                .contains("+7")
                .contains("Droguería Andina")
                .contains("GR-000777");
        assertThat(ultima.get("record").asText()).isEqualTo("MAT-001");
    }

    @Test
    void sinNingunaCantidadNoSeRegistraNada() throws Exception {
        comprar(compra(List.of(linea("MAT-001", ""))), 422);
    }

    @Test
    void unaCantidadQueNoEsUnEnteroPositivoTumbaLaCompraEntera() throws Exception {
        int antes = recurso("MAT-001").get("qty").asInt();

        comprar(compra(List.of(linea("MAT-001", "5"), linea("MAT-002", "-3"))), 422);

        assertThat(recurso("MAT-001").get("qty").asInt())
                .as("ninguna linea se aplica si alguna es invalida")
                .isEqualTo(antes);
    }
}
