package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.model.old.beans.loaders.ObstetricsRecordLoader;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricsRecordBean;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

/**
 * DAO - Database Access Object, one DAO per table
 * ObstetricsRecordDAO corresponds to the obstetrics table in iTrust database
 */

public class ObstetricsRecordDAO {
    private DAOFactory factory;
    private ObstetricsRecordLoader obstetricsLoader;

    /**
     * The typical constructor.
     *
     * @param factory The {@link DAOFactory} associated with this DAO, which is used
     *                for obtaining SQL connections, etc.
     */
    public ObstetricsRecordDAO(DAOFactory factory) {
        this.factory = factory;
        this.obstetricsLoader = new ObstetricsRecordLoader();
    }

    /**
     * Adds an obstetrics record to the table
     *
     * @throws DBException
     */
    public void addObstetricsRecord(ObstetricsRecordBean p) throws DBException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = factory.getConnection();
            ps = conn.prepareStatement("INSERT INTO obstetrics(MID, initDate, LMP, EDD, weekPregnant, concepYear,"
                    + " hrsLabor, weightGain, weight, bloodPressureL, bloodPressureH, FHR, deliveryType, pregnancyStatus,"
                    + " multiPregnancy, babyCount, lyingPlacenta) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps = obstetricsLoader.loadParameters(ps, p);
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
     * Update an obstetrics record in the table
     * @param p
     * @throws DBException
     */
    public void editObstetricsRecordOV(ObstetricsRecordBean p) throws DBException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = factory.getConnection();
            ps = conn.prepareStatement("UPDATE obstetrics SET MID=?, initDate=?, LMP=?, EDD=?, weekPregnant=?, concepYear=?,"
                    + " hrsLabor=?, weightGain=?, weight=?, bloodPressureL=?, bloodPressureH=?, FHR=?, deliveryType=?, pregnancyStatus=?,"
                    + " multiPregnancy=?, babyCount=?, lyingPlacenta=? WHERE id=?"
            );
            ps = obstetricsLoader.loadParameters(ps, p);
            ps.setLong(18, p.getOid());
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
     * Helper function for retrieving obstetrics records
     *
     * @param mid  the patient's id
     * @param flag which type of records
     * @return a list of ObstetricsRecordBean's with the information from all the obstetrics records
     * correlating to the given MID
     * @throws DBException
     */
    public List<ObstetricsRecordBean> getObstetricsRecordsByMIDCommon(long mid, int flag) throws DBException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = factory.getConnection();
            if (flag == 0) {
                ps = conn.prepareStatement("SELECT * FROM  obstetrics WHERE MID=? ORDER BY initDate DESC");
            }
            else if (flag == 1) {
                ps = conn.prepareStatement("SELECT * FROM  obstetrics WHERE MID=? AND pregnancyStatus='Initial' ORDER BY initDate DESC");
            }
            else if (flag == 2) {
                ps = conn.prepareStatement("SELECT * FROM  obstetrics WHERE MID=? AND pregnancyStatus='Complete' ORDER BY concepYear DESC");
            }
            else {
                ps = conn.prepareStatement("SELECT * FROM  obstetrics WHERE MID=? AND pregnancyStatus='OfficeVisit' ORDER BY initDate DESC");
            }
            ps.setLong(1, mid);
            ResultSet rs = ps.executeQuery();
            List<ObstetricsRecordBean> list = obstetricsLoader.loadList(rs);
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
     * Returns a list of obstetrics records correlating to the given MID
     *
     * @param mid  the patient's id
     * @return a list of ObstetricsRecordBean's with the information from all the obstetrics records
     * correlating to the given MID, all types of records
     * @throws DBException
     */
    public List<ObstetricsRecordBean> getObstetricsRecordsByMID(long mid) throws DBException {
       return getObstetricsRecordsByMIDCommon(mid, 0);
    }

    /**
     * Returns a list of obstetrics records correlating to the given MID
     *
     * @param mid  the patient's id
     * @return a list of ObstetricsRecordBean's with the information from all the obstetrics records
     * correlating to the given MID, only initial records
     * @throws DBException
     */
    public List<ObstetricsRecordBean> getObstetricsRecordsByMIDInitial(long mid) throws DBException {
        return getObstetricsRecordsByMIDCommon(mid, 1);
    }

    /**
     * Returns a list of obstetrics records correlating to the given MID
     *
     * @param mid  the patient's id
     * @return a list of ObstetricsRecordBean's with the information from all the obstetrics records
     * correlating to the given MID, only prior pregnancy records
     * @throws DBException
     */
    public List<ObstetricsRecordBean> getObstetricsRecordsByMIDPrior(long mid) throws DBException {
        return getObstetricsRecordsByMIDCommon(mid, 2);
    }

    /**
     * Returns a list of obstetrics records correlating to the given MID
     *
     * @param mid  the patient's id
     * @return a list of ObstetricsRecordBean's with the information from all the obstetrics records
     * correlating to the given MID, only office visit records
     * @throws DBException
     */
    public List<ObstetricsRecordBean> getObstetricsRecordsByMIDOV(long mid) throws DBException {
        return getObstetricsRecordsByMIDCommon(mid, 3);
    }

}
