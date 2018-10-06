package com.accenture.login.model;

import java.awt.TrayIcon.MessageType;
import java.util.Date;

public class ChatMessage {

	private final String content;
    private final String sender;
    private final Date sendDate;

    public ChatMessage(String content, String sender, Date sendDate) {
        this.content = content;
        this.sender = sender;
        this.sendDate = sendDate;
    }

    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public MessageType getType(){
        return MessageType.NONE;
    }
}