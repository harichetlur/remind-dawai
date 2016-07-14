package com.dawai;

import java.util.*;

public class Nurse {

    private static DupliMap fDawaiMap = new DupliMap();;

    public Nurse(Prescription fPrescription) {
        this.fPrescription = fPrescription;
    }

    Prescription fPrescription;

    public DupliMap getDawaiToTake(Calendar cal) {
        DupliMap dawaiMap = new DupliMap();

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int day = cal.get(Calendar.DAY_OF_WEEK);

        if(hour == 0 ) {
            fDawaiMap.remove(TimeFrame.DAY);
            dawaiMap.putAll(fPrescription.getForTimeFrame(TimeFrame.DAY));
        } else if (hour >= 6 && hour < 12) {
            //Clear any daily/nightly doses
            fDawaiMap.remove(TimeFrame.NIGHT);
            dawaiMap.putAll(fPrescription.getForTimeFrame(TimeFrame.MORNING));

            switch (day) {
                case 1:
                    fDawaiMap.remove(TimeFrame.SAT);
                    if(fPrescription.getForTimeFrame(TimeFrame.SUN)!=null)
                        dawaiMap.putAll(fPrescription.getForTimeFrame(TimeFrame.SUN));
                    break;
                case 2:
                    fDawaiMap.remove(TimeFrame.SUN);
                    if(fPrescription.getForTimeFrame(TimeFrame.MON)!=null)
                        dawaiMap.putAll(fPrescription.getForTimeFrame(TimeFrame.MON));
                    break;
                case 3:
                    fDawaiMap.remove(TimeFrame.MON);
                    if(fPrescription.getForTimeFrame(TimeFrame.TUE)!=null)
                        dawaiMap.putAll(fPrescription.getForTimeFrame(TimeFrame.TUE));
                    break;
                case 4:
                    fDawaiMap.remove(TimeFrame.TUE);
                    if(fPrescription.getForTimeFrame(TimeFrame.WED)!=null)
                        dawaiMap.putAll(fPrescription.getForTimeFrame(TimeFrame.WED));
                    break;
                case 5:
                    fDawaiMap.remove(TimeFrame.WED);
                    if(fPrescription.getForTimeFrame(TimeFrame.THU)!=null)
                        dawaiMap.putAll(fPrescription.getForTimeFrame(TimeFrame.THU));
                    break;
                case 6:
                    fDawaiMap.remove(TimeFrame.THU);
                    if(fPrescription.getForTimeFrame(TimeFrame.FRI)!=null)
                        dawaiMap.putAll(fPrescription.getForTimeFrame(TimeFrame.FRI));
                    break;
                case 7:
                    fDawaiMap.remove(TimeFrame.FRI);
                    if(fPrescription.getForTimeFrame(TimeFrame.SAT)!=null)
                        dawaiMap.putAll(fPrescription.getForTimeFrame(TimeFrame.SAT));
                    break;
            }
        } else if (hour >= 12 && hour < 18) { //Check if afternoon
            fDawaiMap.remove(TimeFrame.MORNING);
            dawaiMap.putAll(fPrescription.getForTimeFrame(TimeFrame.AFTERNOON));

        } else {
            fDawaiMap.remove(TimeFrame.AFTERNOON);
            dawaiMap.putAll(fPrescription.getForTimeFrame(TimeFrame.NIGHT));
        }


	    for(Map.Entry<TimeFrame, List<Dawai>> e : dawaiMap.entrySet()) {
		    List<Dawai> dawaiToBeAdded = e.getValue();
		    List<Dawai> existingDawai = fDawaiMap.get(e.getKey());

		    if(existingDawai == null) {
			    fDawaiMap.put(e.getKey(), dawaiToBeAdded);
		    } else {
			    Iterator<Dawai> i = dawaiToBeAdded.iterator();
			    while (i.hasNext()) {
				    Dawai d = i.next();

				    if (fDawaiMap.getDawai(d.getDawaiId()) != null)
					    i.remove();
			    }
			    existingDawai.addAll(dawaiToBeAdded);
		    }
	    }

	    return dawaiMap;

    }

    public String done(int dawaiId) {
        Dawai d = fDawaiMap.getDawai(dawaiId);

        if(d == null)
            throw new IllegalArgumentException("Boo");

	    if(d.isDone())
		    return String.format("Dawai %s is already marked as done.", d.getDawaiName());

        d.setDone(true);
        return String.format("Marked %s as done.", d.getDawaiName());
    }

    public List<Dawai> getWhatsLeft() {
        List<Dawai> dawaiList = new ArrayList<>();
	    for(List<Dawai> dList : fDawaiMap.values()) {
		    for(Dawai d : dList) {
			    if(!d.isDone())
				    dawaiList.add(d);
		    }
	    }
        return dawaiList;
    }

}
