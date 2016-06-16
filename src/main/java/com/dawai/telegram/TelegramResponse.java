package com.dawai.telegram;

/**
 * Created by harichetlur on 6/14/16.
 */
public class TelegramResponse {
    private boolean ok;
    private Update[] result;

    public TelegramResponse(boolean ok, Update[] result) {
        this.ok = ok;
        this.result = result;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Update[] getResult() {
        return result;
    }

    public void setResult(Update[] result) {
        this.result = result;
    }
}
