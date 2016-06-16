package com.dawai;

import java.util.*;

public class Prescription {

    private DupliMap fDosage;

    public Prescription(List<Dawai> dawaiList) {
        fDosage = new DupliMap();
        for(Dawai dawai : dawaiList)
            for(TIME_FRAME dose: dawai.getfDoses())
                fDosage.put(dose, dawai);
    }

    public List<Dawai> getForTimeFrame(TIME_FRAME timeFrame) {
        return fDosage.get(timeFrame);
    }
}

class DupliMap extends HashMap<TIME_FRAME, List<Dawai>> {
    public void put(TIME_FRAME key, Dawai number) {
        List<Dawai> current = get(key);
        if (current == null) {
            current = new ArrayList<>();
            super.put(key, current);
        }
        current.add(number);
    }
}