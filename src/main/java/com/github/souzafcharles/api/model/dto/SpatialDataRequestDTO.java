package com.github.souzafcharles.api.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SpatialDataRequestDTO(
        @JsonProperty("point") List<Double> pointCoords,
        @JsonProperty("multipoint") List<List<Double>> multiPointCoords,
        @JsonProperty("linestring") List<List<Double>> lineStringCoords,
        @JsonProperty("multilinestring") List<List<List<Double>>> multiLineStringCoords,
        @JsonProperty("polygon") List<List<List<Double>>> polygonCoords,
        @JsonProperty("multipolygon") List<List<List<List<Double>>>> multiPolygonCoords
) {}