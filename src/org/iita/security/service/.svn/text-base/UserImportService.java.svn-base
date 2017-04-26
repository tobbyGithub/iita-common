/**
 * iita-common Jun 18, 2009
 */
package org.iita.security.service;

import java.util.List;

import org.iita.security.model.User;

/**
 * Defines protocol of importing user data on request.
 * 
 * @author mobreza
 */
public interface UserImportService {
	/**
	 * Finds user information in underlying system and converts to User object that can then be persisted.
	 * 
	 * @param username
	 * @return
	 */
	User findUser(String username);

	/**
	 * Finds all users in underlying system, converts to User object
	 * 
	 * @return
	 */
	List<User> findUsers();

	/**
	 * @param filter
	 * @return
	 */
	List<User> findAll(String filter);

	/**
	 * @param staffID
	 * @return
	 */
	User findUserByStaffID(String staffID);
}
