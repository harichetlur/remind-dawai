package com.dawai;

import java.util.*;

public class Prescription {

    private DupliMap fDosage;

    public Prescription(List<Dawai> dawaiList) {
        fDosage = new DupliMap();
        for(Dawai dawai : dawaiList)
            for(TimeFrame dose: dawai.getDoses())
                fDosage.put(dose, dawai);
    }

    public DupliMap getForTimeFrame(TimeFrame timeFrame) {
        DupliMap returnValue = new DupliMap();
	    if(fDosage.get(timeFrame)!=null) {
		    for (Dawai d : fDosage.get(timeFrame)) {
			    returnValue.put(timeFrame, d);
		    }
	    }
        return returnValue;
    }
}

class DupliMap extends HashMap<TimeFrame, List<Dawai>> {

    public void put(TimeFrame key, Dawai number) {
        List<Dawai> current = get(key);
        if (current == null) {
            current = new ArrayList<>();
            super.put(key, current);
        }
        current.add(number);
    }

	public Dawai getDawai(int dawaiId) {
		for(List<Dawai> list : super.values()) {
			for(Dawai d: list) {
				if (d.getDawaiId() == dawaiId)
					return d;
			}
		}
		return null;
	}
}