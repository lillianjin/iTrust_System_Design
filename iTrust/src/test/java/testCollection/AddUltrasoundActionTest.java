package testCollection;

import edu.ncsu.csc.itrust.action.AddUltrasoundAction;
import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

public class AddUltrasoundActionTest extends TestCase {
    public void test() throws Exception {
        DAOFactory prodDAO = TestDAOFactory.getTestInstance();
        AddUltrasoundAction addAction = new AddUltrasoundAction(prodDAO, 9000000012L);
        UltrasoundRecordBean bean = new UltrasoundRecordBean();
        bean.setMid(113242);
        bean.setFetusid(12123);
        bean.setVisitDate("11/15/2018");
        bean.setAbdominalCircumference(5.3);
        bean.setBiparietalDiameter(43.2);
        bean.setCrownRumpLength(2.4);
        bean.setEstimatedFetalWeight(23.4);
        bean.setFemurLength(4.2);
        bean.setHeadCircumference(5.4);
        bean.setHumerusLength(34.3);
        bean.setOccipitofrontalDiameter(5.3);
        assertEquals(113242, addAction.addUltrasoundRecord(bean));
    }
}
