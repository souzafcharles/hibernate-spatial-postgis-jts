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

    // Exception messages
    public static final String DATA_INTEGRITY_VIOLATION = "Data integrity violation";
    public static final String INVALID_JSON_FORMAT = "Invalid JSON format or structure";
    public static final String INVALID_PARAMETER_TYPE = "Invalid parameter type for '%s'. Expected: %s";
    public static final String MISSING_REQUIRED_PARAMETER = "Required parameter '%s' is missing";

    // GeoJSON and Service messages
    public static final String GEOJSON_FEATURE_TYPE = "Feature";
    public static final String GEOJSON_PROPERTIES_DESCRIPTION = "Polygon from database";
    public static final String GEOJSON_PROPERTIES_KEY = "properties";

    // OpenAPI General
    public static final String OPENAPI_TITLE = "Spring Boot Spatial PostGIS API";
    public static final String OPENAPI_DESCRIPTION = "API for handling spatial data with PostgreSQL and PostGIS";

    // Controller endpoints documentation
    public static final String TAG_NAME = "Spatial Data";
    public static final String TAG_DESCRIPTION = "API for managing spatial data with JTS geometries and GeoJSON";
    public static final String CREATE_SERIALIZER_SUMMARY = "Create spatial data from coordinate lists";
    public static final String CREATE_SERIALIZER_DESCRIPTION = "Accepts coordinate arrays and creates JTS geometries";
    public static final String CREATE_DESERIALIZER_SUMMARY = "Create spatial data from GeoJSON geometries";
    public static final String CREATE_DESERIALIZER_DESCRIPTION = "Accepts GeoJSON geometry objects and stores them as JTS geometries";
    public static final String GET_ALL_SUMMARY = "Retrieve all spatial data";
    public static final String GET_ALL_DESCRIPTION = "Returns all spatial data records with their JTS geometries";
    public static final String GET_BY_ID_SUMMARY = "Get spatial data by ID";
    public static final String GET_BY_ID_DESCRIPTION = "Returns a specific spatial data record by its ID";
    public static final String GET_GEOJSON_SUMMARY = "Get polygon as GeoJSON";
    public static final String GET_GEOJSON_DESCRIPTION = "Returns a specific polygon geometry in GeoJSON format";
}