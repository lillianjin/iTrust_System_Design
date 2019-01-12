package testCollection;

import edu.ncsu.csc.itrust.action.ViewPostpartumCareAction;
import edu.ncsu.csc.itrust.model.old.beans.InfantVisitRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

import java.util.List;

public class ViewPostpartumCareActionTest extends TestCase {
    public void test() throws Exception {
        DAOFactory prodDAO = TestDAOFactory.getTestInstance();
        ViewPostpartumCareAction viewAction = new ViewPostpartumCareAction(prodDAO, 9000000012L);
        List<InfantVisitRecordBean> office = viewAction.getInfantVisitRecordsByMID(1);
        assertEquals(office, office);
    }
}
