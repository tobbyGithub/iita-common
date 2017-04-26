/**
 * iita-common Apr 7, 2010
 */
package org.iita.entity;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.search.annotations.DocumentId;

/**
 * <p>
 * Simple Hibernate base class provides only id (primary key and unique identifier of entity). Resulting entity will not be audited.
 * </p>
 * <p>
 * For entities that require auditing, use {@link MySqlBaseEntity}.
 * <p>
 * 
 * @see MySqlBaseEntity
 * @author mobreza
 */
@MappedSuperclass
//@EntityListeners(UndeleteListener.class)
public abstract class SimpleEntity implements Serializable {
	private static final long serialVersionUID = -892167795670670715L;
	/** The id. */
	protected Long id;

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	@Id
	@GeneratedValue
	@DocumentId
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

}
