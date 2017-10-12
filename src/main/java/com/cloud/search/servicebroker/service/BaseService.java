package com.cloud.search.servicebroker.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.stereotype.Service;

import com.cloud.search.servicebroker.entity.SearchEntity;

/**
 * @author Chandrakant Bagade
 *
 *         This is base class for Search Service related classes , responsible
 *         for creating search related entities.
 */
@Service
public class BaseService {

	private static final Map<String, SearchEntity> searchEntityInst = new HashMap<String, SearchEntity>();

	/**
	 * create index writer for lucene search functaionality using RAM to hold
	 * content. All search entities are bind against service instance id
	 * 
	 * @param instanceId
	 *            service instance id for which search related indexwriter to
	 *            initialize
	 * @throws IOException
	 */

	public void createSearchEntity(String instanceId) throws IOException {

		SearchEntity entity = searchEntityInst.get(instanceId);

		if (entity == null || entity.getRamDirectory() == null) {

			RAMDirectory ramDirectory = new RAMDirectory();
			Analyzer analyzer = new StandardAnalyzer();

			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			iwc.setOpenMode(OpenMode.CREATE);

			IndexWriter writer = new IndexWriter(ramDirectory, iwc);

			entity = new SearchEntity(writer, ramDirectory, analyzer);

			searchEntityInst.put(instanceId, entity);

		}

	}

	/**
	 * delete index writer stored agsinst service instance id
	 * 
	 * @param instanceId
	 *            service instance id for which search related indexwriter is
	 *            initialize
	 */

	public void deleteSearchEntity(String instanceId) {
		searchEntityInst.remove(instanceId);
	}

	/**
	 * get search entitities
	 * 
	 * @param instanceId
	 *            service instance id for which search related indexwriter is
	 *            initialize
	 * @return SearchEntity , holding indexwriter
	 */

	public SearchEntity getSearchEntity(String instanceId) {
		return searchEntityInst.get(instanceId);
	}
}
