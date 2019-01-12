package testCollection;

import edu.ncsu.csc.itrust.model.old.beans.ChildbirthRecordBean;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.model.old.enums.ChildbirthVisitType;

import java.text.ParseException;
import java.text.SimpleDateFormat;


public class ChildbirthRecordBeanTest extends TestCase {
    public void testGet() throws ParseException {
        ChildbirthRecordBean bean = new ChildbirthRecordBean();

        bean.setOid(1);
        bean.setMid(806);
        bean.setVisitDate("10/20/2018");
        bean.setChildbirthVisitType(ChildbirthVisitType.fromString("Pre-scheduled"));
        bean.setDeliveryType(DeliveryType.fromString("Vaginal Delivery"));
        bean.setPitocin(true);
        bean.setNitrousOxide(true);
        bean.setPethidine(false);
        bean.setEpiduralAnaesthesia(false);
        bean.setMagnesiumSulfate(false);
        bean.setRhImmuneGlobulin(false);
        bean.setPitocinDosage(2.0);
        bean.setNitrousOxideDosage(1.0);
        bean.setPethidineDosage(1.0);
        bean.setEpiduralAnaesthesiaDosage(1.0);
        bean.setMagnesiumSulfateDosage(2.0);
        bean.setRhImmuneGlobulinDosage(2.0);
        bean.setBabyID(200);
        bean.setDeliveryTime("1/20/2018");
        bean.setBabySex("Male");

        assertEquals(bean.getOid(), 1);
        assertEquals(bean.getMid(), 806);
        assertEquals(bean.getVisitDateString(), "10/20/2018");
        assertEquals(bean.getVisitDate(), new SimpleDateFormat("MM/dd/yyyy").parse("10/20/2018"));
        assertEquals(bean.getChildbirthVisitType().toString(), "Pre-scheduled");
        assertEquals(bean.getDeliveryType().toString(), "Vaginal Delivery");
        assertEquals(bean.getPitocin(), true);
        assertEquals(bean.getNitrousOxide(), true);
        assertEquals(bean.getPethidine(), false);
        assertEquals(bean.getEpiduralAnaesthesia(), false);
        assertEquals(bean.getMagnesiumSulfate(), false);
        assertEquals(bean.getRhImmuneGlobulin(), false);
        assertEquals(bean.getPitocinDosage(), 2.0);
        assertEquals(bean.getNitrousOxideDosage(), 1.0);
        assertEquals(bean.getPethidineDosage(), 1.0);
        assertEquals(bean.getEpiduralAnaesthesiaDosage(), 1.0);
        assertEquals(bean.getMagnesiumSulfateDosage(), 2.0);
        assertEquals(bean.getRhImmuneGlobulinDosage(), 2.0);
        assertEquals(bean.getBabyID(), 200);
        assertEquals(bean.getDeliverTime(), new SimpleDateFormat("MM/dd/yyyy").parse("1/20/2018"));
        assertEquals(bean.getDeliveryTimeString(), "1/20/2018");
        assertEquals(bean.getBabySex(), "Male");
    }

    public void testNull() {
        //make a bean with improperly formatted dates
        ChildbirthRecordBean bean = new ChildbirthRecordBean();
        bean.setVisitDate("");
        bean.setDeliveryTime("");

        assertEquals(bean.getVisitDate(),null);
        assertEquals(bean.getDeliverTime(),null);
    }

    public void testError() {
        //make a bean with improperly formatted dates
        ChildbirthRecordBean bean = new ChildbirthRecordBean();
        bean.setVisitDate("20180817");
        bean.setDeliveryTime("20189716");

        assertEquals(bean.getVisitDate(),null);
        assertEquals(bean.getDeliverTime(),null);
    }
}
