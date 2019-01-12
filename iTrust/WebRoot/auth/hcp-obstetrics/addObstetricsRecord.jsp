<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList" %>
<%@page import="edu.ncsu.csc.itrust.action.ViewPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewObstetricsAction"%>
<%@page import="edu.ncsu.csc.itrust.action.AddObstetricsAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricsRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Gender"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.DeliveryType"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.PregnancyStatus"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<%@include file="/global.jsp" %>

<%pageTitle = "iTrust - Initialize Obstetrics Record";%>

<%@include file="/header.jsp" %>
<itrust:patientNav thisTitle="Obstetrics Initialization" />

<%
    /* Require a Patient ID first */
    String pidString = (String) session.getAttribute("pid");
    if (pidString == null || pidString.equals("") || 1 > pidString.length()) {
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-obstetrics/addObstetricsRecord.jsp");
        return;
    }

    // check if the selected patient is female, if not, throw an exception
    ViewPatientAction patientAction = new ViewPatientAction(prodDAO, loggedInMID, pidString);
    PatientBean chosenPatient = patientAction.getPatient(pidString);
    if (chosenPatient == null || !chosenPatient.getGender().equals(Gender.Female)) {
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp-obstetrics/addObstetricsRecord.jsp");
        throw new ITrustException("The patient is not eligible for obstetrics care."); //this shows up
    }

    // check if the HCP is ob/gyn HCP, if not, throw an exception
    ViewPersonnelAction personnelAction = new ViewPersonnelAction(prodDAO, loggedInMID);
    PersonnelBean currentPersonnel = personnelAction.getPersonnel("" + loggedInMID);
    if (!currentPersonnel.getSpecialty().equalsIgnoreCase("ob/gyn")) {
        // the following line later to direct to the view record page
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp-obstetrics/addObstetricsRecord.jsp");
        throw new ITrustException("Only HCPs with a specialization of OB/GYN may create a new obstetrics record");
    }


    AddObstetricsAction addAction = new AddObstetricsAction(prodDAO, loggedInMID);

    // Run the following code after the form is submitted
    if ("true".equals(request.getParameter("formIsFilled"))) {
        // get the number of added prior pregnancies
        int priorNum = Integer.parseInt(request.getParameter("priorPregnancies"));
        List<String> errorList = new ArrayList<String>();
        try {
            // Add prior pregnancy records if any
            for (int i = 0; i < priorNum; i++) {
                String errors = "";
                String errortitle = "<p class=\"iTrustError\">Prior Pregnancy " + (i + 1) + " cannot be added:</p>";

                // get user-input year conception
                int yearConception = 0;
                try {
                    yearConception = Integer.parseInt(request.getParameter("yearConception" + i));
                } catch (NumberFormatException e) {
                    errors += "<p class=\"iTrustError\">Year conception must be an integer</p>";
                }
                
                // get user-input Weeks-Days Pregnant
                String weeksPregnant = request.getParameter("weeksPregnant" + i);
                String daysPregnant = request.getParameter("daysPregnant" + i);
                String weeksdaysPreg = weeksPregnant + "-" + daysPregnant;

                // get user-input Hours in labor
                double hrsInLabor = 0;
                try {
                    hrsInLabor = Double.parseDouble(request.getParameter("hoursInLabor" + i));
                } catch (NumberFormatException e) {
                    errors += "<p class=\"iTrustError\">Hours in labor must be a double</p>";
                    
                }

                // get weight gain
                double weightGain = 0;
                try {
                    weightGain = Double.parseDouble(request.getParameter("weightGain" + i));
                } catch (NumberFormatException e) {
                    errors += "<p class=\"iTrustError\">Weight Gain must be a double</p>";
                }

                // get user-input Delivery type
                DeliveryType delivery = DeliveryType.fromString(request.getParameter("deliveryType" + i));

                // get multipregnancy check
                boolean multipregnancy = false;
                if (request.getParameter("multipregnancy" + i) != null && request.getParameter("multipregnancy" + i).equals("on")) {
                    multipregnancy = true;
                }

                // get babyc count
                int babyCount = 1;
                String babyCountStr = request.getParameter("babyCount" + i);
                if (babyCountStr != null && !babyCountStr.equals("")) {
                    try {
                        babyCount = Integer.parseInt(babyCountStr);
                    } catch (NumberFormatException e) {
                        errors += "<p class=\"iTrustError\">Baby count must be an integer</p>";
                    }
                }
                /* System.out.println(errors); */
                if (errors.equals("")) {
                    ObstetricsRecordBean prior = new ObstetricsRecordBean();
                    prior.setMid(Long.parseLong(pidString));
                    prior.setYearConception(yearConception);
                    prior.setWeeksPregnant(weeksdaysPreg);
                    prior.setHoursInLabor(hrsInLabor);
                    prior.setWeightGain(weightGain);
                    prior.setDeliveryType(delivery);
                    prior.setPregStatus(PregnancyStatus.Complete);
                    prior.setMultiPregnancy(multipregnancy);
                    prior.setBabyCount(babyCount);
                    addAction.addObstetricsRecord(prior);
                } else {
                    errors = errortitle + errors;
                	errorList.add(errors);
                }
            }
            // Add initial record
            String errorsOB = "";
            String errorOBtitle = "<p class=\"iTrustError\">Initial Obstetrics Record cannot be added:</p>";

            ObstetricsRecordBean initial = new ObstetricsRecordBean();
            initial.setMid(Long.parseLong(pidString));
            String curr = request.getParameter("curr");
            String lmp = request.getParameter("lmp");
            if (curr != null && !curr.equals("") && lmp != null && !lmp.equals("")) {
            	initial.setinitDate(request.getParameter("curr"));
            	initial.setLMP(request.getParameter("lmp"));
            	initial.setEDD(request.getParameter("edd"));
            	String[] arr = request.getParameter("weeksdaysPregnant").split(" ");
            	if (arr.length >= 2) {
           			initial.setWeeksPregnant((arr[0] + "-" + arr[2]));
            	}
            	initial.setPregStatus(PregnancyStatus.Initial);
            	initial.setDeliveryType(DeliveryType.Vaginal);
            	addAction.addObstetricsRecord(initial);
            } else {
                if (curr == null || curr.equals("")) {
                    errorsOB += "<p class=\"iTrustError\">Today's Date cannot be empty</p>";
                }
                if (lmp == null || lmp.equals("")) {
                    errorsOB += "<p class=\"iTrustError\">Last LMP Date cannot be empty</p>";
                }
            }
            if (!errorsOB.equals("")) {
                errorsOB = errorOBtitle + errorsOB;
                errorList.add(errorsOB);
            }
            
            if (errorList.size() == 0) {
            	response.sendRedirect("/iTrust/auth/hcp-obstetrics/obstetricsHome.jsp?initial");
            } else {
                for (String errors : errorList) {
                    out.write(errors);
                }
            }
        } catch(FormValidationException e) {
            out.write("<p class=\"iTrustError\">" + e.getMessage() + "</p>");
        }
       
    }

%>
<style>
.button {
    border-radius: 4px;
    background-color: rgb(248, 248, 248);
}
</style>

<div id="mainpage" align=center>
    <form action="/iTrust/auth/hcp-obstetrics/addObstetricsRecord.jsp" method="post" id="newRecord">
        <input type="hidden" name="formIsFilled" value="true" />
        <table class="fTable" align="center">
            <tr><th colspan="3">Initialize New Pregnancy</th></tr>
            <tr>
                <td width="200px"><label for="curr">Today's Date: </label></td>
                <td width="200px">
                    <input type="text" name="curr" id="curr" onchange="caculateWeeksPregnant();" size="10" />
                    <input type="button" value="Select Date" onclick="displayDatePicker('curr');" />
                </td>
                <td width="200px" id="date-invalid"></td>
            </tr>
            <tr>
                <td><label for="lmp">Last menstrual period: </label></td>
                <td>
                    <input type="text" name="lmp" id="lmp" onchange="caculateWeeksPregnant();calculateEDD();" size="10" />
                    <input type="button" value="Select Date" onclick="displayDatePicker('lmp');" />
                </td>
                <td id="lmp-invalid" width="200px"></td>
            </tr>
            <tr>
                <td><label for="weeksPregnant">Weeks-Days pregnant: </label></td>
                <td><input readonly type="text" name="weeksdaysPregnant" id="weeksdaysPregnant" /></td>
                <td></td>
            </tr>
            <tr>
                <td><label for="edd">Estimated delivery date: </label></td>
                <td><input readonly type="text" name="edd" id="edd" /></td>
                <td></td>
            </tr>
        </table>
<!-- Show any past preganancies already existing iun the system here -->
<%
    ViewObstetricsAction viewObstetrics = new ViewObstetricsAction(prodDAO, loggedInMID);
    List<ObstetricsRecordBean> priorPregnancies = viewObstetrics.getPriorPregnancyObstetricsRecordByMID(Long.parseLong(pidString));
    if (priorPregnancies != null && !priorPregnancies.isEmpty()) {
        // Create the table of past pregnancies
        StringBuilder history = new StringBuilder();
        history.append("<br /><h2 align=\"center\">Prior Pregnancy Records</h2><table class=\"fTable\" align=\"center\">")
                .append("<tr>")
                .append("<th>Year of Conception</th>")
                .append("<th>Weeks-Days Pregnant</th>")
                .append("<th>Hours in Labor</th>")
                .append("<th>Delivery Type</th>")
                .append("</tr>");
        int count = 0;
        for (ObstetricsRecordBean bean: priorPregnancies) {
            if (bean != null) {
                history.append("<tr>")
                        .append("<td>").append(bean.getYearConception()).append("</td>")
                        .append("<td>").append(bean.getWeeksPregnant()).append("</td>")
                        .append("<td>").append(bean.getHoursInLabor()).append("</td>")
                        .append("<td>").append(bean.getDeliveryType().toString()).append("</td>")
                        .append("</tr>");
                count++;
            }
        }
        history.append("</table>");
        if (count != 0) {
            out.write(history.toString());
        }
    }
%>
        <br />
        <input type="hidden" id="priorPregnancies" name="priorPregnancies" value="0">
        <p><button class="button" type="button" id="priorPregnancy" onclick="showPriorPregnancyForm();updateSubmitValue();">Add Prior Pregnancy</button></p>
        <input type="submit" id="submit" value="Submit" />
    </form>
</div>

<script type="text/javascript">
    // call the calculate functions when date picker is closed
    function datePickerClosed() {
        var lmp = document.getElementById("lmp");
        if (lmp.value != "") {
            calculateEDD();
            caculateWeeksPregnant();
        }
    }

    // calculate the estimated delivery date
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

    // calculate the pregnant weeks
    function caculateWeeksPregnant() {
        var lmp = document.getElementById("lmp");
        var curr = document.getElementById("curr");

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
                // the format defined in ValidationFormat for weeksdaysPregnant is "NumOfWeeks-NumOfDays"
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
                document.getElementById("weeksdaysPregnant").value = pregWeeksDays;
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

    // show form for OB/GYN HCP doctors to fill the patient's prior pregnancies
    function showPriorPregnancyForm() {
        // JS functions are also objects so they van have properties
        if (typeof showPriorPregnancyForm.counter == 'undefined') {
            showPriorPregnancyForm.counter = 0;
        }
        var prior = [];
        var i = showPriorPregnancyForm.counter;

        //table header
        prior.push("<br /><h2>Add Past Pregnancies</h2><table class=\"fTable\" id=\"priorPregnancyTable" + i + "\">");
        prior.push("<tr><th colspan=\"3\">Prior Pregnancy " + (i + 1) + "</th></tr>\n");

        // year conception
        prior.push(
            "<tr>\n",
            "<td width=\"200px\"><label for=\"yearConception" + i + "\">Year of conception: </label></td>\n",
            "<td width=\"200px\"><input type=\"text\" name=\"yearConception" + i + "\" id=\"yearConception" + i + "\" maxlength=\"4\" size=\"10\" /></td>\n",
            "<td width=\"180px\"></td>\n",
            "</tr>\n"
        );

        // weeks days pregnant
        prior.push(
            "<tr>\n",
            "<td width=\"200px\"><label for=\"weeksPregnant" + i + "\">Weeks-Days pregnant: </label></td>\n",
            "<td width=\"200px\">",
            "<input type=\"text\" name=\"weeksPregnant" + i + "\" id=\"weeksPregnant" + i + "\" maxlength=\"2\" size=\"4\"/> - ",
            "<input type=\"text\" name=\"daysPregnant" + i + "\" id=\"daysPregnant" + i + "\" maxlength=\"1\" size=\"4\"/>",
            "</td>\n",
            "<td width=\"180px\"></td>\n",
            "</tr>\n"
        );

        // hours in labor
        prior.push(
            "<tr>\n",
            "<td width=\"200px\"><label for=\"hoursInLabor" + i + "\">Hours in labor: </label></td>\n",
            "<td width=\"200px\"><input type=\"text\" name=\"hoursInLabor" + i + "\" id=\"hoursInLabor" + i + "\" size=\"10\" /></td>\n",
            "<td width=\"180px\"></td>\n",
            "</tr>\n"
        );

        // weight gain
        prior.push(
            "<tr>\n",
            "<td width=\"200px\"><label for=\"weightGain" + i + "\">Weight gained: </label></td>\n",
            "<td width=\"200px\"><input type=\"text\" name=\"weightGain" + i + "\" id=\"weightGain" + i + "\" size=\"10\" /></td>\n",
            "<td width=\"180px\"></td>\n",
            "</tr>\n"
        )

        // delivery type
        prior.push(
            "<tr>\n",
            "<td width=\"200px\"><label for=\"deliveryType" + i + "\">Delivery type: </label></td>\n",
            "<td width=\"200px\">",
            "<select name=\"deliveryType" + i + "\" id=\"deliveryType" + i + "\" />"
        );
        prior.push(
            "<option value=\"Vaginal Delivery\">Vaginal Delivery</option>",
            "<option value=\"Vaginal Delivery Vacuum Assist\">Vaginal Delivery Vacuum Assist</option>",
            "<option value=\"Vaginal Delivery Forceps Assist\">Vaginal Delivery Forceps Assist</option>",
            "<option value=\"Caesarean Section\">Caesarean Section</option>",
            "<option value=\"Miscarriage\">Miscarriage</option>"
        );
        prior.push(
            "</select></td>\n",
            "<td width=\"180x\"></td>\n",
            "</tr>\n"
        );

        // multipregnancy
        prior.push(
            "<tr>\n",
            "<td width=\"200px\"><label for=\"multipregnancy" + i + "\">Multipregnancy: </label></td>\n",
            "<td width=\"200px\"><input type=\"checkbox\" name=\"multipregnancy" + i + "\" id=\"multipregnancy" + i + "\" /></td>\n",
            "<td width=\"180px\"></td>\n",
            "</tr>\n"
        );

        // badyCount
        prior.push(
            "<tr>\n",
            "<td width=\"200px\"><label for=\"babyCount" + i + "\">Baby count: </label></td>\n",
            "<td width=\"200px\"><input type=\"text\" name=\"babyCount" + i + "\" id=\"babyCount" + i + "\" maxlength=\"2\" size=\"10\" /></td>\n",
            "<td width=\"180px\"></td>\n",
            "</tr>\n"
        )

        //end of table
        prior.push(
            "</table>\n",
            "<br />\n"
        );

        var newTable = document.createElement("div");
        newTable.innerHTML = prior.join("");
        var priorDiv = document.getElementById("priorPregnancies");
        var parent = priorDiv.parentNode;
        parent.insertBefore(newTable, priorDiv);
        showPriorPregnancyForm.counter ++;
    }

    function updateSubmitValue() {
        // set the submit button's value to be the number of showPriorPregnancyForm.counter
        if (typeof showPriorPregnancyForm.counter == 'undefined') {
            document.getElementById("priorPregnancies").value  = 0;
        } else {
            document.getElementById("priorPregnancies").value = showPriorPregnancyForm.counter;
        }
    }
</script>

<p><br/><a href="/iTrust/auth/hcp-obstetrics/obstetricsHome.jsp">Back to Home</a></p>
<%@include file="/footer.jsp" %>