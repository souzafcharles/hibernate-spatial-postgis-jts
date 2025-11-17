package com.github.souzafcharles.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.souzafcharles.api.json.GeometryDeserializer;
import com.github.souzafcharles.api.json.GeometrySerializer;
import org.locationtech.jts.geom.Geometry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        // Register custom serializer and deserializer for Geometry types
        module.addSerializer(Geometry.class, new GeometrySerializer());
        module.addDeserializer(Geometry.class, new GeometryDeserializer());
        mapper.registerModule(module);
        return mapper;
    }
}
