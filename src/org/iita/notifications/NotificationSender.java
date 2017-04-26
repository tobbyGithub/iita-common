/**
 * projecttask.Struts Jan 30, 2010
 */
package org.iita.notifications;

import org.iita.security.model.UserNotification.NotificationFrequency;
import org.iita.security.model.UserNotification.NotificationPriority;

/**
 * <p>
 * Notification sender interface defines single method for message relaying. Most common implementation of the class is {@link JMSNotificationSender} that
 * queues messages into a central JSM queue.
 * </p>
 * 
 * @see JMSNotificationSender
 * @author mobreza
 */
public interface NotificationSender {
	void notify(String recipient, String subject, String body, NotificationFrequency frequency, NotificationPriority priority);
}
