package com.github.souzafcharles.api.repository;

import com.github.souzafcharles.api.model.entity.SpatialData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpatialDataRepository extends JpaRepository<SpatialData, Long> {

    List<SpatialData> findByPolygonIsNotNull();

    SpatialData findById(long id);
}