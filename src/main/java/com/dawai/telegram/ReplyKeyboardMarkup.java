package com.dawai.telegram;

/**
 * Created by harichetlur on 6/16/16.
 */
public class ReplyKeyboardMarkup {

    private KeyboardButton[][] keyboardButtons;

    public ReplyKeyboardMarkup(KeyboardButton[][] keyboardButtons) {
        this.keyboardButtons = keyboardButtons;
    }

    public KeyboardButton[][] getKeyboardButtons() {
        return keyboardButtons;
    }

    public void setKeyboardButtons(KeyboardButton[][] keyboardButtons) {
        this.keyboardButtons = keyboardButtons;
    }
}
