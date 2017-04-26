/**
 * projecttask.Struts Jan 30, 2010
 */
package org.iita.security.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import org.iita.entity.VersionedEntity;

/**
 * <p>
 * The entity contains {@link User}'s subscription to application event. A single user may subscribe to various application events.
 * </p>
 * <p>
 * {@link #eventName} is the method name within a notification class
 * </p>
 * 
 * @author mobreza
 */
@Entity
public class UserNotification extends VersionedEntity {
	private static final long serialVersionUID = -4995926281558627351L;
	private User user;
	private String eventName;
	private String formatString;
	private boolean subscribed = false;
	private NotificationFrequency frequency = NotificationFrequency.WITHINAPPLICATION;
	private NotificationPriority priority = NotificationPriority.NORMAL;

	/**
	 * Notification frequencies define the delivery preference class: instant notifications should be delivered the moment they occur, daily and monthly should
	 * be delivered as part of a digest message sent to user daily/monthly.
	 * 
	 * @author mobreza
	 */
	public enum NotificationFrequency {
		/** Deliver as local message within application */
		WITHINAPPLICATION,
		/** Notification delivered instantly to subscriber */
		INSTANT,
		/** Notification delivered as part of daily digest */
		DAILY,
		/** Notification delivered as part of monthly digest */
		MONTHLY;
	}

	/**
	 * Notifications can be classified to three different priority classes.
	 * 
	 * @author mobreza
	 */
	public enum NotificationPriority {
		/** Low priority message */
		LOW,
		/** Normal priority message */
		NORMAL,
		/** High priority message (e.g. delivered via SMS) */
		HIGH
	}

	/**
	 * Get subscriber
	 * 
	 * @return the user
	 */
	@ManyToOne(cascade = {}, optional = false)
	public User getUser() {
		return this.user;
	}

	/**
	 * Set subscriber
	 * 
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Get method name within a notification class that describes the event
	 * 
	 * @return the eventName
	 */
	@Column(length = 200, nullable = false)
	public String getEventName() {
		return this.eventName;
	}

	/**
	 * @param eventName the eventName to set
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	/**
	 * Get custom format string that should be used to format sent message body
	 * 
	 * @return the formatString
	 */
	@Column(length = 1000, nullable = true)
	public String getFormatString() {
		return this.formatString;
	}

	/**
	 * Set custom format string, a <code>null</code> value will use default format string of notification
	 * 
	 * @param formatString the formatString to set
	 */
	public void setFormatString(String formatString) {
		this.formatString = formatString;
	}

	/**
	 * @return the subscribed
	 */
	public boolean isSubscribed() {
		return this.subscribed;
	}

	/**
	 * @param subscribed the subscribed to set
	 */
	public void setSubscribed(boolean subscribed) {
		this.subscribed = subscribed;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.eventName + (subscribed ? " subscribed" : " not subscribed");
	}

	/**
	 * @return the frequency
	 */
	@Enumerated(EnumType.STRING)
	@Column(length = 100)
	public NotificationFrequency getFrequency() {
		return this.frequency;
	}

	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(NotificationFrequency frequency) {
		this.frequency = frequency;
	}

	/**
	 * @return the priority
	 */
	@Enumerated(EnumType.STRING)
	@Column(length = 100)
	public NotificationPriority getPriority() {
		return this.priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(NotificationPriority priority) {
		this.priority = priority;
	}
}
