/**
 * 
 */
package org.iita.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.service.EmailException;
import org.iita.service.EmailService;
import org.iita.util.StringUtil;

/**
 * Email service provider implemented using JavaMail package. Allows for system-wide overriding of recipient email address by setting the
 * <code>overrideRecipient</code> field.
 * 
 * @author mobreza
 */
public class JavaMail implements EmailService {
	private static final Log log = LogFactory.getLog(JavaMail.class);

	private InternetAddress[] i = {};
	private Properties mailProperties;
	/**
	 * Allows for sending all emails to one email address. Very useful for development.
	 */
	private String overrideRecipient = null;

	/**
	 * Allows for BCC-ing all emails to one email address.
	 */
	private String alwaysBCC = null;

	/**
	 * Set a recipient email address to which ALL emails will be sent. Setting this to a valid value will override the "TO:" setting of any email coming in.
	 * 
	 * @param overrideRecipient Email address to which all emails should be sent.
	 */
	public void setOverrideRecipient(String overrideRecipient) {
		// check for empty strings!
		if (overrideRecipient != null && overrideRecipient.trim().length() == 0)
			overrideRecipient = null;

		this.overrideRecipient = overrideRecipient;
		if (this.overrideRecipient != null)
			log.warn("All emails sent to: " + this.overrideRecipient);
	}

	/**
	 * @param alwaysBCC the alwaysBCC to set
	 */
	public void setAlwaysBCC(String alwaysBCC) {
		// check for empty strings!
		if (alwaysBCC != null && alwaysBCC.trim().length() == 0)
			alwaysBCC = null;

		this.alwaysBCC = alwaysBCC;
		if (this.alwaysBCC != null)
			log.warn("All emails BCC'd to: " + this.alwaysBCC);
	}

	/**
	 * @return the mailProperties
	 */
	public Properties getMailProperties() {
		return this.mailProperties;
	}

	/**
	 * @param mailProperties the mailProperties to set
	 */
	public void setMailProperties(Properties mailProperties) {
		this.mailProperties = mailProperties;
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.service.EmailService#sendEmail(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void sendEmail(String sender, String recipient, String subject, String body) throws EmailException {
		this.sendEmail(sender, new String[] { recipient }, new String[] {}, subject, body);
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.service.EmailService#sendEmailWithAttachment(java.lang.String , java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * byte[])
	 */
	@Override
	public void sendEmailWithAttachment(String sender, String recipient, Collection<String> cc, String subject, String body, String attachmentName,
			byte[] attachmentData) throws EmailException {
		log.info("Sending email from " + sender + " to " + recipient + ": " + subject);

		if (overrideRecipient != null) {
			recipient = overrideRecipient;
			cc = null;
			log.info("Overriding recipient to " + recipient + " and clearing any CCs");
		}

		javax.mail.Session session = javax.mail.Session.getInstance(mailProperties);
		MimeMessage msg = new MimeMessage(session);
		try {
			if (sender != null)
				msg.setFrom(new javax.mail.internet.InternetAddress(sender));
			else if (mailProperties.containsKey("mail.sender.default"))
				msg.setFrom(new javax.mail.internet.InternetAddress(mailProperties.getProperty("mail.sender.default")));

			msg.setRecipients(RecipientType.TO, javax.mail.internet.InternetAddress.parse(recipient, false));

			if (cc != null) {
				ArrayList<InternetAddress> ccRecipientAddrs = new ArrayList<InternetAddress>();
				for (String ccRecipient : cc) {
					if (ccRecipient == null || ccRecipient.trim().length() == 0)
						continue;
					ccRecipientAddrs.add(new InternetAddress(ccRecipient, false));
				}
				if (ccRecipientAddrs.size() > 0)
					msg.setRecipients(RecipientType.CC, ccRecipientAddrs.toArray(i));
			}

			// if alwaysBCC is configured, send to that person
			if (this.alwaysBCC != null) {
				log.warn("Sending blind carbon copy to: " + this.alwaysBCC);
				msg.setRecipient(RecipientType.BCC, new InternetAddress(this.alwaysBCC, false));
			}

			msg.setSubject(subject);

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message
			messageBodyPart.setContent(body, "text/plain; charset=UTF-8");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			messageBodyPart = new MimeBodyPart();
			DataSource fileDataSource1 = new ByteArrayDataSource(attachmentData, "application/pdf");
			messageBodyPart.setDataHandler(new DataHandler(fileDataSource1));
			messageBodyPart.setFileName(attachmentName);
			multipart.addBodyPart(messageBodyPart);

			// Put parts in message
			msg.setContent(multipart);

			// Foo
			msg.setSentDate(new Date());
			Transport tr = session.getTransport(mailProperties.getProperty("transport"));
			
			if (mailProperties.getProperty("mail.smtp.auth").equalsIgnoreCase("true")) {
				tr.connect(mailProperties.getProperty("mail.smtp.host"), mailProperties.getProperty("mail.smtp.user"), mailProperties
						.getProperty("mail.smtp.password"));
			} else
				tr.connect();
			
			//tr.connect(mailProperties.getProperty("mail.smtp.host"), mailProperties.getProperty("mail.smtp.user"), mailProperties.getProperty("mail.smtp.password"));
			msg.saveChanges(); // don't forget this
			tr.sendMessage(msg, msg.getAllRecipients());
			tr.close();
			log.info("Email sent to " + recipient);
		} catch (AddressException e) {
			log.error(e.getMessage(), e);
			throw new EmailException(e.getMessage(), e);
		} catch (MessagingException e) {
			log.error(e.getMessage(), e);
			throw new EmailException(e.getMessage(), e);
		}
	}

	/**
	 * @see org.iita.service.EmailService#sendEmail(java.lang.String, java.lang.String[], java.lang.String[], java.lang.String, java.lang.String)
	 */
	@Override
	public void sendEmail(String sender, String[] recipients, String[] ccRecipients, String subject, String body) throws EmailException {
		// call with null HTML body
		sendEmail(sender, recipients, ccRecipients, subject, body, null);
	}

	/**
	 * @throws EmailException
	 * @see org.iita.service.EmailService#sendEmail(java.lang.String, java.lang.String[], org.iita.security.model.User, java.lang.String, java.lang.String)
	 */
	@Override
	public void sendEmail(String sender, String[] recipients, String[] ccRecipients, String subject, String body, String htmlBody) throws EmailException {
		if (body == null && htmlBody == null) {
			log.warn("Not sending email to " + StringUtil.arrayToString(recipients) + ", body is null");
			return;
		}
		log.info("Sending email from " + sender + " to " + StringUtil.arrayToString(recipients) + ": " + subject);

		if (overrideRecipient != null) {
			log.info("Overriding recipient to " + overrideRecipient);
			subject = "[>> " + StringUtil.arrayToString(recipients) + "] " + (subject == null ? StringUtil.EMPTY : subject);
			recipients = new String[] { overrideRecipient };
		}

		if (recipients == null || recipients.length == 0) {
			log.warn("Not sending email from " + sender + ", recipient is null");
			return;
		}

		javax.mail.Session session = javax.mail.Session.getInstance(mailProperties);
		javax.mail.Message msg = new javax.mail.internet.MimeMessage(session);
		try {
			if (sender != null)
				msg.setFrom(new InternetAddress(sender));
			else if (mailProperties.containsKey("mail.sender.default"))
				msg.setFrom(new InternetAddress(mailProperties.getProperty("mail.sender.default")));

			ArrayList<InternetAddress> recipientAddrs = new ArrayList<InternetAddress>();
			if (recipients != null)
				for (String recipient : recipients) {
					recipientAddrs.add(new InternetAddress(recipient, false));
				}
			if (recipientAddrs.size() > 0)
				msg.setRecipients(RecipientType.TO, recipientAddrs.toArray(i));

			recipientAddrs.clear();
			if (ccRecipients != null)
				for (String recipient : ccRecipients) {
					recipientAddrs.add(new InternetAddress(recipient, false));
				}

			if (recipientAddrs.size() > 0)
				msg.setRecipients(RecipientType.CC, recipientAddrs.toArray(i));

			// if alwaysBCC is configured, send to that person
			if (this.alwaysBCC != null) {
				log.warn("Sending blind carbon copy to: " + this.alwaysBCC);
				msg.setRecipient(RecipientType.BCC, new InternetAddress(this.alwaysBCC, false));
			}

			msg.setSubject(subject);
			msg.setSentDate(new Date());

			// construct email body
			if (htmlBody != null) {
				MimeMultipart content = new MimeMultipart("alternative");
				MimeBodyPart html = new MimeBodyPart();
				html.setContent(htmlBody, "text/html");
				content.addBodyPart(html);

				// if have text only version
				if (body != null) {
					MimeBodyPart text = new MimeBodyPart();
					text.setText(body, "UTF-8");
					content.addBodyPart(text);
				}

				msg.setContent(content);
			} else {
				msg.setContent(body, "text/plain; charset=UTF-8");
			}

			Transport tr = session.getTransport(mailProperties.getProperty("transport"));
			if (mailProperties.getProperty("mail.smtp.auth").equalsIgnoreCase("true")) {
				tr.connect(mailProperties.getProperty("mail.smtp.host"), mailProperties.getProperty("mail.smtp.user"), mailProperties
						.getProperty("mail.smtp.password"));
			} else
				tr.connect();

			msg.saveChanges(); // don't forget this
			tr.sendMessage(msg, msg.getAllRecipients());
			tr.close();
			log.debug("Email sent to " + StringUtil.arrayToString(recipients));
		} catch (AddressException e) {
			log.error(e.getMessage(), e);
			throw new EmailException(e.getMessage(), e);
		} catch (MessagingException e) {
			log.error(e.getMessage(), e);
			throw new EmailException(e.getMessage(), e);
		}
	}
	
	/**
	 * @throws EmailException
	 * @see org.iita.service.EmailService#sendEmail(java.lang.String, java.lang.String[], org.iita.security.model.User, java.lang.String, java.lang.String)
	 */
	@Override
	public void sendEmailWithAttachment(String sender, String recipient, Collection<String> cc, String subject, String body, String htmlBody, String attachmentName,
			byte[] attachmentData) throws EmailException {
		if (body == null && htmlBody == null) {
			log.warn("Not sending email to " + recipient + ", body is null");
			return;
		}
		log.info("Sending email from " + sender + " to " + recipient + ": " + subject);

		if (overrideRecipient != null) {
			log.info("Overriding recipient to " + overrideRecipient);
			subject = "[>> " + recipient + "] " + (subject == null ? StringUtil.EMPTY : subject);
			recipient = overrideRecipient;
		}

		if (recipient == null) {
			log.warn("Not sending email from " + sender + ", recipient is null");
			return;
		}

		javax.mail.Session session = javax.mail.Session.getInstance(mailProperties);
		javax.mail.Message msg = new javax.mail.internet.MimeMessage(session);
		try {
			if (sender != null)
				msg.setFrom(new InternetAddress(sender));
			else if (mailProperties.containsKey("mail.sender.default"))
				msg.setFrom(new InternetAddress(mailProperties.getProperty("mail.sender.default")));

			ArrayList<InternetAddress> recipientAddrs = new ArrayList<InternetAddress>();
			//if (recipients != null)
				//for (String recipient : recipients) {
					recipientAddrs.add(new InternetAddress(recipient, false));
				//}
			if (recipientAddrs.size() > 0)
				msg.setRecipients(RecipientType.TO, recipientAddrs.toArray(i));

			recipientAddrs.clear();
			if (cc != null)
				for (String ccRecipient : cc) {
					recipientAddrs.add(new InternetAddress(ccRecipient, false));
				}

			if (recipientAddrs.size() > 0)
				msg.setRecipients(RecipientType.CC, recipientAddrs.toArray(i));

			// if alwaysBCC is configured, send to that person
			if (this.alwaysBCC != null) {
				log.warn("Sending blind carbon copy to: " + this.alwaysBCC);
				msg.setRecipient(RecipientType.BCC, new InternetAddress(this.alwaysBCC, false));
			}

			msg.setSubject(subject);
			msg.setSentDate(new Date());

			// construct email body
			if (htmlBody != null) {
				MimeMultipart content = new MimeMultipart("alternative");
				MimeBodyPart html = new MimeBodyPart();
				html.setContent(htmlBody, "text/html");
				content.addBodyPart(html);

				// if have text only version
				if (body != null) {
					MimeBodyPart text = new MimeBodyPart();
					text.setText(body, "UTF-8");
					content.addBodyPart(text);
				}
				
				// Part two is attachment
				html = new MimeBodyPart();
				DataSource fileDataSource1 = new ByteArrayDataSource(attachmentData, "application/pdf");
				html.setDataHandler(new DataHandler(fileDataSource1));
				html.setFileName(attachmentName);
				content.addBodyPart(html);
				
				msg.setContent(content);
			} else {
				msg.setContent(body, "text/plain; charset=UTF-8");
			}

			Transport tr = session.getTransport(mailProperties.getProperty("transport"));
			if (mailProperties.getProperty("mail.smtp.auth").equalsIgnoreCase("true")) {
				tr.connect(mailProperties.getProperty("mail.smtp.host"), mailProperties.getProperty("mail.smtp.user"), mailProperties
						.getProperty("mail.smtp.password"));
			} else
				tr.connect();

			msg.saveChanges(); // don't forget this
			tr.sendMessage(msg, msg.getAllRecipients());
			tr.close();
			log.debug("Email sent to " + recipient);
		} catch (AddressException e) {
			log.error(e.getMessage(), e);
			throw new EmailException(e.getMessage(), e);
		} catch (MessagingException e) {
			log.error(e.getMessage(), e);
			throw new EmailException(e.getMessage(), e);
		}
	}
}
