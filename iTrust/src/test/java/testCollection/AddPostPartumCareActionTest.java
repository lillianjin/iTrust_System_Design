package testCollection;

import edu.ncsu.csc.itrust.action.AddPostpartumCareAction;
import edu.ncsu.csc.itrust.model.old.beans.InfantVisitRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

public class AddPostPartumCareActionTest extends TestCase {
    public void test() throws Exception {
        DAOFactory prodDAO = TestDAOFactory.getTestInstance();
        AddPostpartumCareAction addAction = new AddPostpartumCareAction(prodDAO, 9000000012L);
        InfantVisitRecordBean bean = new InfantVisitRecordBean();

        bean.setIid(1);
        bean.setMid(806);
        bean.setVisitDate("10/20/2018");
        bean.setWeight(20.0);
        bean.setHeight(55.0);
        bean.setHeartbeatRate(70);
        bean.setBloodPressureL(55);
        bean.setBloodPressureH(75);
        assertEquals(806, addAction.addInfantVisitRecord(bean));
    }
}
