<%@page errorPage="/auth/exceptionHandler.jsp" %>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPostpartumCareAction"%>
<%@page import="edu.ncsu.csc.itrust.action.AddPostpartumCareAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.InfantVisitRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.InfantVisitRecordDAO"%>>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ChildbirthRecordDAO"%>>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO"%>>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<%@include file="/global.jsp" %>

<%pageTitle = "iTrust - Add Infant Care Office Visit";%>

<%@include file="/header.jsp" %>
<itrust:patientNav thisTitle="Add Infant Care Office Visit" />

<%
String pidString = (String) session.getAttribute("pid");
if (pidString == null || pidString.equals("") || 1 > pidString.length()) {
    response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-infantcare/addInfantVisitRecord.jsp");
    return;
}
ViewPatientAction patientAction = new ViewPatientAction(prodDAO, loggedInMID, pidString);
PatientBean chosenPatient = patientAction.getPatient(pidString);

ViewPersonnelAction personnelAction = new ViewPersonnelAction(prodDAO, loggedInMID);
PersonnelBean currentPersonnel = personnelAction.getPersonnel("" + loggedInMID);
if (!currentPersonnel.getSpecialty().equalsIgnoreCase("ob/gyn")) {
	response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp-infantcare/addInfantVisitRecord.jsp");
    throw new ITrustException("Only HCPs with a specialization of OB/GYN may create a new infant care office visit");
}

//ChildbirthRecordDAO cbrDAO = new ChildbirthRecordDAO(prodDAO);
InfantVisitRecordDAO ivrDAO = new InfantVisitRecordDAO(prodDAO);
//if (!cbrDAO.isValidInfant(chosenPatient.getMID())) {
//    response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp-infantcare/addInfantVisitRecord.jsp");
//    throw new ITrustException("This is not a valid infant");
//}

PatientDAO pDAO = new PatientDAO(prodDAO);
	if (!pDAO.isValidInfant(chosenPatient.getMID())) {
    response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp-infantcare/addInfantVisitRecord.jsp");
    throw new ITrustException("This is not a valid infant");}

AddPostpartumCareAction addAction = new AddPostpartumCareAction(prodDAO, loggedInMID);
if ("true".equals(request.getParameter("formIsFilled"))) {	
	String errorTitle = "<p class=\"iTrustError\">Infant Office Visit cannot be added:</p>";
    String errors = "";

	String visitDate = request.getParameter("dov");
	//String cbd = request.getParameter("cbd");
	//String gender= request.getParameter("gender");
	String weight = request.getParameter("weight");
	String height = request.getParameter("height");
	String heartBeatRate = request.getParameter("heartBeatRate");
	//String bloodType = request.getParameter("bloodType");
	String bloodPressureH = request.getParameter("bloodPressureH");
	String bloodPressureL = request.getParameter("bloodPressureL");
    String gender = chosenPatient.getGender().toString();
    String bloodType = chosenPatient.getBloodType().toString();
    String cbd = chosenPatient.getDateOfBirthStr();

    try {
		InfantVisitRecordBean ivrb = new InfantVisitRecordBean();
		ivrb.setMid(Long.parseLong(pidString));
		if (visitDate != null && !visitDate.equals("")) {
			ivrb.setVisitDate(visitDate);
		} else {
			errors += "<p class=\"iTrustError\">Date of visit cannot be empty</p>";
		}

		try {
			ivrb.setWeight(Double.parseDouble(weight));
		} catch (NumberFormatException e) {
			errors += "<p class=\"iTrustError\">Weight must have a positive numeric value</p>";
		}
		try {
			ivrb.setHeight(Double.parseDouble(height));
		} catch (NumberFormatException e) {
			errors += "<p class=\"iTrustError\">Height must have a positive numeric value</p>";
		}
		try {
			ivrb.setHeartbeatRate(Integer.parseInt(heartBeatRate));
		} catch (NumberFormatException e) {
			errors += "<p class=\"iTrustError\">Heart Beat Rate must be an integer</p>";
		}
		try {
			ivrb.setBloodPressureH(Integer.parseInt(bloodPressureH));
		} catch (NumberFormatException e) {
			errors += "<p class=\"iTrustError\">High Blood pressure must be an integer</p>";
		}
		try {
			ivrb.setBloodPressureL(Integer.parseInt(bloodPressureL));
		} catch (NumberFormatException e) {
			errors += "<p class=\"iTrustError\">Low Blood pressure diastolic must be an integer</p>";
		}
		if (errors.equals("")) {
			addAction.addInfantVisitRecord(ivrb);
			response.sendRedirect("/iTrust/auth/hcp-infantcare/infantVisitHome.jsp?addOV");
		} else {
		    out.write(errorTitle + errors);
		}
	} catch (FormValidationException e) {
		out.write("<p class=\"iTrustError\">\"" + e.getMessage() + "\"</p>");
	}

	List<InfantVisitRecordBean> pastVisits = ivrDAO.getInfantVisitRecordsByMID(chosenPatient.getMID());
}
%>

<style>
.button {
    border-radius: 4px;
    background-color: rgb(248, 248, 248);
}
td {
	display: inline-block;
}
</style>

<%--@Start editting here@--%>

<div id="mainpage" align=center>
	    <form action="/iTrust/auth/hcp-infantcare/addInfantVisitRecord.jsp" method="post" id="newOfficeVisit">
        <input type="hidden" name="formIsFilled" value="true" />
        <table class="fTable" align="center">
			<tr><th colspan="3">Add Infant Office Visit</th></tr>
			<tr>
				<td><label for="dov">Date of visit: </label></td>
				<td>
					<input type="text" name="dov" id="dov" size="10" />
					<input type="button" value="Select Date" onclick="displayDatePicker('dov');" />
				</td>
				<td width="200px" id="dov-invalid"></td>
			</tr>
            <%--
			<tr>
				<td><label for="cbd">Child Birth Date: </label></td>
				<td>
					<input type="text" name="cbd" id="cbd" size="7" />
					<input type="button" value="Select Date" onclick="displayDatePicker('cbd');" />
				</td>
				<td width="200px" id="cbd-invalid"></td>
			</tr>
			--%>
			<%--
			<tr>
				<td class="subHeaderVertical">Gender:</td>
				<td><select name="genderStr">
					<%
						for (Gender g : Gender.values()) {
							selected = (g.equals(p.getGender())) ? "selected=selected" : "";
					%>
					<option value="<%=g.getName()%>" <%= StringEscapeUtils.escapeHtml("" + (selected)) %>><%= StringEscapeUtils.escapeHtml("" + (g.getName())) %></option>
					<%
						}
					%>
				</select></td>
			</tr>


			<tr>
				<td><label for="gender">Gender: </label></td>
				<td><input type="radio" name="gender" value="f" />Female <input type="radio" name="gender" value="m" />Male</td>
				<td width="200px" id="gender-invalid"></td>
			</tr>
            --%>




			<tr>
				<td><label for="weight">Weight: </label></td>
				<td><input type="text" name="weight" id="weight" size="3" /> lbs</td>
				<td width="200px" id="weight-invalid"></td>
			</tr>
			<tr>
				<td><label for="height">Height: </label></td>
				<td><input type="text" name="height" id="height" size="3" />cm</td>
				<td width="200px" id="height-invalid"></td>
			</tr>
			<tr>
				<td><label for="heartBeatRate">Heart Beat Rate: </label></td>
				<td><input type="text" name="heartBeatRate" id="heartBeatRate" size="3" /> bpm</td>
				<td width="200px" id="heartBeatRate-invalid"></td>
			</tr>

            <%--
			<tr>
				<td><label for="bloodType">Blood Type:</label></td>
				<td>
					<%-- use document.getElementById("pdt").selectedOptions[0].value to get option value --%>
            <%--
					<select name="bloodType" id="bloodType">
						<option value="ap">A+</option>
						<option value="an">A-</option>
						<option value="bp">B+</option>
						<option value="bn">B-</option>
						<option value="abp">AB+</option>
						<option value="abn">AB-</option>
						<option value="op">O+</option>
						<option value="on">O-</option>
						<option value="ns">N/S</option>
					</select>
				</td>
				<td width="200px"></td>
			</tr>
            --%>
			<tr>
				<td><label>Blood Pressure: </label></td>
				<td>
					<input type="text" name="bloodPressureL" id="bloodPressureL" size="4" /> /
					<input type="text" name="bloodPressureH" id="bloodPressureH" size="4" /> mmHg
				</td>
				<td width="200px" id="bp-invalid"></td>
			</tr>
		</table>
        <br />
        <input type="submit" id="submit" value="Submit" />
    </form>
</div>

<p><br/><a href="/iTrust/auth/hcp-infantcare/infantVisitHome.jsp">Back to Home</a></p>
<%@include file="/footer.jsp" %>