<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="edu.ncsu.csc.itrust.action.ViewPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPostpartumCareAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.InfantVisitRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Gender"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.BloodType"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<%@include file="/global.jsp" %>

<%pageTitle = "iTrust - Edit Infant Postpartum Care Record";%>

<%@include file="/header.jsp" %>
<itrust:patientNav thisTitle="Infant Postpartum Care Record" />

<%
    /* Require a Patient ID first */
    String pidString = (String) session.getAttribute("pid");
    if (pidString == null || pidString.equals("") || 1 > pidString.length()) {
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-infantcare/editInfantVisitRecord.jsp");
        return;
    }

    // Choose a new birth infant
    ViewPatientAction patientAction = new ViewPatientAction(prodDAO, loggedInMID, pidString);
    PatientBean chosenPatient = patientAction.getPatient(pidString);


    // check if the HCP is ob/gyn HCP, if not, throw an exception
    ViewPersonnelAction personnelAction = new ViewPersonnelAction(prodDAO, loggedInMID);
    PersonnelBean currentPersonnel = personnelAction.getPersonnel("" + loggedInMID);
    if (!currentPersonnel.getSpecialty().equalsIgnoreCase("ob/gyn")) {
        // the following line later to direct to the view record page
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp-infantcare/editInfantVisitRecord.jsp");
        throw new ITrustException("Only HCPs with a specialization of OB/GYN may edit infant visit record");
    }

    // Get the paramters passed from infantVisitRecordHome.jsp when user clicks the edit button
    long iid = Long.parseLong(request.getParameter("iid"));
    String patientName = request.getParameter("pName");
    String cbd = request.getParameter("cbd");
    String visitDate = request.getParameter("visitDate");
    
    // Declare changeable variables
    double weight = 0;
    double height = 0;
    int heartBeatRate = 0;
    int bloodPressureL = 0;
    int bloodPressureH = 0;
    
    // Get initial values of those changeable variables
    if (request.getParameter("formIsFilled") == null){
	    weight = Double.parseDouble(request.getParameter("weight"));
	    height = Double.parseDouble(request.getParameter("height"));
	    heartBeatRate = Integer.parseInt(request.getParameter("heartBeatRate"));
	    bloodPressureL = Integer.parseInt(request.getParameter("bloodPressureL"));
	    bloodPressureH = Integer.parseInt(request.getParameter("bloodPressureH"));
    }

    EditPostpartumCareAction editAction = new EditPostpartumCareAction(prodDAO, loggedInMID);

    // Run the following code after the user clicks save
    if (request.getParameter("formIsFilled") != null && "true".equals(request.getParameter("formIsFilled"))) {
        try {
            InfantVisitRecordBean update = new InfantVisitRecordBean();

            String errorTitle = "<p class=\"iTrustError\">Infant Office Visit failed to be updated:</p>";
            String errors = "";

            iid = Long.parseLong(request.getParameter("iid"));
            visitDate = request.getParameter("visitDate");

            // get weight
            try {
                weight = Double.parseDouble(request.getParameter("weight"));
            } catch (NumberFormatException e) {
                errors += "<p class=\"iTrustError\">Weight must be a double</p>";
            }

            // get height
            try {
                height = Double.parseDouble(request.getParameter("height"));
            } catch (NumberFormatException e) {
                errors += "<p class=\"iTrustError\">Height must be a double</p>";
            }

            // get heart beat rate
            try {
                heartBeatRate = Integer.parseInt(request.getParameter("heartBeatRate"));
            } catch (NumberFormatException e) {
                errors += "<p class=\"iTrustError\">Heart Beat Rate must be an int</p>";
            }

            // get bloodPressureL and bloodPressureH
            try {
                bloodPressureL = Integer.parseInt(request.getParameter("bloodPressureL"));
            } catch (NumberFormatException e) {
                errors += "<p class=\"iTrustError\">Blood Pressure L must be an int</p>";
            }
            try {
                bloodPressureH = Integer.parseInt(request.getParameter("bloodPressureH"));
            } catch (NumberFormatException e) {
                errors += "<p class=\"iTrustError\">Blood Pressure H must be an int</p>";
            }

            if (errors.equals("")) {
                // Attributes that are not editable
                update.setIid(iid);
                update.setMid(Long.parseLong(pidString));
                update.setVisitDate(visitDate);

                // Attributes that are editable
                update.setWeight(weight);
                update.setHeight(height);
                update.setHeartbeatRate(heartBeatRate);
                update.setBloodPressureL(bloodPressureL);
                update.setBloodPressureH(bloodPressureH);
                editAction.editInfantVisitRecord(update);
                response.sendRedirect("/iTrust/auth/hcp-infantcare/infantVisitHome.jsp?editOV");
            } else {
            	out.write(errorTitle + errors);
            }
            
        } catch(FormValidationException e) {
            out.write("<p class=\"iTrustError\">" + e.getMessage() + "</p>");
        }
    }
%>

<div id="mainpage" align=center>
    <form action="/iTrust/auth/hcp-infantcare/editInfantVisitRecord.jsp" method="post" id="updateRecord">
        <input type="hidden" name="formIsFilled" value="true">
        <input type="hidden" name="iid" value="<%=iid%>">
        <table class="fTable" align="center">
            <tr><th colspan="3">Edit Infant Postpartum Care Record</th></tr>
            <tr>
                <td width="200px"><label for="pname">Patient Name:</label></td>
                <td width="250px"><%=patientName%></td>
                <td></td>
            </tr>
            <tr>
                <td width="200px"><label for="cbd">Date of Birth:</label></td>
                <td width="250px"><%=cbd%></td>
                <td></td>
            </tr>
            <tr>
                <td><label for="visitDate">Office Visit Date:</label></td>
                <td><input type="text" name="visitDate" id="visitDate" size="10" value="<%=visitDate%>" readonly></td>
                <td></td>
            </tr>
            <tr>
                <td><label for="weight">Weight: </label></td>
                <td><input type="text" name="weight" id="weight" size="5" value="<%=weight%>"> lbs</td>
                <td></td>
            </tr>
            <tr>
                <td><label for="height">Height: </label></td>
                <td><input type="text" name="height" id="height" size="5" value="<%=height%>"> cm</td>
                <td></td>
            </tr>
            <tr>
                <td><label for="heartBeatRate">Fetal Heart Rate: </label></td>
                <td><input type="text" name="heartBeatRate" id="heartBeatRate" maxlength="2" size="3" value="<%=heartBeatRate%>"> bpm</td>
                <td></td>
            </tr>
            <tr>
                <td><label for="blood pressure">Blood Pressure : </label></td>
                <td>
                    <input type="text" name="bloodPressureL" id="bloodPressureL" maxlength="3" size="4" value="<%=bloodPressureL%>">
                    <input type="text" name="bloodPressureH" id="bloodPressureH" maxlength="3" size="4" value="<%=bloodPressureH%>"> mmHg
                </td>
                <td></td>
            </tr>
        </table>
        <br/>
        <input type="submit" id="save" value="save" />
    </form>
</div>
<br/>
<div align=center>
    <form action="/iTrust/auth/hcp-infantcare/infantVisitHome.jsp" method="post" id="backtoHomeButtonForm">
        <input style="font-size: 120%; font-weight: bold;" type=submit value="Back to Postpartum Record Home">
    </form>
</div>

<%@include file="/footer.jsp" %>