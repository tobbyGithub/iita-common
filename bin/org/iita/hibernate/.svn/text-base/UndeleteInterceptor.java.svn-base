package org.iita.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.iita.security.model.User;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;

/**
 * <p>
 * Hibernate interceptor that will store <b>removed</b> entities into a list. Entities in this list could then be re-persisted.
 * </p>
 *<p>
 * <b>Do not use! Not tested.</b>
 * </p>
 * 
 * @author mobreza
 */
public class UndeleteInterceptor extends AuditInterceptor {
	private static final long serialVersionUID = 4122401784670077769L;
	private static final Log LOG = LogFactory.getLog(UndeleteInterceptor.class);
	private static final int maxSize = 20;
	private List<Object> deletedList = null;
	private static Deque<UndeleteData> deletions = new LinkedList<UndeleteData>();

	@Override
	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		if (this.deletedList == null)
			this.deletedList = new LinkedList<Object>();

		LOG.debug(this + " onDelete: " + entity);
		this.deletedList.add(entity);

		super.onDelete(entity, id, state, propertyNames, types);
	}

	@Override
	public void afterTransactionCompletion(Transaction tx) {
		if (this.deletedList != null) {
			LOG.debug(this + " afterTransactionCompletion: " + this.deletedList.size() + " deleted entitites");
			UndeleteData undeleteData = new UndeleteData();
			undeleteData.setEntities(this.deletedList);
			this.deletedList = null;

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Object u = null;
			if (authentication != null)
				u = authentication.getPrincipal();
			if (u != null && u instanceof User) {
				undeleteData.setUser((User) u);
			}

			synchronized (deletions) {
				if (deletions.size() > UndeleteInterceptor.maxSize) {
					LOG.debug("Deleted queue has " + deletions.size() + " elements. Removing head.");
					deletions.removeFirst();
				}
				LOG.info("Entity added to deleted entities list");
				deletions.addFirst(undeleteData);
			}
		}

		super.afterTransactionCompletion(tx);
	}

	/**
	 * Get list of deleted entities.
	 * 
	 * @return
	 */
	public static List<UndeleteData> getDeletions() {
		ArrayList<UndeleteData> copy = new ArrayList<UndeleteData>();
		synchronized (deletions) {
			LOG.debug("Creating copy of deleted entities list.");
			copy.addAll(deletions);
		}
		return copy;
	}

	/**
	 * @param deleted
	 */
	public static void undelete(UndeleteData deleted) {
		synchronized (deletions) {
			LOG.info("Removing undeleted entity from queue.");
			deletions.remove(deleted);
		}
	}
}
