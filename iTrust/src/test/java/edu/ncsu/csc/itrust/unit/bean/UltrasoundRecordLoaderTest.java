package edu.ncsu.csc.itrust.unit.bean;


import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.UltrasoundRecordLoader;
import junit.framework.TestCase;
import org.easymock.classextension.IMocksControl;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createControl;
import static org.junit.Assert.fail;

public class UltrasoundRecordLoaderTest extends TestCase {
    private IMocksControl ctrl;
    private ResultSet rs;
    private UltrasoundRecordLoader load;


    @Override
    @Before
    public void setUp() throws Exception{
        ctrl = createControl();
        rs = ctrl.createMock(ResultSet.class);
        load = new UltrasoundRecordLoader();
    }

    @Test
    public void testLoadList() throws SQLException {
        List<UltrasoundRecordBean> l = load.loadList(rs);
        assertEquals(0, l.size());
    }

    @Test
    public void testLoadSingle(){

        try {
            expect(rs.getLong("MID")).andReturn(806L).once();
            expect(rs.getLong("fetusID")).andReturn(608608L).once();
            expect(rs.getDouble("crownRumpLength")).andReturn(2.3);
            expect(rs.getDouble("biparietalDiameter")).andReturn(3.4);
            expect(rs.getDouble("headCircumference")).andReturn(4.5).once();
            expect(rs.getDouble("femurLength")).andReturn(5.6).once();
            expect(rs.getDouble("occipitofrontalDiameter")).andReturn(6.7).once();
            expect(rs.getDouble("abdominalCircumference")).andReturn(7.8).once();
            expect(rs.getDouble("humerusLength")).andReturn(8.9).once();
            expect(rs.getDouble("estimatedFetalWeight")).andReturn(9.1).once();
            expect(rs.getDate("visitDate")).andReturn(new java.sql.Date(new Date(0).getTime()));

            ctrl.replay();

            UltrasoundRecordBean r = load.loadSingle(rs);

            assertEquals(806L, r.getMid());
            assertEquals(608608L, r.getFetusID());
            // dont know how to write Date test...
            //assertEquals(new java.sql.Date(0).getTime(), r.getVisitDate().getTime());
            //assertEquals(new SimpleDateFormat("11/17/2018"), r.getVisitDate().getTime());
            assertEquals(2.3, r.getCrownRumpLength());
            assertEquals(3.4, r.getBiparietalDiameter());
            assertEquals(4.5, r.getHeadCircumference());
            assertEquals(5.6, r.getFemurLength());
            assertEquals(6.7, r.getOccipitofrontalDiameter());
            assertEquals(7.8, r.getAbdominalCircumference());
            assertEquals(8.9, r.getHumerusLength());
            assertEquals(9.1, r.getEstimatedFetalWeight());


        }catch (SQLException e){
            //TODO
        }
    }

    @Test
    public void testLoadParameters() {
        try {
            load.loadParameters(null, null);
            fail();
        } catch (Exception e) {

        }
    }
}
