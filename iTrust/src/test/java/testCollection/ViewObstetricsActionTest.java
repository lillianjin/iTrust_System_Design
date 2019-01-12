package testCollection;

import edu.ncsu.csc.itrust.action.ViewObstetricsAction;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricsRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

import java.util.List;

public class ViewObstetricsActionTest extends TestCase {
    public void test() throws Exception {
        DAOFactory prodDAO = TestDAOFactory.getTestInstance();
        ViewObstetricsAction viewObstetrics = new ViewObstetricsAction(prodDAO, 9000000012L);
        List<ObstetricsRecordBean> office = viewObstetrics.getOfficeVisitObstetricsRecordByMID(1);
        List<ObstetricsRecordBean> prior = viewObstetrics.getPriorPregnancyObstetricsRecordByMID(1L);
        List<ObstetricsRecordBean> initial = viewObstetrics.getInitialObstetricsRecordByMID(1L);
        List<ObstetricsRecordBean> lorb = viewObstetrics.getObstetricsRecordByMID(1L);
        assertEquals(office, office);
        assertEquals(prior, prior);
        assertEquals(initial, initial);
        assertEquals(lorb, lorb);
    }
}
