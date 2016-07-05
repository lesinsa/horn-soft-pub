package ru.prbb.common.mail;

import javax.mail.MessagingException;

public interface ISender {
    public void send(String sendTo, String topic, String message, MailSettings mailSettings) throws MessagingException;

    public void send(String sendTo, String topic, String message, byte[] attachment, final MailSettings mailSettings) throws MessagingException;
}
