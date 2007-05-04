package org.codehaus.waffle.migration.migration.actions.struts;

import org.codehaus.waffle.migration.migration.forms.struts.CalendarForm;
import org.codehaus.waffle.migration.migration.model.CalendarUtils;
import org.codehaus.waffle.migration.migration.model.Month;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class CalendarAction extends Action {
    private Map monthsMap;

    public CalendarAction() {
        monthsMap = CalendarUtils.createMonthsMap();
    }

    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        CalendarForm monthsForm = (CalendarForm) form;
        if (monthsForm.getAction().equals("query")) {
            return mapping.findForward(listMonths(monthsForm));
        }
        return mapping.findForward(showForm(monthsForm));
    }

    private String showForm(CalendarForm form) {
        form.setLanguages(createLabelValueBeans(new String[]{"English", "Spanish"}));
        form.setYears(createLabelValueBeans(new String[]{"2005", "2006"}));
        return "success";
    }

    private LabelValueBean[] createLabelValueBeans(String[] values) {
        LabelValueBean[] beans = new LabelValueBean[values.length];
        for (int i = 0; i < beans.length; i++) {
            String value = values[i];
            beans[i] = new LabelValueBean(value, value);
        }
        return beans;
    }

    private String listMonths(CalendarForm form) {
        String language = form.getLanguage();
        String year = form.getYear();
        Month[] months = (Month[]) monthsMap.get(language);
        changeYear(months, year);
        form.setMonths(months);
        return "success";
    }

    private void changeYear(Month[] months, String year) {
        for (Month month : months) {
            month.changeYear(year);
        }
    }

}
