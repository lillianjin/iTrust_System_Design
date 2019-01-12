package testCollection;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.InfantVisitRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.InfantVisitRecordDAO;
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

public class InfantVisitRecordDAOTest extends TestCase {
        InfantVisitRecordBean bean = new InfantVisitRecordBean();
        DAOFactory factory = TestDAOFactory.getTestInstance();
        InfantVisitRecordDAO ivDAO = factory.getInfantVisitRecordDAO();

        //@Override
        public void setUp() throws Exception {
            //protected void setUp() throws Exception {
            //ivDAO = new UltrasoundRecordDAO(factory);
        	TestDataGenerator gen = new TestDataGenerator();
            gen.clearAllTables();
            bean.setIid(1);
            bean.setMid(123);
            bean.setVisitDate("10/09/2018");
            bean.setWeight(10.0);
            bean.setHeight(10.0);
            bean.setHeartbeatRate(80);
            bean.setBloodPressureL(60);
            bean.setBloodPressureH(90);


        }

        @Test
        public void testaddUltrasoundRecord() throws DBException {
        	ivDAO.addInfantVisitRecord(bean);
            List<InfantVisitRecordBean> list = ivDAO.getInfantVisitRecordsByMID(123);
//    } catch (SQLException e) {
//        e.printStackTrace();
//        throw new DBException(e);
//    } finally {
//        DBUtil.closeConnection(conn, ps);
        }
    @Test
    public void testeditUltrasoundRecord() throws DBException {
        ivDAO.editInfantVisitRecord(bean);
        List<InfantVisitRecordBean> list = ivDAO.getInfantVisitRecordsByMID(123);
//    } catch (SQLException e) {
//        e.printStackTrace();
//        throw new DBException(e);
//    } finally {
//        DBUtil.closeConnection(conn, ps);
    }

        @Test
        public void testgetUltrasoundRecordsByMID() throws ParseException {

            try {
                List<InfantVisitRecordBean> list = new ArrayList<>();
                assertEquals(list.size(), 0);
                ivDAO.addInfantVisitRecord(bean);
                list = ivDAO.getInfantVisitRecordsByMID(123);

                //assertEquals(list.size(), 1);
                InfantVisitRecordBean bean = list.get(0);

                assertEquals(bean.getIid(), 1);
                assertEquals(bean.getMid(), 123);
                assertEquals(bean.getVisitDateString(), "10/09/2018");
                assertEquals(bean.getVisitDate(), new SimpleDateFormat("MM/dd/yyyy").parse("10/09/2018"));
                assertEquals(bean.getWeight(), 10.0);
                assertEquals(bean.getHeight(), 10.0);
                assertEquals(bean.getHeartbeatRate(), 80);
                assertEquals(bean.getBloodPressureL(), 60);
                assertEquals(bean.getBloodPressureH(),90);



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
            InfantVisitRecordBean bean = null;
            try {
                bean = ivDAO.getInfantVisitRecordsByMID(1111111).get(0);
            } catch (DBException e) {
                assertNull(bean);
            }
            builder.createTables(); //now put them back so future tests aren't broken
        } catch(Exception e) {
            fail();
        }

    }
}
