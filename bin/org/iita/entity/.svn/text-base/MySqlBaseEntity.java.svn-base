package org.iita.entity;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Date;

import javax.persistence.MappedSuperclass;

/**
 * The class is the base entity for audited Hibernate managed entities (see {@link Auditable}). Most entities will extend this class as it provides core methods
 * shared between all audited entities.
 * 
 * <pre>
 * &#064;Entity
 * public class Order extends MySqlBaseEntity {
 * 	public static final long serialVersionUID = ....L;
 * 	// ... other fields of entity
 * // ... getters and setters 
 * }
 * </pre>
 * <p>
 * The resulting entity will be audited on persist/merge calls, current user's username and date of update will be stored with the entity.
 * </p>
 * <p>
 * For entities that do not require auditing, use {@link SimpleEntity}.
 * </p>
 * <p>
 * Note: the name of this class is very unfortunate as the entity is not MySql specific.
 * </p>
 */
@MappedSuperclass
public abstract class MySqlBaseEntity extends SimpleEntity implements Serializable, Auditable {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5679412449041448091L;

	private Date lastUpdated;

	private String lastUpdatedBy;

	private Date createdDate;

	private String createdBy;

	public Object displayPropertyValue(String property) throws Exception {
		Object value = null;

		Method[] m = this.getClass().getDeclaredMethods();
		for (int i = 0; i < m.length; i++) {
			if (m[i].getName().equalsIgnoreCase("get" + property) || m[i].getName().equalsIgnoreCase("is" + property)) {
				value = m[i].invoke(this, (Object[]) null);
				break;
			}
		}

		if (value != null && "false".equals(value.toString())) {
			value = "N/A";
		}
		if (value != null && "true".equals(value.toString())) {
			value = "YES";
		}

		if (value instanceof Double) {
			DecimalFormat formatMask = new DecimalFormat("###,###,###,###,###,###,###,###,###.00");
			value = formatMask.format(value);
		}

		return value;
	}

	@Override
	public Date getLastUpdated() {
		return lastUpdated;
	}

	@Override
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	@Override
	public void setLastUpdated(Date timestamp) {
		lastUpdated = timestamp;
	}

	@Override
	public void setLastUpdatedBy(String username) {
		lastUpdatedBy = username;
	}

	@Override
	public String getCreatedBy() {
		return createdBy;
	}

	@Override
	public Date getCreatedDate() {
		return createdDate;
	}

	@Override
	public void setCreatedBy(String username) {
		createdBy = username;
	}

	@Override
	public void setCreatedDate(Date timestamp) {
		createdDate = timestamp;
	}

	public String buildViewUrl(String prefix) {
		StringBuilder builder = new StringBuilder();

		builder.append(prefix);
		builder.append("?id=");
		builder.append(getId());
		builder.append("&typeId=");
		builder.append(this.getClass().getSimpleName());

		return builder.toString();
	}

	public String shortString() {
		String shortString = toString();
		StringBuilder b = new StringBuilder(toString());

		int openingBraceIdx = b.indexOf("{");
		int closingBraceIdx = b.indexOf("}");

		if (openingBraceIdx > -1 && closingBraceIdx > -1) {
			shortString = b.substring(closingBraceIdx + 1);
		} else {
			if (b.length() > 27) {
				shortString = b.substring(0, 27) + "...";
			}
		}

		return shortString;
	}
}
