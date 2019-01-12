package edu.ncsu.csc.itrust.model.old.beans;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.model.old.enums.PregnancyStatus;

public class ObstetricsRecordBean {
    // the oid is namely the auto-increment id
    private long oid;
    private long mid;
    private String initDate;
    private String lmp;
    private String edd;
    private String weeksPregnant;
    private int yearConception;
    private double hoursInLabor;
    private double weightGain;
    private double weight;
    private int bloodPressureL;
    private int bloodPressureH;
    private int FHR;
    private DeliveryType deliveryType;
    private PregnancyStatus pregStatus;
    private boolean multiPregnancy;
    private int babyCount;
    private boolean lyingPlacenta;

    public void setOid(long oid) { this.oid = oid; }
    public long getOid() { return oid; }

    public void setMid(long mid) {
        this.mid = mid;
    }
    public long getMid() {
        return mid;
    }

    public void setinitDate(String initDate) {
        this.initDate = initDate;
    }
    public Date getinitDate() {
        if (initDate == null || initDate.equals("")) {
            return null;
        }
        Date date;
        try {
            date = new SimpleDateFormat("MM/dd/yyyy").parse(initDate);
        } catch (ParseException e) {
            return null;
        }
        return date;
    }
    public String geinitDateString() {
        return initDate;
    }

    public void setLMP(String lmp) {
        this.lmp = lmp;
    }
    public Date getLMP() {
        if (lmp == null || lmp.equals("")) {
            return null;
        }
        Date date;
        try {
            date = new SimpleDateFormat("MM/dd/yyyy").parse(lmp);
        } catch (ParseException e) {
            return null;
        }
        return date;
    }
    public String getLMPString() {
        return lmp;
    }

    public void setEDD(String edd) {this.edd = edd; }
    public Date getEDD() {
        if (edd == null || edd.equals("")) {
            return null;
        }
        Date date;
        try {
            date = new SimpleDateFormat("MM/dd/yyyy").parse(edd);
        } catch (ParseException e) {
            return null;
        }
        return date;
    }
    public String getEDDString() {
        return edd;
    }

    public void setWeeksPregnant(String weeksPregnant) {
        this.weeksPregnant = weeksPregnant;
    }
    public String getWeeksPregnant() {
        return weeksPregnant;
    }

    public void setYearConception(int yearConception) {
        this.yearConception = yearConception;
    }
    public int getYearConception() {
        return yearConception;
    }

    public void setHoursInLabor(double hoursInLabor) {
        this.hoursInLabor = hoursInLabor;
    }
    public double getHoursInLabor() {
        return hoursInLabor;
    }

    public void setWeightGain(double weightGain) {
        this.weightGain = weightGain;
    }
    public double getWeightGain() { return weightGain; }

    public void setWeight(double weight) { this.weight = weight; }
    public double getWeight() { return weight; }

    public void setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }
    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public void setPregStatus(PregnancyStatus pregStatus) {
        this.pregStatus = pregStatus;
    }
    public PregnancyStatus getPregStatus() { return pregStatus; }

    public void setMultiPregnancy(boolean multiPregnancy) {
        this.multiPregnancy = multiPregnancy;
    }
    public boolean getMultiPregnancy() {
        return multiPregnancy;
    }

    public void setBabyCount(int babyCount) {
        this.babyCount = babyCount;
    }
    public int getBabyCount() {
        return babyCount;
    }

    public void setBloodPressureL(int bloodPressureL) { this.bloodPressureL = bloodPressureL; }
    public int getBloodPressureL() { return bloodPressureL; }

    public void setBloodPressureH (int bloodPressureH) { this.bloodPressureH = bloodPressureH; }
    public int getBloodPressureH() { return bloodPressureH; }

    public void setFHR (int FHR) { this.FHR = FHR; }
    public int getFHR() { return FHR; }

    public void setLyingPlacenta(boolean LyingPlacenta) { this.lyingPlacenta = LyingPlacenta; }
    public boolean getLyingPlacenta() { return lyingPlacenta; }
}