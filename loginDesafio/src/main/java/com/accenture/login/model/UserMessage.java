package com.accenture.login.model;

import java.util.Date;

public class UserMessage {

	private String content;
    private String sender;
    private Date sendDate;

    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    public Date getSendDate() {
        return sendDate;
    }
}