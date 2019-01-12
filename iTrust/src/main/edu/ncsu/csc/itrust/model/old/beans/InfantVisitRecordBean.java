package edu.ncsu.csc.itrust.model.old.beans;


import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import edu.ncsu.csc.itrust.model.old.enums.BloodType;
import edu.ncsu.csc.itrust.model.old.enums.Gender;


public class InfantVisitRecordBean {

    // the iid is namely the auto-increment id
    private long iid;
    private long mid;
    private String visitDate;
//    private String childBirthDate;
//    private String gender;
    private double weight;
    private double height;
    private int heartbeatRate;
//    private String bloodType;
    private int bloodPressureL;
    private int bloodPressureH;

    public void setIid(long iid) { this.iid = iid; }
    public long getIid() { return iid; }
    public void setMid(long mid) {
        this.mid = mid;
    }
    public long getMid() {
        return mid;
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
    public String getVisitDateString() {
        return visitDate;
    }

//    public void setChildBirthDate(String childBirthDate) {
//        this.childBirthDate = childBirthDate;
//    }
//    public Date getChildBirthDate() {
//        if (childBirthDate == null || childBirthDate.equals("")) {
//            return null;
//        }
//        Date date;
//        try {
//            date = new SimpleDateFormat("MM/dd/yyyy").parse(childBirthDate);
//        } catch (ParseException e) {
//            return null;
//        }
//        return date;
//    }
//    public String getCBDString() {
//        return childBirthDate;
//    }


//    public void setGender(String gender) {
//        this.gender = gender;
//    }
//    public String getGender() {
//        return gender;
//    }
//    public String getGenderStr() {
//        return gender.toString();
//    }

    /*
    public void setGenderStr(String gender) {
        this.gender = Gender.parse(gender);
    }
    */


    public void setWeight(double weight) {
        this.weight = weight;
    }
    public double getWeight() {
        return weight;
    }


    public void setHeight(double height) {
        this.height = height;
    }
    public double getHeight() {
        return height;
    }

    public void setHeartbeatRate(int heartbeatRate){
        this.heartbeatRate = heartbeatRate;
    }
    public int getHeartbeatRate(){
        return heartbeatRate;
    }

//    public void setBloodType(String bloodType) {
//        this.bloodType = bloodType;
//    }
//    public String getBloodType() {
//        return bloodType;
//    }

    public void setBloodPressureL(int bloodPressureL) { this.bloodPressureL = bloodPressureL; }
    public int getBloodPressureL() { return bloodPressureL; }

    public void setBloodPressureH (int bloodPressureH) { this.bloodPressureH = bloodPressureH; }
    public int getBloodPressureH() { return bloodPressureH; }
}
