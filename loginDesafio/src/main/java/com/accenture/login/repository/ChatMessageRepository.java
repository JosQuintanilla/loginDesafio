package com.accenture.login.repository;

import com.accenture.login.model.ChatMessage;
import com.accenture.login.model.ChatMessages;

public interface ChatMessageRepository {

	void save(ChatMessage message);
    ChatMessages loadChannelMessages();

}