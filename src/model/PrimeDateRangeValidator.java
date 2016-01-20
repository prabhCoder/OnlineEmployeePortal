package model;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.primefaces.component.calendar.Calendar;
 
@FacesValidator("primeDateRangeValidator")
public class PrimeDateRangeValidator implements Validator {
     
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    	 if (value == null) {
             return; // Let required="true" handle.
         }

         UIInput startDateComponent = (UIInput) component.getAttributes().get("startDateComponent");

         if (!startDateComponent.isValid()) {
             return; // Already invalidated. Don't care about it then.
         }

         Date startDate = (Date) startDateComponent.getValue();

         if (startDate == null) {
             return; // Let required="true" handle.
         }

         Date endDate = (Date) value;

         if (startDate.after(endDate)) {
             startDateComponent.setValid(false);
             System.out.println("invalid date");
             context.addMessage(null, new FacesMessage("Invalid date"));
             context.validationFailed();
             ((UIInput) component).setValid(false);
             startDateComponent.setValid(false);
         }
     }
}