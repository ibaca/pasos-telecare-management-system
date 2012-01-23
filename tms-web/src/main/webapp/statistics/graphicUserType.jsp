<%-- 
    Document   : graphic_user_type.jsp
    Created on : 23-ene-2012, 23:13:08
    Author     : agumpg
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="org.inftel.tms.statistics.ChartUserTypeBean"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="chartUserTypeBean" scope="request" class="ChartUserTypeBean"></jsp:useBean>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Estadística por perfiles de usuarios</title>
    </head>
    <body>
        <h1>Estadística por perfiles de usuarios</h1>
               
        <p:pieChart value="${chartUserTypeBean.pieModel}" legendPosition="w"  
                title="Perfiles de usuarios" style="width:400px;height:300px" /> 
            
    </body>
</html>
