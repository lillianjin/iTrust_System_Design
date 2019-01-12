package testCollection;

import edu.ncsu.csc.itrust.model.old.validate.ChildbirthRecordValidator;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.validate.ValidationFormat;
import edu.ncsu.csc.itrust.model.old.beans.ChildbirthRecordBean;

public class ChildbirthRecordValidatorTest extends TestCase{

    /**
     * testChildbirthRecordAllCorrect
     *
     * @throws Exception
     */
    public void testChildbirthRecordAllCorrect() throws Exception {
        ChildbirthRecordBean crb = new ChildbirthRecordBean();
        crb.setVisitDate("10/10/2018");
        new ChildbirthRecordValidator().validate(crb);
    }


    /**
     * testChildbirthRecordAllErrors
     *
     * @throws Exception
     */
    public void testChildbirthRecordAllErrors() throws Exception {
        ChildbirthRecordBean crb = new ChildbirthRecordBean();
        crb.setVisitDate("");
        try {
            new ChildbirthRecordValidator().validate(crb);
            fail("exception should have been thrown");
        } catch (FormValidationException e) {
            assertEquals("Visit Date is a required field", e.getErrorList().get(0));
        }

    }

}

