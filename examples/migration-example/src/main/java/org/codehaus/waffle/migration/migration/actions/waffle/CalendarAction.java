package org.codehaus.waffle.migration.migration.actions.waffle;

import java.util.Map;

import org.codehaus.waffle.action.annotation.ActionMethod;
import org.codehaus.waffle.migration.migration.model.CalendarUtils;
import org.codehaus.waffle.migration.migration.model.Month;

public class CalendarAction {
    private String language;
    private LabelValue[] languages;
    private String year;
    private LabelValue[] years;
    private Map monthsMap;
    private Month[] months;

    public CalendarAction() {
        monthsMap = CalendarUtils.createMonthsMap();
        languages = createLabelValues(new String[] { "English", "Spanish" });
        years = createLabelValues(new String[] { "2005", "2006" });
    }

    public String getLanguage() {
        return language;
    }

    public LabelValue[] getLanguages() {
        return languages;
    }

    public String getYear() {
        return year;
    }

    public LabelValue[] getYears() {
        return years;
    }

    public Month[] getMonths() {        
        return months;
    }

    @ActionMethod(parameters = {"language", "year"})
    public void listMonths(String language, String year) {
        this.language = language;
        this.year = year;
        months = (Month[])monthsMap.get(language);
        changeYear();
    }

    private void changeYear() {
        for (Month month: months){
            month.changeYear(year);
        }
    }

    private LabelValue[] createLabelValues(String[] values) {
        LabelValue[] beans = new LabelValue[values.length];
        for (int i = 0; i < beans.length; i++) {
            String value = values[i];
            beans[i] = new LabelValue(value, value);
        }
        return beans;
    }

}
