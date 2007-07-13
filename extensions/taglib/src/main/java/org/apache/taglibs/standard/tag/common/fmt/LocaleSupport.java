package org.apache.taglibs.standard.tag.common.fmt;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.core.Config;
import java.util.Locale;

public class LocaleSupport {

    public Locale getFormattingLocale(PageContext pc) {
        Object configuredLocale = Config.find(pc, Config.FMT_LOCALE);
        if (configuredLocale == null) {
            configuredLocale = Config.find(pc, Config.FMT_FALLBACK_LOCALE);
        }
        if (configuredLocale instanceof Locale) {
            return (Locale) configuredLocale;
        } else if (configuredLocale instanceof String) {
            return stringToLocale((String) configuredLocale);
        } else {
            return pc.getRequest().getLocale();
        }

    }

    /**
     * Extracted from XStream project, copyright Joe Walnes
     *
     * @param str the string to convert
     * @return the locale
     */
    private Locale stringToLocale(String str) {
        int[] underscorePositions = underscorePositions(str);
        String language, country, variant;
        if (underscorePositions[0] == -1) { // "language"
            language = str;
            country = "";
            variant = "";
        } else if (underscorePositions[1] == -1) { // "language_country"
            language = str.substring(0, underscorePositions[0]);
            country = str.substring(underscorePositions[0] + 1);
            variant = "";
        } else { // "language_country_variant"
            language = str.substring(0, underscorePositions[0]);
            country = str.substring(underscorePositions[0] + 1, underscorePositions[1]);
            variant = str.substring(underscorePositions[1] + 1);
        }
        return new Locale(language, country, variant);
    }

    private int[] underscorePositions(String in) {
        int[] result = new int[2];
        for (int i = 0; i < result.length; i++) {
            int last = i == 0 ? 0 : result[i - 1];
            result[i] = in.indexOf('_', last + 1);
        }
        return result;
    }
}
