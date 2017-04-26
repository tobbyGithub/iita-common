/**
 * 
 */
package org.iita.security;

import org.iita.security.model.User;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;

/**
 * <p>
 * Utility class to check for user's granted roles.
 * </p>
 * 
 * @author mobreza
 */
public class Authorize {

	/**
	 * Does user have any of the requestedAuthorities (roles)?
	 * 
	 * @param string String of roles, separated by comma (e.g. <code>"ROLE_ADMIN,ROLE_MODERATOR"</code>)
	 * @return <code>true</code> if user has at least one of the roles required
	 */
	public static boolean hasAny(String... requestedAuthorities) {
		String[] requested = requestedAuthorities;
		if (requestedAuthorities.length == 1)
			requested = requestedAuthorities[0].split(",");
		for (String r : requested) {
			if (hasAuthority(r.trim()))
				return true;
		}
		return false;
	}

	/**
	 * Check if user has any of the requested global roles
	 * 
	 * @param user
	 * @param requestedAuthorities
	 * @return
	 */
	public static boolean hasAny(User user, String... requestedAuthorities) {
		for (String role : requestedAuthorities) {
			if (user.hasRole(role))
				return true;
		}
		return false;
	}

	/**
	 * @param string
	 * @return
	 */
	public static boolean hasAll(String... requestedAuthorities) {
		String[] requested = requestedAuthorities;
		if (requestedAuthorities.length == 1)
			requested = requestedAuthorities[0].split(",");
		boolean hasAll = true;
		for (String r : requested) {
			hasAll &= hasAuthority(r.trim());
			if (!hasAll)
				return false;
		}
		return hasAll;
	}

	/**
	 * Does current user have required role? Note that role names are case-sensitive.
	 * 
	 * @param role Single role name (e.g. <code>"ROLE_ADMIN"</code>)
	 * @return <code>true</code> if current user has the role
	 */
	public static boolean hasAuthority(String role) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null)
			return false;
		for (GrantedAuthority authority : authentication.getAuthorities()) {
			if (authority.getAuthority().equals(role))
				return true;
		}
		return false;
	}

	/**
	 * Does a user have a particular role?
	 * 
	 * @param user {@link User} object
	 * @param role Single role name (e.g. <code>"ROLE_ADMIN"</code>)
	 * @return <code>true</code> if current user has the role
	 */
	public static boolean hasRole(User user, String role) {
		if (user == null)
			return false;

		for (GrantedAuthority authority : user.getAuthorities()) {
			if (authority.getAuthority().equalsIgnoreCase(role))
				return true;
		}
		return false;
	}

	/**
	 * Get current user from context
	 * 
	 * @return Current user as {@link User} or <code>null</code> if user is not logged in or is not an instance of {@link User}
	 */
	public static User getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null)
			return null;
		if (authentication.getPrincipal() instanceof User) {
			return (User) authentication.getPrincipal();
		}
		return null;
	}

	public static User getUser(Authentication authentication) {
		if (authentication == null)
			return null;
		if (authentication.getPrincipal() instanceof User) {
			return (User) authentication.getPrincipal();
		}
		return null;
	}

	/**
	 * Get current user from context
	 * 
	 * @return Current user as {@link User} or <code>null</code> if user is not logged in or is not an instance of {@link User}
	 */
	public static User getPrincipal() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null)
			return null;
		if (authentication.getPrincipal() instanceof User) {
			User user = (User) authentication.getPrincipal();
			if (user.getImpersonator() == null)
				return user;
			else
				return (User) user.getImpersonator().getPrincipal();
		}
		return null;
	}

	/**
	 * Check if user has access to object with any of the listed roles. If no access is granted (or object is null) check global roles.
	 * 
	 * @param userRoleAccess Object supporting userRoleAccess
	 * @param roles
	 * @return
	 */
	public static boolean hasAny(UserRoleAccess userRoleAccess, String... roles) {
		return hasAny(Authorize.getUser(), userRoleAccess, roles);
	}

	/**
	 * Check if user has access to object with any of the listed roles. If no access is granted (or object is null) check global roles.
	 * 
	 * @param user
	 * @param userRoleAccess
	 * @param roles
	 * @return
	 */
	public static boolean hasAny(User user, UserRoleAccess userRoleAccess, String... roles) {
		// if object not provided, check global roles
		if (userRoleAccess == null)
			return hasAny(user, roles);

		// check if any role granted on object
		for (String role : roles) {
			if (userRoleAccess.isRoleGranted(user, role))
				return true;
		}

		// check global roles
		return hasAny(user, roles);
	}

}
