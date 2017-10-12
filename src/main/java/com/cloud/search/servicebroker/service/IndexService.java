package com.cloud.search.servicebroker.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import com.cloud.search.servicebroker.entity.ServiceConstants;

/**
 * @author Chandrakant Bagade
 *
 *         This class will index and store documents. The class uses lucene
 *         search APIs to store and index documents
 */
@Service
public class IndexService extends BaseService {

	/**
	 * index the content. It analyze content as String , JSON Object , JSON
	 * Array and index it.
	 * 
	 * @param instanceId
	 *            , service instance id against which content to index
	 * @param content
	 *            content to index
	 * @throws IOException
	 */
	public void indexContent(String instanceId, String content)
			throws IOException {

		IndexWriter writer = getSearchEntity(instanceId).getWriter();

		List<Document> documents = null;
		if (content.startsWith("{")) {

			JSONObject obj = (JSONObject) JSONValue.parse(content);
			if (obj == null) {
				documents = getDocuments(content);
			} else {
				documents = getDocuments(obj);
			}

		} else if (content.startsWith("[")) {

			JSONArray obj = (JSONArray) JSONValue.parse(content);
			if (obj == null) {
				documents = getDocuments(content);
			} else {
				documents = getDocuments(obj);
			}

		} else {
			documents = getDocuments(content);

		}

		for (Document doc : documents) {
			writer.addDocument(doc);
		}

		writer.numDocs();
		writer.commit();

	}

	private List<Document> getDocuments(String content) throws IOException {
		Document document = new Document();

		Field contentField = new TextField(ServiceConstants.CONTENTS, content,
				Store.YES);
		Field contentNameField = new StoredField(ServiceConstants.CONTENT_NAME,
				"title");

		document.add(contentField);
		document.add(contentNameField);
		List<Document> documents = new ArrayList<Document>();
		documents.add(document);
		return documents;
	}

	private List<Document> getDocuments(JSONArray jsonObjects)
			throws IOException {

		JSONParser parser = new JSONParser();

		List<Document> documents = new ArrayList<Document>();
		Document doc = new Document();
		processJSONArray(jsonObjects, doc, "array_content");

		Field contentField = new TextField(ServiceConstants.CONTENTS,
				jsonObjects.toString(), Store.YES);

		doc.add(contentField);
		documents.add(doc);

		return documents;

	}

	private Document getDocumentForJSON(JSONObject object, Document doc) {

		for (String field : (Set<String>) object.keySet()) {
			if (field == null || field.trim().equals("")
					|| object.get(field) == null) {
				continue;
			}
			Class type = object.get(field).getClass();

			if (type.equals(Long.class)) {
				doc.add(new LongPoint(field, (Long) object.get(field)));
			} else if (type.equals(Double.class)) {
				doc.add(new DoublePoint(field, (Double) object.get(field)));
			} else if (type.equals(JSONObject.class)) {
				getDocumentForJSON((JSONObject) object.get(field), doc);
			} else if (type.equals(JSONArray.class)) {
				processJSONArray((JSONArray) object.get(field), doc, field);
			}

			if (!type.equals(JSONArray.class) && !type.equals(JSONObject.class)) {

				doc.add(new StringField(field, object.get(field).toString(),
						Field.Store.YES));
			}
		}
		return doc;
	}

	private void processJSONArray(JSONArray jsonobjects, Document doc,
			String field) {

		for (Object obj : (List<Object>) jsonobjects) {
			String arrObj = obj.toString();
			if (arrObj.startsWith("{")) {
				getDocumentForJSON((JSONObject) JSONValue.parse(arrObj), doc);
			} else if (arrObj.startsWith("[")) {
				processJSONArray((JSONArray) JSONValue.parse(arrObj), doc,
						field);
			} else {
				doc.add(new StringField(field, arrObj, Field.Store.YES));
			}

		}
	}

	private List<Document> getDocuments(JSONObject object) throws IOException {

		Document doc = getDocumentForJSON(object, new Document());

		Field contentField = new TextField(ServiceConstants.CONTENTS,
				object.toString(), Store.YES);
		doc.add(contentField);
		List<Document> documents = new ArrayList<Document>();

		documents.add(doc);
		return documents;
	}

}
