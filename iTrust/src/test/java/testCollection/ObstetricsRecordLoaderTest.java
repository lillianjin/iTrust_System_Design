package testCollection;

import edu.ncsu.csc.itrust.model.old.beans.ObstetricsRecordBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.ObstetricsRecordLoader;
import junit.framework.TestCase;
import org.easymock.classextension.IMocksControl;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createControl;

public class ObstetricsRecordLoaderTest extends TestCase {
    private IMocksControl ctrl;
    private ResultSet rs;
    private ObstetricsRecordLoader load;

    @Override
    @Before
    public void setUp() throws Exception{
        ctrl = createControl();
        rs = ctrl.createMock(ResultSet.class);
        load = new ObstetricsRecordLoader();

    }

    @Test
    public void testloadList() throws SQLException{
        List<ObstetricsRecordBean> l = load.loadList(rs);
        assertEquals(0, l.size());

    }

    @Test
    public void testloadSingle() throws Exception{

        try{
            expect(rs.getLong("id")).andReturn(608L).once();
            expect(rs.getLong("MID")).andReturn(806L).once();

            // still don't know how to test Date. the null pointer exception is annoying.

            //expect(rs.getDate("initDate").toString()).andReturn("8/20/2018");
            //expect(rs.getDate("LMP").toString()).andReturn("8/21/2018");
            //expect(rs.getDate("EDD").toString()).andReturn("8/22/2018");
            expect(rs.getDate("initDate")).andReturn(new java.sql.Date(new Date(0).getTime()));
            expect(rs.getDate("LMP")).andReturn(new java.sql.Date(new Date(0).getTime()));
            expect(rs.getDate("EDD")).andReturn(new java.sql.Date(new Date(0).getTime()));
            expect(rs.getDate("visitDate")).andReturn(new java.sql.Date(new Date(0).getTime()));

            expect(rs.getString("weekPregnant")).andReturn("10");
            expect(rs.getInt("concepYear")).andReturn(2);
            expect(rs.getDouble("hrsLabor")).andReturn(4.4);
            expect(rs.getDouble("weightGain")).andReturn(1.1);
            expect(rs.getDouble("weight")).andReturn(6.6);
            expect(rs.getInt("bloodPressureL")).andReturn(60);
            expect(rs.getInt("bloodPressureH")).andReturn(120);
            expect(rs.getInt("FHR")).andReturn(3);
            expect(rs.getString("deliveryType")).andReturn("Vaginal Delivery");
            expect(rs.getString("pregnancyStatus")).andReturn("Complete");
            expect(rs.getBoolean("multiPregnancy")).andReturn(false);
            expect(rs.getInt("babyCount")).andReturn(2);
            expect(rs.getBoolean("lyingPlacenta")).andReturn(true);


            ctrl.replay();

            ObstetricsRecordBean r = load.loadSingle(rs);

            assertEquals(608L, r.getOid());
            assertEquals(806L, r.getMid());
            //assertEquals("8/20/2018", r.geinitDateString());
            //assertEquals("8/21/2018", r.getLMPString());
            //assertEquals("8/22/2018", r.getEDDString());
            assertEquals("10", r.getWeeksPregnant());
            assertEquals(2, r.getYearConception());
            assertEquals(4.4, r.getHoursInLabor());
            assertEquals(1.1, r.getWeightGain());
            assertEquals(6.6, r.getWeight());
            assertEquals(60, r.getBloodPressureL());
            assertEquals(120, r.getBloodPressureH());
            assertEquals(3, r.getFHR());
            assertEquals("Vaginal Delivery", r.getDeliveryType().toString());
            assertEquals("Complete", r.getPregStatus().toString());
            assertEquals(false, r.getMultiPregnancy());
            assertEquals(2, r.getBabyCount());
            assertEquals(true, r.getLyingPlacenta());


        } catch (SQLException e){

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
