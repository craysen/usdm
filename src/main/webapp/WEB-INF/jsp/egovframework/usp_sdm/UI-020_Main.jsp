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
    
    <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
    <script type="text/javaScript" language="javascript" defer="defer">

    /* 최초 화면호출 이벤트 */
    function form_onLoad()
    {
    	document.getElementById("jsontest").style.display = "none";
    	
    	return;
    }
    
    function title_onclick()
    {
    	if (document.getElementById("jsontest").style.display == "block")
    		document.getElementById("jsontest").style.display = "none";
    	else
    		document.getElementById("jsontest").style.display = "block";
    }
    
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
          <font size="4"><b>&nbsp;지하공간 센싱데이터 관리 시스템 Home</b></font>
        </td>
      </tr>
      <tr><td bgcolor="#D3E2EC" height="3px"></td></tr>
    </table>
  </div>
  <br></br>
  <br></br>
  <!-- // 타이틀 -->

  <a href="javascript:title_onclick()";>
  <b><Font size="3">&nbsp;U</Font></b>nderground
  <b><Font size="3">&nbsp;S</Font></b>ensing
  <b><Font size="3">&nbsp;D</Font></b>ata
  <b><Font size="3">&nbsp;M</Font></b>anagement
  <b><Font size="3">&nbsp;S</Font></b>ystem
  </a>

  <br></br>
  <br></br>
  
  <div id="jsontest">
  <B>[JSON test]</B>
  <br></br>
  <table>
  	<colgroup>
    	<col width="30"/>
        <col width="200"/>
    	<col width="30"/>
        <col width="?"/>
    </colgroup>
    <tr>
    	<td></td><td><b>MWS</b></td>
    	<td></td><td><b>API</b></td>
    </tr>
    <tr>
  		<td><input type="button" value="go" onclick="javascript:login_onclick();"/></td>
  		<td colspan=3>
  			<font color="blue">/connection/login</font>
  		</td>
  	</tr>
    <tr>
  		<td><input type="button" value="go" onclick="javascript:logout_onclick();"/></td>
  		<td colspan=3>
  			<font color="blue">/connection/logout</font>
  		</td>
  	</tr>
  	<tr><td><br></br></td></tr>
  	<tr>
  		<td><input type="button" value="go" onclick="javascript:gateway_onclick();"/></td>
  		<td>
  			/resource/gateway
  		</td>
  		<td><input type="button" value="go" onclick="javascript:latest_onclick();"/></td>
  		<td>
  			/query/latestValue
  		</td>
  	</tr>
  	<tr>
  		<td><input type="button" value="go" onclick="javascript:pan_onclick();"/></td>
  		<td>
  			/resource/pan
  		</td>
  		<td><input type="button" value="go" onclick="javascript:spatio_onclick();"/></td>
  		<td>
  			/query/spatioTemporal
  		</td>
  	</tr>
  	<tr>
  		<td><input type="button" value="go" onclick="javascript:node_onclick();"/></td>
  		<td colspan=3>
  			/resource/node
  		</td>
  	</tr>
  	<tr>
  		<td><input type="button" value="go" onclick="javascript:transducer_onclick();"/></td>
  		<td>
  			/resource/transducer
  		</td>
  		<td><input type="button" value="go" onclick="javascript:gwlist_onclick();"/></td>
  		<td>
  			/information/gatewayIDList
  		</td>
  	</tr>
  	<tr>
  		<td><input type="button" value="go" onclick="javascript:update_onclick();"/></td>
  		<td>
  			<font color="blue">/resource/update</font>
  		</td>
  		<td><input type="button" value="go" onclick="javascript:description_onclick();"/></td>
  		<td>
  			/information/resourceDescription
  		</td>
  	</tr>
  	<tr>
  		<td><input type="button" value="go" onclick="javascript:delete_onclick();"/></td>
  		<td>
  			<font color="blue">/resource/delete</font>
  		</td>
  		<td><input type="button" value="go" onclick="javascript:status_onclick();"/></td>
  		<td>
  			/information/resourceStatus
  		</td>
  	</tr>
  	<tr><td><br></br></td></tr>
  	<tr>
  		<td><input type="button" value="go" onclick="javascript:sensing_onclick();"/></td>
  		<td>
  			/report/sensingValue
  		</td>
  		<td><input type="button" value="go" onclick="javascript:insertsri_onclick();"/></td>
  		<td>
  			/sri/insertAssessValues
  		</td>
  	</tr>
  	<tr>
  		<td><input type="button" value="go" onclick="javascript:actuation_onclick();"/></td>
  		<td>
  			<font color="blue">/report/actuationResult</font>
  		</td>
		<td><input type="button" value="go" onclick="javascript:retrievesri_onclick();"/></td>
  		<td>
  			/sri/retrieveAssessValues
  		</td>  		
  	</tr>
  	<tr>
  		<td></td>
  		<td></td>
  		<td><input type="button" value="go" onclick="javascript:utility_onclick();"/></td>
  		<td>
  			<font color="red">/sri/assessUtilities</font>
  		</td>
  	</tr>
  	<tr>
  		<td><input type="button" value="go" onclick="javascript:statusResource_onclick();"/></td>
  		<td>
  			<font color="blue">/notice/statusResource</font>
  		</td>
  		<td><input type="button" value="go" onclick="javascript:assesssri_onclick();"/></td>
  		<td>
  			<font color="red">/sri/assessSRI</font>
  		</td>
  	</tr>
  	<tr>
  		<td><input type="button" value="go" onclick="javascript:reboot_onclick();"/></td>
  		<td><font color="red">/notice/reboot</font></td>
  		<td><input type="button" value="go" onclick="javascript:retrieveriskvalues_onclick();"/></td>
  		<td>
  			/sri/retrieveRiskValues
  		</td>
  	</tr>
  </table>
  
  <br></br>
  <textarea id="jsontext" readonly="readonly" style="word-break:break-all;" cols="80" rows="20">
  </textarea>
  
  </div>
  
</div>
	
</form>
</body>
</html>