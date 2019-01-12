package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.model.old.beans.loaders.ChildbirthRecordLoader;
import edu.ncsu.csc.itrust.model.old.beans.ChildbirthRecordBean;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

/**
 * DAO - Database Access Object, one DAO per table
 * ChildbirthRecordDAO corresponds to the childbirth table in iTrust database
 */
public class ChildbirthRecordDAO {
	private DAOFactory factory;
	private ChildbirthRecordLoader childbirthLoader;
	/**
     * The typical constructor.
     *
     * @param factory The {@link DAOFactory} associated with this DAO, which is used
     *                for obtaining SQL connections, etc.
     */
	public ChildbirthRecordDAO(DAOFactory factory) {
        this.factory = factory;
        this.childbirthLoader = new ChildbirthRecordLoader();
    }
	/**
     * Adds an childbirth record to the table
     *
     * @throws DBException
     */
	public void addChildbirthRecord(ChildbirthRecordBean p) throws DBException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = factory.getConnection();
            ps = conn.prepareStatement("INSERT INTO childbirth(MID, visitDate, visitType, deliveryType, pitocin, pitocinDosage,"
                    + " nitrousOxide, nitrousOxideDosage, pethidine, pethidineDosage, epiduralAnaesthesia, epiduralAnaesthesiaDosage, magnesiumSulfate, magnesiumSulfateDosage,"
                    + " rhImmuneGlobulin, rhImmuneGlobulinDosage, babyID, deliveryTime, babySex) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps = childbirthLoader.loadParameters(ps, p);
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
     * Update an childbirth record in the table
     * @param p
     * @throws DBException
     */
	public void editChildbirthRecordOV(ChildbirthRecordBean p) throws DBException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = factory.getConnection();
            ps = conn.prepareStatement("UPDATE childbirth SET MID=?, visitDate=?, visitType=?, deliveryType=?, pitocin=?, pitocinDosage=?,"
                    + " nitrousOxide=?, nitrousOxideDosage=?, pethidine=?, pethidineDosage=?, epiduralAnaesthesia=?, epiduralAnaesthesiaDosage=?, magnesiumSulfate=?, magnesiumSulfateDosage=?,"
                    + " rhImmuneGlobulin=?, rhImmuneGlobulinDosage=?, babyID=?, deliveryTime=?, babySex=? WHERE id=?"
            );
            ps = childbirthLoader.loadParameters(ps, p);
            ps.setLong(20, p.getOid());
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
     * Helper function for retrieving childbirth records
     *
     * @param mid  the patient's id
     * @param flag which type of records
     * @return a list of ChildbrithRecordBean's with the information from all the childbirth records
     * correlating to the given MID
     * @throws DBException
     */
	public List<ChildbirthRecordBean> getChildbirthRecordsByMIDCommon(long mid, int flag) throws DBException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = factory.getConnection();
            if (flag == 0) {
                ps = conn.prepareStatement("SELECT * FROM childbirth WHERE MID=? ORDER BY babyID");
            }
            ps.setLong(1, mid);
            ResultSet rs = ps.executeQuery();
            List<ChildbirthRecordBean> list = childbirthLoader.loadList(rs);
            rs.close();
            ps.close();
            return list;
        } catch (SQLException e) {
            throw new DBException(e);
        } finally {
            DBUtil.closeConnection(conn, ps);
        }
    }
	/**
     * Returns a list of childbirth records correlating to the given MID
     *
     * @param mid  the patient's id
     * @return a list of ObstetricsRecordBean's with the information from all the childbirth records
     * correlating to the given MID, all types of records
     * @throws DBException
     */
	public List<ChildbirthRecordBean> getChildbirthRecordsByMID(long mid) throws DBException {
		return getChildbirthRecordsByMIDCommon(mid, 0);
	}

//    public boolean isValidInfant(long mid) throws DBException {
//        Connection conn = null;
//        PreparedStatement ps = null;
//        try {
//            conn = factory.getConnection();
//            ps = conn.prepareStatement("SELECT MID FROM childbirth");
//            ResultSet rs = ps.executeQuery();
//            while(rs.next()) {
//                if (mid == rs.getLong("MID"))
//                    return true;
//            }
//            rs.close();
//            ps.close();
//            return false;
//        } catch (SQLException e) {
//            throw new DBException(e);
//        } finally {
//            DBUtil.closeConnection(conn, ps);
//        }
//    }

}

