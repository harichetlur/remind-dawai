package com.dawai;

public class Dawai {

    private String fDawaiName;
    private TIME_FRAME[] fDoses;

    public Dawai(String fDawaiName, TIME_FRAME[] fDoses) {
        this.fDawaiName = fDawaiName;
        this.fDoses = fDoses;
    }

    public String getfDawaiName() {
        return fDawaiName;
    }

    public TIME_FRAME[] getfDoses() {
        return fDoses;
    }
}
