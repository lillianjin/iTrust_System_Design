package edu.ncsu.csc.itrust.model.old.beans;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.model.old.enums.ChildbirthVisitType;

public class ChildbirthRecordBean {
	private long oid;
	private long mid;
	private String visitDate;
	private ChildbirthVisitType childbirthVisitType;
	private DeliveryType deliveryType;
	private boolean pitocin;
	private double pitocinDosage;
	private boolean nitrousOxide;
	private double nitrousOxideDosage;
	private boolean pethidine;
	private double pethidineDosage;
	private boolean epiduralAnaesthesia;
	private double epiduralAnaesthesiaDosage;
	private boolean magnesiumSulfate;
	private double magnesiumSulfateDosage;
	private boolean rhImmuneGlobulin;
	private double rhImmuneGlobulinDosage;
	private int babyID;
	private String deliveryTime;
	private String babySex;

	public void setOid(long oid) {  this.oid = oid;  }
	public long getOid() {  return oid;  }
	public void setMid(long mid) {  this.mid = mid;  }
	public long getMid() {  return mid;  }
	public void setVisitDate(String visitDate) {  this.visitDate = visitDate;  }
	public String getVisitDateString() {
		return visitDate;
	}
	public Date getVisitDate() {
		if (visitDate == null || visitDate.equals("")) return null;
		Date date;
		try {
			date = new SimpleDateFormat("MM/dd/yyyy").parse(visitDate);
		} catch (ParseException e) {
			return null;
		}
		return date;
	}
	public void setChildbirthVisitType(ChildbirthVisitType childbirthVisitType) {
		this.childbirthVisitType = childbirthVisitType;
	}
	public ChildbirthVisitType getChildbirthVisitType() {
		return childbirthVisitType;
	}
	public void setDeliveryType(DeliveryType deliveryType) {
		this.deliveryType = deliveryType;
	}
	public DeliveryType getDeliveryType() {
		return deliveryType;
	}
	
	public void setPitocin(boolean pitocin) {
		this.pitocin = pitocin;
	}
	public boolean getPitocin() {
		return pitocin;
	}
	
	public void setNitrousOxide(boolean nitrousOxide) {
		this.nitrousOxide = nitrousOxide;
	}
	public boolean getNitrousOxide() {
		return nitrousOxide;
	}
	
	public void setPethidine(boolean pethidine) {
		this.pethidine = pethidine;
	}
	public boolean getPethidine() {
		return pethidine;
	}
	
	public void setEpiduralAnaesthesia(boolean epiduralAnaesthesia) {
		this.epiduralAnaesthesia = epiduralAnaesthesia;
	}
	public boolean getEpiduralAnaesthesia() {
		return epiduralAnaesthesia;
	}
	
	public void setMagnesiumSulfate(boolean magnesiumSulfate) {
		this.magnesiumSulfate = magnesiumSulfate;
	}
	public boolean getMagnesiumSulfate() {
		return magnesiumSulfate;
	}
	
	public void setRhImmuneGlobulin(boolean rhImmuneGlobulin) {
		this.rhImmuneGlobulin = rhImmuneGlobulin;
	}
	public boolean getRhImmuneGlobulin() {
		return rhImmuneGlobulin;
	}
	
	public void setPitocinDosage(double pitocinDosage) {
		this.pitocinDosage = pitocinDosage;
	}
	public double getPitocinDosage() {
		return pitocinDosage;
	}
	
	public void setNitrousOxideDosage(double nitrousOxideDosage) {
		this.nitrousOxideDosage = nitrousOxideDosage;
	}
	public double getNitrousOxideDosage() {
		return nitrousOxideDosage;
	}
	
	public void setPethidineDosage(double pethidineDosage) {
		this.pethidineDosage = pethidineDosage;
	}
	public double getPethidineDosage() {
		return pethidineDosage;
	}
	public void setEpiduralAnaesthesiaDosage(double epiduralAnaesthesiaDosage) {
		this.epiduralAnaesthesiaDosage = epiduralAnaesthesiaDosage;
	}
	public double getEpiduralAnaesthesiaDosage() {
		return epiduralAnaesthesiaDosage;
	}
	public void setMagnesiumSulfateDosage(double magnesiumSulfateDosage) {
		this.magnesiumSulfateDosage = magnesiumSulfateDosage;
	}
	public double getMagnesiumSulfateDosage() {
		return magnesiumSulfateDosage;
	}
	public void setRhImmuneGlobulinDosage(double rhImmuneGlobulinDosage) {
		this.rhImmuneGlobulinDosage = rhImmuneGlobulinDosage;
	}
	public double getRhImmuneGlobulinDosage() {
		return rhImmuneGlobulinDosage;
	}
	public void setBabyID(int babyID) {
		this.babyID = babyID;
	}
	public int getBabyID() {
		return babyID;
	}
	public void setDeliveryTime (String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public String getDeliveryTimeString() {
		return deliveryTime;
	}
	public Date getDeliverTime() {
		if (deliveryTime == null || deliveryTime.equals("")) return null;
		Date date;
		try {
			date = new SimpleDateFormat("MM/dd/yyyy").parse(deliveryTime);
		} catch (ParseException e) {
			return null;
		}
		return date;
	}
	public void setBabySex(String babySex) {
		this.babySex = babySex;
	}
	public String getBabySex() {
		return babySex;
	}
}
