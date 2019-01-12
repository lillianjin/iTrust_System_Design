package testCollection;

import edu.ncsu.csc.itrust.action.ViewChildbirthAction;
import edu.ncsu.csc.itrust.model.old.beans.ChildbirthRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

import java.util.List;

public class ViewChildbirthActionTest extends TestCase {
    public void test() throws Exception {
        DAOFactory prodDAO = TestDAOFactory.getTestInstance();
        ViewChildbirthAction vcAction = new ViewChildbirthAction(prodDAO, 9000000012L);
        List<ChildbirthRecordBean> childbirth = vcAction.getChildbirthRecordsByMID(1L);
        assertEquals(childbirth, childbirth);
    }
}
