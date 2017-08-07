<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="true" %>
<%@ taglib prefix="c"         uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"      uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring"    uri="http://www.springframework.org/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>하수맨홀 및 하수관 선택</title>
    <link type="text/css" rel="stylesheet" href="<c:url value='/css/egovframework/sample.css'/>"/>
    
    <!-- script type="text/javascript" src="https://openapi.map.naver.com/openapi/v3/maps.js?clientId=J0pIKmd35JT8_HQBgyRN"></script-->
    <script type="text/javascript" src="http://openapi.map.naver.com/openapi/v2/maps.js?clientId=pBhSW8S0hKOk4pJCpDFA"></script>
    
</head>
<body style="text-align:center; margin:0 auto; display:inline; padding-top:100px;">

<form:form id="mapForm" name="mapForm">

	<div id="map" style="width:100%;height:499px;"></div>
	    
    <!-- 버튼 -->
    <table width="100%">
    	<tr align="center"><td>
			<span class="btn_blue_l">
				<a href="javascript:this.close();">취소</a>
		        <img src="<c:url value='/images/egovframework/usdm/btn_bg_r.gif'/>" style="margin-left:6px;" alt=""/>
		    </span>&nbsp;&nbsp;&nbsp;&nbsp;
			<span class="btn_blue_l">
				<a href="javascript:btnEnter_onclick();">확인</a>
		    	<img src="<c:url value='/images/egovframework/usdm/btn_bg_r.gif'/>" style="margin-left:6px;" alt=""/>
			</span>
		</td></tr>
	</table>
	<!-- /버튼 -->
	
	<script type="text/javascript">

		// 좌표계설정(WGS)
	    nhn.api.map.setDefaultPoint('LatLng');
	    
	 	// 지도 중심점(월평역)
	    var oCenterPoint = new nhn.api.map.LatLng(36.358274, 127.364318);
	    
	    var defaultLevel = 11;
	    var oMap = new nhn.api.map.Map(document.getElementById('map'), { 
	                                    point : oCenterPoint,
	                                    zoom : defaultLevel,
	                                    enableWheelZoom : true,
	                                    enableDragPan : true,
	                                    enableDblClickZoom : false,
	                                    mapMode : 0,
	                                    activateTrafficMap : false,
	                                    activateBicycleMap : false,
	                                    minMaxLevel : [ 1, 14 ]           });
	    
	    // 축척컨트롤
	    var oSlider = new nhn.api.map.ZoomControl();
	    oMap.addControl(oSlider);
	    oSlider.setPosition({
	            top : 10,
	            left : 10
	    });
	
	    // 일반/위성지도 컨트롤
	    var oMapTypeBtn = new nhn.api.map.MapTypeBtn();
	    oMap.addControl(oMapTypeBtn);
	    oMapTypeBtn.setPosition({
	            top : 10,
	            right : 10
	    });
	    
	    // 하수도geometry 표시
	    var selectedID = new Array();
	    
		<c:forEach items="${geomList}" var="drainpipe">
			addPolyline("${drainpipe.id}", "${drainpipe.geom}");
		</c:forEach>
		
		function addPolyline(id, coordList) {
			// 선 굵기
			var normalStroke    = 3;	// 평상시
			var highlightStroke = 6;	// 선택시
			
			// 선 색깔
			var normalColor    = '#FF0000';		// 평상시
			var highlightColor = '#FFE400';		// 하이라이트
			var selectedColor  = '#0054FF';		// 선택시
			
			var coord = coordList.split(',');
			var points = new Array();
			
			for (var i=0; i<coord.length; i++) {
		    	var temp = coord[i].split(' ');
		        var pos = new nhn.api.map.LatLng(temp[0], temp[1]);
		        
			    points.push(pos);
			}

		    var polyline = new nhn.api.map.Polyline(
		    	points,
		    	{
		            ID : id,
		            selected : 'N',
		            strokeColor : normalColor,
		            strokeWidth : normalStroke,
		            strokeOpacity : 0.7,
		            strokeStyle : 'solid'
		        });

		    // 클릭 이벤트
		    polyline.attach('click', function(e) {
		    	// 선택되지 않은 상태인 경우
		    	if (polyline.getStyle("selected") == 'N') {
		    		polyline.setStyle("selected", 'Y');
			    	polyline.setStyle("strokeColor", selectedColor);
			    	
			    	selectedID.push(polyline.getStyle("ID"));
		    	}
		    	// 선택된 상태인 경우
		    	else {
		    		polyline.setStyle("selected", 'N');
			    	polyline.setStyle("strokeColor", normalColor);
			    	
			    	//removeSelectedId(polyline.getStyle("ID"));
		    	}
		    	
		    });
		    
		    // 마우스오버 이벤트
		    polyline.attach('mouseenter', function(e) {
		    	polyline.setStyle("strokeWidth", highlightStroke);
		    	
		    	// 선택되지 않은 상태인 경우
		    	if (polyline.getStyle("selected") == 'N') {
		    		polyline.setStyle("strokeColor", highlightColor);
		    	}
		    });
		    
		    // 마우스떠남 이벤트
		    polyline.attach('mouseleave', function(e) {
		    	polyline.setStyle("strokeWidth", normalStroke);
		    	
		    	// 선택되지 않은 상태인 경우
		    	if (polyline.getStyle("selected") == 'N') {
			    	polyline.setStyle("strokeColor", normalColor);
		    	}
		    });
		   
		    oMap.addOverlay(polyline);
		}
		
		/*
		function removeSelectedId(id) {
			selectedID;
		}
		*/
		
		function btnEnter_onclick() {
			alert(selectedID);
		}

	</script>

</form:form>
</body>
</html>