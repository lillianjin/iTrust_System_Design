package testCollection;


import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;
import junit.framework.TestCase;

import java.text.ParseException;
import java.text.SimpleDateFormat;


/*
    Test for UltrasoundRecordBean
 */
public class UltrasoundRecordBeanTest extends TestCase{

    private UltrasoundRecordBean bean = new UltrasoundRecordBean();

    @Override
    protected void setUp() throws Exception {
        bean.setMid(113242);
        bean.setFetusid(12123);
        bean.setVisitDate("11/15/2018");
        bean.setAbdominalCircumference(5.3);
        bean.setBiparietalDiameter(43.2);
        bean.setCrownRumpLength(2.4);
        bean.setEstimatedFetalWeight(23.4);
        bean.setFemurLength(4.2);
        bean.setHeadCircumference(5.4);
        bean.setHumerusLength(34.3);
        bean.setOccipitofrontalDiameter(5.3);
    }


    public void testBean(){
        assertEquals(bean.getMid(), 113242);
        assertEquals(bean.getFetusID(), 12123);
        try {
            assertEquals(bean.getVisitDate(), new SimpleDateFormat("MM/dd/yyyy").parse("11/15/2018"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertEquals(bean.getAbdominalCircumference(), 5.3);
        assertEquals(bean.getBiparietalDiameter(), 43.2);
        assertEquals(bean.getCrownRumpLength(), 2.4);
        assertEquals(bean.getEstimatedFetalWeight(), 23.4);
        assertEquals(bean.getFemurLength(), 4.2);
        assertEquals(bean.getHeadCircumference(), 5.4);
        assertEquals(bean.getHumerusLength(), 34.3);
        assertEquals(bean.getOccipitofrontalDiameter(), 5.3);

    }

    public void testNull() {
        bean.setVisitDate(null);
        assertEquals(bean.getVisitDate(), null);
    }

    public void testError() {
        bean.setVisitDate("12345678");
        assertEquals(bean.getVisitDate(), null);
    }

}
