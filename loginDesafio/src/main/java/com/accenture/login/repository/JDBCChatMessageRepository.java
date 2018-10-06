package com.accenture.login.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.accenture.login.model.ChatMessage;
import com.accenture.login.model.ChatMessages;

@Repository("chatMessageRepository")
public class JDBCChatMessageRepository implements ChatMessageRepository {

    static final String CreateQuery = "insert into message (content, sender, sendDate) values (?, ?, ?)";
    static final String FindAllQuery = "select content, sender, sendDate from message";
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JDBCChatMessageRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void save(ChatMessage message) {
        try{
         jdbcTemplate.update(CreateQuery,
            message.getContent(),
            message.getSender(),
            message.getSendDate());
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    
    public List<ChatMessages> allMessages(){
    	List<ChatMessages> listaMessages = new ArrayList<>();
    	try {
    		System.out.println("ALl: ");
    		jdbcTemplate.execute(FindAllQuery);
    	}catch (Exception e) {
			// TODO: handle exception
		}
    	
    	return listaMessages;
    }
    
   
    @Override
    public ChatMessages loadChannelMessages() {
    	System.out.println("FindAllQuery: "+FindAllQuery);
        try{
            return jdbcTemplate.queryForObject(FindAllQuery, this::createMessagesFromResult );
        }catch (EmptyResultDataAccessException emptyData){
            return new ChatMessages();
        }
    }

    private ChatMessages createMessagesFromResult(ResultSet resultSet, int rowNumber) {
        ChatMessages messages = new ChatMessages();
        try {
            while (resultSet.next()){
                messages.add(new ChatMessage(
                        resultSet.getString("content"),
                        resultSet.getString("sender"),
                        resultSet.getDate("sendDate")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  messages;
    }


}
