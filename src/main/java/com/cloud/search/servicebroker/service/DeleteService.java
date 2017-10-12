package com.cloud.search.servicebroker.service;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.springframework.stereotype.Service;

import com.cloud.search.servicebroker.entity.SearchEntity;
import com.cloud.search.servicebroker.entity.ServiceConstants;

/**
 * @author Chandrakant Bagade
 *
 *         This class provide delete functionality for documents stored for
 *         search functionality
 */
@Service
public class DeleteService extends BaseService {

	/**
	 * delete documents , matched with content passed as parameter to method
	 * 
	 * @param instanceId
	 *            , service instance if against which documents are stored
	 * @param content
	 *            , content to match for deletion
	 * @throws IOException
	 * @throws ParseException
	 */
	public void delete(String instanceId, String content) throws IOException,
			ParseException {

		SearchEntity entity = getSearchEntity(instanceId);
		Analyzer analyzer = entity.getAnalyzer();
		QueryParser queryParser = new QueryParser(ServiceConstants.CONTENTS,
				analyzer);

		Query query = queryParser.parse(content);
		IndexWriter writer = entity.getWriter();
		writer.deleteDocuments(query);
		writer.commit();

	}

	/**
	 * delete documents matching key and value - both String
	 * 
	 * @param instanceId
	 *            , service instance id against which documents are stored
	 * @param key
	 *            , key to match
	 * @param content
	 *            , value to match
	 * @throws IOException
	 * @throws ParseException
	 */
	public void delete(String instanceId, String key, String content)
			throws IOException, ParseException {

		SearchEntity entity = getSearchEntity(instanceId);
		Term t = new Term(key, content);
		Query query = new TermQuery(t);

		IndexWriter writer = entity.getWriter();
		writer.deleteDocuments(query);
		writer.commit();

	}

	/**
	 * delete documents matching key (String) and value (long)
	 * 
	 * @param instanceId
	 *            , service instance id against which documents are stored
	 * @param key
	 *            , key to match
	 * @param content
	 *            , value to match
	 * @throws IOException
	 * @throws ParseException
	 */
	public void delete(String instanceId, String key, long content)
			throws IOException, ParseException {

		SearchEntity entity = getSearchEntity(instanceId);
		Query query = LongPoint.newExactQuery(key, content);
		IndexWriter writer = entity.getWriter();
		writer.deleteDocuments(query);
		writer.commit();

	}

}
