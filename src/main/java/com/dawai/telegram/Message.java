package com.dawai.telegram;

/**
 * Created by harichetlur on 6/14/16.
 */
public class Message {

    private int message_id;
    private User from;
    private int date;
    private String text;
    private Chat chat;

    public Message(int message_id, User from, int date, String text, Chat chat) {
        this.message_id = message_id;
        this.from = from;
        this.date = date;
        this.text = text;
        this.chat = chat;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
