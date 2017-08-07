<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="true" %>
<%@ taglib prefix="c"         uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"      uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring"    uri="http://www.springframework.org/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>하수맨홀 및 하수관</title>
    <style>
		.area {
		    position: absolute;
		    background: #fff;
		    border: 1px solid #888;
		    border-radius: 3px;
		    font-size: 12px;
		    top: -5px;
		    left: 15px;
		    padding:2px;
		}
		
		.info {
		    font-size: 12px;
		    padding: 5px;
		}
		.info .title {
		    font-weight: bold;
		}
	</style>

    <link type="text/css" rel="stylesheet" href="<c:url value='/css/egovframework/sample.css'/>"/>
    
</head>

<body style="text-align:center; margin:0 auto; display:inline; padding-top:100px;" onload="javascript:form_onLoad();">

<form id="mapForm" name="mapForm">

	<div id="map" style="width:100%;height:499px;position:relative;overflow:hidden;"></div>
	
	<!-- 버튼 -->
    <table id="btnTbl" width="100%" style="visibility:hidden">
    	<tr align="center"><td>
			<span class="btn_blue_l">
				<a href="javascript:btnRemoveall_onclick();">선택해제</a>
		        <img src="<c:url value='/images/egovframework/usdm/btn_bg_r.gif'/>" style="margin-left:6px;" alt=""/>
		    </span>&nbsp;&nbsp;&nbsp;&nbsp;
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
    <table id="btnTblReadonly" width="100%" style="visibility:hidden">
    	<tr align="center"><td>
			<span class="btn_blue_l">
				<a href="javascript:this.close();">닫기</a>
		        <img src="<c:url value='/images/egovframework/usdm/btn_bg_r.gif'/>" style="margin-left:6px;" alt=""/>
		    </span>&nbsp;&nbsp;&nbsp;&nbsp;
		</td></tr>
	</table>
	<!-- /버튼 --> 
	
	<!-- API key for localhost -->
	<!-- 
	<script type="text/javascript" src="//apis.daum.net/maps/maps3.js?apikey=94e1ab6949314b3111887925d09a68a9&libraries=services,clusterer,drawing"></script>
	    -->
	    
	<!-- API key for 129.254.221.32 -->
	<script type="text/javascript" src="//apis.daum.net/maps/maps3.js?apikey=63577337fdb613a0b520b5b9af9074a2&libraries=services,clusterer,drawing"></script>
	   
	<script>
		
		//---------------------
		// 지도 그리기 START --
		//---------------------
		
		// 지도를 담을 영역의 DOM 레퍼런스
		var container = document.getElementById('map');
		
		// 지도를 생성할 때 필요한 기본 옵션
		var options = {
				// 지도의 중심좌표(대전 월평역)
			center: new daum.maps.LatLng(36.358274, 127.364318),
		
			// 지도의 레벨(확대, 축소 정도)
			level: 2
		};
	
		// 지도 생성 및 객체 리턴
		var map = new daum.maps.Map(container, options), customOverlay = new daum.maps.CustomOverlay({});
		
		// 일반 지도와 스카이뷰로 지도 타입을 전환할 수 있는 지도타입 컨트롤을 생성합니다
		var mapTypeControl = new daum.maps.MapTypeControl();
		// 지도에 컨트롤을 추가해야 지도위에 표시됩니다
		// daum.maps.ControlPosition은 컨트롤이 표시될 위치를 정의하는데 TOPRIGHT는 오른쪽 위를 의미합니다
		map.addControl(mapTypeControl, daum.maps.ControlPosition.TOPRIGHT);

		// 지도 확대 축소를 제어할 수 있는 줌 컨트롤을 생성합니다
		var zoomControl = new daum.maps.ZoomControl();
		map.addControl(zoomControl,    daum.maps.ControlPosition.RIGHT);
		
		//-------------------
		// 지도 그리기 END --
		//-------------------
		
		//--------------------------
		// Global Variables START --
		//--------------------------
		
		// 마커 투명도
		var normalOpacity   = 0.3; // 평상시
		var selectedOpacity = 1.0; // 선택시
		
		// 선 굵기
		var normalStroke    = 3; // 평상시
		var highlightStroke = 6; // 하이라이트
		
		// 선 색깔
		var normalColor   = '#FF0000'; // 평상시
		var selectedColor = '#0100FF'; // 선택시
		
		var selectedManhole;				// 사용자가 선택한 맨홀ID
		var selectedPipe = new Array();		// 사용자가 선택한 하수관ID
		var readonly = 'N';				// 읽기전용여부
		
		//------------------------
		// Global Variables END --
		//------------------------
		
		//---------------------
		// 맨홀 그리기 START --
		//---------------------
		
		// 지도에 표시된 마커 객체를 가지고 있을 배열입니다
	    var markers = [];

		<c:forEach items="${manholeList}" var="drainmanhole">
			addMarker("${drainmanhole.id}", "${drainmanhole.geom}");
		</c:forEach>
		
		function addMarker(id, coordList) {
			// marker 객체생성
			var marker = new daum.maps.Marker({
			    map: map,
			    position: new daum.maps.LatLng(coordList.split(' ')[0], coordList.split(' ')[1]),
			    opacity: normalOpacity,
			    title: '맨홀ID: ' + id,
			    clickable: true
			});
			
			marker.customData = id + ',N';
			
			// 생성된 마커를 배열에 추가합니다
		    markers.push(marker);
			
			// 클릭 이벤트
			daum.maps.event.addListener(marker, 'click', function() {
				// 읽기전용으로 열린 경우 이벤트 무시
				if (readonly == 'Y') return;
				
				var manholeId = this.customData.split(',')[0];
				var selected  = this.customData.split(',')[1];
				
				// 선택
				if (selected == 'N') {
					selected = 'Y';
					selectedManhole = manholeId;
					
					for (var i = 0; i < markers.length; i++) {
						markers[i].customData = markers[i].customData.split(',')[0] + ',N';
				        markers[i].setOpacity(normalOpacity);
				    }
					
					this.setOpacity(selectedOpacity);
				}
				// 선택해제
				else {
					selected = 'N';
					selectedManhole = null;
					
					this.setOpacity(normalOpacity);
				}
				
				this.customData = manholeId + ',' + selected;
			});
			
			// 마우스오버 이벤트
			daum.maps.event.addListener(marker, 'mouseover', function() {
				// 선택되지 않은 맨홀인 경우
				if (this.customData.split(',')[1] == 'N') {
					this.setOpacity(selectedOpacity);
				}
			});
			
			// 마우스떠남 이벤트
			daum.maps.event.addListener(marker, 'mouseout', function() {
				// 선택되지 않은 맨홀인 경우
				if (this.customData.split(',')[1] == 'N') {
					this.setOpacity(normalOpacity);
				}
			});
		}
		
		//-------------------
		// 맨홀 그리기 END --
		//-------------------
		
		//-----------------------
		// 하수관 그리기 START --
		//-----------------------
		
		// 지도에 표시된 라인 객체를 가지고 있을 배열입니다
	    var polylines = [];
	    
		<c:forEach items="${pipeList}" var="drainpipe">
			addPolyline("${drainpipe.id}", "${drainpipe.geom}");
		</c:forEach>
		
		function addPolyline(id, coordList) {
			// 좌표 추출
			var coord = coordList.split(',');
			var points = new Array();
			
			// 좌표의 array 생성
			for (var i=0; i<coord.length; i++) {
		    	var temp = coord[i].split(' ');
		        var pos = new daum.maps.LatLng(temp[0], temp[1]);
		        
			    points.push(pos);
			}

			// polyline 객체생성
			var polyline = new daum.maps.Polyline({
				map: map, // 선이 표시될 지도객체
			    path: points, // 선을 구성하는 좌표배열 입니다
			    strokeWeight: normalStroke, // 선의 두께 입니다
			    strokeColor: normalColor, // 선의 색깔입니다
			    strokeOpacity: 0.7, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
			    strokeStyle: 'solid' // 선의 스타일입니다
			});
			
			polyline.customData = id + ',N';
			
		    polylines.push(polyline);
			
			// 클릭 이벤트
			daum.maps.event.addListener(polyline, 'click', function() {
				// 읽기전용으로 열린 경우 이벤트 무시
				if (readonly == 'Y') return;
				
				var pipeId   = this.customData.split(',')[0];
				var selected = this.customData.split(',')[1];
				
				// 선택
				if (selected == 'N') {
					selected = 'Y';
					selectedPipe.push(pipeId);
					
					var option = { 
						strokeColor: selectedColor,
						strokeWeight: highlightStroke
					};
				}
				// 선택해제
				else {
					selected = 'N';
					selectedPipe.splice(selectedPipe.indexOf(pipeId), 1);
					
					var option = {
						strokeColor: normalColor
					};
				}
				
			    this.setOptions(option);
			    
			    this.customData = pipeId + ',' + selected;
			});
			
			// 마우스오버 이벤트
			daum.maps.event.addListener(polyline, 'mouseover', function(mouseEvent) {
				var pipeId   = this.customData.split(',')[0];
				var selected = this.customData.split(',')[1];
				
				customOverlay.setContent('<div class="area">' + '하수관ID: ' + pipeId + '</div>');
		        
		        customOverlay.setPosition(mouseEvent.latLng); 
		        customOverlay.setMap(map)

		        if (selected == 'N') {
			        var option = { 
						strokeWeight: highlightStroke
					};
			        
					this.setOptions(option);
		        }
			});   

			// 마우스이동 이벤트
			daum.maps.event.addListener(polyline, 'mousemove', function(mouseEvent) {
		        customOverlay.setPosition(mouseEvent.latLng); 
		    });
			
			// 마우스떠남 이벤트
			daum.maps.event.addListener(polyline, 'mouseout', function() {
				var selected = this.customData.split(',')[1];
				
				customOverlay.setMap(null);
				
				if (selected == 'N') {
					var option = { 
						strokeWeight: normalStroke
					};
	
					this.setOptions(option);
				}
			});
		}
		
		//---------------------
		// 하수관 그리기 END --
		//---------------------
		
		// '선택해제' 버튼 클릭이벤트
		// 모든 선택을 초기화한다
		function btnRemoveall_onclick() {
			if (!confirm('선택된 모든 맨홀과 하수관의 선택을 취소합니다.\n계속하시겠습니까?'))
				return;
			
			selectedManhole = null;
			selectedPipe    = [];
			
			// 선택된 맨홀 초기화
			for (var i=0; i<markers.length; i++) {
				markers[i].customData = markers[i].customData.split(',')[0] + ',N';
		        markers[i].setOpacity(normalOpacity);
		    }
			
			// 선택된 하수관 초기화
			for (var i=0; i<polylines.length; i++) {
				polylines[i].customData = polylines[i].customData.split(',')[0] + ',N';
				
				var option = {
					strokeColor: normalColor,
					strokeWeight: normalStroke
				};
				polylines[i].setOptions(option);
			}
		}
		
		// '확인' 버튼 클릭이벤트
		// 선택된 맨홀ID와 하수관ID를 반환하고 창을 닫는다
		function btnEnter_onclick() {
			if (selectedManhole == null) {
				alert('선택된 맨홀이 없습니다.');
				return;
			}
			
			if (selectedPipe.length == 0) {
				alert('선택된 하수관이 없습니다.');
				return;
			}
			
			opener.document.detailForm.manholeFtrIdn.value = selectedManhole;
			opener.document.detailForm.pipeFtrIdn.value    = selectedPipe;
			
			window.close();
		}
		
		// 화면 load 이벤트
		// parent화면에서 선택된 맨홀ID와 하수관ID에 해당하는 객체를 선택된 상태로 설정한다
		function form_onLoad() {
			readonly = opener.document.getElementById("argReadonly").value;
			
			if (readonly == "Y") {
				document.getElementById("btnTbl").style.display = "none";
				document.getElementById("btnTblReadonly").style.visibility = "visible";
			}
			else {
				document.getElementById("btnTbl").style.visibility = "visible";
				document.getElementById("btnTblReadonly").style.display = "none";
			}
				
			var selectedManholeId = opener.document.detailForm.manholeFtrIdn.value; 
			var selectedPipeIds   = opener.document.detailForm.pipeFtrIdn.value.split(',');

			var manholeIndex = -1;
			var pipeIndex    = -1;
			
			// 선택된 맨홀이 있는 경우
			if (selectedManholeId != null || selectedManholeId != '') {
				// 맨홀 index 탐색
				for (var i=0; i<markers.length; i++) {
					if (markers[i].customData.split(',')[0] == selectedManholeId) {
						manholeIndex = i;
						break;
					}
				}
				
				if (manholeIndex >= 0) {
					// 선택된 맨홀ID에 설정
					selectedManhole = selectedManholeId;
					
					// 선택된 맨홀의 스타일 변경 
					markers[manholeIndex].setOpacity(selectedOpacity);
					
					// 선택된 맨홀의 속성 변경
					markers[manholeIndex].customData = selectedManholeId + ',Y';
					
					// 맨홀 위치로 지도 중심이동
					map.setCenter(markers[manholeIndex].getPosition());
				}
			}

			// 선택된 하수관이 있는 경우
			if (selectedPipeIds != null || selectedPipeIds.length > 0) {
				// 하수관 index 탐색
				for (var i=0; i<selectedPipeIds.length; i++) {
					for (var j=0; j<polylines.length; j++) {
						if (selectedPipeIds[i] == polylines[j].customData.split(',')[0]) {
							pipeIndex = j;
							break;
						}
					}
					
					if (pipeIndex >= 0) {
						// 선택된 하수관ID에 설정
						selectedPipe.push(selectedPipeIds[i]);
						
						// 선택된 하수관의 스타일 변경
						var option = { 
							strokeColor: selectedColor,
							strokeWeight: highlightStroke
						};
						polylines[pipeIndex].setOptions(option);
						
						// 선택된 하수관의 속성 변경
						polylines[pipeIndex].customData = selectedPipeIds[i] + ',Y';
					}
				}
			}
		}
		
	</script>

</form>
</body>
</html>