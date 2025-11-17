package com.github.souzafcharles.api.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record SpatialDataRequestDTO(
        @JsonProperty("point")
        List<Double> pointCoordinates,
        @JsonProperty("multipoint")
        List<List<Double>> multiPointCoordinates,
        @JsonProperty("linestring")
        List<List<Double>> lineStringCoordinates,
        @JsonProperty("multilinestring")
        List<List<List<Double>>> multiLineStringCoordinates,
        @JsonProperty("polygon")
        List<List<List<Double>>> polygonCoordinates,
        @JsonProperty("multipolygon")
        List<List<List<List<Double>>>> multiPolygonCoordinates

) {}
