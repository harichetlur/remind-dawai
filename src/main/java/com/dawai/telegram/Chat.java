package com.dawai.telegram;

/**
 * Created by harichetlur on 6/14/16.
 */
public class Chat {
    private int id;
    private String type;

    public Chat(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
