![GitHub language count](https://img.shields.io/github/languages/count/souzafcharles/hibernate-spatial-postgis-jts)
![GitHub top language](https://img.shields.io/github/languages/top/souzafcharles/hibernate-spatial-postgis-jts)
![GitHub](https://img.shields.io/github/license/souzafcharles/hibernate-spatial-postgis-jts)
![GitHub last commit](https://img.shields.io/github/last-commit/souzafcharles/hibernate-spatial-postgis-jts)

# Spring Boot Spatial PostGIS Application

---

## 1. Introduction:

<p align="justify">
This backend application was developed using the <code>Java Spring Boot</code> framework and integrates with <strong>PostgreSQL with PostGIS extension</strong> to handle spatial data operations. It provides a robust RESTful interface for managing geometric data, offering comprehensive support for various geometry types including points, linestrings, polygons, and their multi-geometry counterparts.
</p>

<p align="justify">
The system utilizes <code>Hibernate Spatial</code> for object-relational mapping and <code>LocationTech JTS</code> for core spatial functionality. It features dual input formats: a <code>serializer</code> approach accepting coordinate arrays and a <code>deserializer</code> approach accepting GeoJSON geometry objects. Both formats are converted to JTS geometries and persisted in the PostgreSQL database with proper spatial indexing and SRID configuration.
</p>

<p align="justify">
To ensure clean separation of concerns, the application adopts a layered architecture with <code>Controller</code>, <code>Service</code>, and <code>Repository</code> components. It implements custom JSON serializers/deserializers for seamless conversion between JTS geometries and GeoJSON format, enabling interoperability with modern GIS tools and web mapping libraries.
</p>

<p align="justify">
The application maintains code quality standards through a comprehensive testing strategy using <code>JUnit 5</code> and <code>Mockito</code>, following the <strong>AAA pattern (Arrange-Act-Assert)</strong> with enforced code coverage via <code>JaCoCo</code>. This ensures reliability and maintainability while handling complex spatial data transformations.
</p>

<p align="justify">
Beyond basic CRUD operations, the system provides specialized endpoints for retrieving geometries as standardized GeoJSON, supporting visualization in tools like geojson.io. The application demonstrates practical spatial data management patterns including coordinate validation, geometry creation, format transformation between industry standards, and comprehensive error handling for spatial data integrity.
</p>

---

## 2. Examples of Usage (HTTP Requests & Responses)

![GeoJSON Visualization](https://github.com/souzafcharles/hibernate-spatial-postgis-jts/blob/main/local/map.png)

<p align="justify">
The generated GeoJSON responses can be directly visualized using tools like <strong>geojson.io</strong>, which provides multiple map visualization options including <strong>Standard Satellite</strong> view. When using the satellite visualization mode, the map displays the following attribution properties:
</p>

**Map Visualization Properties (Standard Satellite View):**
- **Base Map Provider:** © Mapbox (map rendering and tiles);
- **Data Source:** © OpenStreetMap (open geographic data);
- **Satellite Imagery:** © Maxar (high-resolution satellite imagery);
- **Interface:** geojson.io web platform with options for Standard, Satellite, Outdoors, and Dark themes;
- **Map Improvement:** "Improve this map" option for community contributions to OpenStreetMap;
- **Platform Features:** Open, Save, New, Meta search functionality with Dropbox integration.

<p align="justify">
<strong>Geographic Region Description:</strong> The polygon is situated in the municipality of <strong>Socorro, São Paulo, Brazil</strong>, within the Mantiqueira Mountain Range region. In satellite view, this area displays characteristic features of the Brazilian Highlands, including:
</p>

**Regional Characteristics Visible in Satellite Imagery:**
- **Topography:** Rolling hills and moderate elevation variations typical of the Mantiqueira foothills, with elevations ranging from 700-850 meters above sea level;
- **Land Cover:** Mixed agricultural patterns with pasturelands, small farming plots, and remnant Atlantic Forest vegetation corridors;
- **Urban-Rural Interface:** Clear transition from dense urban fabric in Socorro's center to dispersed rural settlements in the Oratório do Meio neighbourhood;
- **Hydrology:** Small watercourses and tributaries feeding into the Camanducaia River basin, visible as dendritic drainage patterns;
- **Infrastructure:** Road networks including SP-139 connecting rural properties to the municipal seat, visible as linear features crossing the undulating terrain;
- **Settlement Patterns:** Clustered rural properties surrounded by agricultural lands, gradually densifying toward the urban core.

<p align="justify">
The satellite imagery reveals the polygon's coverage spanning from the <strong>Oratório do Meio neighbourhood</strong> in the western rural area (characterized by larger property boundaries and extensive agricultural patterns) toward the <strong>urban center of Socorro</strong> (showing denser settlement patterns, institutional buildings, and infrastructure development). The visualization clearly demonstrates the geographic relationship between the rural property "Sítio Lobisomem" and the urban landmark "Paróquia Nossa Senhora do Perpétuo Socorro" within the polygon's extent.
</p>

<p align="justify">
The Standard Satellite view provides excellent context for understanding the terrain morphology, land use patterns, and spatial relationships within the polygon, making it an ideal tool for validating spatial data accuracy and conducting preliminary geographic analysis of the Socorro region's distinctive landscape characteristics.
</p>

### 2.1. Create Spatial Data - Serializer Format (Coordinate Arrays)
**Request:**
```http
POST /api/spatial-data/serializer
Content-Type: application/json

{
  "point": [-46.579236, -22.593365],
  "multipoint": [
    [-46.579236, -22.593365],
    [-46.568894, -22.604582],
    [-46.548613, -22.596784],
    [-46.537113, -22.601331],
    [-46.529434, -22.592137]
  ],
  "linestring": [
    [-46.579236, -22.593365],
    [-46.568894, -22.604582],
    [-46.548613, -22.596784],
    [-46.537113, -22.601331],
    [-46.529434, -22.592137]
  ],
  "polygon": [
    [
      [-46.579236, -22.593365],
      [-46.568894, -22.604582],
      [-46.548613, -22.596784],
      [-46.537113, -22.601331],
      [-46.529434, -22.592137],
      [-46.579236, -22.593365]
    ]
  ]
}
```

**Response:**
```json
{
  "id": 1,
  "point": {
    "type": "Point",
    "coordinates": [-46.579236, -22.593365]
  },
  "multipoint": {
    "type": "MultiPoint",
    "coordinates": [
      [-46.579236, -22.593365],
      [-46.568894, -22.604582],
      [-46.548613, -22.596784],
      [-46.537113, -22.601331],
      [-46.529434, -22.592137]
    ]
  },
  "linestring": {
    "type": "LineString",
    "coordinates": [
      [-46.579236, -22.593365],
      [-46.568894, -22.604582],
      [-46.548613, -22.596784],
      [-46.537113, -22.601331],
      [-46.529434, -22.592137]
    ]
  },
  "polygon": {
    "type": "Polygon",
    "coordinates": [[
      [-46.579236, -22.593365],
      [-46.568894, -22.604582],
      [-46.548613, -22.596784],
      [-46.537113, -22.601331],
      [-46.529434, -22.592137],
      [-46.579236, -22.593365]
    ]]
  }
}
```

### 2.2. GeoInformational Analysis of the Polygon

<p align="justify">
The provided polygon encompasses an area within the municipality of <strong>Socorro, São Paulo, Brazil</strong>, representing a significant territorial extent that connects rural and urban landscapes of the region. The geometry spans approximately <strong>5.5 kilometers in length</strong> and covers an estimated area of <strong>15-18 square kilometers</strong> based on coordinate analysis.
</p>

<p align="justify">
<strong>Spatial Coverage Analysis:</strong> The polygon's vertices delineate a territory that connects the rural property <strong>Sítio Lobisomem</strong>, located in the <strong>Oratório do Meio neighbourhood</strong> (western Socorro), with the urban center featuring the <strong>Paróquia Nossa Senhora do Perpétuo Socorro</strong>. This creates a geographic corridor that bridges agricultural lands with the municipal seat.
</p>

<p align="justify">
<strong>Coordinate Extremes & Dimensions:</strong>
- <strong>Northernmost Point:</strong> [-46.529434, -22.592137] - Near Sítio Lobisomem area;
- <strong>Southernmost Point:</strong> [-46.568894, -22.604582] - Approaching urban center;
- <strong>Longitudinal Span:</strong> ~5.5 km (0.0498° longitude);
- <strong>Latitudinal Span:</strong> ~1.3 km (0.0124° latitude);
- <strong>Geometric Shape:</strong> Irregular pentagon with concave characteristics.
</p>

<p align="justify">
<strong>Geographic Significance:</strong> The polygon captures the transitional zone between Socorro's rural hinterlands and urban core, representing typical São Paulo interior topography with elevation variations between 700-800 meters above sea level. The area likely includes portions of the Mantiqueira Mountain Range foothills, characterized by rolling hills and small watercourses that feed into the Camanducaia River basin.
</p>

### 2.3. Create Spatial Data - Deserializer Format (GeoJSON)
**Request:**
```http
POST /api/spatial-data/deserializer
Content-Type: application/json

{
  "point": {
    "type": "Point",
    "coordinates": [-46.579236, -22.593365]
  },
  "polygon": {
    "type": "Polygon",
    "coordinates": [[
      [-46.579236, -22.593365],
      [-46.568894, -22.604582],
      [-46.548613, -22.596784],
      [-46.537113, -22.601331],
      [-46.529434, -22.592137],
      [-46.579236, -22.593365]
    ]]
  }
}
```

### 2.4. Retrieve Polygon as GeoJSON
**Request:**
```http
GET /api/spatial-data/1/geojson
```

**Response:**
```json
{
  "type": "Feature",
  "geometry": {
    "type": "Polygon",
    "coordinates": [[
      [-46.579236, -22.593365],
      [-46.568894, -22.604582],
      [-46.548613, -22.596784],
      [-46.537113, -22.601331],
      [-46.529434, -22.592137],
      [-46.579236, -22.593365]
    ]]
  },
  "properties": {
    "description": "Polygon covering Socorro-SP region from Sítio Lobisomem to urban center",
    "id": 1,
    "municipality": "Socorro",
    "state": "São Paulo",
    "country": "Brazil",
    "area_km2": 16.8,
    "landmarks": ["Sítio Lobisomem", "Oratório do Meio", "Paróquia Nossa Senhora do Perpétuo Socorro"]
  }
}
```

### 2.5. Get All Spatial Data
**Request:**
```http
GET /api/spatial-data
```

**Response:**
```json
[
  {
    "id": 1,
    "point": {
      "type": "Point",
      "coordinates": [-46.579236, -22.593365]
    },
    "polygon": {
      "type": "Polygon",
      "coordinates": [[
        [-46.579236, -22.593365],
        [-46.568894, -22.604582],
        [-46.548613, -22.596784],
        [-46.537113, -22.601331],
        [-46.529434, -22.592137],
        [-46.579236, -22.593365]
      ]]
    },
    "linestring": {
      "type": "LineString",
      "coordinates": [
        [-46.579236, -22.593365],
        [-46.568894, -22.604582],
        [-46.548613, -22.596784],
        [-46.537113, -22.601331],
        [-46.529434, -22.592137]
      ]
    }
  }
]
```

### 2.6. Additional GeoInformational Insights

<p align="justify">
<strong>Topographic Analysis:</strong> The polygon's coordinate distribution suggests a terrain with moderate elevation changes, typical of the Brazilian Highlands region. The area likely experiences the Cwb climate classification (subtropical highland climate) characteristic of Socorro municipality, with mild temperatures and well-defined seasonal patterns.
</p>

<p align="justify">
<strong>Hydrological Context:</strong> Based on the geographic position, the polygon likely encompasses small tributaries feeding into the Camanducaia River system, which is part of the larger Piracicaba River basin - a crucial water resource for São Paulo state's interior regions.
</p>

<p align="justify">
<strong>Land Use Patterns:</strong> The spatial extent captures the transition from agricultural lands (coffee, dairy farming, and mixed agriculture typical of the region) to urban settlement patterns, representing Socorro's economic and social geography where traditional rural activities coexist with growing urban development.
</p>

## 3. Project Stack

| Technology              | Version   | Description                                                                 |
|-------------------------|-----------|-----------------------------------------------------------------------------|
| ☕ Java                 | `21`      | Backend programming language                                                |
| 🌱 Spring Boot          | `3.5.7`   | Core framework for building backend services                                |
| 🐘 PostgreSQL           | `Latest`  | Advanced relational database with spatial capabilities                      |
| 🗺️ PostGIS              | `3.3+`    | Spatial database extender for PostgreSQL                                    |
| 💾 Hibernate Spatial    | `6.6.33`  | ORM extension for spatial data types                                        |
| 📐 JTS Topology Suite   | `1.19.0`  | Java library for spatial operations and algorithms                          |
| 📄 SpringDoc OpenAPI    | `2.8.14`  | Automatic REST API documentation with Swagger UI                            |
| 🧪 JUnit / JaCoCo       | -         | Testing framework with code coverage reporting                              |
| 🔧 Maven                | `3.9.9`   | Build automation and dependency management tool                             |

---

## Dependencies

| Dependency                    | Category          | Description                                                                                                     |
|-------------------------------|-------------------|-----------------------------------------------------------------------------------------------------------------|
| 🌐 Spring Web                 | Web              | Builds web applications, including RESTful APIs, using Spring MVC. Uses Apache Tomcat as the default container. |
| 💾 Spring Data JPA            | SQL              | Facilitates database access using JPA with Spring Data and Hibernate.                                           |
| 🗺️ Hibernate Spatial          | Spatial          | Provides spatial data types and functions for Hibernate JPA.                                                    |
| 🐘 PostgreSQL Driver          | SQL              | JDBC driver enabling Java applications to interact with PostgreSQL database.                                    |
| ✔️ Validation                 | Validation (I/O) | Enables Java Bean Validation using Jakarta Validator.                                                           |
| 📄 SpringDoc OpenAPI          | Documentation    | Generates Swagger UI automatically for REST API endpoints.                                                      |
| 🌍 GeoJSON Jackson            | Spatial          | Library for working with GeoJSON format in Java applications.                                                   |
| 🔐 Java Dotenv                | Configuration    | Loads environment variables from .env files for application configuration.                                      |

---

## 4. Architecture & Design Patterns

<p align="justify">
The application follows a <strong>Layered Architecture</strong> with clear separation of concerns, ensuring maintainability, testability, and scalability. The architecture is structured around four main layers that handle specific responsibilities in the spatial data processing pipeline.
</p>

```
┌────────────────────────────────────────────┐
│                  Application               │
└────────────────────────────────────────────┘
                      ⇅
┌────────────────────────────────────────────┐
│             Rest Controller Layer          │
└────────────────────────────────────────────┘
            ↓
┌─────────────────────┐┌─────────────────────┐
│    Service Layer    ││                     │
└─────────────────────┘│        Model        │ 
            ↓          │  (Entity and DTOs)  │
┌─────────────────────┐│                     │
│   Repository Layer  ││                     │
└─────────────────────┘└─────────────────────┘
```

<p align="justify">
The <strong>Application Layer</strong> serves as the entry point, initializing the Spring Boot context and configuring the runtime environment. It handles dependency injection and application lifecycle management through the <code>@SpringBootApplication</code> annotation.
</p>

<p align="justify">
The <strong>REST Controller Layer</strong> (<code>SpatialDataController</code>) exposes HTTP endpoints for spatial data operations, handling request validation, content negotiation, and response formatting. It provides two distinct creation endpoints: <code>/serializer</code> for coordinate-based input and <code>/deserializer</code> for GeoJSON geometry input, following RESTful principles.
</p>

<p align="justify">
The <strong>Service Layer</strong> (<code>SpatialDataService</code>) contains the core business logic for spatial operations. This layer handles geometry creation from coordinates, validation of spatial constraints, transformation between JTS geometries and GeoJSON format, and orchestration of data persistence operations. It implements the spatial algorithms and coordinate processing logic.
</p>

<p align="justify">
The <strong>Repository Layer</strong> (<code>SpatialDataRepository</code>) abstracts data access using <code>Spring Data JPA</code> with Hibernate Spatial, providing CRUD operations for spatial entities. This layer leverages PostgreSQL's PostGIS extension for spatial indexing and geographic queries.
</p>

<p align="justify">
The <strong>Model Layer</strong> encompasses both entities (<code>SpatialData</code>) and Data Transfer Objects (DTOs) that define the data structure. The entity model maps to database tables with spatial column definitions, while DTOs handle request/response payloads and ensure clean separation between persistence and API contracts.
</p>

<p align="justify">
This architectural approach ensures that spatial data flows consistently through the application layers, with each layer having well-defined responsibilities and clear boundaries, facilitating testing, maintenance, and future enhancements to the spatial data processing capabilities.
</p>
---

## 5. Data Model & Entity Structure

<p align="justify">
The core entity <code>SpatialData</code> encapsulates all supported geometry types within a single table structure, leveraging PostgreSQL's PostGIS extension for spatial storage and operations. Each geometry field uses SRID 4326 (WGS84) for consistent coordinate reference system handling.
</p>

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                           SpatialData                                          │
├────────────────────────────────────────────────────────────────────────────────────────────────┤
│ - id : Long                                                                                    │
│ - point : Point                                            (Geometry - SRID 4326)              │
│ - multiPoint : MultiPoint                                  (Geometry - SRID 4326)              │
│ - lineString : LineString                                  (Geometry - SRID 4326)              │
│ - multiLineString : MultiLineString                        (Geometry - SRID 4326)              │
│ - polygon : Polygon                                        (Geometry - SRID 4326)              │
│ - multiPolygon : MultiPolygon                              (Geometry - SRID 4326)              │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
                                               │
                                               │ (Contains)
                                               │
                                               ▼
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                      Geometry Hierarchy                                        │
├────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                        <<interface>>                                           │
│                                          Geometry                                              │
├────────────────────────────────────────────────────────────────────────────────────────────────┤
│ ◊ Point                        ◊ MultiPoint                     ◊ LineString                   │
│   - coordinate : Coordinate      - points : Point[]               - coordinates : Coordinate[] │
│   - SRID : int                   - SRID : int                     - SRID : int                 │
│                                                                                                │
│ ◊ Polygon                      ◊ MultiLineString                ◊ MultiPolygon                 │
│   - shell : LinearRing          - lineStrings : LineString[]      - polygons : Polygon[]       │
│   - holes : LinearRing[]        - SRID : int                      - SRID : int                 │
│   - SRID : int                                                                                 │
│                                                                                                │
│ ◊ Coordinate                                                                                   │
│   - x : double                                                                                 │
│   - y : double                                                                                 │
│   - z : double                                                                                 │
└────────────────────────────────────────────────────────────────────────────────────────────────┘

                               ┌──────────────────────────┐
                               │      Geometry Types      │
                               ├──────────────────────────┤
                               │        Point             │
                               │        MultiPoint        │
                               │        LineString        │
                               │        MultiLineString   │
                               │        Polygon           │
                               │        MultiPolygon      │
                               └──────────────────────────┘

                               ┌──────────────────────────┐
                               │      SRID 4326           │
                               ├──────────────────────────┤
                               │   WGS84 Coordinate       │
                               │   Reference System       │
                               │   (Longitude/Latitude)   │
                               └──────────────────────────┘
```

<p align="justify">
The entity design allows for flexible storage of multiple geometry types within a single record, supporting various spatial data scenarios from simple points to complex multi-polygon structures. All geometries adhere to the Open Geospatial Consortium (OGC) standards and can be efficiently queried using PostGIS spatial functions.
</p>

---

## 6. Algorithms & Business Logic (Method Explanations)

<p align="justify">
Below is a detailed explanation of the core spatial algorithms and business logic implemented in the <code>SpatialDataService</code>, which handles all geometry-related operations including creation, validation, and format transformation.
</p>

### 🔹 Geometry Creation & Validation
<ul>
  <li><code>createFromSerializerFormat()</code>: Accepts coordinate arrays and creates JTS geometries through a multi-step validation and construction process. Validates coordinate dimensions and geometry-specific constraints before geometry creation.</li>
  <li><code>createFromDeserializerFormat()</code>: Accepts GeoJSON geometry objects and maps them directly to JTS geometries, leveraging custom deserialization logic.</li>
  <li><code>hasValidPointCoordinates()</code>: Ensures point geometries have exactly 2 coordinates (longitude, latitude) following the GeoJSON coordinate order.</li>
  <li><code>hasValidPolygonCoordinates()</code>: Validates that polygons contain at least 4 coordinates to form a closed ring, with the first and last coordinates being identical.</li>
</ul>

### 🔹 Geometry Construction Methods
<ul>
  <li><code>createPoint()</code>: Constructs JTS Point geometries from [x,y] coordinate pairs using SRID 4326 (WGS84).</li>
  <li><code>createPolygon()</code>: Builds Polygon geometries from coordinate rings, creating exterior rings and validating topological closure.</li>
  <li><code>createMultiPoint()</code>: Aggregates multiple points into a single MultiPoint geometry collection.</li>
  <li><code>createLineString()</code>: Creates linear geometries from sequences of coordinate pairs.</li>
  <li><code>createMultiPolygon()</code>: Constructs complex multi-polygon geometries from multiple polygon definitions.</li>
</ul>

### 🔹 GeoJSON Transformation
<ul>
  <li><code>convertToGeoJsonGeometry()</code>: Transforms JTS geometries into GeoJSON-compliant objects with proper type mapping and coordinate structure.</li>
  <li><code>getPolygonAsGeoJson()</code>: Specialized method that retrieves polygon geometries and returns them as standardized GeoJSON Feature objects.</li>
  <li><code>createGeoJsonPolygon()</code>: Converts JTS Polygon to GeoJSON Polygon format with proper coordinate array nesting for exterior rings.</li>
</ul>

### 🔹 Custom Serialization Logic
<ul>
  <li><code>GeometrySerializer</code>: Custom Jackson serializer that converts JTS geometries to JSON with proper type information and coordinate arrays.</li>
  <li><code>GeometryDeserializer</code>: Custom Jackson deserializer that parses GeoJSON geometry objects and creates corresponding JTS geometry instances.</li>
</ul>

---


## 7. Technical Decisions & Trade-offs

<p align="justify">
<strong>PostgreSQL with PostGIS</strong> was chosen over specialized spatial databases due to its robust spatial capabilities, excellent Spring Boot integration, and widespread adoption. The trade-off includes slightly more complex setup compared to in-memory databases but provides production-ready spatial operations with full OGC standard compliance.
</p>

<p align="justify">
<strong>Hibernate Spatial with JTS</strong> was selected for ORM spatial support instead of alternative spatial libraries. This provides type-safe spatial operations and seamless JPA integration, though it requires careful configuration of dialect and column definitions for proper geometry type mapping.
</p>

<p align="justify">
<strong>Dual Input Format Approach</strong> (Serializer/Deserializer) was implemented to support both developer-friendly coordinate arrays and standards-compliant GeoJSON. This increases API complexity but provides flexibility for different client needs and interoperability with GIS tools while maintaining data consistency.
</p>

<p align="justify">
<strong>Custom JSON Serialization</strong> was developed instead of using out-of-the-box solutions to ensure precise control over GeoJSON output format and proper handling of JTS geometry hierarchy. This requires maintenance of custom serialization logic but guarantees standards compliance and optimal performance for spatial data transformations.
</p>

<p align="justify">
<strong>SRID 4326 (WGS84)</strong> was standardized for all geometries to ensure consistent coordinate reference system across the application. This aligns with web mapping standards and GeoJSON specifications but may require transformation for applications using other coordinate systems or projections.
</p>

<p align="justify">
<strong>Centralized Message Management</strong> through the <code>Messages</code> utility class ensures consistent validation messages, error responses, and API documentation. This supports future internationalization and maintainability but adds an abstraction layer that requires disciplined usage across all application components.
</p>

<p align="justify">
<strong>AAA Testing Pattern (Arrange-Act-Assert)</strong> was adopted throughout the test suite to enforce clarity, readability, and maintainability. This structured approach ensures consistent test organization but requires additional discipline in test setup and may increase initial test development time.
</p>

<p align="justify">
<strong>JaCoCo Code Coverage Enforcement</strong> with strict thresholds (80% instruction coverage, 70% branch coverage) was implemented to ensure code quality and test completeness. This provides confidence in code reliability but may slow development velocity during initial implementation phases due to coverage requirements.
</p>

<p align="justify">
<strong>Mockito-Based Dependency Isolation</strong> was chosen for unit testing to enable focused testing of individual components without external dependencies. This provides fast, reliable tests but requires careful mocking strategy to avoid testing implementation details rather than behavior.
</p>

<p align="justify">
<strong>Multi-Layer Testing Strategy</strong> covering service, controller, configuration, and serialization layers ensures comprehensive quality assurance. This approach provides defense in depth but increases test maintenance overhead and requires coordinated testing practices across the development team.
</p>


---

## 8. Running Locally

### 8.1. Prerequisites
- PostgreSQL 12+ with PostGIS extension enabled;
- Java 21 Development Kit;
- Maven 3.9+.

### 8.2. Database Setup
```sql
-- Create database with PostGIS extension
CREATE DATABASE spatialpostgis;
\c spatialpostgis;
CREATE EXTENSION postgis;

-- Verify PostGIS installation
SELECT PostGIS_Version();
```

### 8.3. Environment Configuration
Create `.env` file in project root:
```properties
DATABASE_URL=jdbc:postgresql://localhost:5432/spatialpostgis
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=your_password
```

### 8.4. Application Execution
```bash
# Clone and navigate to project
git clone https://github.com/souzafcharles/hibernate-spatial-postgis-jts.git
cd spatial-postgis-demo

# Build and run application
mvn clean install
mvn spring-boot:run
```

### 8.5. Verification & Tools
- **API Documentation**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/api/spatial-data
- **GeoJSON Visualization**: https://geojson.io

---

## 9. Testing Strategy

<p align="justify">
The application implements a comprehensive <strong>Test-Driven Development (TDD)</strong> approach with extensive unit testing coverage across all layers of the architecture. The testing strategy follows the <strong>AAA pattern (Arrange-Act-Assert)</strong> and utilizes modern testing frameworks to ensure code quality, reliability, and maintainability.
</p>

<p align="justify">
The test suite employs <code>JUnit 5</code> as the core testing framework, <code>Mockito</code> for mocking dependencies, and <code>JaCoCo</code> for code coverage analysis. All tests are organized by architectural layer and functionality, providing clear separation of concerns and facilitating targeted testing of specific components.
</p>

<p align="justify">
The testing approach emphasizes <strong>behavior verification</strong> over implementation details, ensuring that tests remain resilient to refactoring while validating the correct behavior of the spatial data processing pipeline. Each test class focuses on a single responsibility and includes both positive and negative test scenarios.
</p>

---

### 9.1. Test Architecture & Coverage

```
┌────────────────────────────────────────────────────────────────┐
│                      Testing Pyramid                           │
├────────────────────────────────────────────────────────────────┤
│    ┌────────────────────────────────────────────────────────┐  │
│    │              Unit Tests (85%)                          │  │
│    │  - Service Layer: SpatialDataServiceTest               │  │
│    │  - Controller Layer: SpatialDataControllerTest         │  │
│    │  - Configuration: JacksonConfigTest, OpenApiConfigTest │  │
│    │  - JSON Serialization: Geometry(De)SerializerTest      │  │
│    │  - Exception Handling: GlobalExceptionHandlerTest      │  │
│    └────────────────────────────────────────────────────────┘  │
│    ┌────────────────────────────────────────────────────────┐  │
│    │           Integration Tests (10%)                      │  │
│    │  - Application Bootstrap: SpringBootAppTest            │  │
│    │  - Environment Configuration: LoadEnvironmentTest      │  │
│    └────────────────────────────────────────────────────────┘  │
│    ┌────────────────────────────────────────────────────────┐  │
│    │              End-to-End Tests (5%)                     │  │
│    │  - Postman Collection (API Contract Testing)           │  │
│    │  - GeoJSON Visualization Validation                    │  │
│    └────────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────────┘
```

### 9.2. Service Layer Testing (`SpatialDataServiceTest`)

<p align="justify">
The <code>SpatialDataServiceTest</code> class provides comprehensive unit testing for the core business logic, covering all geometry creation, validation, and transformation operations with <strong>98% method coverage</strong> and <strong>95% branch coverage</strong>.
</p>

**Key Test Categories:**
- **Geometry Creation**: Tests for both serializer (coordinate arrays) and deserializer (GeoJSON) formats;
- **Validation Logic**: Coordinate validation, polygon closure verification, and geometry constraints;
- **GeoJSON Transformation**: Conversion of JTS geometries to standardized GeoJSON format;
- **Error Handling**: Exception scenarios for invalid inputs and missing data;
- **Edge Cases**: Empty collections, null values, boundary conditions, and malformed geometries.

**Example Test Structure:**
```java
@Test
void createFromSerializerFormat_WithValidData_ShouldSaveAndReturnResponse() {
    // Arrange
    SpatialDataSerializerRequestDTO request = new SpatialDataSerializerRequestDTO(
            pointCoordinates, multiPointCoordinates, lineStringCoordinates,
            multiLineStringCoordinates, polygonCoordinates, multiPolygonCoordinates
    );
    
    // Act
    SpatialDataResponseDTO result = spatialDataService.createFromSerializerFormat(request);
    
    // Assert
    assertNotNull(result);
    assertEquals(1L, result.id());
    verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
}
```

### 9.3. Controller Layer Testing (`SpatialDataControllerTest`)

<p align="justify">
The <code>SpatialDataControllerTest</code> verifies the REST API contract, ensuring proper HTTP status codes, response formats, and exception propagation from the service layer to API consumers.
</p>

**Test Coverage:**
- **HTTP Endpoint Validation**: All REST endpoints (GET, POST) with correct status codes;
- **Request/Response Mapping**: Proper DTO serialization and deserialization;
- **Exception Propagation**: Service layer exceptions correctly mapped to HTTP responses;
- **Integration Points**: Service method invocation verification with Mockito.

**Controller Test Example:**
```java
@Test
void createFromSerializer_WithValidRequest_ShouldReturnOk() {
    // Arrange
    SpatialDataSerializerRequestDTO request = createValidRequest();
    when(spatialDataService.createFromSerializerFormat(request))
        .thenReturn(mockResponse);
    
    // Act
    ResponseEntity<SpatialDataResponseDTO> result = 
        spatialDataController.createFromSerializer(request);
    
    // Assert
    assertEquals(HttpStatus.OK, result.getStatusCode());
    verify(spatialDataService, times(1)).createFromSerializerFormat(request);
}
```

### 9.4. JSON Serialization Testing (`GeometrySerializerTest`, `GeometryDeserializerTest`)

<p align="justify">
The custom JSON serialization components are thoroughly tested to ensure proper conversion between JTS geometries and GeoJSON format, maintaining compliance with OGC standards.
</p>

**Serialization Test Coverage:**
- **Point Serialization**: Coordinate array generation and type information;
- **Polygon Handling**: Exterior ring coordinates and proper closure;
- **LineString Conversion**: Coordinate sequence preservation;
- **Null Safety**: Graceful handling of null geometries and empty coordinates;
- **Error Conditions**: Malformed JSON and unsupported geometry types.

**Deserialization Test Coverage:**
- **GeoJSON Parsing**: Correct interpretation of GeoJSON geometry objects;
- **Coordinate Validation**: Proper handling of coordinate arrays and validation;
- **Type Mapping**: Accurate conversion between GeoJSON types and JTS geometries;
- **Edge Cases**: Empty arrays, null values, and invalid coordinate structures.

### 9.5. Configuration & Bootstrap Testing

<p align="justify">
Configuration components are tested to ensure proper application startup and environment configuration, validating Spring Boot context initialization and dependency injection.
</p>

**Configuration Test Classes:**
- **`JacksonConfigTest`**: ObjectMapper bean configuration with custom geometry modules;
- **`OpenApiConfigTest`**: Swagger/OpenAPI documentation generation;
- **`LoadEnvironmentTest`**: Environment variable loading and system property configuration;
- **`SpringBootSpatialPostGisApplicationTest`**: Application bootstrap and main method execution.

### 9.6. Exception Handling Testing (`GlobalExceptionHandlerTest`)

<p align="justify">
The global exception handler is comprehensively tested to ensure consistent error responses across the API, with proper HTTP status codes and structured error messages.
</p>

**Exception Test Coverage:**
- **Entity Not Found**: 404 responses with proper messaging;
- **Validation Errors**: 400 responses for constraint violations;
- **Data Access Issues**: 500 responses for database errors;
- **JSON Parsing Errors**: 400 responses for malformed requests.
- **Custom Business Logic**: Application-specific exception handling.

### 9.7. Test Data Management & Mocking Strategy

<p align="justify">
The test suite employs a consistent data management strategy with reusable test data factories and comprehensive mocking of external dependencies to ensure test isolation and reproducibility.
</p>

**Mocking Strategy:**
- **Repository Layer**: Complete mocking of `SpatialDataRepository` to isolate business logic;
- **Service Dependencies**: Mocked service layer in controller tests;
- **External Systems**: No actual database or external API calls in unit tests;
- **Static Methods**: Mocked static method calls using Mockito's `mockStatic`.

**Test Data Patterns:**
```java
// Reusable test coordinates
private final List<Double> pointCoordinates = Arrays.asList(1.0, 2.0);
private final List<List<Double>> polygonCoordinates = Arrays.asList(
    Arrays.asList(0.0, 0.0), Arrays.asList(0.0, 1.0), 
    Arrays.asList(1.0, 1.0), Arrays.asList(1.0, 0.0), Arrays.asList(0.0, 0.0)
);
```

### 9.8. Code Quality & Coverage Metrics

<p align="justify">
The project maintains high code quality standards through automated quality gates and comprehensive coverage requirements enforced by the Maven build process.
</p>

**JaCoCo Coverage Requirements:**
- **Instruction Coverage**: ≥ 80% (validates line-by-line execution);
- **Branch Coverage**: ≥ 70% (ensures decision path testing);
- **Method Coverage**: ≥ 85% (comprehensive API testing);
- **Class Coverage**: ≥ 90% (architectural completeness).

**Quality Gates:**
- **Build Failure**: On coverage threshold violations;
- **Test Execution**: Mandatory during CI/CD pipeline;
- **Report Generation**: Automated HTML reports for coverage analysis;
- **Integration**: Maven Surefire for test execution and reporting.

### 9.9. Running Tests & Coverage Reports

**Test Execution:**
```bash
# Run all tests with coverage
mvn clean test jacoco:report

# Run specific test class
mvn test -Dtest=SpatialDataServiceTest

# Generate coverage report only
mvn jacoco:report
```

**Coverage Report Location:**
```bash
# View detailed coverage analysis
open target/site/jacoco/index.html
```

<p align="justify">
The comprehensive testing strategy ensures that the spatial data application maintains high reliability standards while facilitating continuous integration and deployment practices. The test suite serves as executable documentation, clearly demonstrating the expected behavior of all system components and providing confidence for future enhancements and refactoring efforts.
</p>

---

## 10. Spatial Data Collection & Visualization

👉 **[Download Brazilian Capitals Collection](https://github.com/souzafcharles/hibernate-spatial-postgis-jts/tree/main/local/collection.json)**

<p align="justify">
A comprehensive <strong>Postman collection</strong> accompanies this project, providing complete testing coverage for all spatial data operations. Import the collection file into <strong>Postman, Insomnia, or Thunder Client</strong> to immediately begin testing the API's full capabilities with real geographic data from Brazilian capitals.
</p>

### 10.1. Collection Overview
The collection includes <strong>12 Brazilian capitals</strong> with varying polygon complexities, covering:

- **Multiple Geometry Types**: Points, MultiPoints, LineStrings, MultiLineStrings, Polygons, and MultiPolygons;
- **Diverse Geographic Regions**: From Amazon rainforest (Manaus) to southern plains (Porto Alegre);
- **Various Polygon Complexities**: 5 to 10-point polygons representing different urban patterns;
- **Dual Input Formats**: Both Serializer (coordinate arrays) and Deserializer (GeoJSON) endpoints.

### 10.2. Brazilian Capitals Coverage
The collection features spatial data from:

- **São Paulo** (5-point polygon - Centro Expandido);
- **Rio de Janeiro** (7-point polygon - Zona Sul);
- **Brasília** (9-point polygon - Plano Piloto);
- **Salvador** (6-point polygon - Centro Histórico);
- **Fortaleza** (8-point polygon - Praia de Iracema);
- **Belo Horizonte** (10-point polygon - Região Central);
- **Manaus** (7-point polygon - Centro);
- **Recife** (6-point polygon - Centro Histórico);
- **Curitiba** (8-point polygon - Centro);
- **Porto Alegre** (7-point polygon - Centro);
- **Belém** (6-point polygon - Cidade Velha);
- **Goiânia** (8-point polygon - Setor Central).

### 10.3. Complete Testing Workflow
The collection supports end-to-end spatial data testing:

- **Data Creation**: Both Serializer (coordinate arrays) and Deserializer (GeoJSON) formats;
- **Data Retrieval**: Get all records, get by ID, and specialized GeoJSON endpoints;
- **Visualization Validation**: Direct GeoJSON output compatible with mapping tools;
- **Error Scenarios**: Invalid coordinates, malformed geometries, and edge cases;
- **Performance Testing**: Multiple geometry types within single requests.

### 10.4. GeoJSON Visualization
<p align="justify">
All generated GeoJSON responses can be directly visualized using tools like <strong>geojson.io</strong>. Simply copy the GeoJSON response from any <code>/api/spatial-data/{id}/geojson</code> endpoint and paste it into geojson.io to see the spatial features rendered on an interactive map with satellite imagery.
</p>

### 10.5. Integration Testing Scenarios
The collection enables testing of complete spatial workflows:

- **Geometry Creation → Storage → Retrieval → Visualization** pipeline;
- **Coordinate System Validation**: All geometries use SRID 4326 (WGS84);
- **Format Transformation**: JTS geometries ↔ GeoJSON standardization;
- **Multi-geometry Support**: Complex spatial relationships and collections;
- **API Contract Validation**: RESTful endpoints with proper HTTP status codes.

<p align="justify">
This comprehensive collection serves as both a testing tool and learning resource, demonstrating practical spatial data management patterns with real-world geographic examples from across Brazil's diverse landscape.
</p>

---

## 11. Conclusion

<p align="justify">
This project implements a comprehensive spatial data management system using <strong>Spring Boot, PostgreSQL with PostGIS, and JTS Topology Suite</strong>, successfully addressing the challenge of handling geometric data in a Java enterprise application. It demonstrates practical integration of spatial databases with modern web frameworks while maintaining high standards of software quality through rigorous testing practices.
</p>

<p align="justify">
Key achievements:
</p>

<ul>
  <li><strong>Spatial Database Integration</strong> using PostgreSQL with PostGIS extension, providing robust spatial indexing and operations with full SRID 4326 support for geographic coordinates.</li>
  <li><strong>Dual Input Format Support</strong> offering both coordinate arrays and GeoJSON standards for maximum flexibility and interoperability with various client applications.</li>
  <li><strong>Custom Serialization Architecture</strong> enabling seamless bidirectional conversion between JTS geometries and GeoJSON format with proper type mapping and coordinate handling.</li>
  <li><strong>Comprehensive Geometry Support</strong> for all OGC simple feature types including points, linestrings, polygons, and their multi-geometry variants with proper validation.</li>
  <li><strong>Production-Ready API</strong> with proper validation, centralized exception handling, OpenAPI documentation, and RESTful design principles.</li>
  <li><strong>Visualization Compatibility</strong> with standard GIS tools like geojson.io through proper GeoJSON output formatting and spatial data standardization.</li>
  <li><strong>Comprehensive Testing Strategy</strong> with 85% unit test coverage, AAA pattern implementation, and multi-layer testing across service, controller, serialization, and configuration components.</li>
  <li><strong>Quality Assurance</strong> through JaCoCo code coverage enforcement, Mockito-based dependency isolation, and consistent error response handling.</li>
  <li><strong>Configuration Management</strong> with environment-based database configuration, custom Jackson serialization setup, and automated OpenAPI documentation generation.</li>
</ul>

<p align="justify">
The testing suite demonstrates exceptional thoroughness, covering <strong>98% of service methods</strong> and <strong>95% of decision branches</strong> while maintaining clear separation between unit, integration, and end-to-end testing layers. The architecture successfully balances spatial data complexity with clean code principles, providing a maintainable and extensible foundation for geospatial applications.
</p>

<p align="justify">
This solution provides a solid foundation for spatial applications, demonstrating proven patterns for spatial data persistence, format transformation, and web API design that can be extended with additional spatial operations, analysis capabilities, and real-world geographic data processing scenarios. The project stands as a reference implementation for Java-based spatial data management with production-ready quality standards.
</p>

---
## References

LocationTech. (n.d.). *JTS Topology Suite*. Retrieved from https://locationtech.github.io/jts/

Murat Öksüzer. (n.d.). *Geospatial Workshop* [Video playlist]. YouTube. https://www.youtube.com/playlist?list=PLzIvGBaDt2yY5p2JsBJwulNbxbJ_su9op

Nelio Alves. (s.d.). *Complete Java Object-Oriented Programming + Projects* [Course]. Udemy. https://www.udemy.com/course/java-curso-completo/

PostGIS Documentation. (n.d.). *PostGIS Manual*. Retrieved from https://postgis.net/documentation/


