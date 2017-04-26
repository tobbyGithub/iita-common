/**
 * iita-common Oct 11, 2010
 */
package org.iita.security;

import org.iita.security.model.User;

/**
 * User role access extends core {@link UserAccess} interface by providing additional checking for user roles.
 * 
 * @author mobreza
 */
public interface UserRoleAccess extends UserAccess {
	/**
	 * Is <code>role</code> granted to user on this object?
	 * 
	 * @param user
	 * @param role
	 * @return <code>true</code> if role is granted, <code>false</code> otherwise.
	 */
	boolean isRoleGranted(User user, String role);
}
