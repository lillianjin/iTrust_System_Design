<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricsRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Gender"%>
<%@page import="edu.ncsu.csc.itrust.logger.TransactionLogger"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.TransactionType"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@ page import="edu.ncsu.csc.itrust.model.old.beans.ChildbirthRecordBean" %>
<%@ page import="edu.ncsu.csc.itrust.action.*" %>

<%@include file="/global.jsp"%>

<% pageTitle = "iTrust - Obstetrics Home";%>

<%@include file="/header.jsp"%>
<itrust:patientNav thisTitle="Obstetrics Home" />

<%
    /* Require a Patient ID first */
    String pidString = (String) session.getAttribute("pid");
    if (pidString == null || 1 > pidString.length()) {
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp-obstetrics/obstetricsHome.jsp");
        return;
    }

    // check if the selected patient is female, if not, throw an exception
    ViewPatientAction patientAction = new ViewPatientAction(prodDAO, loggedInMID, pidString);
    PatientBean chosenPatient = patientAction.getPatient(pidString);
    if (chosenPatient == null || !chosenPatient.getGender().equals(Gender.Female)) {
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp-obstetrics/obstetricsHome.jsp");
        throw new ITrustException("The patient is not eligible for obstetrics care.");
    }

    //also handle when other JSPs redirect here after successfully doing whatever it is they do
    if (request.getParameter("initial") != null) {
        out.write("<p style=\"border: 1px solid #090; color: #090; font-size: 20px; padding: 2px;\">" +
                "Obstetrics Record successfully added</p>");
    }
    else if (request.getParameter("addOV") != null) {
        out.write("<p style=\"border: 1px solid #090; color: #090; font-size: 20px; padding: 2px;\">" +
                "Obstetrics Office Visit successfully added</p>");
    }
    else if (request.getParameter("editOV") != null) {
        out.write("<p style=\"border: 1px solid #090; color: #090; font-size: 20px; padding: 2px;\">" +
                "Obstetrics Office Visit successfully edited</p>");
    }
    else if (request.getParameter("addCHV") != null) {
        out.write("<p style=\"border: 1px solid #090; color: #090; font-size: 20px; padding: 2px;\">" +
                "Childbirth Hospital Visit successfully added</p>");
    }
    else if (request.getParameter("editCHV") != null) {
        out.write("<p style=\"border: 1px solid #090; color: #090; font-size: 20px; padding: 2px;\">" +
                "Childbirth Hospital Visit successfully edited</p>");
    }

    PatientDAO patients = new PatientDAO(prodDAO);
    PatientBean patient = null;

    try{
        patient = patients.getPatient(Long.parseLong(pidString));
    } catch (NumberFormatException e) {
        throw new ITrustException("Illegal patient id string");
    }
    if (patient == null) {
        throw new ITrustException("Selected patient does not exist");
    }
%>

<div id="mainpage" align=center>

<%
    ViewObstetricsAction viewObstetrics = new ViewObstetricsAction(prodDAO, loggedInMID);
    // get the current viewing personnel
    ViewPersonnelAction personnelAction = new ViewPersonnelAction(prodDAO, loggedInMID);
    PersonnelBean currentPersonnel = personnelAction.getPersonnel("" + loggedInMID);
    // show initial records
    List<ObstetricsRecordBean> initial = viewObstetrics.getInitialObstetricsRecordByMID(Long.parseLong(pidString));
    if (initial != null && !initial.isEmpty()) {
        StringBuilder iniTable = new StringBuilder();
        iniTable.append("<br /><h2 align=\"center\">Initial Obstetrics Records</h2><table class=\"fTable\" align=\"center\">")
                .append("<tr>")
                .append("<th>Initialization Date</th>")
                .append("<th>Last Menstrual Period </th>")
                .append("<th>Weeks-Days Pregnant</th>")
                .append("<th>Estimated Delivery Date</th>")
                .append("</tr>");
        int count = 0;
        for (ObstetricsRecordBean bean : initial) {
            if (bean != null) {
                iniTable.append("<tr>")
                        .append("<td>").append(bean.geinitDateString()).append("</td>")
                        .append("<td>").append(bean.getLMPString()).append("</td>")
                        .append("<td>").append(bean.getWeeksPregnant()).append("</td>")
                        .append("<td>").append(bean.getEDDString()).append("</td>")
                        .append("</tr>");
                TransactionLogger.getInstance().logTransaction(TransactionType.VIEW_INITIAL_OBSTETRICS_RECORD, loggedInMID,
                        Long.parseLong(pidString), "the estimated due date is " + bean.getEDDString());
                count++;
            }
        }
        iniTable.append("</table>");
        if (count != 0) {
            out.write(iniTable.toString());
        }
    }
    // show prior pregnancy obstetrics records
    List<ObstetricsRecordBean> prior = viewObstetrics.getPriorPregnancyObstetricsRecordByMID(Long.parseLong(pidString));
    if (prior != null && !prior.isEmpty()) {
        StringBuilder priorTable = new StringBuilder();
        priorTable.append("<br /><h2 align=\"center\">Prior Pregnancy Records</h2><table class=\"fTable\" align=\"center\">")
                  .append("<tr>")
                  .append("<th>Year of Conception</th>")
                  .append("<th>Weeks-Days Pregnant</th>")
                  .append("<th>Hours in Labor</th>")
                  .append("<th>Delivery Type</th>")
                  .append("</tr>");
        int count = 0;
        for (ObstetricsRecordBean bean : prior) {
            if (bean != null) {
                priorTable.append("<tr>")
                          .append("<td>").append(bean.getYearConception()).append("</td>")
                          .append("<td>").append(bean.getWeeksPregnant()).append("</td>")
                          .append("<td>").append(bean.getHoursInLabor()).append("</td>")
                          .append("<td>").append(bean.getDeliveryType().toString()).append("</td>")
                          .append("</tr>");
                count++;
            }
        }
        priorTable.append("</table>");
        if (count != 0) {
            out.write(priorTable.toString());
        }
    }

    // show office visit obstetrics records
    List<ObstetricsRecordBean> office = viewObstetrics.getOfficeVisitObstetricsRecordByMID(Long.parseLong(pidString));
    if (office != null && !office.isEmpty()) {
        StringBuilder officeTable = new StringBuilder();
        officeTable.append("<br /><h2 align=\"center\">Office Visit Obstetrics Records</h2><table class=\"fTable\" align=\"center\">")
                   .append("<tr>")
                   .append("<th>Office Visit Date</th>")
                   .append("<th>Estimated Delivery Date</th>")
                   .append("<th>Blood Pressure</th>")
                   .append("<th>Weight</th>")
                   .append("<th>Fetal Heart Rate</th>")
                   .append("<th>Baby Count</th>")
                   .append("<th>Low Lying Placenta</th>");
        // if specialty is ob/gyn show the edit button (header row)
        if (currentPersonnel != null && currentPersonnel.getSpecialty().equalsIgnoreCase("ob/gyn")) {
            officeTable.append("<th></th>");
        }
        officeTable.append("</tr>");
        int count = 0;
        for (ObstetricsRecordBean bean : office) {
            if (bean != null) {
                long oid = bean.getOid();
                String initDate = bean.geinitDateString();
                String edd = bean.getEDDString();
                String lmp = bean.getLMPString();
                String weeksPreg = bean.getWeeksPregnant();
                int bloodPressureH = bean.getBloodPressureH();
                int bloodPressureL = bean.getBloodPressureL();
                double weight = bean.getWeight();
                int fhr = bean.getFHR();
                int babyCount = bean.getBabyCount();
                boolean multiPreg = bean.getMultiPregnancy();
                boolean lowLyingPlacenta = bean.getLyingPlacenta();

                officeTable.append("<tr>")
                           .append("<td>").append(initDate).append("</td>")
                           .append("<td>").append(edd).append("</td>")
                           .append("<td>").append(bloodPressureH).append('/').append(bean.getBloodPressureL()).append("</td>")
                           .append("<td>").append(weight).append("</td>")
                           .append("<td>").append(fhr).append("</td>")
                           .append("<td>").append(babyCount).append("</td>");
                if (lowLyingPlacenta) {
                    officeTable.append("<td> Yes </td>");
                } else {
                    officeTable.append("<td> No </td>");
                }
                // if specialty is ob/gyn show the edit button and send the parameters when user clicks edit
                if (currentPersonnel != null && currentPersonnel.getSpecialty().equalsIgnoreCase("ob/gyn")) {
                    officeTable.append("<td><form action=\"/iTrust/auth/hcp-obstetrics/editObstetricsOfficeVisit.jsp\" method=\"post\" id=\"editOVButtonForm\">")
                                .append("<input type=\"hidden\" name=\"oid\" value=\"").append(oid).append("\">")
                                .append("<input type=\"hidden\" name=\"pName\" value=\"").append(chosenPatient.getFullName()).append("\">")
                                .append("<input type=\"hidden\" name=\"initDate\" value=\"").append(initDate).append("\">")
                                .append("<input type=\"hidden\" name=\"lmp\" value=\"").append(lmp).append("\">")
                                .append("<input type=\"hidden\" name=\"weeksPreg\" value=\"").append(weeksPreg).append("\">")
                                .append("<input type=\"hidden\" name=\"edd\" value=\"").append(edd).append("\">")
                                .append("<input type=\"hidden\" name=\"bloodPressureH\" value=\"").append(bloodPressureH).append("\">")
                                .append("<input type=\"hidden\" name=\"bloodPressureL\" value=\"").append(bloodPressureL).append("\">")
                                .append("<input type=\"hidden\" name=\"weight\" value=\"").append(weight).append("\">")
                                .append("<input type=\"hidden\" name=\"fhr\" value=\"").append(fhr).append("\">")
                                .append("<input type=\"hidden\" name=\"multiPreg\" value=\"").append(multiPreg).append("\">")
                                .append("<input type=\"hidden\" name=\"babyCount\" value=\"").append(babyCount).append("\">")
                                .append("<input type=\"hidden\" name=\"lowLyingPlacenta\" value=\"").append(lowLyingPlacenta).append("\">")
                                .append("<input type=\"submit\" value=\"Edit\">")
                                .append("</form></td>");
                }

                officeTable.append("</tr>");
                TransactionLogger.getInstance().logTransaction(TransactionType.VIEW_OBSTETRICS_OV, loggedInMID,
                        Long.parseLong(pidString), "View office obstetrics record added on" + initDate + " for oid " + Long.toString(oid));
                count++;
            }
        }
        officeTable.append("</table>");
        if (count != 0) {
            out.write(officeTable.toString());
        }
    }

    ViewChildbirthAction vcAction = new ViewChildbirthAction(prodDAO, loggedInMID);
    List<ChildbirthRecordBean> childbirth = vcAction.getChildbirthRecordsByMID(Long.parseLong(pidString));
    if (childbirth != null && !childbirth.isEmpty()) {
        StringBuilder sb = new StringBuilder();
        sb.append("<br /><h2 align=\"center\">Childbirth Hospital Visit Records</h2><table class=\"fTable\" align=\"center\">")
                .append("<tr>")
                .append("<th>Visit Date</th>")
                .append("<th>Visit Type</th>")
                .append("<th>Delivery Type</th>")
                .append("<th>Delivery Date</th>")
                .append("<th>Pitocin</th>")
                .append("<th>Nitrous Oxide</th>")
                .append("<th>Pethidine</th>")
                .append("<th>Epidural Anaesthesia</th>")
                .append("<th>Magnesium Sulfate</th>")
                .append("<th>RH Immune Globulin</th>");
        if (currentPersonnel != null && currentPersonnel.getSpecialty().equalsIgnoreCase("ob/gyn")) {
            sb.append("<th></th>");
        }
        sb.append("</tr>");
        int cnt = 0;
        for (ChildbirthRecordBean bean: childbirth) {
            if (bean != null) {
                long oid = bean.getOid();
                String dov = bean.getVisitDateString();
                String vt = bean.getChildbirthVisitType().toString();
                String dt = bean.getDeliveryType().toString();
                String dd = bean.getDeliveryTimeString();
                double pc = bean.getPitocinDosage();
                double no = bean.getNitrousOxideDosage();
                double pd = bean.getPethidineDosage();
                double ea = bean.getEpiduralAnaesthesiaDosage();
                double ms = bean.getMagnesiumSulfateDosage();
                double rhig = bean.getRhImmuneGlobulinDosage();

                sb.append("<tr>")
                        .append("<td>").append(dov).append("</td>")
                        .append("<td>").append(vt).append("</td>")
                        .append("<td>").append(dt).append("</td>")
                        .append("<td>").append(dd == null || dd.length() == 0? "TBD": dd).append("</td>")
                        .append("<td>").append(pc).append("</td>")
                        .append("<td>").append(no).append("</td>")
                        .append("<td>").append(pd).append("</td>")
                        .append("<td>").append(ea).append("</td>")
                        .append("<td>").append(ms).append("</td>")
                        .append("<td>").append(rhig).append("</td>");

                if (currentPersonnel != null && currentPersonnel.getSpecialty().equalsIgnoreCase("ob/gyn")) {
                    sb.append("<td><form action=\"/iTrust/auth/hcp-obstetrics/editChildbirthHospitalVisit.jsp\" method=\"post\" id=\"editCHButtonForm\">")
                            .append("<input type=\"hidden\" name=\"oid\" value=\"").append(oid).append("\">")
                            .append("<input type=\"hidden\" name=\"pName\" value=\"").append(chosenPatient.getFullName()).append("\">")
                            .append("<input type=\"hidden\" name=\"dov\" value=\"").append(dov).append("\">")
                            .append("<input type=\"hidden\" name=\"vt\" value=\"").append(vt).append("\">")
                            .append("<input type=\"hidden\" name=\"dt\" value=\"").append(dt).append("\">")
                            .append("<input type=\"hidden\" name=\"dod\" value=\"").append(dd).append("\">")
                            .append("<input type=\"hidden\" name=\"pc\" value=\"").append(pc).append("\">")
                            .append("<input type=\"hidden\" name=\"no\" value=\"").append(no).append("\">")
                            .append("<input type=\"hidden\" name=\"pd\" value=\"").append(pd).append("\">")
                            .append("<input type=\"hidden\" name=\"ea\" value=\"").append(ea).append("\">")
                            .append("<input type=\"hidden\" name=\"ms\" value=\"").append(ms).append("\">")
                            .append("<input type=\"hidden\" name=\"rhig\" value=\"").append(rhig).append("\">")
                            .append("<input type=\"submit\" value=\"Edit\">")
                            .append("</form></td>");
                }
                sb.append("</tr>");
                //TODO: Add logging
                cnt++;
            }
        }
        sb.append("</table>");
        if (cnt != 0)
            out.write(sb.toString());
    }
%>
</div>
<br />
<div align=center>
<%
    //if specialty is ob/gyn show the initialize/add office visit buttons
    if (currentPersonnel != null && currentPersonnel.getSpecialty().equalsIgnoreCase("ob/gyn")) {
        out.write("<form action=\"/iTrust/auth/hcp-obstetrics/addObstetricsRecord.jsp\" method=\"post\" id=\"addInitialButtonForm\">");
        out.write("<input style=\"font-size: 120%; font-weight: bold;\" type=submit value=\"Initialize Obstetrics Record\">");
        out.write("</form><br />");
        out.write("<form action=\"/iTrust/auth/hcp-obstetrics/addObstetricsOfficeVisit.jsp\" method=\"post\" id=\"addOVButtonForm\">");
        out.write("<input style=\"font-size: 120%; font-weight: bold;\" type=submit value=\"Add Obstetrics Office Visit\">");
        out.write("</form><br />");
        out.write("<form action=\"/iTrust/auth/hcp-obstetrics/addChildbirthHospitalVisit.jsp\" method=\"post\" id=\"addCBHVButtonForm\">");
        out.write("<input style=\"font-size: 120%; font-weight: bold;\" type=submit value=\"Add Childbirth Hospital Visit\">");
        out.write("</form><br />");
        out.write("<form action=\"/iTrust/auth/hcp/scheduleAppt.jsp\" method=\"post\" id=\"\">");
        out.write("<input style=\"font-size: 120%; font-weight: bold;\" type=submit value=\"Schedule Next Appointment\">");
        out.write("</form>");
    }

%>

</div>

<%@include file="/footer.jsp" %>