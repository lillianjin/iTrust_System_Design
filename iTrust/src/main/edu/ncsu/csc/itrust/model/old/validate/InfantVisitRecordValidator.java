package edu.ncsu.csc.itrust.model.old.validate;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.InfantVisitRecordBean;

public class InfantVisitRecordValidator extends BeanValidator<InfantVisitRecordBean>{
    /**
     * default constructor
     */
    public InfantVisitRecordValidator() { }

    /**
     * @param p InfantVisitRecordBean
     * @throws FormValidationException
     */
    public void validate(InfantVisitRecordBean p) throws FormValidationException {
        ErrorList errorList = new ErrorList();
        if (p != null) {
            if (p.getVisitDate() == null || p.getVisitDate().equals("")) {
                errorList.addIfNotNull("Visit Date is a required field");
            }

            //errorList.addIfNotNull(checkFormat("Gender", p.getGender().toString(), ValidationFormat.GENDERCOD, false));
            errorList.addIfNotNull(checkFormat("Weight", p.getWeight(), ValidationFormat.WEIGHT_OV, false));
            errorList.addIfNotNull(checkFormat("Height", p.getHeight(), ValidationFormat.HEIGHT_OV, false));
            errorList.addIfNotNull(checkFormat("Heart Beat Rate", p.getHeartbeatRate(), ValidationFormat.HEART_BEAT_RATE, false));
            //errorList.addIfNotNull(checkFormat("Blood Type", p.getBloodType().toString(), ValidationFormat.BLOODTYPE, false));
            errorList.addIfNotNull(checkFormat("Blood Pressure", p.getBloodPressureL(), ValidationFormat.SYSTOLIC_BLOOD_PRESSURE, false));
            errorList.addIfNotNull(checkFormat("Blood Pressure", p.getBloodPressureH(), ValidationFormat.DIASTOLIC_BLOOD_PRESSURE, false));

        }

        if (errorList.hasErrors()) {
            for (String err : errorList.getMessageList()) {
                System.out.println(err);
            }
            throw new FormValidationException(errorList);
        }
    }
}
