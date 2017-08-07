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

    /* 목록 버튼 클릭 이벤트 */
    function btnList_onclick()
    {
    	form = document.detailForm;

   		form.action = "<c:url value='/drainpipe/selectDrainAccidentList.do'/>";
       	form.submit();
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
    
    /* 하수관보기 버튼 클릭 이벤트 */
    function btnFindPipe_onclick()
    {
    	var url = "<c:url value='/drainpipe/selectAccidentPipePopup.do'/>";
    	var openParam = "width=850, height=570, scrollbars=no, toolbar=no, resizable=no, status=no, location=no, menubar=no";
    	
    	var popupObj = window.open(url, "mapPopup", openParam);
    }
    
    </script>
</head>
<body style="text-align:center; margin:0 auto; display:inline; padding-top:100px;" onload="javascript:form_onLoad();">

<form:form commandName="accidentVO" id="detailForm" name="detailForm" enctype="multipart/form-data">
	<input type="hidden" id="argReadonly" value="Y"/>
    <div id="content_pop">
    	<!-- 타이틀 -->
    	<div id="title">
    	<table width="100%" border="0">
    		<tr>
    			<td>
    				<img src="<c:url value='/images/egovframework/usdm/title_dot.gif'/>"/>
                    <font size="4"><b>&nbsp;하수도 사고정보 상세</b></font>
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
    		<!--
    			<li style="display:none;">
                    <span class="btn_blue_l">
                        <a href="javascript:btnSave_onclick();">저장</a>
                        <img src="<c:url value='/images/egovframework/usdm/btn_bg_r.gif'/>" style="margin-left:6px;" alt=""/>
                    </span>
                </li>
            -->
                <li>
                    <span class="btn_blue_l">
                        <a href="javascript:btnList_onclick();">목록</a>
                        <img src="<c:url value='/images/egovframework/usdm/btn_bg_r.gif'/>" style="margin-left:6px;"/>
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
    			<td class="tbtd_caption">하수관ID</td>
    			<td class="tbtd_content">
    				<span class="btn_blue_l">
	                    <a href="javascript:btnFindPipe_onclick();">보기</a>
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
    				<form:input path="accidentType" maxlength="10" cssClass="txt"/>
    			</td>
    			<td class="tbtd_caption">사고형태</td>
    			<td class="tbtd_content" colspan="3">
    				<form:input path="accidentShape" maxlength="10" cssClass="txt"/>
    			</td>
    		</tr>
    		<tr>
    			<td class="tbtd_caption">사고명세</td>
    			<td class="tbtd_content" colspan="3">
    				<form:input path="accidentDesc" maxlength="100" cssClass="longtxt"/>
    			</td>
    		</tr>
    	</table>
      </div>
    </div>
</form:form>
</body>
</html>