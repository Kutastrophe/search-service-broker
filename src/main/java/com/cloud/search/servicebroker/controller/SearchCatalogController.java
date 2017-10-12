package com.cloud.search.servicebroker.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.servicebroker.model.Catalog;
import org.springframework.cloud.servicebroker.model.Plan;
import org.springframework.cloud.servicebroker.model.ServiceDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Chandrakant Bagade
 * 
 *         This class will provide catalog about service, which provide
 *         information like service name , plans , costing , metadata and other
 *         information
 *
 */
@Configuration
public class SearchCatalogController {

	/**
	 * 
	 * This method serves catalog information for service as service name ,
	 * plans , costing , metadata and other information.
	 * 
	 * path for this method to call is /v2/catalog
	 * 
	 * @return Catalog
	 */

	@Bean
	public Catalog catalog() {

		return new Catalog(
				Collections.singletonList(new ServiceDefinition(
						"search-service-broker", "search-service",
						"A search service broker implementation", true, false,
						Collections.singletonList(new Plan("search-plan",
								"basic", "This is a free basic plan.",
								getPlanMetadata())), Arrays.asList("search",
								"document"), getMetadata(), null, null)));
	}

	private Map<String, Object> getMetadata() {
		Map<String, Object> sdMetadata = new HashMap<>();
		sdMetadata.put("displayName", "Search Service");

		sdMetadata.put("longDescription",
				"A Search Service based on Lucene APIs");
		return sdMetadata;
	}

	private Map<String, Object> getPlanMetadata() {
		Map<String, Object> planMetadata = new HashMap<>();
		planMetadata.put("costs", getCosts());
		return planMetadata;
	}

	private List<Map<String, Object>> getCosts() {
		Map<String, Object> costsMap = new HashMap<>();

		Map<String, Object> amount = new HashMap<>();
		amount.put("INR", 0.0);

		costsMap.put("amount", amount);

		return Collections.singletonList(costsMap);
	}

}