package testCollection;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricsRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricsRecordDAO;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.model.old.enums.PregnancyStatus;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;
import org.junit.Test;
import edu.ncsu.csc.itrust.unit.DBBuilder;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.*;


public class ObstetricsRecordDAOTest extends TestCase {
        ObstetricsRecordBean bean;
        DAOFactory factory = TestDAOFactory.getTestInstance();
        ObstetricsRecordDAO orDAO = new ObstetricsRecordDAO(factory);

        //@Override
        public void setUp() throws Exception {
            TestDataGenerator gen = new TestDataGenerator();
            gen.clearAllTables();

            bean = new ObstetricsRecordBean();
            bean.setOid(1);
            bean.setBabyCount(2);
            bean.setBloodPressureH(120);
            bean.setBloodPressureL(60);
            bean.setDeliveryType(DeliveryType.fromString("Vaginal Delivery"));
            bean.setEDD("10/20/2018");
            bean.setFHR(3);
            bean.setHoursInLabor(4.4);
            bean.setinitDate("08/20/2018");
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


        @Test
        public void testObstetricsRecord() throws DBException {
            // add the bean
            orDAO.addObstetricsRecord(bean);
            // edit the bean
            orDAO.editObstetricsRecordOV(bean);

            // get the bean by MID
            List<ObstetricsRecordBean> list = orDAO.getObstetricsRecordsByMID(806);

            assertEquals(list.get(0).getOid(), bean.getOid());
            assertEquals(list.get(0).getBabyCount(), bean.getBabyCount());
            assertEquals(list.get(0).getBloodPressureH(), bean.getBloodPressureH());
            assertEquals(list.get(0).getBloodPressureL(), bean.getBloodPressureL());
            assertEquals(list.get(0).getDeliveryType().toString(), bean.getDeliveryType().toString());
            assertEquals(list.get(0).getEDDString(), bean.getEDDString());
            assertEquals(list.get(0).getEDD(), bean.getEDD());
            assertEquals(list.get(0).getFHR(), bean.getFHR());
            assertEquals(list.get(0).getHoursInLabor(), bean.getHoursInLabor());
            assertEquals(list.get(0).getinitDate(), bean.getinitDate());
            assertEquals(list.get(0).geinitDateString(), bean.geinitDateString());
            assertEquals(list.get(0).getLMP(), bean.getLMP());
            assertEquals(list.get(0).getLMPString(), bean.getLMPString());
            assertEquals(list.get(0).getLyingPlacenta(), bean.getLyingPlacenta());
            assertEquals(list.get(0).getMid(), bean.getMid());
            assertEquals(list.get(0).getMultiPregnancy(), bean.getMultiPregnancy());
            assertEquals(list.get(0).getPregStatus().toString(), bean.getPregStatus().toString());
            assertEquals(list.get(0).getWeeksPregnant(), bean.getWeeksPregnant());
            assertEquals(list.get(0).getWeight(), bean.getWeight());
            assertEquals(list.get(0).getWeightGain(), bean.getWeightGain());
            assertEquals(list.get(0).getYearConception(), bean.getYearConception());

        }
    @Test
    public void testErrors() throws DBException {
        DBBuilder builder = new DBBuilder();

        try {
            builder.dropTables(); //drop all tables in the DB
            ObstetricsRecordBean bean = null;
            try {
                bean = orDAO.getObstetricsRecordsByMID(806).get(0);
            } catch (DBException e) {
                assertNull(bean);
            }
            builder.createTables(); //now put them back so future tests aren't broken
        } catch(Exception e) {
            fail();
        }

    }
//    @Test
//    public void testObstetricsRecord1() throws DBException {
//        // add the bean
//        orDAO.addObstetricsRecord(bean);
//        // edit the bean
//        orDAO.editObstetricsRecordOV(bean);
//
//        // get the bean by MIDInitial
//        List<ObstetricsRecordBean> list = orDAO.getObstetricsRecordsByMIDInitial(806);
//
//        assertEquals(list.get(0).getOid(), bean.getOid());
//        assertEquals(list.get(0).getBabyCount(), bean.getBabyCount());
//        assertEquals(list.get(0).getBloodPressureH(), bean.getBloodPressureH());
//        assertEquals(list.get(0).getBloodPressureL(), bean.getBloodPressureL());
//        assertEquals(list.get(0).getDeliveryType().toString(), bean.getDeliveryType().toString());
//        assertEquals(list.get(0).getEDDString(), bean.getEDDString());
//        assertEquals(list.get(0).getEDD(), bean.getEDD());
//        assertEquals(list.get(0).getFHR(), bean.getFHR());
//        assertEquals(list.get(0).getHoursInLabor(), bean.getHoursInLabor());
//        assertEquals(list.get(0).getinitDate(), bean.getinitDate());
//        assertEquals(list.get(0).geinitDateString(), bean.geinitDateString());
//        assertEquals(list.get(0).getLMP(), bean.getLMP());
//        assertEquals(list.get(0).getLMPString(), bean.getLMPString());
//        assertEquals(list.get(0).getLyingPlacenta(), bean.getLyingPlacenta());
//        assertEquals(list.get(0).getMid(), bean.getMid());
//        assertEquals(list.get(0).getMultiPregnancy(), bean.getMultiPregnancy());
//        assertEquals(list.get(0).getPregStatus().toString(), bean.getPregStatus().toString());
//        assertEquals(list.get(0).getWeeksPregnant(), bean.getWeeksPregnant());
//        assertEquals(list.get(0).getWeight(), bean.getWeight());
//        assertEquals(list.get(0).getWeightGain(), bean.getWeightGain());
//        assertEquals(list.get(0).getYearConception(), bean.getYearConception());
//
//    }
    @Test
    public void testErrors1() throws DBException {
        DBBuilder builder = new DBBuilder();

        try {
            builder.dropTables(); //drop all tables in the DB
            ObstetricsRecordBean bean = null;
            try {
                bean = orDAO.getObstetricsRecordsByMIDInitial(806).get(0);
            } catch (DBException e) {
                assertNull(bean);
            }
            builder.createTables(); //now put them back so future tests aren't broken
        } catch(Exception e) {
            fail();
        }

    }
    @Test
    public void testObstetricsRecord2() throws DBException {
        // add the bean
        orDAO.addObstetricsRecord(bean);
        // edit the bean
        orDAO.editObstetricsRecordOV(bean);

        // get the bean by MIDPrior
        List<ObstetricsRecordBean> list = orDAO.getObstetricsRecordsByMIDPrior(806);

        assertEquals(list.get(0).getOid(), bean.getOid());
        assertEquals(list.get(0).getBabyCount(), bean.getBabyCount());
        assertEquals(list.get(0).getBloodPressureH(), bean.getBloodPressureH());
        assertEquals(list.get(0).getBloodPressureL(), bean.getBloodPressureL());
        assertEquals(list.get(0).getDeliveryType().toString(), bean.getDeliveryType().toString());
        assertEquals(list.get(0).getEDDString(), bean.getEDDString());
        assertEquals(list.get(0).getEDD(), bean.getEDD());
        assertEquals(list.get(0).getFHR(), bean.getFHR());
        assertEquals(list.get(0).getHoursInLabor(), bean.getHoursInLabor());
        assertEquals(list.get(0).getinitDate(), bean.getinitDate());
        assertEquals(list.get(0).geinitDateString(), bean.geinitDateString());
        assertEquals(list.get(0).getLMP(), bean.getLMP());
        assertEquals(list.get(0).getLMPString(), bean.getLMPString());
        assertEquals(list.get(0).getLyingPlacenta(), bean.getLyingPlacenta());
        assertEquals(list.get(0).getMid(), bean.getMid());
        assertEquals(list.get(0).getMultiPregnancy(), bean.getMultiPregnancy());
        assertEquals(list.get(0).getPregStatus().toString(), bean.getPregStatus().toString());
        assertEquals(list.get(0).getWeeksPregnant(), bean.getWeeksPregnant());
        assertEquals(list.get(0).getWeight(), bean.getWeight());
        assertEquals(list.get(0).getWeightGain(), bean.getWeightGain());
        assertEquals(list.get(0).getYearConception(), bean.getYearConception());

    }
    @Test
    public void testErrors2() throws DBException {
        DBBuilder builder = new DBBuilder();

        try {
            builder.dropTables(); //drop all tables in the DB
            ObstetricsRecordBean bean = null;
            try {
                bean = orDAO.getObstetricsRecordsByMIDPrior(806).get(0);
            } catch (DBException e) {
                assertNull(bean);
            }
            builder.createTables(); //now put them back so future tests aren't broken
        } catch(Exception e) {
            fail();
        }

    }
//    @Test
//    public void testObstetricsRecord3() throws DBException {
//        // add the bean
//        orDAO.addObstetricsRecord(bean);
//        // edit the bean
//        orDAO.editObstetricsRecordOV(bean);
//
//        // get the bean by MIDOV
//        List<ObstetricsRecordBean> list = orDAO.getObstetricsRecordsByMIDOV(806);
//
//        assertEquals(list.get(0).getOid(), bean.getOid());
//        assertEquals(list.get(0).getBabyCount(), bean.getBabyCount());
//        assertEquals(list.get(0).getBloodPressureH(), bean.getBloodPressureH());
//        assertEquals(list.get(0).getBloodPressureL(), bean.getBloodPressureL());
//        assertEquals(list.get(0).getDeliveryType().toString(), bean.getDeliveryType().toString());
//        assertEquals(list.get(0).getEDDString(), bean.getEDDString());
//        assertEquals(list.get(0).getEDD(), bean.getEDD());
//        assertEquals(list.get(0).getFHR(), bean.getFHR());
//        assertEquals(list.get(0).getHoursInLabor(), bean.getHoursInLabor());
//        assertEquals(list.get(0).getinitDate(), bean.getinitDate());
//        assertEquals(list.get(0).geinitDateString(), bean.geinitDateString());
//        assertEquals(list.get(0).getLMP(), bean.getLMP());
//        assertEquals(list.get(0).getLMPString(), bean.getLMPString());
//        assertEquals(list.get(0).getLyingPlacenta(), bean.getLyingPlacenta());
//        assertEquals(list.get(0).getMid(), bean.getMid());
//        assertEquals(list.get(0).getMultiPregnancy(), bean.getMultiPregnancy());
//        assertEquals(list.get(0).getPregStatus().toString(), bean.getPregStatus().toString());
//        assertEquals(list.get(0).getWeeksPregnant(), bean.getWeeksPregnant());
//        assertEquals(list.get(0).getWeight(), bean.getWeight());
//        assertEquals(list.get(0).getWeightGain(), bean.getWeightGain());
//        assertEquals(list.get(0).getYearConception(), bean.getYearConception());
//
//    }
    @Test
    public void testErrors3() throws DBException {
        DBBuilder builder = new DBBuilder();

        try {
            builder.dropTables(); //drop all tables in the DB
            ObstetricsRecordBean bean = null;
            try {
                bean = orDAO.getObstetricsRecordsByMIDOV(806).get(0);
            } catch (DBException e) {
                assertNull(bean);
            }
            builder.createTables(); //now put them back so future tests aren't broken
        } catch(Exception e) {
            fail();
        }

    }

    @Test
    public void testNull() {
        //make a bean with improperly formatted dates
        ObstetricsRecordBean bean = new ObstetricsRecordBean();
        bean.setinitDate(null);
        bean.setLMP(null);
        bean.setEDD(null);

        assertEquals(bean.getLMP(),null);
        assertEquals(bean.getEDD(),null);
        assertEquals(bean.getinitDate(),null);

    }
}
