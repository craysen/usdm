<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="true" %>
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
    
    /* 메뉴 선택 이벤트 */
    function menu_onclick(menu_cd)
    {
    	var form = document.leftMenu;
    	
    	switch (menu_cd) {
    	// Home
    	case "UI020":
    		form.action = "<c:url value='/mainframe.do'/>";
    		break;
    	
    	// 센서 등록
    	case "UI030":
    		break;
    	
    	// 하수도 센싱데이터 입력 - 텍스트/엑셀
    	case "UI040":
    		break;
    	
    	// 하수도 센싱데이터 입력 - 이미지
    	case "UI041":
    		form.action = "<c:url value='/drainpipe/insertDrainPipeImage.do'/>";
    		break;
    	
    	// 하수도 센싱데이터 입력 - 동영상
    	case "UI042":
    		form.action = "<c:url value='/drainpipe/insertDrainPipeVideo.do'/>";
    		break;
    		
    	// 도시철도 센싱데이터 입력 - 텍스트/엑셀
    	case "UI050":
    		break;
    	
    	// 도시철도 센싱데이터 입력 - 이미지
    	case "UI051":
    		break;
    	
    	// 도시철도 센싱데이터 입력 - 동영상
    	case "UI052":
    		form.action = "<c:url value='/railroad/insertRailroadVideo.do'/>";
    		break;
    		
    	// 지하수 센싱데이터 입력 - 텍스트/엑셀
    	case "UI060":
    		form.action = "<c:url value='/underwater/insertUnderWaterFile.do'/>";
    		break;
    		
    	// 하수도 센싱데이터 조회
    	case "UI070":
    		break;
    		
    	// 도시철도 센싱데이터 조회
    	case "UI071":
    		break;
    		
    	// 지하수 센싱데이터 조회
    	case "UI072":
    		form.action = "<c:url value='/underwater/selectUnderWaterData.do'/>";
    		break;
    		
    	// 하수도 파일정보 조회
    	case "UI080":
    		form.action = "<c:url value='/drainpipe/selectDrainPipeFileList.do'/>";
    		break;
    		
    	// 도시철도 파일정보 조회
    	case "UI090":
    		form.action = "<c:url value='/railroad/selectRailroadFileList.do'/>";
    		break;
    		
    	// 지하수 파일정보 조회
    	case "UI100":
    		break;
    		
    	// 하수도 사고정보 입력
    	case "UI110":
    		form.action = "<c:url value='/drainpipe/insertDrainAccident.do'/>";
    		break;
    		
    	// 상수도 사고정보 입력
    	case "UI120":
    		form.action = "<c:url value='/waterpipe/insertWaterAccident.do'/>";
    		break;
    		
    	// 하수도 사고목록 조회
    	case "UI112":
    		form.action = "<c:url value='/drainpipe/selectDrainAccidentList.do'/>";
    		break;
    		
    	// 상수도 사고목록 조회
    	case "UI122":
    		form.action = "<c:url value='/waterpipe/selectWaterAccidentList.do'/>";
    		break;
    		
    	default:
    		return;
    	}
    	
	    form.target = "MainFrame";
	    form.submit();
    }
    
    /* 로그아웃 버튼 클릭 이벤트 */
    function btnLogout_onclick()
    {
    	var form = document.leftMenu;
    	
    	if (confirm("로그아웃 하시겠습니까?"))
    	{
    		form.action = "<c:url value='/logout.do'/>";
        	form.target = "_top";
        	form.submit();	
    	}
    	
    	return;
    }
    
    </script>
</head>

<body style="background-color:#0C78B0">
<form name="leftMenu">

	<div id="userInfo">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr bgcolor="#1fa0e4">
				<td align="center" valign="middle">
					<br></br>
					<c:out value="${sessionScope.userLoginInfo.userName}" /> 님<br></br>
					<a href="javascript:btnLogout_onclick()"><img src="<c:url value='/images/egovframework/usdm/logout_btn.gif' />" alt="로그아웃" /></a>
					<br></br>
				</td>
			</tr>
		</table>
    </div>

	<br>

	<c:set value="${sessionScope.userLoginInfo.authGroupCd}" var="auth"/>
	<div id='cssmenu'>
		<ul>
			<li><a href='#' onclick="javascript:menu_onclick('UI020')"><span>Home</span></a></li>
			
			<!-- 
			<c:if test="${auth=='ADMIN' || auth=='KICT' || auth=='KRRI' || auth=='KIGAM'}">
				<li><a href='#' onclick="javascript:menu_onclick('UI030')"><span>센서 등록</span></a></li>
			</c:if>
			-->
			
			<c:if test="${auth=='ADMIN' || auth=='KICT'}">
				<li class='active has-sub'>
					<a href='#'><span>
						<c:choose>
							<c:when test="${auth=='ADMIN'}">하수도 데이터 입력</c:when>
							<c:when test="${auth=='KICT'}">센싱데이터 입력</c:when>
						</c:choose>
					</span></a>
					<ul>
						<!-- <li><a href='#' onclick="javascript:menu_onclick('UI040')"><span>텍스트/엑셀 파일 업로드</span></a></li> -->
						<li><a href='#' onclick="javascript:menu_onclick('UI041')"><span>이미지 파일 업로드</span></a></li>
						<li><a href='#' onclick="javascript:menu_onclick('UI042')"><span>동영상 파일 업로드</span></a></li>
					</ul>
				</li>
			</c:if>
			
			<c:if test="${auth=='ADMIN' || auth=='KRRI'}">
				<li class='active has-sub'>
					<a href='#'><span>
						<c:choose>
							<c:when test="${auth=='ADMIN'}">도시철도 데이터 입력</c:when>
							<c:when test="${auth=='KRRI'}">센싱데이터 입력</c:when>
						</c:choose>
					</span></a>
					<ul>
						<!-- <li><a href='#' onclick="javascript:menu_onclick('UI050')"><span>텍스트/엑셀 파일 업로드</span></a></li> -->
						<!-- <li><a href='#' onclick="javascript:menu_onclick('UI051')"><span>이미지 파일 업로드</span></a></li> -->
						<li><a href='#' onclick="javascript:menu_onclick('UI052')"><span>동영상 파일 업로드</span></a></li>
					</ul>
				</li>
			</c:if>
			
			<c:if test="${auth=='ADMIN' || auth=='KIGAM'}">
				<li class='active has-sub'>
					<a href='#'><span>
						<c:choose>
							<c:when test="${auth=='ADMIN'}">지하수 데이터 입력</c:when>
							<c:when test="${auth=='KIGAM'}">센싱데이터 입력</c:when>
						</c:choose>
					</span></a>
					<ul>
						<li><a href='#' onclick="javascript:menu_onclick('UI060')"><span>텍스트/엑셀 파일 업로드</span></a></li>
					</ul>
				</li>
			</c:if>
			
			<c:choose>
				<c:when test="${auth=='ADMIN' || auth=='ETRI'}">
					<li class='active has-sub'><a href='#'><span>센싱데이터 조회</span></a>
						<ul>
							<!-- <li><a href='#' onclick="javascript:menu_onclick('UI070')"><span>하수도 센싱데이터 조회</span></a></li> -->
							<!-- <li><a href='#' onclick="javascript:menu_onclick('UI071')"><span>도시철도 센싱데이터 조회</span></a></li> -->
							<li><a href='#' onclick="javascript:menu_onclick('UI072')"><span>지하수 센싱데이터 조회</span></a></li>
						</ul>
					</li>
				</c:when>
				<c:when test="${auth=='KICT'}">
					<!-- <li><a href='#' onclick="javascript:menu_onclick('UI070')"><span>센싱데이터 조회</span></a></li> -->
				</c:when>
				<c:when test="${auth=='KRRI'}">
					<!-- <li><a href='#' onclick="javascript:menu_onclick('UI071')"><span>센싱데이터 조회</span></a></li> -->
				</c:when>
				<c:when test="${auth=='KIGAM'}">
					<li><a href='#' onclick="javascript:menu_onclick('UI072')"><span>센싱데이터 조회</span></a></li>
				</c:when>
			</c:choose>
			
			<c:choose>
				<c:when test="${auth=='ADMIN' || auth=='ETRI'}">
					<li class='active has-sub'><a href='#'><span>파일정보 조회</span></a>
						<ul>
							<li><a href='#' onclick="javascript:menu_onclick('UI080')"><span>하수도 파일정보 조회</span></a></li>
							<li><a href='#' onclick="javascript:menu_onclick('UI090')"><span>도시철도 파일정보 조회</span></a></li>
							<!-- <li><a href='#' onclick="javascript:menu_onclick('UI100')"><span>지하수 파일정보 조회</span></a></li> -->
						</ul>
					</li>
				</c:when>
				<c:when test="${auth=='KICT'}">
					<li><a href='#' onclick="javascript:menu_onclick('UI080')"><span>파일정보 조회</span></a></li>
				</c:when>
				<c:when test="${auth=='KRRI'}">
					<li><a href='#' onclick="javascript:menu_onclick('UI090')"><span>파일정보 조회</span></a></li>
				</c:when>
				<c:when test="${auth=='KIGAM'}">
					<!-- <li><a href='#' onclick="javascript:menu_onclick('UI100')"><span>파일정보 조회</span></a></li> -->
				</c:when>
			</c:choose>
			
			<c:choose>
				<c:when test="${auth=='ADMIN' || auth=='KICT'}">
					<li class='active has-sub'><a href='#'><span>사고정보 입력</span></a>
						<ul>
							<li><a href='#' onclick="javascript:menu_onclick('UI120')"><span>상수도 사고정보 입력</span></a></li>
							<li><a href='#' onclick="javascript:menu_onclick('UI110')"><span>하수도 사고정보 입력</span></a></li>
						</ul>
					</li>
				</c:when>
			</c:choose>
			
			<c:choose>
				<c:when test="${auth=='ADMIN' || auth=='ETRI' || auth=='KICT'}">
					<li class='active has-sub'><a href='#'><span>사고정보 조회</span></a>
						<ul>
							<li><a href='#' onclick="javascript:menu_onclick('UI122')"><span>상수도 사고정보 조회</span></a></li>
							<li><a href='#' onclick="javascript:menu_onclick('UI112')"><span>하수도 사고정보 조회</span></a></li>
						</ul>
					</li>
				</c:when>
			</c:choose>
			
		</ul>
	</div>

	<div id='logo'>
		<img src="<c:url value='/images/egovframework/usdm/logo_ugs.png'/>" width="80%" height="80%"/>
		<br></br>
		<!-- 2015 Realtimetech Co.,Ltd. -->
	</div>

</form>
</body>
</html>
