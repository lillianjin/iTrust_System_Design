package edu.ncsu.csc.itrust.model.old.validate;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.ChildbirthRecordBean;

import edu.ncsu.csc.itrust.exception.ErrorList;

public class ChildbirthRecordValidator extends BeanValidator<ChildbirthRecordBean> {
	/**
     * default constructor
     */
    public ChildbirthRecordValidator() { }
    /**
     * @param p ChildbirthRecordBean
     * @throws FormValidationException
     */
    public void validate(ChildbirthRecordBean p) throws FormValidationException {
        ErrorList errorList = new ErrorList();
        if (p != null) {
            
            
            if (p.getVisitDate() == null || p.getVisitDate().equals("")) {
                errorList.addIfNotNull("Visit Date is a required field");
            }
            
            if (errorList.hasErrors()) {
            	// added to print out format errors in console since the IOException is does not function correctly
            	for (String err : errorList.getMessageList()) {
            		System.out.println(err);
            	}
                throw new FormValidationException(errorList);
            }
        }
    }
}
