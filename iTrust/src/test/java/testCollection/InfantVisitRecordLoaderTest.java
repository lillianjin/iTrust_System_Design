package testCollection;

import edu.ncsu.csc.itrust.model.old.beans.InfantVisitRecordBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.InfantVisitRecordLoader;
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

public class InfantVisitRecordLoaderTest extends TestCase {
    private IMocksControl ctrl;
    private ResultSet rs;
    private InfantVisitRecordLoader load;


    @Override
    @Before
    public void setUp() throws Exception {
        ctrl = createControl();
        rs = ctrl.createMock(ResultSet.class);
        load = new InfantVisitRecordLoader();
    }

    @Test
    public void testLoadList() throws SQLException {
        List<InfantVisitRecordBean> l = load.loadList(rs);
        assertEquals(0, l.size());
    }

    @Test
    public void testLoadSingle() {
        try {
            expect(rs.getLong("id")).andReturn(608L).once();
            expect(rs.getLong("MID")).andReturn(806L).once();
            expect(rs.getDate("visitDate")).andReturn(new java.sql.Date(new Date(0).getTime())).once();
            //expect(rs.getDate("childBirthDate")).andReturn(new java.sql.Date(new Date(0).getTime())).once();
            //expect(rs.getString("gender")).andReturn("Male").once();
            expect(rs.getDouble("weight")).andReturn(4.5).once();
            expect(rs.getDouble("height")).andReturn(50.2).once();
            expect(rs.getInt("heartbeatRate")).andReturn(70).once();
            //expect(rs.getString("bloodType")).andReturn("A+").once();
            expect(rs.getInt("BloodPressureL")).andReturn(80).once();
            expect(rs.getInt("BloodPressureH")).andReturn(120).once();

            ctrl.replay();

            InfantVisitRecordBean r = load.loadSingle(rs);

            assertEquals(608L, r.getIid());
            assertEquals(806L, r.getMid());


            //assertEquals("Male", r.getGender());
            assertEquals(4.5, r.getWeight());
            assertEquals(50.2, r.getHeight());
            assertEquals(70, r.getHeartbeatRate());
            //assertEquals("A+", r.getBloodType());
            //assertEquals(80, r.getBloodPressure());




            assertEquals(4.5, r.getWeight());
            assertEquals(50.2, r.getHeight());
            assertEquals(70, r.getHeartbeatRate());
            //assertEquals("A+", r.get);
            assertEquals(80, r.getBloodPressureL());
            assertEquals(120, r.getBloodPressureH());




        } catch (SQLException e) {
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
