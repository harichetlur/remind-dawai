package com.dawai.telegram;

/**
 * Created by harichetlur on 6/16/16.
 */
public class ReplyKeyboardMarkup {

    private KeyboardButton[][] keyboard;

    public ReplyKeyboardMarkup(KeyboardButton[][] keyboardButtons) {
        this.keyboard = keyboardButtons;
    }

    public KeyboardButton[][] getKeyboard() {
        return keyboard;
    }

    public void setKeyboard(KeyboardButton[][] keyboard) {
        this.keyboard = keyboard;
    }
}
