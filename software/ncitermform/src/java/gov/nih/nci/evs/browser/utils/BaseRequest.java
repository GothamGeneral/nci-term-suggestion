package gov.nih.nci.evs.browser.utils;

import java.util.*;

import javax.servlet.http.*;

import org.apache.log4j.Logger;

public class BaseRequest {
    private static Logger _logger = Logger.getLogger(BaseRequest.class);
    public static final String[] EMPTY_PARAMETERS = new String[] {};
    protected final String INDENT = "    ";
    protected HttpServletRequest _request = HTTPUtils.getRequest();
    protected String[] _parameters = EMPTY_PARAMETERS;
    protected HashMap<String, String> _parametersHashMap = null;

    protected void setDefaulSessiontAttribute(
        HttpServletRequest request, String name, Object value) {
        Object v = request.getSession().getAttribute(name);
        if (v == null)
            request.getSession().setAttribute(name, value);
    }
    
    private HashMap<String, String> getParametersHashMap(
        HttpServletRequest request, String[] parameters) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        for (int i = 0; i < parameters.length; ++i) {
            String key = parameters[i];
            String value = (String) request.getParameter(key);
            if (value == null)
                value = "[Not Set]";
            hashMap.put(key, value);
        }
        return hashMap;
    }

    protected void setParameters(String[] parameters) {
        _parameters = parameters;
        _parametersHashMap = getParametersHashMap(_request, parameters);
    }

    private String debugParameters(String text, String[] parameters,
        HashMap<String, String> parametersHashMap) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(text + "\n");
        for (int i = 0; i < parameters.length; ++i) {
            String parameter = parameters[i];
            buffer.append("  * ");
            buffer.append(parameter + ": ");
            buffer.append(parametersHashMap.get(parameter));
            buffer.append("\n");
        }
        _logger.debug(StringUtils.SEPARATOR);
        _logger.debug(buffer.toString());
        return buffer.toString();
    }

    protected String debugParameters(String text) {
        return debugParameters(text, _parameters, _parametersHashMap);
    }

    public void clear() {
        clearAttributes();
        setParameters(EMPTY_PARAMETERS);
    }

    private void updateAttributes(HttpServletRequest request,
        String[] parameters, HashMap<String, String> parametersHashMap) {
        for (int i = 0; i < parameters.length; ++i) {
            String parameter = parameters[i];
            String value = parametersHashMap.get(parameter);
            request.setAttribute(parameter, value);
        }
        debugParameters("HTTPUtils.updateAttributes:", parameters,
            parametersHashMap);
    }

    protected void updateAttributes(String[] parameters) {
        updateAttributes(_request, parameters, _parametersHashMap);
    }

    protected void updateAttributes() {
        updateAttributes(_parameters);
    }

    private void clearAttributes(HttpServletRequest request,
        String[] parameters) {
        for (int i = 0; i < parameters.length; ++i) {
            String parameter = parameters[i];
            request.setAttribute(parameter, null);
        }
    }

    protected void clearAttributes(String[] parameters) {
        clearAttributes(_request, parameters);
    }

    protected void clearAttributes() {
        clearAttributes(_parameters);
    }

    private void updateSessionAttributes(HttpServletRequest request, 
        String[] parameters) {
        for (int i = 0; i < parameters.length; ++i) {
            String name = parameters[i];
            String value = request.getParameter(name);
            request.getSession().setAttribute(name, value);
        }
    }

    protected void updateSessionAttributes(String[] parameters) {
        updateSessionAttributes(_request, parameters);
    }

    private void clearSessionAttributes(HttpServletRequest request, 
        String[] parameters) {
        for (int i = 0; i < parameters.length; ++i) {
            String name = parameters[i];
            request.getSession().setAttribute(name, null);
        }
    }
    protected void clearSessionAttributes(String[] parameters) {
        clearSessionAttributes(_request, parameters);
    }
}
