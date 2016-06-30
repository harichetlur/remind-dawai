package com.dawai.telegram.factory;

import com.dawai.Dawai;
import com.dawai.telegram.KeyboardButton;
import com.dawai.telegram.ReplyKeyboardMarkup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by harichetlur on 6/20/16.
 */
public class ReplyKeyboardMarkupFactory {

    private static Map<String, ReplyKeyboardMarkup> fMarkupCache;

    static {
        fMarkupCache = new HashMap();
    }

    public static ReplyKeyboardMarkup getBaseCommandsKeyboardMarkup() {

        ReplyKeyboardMarkup replyKeyboardMarkup =  fMarkupCache.get("baseCommands");
        if(replyKeyboardMarkup != null)
            return replyKeyboardMarkup;

        KeyboardButton[][] keyboardButtons = new KeyboardButton[1][2];
        keyboardButtons[0][0] = new KeyboardButton("Done with a dawai");
        keyboardButtons[0][1] = new KeyboardButton("See what's left");

        replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardButtons);
        fMarkupCache.put("baseCommands", replyKeyboardMarkup);

        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup getDawaiKeyboardMarkup(List<Dawai> dawaiList) {
        int size = dawaiList.size();
        int colCount = size > 1? 2 : 1;
        int rowCount = (int)Math.ceil((double) size/(double) colCount);

        KeyboardButton[][] keyboardButtons = new KeyboardButton[rowCount][colCount];
        int count = 0;
        for(Dawai d: dawaiList) {
            keyboardButtons[count/colCount][count%colCount] = new KeyboardButton(d.getDawaiId() + "- " +d.getDawaiName());
            count++;
        }

        if(count!= (colCount * rowCount))
            keyboardButtons[count/colCount][count%colCount] = new KeyboardButton("");

        return new ReplyKeyboardMarkup(keyboardButtons);
    }
}
