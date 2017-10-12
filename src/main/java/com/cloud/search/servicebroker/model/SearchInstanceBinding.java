package com.cloud.search.servicebroker.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 
 * @author Chandrakant Bagade
 * 
 *         Binding class for search instance
 * 
 */
@Entity
public class SearchInstanceBinding {

	@Id
	private String id;
	private String serviceInstanceId;
	private String appGuid;

	public SearchInstanceBinding() {

	}

	public SearchInstanceBinding(String id, String serviceInstanceId,
			String appGuid) {
		this.id = id;
		this.serviceInstanceId = serviceInstanceId;
		this.appGuid = appGuid;
	}

	public String getId() {
		return id;
	}

	public String getServiceInstanceId() {
		return serviceInstanceId;
	}

	public String getAppGuid() {
		return appGuid;
	}

	@Override
	public String toString() {
		return "ServiceInstanceBinding [id=" + id + ", serviceInstanceId="
				+ serviceInstanceId + ", appGuid=" + appGuid + "]";
	}

}