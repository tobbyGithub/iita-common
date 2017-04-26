/**
 * projecttask.Struts Feb 1, 2010
 */
package org.iita.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.iita.service.impl.NotificationSubscriptionServiceImpl;

/**
 * <p>
 * The @Notification annotation is used to identify application event methods that users can subscribe to.
 * </p>
 * <p>
 * The {@link NotificationSubscriptionServiceImpl} will scan the provided notification implementation class for all methods and filter out the ones with this
 * annotation. The annotation provides two pieces of information: the <b>required roles</b> a user must have in order to subscribe to the notification, and the
 * default message format string.
 * </p>
 * 
 * <pre>
 * &#064;Notification(defaultFormat = AUTHENTICATION_FAILED, requiredRoles = { &quot;ROLE_ADMIN&quot; })
 * public void authenticationFailed(String message) {
 * 	sendNotification(&quot;authenticationFailed&quot;, null, AUTHENTICATION_FAILED, message);
 * }
 * </pre>
 * 
 * @author mobreza
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Notification {
	/**
	 * List of roles that are allowed to subscribe to the notification
	 * 
	 * @return
	 */
	String[] requiredRoles();

	/**
	 * Default message format string. This will usually be a <code>static const</code> string.
	 * 
	 * @return
	 */
	String defaultFormat();
}
