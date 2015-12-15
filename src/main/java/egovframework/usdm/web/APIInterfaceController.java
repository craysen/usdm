/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package egovframework.usdm.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.usdm.service.*;
import egovframework.usdm.web.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springmodules.validation.commons.DefaultBeanValidator;

@Controller
public class APIInterfaceController {

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** MWSInterfaceService */
	@Resource(name = "apiInterfaceService")
	private APIInterfaceService apiInterfaceService;
	
	/** Validator */
	@Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;

	// query/latestValue
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/query/latestValue", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQueryLatestValue(@RequestBody String filterJSON) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			List<String> targetList		= (ArrayList<String>)filter.get("targetList");
			List<String> sensorTypeList = (ArrayList<String>)filter.get("sensingTypeList");
			
			List<Map<String, Object>> conditionList = (ArrayList<Map<String, Object>>)filter.get("conditionList");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (sessionKey.equals("")) 	throw new LoginSessionException();
			if (targetList == null)		throw new Exception();
			if (sensorTypeList == null)	throw new Exception();
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			/*
			LoginSessionVO loginSessionVO = new LoginSessionVO();
			loginSessionVO.setSessionKey(sessionKey);
			
			if (apiInterfaceService.validateSessionKey(loginSessionVO) < 0) {
				throw new LoginSessionException();
			}
			*/
				
			String gwID 		= "";
			String panID 		= "";
			String snID 		= "";
			String tdID			= "";
			String sensorType 	= "";
			
			Map<String, Object> conditionMap = new LinkedHashMap<String, Object>();
			String additionalCondition 	= "";
			String additionalCondition1 = "";
			String logicalOperator		= "";
			String additionalCondition2 = "";
			
			List<EgovMap> queryResult = new ArrayList<EgovMap>();
			List<?> tempResult = null;
			
			for (int i=0; i<targetList.size(); i++) {
				gwID	= UsdmUtils.getGwIDFromAddress(targetList.get(i));
				panID	= UsdmUtils.getPanIDFromAddress(targetList.get(i));
				snID 	= UsdmUtils.getSnIDFromAddress(targetList.get(i));
				tdID 	= UsdmUtils.getTdIDFromAddress(targetList.get(i));
				
				for (int j=0; j<sensorTypeList.size(); j++) {
					sensorType = sensorTypeList.get(j);
					
					additionalCondition1 = "";
					logicalOperator		 = "";
					additionalCondition2 = "";
					
					if (conditionList != null) {
						// conditionList에서 sensorType과 동일한 tdType을 갖는 element 검색
						for (int k=0; k<conditionList.size(); k++) {
							conditionMap = conditionList.get(k);
							
							if (conditionMap.get("tdType").equals(sensorType)) {
								if (!additionalCondition1.equals("") && !additionalCondition2.equals("")) {
									throw new Exception();
								}
								
								if (logicalOperator.equals("")) {
									additionalCondition1 = "sensingValue "
														 + getOperator((String)conditionMap.get("operation")) + " "
														 + (String)conditionMap.get("value");
								}
								else {
									additionalCondition2 = "sensingValue "
														 + getOperator((String)conditionMap.get("operation")) + " "
														 + (String)conditionMap.get("value");
								}
						
								if (!conditionMap.get("logicalOp").equals("")) {
									logicalOperator = (String)conditionMap.get("logicalOp");
								}
							}
						}
						
						if (logicalOperator.equals("")) {
							additionalCondition = " AND " + additionalCondition1;
						}
						else {
							additionalCondition = " AND (" + additionalCondition1 + " "
												+ logicalOperator + " " + additionalCondition2 + ")";
						}
					}
					
					SNSensingValueVO sensingValueVO = new SNSensingValueVO();
					sensingValueVO.setGwID(gwID);
					sensingValueVO.setPanID(panID);
					sensingValueVO.setSnID(snID);
					sensingValueVO.setTdID(tdID);
					sensingValueVO.setSensorType(sensorType);
					sensingValueVO.setAdditionalCondition(additionalCondition);
					
					tempResult = apiInterfaceService.selectSensingValueQuery(sensingValueVO);
					
					queryResult.addAll((List<EgovMap>)tempResult);
				}
			}
			
			EgovMap queryMap = new EgovMap();
			List<Map<String, Object>> sensingValueList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> sensingValue = new LinkedHashMap<String, Object>();
				sensingValue.put("tdAddress",		queryMap.get("tdaddress"));
				sensingValue.put("sensingTime",  	UsdmUtils.convertDateToStr((double)queryMap.get("sensingtime"), "yyyy-MM-dd HH:mm:ss.SSS"));
				sensingValue.put("sensorType", 		queryMap.get("sensortype"));
				sensingValue.put("sensingValue",	queryMap.get("sensingvalue").toString());
				
				sensingValueList.add(sensingValue);
			}
			
			responseMessage.put("responseCode", "200");
			responseMessage.put("responseMsg",  "Query Success");
			responseMessage.put("resultSet",  	sensingValueList);
			
		} catch (LoginSessionException e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "202");
			responseMessage.put("responseMsg",  "Query Error: Invalid SessionKey");
		
		} catch (Exception e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "201");
			responseMessage.put("responseMsg",  "Query Error: Incorrect Request");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/latestValue", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// query/spatioTemporal
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/query/spatioTemporal", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQuerySpatioTemporal(@RequestBody String filterJSON) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			List<String> targetList		= (ArrayList<String>)filter.get("targetList");
			List<String> sensorTypeList = (ArrayList<String>)filter.get("sensingTypeList");
			
			List<Map<String, Object>> conditionList = (ArrayList<Map<String, Object>>)filter.get("conditionList");
			
			Map<String, Object> temporalConditionMap = (Map<String, Object>)filter.get("temporalCondition");
			Map<String, Object> spatialConditionMap  = (Map<String, Object>)filter.get("spatialCondition");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (sessionKey.equals("")) 	throw new LoginSessionException();
			if (targetList == null)		throw new Exception();
			if (sensorTypeList == null)	throw new Exception();
			
			// 시공간 조건이 존재하지 않는 경우 
			if (temporalConditionMap == null && spatialConditionMap == null) {
				throw new Exception("Either of temporal or spatial condition must exist");
			}
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			/*
			LoginSessionVO loginSessionVO = new LoginSessionVO();
			loginSessionVO.setSessionKey(sessionKey);
			
			if (apiInterfaceService.validateSessionKey(loginSessionVO) < 0) {
				throw new LoginSessionException();
			}
			*/
			
			String gwID 		= "";
			String panID 		= "";
			String snID 		= "";
			String tdID			= "";
			String sensorType 	= "";
			
			Map<String, Object> conditionMap = new LinkedHashMap<String, Object>();
			String additionalCondition 	= "";
			String additionalCondition1 = "";
			String logicalOperator		= "";
			String additionalCondition2 = "";
			
			String temporalCondition 	= "";
			String spatialCondition 	= "";
			
			// 시간조건이 존재하는 경우
			if (temporalConditionMap != null) {
				long startTime = UsdmUtils.convertStrToDate((String)temporalConditionMap.get("startTime"), "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
				long endTime   = UsdmUtils.convertStrToDate((String)temporalConditionMap.get("endTime"),   "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
				
				temporalCondition = " AND sensingTime >= " + startTime + " AND sensingTime <= " + endTime;
			}
			
			// 공간조건이 존재하는 경우
			if (spatialConditionMap != null) {
				String spatialType = (String)spatialConditionMap.get("type");
				
				// Polygon
				if (spatialType.equals("Polygon")) {
					List<List<String>> coordinateList = (ArrayList<List<String>>)spatialConditionMap.get("coordinates");
					String coordinateStr = "";
					
					for (int i=0; i<coordinateList.size(); i++) {
						
						coordinateStr += coordinateList.get(i).get(0) + " " + coordinateList.get(i).get(1); 
						
						if (i < coordinateList.size() - 1)
							coordinateStr += ",";
					}
					
					spatialCondition += " AND ST_INTERSECTS(ST_GEOMFROMTEXT('POLYGON((" + coordinateStr + "))',0), position) = 1";
				}
				// Circle
				else if (spatialType.equals("Circle")) {
					List<String> center = (ArrayList<String>)spatialConditionMap.get("center");

					double centerX = Double.parseDouble(center.get(0));
					double centerY = Double.parseDouble(center.get(1));
					double radius  = Double.parseDouble((String)spatialConditionMap.get("radius"));
					
					double radiusLongitude = radius * 0.00001111111;
					double radiusLatitude  = radius * 0.00000925925;

					double minX = centerX - radiusLongitude;
					double minY = centerY - radiusLatitude;
					double maxX = centerX + radiusLongitude;
					double maxY = centerY + radiusLatitude;
					
					String mbrPolygon  = minX + " " + minY + ","
									   + maxX + " " + minY + ","
									   + maxX + " " + maxY + ","
									   + minX + " " + maxY + ","
									   + minX + " " + minY;
					
					spatialCondition += " AND ST_INTERSECTS(ST_GEOMFROMTEXT('POLYGON((" + mbrPolygon + "))',0), position) = 1";
					spatialCondition += " AND SQRT(POWER(ABS(longitude - " + centerX + ")*90000,2) + POWER(ABS(latitude - " + centerY + ")*108000,2)) <= " + radius;
					
				}
				else {
					throw new Exception("Unsupported Spatial Object Type");
				}
			}
			
			List<EgovMap> queryResult = new ArrayList<EgovMap>();
			List<?> tempResult = null;
			
			for (int i=0; i<targetList.size(); i++) {
				gwID	= UsdmUtils.getGwIDFromAddress(targetList.get(i));
				panID	= UsdmUtils.getPanIDFromAddress(targetList.get(i));
				snID 	= UsdmUtils.getSnIDFromAddress(targetList.get(i));
				tdID 	= UsdmUtils.getTdIDFromAddress(targetList.get(i));
				
				for (int j=0; j<sensorTypeList.size(); j++) {
					sensorType = sensorTypeList.get(j);
					
					additionalCondition  = "";
					additionalCondition1 = "";
					logicalOperator		 = "";
					additionalCondition2 = "";
					
					if (conditionList != null) {
						// conditionList에서 sensorType과 동일한 tdType을 갖는 element 검색
						for (int k=0; k<conditionList.size(); k++) {
							conditionMap = conditionList.get(k);
							
							if (conditionMap.get("tdType").equals(sensorType)) {
								if (!additionalCondition1.equals("") && !additionalCondition2.equals("")) {
									throw new Exception();
								}
								
								if (logicalOperator.equals("")) {
									additionalCondition1 = "sensingValue "
														 + getOperator((String)conditionMap.get("operation")) + " "
														 + (String)conditionMap.get("value");
								}
								else {
									additionalCondition2 = "sensingValue "
														 + getOperator((String)conditionMap.get("operation")) + " "
														 + (String)conditionMap.get("value");
								}
						
								if (!conditionMap.get("logicalOp").equals("")) {
									logicalOperator = (String)conditionMap.get("logicalOp");
								}
							}
						}
						
						if (!additionalCondition1.equals("") || !additionalCondition2.equals("")) {
							if (logicalOperator.equals("")) {
								additionalCondition = " AND " + additionalCondition1;
							}
							else {
								additionalCondition = " AND (" + additionalCondition1 + " " + logicalOperator + " " + additionalCondition2 + ")";
							}
						}
					}

					SNSensingValueVO sensingValueVO = new SNSensingValueVO();
					sensingValueVO.setGwID(gwID);
					sensingValueVO.setPanID(panID);
					sensingValueVO.setSnID(snID);
					sensingValueVO.setTdID(tdID);
					sensingValueVO.setSensorType(sensorType);
					sensingValueVO.setAdditionalCondition(additionalCondition);
					sensingValueVO.setTemporalCondition(temporalCondition);
					sensingValueVO.setSpatialCondition(spatialCondition);
					
					tempResult = apiInterfaceService.selectSensingValueSpatioTemporal(sensingValueVO);
					
					queryResult.addAll((List<EgovMap>)tempResult);
				}
			}
			
			EgovMap queryMap = new EgovMap();
			List<Map<String, Object>> sensingValueList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> sensingValue = new LinkedHashMap<String, Object>();
				sensingValue.put("tdAddress",		queryMap.get("tdaddress"));
				sensingValue.put("sensingTime",  	UsdmUtils.convertDateToStr((double)queryMap.get("sensingtime"), "yyyy-MM-dd HH:mm:ss.SSS"));
				sensingValue.put("sensorType", 		queryMap.get("sensortype"));
				sensingValue.put("sensingValue",	queryMap.get("sensingvalue").toString());
				
				sensingValueList.add(sensingValue);
			}
			
			responseMessage.put("responseCode", "200");
			responseMessage.put("responseMsg",  "Query Success");
			responseMessage.put("resultSet",  	sensingValueList);
			
		} catch (LoginSessionException e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "202");
			responseMessage.put("responseMsg",  "Query Error: Invalid SessionKey");
		
		} catch (Exception e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "201");
			responseMessage.put("responseMsg",  "Query Error: Incorrect Request");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/spatioTemporal", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/gatewayIDList
	@RequestMapping(value="/information/gatewayIDList", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationGatewayIDList(@RequestBody String filterJSON) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (sessionKey.equals("")) throw new LoginSessionException();
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			/*
			LoginSessionVO loginSessionVO = new LoginSessionVO();
			loginSessionVO.setSessionKey(sessionKey);
			
			if (apiInterfaceService.validateSessionKey(loginSessionVO) < 0) {
				throw new LoginSessionException();
			}
			*/
			
			List<?> 		queryResult 	= apiInterfaceService.selectGatewayIDList();
			EgovMap 		queryMap 		= new EgovMap();
			List<String> 	gatewayIDList 	= new ArrayList<String>();
			
			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);
				gatewayIDList.add((String)queryMap.get("gwid"));
			}
			
			responseMessage.put("responseCode", "400");
			responseMessage.put("responseMsg",  "Information Success");
			responseMessage.put("gwIDList",  	gatewayIDList);
			
		} catch (LoginSessionException e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "402");
			responseMessage.put("responseMsg",  "Information Error: Invalid SessionKey");
		
		} catch (Exception e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "401");
			responseMessage.put("responseMsg",  "Information Error: Incorrect Request");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/gatewayIDList", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/resourceDescription
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/resourceDescription", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationResourceDescription(@RequestBody String filterJSON) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			ArrayList<String> targetList = (ArrayList<String>)filter.get("targetList");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (sessionKey.equals("")) 	throw new LoginSessionException();
			if (targetList.isEmpty())	throw new Exception();
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			/*
			LoginSessionVO loginSessionVO = new LoginSessionVO();
			loginSessionVO.setSessionKey(sessionKey);
			
			if (apiInterfaceService.validateSessionKey(loginSessionVO) < 0) {
				throw new LoginSessionException();
			}
			*/
			
			String gwID 	= "";
			String panID 	= "";
			String snID 	= "";
			String tdID		= "";
			
			List<?>	tempGatewayResult 		= null;
			List<?> tempPanResult 			= null;
			List<?> tempNodeResult 			= null;
			List<?> tempTransducerResult 	= null;
			
			List<Map<String, Object>> resourceDescriptionList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<targetList.size(); i++) {
				gwID	= UsdmUtils.getGwIDFromAddress(targetList.get(i));
	            panID	= UsdmUtils.getPanIDFromAddress(targetList.get(i));
	            snID 	= UsdmUtils.getSnIDFromAddress(targetList.get(i));
	            tdID 	= UsdmUtils.getTdIDFromAddress(targetList.get(i));
			
	            /******************************/
				/* message validation : start */
				/******************************/
	            
				if (gwID.equals("")) throw new Exception();
				
				/****************************/
				/* message validation : end */
				/****************************/

				// 모든 resource의 정보 조회
				if (gwID.equals("FF")) {
					SNGatewayVO gatewayVO = new SNGatewayVO();
					gatewayVO.setGwID(gwID);
					
					tempGatewayResult 		= apiInterfaceService.selectGatewayDescription(gatewayVO);
					tempPanResult 			= apiInterfaceService.selectPanDescriptionByGwID(gatewayVO);
					tempNodeResult 			= apiInterfaceService.selectNodeDescriptionByGwID(gatewayVO);
					tempTransducerResult 	= apiInterfaceService.selectTransducerDescriptionByGwID(gatewayVO);
				}
				// gateway 정보 조회
				else if (panID.equals("")) {
					SNGatewayVO gatewayVO = new SNGatewayVO();
					gatewayVO.setGwID(gwID);
					
					tempGatewayResult = apiInterfaceService.selectGatewayDescription(gatewayVO);
				}
				// gateway 정보 조회 (하위포함)
				else if (panID.equals("FF")) {
					SNGatewayVO gatewayVO = new SNGatewayVO();
					gatewayVO.setGwID(gwID);
					
					tempGatewayResult 		= apiInterfaceService.selectGatewayDescription(gatewayVO);
					tempPanResult 			= apiInterfaceService.selectPanDescriptionByGwID(gatewayVO);
					tempNodeResult 			= apiInterfaceService.selectNodeDescriptionByGwID(gatewayVO);
					tempTransducerResult 	= apiInterfaceService.selectTransducerDescriptionByGwID(gatewayVO);
				}
				// pan 상태 조회
				else if (snID.equals("")) {
					SNPanVO panVO = new SNPanVO();
					panVO.setGwID(gwID);
					panVO.setPanID(panID);
					
					tempPanResult 			= apiInterfaceService.selectPanDescription(panVO);
				}
				// pan 상태 조회 (하위포함)
				else if (snID.equals("FF")) {
					SNPanVO panVO = new SNPanVO();
					panVO.setGwID(gwID);
					panVO.setPanID(panID);
					
					tempPanResult 			= apiInterfaceService.selectPanDescription(panVO);
					tempNodeResult 			= apiInterfaceService.selectNodeDescriptionByPanID(panVO);
					tempTransducerResult 	= apiInterfaceService.selectTransducerDescriptionByPanID(panVO);
				}
				// node 상태 조회
				else if (tdID.equals("")) {
					SNNodeVO nodeVO = new SNNodeVO();
					nodeVO.setGwID(gwID);
					nodeVO.setPanID(panID);
					nodeVO.setSnID(snID);
					
					tempNodeResult 			= apiInterfaceService.selectNodeDescription(nodeVO);
				}
				// node 상태 조회 (하위포함)
				else if (tdID.equals("FF")) {
					SNNodeVO nodeVO = new SNNodeVO();
					nodeVO.setGwID(gwID);
					nodeVO.setPanID(panID);
					nodeVO.setSnID(snID);
					
					tempNodeResult 			= apiInterfaceService.selectNodeDescription(nodeVO);
					tempTransducerResult 	= apiInterfaceService.selectTransducerDescriptionBySnID(nodeVO);
				}
				// transducer 상태 조회
				else {
					SNTransducerVO transducerVO = new SNTransducerVO();
					transducerVO.setGwID(gwID);
					transducerVO.setPanID(panID);
					transducerVO.setSnID(snID);
					transducerVO.setTdID(tdID);
					
					tempTransducerResult 	= apiInterfaceService.selectTransducerDescription(transducerVO);
				}
				
				if (tempGatewayResult != null) {
					for (int j=0; j<tempGatewayResult.size(); j++) {
						resourceDescriptionList.add(getGatewayDescription((EgovMap)tempGatewayResult.get(j)));
					}
					tempGatewayResult = null;
				}
				if (tempPanResult != null) {
					for (int j=0; j<tempPanResult.size(); j++) {
						resourceDescriptionList.add(getPanDescription((EgovMap)tempPanResult.get(j)));
					}
					tempPanResult = null;
				}
				if (tempNodeResult != null) {
					for (int j=0; j<tempNodeResult.size(); j++) {
						resourceDescriptionList.add(getNodeDescription((EgovMap)tempNodeResult.get(j)));
					}
					tempNodeResult = null;
				}
				if (tempTransducerResult != null) {
					for (int j=0; j<tempTransducerResult.size(); j++) {
						resourceDescriptionList.add(getTransducerDescription((EgovMap)tempTransducerResult.get(j)));
					}
					tempTransducerResult = null;
				}
			}
			
			responseMessage.put("responseCode", "400");
			responseMessage.put("responseMsg",  "Information Success");
			responseMessage.put("resourceDescriptionList", resourceDescriptionList);
			
		} catch (LoginSessionException e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "402");
			responseMessage.put("responseMsg",  "Information Error: Invalid SessionKey");
		
		} catch (Exception e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "401");
			responseMessage.put("responseMsg",  "Information Error: Incorrect Request");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/resourceDescription", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/resourceStatus
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/resourceStatus", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationResourceStatus(@RequestBody String filterJSON) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			ArrayList<String> targetList = (ArrayList<String>)filter.get("targetList");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (sessionKey.equals("")) 	throw new LoginSessionException();
			if (targetList.isEmpty())	throw new Exception();
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			/*
			LoginSessionVO loginSessionVO = new LoginSessionVO();
			loginSessionVO.setSessionKey(sessionKey);
			
			if (apiInterfaceService.validateSessionKey(loginSessionVO) < 0) {
				throw new LoginSessionException();
			}
			*/
			
			String gwID 	= "";
			String panID 	= "";
			String snID 	= "";
			String tdID		= "";
			
			List<EgovMap> queryResult = new ArrayList<EgovMap>();
			List<?> tempResult = null;
			
			for (int i=0; i<targetList.size(); i++) {
				gwID	= UsdmUtils.getGwIDFromAddress(targetList.get(i));
				panID	= UsdmUtils.getPanIDFromAddress(targetList.get(i));
				snID 	= UsdmUtils.getSnIDFromAddress(targetList.get(i));
				tdID 	= UsdmUtils.getTdIDFromAddress(targetList.get(i));
				
				/******************************/
				/* message validation : start */
				/******************************/
				
				if (gwID.equals("")) throw new Exception();
				
				/****************************/
				/* message validation : end */
				/****************************/
				
				// 모든 resource의 상태 조회
				if (gwID.equals("FF")) {
					SNGatewayVO gatewayVO = new SNGatewayVO();
					gatewayVO.setGwID(gwID);
					
					tempResult = apiInterfaceService.selectGatewayStatusDescent(gatewayVO);
				}
				// gateway 상태 조회
				else if (panID.equals("")) {
					SNGatewayVO gatewayVO = new SNGatewayVO();
					gatewayVO.setGwID(gwID);
					
					tempResult = apiInterfaceService.selectGatewayStatus(gatewayVO);
				}
				// gateway 상태 조회 (하위포함)
				else if (panID.equals("FF")) {
					SNGatewayVO gatewayVO = new SNGatewayVO();
					gatewayVO.setGwID(gwID);
					
					tempResult = apiInterfaceService.selectGatewayStatusDescent(gatewayVO);
				}
				// pan 상태 조회
				else if (snID.equals("")) {
					SNPanVO panVO = new SNPanVO();
					panVO.setGwID(gwID);
					panVO.setPanID(panID);
					
					tempResult = apiInterfaceService.selectPanStatus(panVO);
				}
				// pan 상태 조회 (하위포함)
				else if (snID.equals("FF")) {
					SNPanVO panVO = new SNPanVO();
					panVO.setGwID(gwID);
					panVO.setPanID(panID);
					
					tempResult = apiInterfaceService.selectPanStatusDescent(panVO);
				}
				// node 상태 조회
				else if (tdID.equals("")) {
					SNNodeVO nodeVO = new SNNodeVO();
					nodeVO.setGwID(gwID);
					nodeVO.setPanID(panID);
					nodeVO.setSnID(snID);
					
					tempResult = apiInterfaceService.selectNodeStatus(nodeVO);
				}
				// node 상태 조회 (하위포함)
				else if (tdID.equals("FF")) {
					SNNodeVO nodeVO = new SNNodeVO();
					nodeVO.setGwID(gwID);
					nodeVO.setPanID(panID);
					nodeVO.setSnID(snID);
					
					tempResult = apiInterfaceService.selectNodeStatusDescent(nodeVO);
				}
				// transducer 상태 조회
				else {
					SNTransducerVO transducerVO = new SNTransducerVO();
					transducerVO.setGwID(gwID);
					transducerVO.setPanID(panID);
					transducerVO.setSnID(snID);
					transducerVO.setTdID(tdID);
					
					tempResult = apiInterfaceService.selectTransducerStatus(transducerVO);
				}
				
				queryResult.addAll((List<EgovMap>)tempResult);
			}
			
			EgovMap queryMap = new EgovMap();
			List<Map<String, Object>> statusList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> statusMap = new LinkedHashMap<String, Object>();
				statusMap.put("targetAddress", queryMap.get("address"));
				statusMap.put("statusCode",    queryMap.get("statuscode"));
				
				statusList.add(statusMap);
			}
			
			responseMessage.put("responseCode", "400");
			responseMessage.put("responseMsg",  "Information Success");
			responseMessage.put("statusList",  	statusList);
			
		} catch (LoginSessionException e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "402");
			responseMessage.put("responseMsg",  "Information Error: Invalid SessionKey");
		
		} catch (Exception e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "401");
			responseMessage.put("responseMsg",  "Information Error: Incorrect Request");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/resourceStatus", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// sri/insertAssessValues
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/sri/insertAssessValues", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonSriInsertAssessValues(@RequestBody String filterJSON) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey 	= (String)filter.get("sessionKey");
			String category		= (String)filter.get("category");
			ArrayList<LinkedHashMap<String, Object>> assessValuesList = (ArrayList<LinkedHashMap<String, Object>>)filter.get("assessValues");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (sessionKey.equals("")) 	throw new LoginSessionException();
			if (category.equals("")) 	throw new Exception();
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			/*
			LoginSessionVO loginSessionVO = new LoginSessionVO();
			loginSessionVO.setSessionKey(sessionKey);
			
			if (apiInterfaceService.validateSessionKey(loginSessionVO) < 0) {
				throw new LoginSessionException();
			}
			*/
			
			LinkedHashMap<String, Object> assessValues = new LinkedHashMap<String, Object>(); 
			String 	type 		= "";
			double 	value 		= 0;
			long 	date 		= 0;
			
			LinkedHashMap<String, Object> coordinate = new LinkedHashMap<String, Object>();
			String 	longitude	= "";
			String 	latitude 	= "";
			String 	altitude 	= "";
			
			for (int i=0; i<assessValuesList.size(); i++) {
				
				assessValues = assessValuesList.get(i);
				
				type  		= (String)assessValues.get("type");
				value 		= Double.parseDouble((String)assessValues.get("value"));
				date  		= UsdmUtils.convertStrToDate((String)assessValues.get("date"), "yyyy-MM-dd");
				
				coordinate 	= (LinkedHashMap<String, Object>)assessValues.get("coordinate");
				longitude	= (String)coordinate.get("longitude");
				latitude	= (String)coordinate.get("latitude");
				altitude	= (String)coordinate.get("altitude");
				
				/******************************/
				/* message validation : start */
				/******************************/
				
				if (type.equals("")) throw new Exception();
				
				/****************************/
				/* message validation : end */
				/****************************/
				
				SNAssessValueVO assessValueVO = new SNAssessValueVO();
				
				assessValueVO.setCategory(category);
				assessValueVO.setType(type);
				assessValueVO.setDate(date);
				assessValueVO.setValue(value);
				assessValueVO.setLongitude(longitude);
				assessValueVO.setLatitude(latitude);
				assessValueVO.setAltitude(altitude);
				
				apiInterfaceService.insertAssessValues(assessValueVO);
			}
			
			responseMessage.put("responseCode", "900");
			responseMessage.put("responseMsg",  "SRI Success");
			
		} catch (LoginSessionException e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "903");
			responseMessage.put("responseMsg",  "SRI Error: Invalid SessionKey");
		
		} catch (Exception e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "901");
			responseMessage.put("responseMsg",  "SRI Error: Incorrect Message");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/sri/insertAssessValues", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// sri/retrieveAssessValues
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/sri/retrieveAssessValues", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonSriRetrieveAssessValues(@RequestBody String filterJSON) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey 	= (String)filter.get("sessionKey");
			String category		= (String)filter.get("category");
			List<String> bbox 	= (ArrayList<String>)filter.get("bbox");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (sessionKey.equals("")) 	throw new LoginSessionException();
			if (category.equals(""))	throw new Exception();
			
			/****************************/
			/* message validation : end */
			/****************************/
		
			/*
			LoginSessionVO loginSessionVO = new LoginSessionVO();
			loginSessionVO.setSessionKey(sessionKey);
			
			if (apiInterfaceService.validateSessionKey(loginSessionVO) < 0) {
				throw new LoginSessionException();
			}
			*/
			
			String minX = bbox.get(0);
			String minY = bbox.get(1);
			String maxX = bbox.get(2);
			String maxY = bbox.get(3);
			
			SNAssessValueVO assessValueVO = new SNAssessValueVO();
			
			assessValueVO.setCategory(category);
			assessValueVO.setMinX(minX);
			assessValueVO.setMinY(minY);
			assessValueVO.setMaxX(maxX);
			assessValueVO.setMaxY(maxY);

			List<?> queryResult = apiInterfaceService.retrieveAssessValues(assessValueVO);
			
			EgovMap queryMap = new EgovMap();
			List<Map<String, Object>> assessValueList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);

				Map<String, Object> coordinate = new LinkedHashMap<String, Object>();
				coordinate.put("longitude", queryMap.get("longitude"));
				coordinate.put("latitude",  queryMap.get("latitude"));
				coordinate.put("altitude",  queryMap.get("altitude"));

				Map<String, Object> assessValue = new LinkedHashMap<String, Object>();
				assessValue.put("type",  		queryMap.get("type"));
				assessValue.put("date",  		UsdmUtils.convertDateToStr((double)queryMap.get("date"), "yyyy-MM-dd"));
				assessValue.put("value", 		queryMap.get("value"));
				assessValue.put("coordinate", 	coordinate);
				
				assessValueList.add(assessValue);
			}
			
			responseMessage.put("responseCode", "900");
			responseMessage.put("responseMsg",  "SRI Success");
			responseMessage.put("assessValues",	assessValueList);
			
		} catch (LoginSessionException e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "903");
			responseMessage.put("responseMsg",  "SRI Error: Invalid SessionKey");
		
		} catch (Exception e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "901");
			responseMessage.put("responseMsg",  "SRI Error: Incorrect Message");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/sri/retrieveAssessValues", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// sri/assessUtilities
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/sri/assessUtilities", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonSriAssessUtilities(@RequestBody String filterJSON) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String 			sessionKey 		= (String)filter.get("sessionKey");
			List<String>	categoryList	= (ArrayList<String>)filter.get("category");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (sessionKey.equals(""))	throw new LoginSessionException();
			if (categoryList.isEmpty())	throw new Exception();
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			/*
			LoginSessionVO loginSessionVO = new LoginSessionVO();
			loginSessionVO.setSessionKey(sessionKey);
			
			if (apiInterfaceService.validateSessionKey(loginSessionVO) < 0) {
				throw new LoginSessionException();
			}
			*/
			
			double value = 0;
			long date = System.currentTimeMillis();
			
			for (int i=0; i<categoryList.size(); i++) {
				// value 계산 필요
				value = 0.99;
				
				SNAssessValueVO assessValueVO = new SNAssessValueVO();
				
				assessValueVO.setCategory(categoryList.get(i));
				assessValueVO.setType("risk");
				assessValueVO.setDate(date);
				assessValueVO.setValue(value);
				assessValueVO.setLongitude("127.38115");
				assessValueVO.setLatitude("36.357439");
				assessValueVO.setAltitude("51.427852");
				
				apiInterfaceService.insertAssessValues(assessValueVO);
			}
			
			responseMessage.put("responseCode", "900");
			responseMessage.put("responseMsg",  "SRI Success");
			
		} catch (LoginSessionException e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "903");
			responseMessage.put("responseMsg",  "SRI Error: Invalid SessionKey");
		
		} catch (Exception e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "901");
			responseMessage.put("responseMsg",  "SRI Error: Incorrect Message");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/sri/assessUtilities", filterJSON, responseJson);

		return responseJson;
	}
	
	// sri/assessSRI
	@RequestMapping(value="/sri/assessSRI", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonSriAssessSRI(@RequestBody String filterJSON) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (sessionKey.equals("")) 		throw new LoginSessionException();
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			/*
			LoginSessionVO loginSessionVO = new LoginSessionVO();
			loginSessionVO.setSessionKey(sessionKey);
			
			if (apiInterfaceService.validateSessionKey(loginSessionVO) < 0) {
				throw new LoginSessionException();
			}
			*/
			
			double value = 0;
			long date = System.currentTimeMillis();
			
			// value 계산 필요
			value = 0.99;
			
			SNAssessValueVO assessValueVO = new SNAssessValueVO();
			
			assessValueVO.setCategory("SRI");
			assessValueVO.setType("risk");
			assessValueVO.setDate(date);
			assessValueVO.setValue(value);
			assessValueVO.setLongitude("127.38115");
			assessValueVO.setLatitude("36.357439");
			assessValueVO.setAltitude("51.427852");
			
			apiInterfaceService.insertAssessValues(assessValueVO);
			
			responseMessage.put("responseCode", "900");
			responseMessage.put("responseMsg",  "SRI Success");
			
		} catch (LoginSessionException e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "903");
			responseMessage.put("responseMsg",  "SRI Error: Invalid SessionKey");
		
		} catch (Exception e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "901");
			responseMessage.put("responseMsg",  "SRI Error: Incorrect Message");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/sri/assessSRI", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// sri/retrieveRiskValues
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/sri/retrieveRiskValues", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonSriRetrieveRiskValues(@RequestBody String filterJSON) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String 			sessionKey 		= (String)filter.get("sessionKey");
			List<String>	categoryList	= (ArrayList<String>)filter.get("category");
			List<String> 	bbox 			= (ArrayList<String>)filter.get("bbox");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (sessionKey.equals("")) 		throw new LoginSessionException();
			if (categoryList.isEmpty())		throw new Exception();
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			/*
			LoginSessionVO loginSessionVO = new LoginSessionVO();
			loginSessionVO.setSessionKey(sessionKey);
			
			if (apiInterfaceService.validateSessionKey(loginSessionVO) < 0) {
				throw new LoginSessionException();
			}
			*/
			
			String category = "";
			
			for (int i=0; i<categoryList.size(); i++) {
				category += "'" + categoryList.get(i) + "'";
				
				if (i < categoryList.size()-1)
					category += ",";
			}
			
			String minX = bbox.get(0);
			String minY = bbox.get(1);
			String maxX = bbox.get(2);
			String maxY = bbox.get(3);
			
			SNAssessValueVO assessValueVO = new SNAssessValueVO();
			
			assessValueVO.setCategory(category);
			assessValueVO.setMinX(minX);
			assessValueVO.setMinY(minY);
			assessValueVO.setMaxX(maxX);
			assessValueVO.setMaxY(maxY);
			
			List<?> queryResult = apiInterfaceService.retrieveRiskValues(assessValueVO);
			
			EgovMap queryMap = new EgovMap();
			List<Map<String, Object>> riskValueList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> coordinate = new LinkedHashMap<String, Object>();
				coordinate.put("longitude", queryMap.get("longitude"));
				coordinate.put("latitude",  queryMap.get("latitude"));
				coordinate.put("altitude",  queryMap.get("altitude"));
				
				Map<String, Object> riskValue = new LinkedHashMap<String, Object>();
				riskValue.put("category",	queryMap.get("category"));
				riskValue.put("date",  		UsdmUtils.convertDateToStr((double)queryMap.get("date"), "yyyy-MM-dd"));
				riskValue.put("value", 		queryMap.get("value").toString());
				riskValue.put("coordinate",	coordinate);
				
				riskValueList.add(riskValue);
			}
			
			responseMessage.put("responseCode", "900");
			responseMessage.put("responseMsg",  "SRI Success");
			responseMessage.put("riskValues",	riskValueList);
			
		} catch (LoginSessionException e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "903");
			responseMessage.put("responseMsg",  "SRI Error: Invalid SessionKey");
		
		} catch (Exception e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "901");
			responseMessage.put("responseMsg",  "SRI Error: Incorrect Message");
		}

		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/sri/retrieveRiskValues", filterJSON, responseJson);
		
		return responseJson;
	}
	
	public static String getOperator(String operation) throws Exception {
		String operator = "";
		
		if (operation.equals("GT")) {
			operator = ">";
		}
		else if (operation.equals("GE")) {
			operator = ">=";
		}
		else if (operation.equals("LT")) {
			operator = "<";
		}
		else if (operation.equals("LE")) {
			operator = "<=";
		}
		else if (operation.equals("EQ")) {
			operator = "=";
		}
		else if (operation.equals("NE")) {
			operator = "!=";
		}
		else {
			throw new Exception("Unsupported Operation.");
		}
		
		return operator;
	}
	
	public static Map<String, Object> getGatewayDescription(EgovMap tempResultMap) throws Exception {
		Map<String, Object> description 		= new LinkedHashMap<String, Object>();
		Map<String, Object> resourceDescription = new LinkedHashMap<String, Object>();
		
		Map<String, Object> coordinate = new LinkedHashMap<String, Object>();
		coordinate.put("longitude", tempResultMap.get("longitude"));
		coordinate.put("latitude", tempResultMap.get("latitude"));
		coordinate.put("altitude", tempResultMap.get("altitude"));
		
		String supportedTransportProtocolListStr 			= (String)tempResultMap.get("supportedtransportprotocollist");
		String supportedTransportConnectionControlListStr 	= (String)tempResultMap.get("supportedtransportconnectioncontrollist");
		String supportedTransportDirectionListStr 			= (String)tempResultMap.get("supportedtransportdirectionlist");
		String supportedOperationListStr 					= (String)tempResultMap.get("supportedoperationlist");
		String supportedAttributeListStr 					= (String)tempResultMap.get("supportedattributelist");
		String supportedSensorServiceCenterListStr 			= (String)tempResultMap.get("supportedsensorservicecenterlist");
		String panIDListStr 								= (String)tempResultMap.get("panidlist");

		List<String> supportedTransportProtocolList 			= new ArrayList<String>(Arrays.asList(supportedTransportProtocolListStr.split(",")));
		List<String> supportedTransportConnectionControlList	= new ArrayList<String>(Arrays.asList(supportedTransportConnectionControlListStr.split(",")));
		List<String> supportedTransportDirectionList 			= new ArrayList<String>(Arrays.asList(supportedTransportDirectionListStr.split(",")));
		List<String> supportedOperationList 					= new ArrayList<String>(Arrays.asList(supportedOperationListStr.split(",")));
		List<String> supportedAttributeList 					= new ArrayList<String>(Arrays.asList(supportedAttributeListStr.split(",")));
		List<String> supportedSensorServiceCenterList 			= new ArrayList<String>(Arrays.asList(supportedSensorServiceCenterListStr.split(",")));
		List<String> panIDList 									= new ArrayList<String>(Arrays.asList(panIDListStr.split(",")));
		
		resourceDescription.put("gwID", 									tempResultMap.get("gwid"));
		resourceDescription.put("url", 										tempResultMap.get("url"));
		resourceDescription.put("manufacturer", 							tempResultMap.get("manufacturer"));
		resourceDescription.put("productNo", 								tempResultMap.get("productno"));
		resourceDescription.put("location", 								tempResultMap.get("location"));
		resourceDescription.put("coordinate", 								coordinate);
		resourceDescription.put("supportedTransportProtocolList", 			supportedTransportProtocolList);
		resourceDescription.put("supportedTransportConnectionControlList",	supportedTransportConnectionControlList);
		resourceDescription.put("supportedTransportDirectionList", 			supportedTransportDirectionList);
		resourceDescription.put("supportedOperationList", 					supportedOperationList);
		resourceDescription.put("supportedAttributeList", 					supportedAttributeList);
		resourceDescription.put("supportedSensorServiceCenterList", 		supportedSensorServiceCenterList);
		resourceDescription.put("panIDList", 								panIDList);
		resourceDescription.put("monitoringMode", 							tempResultMap.get("monitoringmode"));
		resourceDescription.put("monitoringPeriod", 						tempResultMap.get("monitoringperiod"));
		
		description.put("resourceType", "Gateway");
		description.put("description",  resourceDescription);
		
		return description;
	}
	
	public static Map<String, Object> getPanDescription(EgovMap tempResultMap) throws Exception {
		Map<String, Object> description 		= new LinkedHashMap<String, Object>();
		Map<String, Object> resourceDescription = new LinkedHashMap<String, Object>();

		String supportedChannelListStr 			= (String)tempResultMap.get("supportedchannellist");
		String supportedTopologyListStr 		= (String)tempResultMap.get("supportedtopologylist");
		String supportedProtocolStackListStr	= (String)tempResultMap.get("supportedprotocolstacklist");
		String supportedOperationListStr 		= (String)tempResultMap.get("supportedoperationlist");
		String supportedAttributeListStr 		= (String)tempResultMap.get("supportedattributelist");
		String snIDListStr 						= (String)tempResultMap.get("snidlist");

		List<String> supportedChannelList 		= new ArrayList<String>(Arrays.asList(supportedChannelListStr.split(",")));
		List<String> supportedTopologyList		= new ArrayList<String>(Arrays.asList(supportedTopologyListStr.split(",")));
		List<String> supportedProtocolStackList	= new ArrayList<String>(Arrays.asList(supportedProtocolStackListStr.split(",")));
		List<String> supportedOperationList 	= new ArrayList<String>(Arrays.asList(supportedOperationListStr.split(",")));
		List<String> supportedAttributeList 	= new ArrayList<String>(Arrays.asList(supportedAttributeListStr.split(",")));
		List<String> snIDList 					= new ArrayList<String>(Arrays.asList(snIDListStr.split(",")));
		
		resourceDescription.put("gwID", 						tempResultMap.get("gwid"));
		resourceDescription.put("panID", 						tempResultMap.get("panid"));
		resourceDescription.put("topology", 					tempResultMap.get("topology"));
		resourceDescription.put("protocolStack", 				tempResultMap.get("protocolstack"));
		resourceDescription.put("panChannel", 					tempResultMap.get("panchannel"));
		resourceDescription.put("supportedChannelList", 		supportedChannelList);
		resourceDescription.put("supportedTopologyList",		supportedTopologyList);
		resourceDescription.put("supportedProtocolStackList",	supportedProtocolStackList);
		resourceDescription.put("supportedOperationList", 		supportedOperationList);
		resourceDescription.put("supportedAttributeList", 		supportedAttributeList);
		resourceDescription.put("snIDList", 					snIDList);
		
		description.put("resourceType", "Pan");
		description.put("description",  resourceDescription);
		
		return description;
	}
	
	public static Map<String, Object> getNodeDescription(EgovMap tempResultMap) throws Exception {
		Map<String, Object> description 		= new LinkedHashMap<String, Object>();
		Map<String, Object> resourceDescription = new LinkedHashMap<String, Object>();

		Map<String, Object> coordinate = new LinkedHashMap<String, Object>();
		coordinate.put("longitude", tempResultMap.get("longitude"));
		coordinate.put("latitude", tempResultMap.get("latitude"));
		coordinate.put("altitude", tempResultMap.get("altitude"));
		
		String roleListStr 					= (String)tempResultMap.get("rolelist");
		String parentNodeIDListStr 			= (String)tempResultMap.get("parentnodeidlist");
		String supportedOperationListStr	= (String)tempResultMap.get("supportedoperationlist");
		String supportedAttributeListStr 	= (String)tempResultMap.get("supportedattributelist");
		String tdIDListStr 					= (String)tempResultMap.get("tdidlist");

		List<String> roleList 				= new ArrayList<String>(Arrays.asList(roleListStr.split(",")));
		List<String> parentNodeIDList		= new ArrayList<String>(Arrays.asList(parentNodeIDListStr.split(",")));
		List<String> supportedOperationList = new ArrayList<String>(Arrays.asList(supportedOperationListStr.split(",")));
		List<String> supportedAttributeList = new ArrayList<String>(Arrays.asList(supportedAttributeListStr.split(",")));
		List<String> tdIDList 				= new ArrayList<String>(Arrays.asList(tdIDListStr.split(",")));
		
		resourceDescription.put("gwID", 					tempResultMap.get("gwid"));
		resourceDescription.put("panID", 					tempResultMap.get("panid"));
		resourceDescription.put("snID", 					tempResultMap.get("snid"));
		resourceDescription.put("globalID", 				tempResultMap.get("globalid"));
		resourceDescription.put("manufacturer", 			tempResultMap.get("manufacturer"));
		resourceDescription.put("productNo", 				tempResultMap.get("productno"));
		resourceDescription.put("location", 				tempResultMap.get("location"));
		resourceDescription.put("coordinate", 				coordinate);
		resourceDescription.put("roleList", 				roleList);
		resourceDescription.put("parentNodeIDList",			parentNodeIDList);
		resourceDescription.put("supportedOperationList", 	supportedOperationList);
		resourceDescription.put("supportedAttributeList",	supportedAttributeList);
		resourceDescription.put("tdIDList", 				tdIDList);
		resourceDescription.put("monitoringMode", 			tempResultMap.get("monitoringmode"));
		resourceDescription.put("monitoringPeriod", 		tempResultMap.get("monitoringperiod"));
		
		description.put("resourceType", "Node");
		description.put("description",  resourceDescription);
		
		return description;
	}
	
	public static Map<String, Object> getTransducerDescription(EgovMap tempResultMap) throws Exception {
		Map<String, Object> description 		= new LinkedHashMap<String, Object>();
		Map<String, Object> resourceDescription = new LinkedHashMap<String, Object>();

		String levelListStr					= (String)tempResultMap.get("level");
		String supportedOperationListStr	= (String)tempResultMap.get("supportedoperationlist");
		String supportedAttributeListStr 	= (String)tempResultMap.get("supportedattributelist");

		List<String> levelList 				= new ArrayList<String>(Arrays.asList(levelListStr.split(",")));
		List<String> supportedOperationList = new ArrayList<String>(Arrays.asList(supportedOperationListStr.split(",")));
		List<String> supportedAttributeList = new ArrayList<String>(Arrays.asList(supportedAttributeListStr.split(",")));
		
		resourceDescription.put("gwID", 					tempResultMap.get("gwid"));
		resourceDescription.put("panID", 					tempResultMap.get("panid"));
		resourceDescription.put("snID", 					tempResultMap.get("snid"));
		resourceDescription.put("tdID", 					tempResultMap.get("tdid"));
		resourceDescription.put("manufacturer", 			tempResultMap.get("manufacturer"));
		resourceDescription.put("productNo", 				tempResultMap.get("productno"));
		resourceDescription.put("transducerCategory",		tempResultMap.get("transducercategory"));
		resourceDescription.put("transducerType", 			tempResultMap.get("transducertype"));
		resourceDescription.put("unit", 					tempResultMap.get("unit"));
		resourceDescription.put("dataType", 				tempResultMap.get("datatype"));
		resourceDescription.put("rangeMin", 				tempResultMap.get("rangemin"));
		resourceDescription.put("rangeMax", 				tempResultMap.get("rangemax"));
		resourceDescription.put("rangeOffset", 				tempResultMap.get("rangeoffset"));
		resourceDescription.put("level", 					levelList);
		resourceDescription.put("supportedOperationList", 	supportedOperationList);
		resourceDescription.put("supportedAttributeList",	supportedAttributeList);
		
		description.put("resourceType", "Transducer");
		description.put("description",  resourceDescription);
		
		return description;
	}
	
}