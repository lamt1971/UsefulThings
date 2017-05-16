package com.electrocomponents.commons.email;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * Replaces internal Sun SMPT implementation with javax.mail equivalent.
 * 
 * This may need to be reworked into a JEE6 singleton bean with an injected mail Session
 * which uses the server's jboss configuration - i.e.
 * 
 * @Resource(name = "java:/Mail")  
 *   private Session mailSession;  
 *   
 * @author Jim Britton
 *
 */
public class SendMailLite {

	/** MIME content-type of the email. */
	private static final String DEFAULT_CONTENT_TYPE = "text/html";
	
	private static final String DEFAULT_SMPT_HOST_PROPERTY_NAME = "common.mail.smtp.host";
	
	private static final Log LOG = LogFactory.getLog(SendMailLite.class);
	
	private SendMailLite() {};
	
	/**
     * Sends an email via the provided SMTP host, using the default ("text/html") message content type.
     * 
     * @param mailServer the SMTP host or null to use the default host (assuming it's configured).
     * @param recipient
     * @param from
     * @param subject
     * @param message
     */
    public static void send(String mailServer, String recipient, String from, String subject, String message) {
        send(mailServer, new String[] {recipient}, null, null, from, subject, message, DEFAULT_CONTENT_TYPE, null, null);
    }

	/**
	 * Sends an email via the provided SMTP host, using the default ("text/html") message content type.
	 * 
	 * @param mailServer the SMTP host.
	 * @param recipients
	 * @param ccRecipients optional array of cc recipients
	 * @param bccRecipients optional array of bcc recipients
	 * @param from
	 * @param subject
	 * @param message
	 */
	public static void send(String mailServer, String recipients[], String[] ccRecipients, String[] bccRecipients, 
            String from, String subject, String message) {
	    send(mailServer, recipients, ccRecipients, bccRecipients, from, subject, message, DEFAULT_CONTENT_TYPE, null, null);
	}
	
	/**
	 * Sends an email via the provided SMTP host.
	 * 
	 * @param mailServer the SMTP host.
	 * @param recipients
     * @param ccRecipients optional array of cc recipients
     * @param bccRecipients optional array of bcc recipients
	 * @param from
	 * @param subject
	 * @param message
	 * @param contentType MIME message content type
	 * @param charSet message character set
	 * @param headers optional map of name / value pairs for additional message headers
	 */
	public static void send(String mailServer, String recipients[], String[] ccRecipients, String[] bccRecipients, 
	        String from, String subject, String message, String contentType, String charSet, 
	        Map<String, String> headers) {

	    try {
	        if (StringUtils.isBlank(mailServer)) {
	            mailServer = System.getProperty(DEFAULT_SMPT_HOST_PROPERTY_NAME);
	        }
            //Set the host smtp address
            Properties props = new Properties();
            props.put("mail.smtp.host", mailServer);
    
            // create some properties and get the default Session
            Session session = Session.getDefaultInstance(props, null);
            session.setDebug(false);
    
            // create a message
            Message msg = new MimeMessage(session);
    
            // set the from and to address
            msg.setFrom(new InternetAddress(from));
            
            for (String recipient : recipients) {
                msg.addRecipient(RecipientType.TO, new InternetAddress(recipient));      
            }
            if (ccRecipients != null) {
                for (String ccRecipient : ccRecipients) {
                    msg.addRecipient(RecipientType.CC, new InternetAddress(ccRecipient));      
                }
            }
            if (bccRecipients != null) {
                for (String bccRecipient : bccRecipients) {
                    msg.addRecipient(RecipientType.BCC, new InternetAddress(bccRecipient));      
                }
            }
    
            if (!StringUtils.isBlank(charSet)) {
                contentType += "; charset=" + charSet;
                msg.addHeader("Content-Transfer-Encoding", "quoted-printable");
            }
            msg.addHeader("Content-Type", contentType);
            
            if (headers != null) {
                for (Entry<String, String> entry: headers.entrySet()) {
                    msg.addHeader(entry.getKey(), entry.getValue());
                }
            }
            
            msg.setSubject(subject);
            msg.setText(message);
            Transport.send(msg);
	    } catch (Exception e) {
	        // to preserve the old API where the clients made no attempt to handle mail sending errors, we 
	        // just log and swallow this.
	        
	        // TODO: throw an error to the caller and expect it to handle this.
	        LOG.error("Error sending mail", e);
	    }
    }
}
