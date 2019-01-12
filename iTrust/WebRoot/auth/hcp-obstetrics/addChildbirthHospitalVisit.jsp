<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList" %>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ChildbirthRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ChildbirthRecordDAO"%>>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Gender"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.DeliveryType"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.ChildbirthVisitType"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@ page import="edu.ncsu.csc.itrust.action.*" %>

<%@include file="/global.jsp" %>

<%pageTitle = "iTrust - Add Childbirth Hospital Visit";%>

<%@include file="/header.jsp" %>
<itrust:patientNav thisTitle="Add Childbirth Hospital Visit" />
<%
    String pidString = (String) session.getAttribute("pid");
    if (pidString == null || pidString.equals("") || 1 > pidString.length()) {
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-obstetrics/addChildbirthHospitalVisit.jsp");
        return;
    }
    ViewPatientAction patientAction = new ViewPatientAction(prodDAO, loggedInMID, pidString);
    PatientBean chosenPatient = patientAction.getPatient(pidString);
    if (chosenPatient == null || !chosenPatient.getGender().equals(Gender.Female)) {
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp-obstetrics/addChildbirthHospitalVisit.jsp");
        throw new ITrustException("The patient is not eligible for obstetrics care.");
    }
    ViewPersonnelAction personnelAction = new ViewPersonnelAction(prodDAO, loggedInMID);
    PersonnelBean currentPersonnel = personnelAction.getPersonnel("" + loggedInMID);
    if (!currentPersonnel.getSpecialty().equalsIgnoreCase("ob/gyn")) {
        // the following line later to direct to the view record page
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp-obstetrics/addObstetricsRecord.jsp");
        throw new ITrustException("Only HCPs with a specialization of OB/GYN may create a new obstetrics record");
    }
    AddChildbirthAction addAction = new AddChildbirthAction(prodDAO, loggedInMID);
    if ("true".equals(request.getParameter("formIsFilled"))) {
        String dov = request.getParameter("dov");
        String scheduleType = request.getParameter("scheduleType");
        String pdt = request.getParameter("pdt");
        String pc = request.getParameter("pc");
        String no = request.getParameter("no");
        String pd = request.getParameter("pd");
        String ea = request.getParameter("ea");
        String ms = request.getParameter("ms");
        String rhig = request.getParameter("rhig");
        String dod = request.getParameter("dod");
        int childrenCnt = Integer.parseInt(request.getParameter("children"));
        List<String> errorList = new ArrayList<String>();
        try {
            for (int i = 0; i < childrenCnt; i++) {
                String errors = "";
                String errortitle = "<p class=\"iTrustError\">Child " + (i + 1) + " cannot be added:</p>";

                String sex = request.getParameter("sex" + i);
                String firstname = request.getParameter("firstname" + i);
                String lastname = request.getParameter("lastname" + i);
                String email = request.getParameter("email" + i);
                AddPatientAction apAction = new AddPatientAction(prodDAO, loggedInMID);
                PatientBean pBean = new PatientBean();
                if (sex != null) {
                    pBean.setGender(sex.equals("f") ? Gender.Female : Gender.Male);
                } else {
                    errors += "<p class=\"iTrustError\">Sex type cannot be null</p>";
                }

                if (firstname != null && firstname.length() > 0) {
                    pBean.setFirstName(firstname);
                } else {
                    errors += "<p class=\"iTrustError\">First name cannot be null</p>";
                }
                if (lastname != null && lastname.length() > 0) {
                    pBean.setLastName(lastname);
                } else {
                    errors += "<p class=\"iTrustError\">Last name cannot be null</p>";
                }
                if (email != null && email.length() > 0) {
                    pBean.setEmail(email);
                } else {
                    errors += "<p class=\"iTrustError\">Email cannot be null</p>";
                }
                if (errors.equals("")) {
                    apAction.addPatient(pBean, loggedInMID);
                } else {
                    errors = errortitle + errors;
                    errorList.add(errors);
                }

            }

            String errorsHV = "";
            String errorHVtitle = "<p class=\"iTrustError\">Childbirth Hospital Visit cannot be added:</p>";

            ChildbirthRecordBean crb = new ChildbirthRecordBean();
            crb.setMid(Long.parseLong(pidString));

            if (dov != null && !dov.equals("")) {
                crb.setVisitDate(dov);
            } else {
                errorsHV += "<p class=\"iTrustError\">Date of Visit cannot be null</p>";
            }

            if (scheduleType != null) {
                crb.setChildbirthVisitType(scheduleType.equals("emergency")? ChildbirthVisitType.ER: ChildbirthVisitType.PreScheduled);
            } else {
                errorsHV += "<p class=\"iTrustError\">Schedule type cannot be null</p>";
            }

            if (pdt.equals("vd")) crb.setDeliveryType(DeliveryType.Vaginal);
            else if (pdt.equals("vdva")) crb.setDeliveryType(DeliveryType.VaginalVA);
            else if (pdt.equals("vdfa")) crb.setDeliveryType(DeliveryType.VaginalFA);
            else if (pdt.equals("cs")) crb.setDeliveryType(DeliveryType.Caesarean);
            else if (pdt.equals("ms")) crb.setDeliveryType(DeliveryType.Miscarriage);
            try {
                if (pc != null && pc.length() > 0) {
                    crb.setPitocin(true);
                    crb.setPitocinDosage(Double.parseDouble(pc));
                }
            } catch (NumberFormatException e) {
                errorsHV += "<p class=\"iTrustError\">Dosage of Pitocin must be a double</p>";
            }
            try {
                if (no != null && no.length() > 0) {
                    crb.setNitrousOxide(true);
                    crb.setNitrousOxideDosage(Double.parseDouble(no));
                }
            } catch (NumberFormatException e) {
                errorsHV += "<p class=\"iTrustError\">Dosage of NitrousOxide must be a double</p>";
            }
            try {
                if (pd != null && pd.length() > 0) {
                    crb.setPethidine(true);
                    crb.setPethidineDosage(Double.parseDouble(pd));
                }
            } catch (NumberFormatException e) {
                errorsHV += "<p class=\"iTrustError\">Dosage of Pethidine must be a double</p>";
            }
            try {
                if (ea != null && ea.length() > 0) {
                    crb.setEpiduralAnaesthesia(true);
                    crb.setEpiduralAnaesthesiaDosage(Double.parseDouble(ea));
                }
            } catch (NumberFormatException e) {
                errorsHV += "<p class=\"iTrustError\">Dosage of Epidural Anaesthesia must be a double</p>";
            }
            try {
                if (ms != null && ms.length() > 0) {
                    crb.setMagnesiumSulfate(true);
                    crb.setMagnesiumSulfateDosage(Double.parseDouble(ms));
                }
            } catch (NumberFormatException e) {
                errorsHV += "<p class=\"iTrustError\">Dosage of Magnesium Sulfate must be a double</p>";
            }
            try {
                if (rhig != null && rhig.length() > 0) {
                    crb.setRhImmuneGlobulin(true);
                    crb.setRhImmuneGlobulinDosage(Double.parseDouble(rhig));
                }
            } catch (NumberFormatException e) {
                errorsHV += "<p class=\"iTrustError\">Dosage of RH Immune Globulin must be a double</p>";
            }
            if (dod != null && dod.length() > 0)
                crb.setDeliveryTime(dod);

            if (errorsHV.equals("")) {
                addAction.addChildbirthRecord(crb);
            } else {
                errorsHV = errorHVtitle + errorsHV;
                errorList.add(errorsHV);
            }
            
            if (errorList.size() == 0) {
                response.sendRedirect("/iTrust/auth/hcp-obstetrics/obstetricsHome.jsp?addCHV");
            } else {
                for (String errors : errorList) {
                    out.write(errors);
                }
            }

        } catch (FormValidationException e) {
            out.write("<p class=\"iTrustError\">\"" + e.getMessage() + "\"</p>");
        }
        ChildbirthRecordDAO crDAO = new ChildbirthRecordDAO(prodDAO);
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

<div id="mainpage" align="center">
    <form action="/iTrust/auth/hcp-obstetrics/addChildbirthHospitalVisit.jsp" method="post" id="childbirthhospitalvisit">
        <input type="hidden" name="formIsFilled" value="true" />
        <table class="fTable" align="center">
            <tr><th colspan="3">Add Childbirth Hospital Visit</th></tr>
            <tr>
                <td><label for="dov">Date of visit: </label></td>
                <td>
                    <input type="text" name="dov" id="dov" size="10" />
                    <input type="button" value="Select Date" onclick="displayDatePicker('dov');" />
                </td>
                <td width="200px" id="dov-invalid"></td>
            </tr>
            <tr>
                <td><label>Schedule Type: </label></td>
                <td>
                    <input type="radio" name="scheduleType" value="emergency"> Emergency
                    <input type="radio" name="scheduleType" value="preschedule"> Preschedule
                </td>
                <td width="200px"></td>
            </tr>
            <tr>
                <td><label for="pdt">Preferred Delivery Type</label></td>
                <td>
                    <%-- use document.getElementById("pdt").selectedOptions[0].value to get option value --%>
                    <select name="pdt" id="pdt">
                        <option value="vd">Vaginal Delivery</option>
                        <option value="vdva">Vaginal Delivery Vacuum Assist</option>
                        <option value="vdfa">Vaginal Delivery Forceps Assist</option>
                        <option value="cs">Caesarean Section</option>
                        <option value="mc">Miscarriage</option>
                    </select>
                </td>
                <td width="200px"></td>
            </tr>
            <tr>
                <td><b>Drugs and Dosage:</b></td>
                <td width="200px" id="drugs-invalid"></td>
            </tr>
            <tr>
                <td><label for="pc">Pitocin: </label></td>
                <td><input type="text" name="pc" id="pc" size="13"> mg</td>
                <td width="200px"></td>
            </tr>
            <tr>
                <td><label for="no">Nitrous oxide: </label></td>
                <td><input type="text" name="no" id="no" size="13"> mg</td>
                <td width="200px"></td>
            </tr>
            <tr>
                <td><label for="pd">Pethidine: </label></td>
                <td><input type="text" name="pd" id="pd" size="13"> mg</td>
                <td width="200px"></td>
            </tr>
            <tr>
                <td><label for="ea">Epidural anaesthesia: </label></td>
                <td><input type="text" name="ea" id="ea" size="13"> mg</td>
                <td width="200px"></td>
            </tr>
            <tr>
                <td><label for="ms">Magnesium sulfate: </label></td>
                <td><input type="text" name="ms" id="ms" size="13"> mg</td>
                <td width="200px"></td>
            </tr>
            <tr>
                <td><label for="rhig">RH immune globulin: </label></td>
                <td><input type="text" name="rhig" id="rhig" size="13"> mg</td>
                <td width="200px"></td>
            </tr>
            <tr>
                <td><label for="dod">Date of delivery: </label></td>
                <td>
                    <input type="text" name="dod" id="dod" size="10" />
                    <input type="button" value="Select Date" onclick="displayDatePicker('dod');" />
                </td>
                <td width="200px" id="dod-invalid"></td>
            </tr>
        </table>
        <br />
        <input type="hidden" id="children" name="children" value="0">
        <p><button class="button" type="button" id="expandChild" onclick="expandChildren(); updateSubmitValue();">Add Children</button></p>
        <input type="submit" id="submit" value="Submit" />
    </form>
</div>

<script type="text/javascript">
    function expandChildren() {
        if (typeof expandChildren.counter == 'undefined')
            expandChildren.counter = 0;
        var strs = [];
        var i = expandChildren.counter;
        strs.push("<br /><h2>Add a child</h2><table class='fTable' id='childrenTable" + i + "'>");
        strs.push("<tr><th colspan='3'>New Child " + (i + 1) + "</th></tr>");
        strs.push(
            "<tr>" +
                "<td><label>First Name</label></td>" +
                "<td><input type='text' name='firstname" + i + "'/></td>" +
                "<td width='200px'></td>" +
            "</tr>"
        );
        strs.push(
            "<tr>" +
            "<td><label>Last Name</label></td>" +
            "<td><input type='text' name='lastname" + i + "'/></td>" +
            "<td width='200px'></td>" +
            "</tr>"
        );
        strs.push(
            "<tr>" +
                "<td><label>Email</label></td>" +
                "<td><input type='email' name='email" + i + "'/></td>" +
                "<td width='200px'></td>" +
            "</tr>"
        );
        strs.push(
            "<tr>" +
                "<td><label>Sex</label></td>" +
                "<td><input type='radio' name='sex" + i + "' value='f' />Female <input type='radio' name='sex" + i + "' value='m' />Male</td>" +
                "<td width='200px'></td>" +
            "</tr>"
        );
        strs.push("</table><br />");
        var newTable = document.createElement("div");
        newTable.innerHTML = strs.join("");
        var childrnDiv = document.getElementById("children");
        var parent = childrnDiv.parentNode;
        parent.insertBefore(newTable, childrnDiv);
        expandChildren.counter++;
    }
    function updateSubmitValue() {
        if (typeof expandChildren.counter == 'undefined') {
            document.getElementById("children").value  = 0;
        } else {
            document.getElementById("children").value = expandChildren.counter;
        }
    }
</script>

<p><br/><a href="/iTrust/auth/hcp-obstetrics/obstetricsHome.jsp">Back to Home</a></p>
<%@include file="/footer.jsp" %>