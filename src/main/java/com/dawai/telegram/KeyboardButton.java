package com.dawai.telegram;

/**
 * Created by harichetlur on 6/16/16.
 */
public class KeyboardButton {
    private String text;

    public KeyboardButton(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
