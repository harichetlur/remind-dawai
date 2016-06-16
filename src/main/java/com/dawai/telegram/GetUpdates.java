package com.dawai.telegram;

/**
 * Created by harichetlur on 6/14/16.
 */
public class GetUpdates {
    private int offset;
    private int limit;

    public GetUpdates(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
