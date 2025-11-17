package com.github.souzafcharles.api.model.dto;

import org.locationtech.jts.geom.Geometry;

public record SpatialDataResponseDTO(
        Long id,
        Geometry point,
        Geometry multiPoint,
        Geometry lineString,
        Geometry multiLineString,
        Geometry polygon,
        Geometry multiPolygon,
        Geometry geometryCollection
) {}