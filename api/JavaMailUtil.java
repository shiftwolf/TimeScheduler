package com.company;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class JavaMailUtil {

    public static void sendMail(String recepient) throws MessagingException {
        Properties prop = new Properties();

        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");

        String username = "schedulertime@gmail.com";
        String password = "timeschedulerWS2021/22";

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recepient));
        message.setSubject("Test");

        String msg = "Test Mail for Java project.";

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    }
}
