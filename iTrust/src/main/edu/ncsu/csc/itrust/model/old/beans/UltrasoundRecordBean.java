package edu.ncsu.csc.itrust.model.old.beans;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class UltrasoundRecordBean {
    private long mid;
    private long fetusID;
    private String visitDate;
    private double crownRumpLength;
    private double biparietalDiameter;
    private double headCircumference;
    private double femurLength;
    private double occipitofrontalDiameter;
    private double abdominalCircumference;
    private double humerusLength;
    private double estimatedFetalWeight;


    public void setMid(long mid) {
        this.mid = mid;
    }
    public long getMid() {
        return mid;
    }
    public void setFetusid(long fetusID) {
        this.fetusID = fetusID;
    }
    public long getFetusID() {
        return fetusID;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }
    public Date getVisitDate() {
        if (visitDate == null || visitDate.equals("")) {
            return null;
        }
        Date date;
        try {
            date = new SimpleDateFormat("MM/dd/yyyy").parse(visitDate);
        } catch (ParseException e) {
            return null;
        }
        return date;
    }
    public void setCrownRumpLength(double crownRumpLength) {
        this.crownRumpLength = crownRumpLength;
    }
    public double getCrownRumpLength() {
        return crownRumpLength;
    }
    public void setBiparietalDiameter(double biparietalDiameter) {
        this.biparietalDiameter = biparietalDiameter;
    }
    public double getBiparietalDiameter() {
        return biparietalDiameter;
    }
    public void setHeadCircumference(double headCircumference) {
        this.headCircumference = headCircumference;
    }
    public double getHeadCircumference() {
        return headCircumference;
    }
    public void setFemurLength(double femurLength) {
        this.femurLength = femurLength;
    }
    public double getFemurLength() {
        return femurLength;
    }
    public void setOccipitofrontalDiameter(double occipitofrontalDiameter) {
        this.occipitofrontalDiameter = occipitofrontalDiameter;
    }
    public double getOccipitofrontalDiameter() {
        return occipitofrontalDiameter;
    }
    public void setAbdominalCircumference(double abdominalCircumference) {
        this.abdominalCircumference = abdominalCircumference;
    }
    public double getAbdominalCircumference() {
        return abdominalCircumference;
    }
    public void setHumerusLength(double humerusLength) {
        this.humerusLength = humerusLength;
    }
    public double getHumerusLength() {
        return humerusLength;
    }
    public void setEstimatedFetalWeight(double estimatedFetalWeight) {
        this.estimatedFetalWeight = estimatedFetalWeight;
    }
    public double getEstimatedFetalWeight() {
        return estimatedFetalWeight;
    }

}





