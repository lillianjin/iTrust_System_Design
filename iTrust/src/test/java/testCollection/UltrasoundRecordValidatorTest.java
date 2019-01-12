package testCollection;

import edu.ncsu.csc.itrust.model.old.validate.UltrasoundRecordValidator;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.validate.ValidationFormat;
import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;
import edu.ncsu.csc.itrust.model.old.enums.PregnancyStatus;


public class UltrasoundRecordValidatorTest extends TestCase {
    /**
     * testObstetricsRecordInitialAllErrors
     *
     * @throws Exception
     */
    public void testObstetricsRecordInitialAllErrors() throws Exception {

        UltrasoundRecordBean p = new UltrasoundRecordBean();
        p.setVisitDate("");
        p.setCrownRumpLength(-6);
        p.setBiparietalDiameter(-5);
        p.setHeadCircumference(-2);
        p.setFemurLength(-5);
        p.setOccipitofrontalDiameter(-7);
        p.setAbdominalCircumference(-3);
        p.setHumerusLength(-5);
        p.setEstimatedFetalWeight(-5);

        try {
            new UltrasoundRecordValidator().validate(p);
            fail("exception should have been thrown");
        } catch (FormValidationException e) {
            assertEquals("Visit Date is a required field", e.getErrorList().get(0));
            assertEquals("Crown rump length: "+ ValidationFormat.LENGTH_OV.getDescription(), e.getErrorList().get(1));
            assertEquals("Biparietal diameter: "+ ValidationFormat.LENGTH_OV.getDescription(), e.getErrorList().get(2));
            assertEquals("Head circumference: "+ ValidationFormat.LENGTH_OV.getDescription(), e.getErrorList().get(3));
            assertEquals("Femur length: "+ ValidationFormat.LENGTH_OV.getDescription(), e.getErrorList().get(4));
            assertEquals("Occipitofrontal diameter: "+ ValidationFormat.LENGTH_OV.getDescription(), e.getErrorList().get(5));
            assertEquals("Abdominal circumference: "+ ValidationFormat.LENGTH_OV.getDescription(), e.getErrorList().get(6));
            assertEquals("Humerus length: "+ ValidationFormat.LENGTH_OV.getDescription(), e.getErrorList().get(7));
            assertEquals("Estimated fetal weight: "+ ValidationFormat.WEIGHT.getDescription(), e.getErrorList().get(8));

        }

    }

}
