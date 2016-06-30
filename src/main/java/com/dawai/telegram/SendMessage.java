package com.dawai.telegram;

/**
 * Created by harichetlur on 6/14/16.
 */
public class SendMessage {
    private int chat_id;
    private String text;
    private ReplyKeyboardMarkup reply_markup;

    public SendMessage(int chat_id, String text, ReplyKeyboardMarkup reply_markup) {
        this.chat_id = chat_id;
        this.text = text;
        this.reply_markup = reply_markup;
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

    public ReplyKeyboardMarkup getReply_markup() {
        return reply_markup;
    }

    public void setReply_markup(ReplyKeyboardMarkup reply_markup) {
        this.reply_markup = reply_markup;
    }
}
