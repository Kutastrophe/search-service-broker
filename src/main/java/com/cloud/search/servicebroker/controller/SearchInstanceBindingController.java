package com.cloud.search.servicebroker.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceBindingDoesNotExistException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceBindingExistsException;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceAppBindingResponse;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingResponse;
import org.springframework.cloud.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.service.ServiceInstanceBindingService;
import org.springframework.stereotype.Service;

import com.cloud.search.servicebroker.model.SearchInstanceBinding;
import com.cloud.search.servicebroker.repository.SearchBindingRepository;

/**
 * @author Chandrakant Bagade
 * 
 * 
 *         This class lists methods to create , delete service instance bindings
 *         to applications. The methods listed will be called by cloud foundry
 *         platform passing proper service instance ids and binding ids
 *
 */

@Service
public class SearchInstanceBindingController implements
		ServiceInstanceBindingService {

	@Autowired
	private SearchBindingRepository bindingRepository;

	@Autowired
	public SearchInstanceBindingController(
			SearchBindingRepository bindingRepository) {
		this.bindingRepository = bindingRepository;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.cloud.servicebroker.service.ServiceInstanceBindingService
	 * #
	 * createServiceInstanceBinding(org.springframework.cloud.servicebroker.model
	 * .CreateServiceInstanceBindingRequest)
	 * 
	 * path PUT
	 * 
	 * /v2/service_instances/{instance_id}/service_bindings/{binding_id}
	 */
	@Override
	public CreateServiceInstanceBindingResponse createServiceInstanceBinding(
			CreateServiceInstanceBindingRequest request) {

		String methodInfo = "createServiceInstanceBinding () :: ";

		String bindingId = request.getBindingId();
		String serviceInstanceId = request.getServiceInstanceId();

		System.out.println(methodInfo + " , bindingId  = " + bindingId
				+ " , serviceInstanceId = " + serviceInstanceId);

		SearchInstanceBinding binding = bindingRepository.findOne(bindingId);
		if (binding != null) {
			throw new ServiceInstanceBindingExistsException(serviceInstanceId,
					bindingId);
		}

		Map<String, Object> credentials = new HashMap<>();

		this.listAppFilesToSetCredentials(credentials,
				new File(System.getProperty("user.dir")));
		credentials.put("uri",
				"http://search-service-broker.bosh-lite.com/search/"
						+ serviceInstanceId);

		binding = new SearchInstanceBinding(bindingId, serviceInstanceId,
				request.getBoundAppGuid());

		bindingRepository.save(binding);

		return new CreateServiceInstanceAppBindingResponse()
				.withCredentials(credentials);

	}

	private void listAppFilesToSetCredentials(Map<String, Object> credentials,
			final File folder) {

		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listAppFilesToSetCredentials(credentials, fileEntry);
			} else {
				if (fileEntry.getName().equals("application.properties")) {

					InputStream inputStream = null;
					try {
						inputStream = new FileInputStream(new File(folder
								+ File.separator + "application.properties"));

						Properties props = new Properties();
						props.load(inputStream);

						credentials.put("user",
								props.getProperty("security.user.name"));
						credentials.put("password",
								props.getProperty("security.user.password"));

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {

						try {
							if (inputStream != null) {
								inputStream.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}

					}

					break;

				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.cloud.servicebroker.service.ServiceInstanceBindingService
	 * #
	 * deleteServiceInstanceBinding(org.springframework.cloud.servicebroker.model
	 * .DeleteServiceInstanceBindingRequest)
	 * 
	 * Path DELETE
	 * /v2/service_instances/{instance_id}/service_bindings/{binding_id}
	 */
	@Override
	public void deleteServiceInstanceBinding(
			DeleteServiceInstanceBindingRequest request) {

		String methodInfo = "deleteServiceInstanceBinding () :: ";

		String bindingId = request.getBindingId();

		System.out.println(methodInfo + "  , bindingId  = " + bindingId);

		SearchInstanceBinding binding = getServiceInstanceBinding(bindingId);

		if (binding == null) {
			throw new ServiceInstanceBindingDoesNotExistException(bindingId);
		}

		bindingRepository.delete(bindingId);
	}

	protected SearchInstanceBinding getServiceInstanceBinding(String id) {
		return bindingRepository.findOne(id);
	}

}
