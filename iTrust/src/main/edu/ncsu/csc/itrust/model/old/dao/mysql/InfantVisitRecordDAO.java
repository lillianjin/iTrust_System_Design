package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.model.old.beans.loaders.InfantVisitRecordLoader;
import edu.ncsu.csc.itrust.model.old.beans.InfantVisitRecordBean;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

/**
 * DAO - Database Access Object, one DAO per table
 * InfantVisitRecordDAO corresponds to the infantVisit table in iTrust database
 */

public class InfantVisitRecordDAO {
    private DAOFactory factory;
    private InfantVisitRecordLoader infantVisitLoader;

    /**
     * The typical constructor.
     *
     * @param factory
     *            The {@link DAOFactory} associated with this DAO, which is used
     *            for obtaining SQL connections, etc.
     */
    public InfantVisitRecordDAO(DAOFactory factory) {
        this.factory = factory;
        this.infantVisitLoader = new InfantVisitRecordLoader();
    }

    /**
     * Adds an infantVisit record to the table
     *
     * @throws DBException
     */

    /*
    CREATE TABLE infantVisit
(
	id 				      BIGINT(20) 		UNSIGNED AUTO_INCREMENT,
	MID 			      BIGINT		UNSIGNED NOT NULL default '0',
	visitDate       DATE,
	childBirthDate  DATE,
	gender VARCHAR(13) default 'Not Specified',
	weight FLOAT default 0,
	height FLOAT default 0,
	heartbeatRate INT default 0,
	bloodType VARCHAR(3) default '',
	bloodPressure int(10) SIGNED default -1,
	PRIMARY KEY (id)
) ENGINE=MyISAM;
     */
    public void addInfantVisitRecord(InfantVisitRecordBean p) throws DBException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = factory.getConnection();
            ps = conn.prepareStatement("INSERT INTO infantVisit(MID, visitDate,"
                    +" weight, height, heartbeatRate,"
                    +" bloodPressureL, bloodPressureH)"
                    +" VALUES(?, ?, ?, ?, ?, ?, ?)");
            ps = infantVisitLoader.loadParameters(ps, p);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException(e);
        } finally {
            DBUtil.closeConnection(conn, ps);
        }
    }

    public void editInfantVisitRecord(InfantVisitRecordBean p) throws DBException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = factory.getConnection();
            ps = conn.prepareStatement("UPDATE infantVisit SET MID=?, visitDate=?,"
                    +" weight=?, height=?, heartbeatRate=?,"
                    +" bloodPressureL=?, bloodPressureH=?"
                    +" WHERE id=?");
            ps = infantVisitLoader.loadParameters(ps, p);
            ps.setLong(8, p.getIid());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException(e);
        } finally {
            DBUtil.closeConnection(conn, ps);
        }
    }
    /**
     * Returns a list of infantVisit records correlating to the given MID
     *
     * @param mid the patient's id
     * @return a list of InfantVisitRecordBean's with the information from all the infantVisit records
     * correlating to the given MID
     * @throws DBException
     */
    public List<InfantVisitRecordBean> getInfantVisitRecordsByMID(long mid) throws DBException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = factory.getConnection();
            ps = conn.prepareStatement("SELECT * FROM infantVisit WHERE MID=? ORDER BY visitDate DESC");
            ps.setLong(1, mid);
            ResultSet rs = ps.executeQuery();
            List<InfantVisitRecordBean> list = infantVisitLoader.loadList(rs);
            rs.close();
            ps.close();
            return list;
        } catch (SQLException e) {
            throw new DBException(e);
        } finally {
            DBUtil.closeConnection(conn, ps);
        }
    }





}