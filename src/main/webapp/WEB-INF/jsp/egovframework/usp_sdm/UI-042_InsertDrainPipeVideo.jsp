<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="true" %>
<%@ taglib prefix="c"         uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"      uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring"    uri="http://www.springframework.org/tags" %>

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
		form = document.detailForm;
		
		//test_input();
    }
    
    /* 테스트용 */
    function test_input()
    {
		form = document.detailForm;
		
		form.gwID.value = "1";
		form.panID.value = "1";
		form.snID.value = "1";
		form.tdID.value = "1";
		form.resolutionW.value = "640";
		form.resolutionH.value = "480";
		form.numPixel.value = "500000";
		form.addr1.value = "대전시";
		form.addr2.value = "서구";
		form.addr3.value = "월평동";
		form.positionX.value = "127.364258";
		form.positionY.value = "36.35828";
		form.positionZ.value = "55";
		form.manholeFtrIdn.value = "3331";
		form.pipeFtrIdn.value = "8956,8957";
		form.directionAngle.value = "43";
		form.distance.value = "14.75";
		form.recStartTime.value = "2015-10-21 13:35:53";
		form.recEndTime.value = "2015-10-21 13:44:13";
		form.POI.value = "월평역";
    }
    
    /* 센서선택 버튼 클릭 이벤트 */
    function btnFindSensor_onclick()
    {
    	alert("센서선택 팝업 호출");
    }
    
    /* 하수관선택 버튼 클릭 이벤트 */
    function btnFindPipe_onclick()
    {
    	var url = "<c:url value='/drainpipe/selectPipePopup.do'/>";
    	var openParam = "width=850, height=570, scrollbars=no, toolbar=no, resizable=no, status=no, location=no, menubar=no";
    	
    	var popupObj = window.open(url, "mapPopup", openParam);
    }
    
    /* 저장 버튼 클릭 이벤트 */
    function btnSave_onclick()
    {
    	form = document.detailForm;

    	if (validateForm()) {
    		form.action = "<c:url value='/drainpipe/uploadVideoFile.do'/>";
        	form.submit();
    	}
    }
    
    /* 필수입력 체크 */
    function validateForm()
    {
    	form = document.detailForm;
    	
    	if (confirm("저장하시겠습니까?"))
    		return true;
    	else
    		return false;
    }
        
    </script>
</head>
<body style="text-align:center; margin:0 auto; display:inline; padding-top:100px;" onload="javascript:form_onLoad();">

<form:form commandName="drainPipeVideoVO" id="detailForm" name="detailForm" enctype="multipart/form-data">
	<input type="hidden" id="argReadonly" value="N"/>
    <div id="content_pop">
    	<!-- 타이틀 -->
    	<div id="title">
    	<table width="100%" border="0">
    		<tr>
    			<td>
    				<img src="<c:url value='/images/egovframework/usdm/title_dot.gif'/>"/>
                    <font size="4"><b>&nbsp;하수도 센싱 데이터 동영상 입력</b></font>
                </td>
    		</tr>
    		<tr><td bgcolor="#D3E2EC" height="3px"> </td></tr>
    	</table>
    	</div>
    	<br></br>
    	<br></br>
    	<!-- // 타이틀 -->
    	
    	<div id="sysbtn">
    		<ul>
    			<li>
                    <span class="btn_blue_l">
                        <a href="javascript:btnSave_onclick();">저장</a>
                        <img src="<c:url value='/images/egovframework/usdm/btn_bg_r.gif'/>" style="margin-left:6px;" alt=""/>
                    </span>
                </li>
            </ul>
    	</div>
    	<br></br>
    	
    	<div id="sensorTable">
    	<table width="100%" border="1" cellpadding="0" cellspacing="0" style="bordercolor:#D3E2EC; bordercolordark:#FFFFFF; BORDER-TOP:#C2D0DB 2px solid; BORDER-LEFT:#ffffff 1px solid; BORDER-RIGHT:#ffffff 1px solid; BORDER-BOTTOM:#C2D0DB 1px solid; border-collapse: collapse;">
    		<colgroup>
    			<col width="100"/>
    			<col width="?"/>
    		</colgroup>
    		<tr>
    			<td class="tbtd_caption">센서정보</td>
    			<td class="tbtd_content">
    				<form:input path="gwID"  maxlength="30" cssClass="shorttxt"/>
    				<form:input path="panID" maxlength="30" cssClass="shorttxt"/>
    				<form:input path="snID"  maxlength="30" cssClass="shorttxt"/>
    				<form:input path="tdID"  maxlength="30" cssClass="shorttxt"/>&nbsp;
    				<button onclick="javascript:btnFindSensor_onclick();">센서선택</button>
    			</td>
    		</tr>
    	</table>
    	</div>
    	<br></br>
    	<div id="table">
    	<table width="100%" border="1" cellpadding="0" cellspacing="0" style="bordercolor:#D3E2EC; bordercolordark:#FFFFFF; BORDER-TOP:#C2D0DB 2px solid; BORDER-LEFT:#ffffff 1px solid; BORDER-RIGHT:#ffffff 1px solid; BORDER-BOTTOM:#C2D0DB 1px solid; border-collapse: collapse;">
    		<colgroup>
    			<col width="100"/>
    			<col width="?"/>
    			<col width="100"/>
    			<col width="?"/>
    		</colgroup>
    		<tr>
    			<td class="tbtd_caption">파일</td>
    			<td class="tbtd_content" colspan="3">
    				<form:input path="file" name="file" type="file" accept="video/*"/>
    			</td>
    		</tr>
    		<tr>
    			<td class="tbtd_caption">해상도</td>
    			<td class="tbtd_content">
    				<form:input path="resolutionW" maxlength="30" cssClass="shorttxt"/>
    				&nbsp;*&nbsp;
    				<form:input path="resolutionH" maxlength="30" cssClass="shorttxt"/>
    			</td>
    			<td class="tbtd_caption">화소</td>
    			<td class="tbtd_content">
    				<form:input path="numPixel" maxlength="30" cssClass="txt"/>
    			</td>
    		</tr>
    		<tr>
    			<td class="tbtd_caption">주소</td>
    			<td class="tbtd_content" colspan="3">
    				시/도&nbsp;<form:input path="addr1" maxlength="10" cssClass="txt"/>&nbsp;
    				시/군/구&nbsp;<form:input path="addr2" maxlength="10" cssClass="txt"/>&nbsp;
    				읍/면/동&nbsp;<form:input path="addr3" maxlength="10" cssClass="txt"/>
    			</td>
    		</tr>
    		<tr>
    			<td class="tbtd_caption">GPS좌표</td>
    			<td class="tbtd_content" colspan="3">
    				위도&nbsp;<form:input path="positionY" maxlength="10" cssClass="txt"/>&nbsp;
    				경도&nbsp;<form:input path="positionX" maxlength="10" cssClass="txt"/>&nbsp;
    				고도&nbsp;<form:input path="positionZ" maxlength="10" cssClass="txt"/>
    			</td>
    		</tr>
    		<tr>
    			<td class="tbtd_caption">맨홀 및 하수관</td>
    			<td class="tbtd_content" colspan="3">
    				<span class="btn_blue_l">
                        <a href="javascript:btnFindPipe_onclick();">선택</a>
                        <img src="<c:url value='/images/egovframework/usdm/btn_bg_r.gif'/>" style="margin-left:6px;"/>
                    </span>
                    &nbsp;
                    맨홀ID&nbsp;<form:input path="manholeFtrIdn" maxlength="30" cssClass="txt" readonly="readonly"/>&nbsp;
    				하수관ID&nbsp;<form:input path="pipeFtrIdn" maxlength="30" cssClass="longtxt" readonly="readonly"/>
    			</td>
    		</tr>
    		<tr>
    			<td class="tbtd_caption">방향각</td>
    			<td class="tbtd_content">
    				<form:input path="directionAngle" maxlength="30" cssClass="txt"/>
    			</td>
    			<td class="tbtd_caption">이동거리</td>
    			<td class="tbtd_content">
    				<form:input path="distance" maxlength="30" cssClass="txt"/>
    			</td>
    		</tr>
    		<tr>
    			<td class="tbtd_caption">촬영시작시각</td>
    			<td class="tbtd_content" colspan="3">
    				<form:input path="recStartTime" maxlength="30" cssClass="longtxt"/>
    			</td>
    		</tr>
    		<tr>
    			<td class="tbtd_caption">촬영종료시각</td>
    			<td class="tbtd_content" colspan="3">
    				<form:input path="recEndTime" maxlength="30" cssClass="longtxt"/>
    			</td>
    		</tr>
    		<tr>
    			<td class="tbtd_caption">손상여부</td>
    			<td class="tbtd_content" colspan="3">
    				<form:radiobutton path="damage" value="Y"/>손상됨&nbsp;
    				<form:radiobutton path="damage" value="N" checked="checked"/>손상되지 않음
    			</td>
    		</tr>
    		<tr>
    			<td class="tbtd_caption">POI</td>
    			<td class="tbtd_content" colspan="3">
    				<form:input path="POI" maxlength="50" cssClass="longtxt"/>
    			</td>
    		</tr>
    	</table>
      </div>
    </div>
</form:form>
</body>
</html>