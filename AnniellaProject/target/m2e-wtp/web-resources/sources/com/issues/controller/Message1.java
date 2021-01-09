package com.issues.controller;







import java.util.Properties;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.issues.model.Customer;

public class Message1 {
	private final String sender = "MtnRwandaResponse";
	private final String password = "fred18404";
	

	public void successMessage(String details,String message) {
		FacesContext ct = FacesContext.getCurrentInstance();
		ct.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,details,message));
	}
	public void errorMessage(String details,String message) {
		FacesContext ct = FacesContext.getCurrentInstance();
		ct.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,details,message));
	
	}
	
	@SuppressWarnings("unused")
	public boolean validateId(final String id, final String dob) {
		if (!id.isEmpty() || id.length() > 0 || id != null || id != "") {
			if (id.startsWith("1")) {
				if (id.trim().replace(" ", "").length() == 16) {
					String check = id.trim().replace(" ", "").substring(1, 5);
					String val[] = dob.split("/");
					if (check.equals(val[2])) {
						return true;
					} else {
						return false;
					}

				} else {
					return false;
				}
			} else {
				return false;
			}
		} else
			return false;
	}
	
	public boolean validatePhone(String phone) {
		if (phone.length() == 10) {
			if (phone.startsWith("078")) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public void sendMessage(Customer customer, String message1, String subject) {
		try {
			

				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(
						"<h1 style=font-weight: bold; color: maroon;><center>MTN RWANDA </center></h1></center><br />");
				stringBuilder.append("Hello ,"  +customer.getFirstName()+" "+customer.getLastName()  + "<br />");
				stringBuilder.append(
						"I Hope this message will find you well!! :<br /><br />");
				stringBuilder.append("<b>Subject:</b>&nbsp;&nbsp;"+subject + "<br />");
				stringBuilder.append("<b>Issues answer:</b>&nbsp;&nbsp;"+message1 + "<br />");
				stringBuilder.append("<br /><br /> MTN RWANDA we thank you for being with us!!");
				String emailMessage = stringBuilder.toString();

				// Assuming you are sending email through relay.jangosmtp.net
				String host = "smtp.gmail.com";

				Properties props = new Properties();
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.host", host);
				props.put("mail.smtp.port", "587");

				// Get the Session object.
				Session session = Session.getInstance(props, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(sender, password);
					}
				});

				// Create a default MimeMessage object.
				MimeMessage message = new MimeMessage(session);

				// Set From: header field of the header.
				message.setFrom(new InternetAddress(sender));

				// Set To: header field of the header.
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(customer.getEmail()));

				// Set Subject: header field
				message.setSubject("MTN RWANDA Reponse for your Issues!");

				// Now set the actual message

				message.setContent(emailMessage, "text/html");
				// Send message
				Transport.send(message);
			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "."));
			e.printStackTrace();
		} finally {
		}
}
	
	public String getSender() {
		return sender;
	}
	public String getPassword() {
		return password;
	}
	
	
}
