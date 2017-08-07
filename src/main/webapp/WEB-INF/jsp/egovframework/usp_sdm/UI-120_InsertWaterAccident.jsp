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
		var form = document.detailForm;
		
		//test_input();
		
		accidentType_onchange(form.accidentType);
    }
    
    /* 테스트용 */
    function test_input()
    {
		var form = document.detailForm;
	
		form.pipeFtrIdn.value = "10817";
		form.latitude.value = "36.35798";
		form.longitude.value = "127.36542";
		form.accidentTimeStr.value = "2016-12-30 13:34:21.052";
    }
    
    /* 저장 버튼 클릭 이벤트 */
    function btnSave_onclick()
    {
    	var form = document.detailForm;

    	if (validateForm()) {
    		form.action = "<c:url value='/waterpipe/saveWaterAccident.do'/>";
        	form.submit();
    	}
    }
    
    /* 필수입력 체크 */
    function validateForm()
    {
    	var form = document.detailForm;

    	// ======================
    	// 공통 필수입력 - START
    	// ======================
    	// 상수관ID
    	if (form.pipeFtrIdn.value == '') {
    		alert("상수관을 선택하세요.");
    		form.pipeFtrIdn.focus();
    		return false;
    	}
    	// 발생좌표:경도
    	if (form.latitude.value == '') {
    		alert("발생좌표를 입력하세요.");
    		form.latitude.focus();
    		return false;
    	}
    	// 발생좌표:위도
    	if (form.longitude.value == '') {
    		alert("발생좌표를 입력하세요.");
    		form.longitude.focus();
    		return false;
    	}
    	// 발생시간
    	if (form.accidentTimeStr.value == '') {
    		alert("발생시간을 입력하세요.");
    		form.accidentTimeStr.focus();
    		return false;
    	}
    	// ====================
    	// 공통 필수입력 - END
    	// ====================
    		
    	// =================================
    	// 사고유형에 따른 필수입력 - START
    	// =================================
    	switch (form.accidentType.value) {
    	case "burst":		// 사고유형: 파열
    	case "hole":		// 사고유형: 구멍
    		if (form.place.value == '') {
        		alert("위치를 선택하세요.");
        		form.place.focus();
        		return false;
        	}
    		break;
    		
    	case "subsidence":	// 사고유형: 침하
    		if (form.depth.value == '') {
        		alert("지표깊이를 입력하세요.");
        		form.depth.focus();
        		return false;
        	}
    		break;
    		
    	case "cavity":		// 사고유형: 동공
    		if (form.direction.value == '') {
        		alert("방향을 선택하세요.");
        		form.direction.focus();
        		return false;
        	}
    		if (form.depth.value == '') {
        		alert("지표깊이를 입력하세요.");
        		form.depth.focus();
        		return false;
        	}
    		if (form.length.value == '') {
        		alert("길이를 입력하세요.");
        		form.length.focus();
        		return false;
        	}
    		if (form.degree.value == '') {
        		alert("각도를 입력하세요.");
        		form.degree.focus();
        		return false;
        	}
    		break;
    	
    	case "crack":		// 사고유형: 균열
    		if (form.place.value == '') {
        		alert("위치를 선택하세요.");
        		form.place.focus();
        		return false;
        	}
    		break;
    		
    	case "pollution":	// 사고유형: 오염
    		if (form.depth.value == '') {
        		alert("지표깊이를 입력하세요.");
        		form.depth.focus();
        		return false;
        	}
    		break;
    	}
    	// ===============================
    	// 사고유형에 따른 필수입력 - END
    	// ===============================
    		
    	// =================================
    	// 사고형태에 따른 필수입력 - START
    	// =================================
    	switch (form.accidentShape.value) {
    	case "circle":		// 사고형태: 원형
    		if (form.diameter.value == '') {
        		alert("지름을 입력하세요.");
        		form.diameter.focus();
        		return false;
        	}
    		break;
    		
    	case "ellipse":		// 사고형태: 타원형
    		if (form.majorAxis.value == '') {
        		alert("장축길이를 입력하세요.");
        		form.majorAxis.focus();
        		return false;
        	}
    		if (form.minorAxis.value == '') {
        		alert("단축길이를 입력하세요.");
        		form.minorAxis.focus();
        		return false;
        	}
    		break;
    		
    	case "rectangle":	// 사고형태: 사각형
    		if (form.width.value == '') {
        		alert("가로길이를 입력하세요.");
        		form.width.focus();
        		return false;
        	}
    		if (form.height.value == '') {
        		alert("세로길이를 입력하세요.");
        		form.height.focus();
        		return false;
        	}
    		break;
    		
    	case "line":		// 사고형태: 직선
    	case "zigzag":		// 사고형태: 갈지자
    		if (form.length.value == '') {
        		alert("길이를 입력하세요.");
        		form.length.focus();
        		return false;
        	}
    		if (form.degree.value == '') {
        		alert("각도를 입력하세요.");
        		form.degree.focus();
        		return false;
        	}
    		break;
    	}
    	// ===============================
    	// 사고형태에 따른 필수입력 - END
    	// ===============================
    	
    	return confirm("저장하시겠습니까?");
    }
    
    /* 상수관선택 버튼 클릭 이벤트 */
    function btnFindPipe_onclick()
    {
    	var url = "<c:url value='/waterpipe/selectAccidentPipePopup.do'/>";
    	var openParam = "width=850, height=570, scrollbars=no, toolbar=no, resizable=no, status=no, location=no, menubar=no";
    	
    	var popupObj = window.open(url, "mapPopup", openParam);
    }
    
    /* 사고유형 변경 이벤트 */
    function accidentType_onchange(obj)
    {
    	var accidentShape = document.detailForm.accidentShape;
    	
    	// 초기화
    	for (var i=accidentShape.length; i>=0; i--)
    		accidentShape.options[i] = null;
    	
    	var circleOption    = document.createElement("option");
    	var ellipseOption   = document.createElement("option");
    	var rectangleOption = document.createElement("option");
    	var lineOption      = document.createElement("option");
    	var zigzagOption    = document.createElement("option");
    	
    	circleOption.text    = "원형";		circleOption.value    = "circle";
    	ellipseOption.text   = "타원형";	ellipseOption.value   = "ellipse";
    	rectangleOption.text = "사각형";	rectangleOption.value = "rectangle";
    	lineOption.text      = "직선";		lineOption.value      = "line";
    	zigzagOption.text    = "갈지자";	zigzagOption.value    = "zigzag";
	    
    	// 사고유형 값에 따른 사고형태 값 변경
    	switch (obj.value) {
    	case "burst":		// 사고유형: 파열
    	case "hole":		// 사고유형: 구멍
    	case "subsidence":	// 사고유형: 침하
    	case "cavity":		// 사고유형: 동공
    		accidentShape.options.add(circleOption);	// 원형
    		accidentShape.options.add(ellipseOption);	// 타원형
    		accidentShape.options.add(rectangleOption);	// 사각형
    		break;
    		
    	case "crack":		// 사고유형: 균열
    		accidentShape.options.add(lineOption);		// 직선
    		accidentShape.options.add(zigzagOption);	// 갈지자
    		break;
    		
    	case "pollution":	// 사고유형: 오염(TBD)
    		break;
    	}
    	
    	accidentShape_onchange(document.detailForm.accidentShape);
    }
    
    /* 사고형태 변경 이벤트 */
    function accidentShape_onchange(obj)
    {
    	var form = document.detailForm;
    	
    	/*
    	form.direction.style.background-color = "#EAEAEA"; 
    	
    	// 사고형태 값에 따른 사고명세 입력양식 변경
    	switch (obj.value) {
    	case "circle":		// 사고형태: 원형
    		break;
    		
    	case "ellipse":		// 사고형태: 타원형
    		break;
    		
    	case "rectangle":	// 사고형태: 사각형
    		break;
    		
    	case "line":		// 사고형태: 직선
    	case "zigzag":		// 사고형태: 갈지자
    		break;
    	}
    	*/
    }
        
    </script>
</head>
<body style="text-align:center; margin:0 auto; display:inline; padding-top:100px;" onload="javascript:form_onLoad();">

<form:form commandName="accidentVO" id="detailForm" name="detailForm" enctype="multipart/form-data">
	<input type="hidden" id="argReadonly" value="N"/>
    <div id="content_pop">
    
    	<!-- 타이틀 -->
    	<div id="title">
    	<table width="100%" border="0">
    		<tr>
    			<td>
    				<img src="<c:url value='/images/egovframework/usdm/title_dot.gif'/>"/>
                    <font size="4"><b>&nbsp;상수도 사고정보 입력</b></font>
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
    	
    	<div id="table">
    	<table width="100%" border="1" cellpadding="0" cellspacing="0" style="bordercolor:#D3E2EC; bordercolordark:#FFFFFF; BORDER-TOP:#C2D0DB 2px solid; BORDER-LEFT:#ffffff 1px solid; BORDER-RIGHT:#ffffff 1px solid; BORDER-BOTTOM:#C2D0DB 1px solid; border-collapse: collapse;">
    		<colgroup>
    			<col width="100"/>
    			<col width="?"/>
    			<col width="100"/>
    			<col width="?"/>
    		</colgroup>
    		<tr>
    			<td class="tbtd_caption">상수관ID</td>
    			<td class="tbtd_content">
	    			<span class="btn_blue_l">
	                    <a href="javascript:btnFindPipe_onclick();">선택</a>
	                    <img src="<c:url value='/images/egovframework/usdm/btn_bg_r.gif'/>" style="margin-left:6px;"/>
	                </span>
	                &nbsp;
					<form:input path="pipeFtrIdn" maxlength="30" cssClass="txt"/>
				</td>
    			<td class="tbtd_caption">발생좌표</td>
    			<td class="tbtd_content">
    				경도&nbsp;<form:input path="latitude" maxlength="10" cssClass="txt"/>&nbsp;
    				위도&nbsp;<form:input path="longitude" maxlength="10" cssClass="txt"/>
    			</td>
    		</tr>
    		<tr>
    			<td class="tbtd_caption">발생시간</td>
    			<td class="tbtd_content" colspan="3">
    				<form:input path="accidentTimeStr" maxlength="30" cssClass="longtxt"/>
    			</td>
    		</tr>
    		<tr>
    			<td class="tbtd_caption">사고유형</td>
    			<td class="tbtd_content">
    				<form:select path="accidentType" name="accidentType" style="width:70px" onchange="accidentType_onchange(this)">
    					<option value="burst">파열</option>
    					<option value="crack">균열</option>
    					<!--option value="hole">구멍</option-->
    					<!--option value="subsidence">침하</option-->
    					<!--option value="cavity">동공</option-->
    					<!--option value="pollution">오염</option-->
    				</form:select>
    			</td>
    			<td class="tbtd_caption">사고형태</td>
    			<td class="tbtd_content" colspan="3">
    				<form:select path="accidentShape" name="accidentShape" style="width:70px" onchange="accidentShape_onchange(this)">
    				</form:select>
    			</td>
    		</tr>
    		<tr id="diameterTr">
    			<td class="tbtd_caption">지름</td>
    			<td class="tbtd_content" colspan="3">
    				<form:input path="diameter" maxlength="30" cssClass="txt"/>
    			</td>
    		</tr>
    		<tr id="axisTr">
    			<td class="tbtd_caption">장축</td>
    			<td class="tbtd_content">
    				<form:input path="majorAxis" maxlength="30" cssClass="txt"/>
    			</td>
    			<td class="tbtd_caption">단축</td>
    			<td class="tbtd_content">
    				<form:input path="minorAxis" maxlength="30" cssClass="txt"/>
    			</td>
    		</tr>
    		<tr id="rectangleTr">
    			<td class="tbtd_caption">가로</td>
    			<td class="tbtd_content">
    				<form:input path="width" maxlength="30" cssClass="txt"/>
    			</td>
    			<td class="tbtd_caption">세로</td>
    			<td class="tbtd_content">
    				<form:input path="height" maxlength="30" cssClass="txt"/>
    			</td>
    		</tr>
    		<tr id="depthTr">
    			<td class="tbtd_caption">지표깊이</td>
    			<td class="tbtd_content" colspan="3">
    				<form:input path="depth" maxlength="30" cssClass="txt"/>
    			</td>
    		</tr>
    		<tr id="lengthTr">
    			<td class="tbtd_caption">길이</td>
    			<td class="tbtd_content" colspan="3">
    				<form:input path="length" maxlength="30" cssClass="txt"/>
    			</td>
    		</tr>
    		<tr id="degreeTr">
    			<td class="tbtd_caption">각도</td>
    			<td class="tbtd_content" colspan="3">
    				<form:input path="degree" maxlength="30" cssClass="txt"/>
    			</td>
    		</tr>
    		<tr id="directionTr">
    			<td class="tbtd_caption">방향</td>
    			<td class="tbtd_content" colspan="3">
    				<form:select path="direction" name="direction" style="width:70px">
    					<option value="">----</option>
    					<option value="horizontality">수평</option>
    					<option value="verticality">수직</option>
    				</form:select>
    			</td>
    		</tr>
    		<tr id="placeTr">
    			<td class="tbtd_caption">위치</td>
    			<td class="tbtd_content" colspan="3">
    				<form:select path="place" name="place" style="width:70px">
    					<option value="">----</option>
    					<option value="ceiling">천정</option>
    					<option value="floor">바닥</option>
    					<option value="wall">벽</option>
    				</form:select>
    			</td>
    		</tr>
    	</table>
      </div>
    </div>
</form:form>
</body>
</html>