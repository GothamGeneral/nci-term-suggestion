package gov.nih.nci.evs.browser.utils;

import java.util.*;

import org.apache.log4j.*;

/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008,2009 NGIT. This software was developed in conjunction 
 * with the National Cancer Institute, and so to the extent government 
 * employees are co-authors, any rights in such works shall be subject 
 * to Title 17 of the United States Code, section 105.
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 *   1. Redistributions of source code must retain the above copyright 
 *      notice, this list of conditions and the disclaimer of Article 3, 
 *      below. Redistributions in binary form must reproduce the above 
 *      copyright notice, this list of conditions and the following 
 *      disclaimer in the documentation and/or other materials provided 
 *      with the distribution.
 *   2. The end-user documentation included with the redistribution, 
 *      if any, must include the following acknowledgment:
 *      "This product includes software developed by NGIT and the National 
 *      Cancer Institute."   If no such end-user documentation is to be
 *      included, this acknowledgment shall appear in the software itself,
 *      wherever such third-party acknowledgments normally appear.
 *   3. The names "The National Cancer Institute", "NCI" and "NGIT" must 
 *      not be used to endorse or promote products derived from this software.
 *   4. This license does not authorize the incorporation of this software
 *      into any third party proprietary programs. This license does not 
 *      authorize the recipient to use any trademarks owned by either NCI 
 *      or NGIT 
 *   5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED 
 *      WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES 
 *      OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE 
 *      DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
 *      NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, 
 *      INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
 *      BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 *      LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 *      CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT 
 *      LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 *      ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 *      POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * @author EVS Team (David Yee)
 * @version 1.0
 */

public class StringUtils {
    public static final String SEPARATOR =
        "----------------------------------------"
            + "----------------------------------------";
    public static final String SEPARATOR_EQUAL =
        "========================================"
            + "========================================";
    public static final String HTML_SPACE = "&nbsp;";

    public static String toHtml(String text) {
        text = text.replaceAll("\n", "<br/>");
        text = text.replaceAll("  ", "&nbsp;&nbsp;");
        return text;
    }

    public static String getSpaceIfBlank(String value) {
        if (value == null || value.trim().length() <= 0)
            return HTML_SPACE;
        return value;
    }

    public static String getSpaceIfBlank(Boolean value) {
        if (value == null)
            return HTML_SPACE;
        return value.toString();
    }

    public static String getSpaceIfBlank(Character value) {
        if (value == null)
            return HTML_SPACE;
        return getSpaceIfBlank(value.toString());
    }

    public static void debug(Logger logger, String text, List<?> list) {
        if (text != null && text.length() > 0)
            logger.debug(text);

        if (list == null)
            return;

        int i = 0;
        Iterator<?> iterator = list.iterator();
        while (iterator.hasNext()) {
            logger.debug("  " + i + ")" + iterator.next().toString());
            ++i;
        }
    }

    public static void append(StringBuffer buffer, String value,
        String delimiter) {
        if (buffer.length() > 0)
            buffer.append(delimiter);
        buffer.append(value);
    }

    public static String toString(List<?> list, String delimiter) {
        if (list == null)
            return "";

        Iterator<?> iterator = list.iterator();
        StringBuffer buffer = new StringBuffer();
        while (iterator.hasNext()) {
            String value = iterator.next().toString();
            append(buffer, value, delimiter);
        }
        return buffer.toString();
    }

    public static String toString(List<?> list) {
        return toString(list, ", ");
    }

    public static void debugSameLine(Logger logger, String text, List<?> list) {
        StringBuffer buffer = new StringBuffer();
        String values = toString(list);
        if (text != null && text.length() > 0)
            buffer.append(text);
        if (list != null) {
            buffer.append("[size=" + list.size() + "] ");
            buffer.append(values);
        }
        logger.debug(buffer.toString());
    }

    public static void debug(boolean displayInMultipleLines, Logger logger,
        String text, List<?> list) {
        if (displayInMultipleLines)
            debug(logger, text, list);
        else
            debugSameLine(logger, text, list);
    }

    public static String truncate(int maxChar, String text) {
        if (text.length() <= maxChar)
            return text;

        String dots = " ...";
        text = text.substring(0, maxChar - dots.length());
        text += dots;
        return text;
    }

    public static String[] toStrings(String value, String delimiter,
        boolean includeDelimiter, boolean trim) {
        ArrayList<String> list = new ArrayList<String>();
        if (value != null) {
            StringTokenizer tokenizer =
                new StringTokenizer(value, delimiter, includeDelimiter);
            while (tokenizer.hasMoreElements()) {
                String s = tokenizer.nextToken();
                if (trim)
                    s = s.trim();
                if (s.length() > 0)
                    list.add(s);
            }
        }
        return list.toArray(new String[list.size()]);
    }

    public static String[] toStrings(String value, String delimiter,
        boolean includeDelimiter) {
        return toStrings(value, delimiter, includeDelimiter, true);
    }
}
