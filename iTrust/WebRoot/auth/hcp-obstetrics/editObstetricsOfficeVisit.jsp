<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="edu.ncsu.csc.itrust.action.ViewPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditObstetricsAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricsRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Gender"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.DeliveryType"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.PregnancyStatus"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<%@include file="/global.jsp" %>

<%pageTitle = "iTrust - Obstetrics Office Visit";%>

<%@include file="/header.jsp" %>
<itrust:patientNav thisTitle="Obstetrics Office Visit" />

<%
    /* Require a Patient ID first */
    String pidString = (String) session.getAttribute("pid");
    if (pidString == null || pidString.equals("") || 1 > pidString.length()) {
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-obstetrics/editObstetricsOfficeVisit.jsp");
        return;
    }

    // check if the selected patient is female, if not, throw an exception
    ViewPatientAction patientAction = new ViewPatientAction(prodDAO, loggedInMID, pidString);
    PatientBean chosenPatient = patientAction.getPatient(pidString);
    if (chosenPatient == null || !chosenPatient.getGender().equals(Gender.Female)) {
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp-obstetrics/editObstetricsOfficeVisit.jsp");
        throw new ITrustException("The patient is not eligible for obstetrics care."); //this shows up
    }

    // check if the HCP is ob/gyn HCP, if not, throw an exception
    ViewPersonnelAction personnelAction = new ViewPersonnelAction(prodDAO, loggedInMID);
    PersonnelBean currentPersonnel = personnelAction.getPersonnel("" + loggedInMID);
    if (!currentPersonnel.getSpecialty().equalsIgnoreCase("ob/gyn")) {
        // the following line later to direct to the view record page
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp-obstetrics/editObstetricsOfficeVisit.jsp");
        throw new ITrustException("Only HCPs with a specialization of OB/GYN may create a new obstetrics record");
    }

    // Get the paramters passed from obstetricsHome.jsp when user clicks the edit button
    long oid = Long.parseLong(request.getParameter("oid"));
    String patientName = request.getParameter("pName");
    String visitDate = request.getParameter("initDate");
    String lmp = request.getParameter("lmp");
    String weeksPreg = request.getParameter("weeksPreg");
    String edd = request.getParameter("edd");
    
    // Declare changeable variables
    int bloodPressureH = 0;
    int bloodPressureL = 0;
    int fhr = 0;
    double weight = 0;
    boolean multiPreg = false;
    int babyCount = 0;
    boolean lowLyingPlacenta = false;
    
    // Get initial values of these changeable variables;
    if (request.getParameter("formIsFilled") == null) {
	    bloodPressureH = Integer.parseInt(request.getParameter("bloodPressureH"));
	    bloodPressureL = Integer.parseInt(request.getParameter("bloodPressureL"));
	    fhr = Integer.parseInt(request.getParameter("fhr"));
	    weight = Double.parseDouble(request.getParameter("weight"));
	    multiPreg = Boolean.parseBoolean(request.getParameter("multiPreg"));
	    babyCount = Integer.parseInt(request.getParameter("babyCount"));
	    lowLyingPlacenta = Boolean.parseBoolean(request.getParameter("lowLyingPlacenta"));
    }

    EditObstetricsAction editAction = new EditObstetricsAction(prodDAO, loggedInMID);
    
    // Run the following code after the user clicks save
    if (request.getParameter("formIsFilled") != null && "true".equals(request.getParameter("formIsFilled"))) {
        try {
            ObstetricsRecordBean update = new ObstetricsRecordBean();

            String errorTitle = "<p class=\"iTrustError\">Office Visit Record failed to be updated:</p>";
            String errors = "";

            oid = Long.parseLong(request.getParameter("oid"));
            visitDate = request.getParameter("visitDate");
            lmp = request.getParameter("lmp");
            weeksPreg = request.getParameter("weeksPreg");
            edd = request.getParameter("edd");

            // get weight
            try {
                weight = Double.parseDouble(request.getParameter("weight"));
            } catch (NumberFormatException e) {
                errors += "<p class=\"iTrustError\">Weight must be a double</p>";
            }

            // get bloodPressureH and bloodPressureL
            try {
                bloodPressureH = Integer.parseInt(request.getParameter("bloodPressureH"));
            } catch (NumberFormatException e) {
                errors += "<p class=\"iTrustError\">Blood Pressure H must be an int</p>";
            }
            try {
                bloodPressureL = Integer.parseInt(request.getParameter("bloodPressureL"));
            } catch (NumberFormatException e) {
                errors += "Blood Pressure L must be an int";
            }

            // get fetal heart rate
            try {
                fhr = Integer.parseInt(request.getParameter("fhr"));
            } catch (NumberFormatException e) {
                errors += "<p class=\"iTrustError\">Heart rate must be an int</p>";
            }

            // get multipregnancy check
            if (request.getParameter("multiPreg") != null && request.getParameter("multiPreg").equals("on")) {
                multiPreg = true;
            } else {
                multiPreg = false;
            }

            // get baby count
            try {
                babyCount = Integer.parseInt(request.getParameter("babyCount"));
            } catch (NumberFormatException e) {
                errors += "<p class=\"iTrustError\">baby count must be an int</p>";
            }

            // get low Lying Placenta
            if (request.getParameter("placenta") != null && request.getParameter("placenta").equals("on")) {
                lowLyingPlacenta = true;
            } else {
                lowLyingPlacenta = false;
            }
            if (errors.equals("")) {
                // Attributes that are not editable
                update.setOid(oid);
                update.setMid(Long.parseLong(pidString));
                update.setPregStatus(PregnancyStatus.OfficeVisit);
                update.setDeliveryType(DeliveryType.Vaginal);
                update.setinitDate(visitDate);
                update.setLMP(lmp);
                update.setWeeksPregnant(weeksPreg);
                update.setEDD(edd);

                // Attributes that are editable
                update.setWeight(weight);
                update.setBloodPressureH(bloodPressureH);
                update.setBloodPressureL(bloodPressureL);
                update.setFHR(fhr);
                update.setMultiPregnancy(multiPreg);
                update.setBabyCount(babyCount);
                update.setLyingPlacenta(lowLyingPlacenta);
                editAction.editObstetricsRecord(update);
                response.sendRedirect("/iTrust/auth/hcp-obstetrics/obstetricsHome.jsp?editOV");
            }
            else {
                out.write(errorTitle + errors);
            }

        } catch(FormValidationException e) {
            out.write("<p class=\"iTrustError\">" + e.getMessage() + "</p>");
        }
    }
%>

<div id="mainpage" align=center>
    <form action="/iTrust/auth/hcp-obstetrics/editObstetricsOfficeVisit.jsp" method="post" id="updateRecord">
        <input type="hidden" name="formIsFilled" value="true">
        <input type="hidden" name="oid" value="<%=oid%>">
        <table class="fTable" align="center">
            <tr><th colspan="3">Edit Obstetrics Office Visit</th></tr>
            <tr>
                <td width="200px"><label for="pname">Patient Name:</label></td>
                <td width="250px"><%=patientName%></td>
                <td></td>
            </tr>
            <tr>
                <td><label for="ovDate">Office visit date:</label></td>
                <td><input type="text" name="visitDate" id="visitDate" size="10" value="<%=visitDate%>" readonly></td>
                <td></td>
            </tr>
            <tr>
                <td><label for="lmp">Last menstrual period:</label></td>
                <td><input type="text" name="lmp" id="lmp" size="10" value="<%=lmp%>" readonly></td>
                <td></td>
            </tr>
            <tr>
                <td><label for="pregWeekDays">Weeks-Days pregnant:</label></td>
                <td><input type="text" name="weeksPreg" id="weeksPreg" size="10" value="<%=weeksPreg%>" readonly></td>
                <td></td>
            </tr>
            <tr>
                <td><label for="edd">Estimated delivery date:</label></td>
                <td><input type="text" name="edd" id="edd" size="10" value="<%=edd%>" readonly></td>
                <td></td>
            </tr>
            <tr>
                <td><label for="weight">Weight: </label></td>
                <td><input type="text" name="weight" id="weight" size="15" value="<%=weight%>"> lbs</td>
                <td></td>
            </tr>
            <tr>
                <td><label for="blood pressure">Blood Pressure : </label></td>
                <td>
                    <input type="text" name="bloodPressureH" id="bloodPressureH" maxlength="3" size="14" value="<%=bloodPressureH%>">
                    <input type="text" name="bloodPressureL" id="bloodPressureL" maxlength="3" size="14" value="<%=bloodPressureL%>"> mmHg
                </td>
                <td></td>
            </tr>
            <tr>
                <td><label for="fhr">Fetal Heart Rate: </label></td>
                <td><input type="text" name="fhr" id="fhr" maxlength="2" size="13" value="<%=fhr%>"> bpm</td>
                <td></td>
            </tr>
            <tr>
                <td><label for="multiple">Multipregnancy: </label></td>
                <% if (multiPreg) {
                    out.write("<td><input type=\"checkbox\" name=\"multiPreg\" id=\"multiPreg\" checked></td>");
                } else {
                    out.write("<td><input type=\"checkbox\" name=\"multiPreg\" id=\"multiPreg\"></td>");
                }%>
                <td></td>
            </tr>
            <tr>
                <td><label>Baby Count: </label></td>
                <td><input type="text" name="babyCount" id="babyCount" maxlength="2" size="13" value="<%=babyCount%>"/></td>
                <td></td>
            </tr>
            <tr>
                <td><label for="placenta">Low lying Placenta: </label></td>
                <%
                    if (lowLyingPlacenta) {
                        out.write("<td><input type=\"checkbox\" name=\"placenta\" id=\"placenta\" checked></td>");
                    } else {
                        out.write("<td><input type=\"checkbox\" name=\"placenta\" id=\"placenta\"></td>");
                    }
                %>
                <td></td>
            </tr>
        </table>
        <br/>
        <input type="submit" id="save" value="save" />
    </form>
</div>
<br/>
<div align=center>
    <form action="/iTrust/auth/hcp-obstetrics/obstetricsHome.jsp" method="post" id="backtoHomeButtonForm">
        <input style="font-size: 120%; font-weight: bold;" type=submit value="Back to Obstetrics Record Home">
    </form>
</div>

<%@include file="/footer.jsp" %>