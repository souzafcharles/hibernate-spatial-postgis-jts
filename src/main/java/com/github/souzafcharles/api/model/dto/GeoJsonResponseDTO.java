package com.github.souzafcharles.api.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GeoJsonResponseDTO(
        String type,
        Object geometry,
        @JsonProperty("properties") Object properties
) {}