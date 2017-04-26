package org.iita.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.entity.Notification;
import org.iita.security.model.User;
import org.iita.security.model.UserNotification.NotificationFrequency;
import org.iita.security.model.UserNotification.NotificationPriority;
import org.iita.service.EmailService;
import org.iita.service.UserNotificationService;
import org.iita.util.PagedResult;
import org.iita.util.StringUtil;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>Local</b> {@link UserNotificationService} implemenation will use JPA to store and retrieve messages.
 * 
 * @author odada
 */
public class UserNotificationServiceImpl implements UserNotificationService {
	private static final Log log = LogFactory.getLog(UserNotificationServiceImpl.class);

	private EntityManager entityManager;

	private EmailService emailService;

	public Notification findNotification(Long id) {
		return entityManager.find(Notification.class, id);
	}

	/**
	 * @param entityManager the entityManager to set
	 */
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	@Transactional
	public int deleteAllMessages(User user) {
		log.debug("deleteAllMessages " + user);

		return entityManager.createQuery("delete from Notification n where n.subscriber=:user").setParameter("user", user).executeUpdate();
	}

	@Override
	@Transactional
	public void deleteMessage(Notification notification) {
		log.debug("deleteMessage " + notification);

		entityManager.remove(notification);
	}

	@Override
	@Transactional
	public void deleteMessage(Long id) {
		log.debug("deleteMessage " + id);

		try {
			Notification notification = entityManager.find(Notification.class, id);

			if (notification != null) {
				entityManager.remove(notification);
			}
		} catch (Exception e) {
			log.info("No notification with id " + id);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public PagedResult<Notification> listMessages(User user, int startAt, int maxResults, boolean ascending) {
		log.debug("listMessages " + user + " " + startAt + " " + maxResults + " " + ascending);

		String orderBy = "order by id ASC";
		if (!ascending) {
			orderBy = "order by id DESC";
		}

		PagedResult<Notification> result = new PagedResult<Notification>(startAt, maxResults);
		result.setResults(entityManager.createQuery("from Notification n where n.subscriber=:user " + orderBy).setParameter("user", user).getResultList());
		result.setTotalHits((Long) entityManager.createQuery("SELECT count(n) FROM Notification n where n.subscriber=:user").setParameter("user", user)
				.getSingleResult());
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public PagedResult<Notification> listUnread(User user, int startAt, int maxResults, boolean ascending) {
		log.debug("listUnread " + user + " " + startAt + " " + maxResults + " " + ascending);

		String orderBy = "order by id ASC";
		if (!ascending) {
			orderBy = "order by id DESC";
		}

		PagedResult<Notification> result = new PagedResult<Notification>(startAt, maxResults);
		result.setResults(entityManager.createQuery("from Notification n where n.subscriber=:user and n.read=false " + orderBy).setParameter("user", user)
				.getResultList());
		result.setTotalHits((Long) entityManager.createQuery("SELECT count(n) FROM Notification n where n.subscriber=:user and n.read=false").setParameter(
				"user", user).getSingleResult());

		return result;
	}

	@Override
	@Transactional
	public void markAsRead(Notification notification) {
		log.debug("markAsRead " + notification);

		notification.setRead(true);
		entityManager.merge(notification);
	}

	@Override
	@Transactional
	public void markAsUnread(Notification notification) {
		log.debug("markAsUnread " + notification);

		notification.setRead(false);
		entityManager.merge(notification);
	}

	/**
	 * @see org.iita.service.UserNotificationService#sendBroadcast(java.lang.String, java.util.List, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public void sendBroadcast(String appName, List<User> users, String notificationTitle, String notificationMsg) {
		sendBroadcast(appName, users, notificationTitle, notificationMsg, NotificationFrequency.WITHINAPPLICATION, NotificationPriority.NORMAL);
	}

	/**
	 * @see org.iita.service.UserNotificationSender#sendBroadcast(java.lang.String, java.util.List, java.lang.String, java.lang.String,
	 *      org.iita.security.model.UserNotification.NotificationFrequency, org.iita.security.model.UserNotification.NotificationPriority)
	 */
	@Override
	@Transactional
	public void sendBroadcast(String appName, List<User> users, String notificationTitle, String notificationMsg, NotificationFrequency frequency,
			NotificationPriority priority) {
		log.debug("sendBroadcast " + appName + " " + notificationTitle + " " + notificationMsg);

		log.debug("No of users receiving broadcast: " + users.size());

		for (User subscriber : users) {
			sendNotification(appName, subscriber, notificationTitle, notificationMsg, frequency, priority);
		}
	}

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
		if (user == null) {
			log.warn("Subscriber should not be null.");
			return;
		}
		log.debug("sendBroadcast " + user.getDisplayName() + " " + appName + " " + notificationTitle + " " + notificationMsg);

		sendNotification(appName, user, notificationTitle, notificationMsg, frequency, priority);
	}

	/**
	 * @param appName
	 * @param subscriber
	 * @param notificationTitle
	 * @param notificationMsg
	 */

	@Transactional
	private void sendNotification(String appName, User subscriber, String subject, String body, NotificationFrequency frequency, NotificationPriority priority) {
		log.debug("sendNotification " + appName + " " + subscriber.getDisplayName() + " " + subject);
		if (!subscriber.isEnabled()) {
			log.info("User account " + subscriber + " is disabled. Not sending notification.");
			return;
		}
		Notification notif = new Notification();
		notif.setOriginAppName(appName);
		notif.setTitle(subject);
		notif.setMessage(body);
		notif.setSubscriber(subscriber);

		if (notif.getTitle() == null || notif.getTitle().length() == 0)
			notif.setTitle(StringUtil.shortenText(notif.getMessage(), 50, true, true));

		entityManager.persist(notif);
	}

	public long countMessages(User user) {
		log.debug("countMessages " + user);

		return (Long) entityManager.createQuery("SELECT count(n) FROM Notification n where n.subscriber=:user").setParameter("user", user).getSingleResult();
	}

	@Override
	@Transactional(readOnly = true)
	public long countUnread(User user) {
		log.debug("countUnread " + user);

		return (Long) entityManager.createQuery("SELECT count(n) FROM Notification n where n.subscriber=:user and n.read=false").setParameter("user", user)
				.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Notification> listAllMessages(User user) {
		log.debug("listAllMessages " + user);

		return entityManager.createQuery("from Notification n where n.subscriber=:user").setParameter("user", user).getResultList();
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	public EmailService getEmailService() {
		return emailService;
	}
}
