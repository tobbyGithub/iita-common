package org.iita.entity;

import java.util.Date;

import org.iita.hibernate.AuditInterceptor;

/**
 * <p>
 * The interface defines getters and setters that help {@link AuditInterceptor} identify classes that need auditing when merged or persisted.
 * </p>
 * 
 * @see MySqlBaseEntity
 * @author mobreza
 */
public interface Auditable {
	/**
	 * Set last updated date
	 * 
	 * @param timestamp
	 */
	void setLastUpdated(Date timestamp);

	/**
	 * Get last updated date
	 * 
	 * @return
	 */
	Date getLastUpdated();

	/**
	 * Set username of last update
	 * 
	 * @param username
	 */
	void setLastUpdatedBy(String username);

	/**
	 * Get username of last update
	 * 
	 * @return
	 */
	String getLastUpdatedBy();

	/**
	 * Set date of creation, only set when persist is called or if blank
	 * 
	 * @param timestamp
	 */
	void setCreatedDate(Date timestamp);

	/**
	 * Get date of creation
	 * 
	 * @return
	 */
	Date getCreatedDate();

	/**
	 * Set username who created the record
	 * 
	 * @param username
	 */
	void setCreatedBy(String username);

	/**
	 * Get username who created the record
	 * 
	 * @return
	 */
	String getCreatedBy();
}
