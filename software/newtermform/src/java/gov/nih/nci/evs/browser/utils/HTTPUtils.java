package gov.nih.nci.evs.browser.utils;

import java.io.*;
import java.net.*;
import java.util.regex.*;

import javax.servlet.http.*;

public class HTTPUtils {
    public static String getParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (value == null || value.length() <= 0)
            return "";
        return cleanXSS(value);
    }

    public static String getSessionAttributeString(HttpServletRequest request,
        String name, boolean clear) {
        String value = (String) request.getSession().getAttribute(name);
        if (value == null || value.length() <= 0)
            return "";
        if (clear)
            request.getSession().setAttribute(name, null);
        return cleanXSS(value);
    }

    public static String getSessionAttributeString(HttpServletRequest request,
        String name) {
        return getSessionAttributeString(request, name, false);
    }

    public static String cleanXSS(String value) {
        if (value == null || value.length() < 1)
            return value;

        try {
            value = URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // Do nothing, just use the input
        }

        // Remove XSS attacks
        value = replaceAll(value, "<\\s*script\\s*>.*</\\s*script\\s*>", "");
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        value = value.replaceAll("'", "&#39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = replaceAll(value, "[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']",
                "\"\"");
        value = value.replaceAll("\"", "&quot;");
        return value;
    }

    public static String replaceAll(String string, String regex,
            String replaceWith) {
        Pattern myPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        string = myPattern.matcher(string).replaceAll(replaceWith);
        return string;
    }
}
