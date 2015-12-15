<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="true" %>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko-kr" class="ko-kr">

<head>
   <meta http-equiv="Content-Type" content="text/html; utf-8">
   <meta name="viewport" content="width=device-width, initial-scale=1">
   <link rel="stylesheet" href="<c:url value='/css/egovframework/menu_styles.css'/>"/>
   <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
   <script src="<c:url value='/js/menu_script.js'/>"></script>
   
   <script type="text/javaScript" language="javascript" defer="defer">
    
    /* 로그아웃 버튼 클릭 이벤트 */
    function btnLogout_onclick()
    {
    	alert("로그아웃");
    	return;
    }
    
    </script>
</head>

<body style="background-color:#0C78B0">
<form name="topMenu">

	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr bgcolor="#1fa0e4"><td></td></tr>
	</table>

</form>
</body>
</html>
