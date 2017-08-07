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
		form.videoType.value = "SURFACE";
		form.resolutionW.value = "1024";
		form.resolutionH.value = "768";
		form.numPixel.value = "1000000";
		form.startPosX.value = "10";
		form.startPosY.value = "10";
		form.startPosZ.value = "10";
		form.endPosX.value = "10";
		form.endPosY.value = "10";
		form.endPosZ.value = "10";
		form.recStartTime.value = "2015-05-01 23:46:13";
		form.recEndTime.value = "2015-05-01 23:47:12";
		form.workSectionStart.value = "시작지점";
		form.workSectionEnd.value = "종료지점";
		form.measureType.value = "1";
		form.POI.value = "대전시청";
    }
    
    /* 센서선택 버튼 클릭 이벤트 */
    function btnFindSensor_onclick()
    {
    	alert("센서선택 팝업 호출", false);
    }
    
    /* 저장 버튼 클릭 이벤트 */
    function btnSave_onclick()
    {
    	form = document.detailForm;

    	if (validateForm()) {
    		form.action = "<c:url value='/railroad/uploadVideoFile.do'/>";
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
    
    /* 영상 유형 변경 이벤트 */
    function videoType_onchange(obj)
    {
    	return true;
    }
        
    </script>
</head>
<body style="text-align:center; margin:0 auto; display:inline; padding-top:100px;" onload="javascript:form_onLoad();">

<form:form commandName="railroadVideoVO" id="detailForm" name="detailForm" enctype="multipart/form-data">
    <div id="content_pop">
    	<!-- 타이틀 -->
    	<div id="title">
    	<table width="100%" border="0">
    		<tr>
    			<td>
    				<img src="<c:url value='/images/egovframework/usdm/title_dot.gif'/>"/>
                    <font size="4"><b>&nbsp;도시철도 센싱 데이터 동영상 입력</b></font>
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
    			<td class="tbtd_caption">유형</td>
    			<td class="tbtd_content">
    				<form:select path="videoType" name="videoType" style="width:100px" onchange="videoType_onchange(this)">
    					<option value="SURFACE">지반지표</option>
    					<option value="SLAB">궤도슬라브</option>
    					<option value="LINING">터널라이닝</option>
    				</form:select>
    			</td>
    			<td class="tbtd_caption">계측기/시험</td>
    			<td class="tbtd_content">
    				<form:select path="measureType" style="width:100px">
    					<c:forEach var="combo" items="${comboList}" varStatus="status">
    						<form:option value="${combo.typeid}">${combo.typename}</form:option>
    					</c:forEach>
    				</form:select>
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
    			<td class="tbtd_caption">시작좌표</td>
    			<td class="tbtd_content">
    				경도&nbsp;<form:input path="startPosX" maxlength="10" cssClass="shorttxt"/>&nbsp;
    				위도&nbsp;<form:input path="startPosY" maxlength="10" cssClass="shorttxt"/>&nbsp;
    				고도&nbsp;<form:input path="startPosZ" maxlength="10" cssClass="shorttxt"/>
    			</td>
    			<td class="tbtd_caption">종료좌표</td>
    			<td class="tbtd_content">
    				경도&nbsp;<form:input path="endPosX" maxlength="10" cssClass="shorttxt"/>&nbsp;
    				위도&nbsp;<form:input path="endPosY" maxlength="10" cssClass="shorttxt"/>&nbsp;
    				고도&nbsp;<form:input path="endPosZ" maxlength="10" cssClass="shorttxt"/>
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
    			<td class="tbtd_caption">작업시작구간</td>
    			<td class="tbtd_content">
    				<form:input path="workSectionStart" maxlength="30" cssClass="txt"/>
    			</td>
    			<td class="tbtd_caption">작업종료구간</td>
    			<td class="tbtd_content">
    				<form:input path="workSectionEnd" maxlength="30" cssClass="txt"/>
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