package edu.ncsu.csc.itrust.unit.action;


import edu.ncsu.csc.itrust.action.AddUltrasoundAction;
import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

public class AddUltrasoundActionTest extends TestCase {
    private DAOFactory factory = TestDAOFactory.getTestInstance();
    private TestDataGenerator gen;
    private UltrasoundRecordBean p;
    private AddUltrasoundAction action;

    /**
     * Sets up defaults
     */
    @Override
    protected void setUp() throws Exception  {
        gen = new TestDataGenerator();
        gen.clearAllTables();

        p = new UltrasoundRecordBean();
        p.setFetusid(12123);
        p.setVisitDate("11/15/2018");
        p.setAbdominalCircumference(5.3);
        p.setBiparietalDiameter(43.2);
        p.setCrownRumpLength(2.4);
        p.setEstimatedFetalWeight(23.4);
        p.setFemurLength(4.2);
        p.setHeadCircumference(5.4);
        p.setHumerusLength(34.3);
        p.setOccipitofrontalDiameter(5.3);

        action = new AddUltrasoundAction(factory, 806);

    }
    /**
     * Tests adding a new Ultrasound
     *
     * @throws Exception
     *
     */
    public void testAddUltrasound() throws Exception{

        long newMID = action.addUltrasoundRecord(p);

        assertEquals(newMID, p.getMid());
    }
}
