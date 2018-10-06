package com.accenture.login.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.accenture.login.model.ChatMessage;
import com.accenture.login.model.UserMessage;
import com.accenture.login.repository.ChatMessageRepository;

public class ChatMessageProcessor {

	private final ChatMessageRepository chatMessageRepository;


    @Autowired
    public ChatMessageProcessor(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public ChatMessage process(UserMessage rawMessage){
        ChatMessage message = new ChatMessage(rawMessage.getContent(), rawMessage.getSender(), rawMessage.getSendDate());
        chatMessageRepository.save(message);
        return message;
    }
}