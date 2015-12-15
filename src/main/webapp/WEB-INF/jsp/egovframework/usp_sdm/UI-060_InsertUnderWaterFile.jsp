<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="true" %>
<%@ taglib prefix="c"         uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"      uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring"    uri="http://www.springframework.org/tags"%>
<%
  /**
  * @Class Name : UI-060_InsertUnderWaterFile.jsp
  * @Description : 지하수 파일 입력 화면
  * @Modification Information
  *
  *  2015.10.15		hjlee		최초 생성
  *
  */
%>
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
		form.gwID.value = "1";
		form.panID.value = "1";
		form.snID.value = "1";
		form.tdID.value = "1";
		
		return;
    }

    /* 파일 유형 변경 이벤트 */
    function fileType_onchange(obj)
    {
    	var form = document.detailForm;
    	
    	if (form.file.value != "") {
    		if (!confirm("선택된 파일 정보가 삭제됩니다. 계속하시겠습니까?")) {
    			return false;
    		}
    		
    		form.file.value = "";
    	}
    	
    	if (obj.value == "1") {
    		form.file.accept = "text/plain";
    	}
    	else if (obj.value == "2") {
    		form.file.accept = "application/vnd.ms-excel, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    	}
    	
    	return true;
    }
    
    /* 첨부파일 변경 이벤트 */
    function file_onchange(obj)
    {
    	var form = document.detailForm;
    	var file_type = form.fileType.value;
    	var extension = extractExtFromFileName(obj.value);
    	
    	if (extension) {
    		if (file_type == "1") {
    			if (extension != ".txt") {
    				alert("텍스트 파일(.txt)을 선택해 주십시오.");
    				form.file.value = "";
    				
    				return false;
    			}
    		}
    		else if (form.fileType.value == "2") {
    			if (extension != ".xls" && extension != ".xlsx") {
    				alert("엑셀 파일(.xls 또는 .xlsx)을 선택해 주십시오.");
    				form.file.value = "";
    				
    				return false;
    			}
    		}
    	}
    	
    	return true;
    }
    
    /* 저장 버튼 클릭 이벤트 */
    function btnSave_onclick()
    {
    	var form = document.detailForm;

    	if (validateForm()) {
    		if (form.fileType.value == "1") {
    			form.action = "<c:url value='/underwater/insertUnderWaterText.do'/>";
    		}
    		else if (form.fileType.value == "2") {
    			form.action = "<c:url value='/underwater/insertUnderWaterExcel.do'/>";
    		}
    			
        	form.submit();
    	}
    }
    
    /* 필수입력 체크 */
    function validateForm()
    {
    	var form = document.detailForm;
    	
    	if (form.file.value == "") {
    		alert("업로드할 파일을 선택하십시오.");
    		return false;
    	}
    	
    	return true;
    }
    
    /* 파일명에서 확장자 추출 */
    function extractExtFromFileName(filename) {
   	   var len  = filename.length;
   	   var last = filename.lastIndexOf(".");
   	   
   	   if (last == -1) return false;

   	   return filename.substring(last, len).toLowerCase();
    }
        
    </script>
</head>
<body style="text-align:center; margin:0 auto; display:inline; padding-top:100px;" onload="javascript:form_onLoad();">

<form:form commandName="underWaterVO" id="detailForm" name="detailForm" enctype="multipart/form-data">
    <div id="content_pop">
    	<!-- 타이틀 -->
    	<div id="title">
    	<table width="100%" border="0">
    		<tr>
    			<td>
    				<img src="<c:url value='/images/egovframework/usdm/title_dot.gif'/>"/>
                    <font size="4"><b>&nbsp;지하수 센싱데이터 파일 입력</b></font>
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
    				<input name="gwID"  style="height:18px; width:30px;"/>
    				<input name="panID" style="height:18px; width:30px;"/>
    				<input name="snID"  style="height:18px; width:30px;"/>
    				<input name="tdID"  style="height:18px; width:30px;"/>&nbsp;
    				<button onclick="btnFindSensor_onclick();">센서선택</button>
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
    		</colgroup>
    		<tr>
    			<td class="tbtd_caption">파일 유형</td>
    			<td class="tbtd_content">
    				<select id="fileType" name="fileType" style="width:150px" onchange="fileType_onchange(this)" value="1">
    					<option value="1">텍스트 (.txt)</option>
    					<option value="2">엑셀 (.xls, .xlsx)</option>
    				</select>
    			</td>
    		</tr>
    		<tr>
    			<td class="tbtd_caption">파일</td>
    			<td class="tbtd_content">
    				<input id="file" name="file" type="file" accept="text/plain" onchange="file_onchange(this)"/>
    			</td>
    		</tr>
    	</table>
        </div>
    	
    </div>
</form:form>
</body>
</html>