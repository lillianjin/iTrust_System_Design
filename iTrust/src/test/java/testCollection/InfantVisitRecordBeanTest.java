package testCollection;

import edu.ncsu.csc.itrust.model.old.beans.InfantVisitRecordBean;
import junit.framework.TestCase;

import java.text.ParseException;
import java.text.SimpleDateFormat;


public class InfantVisitRecordBeanTest extends TestCase {
    public void testGet() throws ParseException {
        InfantVisitRecordBean bean = new InfantVisitRecordBean();

        bean.setIid(1);
        bean.setMid(806);
        bean.setVisitDate("10/20/2018");
        bean.setWeight(20.0);
        bean.setHeight(55.0);
        bean.setHeartbeatRate(70);
        bean.setBloodPressureL(55);
        bean.setBloodPressureH(75);

        assertEquals(bean.getIid(), 1);
        assertEquals(bean.getMid(), 806);
        assertEquals(bean.getVisitDateString(), "10/20/2018");
        assertEquals(bean.getVisitDate(), new SimpleDateFormat("MM/dd/yyyy").parse("10/20/2018"));
        assertEquals(bean.getWeight(), 20.0);
        assertEquals(bean.getHeight(), 55.0);
        assertEquals(bean.getHeartbeatRate(), 70);
        assertEquals(bean.getBloodPressureL(), 55);
        assertEquals(bean.getBloodPressureH(),75);
    }

    public void testNull() {
        //make a bean with improperly formatted dates
        InfantVisitRecordBean bean = new InfantVisitRecordBean();
        bean.setVisitDate("");

        assertEquals(bean.getVisitDate(),null);
    }

    public void testError() {
        //make a bean with improperly formatted dates
        InfantVisitRecordBean bean = new InfantVisitRecordBean();
        bean.setVisitDate("20180972");

        assertEquals(bean.getVisitDate(),null);
    }
}
