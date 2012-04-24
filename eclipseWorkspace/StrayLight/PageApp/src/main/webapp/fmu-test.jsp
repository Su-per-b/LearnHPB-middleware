<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 



<html>
<head>
	<title>FMU Test</title>
	<link rel="stylesheet" type="text/css" href="css/main.css" />
</head>

<body >
	<h3>FMU Test</h3>
	
<form action="test-fmu-code.jsp" method="post"
                        enctype="multipart/form-data">
<input type="file" name="file" size="50" />
<br />

<input type="submit" value="test" />

</form>


	
	<div>
		<div>
			simulation:&nbsp;
			<input id="submitButton" class="button" type="submit" name="submitButton" value="Submit" />
			<input id="clearButton" class="button" type="button" name="submitButton" value="Clear" />	
		</div>
	</div>
		<br />
	<div id="messageBox"></div>
</body>
</html>