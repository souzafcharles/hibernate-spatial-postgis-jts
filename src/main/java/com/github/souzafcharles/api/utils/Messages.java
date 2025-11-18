package com.github.souzafcharles.api.utils;

public class Messages {

    private Messages() {
        throw new IllegalStateException("Utility class");
    }

    // Error messages
    public static final String INTERNAL_ERROR_OCCURRED = "An internal error occurred";
    public static final String VALIDATION_ERROR = "Validation error";
    public static final String DATABASE_ERROR = "Database error occurred";
    public static final String SPATIAL_DATA_NOT_FOUND = "Spatial data not found with id: %s";
    public static final String NO_POLYGON_FOUND = "Spatial data with id %s does not contain a polygon";
    public static final String INVALID_POLYGON_COORDINATES = "Polygon must contain at least 4 coordinates (closed ring)";
    public static final String UNSUPPORTED_GEOMETRY_TYPE = "Unsupported geometry type: %s";

    // Validation messages
    public static final String POINT_COORDINATES_INVALID = "Point must have exactly 2 coordinates [longitude, latitude]";
    public static final String LINESTRING_COORDINATES_INVALID = "LineString must have at least 2 coordinate pairs";
    public static final String POLYGON_COORDINATES_INVALID = "Polygon coordinates are invalid";
}