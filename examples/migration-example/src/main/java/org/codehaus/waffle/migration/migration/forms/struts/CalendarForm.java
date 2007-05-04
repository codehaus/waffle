package org.codehaus.waffle.migration.migration.forms.struts;

import org.apache.struts.action.ActionForm;
import org.apache.struts.util.LabelValueBean;

import org.codehaus.waffle.migration.migration.model.Month;

public class CalendarForm extends ActionForm {
    private String language;
    private String year;
    private LabelValueBean[] languages;
    private LabelValueBean[] years;
    private Month[] months;
    private String action = "";

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public LabelValueBean[] getLanguages() {
        return languages;
    }

    public void setLanguages(LabelValueBean[] languages) {
        this.languages = languages;
    }

    public Month[] getMonths() {
        return months;
    }

    public void setMonths(Month[] months) {
        this.months = months;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public LabelValueBean[] getYears() {
        return years;
    }

    public void setYears(LabelValueBean[] years) {
        this.years = years;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
    
}
