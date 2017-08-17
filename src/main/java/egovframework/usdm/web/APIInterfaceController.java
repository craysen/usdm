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
import javax.servlet.http.HttpServletResponse;

import egovframework.com.cmm.EgovProperties;
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

	/** APIInterfaceService */
	@Resource(name = "apiInterfaceService")
	private APIInterfaceService apiInterfaceService;
	
	/** Validator */
	@Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;

	// exception에 따른 response status, code, message
	static final int 	SUCCESS_STATUS 	= HttpServletResponse.SC_OK;
	static final int 	SUCCESS_CODE	= 2000;
	static final String SUCCESS_MSG		= "Success";
	
	static final int 	FAILED_STATUS	= HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
	static final int 	FAILED_CODE 	= 5006;
	static final String FAILED_MSG 		= "Failed : SDM Internal Error";
		
	// query/latestValue
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/query/latestValue", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQueryLatestValue(@RequestBody String filterJSON, HttpServletResponse response) {
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
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (targetList == null)		throw new InvalidParameterException("parameter 'targetList' has no value");
			if (sensorTypeList == null)	throw new InvalidParameterException("parameter 'sensingTypeList' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
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

				if (UsdmUtils.containsIgnoreCase(sensorTypeList, "ALL")) {
					SNSensingValueVO sensingValueVO = new SNSensingValueVO();
					sensingValueVO.setGwID(gwID);
					sensingValueVO.setPanID(panID);
					sensingValueVO.setSnID(snID);
					sensingValueVO.setTdID(tdID);
					sensingValueVO.setSensorType("ALL");
					sensingValueVO.setAdditionalCondition("");
					
					tempResult = apiInterfaceService.selectSensingValueLatest(sensingValueVO);
					
					if (tempResult != null)
						queryResult.addAll((List<EgovMap>)tempResult);
				}
				else {
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
										throw new InvalidParameterException();
									}
									
									if (logicalOperator.equals("")) {
										additionalCondition1 = "sensingValue "
															 + UsdmUtils.getOperator((String)conditionMap.get("operation")) + " "
															 + (String)conditionMap.get("value");
									}
									else {
										additionalCondition2 = "sensingValue "
															 + UsdmUtils.getOperator((String)conditionMap.get("operation")) + " "
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
						
						//tempResult = apiInterfaceService.selectSensingValueQuery(sensingValueVO);
						tempResult = apiInterfaceService.selectSensingValueLatest(sensingValueVO);
						
						if (tempResult != null)
							queryResult.addAll((List<EgovMap>)tempResult);
					}
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
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("resultSet",  	sensingValueList);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/latestValue", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// query/latestValueByID
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/query/latestValueByID", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQueryLatestValueByID(@RequestBody String filterJSON, HttpServletResponse response) {
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
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (targetList == null)		throw new InvalidParameterException("parameter 'targetList' has no value");
			if (sensorTypeList == null)	throw new InvalidParameterException("parameter 'sensingTypeList' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String GID 			= "";
			String sensorType 	= "";
			
			Map<String, Object> conditionMap = new LinkedHashMap<String, Object>();
			String additionalCondition 	= "";
			String additionalCondition1 = "";
			String logicalOperator		= "";
			String additionalCondition2 = "";
			
			List<EgovMap> queryResult = new ArrayList<EgovMap>();
			List<?> tempResult = null;
			
			for (int i=0; i<targetList.size(); i++) {
				GID = targetList.get(i);
				
				if (UsdmUtils.containsIgnoreCase(sensorTypeList, "ALL")) {
					SNSensingValueVO sensingValueVO = new SNSensingValueVO();
					sensingValueVO.setGID(GID);
					sensingValueVO.setSensorType("ALL");
					sensingValueVO.setAdditionalCondition("");
					
					tempResult = apiInterfaceService.selectSensingValueLatestByID(sensingValueVO);
					
					if (tempResult != null)
						queryResult.addAll((List<EgovMap>)tempResult);
				}
				else {
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
										throw new InvalidParameterException();
									}
									
									if (logicalOperator.equals("")) {
										additionalCondition1 = "sensingValue "
															 + UsdmUtils.getOperator((String)conditionMap.get("operation")) + " "
															 + (String)conditionMap.get("value");
									}
									else {
										additionalCondition2 = "sensingValue "
															 + UsdmUtils.getOperator((String)conditionMap.get("operation")) + " "
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
						sensingValueVO.setGID(GID);
						sensingValueVO.setSensorType(sensorType);
						sensingValueVO.setAdditionalCondition(additionalCondition);
						
						//tempResult = apiInterfaceService.selectSensingValueQueryByID(sensingValueVO);
						tempResult = apiInterfaceService.selectSensingValueLatestByID(sensingValueVO);
						
						if (tempResult != null)
							queryResult.addAll((List<EgovMap>)tempResult);
					}
				}
			}
			
			EgovMap queryMap = new EgovMap();
			List<Map<String, Object>> sensingValueList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> sensingValue = new LinkedHashMap<String, Object>();
				sensingValue.put("snID",			queryMap.get("sngid").toString());
				sensingValue.put("sensingTime",  	UsdmUtils.convertDateToStr((double)queryMap.get("sensingtime"), "yyyy-MM-dd HH:mm:ss.SSS"));
				sensingValue.put("sensorType", 		queryMap.get("sensortype"));
				sensingValue.put("sensingValue",	queryMap.get("sensingvalue").toString());
				
				sensingValueList.add(sensingValue);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("resultSet",  	sensingValueList);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/latestValueByID", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// query/spatioTemporal
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/query/spatioTemporal", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQuerySpatioTemporal(@RequestBody String filterJSON, HttpServletResponse response) {
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
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (targetList == null)		throw new InvalidParameterException("parameter 'targetList' has no value");
			if (sensorTypeList == null)	throw new InvalidParameterException("parameter 'sensingTypeList' has no value");
			
			// 시공간 조건이 존재하지 않는 경우 
			if (temporalConditionMap == null && spatialConditionMap == null) {
				throw new InvalidParameterException("either of temporal or spatial condition should exist");
			}
			
			/****************************/
			/* message validation : end */
			/****************************/
			
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
						String longitude = coordinateList.get(i).get(0);
				        String latitude  = coordinateList.get(i).get(1);
				        
				        double tmX = UsdmUtils.WGSLongitudeToEast(Double.parseDouble(latitude), Double.parseDouble(longitude));
				        double tmY = UsdmUtils.WGSLatitudeToNorth(Double.parseDouble(latitude), Double.parseDouble(longitude));
				        
				        coordinateStr += Double.toString(tmX) + " " + Double.toString(tmY);
				        
				        if (i<coordinateList.size()-1) coordinateStr += ",";
					}
					
					spatialCondition += " WHERE ST_INTERSECTS(ST_GEOMFROMTEXT('POLYGON((" + coordinateStr + "))',0), position) = 1";
				}
				// Circle
				else if (spatialType.equals("Circle")) {
					List<String> center = (ArrayList<String>)spatialConditionMap.get("center");

					double centerX = Double.parseDouble(center.get(0));
				    double centerY = Double.parseDouble(center.get(1));
				    double radius  = Double.parseDouble((String)spatialConditionMap.get("radius"));
				    
				    double tmX = UsdmUtils.WGSLongitudeToEast(centerY, centerX);
				    double tmY = UsdmUtils.WGSLatitudeToNorth(centerY, centerX);
				    
				    double minX = tmX - radius;
				    double minY = tmY - radius;
				    double maxX = tmX + radius;
				    double maxY = tmY + radius;
				    
				    String mbrPolygon  = minX + " " + minY + ","
				                       + maxX + " " + minY + ","
				                       + maxX + " " + maxY + ","
				                       + minX + " " + maxY + ","
				                       + minX + " " + minY;
				    
				    spatialCondition += " WHERE ST_INTERSECTS(ST_GEOMFROMTEXT('POLYGON((" + mbrPolygon + "))',0), position) = 1";
				    spatialCondition += " AND SQRT(POWER(ABS(ST_X(position)-" + tmX + "),2) + POWER(ABS(ST_Y(position)-" + tmY + "),2)) <= " + radius;
				}
				else {
					throw new InvalidParameterException("unsupported spatial object type");
				}
			}
			
			List<EgovMap> queryResult = new ArrayList<EgovMap>();
			List<?> tempResult = null;
			
			for (int i=0; i<targetList.size(); i++) {
				gwID	= UsdmUtils.getGwIDFromAddress(targetList.get(i));
				panID	= UsdmUtils.getPanIDFromAddress(targetList.get(i));
				snID 	= UsdmUtils.getSnIDFromAddress(targetList.get(i));
				tdID 	= UsdmUtils.getTdIDFromAddress(targetList.get(i));
				
				if (UsdmUtils.containsIgnoreCase(sensorTypeList, "ALL")) {
					SNSensingValueVO sensingValueVO = new SNSensingValueVO();
					sensingValueVO.setGwID(gwID);
					sensingValueVO.setPanID(panID);
					sensingValueVO.setSnID(snID);
					sensingValueVO.setTdID(tdID);
					sensingValueVO.setSensorType("ALL");
					sensingValueVO.setAdditionalCondition("");
					sensingValueVO.setTemporalCondition(temporalCondition);
					sensingValueVO.setSpatialCondition(spatialCondition);
					
					tempResult = apiInterfaceService.selectSensingValueSpatioTemporal(sensingValueVO);
					
					if (tempResult != null)
						queryResult.addAll((List<EgovMap>)tempResult);
				}
				else {
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
										throw new InvalidParameterException();
									}
									
									if (logicalOperator.equals("")) {
										additionalCondition1 = "sensingValue "
															 + UsdmUtils.getOperator((String)conditionMap.get("operation")) + " "
															 + (String)conditionMap.get("value");
									}
									else {
										additionalCondition2 = "sensingValue "
															 + UsdmUtils.getOperator((String)conditionMap.get("operation")) + " "
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
						
						if (tempResult != null)
							queryResult.addAll((List<EgovMap>)tempResult);
					}
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
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("resultSet",  	sensingValueList);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/spatioTemporal", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// query/spatioTemporalByID
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/query/spatioTemporalByID", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQuerySpatioTemporalByID(@RequestBody String filterJSON, HttpServletResponse response) {
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
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (targetList == null)		throw new InvalidParameterException("parameter 'targetList' has no value");
			if (sensorTypeList == null)	throw new InvalidParameterException("parameter 'sensingTypeList' has no value");
			
			// 시공간 조건이 존재하지 않는 경우 
			if (temporalConditionMap == null && spatialConditionMap == null) {
				throw new InvalidParameterException("either of temporal or spatial condition should exist");
			}
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String GID 			= "";
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
						String longitude = coordinateList.get(i).get(0);
				        String latitude  = coordinateList.get(i).get(1);
				        
				        double tmX = UsdmUtils.WGSLongitudeToEast(Double.parseDouble(latitude), Double.parseDouble(longitude));
				        double tmY = UsdmUtils.WGSLatitudeToNorth(Double.parseDouble(latitude), Double.parseDouble(longitude));
				        
				        coordinateStr += Double.toString(tmX) + " " + Double.toString(tmY);
				        
				        if (i<coordinateList.size()-1) coordinateStr += ",";
					}
					
					spatialCondition += " WHERE ST_INTERSECTS(ST_GEOMFROMTEXT('POLYGON((" + coordinateStr + "))',0), position) = 1";
				}
				// Circle
				else if (spatialType.equals("Circle")) {
					List<String> center = (ArrayList<String>)spatialConditionMap.get("center");

					double centerX = Double.parseDouble(center.get(0));
				    double centerY = Double.parseDouble(center.get(1));
				    double radius  = Double.parseDouble((String)spatialConditionMap.get("radius"));
				    
				    double tmX = UsdmUtils.WGSLongitudeToEast(centerY, centerX);
				    double tmY = UsdmUtils.WGSLatitudeToNorth(centerY, centerX);
				    
				    double minX = tmX - radius;
				    double minY = tmY - radius;
				    double maxX = tmX + radius;
				    double maxY = tmY + radius;
				    
				    String mbrPolygon  = minX + " " + minY + ","
				                       + maxX + " " + minY + ","
				                       + maxX + " " + maxY + ","
				                       + minX + " " + maxY + ","
				                       + minX + " " + minY;
				    
				    spatialCondition += " WHERE ST_INTERSECTS(ST_GEOMFROMTEXT('POLYGON((" + mbrPolygon + "))',0), position) = 1";
				    spatialCondition += " AND SQRT(POWER(ABS(ST_X(position)-" + tmX + "),2) + POWER(ABS(ST_Y(position)-" + tmY + "),2)) <= " + radius;
				}
				else {
					throw new InvalidParameterException("unsupported spatial object type");
				}
			}
			
			List<EgovMap> queryResult = new ArrayList<EgovMap>();
			List<?> tempResult = null;
			
			for (int i=0; i<targetList.size(); i++) {
				GID	= targetList.get(i);
				
				if (UsdmUtils.containsIgnoreCase(sensorTypeList, "ALL")) {
					SNSensingValueVO sensingValueVO = new SNSensingValueVO();
					sensingValueVO.setGID(GID);
					sensingValueVO.setSensorType("ALL");
					sensingValueVO.setAdditionalCondition("");
					sensingValueVO.setTemporalCondition(temporalCondition);
					sensingValueVO.setSpatialCondition(spatialCondition);
					
					tempResult = apiInterfaceService.selectSensingValueSpatioTemporalByID(sensingValueVO);
					
					if (tempResult != null)
						queryResult.addAll((List<EgovMap>)tempResult);
				}
				else {
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
										throw new InvalidParameterException();
									}
									
									if (logicalOperator.equals("")) {
										additionalCondition1 = "sensingValue "
															 + UsdmUtils.getOperator((String)conditionMap.get("operation")) + " "
															 + (String)conditionMap.get("value");
									}
									else {
										additionalCondition2 = "sensingValue "
															 + UsdmUtils.getOperator((String)conditionMap.get("operation")) + " "
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
						sensingValueVO.setGID(GID);
						sensingValueVO.setSensorType(sensorType);
						sensingValueVO.setAdditionalCondition(additionalCondition);
						sensingValueVO.setTemporalCondition(temporalCondition);
						sensingValueVO.setSpatialCondition(spatialCondition);
						
						tempResult = apiInterfaceService.selectSensingValueSpatioTemporalByID(sensingValueVO);
						
						if (tempResult != null)
							queryResult.addAll((List<EgovMap>)tempResult);
					}
				}
			}
			
			EgovMap queryMap = new EgovMap();
			List<Map<String, Object>> sensingValueList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> sensingValue = new LinkedHashMap<String, Object>();
				sensingValue.put("snID",			queryMap.get("sngid").toString());
				sensingValue.put("sensingTime",  	UsdmUtils.convertDateToStr((double)queryMap.get("sensingtime"), "yyyy-MM-dd HH:mm:ss.SSS"));
				sensingValue.put("sensorType", 		queryMap.get("sensortype"));
				sensingValue.put("sensingValue",	queryMap.get("sensingvalue").toString());
				
				sensingValueList.add(sensingValue);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("resultSet",  	sensingValueList);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/spatioTemporalByID", filterJSON, responseJson);
		
		return responseJson;
	}
	// query/spatioTemporalXY
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/query/spatioTemporalXY", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQuerySpatioTemporalXY(@RequestBody String filterJSON, HttpServletResponse response) {
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
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (targetList == null)		throw new InvalidParameterException("parameter 'targetList' has no value");
			if (sensorTypeList == null)	throw new InvalidParameterException("parameter 'sensingTypeList' has no value");
			
			// 시공간 조건이 존재하지 않는 경우 
			if (temporalConditionMap == null && spatialConditionMap == null) {
				throw new InvalidParameterException("either of temporal or spatial condition should exist");
			}
			
			/****************************/
			/* message validation : end */
			/****************************/
			
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
				        String longitude = coordinateList.get(i).get(0);
				        String latitude  = coordinateList.get(i).get(1);
				        
				        double tmX = Double.parseDouble(longitude);
				        double tmY = Double.parseDouble(latitude);
				        
				        coordinateStr += Double.toString(tmX) + " " + Double.toString(tmY); 
				        
				        if (i<coordinateList.size()-1) coordinateStr += ",";
				    }
				    
				    spatialCondition += " WHERE ST_INTERSECTS(ST_GEOMFROMTEXT('POLYGON((" + coordinateStr + "))',0), position) = 1";
				}
				// Circle
				else if (spatialType.equals("Circle")) {
				    List<String> center = (ArrayList<String>)spatialConditionMap.get("center");

				    double tmX = Double.parseDouble(center.get(0));
				    double tmY = Double.parseDouble(center.get(1));
				    double radius = Double.parseDouble((String)spatialConditionMap.get("radius"));
				    
				    double minX = tmX - radius;
				    double minY = tmY - radius;
				    double maxX = tmX + radius;
				    double maxY = tmY + radius;
				    
				    String mbrPolygon  = minX + " " + minY + ","
				                       + maxX + " " + minY + ","
				                       + maxX + " " + maxY + ","
				                       + minX + " " + maxY + ","
				                       + minX + " " + minY;
				    
				    spatialCondition += " WHERE ST_INTERSECTS(ST_GEOMFROMTEXT('POLYGON((" + mbrPolygon + "))',0), position) = 1";
				    spatialCondition += " AND SQRT(POWER(ABS(ST_X(position)-" + tmX + "),2) + POWER(ABS(ST_Y(position)-" + tmY + "),2)) <= " + radius;
				}
				else {
					throw new InvalidParameterException("unsupported spatial object type");
				}
			}
			
			List<EgovMap> queryResult = new ArrayList<EgovMap>();
			List<?> tempResult = null;
			
			for (int i=0; i<targetList.size(); i++) {
				gwID	= UsdmUtils.getGwIDFromAddress(targetList.get(i));
				panID	= UsdmUtils.getPanIDFromAddress(targetList.get(i));
				snID 	= UsdmUtils.getSnIDFromAddress(targetList.get(i));
				tdID 	= UsdmUtils.getTdIDFromAddress(targetList.get(i));
				
				if (UsdmUtils.containsIgnoreCase(sensorTypeList, "ALL")) {
					SNSensingValueVO sensingValueVO = new SNSensingValueVO();
					sensingValueVO.setGwID(gwID);
					sensingValueVO.setPanID(panID);
					sensingValueVO.setSnID(snID);
					sensingValueVO.setTdID(tdID);
					sensingValueVO.setSensorType("ALL");
					sensingValueVO.setAdditionalCondition("");
					sensingValueVO.setTemporalCondition(temporalCondition);
					sensingValueVO.setSpatialCondition(spatialCondition);
					
					tempResult = apiInterfaceService.selectSensingValueSpatioTemporal(sensingValueVO);
					
					if (tempResult != null)
						queryResult.addAll((List<EgovMap>)tempResult);
				}
				else {
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
										throw new InvalidParameterException();
									}
									
									if (logicalOperator.equals("")) {
										additionalCondition1 = "sensingValue "
												+ UsdmUtils.getOperator((String)conditionMap.get("operation")) + " "
												+ (String)conditionMap.get("value");
									}
									else {
										additionalCondition2 = "sensingValue "
												+ UsdmUtils.getOperator((String)conditionMap.get("operation")) + " "
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
						
						if (tempResult != null)
							queryResult.addAll((List<EgovMap>)tempResult);
					}
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
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("resultSet",  	sensingValueList);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/spatioTemporalXY", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// query/spatioTemporalXYByID
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/query/spatioTemporalXYByID", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQuerySpatioTemporalXYByID(@RequestBody String filterJSON, HttpServletResponse response) {
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
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (targetList == null)		throw new InvalidParameterException("parameter 'targetList' has no value");
			if (sensorTypeList == null)	throw new InvalidParameterException("parameter 'sensingTypeList' has no value");
			
			// 시공간 조건이 존재하지 않는 경우 
			if (temporalConditionMap == null && spatialConditionMap == null) {
				throw new InvalidParameterException("either of temporal or spatial condition should exist");
			}
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String GID 			= "";
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
				        String longitude = coordinateList.get(i).get(0);
				        String latitude  = coordinateList.get(i).get(1);
				        
				        double tmX = Double.parseDouble(longitude);
				        double tmY = Double.parseDouble(latitude);
				        
				        coordinateStr += Double.toString(tmX) + " " + Double.toString(tmY); 
				        
				        if (i<coordinateList.size()-1) coordinateStr += ",";
				    }
				    
				    spatialCondition += " WHERE ST_INTERSECTS(ST_GEOMFROMTEXT('POLYGON((" + coordinateStr + "))',0), position) = 1";
				}
				// Circle
				else if (spatialType.equals("Circle")) {
				    List<String> center = (ArrayList<String>)spatialConditionMap.get("center");

				    double tmX = Double.parseDouble(center.get(0));
				    double tmY = Double.parseDouble(center.get(1));
				    double radius = Double.parseDouble((String)spatialConditionMap.get("radius"));
				    
				    double minX = tmX - radius;
				    double minY = tmY - radius;
				    double maxX = tmX + radius;
				    double maxY = tmY + radius;
				    
				    String mbrPolygon  = minX + " " + minY + ","
				                       + maxX + " " + minY + ","
				                       + maxX + " " + maxY + ","
				                       + minX + " " + maxY + ","
				                       + minX + " " + minY;
				    
				    spatialCondition += " WHERE ST_INTERSECTS(ST_GEOMFROMTEXT('POLYGON((" + mbrPolygon + "))',0), position) = 1";
				    spatialCondition += " AND SQRT(POWER(ABS(ST_X(position)-" + tmX + "),2) + POWER(ABS(ST_Y(position)-" + tmY + "),2)) <= " + radius;
				}
				else {
					throw new InvalidParameterException("unsupported spatial object type");
				}
			}
			
			List<EgovMap> queryResult = new ArrayList<EgovMap>();
			List<?> tempResult = null;
			
			for (int i=0; i<targetList.size(); i++) {
				GID	= targetList.get(i);
				
				if (UsdmUtils.containsIgnoreCase(sensorTypeList, "ALL")) {
					SNSensingValueVO sensingValueVO = new SNSensingValueVO();
					sensingValueVO.setGID(GID);
					sensingValueVO.setSensorType("ALL");
					sensingValueVO.setAdditionalCondition("");
					sensingValueVO.setTemporalCondition(temporalCondition);
					sensingValueVO.setSpatialCondition(spatialCondition);
					
					tempResult = apiInterfaceService.selectSensingValueSpatioTemporalByID(sensingValueVO);
					
					if (tempResult != null)
						queryResult.addAll((List<EgovMap>)tempResult);
				}
				else {
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
										throw new InvalidParameterException();
									}
									
									if (logicalOperator.equals("")) {
										additionalCondition1 = "sensingValue "
												+ UsdmUtils.getOperator((String)conditionMap.get("operation")) + " "
												+ (String)conditionMap.get("value");
									}
									else {
										additionalCondition2 = "sensingValue "
												+ UsdmUtils.getOperator((String)conditionMap.get("operation")) + " "
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
						sensingValueVO.setGID(GID);
						sensingValueVO.setSensorType(sensorType);
						sensingValueVO.setAdditionalCondition(additionalCondition);
						sensingValueVO.setTemporalCondition(temporalCondition);
						sensingValueVO.setSpatialCondition(spatialCondition);
						
						tempResult = apiInterfaceService.selectSensingValueSpatioTemporalByID(sensingValueVO);
						
						if (tempResult != null)
							queryResult.addAll((List<EgovMap>)tempResult);
					}
				}
			}
			
			EgovMap queryMap = new EgovMap();
			List<Map<String, Object>> sensingValueList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> sensingValue = new LinkedHashMap<String, Object>();
				sensingValue.put("snID",			queryMap.get("sngid").toString());
				sensingValue.put("sensingTime",  	UsdmUtils.convertDateToStr((double)queryMap.get("sensingtime"), "yyyy-MM-dd HH:mm:ss.SSS"));
				sensingValue.put("sensorType", 		queryMap.get("sensortype"));
				sensingValue.put("sensingValue",	queryMap.get("sensingvalue").toString());
				
				sensingValueList.add(sensingValue);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("resultSet",  	sensingValueList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/spatioTemporalXYByID", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// query/accidentByGeoID
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/query/accidentByGeoID", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQueryAccidentByGeoID(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String geoID	= (String)filter.get("geoID");
			String geoType 	= (String)filter.get("geoType");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (geoID.equals(""))	throw new InvalidParameterException("parameter 'geoID' has no value");
			if (geoType.equals(""))	throw new InvalidParameterException("parameter 'geoType' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/

			SNAccidentVO accidentVO = new SNAccidentVO();
			accidentVO.setGeoType(geoType);
			accidentVO.setFtrIdn(geoID);
			
			List<?> queryResult = apiInterfaceService.selectAccident(accidentVO);
			List<Map<String, Object>> accidentList = new ArrayList<Map<String, Object>>();
			
			if (queryResult == null) throw new NoResourceException();
			
			for (int i=0; i<queryResult.size(); i++) {
				EgovMap queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> coordinate = new LinkedHashMap<String, Object>();
				coordinate.put("longitude", queryMap.get("longitude").toString());
				coordinate.put("latitude",  queryMap.get("latitude").toString());

				Map<String, Object> accidentResult = new LinkedHashMap<String, Object>();
				accidentResult.put("serialNumber",	queryMap.get("accidentid").toString());
				accidentResult.put("geoID",			queryMap.get("geoid").toString());
				accidentResult.put("geoType",		geoType);
				accidentResult.put("coordinate",	coordinate);
				accidentResult.put("accidentTime", 	UsdmUtils.convertDateToStr((double)queryMap.get("accidenttime"), "yyyy-MM-dd HH:mm:ss.SSS"));
				accidentResult.put("accidentType", 	queryMap.get("accidenttype"));
				accidentResult.put("accidentShape",	queryMap.get("accidentshape"));
				accidentResult.put("accidentDesc",	getAccidentDescMap((String)queryMap.get("accidentdesc")));
				
				accidentList.add(accidentResult);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("resultSet",  	accidentList);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/accidentByGeoID", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// query/accidentByGeoID2
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/query/accidentByGeoID2", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQueryAccidentByGeoID2(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String geoID	= (String)filter.get("geoID");
			String geoType 	= (String)filter.get("geoType");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (geoID.equals(""))	throw new InvalidParameterException("parameter 'geoID' has no value");
			if (geoType.equals(""))	throw new InvalidParameterException("parameter 'geoType' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			SNAccidentVO accidentVO = new SNAccidentVO();
			accidentVO.setGeoType(geoType);
			accidentVO.setFtrIdn(geoID);
			
			List<?> queryResult	= apiInterfaceService.selectAccident(accidentVO);
			List<Map<String, Object>> accidentList = new ArrayList<Map<String, Object>>();
			
			if (queryResult == null) throw new NoResourceException();
			
			for (int i=0; i<queryResult.size(); i++) {
				EgovMap queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> coordinate = new LinkedHashMap<String, Object>();
				coordinate.put("X", queryMap.get("x").toString());
				coordinate.put("Y", queryMap.get("y").toString());

				Map<String, Object> accidentResult = new LinkedHashMap<String, Object>();
				accidentResult.put("serialNumber",	queryMap.get("accidentid").toString());
				accidentResult.put("geoID",			queryMap.get("geoid").toString());
				accidentResult.put("geoType",		geoType);
				accidentResult.put("coordinate",	coordinate);
				accidentResult.put("accidentTime", 	UsdmUtils.convertDateToStr((double)queryMap.get("accidenttime"), "yyyy-MM-dd HH:mm:ss.SSS"));
				accidentResult.put("accidentType", 	queryMap.get("accidenttype"));
				accidentResult.put("accidentShape",	queryMap.get("accidentshape"));
				accidentResult.put("accidentDesc",	getAccidentDescMap((String)queryMap.get("accidentdesc")));
				
				accidentList.add(accidentResult);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("resultSet",  	accidentList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/accidentByGeoID2", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// query/accidentByRegion
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/query/accidentByRegion", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQueryAccidentByRegion(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			List<String> geoTypeList = (List<String>)filter.get("geoType");
			Map<String, Object> spatialConditionMap  = (Map<String, Object>)filter.get("region");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (geoTypeList.isEmpty())	throw new InvalidParameterException("parameter 'geoType' has no value");
			if (spatialConditionMap.isEmpty()) throw new InvalidParameterException("parameter 'region' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String spatialType = (String)spatialConditionMap.get("type");
			String spatialCondition = "";
			
			// Polygon
			if (spatialType.equals("Polygon")) {
				List<List<String>> coordinateList = (ArrayList<List<String>>)spatialConditionMap.get("coordinates");
				String coordinateStr = "";
				
				for (int i=0; i<coordinateList.size(); i++) {
					String longitude = coordinateList.get(i).get(0);
					String latitude  = coordinateList.get(i).get(1);
					
					double tmX = UsdmUtils.WGSLongitudeToEast(Double.parseDouble(latitude), Double.parseDouble(longitude));
		            double tmY = UsdmUtils.WGSLatitudeToNorth(Double.parseDouble(latitude), Double.parseDouble(longitude));
					
					coordinateStr += Double.toString(tmX) + " " + Double.toString(tmY); 
					
					if (i < coordinateList.size() - 1)
						coordinateStr += ",";
				}
				
				spatialCondition += " AND ST_INTERSECTS(ST_GEOMFROMTEXT('POLYGON((" + coordinateStr + "))',0), coordinate) = 1";
			}
			// Circle
			else if (spatialType.equals("Circle")) {
				List<String> center = (ArrayList<String>)spatialConditionMap.get("center");

				double centerX = Double.parseDouble(center.get(0));
				double centerY = Double.parseDouble(center.get(1));
				double radius  = Double.parseDouble((String)spatialConditionMap.get("radius"));
				
				double tmX = UsdmUtils.WGSLongitudeToEast(centerY, centerX);
	            double tmY = UsdmUtils.WGSLatitudeToNorth(centerY, centerX);
				
				double minX = tmX - radius;
				double minY = tmY - radius;
				double maxX = tmX + radius;
				double maxY = tmY + radius;
				
				String mbrPolygon  = minX + " " + minY + ","
								   + maxX + " " + minY + ","
								   + maxX + " " + maxY + ","
								   + minX + " " + maxY + ","
								   + minX + " " + minY;
				
				spatialCondition += " AND ST_INTERSECTS(ST_GEOMFROMTEXT('POLYGON((" + mbrPolygon + "))',0), coordinate) = 1";
				spatialCondition += " AND SQRT(POWER(ABS(ST_X(coordinate)-" + tmX + "),2) + POWER(ABS(ST_Y(coordinate)-" + tmY + "),2)) <= " + radius;
			}
			else {
				throw new InvalidParameterException("unsupported spatial object type");
			}
			
			List<Map<String, Object>> accidentList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<geoTypeList.size(); i++) {
				String geoType = geoTypeList.get(i);
				
				/******************************/
				/* message validation : start */
				/******************************/
				
				if (geoType.equals("")) throw new InvalidParameterException("parameter 'geoType' has no value");
				
				/****************************/
				/* message validation : end */
				/****************************/
			
				SNAccidentVO accidentVO = new SNAccidentVO();
				accidentVO.setGeoType(geoType);
				accidentVO.setSpatialCondition(spatialCondition);
				
				List<?> queryResult	= apiInterfaceService.selectAccidentByRegion(accidentVO);
				
				for (int j=0; j<queryResult.size(); j++) {
					EgovMap queryMap = (EgovMap)queryResult.get(j);
					
					Map<String, Object> accidentResult = new LinkedHashMap<String, Object>();
					Map<String, Object> coordinate = new LinkedHashMap<String, Object>();
					coordinate.put("longitude", queryMap.get("longitude").toString());
					coordinate.put("latitude",  queryMap.get("latitude").toString());
					
					accidentResult.put("serialNumber",	queryMap.get("accidentid").toString());
					accidentResult.put("geoID",			queryMap.get("geoid").toString());
					accidentResult.put("geoType",		geoType);
					accidentResult.put("coordinate",	coordinate);
					accidentResult.put("accidentTime", 	UsdmUtils.convertDateToStr((double)queryMap.get("accidenttime"), "yyyy-MM-dd HH:mm:ss.SSS"));
					accidentResult.put("accidentType", 	queryMap.get("accidenttype"));
					accidentResult.put("accidentShape",	queryMap.get("accidentshape"));
					accidentResult.put("accidentDesc",	getAccidentDescMap((String)queryMap.get("accidentdesc")));
					
					accidentList.add(accidentResult);
				}
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("resultSet",  	accidentList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/accidentByRegion", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// query/accidentByXYRegion
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/query/accidentByXYRegion", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQueryAccidentByXYRegion(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			List<String> geoTypeList = (List<String>)filter.get("geoType");
			Map<String, Object> spatialConditionMap  = (Map<String, Object>)filter.get("region");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (geoTypeList.isEmpty())	throw new InvalidParameterException("parameter 'geoType' has no value");
			if (spatialConditionMap.isEmpty()) throw new InvalidParameterException("parameter 'region' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String spatialType = (String)spatialConditionMap.get("type");
			String spatialCondition = "";
			
			// Polygon
			if (spatialType.equals("Polygon")) {
				List<List<String>> coordinateList = (ArrayList<List<String>>)spatialConditionMap.get("coordinates");
				String coordinateStr = "";
				
				for (int i=0; i<coordinateList.size(); i++) {
					String longitude = coordinateList.get(i).get(0);
					String latitude  = coordinateList.get(i).get(1);
					
					double tmX = Double.parseDouble(longitude);
		            double tmY = Double.parseDouble(latitude);
					
					coordinateStr += Double.toString(tmX) + " " + Double.toString(tmY); 
					
					if (i < coordinateList.size() - 1)
						coordinateStr += ",";
				}
				
				spatialCondition += " AND ST_INTERSECTS(ST_GEOMFROMTEXT('POLYGON((" + coordinateStr + "))',0), coordinate) = 1";
			}
			// Circle
			else if (spatialType.equals("Circle")) {
				List<String> center = (ArrayList<String>)spatialConditionMap.get("center");

				double tmX = Double.parseDouble(center.get(0));
				double tmY = Double.parseDouble(center.get(1));
				double radius = Double.parseDouble((String)spatialConditionMap.get("radius"));
				
				double minX = tmX - radius;
				double minY = tmY - radius;
				double maxX = tmX + radius;
				double maxY = tmY + radius;
				
				String mbrPolygon  = minX + " " + minY + ","
								   + maxX + " " + minY + ","
								   + maxX + " " + maxY + ","
								   + minX + " " + maxY + ","
								   + minX + " " + minY;
				
				spatialCondition += " AND ST_INTERSECTS(ST_GEOMFROMTEXT('POLYGON((" + mbrPolygon + "))',0), coordinate) = 1";
				spatialCondition += " AND SQRT(POWER(ABS(ST_X(coordinate)-" + tmX + "),2) + POWER(ABS(ST_Y(coordinate)-" + tmY + "),2)) <= " + radius;
			}
			else {
				throw new InvalidParameterException("unsupported spatial object type");
			}
			
			List<Map<String, Object>> accidentList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<geoTypeList.size(); i++) {
				String geoType = geoTypeList.get(i);
				
				/******************************/
				/* message validation : start */
				/******************************/
				
				if (geoType.equals("")) throw new InvalidParameterException("parameter 'geoType' has no value");
				
				/****************************/
				/* message validation : end */
				/****************************/
				
				SNAccidentVO accidentVO = new SNAccidentVO();
				accidentVO.setGeoType(geoType);
				accidentVO.setSpatialCondition(spatialCondition);
				
				List<?> queryResult	= apiInterfaceService.selectAccidentByRegion(accidentVO);
				
				for (int j=0; j<queryResult.size(); j++) {
					EgovMap queryMap = (EgovMap)queryResult.get(j);
					
					Map<String, Object> accidentResult = new LinkedHashMap<String, Object>();
					Map<String, Object> coordinate = new LinkedHashMap<String, Object>();
					coordinate.put("longitude", queryMap.get("longitude").toString());
					coordinate.put("latitude",  queryMap.get("latitude").toString());
					
					accidentResult.put("serialNumber",	queryMap.get("accidentid").toString());
					accidentResult.put("geoID",			queryMap.get("geoid").toString());
					accidentResult.put("geoType",		geoType);
					accidentResult.put("coordinate",	coordinate);
					accidentResult.put("accidentTime", 	UsdmUtils.convertDateToStr((double)queryMap.get("accidenttime"), "yyyy-MM-dd HH:mm:ss.SSS"));
					accidentResult.put("accidentType", 	queryMap.get("accidenttype"));
					accidentResult.put("accidentShape",	queryMap.get("accidentshape"));
					accidentResult.put("accidentDesc",	getAccidentDescMap((String)queryMap.get("accidentdesc")));
					
					accidentList.add(accidentResult);
				}
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("resultSet",  	accidentList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/accidentByXYRegion", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// query/valueByGeoObject
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/query/valueByGeoObject", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQueryValueByGeoObject(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			List<String> targetList		= (ArrayList<String>)filter.get("targetList");
			List<String> sensorTypeList = (ArrayList<String>)filter.get("sensingTypeList");
			
			String objectType = (String)filter.get("objectType");
			String startTime  = (String)filter.get("startTime");
			String endTime    = (String)filter.get("endTime");
			
			List<Map<String, Object>> conditionList = (ArrayList<Map<String, Object>>)filter.get("conditionList");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (targetList.isEmpty())		throw new InvalidParameterException("parameter 'targetList' has no value");
			if (sensorTypeList.isEmpty())	throw new InvalidParameterException("parameter 'sensingTypeList' has no value");
			
			if (objectType.equals(""))	throw new InvalidParameterException("parameter 'objectType' has no value");
			if (startTime.equals(""))	throw new InvalidParameterException("parameter 'startTime' has no value");
			if (endTime.equals(""))		throw new InvalidParameterException("parameter 'endTime' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String geoID      = "";
			String sensorType = "";
			
			long sTime = UsdmUtils.convertStrToDate(startTime, "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			long eTime = UsdmUtils.convertStrToDate(endTime,   "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			
			Map<String, Object> conditionMap = new LinkedHashMap<String, Object>();
			String additionalCondition 	= "";
			String additionalCondition1 = "";
			String logicalOperator		= "";
			String additionalCondition2 = "";
			
			List<EgovMap> queryResult = new ArrayList<EgovMap>();
			List<?> tempResult = null;
			
			for (int i=0; i<targetList.size(); i++) {
				geoID = targetList.get(i);
				
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
									throw new InvalidParameterException();
								}
								
								if (logicalOperator.equals("")) {
									additionalCondition1 = "sensingValue "
														 + UsdmUtils.getOperator((String)conditionMap.get("operation")) + " "
														 + (String)conditionMap.get("value");
								}
								else {
									additionalCondition2 = "sensingValue "
														 + UsdmUtils.getOperator((String)conditionMap.get("operation")) + " "
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

					SNGeoRelationVO nodeGeoRelVO = new SNGeoRelationVO();
					
					nodeGeoRelVO.setFtrIdn(geoID);
					nodeGeoRelVO.setSensorType(sensorType);
					nodeGeoRelVO.setStartTime(sTime);
					nodeGeoRelVO.setEndTime(eTime);
					nodeGeoRelVO.setGeoTable(UsdmUtils.getGeoTableName(objectType));
					nodeGeoRelVO.setAdditionalCondition(additionalCondition);
					
					tempResult = apiInterfaceService.selectValueByGeoObject(nodeGeoRelVO);
					
					queryResult.addAll((List<EgovMap>)tempResult);
				}
			}
			
			EgovMap queryMap = new EgovMap();
			List<Map<String, Object>> sensingValueList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> sensingValue = new LinkedHashMap<String, Object>();
				sensingValue.put("geoID",			queryMap.get("geoid").toString());
				sensingValue.put("snID",			queryMap.get("snid").toString());
				sensingValue.put("sensingTime",  	UsdmUtils.convertDateToStr((double)queryMap.get("sensingtime"), "yyyy-MM-dd HH:mm:ss.SSS"));
				sensingValue.put("tdType", 			queryMap.get("sensortype"));
				sensingValue.put("sensingValue",	queryMap.get("sensingvalue").toString());
				
				sensingValueList.add(sensingValue);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("resultSet",  	sensingValueList);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/valueByGeoObject", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// query/movingObject
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/query/movingObject", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQueryMovingObject(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String targetID		= (String)filter.get("targetID");
			String targetType 	= (String)filter.get("targetType");
			String startTime 	= (String)filter.get("startTime");
			String endTime		= (String)filter.get("endTime");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (targetID.equals(""))	throw new InvalidParameterException("parameter 'targetID' has no value");
			if (targetType.equals(""))	throw new InvalidParameterException("parameter 'targetType' has no value");
			if (startTime.equals(""))	throw new InvalidParameterException("parameter 'startTime' has no value");
			if (endTime.equals(""))		throw new InvalidParameterException("parameter 'endTime' has no value");
			
			// 동영상은 하수도와 철도에 대해서만 존재
			if (!targetType.equals("sewer") && !targetType.equals("subway"))
				throw new InvalidParameterException("unsupported 'targetType'");
			
			/****************************/
			/* message validation : end */
			/****************************/

			long sTime = UsdmUtils.convertStrToDate(startTime, "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			long eTime = UsdmUtils.convertStrToDate(endTime,   "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			
			GeoVideoRelVO videoVO = new GeoVideoRelVO();
			
			videoVO.setFtrIdn(Integer.parseInt(targetID));
			videoVO.setStartTime(UsdmUtils.convertDateToStr(sTime, "yyyy-MM-dd HH:mm:ss.SSSS"));
			videoVO.setEndTime(UsdmUtils.convertDateToStr(eTime, "yyyy-MM-dd HH:mm:ss.SSSS"));
			
			List<EgovMap> queryResult = new ArrayList<EgovMap>(); 
			
			if (targetType.equals("sewer"))
				queryResult = (List<EgovMap>) apiInterfaceService.selectSewerMovingObject(videoVO);
			else if (targetType.equals("subway"))
				queryResult = (List<EgovMap>) apiInterfaceService.selectSubwayMovingObject(videoVO);
				
			EgovMap	queryMap = new EgovMap();
			
			List<Map<String, Object>> videoList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);
				
				int videoID = (int)queryMap.get("videoid");
				
				// START: 조회한 videoID에 해당하는 geo-object ID의 리스트 조회
				GeoVideoRelVO geoVO = new GeoVideoRelVO();
				geoVO.setVideoID(videoID);
				
				List<EgovMap> geoIDResult = new ArrayList<EgovMap>();
				
				if (targetType.equals("sewer"))
					geoIDResult = (List<EgovMap>) apiInterfaceService.selectSewerVideoGeoID(geoVO);
				else if (targetType.equals("subway"))
					geoIDResult = (List<EgovMap>) apiInterfaceService.selectSubwayVideoGeoID(geoVO);
				
				EgovMap geoIDMap = new EgovMap();
				List<String> objectIDList = new ArrayList<String>();
				
				for (int j=0; j<geoIDResult.size(); j++) {
					geoIDMap = (EgovMap)geoIDResult.get(j);
					objectIDList.add(geoIDMap.get("geoid").toString());
				}
				// END: 조회한 videoID에 해당하는 geo-object ID의 리스트 조회
				
				Map<String, Object> videoMap = new LinkedHashMap<String, Object>();
				
				videoMap.put("objectIDList", 	objectIDList);
				videoMap.put("mhID", 			queryMap.get("mhid").toString());
				videoMap.put("direction", 		queryMap.get("direction").toString());
				videoMap.put("startTime", 		queryMap.get("starttime"));
				videoMap.put("endTime", 		queryMap.get("endtime"));
				videoMap.put("filename", 		queryMap.get("filename"));
				videoMap.put("directory", 		UsdmUtils.getFileDownloadPath((String)queryMap.get("filerealname"), targetType));
				videoMap.put("distance", 		queryMap.get("distance").toString());
				
				videoList.add(videoMap);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("resultSet",  	videoList);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/movingObject", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// query/sriGrid
	@RequestMapping(value="/query/sriGrid", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQuerySriGrid(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String minX		= (String)filter.get("minX");
			String minY		= (String)filter.get("minY");
			String maxX		= (String)filter.get("maxX");
			String maxY		= (String)filter.get("maxY");
			String minValue	= (String)filter.get("minValue");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (minX == null || minX.equals("")) 			throw new InvalidParameterException("parameter 'minX' has no value");
			if (minY == null || minY.equals("")) 			throw new InvalidParameterException("parameter 'minY' has no value");
			if (maxX == null || maxX.equals("")) 			throw new InvalidParameterException("parameter 'maxX' has no value");
			if (maxY == null || maxY.equals("")) 			throw new InvalidParameterException("parameter 'maxY' has no value");
			if (minValue == null || minValue.equals("")) 	throw new InvalidParameterException("parameter 'minValue' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
		
			SNSriGridVO sriGridVO = new SNSriGridVO();
			
			sriGridVO.setMinX(minX);
			sriGridVO.setMinY(minY);
			sriGridVO.setMaxX(maxX);
			sriGridVO.setMaxY(maxY);
			sriGridVO.setMinValue(Double.parseDouble(minValue));

			List<?> queryResult = apiInterfaceService.selectSriGrid(sriGridVO);
			
			if (queryResult.isEmpty()) throw new NoResourceException();
			
			EgovMap queryMap = new EgovMap();
			List<Map<String, Object>> resultSetList = new ArrayList<Map<String, Object>>();
			
			double cellSri = 0;
			String cellGrade = "";
			
			String sourceLayer = "";
			
			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);
				
				cellSri   = (double)queryMap.get("cellsri");
				cellGrade = queryMap.get("cellgrade").toString();
				
				if (cellSri == (double)queryMap.get("drainsri") && cellGrade.equals(queryMap.get("draingrade").toString()))
					sourceLayer = "sewer";
				else if (cellSri == (double)queryMap.get("watersri") && cellGrade.equals(queryMap.get("watergrade").toString()))
					sourceLayer = "water";
				else if (cellSri == (double)queryMap.get("subwaysri") && cellGrade.equals(queryMap.get("subwaygrade").toString()))
					sourceLayer = "subway";
				else if (cellSri == (double)queryMap.get("stationsri") && cellGrade.equals(queryMap.get("stationgrade").toString()))
					sourceLayer = "subway_s";
				else if (cellSri == (double)queryMap.get("geologysri") && cellGrade.equals(queryMap.get("geologygrade").toString()))
					sourceLayer = "geology";
				
				Map<String, Object> resultSet = new LinkedHashMap<String, Object>();
				
				resultSet.put("gridIndex",   queryMap.get("cellid").toString());
				resultSet.put("sriValue",    Double.toString(cellSri));
				resultSet.put("sourceLayer", sourceLayer);
				
				resultSetList.add(resultSet);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("resultSet",	resultSetList);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/sriGrid", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// query/xSRIGrid
	@RequestMapping(value="/query/xSRIGrid", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQueryXSRIGrid(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			List<String> cellIDList = (List<String>)filter.get("gridIndices");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (cellIDList == null) throw new InvalidParameterException("parameter 'gridIndices' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			SNSriGridVO sriGridVO = new SNSriGridVO();
			
			sriGridVO.setCellIDList(UsdmUtils.getInOperatorString(cellIDList));
			
			List<?> queryResult = apiInterfaceService.selectXSriGrid(sriGridVO);

			if (queryResult.isEmpty()) throw new NoResourceException();
			
			EgovMap queryMap = new EgovMap();
			List<Map<String, Object>> resultSetList = new ArrayList<Map<String, Object>>();

			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> resultSet = new LinkedHashMap<String, Object>();
				
				resultSet.put("gridIndex", queryMap.get("cellid").toString());
				resultSet.put("rSRI",      queryMap.get("cellsri").toString());
				resultSet.put("wSRI",      queryMap.get("watersri").toString());
				resultSet.put("sSRI",      queryMap.get("drainsri").toString());
				resultSet.put("mSRI",      queryMap.get("stationsri").toString());
				resultSet.put("tSRI",      queryMap.get("subwaysri").toString());
				resultSet.put("gSRI",      queryMap.get("geologysri").toString());
				
				resultSetList.add(resultSet);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("resultSet",	resultSetList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/xSRIGrid", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// query/getWaterManhole
	@RequestMapping(value="/query/getWaterManhole", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQueryGetWaterManhole(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			List<String> manholeIDList = (List<String>)filter.get("manholeIDs");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (manholeIDList==null || manholeIDList.isEmpty()) throw new InvalidParameterException("parameter 'manholeIDs' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String manholeID = "";
			
			List<Map<String, Object>> manholePipeRelList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<manholeIDList.size(); i++) {
				manholeID = manholeIDList.get(i);
			
				ManholePipeRelVO manholePipeRelVO = new ManholePipeRelVO();
				
				manholePipeRelVO.setManholeFtrIdn(Integer.parseInt(manholeID));
			
				List<?> queryResult = apiInterfaceService.selectWaterManholePipeRel(manholePipeRelVO);
				
				EgovMap queryMap = new EgovMap();
				
				List<String> pipeIDList = new ArrayList<String>();
				
				for (int j=0; j<queryResult.size(); j++) {
					queryMap = (EgovMap) queryResult.get(j);
					
					pipeIDList.add(queryMap.get("pipeFtrIdn").toString());
				}

				Map<String, Object> manholePipeRelMap = new HashMap<String, Object>();
				
				manholePipeRelMap.put("manholeID", manholeID);
				manholePipeRelMap.put("pipeIDs",   pipeIDList);
				
				manholePipeRelList.add(manholePipeRelMap);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("resultSet",	manholePipeRelList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/getWaterManhole", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// query/getSensingValueByID
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/query/getSensingValueByID", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQueryGetSensingValueByID(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String snID = UsdmUtils.getStringValue(filter, "snID");
			//String type = UsdmUtils.getStringValue(filter, "type");
			
			int targetTable = UsdmUtils.getIntegerValue(filter, "targetTable");
			int sortType 	= UsdmUtils.getIntegerValue(filter, "sortType");
			int count 		= UsdmUtils.getIntegerValue(filter, "count");
			
			List<String> tdTypeList	= (ArrayList<String>)filter.get("tdTypeList");
			
			List<Map<String, Object>> conditionList = (ArrayList<Map<String, Object>>)filter.get("conditionList");
			
			Map<String, Object> temporalConditionMap = (Map<String, Object>)filter.get("temporalCondition");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (tdTypeList == null)	throw new InvalidParameterException("parameter 'tdTypeList' has no value");
			
			if (!(targetTable==1 || targetTable==2 || targetTable==3))
				throw new InvalidParameterException("parameter 'targetTable' has invalid value");
			
			if (!(sortType==1 || sortType==2))
				throw new InvalidParameterException("parameter 'sortType' has invalid value");
			
			if (count < 0)
				throw new InvalidParameterException("parameter 'count' has invalid value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			// 정렬 방식
			String orderCondition = "ORDER BY sensingTime";
			if (sortType == 2) orderCondition += " DESC";
			
			// Fetch count
			String topCondition = "";
			if (count > 0) topCondition = "TOP " + count;
			
			String temporalCondition = "";
			
			// 시간조건이 존재하는 경우
			if (temporalConditionMap != null) {
				long startTime = UsdmUtils.convertStrToDate((String)temporalConditionMap.get("startTime"), "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
				long endTime   = UsdmUtils.convertStrToDate((String)temporalConditionMap.get("endTime"),   "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
				
				temporalCondition = " AND sensingTime >= " + startTime + " AND sensingTime <= " + endTime;
			}
			
			List<?> queryResult = null;
			
			String tdType = "";
			
			String validQuery 	= "";
			String invalidQuery	= "";
			String query 		= "";
			
			validQuery    = "SELECT b.GID AS snID, b.latitude, b.longitude, a.sensorType, a.sensingTime, a.sensingValue, '1' AS sourceTable";
			validQuery   += "  FROM sdm_SensingValue a, sdm_Node b";
			validQuery   += " WHERE a.snGID = b.GID AND b.GID = " + snID + temporalCondition;
			
			invalidQuery  = "SELECT b.GID AS snID, b.latitude, b.longitude, a.sensorType, a.sensingTime, a.sensingValue, '2' AS sourceTable";
			invalidQuery += "  FROM sdm_SensingValueInvalid a, sdm_Node b";
			invalidQuery += " WHERE a.snGID = b.GID AND b.GID = " + snID + temporalCondition;
			
			// 모든 센서타입을 조회하는 경우
			if (UsdmUtils.containsIgnoreCase(tdTypeList, "ALL")) {
				switch (targetTable) {
				case 1:
					query = validQuery;
					break;
				case 2:
					query = invalidQuery;
					break;
				case 3:
					query = validQuery + " UNION ALL " + invalidQuery;
					break;
				}
			}
			// 특정 센서타입을 조회하는 경우
			else {
				Map<String, Object> conditionMap = new LinkedHashMap<String, Object>();
				
				String additionalCondition 	= "";
				String additionalCondition1 = "";
				String logicalOperator		= "";
				String additionalCondition2 = "";
				
				for (int i=0; i<tdTypeList.size(); i++) {
					tdType = tdTypeList.get(i);
					
					additionalCondition  = " AND sensorType='" + tdType + "'";
					additionalCondition1 = "";
					logicalOperator		 = "";
					additionalCondition2 = "";
					
					if (conditionList != null) {
						// conditionList에서 sensorType과 동일한 tdType을 갖는 element 검색
						for (int j=0; j<conditionList.size(); j++) {
							conditionMap = conditionList.get(j);
							
							if (conditionMap.get("tdType").equals(tdType)) {
								if (!additionalCondition1.equals("") && !additionalCondition2.equals("")) {
									throw new InvalidParameterException();
								}
								
								if (logicalOperator.equals("")) {
									additionalCondition1 = "sensingValue "
														 + UsdmUtils.getOperator((String)conditionMap.get("operation")) + " "
														 + (String)conditionMap.get("value");
								}
								else {
									additionalCondition2 = "sensingValue "
														 + UsdmUtils.getOperator((String)conditionMap.get("operation")) + " "
														 + (String)conditionMap.get("value");
								}
						
								if (!conditionMap.get("logicalOp").equals("")) {
									logicalOperator = (String)conditionMap.get("logicalOp");
								}
							}
						}
						
						if (!additionalCondition1.equals("") || !additionalCondition2.equals("")) {
							if (logicalOperator.equals("")) {
								additionalCondition += " AND " + additionalCondition1;
							}
							else {
								additionalCondition += " AND (" + additionalCondition1 + " " + logicalOperator + " " + additionalCondition2 + ")";
							}
						}
					}
					
					switch (targetTable) {
					case 1:
						query += validQuery + additionalCondition;
						break;
					case 2:
						query += invalidQuery + additionalCondition;
						break;
					case 3:
						query += validQuery + additionalCondition + " UNION ALL " + invalidQuery + additionalCondition;
						break;
					}
					
					if (i < tdTypeList.size()-1) query += " UNION ALL ";
				}
			}
			
			SNSensingValueVO sensingValueVO = new SNSensingValueVO();
			sensingValueVO.setQuery(query);
			sensingValueVO.setTopCondition(topCondition);
			sensingValueVO.setOrderCondition(orderCondition);
			sensingValueVO.setTemporalCondition(temporalCondition);
			
			queryResult = apiInterfaceService.selectSensingValueByID(sensingValueVO);
			
			if (queryResult.isEmpty()) throw new NoResourceException();
			
			EgovMap queryMap = new EgovMap();
			List<Map<String, Object>> sensingValueList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> sensingValue = new LinkedHashMap<String, Object>();
				sensingValue.put("tdType", 			queryMap.get("sensortype").toString());
				sensingValue.put("sensingTime",  	UsdmUtils.convertDateToStr((double)queryMap.get("sensingtime"), "yyyy-MM-dd HH:mm:ss.SSS"));
				sensingValue.put("sensingValue",	queryMap.get("sensingvalue").toString());
				sensingValue.put("sourceTable",		Integer.parseInt(queryMap.get("sourcetable").toString()));
				
				sensingValueList.add(sensingValue);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("snID",  		snID);
			responseMessage.put("latitude",  	((EgovMap)queryResult.get(0)).get("latitude").toString());
			responseMessage.put("longitude",  	((EgovMap)queryResult.get(0)).get("longitude").toString());
			responseMessage.put("resultSet",  	sensingValueList);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/getSensingValueByID", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// query/repairHistory
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/query/repairHistory", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQueryRepairHistory(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String geoType = UsdmUtils.getStringValue(filter, "geoType");
			String geoID   = UsdmUtils.getStringValue(filter, "geoID");
			
			int category = UsdmUtils.getIntegerValue(filter, "category");
			
			Map<String, Object> temporalConditionMap = (Map<String, Object>)filter.get("temporalCondition");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (temporalConditionMap == null)	throw new InvalidParameterException("parameter 'temporalCondition' has no value");
			
			if (!(category==1 || category==2 || category==3 || category==9 || category==0))
				throw new InvalidParameterException("parameter 'category' has invalid value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			int ftrIdn = 0;
			
			if (!geoID.equalsIgnoreCase("ALL"))
				ftrIdn = Integer.parseInt(geoID);
			
			long startTime = UsdmUtils.convertStrToDate((String)temporalConditionMap.get("startTime"), "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			long endTime   = UsdmUtils.convertStrToDate((String)temporalConditionMap.get("endTime"),   "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			
			String temporalCondition = " AND repairDate >= " + startTime + " AND repairDate <= " + endTime;
			
			InfraRepairVO repairVO = new InfraRepairVO();
			
			repairVO.setGeoType(geoType);
			repairVO.setFtrIdn(ftrIdn);
			repairVO.setCategory(category);
			repairVO.setTemporalCondition(temporalCondition);
			
			List<?> queryResult = apiInterfaceService.selectInfraRepair(repairVO);
			
			if (queryResult.isEmpty()) throw new NoResourceException();
			
			EgovMap queryMap = new EgovMap();
			List<Map<String, Object>> resultMapList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
				
				resultMap.put("geoType", 	queryMap.get("geotype").toString());
				resultMap.put("geoID",  	queryMap.get("ftrIdn").toString());
				resultMap.put("date",		UsdmUtils.convertDateToStr((double)queryMap.get("repairdate"), "yyyy-MM-dd"));
				resultMap.put("category",	Integer.parseInt(queryMap.get("category").toString()));
				resultMap.put("contents", 	queryMap.get("contents").toString());

				if (queryMap.get("longitude") != null && queryMap.get("latitude") != null) {
					Map<String, Object> coordinateMap = new LinkedHashMap<String, Object>();
					
					coordinateMap.put("longitude", queryMap.get("longitude").toString());
					coordinateMap.put("latitude",  queryMap.get("latitude").toString());
					
					resultMap.put("coordinate", coordinateMap);
				}
				
				resultMapList.add(resultMap);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("resultSet",  	resultMapList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/repairHistory", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// query/repairHistorySubsidence
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/query/repairHistorySubsidence", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQueryRepairHistorySubsidence(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			int 	target	= UsdmUtils.getIntegerValue(filter, "target");
			String 	place 	= UsdmUtils.getStringValue(filter, "place");
			
			Map<String, Object> temporalConditionMap = (Map<String, Object>)filter.get("temporalCondition");
			
			List<?> spatialConditionList = (List<?>)filter.get("spatialCondition");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (temporalConditionMap == null)	throw new InvalidParameterException("parameter 'temporalCondition' has no value");
			if (spatialConditionList == null)	throw new InvalidParameterException("parameter 'spatialCondition' has no value");
			
			if (!(target==11 || target==12 || target==0))
				throw new InvalidParameterException("parameter 'target' has invalid value");
			
			if (spatialConditionList.size() != 4)
				throw new InvalidParameterException("parameter 'spatialCondition' must have 4 elements");
			
			/****************************/
			/* message validation : end */
			/****************************/

			String temporalCondition = "";
			String spatialCondition  = "";
			
			long startTime = UsdmUtils.convertStrToDate((String)temporalConditionMap.get("startTime"), "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			long endTime   = UsdmUtils.convertStrToDate((String)temporalConditionMap.get("endTime"),   "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			
			temporalCondition = " AND repairDate >= " + startTime + " AND repairDate <= " + endTime;

			String minX = spatialConditionList.get(0).toString();
			String minY = spatialConditionList.get(1).toString();
		    String maxX = spatialConditionList.get(2).toString();
		    String maxY = spatialConditionList.get(3).toString();
		    
		    String mbrPolygon  = minX + " " + minY + ","
		                       + maxX + " " + minY + ","
		                       + maxX + " " + maxY + ","
		                       + minX + " " + maxY + ","
		                       + minX + " " + minY;
		    
		    spatialCondition += " AND ST_INTERSECTS(ST_GEOMFROMTEXT('POLYGON((" + mbrPolygon + "))',0), coordinate) = 1";
		    
			SubsidenceRepairVO repairVO = new SubsidenceRepairVO();
			
			repairVO.setTarget(target);
			repairVO.setPlace(place);
			repairVO.setTemporalCondition(temporalCondition);
			repairVO.setSpatialCondition(spatialCondition);
			
			List<?> queryResult = apiInterfaceService.selectSubsidenceRepair(repairVO);
			
			if (queryResult.isEmpty()) throw new NoResourceException();
			
			EgovMap queryMap = new EgovMap();
			List<Map<String, Object>> resultMapList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);

				Map<String, Object> coordinateMap = new LinkedHashMap<String, Object>();
				coordinateMap.put("longitude", queryMap.get("longitude").toString());
				coordinateMap.put("latitude",  queryMap.get("latitude").toString());
				
				Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
				resultMap.put("target", 	Integer.parseInt(queryMap.get("target").toString()));
				resultMap.put("place", 		queryMap.get("place").toString());
				resultMap.put("date",		UsdmUtils.convertDateToStr((double)queryMap.get("repairdate"), "yyyy-MM-dd"));
				resultMap.put("contents", 	queryMap.get("contents").toString());
				resultMap.put("coordinate", coordinateMap);
				
				resultMapList.add(resultMap);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("resultSet",  	resultMapList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/repairHistorySubsidence", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// query/getEvent
	@RequestMapping(value="/query/getEvent", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQueryGetEvent(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			List<Integer> targetList = (List<Integer>)filter.get("targetList");
			
			Map<String, Object> temporalConditionMap = (Map<String, Object>)filter.get("temporalCondition");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (targetList == null) 			throw new InvalidParameterException("parameter 'targetList' has no value");
			if (temporalConditionMap == null) 	throw new InvalidParameterException("parameter 'temporalCondition' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			List<String> eventNameList = new ArrayList<String>();
			
			for (int i=0; i<targetList.size(); i++) {
				switch (targetList.get(i)) {
				case 1:
					eventNameList.add(UsdmUtils.MQ_REBOOT);
					break;
				case 2:
					eventNameList.add(UsdmUtils.MQ_SRICHANGED);
					break;
				case 3:
					eventNameList.add(UsdmUtils.MQ_LEAKOCCURED);
					break;
				case 4:
					eventNameList.add(UsdmUtils.MQ_ACCIDENTOCCURED);
					break;
				case 5:
					eventNameList.add(UsdmUtils.MQ_RFIDSTATECHANGED);
					break;
				case 9:
					eventNameList.add(UsdmUtils.MQ_LOWBATTERY);
					break;
				case 11:
					eventNameList.add(UsdmUtils.MQ_LOWUARTACTIVITY);
					break;
				default:
					continue;
				}
			}
			
			long startTime = UsdmUtils.convertStrToDate((String)temporalConditionMap.get("startTime"), "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			long endTime   = UsdmUtils.convertStrToDate((String)temporalConditionMap.get("endTime"),   "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			
			EventVO eventVO = new EventVO();
			eventVO.setEventName(UsdmUtils.getInOperatorString(eventNameList));
			eventVO.setTemporalCondition(" AND eventTime >= " + startTime + " AND eventTime <= " + endTime);
			
			List<?> queryResult = apiInterfaceService.selectEvent(eventVO);

			if (queryResult.isEmpty()) throw new NoResourceException();
			
			EgovMap queryMap = new EgovMap();
			List<Map<String, Object>> resultSetList = new ArrayList<Map<String, Object>>();

			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> resultSet = new LinkedHashMap<String, Object>();
				
				resultSet.put("eventName",	queryMap.get("eventname").toString());
				resultSet.put("resourceID", queryMap.get("resourceid").toString());
				resultSet.put("value", 		queryMap.get("eventvalue").toString());
				resultSet.put("timestamp",  UsdmUtils.convertDateToStr((double)queryMap.get("eventtime"), "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
				
				resultSetList.add(resultSet);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("resultSet",	resultSetList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/getEvent", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// query/getGridByColor
	@RequestMapping(value="/query/getGridByColor", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQueryGetGridByColor(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String level = UsdmUtils.getStringValue(filter, "level").toUpperCase();
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			SNSriGridVO sriGridVO = new SNSriGridVO();
			sriGridVO.setGrade(level);
			
			List<?> queryResult = apiInterfaceService.selectGridByColor(sriGridVO);
			
			if (queryResult.isEmpty()) throw new NoResourceException();
			
			double cellSri = 0;
			String sourceLayer = "";
			
			EgovMap queryMap = new EgovMap();
			List<Map<String, Object>> resultSetList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);
				
				cellSri = (double)queryMap.get("cellsri");
				
				if (cellSri == (double)queryMap.get("drainsri") && level.equals(queryMap.get("draingrade").toString()))
					sourceLayer = "sewer";
				else if (cellSri == (double)queryMap.get("watersri") && level.equals(queryMap.get("watergrade").toString()))
					sourceLayer = "water";
				else if (cellSri == (double)queryMap.get("subwaysri") && level.equals(queryMap.get("subwaygrade").toString()))
					sourceLayer = "subway";
				else if (cellSri == (double)queryMap.get("stationsri") && level.equals(queryMap.get("stationgrade").toString()))
					sourceLayer = "subway_s";
				else if (cellSri == (double)queryMap.get("geologysri") && level.equals(queryMap.get("geologygrade").toString()))
					sourceLayer = "geology";
				
				Map<String, Object> resultSet = new LinkedHashMap<String, Object>();
				
				resultSet.put("gridIndex",	 queryMap.get("cellid").toString());
				resultSet.put("sriValue",    cellSri);
				resultSet.put("sourceLayer", sourceLayer);
				
				resultSetList.add(resultSet);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("gridInfoList",	resultSetList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/getGridByColor", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// query/getGridBySRI
	@RequestMapping(value="/query/getGridBySRI", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQueryGetGridBySRI(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			Double sriValue   = UsdmUtils.getDoubleValue(filter, "sriValue");
			String relationOp = UsdmUtils.getStringValue(filter, "relationOp");
			Double operand    = UsdmUtils.getDoubleValue(filter, "operand", false);
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (relationOp.equalsIgnoreCase("BT") || relationOp.equalsIgnoreCase("BE"))
				if (operand == UsdmUtils.NULL_DOUBLE)
					throw new InvalidParameterException("parameter 'operand' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String additionalCondition = "";
			
			if (relationOp.equalsIgnoreCase("BT")) {
				additionalCondition = "WHERE cellSri > " + sriValue + " AND cellSri < " + operand;
			}
			else if (relationOp.equalsIgnoreCase("BE")) {
				additionalCondition = "WHERE cellSri >= " + sriValue + " AND cellSri <= " + operand;
			}
			else {
				additionalCondition = "WHERE cellSri " + UsdmUtils.getOperator(relationOp) + sriValue; 
			}
			
			SNSriGridVO sriGridVO = new SNSriGridVO();
			sriGridVO.setAdditionalCondition(additionalCondition);
			
			List<?> queryResult = apiInterfaceService.selectGridBySRI(sriGridVO);
			
			if (queryResult.isEmpty()) throw new NoResourceException();
			
			double cellSri = 0;
			String cellGrade   = "";
			String sourceLayer = "";
			
			EgovMap queryMap = new EgovMap();
			List<Map<String, Object>> resultSetList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);
				
				cellSri = (double)queryMap.get("cellsri");
				cellGrade = (String)queryMap.get("cellgrade");
				
				if (cellSri == (double)queryMap.get("drainsri") && cellGrade.equals(queryMap.get("draingrade").toString()))
					sourceLayer = "sewer";
				else if (cellSri == (double)queryMap.get("watersri") && cellGrade.equals(queryMap.get("watergrade").toString()))
					sourceLayer = "water";
				else if (cellSri == (double)queryMap.get("subwaysri") && cellGrade.equals(queryMap.get("subwaygrade").toString()))
					sourceLayer = "subway";
				else if (cellSri == (double)queryMap.get("stationsri") && cellGrade.equals(queryMap.get("stationgrade").toString()))
					sourceLayer = "subway_s";
				else if (cellSri == (double)queryMap.get("geologysri") && cellGrade.equals(queryMap.get("geologygrade").toString()))
					sourceLayer = "geology";
				
				Map<String, Object> resultSet = new LinkedHashMap<String, Object>();
				
				resultSet.put("gridIndex",	 queryMap.get("cellid").toString());
				resultSet.put("sriValue",    cellSri);
				resultSet.put("sourceLayer", sourceLayer);
				
				resultSetList.add(resultSet);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("gridInfoList",	resultSetList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/getGridBySRI", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// query/getInfraByColor
	@RequestMapping(value="/query/getInfraByColor", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQueryGetInfraByColor(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String level = UsdmUtils.getStringValue(filter, "level").toUpperCase();
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			SNSriVO sriVO = new SNSriVO();
			sriVO.setSriGrade(level);
			
			List<?> queryResult = apiInterfaceService.selectInfraByColor(sriVO);
			
			if (queryResult.isEmpty()) throw new NoResourceException();
			
			EgovMap queryMap = new EgovMap();
			List<Map<String, Object>> resultSetList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> resultSet = new LinkedHashMap<String, Object>();
				
				resultSet.put("geoID",	 	queryMap.get("geoid").toString());
				resultSet.put("geoType",	queryMap.get("geotype").toString());
				resultSet.put("BSRI", 		queryMap.get("bsri").toString());
				resultSet.put("level", 		queryMap.get("level").toString());
				
				resultSetList.add(resultSet);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("infraInfoList", resultSetList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/getInfraByColor", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// query/getInfraBySRI
	@RequestMapping(value="/query/getInfraBySRI", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQueryGetInfraBySRI(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			Double sriValue   = UsdmUtils.getDoubleValue(filter, "sriValue");
			String relationOp = UsdmUtils.getStringValue(filter, "relationOp");
			Double operand    = UsdmUtils.getDoubleValue(filter, "operand", false);
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (relationOp.equalsIgnoreCase("BT") || relationOp.equalsIgnoreCase("BE"))
				if (operand == UsdmUtils.NULL_DOUBLE)
					throw new InvalidParameterException("parameter 'operand' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String additionalCondition = "";
			
			if (relationOp.equalsIgnoreCase("BT")) {
				additionalCondition = "WHERE bsri > " + sriValue + " AND bsri < " + operand;
			}
			else if (relationOp.equalsIgnoreCase("BE")) {
				additionalCondition = "WHERE bsri >= " + sriValue + " AND bsri <= " + operand;
			}
			else {
				additionalCondition = "WHERE bsri " + UsdmUtils.getOperator(relationOp) + sriValue; 
			}
			
			SNSriVO sriVO = new SNSriVO();
			sriVO.setAdditionalCondition(additionalCondition);
			
			List<?> queryResult = apiInterfaceService.selectInfraBySRI(sriVO);
			
			if (queryResult.isEmpty()) throw new NoResourceException();
			
			EgovMap queryMap = new EgovMap();
			List<Map<String, Object>> resultSetList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> resultSet = new LinkedHashMap<String, Object>();
				
				resultSet.put("geoID",	 	queryMap.get("geoid").toString());
				resultSet.put("geoType",	queryMap.get("geotype").toString());
				resultSet.put("BSRI", 		queryMap.get("bsri").toString());
				resultSet.put("level", 		queryMap.get("level").toString());
				
				resultSetList.add(resultSet);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("infraInfoList", resultSetList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/getInfraBySRI", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// query/getInfraByAttribute
	@RequestMapping(value="/query/getInfraByAttribute", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQueryGetInfraByAttribute(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String target     		= UsdmUtils.getStringValue(filter, "target");
			String attribute  		= UsdmUtils.getStringValue(filter, "attribute");
			String attributeValue	= UsdmUtils.getStringValue(filter, "attributeValue");
			String relationOp 		= UsdmUtils.getStringValue(filter, "relationOp");
			String operand    		= UsdmUtils.getStringValue(filter, "operand", false);
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (relationOp.equalsIgnoreCase("BT") || relationOp.equalsIgnoreCase("BE"))
				if (operand == null)
					throw new InvalidParameterException("parameter 'operand' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String additionalCondition = "";
			
			if (relationOp.equalsIgnoreCase("BT")) {
				additionalCondition = "WHERE " + attribute + " > " + attributeValue + " AND " + attribute + " < " + operand;
			}
			else if (relationOp.equalsIgnoreCase("BE")) {
				additionalCondition = "WHERE " + attribute + " >= " + attributeValue + " AND " + attribute + " <= " + operand;
			}
			else {
				additionalCondition = "WHERE " + attribute + UsdmUtils.getOperator(relationOp) + attributeValue;
			}
			
			SNSriVO sriVO = new SNSriVO();
			sriVO.setGeoType(target);
			sriVO.setAdditionalCondition(additionalCondition);
			
			List<?> queryResult = apiInterfaceService.selectInfraByAttribute(sriVO);
			
			if (queryResult.isEmpty()) throw new NoResourceException();
			
			EgovMap queryMap = new EgovMap();
			List<Map<String, Object>> resultSetList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> resultSet = new LinkedHashMap<String, Object>();
				
				resultSet.put("geoID",	 	queryMap.get("geoid").toString());
				resultSet.put("geoType",	target);
				resultSet.put("BSRI", 		queryMap.get("bsri").toString());
				resultSet.put("level", 		queryMap.get("level").toString());
				
				resultSetList.add(resultSet);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("infraInfoList", resultSetList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/getInfraByAttribute", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// query/getInfraInGrid
	@RequestMapping(value="/query/getInfraInGrid", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQueryGetInfraInGrid(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");

			List<String> gridList   = (List<String>)filter.get("gridList");
			List<String> targetList = (List<String>)filter.get("targetList");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (gridList == null)   throw new InvalidParameterException("parameter 'gridList' has no value");
			if (targetList == null) throw new InvalidParameterException("parameter 'targetList' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String cellIDList = UsdmUtils.getInOperatorString(gridList);
			String typeList   = UsdmUtils.getInOperatorString(targetList);
			
			String additionalCondition = "WHERE geoType IN (" + typeList + ")";
			
			SNSriGridVO sriGridVO = new SNSriGridVO();
			sriGridVO.setCellIDList(cellIDList);
			sriGridVO.setAdditionalCondition(additionalCondition);
			
			List<?> queryResult = apiInterfaceService.selectInfraInGrid(sriGridVO);
			
			if (queryResult.isEmpty()) throw new NoResourceException();
			
			EgovMap queryMap = new EgovMap();
			List<Map<String, Object>> resultSetList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> resultSet = new LinkedHashMap<String, Object>();
				
				resultSet.put("gridIndex",	queryMap.get("gridindex").toString());
				resultSet.put("geoID",	 	queryMap.get("geoid").toString());
				resultSet.put("geoType",	queryMap.get("geotype").toString());
				resultSet.put("BSRI", 		queryMap.get("bsri").toString());
				resultSet.put("level", 		queryMap.get("level").toString());
				
				resultSetList.add(resultSet);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("infraInfoList", resultSetList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/getInfraInGrid", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// query/getInfraInGridBySRI
	@RequestMapping(value="/query/getInfraInGridBySRI", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQueryGetInfraInGridBySRI(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			List<String> gridList   = (List<String>)filter.get("gridList");
			List<String> targetList = (List<String>)filter.get("targetList");
			
			Double sriValue   = UsdmUtils.getDoubleValue(filter, "sriValue");
			String relationOp = UsdmUtils.getStringValue(filter, "relationOp");
			Double operand    = UsdmUtils.getDoubleValue(filter, "operand", false);
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (gridList == null)   throw new InvalidParameterException("parameter 'gridList' has no value");
			if (targetList == null) throw new InvalidParameterException("parameter 'targetList' has no value");
			
			if (relationOp.equalsIgnoreCase("BT") || relationOp.equalsIgnoreCase("BE"))
				if (operand == UsdmUtils.NULL_DOUBLE)
					throw new InvalidParameterException("parameter 'operand' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String cellIDList = UsdmUtils.getInOperatorString(gridList);
			String typeList   = UsdmUtils.getInOperatorString(targetList);
			
			String additionalCondition = "WHERE geoType IN (" + typeList + ")";
			
			if (relationOp.equalsIgnoreCase("BT")) {
				additionalCondition += " AND bsri > " + sriValue + " AND bsri < " + operand;
			}
			else if (relationOp.equalsIgnoreCase("BE")) {
				additionalCondition += " AND bsri >= " + sriValue + " AND bsri <= " + operand;
			}
			else {
				additionalCondition += " AND bsri " + UsdmUtils.getOperator(relationOp) + sriValue; 
			}
			
			SNSriGridVO sriGridVO = new SNSriGridVO();
			sriGridVO.setCellIDList(cellIDList);
			sriGridVO.setAdditionalCondition(additionalCondition);
			
			List<?> queryResult = apiInterfaceService.selectInfraInGrid(sriGridVO);
			
			if (queryResult.isEmpty()) throw new NoResourceException();
			
			EgovMap queryMap = new EgovMap();
			List<Map<String, Object>> resultSetList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> resultSet = new LinkedHashMap<String, Object>();
				
				resultSet.put("gridIndex",	queryMap.get("gridindex").toString());
				resultSet.put("geoID",	 	queryMap.get("geoid").toString());
				resultSet.put("geoType",	queryMap.get("geotype").toString());
				resultSet.put("BSRI", 		queryMap.get("bsri").toString());
				resultSet.put("level", 		queryMap.get("level").toString());
				
				resultSetList.add(resultSet);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("infraInfoList", resultSetList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/getInfraInGridBySRI", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/gatewayIDList
	@RequestMapping(value="/information/gatewayIDList", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationGatewayIDList(@RequestBody String filterJSON, HttpServletResponse response) {
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
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			List<?> 		queryResult 	= apiInterfaceService.selectGatewayIDList();
			EgovMap 		queryMap 		= new EgovMap();
			List<String> 	gatewayIDList 	= new ArrayList<String>();
			
			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);
				gatewayIDList.add((String)queryMap.get("gwid"));
			}
			
			if (gatewayIDList.size() == 0) {
				throw new NoResourceException();
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("gwIDList",  	gatewayIDList);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/gatewayIDList", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/resourceDescription
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/resourceDescription", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationResourceDescription(@RequestBody String filterJSON, HttpServletResponse response) {
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
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (targetList.isEmpty())	throw new InvalidParameterException("parameter 'targetList' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
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
	            
				if (gwID.equals("")) throw new InvalidParameterException("parameter 'gwID' has no value");
				
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
			
			if (resourceDescriptionList.size() == 0) {
				throw new NoResourceException();
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("resourceDescriptionList", resourceDescriptionList);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/resourceDescription", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/resourceStatus
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/resourceStatus", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationResourceStatus(@RequestBody String filterJSON, HttpServletResponse response) {
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
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (targetList.isEmpty())	throw new InvalidParameterException("parameter 'targetList' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
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
				
				if (gwID.equals("")) throw new InvalidParameterException("parameter 'gwID' has no value");
				
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
			
			if (statusList.size() == 0) {
				throw new NoResourceException();
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("statusList",  	statusList);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/resourceStatus", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/gatewayIDList2
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/gatewayIDList2", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationGatewayIDList2(@RequestBody String filterJSON, HttpServletResponse response) {
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
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			List<?> 		queryResult 	= apiInterfaceService.selectGatewayIDList2();
			EgovMap 		queryMap 		= new EgovMap();
			List<String> 	gatewayIDList 	= new ArrayList<String>();
			
			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);
				gatewayIDList.add(queryMap.get("gid").toString());
			}
			
			if (gatewayIDList.size() == 0) {
				throw new NoResourceException();
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("gwIDList",  	gatewayIDList);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/gatewayIDList2", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/resourceDescriptionByID
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/resourceDescriptionByID", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationResourceDescriptionByID(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			ArrayList<String> targetList	= (ArrayList<String>)filter.get("targetList");
			ArrayList<String> typeList 		= (ArrayList<String>)filter.get("targetTypeList");
			ArrayList<String> rangeList 	= (ArrayList<String>)filter.get("rangeList");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (targetList.isEmpty())	throw new InvalidParameterException("parameter 'targetList' has no value");
			if (typeList.isEmpty())		throw new InvalidParameterException("parameter 'targetTypeList' has no value");
			if (rangeList.isEmpty())	throw new InvalidParameterException("parameter 'rangeList' has no value");
			
			if (targetList.size() != typeList.size()) 	throw new InvalidParameterException("'targetList' and 'targetTypeList' must have same size");
			if (targetList.size() != rangeList.size()) 	throw new InvalidParameterException("'targetTypeList' and 'rangeList' must have same size");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String GID		= "";
			String type		= "";
			String range	= "";
			String panRange = "";
			String snRange 	= "";
			String tdRange 	= "";
			
			List<?>	tempGatewayResult 		= null;
			List<?> tempPanResult 			= null;
			List<?> tempNodeResult 			= null;
			List<?> tempTransducerResult 	= null;
			
			List<Map<String, Object>> resourceDescriptionList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<targetList.size(); i++) {
	            GID  	= targetList.get(i);
	            type	= typeList.get(i);
	            range 	= rangeList.get(i);
	            
	            /******************************/
				/* message validation : start */
				/******************************/
	            
				if (GID.equals("")) 	throw new InvalidParameterException("parameter 'targetList' has no value");
				if (type.equals("")) 	throw new InvalidParameterException("parameter 'targetTypeList' has no value");
				//if (range.equals("")) 	throw new InvalidParameterException("parameter 'rangeList' has no value");
				
				if (!type.equals("GATENODE") && !type.equals("PAN") && !type.equals("SENSORNODE") && !type.equals("TRANSDUCER"))
	            	throw new InvalidParameterException("unsupported 'targetType'");
				
				/****************************/
				/* message validation : end */
				/****************************/

				// gateway 정보 조회
				if (type.equals("GATENODE")) {
					panRange = range.split(":",3)[0];
					snRange  = range.split(":",3)[1];
					tdRange  = range.split(":",3)[2];

					SNGatewayVO gatewayVO = new SNGatewayVO();
					gatewayVO.setGID(GID);
					
					tempGatewayResult = apiInterfaceService.selectGatewayDescriptionByGID(gatewayVO);
					
					// 하위 pan을 조회하는 경우
					if (panRange.equals("FF"))
						tempPanResult = apiInterfaceService.selectPanDescriptionByGwGID(gatewayVO);
					
					// 하위 node를 조회하는 경우
					if (snRange.equals("FF"))
						tempNodeResult = apiInterfaceService.selectNodeDescriptionByGwGID(gatewayVO);
					
					// 하위 transducer를 조회하는 경우
					if (tdRange.equals("FF"))
						tempTransducerResult = apiInterfaceService.selectTransducerDescriptionByGwGID(gatewayVO);
				}
				// pan 정보 조회
				else if (type.equals("PAN")) {
					snRange  = range.split(":",2)[0];
					tdRange  = range.split(":",2)[1];
					
					SNPanVO panVO = new SNPanVO();
					panVO.setGID(GID);
					
					tempPanResult = apiInterfaceService.selectPanDescriptionByGID(panVO);
					
					// 하위 node를 조회하는 경우
					if (snRange.equals("FF"))
						tempNodeResult = apiInterfaceService.selectNodeDescriptionByPanGID(panVO);
					
					// 하위 transducer를 조회하는 경우
					if (tdRange.equals("FF"))
						tempTransducerResult = apiInterfaceService.selectTransducerDescriptionByPanGID(panVO);
				}
				// node 정보 조회
				else if (type.equals("SENSORNODE")) {
					SNNodeVO nodeVO = new SNNodeVO();
					nodeVO.setGID(GID);
					
					tempNodeResult = apiInterfaceService.selectNodeDescriptionByGID(nodeVO);
					
					// 하위 transducer를 조회하는 경우
					if (range.equals("FF"))
						tempTransducerResult = apiInterfaceService.selectTransducerDescriptionBySnGID(nodeVO);
				}
				else if (type.equals("TRANSDUCER")) {
					SNTransducerVO transducerVO = new SNTransducerVO();
					transducerVO.setGID(GID);
					
					tempNodeResult = apiInterfaceService.selectTransducerDescriptionByGID(transducerVO);
				}
				else {
					throw new InvalidParameterException("unsupported 'targetType'");
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
			
			if (resourceDescriptionList.size() == 0) {
				throw new NoResourceException();
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("resourceDescriptionList", resourceDescriptionList);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/resourceDescriptionByID", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/resourceStatusByID
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/resourceStatusByID", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationResourceStatusByID(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			ArrayList<String> targetList = (ArrayList<String>)filter.get("targetList");
			ArrayList<String> typeList   = (ArrayList<String>)filter.get("targetTypeList");
			ArrayList<String> rangeList 	= (ArrayList<String>)filter.get("rangeList");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (targetList.isEmpty())	throw new InvalidParameterException("parameter 'targetList' has no value");
			if (typeList.isEmpty())		throw new InvalidParameterException("parameter 'targetTypeList' has no value");
			if (rangeList.isEmpty())	throw new InvalidParameterException("parameter 'rangeList' has no value");
			
			if (targetList.size() != typeList.size()) 	throw new InvalidParameterException("'targetList' and 'targetTypeList' must have same size");
			if (targetList.size() != rangeList.size()) 	throw new InvalidParameterException("'targetTypeList' and 'rangeList' must have same size");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String GID  	= "";
			String type 	= "";
			String range	= "";
			String panRange = "";
			String snRange 	= "";
			String tdRange 	= "";
			
			List<?>	tempGatewayResult 		= null;
			List<?> tempPanResult 			= null;
			List<?> tempNodeResult 			= null;
			List<?> tempTransducerResult 	= null;
			
			List<EgovMap> resourceStatusResult = new ArrayList<EgovMap>();
			
			for (int i=0; i<targetList.size(); i++) {
				GID  	= targetList.get(i);
				type 	= typeList.get(i);
				range 	= rangeList.get(i);
				
				/******************************/
				/* message validation : start */
				/******************************/
				
				if (GID.equals("")) 	throw new InvalidParameterException("parameter 'targetList' has no value");
				if (type.equals("")) 	throw new InvalidParameterException("parameter 'targetTypeList' has no value");
				//if (range.equals("")) 	throw new InvalidParameterException("parameter 'rangeList' has no value");
				
				if (!type.equals("GATENODE") && !type.equals("PAN") && !type.equals("SENSORNODE") && !type.equals("TRANSDUCER"))
	            	throw new InvalidParameterException("unsupported 'targetList'");
				
				/****************************/
				/* message validation : end */
				/****************************/
				
				// gateway 상태 조회
				if (type.equals("GATENODE")) {
					panRange = range.split(":",3)[0];
			        snRange  = range.split(":",3)[1];
			        tdRange  = range.split(":",3)[2];

			        SNGatewayVO gatewayVO = new SNGatewayVO();
			        gatewayVO.setGID(GID);
			        
			        tempGatewayResult = apiInterfaceService.selectGatewayStatusByGID(gatewayVO);
			        
			        // 하위 pan을 조회하는 경우
			        if (panRange.equals("FF"))
			            tempPanResult = apiInterfaceService.selectPanStatusByGwGID(gatewayVO);
			        
			        // 하위 node를 조회하는 경우
			        if (snRange.equals("FF"))
			            tempNodeResult = apiInterfaceService.selectNodeStatusByGwGID(gatewayVO);
			        
			        // 하위 transducer를 조회하는 경우
			        if (tdRange.equals("FF"))
			            tempTransducerResult = apiInterfaceService.selectTransducerStatusByGwGID(gatewayVO);
				}
				// pan 정보 조회
			    else if (type.equals("PAN")) {
			        snRange  = range.split(":",2)[0];
			        tdRange  = range.split(":",2)[1];
			        
			        SNPanVO panVO = new SNPanVO();
			        panVO.setGID(GID);
			        
			        tempPanResult = apiInterfaceService.selectPanStatusByGID(panVO);
			        
			        // 하위 node를 조회하는 경우
			        if (snRange.equals("FF"))
			            tempNodeResult = apiInterfaceService.selectNodeStatusByPanGID(panVO);
			        
			        // 하위 transducer를 조회하는 경우
			        if (tdRange.equals("FF"))
			            tempTransducerResult = apiInterfaceService.selectTransducerStatusByPanGID(panVO);
			    }
			    // node 정보 조회
			    else if (type.equals("SENSORNODE")) {
			        SNNodeVO nodeVO = new SNNodeVO();
			        nodeVO.setGID(GID);
			        
			        tempNodeResult = apiInterfaceService.selectNodeStatusByGID(nodeVO);
			        
			        // 하위 transducer를 조회하는 경우
			        if (range.equals("FF"))
			            tempTransducerResult = apiInterfaceService.selectTransducerStatusBySnGID(nodeVO);
			    }
			    else if (type.equals("TRANSDUCER")) {
			        SNTransducerVO transducerVO = new SNTransducerVO();
			        transducerVO.setGID(GID);
			        
			        tempNodeResult = apiInterfaceService.selectTransducerStatusByGID(transducerVO);
			    }
			    else {
			        throw new InvalidParameterException("unsupported 'targetList'");
			    }
				
			    if (tempGatewayResult != null) {
			    	resourceStatusResult.addAll((List<EgovMap>)tempGatewayResult);
			        tempGatewayResult = null;
			    }
			    if (tempPanResult != null) {
			    	resourceStatusResult.addAll((List<EgovMap>)tempPanResult);
			        tempPanResult = null;
			    }
			    if (tempNodeResult != null) {
			    	resourceStatusResult.addAll((List<EgovMap>)tempNodeResult);
			        tempNodeResult = null;
			    }
			    if (tempTransducerResult != null) {
			    	resourceStatusResult.addAll((List<EgovMap>)tempTransducerResult);
			        tempTransducerResult = null;
			    }
			}

			if (resourceStatusResult.size() == 0) {
				throw new NoResourceException();
			}
			
			EgovMap queryMap = new EgovMap();
			List<Map<String, Object>> statusList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<resourceStatusResult.size(); i++) {
				queryMap = (EgovMap)resourceStatusResult.get(i);
				
				Map<String, Object> statusMap = new LinkedHashMap<String, Object>();
				statusMap.put("targetAddress", queryMap.get("gid"));
				statusMap.put("statusCode",    queryMap.get("statuscode"));
				
				statusList.add(statusMap);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("statusList",  	statusList);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/resourceStatusByID", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/connectivity
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/connectivity", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationConnectivity(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String category = (String)filter.get("category");
			
			Map<String, Object> temporalConditionMap = (LinkedHashMap<String, Object>)filter.get("temporalCondition");
			
			List<String> resourceList		= (ArrayList<String>)filter.get("resourceList");
			List<String> resourceTypeList	= (ArrayList<String>)filter.get("resourceTypeList");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (category.equals("")) 			throw new InvalidParameterException("parameter 'category' has no value");
			if (temporalConditionMap != null && temporalConditionMap.isEmpty())
				throw new InvalidParameterException("parameter 'temporalCondition' has no value");
			if (resourceList.isEmpty()) 		throw new InvalidParameterException("parameter 'resourceList' has no value");
			if (resourceTypeList.isEmpty())		throw new InvalidParameterException("parameter 'resourceTypeList' has no value");
			
            if (!category.equals("GEOOBJECT") && !category.equals("NETWORKDEVICE"))
            	throw new InvalidParameterException("unsupported 'category'");
            
            if (resourceList.size() != resourceTypeList.size())
            	throw new InvalidParameterException("'resourceList' and 'resourceTypeList' must have same size");
            
			/****************************/
			/* message validation : end */
			/****************************/

            String temporalCondition = "";
            
            if (temporalConditionMap != null) {
				long startTime = UsdmUtils.convertStrToDate((String)temporalConditionMap.get("startTime"), "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
				long endTime   = UsdmUtils.convertStrToDate((String)temporalConditionMap.get("endTime"),   "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
				
				temporalCondition = " AND a.startTime <= " + endTime
						       	  + " AND (0 = a.endTime OR a.endTime >=" +  startTime + ")";
            }
            else {
            	// temporal 조건이 없으면(필드가 없거나 null이면) 최신 이력만을 조회한다.
            	temporalCondition = " AND a.endTime = 0";
            }
			
			String resourceID   = "";
        	String resourceType = "";
        	
        	SNGeoRelationVO geoRelVO = new SNGeoRelationVO();
        	
        	List<Map<String, Object>> connectivityList = new ArrayList<Map<String, Object>>();
        	
        	for (int i=0; i<resourceList.size(); i++) {
            	resourceID   = resourceList.get(i);
            	resourceType = resourceTypeList.get(i);
            	
	            // 검색대상이 geo-object인 경우
	            if (category.equals("GEOOBJECT")) {
	            	/******************************/
	    			/* message validation : start */
	    			/******************************/
	    			
	                if (!resourceType.equals("water")
	                	&& !resourceType.equals("sewer")
	                	&& !resourceType.equals("subway")
	                	&& !resourceType.equals("subway_s")
	                	&& !resourceType.equals("geology")
	                	&& !resourceType.equals("groundwater")
	                	&& !resourceType.equals("w_manhole")
	                	&& !resourceType.equals("s_manhole"))
	                	throw new InvalidParameterException("unsupported 'resourceType'");
	                
	    			/****************************/
	    			/* message validation : end */
	    			/****************************/
	            	
	                geoRelVO.setGID("");
	                geoRelVO.setTemporalCondition(temporalCondition);
	                geoRelVO.setFtrIdn(resourceID);
	                //geoRelVO.setGeoTable(UsdmUtils.getGeoTableName(resourceType));
	            }
	            // 검색대상이 네트워크 자원인 경우
	            else if (category.equals("NETWORKDEVICE")) {
	            	
	            	/******************************/
	    			/* message validation : start */
	    			/******************************/
	    			
	                if (!resourceType.equals("GATENODE")
	                	&& !resourceType.equals("PAN")
	                	&& !resourceType.equals("SENSORNODE")
	                	&& !resourceType.equals("TRANSDUCER"))
	                	throw new InvalidParameterException("unsupported 'resourceType'");
	                
	    			/****************************/
	    			/* message validation : end */
	    			/****************************/
	                
	                geoRelVO.setGID(resourceID);
	                geoRelVO.setTemporalCondition(temporalCondition);
	                geoRelVO.setFtrIdn("");
	                //geoRelVO.setGeoTable(UsdmUtils.getGeoTableName(resourceType));
	            }
	            
	            List<?> queryResult = apiInterfaceService.selectConnectivity(geoRelVO);
	            
	            EgovMap queryMap = new EgovMap();

				for (int j=0; j<queryResult.size(); j++) {
					queryMap = (EgovMap)queryResult.get(j);
					
					double eTime = (double)queryMap.get("endtime");
					String eTimeStr;

					// endTime이 0인 경우(현재 연결되어 있는 경우) endTime은 ""으로 전달
					if (eTime == 0)
						eTimeStr = "";
					else
						eTimeStr = UsdmUtils.convertDateToStr(eTime, "yyyy-MM-dd HH:mm:ss.SSS");
					
					Map<String, Object> connectivityMap = new LinkedHashMap<String, Object>();
					connectivityMap.put("geoID",		queryMap.get("geoid").toString());
					// NETWORKDEVICE인 경우 type을 얻어올 수 없음
					if (category.equals("GEOOBJECT")) connectivityMap.put("type", resourceType);
					connectivityMap.put("snID", 		queryMap.get("snid").toString());
					connectivityMap.put("snAddress",	queryMap.get("snaddress"));
					connectivityMap.put("startTime",  	UsdmUtils.convertDateToStr((double)queryMap.get("starttime"), "yyyy-MM-dd HH:mm:ss.SSS"));
					connectivityMap.put("endTime",  	eTimeStr);
					
					connectivityList.add(connectivityMap);
				}
			}
			
            if (connectivityList.size() == 0) throw new NoResourceException();
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("mapList",  	connectivityList);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/connectivity", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/connectivityUpdate
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/connectivityUpdate", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationConnectivityUpdate(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		int result = 0;
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String category = (String)filter.get("category");
			String reason 	= (String)filter.get("reason");
			
			ArrayList<Map<String, Object>> mapList  = (ArrayList<Map<String, Object>>)filter.get("mapList");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (category.equals("")) 	throw new InvalidParameterException("parameter 'category' has no value");
			if (reason.equals("")) 		throw new InvalidParameterException("parameter 'reason' has no value");
			if (mapList.isEmpty()) 		throw new InvalidParameterException("parameter 'mapList' has no value");
			
            if (!category.equals("GEOOBJECT") && !category.equals("NETWORKDEVICE"))
            	throw new InvalidParameterException("unsupported 'category'");
            
            if (!reason.equals("1") && !reason.equals("2"))
            	throw new InvalidParameterException("unsupported 'reason'");
        		
			/****************************/
			/* message validation : end */
			/****************************/

			Map<String, Object> map = new LinkedHashMap<String, Object>();
			
			String 	geoID 		= "";
			String 	type  		= "";
    		String 	snGID		= "";
    		String 	snAddress	= "";
    		String 	gwID		= "";
            String 	panID		= "";
            String 	snID 		= "";
			
			for (int i=0; i<mapList.size(); i++) {
            	map = (LinkedHashMap<String, Object>)mapList.get(i);
            	
            	geoID 		= (String)map.get("geoID");
            	type 		= (String)map.get("type");
            	snGID 		= (String)map.get("snID");
            	snAddress	= (String)map.get("snAddress");
            	
            	/******************************/
    			/* message validation : start */
    			/******************************/
    			
    			if (geoID.equals("")) 		throw new InvalidParameterException("parameter 'geoID' has no value");
    			if (type.equals("")) 		throw new InvalidParameterException("parameter 'type' has no value");
    			if (snGID.equals("")) 		throw new InvalidParameterException("parameter 'snID' has no value");
    			if (snAddress.equals("")) 	throw new InvalidParameterException("parameter 'snAddress' has no value");
            		
    			/****************************/
    			/* message validation : end */
    			/****************************/
            	
            	gwID	= UsdmUtils.getGwIDFromAddress(snAddress);
                panID	= UsdmUtils.getPanIDFromAddress(snAddress);
                snID 	= UsdmUtils.getSnIDFromAddress(snAddress);
                
                SNGeoRelationVO geoRelVO = new SNGeoRelationVO();
                
                geoRelVO.setGID(snGID);
            	geoRelVO.setCurrentTime(System.currentTimeMillis());
            	geoRelVO.setEndTime(UsdmUtils.maxTime);
            	geoRelVO.setFtrIdn(geoID);
            	geoRelVO.setGwID(gwID);
            	geoRelVO.setPanID(panID);
            	geoRelVO.setSnID(snID);
            	geoRelVO.setReason(reason);
            	geoRelVO.setGeoTable(UsdmUtils.getGeoTableName(type));
            	
	            // 검색대상이 geo-object인 경우
	            if (category.equals("GEOOBJECT")) {
                	result = apiInterfaceService.insertNodeGeoRelationByGeoID(geoRelVO);
	            }
	            // 검색대상이 네트워크 자원인 경우
	            else if (category.equals("NETWORKDEVICE")) {
                	result = apiInterfaceService.insertNodeGeoRelationBySnGID(geoRelVO);
	            }
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/connectivityUpdate", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/updateDesc
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/updateDesc", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationUpdateDesc(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		int result = 0;
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String sourceAddress 	= (String)filter.get("sourceAddress");
			String sourceType		= (String)filter.get("sourceType");
			String newAddress 		= (String)filter.get("newAddress");
			String nickName			= (String)filter.get("nickName");
			
			LinkedHashMap<String, Object> coordinate = new LinkedHashMap<String, Object>();
			String 	longitude = "";
			String 	latitude  = "";
    		String 	altitude  = "";
    		
    		double tmX = 0;
    		double tmY = 0;
    		
    		String gwID		= UsdmUtils.getGwIDFromAddress(sourceAddress);
            String panID	= UsdmUtils.getPanIDFromAddress(sourceAddress);
            String snID 	= UsdmUtils.getSnIDFromAddress(sourceAddress);
            String tdID 	= UsdmUtils.getTdIDFromAddress(sourceAddress);
            
            String newGwID 	= UsdmUtils.getGwIDFromAddress(newAddress);
            String newPanID = UsdmUtils.getPanIDFromAddress(newAddress);
            String newSnID 	= UsdmUtils.getSnIDFromAddress(newAddress);
            String newTdID 	= UsdmUtils.getTdIDFromAddress(newAddress);
			
			/******************************/
			/* message validation : start */
			/******************************/
			
            if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (sourceAddress.equals("")) 	throw new InvalidParameterException("parameter 'sourceAddress' has no value");
			if (sourceType.equals("")) 		throw new InvalidParameterException("parameter 'sourceType' has no value");
			if (newAddress.equals("")) 		throw new InvalidParameterException("parameter 'newAddress' has no value");
			if (nickName.equals("")) 		throw new InvalidParameterException("parameter 'nickName' has no value");
			
			if (sourceAddress.equals(newAddress)) throw new InvalidParameterException("'newAddress' need to be different from 'sourceAddress'");
			
			if (gwID.equals("") 	|| newGwID.equals(""))		throw new InvalidParameterException("either of 'sourceGwID' or 'newGwID' required");
            if (panID.equals("") 	&& !newPanID.equals(""))	throw new InvalidParameterException("parameter 'panID' has no value");
            if (snID.equals("") 	&& !newSnID.equals(""))		throw new InvalidParameterException("parameter 'snID' has no value");
            if (tdID.equals("") 	&& !newTdID.equals(""))		throw new InvalidParameterException("parameter 'tdID' has no value");
            if (newPanID.equals("") && !panID.equals(""))		throw new InvalidParameterException("parameter 'newPanID' has no value");
            if (newSnID.equals("")  && !snID.equals(""))		throw new InvalidParameterException("parameter 'newSnID' has no value");
            if (newTdID.equals("")  && !tdID.equals(""))		throw new InvalidParameterException("parameter 'newTdID' has no value");
			
            if (!sourceType.equals("GATENODE") && !sourceType.equals("PAN") && !sourceType.equals("SENSORNODE") && !sourceType.equals("TRANSDUCER"))
            	throw new InvalidParameterException("unsupported 'sourceType'");
            
            if (sourceType.equals("GATENODE") || sourceType.equals("SENSORNODE")) {
            	try {
	            	coordinate	= (LinkedHashMap<String, Object>)filter.get("coordinate");
	    			longitude	= (String)coordinate.get("longitude");
	    			latitude	= (String)coordinate.get("latitude");
	        		altitude	= (String)coordinate.get("altitude");
            	
            	} catch (Exception e) {
            		throw new InvalidParameterException("wrong coordinate format");
            	}
        		
				if (coordinate.isEmpty())	throw new InvalidParameterException("parameter 'coordinate' has no value");
				if (longitude.equals(""))	throw new InvalidParameterException("parameter 'longitude' has no value");
				if (latitude.equals("")) 	throw new InvalidParameterException("parameter 'latitude' has no value");
				if (altitude.equals("")) 	throw new InvalidParameterException("parameter 'altitude' has no value");
				
				tmX = UsdmUtils.WGSLongitudeToEast(Double.parseDouble(latitude), Double.parseDouble(longitude));
	            tmY = UsdmUtils.WGSLatitudeToNorth(Double.parseDouble(latitude), Double.parseDouble(longitude));
            }
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			if (sourceType.equals("GATENODE")) {
				SNGatewayVO gatewayVO = new SNGatewayVO();
            	
            	gatewayVO.setGwID(gwID);
            	gatewayVO.setNewGwID(newGwID);
            	gatewayVO.setNickName(nickName);
            	gatewayVO.setLongitude(longitude);
            	gatewayVO.setLatitude(latitude);
            	gatewayVO.setAltitude(altitude);
            	gatewayVO.setX(Double.toString(tmX));
            	gatewayVO.setY(Double.toString(tmY));
				
            	result = apiInterfaceService.updateGwDescription(gatewayVO);
			}
			else if (sourceType.equals("PAN")) {
				SNPanVO panVO = new SNPanVO();
            	
				panVO.setGwID(gwID);
				panVO.setPanID(panID);
				panVO.setNewGwID(newGwID);
				panVO.setNewPanID(newPanID);
				panVO.setNickName(nickName);
            	
            	result = apiInterfaceService.updatePanDescription(panVO);
			}
			else if (sourceType.equals("SENSORNODE")) {
				SNNodeVO nodeVO = new SNNodeVO();
            	
				nodeVO.setGwID(gwID);
				nodeVO.setPanID(panID);
				nodeVO.setSnID(snID);
				nodeVO.setNewGwID(newGwID);
				nodeVO.setNewPanID(newPanID);
				nodeVO.setNewSnID(newSnID);
				nodeVO.setNickName(nickName);
				nodeVO.setLongitude(longitude);
				nodeVO.setLatitude(latitude);
				nodeVO.setAltitude(altitude);
				nodeVO.setX(Double.toString(tmX));
				nodeVO.setY(Double.toString(tmY));
            	
            	result = apiInterfaceService.updateSnDescription(nodeVO);
			}
			else if (sourceType.equals("TRANSDUCER")) {
				SNTransducerVO transducerVO = new SNTransducerVO();
            	
				transducerVO.setGwID(gwID);
				transducerVO.setPanID(panID);
				transducerVO.setSnID(snID);
				transducerVO.setTdID(tdID);
				transducerVO.setNewGwID(newGwID);
				transducerVO.setNewPanID(newPanID);
				transducerVO.setNewSnID(newSnID);
				transducerVO.setNewTdID(newTdID);
				transducerVO.setNickName(nickName);
            	
            	result = apiInterfaceService.updateTdDescription(transducerVO);
			}
			else {
				throw new InvalidParameterException("unsupported 'sourceType'");
			}
			
			if (result < 0) {
				throw new NoResourceException();
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/updateDesc", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/updateDescByID
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/updateDescByID", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationUpdateDescByID(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		int result = 0;
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String GID 			= (String)filter.get("sourceID");
			String sourceType	= (String)filter.get("sourceType");
			String newAddress 	= (String)filter.get("newAddress");
			String nickName		= (String)filter.get("nickName");
			
			LinkedHashMap<String, Object> coordinate = new LinkedHashMap<String, Object>();
			String 	longitude = "";
			String 	latitude  = "";
    		String 	altitude  = "";
    		
    		double tmX = 0;
    		double tmY = 0;
    		
            String newGwID 	= UsdmUtils.getGwIDFromAddress(newAddress);
            String newPanID = UsdmUtils.getPanIDFromAddress(newAddress);
            String newSnID 	= UsdmUtils.getSnIDFromAddress(newAddress);
            String newTdID 	= UsdmUtils.getTdIDFromAddress(newAddress);
            
            // 노드에 연결하는 geo-object는 상수도맨홀로 고정(추후 메세지입력 추가하여 변경가능)
 			String geoType = "w_manhole";
			
			/******************************/
			/* message validation : start */
			/******************************/
			
 			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (GID.equals("")) 		throw new InvalidParameterException("parameter 'sourceID' has no value");
			if (sourceType.equals("")) 	throw new InvalidParameterException("parameter 'sourceType' has no value");
			if (newAddress.equals("")) 	throw new InvalidParameterException("parameter 'newAddress' has no value");
			if (nickName.equals("")) 	throw new InvalidParameterException("parameter 'nickName' has no value");
			if (geoType.equals("")) 	throw new InvalidParameterException("parameter 'geoType' has no value");
			
            if (!sourceType.equals("GATENODE") && !sourceType.equals("PAN") && !sourceType.equals("SENSORNODE") && !sourceType.equals("TRANSDUCER"))
            	throw new InvalidParameterException("unsupported 'sourceType'");
            
            if (sourceType.equals("GATENODE") || sourceType.equals("SENSORNODE")) {
            	try {
	            	coordinate	= (LinkedHashMap<String, Object>)filter.get("coordinate");
	    			longitude	= (String)coordinate.get("longitude");
	    			latitude	= (String)coordinate.get("latitude");
	        		altitude	= (String)coordinate.get("altitude");
            	
            	} catch (Exception e) {
            		throw new InvalidParameterException("wrong coordinate format");
            	}
        		
				if (coordinate.isEmpty())	throw new InvalidParameterException("parameter 'coordinate' has no value");
				if (longitude.equals(""))	throw new InvalidParameterException("parameter 'longitude' has no value");
				if (latitude.equals("")) 	throw new InvalidParameterException("parameter 'latitude' has no value");
				if (altitude.equals("")) 	throw new InvalidParameterException("parameter 'altitude' has no value");
				
				tmX = UsdmUtils.WGSLongitudeToEast(Double.parseDouble(latitude), Double.parseDouble(longitude));
	            tmY = UsdmUtils.WGSLatitudeToNorth(Double.parseDouble(latitude), Double.parseDouble(longitude));
            }
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			if (sourceType.equals("GATENODE")) {
				SNGatewayVO gatewayVO = new SNGatewayVO();
            	
            	gatewayVO.setGID(GID);
            	gatewayVO.setNewGwID(newGwID);
            	gatewayVO.setNickName(nickName);
            	gatewayVO.setLongitude(longitude);
            	gatewayVO.setLatitude(latitude);
            	gatewayVO.setAltitude(altitude);
            	gatewayVO.setX(Double.toString(tmX));
            	gatewayVO.setY(Double.toString(tmY));
				
            	result = apiInterfaceService.updateGwDescriptionByID(gatewayVO);
			}
			else if (sourceType.equals("PAN")) {
				SNPanVO panVO = new SNPanVO();
            	
				panVO.setGID(GID);
				panVO.setNewGwID(newGwID);
				panVO.setNewPanID(newPanID);
				panVO.setNickName(nickName);
            	
            	result = apiInterfaceService.updatePanDescriptionByID(panVO);
			}
			else if (sourceType.equals("SENSORNODE")) {
				SNNodeVO nodeVO = new SNNodeVO();
            	
				nodeVO.setGID(GID);
				nodeVO.setNewGwID(newGwID);
				nodeVO.setNewPanID(newPanID);
				nodeVO.setNewSnID(newSnID);
				nodeVO.setNickName(nickName);
				nodeVO.setLongitude(longitude);
				nodeVO.setLatitude(latitude);
				nodeVO.setAltitude(altitude);
				nodeVO.setX(Double.toString(tmX));
				nodeVO.setY(Double.toString(tmY));
				nodeVO.setGeoTable(UsdmUtils.getGeoTableName(geoType));
	            nodeVO.setCurrentTime(System.currentTimeMillis());
	            nodeVO.setEndTime(UsdmUtils.maxTime);
            	
            	result = apiInterfaceService.updateSnDescriptionByID(nodeVO);
			}
			else if (sourceType.equals("TRANSDUCER")) {
				SNTransducerVO transducerVO = new SNTransducerVO();
            	
				transducerVO.setGID(GID);
				transducerVO.setNewGwID(newGwID);
				transducerVO.setNewPanID(newPanID);
				transducerVO.setNewSnID(newSnID);
				transducerVO.setNewTdID(newTdID);
				transducerVO.setNickName(nickName);
            	
            	result = apiInterfaceService.updateTdDescriptionByID(transducerVO);
			}
			else {
				throw new InvalidParameterException("unsupported 'sourceType'");
			}
			
			if (result < 0) {
				throw new NoResourceException();
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/updateDescByID", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/registerAccident
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/registerAccident", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationRegisterAccident(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		int result = 0;
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String geoID 			= (String)filter.get("geoID");
			String geoType 			= (String)filter.get("geoType");
			String coordinateType 	= (String)filter.get("coordinateType");
			long   accidentTime 	= UsdmUtils.convertStrToDate((String)filter.get("accidentTime"), "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			String accidentType 	= (String)filter.get("accidentType");	// 사고유형: 파열(burst), 균열(crack), 구멍(hole), 지반침하(subsidence), 동공(cavity), 오염(pollution)
			String accidentShape 	= "";	// 사고형태: 원형(circle), 타원형(ellipse), 사각형(rectangle), 직선(line), 갈지자(zigzag)
			
			// 사고명세
			String 	diameter 		= "";	// 지름 (파열,구멍,침하,동공 / 원형)
			String 	majorAxis 		= "";	// 장축 (파열,구멍,침하,동공 / 타원형)
			String 	minorAxis 		= "";	// 단축 (파열,구멍,침하,동공 / 타원형)
			String 	width 			= "";	// 가로 (파열,구멍,침하,동공 / 사각형)
			String 	height 			= "";	// 세로 (파열,구멍,침하,동공 / 사각형)
			String 	depth 			= "";	// 지표깊이 (침하,동공,오염)
			String 	length 			= "";	// 길이 (균열,동공)
			String	degree			= "";	// 각도 (균열,동공)
			String 	direction 		= "";	// 방향 (동공)
			String 	place			= "";	// 위치 (파열,균열,구멍)
			
			LinkedHashMap<String, Object> coordinate = (LinkedHashMap<String, Object>)filter.get("coordinate");
			String 	longitude = (String)coordinate.get("longitude");
			String 	latitude  = (String)coordinate.get("latitude");
			
			LinkedHashMap<String, Object> accidentDesc = (LinkedHashMap<String, Object>)filter.get("accidentDesc");
			String accidentDescStr = "";
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (geoID.equals(""))			throw new InvalidParameterException("parameter 'geoID' has no value");
			if (geoType.equals(""))			throw new InvalidParameterException("parameter 'geoType' has no value");
			if (coordinateType.equals(""))	throw new InvalidParameterException("parameter 'coordinateType' has no value");
			if (accidentType.equals(""))	throw new InvalidParameterException("parameter 'accidentType' has no value");
			
			if (coordinate.isEmpty())	throw new InvalidParameterException("parameter 'coordinate' has no value");
			if (accidentDesc.isEmpty())	throw new InvalidParameterException("parameter 'accidentDesc' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			// 사고유형에 따른 형태 parsing
			// 사고유형이 '오염'인 경우 사고형태가 존재하지 않음
			if (!accidentType.equals("pollution"))
				accidentShape = (String)filter.get("accidentShape");
			
			// 사고유형 및 형태에 따른 명세 parsing
			switch(accidentType) {
			// 유형: 파열,구멍
			case "burst":
			case "hole":
				// 형태: 원형
				if (accidentShape.equals("circle")) {
					// 지름
					diameter = (String)accidentDesc.get("diameter");
					accidentDescStr += "diameter:" + diameter + ",";
				}
				// 형태: 타원형
				else if (accidentShape.equals("ellipse")) {
					// 장축,단축
					majorAxis = (String)accidentDesc.get("majorAxis");
					minorAxis = (String)accidentDesc.get("minorAxis");
					accidentDescStr += "majorAxis:" + majorAxis + ",minorAxis:" + minorAxis + ",";
				}
				// 형태: 사각형
				else if (accidentShape.equals("rectangle")) {
					// 가로,세로
					width  = (String)accidentDesc.get("width");
					height = (String)accidentDesc.get("height");
					accidentDescStr += "width:" + width + ",height:" + height + ",";
				}
				else
					throw new InvalidParameterException("unsupported accident shape");
				
				// 위치
				place = (String)accidentDesc.get("place");
				accidentDescStr += "place:" + place;
				
				break;
				
			// 유형: 균열
			case "crack":
				// 길이
				length = (String)accidentDesc.get("length");
				// 각도
				degree = (String)accidentDesc.get("degree");
				// 위치
				place = (String)accidentDesc.get("place");
				
				accidentDescStr += "length:" + length + ",degree:" + degree + ",place:" + place;
				
				break;
			
			// 유형: 침하
			case "subsidence":
				// 형태: 원형
				if (accidentShape.equals("circle")) {
					// 지름
					diameter = (String)accidentDesc.get("diameter");
					accidentDescStr += "diameter:" + diameter + ",";
				}
				// 형태: 타원형
				else if (accidentShape.equals("ellipse")) {
					// 장축,단축
					majorAxis = (String)accidentDesc.get("majorAxis");
					minorAxis = (String)accidentDesc.get("minorAxis");
					accidentDescStr += "majorAxis:" + majorAxis + ",minorAxis:" + minorAxis + ",";
				}
				// 형태: 사각형
				else if (accidentShape.equals("rectangle")) {
					// 가로,세로
					width  = (String)accidentDesc.get("width");
					height = (String)accidentDesc.get("height");
					accidentDescStr += "width:" + width + ",height:" + height + ",";
				}
				else
					throw new InvalidParameterException("unsupported accident shape");
				
				// 지표깊이
				depth = (String)accidentDesc.get("depth");
				accidentDescStr += "depth:" + depth;
				
				break;
			
			// 유형: 동공
			case "cavity":
				// 형태: 원형
				if (accidentShape.equals("circle")) {
					// 지름
					diameter = (String)accidentDesc.get("diameter");
					accidentDescStr += "diameter:" + diameter + ",";
				}
				// 형태: 타원형
				else if (accidentShape.equals("ellipse")) {
					// 장축,단축
					majorAxis = (String)accidentDesc.get("majorAxis");
					minorAxis = (String)accidentDesc.get("minorAxis");
					accidentDescStr += "majorAxis:" + majorAxis + ",minorAxis:" + minorAxis + ",";
				}
				// 형태: 사각형
				else if (accidentShape.equals("rectangle")) {
					// 가로,세로
					width  = (String)accidentDesc.get("width");
					height = (String)accidentDesc.get("height");
					accidentDescStr += "width:" + width + ",height:" + height + ",";
				}
				else
					throw new InvalidParameterException("unsupported accident shape");
				
				// 방향
				direction = (String)accidentDesc.get("direction");
				// 지표깊이
				depth = (String)accidentDesc.get("depth");
				// 길이
				length = (String)accidentDesc.get("length");
				// 각도
				degree = (String)accidentDesc.get("degree");
				
				accidentDescStr += "direction:" + direction + ",length:" + length + ",degree:" + degree + ",depth:" + depth;
				
				break;
				
			// 유형: 오염
			case "pollution":
				// 지표깊이
				depth = (String)accidentDesc.get("depth");
				accidentDescStr += "depth:" + depth;
				
				break;
				
			default:
				throw new InvalidParameterException("unsupported accident type");
			}
			
			double tmX;
			double tmY;

			// 좌표변환(위경도->TM)
			if (coordinateType.equals("1")) {
				tmX = UsdmUtils.WGSLongitudeToEast(Double.parseDouble(latitude), Double.parseDouble(longitude));
	            tmY = UsdmUtils.WGSLatitudeToNorth(Double.parseDouble(latitude), Double.parseDouble(longitude));
			}
			// 좌표변환하지 않음
			else if (coordinateType.equals("2")) {
				tmX = Double.parseDouble(longitude);
	            tmY = Double.parseDouble(latitude);
			}
			else {
				throw new InvalidParameterException("unsupported coordinate type");
			}
            
			SNAccidentVO accidentVO = new SNAccidentVO();
			
			accidentVO.setGeoType(geoType);
			accidentVO.setGeoTable(UsdmUtils.getGeoTableName(geoType));
			accidentVO.setFtrIdn(geoID);
			accidentVO.setLongitude(longitude);
			accidentVO.setLatitude(latitude);
			accidentVO.setX(Double.toString(tmX));
			accidentVO.setY(Double.toString(tmY));
			accidentVO.setAccidentTime(accidentTime);
			accidentVO.setAccidentType(accidentType);
			accidentVO.setAccidentShape(accidentShape);
			accidentVO.setAccidentDesc(accidentDescStr);
			
			result = apiInterfaceService.insertAccident(accidentVO);
			
			// geo-object가 존재하지 않는 경우
			if (result == -1) throw new NoResourceException();
			// 동일한 사고정보가 존재하는 경우
			else if (result == -2) 	throw new InvalidParameterException("accident information already exists");
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/registerAccident", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/setThreshold
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/setThreshold", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationSetThreshold(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			List<LinkedHashMap<String, Object>> conditionList = (List<LinkedHashMap<String, Object>>) filter.get("conditionList");
					
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (conditionList==null || conditionList.isEmpty()) throw new InvalidParameterException("parameter 'conditionList' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String tdType 		= null;
			String operator1	= null;
			String operand1 	= null;
			String logicalOp 	= null;
			String operator2 	= null;
			String operand2		= null;
			
			for (int i=0; i<conditionList.size(); i++) {
				
				LinkedHashMap<String, Object> conditionMap = conditionList.get(i);
				
				tdType    = (String)conditionMap.get("tdType");
				operator1 = (String)conditionMap.get("relationOp1");
				operand1  = (String)conditionMap.get("value1");
				logicalOp = (String)conditionMap.get("logicalOp");
				operator2 = (String)conditionMap.get("relationOp2");
				operand2  = (String)conditionMap.get("value2");
				
				/******************************/
				/* message validation : start */
				/******************************/
				
				if (tdType==null    || tdType.equals("")) 		throw new InvalidParameterException("parameter 'tdType' has no value");
				if (operator1==null || operator1.equals("")) 	throw new InvalidParameterException("parameter 'relationOp1' has no value");
				if (operand1==null  || operand1.equals("")) 	throw new InvalidParameterException("parameter 'value1' has no value");
				if (logicalOp!=null && logicalOp.equals("")) 	throw new InvalidParameterException("parameter 'logicalOp' has no value");
				if (operator2!=null && operator2.equals("")) 	throw new InvalidParameterException("parameter 'relationOp2' has no value");
				if (operand2!=null  && operand2.equals("")) 	throw new InvalidParameterException("parameter 'value2' has no value");
				
				/****************************/
				/* message validation : end */
				/****************************/
				
				SNSensingValueVO sensingValueVO = new SNSensingValueVO();
				
				sensingValueVO.setSensorType(tdType);
				sensingValueVO.setOperator1(operator1);
				sensingValueVO.setOperand1(Double.parseDouble(operand1));
				
				if (logicalOp != null) sensingValueVO.setLogicalOp(logicalOp);
				if (operator2 != null) sensingValueVO.setOperator2(operator2);
				if (operand2  != null) sensingValueVO.setOperand2(Double.parseDouble(operand2));
				
				apiInterfaceService.insertThreshold(sensingValueVO);
			}

			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/setThreshold", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/mapWaterManhole
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/mapWaterManhole", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationMapWaterManhole(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			List<Map<String, Object>> mappingList = (List<Map<String, Object>>) filter.get("mappingList");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (mappingList==null || mappingList.isEmpty()) throw new InvalidParameterException("parameter 'mappingList' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			for (int i=0; i<mappingList.size(); i++) {
				Map<String, Object> mappingMap = mappingList.get(i);
				
				String manholeID = (String) mappingMap.get("manholeID");
				List<String> pipeIDList = (List<String>) mappingMap.get("pipeIDs");
				
				/******************************/
				/* message validation : start */
				/******************************/
				
				if (manholeID==null  || manholeID.equals("")) throw new InvalidParameterException("parameter 'manholeID' has no value");
				if (pipeIDList==null || pipeIDList.isEmpty()) throw new InvalidParameterException("parameter 'pipeIDs' has no value");
				
				/****************************/
				/* message validation : end */
				/****************************/
				
				for (int j=0; j<pipeIDList.size(); j++) {
					String pipeID = pipeIDList.get(j);
					
					/******************************/
					/* message validation : start */
					/******************************/
					
					if (pipeID==null || pipeID.equals("")) throw new InvalidParameterException("parameter 'pipeID' has no value");
					
					/****************************/
					/* message validation : end */
					/****************************/

					ManholePipeRelVO manholePipeRelVO = new ManholePipeRelVO();
				
					manholePipeRelVO.setManholeFtrIdn(Integer.parseInt(manholeID));
					manholePipeRelVO.setPipeFtrIdn(Integer.parseInt(pipeID));
					
					apiInterfaceService.insertWaterManholePipeRel(manholePipeRelVO);
				}
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/mapWaterManhole", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/setLeakThreshold
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/setLeakThreshold", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationSetLeakThreshold(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			List<LinkedHashMap<String, Object>> conditionList = (List<LinkedHashMap<String, Object>>) filter.get("conditionList");
					
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (conditionList==null || conditionList.isEmpty()) throw new InvalidParameterException("parameter 'conditionList' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String snID 		= null;
			String tdType 		= null;
			String operator		= null;
			String operand 		= null;
			String counterLimit	= null;
			
			for (int i=0; i<conditionList.size(); i++) {
				
				LinkedHashMap<String, Object> conditionMap = conditionList.get(i);
				
				snID    		= (String)conditionMap.get("snID");
				tdType 			= (String)conditionMap.get("tdType");
				operator  		= (String)conditionMap.get("relationOp");
				operand 		= (String)conditionMap.get("value");
				counterLimit 	= (String)conditionMap.get("count");
				
				/******************************/
				/* message validation : start */
				/******************************/
				
				if (snID==null         || snID.equals("")) 			throw new InvalidParameterException("parameter 'snID' has no value");
				if (tdType==null       || tdType.equals("")) 		throw new InvalidParameterException("parameter 'tdType' has no value");
				if (operator==null     || operator.equals("")) 		throw new InvalidParameterException("parameter 'relationOp' has no value");
				if (operand==null      || operand.equals("")) 		throw new InvalidParameterException("parameter 'value' has no value");
				if (counterLimit==null || counterLimit.equals("")) 	throw new InvalidParameterException("parameter 'count' has no value");
				
				/****************************/
				/* message validation : end */
				/****************************/
				
				SNSensingValueVO sensingValueVO = new SNSensingValueVO();
				
				sensingValueVO.setGID(snID);
				sensingValueVO.setSensorType(tdType);
				sensingValueVO.setOperator1(operator);
				sensingValueVO.setOperand1(Double.parseDouble(operand));
				sensingValueVO.setCounter(Integer.parseInt(counterLimit));
				
				apiInterfaceService.insertLeakThreshold(sensingValueVO);
			}

			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/setLeakThreshold", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/repairInfra
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/repairInfra", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationRepairInfra(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");

			int category = UsdmUtils.getIntegerValue(filter, "category");
			
			String geoType 	= UsdmUtils.getStringValue(filter, "geoType");
			String date 	= UsdmUtils.getStringValue(filter, "date");
			String contents	= UsdmUtils.getStringValue(filter, "contents");
			String geoID 	= UsdmUtils.getStringValue(filter, "geoID");
			
			List<Integer> accidentList = (List<Integer>)filter.get("accidentList");
			
			Map<String, Object> locPartRepair = (Map<String, Object>)filter.get("locPartRepair");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (!(geoType.equals("water") || geoType.equals("sewer") || geoType.equals("subway") || geoType.equals("subway_s") || geoType.equals("geology")))
				throw new InvalidParameterException("parameter 'geoType' has invalid value");
			
			if (!(category==1 || category==2 || category==3 || category==9))
				throw new InvalidParameterException("parameter 'category' has invalid value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			InfraRepairVO repairVO = new InfraRepairVO();
			
			repairVO.setGeoType(geoType);
			repairVO.setFtrIdn(Integer.parseInt(geoID));
			repairVO.setRepairDate(UsdmUtils.convertStrToDate(date, "yyyy-MM-dd"));
			repairVO.setCategory(category);
			repairVO.setContents(contents);
			repairVO.setGeoTable(UsdmUtils.getGeoTableName(geoType));
			repairVO.setSriTable(UsdmUtils.getSRITableName(geoType));
			
			if (accidentList != null)
				repairVO.setAccidentIDList(UsdmUtils.getInOperatorString(accidentList));
			
			if (locPartRepair != null) {
				double longitude = Double.parseDouble(locPartRepair.get("longitude").toString());
				double latitude  = Double.parseDouble(locPartRepair.get("latitude").toString());
				
				String wkt = "ST_POINTFROMTEXT('POINT(" + latitude + " " + longitude + ")',0)";
				
				repairVO.setLongitude(longitude);
				repairVO.setLatitude(latitude);
				repairVO.setWkt(wkt);
			}
			
			// 매설물 복구정보 생성
			String cellIDList = apiInterfaceService.insertInfraRepair(repairVO);
			
			if (cellIDList.equals("")) throw new NoResourceException(); 
				
			// 변경된 매설물 SRI 조회
			EgovMap geoChangeResult = apiInterfaceService.selectInfraRepairGeoChange(repairVO);

			Map<String, Object> geoResultSet = new LinkedHashMap<String, Object>();
			geoResultSet.put("geoID",    geoChangeResult.get("ftrIdn").toString());
			geoResultSet.put("sriLevel", geoChangeResult.get("level").toString());
			
			List<Map<String, Object>> geoResultSetList  = new ArrayList<Map<String, Object>>();
			geoResultSetList.add(geoResultSet);
			
			// 변경된 그리드 조회
			repairVO.setCellIDList(cellIDList);
			List<?> gridChangedResult = apiInterfaceService.selectInfraRepairGridChanged(repairVO);
			
			List<Map<String, Object>> resultSetList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<gridChangedResult.size(); i++) {
				EgovMap queryMap = (EgovMap)gridChangedResult.get(i);
				
				Map<String, Object> resultSet = new LinkedHashMap<String, Object>();
				resultSet.put("gridIndex",	queryMap.get("cellid").toString());
				resultSet.put("rSRI",	 	queryMap.get("cellsri").toString());
				resultSet.put("wSRI",	 	queryMap.get("watersri").toString());
				resultSet.put("sSRI",	 	queryMap.get("drainsri").toString());
				resultSet.put("mSRI",	 	queryMap.get("subwaysri").toString());
				resultSet.put("tSRI",	 	queryMap.get("stationsri").toString());
				resultSet.put("gSRI",	 	queryMap.get("geologysri").toString());
				
				resultSetList.add(resultSet);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("geoChanged",   geoResultSetList);
			responseMessage.put("gridChanged",  resultSetList);
			
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/repairInfra", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/repairSubsidence
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/repairSubsidence", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationRepairSubsidence(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			int target = UsdmUtils.getIntegerValue(filter, "target");
			
			String date 	= UsdmUtils.getStringValue(filter, "date");
			String contents	= UsdmUtils.getStringValue(filter, "contents");
			String place	= UsdmUtils.getStringValue(filter, "place");
			
			Map<String, Object> locRepair = (Map<String, Object>)filter.get("locRepair");
			
			List<String> sewerPipeList = (List<String>)filter.get("sPipeList");
			List<String> stationList   = (List<String>)filter.get("mStationList");
			List<String> subwayList    = (List<String>)filter.get("tSubwayList");
			
			double sewerDistance   = UsdmUtils.getDoubleValue(filter, "sDistance", false);
			double stationDistance = UsdmUtils.getDoubleValue(filter, "mDistance", false);
			double subwayDistance  = UsdmUtils.getDoubleValue(filter, "tDistance", false);
			
			List<Integer> accidentList = (List<Integer>)filter.get("accidentList");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (!(target==11 || target==12 || target==13))
				throw new InvalidParameterException("parameter 'target' has invalid value");
			
			if (locRepair == null) throw new InvalidParameterException("parameter 'locRepair' has no value");
			
			if (sewerPipeList != null && sewerDistance == UsdmUtils.NULL_DOUBLE)
				throw new InvalidParameterException("parameter 'sDistance' has no value");
			
			if (stationList != null && stationDistance == UsdmUtils.NULL_DOUBLE)
				throw new InvalidParameterException("parameter 'mDistance' has no value");
			
			if (subwayList != null && subwayDistance == UsdmUtils.NULL_DOUBLE)
				throw new InvalidParameterException("parameter 'tDistance' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			double longitude = Double.parseDouble(locRepair.get("longitude").toString());
			double latitude  = Double.parseDouble(locRepair.get("latitude").toString());
				
			String wkt = "ST_POINTFROMTEXT('POINT(" + latitude + " " + longitude + ")',0)";
			
			SubsidenceRepairVO repairVO = new SubsidenceRepairVO();
			
			repairVO.setTarget(target);
			repairVO.setRepairDate(UsdmUtils.convertStrToDate(date, "yyyy-MM-dd"));
			repairVO.setContents(contents);
			repairVO.setPlace(place);
			repairVO.setLongitude(longitude);
			repairVO.setLatitude(latitude);
			repairVO.setWkt(wkt);
			repairVO.setSewerPipeList(sewerPipeList);
			repairVO.setStationList(stationList);
			repairVO.setSubwayList(subwayList);
			repairVO.setSewerDistance(sewerDistance);
			repairVO.setStationDistance(stationDistance);
			repairVO.setSubwayDistance(subwayDistance);
			repairVO.setSewerPipeIDList(UsdmUtils.getInOperatorString(sewerPipeList));
			repairVO.setStationIDList(UsdmUtils.getInOperatorString(stationList));
			repairVO.setSubwayIDList(UsdmUtils.getInOperatorString(subwayList));
			
			if (accidentList != null)
				repairVO.setAccidentIDList(UsdmUtils.getInOperatorString(accidentList));
			
			// 지반침하/동공 복구정보 생성
			String cellIDList = apiInterfaceService.insertSubsidenceRepair(repairVO);
			
			if (cellIDList.equals("")) throw new NoResourceException();

			// 매설물 SRI 조회
			List<Map<String, Object>> geoResultSetList  = new ArrayList<Map<String, Object>>();
			
			// 변경된 하수관 조회
			if (sewerPipeList != null) {
				List<?> geoChangeResult = apiInterfaceService.selectSubsidenceRepairSewerChange(repairVO);
				
				for (int i=0; i<geoChangeResult.size(); i++) {
					EgovMap queryMap = (EgovMap)geoChangeResult.get(i);
					
					Map<String, Object> resultSet = new LinkedHashMap<String, Object>();
					resultSet.put("geoID",		queryMap.get("geoid").toString());
					resultSet.put("geoType",	"sewer");
					resultSet.put("sriValue",	queryMap.get("sri").toString());
					resultSet.put("sriLevel",	queryMap.get("level").toString());
					
					geoResultSetList.add(resultSet);
				}
			}
			// 변경된 역사 조회
			if (stationList != null) {
				List<?> geoChangeResult = apiInterfaceService.selectSubsidenceRepairStationChange(repairVO);
				
				for (int i=0; i<geoChangeResult.size(); i++) {
					EgovMap queryMap = (EgovMap)geoChangeResult.get(i);
					
					Map<String, Object> resultSet = new LinkedHashMap<String, Object>();
					resultSet.put("geoID",		queryMap.get("geoid").toString());
					resultSet.put("geoType",	"subway_s");
					resultSet.put("sriValue",	queryMap.get("sri").toString());
					resultSet.put("sriLevel",	queryMap.get("level").toString());
					
					geoResultSetList.add(resultSet);
				}
			}
			// 변경된 선로 조회
			if (subwayList != null) {
				List<?> geoChangeResult = apiInterfaceService.selectSubsidenceRepairSubwayChange(repairVO);
				
				for (int i=0; i<geoChangeResult.size(); i++) {
					EgovMap queryMap = (EgovMap)geoChangeResult.get(i);
					
					Map<String, Object> resultSet = new LinkedHashMap<String, Object>();
					resultSet.put("geoID",		queryMap.get("geoid").toString());
					resultSet.put("geoType",	"subway");
					resultSet.put("sriValue",	queryMap.get("sri").toString());
					resultSet.put("sriLevel",	queryMap.get("level").toString());
					
					geoResultSetList.add(resultSet);
				}
			}
			
			// 변경된 그리드 조회
			InfraRepairVO infraRepairVO = new InfraRepairVO();
			infraRepairVO.setCellIDList(cellIDList);
			List<?> gridChangedResult = apiInterfaceService.selectInfraRepairGridChanged(infraRepairVO);
			
			List<Map<String, Object>> gridResultSetList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<gridChangedResult.size(); i++) {
				EgovMap queryMap = (EgovMap)gridChangedResult.get(i);
				
				Map<String, Object> resultSet = new LinkedHashMap<String, Object>();
				resultSet.put("gridIndex",	queryMap.get("cellid").toString());
				resultSet.put("rSRI",	 	queryMap.get("cellsri").toString());
				resultSet.put("wSRI",	 	queryMap.get("watersri").toString());
				resultSet.put("sSRI",	 	queryMap.get("drainsri").toString());
				resultSet.put("mSRI",	 	queryMap.get("subwaysri").toString());
				resultSet.put("tSRI",	 	queryMap.get("stationsri").toString());
				resultSet.put("gSRI",	 	queryMap.get("geologysri").toString());
				
				gridResultSetList.add(resultSet);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("geoChanged",   geoResultSetList);
			responseMessage.put("gridChanged",  gridResultSetList);
			
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/repairSubsidence", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/geoobjectListRequest
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/geoobjectListRequest", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationGeoobjectListRequest(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String type      = (String)filter.get("objectType");
			String longitude = (String)filter.get("longitude");
			String latitude  = (String)filter.get("latitude");
			String radius    = (String)filter.get("radius");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (type.equals(""))		throw new InvalidParameterException("parameter 'objectType' has no value");
			if (longitude.equals(""))	throw new InvalidParameterException("parameter 'longitude' has no value");
			if (latitude.equals(""))	throw new InvalidParameterException("parameter 'latitude' has no value");
			if (radius.equals(""))		throw new InvalidParameterException("parameter 'radius' has no value");
			
			if (!type.equals("water")
                && !type.equals("sewer")
            	&& !type.equals("subway")
            	&& !type.equals("subway_s")
            	&& !type.equals("geology")
            	&& !type.equals("groundwater")
            	&& !type.equals("w_manhole")
            	&& !type.equals("s_manhole"))
            	throw new InvalidParameterException("unsupported 'objectType'");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String spatialCondition = "";
			
		    double tmX = UsdmUtils.WGSLongitudeToEast(Double.parseDouble(latitude), Double.parseDouble(longitude));
		    double tmY = UsdmUtils.WGSLatitudeToNorth(Double.parseDouble(latitude), Double.parseDouble(longitude));
		    double tmRadius = Double.parseDouble(radius);
		    
		    double minX = tmX - tmRadius;
		    double minY = tmY - tmRadius;
		    double maxX = tmX + tmRadius;
		    double maxY = tmY + tmRadius;
		    
		    String mbrPolygon  = minX + " " + minY + ","
		                       + maxX + " " + minY + ","
		                       + maxX + " " + maxY + ","
		                       + minX + " " + maxY + ","
		                       + minX + " " + minY;
		    
		    spatialCondition += "WHERE ST_INTERSECTS(ST_GEOMFROMTEXT('POLYGON((" + mbrPolygon + "))',0), geom) = 1";
		    spatialCondition += " AND SQRT(POWER(ABS(ST_X(geom)-" + tmX + "),2) + POWER(ABS(ST_Y(geom)-" + tmY + "),2)) <= " + tmRadius;
			
			
			SNSensingValueVO sensingValueVO = new SNSensingValueVO();
			
			sensingValueVO.setGeoObjectType(type);
			sensingValueVO.setGeoTable(UsdmUtils.getGeoTableName(type));
			sensingValueVO.setSpatialCondition(spatialCondition);

			List<?> queryResult = apiInterfaceService.selectGeoobjectList(sensingValueVO);
			
			List<Map<String, Object>> geoobjectList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				EgovMap queryMap = (EgovMap)queryResult.get(i);
				
				double resultY = (double)queryMap.get("y");
				double resultX = (double)queryMap.get("x");
				
				Map<String, Object> descriptionMap = new LinkedHashMap<String, Object>();
				
				descriptionMap.put("geoID",		Integer.toString((int)queryMap.get("geoid")));
				descriptionMap.put("latitude",  Double.toString(UsdmUtils.TMNorthToLatitude(resultY, resultX)));
				descriptionMap.put("longitude", Double.toString(UsdmUtils.TMEastToLongitude(resultY, resultX)));
				
				switch (type) {
				// objectType: 상수도맨홀
				case "w_manhole":
					descriptionMap.put("dpgStd",  (String)queryMap.get("dpgstd"));	// 규격
					descriptionMap.put("somCode", (String)queryMap.get("somcde"));	// 맨홀종류
					descriptionMap.put("mhsCode", (String)queryMap.get("mhscde"));	// 맨홀형태
					break;
				}
				
				Map<String, Object> geoobjectMap = new LinkedHashMap<String, Object>();
				
				geoobjectMap.put("resourceType", "geoobjectList");
				geoobjectMap.put("description",  descriptionMap);
				
				geoobjectList.add(geoobjectMap);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode",  SUCCESS_CODE);
			responseMessage.put("responseMsg",   SUCCESS_MSG);
			responseMessage.put("geoobjectList", geoobjectList);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/geoobjectListRequest", filterJSON, responseJson);
		
		return responseJson;
	}
		
	// information/firmwareDataStore
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/firmwareDataStore", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationFirmwareDataStore(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String targetDeviceType		= (String)filter.get("targetDeviceType");
			String manufacturerID		= (String)filter.get("firmwareManufacturerID");
			String majorVersion			= (String)filter.get("firmwareMajorVersion");
			String minorVersion			= (String)filter.get("firmwareMinorVersion");
			String description			= (String)filter.get("firmwareDescription");
			String dataSize				= (String)filter.get("firmwareDataSize");
			String binaryData			= (String)filter.get("firmwareBinaryData");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (targetDeviceType.equals(""))	throw new InvalidParameterException("parameter 'targetDeviceType' has no value");
			if (manufacturerID.equals("")) 		throw new InvalidParameterException("parameter 'firmwareManufacturerID' has no value");
			if (majorVersion.equals("")) 		throw new InvalidParameterException("parameter 'firmwareMajorVersion' has no value");
			if (minorVersion.equals("")) 		throw new InvalidParameterException("parameter 'firmwareMinorVersion' has no value");
			if (dataSize.equals("")) 			throw new InvalidParameterException("parameter 'firmwareDataSize' has no value");
			if (binaryData.equals("")) 			throw new InvalidParameterException("parameter 'firmwareBinaryData' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String firmwareID;
			
			SNFirmwareVO firmwareVO = new SNFirmwareVO();
			
			firmwareVO.setTargetDeviceType(Integer.parseInt(targetDeviceType));
			firmwareVO.setManufacturerID(Integer.parseInt(manufacturerID));
			firmwareVO.setMajorVersion(Integer.parseInt(majorVersion));
			firmwareVO.setMinorVersion(Integer.parseInt(minorVersion));
			firmwareVO.setDescription(description);
			firmwareVO.setDataSize(Integer.parseInt(dataSize));
			firmwareVO.setBinaryData(binaryData);
			
			firmwareID = apiInterfaceService.insertFirmwareData(firmwareVO);
			
			if (firmwareID.equals("")) throw new Exception();
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode",  	SUCCESS_CODE);
			responseMessage.put("responseMsg",   	SUCCESS_MSG);
			responseMessage.put("firmWareDataID",   firmwareID);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/firmwareDataStore", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/firmwareListRequest
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/firmwareListRequest", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationFirmwareListRequest(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String targetDeviceType	= (String)filter.get("targetDeviceType");
			String manufacturerID	= (String)filter.get("firmwareManufacturerID");
			String majorVersion		= (String)filter.get("firmwareMajorVersion");
			String minorVersion 	= (String)filter.get("firmwareMinorVersion");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (targetDeviceType!=null && targetDeviceType.equals(""))
				throw new InvalidParameterException("parameter 'targetDeviceType'has no value");
			if (manufacturerID!=null && manufacturerID.equals(""))
				throw new InvalidParameterException("parameter 'firmwareManufacturerID no value");
			if (majorVersion!=null && majorVersion.equals(""))
				throw new InvalidParameterException("parameter 'firmwareMajorVersion no value");
			if (minorVersion!=null && minorVersion.equals(""))
				throw new InvalidParameterException("parameter 'firmwareMinorVersion no value");
			
			//if (targetDeviceType==null && manufacturerID==null && majorVersion==null && minorVersion==null)
				//throw new InvalidParameterException("either of 'targetDeviceType','firmwareManufacturerID','firmwareMajorVersion' or 'firmwareMinorVersion' required");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			if (targetDeviceType == null) targetDeviceType = "-1";
			if (manufacturerID   == null) manufacturerID   = "-1";
			if (majorVersion     == null) majorVersion     = "-1";
			if (minorVersion     == null) minorVersion     = "-1";
			
			SNFirmwareVO firmwareVO = new SNFirmwareVO();
			
			firmwareVO.setTargetDeviceType(Integer.parseInt(targetDeviceType));
			firmwareVO.setManufacturerID(Integer.parseInt(manufacturerID));
			firmwareVO.setMajorVersion(Integer.parseInt(majorVersion));
			firmwareVO.setMinorVersion(Integer.parseInt(minorVersion));
			
			List<?> queryResult = apiInterfaceService.selectFirmwareList(firmwareVO);
			
			List<Map<String, Object>> firmwareList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				EgovMap queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> descriptionMap = new LinkedHashMap<String, Object>();
				
				descriptionMap.put("firmwareDataID",		 Integer.toString((int)queryMap.get("firmwareid")));
				descriptionMap.put("targetDeviceType",  	 Integer.toString((int)queryMap.get("targetdevicetype")));
				descriptionMap.put("firmwareManufacturerID", Integer.toString((int)queryMap.get("manufacturerid")));
				descriptionMap.put("firmwareMajorVersion",   Integer.toString((int)queryMap.get("majorversion")));
				descriptionMap.put("firmwareMinorVersion",   Integer.toString((int)queryMap.get("minorversion")));
				descriptionMap.put("firmwareDescription", 	 (String)queryMap.get("description"));

				Map<String, Object> firmwareMap = new LinkedHashMap<String, Object>();
				
				firmwareMap.put("resourceType", "firmwareVersionList");
				firmwareMap.put("description",  descriptionMap);
				
				firmwareList.add(firmwareMap);
			}
			
			if (firmwareList.isEmpty()) throw new NoResourceException();
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("firmwareVersionList", firmwareList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/firmwareListRequest", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/firmwareDataRequest
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/firmwareDataRequest", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationFirmwareDataRequest(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String firmwareID = (String)filter.get("firmwareDataID");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (firmwareID.equals(""))	throw new InvalidParameterException("parameter 'firmwareDataID' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			SNFirmwareVO firmwareVO = new SNFirmwareVO();
			firmwareVO.setFirmwareID(Integer.parseInt(firmwareID));
			
			EgovMap queryMap = (EgovMap) apiInterfaceService.selectFirmwareData(firmwareVO);
				
			if (queryMap == null) throw new NoResourceException();
			
			Map<String, Object> descriptionMap = new LinkedHashMap<String, Object>();
			
			descriptionMap.put("targetDeviceType",       Integer.toString((int)queryMap.get("targetdevicetype")));
			descriptionMap.put("firmwareManufacturerID", Integer.toString((int)queryMap.get("manufacturerid")));
			descriptionMap.put("firmwareMajorVersion",   Integer.toString((int)queryMap.get("majorversion")));
			descriptionMap.put("firmwareMinorVersion",   Integer.toString((int)queryMap.get("minorversion")));
			descriptionMap.put("firmwareDescription", 	 (String)queryMap.get("description"));
			descriptionMap.put("firmwareDataSize", 	  	 Integer.toString((int)queryMap.get("datasize")));
			descriptionMap.put("firmwareBinaryData", 	 (String)queryMap.get("binarydata"));
			
			Map<String, Object> firmwareMap = new LinkedHashMap<String, Object>();
			
			firmwareMap.put("resourceType", "firmwareData");
			firmwareMap.put("description",  descriptionMap);
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("firmwareData", firmwareMap);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());

		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/firmwareDataRequest", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/firmwareDataDelete
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/firmwareDataDelete", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationFirmwareDataDelete(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		int result = 0;
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String firmwareID = (String)filter.get("firmwareDataID");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (firmwareID.equals(""))	throw new InvalidParameterException("parameter 'firmwareDataID' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			SNFirmwareVO firmwareVO = new SNFirmwareVO();
			firmwareVO.setFirmwareID(Integer.parseInt(firmwareID));
			
			result = apiInterfaceService.deleteFirmwareData(firmwareVO);
			
			if (result < 0) throw new NoResourceException();
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/firmwareDataDelete", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/firmwareUpdateHistoryStore
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/firmwareUpdateHistoryStore", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationFirmwareUpdateHistoryStore(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		int result = 0;
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");

			String gwID = (String)filter.get("gwID");
			
			List<String> targetList = (List<String>)filter.get("targetList");
			
			String transmissionTime			= (String)filter.get("transmissionTime");
			String transmissionResult 		= (String)filter.get("transmissionResult");
			String firmwareID				= (String)filter.get("firmwareDataID");
			String updateType				= (String)filter.get("firmwareUpdateType");
			String updateTime				= (String)filter.get("firmwareUpdateTime");
			String recommended1BlockSize	= (String)filter.get("recommended1BlockSize");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (gwID.equals(""))				throw new InvalidParameterException("parameter 'gwID' has no value");			
			if (targetList == null || targetList.isEmpty()) throw new InvalidParameterException("parameter 'targetList' has no value");
			if (transmissionTime.equals(""))	throw new InvalidParameterException("parameter 'transmissionTime' has no value");
			if (transmissionResult.equals(""))	throw new InvalidParameterException("parameter 'transmissionResult' has no value");
			if (firmwareID.equals(""))			throw new InvalidParameterException("parameter 'firmwareDataID' has no value");
			if (updateType.equals(""))			throw new InvalidParameterException("parameter 'firmwareUpdateType' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			// firmwareID에 해당하는 레코드 존재여부 조회
			SNFirmwareVO firmwareVO = new SNFirmwareVO();
			firmwareVO.setFirmwareID(Integer.parseInt(firmwareID));
			
			if (!apiInterfaceService.selectExistingFirmwareData(firmwareVO))
				throw new InvalidParameterException("parameter 'firmwareDataID' is invalid");
			
			// firmware update history 생성
			for (int i=0; i<targetList.size(); i++) {
				String targetID = targetList.get(i);
				
				SNFirmwareUpdateHistoryVO firmwareUpdateHistoryVO = new SNFirmwareUpdateHistoryVO();
				
				firmwareUpdateHistoryVO.setGwID(gwID);
				firmwareUpdateHistoryVO.setTargetID(targetID);
				firmwareUpdateHistoryVO.setTransmissionTime(UsdmUtils.convertStrToDate(transmissionTime, "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
				firmwareUpdateHistoryVO.setTransmissionResult(transmissionResult);
				firmwareUpdateHistoryVO.setFirmwareID(Integer.parseInt(firmwareID));
				firmwareUpdateHistoryVO.setUpdateType(Integer.parseInt(updateType));
				firmwareUpdateHistoryVO.setUpdateTime(UsdmUtils.convertStrToDate(updateTime, "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
				firmwareUpdateHistoryVO.setRecommended1BlockSize(Integer.parseInt(recommended1BlockSize));
				
				result = apiInterfaceService.insertFirmwareUpdateHistory(firmwareUpdateHistoryVO);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/firmwareUpdateHistoryStore", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/firmwareUpdateHistoryRequest
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/firmwareUpdateHistoryRequest", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationFirmwareUpdateHistoryRequest(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String gwID = (String)filter.get("gwID");
			ArrayList<String> targetIDList	= (ArrayList<String>)filter.get("targetList");
			
			Map<String, Object> temporalConditionMap = (Map<String, Object>)filter.get("temporalCondition");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (gwID!=null && gwID.equals(""))
				throw new InvalidParameterException("parameter 'gwID' has no value");

			if (targetIDList!=null && targetIDList.isEmpty())
				throw new InvalidParameterException("parameter 'targetList' has no value");
			
			if (temporalConditionMap!=null && temporalConditionMap.isEmpty())
				throw new InvalidParameterException("parameter 'temporalCondition' has no value");
			
			//if (gwID==null && targetIDList==null && temporalConditionMap==null)
				//throw new InvalidParameterException("either of 'gwID', 'targetIDList' or 'temporalCondition' required");
			
			/****************************/
			/* message validation : end */
			/****************************/

			String gwIDCondition     = "";
			String targetIDCondition = "";
			String temporalCondition = "";
			
			// gwID 조건이 존재하는 경우
			if (gwID != null)
				gwIDCondition = " AND gwID = " + gwID;
			
			// targetID 조건이 존재하는 경우
			if (targetIDList != null)
				targetIDCondition = " AND targetID IN (" + UsdmUtils.getInOperatorString(targetIDList) + ")";
			
			// 시간조건이 존재하는 경우
			if (temporalConditionMap != null) {
				long startTime = UsdmUtils.convertStrToDate((String)temporalConditionMap.get("startTime"), "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
				long endTime   = UsdmUtils.convertStrToDate((String)temporalConditionMap.get("endTime"),   "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
				
				temporalCondition = " AND transmissionTime >= " + startTime + " AND transmissionTime <= " + endTime;
			}
						
			SNFirmwareUpdateHistoryVO firmwareUpdateHistoryVO = new SNFirmwareUpdateHistoryVO();
			
			firmwareUpdateHistoryVO.setGwIDCondition(gwIDCondition);
			firmwareUpdateHistoryVO.setTargetCondition(targetIDCondition);
			firmwareUpdateHistoryVO.setTemporalCondition(temporalCondition);
			
			List<?> queryResult = apiInterfaceService.selectFirmwareUpdateHistory(firmwareUpdateHistoryVO);
			
			if (queryResult.isEmpty()) throw new NoResourceException();
			
			List<Map<String, Object>> firmwareHistoryList = new ArrayList<Map<String, Object>>();

			for (int i=0; i<queryResult.size(); i++) {
				EgovMap queryMap = (EgovMap)queryResult.get(i);

				String gwIDResult 				= (String)queryMap.get("gwid");
				double transmissionTimeResult 	= (double)queryMap.get("transmissiontime");
				
				// targetID의 list조회
				firmwareUpdateHistoryVO.setGwID(gwIDResult);
				firmwareUpdateHistoryVO.setTransmissionTime((long)transmissionTimeResult);
				
				List<?> targetQueryResult = apiInterfaceService.selectFirmwareUpdateHistoryTargetList(firmwareUpdateHistoryVO);
				
				List<String> targetIDResult = new ArrayList<String>();
				
				for (int j=0; j<targetQueryResult.size(); j++) {
					EgovMap targetQueryMap = (EgovMap)targetQueryResult.get(j);

					targetIDResult.add((String)targetQueryMap.get("targetid"));
				}
				
				Map<String, Object> descriptionMap = new LinkedHashMap<String, Object>();
				
				descriptionMap.put("gwID",	 					gwIDResult);
				descriptionMap.put("targetList", 				targetIDResult);
				descriptionMap.put("transmissionTime",		 	UsdmUtils.convertDateToStr(transmissionTimeResult, "yyyy-MM-dd HH:mm:ss.SSS"));
				descriptionMap.put("transmissionResult",		(String)queryMap.get("transmissionresult"));
				descriptionMap.put("firmwareDataID",		 	Integer.toString((int)queryMap.get("firmwareid")));
				descriptionMap.put("targetDeviceType",  	 	Integer.toString((int)queryMap.get("targetdevicetype")));
				descriptionMap.put("firmwareManufacturerID", 	Integer.toString((int)queryMap.get("manufacturerid")));
				descriptionMap.put("firmwareMajorVersion",   	Integer.toString((int)queryMap.get("majorversion")));
				descriptionMap.put("firmwareMinorVersion",   	Integer.toString((int)queryMap.get("minorversion")));
				descriptionMap.put("firmwareUpdateType", 	 	Integer.toString((int)queryMap.get("updatetype")));
				descriptionMap.put("firmwareUpdateTime", 	 	UsdmUtils.convertDateToStr((double)queryMap.get("updatetime"), "yyyy-MM-dd HH:mm:ss.SSS"));
				descriptionMap.put("recommended1BlockSize", 	Integer.toString((int)queryMap.get("recommended1blocksize")));

				Map<String, Object> firmwareHistoryMap = new LinkedHashMap<String, Object>();
				
				firmwareHistoryMap.put("resourceType", "firmwareHistoryList");
				firmwareHistoryMap.put("description",  descriptionMap);
				
				firmwareHistoryList.add(firmwareHistoryMap);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("firmwareHistoryList", firmwareHistoryList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/firmwareUpdateHistoryRequest", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/transducerDefaultDescription
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/transducerDefaultDescription", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationTransducerDefaultDescription(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		int result = 0;
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String tdID 				= (String)filter.get("tdID");
			String manufacturer 		= (String)filter.get("manufacturer");
			String productNo 			= (String)filter.get("productNo");
			String transducerCategory 	= (String)filter.get("transducerCategory");
			String transducerType 		= (String)filter.get("transducerType");
			String unit 				= (String)filter.get("unit");
			String dataType 			= (String)filter.get("dataType");
			
			LinkedHashMap<String, Object> range = (LinkedHashMap<String, Object>)filter.get("range");
			
			ArrayList<String> levelList = (ArrayList<String>)filter.get("level");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (tdID==null               || tdID.equals(""))				throw new InvalidParameterException("parameter 'tdID' has no value");
			if (transducerCategory==null || transducerCategory.equals(""))	throw new InvalidParameterException("parameter 'transducerCategory' has no value");
			if (transducerType==null     || transducerType.equals(""))		throw new InvalidParameterException("parameter 'transducerType' has no value");
			if (unit==null               || unit.equals(""))				throw new InvalidParameterException("parameter 'unit' has no value");
			if (dataType==null           || dataType.equals(""))			throw new InvalidParameterException("parameter 'dataType' has no value");
			
			if (manufacturer!=null && manufacturer.equals(""))	throw new InvalidParameterException("parameter 'manufacturer' has no value");
			if (productNo!=null    && productNo.equals(""))		throw new InvalidParameterException("parameter 'productNo' has no value");
			
			if (range!=null && range.isEmpty()) throw new InvalidParameterException("parameter 'range' has no value");
			
			if (levelList!=null && levelList.isEmpty()) throw new InvalidParameterException("parameter 'level' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String 	rangeMin 	= "";
			String 	rangeMax 	= "";
			String 	rangeOffset = "";
			
			if (range != null) {
				rangeMin 	= (String)range.get("MIN");
				rangeMax 	= (String)range.get("MAX");
				rangeOffset = (String)range.get("OFFSET");
			}
			
			String level = "";
			
			if (levelList != null) {
				level = String.join(",", levelList);
			}
			
			SNTransducerVO transducerVO = new SNTransducerVO();
			
			transducerVO.setTdID(tdID);
			transducerVO.setManufacturer(manufacturer);
			transducerVO.setProductNo(productNo);
			transducerVO.setTransducerCategory(transducerCategory);
			transducerVO.setTransducerType(transducerType);
			transducerVO.setUnit(unit);
			transducerVO.setDataType(dataType);
			transducerVO.setRangeMin(Double.parseDouble(rangeMin));
			transducerVO.setRangeMax(Double.parseDouble(rangeMax));
			transducerVO.setRangeOffset(Double.parseDouble(rangeOffset));
			transducerVO.setLevel(level);
			
			result = apiInterfaceService.insertTdDefault(transducerVO);
			
			// 동일한 tdID를 갖는 레코드가 존재하는 경우
			if (result < 0) throw new InvalidParameterException("duplicate tdID");
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/transducerDefaultDescription", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/transducerDefaultRequest
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/transducerDefaultRequest", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationTransducerDefaultRequest(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			List<String> tdIDList = (List<String>)filter.get("tdIDList");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			//if (tdIDList == null) throw new InvalidParameterException("parameter 'tdIDList' required");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String tdIDCondition;
			
			if (tdIDList == null)
				tdIDCondition = "";
			else
				tdIDCondition = "WHERE tdID IN (" + UsdmUtils.getInOperatorString(tdIDList) + ")";
			
			SNTransducerVO transducerVO = new SNTransducerVO();
			transducerVO.setTdIDCondition(tdIDCondition);
			
			List<?> queryResult = apiInterfaceService.selectTdDefault(transducerVO);
			
			if (queryResult.isEmpty()) throw new NoResourceException();
			
			List<Map<String, Object>> tdDefaultList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				EgovMap queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> range = new LinkedHashMap<String, Object>();
				range.put("MIN", 	Double.toString((double)queryMap.get("rangemin")));
				range.put("MAX", 	Double.toString((double)queryMap.get("rangemax")));
				range.put("OFFSET",	Double.toString((double)queryMap.get("rangeoffset")));
				
				String level = (String)queryMap.get("level");
				List<String> levelList = new ArrayList<String>(Arrays.asList(level.split(",")));
				
				Map<String, Object> descMap = new LinkedHashMap<String, Object>();
				descMap.put("tdID",		  			(String)queryMap.get("tdid"));
				descMap.put("manufacturer",		  	(String)queryMap.get("manufacturer"));
				descMap.put("productNo",		  	(String)queryMap.get("productno"));
				descMap.put("transducerCategory",	(String)queryMap.get("transducercategory"));
				descMap.put("transducerType",		(String)queryMap.get("transducertype"));
				descMap.put("unit",		  			(String)queryMap.get("unit"));
				descMap.put("dataType",		  		(String)queryMap.get("datatype"));
				descMap.put("range", 				range);
				descMap.put("level",		  		levelList);
				
				Map<String, Object> tdDefaultMap = new LinkedHashMap<String, Object>();				
				tdDefaultMap.put("resourceType", "transDesc");
				tdDefaultMap.put("description",  descMap);
				
				tdDefaultList.add(tdDefaultMap);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("transDescList", tdDefaultList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/transducerDefaultRequest", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/transducerDefaultUpdate
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/transducerDefaultUpdate", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationTransducerDefaultUpdate(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		int result = 0;
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String tdID 				= (String)filter.get("tdID");
			String manufacturer 		= (String)filter.get("manufacturer");
			String productNo 			= (String)filter.get("productNo");
			String transducerCategory 	= (String)filter.get("transducerCategory");
			String transducerType 		= (String)filter.get("transducerType");
			String unit 				= (String)filter.get("unit");
			String dataType 			= (String)filter.get("dataType");
			
			LinkedHashMap<String, Object> range = (LinkedHashMap<String, Object>)filter.get("range");
			
			ArrayList<String> levelList = (ArrayList<String>)filter.get("level");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (tdID.equals(""))				throw new InvalidParameterException("parameter 'tdID' has no value");
			if (manufacturer.equals(""))		throw new InvalidParameterException("parameter 'manufacturer' has no value");
			if (productNo.equals(""))			throw new InvalidParameterException("parameter 'productNo' has no value");
			if (transducerCategory.equals(""))	throw new InvalidParameterException("parameter 'transducerCategory' has no value");
			if (transducerType.equals(""))		throw new InvalidParameterException("parameter 'transducerType' has no value");
			if (unit.equals(""))				throw new InvalidParameterException("parameter 'unit' has no value");
			if (dataType.equals(""))			throw new InvalidParameterException("parameter 'dataType' has no value");
			
			if (range.isEmpty()) throw new InvalidParameterException("parameter 'range' has no value");
			
			if (levelList.isEmpty()) throw new InvalidParameterException("parameter 'level' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String 	rangeMin 	= (String)range.get("MIN");
			String 	rangeMax 	= (String)range.get("MAX");
			String 	rangeOffset = (String)range.get("OFFSET");
			
			String level = String.join(",", levelList);
			
			SNTransducerVO transducerVO = new SNTransducerVO();
			transducerVO.setTdID(tdID);
			transducerVO.setManufacturer(manufacturer);
			transducerVO.setProductNo(productNo);
			transducerVO.setTransducerCategory(transducerCategory);
			transducerVO.setTransducerType(transducerType);
			transducerVO.setUnit(unit);
			transducerVO.setDataType(dataType);
			transducerVO.setRangeMin(Double.parseDouble(rangeMin));
			transducerVO.setRangeMax(Double.parseDouble(rangeMax));
			transducerVO.setRangeOffset(Double.parseDouble(rangeOffset));
			transducerVO.setLevel(level);
			
			result = apiInterfaceService.updateTdDefault(transducerVO);
			
			// 동일한 tdID를 갖는 레코드가 존재하지 않는 경우
			if (result < 0) throw new NoResourceException();
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/transducerDefaultUpdate", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/transducerDefaultDelete
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/transducerDefaultDelete", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationTransducerDefaultDelete(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		int result = 0;
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String tdID = (String)filter.get("tdID");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (tdID.equals("")) throw new InvalidParameterException("parameter 'tdID' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			SNTransducerVO transducerVO = new SNTransducerVO();
			transducerVO.setTdID(tdID);
			
			result = apiInterfaceService.deleteTdDefault(transducerVO);
			
			// 동일한 tdID를 갖는 레코드가 존재하지 않는 경우
			if (result < 0) throw new NoResourceException();
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/transducerDefaultDelete", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/userDefinedMessageStore
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/userDefinedMessageStore", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationUserDefinedMessageStore(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		int result = 0;
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");

			String targetID					= (String)filter.get("targetID");
			String commandID				= (String)filter.get("commandID");
			String transmissionDirection	= (String)filter.get("transmissionDirection");
			String transmissionResult 		= (String)filter.get("transmissionResult");
			String transmissionTime			= (String)filter.get("transmissionTime");
			String messageDataSize			= (String)filter.get("messageDataSize");
			String messageBinaryData		= (String)filter.get("messageBinaryData");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (targetID==null              || targetID.equals(""))					throw new InvalidParameterException("parameter 'targetID' has no value");			
			if (commandID==null             || commandID.equals(""))				throw new InvalidParameterException("parameter 'commandID' has no value");			
			if (transmissionDirection==null || transmissionDirection.equals(""))	throw new InvalidParameterException("parameter 'transmissionDirection' has no value");			
			if (transmissionResult==null    || transmissionResult.equals(""))		throw new InvalidParameterException("parameter 'transmissionResult' has no value");			
			if (transmissionTime==null      || transmissionTime.equals(""))			throw new InvalidParameterException("parameter 'transmissionTime' has no value");			
			if (messageDataSize==null       || messageDataSize.equals(""))			throw new InvalidParameterException("parameter 'messageDataSize' has no value");			
			if (messageBinaryData==null     || messageBinaryData.equals(""))		throw new InvalidParameterException("parameter 'messageBinaryData' has no value");			
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			UserDefinedMessageVO udmVO = new UserDefinedMessageVO();
			
			udmVO.setTargetID(targetID);
			udmVO.setTransmissionTime(UsdmUtils.convertStrToDate(transmissionTime, "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
			udmVO.setCommandID(Integer.parseInt(commandID));
			udmVO.setTransmissionDirection(transmissionDirection);
			udmVO.setTransmissionResult(transmissionResult);
			udmVO.setMessageDataSize(Integer.parseInt(messageDataSize));
			udmVO.setMessageBinaryData(messageBinaryData);
			
			result = apiInterfaceService.insertUserDefinedMessage(udmVO);
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/userDefinedMessageStore", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// information/userDefinedMessageRequest
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/information/userDefinedMessageRequest", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonInformationUserDefinedMessageRequest(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String targetID = (String)filter.get("targetID");
			
			Map<String, Object> temporalConditionMap = (Map<String, Object>)filter.get("temporalCondition");
			
			String recentCount = (String)filter.get("recentCount");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (targetID==null || targetID.equals("")) throw new InvalidParameterException("parameter 'targetID' has no value");
			
			if (temporalConditionMap!=null && temporalConditionMap.isEmpty())
				throw new InvalidParameterException("parameter 'temporalCondition' has no value");
			
			if (recentCount!=null && recentCount.equals("")) throw new InvalidParameterException("parameter 'recentCount' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String temporalCondition = "";
			
			// 시간조건이 존재하는 경우
			if (temporalConditionMap != null) {
				long startTime = UsdmUtils.convertStrToDate((String)temporalConditionMap.get("startTime"), "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
				long endTime   = UsdmUtils.convertStrToDate((String)temporalConditionMap.get("endTime"),   "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
				
				temporalCondition = " AND transmissionTime >= " + startTime + " AND transmissionTime <= " + endTime;
			}
			
			// recentCount값이 존재하는 경우
			int recordCount = 0;
			
			if (recentCount != null) {
				recordCount = Integer.parseInt(recentCount);
				
				if (recordCount < 0)
					throw new InvalidParameterException("value of parameter 'recentCount' must be larger than 0");
			}
			
			UserDefinedMessageVO udmVO = new UserDefinedMessageVO();
			
			udmVO.setTargetID(targetID);
			udmVO.setTemporalCondition(temporalCondition);
			
			List<?> queryResult = apiInterfaceService.selectUserDefinedMessage(udmVO);
			
			if (queryResult.isEmpty()) throw new NoResourceException();
			
			List<Map<String, Object>> udmList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				// recentCount의 값만큼 결과 record의 수를 제한
				if (recentCount!=null && i==recordCount) {
					break;
				}
				
				EgovMap queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> descMap = new LinkedHashMap<String, Object>();
				descMap.put("targetID",		  			queryMap.get("targetid").toString());
				descMap.put("commandID",		  		queryMap.get("commandid").toString());
				descMap.put("transmissionDirection",	queryMap.get("transmissiondirection").toString());
				descMap.put("transmissionResult",		queryMap.get("transmissionresult").toString());
				descMap.put("transmissionTime",			UsdmUtils.convertDateToStr((double)queryMap.get("transmissiontime"), "yyyy-MM-dd HH:mm:ss.SSS"));
				descMap.put("messageDataSize",		  	queryMap.get("datasize").toString());
				descMap.put("messageBinaryData",		queryMap.get("binarydata").toString());
				
				Map<String, Object> udmMap = new LinkedHashMap<String, Object>();				
				udmMap.put("resourceType", "userDefinedMessage");
				udmMap.put("description",  descMap);
				
				udmList.add(udmMap);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("userDefinedMessageList", udmList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/information/userDefinedMessageRequest", filterJSON, responseJson);
		
		return responseJson;
	}
		
	// sri/getWaterBSRIByID
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/sri/getWaterBSRIByID", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonSriGetWaterBSRIByID(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			List<String> targetList = (ArrayList<String>)filter.get("targetList");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (targetList == null) throw new InvalidParameterException("parameter 'targetList' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String geoID = UsdmUtils.getInOperatorString(targetList);
			
			SNSriVO sriVO = new SNSriVO();
			sriVO.setGeoID(geoID);
			
			List<?> queryResult = apiInterfaceService.getWaterBSRIByID(sriVO);
			
			if (queryResult.isEmpty()) throw new NoResourceException();
			
			List<Map<String, Object>> bsriList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				EgovMap queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> bsriMap = new LinkedHashMap<String, Object>();
				
				bsriMap.put("geoID",		 	queryMap.get("geoid").toString());
				bsriMap.put("BSRI",  	 		queryMap.get("bsri").toString());
				bsriMap.put("level", 			queryMap.get("level").toString());
				bsriMap.put("indirectAssess",   queryMap.get("indirectassess").toString());
				bsriMap.put("leakNoise",   		queryMap.get("leaknoise").toString());
				bsriMap.put("pipeMove", 	 	queryMap.get("pipemove").toString());

				bsriList.add(bsriMap);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("bsriList",     bsriList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/sri/getWaterBSRIByID", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// sri/getWaterBSRIByRegion
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/sri/getWaterBSRIByRegion", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonSriGetWaterBSRIByRegion(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String coordinateType = (String)filter.get("coordinateType");
			
			List<String> bbox = (ArrayList<String>)filter.get("bbox");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (coordinateType == null) throw new InvalidParameterException("parameter 'coordinateType' has no value");
			if (bbox == null) 			throw new InvalidParameterException("parameter 'bbox' has no value");
			
			if (!coordinateType.equals("1") && !coordinateType.equals("2"))
				throw new InvalidParameterException("unsupported coordinate type");
			
			if (bbox.size() != 4) throw new InvalidParameterException("number of elements for parameter 'bbox' is invalid (4 required)");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String minX = bbox.get(0);
			String minY = bbox.get(1);
			String maxX = bbox.get(2);
			String maxY = bbox.get(3);
			
			// 좌표타입이 '1'(위경도좌표)이면 TM좌표로 변환
			if (coordinateType.equals("1")) {
				double tmMinX = UsdmUtils.WGSLongitudeToEast(Double.parseDouble(minY), Double.parseDouble(minX));
				double tmMinY = UsdmUtils.WGSLatitudeToNorth(Double.parseDouble(minY), Double.parseDouble(minX));
				double tmMaxX = UsdmUtils.WGSLongitudeToEast(Double.parseDouble(maxY), Double.parseDouble(maxX));
				double tmMaxY = UsdmUtils.WGSLatitudeToNorth(Double.parseDouble(maxY), Double.parseDouble(maxX));
				
				minX = Double.toString(tmMinX);
				minY = Double.toString(tmMinY);
				maxX = Double.toString(tmMaxX);
				maxY = Double.toString(tmMaxY);
			}
			
			SNSriVO sriVO = new SNSriVO();
			
			sriVO.setMinX(minX);
			sriVO.setMinY(minY);
			sriVO.setMaxX(maxX);
			sriVO.setMaxY(maxY);
			
			List<?> queryResult = apiInterfaceService.getWaterBSRIByRegion(sriVO);
			
			if (queryResult.isEmpty()) throw new NoResourceException();
			
			List<Map<String, Object>> bsriList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				EgovMap queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> bsriMap = new LinkedHashMap<String, Object>();
				
				bsriMap.put("geoID",		 	queryMap.get("geoid").toString());
				bsriMap.put("BSRI",  	 		queryMap.get("bsri").toString());
				bsriMap.put("level", 			queryMap.get("level").toString());
				bsriMap.put("indirectAssess",   queryMap.get("indirectassess").toString());
				bsriMap.put("leakNoise",   		queryMap.get("leaknoise").toString());
				bsriMap.put("pipeMove", 	 	queryMap.get("pipemove").toString());
				
				bsriList.add(bsriMap);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("bsriList",     bsriList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/sri/getWaterBSRIByRegion", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// sri/getSewerBSRIByID
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/sri/getSewerBSRIByID", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonSriGetSewerBSRIByID(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			List<String> targetList = (ArrayList<String>)filter.get("targetList");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (targetList == null) throw new InvalidParameterException("parameter 'targetList' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String geoID = UsdmUtils.getInOperatorString(targetList);
			
			SNSriVO sriVO = new SNSriVO();
			sriVO.setGeoID(geoID);
			
			List<?> queryResult = apiInterfaceService.getSewerBSRIByID(sriVO);
			
			if (queryResult.isEmpty()) throw new NoResourceException();
			
			List<Map<String, Object>> bsriList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				EgovMap queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> bsriMap = new LinkedHashMap<String, Object>();
				
				bsriMap.put("geoID",		 	queryMap.get("geoid").toString());
				bsriMap.put("BSRI",  	 		queryMap.get("bsri").toString());
				bsriMap.put("level", 			queryMap.get("level").toString());
				bsriMap.put("indirectAssess",   queryMap.get("indirectassess").toString());
				bsriMap.put("internalState",   	queryMap.get("internalstate").toString());
				bsriMap.put("emptySpace", 	 	queryMap.get("emptyspace").toString());
				
				bsriList.add(bsriMap);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("bsriList",     bsriList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/sri/getSewerBSRIByID", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// sri/getSewerBSRIByRegion
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/sri/getSewerBSRIByRegion", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonSriGetSewerBSRIByRegion(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String coordinateType = (String)filter.get("coordinateType");
			
			List<String> bbox = (ArrayList<String>)filter.get("bbox");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (coordinateType == null) throw new InvalidParameterException("parameter 'coordinateType' has no value");
			if (bbox == null) 			throw new InvalidParameterException("parameter 'bbox' has no value");
			
			if (!coordinateType.equals("1") && !coordinateType.equals("2"))
				throw new InvalidParameterException("unsupported coordinate type");
			
			if (bbox.size() != 4) throw new InvalidParameterException("number of elements for parameter 'bbox' is invalid (4 required)");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String minX = bbox.get(0);
			String minY = bbox.get(1);
			String maxX = bbox.get(2);
			String maxY = bbox.get(3);
			
			// 좌표타입이 '1'(위경도좌표)이면 TM좌표로 변환
			if (coordinateType.equals("1")) {
				double tmMinX = UsdmUtils.WGSLongitudeToEast(Double.parseDouble(minY), Double.parseDouble(minX));
				double tmMinY = UsdmUtils.WGSLatitudeToNorth(Double.parseDouble(minY), Double.parseDouble(minX));
				double tmMaxX = UsdmUtils.WGSLongitudeToEast(Double.parseDouble(maxY), Double.parseDouble(maxX));
				double tmMaxY = UsdmUtils.WGSLatitudeToNorth(Double.parseDouble(maxY), Double.parseDouble(maxX));
				
				minX = Double.toString(tmMinX);
				minY = Double.toString(tmMinY);
				maxX = Double.toString(tmMaxX);
				maxY = Double.toString(tmMaxY);
			}
			
			SNSriVO sriVO = new SNSriVO();
			
			sriVO.setMinX(minX);
			sriVO.setMinY(minY);
			sriVO.setMaxX(maxX);
			sriVO.setMaxY(maxY);
			
			List<?> queryResult = apiInterfaceService.getSewerBSRIByRegion(sriVO);
			
			if (queryResult.isEmpty()) throw new NoResourceException();
			
			List<Map<String, Object>> bsriList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				EgovMap queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> bsriMap = new LinkedHashMap<String, Object>();
				
				bsriMap.put("geoID",		 	queryMap.get("geoid").toString());
				bsriMap.put("BSRI",  	 		queryMap.get("bsri").toString());
				bsriMap.put("level", 			queryMap.get("level").toString());
				bsriMap.put("indirectAssess",   queryMap.get("indirectassess").toString());
				bsriMap.put("internalState",   	queryMap.get("internalstate").toString());
				bsriMap.put("emptySpace", 	 	queryMap.get("emptyspace").toString());
				
				bsriList.add(bsriMap);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("bsriList",     bsriList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/sri/getSewerBSRIByRegion", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// sri/getMLineBSRIByID
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/sri/getMLineBSRIByID", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonSriGetMLineBSRIByID(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			List<String> targetList = (ArrayList<String>)filter.get("targetList");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (targetList == null) throw new InvalidParameterException("parameter 'targetList' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String geoID = UsdmUtils.getInOperatorString(targetList);
			
			SNSriVO sriVO = new SNSriVO();
			sriVO.setGeoID(geoID);
			
			List<?> queryResult = apiInterfaceService.getSubwayBSRIByID(sriVO);
			
			if (queryResult.isEmpty()) throw new NoResourceException();
			
			List<Map<String, Object>> bsriList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				EgovMap queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> bsriMap = new LinkedHashMap<String, Object>();
				
				bsriMap.put("geoID",		 	queryMap.get("geoid").toString());
				bsriMap.put("BSRI",  	 		queryMap.get("bsri").toString());
				bsriMap.put("level", 			queryMap.get("level").toString());
				bsriMap.put("structureGrade",   queryMap.get("structuregrade").toString());
				bsriMap.put("designType",   	queryMap.get("designtype").toString());
				bsriMap.put("maxDepth", 	 	queryMap.get("maxdepth").toString());
				bsriMap.put("duration", 	 	queryMap.get("duration").toString());
				bsriMap.put("groundCount", 	 	queryMap.get("groundcount").toString());
				bsriMap.put("roadCount", 	 	queryMap.get("roadcount").toString());
				bsriMap.put("evaDistance", 	 	queryMap.get("evadistance").toString());
				bsriMap.put("evaDepth", 	 	queryMap.get("evadepth").toString());
				bsriMap.put("collectingWell",	queryMap.get("collectingwell").toString());
				
				bsriList.add(bsriMap);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("bsriList",     bsriList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/sri/getMLineBSRIByID", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// sri/getMLineBSRIByRegion
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/sri/getMLineBSRIByRegion", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonSriGetMLineBSRIByRegion(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String coordinateType = (String)filter.get("coordinateType");
			
			List<String> bbox = (ArrayList<String>)filter.get("bbox");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (coordinateType == null) throw new InvalidParameterException("parameter 'coordinateType' has no value");
			if (bbox == null) 			throw new InvalidParameterException("parameter 'bbox' has no value");
			
			if (!coordinateType.equals("1") && !coordinateType.equals("2"))
				throw new InvalidParameterException("unsupported coordinate type");
			
			if (bbox.size() != 4) throw new InvalidParameterException("number of elements for parameter 'bbox' is invalid (4 required)");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String minX = bbox.get(0);
			String minY = bbox.get(1);
			String maxX = bbox.get(2);
			String maxY = bbox.get(3);
			
			// 좌표타입이 '1'(위경도좌표)이면 TM좌표로 변환
			if (coordinateType.equals("1")) {
				double tmMinX = UsdmUtils.WGSLongitudeToEast(Double.parseDouble(minY), Double.parseDouble(minX));
				double tmMinY = UsdmUtils.WGSLatitudeToNorth(Double.parseDouble(minY), Double.parseDouble(minX));
				double tmMaxX = UsdmUtils.WGSLongitudeToEast(Double.parseDouble(maxY), Double.parseDouble(maxX));
				double tmMaxY = UsdmUtils.WGSLatitudeToNorth(Double.parseDouble(maxY), Double.parseDouble(maxX));
				
				minX = Double.toString(tmMinX);
				minY = Double.toString(tmMinY);
				maxX = Double.toString(tmMaxX);
				maxY = Double.toString(tmMaxY);
			}
			
			SNSriVO sriVO = new SNSriVO();
			
			sriVO.setMinX(minX);
			sriVO.setMinY(minY);
			sriVO.setMaxX(maxX);
			sriVO.setMaxY(maxY);
			
			List<?> queryResult = apiInterfaceService.getSubwayBSRIByRegion(sriVO);
			
			if (queryResult.isEmpty()) throw new NoResourceException();
			
			List<Map<String, Object>> bsriList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				EgovMap queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> bsriMap = new LinkedHashMap<String, Object>();
				
				bsriMap.put("geoID",		 	queryMap.get("geoid").toString());
				bsriMap.put("BSRI",  	 		queryMap.get("bsri").toString());
				bsriMap.put("level", 			queryMap.get("level").toString());
				bsriMap.put("structureGrade",   queryMap.get("structuregrade").toString());
				bsriMap.put("designType",   	queryMap.get("designtype").toString());
				bsriMap.put("maxDepth", 	 	queryMap.get("maxdepth").toString());
				bsriMap.put("duration", 	 	queryMap.get("duration").toString());
				bsriMap.put("groundCount", 	 	queryMap.get("groundcount").toString());
				bsriMap.put("roadCount", 	 	queryMap.get("roadcount").toString());
				bsriMap.put("evaDistance", 	 	queryMap.get("evadistance").toString());
				bsriMap.put("evaDepth", 	 	queryMap.get("evadepth").toString());
				bsriMap.put("collectingWell",	queryMap.get("collectingwell").toString());
				
				bsriList.add(bsriMap);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("bsriList",     bsriList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/sri/getMLineBSRIByRegion", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// sri/getMStationBSRIByID
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/sri/getMStationBSRIByID", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonSriGetMStationBSRIByID(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			List<String> targetList = (ArrayList<String>)filter.get("targetList");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (targetList == null) throw new InvalidParameterException("parameter 'targetList' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String geoID = UsdmUtils.getInOperatorString(targetList);
			
			SNSriVO sriVO = new SNSriVO();
			sriVO.setGeoID(geoID);
			
			List<?> queryResult = apiInterfaceService.getStationBSRIByID(sriVO);
			
			if (queryResult.isEmpty()) throw new NoResourceException();
			
			List<Map<String, Object>> bsriList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				EgovMap queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> bsriMap = new LinkedHashMap<String, Object>();
				
				bsriMap.put("geoID",		 	queryMap.get("geoid").toString());
				bsriMap.put("BSRI",  	 		queryMap.get("bsri").toString());
				bsriMap.put("level", 			queryMap.get("level").toString());
				bsriMap.put("maxDepth", 	 	queryMap.get("maxdepth").toString());
				bsriMap.put("duration", 	 	queryMap.get("duration").toString());
				bsriMap.put("groundCount", 	 	queryMap.get("groundcount").toString());
				bsriMap.put("roadCount", 	 	queryMap.get("roadcount").toString());
				bsriMap.put("evaDistance", 	 	queryMap.get("evadistance").toString());
				bsriMap.put("evaDepth", 	 	queryMap.get("evadepth").toString());
				bsriMap.put("collectingWell",	queryMap.get("collectingwell").toString());
				
				bsriList.add(bsriMap);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("bsriList",     bsriList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/sri/getMStationBSRIByID", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// sri/getMStationBSRIByRegion
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/sri/getMStationBSRIByRegion", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonSriGetMStationBSRIByRegion(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String coordinateType = (String)filter.get("coordinateType");
			
			List<String> bbox = (ArrayList<String>)filter.get("bbox");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (coordinateType == null) throw new InvalidParameterException("parameter 'coordinateType' has no value");
			if (bbox == null) 			throw new InvalidParameterException("parameter 'bbox' has no value");
			
			if (!coordinateType.equals("1") && !coordinateType.equals("2"))
				throw new InvalidParameterException("unsupported coordinate type");
			
			if (bbox.size() != 4) throw new InvalidParameterException("number of elements for parameter 'bbox' is invalid (4 required)");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String minX = bbox.get(0);
			String minY = bbox.get(1);
			String maxX = bbox.get(2);
			String maxY = bbox.get(3);
			
			// 좌표타입이 '1'(위경도좌표)이면 TM좌표로 변환
			if (coordinateType.equals("1")) {
				double tmMinX = UsdmUtils.WGSLongitudeToEast(Double.parseDouble(minY), Double.parseDouble(minX));
				double tmMinY = UsdmUtils.WGSLatitudeToNorth(Double.parseDouble(minY), Double.parseDouble(minX));
				double tmMaxX = UsdmUtils.WGSLongitudeToEast(Double.parseDouble(maxY), Double.parseDouble(maxX));
				double tmMaxY = UsdmUtils.WGSLatitudeToNorth(Double.parseDouble(maxY), Double.parseDouble(maxX));
				
				minX = Double.toString(tmMinX);
				minY = Double.toString(tmMinY);
				maxX = Double.toString(tmMaxX);
				maxY = Double.toString(tmMaxY);
			}
			
			SNSriVO sriVO = new SNSriVO();
			
			sriVO.setMinX(minX);
			sriVO.setMinY(minY);
			sriVO.setMaxX(maxX);
			sriVO.setMaxY(maxY);
			
			List<?> queryResult = apiInterfaceService.getStationBSRIByRegion(sriVO);
			
			if (queryResult.isEmpty()) throw new NoResourceException();
			
			List<Map<String, Object>> bsriList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				EgovMap queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> bsriMap = new LinkedHashMap<String, Object>();
				
				bsriMap.put("geoID",		 	queryMap.get("geoid").toString());
				bsriMap.put("BSRI",  	 		queryMap.get("bsri").toString());
				bsriMap.put("level", 			queryMap.get("level").toString());
				bsriMap.put("maxDepth", 	 	queryMap.get("maxdepth").toString());
				bsriMap.put("duration", 	 	queryMap.get("duration").toString());
				bsriMap.put("groundCount", 	 	queryMap.get("groundcount").toString());
				bsriMap.put("roadCount", 	 	queryMap.get("roadcount").toString());
				bsriMap.put("evaDistance", 	 	queryMap.get("evadistance").toString());
				bsriMap.put("evaDepth", 	 	queryMap.get("evadepth").toString());
				bsriMap.put("collectingWell",	queryMap.get("collectingwell").toString());
				
				bsriList.add(bsriMap);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("bsriList",     bsriList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/sri/getMStationBSRIByRegion", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// sri/updateWaterBSRIByID
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/sri/updateWaterBSRIByID", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonSriUpdateWaterBSRIByID(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			List<Map<String, Object>> bsriList = (ArrayList<Map<String, Object>>)filter.get("bsriList");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (bsriList == null) throw new InvalidParameterException("parameter 'bsriList' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			Map<String, Object> bsriMap = new HashMap<String, Object>();
			
			String geoID;
			String roadType;
			String complaintCount;
			String recordPeriod;
			String leakSignal;
			String pipeMove;
			
			for (int i=0; i<bsriList.size(); i++) {
				bsriMap = bsriList.get(i);
				
				geoID 			= (String)bsriMap.get("geoID");
				roadType 		= (String)bsriMap.get("roadType");
				complaintCount 	= (String)bsriMap.get("complaintCount");
				recordPeriod 	= (String)bsriMap.get("recordPeriod");
				leakSignal 		= (String)bsriMap.get("leakSignal");
				pipeMove 		= (String)bsriMap.get("pipeMove");
				
				/******************************/
				/* message validation : start */
				/******************************/
				
				if (geoID.equals("")) 			throw new InvalidParameterException("parameter 'geoID' has no value");
				if (roadType.equals("")) 		throw new InvalidParameterException("parameter 'roadType' has no value");
				if (complaintCount.equals("")) 	throw new InvalidParameterException("parameter 'complaintCount' has no value");
				if (recordPeriod.equals("")) 	throw new InvalidParameterException("parameter 'recordPeriod' has no value");
				if (leakSignal.equals("")) 		throw new InvalidParameterException("parameter 'leakSignal' has no value");
				if (pipeMove.equals("")) 		throw new InvalidParameterException("parameter 'pipeMove' has no value");
				
				/****************************/
				/* message validation : end */
				/****************************/
				
				SNSriVO sriVO = new SNSriVO();
				
				sriVO.setGeoID(geoID);
				sriVO.setRoadType(roadType);
				sriVO.setComplaintCount(complaintCount);
				sriVO.setRecordPeriod(recordPeriod);
				sriVO.setLeakSignal(leakSignal);
				sriVO.setPipeMove(pipeMove);
				
				apiInterfaceService.updateWaterBSRIByID(sriVO);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/sri/updateWaterBSRIByID", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// sri/updateSewerBSRIByID
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/sri/updateSewerBSRIByID", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonSriUpdateSewerBSRIByID(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			List<Map<String, Object>> bsriList = (ArrayList<Map<String, Object>>)filter.get("bsriList");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (bsriList == null) throw new InvalidParameterException("parameter 'bsriList' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			Map<String, Object> bsriMap = new HashMap<String, Object>();
			
			String geoID;
			String roadType;
			String recordPeriod;
			String internalState;
			String emptySpace;
			
			for (int i=0; i<bsriList.size(); i++) {
				bsriMap = bsriList.get(i);
				
				geoID 			= (String)bsriMap.get("geoID");
				roadType 		= (String)bsriMap.get("roadType");
				recordPeriod 	= (String)bsriMap.get("recordPeriod");
				internalState 	= (String)bsriMap.get("internalState");
				emptySpace 		= (String)bsriMap.get("emptySpace");
				
				/******************************/
				/* message validation : start */
				/******************************/
				
				if (geoID.equals("")) 			throw new InvalidParameterException("parameter 'geoID' has no value");
				if (roadType.equals("")) 		throw new InvalidParameterException("parameter 'roadType' has no value");
				if (recordPeriod.equals("")) 	throw new InvalidParameterException("parameter 'recordPeriod' has no value");
				if (internalState.equals("")) 	throw new InvalidParameterException("parameter 'internalState' has no value");
				if (emptySpace.equals("")) 		throw new InvalidParameterException("parameter 'emptySpace' has no value");
				
				/****************************/
				/* message validation : end */
				/****************************/
				
				SNSriVO sriVO = new SNSriVO();
				
				sriVO.setGeoID(geoID);
				sriVO.setRoadType(roadType);
				sriVO.setRecordPeriod(recordPeriod);
				sriVO.setInternalState(internalState);
				sriVO.setEmptySpace(emptySpace);
				
				apiInterfaceService.updateSewerBSRIByID(sriVO);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/sri/updateSewerBSRIByID", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// sri/updateMLineBSRIByID
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/sri/updateMLineBSRIByID", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonSriUpdateMLineBSRIByID(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			List<Map<String, Object>> bsriList = (ArrayList<Map<String, Object>>)filter.get("bsriList");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (bsriList == null) throw new InvalidParameterException("parameter 'bsriList' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			Map<String, Object> bsriMap = new HashMap<String, Object>();
			
			String geoID;
			String structureGrade;
			String groundCount50;
			String groundCount100;
			String groundCount200;
			String roadCount50;
			String roadCount100;
			String roadCount200;
			String evaDistance;
			String evaDepth;
			String collectingWell;
			
			for (int i=0; i<bsriList.size(); i++) {
				bsriMap = bsriList.get(i);
				
				geoID 			= (String)bsriMap.get("geoID");
				structureGrade 	= (String)bsriMap.get("structureGrade");
				groundCount50 	= (String)bsriMap.get("groundCount50");
				groundCount100 	= (String)bsriMap.get("groundCount100");
				groundCount200 	= (String)bsriMap.get("groundCount200");
				roadCount50 	= (String)bsriMap.get("roadCount50");
				roadCount100 	= (String)bsriMap.get("roadCount100");
				roadCount200 	= (String)bsriMap.get("roadCount200");
				evaDistance 	= (String)bsriMap.get("evaDistance");
				evaDepth 		= (String)bsriMap.get("evaDepth");
				collectingWell 	= (String)bsriMap.get("collectingWell");
				
				/******************************/
				/* message validation : start */
				/******************************/
				
				if (geoID.equals("")) 			throw new InvalidParameterException("parameter 'geoID' has no value");
				if (structureGrade.equals("")) 	throw new InvalidParameterException("parameter 'structureGrade' has no value");
				if (groundCount50.equals("")) 	throw new InvalidParameterException("parameter 'groundCount50' has no value");
				if (groundCount100.equals("")) 	throw new InvalidParameterException("parameter 'groundCount100' has no value");
				if (groundCount200.equals("")) 	throw new InvalidParameterException("parameter 'groundCount200' has no value");
				if (roadCount50.equals("")) 	throw new InvalidParameterException("parameter 'roadCount50' has no value");
				if (roadCount100.equals("")) 	throw new InvalidParameterException("parameter 'roadCount100' has no value");
				if (roadCount200.equals("")) 	throw new InvalidParameterException("parameter 'roadCount200' has no value");
				if (evaDistance.equals("")) 	throw new InvalidParameterException("parameter 'evaDistance' has no value");
				if (evaDepth.equals("")) 		throw new InvalidParameterException("parameter 'evaDepth' has no value");
				if (collectingWell.equals("")) 	throw new InvalidParameterException("parameter 'collectingWell' has no value");
				
				/****************************/
				/* message validation : end */
				/****************************/
				
				SNSriVO sriVO = new SNSriVO();
				
				sriVO.setGeoID(geoID);
				sriVO.setStructureGrade(structureGrade);
				sriVO.setGroundCount50(groundCount50);
				sriVO.setGroundCount100(groundCount100);
				sriVO.setGroundCount200(groundCount200);
				sriVO.setRoadCount50(roadCount50);
				sriVO.setRoadCount100(roadCount100);
				sriVO.setRoadCount200(roadCount200);
				sriVO.setEvaDistance(evaDistance);
				sriVO.setEvaDepth(evaDepth);
				sriVO.setCollectingWell(collectingWell);
				
				apiInterfaceService.updateSubwayBSRIByID(sriVO);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/sri/updateMLineBSRIByID", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// sri/updateMStatiionBSRIByID
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/sri/updateMStationBSRIByID", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonSriUpdateMStationBSRIByID(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			List<Map<String, Object>> bsriList = (ArrayList<Map<String, Object>>)filter.get("bsriList");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (bsriList == null) throw new InvalidParameterException("parameter 'bsriList' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			Map<String, Object> bsriMap = new HashMap<String, Object>();
			
			String geoID;
			String structureGrade;
			String groundCount50;
			String groundCount100;
			String groundCount200;
			String roadCount50;
			String roadCount100;
			String roadCount200;
			String evaDistance;
			String evaDepth;
			String collectingWell;
			
			for (int i=0; i<bsriList.size(); i++) {
				bsriMap = bsriList.get(i);
				
				geoID 			= (String)bsriMap.get("geoID");
				groundCount50 	= (String)bsriMap.get("groundCount50");
				groundCount100 	= (String)bsriMap.get("groundCount100");
				groundCount200 	= (String)bsriMap.get("groundCount200");
				roadCount50 	= (String)bsriMap.get("roadCount50");
				roadCount100 	= (String)bsriMap.get("roadCount100");
				roadCount200 	= (String)bsriMap.get("roadCount200");
				evaDistance 	= (String)bsriMap.get("evaDistance");
				evaDepth 		= (String)bsriMap.get("evaDepth");
				collectingWell 	= (String)bsriMap.get("collectingWell");
				
				/******************************/
				/* message validation : start */
				/******************************/
				
				if (geoID.equals("")) 			throw new InvalidParameterException("parameter 'geoID' has no value");
				if (groundCount50.equals("")) 	throw new InvalidParameterException("parameter 'groundCount50' has no value");
				if (groundCount100.equals("")) 	throw new InvalidParameterException("parameter 'groundCount100' has no value");
				if (groundCount200.equals("")) 	throw new InvalidParameterException("parameter 'groundCount200' has no value");
				if (roadCount50.equals("")) 	throw new InvalidParameterException("parameter 'roadCount50' has no value");
				if (roadCount100.equals("")) 	throw new InvalidParameterException("parameter 'roadCount100' has no value");
				if (roadCount200.equals("")) 	throw new InvalidParameterException("parameter 'roadCount200' has no value");
				if (evaDistance.equals("")) 	throw new InvalidParameterException("parameter 'evaDistance' has no value");
				if (evaDepth.equals("")) 		throw new InvalidParameterException("parameter 'evaDepth' has no value");
				if (collectingWell.equals("")) 	throw new InvalidParameterException("parameter 'collectingWell' has no value");
				
				/****************************/
				/* message validation : end */
				/****************************/
				
				SNSriVO sriVO = new SNSriVO();
				
				sriVO.setGeoID(geoID);
				sriVO.setGroundCount50(groundCount50);
				sriVO.setGroundCount100(groundCount100);
				sriVO.setGroundCount200(groundCount200);
				sriVO.setRoadCount50(roadCount50);
				sriVO.setRoadCount100(roadCount100);
				sriVO.setRoadCount200(roadCount200);
				sriVO.setEvaDistance(evaDistance);
				sriVO.setEvaDepth(evaDepth);
				sriVO.setCollectingWell(collectingWell);
				
				apiInterfaceService.updateStationBSRIByID(sriVO);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/sri/updateMStationBSRIByID", filterJSON, responseJson);
		
		return responseJson;
	}
		
	// sri/insertAssessValues
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/sri/insertAssessValues", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonSriInsertAssessValues(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String category = (String)filter.get("category");
			
			ArrayList<LinkedHashMap<String, Object>> assessValuesList = (ArrayList<LinkedHashMap<String, Object>>)filter.get("assessValues");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (category.equals("")) throw new InvalidParameterException("parameter 'category' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
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
				
				if (type.equals("")) throw new InvalidParameterException("parameter 'type' has no value");
				
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
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/sri/insertAssessValues", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// sri/retrieveAssessValues
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/sri/retrieveAssessValues", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonSriRetrieveAssessValues(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			String category		= (String)filter.get("category");
			
			List<String> bbox 	= (ArrayList<String>)filter.get("bbox");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (category.equals("")) throw new InvalidParameterException("parameter 'category' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
		
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
			
			if (assessValueList.size() == 0) {
				throw new NoResourceException();
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("assessValues",	assessValueList);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/sri/retrieveAssessValues", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// sri/assessUtilities
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/sri/assessUtilities", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonSriAssessUtilities(@RequestBody String filterJSON, HttpServletResponse response) {
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
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (categoryList.isEmpty())	throw new InvalidParameterException("parameter 'category' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
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
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/sri/assessUtilities", filterJSON, responseJson);

		return responseJson;
	}
	
	// sri/assessSRI
	@RequestMapping(value="/sri/assessSRI", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonSriAssessSRI(@RequestBody String filterJSON, HttpServletResponse response) {
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
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			/****************************/
			/* message validation : end */
			/****************************/
			
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
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/sri/assessSRI", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// sri/retrieveRiskValues
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/sri/retrieveRiskValues", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonSriRetrieveRiskValues(@RequestBody String filterJSON, HttpServletResponse response) {
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
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (categoryList.isEmpty()) throw new InvalidParameterException("parameter 'category' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
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
			
			if (riskValueList.size() == 0) {
				throw new NoResourceException();
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("riskValues",	riskValueList);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}

		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/sri/retrieveRiskValues", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// sri/updateRiskValues
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/sri/updateRiskValues", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonSriUpdateRiskValues(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
		/*
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		*/
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/sri/updateRiskValues", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// rfid/initializeRFID
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/rfid/initializeRFID", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonRfidInitializeRFID(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			List<LinkedHashMap<String, Object>> rfidInfoList = (List<LinkedHashMap<String, Object>>) filter.get("rfidInfoList");
					
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (rfidInfoList==null || rfidInfoList.isEmpty()) throw new InvalidParameterException("parameter 'rfidInfoList' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String rfid			= null;
			String latitude		= null;
			String longitude	= null;
			String state		= null;
			String strength		= null;
			
			for (int i=0; i<rfidInfoList.size(); i++) {
				
				LinkedHashMap<String, Object> rfidInfoMap = rfidInfoList.get(i);
				
				rfid		= (String)rfidInfoMap.get("rfid");
				latitude	= (String)rfidInfoMap.get("latitude");
				longitude 	= (String)rfidInfoMap.get("longitude");
				state		= (String)rfidInfoMap.get("state");
				strength 	= (String)rfidInfoMap.get("strength");
				
				/******************************/
				/* message validation : start */
				/******************************/
				
				if (rfid==null      || rfid.equals("")) 		throw new InvalidParameterException("parameter 'rfid' has no value");
				if (latitude==null  || latitude.equals("")) 	throw new InvalidParameterException("parameter 'latitude' has no value");
				if (longitude==null || longitude.equals("")) 	throw new InvalidParameterException("parameter 'longitude' has no value");
				if (state==null     || state.equals("")) 		throw new InvalidParameterException("parameter 'state' has no value");
				if (strength==null  || strength.equals("")) 	throw new InvalidParameterException("parameter 'strength' has no value");
				
				/****************************/
				/* message validation : end */
				/****************************/
				
				RfidVO rfidVO = new RfidVO();
				
				rfidVO.setRfid(Integer.parseInt(rfid));
				rfidVO.setLatitude(Double.parseDouble(latitude));
				rfidVO.setLongitude(Double.parseDouble(longitude));
				rfidVO.setState(state);
				rfidVO.setStrength(Double.parseDouble(strength));
				
				apiInterfaceService.insertRfid(rfidVO);
			}

			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/rfid/initializeRFID", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// rfid/updateRFID
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/rfid/updateRFID", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonRfidUpdateRFID(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			List<LinkedHashMap<String, Object>> rfidInfoList = (List<LinkedHashMap<String, Object>>) filter.get("rfidInfo2List");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (rfidInfoList==null || rfidInfoList.isEmpty()) throw new InvalidParameterException("parameter 'rfidInfo2List' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String rfid		= null;
			String state	= null;
			String strength	= null;
			
			for (int i=0; i<rfidInfoList.size(); i++) {
				
				LinkedHashMap<String, Object> rfidInfoMap = rfidInfoList.get(i);
				
				rfid		= (String)rfidInfoMap.get("rfid");
				state		= (String)rfidInfoMap.get("state");
				strength	= (String)rfidInfoMap.get("strength");
				
				/******************************/
				/* message validation : start */
				/******************************/
				
				if (rfid==null     || rfid.equals("")) 		throw new InvalidParameterException("parameter 'rfid' has no value");
				if (state==null    || state.equals("")) 	throw new InvalidParameterException("parameter 'state' has no value");
				if (strength==null || strength.equals("")) 	throw new InvalidParameterException("parameter 'strength' has no value");
				
				/****************************/
				/* message validation : end */
				/****************************/
				
				RfidVO rfidVO = new RfidVO();
				
				rfidVO.setRfid(Integer.parseInt(rfid));
				rfidVO.setState(state);
				rfidVO.setStrength(Double.parseDouble(strength));
				
				apiInterfaceService.updateRfid(rfidVO);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/rfid/updateRFID", filterJSON, responseJson);
		
		return responseJson;
	}
	
	// rfid/getRFID
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/rfid/getRFID", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonRfidGetRFID(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String sessionKey = (String)filter.get("sessionKey");
			
			ArrayList<String> rfidList	= (ArrayList<String>)filter.get("rfidList");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (!validateSessionKey(sessionKey)) throw new InvalidSessionKeyException();
			
			if (rfidList!=null && rfidList.isEmpty() ) throw new InvalidParameterException("parameter 'rfidList' has no value");
			
			/****************************/
			/* message validation : end */
			/****************************/

			RfidVO rfidVO = new RfidVO();
			
			if (rfidList != null)
				rfidVO.setRfidCondition(" WHERE rfid IN (" + UsdmUtils.getInOperatorString(rfidList) + ")");
			
			List<?> queryResult = apiInterfaceService.selectRfid(rfidVO);
			
			if (queryResult.isEmpty()) throw new NoResourceException();
			
			List<Map<String, Object>> rfidInfoList = new ArrayList<Map<String, Object>>();

			for (int i=0; i<queryResult.size(); i++) {
				EgovMap queryMap = (EgovMap)queryResult.get(i);
				
				Map<String, Object> rfidInfoMap = new LinkedHashMap<String, Object>();
				
				rfidInfoMap.put("rfid",	 		queryMap.get("rfid").toString());
				rfidInfoMap.put("latitude",		queryMap.get("latitude").toString());
				rfidInfoMap.put("longitude",	queryMap.get("longitude").toString());
				rfidInfoMap.put("state",		queryMap.get("state"));
				rfidInfoMap.put("strength",		queryMap.get("strength").toString());

				rfidInfoList.add(rfidInfoMap);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("rfidInfoList", rfidInfoList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/rfid/getRFID", filterJSON, responseJson);
		
		return responseJson;
	}
	
	public boolean validateSessionKey(String sessionKey) throws Exception {
		if (sessionKey==null || sessionKey.equals(""))
			return false;
		
		LoginSessionVO loginSessionVO = new LoginSessionVO();
		loginSessionVO.setSessionKey(sessionKey);
		
		if (apiInterfaceService.validateSessionKey(loginSessionVO) < 0)
			return false;
		
		return true;
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
		
		if (tempResultMap.get("gid") != null)
			resourceDescription.put("GID", tempResultMap.get("gid").toString());
		else
			resourceDescription.put("GID", null);
		
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
		resourceDescription.put("nickName", 								tempResultMap.get("nickname"));
		
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
		
		if (tempResultMap.get("gid") != null)
			resourceDescription.put("GID", tempResultMap.get("gid").toString());
		else
			resourceDescription.put("GID", null);
		
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
		resourceDescription.put("nickName", 					tempResultMap.get("nickname"));
		
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
		
		if (tempResultMap.get("gid") != null)
			resourceDescription.put("GID", tempResultMap.get("gid").toString());
		else
			resourceDescription.put("GID", null);
		
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
		resourceDescription.put("nickName", 				tempResultMap.get("nickname"));
		
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
		resourceDescription.put("nickName",					tempResultMap.get("nickname"));
		
		description.put("resourceType", "Transducer");
		description.put("description",  resourceDescription);
		
		return description;
	}
	
	public static Map<String, Object> getAccidentDescMap(String accidentDesc) throws InvalidParameterException {
		Map<String, Object> descMap = new LinkedHashMap<String, Object>();
		
		try {
			String[] tuple = accidentDesc.split(",");
			String name;
			String value;
			
			for (int i=0; i<tuple.length; i++) {
				name  = tuple[i].split(":")[0];
				value = tuple[i].split(":")[1];
				
				descMap.put(name, value);
			}
		
		} catch (Exception e) {
			throw new InvalidParameterException();
		}
		
		return descMap;
	}
	
	// query/waterPipe2WGS (테스트용)
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/query/waterPipe2WGS", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQueryWaterPipe2WGS(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			List<?> queryResult = apiInterfaceService.selectWaterPipeGeometry();
			
			EgovMap queryMap = new EgovMap();
			
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);
				
				String tmString = (String)queryMap.get("geom");
				String[] tmArray = tmString.split(",");
				
				double tmX, tmY;
				String longitude, latitude;
				String wgsString = "";
				
				for (int j=0; j<tmArray.length; j++) {
					tmX = Double.parseDouble(tmArray[j].split(" ")[0]);
					tmY = Double.parseDouble(tmArray[j].split(" ")[1]);
					
					latitude  = Double.toString(UsdmUtils.TMNorthToLatitude(tmY, tmX));
					longitude = Double.toString(UsdmUtils.TMEastToLongitude(tmY, tmX));
					
					wgsString += latitude + " " + longitude;
					
					if (j < tmArray.length-1) wgsString += ",";
				}

				String sql = "update sdm_waterpipe set wgscoords='" + wgsString + "' where ftr_idn=" + (int)queryMap.get("id") + ";";
				
				Map<String, Object> result = new LinkedHashMap<String, Object>();
				result.put("sql", sql);
				
				resultList.add(result);
				
				System.out.println(sql);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("resultList",  	resultList);
		
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();

			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
		
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		return responseJson;
	}
	
	// query/drainPipe2WGS (테스트용)
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/query/drainPipe2WGS", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQueryDrainPipe2WGS(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			List<?> queryResult = apiInterfaceService.selectDrainPipeGeometry();
			
			EgovMap queryMap = new EgovMap();
			
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);
				
				String tmString = (String)queryMap.get("geom");
				String[] tmArray = tmString.split(",");
				
				double tmX, tmY;
				String longitude, latitude;
				String wgsString = "";
				
				for (int j=0; j<tmArray.length; j++) {
					tmX = Double.parseDouble(tmArray[j].split(" ")[0]);
					tmY = Double.parseDouble(tmArray[j].split(" ")[1]);
					
					latitude  = Double.toString(UsdmUtils.TMNorthToLatitude(tmY, tmX));
					longitude = Double.toString(UsdmUtils.TMEastToLongitude(tmY, tmX));
					
					wgsString += latitude + " " + longitude;
					
					if (j < tmArray.length-1) wgsString += ",";
				}
				
				String sql = "update sdm_drainpipe set wgscoords='" + wgsString + "' where ftr_idn=" + (int)queryMap.get("id") + ";";
				
				Map<String, Object> result = new LinkedHashMap<String, Object>();
				result.put("sql", sql);
				
				resultList.add(result);
				
				System.out.println(sql);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("resultList",  	resultList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		return responseJson;
	}
	
	// query/drainManhole2WGS (테스트용)
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/query/drainManhole2WGS", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQueryDrainManholeWGS(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			List<?> queryResult = apiInterfaceService.selectDrainManholeGeometry();
			
			EgovMap queryMap = new EgovMap();
			
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			
			for (int i=0; i<queryResult.size(); i++) {
				queryMap = (EgovMap)queryResult.get(i);
				
				String tmString = (String)queryMap.get("geom");
				
				double tmX, tmY;
				String longitude, latitude;
				String wgsString = "";
				
				tmX = Double.parseDouble(tmString.split(" ")[0]);
				tmY = Double.parseDouble(tmString.split(" ")[1]);
					
				latitude  = Double.toString(UsdmUtils.TMNorthToLatitude(tmY, tmX));
				longitude = Double.toString(UsdmUtils.TMEastToLongitude(tmY, tmX));
				
				wgsString += latitude + " " + longitude;
				
				String sql = "update sdm_drainmanhole set wgscoords='" + wgsString + "' where ftr_idn=" + (int)queryMap.get("id") + ";";
				
				Map<String, Object> result = new LinkedHashMap<String, Object>();
				result.put("sql", sql);
				
				resultList.add(result);
				
				System.out.println(sql);
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("resultList",  	resultList);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		return responseJson;
	}
	
	// query/WGS2TM (테스트용)
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/query/WGS2TM", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonQueryConvertCoord(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			List<Map<String,String>> list = (List<Map<String,String>>) filter.get("list");
			Map<String,String> map = new HashMap<String,String>();
			
			for (int i=0; i<list.size(); i++) {
				map = list.get(i);
				
				String ftr_idn   = map.get("ftridn");
				String latitude  = map.get("latitude");
				String longitude = map.get("longitude");
						
				double tmX = UsdmUtils.WGSLongitudeToEast(Double.parseDouble(latitude), Double.parseDouble(longitude));
		        double tmY = UsdmUtils.WGSLatitudeToNorth(Double.parseDouble(latitude), Double.parseDouble(longitude));
		        
		        String sql = "update sdm_drainaccident set coordinate = (st_pointfromtext('POINT(" + tmX + " " + tmY + ")',0)) where pipe_ftr_idn = " + ftr_idn + ";";
		        System.out.println(sql);	
			}
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			
		} catch (InvalidSessionKeyException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (NoResourceException e) {
			e.printStackTrace();
			
			response.setStatus(e.getStatus());
			responseMessage.put("responseCode", e.getCode());
			responseMessage.put("responseMsg",  e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		return responseJson;
	}
	
	// 모든 SRI그리드의 대표SRI값과 등급을 계산하여 update한다
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/query/updateSriGridAll", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonUpdateSriGridAll(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			apiInterfaceService.updateSriGridAll();
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/query/updateSriGridAll", filterJSON, responseJson);
		
		return responseJson;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/test/xml", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonTestXml(@RequestBody String filterJSON, HttpServletResponse response) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		String config = ""; 
		
		try {
			config = EgovProperties.getProperty("log.path");
			
			response.setStatus(SUCCESS_STATUS);
			responseMessage.put("responseCode", SUCCESS_CODE);
			responseMessage.put("responseMsg",  SUCCESS_MSG);
			responseMessage.put("config",  		config);
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response.setStatus(FAILED_STATUS);
			responseMessage.put("responseCode", FAILED_CODE);
			responseMessage.put("responseMsg",  FAILED_MSG + " (" + e.getCause() + ")");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/test/", filterJSON, responseJson);
		
		return responseJson;
	}
	
}