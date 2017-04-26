/**
 * 
 */
package org.iita.security.service;

/**
 * UserService exception.
 * 
 * @author mobreza
 */
public class UserServiceException extends Exception {
	
	/** Serial ID. */
	private static final long serialVersionUID = -7145634431276770957L;

	/**
	 * Instantiates a new user service exception.
	 */
	public UserServiceException() {

	}

	/**
	 * Instantiates a new user service exception.
	 * 
	 * @param message the message
	 */
	public UserServiceException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new user service exception.
	 * 
	 * @param message the message
	 * @param innerException the inner exception
	 */
	public UserServiceException(String message, Throwable innerException) {
		super(message, innerException);
	}

}
