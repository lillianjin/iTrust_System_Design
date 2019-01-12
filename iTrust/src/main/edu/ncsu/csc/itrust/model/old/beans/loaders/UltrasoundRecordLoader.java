package edu.ncsu.csc.itrust.model.old.beans.loaders;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.model.old.enums.PregnancyStatus;

/**
 * Loader for UltrasoundRecordBeans
 *
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency.
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class UltrasoundRecordLoader implements BeanLoader<UltrasoundRecordBean> {
    /**
     * loadList
     * @param rs rs
     * @throws SQLException
     */
    public List<UltrasoundRecordBean> loadList(ResultSet rs) throws SQLException {
        List<UltrasoundRecordBean> list = new ArrayList<UltrasoundRecordBean>();
        while (rs.next()) {
            list.add(loadSingle(rs));
        }
        return list;
    }

    public UltrasoundRecordBean loadSingle(ResultSet rs) throws SQLException {
        UltrasoundRecordBean p = new UltrasoundRecordBean();
        p.setMid(rs.getLong("MID"));
        p.setFetusid(rs.getLong("fetusID"));
        p.setVisitDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date(rs.getDate("visitDate").getTime())));
        p.setCrownRumpLength(rs.getDouble("crownRumpLength"));
        p.setBiparietalDiameter(rs.getDouble("biparietalDiameter"));
        p.setHeadCircumference(rs.getDouble("headCircumference"));
        p.setFemurLength(rs.getDouble("femurLength"));
        p.setOccipitofrontalDiameter(rs.getDouble("occipitofrontalDiameter"));
        p.setAbdominalCircumference(rs.getDouble("abdominalCircumference"));
        p.setHumerusLength(rs.getDouble("humerusLength"));
        p.setEstimatedFetalWeight(rs.getDouble("estimatedFetalWeight"));

        return p;
    }


    /**
     *
     * @param ps The prepared statement to be loaded.
     * @param p UltrasoundRecordBean that stores data to be inserted
     * @return PreparedStatement
     * @throws SQLException
     */
    public PreparedStatement loadParameters(PreparedStatement ps, UltrasoundRecordBean p) throws SQLException {
        int i = 1;

        ps.setLong(i++, p.getMid());
        ps.setLong(i++, p.getFetusID());
        ps.setDate(i++, new java.sql.Date(p.getVisitDate().getTime()));
        ps.setDouble(i++, p.getCrownRumpLength());
        ps.setDouble(i++, p.getBiparietalDiameter());
        ps.setDouble(i++, p.getHeadCircumference());
        ps.setDouble(i++, p.getFemurLength());
        ps.setDouble(i++, p.getOccipitofrontalDiameter());
        ps.setDouble(i++, p.getAbdominalCircumference());
        ps.setDouble(i++, p.getHumerusLength());
        ps.setDouble(i++, p.getEstimatedFetalWeight());

        return ps;
    }
}
