package com.dawai;

import java.util.*;

public class Nurse {

    private static Stack<Dawai> fStack = new Stack<>();;

    public Nurse(Prescription fPrescription) {
        this.fPrescription = fPrescription;
    }

    Prescription fPrescription;

    public List<Dawai> getDawaiToTake(Calendar cal) {
        List<Dawai> dawaiList = new ArrayList<>();

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int day = cal.get(Calendar.DAY_OF_WEEK);

        if(hour == 0 ) {
            fStack.remove(TIME_FRAME.DAY);
            dawaiList.addAll(fPrescription.getForTimeFrame(TIME_FRAME.DAY));
        } else if (hour >= 6 && hour < 12) {
            //Clear any daily/nightly doses
            fStack.remove(TIME_FRAME.NIGHT);
            dawaiList.addAll(fPrescription.getForTimeFrame(TIME_FRAME.MORNING));

            switch (day) {
                case 1:
                    fStack.remove(TIME_FRAME.SAT);
                    if(fPrescription.getForTimeFrame(TIME_FRAME.SUN)!=null)
                        dawaiList.addAll(fPrescription.getForTimeFrame(TIME_FRAME.SUN));
                    break;
                case 2:
                    fStack.remove(TIME_FRAME.SUN);
                    if(fPrescription.getForTimeFrame(TIME_FRAME.MON)!=null)
                        dawaiList.addAll(fPrescription.getForTimeFrame(TIME_FRAME.MON));
                    break;
                case 3:
                    fStack.remove(TIME_FRAME.MON);
                    if(fPrescription.getForTimeFrame(TIME_FRAME.TUE)!=null)
                        dawaiList.addAll(fPrescription.getForTimeFrame(TIME_FRAME.TUE));
                    break;
                case 4:
                    fStack.remove(TIME_FRAME.TUE);
                    if(fPrescription.getForTimeFrame(TIME_FRAME.WED)!=null)
                        dawaiList.addAll(fPrescription.getForTimeFrame(TIME_FRAME.WED));
                    break;
                case 5:
                    fStack.remove(TIME_FRAME.WED);
                    if(fPrescription.getForTimeFrame(TIME_FRAME.THU)!=null)
                        dawaiList.addAll(fPrescription.getForTimeFrame(TIME_FRAME.THU));
                    break;
                case 6:
                    fStack.remove(TIME_FRAME.THU);
                    if(fPrescription.getForTimeFrame(TIME_FRAME.FRI)!=null)
                        dawaiList.addAll(fPrescription.getForTimeFrame(TIME_FRAME.FRI));
                    break;
                case 7:
                    fStack.remove(TIME_FRAME.FRI);
                    if(fPrescription.getForTimeFrame(TIME_FRAME.SAT)!=null)
                        dawaiList.addAll(fPrescription.getForTimeFrame(TIME_FRAME.SAT));
                    break;
            }
        } else if (hour >= 12 && hour < 18) { //Check if afternoon
            fStack.remove(TIME_FRAME.MORNING);
            dawaiList.addAll(fPrescription.getForTimeFrame(TIME_FRAME.AFTERNOON));

        } else {
            fStack.remove(TIME_FRAME.AFTERNOON);
            dawaiList.addAll(fPrescription.getForTimeFrame(TIME_FRAME.NIGHT));
        }

        Iterator<Dawai> i = dawaiList.iterator();
        while(i.hasNext()) {
            Dawai d = i.next();
            if(fStack.contains(d))
                i.remove();
        }

        fStack.addAll(dawaiList);
        return dawaiList;
    }

    public String done() {
        if(fStack.size() > 1) {
            Dawai d = fStack.pop();
            return String.format("Done with %s", d.getfDawaiName());
        } else if(fStack.size() == 1) {
            Dawai d = fStack.pop();
            return String.format("Done with %s. All done for now. Good job.", d.getfDawaiName());
        } else {
            return "Done with what exactly?";
        }
    }

    public Enumeration getWhatsLeft() {
        return fStack.elements();
    }

}
