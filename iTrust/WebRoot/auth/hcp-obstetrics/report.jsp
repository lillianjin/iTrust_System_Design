<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Gender"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@ page import="edu.ncsu.csc.itrust.action.*" %>
<%@ page import="edu.ncsu.csc.itrust.model.old.beans.*" %>

<%@include file="/global.jsp"%>

<% pageTitle = "iTrust - Labor and Delivery Report";%>

<%@include file="/header.jsp"%>
<itrust:patientNav thisTitle="Labor and Delivery Report" />

<%
    String pidString = (String) session.getAttribute("pid");
    if (pidString == null || 1 > pidString.length()) {
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp-obstetrics/report.jsp");
        return;
    }
    ViewPatientAction patientAction = new ViewPatientAction(prodDAO, loggedInMID, pidString);
    PatientBean chosenPatient = patientAction.getPatient(pidString);
    if (chosenPatient == null || !chosenPatient.getGender().equals(Gender.Female)) {
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp-obstetrics/report.jsp");
        throw new ITrustException("The patient is not eligible for obstetrics care.");
    }
%>

<div id="mainpage" align="center">
<%
    ViewPregnancyReportAction vrpa = new ViewPregnancyReportAction(prodDAO, loggedInMID, Long.parseLong(pidString));
    List<ObstetricsRecordBean> lorb = vrpa.getObstetricsRecordBeanByMIDPrior();
    String pp = "";
    pp += "<br /><h2 align='center'>Past Pregnancy</h2><table class='fTable' align='center'>"
            + "<tr>"
            + "<th>Pregnancy Term</th>"
            + "<th>Delivery Type</th>"
            + "<th>Conception Year</th>"
            + "</tr>";
    for (ObstetricsRecordBean orb: lorb) {
        if (orb != null) {
            pp += "<tr>"
                    + "<td>" + orb.getWeeksPregnant().toString().split("-")[0] + "</td>"
                    + "<td>" + orb.getDeliveryType().toString() + "</td>"
                    + "<td>" + orb.getYearConception() + "</td>"
                    + "</tr>";
        }
    }
    pp += "</table>";

    PatientBean pb = vrpa.getPatientReocrdBeanByMID();
    String bt = "";
    bt += "<br /><table class='fTable' align='center'>"
            + "<tr>" + "<th>Blood Type</th>" + "</tr>";
    bt += "<tr><td>" + pb.getBloodType().toString() + "</td></tr>";
    bt += "</table>";

    List<ObstetricsRecordBean> lov = vrpa.getObstetricsRecordBeanByMIDOV();
    String ov = "";
    boolean rhflag = false;
    boolean hbpflag = false;
    boolean amaflag = false;
    boolean pecflag = false;
    boolean rmaflag = false;
    boolean llpflag = false;
    boolean gpmflag = false;
    boolean fhrflag = false;
    boolean mflag = false;
    boolean awcflag = false;
    ov += "<br /><h2 align='center'>Obstetrics Office Visit Information</h2><table class='fTable' align='center'>"
            + "<tr>"
            + "<th>Weeks Pregnant</th>"
            + "<th>Weight</th>"
            + "<th>Blood Pressure</th>"
            + "<th>Fetal Heart Rate</th>"
            + "<th>Multipregnancy</th>"
            + "<th>Low Lying Placenta</th>"
            + "</tr>";
    for (ObstetricsRecordBean orb: lov) {
        if (orb != null) {
            ov += "<tr>"
                    + "<td>" + orb.getWeeksPregnant().split("-")[0] + "</td>"
                    + "<td>" + orb.getWeight() + "</td>"
                    + "<td>" + orb.getBloodPressureH() + "/" + orb.getBloodPressureL() + "</td>"
                    + "<td>" + orb.getFHR() + "</td>"
                    + "<td>" + (orb.getBabyCount() > 1? orb.getBabyCount(): "No") + "</td>"
                    + "<td>" + (orb.getLyingPlacenta()? "Yes": "No") + "</td>"
                    + "</tr>";

            hbpflag |= (orb.getBloodPressureH() > 140 || orb.getBloodPressureL() > 90);
            amaflag |= (pb.getAge() >= 35);
            // TODO: pecflag, rmaflag
            llpflag |= orb.getLyingPlacenta();
            fhrflag |= (orb.getFHR() >= 160 || orb.getFHR() <= 120);
            mflag |= (orb.getBabyCount() > 1);
            awcflag = (orb.getWeightGain() <= 15 || orb.getWeightGain() >= 35);
        }
    }
    ov += "</table>";

    String pec = "";
    pec += "<br /><h2 align='center'>Relevant Pre-existing Conditions</h2><table class='fTable' align='center'>";
    // TODO
    pec += "</table>";

    String da = "";
    da += "<br /><h2 align='center'>Drug Allergies</h2><table class='fTable' align='center'>";
    for (AllergyBean ab: vrpa.getAllergies()) {
        da += "<tr><td>" + ab.toString() + "</td></tr>";
        rmaflag = true;
    }
    da += "</table>";

    String cwf = "";
    cwf += "<br /><h2 align='center'>Pregnancy Complication Warning Flags</h2><table class='fTable' align='center'>";
    // TODO
    cwf += "<tr><td>RH</td><td>" + (rhflag? "Yes": "No") + "</td></tr>"
            + "<tr><td>High Blood Pressure</td><td>" + (hbpflag? "Yes": "No") + "</td></tr>"
            + "<tr><td>Advanced Maternal Age</td><td>" + (amaflag? "Yes": "No") + "</td></tr>"
            + "<tr><td>Pre-existing Condition</td><td>" + (pecflag? "Yes": "No") + "</td></tr>"
            + "<tr><td>Relevant Maternal Allergies</td><td>" + (rmaflag? "Yes": "No") + "</td></tr>"
            + "<tr><td>Low-lying Placenta</td><td>" + (llpflag? "Yes": "No") + "</td></tr>"
            + "<tr><td>Genetic Potential For Miscarriage</td><td>" + (gpmflag? "Yes": "No") + "</td></tr>"
            + "<tr><td>Abnormal Fetal Heart Rate</td><td>" + (fhrflag? "Yes": "No") + "</td></tr>"
            + "<tr><td>Multiples</td><td>" + (mflag? "Yes": "No") + "</td></tr>"
            + "<tr><td>Atypical Weight Change</td><td>" + (awcflag? "Yes": "No") + "</td></tr>";
    cwf += "</table>";

    out.write(pp);
    out.write(bt);
    out.write(ov);
    out.write(cwf);
    out.write(pec);
    out.write(da);
%>
</div>

<%@include file="/footer.jsp" %>