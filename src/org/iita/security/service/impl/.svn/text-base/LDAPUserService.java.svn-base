/**
 * iita-common Jun 18, 2009
 */
package org.iita.security.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.security.model.User;
import org.iita.security.model.User.AuthenticationType;
import org.iita.security.service.AuthenticationService;
import org.iita.security.service.UserImportService;
import org.iita.security.service.UserServiceException;

/**
 * The Class LDAPUserService.
 * 
 * @author mobreza
 */
public class LDAPUserService implements UserImportService, AuthenticationService {

	/** The Constant LOG. */
	private static final Log LOG = LogFactory.getLog(LDAPUserService.class);

	/** The ldap host. */
	private String ldapHost;

	/** The timeout. */
	private int timeout = 10000;

	/** LDAP authentication username for lookups. */
	private String username;

	/** LDAP password for lookups. */
	private String password;

	/** Domain name to prepend to username for authentication. */
	private String domainName = "IITA-IBADAN";

	/**
	 * Find user.
	 * 
	 * @param username the username
	 * 
	 * @return the user
	 * 
	 * @see org.iita.security.service.UserImportService#findUser(java.lang.String)
	 */
	@Override
	public User findUser(String username) {
		try {
			NamingEnumeration<SearchResult> results = searchUser(username, true);
			SearchResult searchResult = null;
			if (results.hasMore()) {
				searchResult = (SearchResult) results.next();
			}
			if (searchResult != null) {
				LOG.info("Found object: " + searchResult.getName() + " " + searchResult);
			}
			if (results.hasMoreElements() && results.hasMore()) {
				results.close();
				throw new UserServiceException("LDAP has several users with username=" + username + ". Don't know what to do!");
			}
			if (searchResult == null) {
				LOG.debug("Search found no matching users for query: " + username);
				return null;
			} else {
				User user = convertToUser(searchResult);
				if (user.getUsername() != null) {
					if (user.getMail() == null) {
						user.setMail(user.getUsername() + "@localhost");
						LOG.warn("User has no email address configured, using " + user.getMail());
					}
					return user;
				} else
					return null;
			}
		} catch (NamingException e) {
			LOG.error(e);
			return null;
		} catch (UserServiceException e) {
			LOG.error(e);
			return null;
		}
	}
	
	/**
	 * @see org.iita.security.service.UserImportService#findUserByStaffID(java.lang.String)
	 */
	@Override
	public User findUserByStaffID(String staffID) {
		LOG.info("Searching LDAP for user with staff ID: " + staffID);
		try {
			NamingEnumeration<SearchResult> results = searchUser(staffID, true, "extensionAttribute1");
			SearchResult searchResult = null;
			if (results.hasMore()) {
				searchResult = (SearchResult) results.next();
			}
			if (searchResult != null) {
				LOG.info("Found object: " + searchResult.getName() + " " + searchResult);
			}
			if (results.hasMoreElements() && results.hasMore()) {
				results.close();
				throw new UserServiceException("LDAP has several users with staffId=" + staffID + ". Don't know what to do!");
			}
			if (searchResult == null) {
				LOG.debug("Search found no matching users for query: " + staffID);
				return null;
			} else {
				User user = convertToUser(searchResult);
				LOG.info("Found user in LDAP: " + user);
				if (user.getUsername() != null) {
					if (user.getMail() == null) {
						user.setMail(user.getUsername() + "@localhost");
						LOG.warn("User has no email address configured, using " + user.getMail());
					}
					return user;
				} else
					return null;
			}
		} catch (NamingException e) {
			LOG.error(e);
			return null;
		} catch (UserServiceException e) {
			LOG.error(e);
			return null;
		}
	}

	/**
	 * @see org.iita.security.service.UserImportService#findAll(java.lang.String)
	 */
	@Override
	public List<User> findAll(String filter) {
		List<User> ldapSearch = new ArrayList<User>();

		User singleUser = findUser(filter);
		if (singleUser != null) {
			ldapSearch.add(singleUser);
			return ldapSearch;
		}

		try {
			NamingEnumeration<SearchResult> results = searchUser(filter, false);
			while (results.hasMore()) {
				SearchResult searchResult = (SearchResult) results.next();
				User user = convertToUser(searchResult);
				if (user.getUsername() != null && user.getMail() != null)
					ldapSearch.add(user);
			}
		} catch (NamingException e) {
			LOG.error(e);
		} catch (UserServiceException e) {
			LOG.error(e);
			return null;
		}

		Collections.sort(ldapSearch, new Comparator<User>() {
			/**
			 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
			 */
			@Override
			public int compare(User arg0, User arg1) {
				return arg0.getFullName().compareTo(arg1.getFullName());
			}
		});
		return ldapSearch;
	}

	private NamingEnumeration<SearchResult> searchUser(String filter, boolean fullMatch) throws NamingException, UserServiceException {
		return searchUser(filter, fullMatch, "sAMAccountName", "mail");
	}
	
	/**
	 * Search for user. Only works fine with unique usernames!
	 * 
	 * @param username the username2
	 * 
	 * @return the search result
	 * 
	 * @throws NamingException the naming exception
	 * @throws UserServiceException When error occurs in searching for unique user
	 */
	private NamingEnumeration<SearchResult> searchUser(String filter, boolean fullMatch, String ... searchFields) throws NamingException, UserServiceException {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, this.ldapHost);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, this.username);
		env.put(Context.SECURITY_CREDENTIALS, this.password);
		env.put(Context.REFERRAL, "follow");
		env.put("com.sun.jndi.ldap.read.timeout", "" + this.timeout);
		DirContext ctx = null;
		try {
			ctx = new InitialDirContext(env);

			SearchControls controls = new SearchControls();
			controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			LOG.info("Search LDAP for user: " + filter);
			NamingEnumeration<SearchResult> results;
			
			// construct filter:  "(&(|(sAMAccountName=" + filter + ")(mail=" + filter + "))(objectclass=user))"
			StringBuilder sb=new StringBuilder("(&(");
			if (searchFields.length>1)
				sb.append("|");
			for (String field : searchFields) {
				sb.append("(").append(field).append("=");
				if (fullMatch)
					sb.append(filter);
				else
					sb.append("*").append(filter).append("*");
				sb.append(")");
			}
			sb.append(")(objectclass=user))");
			
			LOG.debug("LDAP query: " + sb.toString());
			
			results = ctx.search("", sb.toString(), controls);
			return results;

		} catch (javax.naming.AuthenticationException e) {
			LOG.warn("LDAP authentication failed for " + this.username + ". Details: " + e);
			throw (e);
		} catch (CommunicationException up) {
			LOG.error(up);
			throw up;
		} catch (NamingException e) {
			LOG.error(e);
			throw e;
		}
	}

	/**
	 * Convert to user.
	 * 
	 * @param searchResult the search result
	 * 
	 * @return the user
	 */
	private User convertToUser(SearchResult searchResult) {
		if (searchResult == null) {
			LOG.debug("Will not convert null search result to User object");
			return null;
		}

		// register user
		User newUser = new User();
		// register user data
		LOG.debug("Populating user object data.");
		fillUserData(newUser, searchResult);
		// set default authentication type
		LOG.debug("Setting auth type to LDAP");
		newUser.setAuthenticationType(AuthenticationType.LDAP);
		// return new user instance
		LOG.debug("User data converted. Returning user object.");
		return newUser;
	}

	/**
	 * Copies sAMAccountName, sn, givenName, mail, displayName, department, description.
	 * 
	 * @param user the user
	 * @param searchResult the search result
	 */
	private void fillUserData(User user, SearchResult searchResult) {
		Attributes attributes = searchResult.getAttributes();
		try {
			user.setUserName(attributes.get("sAMAccountName").get().toString().trim());
			LOG.debug("Username: " + user.getUsername());
		} catch (Exception e2) {
			LOG.debug("LDAP username: " + e2.getMessage());
		}
		try {
			user.setLastName(attributes.get("sn").get().toString().trim());
		} catch (Exception e2) {
			LOG.debug("LDAP sn: " + e2.getMessage());
		}
		try {
			user.setFirstName(attributes.get("givenName").get().toString().trim());
		} catch (Exception e2) {
			LOG.debug("LDAP givenName: " + e2.getMessage());
		}
		try {
			user.setMail(attributes.get("mail").get().toString().trim());
		} catch (Exception e2) {
			LOG.debug("LDAP mail: " + e2.getMessage());
		}
		try {
			user.setDisplayName(attributes.get("displayName").get().toString().trim());
		} catch (Exception e2) {
			LOG.debug("LDAP displayName: " + e2.getMessage());
		}
		try {
			user.setDepartment(attributes.get("department").get().toString().trim());
		} catch (Exception e2) {
			LOG.debug("LDAP department: " + e2.getMessage());
		}
		try {
			user.setDescription(attributes.get("description").get().toString().trim());
		} catch (Exception e2) {
			LOG.debug("LDAP description: " + e2.getMessage());
		}
		try {
			String staffId = attributes.get("extensionAttribute1").get().toString().trim();
			if (staffId == null || staffId.length() == 0 || staffId.equalsIgnoreCase("E00000"))
				staffId = null;
			else
				LOG.warn("Staff ID for " + user.getDisplayName() + ": " + staffId);
			user.setStaffId(staffId);
		} catch (Exception e2) {
			LOG.debug("LDAP extensionAttribute1: " + e2.getMessage());
		}

		try {
			if (attributes.get("mailNickname") != null) {
				NamingEnumeration<?> aliases = attributes.get("mailNickname").getAll();
				while (aliases.hasMore()) {
					String alias = aliases.next().toString();
					user.addAlias(alias);
				}

				aliases = attributes.get("proxyAddresses").getAll();
				while (aliases.hasMore()) {
					String alias = aliases.next().toString();
					if (alias.startsWith("SMTP:") || alias.startsWith("smtp:")) {
						user.addAlias(alias.substring(alias.indexOf(':') + 1));
					}
				}
			}
		} catch (NamingException e) {
			LOG.debug("LDAP mailNickname: " + e);
		}
	}

	/**
	 * The LDAP user service will always return <code>null</code>. Too much data.
	 * 
	 * @return the list< user>
	 * 
	 * @see org.iita.security.service.UserImportService#findUsers()
	 */
	@Override
	public List<User> findUsers() {
		// will not return all users from LDAP, too much data
		return null;
	}

	/**
	 * Authenticate.
	 * 
	 * @param username the username
	 * @param password the password
	 * 
	 * @return true, if authenticate
	 * @throws CommunicationException If communication with LDAP host is down
	 * 
	 * @see org.iita.security.service.AuthenticationService#authenticate(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean authenticate(String username, String password, User user) throws CommunicationException {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, this.ldapHost);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		if (username != null && username.contains("\\")) {
			LOG.info("Authenticating against LDAP: " + username);
			env.put(Context.SECURITY_PRINCIPAL, username);
		} else {
			LOG.info("Authenticating against LDAP: " + (domainName == null || domainName.length() == 0 ? "" : domainName + "\\") + username);
			env.put(Context.SECURITY_PRINCIPAL, (domainName == null || domainName.length() == 0 ? "" : domainName + "\\") + username);
		}
		env.put(Context.SECURITY_CREDENTIALS, password);
		//LOG.debug("Passwd: " + password.length() + " chars, first chars: " + password.substring(0, 2) + " last: " + password.substring(password.length()-2));
		env.put("com.sun.jndi.ldap.read.timeout", "" + this.timeout);
		try {
			new InitialDirContext(env);

			// TODO need to fetch user object from LDAP
			// TODO if fetched, then update some properties of {@link User) entity

			return true;
		} catch (javax.naming.AuthenticationException e) {
			LOG.warn("LDAP authentication failed for " + username + ". Details: " + e);
			LOG.warn(e.getMessage(), e);
		} catch (CommunicationException up) {
			LOG.error(up);
			throw up;
		} catch (NamingException e) {
			LOG.error(e);
		}

		return false;
	}

	/**
	 * Sets the domain name.
	 * 
	 * @param domainName the domainName to set
	 */
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	/**
	 * Sets the ldap host.
	 * 
	 * @param ldapHost the ldapHost to set
	 */
	public void setLdapHost(String ldapHost) {
		this.ldapHost = ldapHost;
	}

	/**
	 * Sets the password.
	 * 
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Sets the timeout.
	 * 
	 * @param timeout the timeout to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * Sets the username.
	 * 
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
}
