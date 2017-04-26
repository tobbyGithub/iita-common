/**
 * iita-common Jan 30, 2010
 */
package org.iita.service;

import java.util.List;

import org.iita.security.model.User;
import org.iita.security.model.UserNotification.NotificationFrequency;
import org.iita.security.model.UserNotification.NotificationPriority;

/**
 * {@link UserNotificationSender} defines base notification delivery methods. Classes only sending out messages should use this interface instead of fully
 * functional {@link UserNotificationService}.
 * 
 * @author mobreza
 */
public interface UserNotificationSender {

	public abstract void sendBroadcast(String appName, List<User> users, String notificationTitle, String notificationMsg);

	public abstract void sendBroadcast(String appName, List<User> users, String notificationTitle, String notificationMsg, NotificationFrequency frequency,
			NotificationPriority priority);

	public abstract void sendBroadcast(String appName, User user, String notificationTitle, String notificationMsg);

	public abstract void sendBroadcast(String appName, User user, String notificationTitle, String notificationMsg, NotificationFrequency frequency,
			NotificationPriority priority);

}