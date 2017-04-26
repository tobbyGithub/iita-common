package org.iita.hibernate;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.iita.entity.Auditable;
import org.iita.security.Authorize;
import org.iita.security.model.User;

/**
 * This Hibernate interceptor will automagically fill auditing data (creation date, username, modification date, username) of entities that implement
 * {@link Auditable} interface on persist and merge. 
 * 
 * <p>The interceptor must be registered with <code>applicationContext.xml</code> file as:</p>
 * 
 * <pre>
 * &lt;!-- Auditing --&gt;
 * &lt;entry key=&quot;hibernate.ejb.interceptor&quot; value=&quot;org.iita.hibernate.AuditInterceptor&quot; /&gt;
 * </pre>
 * 
 * @author mobreza
 */
public class AuditInterceptor extends EmptyInterceptor {
	private static final String DEFAULTUSERNAME = "SYSTEM";
	private static final Log LOG = LogFactory.getLog(AuditInterceptor.class);
	private static final long serialVersionUID = 1L;

	//	@Override
	//	public void afterTransactionBegin(Transaction tx) {
	//		
	//	}

	/**
	 * As the entity is removed from the DB, this interceptor does nothing on delete.
	 * 
	 * @see org.hibernate.EmptyInterceptor#onDelete(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
	 */
	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {

	}

	/**
	 * Fetch username of currently logged on user and apply current timestamp and this username to {@link Auditable} entities. Only
	 * {@link Auditable#getLastUpdated()} and {@link Auditable#getLastUpdatedBy()} are filled, other two are filled if missing.
	 * 
	 * @see org.hibernate.EmptyInterceptor#onFlushDirty(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.Object[], java.lang.String[],
	 *      org.hibernate.type.Type[])
	 */
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
		String authenticationName = DEFAULTUSERNAME;
		Date auditDate = new Date();
		User user = Authorize.getUser();
		if (user != null)
			authenticationName = getAuditedName(user);

		boolean changedFlag = false;
		if (entity instanceof Auditable) {
			LOG.trace("Auditing " + entity.getClass());

			for (int i = 0; i < propertyNames.length; i++) {
				if ("lastUpdated".equals(propertyNames[i])) {
					currentState[i] = auditDate;
					LOG.trace("Auditing lastUpdate: " + currentState[i]);
					changedFlag = true;
				} else if ("createdDate".equals(propertyNames[i]) && currentState[i] == null) {
					// only update if null
					if (previousState[i] == null) {
						LOG.trace("Current value createdDate: " + currentState[i] + " " + previousState[i]);
						currentState[i] = auditDate;
						LOG.trace("Auditing createdDate: " + currentState[i]);
					} else {
						currentState[i] = previousState[i];
					}
					changedFlag = true;
				} else if ("lastUpdatedBy".equals(propertyNames[i])) {
					currentState[i] = authenticationName;
					LOG.trace("Auditing lastUpdatedBy: " + currentState[i]);
					changedFlag = true;
				} else if ("createdBy".equals(propertyNames[i]) && currentState[i] == null) {
					LOG.trace("Current value createdBy: " + currentState[i] + " " + previousState[i]);
					// only update if null
					if (previousState[i] == null) {
						currentState[i] = authenticationName;
						LOG.trace("Auditing createdBy: " + currentState[i]);
						changedFlag = true;
					} else {
						currentState[i] = previousState[i];
					}
				}
			}
		}
		return changedFlag;
	}

	/**
	 * @param user
	 * @return
	 */
	private String getAuditedName(User user) {
		if (user == null)
			return DEFAULTUSERNAME;
		if (user.getImpersonator() == null)
			return user.getUsername();
		else
			return user.getUsername() + "/" + user.getImpersonator().getName();
	}

	/**
	 * We don't do anything on load
	 * 
	 * @see org.hibernate.EmptyInterceptor#onLoad(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
	 */
//	public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
//		return false;
//	}

	/**
	 * Called when objects are <b>persisted</b> to database. All fields of {@link Auditable} are filled.
	 * 
	 * @see org.hibernate.EmptyInterceptor#onSave(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
	 */
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		boolean changedFlag = false;
		String authenticationName = DEFAULTUSERNAME;
		Date auditDate = new Date();
		User user = Authorize.getUser();
		if (user != null)
			authenticationName = getAuditedName(user);

		if (entity instanceof Auditable) {
			for (int i = 0; i < propertyNames.length; i++) {
				if ("lastUpdated".equals(propertyNames[i])) {
					state[i] = auditDate;
					LOG.trace("Auditing lastUpdated: " + state[i]);
					changedFlag = true;
				} else if ("createdDate".equals(propertyNames[i])) {
					state[i] = auditDate;
					LOG.trace("Auditing createdDate: " + state[i]);
					changedFlag = true;
				} else if ("lastUpdatedBy".equals(propertyNames[i])) {
					state[i] = authenticationName;
					LOG.trace("Auditing lastUpdatedBy: " + state[i]);
					changedFlag = true;
				} else if ("createdBy".equals(propertyNames[i])) {
					state[i] = authenticationName;
					LOG.trace("Auditing createdBy: " + state[i]);
					changedFlag = true;
				}
			}
		}
		return changedFlag;
	}

//	public void afterTransactionCompletion(Transaction tx) {
//		if (tx.wasCommitted()) {
//			LOG.debug("Creations: " + creates + ", Updates: " + updates + ", Loads: " + loads);
//		}
//	}

}