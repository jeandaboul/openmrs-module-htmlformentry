package org.openmrs.module.htmlformentry.widget;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.HtmlFormEntryUtil;
import org.openmrs.module.htmlformentry.FormEntryContext.Mode;
import org.openmrs.module.htmlformentry.element.PersonStub;
import org.springframework.util.StringUtils;

public class PersonStubWidget implements Widget {
    
    private PersonStub person;
    private List<PersonStub> options;
    
    public PersonStubWidget() { }

    public void setInitialValue(Object initialValue) {
        person = (PersonStub) initialValue;
    }
    
    public String generateHtml(FormEntryContext context) {
        if (context.getMode() == Mode.VIEW) {
            if (person != null)
                return WidgetFactory.displayValue(person.getGivenName() + " " + person.getFamilyName());
            else
                return "";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("<select name=\"" + context.getFieldName(this) + "\">");
        // TODO translate
        sb.append("\n<option value=\"\">");
        sb.append(Context.getMessageSourceService().getMessage("general.choose") + "...");
        sb.append("</option>");
        List<PersonStub> personList;
        if (options != null) {
            personList = options;
        } else {
            // if no options are specified, set the personList to an empty list
            // (fetching *all* the persons doesn't really make much sense)
            personList = new LinkedList<PersonStub>();
            //personList = Context.getPersonService().getPeople("", true);
        }
        for (PersonStub p : personList) {
            sb.append("\n<option");
            if (person != null && person.equals(p))
                sb.append(" selected=\"true\"");
            sb.append(" value=\"" + p.getPersonId() + "\">").append(p.getGivenName() + " " + p.getFamilyName()).append("</option>");
        }
        sb.append("</select>");
        return sb.toString();
    }
    
    /**
     * Sets the Persons to use as options for this widget
     * 
     * @param options
     */
    public void setOptions(List<PersonStub> options) {
        this.options = options;
    }
    
    public Object getValue(FormEntryContext context, HttpServletRequest request) {
        String val = request.getParameter(context.getFieldName(this));
        if (StringUtils.hasText(val))
            return HtmlFormEntryUtil.convertToType(val, Person.class);
        return null;
    }
    
    
}
