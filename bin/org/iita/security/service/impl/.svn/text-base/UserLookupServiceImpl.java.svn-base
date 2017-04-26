/**
 * 
 */
package org.iita.security.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.security.model.User;
import org.iita.security.model.UserLookup;
import org.iita.security.service.UserLookupService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author aafolayan
 * 
 */
@Transactional
public class UserLookupServiceImpl implements UserLookupService {
	private static final Log log = LogFactory.getLog(UserLookupService.class);
	private EntityManager em;

	/**
	 * @return the em
	 */
	public EntityManager getEntityManager() {
		return em;
	}

	/**
	 * @param em the em to set
	 */
	@PersistenceContext
	// DI operation goes here
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.par.service.UserLookupService#find(int)
	 */
	@Override
	public UserLookup find(long id) {
		return em.find(UserLookup.class, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.par.service.UserLookupService#findAll()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UserLookup> findAll() {
		Query query = getEntityManager().createQuery("from UserLookup u ");
		return query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.par.service.UserLookupService#findAll(int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UserLookup> findAll(int start, int maxResults) {
		Query query = getEntityManager().createQuery("from UserLookup u").setFirstResult(start).setMaxResults(maxResults);
		return query.getResultList();
	}

	@Override
	public void remove(UserLookup lookup) {
		if (lookup == null || lookup.getId() == null)
			return;
		this.em.remove(lookup);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.par.service.UserLookupService#save(org.iita.par.model.UserLookup )
	 */
	@Override
	public String save(UserLookup userLookup) {
		if (userLookup.getId() == null) {
			log.info("Adding " + userLookup.getIdentifier() + " to user " + userLookup.getUser());
			log.debug("new user lookup id is:  " + userLookup.getId());
			em.persist(userLookup);
			log.debug("new user lookup id is: now persisted!");
		}
		// update driver info
		else {
			log.info("Updating " + userLookup.getIdentifier() + " to user " + userLookup.getUser());
			log.debug("new updated user lookup id is:  " + userLookup.getId());
			em.merge(userLookup);
		}

		return "success";
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserLookup> findByUser(User user) {
		log.info("listing lookups for " + user);
		return (List<UserLookup>) em.createQuery("from UserLookup u where u.user=:user ").setParameter("user", user).getResultList();
	}

}
