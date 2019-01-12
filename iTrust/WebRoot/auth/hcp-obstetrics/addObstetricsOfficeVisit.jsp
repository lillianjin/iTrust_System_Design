<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList" %>
<%@page import="edu.ncsu.csc.itrust.action.ViewPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewObstetricsAction"%>
<%@page import="edu.ncsu.csc.itrust.action.AddObstetricsAction"%>
<%@page import="edu.ncsu.csc.itrust.action.AddUltrasoundAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricsRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricsRecordDAO"%>>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Gender"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.DeliveryType"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.PregnancyStatus"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<%//here from TD %>
<%@page import="com.sun.xml.internal.bind.CycleRecoverable.Context"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="java.sql.Connection"%>
<%@page import="javax.imageio.ImageIO"%>
<%//here from TD %>

<%@include file="/global.jsp" %>

<%pageTitle = "iTrust - Add Obstetrics Office Visit";%>

<%@include file="/header.jsp" %>
<itrust:patientNav thisTitle="Add Obstetrics Office Visit" />

<%
String pidString = (String) session.getAttribute("pid");
if (pidString == null || pidString.equals("") || 1 > pidString.length()) {
    response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-obstetrics/addObstetricsOfficeVisit.jsp");
    return;
}
ViewPatientAction patientAction = new ViewPatientAction(prodDAO, loggedInMID, pidString);
PatientBean chosenPatient = patientAction.getPatient(pidString);
if (chosenPatient == null || !chosenPatient.getGender().equals(Gender.Female)) {
    response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp-obstetrics/addObstetricsOfficeVisit.jsp");
    throw new ITrustException("The patient is not eligible for obstetrics care.");
}
ViewPersonnelAction personnelAction = new ViewPersonnelAction(prodDAO, loggedInMID);
PersonnelBean currentPersonnel = personnelAction.getPersonnel("" + loggedInMID);
if (!currentPersonnel.getSpecialty().equalsIgnoreCase("ob/gyn")) {
	response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp-obstetrics/addObstetricsOfficeVisit.jsp");
    throw new ITrustException("Only HCPs with a specialization of OB/GYN may create a new obstetrics office visit");
}

AddObstetricsAction addAction = new AddObstetricsAction(prodDAO, loggedInMID);
if ("true".equals(request.getParameter("formIsFilled"))) {
	String date = request.getParameter("date");
	String lmp = request.getParameter("lmp");
	String edd = request.getParameter("edd");
	String weeksPregnant = request.getParameter("weeksPregnant");
	String weight = request.getParameter("weight");
	String bps = request.getParameter("bps");
	String bpd = request.getParameter("bpd");
	String fhr = request.getParameter("fhr");
	String twins = request.getParameter("twins");
	String placenta = request.getParameter("placenta");
	int ultracnt = Integer.parseInt(request.getParameter("ultrasound"));
	List<String> errorList = new ArrayList<String>();
	try {
		for (int i = 0; i < ultracnt; i++) {
		    String errors = "";
		    String errortitle = "<p class=\"iTrustError\">Ultrasound Record " + (i + 1) + " cannot be added</p>";

			UltrasoundRecordBean urb = new UltrasoundRecordBean();
			urb.setMid(Long.parseLong(pidString));
			urb.setVisitDate(date);

			try {
				urb.setFetusid(Integer.parseInt(request.getParameter("fid" + i)));
			} catch (NumberFormatException e) {
				errors += "<p class=\"iTrustError\">Fetus' ID must be an integer</p>";
			}

			try {
				urb.setCrownRumpLength(Double.parseDouble(request.getParameter("crl" + i)));
			} catch (NumberFormatException e) {
				errors += "<p class=\"iTrustError\">Crown Rump Length must be a double</p>";
			}
			try {
				urb.setBiparietalDiameter(Double.parseDouble(request.getParameter("bd" + i)));
			} catch (NumberFormatException e) {
				errors += "<p class=\"iTrustError\">Biparietal Diameter must be a double</p>";
			}

			try {
				urb.setHeadCircumference(Double.parseDouble(request.getParameter("hc" + i)));
			} catch (NumberFormatException e) {
				errors += "<p class=\"iTrustError\">Head Circumference must be a double</p";
			}
			try {
				urb.setFemurLength(Double.parseDouble(request.getParameter("fl" + i)));
			} catch (NumberFormatException e) {
				errors += "<p class=\"iTrustError\">Femur Length must be a double</p>";
			}
			try {
				urb.setOccipitofrontalDiameter(Double.parseDouble(request.getParameter("od" + i)));
			} catch (NumberFormatException e) {
				errors += "<p class=\"iTrustError\">Occipitofrontal Diameter must be a double</p>";
			}
			try {
				urb.setAbdominalCircumference(Double.parseDouble(request.getParameter("ac" + i)));
			} catch (NumberFormatException e) {
				errors += "<p class=\"iTrustError\">Abdominal Circumference must be a double</p>";
			}
			try {
				urb.setHumerusLength(Double.parseDouble(request.getParameter("hl" + i)));
			} catch (NumberFormatException e) {
				errors += "<p class=\"iTrustError\">Humerus Length must be a double</p>";
			}
			try {
				urb.setEstimatedFetalWeight(Double.parseDouble(request.getParameter("efw" + i)));
			} catch (NumberFormatException e) {
				errors += "<p class=\"iTrustError\">Estimated Fetal Weight must be a double</p>";
			}
			if (errors.equals("")) {
				AddUltrasoundAction addUAction = new AddUltrasoundAction(prodDAO, loggedInMID);
				addUAction.addUltrasoundRecord(urb);
			} else {
			    errors = errortitle + errors;
			    errorList.add(errors);
			}
		}
		String errorsOV = "";
		String errorOVtitle = "<p class=\"iTrustError\">Office Visit cannot be added:</p>";
		ObstetricsRecordBean orb = new ObstetricsRecordBean();
		orb.setMid(Long.parseLong(pidString));

		if (date != null && !date.equals("") && lmp != null && !lmp.equals("")) {
			orb.setinitDate(date);
			orb.setLMP(lmp);
			orb.setEDD(edd);
			String[] arr = weeksPregnant.split(" ");
			if (arr.length >= 2) {
				orb.setWeeksPregnant((arr[0] + "-" + arr[2]));
			}
		} else {
		    if (date == null || date.equals("")) {
		        errorsOV += "<p class=\"iTrustError\">Date of Visit cannot be empty</p>";
			}
			if (lmp == null || lmp.equals("")) {
			    errorsOV += "<p class=\"iTrustError\">Last LMP Date cannot be empty</p>";
			}
		}
		
		orb.setPregStatus(PregnancyStatus.OfficeVisit);
		orb.setDeliveryType(DeliveryType.Vaginal);
		
		orb.setMultiPregnancy(twins != null && twins.equals("on"));
		orb.setLyingPlacenta(placenta != null && placenta.equals("on"));
		try {
			orb.setWeight(Double.parseDouble(weight));
		} catch (NumberFormatException e) {
			errorsOV += "<p class=\"iTrustError\">Weight must have a positive numeric value</p>";
		}
		try {
			orb.setBloodPressureH(Integer.parseInt(bps));
		} catch (NumberFormatException e) {
			errorsOV += "<p class=\"iTrustError\">Blood pressure systolic must be an integer</p>";
		}
		try {
			orb.setBloodPressureL(Integer.parseInt(bpd));
		} catch (NumberFormatException e) {
			errorsOV += "<p class=\"iTrustError\">Blood pressure diastolic must be an integer</p>";
		}
		try {
			orb.setFHR(Integer.parseInt(fhr));
		} catch (NumberFormatException e) {
			errorsOV += "<p class=\"iTrustError\">Fetal heart rate must be an integer</p>";
		}

		if (errorsOV.equals("")) {
			addAction.addObstetricsRecord(orb);
		} else {
		    errorsOV = errorOVtitle + errorsOV;
		    errorList.add(errorsOV);
		}
		
		if (errorList.size() == 0) {
			response.sendRedirect("/iTrust/auth/hcp-obstetrics/obstetricsHome.jsp?addOV");
		} else {
		    for (String errors : errorList) {
		        out.write(errors);
			}
		}
	} catch (FormValidationException e) {
		out.write("<p class=\"iTrustError\">\"" + e.getMessage() + "\"</p>");
	}

	ObstetricsRecordDAO orDAO = new ObstetricsRecordDAO(prodDAO);
	List<ObstetricsRecordBean> pastVisits = orDAO.getObstetricsRecordsByMID(chosenPatient.getMID());
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

<div id="mainpage" align=center>
	    <form action="/iTrust/auth/hcp-obstetrics/addObstetricsOfficeVisit.jsp" method="post" id="newOfficeVisit">
        <input type="hidden" name="formIsFilled" value="true" />
        <table class="fTable" align="center">
			<tr><th colspan="3">Add Obstetrics Office Visit</th></tr>
			<tr>
				<td><label for="date">Date of visit: </label></td>
				<td>
 					<input type="text" name="date" id="date" onchange="calculateWeeksPregnant();" size="10" />
 					<input type="button" value="Select Date" onclick="displayDatePicker('date');" />
				</td>
				<td width="200px" id="date-invalid"></td>
			</tr>
			<tr>
				<td><label for="lmp">Last menstrual period: </label></td>
				<td>
					<input type="text" name="lmp" id="lmp" onchange="calculateWeeksPregnant(); calculateEDD();" size="10" />
					<input type="button" value="Select Date" onclick="displayDatePicker('lmp');" />
				</td>
				<td width="200px" id="lmp-invalid"></td>
			</tr>
			<tr>
				<td><label for="edd">Estimated delivery date: </label></td>
				<td><input readonly type="text" name="edd" id="edd" size="17" /></td>
				<td></td>
			</tr>
			<tr>
				<td><label for="weeksPregnant">Weeks pregnant: </label></td>
				<td><input readonly type="text" name="weeksPregnant" id="weeksPregnant" size="13" /></td>
				<td></td>
			</tr>
			<tr>
				<td><label for="weight">Weight: </label></td>
				<td><input type="text" name="weight" id="weight" size="13" /> lbs</td>
				<td width="200px" id="weight-invalid"></td>
			</tr>
			<tr>
				<td><label>Blood Pressure: </label></td>
				<td>
					<input type="text" name="bps" id="bps" size="12" /> /
					<input type="text" name="bpd" id="bpd" size="12" /> mmHg
				</td>
				<td width="200px" id="bp-invalid"></td>
			</tr>
			<tr>
				<td><label for="fhr">Fetal Heart Rate: </label></td>
				<td><input type="text" name="fhr" id="fhr" size="13" /> bpm</td>
				<td width="200px" id="fhr-invalid"></td>
			</tr>
			<tr>
				<td><label for="twins">Multipregnant: </label></td>
				<td><input type="checkbox" name="twins" id="twins" /></td>
				<td></td>
			</tr>
			<tr>
				<td><label for="placenta">Low-lying Placenta: </label></td>
				<td><input type="checkbox" name="placenta" id="placenta" /></td>
				<td></td>
			</tr>
		</table>
        <br />
		<input type="hidden" id="ultrasound" name="ultrasound" value="0">
        <p><button class="button" type="button" id="expandUltra" onclick="expandUltrasound();updateSubmitValue();">Add Ultrasound</button></p>
        <input type="submit" id="submit" value="Submit" />
    </form>
</div>

<script type="text/javascript">
function expandUltrasound() {
	// document.getElementById("ultrasound").value = 1;
    if (typeof expandUltrasound.counter == 'undefined') {
        expandUltrasound.counter = 0;
    }
	var strs = [];
    var i = expandUltrasound.counter;

	strs.push("<br /><h2>Add Ultrasound</h2><table class='fTable' id='ultrasoundTable" + i + "'>");
    strs.push("<tr><th colspan='3'>Ultrasound " + (i + 1) + "</th></tr>");

	strs.push(
		"<tr>\n",
		"<td><label for=\"fid\">Fetus' ID: </label></td>\n",
		"<td><input type='text' name='fid" + i + "'size=\"10\" /></td>\n",
		"<td width=\"200px\"></td>\n",
        "</tr>\n"
	);
	
	strs.push(
			"<tr>\n",
			"<td><label for=\"crl\">Crown Rump Length: </label></td>\n",
        	"<td><input type='text' name='crl" + i + "'/></td>",
			"<td width=\"200px\"></td>\n",
	        "</tr>\n"
	);
	
	strs.push(
			"<tr>\n",
			"<td><label for=\"bd\">Biparieta Diameter: </label></td>\n",
        	"<td><input type='text' name='bd" + i + "'/></td>",
			"<td width=\"200px\"></td>\n",
	        "</tr>\n"
	);
	
	strs.push(
			"<tr>\n",
			"<td><label for=\"hc\">Head Circumference: </label></td>\n",
        	"<td><input type='text' name='hc" + i + "'/></td>",
			"<td width=\"200px\"></td>\n",
	        "</tr>\n"
	);
	
	strs.push(
			"<tr>\n",
			"<td><label for=\"fl\">Femur Length: </label></td>\n",
        	"<td><input type='text' name='fl" + i + "'/></td>",
			"<td width=\"200px\"></td>\n",
	        "</tr>\n"
	);
	
	strs.push(
			"<tr>\n",
			"<td><label for=\"od\">Occipitofrontal Diameter: </label></td>\n",
        	"<td><input type='text' name='od" + i + "'/></td>",
			"<td width=\"200px\"></td>\n",
	        "</tr>\n"
	);
	
	strs.push(
			"<tr>\n",
			"<td><label for=\"ac\">Abdominal Circumference: </label></td>\n",
        	"<td><input type='text' name='ac" + i + "'/></td>",
			"<td width=\"200px\"></td>\n",
	        "</tr>\n"
	);
	
	strs.push(
			"<tr>\n",
			"<td><label for=\"hl\">Humerus Length: </label></td>\n",
        	"<td><input type='text' name='hl" + i + "'/></td>",
			"<td width=\"200px\"></td>\n",
	        "</tr>\n"
	);
	
	strs.push(
			"<tr>\n",
			"<td><label for=\"efw\">Estimated Fetal Weight: </label></td>\n",
        	"<td><input type='text' name='efw" + i + "'/></td>",
			"<td width=\"200px\"></td>\n",
	        "</tr>\n"
	);
	
	
	strs.push(
			"<tr>\n",
			"<td><label for=\"up\">Ultrasound Photo: </label></td>\n",
        	"<td><input type='file' name='up" + i + "'/></td>",
			"<td width=\"200px\"></td>\n",
	        "</tr>\n"
	);
	
	//end of table
    strs.push(
        "</table>\n",
        "<br />\n"
    );
	
	var newTable = document.createElement("div");
    newTable.innerHTML = strs.join("");
    var usDiv = document.getElementById("ultrasound");
    var parent = usDiv.parentNode;
    parent.insertBefore(newTable, usDiv);
    expandUltrasound.counter++;

}
function updateSubmitValue() {
	if (typeof expandUltrasound.counter == 'undefined') {
        document.getElementById("ultrasound").value  = 0;
    } else {
        document.getElementById("ultrasound").value = expandUltrasound.counter;
    }
}
function datePickerClosed() {
    var lmp = document.getElementById("lmp");
    if (lmp.value != "") {
        calculateEDD();
        calculateWeeksPregnant();
    }
}
function calculateEDD() {
    var lmp = document.getElementById("lmp");
    var valid = false;
    if (lmp.value != "") {
        var lmpDate = new Date(lmp.value);
        if (!isNaN(lmpDate.getTime())) {
            var eddDate = new Date(lmpDate.getTime() + (1000 * 280 * 24 * 60 * 60));
            if (!isNaN(eddDate.getTime())) {
                var edd = (eddDate.getMonth()+1) + "/" + eddDate.getDate() + "/" + eddDate.getFullYear();
                document.getElementById("edd").value = edd;
                valid = true;
            }
        }
    }
    if (!valid) {
        document.getElementById("lmp-invalid").innerHTML = "<span class=\"iTrustError\">Invalid LMP</span>";
    }
}
function calculateWeeksPregnant() {
    var lmp = document.getElementById("lmp");
    var curr = document.getElementById("date");
    if (lmp.value != "" && curr.value != "") {
        var lmpDate = new Date(lmp.value);
        var currDate = new Date(curr.value);
        if (!isNaN(lmpDate.getTime()) && !isNaN(currDate.getTime())) {
            // reset invalid
            document.getElementById("lmp-invalid").innerHTML = "";
            document.getElementById("date-invalid").innerHTML = "";

            var pregDays = (currDate.getTime() - lmpDate.getTime()) / 1000 / 24 / 60 / 60;
            var pregWeeks = ~~(pregDays / 7);
            var pregDays = ~~(pregDays % 7);
            var pregWeeksDays = pregWeeks;
            if (pregWeeks > 1) {
                pregWeeksDays += " weeks ";
            } else {
                pregWeeksDays += " week ";
            }
            pregWeeksDays += pregDays;
            if (pregDays > 1) {
                pregWeeksDays += " days";
            } else {
                pregWeeksDays += " day";
            }
            document.getElementById("weeksPregnant").value = pregWeeksDays;
        } else {
            if (isNaN(lmpDate.getTime())) {
                document.getElementById("lmp-invalid").innerHTML = "<span class=\"iTrustError\">Invalid LMP</span>";
            } else {
                document.getElementById("lmp-invalid").innerHTML = "";
            }
            if (isNaN(dateDate.getTime())) {
                document.getElementById("date-invalid").innerHTML = "<span class=\"iTrustError\">Invalid Date</span>";
            } else {
                document.getElementById("date-invalid").innerHTML = "";
            }
        }
    } else {
        if (lmp.value == "") {
            document.getElementById("lmp-invalid").innerHTML = "<span class=\"iTrustError\">Invalid LMP</span>";
        } 
        if (curr.value == "") {
            document.getElementById("date-invalid").innerHTML = "<span class=\"iTrustError\">Invalid Date</span>";
        } 
    }
}
</script>

<p><br/><a href="/iTrust/auth/hcp-obstetrics/obstetricsHome.jsp">Back to Home</a></p>
<%@include file="/footer.jsp" %>