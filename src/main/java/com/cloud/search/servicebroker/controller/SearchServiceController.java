package com.cloud.search.servicebroker.controller;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloud.search.servicebroker.service.DeleteService;
import com.cloud.search.servicebroker.service.IndexService;
import com.cloud.search.servicebroker.service.SearchService;

/**
 * @author Chandrakant Bagade
 *
 *         This class acts as REST resource for search functionality. Class will
 *         help adding documents for indexing , searching and deleting
 */
@RestController
public class SearchServiceController {

	// autowiring of search related services
	@Autowired
	IndexService indexService;

	@Autowired
	SearchService searchService;

	@Autowired
	DeleteService deleteService;

	/**
	 * add new content
	 * 
	 * @param instanceId
	 *            , service instance id , assigned by cloud foundry while
	 *            service creation
	 * @param content
	 *            , content to store , it could be plain text , JSON Object,
	 *            JSON Array in String format
	 * @return ResponseEntity having a String message and response code
	 */

	@RequestMapping(value = "/search/{instanceId}", method = RequestMethod.PUT)
	public ResponseEntity<String> putContent(
			@PathVariable("instanceId") String instanceId,
			@RequestBody String content) {

		String methodInfo = "putContent() :: ";

		System.out.println(methodInfo + " , instanceId " + instanceId
				+ " content , " + content);

		if (content == null || content.trim().equals("")) {
			return new ResponseEntity<>("No content is passed to store",
					HttpStatus.BAD_REQUEST);
		}

		try {

			indexService.indexContent(instanceId, content);

		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					"There is problem with content update.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		System.out.println(methodInfo + " , returning ");
		return new ResponseEntity<>("The content is updted", HttpStatus.CREATED);

	}

	/**
	 * get indexed content matching - content passed in method body and key
	 * passed in path (fetch content based on key value pair)
	 * 
	 * @param instanceId
	 *            , service instance id , assigned by cloud foundry while
	 *            service creation
	 * 
	 * @param key
	 *            , key to match, key is optional
	 * @param content
	 *            , content to match
	 * 
	 * @return ResponseEntity having a String message and response code
	 */

	@RequestMapping("/search/{instanceId}")
	public ResponseEntity<String> getContent(
			@PathVariable("instanceId") String instanceId,
			@RequestParam(value = "key", required = false) String key,
			@RequestParam String content) {

		String methodInfo = "getContent() :: ";

		System.out.println(methodInfo + " , instanceId " + instanceId
				+ " content , " + content + " , key " + key);

		if (content == null || content.trim().equals("")) {
			return new ResponseEntity<>("No content is passed to match with",
					HttpStatus.BAD_REQUEST);
		}

		String regex = "\\d+";
		List<String> result = null;

		try {

			if (key == null || key.trim().equals("")) {
				result = searchService.search(instanceId, content);
			} else {

				if ("true".equals(content) || "false".equals(content)) {
					result = searchService.search(instanceId, key, new Boolean(
							content).toString());
				} else if (content.matches(regex)) {
					result = searchService.search(instanceId, key, new Long(
							content));
				} else {
					result = searchService.search(instanceId, key, content);
				}
			}

			if (result == null || result.isEmpty()) {
				return new ResponseEntity<>(
						"There is no result found for provided criteria",
						HttpStatus.OK);
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					"There is problem with getting content.",
					HttpStatus.INTERNAL_SERVER_ERROR);

		}
		System.out.println(methodInfo + " returning ");
		return new ResponseEntity<>(result.toString(), HttpStatus.OK);

	}

	/**
	 * delete documents matching - content passed in method body and key passed
	 * in path (fetch content based on key value pair)
	 * 
	 * @param instanceId
	 *            , service instance id , assigned by cloud foundry while
	 *            service creation
	 * 
	 * @param key
	 *            , key to match , key is optional
	 * @param content
	 *            , content to match
	 * 
	 * @return ResponseEntity having a String message and response code
	 */

	@RequestMapping(value = "/search/{instanceId}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteContent(
			@PathVariable("instanceId") String instanceId,
			@RequestParam(value = "key", required = false) String key,
			@RequestParam String content) {

		String methodInfo = "deleteContent() :: ";

		System.out.println(methodInfo + " , instanceId " + instanceId
				+ " content , " + content + " , key " + key);

		if (content == null || content.trim().equals("")) {
			return new ResponseEntity<>(
					"No content is passed to match with for deletion",
					HttpStatus.BAD_REQUEST);
		}

		String regex = "\\d+";
		try {

			if (key == null || key.trim().equals("")) {
				deleteService.delete(instanceId, content);
			} else {

				if ("true".equals(content) || "false".equals(content)) {
					deleteService.delete(instanceId, key,
							new Boolean(content).toString());
				} else if (content.matches(regex)) {
					deleteService.delete(instanceId, key, new Long(content));
				} else {
					deleteService.delete(instanceId, key, content);
				}
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					"There is problem with deleting content.",
					HttpStatus.INTERNAL_SERVER_ERROR);

		}
		System.out.println(methodInfo + " returning ");
		return new ResponseEntity<>("The matching content is deleted",
				HttpStatus.OK);
	}

}
