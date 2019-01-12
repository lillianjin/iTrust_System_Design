package testCollection;

import edu.ncsu.csc.itrust.model.old.validate.ObstetricsRecordValidator;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.validate.ValidationFormat;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricsRecordBean;
import edu.ncsu.csc.itrust.model.old.enums.PregnancyStatus;


public class ObstetricsRecordValidatorTest extends TestCase {

    /**
     * testObstetricsRecordInitialAllErrors
     *
     * @throws Exception
     */
    public void testObstetricsRecordInitialAllErrors() throws Exception {

        ObstetricsRecordBean p = new ObstetricsRecordBean();
        p.setPregStatus(PregnancyStatus.fromString("Initial"));
        p.setLMP("10/10/2018");
        p.setinitDate("09/09/2018");
        p.setWeeksPregnant("");

        try {
            new ObstetricsRecordValidator().validate(p);
            fail("exception should have been thrown");
        } catch (FormValidationException e) {
            assertEquals("Last menstrual period cannot be after current date", e.getErrorList().get(0));
            assertEquals("Weeks-Days pregnant: "+ ValidationFormat.WEEKS_PREGNANT.getDescription(), e.getErrorList().get(1));
        }

    }

    /**
     * testObstetricsRecordCompleteAllErrors
     *
     * @throws Exception
     */
    public void testObstetricsRecordCompleteAllErrors() throws Exception {

        ObstetricsRecordBean p = new ObstetricsRecordBean();
        p.setPregStatus(PregnancyStatus.fromString("Complete"));
        p.setYearConception(-2);
        p.setWeeksPregnant("108-108");
        p.setHoursInLabor(-3);

        try {
            new ObstetricsRecordValidator().validate(p);
            fail("exception should have been thrown");
        } catch (FormValidationException e) {
            assertEquals("Year Conception: "+ ValidationFormat.YEAR.getDescription(), e.getErrorList().get(0));
            assertEquals("Weeks Pregnant: "+ ValidationFormat.WEEKS_PREGNANT.getDescription(), e.getErrorList().get(1));
            assertEquals("Hours in Labor: "+ ValidationFormat.HOURS_LABOR.getDescription(), e.getErrorList().get(2));
        }

    }


    /**
     * testObstetricsRecordOfficeVisitAllErrors
     *
     * @throws Exception
     */
    public void testObstetricsRecordOfficeVisitAllErrors() throws Exception {

        ObstetricsRecordBean p = new ObstetricsRecordBean();
        p.setPregStatus(PregnancyStatus.fromString("OfficeVisit"));
        p.setinitDate("");
        p.setWeeksPregnant("108-108");
        p.setWeight(-3);
        p.setBloodPressureH(150000);
        p.setBloodPressureL(100000);
        p.setFHR(-1);

        try {
            new ObstetricsRecordValidator().validate(p);
            fail("exception should have been thrown");
        } catch (FormValidationException e) {
            assertEquals("Current Date is a required field", e.getErrorList().get(0));
            assertEquals("Weeks Pregnant: "+ ValidationFormat.WEEKS_PREGNANT_OV.getDescription(), e.getErrorList().get(1));
            assertEquals("Weight: "+ ValidationFormat.WEIGHT.getDescription(), e.getErrorList().get(2));
            assertEquals("Blood Pressure Diastolic: "+ ValidationFormat.DIASTOLIC_BLOOD_PRESSURE.getDescription(), e.getErrorList().get(3));
            assertEquals("Blood Pressure Systolic: "+ ValidationFormat.SYSTOLIC_BLOOD_PRESSURE.getDescription(), e.getErrorList().get(4));
            assertEquals("Fetal Heart Rate: "+ ValidationFormat.FHR.getDescription(), e.getErrorList().get(5));
        }

    }



}






