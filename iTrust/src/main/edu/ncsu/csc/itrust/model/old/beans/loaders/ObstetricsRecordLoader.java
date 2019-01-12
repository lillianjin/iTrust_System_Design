package edu.ncsu.csc.itrust.model.old.beans.loaders;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.old.beans.ObstetricsRecordBean;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.model.old.enums.PregnancyStatus;

/**
 * Loader for ObstetricsRecordBeans
 *
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency.
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class ObstetricsRecordLoader implements BeanLoader<ObstetricsRecordBean> {
    /**
     * loadList
     * @param rs rs
     * @throws SQLException
     */
    public List<ObstetricsRecordBean> loadList(ResultSet rs) throws SQLException {
        List<ObstetricsRecordBean> list = new ArrayList<ObstetricsRecordBean>();
        while (rs.next()) {
            list.add(loadSingle(rs));
        }
        return list;
    }

    public ObstetricsRecordBean loadSingle(ResultSet rs) throws SQLException {
        ObstetricsRecordBean p = new ObstetricsRecordBean();

        p.setOid(rs.getLong("id"));
        p.setMid(rs.getLong("MID"));

        // prior pregnancy records do not have initDate, LMP and EDD
        try {
            p.setinitDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date(rs.getDate("initDate").getTime())));
        } catch (NullPointerException e) {}
        try {
            p.setLMP(new SimpleDateFormat("MM/dd/yyyy").format(new Date(rs.getDate("LMP").getTime())));
        } catch (NullPointerException e) {}
        try {
            p.setEDD(new SimpleDateFormat("MM/dd/yyyy").format(new Date(rs.getDate("EDD").getTime())));
        } catch (NullPointerException e) {}
        

        p.setWeeksPregnant(rs.getString("weekPregnant"));
        p.setYearConception(rs.getInt("concepYear"));
        p.setHoursInLabor(rs.getDouble("hrsLabor"));
        p.setWeightGain(rs.getDouble("weightGain"));
        p.setWeight(rs.getDouble("weight"));
        p.setBloodPressureL(rs.getInt("bloodPressureL"));
        p.setBloodPressureH(rs.getInt("bloodPressureH"));
        p.setFHR(rs.getInt("FHR"));
        p.setDeliveryType(DeliveryType.fromString(rs.getString("deliveryType")));
        p.setPregStatus(PregnancyStatus.fromString(rs.getString("pregnancyStatus")));
        p.setMultiPregnancy(rs.getBoolean("multiPregnancy"));
        p.setBabyCount(rs.getInt("babyCount"));
        p.setLyingPlacenta(rs.getBoolean("lyingPlacenta"));

        return p;
    }


    /**
     *
     * @param ps The prepared statement to be loaded.
     * @param p ObstetricsRecordBean that stores data to be inserted
     * @return PreparedStatement
     * @throws SQLException
     */
    public PreparedStatement loadParameters(PreparedStatement ps, ObstetricsRecordBean p) throws SQLException {
        int i = 1;
        ps.setLong(i++, p.getMid());

        // set initial date (date visit), may be null for prior pregnancy records
        if (p.getinitDate() != null) {
        	ps.setDate(i, new java.sql.Date(p.getinitDate().getTime()));
        } else {
        	ps.setDate(i, null);
        }
        i++;
        // set last menstrual period, may be null for prior pregnancy records
        if (p.getLMP() != null) {
        	ps.setDate(i, new java.sql.Date(p.getLMP().getTime()));
        } else {
        	ps.setDate(i, null);
        }
        i++;
        // set estimated delivery date, may be null for prior pregnancy records
        if (p.getEDD() != null) {
        	ps.setDate(i, new java.sql.Date(p.getEDD().getTime()));
        } else {
        	ps.setDate(i, null);
        }
        i++;

        ps.setString(i++, p.getWeeksPregnant());
        ps.setInt(i++, p.getYearConception());
        ps.setDouble(i++, p.getHoursInLabor());
        ps.setDouble(i++, p.getWeightGain());
        ps.setDouble(i++, p.getWeight());
        ps.setInt(i++, p.getBloodPressureL());
        ps.setInt(i++, p.getBloodPressureH());
        ps.setInt(i++, p.getFHR());
        ps.setString(i++, p.getDeliveryType().toString());
        ps.setString(i++, p.getPregStatus().toString());
        ps.setBoolean(i++, p.getMultiPregnancy());
        ps.setInt(i++, p.getBabyCount());
        ps.setBoolean(i++, p.getLyingPlacenta());
        return ps;
    }
}
