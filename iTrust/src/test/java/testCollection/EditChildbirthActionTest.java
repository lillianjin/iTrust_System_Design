package testCollection;

import edu.ncsu.csc.itrust.action.EditChildbirthAction;
import edu.ncsu.csc.itrust.model.old.beans.ChildbirthRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.enums.ChildbirthVisitType;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

public class EditChildbirthActionTest extends TestCase {
    public void test() throws Exception {
        DAOFactory prodDAO = TestDAOFactory.getTestInstance();
        EditChildbirthAction ecbAction = new EditChildbirthAction(prodDAO, 9000000012L);
        ChildbirthRecordBean bean = new ChildbirthRecordBean();

        bean.setOid(1);
        bean.setMid(806);
        bean.setVisitDate("10/20/2018");
        bean.setChildbirthVisitType(ChildbirthVisitType.fromString("Pre-scheduled"));
        bean.setDeliveryType(DeliveryType.fromString("Vaginal Delivery"));
        bean.setPitocin(true);
        bean.setNitrousOxide(true);
        bean.setPethidine(false);
        bean.setEpiduralAnaesthesia(false);
        bean.setMagnesiumSulfate(false);
        bean.setRhImmuneGlobulin(false);
        bean.setPitocinDosage(2.0);
        bean.setNitrousOxideDosage(1.0);
        bean.setPethidineDosage(1.0);
        bean.setEpiduralAnaesthesiaDosage(1.0);
        bean.setMagnesiumSulfateDosage(2.0);
        bean.setRhImmuneGlobulinDosage(2.0);
        bean.setBabyID(200);
        bean.setDeliveryTime("1/20/2018");
        bean.setBabySex("Male");
        ecbAction.editChildbirthRecord(bean);
    }
}
