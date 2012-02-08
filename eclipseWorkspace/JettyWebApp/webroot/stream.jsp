<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 






<html>
<head>
	<title>WebSocket Test</title>
	<script type="text/javascript" src="js/stream.js"></script>
	<link rel="stylesheet" type="text/css" href="css/main.css" />
</head>

<body onload="onLoad()" >
	<h3>Simulation Test</h3>
	<div>
		<div>
			simulation:&nbsp;

		
			<input id="submitButton" disabled="true"class="button" type="submit" name="submitButton" value="Submit" />
			<input id="clearButton" class="button" type="button" name="submitButton" value="Clear" />	
				

		</div>
		
	</div>
	
		<br />
	<div id="messageBox"></div>
	
		
</body>
</html>