package com.etrichmond.driver_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class DriverManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(DriverManagementApplication.class, args);
	}

}
