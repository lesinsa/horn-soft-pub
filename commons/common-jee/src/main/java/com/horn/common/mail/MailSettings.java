package com.horn.common.mail;

public class MailSettings {
    private String mailFrom;
    private String smtpHostName;
    private String smtpAuthUser;
    private String smtpAuthPass;

    public MailSettings(String mailFrom, String smtpHostName, String smtpAuthUser, String smtpAuthPass) {
        this.mailFrom = mailFrom;
        this.smtpHostName = smtpHostName;
        this.smtpAuthUser = smtpAuthUser;
        this.smtpAuthPass = smtpAuthPass;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public void setSmtpHostName(String smtpHostName) {
        this.smtpHostName = smtpHostName;
    }

    public void setSmtpAuthUser(String smtpAuthUser) {
        this.smtpAuthUser = smtpAuthUser;
    }

    public void setSmtpAuthPass(String smtpAuthPass) {
        this.smtpAuthPass = smtpAuthPass;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public String getSmtpHostName() {
        return smtpHostName;
    }

    public String getSmtpAuthUser() {
        return smtpAuthUser;
    }

    public String getSmtpAuthPass() {
        return smtpAuthPass;
    }
}
