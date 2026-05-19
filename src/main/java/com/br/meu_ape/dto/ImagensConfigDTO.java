package com.br.meu_ape.dto;

import java.util.List;

public record ImagensConfigDTO(
        boolean manterBanner,
        ImagemMetadadosDTO bannerMeta,

        boolean manterMap,
        ImagemMetadadosDTO mapMeta,

        List<String> plantasMantidas,
        List<ImagemMetadadosDTO> plantasMeta,

        List<String> galeriaMantida,
        List<ImagemMetadadosDTO> galeriaMeta
) {}