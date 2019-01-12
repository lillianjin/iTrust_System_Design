<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.InfantVisitRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO"%>
<%--@page import="edu.ncsu.csc.itrust.model.old.enums.Gender"--%>
<%@page import="edu.ncsu.csc.itrust.logger.TransactionLogger"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.TransactionType"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@ page import="edu.ncsu.csc.itrust.action.*" %>

<%@include file="/global.jsp"%>

<% pageTitle = "iTrust - Infant Visit Home";%>

<%@include file="/header.jsp"%>
<itrust:patientNav thisTitle="Infant Visit Home" />

<%
    /* Require a Patient ID first */
    String pidString = (String) session.getAttribute("pid");

    if (pidString == null || 1 > pidString.length()) {
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp-infantcare/infantVisitHome.jsp");
        return;
    }

    ViewPatientAction patientAction = new ViewPatientAction(prodDAO, loggedInMID, pidString);
    PatientBean chosenPatient = patientAction.getPatient(pidString);
    String cbd = chosenPatient.getDateOfBirthStr();

    //also handle when other JSPs redirect here after successfully doing whatever it is they do
    if (request.getParameter("addOV") != null) {
        out.write("<p style=\"border: 1px solid #090; color: #090; font-size: 20px; padding: 2px;\">" +
                "Infant Care Office Visit successfully added</p>");
    }
    else if (request.getParameter("editOV") != null) {
        out.write("<p style=\"border: 1px solid #090; color: #090; font-size: 20px; padding: 2px;\">" +
                "Infant Care Office Visit successfully edited</p>");
    }

    PatientDAO patients = new PatientDAO(prodDAO);
    PatientBean patient = null;

    if (!patients.isValidInfant(chosenPatient.getMID())) {
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp-infantcare/infantVisitHome.jsp");
        throw new ITrustException("This is not a valid infant");}

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
    ViewPostpartumCareAction viewInfantVisit = new ViewPostpartumCareAction(prodDAO, loggedInMID);
    // get the current viewing personnel
    ViewPersonnelAction personnelAction = new ViewPersonnelAction(prodDAO, loggedInMID);
    PersonnelBean currentPersonnel = personnelAction.getPersonnel("" + loggedInMID);

    // show office visit infant care records
    List<InfantVisitRecordBean> office = viewInfantVisit.getInfantVisitRecordsByMID(Long.parseLong(pidString));
    StringBuilder officeTable = new StringBuilder();
    officeTable.append("<br /><h2 align=\"center\">Office Visit Infant Records</h2><table class=\"fTable\" align=\"center\">")
            .append("<tr>")
            .append("<th>Office Visit Date</th>")
            .append("<th>Date of Birth</th>")
            .append("<th>Weight</th>")
            .append("<th>Height</th>")
            .append("<th>Heart Beat Rate</th>")
            .append("<th>Blood Pressure</th>");
    // if specialty is ob/gyn show the edit button (header row)
    if (currentPersonnel != null && currentPersonnel.getSpecialty().equalsIgnoreCase("ob/gyn")) {
        officeTable.append("<th></th>");
    }
    officeTable.append("</tr>");
    if (office != null && !office.isEmpty()) {
        int count = 0;
        for (InfantVisitRecordBean bean : office) {
            if (bean != null) {
                long iid = bean.getIid();
                String visitDate = bean.getVisitDateString();
                double weight = bean.getWeight();
                double height = bean.getHeight();
                int heartBeatRate = bean.getHeartbeatRate();
                int bloodPressureH = bean.getBloodPressureH();
                int bloodPressureL = bean.getBloodPressureL();

                officeTable.append("<tr>")
                        .append("<td>").append(visitDate).append("</td>")
                        .append("<td>").append(cbd).append("</td>")
                        .append("<td>").append(weight).append("</td>")
                        .append("<td>").append(height).append("</td>")
                        .append("<td>").append(heartBeatRate).append("</td>")
                        .append("<td>").append(bloodPressureL).append('/').append(bean.getBloodPressureH()).append("</td>");

                // if specialty is ob/gyn show the edit button and send the parameters when user clicks edit
                if (currentPersonnel != null && currentPersonnel.getSpecialty().equalsIgnoreCase("ob/gyn")) {
                    officeTable.append("<td><form action=\"/iTrust/auth/hcp-infantcare/editInfantVisitRecord.jsp\" method=\"post\" id=\"editOVButtonForm\">")
                            .append("<input type=\"hidden\" name=\"iid\" value=\"").append(iid).append("\">")
                            .append("<input type=\"hidden\" name=\"pName\" value=\"").append(chosenPatient.getFullName()).append("\">")
                            .append("<input type=\"hidden\" name=\"cbd\" value=\"").append(cbd).append("\">")
                            .append("<input type=\"hidden\" name=\"visitDate\" value=\"").append(visitDate).append("\">")
                            .append("<input type=\"hidden\" name=\"weight\" value=\"").append(weight).append("\">")
                            .append("<input type=\"hidden\" name=\"height\" value=\"").append(height).append("\">")
                            .append("<input type=\"hidden\" name=\"heartBeatRate\" value=\"").append(heartBeatRate).append("\">")
                            .append("<input type=\"hidden\" name=\"bloodPressureL\" value=\"").append(bloodPressureL).append("\">")
                            .append("<input type=\"hidden\" name=\"bloodPressureH\" value=\"").append(bloodPressureH).append("\">")
                            .append("<input type=\"submit\" value=\"Edit\">")
                            .append("</form></td>");
                }
                officeTable.append("</tr>");
                count++;
            }
        }
    }
    officeTable.append("</table>");
    out.write(officeTable.toString());
    TransactionLogger.getInstance().logTransaction(TransactionType.VIEW_INFANT_CARE_RECORD, loggedInMID,
            Long.parseLong(pidString), "View postpartum care record for chosen infant with MID " + Long.toString(chosenPatient.getMID()));
%>
</div>
<br />
<div align=center>
<%
    //if specialty is ob/gyn show the initialize/add office visit buttons
    if (currentPersonnel != null && currentPersonnel.getSpecialty().equalsIgnoreCase("ob/gyn")) {
        out.write("<form action=\"/iTrust/auth/hcp-infantcare/addInfantVisitRecord.jsp\" method=\"post\" id=\"addOVButtonForm\">");
        out.write("<input style=\"font-size: 120%; font-weight: bold;\" type=submit value=\"Add Infant Postpartum Care Record\">");
        out.write("</form>");
    }
%>
</div>

<br />

<div id="mainpage" align=center>
    <form action="/iTrust/auth/hcp-infantcare/infantVisitHome.jsp" method="post" id="immuRecord">
        <input type="hidden" name="formIsFilled" value="true" />
        <h2 align="center">Vaccine Recommendation Table</h2>
        <table class="fTable" align="center">
            <tr>
                <th width="100px"></th>
                <th>Vaccine Name</th>
                <th>Recommend Date</th>
                <th width="100px"></th>
            </tr>
            <tr style="visibility:collapse">
                <td width="100px"></td>
                <td><label for="birth">Birth Date</label></td>
                <td>
                    <p id="birth"><%=cbd%></p>
                </td>
                <td width="100px"></td>
            </tr>
            <tr>
                <td width="100px"></td>
                <td><label for="hepb">Hepatitis B</label></td>
                <td><p id="hepb"></p></td>
                <td width="100px"></td>
            </tr>
            <tr>
                <td width="100px"></td>
                <td><label for="hepa">Hepatitis A</label></td>
                <td><p id="hepa"></p></td>
                <td width="100px"></td>
            </tr>
            <tr>
                <td width="100px"></td>
                <td><label for="dtap">DTaP</label></td>
                <td><p id="dtap"></p></td>
                <td width="100px"></td>
            </tr>
            <tr>
                <td width="100px"></td>
                <td><label for="flu">Influenza Vaccine</label></td>
                <td><p id="flu"></p></td>
                <td width="100px"></td>
            </tr>
            <tr>
                <td width="100px"></td>
                <td><label for="mmr">Measles, Mumps, and Rubella</label></td>
                <td><p id="mmr"></p></td>
                <td width="100px"></td>
            </tr>
        </table>
    </form>
</div>


<script type="text/javascript">
    // call the calculate functions when child date of birth is shown
    calculate();

    function calculate() {
        var cbd = document.getElementById("birth").innerHTML;
        var cbdDate = new Date(cbd);
        if (!isNaN(cbdDate.getTime())) {
            // alculate the estimated immunization date of Hepatitis B (HepB)
            var hepbDate = new Date(cbdDate.getTime() + (1000 * 30 * 24 * 60 * 60));
            var hepb = (hepbDate.getMonth()+1) + "/" + hepbDate.getDate() + "/" + hepbDate.getFullYear();
            document.getElementById("hepb").innerHTML = hepb;

            //calculate the estimated immunization date of Hepatitis A (HepA)
            var hepaDate = new Date(cbdDate.getTime() + (1000 * 365 * 24 * 60 * 60));
            var hepa = (hepaDate.getMonth()+1) + "/" + hepaDate.getDate() + "/" + hepaDate.getFullYear();
            document.getElementById("hepa").innerHTML = hepa;

            //calculate the estimated immunization date of Diphtheria, Tetanus, Pertussis (DTaP)
            var dtapDate = new Date(cbdDate.getTime() + (1000 * 60 * 24 * 60 * 60));
            var dtap = (dtapDate.getMonth()+1) + "/" + dtapDate.getDate() + "/" + dtapDate.getFullYear();
            document.getElementById("dtap").innerHTML = dtap;

            // calculate the estimated immunization date of Influenza Vaccine (Flu)
            var fluDate = new Date(cbdDate.getTime() + (1000 * 180 * 24 * 60 * 60));
            var flu = (fluDate.getMonth()+1) + "/" + fluDate.getDate() + "/" + fluDate.getFullYear();
            document.getElementById("flu").innerHTML = flu;

            // calculate the estimated immunization date of Measles, Mumps, and Rubella (MMR)
            var mmrDate = new Date(cbdDate.getTime() + (1000 * 365 * 24 * 60 * 60));
            var mmr = (mmrDate.getMonth()+1) + "/" + mmrDate.getDate() + "/" + mmrDate.getFullYear();
            document.getElementById("mmr").innerHTML = mmr;
        }
    }

</script>

<%@include file="/footer.jsp" %>