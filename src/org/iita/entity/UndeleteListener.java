/**
 * iita-common Apr 16, 2010
 */
package org.iita.entity;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.PostRemove;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * Undelete listener can be registered on {@link SimpleEntity}. <b>This code is not tested properly, do not use!</b>
 * </p>
 * 
 * @author mobreza
 */
public class UndeleteListener {
	private static final Log LOG = LogFactory.getLog(UndeleteListener.class);
	private static final int maxSize = 50;
	private static Deque<Object> deletedEntities = new LinkedList<Object>();

	@PostRemove
	public void entityDeleted(SimpleEntity entity) {
		LOG.info(this + " Entity " + entity.getClass().getName() + " removed");
		synchronized (deletedEntities) {
			if (deletedEntities.size() > UndeleteListener.maxSize) {
				LOG.debug("Deleted queue has " + deletedEntities.size() + " elements. Removing head.");
				deletedEntities.removeFirst();
			}
			LOG.info("Entity added to deleted entities list");
			deletedEntities.addFirst(entity);
		}
	}

	/**
	 * Get list of deleted entities.
	 * 
	 * @return
	 */
	public static List<Object> getDeletedEntities() {
		ArrayList<Object> copy = new ArrayList<Object>();
		synchronized (deletedEntities) {
			LOG.debug("Creating copy of deleted entities list.");
			copy.addAll(deletedEntities);
		}
		return copy;
	}

	/**
	 * @param deleted
	 */
	public static void undelete(Object deleted) {
		synchronized (deletedEntities) {
			LOG.info("Removing undeleted entity from queue.");
			deletedEntities.remove(deleted);
		}
	}
}