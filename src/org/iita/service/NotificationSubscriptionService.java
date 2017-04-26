/**
 * projecttask.Struts Feb 1, 2010
 */
package org.iita.service;

import java.lang.reflect.Method;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.iita.notifications.ApplicationEventNotification.EventNotificationType;
import org.iita.security.model.User;
import org.iita.security.model.UserNotification;

/**
 * @author mobreza
 *
 */
public interface NotificationSubscriptionService {
	List<Method> listAvailableNotifications();

	/**
	 * @param user
	 * @return
	 */
	List<Method> listAvailableNotifications(User user);

	/**
	 * @param user
	 * @return
	 */
	Hashtable<String, UserNotification> getSubscriptions(User user);

	/**
	 * @param user 
	 * @param subscriptions
	 */
	void updateSubscriptions(User user, Dictionary<String, UserNotification> subscriptions);

	/**
	 * @param eventName
	 * @param eventNotificationType
	 * @param allowedSubscribers
	 * @return
	 */
	List<UserNotification> getUserNotifications(String eventName, EventNotificationType eventNotificationType, List<User> allowedSubscribers);
}
