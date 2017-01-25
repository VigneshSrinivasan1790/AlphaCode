package com.alpha.util;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;

public class MailerUtil {
	Logger logger = Logger.getLogger(MailerUtil.class.getName());

	public static final String FROM_MAILER = "support@alignmygoals.com";
	public static final String FROM_PASSWORD = "support123*";
	public static final int MAIL_PORT = 465;
	public static final String MAIL_HOST = "host3.dns2dns.com";
	public static final String MAIL_SUCCESS = "Mail Sent";
	public static final String MAIL_FAILURE = "Mail Sending failed";


	public String sendMailTo(String name,String contactNo, String mailId, String clientMessage) {
		logger.info(String.format("Sending mail to %s.", name ));
		try {
			String host = MAIL_HOST;
			String address = FROM_MAILER; 
			String from = FROM_MAILER; 
			String pass = FROM_PASSWORD; 
			String to = mailId;
			Multipart multiPart; 
			String finalString=""; 
			Properties props = System.getProperties(); 
			//props.put("mail.smtp.starttls.enable", "true"); 
			props.put("mail.smtp.host", host); 
			props.put("mail.smtp.user", address); 
			props.put("mail.smtp.password", pass); 
			props.put("mail.smtp.port", MAIL_PORT); 
			props.put("mail.smtp.auth", "true");
			Session session = Session.getInstance(props, new GMailAuthenticator(from, pass));
			DataHandler handler=new DataHandler(new ByteArrayDataSource(finalString.getBytes(),"text/plain" )); 
			MimeMessage message = new MimeMessage(session); 
			message.setFrom(new InternetAddress(from)); 
			message.setDataHandler(handler); 
			multiPart=new MimeMultipart(); 
			InternetAddress toAddress; 
			toAddress = new InternetAddress(to); 
			message.addRecipient(Message.RecipientType.TO, toAddress); 
			message.setSubject("[DO NOT REPLY] - User Activation : AlignMyGoals"); 
			message.setContent(multiPart);
			message.setText("Hi : " + name + ",<br/><br/>" + "Click on the below link to activate your account with AlignMyGoals.com " +contactNo + 
					"<br/><br/>" +
					"Activation Link: " + clientMessage +
					"<br/><br/>" +
					"Thanks," +
					"<br/><br/>" +
					"Support <br/>" +
					"AlignMyGoals"); 
			Transport transport = session.getTransport("smtp");
			transport.connect(host,address , pass); 
			transport.sendMessage(message, message.getAllRecipients()); 
			transport.close(); 
			return MAIL_SUCCESS;
		}
		catch (Exception e) {
			e.printStackTrace();
			return MAIL_FAILURE;
		}
	}
	
	class GMailAuthenticator extends Authenticator {
		String user;
		String pw;
		public GMailAuthenticator (String username, String password)
		{
		super();
		this.user = username;
		this.pw = password;
		}
		public PasswordAuthentication getPasswordAuthentication()
		{
		return new PasswordAuthentication(user, pw);
		}
		}

}
