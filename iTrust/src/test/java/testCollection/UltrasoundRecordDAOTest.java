package testCollection;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.UltrasoundRecordDAO;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import org.junit.Test;
import java.sql.SQLException;
import edu.ncsu.csc.itrust.unit.DBBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class UltrasoundRecordDAOTest extends TestCase {
    UltrasoundRecordBean bean;
    DAOFactory factory = TestDAOFactory.getTestInstance();
    UltrasoundRecordDAO urDAO = new UltrasoundRecordDAO(factory);

    //@Override
    public void setUp() throws Exception {
        TestDataGenerator gen = new TestDataGenerator();
        gen.clearAllTables();

        bean = new UltrasoundRecordBean();
        bean.setMid(1111111);
        bean.setFetusid(2);
        bean.setVisitDate("11/15/2018");
        bean.setAbdominalCircumference(2.3);
        bean.setBiparietalDiameter(3.4);
        bean.setCrownRumpLength(4.5);
        bean.setEstimatedFetalWeight(5.6);
        bean.setFemurLength(6.7);
        bean.setHeadCircumference(7.8);
        bean.setHumerusLength(8.9);
        bean.setOccipitofrontalDiameter(9.9);
    }

    @Test
    public void testUltrasoundRecord() throws DBException {
        // add the bean
        urDAO.addUltrasoundRecord(bean);

        //get the bean by MID
        List<UltrasoundRecordBean> list = urDAO.getUltrasoundRecordsByMID(1111111);
        assertEquals(list.get(0).getMid(), bean.getMid());
        assertEquals(list.get(0).getFetusID(), bean.getFetusID());
        assertEquals(list.get(0).getVisitDate(), bean.getVisitDate());
        assertEquals(list.get(0).getCrownRumpLength(), bean.getCrownRumpLength());
        assertEquals(list.get(0).getBiparietalDiameter(), bean.getBiparietalDiameter());
        assertEquals(list.get(0).getHeadCircumference(), bean.getHeadCircumference());
        assertEquals(list.get(0).getFemurLength(), bean.getFemurLength());
        assertEquals(list.get(0).getOccipitofrontalDiameter(), bean.getOccipitofrontalDiameter());
        assertEquals(list.get(0).getAbdominalCircumference(), bean.getAbdominalCircumference());
        assertEquals(list.get(0).getHumerusLength(), bean.getHumerusLength());
        assertEquals(list.get(0).getEstimatedFetalWeight(), bean.getEstimatedFetalWeight());
    }

    @Test
    public void testErrors() throws DBException {
        DBBuilder builder = new DBBuilder();

        try {
            builder.dropTables(); //drop all tables in the DB
            UltrasoundRecordBean bean = null;
            try {
                bean = urDAO.getUltrasoundRecordsByMID(1111111).get(0);
            } catch (DBException e) {
                assertNull(bean);
            }
            builder.createTables(); //now put them back so future tests aren't broken
        } catch(Exception e) {
            fail();
        }

    }

}
