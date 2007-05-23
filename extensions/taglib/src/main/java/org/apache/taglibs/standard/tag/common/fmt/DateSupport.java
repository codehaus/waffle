package org.apache.taglibs.standard.tag.common.fmt;

import org.waffle.taglib.form.TimeTag;
import org.waffle.taglib.form.DateTag;

import java.util.Locale;

import javax.servlet.jsp.PageContext;

public class DateSupport {

    public static Locale getFormattingLocale(PageContext pc, DateTag fromTag, boolean format, Locale[] avail) {
        return SetLocaleSupport.getFormattingLocale(pc, fromTag, format, avail);
    }

    public static Locale getFormattingLocale(PageContext pc, TimeTag fromTag, boolean format, Locale[] avail) {
        return SetLocaleSupport.getFormattingLocale(pc, fromTag, format, avail);
    }

}
