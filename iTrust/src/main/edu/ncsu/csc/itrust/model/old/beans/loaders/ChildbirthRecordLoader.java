package edu.ncsu.csc.itrust.model.old.beans.loaders;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.old.beans.ChildbirthRecordBean;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.model.old.enums.ChildbirthVisitType;
/**
 * Loader for ChildbirthRecordBeans
 *
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency.
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class ChildbirthRecordLoader implements BeanLoader<ChildbirthRecordBean> {
	 /**
     * loadList
     * @param rs rs
     * @throws SQLException
     */
	public List<ChildbirthRecordBean> loadList(ResultSet rs) throws SQLException {
		List<ChildbirthRecordBean> list = new ArrayList<ChildbirthRecordBean>();
		while (rs.next()) {
		    list.add(loadSingle(rs));
		}
		return list;
	}
	
	public ChildbirthRecordBean loadSingle(ResultSet rs) throws SQLException {
		ChildbirthRecordBean p = new ChildbirthRecordBean();
		p.setOid(rs.getLong("id"));
		p.setMid(rs.getLong("MID"));
		try {
			p.setVisitDate(new SimpleDateFormat("MM/dd/yy").format(new Date(rs.getDate("visitDate").getTime())));
		} catch (NullPointerException e) {}
		p.setChildbirthVisitType(ChildbirthVisitType.fromString(rs.getString("visitType")));
		p.setDeliveryType(DeliveryType.fromString(rs.getString("deliveryType")));
		p.setPitocin(rs.getBoolean("pitocin"));
		p.setPitocinDosage(rs.getDouble("pitocinDosage"));
		p.setNitrousOxide(rs.getBoolean("nitrousOxide"));
		p.setNitrousOxideDosage(rs.getDouble("nitrousOxideDosage"));
		p.setPethidine(rs.getBoolean("pethidine"));
		p.setPethidineDosage(rs.getDouble("pethidineDosage"));
		p.setEpiduralAnaesthesia(rs.getBoolean("epiduralAnaesthesia"));
		p.setEpiduralAnaesthesiaDosage(rs.getDouble("epiduralAnaesthesiaDosage"));
		p.setMagnesiumSulfate(rs.getBoolean("magnesiumSulfate"));
		p.setMagnesiumSulfateDosage(rs.getDouble("magnesiumSulfateDosage"));
		p.setRhImmuneGlobulin(rs.getBoolean("rhImmuneGlobulin"));
		p.setRhImmuneGlobulinDosage(rs.getDouble("rhImmuneGlobulinDosage"));
		p.setBabyID(rs.getInt("babyID"));
		Date dt = rs.getDate("deliveryTime");
		p.setDeliveryTime(dt != null? new SimpleDateFormat("MM/dd/yy").format(new Date(dt.getTime())): "");
		// Commented out the following line because input delivery time can be null and the following line of code will cause null pointer throw out
		//p.setDeliveryTime(new SimpleDateFormat("MM/dd/yy").format(new Date(rs.getDate("deliveryTime").getTime())));
		p.setBabySex(rs.getString("babySex"));
		return p;
	}
	/**
    *
    * @param ps The prepared statement to be loaded.
    * @param p ChildbirthRecordBean that stores data to be inserted
    * @return PreparedStatement
    * @throws SQLException
    */
	public PreparedStatement loadParameters(PreparedStatement ps, ChildbirthRecordBean p) throws SQLException {
		int i = 1;
		ps.setLong(i++, p.getMid());
		if (p.getVisitDate() != null) {
			ps.setDate(i, new java.sql.Date(p.getVisitDate().getTime()));
		} else {
			ps.setDate(i, null);
		}
		i++;
		ps.setString(i++, p.getChildbirthVisitType().toString());
		ps.setString(i++, p.getDeliveryType().toString());
		ps.setBoolean(i++, p.getPitocin());
		ps.setDouble(i++, p.getPitocinDosage());
		ps.setBoolean(i++, p.getNitrousOxide());
		ps.setDouble(i++, p.getNitrousOxideDosage());
		ps.setBoolean(i++, p.getPethidine());
		ps.setDouble(i++, p.getPethidineDosage());
		ps.setBoolean(i++, p.getEpiduralAnaesthesia());
		ps.setDouble(i++, p.getEpiduralAnaesthesiaDosage());
		ps.setBoolean(i++, p.getMagnesiumSulfate());
		ps.setDouble(i++, p.getMagnesiumSulfateDosage());
		ps.setBoolean(i++, p.getRhImmuneGlobulin());
		ps.setDouble(i++, p.getRhImmuneGlobulinDosage());
		ps.setInt(i++, p.getBabyID());
		if (p.getDeliverTime() != null) {
			ps.setDate(i, new java.sql.Date(p.getDeliverTime().getTime()));
		} else {
			ps.setDate(i, null);
		}
		i++;
		ps.setString(i++, p.getBabySex());
		return ps;
	}

	   
}
