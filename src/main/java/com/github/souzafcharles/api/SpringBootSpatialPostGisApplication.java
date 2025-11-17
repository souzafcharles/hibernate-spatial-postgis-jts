package com.github.souzafcharles.api;

import com.github.souzafcharles.api.config.LoadEnvironment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootSpatialPostGisApplication {

	public static void main(String[] args) {
        LoadEnvironment.loadEnv();
		SpringApplication.run(SpringBootSpatialPostGisApplication.class, args);
	}

}
