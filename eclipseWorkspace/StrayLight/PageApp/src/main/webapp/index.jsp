<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 


<%
	uploadObj.checkForUpload(pageContext, request);
%>

<html>
<head>
	<title>WebSocket Test</title>
	<script type="text/javascript" src="js/main.js"></script>
	<link rel="stylesheet" type="text/css" href="css/main.css" />
</head>

<body onload="onLoad()" >
	<h3>Simulation Test</h3>
	<div>
		<div>
			simulation:&nbsp;

		
			<input id="submitButton" disabled="disabled" class="button" type="submit" name="submitButton" value="Submit" />
			<input id="clearButton" class="button" type="button" name="submitButton" value="Clear" />	
				
			<form action="<%= request.getRequestURI() %>" method="POST" enctype="multipart/form-data">
				<input type="file" name="file" size="50" /><br />
				<input type="submit" value="Upload File" />
				<input type="HIDDEN" name="process" value="true">
			</form>

		</div>
		
	</div>
	
		<br />
	<div id="messageBox"></div>
	
		
</body>
</html>