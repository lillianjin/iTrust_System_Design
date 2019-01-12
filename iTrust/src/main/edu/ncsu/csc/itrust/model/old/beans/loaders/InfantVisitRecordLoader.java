package edu.ncsu.csc.itrust.model.old.beans.loaders;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.model.old.enums.BloodType;
import edu.ncsu.csc.itrust.model.old.enums.Gender;

import edu.ncsu.csc.itrust.model.old.beans.InfantVisitRecordBean;


/**
 * Loader for InfantVisitRecordBeans
 *
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency.
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class InfantVisitRecordLoader implements BeanLoader<InfantVisitRecordBean> {
    /**
     * loadList
     * @param rs rs
     * @throws SQLException
     */
    public List<InfantVisitRecordBean> loadList(ResultSet rs) throws SQLException {
        List<InfantVisitRecordBean> list = new ArrayList<>();
        while (rs.next()) {
            list.add(loadSingle(rs));
        }
        return list;
    }



    public InfantVisitRecordBean loadSingle(ResultSet rs) throws SQLException {
        InfantVisitRecordBean p = new InfantVisitRecordBean();
        p.setIid(rs.getLong("id"));
        p.setMid(rs.getLong("MID"));
        p.setVisitDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date(rs.getDate("visitDate").getTime())));
//        p.setChildBirthDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date(rs.getDate("childBirthDate").getTime())));
//        p.setGender(rs.getString("gender"));
        p.setWeight(rs.getDouble("weight"));
        p.setHeight(rs.getDouble("height"));
        p.setHeartbeatRate(rs.getInt("heartbeatRate"));
//        p.setBloodType(rs.getString("bloodType"));
        p.setBloodPressureL(rs.getInt("BloodPressureL"));
        p.setBloodPressureH(rs.getInt("BloodPressureH"));

        return p;
    }


    /**
     *
     * @param ps The prepared statement to be loaded.
     * @param p InfantVisitRecordBean that stores data to be inserted
     * @return PreparedStatement
     * @throws SQLException
     */
    public PreparedStatement loadParameters(PreparedStatement ps, InfantVisitRecordBean p) throws SQLException {
        int i = 1;

        ps.setLong(i++, p.getMid());
        ps.setDate(i++, new java.sql.Date(p.getVisitDate().getTime()));
//        ps.setDate(i++, new java.sql.Date(p.getChildBirthDate().getTime()));
//        ps.setString(i++, p.getGender());
        ps.setDouble(i++, p.getWeight());
        ps.setDouble(i++, p.getHeight());
        ps.setInt(i++, p.getHeartbeatRate());
//        ps.setString(i++, p.getBloodType());
        ps.setInt(i++, p.getBloodPressureL());
        ps.setInt(i++, p.getBloodPressureH());

        return ps;
    }
}
