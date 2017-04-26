package org.iita.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * <p>
 * VersionedEntity extends the {@link MySqlBaseEntity} class by adding "version" field. The field is annotated with @Version, ensuring Hibernate uses proper
 * data record versioning and reporting conflicts.
 * </p>
 * <p>
 * To make use of Hibernate's versioning, the last known version value must be passed to persist method. Example using Struts form:
 * </p>
 * 
 * <pre>
 * &lt;s:form method=&quot;post&quot; action=&quot;action!update&quot;&gt;
 * 	&lt;s:hidden name=&quot;id&quot; value=&quot;%{entity.id}&quot; /&gt;
 * 	&lt;b&gt;&lt;s:hidden name=&quot;version&quot; value=&quot;%{entity.version}&quot; /&gt;&lt;/b&gt;
 * ...
 * &lt;/s:form&gt;
 * </pre>
 * <p>
 * When version value passed in through the form is lower than actual version in the database, Hibernate will throw an exception which should be used to notify
 * the user that the record had changed.
 * </p>
 * 
 * @author mobreza
 */
@MappedSuperclass
public abstract class VersionedEntity extends MySqlBaseEntity {

	private static final long serialVersionUID = -3683125211329823418L;
	private int version = 0;

	/**
	 * Get record version
	 * 
	 * @return record version
	 */
	@Version
	public int getVersion() {
		return version;
	}

	/**
	 * Set version
	 * 
	 * @param version
	 */
	public void setVersion(int version) {
		this.version = version;
	}
}
