<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title> My first JSP   </title>
	</head>	
	<body>		
		<form action="ServletCredenziali" method="post">	
		<input type="hidden" name="idcredenziali" value='1'/>		
			Email <input type="text" name="email" size="20px"><br/>
			Password <input type="password" name="password" size="20px"><br/>
			<input type="submit" value="submit"> <br/>						
		</form>		
	</body>	
</html>