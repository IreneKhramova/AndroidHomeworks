package com.example.irene.khramovahomework7;

import com.example.irene.khramovahomework7.data.Bridge;
import com.example.irene.khramovahomework7.data.Divorce;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DivorceUtil {
    private static final String DIVORCE_SOON = "Less than an hour before divorce";
    private static final String DIVORCE_LATE = "Divorced";
    private static final String DIVORCE_NORMAL = "Not divorced";
    private static final int MINUTES_IN_HOUR = 60;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("H:mm", Locale.getDefault());

    public static StringBuffer getDivorceTime(Bridge bridge) {
        StringBuffer divorceTime = new StringBuffer();

        for (Divorce divorce : bridge.getDivorces()) {
            String start = DATE_FORMAT.format(divorce.getStart());
            String end = DATE_FORMAT.format(divorce.getEnd());
            divorceTime
                    .append(start)
                    .append(" - ")
                    .append(end)
                    .append("     ");
        }
        return divorceTime;
    }

    private static String getDivorceMark(Bridge bridge) {
        String currTime = DATE_FORMAT.format(System.currentTimeMillis());
        String[] partsCurr = currTime.split(":");
        int currMin = MINUTES_IN_HOUR * Integer.parseInt(partsCurr[0]) + Integer.parseInt(partsCurr[1]);
        String divorceMark = DIVORCE_NORMAL;

        for (int i = 0; i < bridge.getDivorces().size(); i++) {
            Divorce divorce = bridge.getDivorces().get(i);
            String start = DATE_FORMAT.format(divorce.getStart());
            String end = DATE_FORMAT.format(divorce.getEnd());

            String[] partsStart = start.split(":");
            String[] partsEnd = end.split(":");
            int startMin = MINUTES_IN_HOUR * Integer.parseInt(partsStart[0]) + Integer.parseInt(partsStart[1]);
            int endMin = MINUTES_IN_HOUR * Integer.parseInt(partsEnd[0]) + Integer.parseInt(partsEnd[1]);

            if (startMin - currMin <= MINUTES_IN_HOUR && startMin > currMin) {
                divorceMark = DIVORCE_SOON;
            } else if (startMin > endMin) {
                //В промежуток попадает полночь
                if (currMin >= startMin || currMin < endMin) {
                    divorceMark = DIVORCE_LATE;
                    break;
                }
            } else {
                if (currMin >= startMin && currMin < endMin) {
                    divorceMark = DIVORCE_LATE;
                    break;
                }
            }
        }
        return divorceMark;
    }

    public static int getDivorceImgResId(Bridge bridge) {
        switch (getDivorceMark(bridge)) {
            case DIVORCE_LATE:
                return R.drawable.ic_bridge_late;
            case DIVORCE_SOON:
                return R.drawable.ic_bridge_soon;
            default:
                //DIVORCE_NORMAL
                return R.drawable.ic_bridge_normal;
        }
    }

    public static int getDivorceBitmapResId(Bridge bridge) {
        switch (getDivorceMark(bridge)) {
            case DIVORCE_LATE:
                return R.drawable.ic_bridge_late_map;
            case DIVORCE_SOON:
                return R.drawable.ic_bridge_soon_map;
            default:
                //DIVORCE_NORMAL
                return R.drawable.ic_bridge_normal_map;
        }
    }

    public static boolean isDivorced(Bridge bridge) {
        if (getDivorceMark(bridge).equals(DIVORCE_NORMAL)) {
            return false;
        } else {
            return true;
        }
    }

    //TODO:
    public static Long getNearestDivorceStart(Bridge bridge) {
        String[] nearestDivorceStart = null;

        String currTime = DATE_FORMAT.format(System.currentTimeMillis());
        String[] partsCurr = currTime.split(":");
        int currMin = MINUTES_IN_HOUR * Integer.parseInt(partsCurr[0]) + Integer.parseInt(partsCurr[1]);

        for (int i = 0; i < bridge.getDivorces().size(); i++) {
            Divorce divorce = bridge.getDivorces().get(i);
            String start = DATE_FORMAT.format(divorce.getStart());

            String[] partsStart = start.split(":");
            int startMin = MINUTES_IN_HOUR * Integer.parseInt(partsStart[0]) + Integer.parseInt(partsStart[1]);

            /* divorces идут по порядку.
             * Первый divorce, startMin которого больше currMin, и есть ближайший.
             */
            if(startMin > currMin) {
                nearestDivorceStart = partsStart;
            }
        }

        /* Если не нашлось такого divorce, значит ближайший - это первый после полуночи */
        if(nearestDivorceStart == null) {
            Divorce divorce = bridge.getDivorces().get(0);
            //TODO: вынести в метод
            String start = DATE_FORMAT.format(divorce.getStart());
            nearestDivorceStart = start.split(":");
        }

        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR, Integer.parseInt(nearestDivorceStart[0]));
        now.set(Calendar.MINUTE, Integer.parseInt(nearestDivorceStart[1]));
        now.set(Calendar.SECOND, 0);
        return now.getTimeInMillis();
    }
}
