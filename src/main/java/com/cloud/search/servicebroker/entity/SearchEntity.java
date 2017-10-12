package com.cloud.search.servicebroker.entity;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.RAMDirectory;

/**
 * @author Chandrakant Bagade
 *
 *         This class holds the entities require for search. The search
 *         functionality is based on Lucene search APIs. The content is indexed
 *         on to RAM. This class holds references for writer , ram , analyzer
 */
public class SearchEntity {

	private IndexWriter writer;
	private RAMDirectory ramDirectory;
	private Analyzer analyzer;

	public SearchEntity(IndexWriter writer, RAMDirectory ramDirectory,
			Analyzer analyzer) {

		this.writer = writer;
		this.ramDirectory = ramDirectory;
		this.analyzer = analyzer;
	}

	public IndexWriter getWriter() {
		return writer;
	}

	public RAMDirectory getRamDirectory() {
		return ramDirectory;
	}

	public Analyzer getAnalyzer() {
		return analyzer;
	}

	public void setWriter(IndexWriter writer) {
		this.writer = writer;
	}

}
