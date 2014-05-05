package io.github.redinzane.realisticchat.minions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CHAT_LOGS")
public class ChatMessage {
    @Id
    int id;
    @Column(name = "cUUID")
    String conversationUUID;
    @Column(name = "pUUID")
    String playerUUID;
    @Column(name = "name")
    String name;
    @Column(name = "type")
    String typeOfChat;
    @Column(name = "receivers")
    String receivers;
    @Column(name = "message")
    String message;
    @Column(name = "timestamp")
    long timestamp;
    
    public ChatMessage(String cUUID, String pUUID, String player, String type, String receiver, String msg, long time) {
        this.conversationUUID = cUUID;
        this.playerUUID = pUUID;
        this.typeOfChat = type;
        this.receivers = receiver;
        this.message = msg;
        this.timestamp =  time;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getConversatinoUUID() {
        return conversationUUID;
    }
    public void setConversatioUUID(String conversationUUID) {
        this.conversationUUID = conversationUUID;
    }
    public String getPlayerUUID() {
        return playerUUID;
    }
    public void setPlayerUUID(String playerUUID) {
        this.playerUUID = playerUUID;
    }
    public String getTypeOfChat() {
        return typeOfChat;
    }
    public void setTypeOfChat(String typeOfChat) {
        this.typeOfChat = typeOfChat;
    }
    public String getReceivers() {
        return receivers;
    }
    public void setReceivers(String receivers) {
        this.receivers = receivers;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    
}
