package edu.ncsu.csc.itrust.unit.bean;

import edu.ncsu.csc.itrust.model.old.beans.ObstetricsRecordBean;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.model.old.enums.PregnancyStatus;
import junit.framework.TestCase;

public class ObstetricsRecordBeanTest extends TestCase {

    private ObstetricsRecordBean bean = new ObstetricsRecordBean();

    @Override
    public void setUp() throws Exception{
        bean.setBabyCount(2);
        bean.setBloodPressureH(120);
        bean.setBloodPressureL(60);
        bean.setDeliveryType(DeliveryType.fromString("Vaginal Delivery"));
        bean.setEDD("10/20/2018");
        bean.setFHR(3);
        bean.setHoursInLabor(4.4);
        bean.setinitDate("8/20/2018");
        bean.setLMP("11/18/2018");
        bean.setLyingPlacenta(true);
        bean.setMid(806);
        bean.setMultiPregnancy(false);
        bean.setPregStatus(PregnancyStatus.fromString("Complete"));
        bean.setWeeksPregnant("10");
        bean.setWeight(6.6);
        bean.setWeightGain(1.1);
        bean.setYearConception(2);

    }


    public void testBean(){
        assertEquals(bean.getBabyCount(), 2);
        assertEquals(bean.getBloodPressureH(), 120);
        assertEquals(bean.getBloodPressureL(), 60);
        assertEquals(bean.getDeliveryType().toString(), "Vaginal Delivery");
        assertEquals(bean.getEDDString(), "10/20/2018");
        assertEquals(bean.getFHR(), 3);
        assertEquals(bean.getHoursInLabor(), 4.4);
        assertEquals(bean.geinitDateString(), "8/20/2018");
        assertEquals(bean.getLMPString(), "11/18/2018");
        assertEquals(bean.getLyingPlacenta(), true);
        assertEquals(bean.getMid(), 806);
        assertEquals(bean.getMultiPregnancy(), false);
        assertEquals(bean.getPregStatus().toString(), "Complete");
        assertEquals(bean.getWeeksPregnant(), "10");
        assertEquals(bean.getWeight(), 6.6);
        assertEquals(bean.getWeightGain(), 1.1);
        assertEquals(bean.getYearConception(), 2);

    }
}
