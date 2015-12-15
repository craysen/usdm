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
    <!--

    /* 최초 화면호출 이벤트 */
    function form_onLoad()
    {
    	return;
    }
    
    /* 글 등록 화면 function */
    function btnRegister_onclick()
    {
       	document.listForm.action = "<c:url value='/underwater/insertUnderWaterFile.do'/>";
       	document.listForm.submit();
    }
        
    -->
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
	                    <font size="4"><b>&nbsp;지하수 센싱데이터 조회</b></font>
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
        	              <a href="javascript:btnRegister_onclick();"><spring:message code="button.create" /></a>
                          <img src="<c:url value='/images/egovframework/usdm/btn_bg_r.gif'/>" style="margin-left:6px;" alt=""/>
                      </span>
                  </li>
              </ul>
        	</div>
        	<br></br>
	    	
        	<!-- List -->
        	<div id="table">
        		<table width="100%" border="0" cellpadding="0" cellspacing="0">
        			<colgroup>
        				<col width="40"/>
        				<col width="40"/>
        				<col width="40"/>
        				<col width="40"/>
        				<col width="40"/>
        				<col width="40"/>
        				<col width="40"/>
        				<col width="40"/>
        				<col width="40"/>
        				<col width="40"/>
        				<col width="40"/>
        				<col width="40"/>
        			</colgroup>
        			<tr>
        				<th align="center" rowspan="2">센서ID</th>
        				<th align="center" rowspan="2">측정시간</th>
        				<th align="center" colspan="4">지하수</th>
        				<th align="center" colspan="3">상층토양</th>
        				<th align="center" colspan="3">하층토양</th>
        			</tr>
        			<tr>
        				<th align="center">수위</th>
        				<th align="center">온도</th>
        				<th align="center">전도도</th>
        				<th align="center">탁도</th>
        				<th align="center">습도</th>
        				<th align="center">전도도</th>
        				<th align="center">온도</th>
        				<th align="center">습도</th>
        				<th align="center">전도도</th>
        				<th align="center">온도</th>
        			</tr>
        			<c:forEach var="result" items="${resultList}" varStatus="status">
            			<tr>
            				<td align="center" class="listtd"><c:out value="${result.sensorid}"/></td>
            				<td align="center" class="listtd"><c:out value="${result.measuredate}"/>&nbsp;<c:out value="${result.measuretime}"/></td>
            				<td align="right"  class="listtd"><c:out value="${result.waterlevel}"/></td>
            				<td align="right"  class="listtd"><c:out value="${result.watertemp}"/></td>
            				<td align="right"  class="listtd"><c:out value="${result.waterconduct}"/></td>
            				<td align="right"  class="listtd"><c:out value="${result.waterturb}"/></td>
            				<td align="right"  class="listtd"><c:out value="${result.uppersoilmoist}"/></td>
            				<td align="right"  class="listtd"><c:out value="${result.uppersoilconduct}"/></td>
            				<td align="right"  class="listtd"><c:out value="${result.uppersoiltemp}"/></td>
            				<td align="right"  class="listtd"><c:out value="${result.lowersoilmoist}"/></td>
            				<td align="right"  class="listtd"><c:out value="${result.lowersoilconduct}"/></td>
            				<td align="right"  class="listtd"><c:out value="${result.lowersoiltemp}"/></td>
            			</tr>
        			</c:forEach>
        		</table>
        	</div>
        	<!-- /List -->
        	
        </div>
    </form:form>
</body>
</html>
