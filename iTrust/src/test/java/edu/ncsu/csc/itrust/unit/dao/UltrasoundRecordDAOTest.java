package edu.ncsu.csc.itrust.unit.dao;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.UltrasoundRecordDAO;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.*;


public class UltrasoundRecordDAOTest extends TestCase {
    UltrasoundRecordBean p = new UltrasoundRecordBean();
    DAOFactory factory = TestDAOFactory.getTestInstance();
    UltrasoundRecordDAO URDAO = factory.getUltrasoundRecordDAO();

    @Override
    protected void setUp() throws Exception {
        //URDAO = new UltrasoundRecordDAO(factory);

        p.setMid(1111);
        p.setFetusid(2222);
        p.setVisitDate("11/15/2018");
        p.setAbdominalCircumference(2.3);
        p.setBiparietalDiameter(3.4);
        p.setCrownRumpLength(4.5);
        p.setEstimatedFetalWeight(5.6);
        p.setFemurLength(6.7);
        p.setHeadCircumference(7.8);
        p.setHumerusLength(8.9);
        p.setOccipitofrontalDiameter(9.9);


    }

    @Test
    public void testaddUltrasoundRecord() throws DBException{
        List<UltrasoundRecordBean> list = new ArrayList<UltrasoundRecordBean>();
        assertEquals(list.size(), 0);
        URDAO.addUltrasoundRecord(p);
        assertEquals(list.size(), 1);
    }

    @Test
    public void testgetUltrasoundRecordsByMID() throws DBException{

        try {
            List<UltrasoundRecordBean> list = new ArrayList<>();
            assertEquals(list.size(), 0);
            list = URDAO.getUltrasoundRecordsByMID(1111);

            //assertEquals(list.size(), 1);
            UltrasoundRecordBean r = list.get(0);

            assertEquals(r.getMid(), 1111);
            assertEquals(r.getFetusID(), 2222);
            //assertEquals(r.getVisitDate(), );
            assertEquals(r.getAbdominalCircumference(), 2.3);
            assertEquals(r.getBiparietalDiameter(), 3.4);
            assertEquals(r.getCrownRumpLength(), 4.5);
            assertEquals(r.getEstimatedFetalWeight(), 5.6);
            assertEquals(r.getFemurLength(), 6.7);
            assertEquals(r.getHeadCircumference(), 7.8);
            assertEquals(r.getHumerusLength(), 8.9);
            assertEquals(r.getOccipitofrontalDiameter(), 9.9);

            //assertNotNull(list);
            System.out.println("get successfully");

        } catch (DBException e){
            System.out.println(e.getMessage());
        }


    }
}
