/**
 * projecttask.Struts Jan 30, 2010
 */
package org.iita.notifications;

import java.util.ArrayList;
import java.util.List;

import org.iita.security.model.User;
import org.iita.security.model.UserNotification;

/**
 * <p>
 * The object holds the pre-processed notification data. {@link ApplicationEventNotification}s are generated in ApplicationNotification classes and eventually
 * sent to subscribed users (see {@link UserNotification}) that are listed in {@link #allowedSubscribers}.
 * </p>
 * 
 * @author mobreza
 */
public class ApplicationEventNotification {

	/**
	 * The Enum EventNotificationType.
	 */
	public enum EventNotificationType {
		/** SINGLE notification */
		SINGLE,
		/** MULTIPLE notification */
		MULTIPLE,
		/** BROADCAST notifications */
		BROADCAST;
	}

	/** The params. */
	private Object[] params;

	/** The default format. */
	private String defaultFormat;

	/** The event name. */
	private String eventName;

	/** The subject. */
	private String subject;

	/** The allowed subscribers. */
	private List<User> allowedSubscribers;

	/** The notitication type. */
	private EventNotificationType notiticationType;

	private User sender;

	/**
	 * The Constructor.
	 * 
	 * @param eventName the event name
	 * @param defaultFormat the default format
	 * @param params the params
	 * @param subject the subject
	 */
	public ApplicationEventNotification(User sender, String eventName, String subject, String defaultFormat, Object[] params) {
		this.sender = sender;
		this.eventName = eventName;
		this.defaultFormat = defaultFormat;
		this.params = params;
		this.subject = subject;
		this.notiticationType = EventNotificationType.BROADCAST;
	}

	/**
	 * The Constructor.
	 * 
	 * @param allowedSubscribers the allowed subscribers
	 * @param eventName the event name
	 * @param subject the subject
	 * @param defaultFormat the default format
	 * @param params the params
	 */
	public ApplicationEventNotification(User sender, String eventName, List<User> allowedSubscribers, String subject, String defaultFormat, Object[] params) {
		this.sender = sender;
		this.eventName = eventName;
		this.allowedSubscribers = allowedSubscribers;
		this.subject = subject;
		this.defaultFormat = defaultFormat;
		this.params = params;
		this.notiticationType = EventNotificationType.MULTIPLE;
	}

	/**
	 * The Constructor.
	 * 
	 * @param singleSubscriber the single subscriber
	 * @param eventName the event name
	 * @param subject the subject
	 * @param defaultFormat the default format
	 * @param params the params
	 * @param sender
	 */
	public ApplicationEventNotification(User sender, String eventName, User singleSubscriber, String subject, String defaultFormat, Object[] params) {
		this.sender = sender;
		this.eventName = eventName;
		this.allowedSubscribers = new ArrayList<User>();
		this.allowedSubscribers.add(singleSubscriber);
		this.subject = subject;
		this.defaultFormat = defaultFormat;
		this.params = params;
		this.notiticationType = EventNotificationType.SINGLE;
	}

	/**
	 * Gets the params.
	 * 
	 * @return the params
	 */
	public Object[] getParams() {
		return this.params;
	}

	/**
	 * Gets the default format.
	 * 
	 * @return the defaultFormat
	 */
	public String getDefaultFormat() {
		return this.defaultFormat;
	}

	/**
	 * Gets the event name.
	 * 
	 * @return the eventName
	 */
	public String getEventName() {
		return this.eventName;
	}

	/**
	 * Gets the subject.
	 * 
	 * @return the subject
	 */
	public String getSubject() {
		return this.subject;
	}

	/**
	 * Gets the allowed subscribers.
	 * 
	 * @return the allowedSubscribers
	 */
	public List<User> getAllowedSubscribers() {
		return this.allowedSubscribers;
	}

	/**
	 * Gets the notitication type.
	 * 
	 * @return the notiticationType
	 */
	public EventNotificationType getNotiticationType() {
		return this.notiticationType;
	}

	/**
	 * @return
	 */
	public User getSender() {
		return this.sender;
	}
}
