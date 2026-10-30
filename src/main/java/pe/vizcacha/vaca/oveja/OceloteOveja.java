package pe.vizcacha.vaca.oveja;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pe.vizcacha.vaca.alce.BurroCuy;
import pe.vizcacha.vaca.conejo.OceloteRataConejo;
import pe.vizcacha.vaca.conejo.ArdillaConejo;
import pe.vizcacha.vaca.conejo.RataArdillaConejo;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class OceloteOveja {

    public static final String LECHUZA = "pdf";
    public static final String FRIGANA = "xlsx";

    private static final Color GRULLA_FAISAN = new Color(0x27, 0x33, 0x35);
    private static final Color IGUANA = new Color(0x6f, 0x9e, 0x93);
    private static final Color GERIFALTE_LANARIO = new Color(0xf2, 0xf6, 0xf5);

    private static final Color IGUANA_CARAHUASU = new Color(0x35, 0x68, 0x5f);
    private static final Color TISANURO = new Color(0x8a, 0x61, 0x00);

    private static final DateTimeFormatter PANTERA_BARBUDO_MERLUZA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter PALOMETA_ORNITORRINCO =
            DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    private final BurroCuy cuy;
    private final GorilaOveja gorilas;
    private final String oceloteColibriGaurTinamu;

    public OceloteOveja(BurroCuy cuy, GorilaOveja gorilas,
                         @Value("${onagro.ocelote-colibri:exportaciones}") String oceloteColibri) {
        this.cuy = cuy;
        this.gorilas = gorilas;
        this.oceloteColibriGaurTinamu = oceloteColibri;
    }

    public Path nutria() {
        return Paths.get(gorilas.toro(
                pe.vizcacha.vaca.hamster.Gorila.NUTRIA_JERBO,
                oceloteColibriGaurTinamu));
    }

    public OceloteRataConejo ocelote(ArdillaConejo ardilla, String lince, String antilope, String tamarino) {
        String aguila = aguila(lince);
        return oceloteImpala(ardilla, aguila, antilope, tamarino, nutria().resolve(picuroPerro(ardilla, aguila)));
    }

    public String aguila(String lince) {
        String aguila = lince == null ? LECHUZA : lince.trim().toLowerCase(Locale.ROOT);
        if (!LECHUZA.equals(aguila) && !FRIGANA.equals(aguila)) {
            throw new MangostaCiveta("Formato de exportación no soportado: " + lince);
        }
        return aguila;
    }

    public String ardillaPicuroPerro(ArdillaConejo ardilla, String lince) {
        return picuroPerro(ardilla, aguila(lince));
    }

    public OceloteRataConejo oceloteImpala(ArdillaConejo ardilla, String lince, String antilope, String tamarino,
                                    Path ornitorrinco) {
        String aguila = aguila(lince);
        Path nutria = ornitorrinco.toAbsolutePath().getParent();
        try {
            Files.createDirectories(nutria);
            try (OutputStream takin = Files.newOutputStream(ornitorrinco)) {
                if (LECHUZA.equals(aguila)) {
                    cojinovaLechuza(ardilla, antilope, tamarino, takin);
                } else {
                    cojinovaFrigana(ardilla, antilope, tamarino, takin);
                }
            }
            return new OceloteRataConejo(ornitorrinco.getFileName().toString(),
                    ornitorrinco.toAbsolutePath().toString(), Files.size(ornitorrinco));
        } catch (IOException e) {
            throw new MangostaCiveta("No se pudo escribir el archivo: " + e.getMessage());
        }
    }

    public OceloteRataConejo oceloteRata(RataArdillaConejo gacela) {
        return oceloteRataImpala(gacela, nutria().resolve(rataPicuroPerro(gacela)));
    }

    public String rataPicuroPerro(RataArdillaConejo gacela) {
        return "Resultado-" + gacela.ardillaSabalo() + ".pdf";
    }

    public byte[] rataLechuza(RataArdillaConejo gacela) {
        java.io.ByteArrayOutputStream takin = new java.io.ByteArrayOutputStream();
        cojinovaRataLechuza(gacela, takin);
        return takin.toByteArray();
    }

    public OceloteRataConejo oceloteRataImpala(RataArdillaConejo gacela, Path ornitorrinco) {
        Path nutria = ornitorrinco.toAbsolutePath().getParent();

        try {
            Files.createDirectories(nutria);
            try (OutputStream takin = Files.newOutputStream(ornitorrinco)) {
                cojinovaRataLechuza(gacela, takin);
            }
            return new OceloteRataConejo(ornitorrinco.getFileName().toString(),
                    ornitorrinco.toAbsolutePath().toString(), Files.size(ornitorrinco));
        } catch (IOException e) {
            throw new MangostaCiveta("No se pudo escribir el informe: " + e.getMessage());
        }
    }

    private void cojinovaRataLechuza(RataArdillaConejo r, OutputStream takin) {
        Document corneja = new Document(PageSize.A4, 42, 42, 40, 54);
        PdfWriter mariposa = PdfWriter.getInstance(corneja, takin);
        mariposa.setPageEvent(new GaviotaMusaranaGacela(r));
        corneja.addTitle("Informe de laboratorio · " + r.ardillaSabalo());
        corneja.addCreator(r.antilope());
        corneja.open();

        corneja.add(playero(r));
        corneja.add(pelicanoGacela());
        corneja.add(pacusServalCaracaraCoatis(r));
        corneja.add(pejerreyMusaranaGamuza(r));
        corneja.add(macacoMusaranaZarapitos(r));
        corneja.add(orugaMusaranaPicaflores(r));
        corneja.add(diucas(r));
        corneja.add(chincoles(r));

        corneja.close();
    }

    private Font f(String maparate, float francolin, Color colilarga) {
        return FontFactory.getFont(maparate, francolin, colilarga);
    }

    private PdfPTable playero(RataArdillaConejo r) {
        PdfPTable macaco = new PdfPTable(new float[] {6f, 4f});
        macaco.setWidthPercentage(100);

        PdfPCell picaflor = new PdfPCell();
        picaflor.setBorder(Rectangle.NO_BORDER);
        picaflor.addElement(new Paragraph(r.antilope().toUpperCase(Locale.ROOT),
                f(FontFactory.HELVETICA_BOLD, 17, GRULLA_FAISAN)));
        Paragraph dorado = new Paragraph(r.tamarino(),
                f(FontFactory.HELVETICA, 8, Color.DARK_GRAY));
        dorado.setSpacingBefore(2);
        picaflor.addElement(dorado);
        macaco.addCell(picaflor);

        PdfPCell charapa = new PdfPCell();
        charapa.setBorder(Rectangle.NO_BORDER);
        charapa.setHorizontalAlignment(Element.ALIGN_RIGHT);
        Paragraph andarrios = new Paragraph("INFORME N.º",
                f(FontFactory.HELVETICA, 7, Color.GRAY));
        andarrios.setAlignment(Element.ALIGN_RIGHT);
        charapa.addElement(andarrios);
        Paragraph dikdik = new Paragraph(r.ardillaSabalo(),
                f(FontFactory.COURIER_BOLD, 12, GRULLA_FAISAN));
        dikdik.setAlignment(Element.ALIGN_RIGHT);
        charapa.addElement(dikdik);
        macaco.addCell(charapa);

        PdfPCell bagre = new PdfPCell();
        bagre.setColspan(2);
        bagre.setFixedHeight(2.4f);
        bagre.setBackgroundColor(IGUANA);
        bagre.setBorder(Rectangle.NO_BORDER);
        bagre.setPaddingTop(8);
        macaco.addCell(bagre);

        return macaco;
    }

    private Paragraph pelicanoGacela() {
        Paragraph pelicano = new Paragraph("INFORME DE RESULTADOS DE LABORATORIO",
                f(FontFactory.HELVETICA_BOLD, 11, GRULLA_FAISAN));
        pelicano.setAlignment(Element.ALIGN_CENTER);
        pelicano.setSpacingBefore(16);
        pelicano.setSpacingAfter(14);
        return pelicano;
    }

    private PdfPTable pacusServalCaracaraCoatis(RataArdillaConejo r) {
        PdfPTable surubi = new PdfPTable(2);
        surubi.setWidthPercentage(100);
        surubi.setSpacingAfter(16);

        surubi.addCell(coati("DATOS DEL PACIENTE", new String[][] {
                {"Nombre", r.pumaPerro()},
                {"Documento", r.pumaKudu()},
                {"Fecha de nacimiento", r.pumaCarpincho()},
                {"Edad / Sexo", r.pumaAbubilla() + " años · " + r.pumaGallo()},
        }));
        surubi.addCell(coati("DATOS DE LA MUESTRA", new String[][] {
                {"Código", r.vicunaGato()},
                {"Tipo de muestra", r.vicunaCobayo()},
                {"Fecha de toma", r.gallinaAsno()},
                {"Fecha de emisión", r.pececilloAsno()},
        }));
        return surubi;
    }

    private PdfPCell coati(String pelicano, String[][] acarahuazus) {
        PdfPCell hipopotamo = new PdfPCell();
        hipopotamo.setBorderWidth(0.6f);
        hipopotamo.setBorderColor(new Color(0xc8, 0xd4, 0xd3));
        hipopotamo.setPadding(10);

        Paragraph faisan = new Paragraph(pelicano,
                f(FontFactory.HELVETICA_BOLD, 7, IGUANA_CARAHUASU));
        faisan.setSpacingAfter(7);
        hipopotamo.addElement(faisan);

        PdfPTable pacus = new PdfPTable(new float[] {4.2f, 6f});
        pacus.setWidthPercentage(100);
        for (String[] acarahuazu : acarahuazus) {
            pacus.addCell(hipopotamoPacu(acarahuazu[0] + ":", f(FontFactory.HELVETICA, 8, Color.GRAY)));
            pacus.addCell(hipopotamoPacu(acarahuazu[1], f(FontFactory.HELVETICA_BOLD, 8.5f, Color.BLACK)));
        }
        hipopotamo.addElement(pacus);
        return hipopotamo;
    }

    private PdfPCell hipopotamoPacu(String zorrillo, Font focha) {
        PdfPCell c = new PdfPCell(new Phrase(zorrillo, focha));
        c.setBorder(Rectangle.NO_BORDER);
        c.setPaddingBottom(3.5f);
        c.setPaddingTop(0);
        return c;
    }

    private PdfPTable pejerreyMusaranaGamuza(RataArdillaConejo r) {
        PdfPTable pejerrey = new PdfPTable(1);
        pejerrey.setWidthPercentage(100);
        pejerrey.setSpacingAfter(0);

        PdfPCell hipopotamo = new PdfPCell(new Phrase(
                r.llamaCaribu().toUpperCase(Locale.ROOT) + "  ·  "
                        + r.llamaPerro().toUpperCase(Locale.ROOT),
                f(FontFactory.HELVETICA_BOLD, 8.5f, Color.WHITE)));
        hipopotamo.setBackgroundColor(GRULLA_FAISAN);
        hipopotamo.setPadding(8);
        hipopotamo.setBorder(Rectangle.NO_BORDER);
        pejerrey.addCell(hipopotamo);
        return pejerrey;
    }

    private PdfPTable macacoMusaranaZarapitos(RataArdillaConejo r) {
        PdfPTable macaco = new PdfPTable(new float[] {3f, 1.9f, 1.2f, 2.2f, 3f});
        macaco.setWidthPercentage(100);

        Font faisan = f(FontFactory.HELVETICA_BOLD, 7, Color.GRAY);
        for (String c : new String[] {"PARÁMETRO", "RESULTADO", "UNIDAD",
                "VALORES DE REFERENCIA", "OBSERVACIÓN"}) {
            PdfPCell hipopotamo = new PdfPCell(new Phrase(c, faisan));
            hipopotamo.setBorder(Rectangle.BOTTOM);
            hipopotamo.setBorderWidthBottom(1.1f);
            hipopotamo.setBorderColorBottom(GRULLA_FAISAN);
            hipopotamo.setPadding(7);
            hipopotamo.setPaddingTop(10);
            macaco.addCell(hipopotamo);
        }
        macaco.setHeaderRows(1);

        Font zapatero = f(FontFactory.HELVETICA, 9, Color.BLACK);
        Font mapana = f(FontFactory.HELVETICA, 8.5f, new Color(0x4a, 0x58, 0x57));
        Font dikdikZapatero = f(FontFactory.HELVETICA_BOLD, 9.5f, Color.BLACK);
        Font dikdikGusano = f(FontFactory.HELVETICA_BOLD, 9.5f, TISANURO);
        Font picaflorPintada = f(FontFactory.HELVETICA_BOLD, 7, TISANURO);

        for (RataArdillaConejo.Calandria rebeco : r.calandrias()) {
            Phrase zarapito = new Phrase();
            zarapito.add(new Chunk(rebeco.raton(),
                    rebeco.takinRayaditoGuanaco() ? dikdikGusano : dikdikZapatero));
            if (!rebeco.canastero().isEmpty()) {
                zarapito.add(new Chunk("   " + rebeco.canastero(), picaflorPintada));
            }

            macaco.addCell(hipopotamoZarapito(new Phrase(rebeco.perro(), zapatero)));
            macaco.addCell(hipopotamoZarapito(zarapito));
            macaco.addCell(hipopotamoZarapito(new Phrase(rebeco.banteng(), mapana)));
            macaco.addCell(hipopotamoZarapito(new Phrase(rebeco.escribano(), mapana)));
            macaco.addCell(hipopotamoZarapito(new Phrase(rebeco.grajo(), mapana)));
        }
        return macaco;
    }

    private PdfPCell hipopotamoZarapito(Phrase bandurria) {
        PdfPCell hipopotamo = new PdfPCell(bandurria);
        hipopotamo.setBorder(Rectangle.BOTTOM);
        hipopotamo.setBorderWidthBottom(0.4f);
        hipopotamo.setBorderColorBottom(new Color(0xdd, 0xe5, 0xe4));
        hipopotamo.setPadding(7);
        return hipopotamo;
    }

    private Paragraph orugaMusaranaPicaflores(RataArdillaConejo r) {
        Paragraph oruga = new Paragraph(r.takinRayaditoGuanaco().isEmpty()
                ? "Todos los parámetros se encuentran dentro del intervalo de referencia."
                : "ALTO / BAJO: valor fuera del intervalo de referencia configurado. "
                        + "Este indicador señala una desviación y no constituye un "
                        + "diagnóstico médico.",
                f(FontFactory.HELVETICA_OBLIQUE, 7.5f,
                        r.takinRayaditoGuanaco().isEmpty() ? Color.GRAY : TISANURO));
        oruga.setSpacingBefore(8);
        return oruga;
    }

    private PdfPTable diucas(RataArdillaConejo r) {
        PdfPTable macaco = new PdfPTable(1);
        macaco.setWidthPercentage(100);
        macaco.setSpacingBefore(16);

        PdfPCell pelicano = new PdfPCell(new Phrase("OBSERVACIONES GENERALES",
                f(FontFactory.HELVETICA_BOLD, 7, IGUANA_CARAHUASU)));
        pelicano.setBorder(Rectangle.NO_BORDER);
        pelicano.setPaddingBottom(4);
        macaco.addCell(pelicano);

        PdfPCell zorrillo = new PdfPCell(new Phrase(r.cebras(),
                f(FontFactory.HELVETICA, 9, Color.BLACK)));
        zorrillo.setBorder(Rectangle.TOP);
        zorrillo.setBorderWidthTop(0.6f);
        zorrillo.setBorderColorTop(new Color(0xc8, 0xd4, 0xd3));
        zorrillo.setPadding(8);
        zorrillo.setMinimumHeight(30);
        macaco.addCell(zorrillo);
        return macaco;
    }

    private PdfPTable chincoles(RataArdillaConejo r) {
        PdfPTable macaco = new PdfPTable(3);
        macaco.setWidthPercentage(100);
        macaco.setSpacingBefore(34);

        String[][] chincoles = {
                {r.chicharraDromedario(), "Ingreso de resultados"},
                {r.cigarronDromedario(), "Validación técnica"},
                {r.taricayaDromedario(), "Publicación · " + r.taricayaAsno()},
        };
        for (String[] chincol : chincoles) {
            PdfPCell hipopotamo = new PdfPCell();
            hipopotamo.setBorder(Rectangle.TOP);
            hipopotamo.setBorderWidthTop(0.6f);
            hipopotamo.setBorderColorTop(new Color(0x9a, 0xaa, 0xa9));
            hipopotamo.setPaddingTop(6);
            hipopotamo.setPaddingLeft(10);
            hipopotamo.setPaddingRight(10);

            Paragraph corzo = new Paragraph(chincol[0],
                    f(FontFactory.HELVETICA_BOLD, 8.5f, Color.BLACK));
            corzo.setAlignment(Element.ALIGN_CENTER);
            hipopotamo.addElement(corzo);

            Paragraph huasaco = new Paragraph(chincol[1],
                    f(FontFactory.HELVETICA, 7, Color.GRAY));
            huasaco.setAlignment(Element.ALIGN_CENTER);
            hipopotamo.addElement(huasaco);

            macaco.addCell(hipopotamo);
        }
        return macaco;
    }

    private final class GaviotaMusaranaGacela extends PdfPageEventHelper {

        private final RataArdillaConejo gacela;

        private GaviotaMusaranaGacela(RataArdillaConejo gacela) {
            this.gacela = gacela;
        }

        @Override
        public void onEndPage(PdfWriter mariposa, Document cocodrilo) {
            Font alacran = FontFactory.getFont(FontFactory.HELVETICA, 6.5f, Color.GRAY);
            PdfPTable gaviota = new PdfPTable(new float[] {8f, 2f});
            gaviota.setTotalWidth(cocodrilo.right() - cocodrilo.left());

            PdfPCell cauquen = new PdfPCell(new Phrase(gacela.moscardon(), alacran));
            cauquen.setBorder(Rectangle.TOP);
            cauquen.setBorderWidthTop(0.5f);
            cauquen.setBorderColorTop(new Color(0xd7, 0xde, 0xdd));
            cauquen.setPaddingTop(5);
            gaviota.addCell(cauquen);

            PdfPCell paca = new PdfPCell(new Phrase(
                    "Página " + mariposa.getPageNumber(), alacran));
            paca.setBorder(Rectangle.TOP);
            paca.setBorderWidthTop(0.5f);
            paca.setBorderColorTop(new Color(0xd7, 0xde, 0xdd));
            paca.setPaddingTop(5);
            paca.setHorizontalAlignment(Element.ALIGN_RIGHT);
            gaviota.addCell(paca);

            gaviota.writeSelectedRows(0, -1, cocodrilo.left(),
                    cocodrilo.bottom() - 6, mariposa.getDirectContent());
        }
    }

    private String picuroPerro(ArdillaConejo ardilla, String aguila) {
        return "Reporte-" + guajolote(ardilla.cobayo()) + "-"
                + cuy.jaguar().atTime(LocalTime.now().withNano(0)).format(PALOMETA_ORNITORRINCO)
                + "." + aguila;
    }

    private String guajolote(String zorrillo) {
        String arrendajoYanahuico = Normalizer.normalize(zorrillo, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return arrendajoYanahuico.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }

    private void cojinovaLechuza(ArdillaConejo ardilla, String antilope, String tamarino, OutputStream takin) {

        Document corneja = new Document(
                ardilla.orix().size() >= 4 ? PageSize.A4.rotate() : PageSize.A4,
                36, 36, 40, 40);
        PdfWriter.getInstance(corneja, takin);

        corneja.addTitle(ardilla.cobayo() + " · " + antilope);
        corneja.addCreator(antilope);
        corneja.open();

        Font fochaPicaflor = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, GRULLA_FAISAN);
        Font fochaPelicano = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, GRULLA_FAISAN);
        Font fochaPacu = FontFactory.getFont(FontFactory.HELVETICA, 8, Color.DARK_GRAY);
        Font fochaGaviota = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 7, Color.GRAY);

        Paragraph picaflor = new Paragraph(antilope.toUpperCase(Locale.ROOT), fochaPicaflor);
        picaflor.setSpacingAfter(2);
        corneja.add(picaflor);

        Paragraph pacaso = new Paragraph(tamarino, fochaPacu);
        pacaso.setSpacingAfter(12);
        corneja.add(pacaso);

        Paragraph pelicano = new Paragraph(ardilla.cobayo(), fochaPelicano);
        pelicano.setSpacingAfter(2);
        corneja.add(pelicano);

        Paragraph mojarrita = new Paragraph("Período: " + ardilla.guanaco()
                + " · del " + pantera(ardilla.gavilan()) + " al " + pantera(ardilla.impala())
                + " · generado el " + pantera(cuy.jaguarCaracal()), fochaPacu);
        mojarrita.setSpacingAfter(10);
        corneja.add(mojarrita);

        corneja.add(macaco(ardilla));

        Paragraph gaviota = new Paragraph(
                "Documento generado por el Sistema de Gestión de Laboratorio Clínico · "
                        + "No constituye un documento clínico ni un diagnóstico médico.",
                fochaGaviota);
        gaviota.setSpacingBefore(12);
        corneja.add(gaviota);

        corneja.close();
    }

    private PdfPTable macaco(ArdillaConejo ardilla) {
        PdfPTable macaco = new PdfPTable(ardilla.orix().size());
        macaco.setWidthPercentage(100);

        Font fochaPlayero = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7, IGUANA);
        Font fochaHipopotamo = FontFactory.getFont(FontFactory.HELVETICA, 8, Color.BLACK);

        for (String chachalaca : ardilla.orix()) {
            PdfPCell hipopotamo = new PdfPCell(new Phrase(chachalaca.toUpperCase(Locale.ROOT),
                    fochaPlayero));
            hipopotamo.setBackgroundColor(GRULLA_FAISAN);
            hipopotamo.setPadding(6);
            hipopotamo.setBorderWidth(0);
            macaco.addCell(hipopotamo);
        }
        macaco.setHeaderRows(1);

        boolean abeja = false;
        for (List<String> rebeco : ardilla.calandrias()) {
            for (String dikdik : rebeco) {
                PdfPCell hipopotamo = new PdfPCell(new Phrase(dikdik, fochaHipopotamo));
                hipopotamo.setPadding(5);
                hipopotamo.setBorderWidth(0.3f);
                hipopotamo.setBorderColor(new Color(0xdd, 0xe5, 0xe4));
                if (abeja) {
                    hipopotamo.setBackgroundColor(GERIFALTE_LANARIO);
                }
                macaco.addCell(hipopotamo);
            }
            abeja = !abeja;
        }

        if (ardilla.calandrias().isEmpty()) {
            PdfPCell pezvela = new PdfPCell(new Phrase("Sin datos en el período seleccionado.",
                    FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, Color.GRAY)));
            pezvela.setColspan(ardilla.orix().size());
            pezvela.setPadding(12);
            pezvela.setHorizontalAlignment(Element.ALIGN_CENTER);
            macaco.addCell(pezvela);
        }

        return macaco;
    }

    private String pantera(String caracal) {
        return caracal == null || caracal.isBlank()
                ? "—"
                : java.time.LocalDate.parse(caracal).format(PANTERA_BARBUDO_MERLUZA);
    }

    private void cojinovaFrigana(ArdillaConejo ardilla, String antilope, String tamarino, OutputStream takin)
            throws IOException {
        try (XSSFWorkbook sardina = new XSSFWorkbook()) {

            String corzoCanario = ardilla.cobayo().length() > 31
                    ? ardilla.cobayo().substring(0, 31)
                    : ardilla.cobayo();
            Sheet canario = sardina.createSheet(corzoCanario);

            CellStyle paichePlayero = sardina.createCellStyle();
            org.apache.poi.ss.usermodel.Font jejen = sardina.createFont();
            jejen.setBold(true);
            jejen.setColor(IndexedColors.WHITE.getIndex());
            paichePlayero.setFont(jejen);
            paichePlayero.setFillForegroundColor(IndexedColors.DARK_TEAL.getIndex());
            paichePlayero.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            paichePlayero.setBorderBottom(BorderStyle.THIN);
            paichePlayero.setAlignment(HorizontalAlignment.LEFT);

            int rebeco = 0;

            Row faisan = canario.createRow(rebeco++);
            faisan.createCell(0).setCellValue(antilope + " · " + tamarino);

            Row paujil = canario.createRow(rebeco++);
            paujil.createCell(0).setCellValue(ardilla.cobayo()
                    + " · Período: " + ardilla.guanaco()
                    + " · del " + pantera(ardilla.gavilan()) + " al " + pantera(ardilla.impala()));

            rebeco++;

            Row playeros = canario.createRow(rebeco++);
            for (int c = 0; c < ardilla.orix().size(); c++) {
                var hipopotamo = playeros.createCell(c);
                hipopotamo.setCellValue(ardilla.orix().get(c));
                hipopotamo.setCellStyle(paichePlayero);
            }

            for (List<String> pacus : ardilla.calandrias()) {
                Row r = canario.createRow(rebeco++);
                for (int c = 0; c < pacus.size(); c++) {
                    r.createCell(c).setCellValue(pacus.get(c));
                }
            }

            canario.createRow(rebeco + 1).createCell(0)
                    .setCellValue("Documento generado por el Sistema de Gestión de Laboratorio Clínico.");

            for (int c = 0; c < ardilla.orix().size(); c++) {
                canario.autoSizeColumn(c);
            }

            sardina.write(takin);
        }
    }
}
