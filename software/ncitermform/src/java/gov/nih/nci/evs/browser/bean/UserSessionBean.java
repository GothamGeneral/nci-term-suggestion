package gov.nih.nci.evs.browser.bean;

import gov.nih.nci.evs.browser.webapp.*;

import javax.faces.context.*;
import javax.servlet.http.*;

public class UserSessionBean {
	private HttpServletRequest getRequest() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.
        getCurrentInstance().getExternalContext().getRequest();
		return request;
	}
	
    public String changeRequest() {
        getRequest().setAttribute(
                FormRequest.MESSAGE, "UserSessionBean.changeRequest");
        return FormRequest.MESSAGE_STATE;
    }
    
    public String requestNewConcept() {
        NewConceptRequest request = new NewConceptRequest(getRequest());
        return request.submitForm();
    }
    
    public String clearNewConcept() {
        NewConceptRequest request = new NewConceptRequest(getRequest());
        return request.clearForm();  
    }

    public String requestModifyConcept() {
        ModifyConceptRequest request = new ModifyConceptRequest(getRequest());
        return request.submitForm();
    }
    
    public String clearModifyConcept() {
        ModifyConceptRequest request = new ModifyConceptRequest(getRequest());
        return request.clearForm();  
    }
    
    public String requestSuggestion() {
        SuggestionRequest request = new SuggestionRequest(getRequest());
        return request.submitForm();
    }
    
    public String clearSuggestion() {
        SuggestionRequest request = new SuggestionRequest(getRequest());
        return request.clearForm();  
    }

    public String contactUs() {
        ContactUsRequest request = new ContactUsRequest(getRequest());
        return request.submitForm();
    }
    
    public String clearContactUs() {
        ContactUsRequest request = new ContactUsRequest(getRequest());
        return request.clearForm();  
    }
}
