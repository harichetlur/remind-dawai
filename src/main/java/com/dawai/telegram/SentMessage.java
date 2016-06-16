package com.dawai.telegram;

/**
 * Created by harichetlur on 6/14/16.
 */
public class SentMessage {
    private int chat_id;
    private String text;

    public SentMessage(int chat_id, String text) {
        this.chat_id = chat_id;
        this.text = text;
    }

    public int getChat_id() {
        return chat_id;
    }

    public void setChat_id(int chat_id) {
        this.chat_id = chat_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
