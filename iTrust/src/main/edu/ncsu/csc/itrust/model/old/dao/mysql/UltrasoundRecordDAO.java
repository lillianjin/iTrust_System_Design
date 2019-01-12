package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.model.old.beans.loaders.UltrasoundRecordLoader;
import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

/**
 * DAO - Database Access Object, one DAO per table
 * UltrasoundRecordDAO corresponds to the ultrasound table in iTrust database
 */

public class UltrasoundRecordDAO {
    private DAOFactory factory;
    private UltrasoundRecordLoader ultrasoundLoader;

    /**
     * The typical constructor.
     *
     * @param factory
     *            The {@link DAOFactory} associated with this DAO, which is used
     *            for obtaining SQL connections, etc.
     */
    public UltrasoundRecordDAO(DAOFactory factory) {
        this.factory = factory;
        this.ultrasoundLoader = new UltrasoundRecordLoader();
    }

    /**
     * Adds an ultrasound record to the table
     *
     * @throws DBException
     */
    public void addUltrasoundRecord(UltrasoundRecordBean p) throws DBException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = factory.getConnection();
            ps = conn.prepareStatement("INSERT INTO ultrasound(MID, fetusID, visitDate, crownRumpLength,"
                    +" biparietalDiameter, headCircumference, femurLength, occipitofrontalDiameter,"
                    +" abdominalCircumference, humerusLength, estimatedFetalWeight)"
                    +" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps = ultrasoundLoader.loadParameters(ps, p);
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
     * Returns a list of ultrasound records correlating to the given MID
     *
     * @param mid the patient's id
     * @return a list of UltrasoundRecordBean's with the information from all the ultrasound records
     * correlating to the given MID
     * @throws DBException
     */
    public List<UltrasoundRecordBean> getUltrasoundRecordsByMID(long mid) throws DBException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = factory.getConnection();
            ps = conn.prepareStatement("SELECT * FROM  ultrasound WHERE MID=? ORDER BY visitDate DESC");
            ps.setLong(1, mid);
            ResultSet rs = ps.executeQuery();
            List<UltrasoundRecordBean> list = ultrasoundLoader.loadList(rs);
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