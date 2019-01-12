<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="edu.ncsu.csc.itrust.action.ViewPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditChildbirthAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ChildbirthRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Gender"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.ChildbirthVisitType"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.DeliveryType"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<%@include file="/global.jsp" %>

<%pageTitle = "iTrust - Edit Childbirth Hospital Visit";%>

<%@include file="/header.jsp" %>
<itrust:patientNav thisTitle="Edit Childbirth Hospital Visit" />

<%
    String pidString = (String) session.getAttribute("pid");
    if (pidString == null || pidString.equals("") || 1 > pidString.length()) {
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-obstetrics/editObstetricsOfficeVisit.jsp");
        return;
    }
    ViewPatientAction patientAction = new ViewPatientAction(prodDAO, loggedInMID, pidString);
    PatientBean chosenPatient = patientAction.getPatient(pidString);
    if (chosenPatient == null || !chosenPatient.getGender().equals(Gender.Female)) {
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp-obstetrics/editObstetricsOfficeVisit.jsp");
        throw new ITrustException("The patient is not eligible for obstetrics care."); //this shows up
    }
    ViewPersonnelAction personnelAction = new ViewPersonnelAction(prodDAO, loggedInMID);
    PersonnelBean currentPersonnel = personnelAction.getPersonnel("" + loggedInMID);
    if (!currentPersonnel.getSpecialty().equalsIgnoreCase("ob/gyn")) {
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp-obstetrics/editObstetricsOfficeVisit.jsp");
        throw new ITrustException("Only HCPs with a specialization of OB/GYN may create a new obstetrics record");
    }

    EditChildbirthAction editAction = new EditChildbirthAction(prodDAO, loggedInMID);
    long oid = Long.parseLong(request.getParameter("oid"));
    String pName = request.getParameter("pName");
    String dov = request.getParameter("dov");
    String scheduleType = request.getParameter("vt");
    String pdt = request.getParameter("pdt");
    String pc = request.getParameter("pc");
    String no = request.getParameter("no");
    String pd = request.getParameter("pd");
    String ea = request.getParameter("ea");
    String ms = request.getParameter("ms");
    String rhig = request.getParameter("rhig");
    String dod = request.getParameter("dod");
    if ("true".equals(request.getParameter("formIsFilled"))) {
        try {
        	String errorTitle = "<p class=\"iTrustError\">Childbirth Hospital Visit Record failed to be updated:</p>";
            String errors = "";

            ChildbirthRecordBean crb = new ChildbirthRecordBean();
            crb.setOid(oid);
            System.out.println(oid);
            crb.setMid(Long.parseLong(pidString));
            System.out.println(Long.parseLong(pidString));
            crb.setVisitDate(dov);
            System.out.println(dov);
            crb.setChildbirthVisitType(scheduleType.equals("emergency")? ChildbirthVisitType.ER: ChildbirthVisitType.PreScheduled);
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
                errors += "Dosage of Pitocin must be a double";
            }
            try {
                if (no != null && no.length() > 0) {
                    crb.setNitrousOxide(true);
                    crb.setNitrousOxideDosage(Double.parseDouble(no));
                }
            } catch (NumberFormatException e) {
                errors += "Dosage of NitrousOxide must be a double";
            }
            try {
                if (pd != null && pd.length() > 0) {
                    crb.setPethidine(true);
                    crb.setPethidineDosage(Double.parseDouble(pd));
                }
            } catch (NumberFormatException e) {
                errors += "Dosage of Pethidine must be a double";
            }
            try {
                if (ea != null && ea.length() > 0) {
                    crb.setEpiduralAnaesthesia(true);
                    crb.setEpiduralAnaesthesiaDosage(Double.parseDouble(ea));
                }
            } catch (NumberFormatException e) {
                errors += "Dosage of Epidural Anaesthesia must be a double";
            }
            try {
                if (ms != null && ms.length() > 0) {
                    crb.setMagnesiumSulfate(true);
                    crb.setMagnesiumSulfateDosage(Double.parseDouble(ms));
                }
            } catch (NumberFormatException e) {
                errors += "Dosage of Magnesium Sulfate must be a double";
            }
            try {
                if (rhig != null && rhig.length() > 0) {
                    crb.setRhImmuneGlobulin(true);
                    crb.setRhImmuneGlobulinDosage(Double.parseDouble(rhig));
                }
            } catch (NumberFormatException e) {
                errors += "Dosage of RH Immune Globulin must be a double";
            }
            if (dod != null && dod.length() > 0)
                crb.setDeliveryTime(dod);

            if (errors.equals("")) {
                editAction.editChildbirthRecord(crb);
                response.sendRedirect("/iTrust/auth/hcp-obstetrics/obstetricsHome.jsp?editCHV");
            } else {
                out.write(errorTitle + errors);
            }
        } catch (FormValidationException e) {
            out.write("<p class=\"iTrustError\">\"" + e.getMessage() + "\"</p>");
        }
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
    <form action="/iTrust/auth/hcp-obstetrics/editChildbirthHospitalVisit.jsp" method="post" id="childbirthhospitalvisit">
        <input type="hidden" name="formIsFilled" value="true" />
        <input type="hidden" name="oid" value="<%=oid%>">
        <table class="fTable" align="center">
            <tr><th colspan="3">Edit Childbirth Hospital Visit</th></tr>
            <tr>
                <td width="200px"><label>Patient Name:</label></td>
                <td width="250px"><%=pName%></td>
                <td></td>
            </tr>
            <tr>
                <td><label for="dov">Date of visit: </label></td>
                <td><input type="text" name="dov" id="dov" size="17" value="<%=dov%>" readonly/></td>
                <td width="200px" id="dov-invalid"></td>
            </tr>
            <tr>
                <td><label>Schedule Type: </label></td>
                <td>
                    <%
                        if (scheduleType.equals("Pre-scheduled")) {
                            out.write(
                                    "<input type=\"radio\" name=\"vt\" value=\"emergency\"> Emergency "
                                            + "<input type=\"radio\" name=\"vt\" value=\"preschedule\" checked> Preschedule"
                            );
                        } else {
                            out.write(
                                    "<input type=\"radio\" name=\"vt\" value=\"emergency\" checked> Emergency "
                                            + "<input type=\"radio\" name=\"vt\" value=\"preschedule\"> Preschedule"
                            );
                        }
                    %>
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
                <td><input type="text" name="pc" id="pc" size="13" value="<%=pc%>"> mg</td>
                <td width="200px"></td>
            </tr>
            <tr>
                <td><label for="no">Nitrous oxide: </label></td>
                <td><input type="text" name="no" id="no" size="13" value="<%=no%>"> mg</td>
                <td width="200px"></td>
            </tr>
            <tr>
                <td><label for="pd">Pethidine: </label></td>
                <td><input type="text" name="pd" id="pd" size="13" value="<%=pd%>"> mg</td>
                <td width="200px"></td>
            </tr>
            <tr>
                <td><label for="ea">Epidural anaesthesia: </label></td>
                <td><input type="text" name="ea" id="ea" size="13" value="<%=ea%>"> mg</td>
                <td width="200px"></td>
            </tr>
            <tr>
                <td><label for="ms">Magnesium sulfate: </label></td>
                <td><input type="text" name="ms" id="ms" size="13" value="<%=ms%>"> mg</td>
                <td width="200px"></td>
            </tr>
            <tr>
                <td><label for="rhig">RH immune globulin: </label></td>
                <td><input type="text" name="rhig" id="rhig" size="13" value="<%=rhig%>"> mg</td>
                <td width="200px"></td>
            </tr>
            <tr>
                <td><label for="dod">Date of delivery: </label></td>
                <td>
                    <input type="text" name="dod" id="dod" size="17" />
                    <input type="button" value="Select Date" onclick="displayDatePicker('dod');" />
                </td>
                <td width="200px" id="dod-invalid"></td>
            </tr>
        </table>
        <br />
        <input type="submit" id="submit" value="Submit" />
    </form>
</div>

<%@include file="/footer.jsp" %>