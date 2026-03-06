package com.vanegas.angela.franquicias_api.infrastruture.config;

import org.springframework.context.annotation.Configuration;
@Configuration
public class AppConfig {
    
    private String endpoint;

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
    
    

 
}
