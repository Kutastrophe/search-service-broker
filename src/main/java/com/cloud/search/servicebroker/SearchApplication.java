package com.cloud.search.servicebroker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 
 * @author Chandrakant Bagade
 * 
 *         This project is based on Spring Boot framework for service broker and
 *         serves as broker and service on cloud foundry
 * 
 *         This class helps to boot-starp search application, starting Spring
 *         which will in turn start components required for application to run.
 *
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableJpaRepositories
public class SearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchApplication.class, args);
	}

}
