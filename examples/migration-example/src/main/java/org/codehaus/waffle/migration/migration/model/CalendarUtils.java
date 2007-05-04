package org.codehaus.waffle.migration.migration.model;

import java.util.HashMap;
import java.util.Map;

public class CalendarUtils {

    public static Map<String, Month[]> createMonthsMap() {
        Map<String, Month[]> map = new HashMap<String, Month[]>();
        map.put("English", createEnglishMonths());
        map.put("Spanish", createSpanishMonths());
        return map;
    }

    private static Month[] createEnglishMonths() {
        return new Month[]{createEnglishMonth("January"),
                createEnglishMonth("February"), createEnglishMonth("March"),
                createEnglishMonth("April"), createEnglishMonth("June"),
                createEnglishMonth("July"), createEnglishMonth("August"),
                createEnglishMonth("September"), createEnglishMonth("October"),
                createEnglishMonth("November"), createEnglishMonth("December")};
    }

    private static Month createEnglishMonth(String month) {
        return new Month("English", month);
    }

    private static Month[] createSpanishMonths() {
        return new Month[]{createSpanishMonth("Jenero"),
                createSpanishMonth("Febrero"), createSpanishMonth("Marzo"),
                createSpanishMonth("Abril"), createSpanishMonth("Junio"),
                createSpanishMonth("Julio"), createSpanishMonth("Agosto"),
                createSpanishMonth("Septiembre"),
                createSpanishMonth("Octubre"), createSpanishMonth("Noviembre"),
                createSpanishMonth("Deciembre")};
    }

    private static Month createSpanishMonth(String month) {
        return new Month("Spanish", month);
    }
}
