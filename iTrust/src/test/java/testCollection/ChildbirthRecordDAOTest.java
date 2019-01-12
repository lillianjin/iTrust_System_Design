package testCollection;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.ChildbirthRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ChildbirthRecordDAO;
import edu.ncsu.csc.itrust.model.old.enums.ChildbirthVisitType;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;
import org.junit.Test;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ChildbirthRecordDAOTest extends TestCase{
    ChildbirthRecordBean bean = new ChildbirthRecordBean();
        DAOFactory factory = TestDAOFactory.getTestInstance();
    ChildbirthRecordDAO cbDAO = factory.getChildbirthRecordDAO();

        //@Override
        public void setUp() throws Exception {
            //protected void setUp() throws Exception {
            //cbDAO = new UltrasoundRecordDAO(factory);
        	TestDataGenerator gen = new TestDataGenerator();
            gen.clearAllTables();
            bean.setOid(1);
            bean.setMid(201);
            bean.setVisitDate("10/09/18");
            bean.setChildbirthVisitType(ChildbirthVisitType.fromString("Pre-scheduled"));
            bean.setDeliveryType(DeliveryType.fromString("Vaginal Delivery"));
            bean.setPitocin(true);
            bean.setNitrousOxide(true);
            bean.setPethidine(false);
            bean.setEpiduralAnaesthesia(false);
            bean.setMagnesiumSulfate(false);
            bean.setRhImmuneGlobulin(false);
            bean.setPitocinDosage(1.0);
            bean.setNitrousOxideDosage(1.0);
            bean.setPethidineDosage(1.0);
            bean.setEpiduralAnaesthesiaDosage(1.0);
            bean.setMagnesiumSulfateDosage(1.0);
            bean.setRhImmuneGlobulinDosage(1.0);
            bean.setBabyID(100);
            bean.setDeliveryTime("10/09/18");
            bean.setBabySex("Male");


        }

        @Test
        public void testaddChildbirthRecord() throws DBException {
            List<ChildbirthRecordBean> list = new ArrayList<ChildbirthRecordBean>();
            //assertEquals(list.size(), 0);
            cbDAO.addChildbirthRecord(bean);
            //assertEquals(list.size(), 0);
//    } catch (SQLException e) {
//        e.printStackTrace();
//        throw new DBException(e);
//    } finally {
//        DBUtil.closeConnection(conn, ps);
        }

    @Test
    public void testeditChildbirthRecord() throws DBException {
        List<ChildbirthRecordBean> list = new ArrayList<ChildbirthRecordBean>();
        //assertEquals(list.size(), 0);
        cbDAO.editChildbirthRecordOV(bean);
        //assertEquals(list.size(), 0);
//    } catch (SQLException e) {
//        e.printStackTrace();
//        throw new DBException(e);
//    } finally {
//        DBUtil.closeConnection(conn, ps);
    }

        @Test
        public void testgetChildbirthRecordsByMID() throws ParseException{

            try {
                List<ChildbirthRecordBean> list = new ArrayList<>();
                assertEquals(list.size(), 0);
                cbDAO.addChildbirthRecord(bean);
                list = cbDAO.getChildbirthRecordsByMID(201);

                //assertEquals(list.size(), 1);
                ChildbirthRecordBean bean = list.get(0);

                assertEquals(bean.getOid(), 1);
                assertEquals(bean.getMid(), 201);
                //assertEquals(bean.getVisitDateString(), "10/09/18");
                //assertEquals(bean.getVisitDate(), new SimpleDateFormat("MM/dd/yy").parse("10/09/18"));
                assertEquals(bean.getChildbirthVisitType().toString(), "Pre-scheduled");
                assertEquals(bean.getDeliveryType().toString(), "Vaginal Delivery");
                assertEquals(bean.getPitocin(), true);
                assertEquals(bean.getNitrousOxide(), true);
                assertEquals(bean.getPethidine(), false);
                assertEquals(bean.getEpiduralAnaesthesia(), false);
                assertEquals(bean.getMagnesiumSulfate(), false);
                assertEquals(bean.getRhImmuneGlobulin(), false);
                assertEquals(bean.getPitocinDosage(), 1.0);
                assertEquals(bean.getNitrousOxideDosage(), 1.0);
                assertEquals(bean.getPethidineDosage(), 1.0);
                assertEquals(bean.getEpiduralAnaesthesiaDosage(), 1.0);
                assertEquals(bean.getMagnesiumSulfateDosage(), 1.0);
                assertEquals(bean.getRhImmuneGlobulinDosage(), 1.0);
                assertEquals(bean.getBabyID(), 100);
                //assertEquals(bean.getDeliverTime(), new SimpleDateFormat("MM/dd/yy").parse("10/09/18"));
                //assertEquals(bean.getDeliveryTimeString(), "10/09/18");
                assertEquals(bean.getBabySex(), "Male");

                //assertNotNull(list);
                System.out.println("get successfully");

            } catch (DBException e) {
                System.out.println(e.getMessage());
            }
        }
    @Test
    public void testErrors() throws SQLException {
        DBBuilder builder = new DBBuilder();

        try {
            builder.dropTables(); //drop all tables in the DB
            ChildbirthRecordBean bean = null;
            try {
                bean = cbDAO.getChildbirthRecordsByMID(201).get(0);
            } catch (DBException e) {
                assertNull(bean);
            }
            builder.createTables(); //now put them back so future tests aren't broken
        } catch(Exception e) {
            fail();
        }

    }


    }
