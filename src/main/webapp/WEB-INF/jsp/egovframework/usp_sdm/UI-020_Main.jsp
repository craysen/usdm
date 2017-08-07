<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="true" %>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <script type="text/javascript" src="https://openapi.map.naver.com/openapi/v3/maps.js?clientId=J0pIKmd35JT8_HQBgyRN"></script>
    
    <title>SDM</title>
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
    
    <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
    <script type="text/javaScript" language="javascript" defer="defer">

    /* 최초 화면호출 이벤트 */
    function form_onLoad()
    {
    	return;
    }
    
    /*
 	// connection/login
    function login_onclick()
    {
    	var json = {"userID":"ws", "userPW":"mws"};
    	
    	$.ajax({
    		  type			: "POST"
    		, url			: "http://localhost:8080/usp_sdm/connection/login"
    		, data			: JSON.stringify(json)
    		, dataType		: "json"
    		, contentType	: "application/json"
    		, success 		: function(response) {
				    			responseJson = response.responseCode + " " + response.responseMsg;
								document.mainForm.jsontext.value = responseJson;
    						}
    	});
    }
 	
 	// connection/logout
    function logout_onclick()
    {
    	var json = {"userID":"mws", "sessionKey":"gpq77hfulimw61b1gme7"};
    	
    	$.ajax({
    		  type			: "POST"
    		, url			: "http://localhost:8080/usp_sdm/connection/logout"
    		, data			: JSON.stringify(json)
    		, dataType		: "json"
    		, contentType	: "application/json"
    		, success 		: function(response) {
				    			responseJson = response.responseCode + " " + response.responseMsg;
								document.mainForm.jsontext.value = responseJson;
    						}
    	});
    }
 
    // resource/gateway
    function gateway_onclick()
    {
    	var json = {"gwID": "ETRI_GW_001",
	    			"url" : "http://192.168.0.50:8989",
	    			"manufacturer" : "ETRI",
	    			"productNo" : "13-ETRI-256",
	    			"location": "ETRI_12동_506호",
	    			"coordinate": {"longitude":"135.253", "latitude":"30.254", "altitude":"55.25"},
	    			"dateTime" : 12312312312,
	    			"supportedTransportProtocolList" : ["XML_OVER_HTTP"],
	    			"supportedTransportConnectionControlList" : ["CLOSE", "KEEP_ALIVE"],
	    			"supportedTransportDirectionList" : ["FROM_MIDDLEWARE_TO_RESOURCE", "FROM_RESOURCE_TO_MIDDLEWARE"],
	    			"supportedOperationList" : ["COMMAND_CONTROL", "COMMAND_USERDEFINED", "COMMAND_MONITORING", "COMMAND_PUSH", "COMMAND_REALTIME"],
	    			"supportedAttributeList" : ["ATTRIBUTE_DURATION", "ATTRIBUTE_RESET"],
	    			"supportedSensorServiceCenterList" : ["cosmos"],
	    			"panIDList" : ["1", "2"],
	    			"monitoringMode" : "MONITORING_PULL",
	    			"monitoringPeriod" : 60};
    	
    	$.ajax({
    		  type			: "POST"
    		, url			: "http://localhost:8080/usp_sdm/resource/gateway"
    		, data			: JSON.stringify(json)
    		, dataType		: "json"
    		, contentType	: "application/json"
    		, success 		: function(response) {
				    			responseJson = response.responseCode + " " + response.responseMsg;
								document.mainForm.jsontext.value = responseJson;
    						}
    	});
    }
    
    // resource/pan
    function pan_onclick()
    {
    	var json = {"gwID" : "ETRI_GW_001",
	    		    "panID" : "1",
	    		    "topology" : "STAR",
		    		"protocolStack" : "ZIGBEE",
		    		"panChannel" : 11,
		    		"supportedChannelList" : ["12", "13"],
		    		"supportedTopologyList" : ["STAR"],
		    		"supportedProtocolStackList" : ["ZIGBEE"],
		    		"supportedOperationList" : ["COMMAND_CONTROL", "COMMAND_USERDEFINED", "COMMAND_MONITORING", "COMMAND_PUSH", "COMMAND_REALTIME"],
		    		"supportedAttributeList" : ["ATTRIBUTE_DURATION", "ATTRIBUTE _RESET"],
		    		"snIDList" : ["1", "2"]};
    	
    	$.ajax({
    		  type			: "POST"
    		, url			: "http://localhost:8080/usp_sdm/resource/pan"
    		, data			: JSON.stringify(json)
    		, dataType		: "json"
    		, contentType	: "application/json"
    		, success 		: function(response) {
				    			responseJson = response.responseCode + " " + response.responseMsg;
								document.mainForm.jsontext.value = responseJson;
    						}
    	});
    }
    
    // resource/node
    function node_onclick()
    {
    	var json = {"gwID" : "ETRI_GW_001",
	    			"panID" : "1",
	    			"snID" : "2",
	    			"globalID" : "1294583040",
	    			"manufacturer" : "ETRI",
	    			"productNo" : "13-ETRI-1234",
	    			"location": "ETRI_12동_506호",
	    			"coordinate": {"longitude":"135.253", "latitude":"30.254", "altitude":"55.25"},
	    			"role" : "LEAF",
	    			"roleList" : ["LEAF"],
	    			"parentNodeIDList" : ["1", "3"],
	    			"supportedOperationList" : ["COMMAND_CONTROL", "COMMAND_USERDEFINED", "COMMAND_MONITORING", "COMMAND_PUSH", "COMMAND_REALTIME"],
	    			"supportedAttributeList" : ["ATTRIBUTE_DURATION", "ATTRIBUTE _RESET"],
	    			"tdIDList" : ["1", "2"],
	    			"monitoringMode" : "MONITORING_PULL",
	    			"monitoringPeriod" : 60};
    	
    	$.ajax({
    		  type			: "POST"
    		, url			: "http://localhost:8080/usp_sdm/resource/node"
    		, data			: JSON.stringify(json)
    		, dataType		: "json"
    		, contentType	: "application/json"
    		, success 		: function(response) {
				    			responseJson = response.responseCode + " " + response.responseMsg;
								document.mainForm.jsontext.value = responseJson;
    						}
    	});
    }
    
    // resource/transducer
    function transducer_onclick()
    {
    	var json = {"gwID" : "ETRI_GW_001",
	    			"panID" : "1",
	    			"snID" : "2",
	    			"tdID" : "1",
	    			"manufacturer" : "ETRI",
	    			"productNo" : "15-ETRI-1234",
	    			"transducerCategory" : "SENSOR", 
	    			"transducerType" : "ILLUMINATION", 
	    			"unit" : "LUX ",
	    			"dataType" : "DOUBLE",
	    			"range" : {"min":0.0, "max":100.0, "offset":10.0}, 
	    			"level" : null,
	    			"supportedOperationList" : ["COMMAND_CONTROL", "COMMAND_USERDEFINED", "COMMAND_MONITORING", "COMMAND_PUSH", "COMMAND_REALTIME"],
	    			"supportedAttributeList" : ["ATTRIBUTE_DURATION", "ATTRIBUTE _RESET"]};
    	
    	$.ajax({
    		  type			: "POST"
    		, url			: "http://localhost:8080/usp_sdm/resource/transducer"
    		, data			: JSON.stringify(json)
    		, dataType		: "json"
    		, contentType	: "application/json"
    		, success 		: function(response) {
				    			responseJson = response.responseCode + " " + response.responseMsg;
								document.mainForm.jsontext.value = responseJson;
    						}
    	});
    }
    
    // resource/update
    function update_onclick()
    {
    	var json = {"oldAddress" : "2:::", "newAddress" : "1:::"};
    	
    	$.ajax({
    		  type			: "POST"
    		, url			: "http://localhost:8080/usp_sdm/resource/update"
    		, data			: JSON.stringify(json)
    		, dataType		: "json"
    		, contentType	: "application/json"
    		, success 		: function(response) {
				    			responseJson = response.responseCode + " " + response.responseMsg;
								document.mainForm.jsontext.value = responseJson;
    						}
    	});
    }
    
    // resource/delete
    function delete_onclick()
    {
    	var json = {"addressList" : ["1:::"]};
    	
    	$.ajax({
    		  type			: "POST"
    		, url			: "http://localhost:8080/usp_sdm/resource/delete"
    		, data			: JSON.stringify(json)
    		, dataType		: "json"
    		, contentType	: "application/json"
    		, success 		: function(response) {
				    			responseJson = response.responseCode + " " + response.responseMsg;
								document.mainForm.jsontext.value = responseJson;
    						}
    	});
    }
    
    // report/sensingValue
    function sensing_onclick()
    {
    	var json = {"reqID" : "384847239347",
    			 	"sensingNodeList" : [ { "snAddress" : "ETRI_GW_001:1:2",
    			 							"reportTime" : 1447230651596,
    		    							"sensingValueList" : [ { "tdID":"1", "sensingTime":1447230651596, "sensorType":"ILLUMINATION", "sensingValue":"52.4" },
										   						   { "tdID":"1", "sensingTime":1447230651597, "sensorType":"ILLUMINATION", "sensingValue":"122.0" }] } ] };
    	
    	$.ajax({
    		  type			: "POST"
    		, url			: "http://localhost:8080/usp_sdm/report/sensingValue"
    		, data			: JSON.stringify(json)
    		, dataType		: "json"
    		, contentType	: "application/json"
    		, success 		: function(response) {
				    			var responseJson = response.responseCode + " " + response.responseMsg + "\n";
								document.mainForm.jsontext.value = responseJson;
    						}
    	});
    }
    
    // report/actuationResult
    function actuation_onclick()
    {
    	var json = {"reqID" : "384847239347",
    				"actuationValueList" : [ {"tdAddress":"1:0:0:5", "actuationResult":"OFF"}, {"tdAddress":"1:0:0:8", "actuationResult":"OFF"} ]};

    	
    	$.ajax({
    		  type			: "POST"
    		, url			: "http://localhost:8080/usp_sdm/report/actuationResult"
    		, data			: JSON.stringify(json)
    		, dataType		: "json"
    		, contentType	: "application/json"
    		, success 		: function(response) {
    							var responseJson = response.responseCode + " " + response.responseMsg + "\n";
								document.mainForm.jsontext.value = responseJson;
    						}
    	});
    }
    
    // notice/statusResource
    function statusResource_onclick()
    {
    	var json = {"targetAddress": "1:::", "statusCode": "NONRESPONSIVE"};
    	
    	$.ajax({
    		  type			: "POST"
    		, url			: "http://localhost:8080/usp_sdm/notice/statusResource"
    		, data			: JSON.stringify(json)
    		, dataType		: "json"
    		, contentType	: "application/json"
    		, success 		: function(response) {
    							var responseJson = response.responseCode + " " + response.responseMsg + "\n";
								document.mainForm.jsontext.value = responseJson;
    						}
    	});
    }
    
    // notice/reboot
    function reboot_onclick()
    {
    	var json = {"sessionKey": "311q07ooo38xej17l6pe"};
    	
    	$.ajax({
    		  type			: "POST"
    		, url			: "http://localhost:8080/usp_sdm/notice/reboot"
    		, data			: JSON.stringify(json)
    		, dataType		: "json"
    		, contentType	: "application/json"
    		, success 		: function(response) {
    							var responseJson = response.responseCode + " " + response.responseMsg + "\n";
								document.mainForm.jsontext.value = responseJson;
    						}
    	});
    }
    
 	// query/latestValue
    function latest_onclick()
    {
		var json = {"sessionKey" : "311q07ooo38xej17l6pe",
			    	"targetList" : [ "FF:FF:FF:FF" ],
			    	"sensingTypeList" : [ "HUMIDITY", "NOISE" ],
			    	"conditionList" : [
			    	                   	{"tdType":"HUMIDITY", "operation":"GT", "value":"50", "logicalOp" : ""},
			                     		{"tdType":"NOISE", "operation":"GT", "value":"20", "logicalOp" : "OR"},
			                     		{"tdType":"NOISE", "operation":"LT", "value":"200", "logicalOp" : ""} ]
				};
    	
    	$.ajax({
    		  type			: "POST"
    		, url			: "http://localhost:8080/usp_sdm/query/latestValue"
    		, data			: JSON.stringify(json)
    		, dataType		: "json"
    		, contentType	: "application/json"
    		, success 		: function(response) {
    							var responseJson = response.responseCode + " " + response.responseMsg + "\n";
    							document.mainForm.jsontext.value = responseJson; 
    						}
    	});
    }
 	
 	// query/spatioTemporal
    function spatio_onclick()
    {
    	var json = {"sessionKey" : "311q07ooo38xej17l6pe",
			    	"targetList" : [ "FF:FF:FF:FF" ],
			    	"sensingTypeList" : [ "HUMIDITY", "NOISE" ],
			    	"conditionList" : [
			    	                   	{"tdType":"HUMID", "operation":"GT", "value":"50", "logicalOp" : " "},
			                     		{"tdType":"NOISE", "operation":"GT", "value":"20", "logicalOp" : "OR"},
			                     		{"tdType":"NOISE", "operation":"LT", "value":"200", "logicalOp" : ""}
			                     	],
    				"temporalCondition" : { "startTime":"2015-11-18T00:30:00.000+09:00", "endTime":"2015-11-19T14:30:00.000+09:00" },
    				//"spatialCondition" : { "type" : "Polygon", "coordinates": [ ["0.0", "-10.0"], ["200.0", "0.0"], ["200.0", "200.0"], ["0.0", "200.0"], ["-10.0", "-10.0"] ] }
    				//"spatialCondition" : { "type" : "Circle", "center" : ["0", "0"], "radius" : "1000" }
				};
	
		$.ajax({
			  type			: "POST"
			, url			: "http://localhost:8080/usp_sdm/query/spatioTemporal"
			, data			: JSON.stringify(json)
			, dataType		: "json"
			, contentType	: "application/json"
			, success 		: function(response) {
								var responseJson = response.responseCode + " " + response.responseMsg + "\n";
								document.mainForm.jsontext.value = responseJson; 
							}
		});
    }
 	
    // information/gatewayIDList
    function gwlist_onclick()
    {
    	var json = {"sessionKey": "311q07ooo38xej17l6pe"};
    	
    	$.ajax({
    		  type			: "POST"
    		, url			: "http://localhost:8080/usp_sdm/information/gatewayIDList"
    		, data			: JSON.stringify(json)
    		, dataType		: "json"
    		, contentType	: "application/json"
    		, success 		: function(response) {
    							var responseJson = response.responseCode + " " + response.responseMsg + "\n";
    							responseJson += response.gwIDList;
    							document.mainForm.jsontext.value = responseJson; 
    						}
    	});
    }
    
 	// information/resourceDescription
    function description_onclick()
    {
    	var json = {"sessionKey" : "311q07ooo38xej17l6pe",
					"targetList" : ["1:::"] };
    	
    	$.ajax({
    		  type			: "POST"
    		, url			: "http://localhost:8080/usp_sdm/information/resourceDescription"
    		, data			: JSON.stringify(json)
    		, dataType		: "json"
    		, contentType	: "application/json"
    		, success 		: function(response) {
    							var responseJson = response.responseCode + " " + response.responseMsg + "\n";
    							document.mainForm.jsontext.value = responseJson; 
    						}
    	});
    }
 
 	// information/resourceStatus
    function status_onclick()
    {
		var json = {"sessionKey" : "311q07ooo38xej17l6pe",
					"targetList" : ["FF:FF:FF:FF", "ETRI_GW_002:FF:FF:FF"] };
    	
    	$.ajax({
    		  type			: "POST"
    		, url			: "http://localhost:8080/usp_sdm/information/resourceStatus"
    		, data			: JSON.stringify(json)
    		, dataType		: "json"
    		, contentType	: "application/json"
    		, success 		: function(response) {
    							var responseJson = response.responseCode + " " + response.responseMsg + "\n";
    							responseJson += response.statusList;
    							document.mainForm.jsontext.value = responseJson; 
    						}
    	});
    }
 	
 	// sri/insertAssessValues
	function insertsri_onclick()
 	{
		var json = {"sessionKey": "311q07ooo38xej17l6pe",
					"category": "water",
					"assessValues": [ {"type" : "leak", "value" : "90.0", "date" : "2015-11-30", "coordinate": {"longitude":"135.253", "latitude":"30.254", "altitude":"55.25" }},
									  {"type" : "displacement", "value" : "2.0", "date" : "2015-11-30", "coordinate": {"longitude":"135.253", "latitude":"30.254", "altitude":"55.25" }} ] };

		$.ajax({
    		  type			: "POST"
    		, url			: "http://localhost:8080/usp_sdm/sri/insertAssessValues"
    		, data			: JSON.stringify(json)
    		, dataType		: "json"
    		, contentType	: "application/json"
    		, success 		: function(response) {
    							var responseJson = response.responseCode + " " + response.responseMsg + "\n";
    							document.mainForm.jsontext.value = responseJson; 
    						}
    	});
 	}
	
	// sri/retrieveAssessValues
	function retrievesri_onclick()
	{
		var json = {"sessionKey": "311q07ooo38xej17l6pe",
					"category": "water",
					"bbox": ["0","0","300","300"] };

		$.ajax({
			  type			: "POST"
			, url			: "http://localhost:8080/usp_sdm/sri/retrieveAssessValues"
			, data			: JSON.stringify(json)
			, dataType		: "json"
			, contentType	: "application/json"
			, success 		: function(response) {
								var responseJson = response.responseCode + " " + response.responseMsg + "\n";
								document.mainForm.jsontext.value = responseJson; 
							}
		});
	}
	
	// sri/assessUtilities
	function utility_onclick()
	{
		var json = {"sessionKey": "311q07ooo38xej17l6pe",
					"category": ["water", "sewer"] };

		$.ajax({
			  type			: "POST"
			, url			: "http://localhost:8080/usp_sdm/sri/assessUtilities"
			, data			: JSON.stringify(json)
			, dataType		: "json"
			, contentType	: "application/json"
			, success 		: function(response) {
								var responseJson = response.responseCode + " " + response.responseMsg + "\n";
								document.mainForm.jsontext.value = responseJson; 
							}
		});
	}
	
	// sri/assessSRI
	function assesssri_onclick()
	{
		var json = {"sessionKey": "311q07ooo38xej17l6pe"};

		$.ajax({
			  type			: "POST"
			, url			: "http://localhost:8080/usp_sdm/sri/assessSRI"
			, data			: JSON.stringify(json)
			, dataType		: "json"
			, contentType	: "application/json"
			, success 		: function(response) {
								var responseJson = response.responseCode + " " + response.responseMsg + "\n";
								document.mainForm.jsontext.value = responseJson; 
							}
		});
	}
	
	// sri/retrieveRiskValues
    function retrieveriskvalues_onclick()
    {
    	var json = {"sessionKey": "311q07ooo38xej17l6pe",
					"category": ["water", "sewer"],
					"bbox": ["0","0","300","300"] };

		$.ajax({
			  type			: "POST"
			, url			: "http://localhost:8080/usp_sdm/sri/retrieveRiskValues"
			, data			: JSON.stringify(json)
			, dataType		: "json"
			, contentType	: "application/json"
			, success 		: function(response) {
								var responseJson = response.responseCode + " " + response.responseMsg + "\n";
								document.mainForm.jsontext.value = responseJson; 
							}
		});
    }
	*/
        
    </script>
</head>

<body style="text-align:center; margin:0 auto; display:inline; padding-top:100px;" onload="javascript:form_onLoad();">
<form name="mainForm" method="post">

<div id="content_pop">

  <!-- 타이틀 -->
  <div id="title">
    <table width="100%" border="0">
      <tr>
        <td>
          <img src="<c:url value='/images/egovframework/usdm/title_dot.gif'/>"/>
          <font size="4"><b>&nbsp;지하공간 센싱데이터 관리 시스템</b></font>
        </td>
      </tr>
      <tr><td bgcolor="#D3E2EC" height="3px"></td></tr>
    </table>
  </div>
  <br></br>
  <br></br>
  <!-- // 타이틀 -->
  
  <div id="map" style="width:100%;height:800px;position:relative;overflow:hidden;"></div>
  
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
			// 지도의 중심좌표
			center: new daum.maps.LatLng(36.358274, 127.364318),
		
			// 지도의 레벨(확대, 축소 정도)
			level: 5
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
		var normalOpacity   = 0.5; // 평상시
		var selectedOpacity = 1.0; // 선택시
		
		//------------------------
		// Global Variables END --
		//------------------------
		
		//-------------------------
		// 센서노드 그리기 START --
		//-------------------------
		
		// 지도에 표시된 마커 객체를 가지고 있을 배열입니다
	    var markers = [];

		<c:forEach items="${sensorList}" var="sensor">
			addMarker("${sensor.id}", "${sensor.geom}");
		</c:forEach>
		
		function addMarker(id, coordList) {
			var position = new daum.maps.LatLng(coordList.split(' ')[0], coordList.split(' ')[1]);
			
			// marker 객체생성
			var marker = new daum.maps.Marker({
			    map: map,
			    position: position,
			    opacity: normalOpacity,
			    clickable: true
			});
			
			// 생성된 마커를 배열에 추가합니다
		    markers.push(marker);
			
			
			// 인포윈도우의 내용 생성
		    var iwContent = '<div style="padding:5px;">';
		    var tableContent = '';
		    var isnull = true;
		    
		    iwContent += '<b>Node [' + id + ']</b><br>';
		    
		    tableContent += '<table border=0 cellspacing=1 cellpadding=5 bgcolor=silver>';
		    tableContent += '<tr align=center bgcolor=#D9E5FF>';
		    tableContent += '<td><b>tdID</td>';
		    tableContent += '<td><b>TYPE</td>';
		    tableContent += '<td><b>TIME</td>';
		    tableContent += '<td><b>VALUE</td>';
		    tableContent += '</tr>';
		    
		    <c:forEach items="${valueList}" var="value">
		    	if (id == '${value.id}') {
		    		tableContent += '<tr align=center bgcolor=white>';
		    		tableContent += '<td>&nbsp;${value.tdid}&nbsp;</td>';
		    		tableContent += '<td>&nbsp;${value.sensortype}&nbsp;</td>';
		    		tableContent += '<td>&nbsp;${value.timestr}&nbsp;</td>';
		    		tableContent += '<td>&nbsp;${value.sensingvalue}&nbsp;</td>';
		    		tableContent += '</tr>';
		    		
		    		if (isnull == true) isnull = false;
		    	}
			</c:forEach>
		    
			tableContent += '</table>';
			
			if (isnull == true) {
				tableContent = '<b>No Data</b>';
			}
			
		    iwContent += tableContent;
		    iwContent += '</div>';

			// 인포윈도우를 생성합니다
			var infowindow = new daum.maps.InfoWindow({
			    position : position, 
			    content : iwContent 
			});
		
			// 마우스오버 이벤트
			daum.maps.event.addListener(marker, 'mouseover', function() {
				this.setOpacity(selectedOpacity);

				// 마커 위에 인포윈도우를 표시합니다. 두번째 파라미터인 marker를 넣어주지 않으면 지도 위에 표시됩니다
				infowindow.open(map, marker);
			});
			
			// 마우스떠남 이벤트
			daum.maps.event.addListener(marker, 'mouseout', function() {
				this.setOpacity(normalOpacity);
				
				infowindow.close();
			});
		}
		
		//-----------------------
		// 센서노드 그리기 END --
		//-----------------------
		
  </script>
  
</div>
	
</form>

</body>
</html>