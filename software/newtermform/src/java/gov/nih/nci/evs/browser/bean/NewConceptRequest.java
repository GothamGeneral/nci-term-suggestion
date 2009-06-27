package gov.nih.nci.evs.browser.bean;

import gov.nih.nci.evs.browser.properties.AppProperties;
import gov.nih.nci.evs.browser.utils.*;

import javax.servlet.http.*;

public class NewConceptRequest extends RequestBase {
    private final String EMAIL = "email";
    private final String OTHER = "other";
    private final String VOCABULARY = "vocabulary";
    private final String TERM = "term";
    private final String SYNONYMS = "synonyms";
    private final String PARENT_CODE = "parentCode";
    private final String DEFINITION = "definition";
    private final String REASON = "reason";

    public NewConceptRequest(HttpServletRequest request) {
        super(request);
        setParameters(new String[] { EMAIL, OTHER, VOCABULARY, TERM, SYNONYMS,
                PARENT_CODE, DEFINITION, REASON });
    }

    public String submit() {
        String mailServer = AppProperties.getInstance().getMailSmtpServer();
        String from = _parametersHashMap.get(EMAIL);
        String[] recipients = AppProperties.getInstance().getContactUsRecipients();
        String subject = getSubject();
        String emailMsg = getEmailMesage();
        
        try {
            MailUtils.postMail(mailServer, from, recipients, subject, emailMsg);
        } catch (Exception e) {
            _request.getSession().setAttribute("message",
                    Utils.toHtml(e.getLocalizedMessage()));
            e.printStackTrace();
            return "message";
        }
        
        _request.getSession().setAttribute("message",
                Utils.toHtml(emailMsg));
        updateAllSessionAttributes();
        return "message";
    }
    
    private String getSubject() {
        String term = _parametersHashMap.get(TERM);
        String value = "Request New Concept";
        if (term.length() > 0)
            value += " (" + term + ")";
        return value;
    }
    
    private String getEmailMesage() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getSubject());
        for (int i = 0; i < _parameters.length; ++i) {
            String parameter = _parameters[i];
            buffer.append("\n* ");
            buffer.append(parameter + ": ");
            buffer.append(_parametersHashMap.get(parameter));
        }
        return buffer.toString();
    }
}
