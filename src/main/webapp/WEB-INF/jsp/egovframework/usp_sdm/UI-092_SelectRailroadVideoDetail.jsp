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
    		form.action = "<c:url value='/railroad/uploadVideoFile.do'/>";
        	form.submit();
    	}
    }

    /* 목록 버튼 클릭 이벤트 */
    function btnList_onclick()
    {
    	form = document.detailForm;

   		form.action = "<c:url value='/railroad/selectRailroadFileList.do'/>";
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
                    <font size="4"><b>&nbsp;도시철도 센싱 데이터 파일 상세</b></font>
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
    			<li style="display:none;">
                    <span class="btn_blue_l">
                        <a href="javascript:btnSave_onclick();">저장</a>
                        <img src="<c:url value='/images/egovframework/usdm/btn_bg_r.gif'/>" style="margin-left:6px;" alt=""/>
                    </span>
                </li>
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
    			<td class="tbtd_caption">파일</td>
    			<td class="tbtd_content" colspan="3">
    				${railroadVideoVO.fileName}&nbsp;
    				<a href="#" onclick="window.open(encodeURI('<c:url value='/download/downloadFile.do?'/>filePath=${railroadVideoVO.filePath}&fileRealName=${railroadVideoVO.fileRealName}&fileName=${railroadVideoVO.fileName}'))">
    					[다운로드]
    				</a>
    			</td>
    		</tr>
    		<tr>
    			<td class="tbtd_caption">유형</td>
    			<td class="tbtd_content">
    				${railroadVideoVO.videoTypeName}
    			</td>
    			<td class="tbtd_caption">계측기/시험</td>
    			<td class="tbtd_content">
    				${railroadVideoVO.measureTypeName}
    			</td>
    		</tr>
    		<tr>
    			<td class="tbtd_caption">해상도</td>
    			<td class="tbtd_content">
    				${railroadVideoVO.resolutionW}
    				&nbsp;*&nbsp;
    				${railroadVideoVO.resolutionH}
    			</td>
    			<td class="tbtd_caption">화소</td>
    			<td class="tbtd_content">
    				${railroadVideoVO.numPixel}
    			</td>
    		</tr>
    		<tr>
    			<td class="tbtd_caption">시작좌표</td>
    			<td class="tbtd_content">
    				경도 ${railroadVideoVO.startPosX}, 위도 ${railroadVideoVO.startPosY}, 고도 ${railroadVideoVO.startPosZ}
    			</td>
    			<td class="tbtd_caption">종료좌표</td>
    			<td class="tbtd_content">
    				경도 ${railroadVideoVO.endPosX}, 위도 ${railroadVideoVO.endPosY}, 고도 ${railroadVideoVO.endPosZ}
    			</td>
    		</tr>
    		<tr>
    			<td class="tbtd_caption">촬영시작시각</td>
    			<td class="tbtd_content" colspan="3">
    				${railroadVideoVO.recStartTime}
    			</td>
    		</tr>
    		<tr>
    			<td class="tbtd_caption">촬영종료시각</td>
    			<td class="tbtd_content" colspan="3">
    				${railroadVideoVO.recEndTime}
    			</td>
    		</tr>
    		<tr>
    			<td class="tbtd_caption">작업시작구간</td>
    			<td class="tbtd_content">
    				${railroadVideoVO.workSectionStart}
    			</td>
    			<td class="tbtd_caption">작업종료구간</td>
    			<td class="tbtd_content">
    				${railroadVideoVO.workSectionEnd}
    			</td>
    		</tr>
    		<tr>
    			<td class="tbtd_caption">손상여부</td>
    			<td class="tbtd_content" colspan="3">
    				${railroadVideoVO.damage}
    			</td>
    		</tr>
    		<tr>
    			<td class="tbtd_caption">POI</td>
    			<td class="tbtd_content" colspan="3">
    				${railroadVideoVO.POI}
    			</td>
    		</tr>
    	</table>
      </div>
    </div>
</form:form>
</body>
</html>