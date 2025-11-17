package com.github.souzafcharles.api.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.locationtech.jts.geom.Geometry;

public record SpatialDataDeserializerRequestDTO(
        @JsonProperty("point")
        Geometry point,
        @JsonProperty("multipoint")
        Geometry multipoint,
        @JsonProperty("linestring")
        Geometry linestring,
        @JsonProperty("multilinestring")
        Geometry multilinestring,
        @JsonProperty("polygon")
        Geometry polygon,
        @JsonProperty("multipolygon")
        Geometry multipolygon
) {}