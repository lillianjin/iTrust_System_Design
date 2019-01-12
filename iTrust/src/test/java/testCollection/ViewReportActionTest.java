package testCollection;

import edu.ncsu.csc.itrust.action.ViewReportAction;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

import java.util.List;

public class ViewReportActionTest extends TestCase {
    public void test() throws Exception {
        DAOFactory prodDAO = TestDAOFactory.getTestInstance();
        ViewReportAction viewAction = new ViewReportAction(prodDAO, 9000000012L);
        List<PersonnelBean> hcps = viewAction.getDeclaredHCPs(1);
        assertEquals(hcps, hcps);
    }
}
