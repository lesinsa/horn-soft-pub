package com.horn.common.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

/**
 * Отправка писем
 */
@ApplicationScoped
public class MailSender implements ISender {

    private static final Logger LOG = LoggerFactory.getLogger(MailSender.class);

    @Override
    public void send(String sendTo, String topic, String message, MailSettings mailSettings) throws MessagingException {
        sendMail(sendTo, topic, message, null, mailSettings);
    }

    @Override
    public void send(String sendTo, String topic, String message, byte[] attachment, MailSettings mailSettings) throws MessagingException {
        sendMail(sendTo, topic, message, attachment, mailSettings);
    }

    private void sendMail(String sendTo, String topic, String message, byte[] attachment, final MailSettings mailSettings) throws MessagingException {
        LOG.debug("Sending email: sendTo={}, topic={}, message={}", sendTo, topic, message);
        Session mailSession = Session.getInstance(getProperties(mailSettings),
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                mailSettings.getSmtpAuthUser(),
                                mailSettings.getSmtpAuthPass());
                    }
                }
        );

        MimeMessage mimeMessage = new MimeMessage(mailSession);
        mimeMessage.setFrom(new InternetAddress(mailSettings.getMailFrom()));
        InternetAddress[] addressTo = InternetAddress.parse(sendTo);
        mimeMessage.setRecipients(Message.RecipientType.TO, addressTo);
        mimeMessage.setSubject(topic);

        Multipart mp = new MimeMultipart();

        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(message, "text/html; charset=UTF-8");
        mp.addBodyPart(htmlPart);
        if (attachment != null) {
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setFileName("attachment.jpg");
            mimeBodyPart.setContent(attachment, "application/jpg");
            mp.addBodyPart(mimeBodyPart);
        }
        mimeMessage.setContent(mp);
        Transport.send(mimeMessage);
        LOG.debug("Message to {} is sent successfully", sendTo);
    }

    private Properties getProperties(MailSettings mailSettings) {
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.host", mailSettings.getSmtpHostName());
        properties.put("mail.smtp.auth", "true");
        return properties;
    }
}
