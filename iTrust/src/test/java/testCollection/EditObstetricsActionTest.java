package testCollection;

import edu.ncsu.csc.itrust.action.AddObstetricsAction;
import edu.ncsu.csc.itrust.action.EditObstetricsAction;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricsRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.model.old.enums.PregnancyStatus;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

public class EditObstetricsActionTest extends TestCase {
    public void test() throws Exception {
        DAOFactory prodDAO = TestDAOFactory.getTestInstance();
        EditObstetricsAction eoAction = new EditObstetricsAction(prodDAO, 9000000012L);
        ObstetricsRecordBean bean = new ObstetricsRecordBean();

        bean.setOid(1);
        bean.setBabyCount(2);
        bean.setBloodPressureH(120);
        bean.setBloodPressureL(60);
        bean.setDeliveryType(DeliveryType.fromString("Vaginal Delivery"));
        bean.setEDD("10/20/2018");
        bean.setFHR(3);
        bean.setHoursInLabor(4.4);
        bean.setinitDate("8/20/2018");
        bean.setLMP("11/18/2018");
        bean.setLyingPlacenta(true);
        bean.setMid(806);
        bean.setMultiPregnancy(false);
        bean.setPregStatus(PregnancyStatus.fromString("Complete"));
        bean.setWeeksPregnant("1-1");
        bean.setWeight(6.6);
        bean.setWeightGain(1.1);
        bean.setYearConception(2018);
        eoAction.editObstetricsRecord(bean);
    }
}
