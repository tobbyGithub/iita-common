/**
 * projecttask.Struts Jan 30, 2010
 */
package org.iita.notifications;

import java.util.Date;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.security.model.UserNotification.NotificationFrequency;
import org.iita.security.model.UserNotification.NotificationPriority;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * <p>
 * This {@link NotificationSender} will deliver messages to a central JMS queue. Usually anoter application will pick up messages from the queue and handle
 * actual delivery of notifications.
 * </p>
 * 
 * @author mobreza
 */
public class JMSNotificationSender implements NotificationSender {
	private static final Log LOG = LogFactory.getLog(JMSNotificationSender.class);
	private String sender = null;

	/** The template. */
	private JmsTemplate template;

	/** The destination. */
	private Destination destination;

	/**
	 * @param sender
	 */
	public JMSNotificationSender(String sender) {
		this.sender = sender;
	}

	/**
	 * @param destination the destination to set
	 */
	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	/**
	 * @param template the template to set
	 */
	public void setTemplate(JmsTemplate template) {
		this.template = template;
	}

	/**
	 * @param frequency
	 * @param priority
	 * @see org.iita.notifications.client.NotificationSender#notify(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void notify(final String recipient, final String subject, final String body, NotificationFrequency frequency, NotificationPriority priority) {
		LOG.debug("Sending notification via JMS to " + destination);
		LOG.debug("Sender: " + this.sender);
		LOG.debug("Recipient:" + recipient);
		LOG.debug("Subject: " + subject);

		template.send(destination, new MyMessageCreator(sender, recipient, subject, body, frequency, priority));
	}

	private class MyMessageCreator implements MessageCreator {
		private String subject;
		private String body;
		private String recipient;
		private String sender;
		private NotificationPriority priority;
		private NotificationFrequency frequency;

		public MyMessageCreator(String sender, String recipient, String subject, String body, NotificationFrequency frequency, NotificationPriority priority) {
			this.sender = sender;
			this.recipient = recipient;
			this.subject = subject;
			this.body = body;
			this.frequency = frequency;
			this.priority = priority;
		}

		public Message createMessage(Session session) throws JMSException {
			LOG.debug("Sending " + destination.toString());
			TextMessage message = session.createTextMessage("Notification message");
			// set required data properties
			message.setStringProperty("sender", this.sender);
			message.setStringProperty("recipient", this.recipient);
			message.setStringProperty("subject", this.subject);
			message.setStringProperty("body", this.body);
			if (this.frequency != null)
				message.setStringProperty("frequency", this.frequency.name());
			if (this.priority != null)
				message.setStringProperty("priority", this.priority.name());

			// other stuffs
			message.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
			message.setJMSTimestamp(new Date().getTime());
			return message;
		}
	}
}
