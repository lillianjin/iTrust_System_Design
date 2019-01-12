package edu.ncsu.csc.itrust.model.old.validate;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;
import edu.ncsu.csc.itrust.exception.ErrorList;

public class UltrasoundRecordValidator extends BeanValidator<UltrasoundRecordBean>{
    /**
     * default constructor
     */
    public UltrasoundRecordValidator() { }

    /**
     * @param p UltrasoundRecordBean
     * @throws FormValidationException
     */
    public void validate(UltrasoundRecordBean p) throws FormValidationException {
        ErrorList errorList = new ErrorList();
        if (p != null) {
            if (p.getVisitDate() == null || p.getVisitDate().equals("")) {
            	errorList.addIfNotNull("Visit Date is a required field");
            }
            errorList.addIfNotNull(checkFormat("Crown rump length", p.getCrownRumpLength(), ValidationFormat.LENGTH_OV, false));
            errorList.addIfNotNull(checkFormat("Biparietal diameter", p.getBiparietalDiameter(), ValidationFormat.LENGTH_OV, false));
            errorList.addIfNotNull(checkFormat("Head circumference", p.getHeadCircumference(), ValidationFormat.LENGTH_OV, false));
            errorList.addIfNotNull(checkFormat("Femur length", p.getFemurLength(), ValidationFormat.LENGTH_OV, false));
            errorList.addIfNotNull(checkFormat("Occipitofrontal diameter", p.getOccipitofrontalDiameter(), ValidationFormat.LENGTH_OV, false));
            errorList.addIfNotNull(checkFormat("Abdominal circumference", p.getAbdominalCircumference(), ValidationFormat.LENGTH_OV, false));
            errorList.addIfNotNull(checkFormat("Humerus length", p.getHumerusLength(), ValidationFormat.LENGTH_OV, false));
            errorList.addIfNotNull(checkFormat("Estimated fetal weight", p.getEstimatedFetalWeight(), ValidationFormat.WEIGHT, false));
        }
        
        if (errorList.hasErrors()) {
        	for (String err : errorList.getMessageList()) {
        		System.out.println(err);
        	}
        	throw new FormValidationException(errorList);
        }
    }
}
