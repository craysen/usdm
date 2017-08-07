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
    <link type="text/css" rel="stylesheet" href="<c:url value='/css/egovframework/sample.css'/>"/>
	
    <script type="text/javaScript" language="javascript" defer="defer">

    /* 최초 화면호출 이벤트 */
    function form_onLoad()
    {
    	return;
    }
    
    /* 상수관선택 버튼 클릭 이벤트 */
    function btnFindPipe_onclick()
    {
    	var url = "<c:url value='/waterpipe/selectAccidentPipePopup.do'/>";
    	var openParam = "width=850, height=570, scrollbars=no, toolbar=no, resizable=no, status=no, location=no, menubar=no";
    	
    	var popupObj = window.open(url, "mapPopup", openParam);
    }
    
    /* 검색 버튼 클릭 이벤트 */
    function btnSearch_onclick()
    {
		var form = document.listForm;
		
		form.action = "<c:url value='/waterpipe/selectWaterAccidentList.do'/>";
    	form.submit();
    }
    
    /* 목록 선택 이벤트 */
    function list_onclick(ftrCde, ftrIdn, longitude, latitude, accidentTime)
    {
    	var form = document.listForm;
    	
   		form.action = "<c:url value='/waterpipe/selectWaterAccidentDetail.do?pipeFtrCde="   + ftrCde
   																		 + "&pipeFtrIdn="   + ftrIdn
   																		 + "&longitude="    + longitude
   																		 + "&latitude="     + latitude
   																		 + "&accidentTime=" + accidentTime+"'/>";
    	
    	form.submit();
    }
    
    /* 사고정보 등록 버튼 클릭 이벤트 */
    function btnRegisterAccident_onclick()
    {
		var form = document.listForm;
    	
   		form.action = "<c:url value='/waterpipe/insertWaterAccident.do'/>";
    	form.submit();
    }
    
    </script>
</head>

<body style="text-align:center; margin:0 auto; display:inline; padding-top:100px;" onload="javascript:form_onLoad();">
    <form:form commandName="usdmSearchVO" id="listForm" name="listForm" method="post">
    	<input type="hidden" id="argReadonly" value="N"/>
        <div id="content_pop">
        
        	<!-- 타이틀 -->
	    	<div id="title">
	    	<table width="100%" border="0">
	    		<tr>
	    			<td>
	    				<img src="<c:url value='/images/egovframework/usdm/title_dot.gif'/>"/>
	                    <font size="4"><b>&nbsp;상수도 사고정보 목록</b></font>
	                </td>
	    		</tr>
	    		<tr><td bgcolor="#D3E2EC" height="3px"></td></tr>
	    	</table>
	    	</div>
	    	<br></br>
	    	<br></br>
	    	<!-- /타이틀 -->

			<!-- 검색조건 -->
        	<div id="search">
        		
        		<table width="100%" border="0" cellpadding="0" cellspacing="0">
        			<colgroup>
        				<col width="60"/>
        				<col width="?"/>
        				<col width="60"/>
        				<col width="?"/>
        				<col width="?"/>
        			</colgroup>
        			<tr><td bgcolor="#97A6B0" height="3px" colspan="5"></td></tr>
        			<tr><td bgcolor="#FFFFFF" height="3px" colspan="5"></td></tr>
        			<tr>
        				<td align="center">상수관ID</td>
        				<td>
        					<span class="btn_blue_l">
			                    <a href="javascript:btnFindPipe_onclick();">선택</a>
			                    <img src="<c:url value='/images/egovframework/usdm/btn_bg_r.gif'/>" style="margin-left:6px;"/>
			                </span>
			                &nbsp;
							<form:input path="ftrIdn" id="pipeFtrIdn" style="width:62px" onfocus="this.select()"/>
        				</td>
        				<td align="center">중심</td>
        				<td>
        					<form:input path="centerX" id="centerX" style="width:70px" onfocus="this.select()"/> 
        					<form:input path="centerY" id="centerY" style="width:70px" onfocus="this.select()"/>
        					&nbsp;반경&nbsp;
        					<form:input path="radius" id="radius" style="width:50px" onfocus="this.select()"/>
        				</td>
        				<td>
	        				<span class="btn_blue_l">
	        	                <a href="javascript:btnSearch_onclick();"><spring:message code="button.search" /></a>
	        	                <img src="<c:url value='/images/egovframework/usdm/btn_bg_r.gif'/>" style="margin-left:6px;" alt=""/>
	        	            </span>
        				</td>
        			</tr>
        			<tr><td bgcolor="#FFFFFF" height="3px" colspan="5"></td></tr>
        			<tr><td bgcolor="#97A6B0" height="3px" colspan="5"></td></tr>
        		</table>
        		
        	</div>
        	<!-- /검색조건 -->
        	
        	<!-- 버튼 -->
			<div id="sysbtn">
        	  <ul>
        	      <li>
        	          <span class="btn_blue_l">
        	              <a href="javascript:btnRegisterAccident_onclick();">사고정보<spring:message code="button.create" /></a>
                          <img src="<c:url value='/images/egovframework/usdm/btn_bg_r.gif'/>" style="margin-left:6px;" alt=""/>
                      </span>
                  </li>
              </ul>
              <br></br>
        	</div>
        	<!-- /버튼 -->
        	
        	<!-- List -->
        	<div id="table">
        		<table width="100%" border="0" cellpadding="0" cellspacing="0">
        			<colgroup>
        				<col width="120"/>
        				<col width="150"/>
        				<col width="150"/>
        				<col width="?"/>
        				<col width="?"/>
        			</colgroup>
        			<tr>
        				<th align="center">상수관ID</th>
        				<th align="center">발생일자</th>
        				<th align="center">발생시각</th>
        				<th align="center">유형</th>
        				<th align="center">형태</th>
        			</tr>
        			<c:forEach var="result" items="${resultList}" varStatus="status">
            			<tr onclick="javascript:list_onclick('${result.pipeFtrCde}','${result.pipeFtrIdn}','${result.longitude}','${result.latitude}','${result.accidenttime}')" style="cursor:pointer;" onmouseover=this.style.backgroundColor="#F5F5F5" onmouseout=this.style.backgroundColor="#FFFFFF">
            				<td align="center" class="listtd"><c:out value="${result.pipeFtrIdn}"/></td>
            				<td align="center" class="listtd"><c:out value="${result.accidentdatestr}"/></td>
            				<td align="center" class="listtd"><c:out value="${result.accidenttimestr}"/></td>
            				<td align="center" class="listtd"><c:out value="${result.accidenttypestr}"/></td>
            				<td align="center" class="listtd"><c:out value="${result.accidentshapestr}"/></td>
            			</tr>
        			</c:forEach>
        		</table>
        	</div>
        	<!-- /List -->

        </div>
    </form:form>
</body>
</html>
