package com.github.souzafcharles.api.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.souzafcharles.api.utils.Messages;
import jakarta.validation.constraints.Size;

import java.util.List;

public record SpatialDataSerializerRequestDTO(
        @JsonProperty("point")
        @Size(min = 2, max = 2, message = Messages.POINT_COORDINATES_INVALID)
        List<Double> point,

        @JsonProperty("multipoint")
        List<@Size(min = 2, max = 2, message = Messages.POINT_COORDINATES_INVALID) List<Double>> multipoint,

        @JsonProperty("linestring")
        @Size(min = 2, message = Messages.LINESTRING_COORDINATES_INVALID)
        List<@Size(min = 2, max = 2, message = Messages.POINT_COORDINATES_INVALID) List<Double>> linestring,

        @JsonProperty("multilinestring")
        List<@Size(min = 2, message = Messages.LINESTRING_COORDINATES_INVALID) List<@Size(min = 2, max = 2, message = Messages.POINT_COORDINATES_INVALID) List<Double>>> multilinestring,

        @JsonProperty("polygon")
        List<@Size(min = 4, message = Messages.POLYGON_COORDINATES_INVALID) List<@Size(min = 2, max = 2, message = Messages.POINT_COORDINATES_INVALID) List<Double>>> polygon,

        @JsonProperty("multipolygon")
        List<List<@Size(min = 4, message = Messages.POLYGON_COORDINATES_INVALID) List<@Size(min = 2, max = 2, message = Messages.POINT_COORDINATES_INVALID) List<Double>>>> multipolygon
) {}