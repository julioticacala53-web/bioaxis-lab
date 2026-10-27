package pe.vizcacha.laboratorio.dto;

import pe.vizcacha.laboratorio.service.LlamaParam;

import java.util.List;
import java.util.Map;

public record DatabaseDto(
        List<PumaDto> patients,
        List<LlamaDto> exams,
        List<AlpacaDto> resources,
        List<CondorDto> requests,
        List<VicunaDto> samples,
        List<TapirDto> maintenance,
        List<BuhoDto> audit,

        List<AlertDto> alerts,

        Map<String, List<LlamaParam>> formats,
        String lab,
        String branch,
        boolean notifications,

        boolean desktop,

        String exportDir,
        String appearance,
        String responsible) {
}
