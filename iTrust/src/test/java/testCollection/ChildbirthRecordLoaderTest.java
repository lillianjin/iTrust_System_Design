package testCollection;

import edu.ncsu.csc.itrust.model.old.beans.ChildbirthRecordBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.ChildbirthRecordLoader;
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

public class ChildbirthRecordLoaderTest extends TestCase {
    private IMocksControl ctrl;
    private ResultSet rs;
    private ChildbirthRecordLoader load;

    @Override
    @Before
    public void setUp() throws Exception{
        ctrl = createControl();
        rs = ctrl.createMock(ResultSet.class);
        load = new ChildbirthRecordLoader();
    }

    @Test
    public void testLoadList() throws SQLException {
        List<ChildbirthRecordBean> l = load.loadList(rs);
        assertEquals(0, l.size());
    }

    @Test
    public void testLoadSingle() throws Exception{
        try {
            expect(rs.getLong("MID")).andReturn(806L).once();
            expect(rs.getLong("id")).andReturn(608L).once();
            expect(rs.getDate("visitDate")).andReturn(new java.sql.Date(new Date(0).getTime()));
            expect(rs.getString("visitType")).andReturn("ER").once();
            expect(rs.getString("deliveryType")).andReturn("Vaginal Delivery").once();
            expect(rs.getBoolean("pitocin")).andReturn(true).once();
            expect(rs.getDouble("pitocinDosage")).andReturn(4.5).once();
            expect(rs.getBoolean("nitrousOxide")).andReturn(true).once();
            expect(rs.getDouble("nitrousOxideDosage")).andReturn(5.6).once();
            expect(rs.getBoolean("pethidine")).andReturn(false).once();
            expect(rs.getDouble("pethidineDosage")).andReturn(4.5).once();
            expect(rs.getBoolean("epiduralAnaesthesia")).andReturn(false).once();
            expect(rs.getDouble("epiduralAnaesthesiaDosage")).andReturn(3.4).once();
            expect(rs.getBoolean("magnesiumSulfate")).andReturn(true).once();
            expect(rs.getDouble("magnesiumSulfateDosage")).andReturn(2.3).once();
            expect(rs.getBoolean("rhImmuneGlobulin")).andReturn(true).once();
            expect(rs.getDouble("rhImmuneGlobulinDosage")).andReturn(1.2).once();
            expect(rs.getInt("babyID")).andReturn(806).once();
            expect(rs.getString("babySex")).andReturn("Male").once();
            expect(rs.getDate("deliveryTime")).andReturn(new java.sql.Date(new Date(0).getTime())).once();
			
            /*
            expect().andReturn().once();
            expect().andReturn().once();
            expect().andReturn().once();
            expect().andReturn().once();
             */

            ctrl.replay();

            ChildbirthRecordBean r = load.loadSingle(rs);
            
            assertEquals(806L, r.getMid());
            assertEquals(608L, r.getOid());

            //assertEquals("ER", r.getChildbirthVisitType());
            //assertEquals("Vaginal Delivery", r.getDeliveryType());

            assertEquals(true, r.getPitocin());
            assertEquals(4.5, r.getPitocinDosage());
            assertEquals(true, r.getNitrousOxide());
            assertEquals(5.6, r.getNitrousOxideDosage());
            assertEquals(false, r.getPethidine());
            assertEquals(4.5, r.getPethidineDosage());
            assertEquals(false, r.getEpiduralAnaesthesia());
            assertEquals(3.4, r.getEpiduralAnaesthesiaDosage());
            assertEquals(true, r.getMagnesiumSulfate());
            assertEquals(2.3, r.getMagnesiumSulfateDosage());
            assertEquals(true, r.getRhImmuneGlobulin());
            assertEquals(1.2, r.getRhImmuneGlobulinDosage());
            assertEquals(806, r.getBabyID());
            //assertEquals("Male", r.getBabySex());


            /*
            assertEquals();
            assertEquals();
            assertEquals();
            assertEquals();
             */

        }
        catch (SQLException e) {
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
