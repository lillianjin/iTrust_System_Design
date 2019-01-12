package edu.ncsu.csc.itrust.model.old.validate;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricsRecordBean;
import edu.ncsu.csc.itrust.model.old.enums.PregnancyStatus;

import edu.ncsu.csc.itrust.exception.ErrorList;

public class ObstetricsRecordValidator extends BeanValidator<ObstetricsRecordBean>{
    /**
     * default constructor
     */
    public ObstetricsRecordValidator() { }

    /**
     * @param p ObstetricsRecordBean
     * @throws FormValidationException
     */
    public void validate(ObstetricsRecordBean p) throws FormValidationException {
        ErrorList errorList = new ErrorList();
        if (p != null) {
            //if it is a initial record
            if (p.getPregStatus().equals(PregnancyStatus.Initial)) {
                if (p.getLMP() != null && p.getinitDate() != null && p.getLMP().after(p.getinitDate())) {
                    errorList.addIfNotNull("Last menstrual period cannot be after current date");
                }
                errorList.addIfNotNull(checkFormat("Weeks-Days pregnant", p.getWeeksPregnant(), ValidationFormat.WEEKS_PREGNANT, false));
            }
            // else if it is a prior pregnancy
            else if (p.getPregStatus().equals(PregnancyStatus.Complete)) {
                errorList.addIfNotNull(checkFormat("Year Conception", p.getYearConception(), ValidationFormat.YEAR, false));
                errorList.addIfNotNull(checkFormat("Weeks Pregnant", p.getWeeksPregnant(), ValidationFormat.WEEKS_PREGNANT, false));
                errorList.addIfNotNull(checkFormat("Hours in Labor", p.getHoursInLabor(), ValidationFormat.HOURS_LABOR, false));
            }
            // else it is an office visit
            else {
                if (p.getinitDate() == null || p.getinitDate().equals("")) {
                    errorList.addIfNotNull("Current Date is a required field");
                }
                errorList.addIfNotNull(checkFormat("Weeks Pregnant", p.getWeeksPregnant(), ValidationFormat.WEEKS_PREGNANT_OV, false));
                errorList.addIfNotNull(checkFormat("Weight", p.getWeight(), ValidationFormat.WEIGHT, false));
                errorList.addIfNotNull(checkFormat("Blood Pressure Diastolic", p.getBloodPressureL(), ValidationFormat.DIASTOLIC_BLOOD_PRESSURE, false));
                errorList.addIfNotNull(checkFormat("Blood Pressure Systolic", p.getBloodPressureH(), ValidationFormat.SYSTOLIC_BLOOD_PRESSURE, false));
                errorList.addIfNotNull(checkFormat("Fetal Heart Rate", p.getFHR(), ValidationFormat.FHR, false));
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
