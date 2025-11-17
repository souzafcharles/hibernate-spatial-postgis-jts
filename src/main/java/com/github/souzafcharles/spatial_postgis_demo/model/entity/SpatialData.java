package com.github.souzafcharles.spatial_postgis_demo.model.entity;

import jakarta.persistence.*;
import org.locationtech.jts.geom.*;

import java.io.Serializable;

@Entity
@Table(name = "spatialdata")
public class SpatialData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "spatial_seq_gen")
    @SequenceGenerator(name = "spatial_seq_gen", sequenceName = "spatial_seq", allocationSize = 1)
    private Long id;

    @Column(name = "point", columnDefinition = "geometry(Point, 4326)")
    private Point point;

    @Column(name = "multipoint", columnDefinition = "geometry(MultiPoint, 4326)")
    private MultiPoint multiPoint;

    @Column(name = "linestring", columnDefinition = "geometry(LineString, 4326)")
    private LineString lineString;

    @Column(name = "multilinestring", columnDefinition = "geometry(MultiLineString, 4326)")
    private MultiLineString multiLineString;

    @Column(name = "polygon", columnDefinition = "geometry(Polygon, 4326)")
    private Polygon polygon;

    @Column(name = "multipolygon", columnDefinition = "geometry(MultiPolygon, 4326)")
    private MultiPolygon multiPolygon;

    @Column(name = "geometrycollection", columnDefinition = "geometry(GeometryCollection, 4326)")
    private GeometryCollection geometryCollection;

    public SpatialData() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public MultiPoint getMultiPoint() {
        return multiPoint;
    }

    public void setMultiPoint(MultiPoint multiPoint) {
        this.multiPoint = multiPoint;
    }

    public LineString getLineString() {
        return lineString;
    }

    public void setLineString(LineString lineString) {
        this.lineString = lineString;
    }

    public MultiLineString getMultiLineString() {
        return multiLineString;
    }

    public void setMultiLineString(MultiLineString multiLineString) {
        this.multiLineString = multiLineString;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public MultiPolygon getMultiPolygon() {
        return multiPolygon;
    }

    public void setMultiPolygon(MultiPolygon multiPolygon) {
        this.multiPolygon = multiPolygon;
    }

    public GeometryCollection getGeometryCollection() {
        return geometryCollection;
    }

    public void setGeometryCollection(GeometryCollection geometryCollection) {
        this.geometryCollection = geometryCollection;
    }
}
