<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="true" %>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>SDM</title>
    
    <frameset rows="30,*" frameborder="0">
    	<frame name="TopFrame" src="<c:url value='/topmenu.do'/>" noresize />
	    <frameset cols="215,*" frameborder="0">
			<frame name="MenuFrame" src="<c:url value='/leftmenu.do'/>" noresize />
			<frame name="MainFrame" src="<c:url value='/mainframe.do'/>" noresize />
		</frameset>
 	</frameset>
 	<base target="MainFrame"/>
</head>
<body>
</body>
</html>