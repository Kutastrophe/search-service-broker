package com.cloud.search.servicebroker.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.stereotype.Service;

import com.cloud.search.servicebroker.entity.SearchEntity;
import com.cloud.search.servicebroker.entity.ServiceConstants;

/**
 * @author Chandrakant Bagade
 * 
 *         This class execute search for stored content
 *
 */
@Service
public class SearchService extends BaseService {

	/**
	 * search content - matching to content passed as method parameter
	 * 
	 * @param instanceId
	 *            , service instance id against which content is stored
	 * @param content
	 *            , content to match
	 * @return List<String> , matching documents
	 * @throws IOException
	 * @throws ParseException
	 */

	public List<String> search(String instanceId, String content)
			throws IOException, ParseException {

		System.out.println("search () - instanceId = " + instanceId
				+ " , content  = " + content);

		SearchEntity entity = getSearchEntity(instanceId);
		RAMDirectory ramDirectory = entity.getRamDirectory();
		Analyzer analyzer = entity.getAnalyzer();
		IndexReader reader = DirectoryReader.open(ramDirectory);
		QueryParser queryParser = new QueryParser(ServiceConstants.CONTENTS,
				analyzer);

		IndexSearcher indexSearcher = new IndexSearcher(reader);
		Query query = queryParser.parse(content);
		TopDocs foundDocs = indexSearcher.search(query,
				ServiceConstants.MAX_SEARCH);

		System.out.println("Total Results :: " + foundDocs.totalHits);

		List<String> result = new ArrayList<String>();

		for (ScoreDoc sd : foundDocs.scoreDocs) {
			Document d = indexSearcher.doc(sd.doc);
			result.add(d.get(ServiceConstants.CONTENTS));
			System.out.println("Document Name : "
					+ d.get(ServiceConstants.CONTENT_NAME) + "  :: Content : "
					+ d.get(ServiceConstants.CONTENTS) + "  :: Score : "
					+ sd.score);

		}
		reader.close();
		return result;

	}

	/**
	 * search content matching key (String) , value (String) passed as method
	 * content
	 * 
	 * @param instanceId
	 *            , service instance id against which content is stored
	 * @param key
	 *            , ket to match
	 * @param content
	 *            , value to match
	 * @return List<String>, matching documents
	 * @throws IOException
	 * @throws ParseException
	 */
	public List<String> search(String instanceId, String key, String content)
			throws IOException, ParseException {

		System.out.println("search () - instanceId = " + instanceId
				+ " , content  = " + content + " , key = " + key);

		SearchEntity entity = getSearchEntity(instanceId);
		RAMDirectory ramDirectory = entity.getRamDirectory();
		Analyzer analyzer = entity.getAnalyzer();
		IndexReader reader = DirectoryReader.open(ramDirectory);

		IndexSearcher indexSearcher = new IndexSearcher(reader);

		Term t = new Term(key, content);

		Query query = new TermQuery(t);
		TopDocs foundDocs = indexSearcher.search(query,
				ServiceConstants.MAX_SEARCH);

		System.out.println("Total Results :: " + foundDocs.totalHits);

		List<String> result = new ArrayList<String>();

		for (ScoreDoc sd : foundDocs.scoreDocs) {
			Document d = indexSearcher.doc(sd.doc);
			result.add(d.get(ServiceConstants.CONTENTS));
			System.out.println("Document : " + d.get(key)
					+ "  :: Content :    " + d.get(ServiceConstants.CONTENTS));
		}
		reader.close();
		return result;

	}

	/**
	 * search content matching key (String) , value (long) passed as method
	 * content
	 * 
	 * @param instanceId
	 *            , service instance id against which content is stored
	 * @param key
	 *            , ket to match
	 * @param content
	 *            , value to match
	 * @return List<String>, matching documents
	 * @throws IOException
	 * @throws ParseException
	 */

	public List<String> search(String instanceId, String key, long content)
			throws IOException, ParseException {

		System.out.println("search () - instanceId = " + instanceId
				+ " , content  = " + content + " , key = " + key);

		SearchEntity entity = getSearchEntity(instanceId);
		RAMDirectory ramDirectory = entity.getRamDirectory();
		Analyzer analyzer = entity.getAnalyzer();
		IndexReader reader = DirectoryReader.open(ramDirectory);

		IndexSearcher indexSearcher = new IndexSearcher(reader);

		// Term t = new Term(key, content);
		List<String> result = new ArrayList<String>();

		Query query = LongPoint.newExactQuery(key, content);
		TopDocs foundDocs = indexSearcher.search(query,
				ServiceConstants.MAX_SEARCH);

		System.out.println("Total Results :: " + foundDocs.totalHits);

		for (ScoreDoc sd : foundDocs.scoreDocs) {
			Document d = indexSearcher.doc(sd.doc);
			result.add(d.get(ServiceConstants.CONTENTS));
			System.out.println("Document : =  " + key + " d " + "get(key) =  "
					+ d.get(key) + "  :: Content :    "
					+ d.get(ServiceConstants.CONTENTS));
		}
		reader.close();
		return result;
	}

}
