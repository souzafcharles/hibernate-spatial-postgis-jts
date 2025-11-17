package com.github.souzafcharles.api.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record SpatialDataSerializerRequestDTO(
        @JsonProperty("point")
        List<Double> point,
        @JsonProperty("multipoint")
        List<List<Double>> multipoint,
        @JsonProperty("linestring")
        List<List<Double>> linestring,
        @JsonProperty("multilinestring")
        List<List<List<Double>>> multilinestring,
        @JsonProperty("polygon")
        List<List<List<Double>>> polygon,
        @JsonProperty("multipolygon")
        List<List<List<List<Double>>>> multipolygon
) {}