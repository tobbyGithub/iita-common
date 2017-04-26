package org.iita.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.iita.service.SearchException;
import org.iita.service.SearchService;
import org.iita.util.PagedResult;

/**
 * Search service implemented using Lucene
 * 
 * @author mobreza
 */
public class SearchServiceImpl implements SearchService<Object> {
	private static final Log LOG = LogFactory.getLog(SearchService.class);
	private EntityManager entityManager;

	/**
	 * @param entityManager the entityManager to set
	 */
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.SearchService#search(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public PagedResult<? extends Object> search(String searchQuery, Class clazz, String[] fields, int startAt, int maxResults) throws SearchException {
		LOG.info("Original query: " + searchQuery + " (" + startAt + ", " + maxResults + ")");
		// if (searchQuery == null || searchQuery.length() == 0)
		// return null;
		// searchQuery=searchQuery.trim();
		// searchQuery = searchQuery.replaceAll("([^\\-\\s]+)\\s+", "+$1* ");
		// if (!searchQuery.endsWith("~"))
		// searchQuery += "*";

		LOG.debug("Searching for: " + searchQuery + " in " + clazz);
		PagedResult<? extends Object> paged = new PagedResult<Object>(startAt, maxResults);
		if (searchQuery == null)
			return paged;

		FullTextEntityManager ftEm = Search.createFullTextEntityManager(this.entityManager);

		org.apache.lucene.search.Query luceneQuery = null;

		try {
			luceneQuery = getLuceneQuery(searchQuery, fields);
		} catch (ParseException e) {
			throw new SearchException(e);
		}

		if (luceneQuery != null) {
			org.hibernate.search.jpa.FullTextQuery query = ftEm.createFullTextQuery(luceneQuery, clazz);
			
			Boolean isReversed = true;
			//Sort sort = new Sort("this.startDate", isReversed);
			Sort sort = new Sort(new SortField("startDate", SortField.STRING, isReversed));
			
			query.setSort(sort);
			paged.setResults(query.setMaxResults(maxResults).setFirstResult(startAt).getResultList());
			LOG.info("Got " + paged.getResults().size() + " objects in paged");
			paged.setTotalHits(query.getResultSize());
			LOG.info("Total hits " + paged.getTotalHits());
		}

		return paged;
	}

	/**
	 * @throws SearchException
	 * @see org.iita.service.SearchService#search(java.lang.String, java.lang.String[], int, int)
	 */
	@Override
	public PagedResult<?> search(String searchQuery, String[] fields, int startAt, int maxResults) throws SearchException {
		LOG.debug("Searching for: " + searchQuery);
		PagedResult<? extends Object> paged = new PagedResult<Object>(startAt, maxResults);
		if (searchQuery == null)
			return paged;

		FullTextEntityManager ftEm = Search.createFullTextEntityManager(this.entityManager);

		org.apache.lucene.search.Query luceneQuery = null;

		try {
			luceneQuery = getLuceneQuery(searchQuery, fields);
		} catch (ParseException e) {
			throw new SearchException(e);
		}

		if (luceneQuery != null) {
			org.hibernate.search.jpa.FullTextQuery query = ftEm.createFullTextQuery(luceneQuery);
			
			Boolean isReversed = true;
			//Sort sort = new Sort("startDate", isReversed);
			Sort sort = new Sort(new SortField("startDate", SortField.STRING, isReversed));
			
			query.setSort(sort);
			paged.setResults(query.setMaxResults(maxResults).setFirstResult(startAt).getResultList());
			LOG.debug("Got " + paged.getResults().size() + " objects in paged");
			paged.setTotalHits(query.getResultSize());
			LOG.debug("Total hits " + paged.getTotalHits());
		}

		return paged;
	}

	/**
	 * @param searchQuery
	 * @return
	 * @throws ParseException
	 */
	private Query getLuceneQuery(String searchQuery, String[] fields) throws ParseException {
		/*
		 * String[] strings = new String[] { "item.name", "location.name", "item.description", "notes", "item.alternativeIdentifier", "location.parent.name",
		 * "location.parent.parent.name", "location.parent.parent.parent.name" };
		 */
		MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, new StandardAnalyzer());
		try {
			return parser.parse(searchQuery);
		} catch (ParseException e) {
			return null;
		}
	}
}
