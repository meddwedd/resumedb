package me.shamanov.resumedb.web.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Author: Mike
 * Date: 09.04.2019
 *
 * Trims from spaces {@link String#trim()} and removes all the HTML tags (all the information in between '<' and '>' along with the tags) from request parameters
 * when {@link HtmlTagsFilterRequestWrapper#getParameter(String)} and {@link HtmlTagsFilterRequestWrapper#getParameterValues(String)} are being called.
 */

public class HtmlTagsFilterRequestWrapper extends HttpServletRequestWrapper {

    private static final String pattern = "<+?.*?>+";

    HtmlTagsFilterRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        return removeTagsFromString(super.getParameter(name));
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] paramValues = super.getParameterValues(name);
        String[] result = new String[paramValues.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = removeTagsFromString(paramValues[i]);
        }
        return result;
    }

    private String removeTagsFromString(String str) {
        return str.trim().replaceAll(pattern, "");
    }
}
