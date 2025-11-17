package com.github.souzafcharles.spatial_postgis_demo;

import com.github.souzafcharles.spatial_postgis_demo.config.LoadEnvironment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootSpatialPostGisApplication {

	public static void main(String[] args) {
        LoadEnvironment.loadEnv();
		SpringApplication.run(SpringBootSpatialPostGisApplication.class, args);
	}

}
