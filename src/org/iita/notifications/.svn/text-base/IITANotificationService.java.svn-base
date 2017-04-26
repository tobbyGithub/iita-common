/**
 * projecttask.Struts Jan 30, 2010
 */
package org.iita.notifications;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.security.model.User;
import org.iita.security.model.UserNotification.NotificationFrequency;
import org.iita.security.model.UserNotification.NotificationPriority;
import org.iita.service.UserNotificationReader;
import org.iita.service.UserNotificationSender;
import org.iita.service.impl.UserNotificationServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * IITA Notification Service will persist messages locally in addition to sending them to central Notification Server (e.g via MQ) depending on implementation
 * (see {@link NotificationSender}).
 * </p>
 * 
 * @author mobreza
 */
public class IITANotificationService extends UserNotificationServiceImpl implements UserNotificationReader, UserNotificationSender {
	private static final Log LOG = LogFactory.getLog(IITANotificationService.class);
	private NotificationSender sender;

	/**
	 * @param sender A {@link NotificationSender} instance that will proxy notifications for delivery
	 */
	public IITANotificationService(NotificationSender sender) {
		this.sender = sender;
	}

	@Override
	@Transactional
	public void sendBroadcast(String appName, List<User> users, String notificationTitle, String notificationMsg) {
		sendBroadcast(appName, users, notificationTitle, notificationMsg, NotificationFrequency.WITHINAPPLICATION, NotificationPriority.NORMAL);
	}

	/**
	 * @see org.iita.service.UserNotificationService#sendBroadcast(java.lang.String, java.util.List, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public void sendBroadcast(String appName, List<User> users, String notificationTitle, String notificationMsg, NotificationFrequency frequency,
			NotificationPriority priority) {
		// use local implementation to store
		// super.sendBroadcast(appName, users, notificationTitle, notificationMsg, frequency, priority);

		// use sender to dispatch
		for (User user : users) {
			sendBroadcast(null, user, notificationTitle, notificationMsg, frequency, priority);
		}
	}

	/**
	 * @see org.iita.service.UserNotificationService#sendBroadcast(java.lang.String, org.iita.security.model.User, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public void sendBroadcast(String appName, User user, String notificationTitle, String notificationMsg) {
		sendBroadcast(appName, user, notificationTitle, notificationMsg, NotificationFrequency.WITHINAPPLICATION, NotificationPriority.NORMAL);
	}

	/**
	 * @see org.iita.service.UserNotificationSender#sendBroadcast(java.lang.String, org.iita.security.model.User, java.lang.String, java.lang.String,
	 *      org.iita.security.model.UserNotification.NotificationFrequency, org.iita.security.model.UserNotification.NotificationPriority)
	 */
	@Override
	@Transactional
	public void sendBroadcast(String appName, User user, String notificationTitle, String notificationMsg, NotificationFrequency frequency,
			NotificationPriority priority) {

		LOG.debug("Sending frequency " + frequency);

		if (frequency == NotificationFrequency.WITHINAPPLICATION) {
			// use local implementation to store locally
			super.sendBroadcast(appName, user, notificationTitle, notificationMsg, frequency, priority);
		} else {
			// use sender to dispatch
			sender.notify(user.getMail(), notificationTitle, notificationMsg, frequency, priority);
		}
	}
}
