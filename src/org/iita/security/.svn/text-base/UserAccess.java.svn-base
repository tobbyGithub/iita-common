/**
 * iita-common Aug 9, 2010
 */
package org.iita.security;

import org.iita.security.model.User;

/**
 * <p>
 * When your application uses {@link DataLoadCheck} voter and after invocation provider, classes that implement this interface are subject to row-level access
 * checks when method invoked is secured using <code>@Secure("BF_USERACCESS")</code> annotation.
 * </p>
 * <p>
 * An example class <code>Order</code> grants user access to order when user is owner of the order:
 * </p>
 * 
 * <pre>
 * public class Order implements UserAccess {
 * 	// Return true if user has access to this object (e.g. by checking if user is owner of the order)
 * 	&#064;Override
 * 	public boolean hasAccess(User user) {
 * 		&lt;b&gt;// grant access to Order if user is owner&lt;/b&gt;
 * 		if (this.owner.getId().equals(user.getId())
 * 			return true;
 * 		else
 * 			return false;
 * 	}
 * }
 * </pre>
 * 
 * @author mobreza
 */
public interface UserAccess {
	/**
	 * Object level access check. The method should return <code>true</code> if user should be allowed access to this instance of class. Commonly the method
	 * will check if user is "owner" or "member" of the instance.
	 * 
	 * @param user Currently authorized user
	 * @return <code>true</code> if user should be allowed access to this instance, <code>false</code> if user is <b>not</b> allowed access to this instance
	 */
	boolean hasAccess(User user);
}
