<%@page import="java.text.ParseException"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.*"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ApptBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ApptTypeBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricsRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.action.AddApptAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditApptTypeAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyApptsAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewObstetricsAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ApptTypeDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricsRecordDAO"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<%@include file="/global.jsp" %>

<%
	pageTitle = "iTrust - Schedule an Appointment";

String headerMessage = "Please fill out the form properly - comments are optional.";

%>

<%@include file="/header.jsp" %>
<form id="mainForm" method="post" action="scheduleAppt.jsp">
		
<%
			AddApptAction action = new AddApptAction(prodDAO, loggedInMID.longValue());
			ViewMyApptsAction viewAction = new ViewMyApptsAction(prodDAO, loggedInMID.longValue());
			EditApptTypeAction types = new EditApptTypeAction(prodDAO, loggedInMID.longValue());
			ApptTypeDAO apptTypeDAO = prodDAO.getApptTypeDAO();
			PatientDAO patientDAO = prodDAO.getPatientDAO();
			ApptTypeBean OOV = new ApptTypeBean("Obstetrics Office Visit", 60);
			ApptTypeBean CV = new ApptTypeBean("Childbrith(delivery) Visit", 300);
			List<ApptTypeBean> addNewType = types.getApptTypes();
			if (addNewType.size() == 6) {
				apptTypeDAO.addApptType(OOV);
				apptTypeDAO.addApptType(CV);
			}
			
			
			long patientID = 0L;
			boolean error = false;
			boolean invalidDate = false;
			String hidden = ""; 
			
			boolean isDead = false;
			if (session.getAttribute("pid") != null) {
				String pidString = (String) session.getAttribute("pid");
				patientID = Long.parseLong(pidString);
				try {
			action.getName(patientID);
				} catch (ITrustException ite) {
			patientID = 0L;
				}
				
				isDead = patientDAO.getPatient(patientID).getDateOfDeathStr().length()>0;
			}
			else {
				
				session.removeAttribute("pid");
			}
			
			String lastSchedDate="";
			String lastApptType="";
			String lastTime1="";
			String lastTime2="";
			String lastTime3="";
			String lastComment="";
			int week = 0;
			int DAY = 0;
			Calendar c = null;
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			
			if (patientID == 0L) {
				response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp/scheduleAppt.jsp");
			} else if(isDead){
		%>
		<div align=center>
			<span class="iTrustError">Cannot schedule appointment. This patient is deceased. Please return and select a different patient.</span>
			<br />
			<a href="/iTrust/auth/getPatientID.jsp?forward=hcp/scheduleAppt.jsp">Back</a>		</div>
		<%	
	}else{
		if (request.getParameter("schedule") != null) {
			ViewObstetricsAction vOA1 = new ViewObstetricsAction(prodDAO, loggedInMID.longValue());
			long pid = Long.parseLong((String) session.getAttribute("pid"));
			List<ObstetricsRecordBean> res1 = vOA1.getObstetricsRecordByMID(pid);
			if (res1.size() != 0) {
				ObstetricsRecordBean ob01 = res1.get(0);
				Date pri1 = ob01.getinitDate();
				String weekPregnant1 = ob01.getWeeksPregnant();
				int idx1 = weekPregnant1.indexOf("-");
				week = Integer.parseInt(weekPregnant1.substring(0, idx1));
			}
			if(!request.getParameter("schedDate").equals("")) {	
				boolean laterThan4 = false;
				boolean isWeekend = false;
				lastSchedDate = request.getParameter("schedDate");
				lastApptType = request.getParameter("apptType");
				lastTime1 = request.getParameter("time1");
				lastTime2 = request.getParameter("time2");
				lastTime3 = request.getParameter("time3");
				if (lastTime3.equals("AM")) {
					if (lastTime1.charAt(0) == '0' && lastTime1.charAt(1) < '9') laterThan4 = true;
				} 
				
				if (lastTime3.equals("PM")) {
					if (lastTime1.charAt(0) == '0' && lastTime1.charAt(1) > '4') laterThan4 = true;
					if (lastTime1.charAt(0) == '1') laterThan4 = true;
					if (lastTime1.equals("04") && Integer.parseInt(lastTime2) > 0) laterThan4 = true;
				}
    			DateFormat fmt = new SimpleDateFormat("MM/dd/yyyy");
    			Date dateWeekday = fmt.parse(lastSchedDate);
    			int year = Integer.parseInt(lastSchedDate.substring(6,10));
    			int month = Integer.parseInt(lastSchedDate.substring(0,2));
    			int day = Integer.parseInt(lastSchedDate.substring(3,5));
    			Calendar cal = new GregorianCalendar(year, month - 1, day);
    			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
    			isWeekend = (Calendar.SUNDAY == dayOfWeek || Calendar.SATURDAY == dayOfWeek);
    			if (lastApptType.contains("(") && week >= 37) {
					laterThan4 = false;
					isWeekend = false;
				}
				lastComment = request.getParameter("comment");
				if (laterThan4 || isWeekend) lastTime2 += "BREAK";
				ApptBean appt = new ApptBean();
				DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
				format.setLenient(false);
				try{				
					Date date = format.parse(lastSchedDate+" "+lastTime1+":"+lastTime2+" "+lastTime3);
					appt.setDate(new Timestamp(date.getTime()));
				}catch(ParseException e){
					invalidDate=true;
				}
				if(invalidDate==false){
					appt.setHcp(loggedInMID);
					appt.setPatient(patientID);
					appt.setApptType(lastApptType);
					String comment = "";
					boolean ignoreConflicts = false;
					if("Override".equals(request.getParameter("scheduleButton"))){
						ignoreConflicts = true;
					}
				
					if(request.getParameter("comment").equals(""))
						comment = null;
					else 
						comment = request.getParameter("comment");
					appt.setComment(comment);
					try {
						headerMessage = action.addAppt(appt, ignoreConflicts);
						if(headerMessage.startsWith("Success")) {
							session.removeAttribute("pid");
						}else{
							error = true;
							
							if (headerMessage.contains("conflict")){
								hidden = "style='display:none;'";
								List<ApptBean> conflicts = action.getConflictsForAppt(loggedInMID.longValue(), appt);
								%>
								<div align=center id="conflictTable">
									<span class="iTrustError"><%=headerMessage %></span>
									
									<table class="fancyTable">
									<tr><th>Patient</th><th>Appointment Type</th><th>Date / Time</th><th>Duration</th></tr>
									<% for( ApptBean conflict : conflicts){ 
											Date d = new Date(conflict.getDate().getTime());
									%>
									
										<tr>
											<td><%= StringEscapeUtils.escapeHtml("" + ( viewAction.getName(conflict.getPatient()) )) %></td>
											<td><%= StringEscapeUtils.escapeHtml("" + ( conflict.getApptType() )) %></td>
												<td><%= StringEscapeUtils.escapeHtml("" + ( format.format(d) )) %></td> 
							 				<td><%= StringEscapeUtils.escapeHtml("" + ( apptTypeDAO.getApptType(conflict.getApptType()).getDuration()+" minutes" )) %></td>
										</tr>
									<% }  %>
									</table>
									<input type="submit" id="overrideButton" name="scheduleButton" value="Override"/>
									<input type="button" id="cancel" name="cancel" value="Cancel" onClick="$('#apptDiv').css('display','block');$('#conflictTable').hide();"/>
								</div>
								<%
							} else {
								%>
									<div align=center>
										<span class="iTrustError"><%=headerMessage %></span>
									</div>
								<%
							}
						}
					} catch (FormValidationException e){
					%>
					<div align=center><span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage())%></span></div>
					<%	
					}
				}else{
					if (laterThan4) headerMessage = "Please schedule your appointment between 9AM to 4PM.";
					else if (isWeekend) headerMessage = "Please schedule your appointment on a weekday.";
					else headerMessage = "Please input a valid date for the appointment.";
				}
			}else{
				headerMessage = "Please input a date for the appointment.";
			}
		}
		
		List<ApptTypeBean> apptTypes = types.getApptTypes();
%>
<%
			ViewObstetricsAction vOA = new ViewObstetricsAction(prodDAO, loggedInMID.longValue());
			long pid = -1;//Long.parseLong((String) session.getAttribute("pid"));
			if (session.getAttribute("pid") != null) pid = Long.parseLong((String) session.getAttribute("pid"));
			List<ObstetricsRecordBean> res = vOA.getObstetricsRecordByMID(pid);
			String output = "";
			if (res.size() != 0) {
				ObstetricsRecordBean ob0 = res.get(0);
				Date pri = ob0.getinitDate();
				String weekPregnant = ob0.getWeeksPregnant();
				int idx = weekPregnant.indexOf("-");
				week = Integer.parseInt(weekPregnant.substring(0, idx));
				DAY = pri.getDay();
				c = Calendar.getInstance();
				c.setTime(pri);
 %>

			<div align="left" <%=hidden %> id="apptDiv">
			<h2>Schedule an Appointment</h2>
			<h4>with <%= StringEscapeUtils.escapeHtml("" + ( action.getName(patientID) )) %> (<a href="/iTrust/auth/getPatientID.jsp?forward=hcp/scheduleAppt.jsp">someone else</a>):</h4>		
			<%
				if (week >= 0 && week <= 13) {
					int add = 28;
					if (DAY == 0) add += 1;
					if (DAY == 6) add += 2;		
					c.add(Calendar.DATE, add);
					output = sdf.format(c.getTime());
					%>
					<h5>Monthly appointments recommended around <%=output%></h5>
					<%
				}
			 %>
			 <%
				if (week >= 14 && week <= 28) {	
					int add = 14;
					if (DAY == 0) add += 1;
					if (DAY == 6) add += 2;		
					c.add(Calendar.DATE, add);
					output = sdf.format(c.getTime());
					%>
					<h5>Biweekly appointments recommended around <%=output%></h5>
					<%
				}
			 %>
			 <%
				if (week >= 29 && week <= 39) {	
					int add = 7;
					if (DAY == 0) add += 1;
					if (DAY == 6) add += 2;		
					c.add(Calendar.DATE, add);
					output = sdf.format(c.getTime());
					%>
					<h5>Weekly appointments recommended around <%=output%></h5>
					<%
				}
			 %>
			  <%
				if (week >= 40) {	
					int add = 2;
					if (DAY == 0) add += 1;
					if (DAY == 6) add += 2;	
					c.add(Calendar.DATE, add);
					output = sdf.format(c.getTime());
					%>
					<h5>Next appointment recommended around <%=output%></h5>
					<%
				}
			 %>
			 
			 
		
			<span class="iTrustMessage"><%= StringEscapeUtils.escapeHtml("" + (headerMessage )) %></span><br /><br />
			<span>Appointment Type: </span>
				<select name="apptType">
					<%
						for(ApptTypeBean b : apptTypes) {
							%>
							<option <% if(b.getName().equals(lastApptType)) out.print("selected='selected'"); %> value="<%= b.getName() %>"><%= StringEscapeUtils.escapeHtml("" + ( b.getName() )) %> - <%= StringEscapeUtils.escapeHtml("" + ( b.getDuration() )) %> minutes</option>
							<%
						}
					%>
				</select>
				<br /><br />
				<span>Schedule Date: </span><input type="text" name="schedDate" 
				<% if (error) {%>
		            value="<%= StringEscapeUtils.escapeHtml(lastSchedDate) %>"
		        <% } else { %>
		            value="<%= output %>"
		        <% } %>
				value="" /><input type="button" value="Select Date" onclick="displayDatePicker('schedDate');" /><br /><br />
				<span>Schedule Time: </span>
				<select name="time1">
					<%	
						String hour = "";
						for(int i = 1; i <= 12; i++) {
							if(i < 10) hour = "0"+i;
							else hour = i+"";
							%>
								<option <% if(hour.toString().equals(lastTime1)) out.print("selected='selected'"); %> value="<%=hour%>"><%= StringEscapeUtils.escapeHtml("" + (hour)) %></option>
							<%
						}
					%>
				</select>:<select name="time2">
					<%
						String min = "";
						for(int i = 0; i < 60; i+=5) {
							if(i < 10) min = "0"+i;
							else min = i+"";
							%>
								<option <% if(min.toString().equals(lastTime2)) out.print("selected='selected'"); %> value="<%=min%>"><%= StringEscapeUtils.escapeHtml("" + (min)) %></option>
							<%
						}
					%>
				</select>
				<select name="time3"><option <% if("AM".equals(lastTime3)) out.print("selected='selected'"); %> value="AM">AM</option
				><option  <% if(!error || "PM".equals(lastTime3)) out.print("selected='selected'"); %> value="PM">PM</option></select><br /><br />
				<span>Comment: </span><br />
				<textarea name="comment" cols="100" rows="10"><% if (error) out.print(StringEscapeUtils.escapeHtml(lastComment)); %></textarea><br />
				<br />
				<input type="submit" value="Add to Google Calendar" name="scheduleButton"/>
				<input type="hidden" value="Schedule" name="schedule"/>
				<input type="hidden" id="override" name="override" value="noignore"/>
		
			<br />
			<br />
		</div>
			</form>
			<%
		} else {
			%>
			<div align="left" <%=hidden %> id="apptDiv">
			<h2>Schedule an Appointment</h2>
			<h4>with <%= StringEscapeUtils.escapeHtml("" + ( action.getName(patientID) )) %> (<a href="/iTrust/auth/getPatientID.jsp?forward=hcp/scheduleAppt.jsp">someone else</a>):</h4>
			<span class="iTrustMessage"><%= StringEscapeUtils.escapeHtml("" + (headerMessage )) %></span><br /><br />
			<span>Appointment Type: </span>
				<select name="apptType">
					<%
						for(ApptTypeBean b : apptTypes) {
							%>
							<option <% if(b.getName().equals(lastApptType)) out.print("selected='selected'"); %> value="<%= b.getName() %>"><%= StringEscapeUtils.escapeHtml("" + ( b.getName() )) %> - <%= StringEscapeUtils.escapeHtml("" + ( b.getDuration() )) %> minutes</option>
							<%
						}
					%>
				</select>
				<br /><br />
				<span>Schedule Date: </span><input type="text" name="schedDate" 
				<% if (error) {%>
		            value="<%= StringEscapeUtils.escapeHtml(lastSchedDate) %>"
		        <% } else { %>
		            value="<%=output %>"
		        <% } %>
				value="" /><input type="button" value="Select Date" onclick="displayDatePicker('schedDate');" /><br /><br />
				<span>Schedule Time: </span>
				<select name="time1">
					<%
						String hour = "";
						for(int i = 1; i <= 12; i++) {
							if(i < 10) hour = "0"+i;
							else hour = i+"";
							%>
								<option <% if(hour.toString().equals(lastTime1)) out.print("selected='selected'"); %> value="<%=hour%>"><%= StringEscapeUtils.escapeHtml("" + (hour)) %></option>
							<%
						}
					%>
				</select>:<select name="time2">
					<%
						String min = "";
						for(int i = 0; i < 60; i+=5) {
							if(i < 10) min = "0"+i;
							else min = i+"";
							%>
								<option <% if(min.toString().equals(lastTime2)) out.print("selected='selected'"); %> value="<%=min%>"><%= StringEscapeUtils.escapeHtml("" + (min)) %></option>
							<%
						}
					%>
				</select>
				<select name="time3"><option <% if("AM".equals(lastTime3)) out.print("selected='selected'"); %> value="AM">AM</option
				><option  <% if(!error || "PM".equals(lastTime3)) out.print("selected='selected'"); %> value="PM">PM</option></select><br /><br />
				<span>Comment: </span><br />
				<textarea name="comment" cols="100" rows="10"><% if (error) out.print(StringEscapeUtils.escapeHtml(lastComment)); %></textarea><br />
				<br />
				<input type="submit" value="Schedule" name="scheduleButton"/>
				<input type="hidden" value="Schedule" name="schedule"/>
				<input type="hidden" id="override" name="override" value="noignore"/>
		
			<br />
			<br />
		</div>
			</form>
			
			<% 
		}
 %>


	
<%	} %>

<%@include file="/footer.jsp" %>