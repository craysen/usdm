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
    
    <!-- jQuery DatePicker -->
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.8.18/themes/base/jquery-ui.css" type="text/css" media="all" />
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type="text/javascript"></script>
	<script src="http://code.jquery.com/ui/1.8.18/jquery-ui.min.js" type="text/javascript"></script>
	<script>
		$(function() {
			  $( "#fromDate, #toDate" ).datepicker({
			    dateFormat: 'yy-mm-dd',
			    prevText: '이전 달',
			    nextText: '다음 달',
			    monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
			    monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
			    dayNames: ['일','월','화','수','목','금','토'],
			    dayNamesShort: ['일','월','화','수','목','금','토'],
			    dayNamesMin: ['일','월','화','수','목','금','토'],
			    showMonthAfterYear: true,
			    yearSuffix: '년'
			  });
			});
	</script>
	<style>
		.ui-datepicker{ font-size: 11px; width: 160px; }
		.ui-datepicker select.ui-datepicker-month{ width:30%; font-size: 10px; }
		.ui-datepicker select.ui-datepicker-year{ width:40%; font-size: 10px; }
	</style>
	<!-- /jQuery DatePicker -->
	
    <script type="text/javaScript" language="javascript" defer="defer">

    /* 최초 화면호출 이벤트 */
    function form_onLoad()
    {
    	return;
    }
    
    /* 검색 버튼 클릭 이벤트 */
    function btnSearch_onclick()
    {
		var form = document.listForm;
		var fromDate = form.fromDate.value;
		var toDate = form.toDate.value;

		if (fromDate != "" && toDate != "") {
			if (fromDate > toDate) {
				alert("검색 시작일자는 종료일자 이전의 날짜이어야 합니다.");
				return;
			}
		}
		
		form.action = "<c:url value='/drainpipe/selectDrainPipeFileList.do'/>";
    	form.submit();
    }
    
    /* 목록 선택 이벤트 */
    function list_onclick(file_id, file_type)
    {
    	var form = document.listForm;
    	
    	// 이미지인 경우
    	if (file_type == "I") {
    		form.action = "<c:url value='/drainpipe/selectDrainPipeImgaeDetail.do?fileId=" + file_id + "'/>";
    	}
    	// 동영상인 경우
    	else if (file_type == "V") {
    		form.action = "<c:url value='/drainpipe/selectDrainPipeVideoDetail.do?fileId=" + file_id + "'/>";
    	}
    	
    	form.submit();
    }
    
    /* 동영상 등록 버튼 클릭 이벤트 */
    function btnRegisterVideo_onclick()
    {
		var form = document.listForm;
    	
   		form.action = "<c:url value='/drainpipe/insertDrainPipeVideo.do'/>";
    	form.submit();
    }
    
    /* 이미지 등록 버튼 클릭 이벤트 */
    function btnRegisterImage_onclick()
    {
		var form = document.listForm;
    	
   		form.action = "<c:url value='/drainpipe/insertDrainPipeImage.do'/>";
    	form.submit();
    }
        
    </script>
</head>

<body style="text-align:center; margin:0 auto; display:inline; padding-top:100px;" onload="javascript:form_onLoad();">
    <form:form commandName="usdmSearchVO" id="listForm" name="listForm" method="post">
        <div id="content_pop">
        
        	<!-- 타이틀 -->
	    	<div id="title">
	    	<table width="100%" border="0">
	    		<tr>
	    			<td>
	    				<img src="<c:url value='/images/egovframework/usdm/title_dot.gif'/>"/>
	                    <font size="4"><b>&nbsp;하수도 센싱 데이터 파일 조회</b></font>
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
        				<td align="center">촬영일자</td>
        				<td>
        					<form:input path="fromDate" id="fromDate" style="width:62px" onfocus="this.select()"/> ~ 
        					<form:input path="toDate" id="toDate" style="width:62px" onfocus="this.select()"/>
        				</td>
        				<td align="center">중심</td>
        				<td>
        					<form:input path="centerX" id="centerX" style="width:50px" onfocus="this.select()"/> 
        					<form:input path="centerY" id="centerY" style="width:50px" onfocus="this.select()"/>
        					&nbsp;반경&nbsp;
        					<form:input path="radius" id="radius" style="width:50px" onfocus="this.select()"/>
        				</td>
        				<td rowspan="2">
	        				<span class="btn_blue_l">
	        	                <a href="javascript:btnSearch_onclick();"><spring:message code="button.search" /></a>
	        	                <img src="<c:url value='/images/egovframework/usdm/btn_bg_r.gif'/>" style="margin-left:6px;" alt=""/>
	        	            </span>
        				</td>
        			</tr>
        			<tr>
        				<td align="center">주소</td>
        				<td>
        					<form:input path="addr" id="addr" style="width:200px" onfocus="this.select()"/>
        				</td>
        				<td align="center">POI</td>
        				<td>
        					<form:input path="poi" id="poi" style="width:200px" onfocus="this.select()"/>
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
        	              <a href="javascript:btnRegisterVideo_onclick();">동영상<spring:message code="button.create" /></a>
                          <img src="<c:url value='/images/egovframework/usdm/btn_bg_r.gif'/>" style="margin-left:6px;" alt=""/>
                      </span>
                  </li>
        	      <li>
        	          <span class="btn_blue_l">
        	              <a href="javascript:btnRegisterImage_onclick();">이미지<spring:message code="button.create" /></a>
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
        				<col width="60"/>
        				<col width="200"/>
        				<col width="?"/>
        				<col width="150"/>
        			</colgroup>
        			<tr>
        				<th align="center">종류</th>
        				<th align="center">파일명</th>
        				<th align="center">촬영장소</th>
        				<th align="center">촬영시간</th>
        			</tr>
        			<c:forEach var="result" items="${resultList}" varStatus="status">
            			<tr onclick="javascript:list_onclick('${result.fileid}','${result.filetype}')" style="cursor:pointer;" onmouseover=this.style.backgroundColor="#F5F5F5" onmouseout=this.style.backgroundColor="#FFFFFF">
            				<td align="center" class="listtd"><c:out value="${result.filetypename}"/></td>
            				<td align="left" class="listtd"><c:out value="${result.filename}"/></td>
            				<td align="left" class="listtd"><c:out value="${result.addr1}"/>&nbsp;<c:out value="${result.addr2}"/>&nbsp;<c:out value="${result.addr3}"/></td>
            				<td align="center" class="listtd"><c:out value="${result.rectime}"/>&nbsp;</td>
            			</tr>
        			</c:forEach>
        		</table>
        	</div>
        	<!-- /List -->

        </div>
    </form:form>
</body>
</html>
