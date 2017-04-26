package org.iita.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.iita.security.model.User;
import org.iita.util.StringUtil;

/**
 * The notification system uses this entity to persist messages delivered to users to local application database.
 * <p>
 * Notification messages contain a subject line ({@link #getTitle()}), notification body ({@link #getMessage()}) and the recipient ({@link #getSubscriber()}).
 * </p>
 * 
 * @author mobreza
 */
@Entity
public class Notification extends MySqlBaseEntity {
	private static final int SHORT_TITLE_LENGTH = 20;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6809780615604671548L;

	/** The title. */
	private String title;

	/** The message. */
	private String message;

	/** The origin app name. */
	private String originAppName;

	/** The subscriber. */
	private User subscriber;

	/** The read. */
	private boolean read;

	/**
	 * Get message title.
	 * 
	 * @return the title
	 */
	@Column(length = 400)
	public String getTitle() {
		return title;
	}

	/**
	 * Set message title.
	 * 
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the body of notification message.
	 * 
	 * @return the message
	 */
	@Lob
	public String getMessage() {
		return message;
	}

	/**
	 * Sets body of notification message.
	 * 
	 * @param message the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets the origin app name.
	 * 
	 * @return the origin app name
	 */
	@Column(length = 200)
	public String getOriginAppName() {
		return originAppName;
	}

	/**
	 * Sets the origin app name.
	 * 
	 * @param originAppName the new origin app name
	 */
	public void setOriginAppName(String originAppName) {
		this.originAppName = originAppName;
	}

	/**
	 * Gets the subscriber.
	 * 
	 * @return the subscriber
	 */
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "subscriberNotificationId")
	public User getSubscriber() {
		return subscriber;
	}

	/**
	 * Sets the subscriber.
	 * 
	 * @param subscriber the new subscriber
	 */
	public void setSubscriber(User subscriber) {
		this.subscriber = subscriber;
	}

	/**
	 * Sets the flag if message was read or not.
	 * 
	 * @param read the new read
	 */
	public void setRead(boolean read) {
		this.read = read;
	}

	/**
	 * Checks if is flagged as "read"
	 * 
	 * @return true, if is read
	 */
	@Column(name = "`read`")
	public boolean isRead() {
		return read;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return StringUtil.shortenText(title, SHORT_TITLE_LENGTH, true, true);
	}
}
