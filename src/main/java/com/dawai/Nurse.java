package com.dawai;

import java.util.*;

public class Nurse {

    private static Map<Integer, Dawai> fDawaiMap = new HashMap<>();;

    public Nurse(Prescription fPrescription) {
        this.fPrescription = fPrescription;
    }

    Prescription fPrescription;

    public Map<Integer, Dawai> getDawaiToTake(Calendar cal) {
        Map<Integer, Dawai> dawaiList = new HashMap<>();

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int day = cal.get(Calendar.DAY_OF_WEEK);

        if(hour == 0 ) {
            fDawaiMap.remove(TIME_FRAME.DAY);
            dawaiList.putAll(fPrescription.getForTimeFrame(TIME_FRAME.DAY));
        } else if (hour >= 6 && hour < 12) {
            //Clear any daily/nightly doses
            fDawaiMap.remove(TIME_FRAME.NIGHT);
            dawaiList.putAll(fPrescription.getForTimeFrame(TIME_FRAME.MORNING));

            switch (day) {
                case 1:
                    fDawaiMap.remove(TIME_FRAME.SAT);
                    if(fPrescription.getForTimeFrame(TIME_FRAME.SUN)!=null)
                        dawaiList.putAll(fPrescription.getForTimeFrame(TIME_FRAME.SUN));
                    break;
                case 2:
                    fDawaiMap.remove(TIME_FRAME.SUN);
                    if(fPrescription.getForTimeFrame(TIME_FRAME.MON)!=null)
                        dawaiList.putAll(fPrescription.getForTimeFrame(TIME_FRAME.MON));
                    break;
                case 3:
                    fDawaiMap.remove(TIME_FRAME.MON);
                    if(fPrescription.getForTimeFrame(TIME_FRAME.TUE)!=null)
                        dawaiList.putAll(fPrescription.getForTimeFrame(TIME_FRAME.TUE));
                    break;
                case 4:
                    fDawaiMap.remove(TIME_FRAME.TUE);
                    if(fPrescription.getForTimeFrame(TIME_FRAME.WED)!=null)
                        dawaiList.putAll(fPrescription.getForTimeFrame(TIME_FRAME.WED));
                    break;
                case 5:
                    fDawaiMap.remove(TIME_FRAME.WED);
                    if(fPrescription.getForTimeFrame(TIME_FRAME.THU)!=null)
                        dawaiList.putAll(fPrescription.getForTimeFrame(TIME_FRAME.THU));
                    break;
                case 6:
                    fDawaiMap.remove(TIME_FRAME.THU);
                    if(fPrescription.getForTimeFrame(TIME_FRAME.FRI)!=null)
                        dawaiList.putAll(fPrescription.getForTimeFrame(TIME_FRAME.FRI));
                    break;
                case 7:
                    fDawaiMap.remove(TIME_FRAME.FRI);
                    if(fPrescription.getForTimeFrame(TIME_FRAME.SAT)!=null)
                        dawaiList.putAll(fPrescription.getForTimeFrame(TIME_FRAME.SAT));
                    break;
            }
        } else if (hour >= 12 && hour < 18) { //Check if afternoon
            fDawaiMap.remove(TIME_FRAME.MORNING);
            dawaiList.putAll(fPrescription.getForTimeFrame(TIME_FRAME.AFTERNOON));

        } else {
            fDawaiMap.remove(TIME_FRAME.AFTERNOON);
            dawaiList.putAll(fPrescription.getForTimeFrame(TIME_FRAME.NIGHT));
        }

        Iterator<Dawai> i = dawaiList.values().iterator();

	    while(i.hasNext()) {
            Dawai d = i.next();
            if(fDawaiMap.containsKey(d.getDawaiId()))
                i.remove();
        }

        fDawaiMap.putAll(dawaiList);
        return dawaiList;
    }

    public String done(int dawaiId) {
        Dawai d = fDawaiMap.get(dawaiId);

        if(d == null)
            throw new IllegalArgumentException("Boo");

	    if(d.isDone())
		    return String.format("Dawai %s is already marked as done.", d.getDawaiName());

        d.setDone(true);
        return String.format("Marked %s as done.", d.getDawaiName());
    }

    public List<Dawai> getWhatsLeft() {
        List<Dawai> dawaiList = new ArrayList<>();
        for(Dawai d : fDawaiMap.values()) {
            if(!d.isDone())
                dawaiList.add(d);
        }
        return dawaiList;
    }

}
