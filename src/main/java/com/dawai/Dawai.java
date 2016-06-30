package com.dawai;

public class Dawai {

    private String fDawaiName;
    private TIME_FRAME[] fDoses;
    private boolean fDone;
	private final int fDawaiId;

    public Dawai(String dawaiName, TIME_FRAME[] doses, int dawaiId) {
        this.fDawaiName = dawaiName;
        this.fDoses = doses;
	    fDawaiId = dawaiId;
	    fDone = false;
    }

    public String getDawaiName() {
        return fDawaiName;
    }

    public TIME_FRAME[] getDoses() {
        return fDoses;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Dawai))
            return false;

        Dawai d = (Dawai)o;
        return d.getDawaiName().equals(this.fDawaiName);
    }

    public boolean isDone() {
        return fDone;
    }

    public void setDone(boolean done) {
        fDone = done;
    }

	public int getDawaiId() {
		return fDawaiId;
	}
}
