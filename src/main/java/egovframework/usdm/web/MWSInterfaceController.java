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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.usdm.service.*;
import egovframework.usdm.web.util.*;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springmodules.validation.commons.DefaultBeanValidator;

@Controller
public class MWSInterfaceController {

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** MWSInterfaceService */
	@Resource(name = "mwsInterfaceService")
	private MWSInterfaceService mwsInterfaceService;
	
	/** Validator */
	@Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;

	@RequestMapping(value="/connection/login", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonConnectionLogin(@RequestBody String filterJSON) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		int result = 0;
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String userID 		= (String)filter.get("userID");
			String userPW 		= (String)filter.get("userPW");
			String sessionKey 	= "";
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (userID.equals(""))	throw new Exception(); 
			if (userPW.equals(""))	throw new Exception(); 
			
			/****************************/
			/* message validation : end */
			/****************************/
			LoginSessionVO loginSessionVO = new LoginSessionVO();
			loginSessionVO.setUserID(userID);
			loginSessionVO.setUserPW(userPW);
			
			result = mwsInterfaceService.selectLoginSession(loginSessionVO);
			
			switch (result) {
			// 정상적으로 로그인 (0)
			case 0:
				sessionKey = UsdmUtils.getRandomString();
				
				loginSessionVO.setSessionKey(sessionKey);
				mwsInterfaceService.updateSessionKeyLogin(loginSessionVO);
				
				responseMessage.put("sessionKey", sessionKey);
				responseMessage.put("responseCode", "100");
				responseMessage.put("responseMsg",  "Connection Success");
				
				break;

			// 존재하지 않는 사용자 (-1)
			case -1:
				responseMessage.put("responseCode", "102");
				responseMessage.put("responseMsg",  "Connection Error: Unregistered userID");
				
				break;
				
			// 패스워드 불일치 (1)
			case 1:
				responseMessage.put("responseCode", "103");
				responseMessage.put("responseMsg",  "Connection Error: Incorrect Password");
				
				break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "101");
			responseMessage.put("responseMsg",  "Connection Error: Incorrect Request");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/connection/login", filterJSON, responseJson);
		
		return responseJson;
	}
	
	@RequestMapping(value="/connection/logout", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonConnectionLogout(@RequestBody String filterJSON) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		int result = 0;
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String userID 		= (String)filter.get("userID");
			String sessionKey 	= (String)filter.get("sessionKey");
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (userID.equals(""))		throw new Exception(); 
			if (sessionKey.equals(""))	throw new Exception(); 
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			LoginSessionVO loginSessionVO = new LoginSessionVO();
			loginSessionVO.setUserID(userID);
			loginSessionVO.setSessionKey(sessionKey);
			
			result = mwsInterfaceService.selectLogoutSession(loginSessionVO);
			
			switch (result) {
			// 정상적으로 로그아웃 (0)
			case 0:
				mwsInterfaceService.updateSessionKeyLogout(loginSessionVO);
				
				responseMessage.put("responseCode", "100");
				responseMessage.put("responseMsg",  "Connection Success");
				
				break;

			// 로그인하지 않은 사용자 (-1)
			case -1:
				responseMessage.put("responseCode", "105");
				responseMessage.put("responseMsg",  "Connection Error: Unlogged UserID");
				
				break;
				
			// 세션키 불일치 (1)
			case 1:
				responseMessage.put("responseCode", "104");
				responseMessage.put("responseMsg",  "Connection Error: Invalid SessionKey");
				
				break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "101");
			responseMessage.put("responseMsg",  "Connection Error: Incorrect Request");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/connection/logout", filterJSON, responseJson);
		
		return responseJson;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/resource/gateway", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonResourceGateway(@RequestBody String filterJSON) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
        try {
        	// convert JSON to map
            Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
            
            // read JSON contents from map
            String 	gwID 				= (String)filter.get("gwID");
            String 	url 				= (String)filter.get("url");
            String 	manufacturer 		= (String)filter.get("manufacturer");
            String 	productNo 			= (String)filter.get("productNo");
            String 	location 			= (String)filter.get("location");
            
            LinkedHashMap<String, Object> coordinate = (LinkedHashMap<String, Object>)filter.get("coordinate");
			
            String 	longitude = (String)coordinate.get("longitude");
			String 	latitude  = (String)coordinate.get("latitude");
    		String 	altitude  = (String)coordinate.get("altitude");
			
            long	dateTime 			= (long)filter.get("dateTime");
            
            ArrayList<String> supportedTransportProtocolListArray 			= (ArrayList<String>)filter.get("supportedTransportProtocolList");
			ArrayList<String> supportedTransportConnectionControlListArray	= (ArrayList<String>)filter.get("supportedTransportConnectionControlList");
			ArrayList<String> supportedTransportDirectionListArray 			= (ArrayList<String>)filter.get("supportedTransportDirectionList");
			ArrayList<String> supportedOperationListArray 					= (ArrayList<String>)filter.get("supportedOpereationList");
			ArrayList<String> supportedAttributeListArray 					= (ArrayList<String>)filter.get("supportedAttributeList");
			ArrayList<String> supportedSensorServiceCenterListArray 		= (ArrayList<String>)filter.get("supportedSensorServiceCenterList");
			ArrayList<String> panIDListArray 								= (ArrayList<String>)filter.get("panIDList");
			
			String supportedTransportProtocolList 			= "";
			String supportedTransportConnectionControlList 	= "";
			String supportedTransportDirectionList 			= "";
			String supportedOperationList 					= "";
			String supportedAttributeList 					= "";
			String supportedSensorServiceCenterList 		= "";
			String panIDList 								= "";
			
			if (supportedTransportProtocolListArray!= null) 			supportedTransportProtocolList 				= String.join(",", supportedTransportProtocolListArray);
			if (supportedTransportConnectionControlListArray != null)	supportedTransportConnectionControlList 	= String.join(",", supportedTransportConnectionControlListArray);
			if (supportedTransportDirectionListArray != null) 			supportedTransportDirectionList 			= String.join(",", supportedTransportDirectionListArray);
			if (supportedOperationListArray != null) 					supportedOperationList 						= String.join(",", supportedOperationListArray);
			if (supportedAttributeListArray != null) 					supportedAttributeList 						= String.join(",", supportedAttributeListArray);
			if (supportedSensorServiceCenterListArray != null) 			supportedSensorServiceCenterList 			= String.join(",", supportedSensorServiceCenterListArray);
			if (panIDListArray != null) 								panIDList 									= String.join(",", panIDListArray);
			
            String 	monitoringMode		= (String)filter.get("monitoringMode");
            int		monitoringPeriod 	= (int)filter.get("monitoringPeriod");
            
            /******************************/
            /* message validation : start */
            /******************************/
            
            if (gwID.equals(""))	throw new Exception();
            
            /****************************/
            /* message validation : end */
            /****************************/
            
            SNGatewayVO gatewayVO = new SNGatewayVO();
            
            gatewayVO.setGwID(gwID);
            gatewayVO.setUrl(url);
            gatewayVO.setManufacturer(manufacturer);
            gatewayVO.setProductNo(productNo);
            gatewayVO.setLocation(location);
            gatewayVO.setLongitude(longitude);
            gatewayVO.setLatitude(latitude);
            gatewayVO.setAltitude(altitude);
            gatewayVO.setDateTime(dateTime);
            gatewayVO.setSupportedTransportProtocolList(supportedTransportProtocolList);
            gatewayVO.setSupportedTransportConnectionControlList(supportedTransportConnectionControlList);
            gatewayVO.setSupportedTransportDirectionList(supportedTransportDirectionList);
            gatewayVO.setSupportedOperationList(supportedOperationList);
            gatewayVO.setSupportedAttributeList(supportedAttributeList);
            gatewayVO.setSupportedSensorServiceCenterList(supportedSensorServiceCenterList);
            gatewayVO.setPanIDList(panIDList);
            gatewayVO.setMonitoringMode(monitoringMode);
            gatewayVO.setMonitoringPeriod(monitoringPeriod);
            gatewayVO.setStatusCode("NORMAL");		// statusCode : 최초등록시 상태는 '정상'
            gatewayVO.setDeleteCode(0);				// deleteCode : 최초등록시 삭제여부는 'N'
            
            mwsInterfaceService.insertGateway(gatewayVO);
            
            responseMessage.put("responseCode", "500");
            responseMessage.put("responseMsg",  "Resource Message Success");
            
        } catch (Exception e) {
            e.printStackTrace();
            
            responseMessage.put("responseCode", "501");
            responseMessage.put("responseMsg",  "Resource Message Error: Incorrect Description");
        }
        
        responseJson = UsdmUtils.convertObjToJson(responseMessage);
        UsdmUtils.writeLog("/resource/gateway", filterJSON, responseJson);

        return responseJson;
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/resource/pan", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonResourcePan(@RequestBody String filterJSON) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String 	gwID 				= (String)filter.get("gwID");
			String 	panID 				= (String)filter.get("panID");
			String 	topology 			= (String)filter.get("topology");
			String 	protocolStack 		= (String)filter.get("protocolStack");
			int		panChannel 			= (int)filter.get("panChannel");
			
			ArrayList<String> supportedChannelListArray 		= (ArrayList<String>)filter.get("supportedChannelList");
			ArrayList<String> supportedTopologyListArray 		= (ArrayList<String>)filter.get("supportedTopologyList");
			ArrayList<String> supportedProtocolStackListArray 	= (ArrayList<String>)filter.get("supportedProtocolStackList");
			ArrayList<String> supportedOperationListArray 		= (ArrayList<String>)filter.get("supportedOpereationList");
			ArrayList<String> supportedAttributeListArray 		= (ArrayList<String>)filter.get("supportedAttributeList");
			ArrayList<String> snIDListArray 					= (ArrayList<String>)filter.get("snIDList");
			
			String supportedChannelList 		= "";
			String supportedTopologyList 		= "";
			String supportedProtocolStackList 	= "";
			String supportedOperationList 		= "";
			String supportedAttributeList 		= "";
			String snIDList 					= "";
			
			if (supportedChannelListArray != null) 		 supportedChannelList 		= String.join(",", supportedChannelListArray);
			if (supportedTopologyListArray != null) 	 supportedTopologyList 		= String.join(",", supportedTopologyListArray);
			if (supportedProtocolStackListArray != null) supportedProtocolStackList = String.join(",", supportedProtocolStackListArray);
			if (supportedOperationListArray != null) 	 supportedOperationList 	= String.join(",", supportedOperationListArray);
			if (supportedAttributeListArray != null) 	 supportedAttributeList 	= String.join(",", supportedAttributeListArray);
			if (snIDListArray != null) 					 snIDList 					= String.join(",", snIDListArray);
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (gwID.equals(""))	throw new Exception();
			if (panID.equals(""))	throw new Exception();
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			SNPanVO panVO = new SNPanVO();
			
			panVO.setGwID(gwID);
			panVO.setPanID(panID);
			panVO.setTopology(topology);
			panVO.setProtocolStack(protocolStack);
			panVO.setPanChannel(panChannel);
			panVO.setSupportedChannelList(supportedChannelList);
			panVO.setSupportedTopologyList(supportedTopologyList);
			panVO.setSupportedProtocolStackList(supportedProtocolStackList);
			panVO.setSupportedOperationList(supportedOperationList);
			panVO.setSupportedAttributeList(supportedAttributeList);
			panVO.setSnIDList(snIDList);
			panVO.setStatusCode("NORMAL");		// statusCode : 최초등록시 상태는 '정상'
			panVO.setDeleteCode(0);				// deleteCode : 최초등록시 삭제여부는 'N'
			
			mwsInterfaceService.insertPan(panVO);
			
			responseMessage.put("responseCode", "500");
			responseMessage.put("responseMsg",  "Resource Message Success");
			
		} catch (Exception e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "501");
			responseMessage.put("responseMsg",  "Resource Message Error: Incorrect Description");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/resource/pan", filterJSON, responseJson);
		
		return responseJson;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/resource/node", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonResourceNode(@RequestBody String filterJSON) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String 	gwID 			= (String)filter.get("gwID");
			String 	panID 			= (String)filter.get("panID");
			String 	snID			= (String)filter.get("snID");
			String 	globalID 		= (String)filter.get("globalID");
			String 	manufacturer	= (String)filter.get("manufacturer");
			String 	productNo 		= (String)filter.get("productNo");
			String 	location 		= (String)filter.get("location");
			
			LinkedHashMap<String, Object> coordinate = (LinkedHashMap<String, Object>)filter.get("coordinate");
			
			String 	longitude = (String)coordinate.get("longitude");
			String 	latitude  = (String)coordinate.get("latitude");
    		String 	altitude  = (String)coordinate.get("altitude");
    		
    		String	role 				= (String)filter.get("role");
			String	monitoringMode 		= (String)filter.get("monitoringMode");
			int		monitoringPeriod	= (int)filter.get("monitoringPeriod");

			ArrayList<String> roleListArray 				= (ArrayList<String>)filter.get("roleList");
			ArrayList<String> parentNodeIDListArray 		= (ArrayList<String>)filter.get("parentNodeIDList");
			ArrayList<String> supportedOperationListArray 	= (ArrayList<String>)filter.get("supportedOpereationList");
			ArrayList<String> supportedAttributeListArray 	= (ArrayList<String>)filter.get("supportedAttributeList");
			ArrayList<String> tdIDListArray 				= (ArrayList<String>)filter.get("tdIDList");
			
			String roleList 				= "";
			String parentNodeIDList 		= "";
			String supportedOperationList 	= "";
			String supportedAttributeList 	= "";
			String tdIDList 				= "";
			
			if (roleListArray != null) 					roleList 				= String.join(",", roleListArray);
			if (parentNodeIDListArray != null) 			parentNodeIDList 		= String.join(",", parentNodeIDListArray);
			if (supportedOperationListArray != null) 	supportedOperationList 	= String.join(",", supportedOperationListArray);
			if (supportedAttributeListArray != null) 	supportedAttributeList 	= String.join(",", supportedAttributeListArray);
			if (tdIDListArray != null) 					tdIDList 				= String.join(",", tdIDListArray);
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (gwID.equals(""))	throw new Exception();
			if (panID.equals(""))	throw new Exception();
			if (snID.equals(""))	throw new Exception();
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			SNNodeVO nodeVO = new SNNodeVO();
			
			nodeVO.setGwID(gwID);
			nodeVO.setPanID(panID);
			nodeVO.setSnID(snID);
			nodeVO.setGlobalID(globalID);
			nodeVO.setManufacturer(manufacturer);
			nodeVO.setProductNo(productNo);
			nodeVO.setLocation(location);
			nodeVO.setLongitude(longitude);
			nodeVO.setLatitude(latitude);
			nodeVO.setAltitude(altitude);
			nodeVO.setRole(role);
			nodeVO.setRoleList(roleList);
			nodeVO.setParentNodeIDList(parentNodeIDList);
			nodeVO.setSupportedOperationList(supportedOperationList);
			nodeVO.setSupportedAttributeList(supportedAttributeList);
			nodeVO.setTdIDList(tdIDList);
			nodeVO.setMonitoringMode(monitoringMode);
			nodeVO.setMonitoringPeriod(monitoringPeriod);
			nodeVO.setStatusCode("NORMAL");		// statusCode : 최초등록시 상태는 '정상'
			nodeVO.setDeleteCode(0);			// deleteCode : 최초등록시 삭제여부는 'N'
			
			mwsInterfaceService.insertNode(nodeVO);
			
			responseMessage.put("responseCode", "500");
			responseMessage.put("responseMsg",  "Resource Message Success");
			
		} catch (Exception e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "501");
			responseMessage.put("responseMsg",  "Resource Message Error: Incorrect Description");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/resource/node", filterJSON, responseJson);
		
		return responseJson;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/resource/transducer", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonResourceTransducer(@RequestBody String filterJSON) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String 	gwID 				= (String)filter.get("gwID");
			String 	panID 				= (String)filter.get("panID");
			String 	snID				= (String)filter.get("snID");
			String 	tdID				= (String)filter.get("tdID");
			String 	manufacturer 		= (String)filter.get("manufacturer");
			String 	productNo 			= (String)filter.get("productNo");
			String 	transducerCategory 	= (String)filter.get("transducerCategory");
			String 	transducerType 		= (String)filter.get("transducerType");
			String	unit 				= (String)filter.get("unit");
			String	dataType 			= (String)filter.get("dataType");
			
			LinkedHashMap<String, Object> range = (LinkedHashMap<String, Object>)filter.get("range");
			
            double 	rangeMin 	= (double)range.get("min");
    		double 	rangeMax 	= (double)range.get("max");
    		double 	rangeOffset = (double)range.get("offset");
    		
    		ArrayList<String> levelArray 					= (ArrayList<String>)filter.get("level");
    		ArrayList<String> supportedOperationListArray 	= (ArrayList<String>)filter.get("supportedOpereationList");
			ArrayList<String> supportedAttributeListArray 	= (ArrayList<String>)filter.get("supportedAttributeList");
			
			String level = "";
			String supportedOperationList 	= "";
			String supportedAttributeList 	= "";
			
			if (levelArray != null) level 	= String.join(",", levelArray);
			if (supportedOperationListArray != null) supportedOperationList 	= String.join(",", supportedOperationListArray);
			if (supportedAttributeListArray != null) supportedAttributeList 	= String.join(",", supportedAttributeListArray);
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (gwID.equals(""))				throw new Exception();
			if (panID.equals(""))				throw new Exception();
			if (snID.equals(""))				throw new Exception();
			if (tdID.equals(""))				throw new Exception();
			if (transducerCategory.equals(""))	throw new Exception();
			if (transducerType.equals(""))		throw new Exception();
			if (unit.equals(""))				throw new Exception();
			if (dataType.equals(""))			throw new Exception();
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			SNTransducerVO transducerVO = new SNTransducerVO();
			
			transducerVO.setGwID(gwID);
			transducerVO.setPanID(panID);
			transducerVO.setSnID(snID);
			transducerVO.setTdID(tdID);
			transducerVO.setManufacturer(manufacturer);
			transducerVO.setProductNo(productNo);
			transducerVO.setTransducerCategory(transducerCategory);
			transducerVO.setTransducerType(transducerType);
			transducerVO.setUnit(unit);
			transducerVO.setDataType(dataType);
			transducerVO.setRangeMin(rangeMin);
			transducerVO.setRangeMax(rangeMax);
			transducerVO.setRangeOffset(rangeOffset);
			transducerVO.setLevel(level);
			transducerVO.setSupportedOperationList(supportedOperationList);
			transducerVO.setSupportedAttributeList(supportedAttributeList);
			transducerVO.setStatusCode("NORMAL");	// statusCode : 최초등록시 상태는 '정상'
			transducerVO.setActuationResult("ON");	// actuationResult : 최초등록시 'ON'
			transducerVO.setDeleteCode(0);			// deleteCode : 최초등록시 삭제여부는 'N'
			
			mwsInterfaceService.insertTransducer(transducerVO);
			
			responseMessage.put("responseCode", "500");
			responseMessage.put("responseMsg",  "Resource Message Success");
			
		} catch (Exception e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "501");
			responseMessage.put("responseMsg",  "Resource Message Error: Incorrect Description");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/resource/transducer", filterJSON, responseJson);
		
		return responseJson;
	}
	
	@RequestMapping(value="/resource/update", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonResourceUpdate(@RequestBody String filterJSON) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		int result = 0;
		
        try {
        	// convert JSON to map
            Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
            
            // read JSON contents from map
            String		oldAddress	= (String)filter.get("oldAddress");
            String 		newAddress	= (String)filter.get("newAddress");
            
            String 		gwID		= UsdmUtils.getGwIDFromAddress(oldAddress);
            String 		panID		= UsdmUtils.getPanIDFromAddress(oldAddress);
            String 		snID 		= UsdmUtils.getSnIDFromAddress(oldAddress);
            String 		tdID 		= UsdmUtils.getTdIDFromAddress(oldAddress);
            
            String 		newGwID 	= UsdmUtils.getGwIDFromAddress(newAddress);
            String 		newPanID 	= UsdmUtils.getPanIDFromAddress(newAddress);
            String 		newSnID 	= UsdmUtils.getSnIDFromAddress(newAddress);
            String 		newTdID 	= UsdmUtils.getTdIDFromAddress(newAddress);
            
            /******************************/
            /* message validation : start */
            /******************************/
            
            if (oldAddress.equals(""))	throw new Exception();
            if (newAddress.equals(""))	throw new Exception();
            	
            if (gwID.equals("") 	|| newGwID.equals(""))		throw new Exception();
            if (panID.equals("") 	&& !newPanID.equals(""))	throw new Exception();
            if (snID.equals("") 	&& !newSnID.equals(""))		throw new Exception();
            if (tdID.equals("") 	&& !newTdID.equals(""))		throw new Exception();
            if (newPanID.equals("") && !panID.equals(""))		throw new Exception();
            if (newSnID.equals("")  && !snID.equals(""))		throw new Exception();
            if (newTdID.equals("")  && !tdID.equals(""))		throw new Exception();
            	
            /****************************/
            /* message validation : end */
            /****************************/
            
            // gateway address 변경
            if (panID.equals("")) {
            	SNGatewayVO gatewayVO = new SNGatewayVO();
            	
            	gatewayVO.setGwID(gwID);
            	gatewayVO.setNewGwID(newGwID);
            	
            	result = mwsInterfaceService.updateGatewayAddress(gatewayVO);
            	
            }
            // pan address 변경
            else if (snID.equals("")) {
            	SNPanVO panVO = new SNPanVO();
            	
            	panVO.setGwID(gwID);
            	panVO.setPanID(panID);
            	panVO.setNewGwID(newGwID);
            	panVO.setNewPanID(newPanID);
            	
            	result = mwsInterfaceService.updatePanAddress(panVO);
            }
            // node address 변경
            else if (tdID.equals("")) {
            	SNNodeVO nodeVO = new SNNodeVO();
            	
            	nodeVO.setGwID(gwID);
            	nodeVO.setPanID(panID);
            	nodeVO.setSnID(snID);
            	nodeVO.setNewGwID(newGwID);
            	nodeVO.setNewPanID(newPanID);
            	nodeVO.setNewSnID(newSnID);
            	
            	result = mwsInterfaceService.updateNodeAddress(nodeVO);
            }
            // transducer address 변경
            else {
            	SNTransducerVO transducerVO = new SNTransducerVO();
            	
            	transducerVO.setGwID(gwID);
            	transducerVO.setPanID(panID);
            	transducerVO.setSnID(snID);
            	transducerVO.setTdID(tdID);
            	transducerVO.setNewGwID(newGwID);
            	transducerVO.setNewPanID(newPanID);
            	transducerVO.setNewSnID(newSnID);
            	transducerVO.setNewTdID(newTdID);
            	
            	result = mwsInterfaceService.updateTransducerAddress(transducerVO);
            }
            
            if (result < 0) {
            	responseMessage.put("responseCode", "502");
            	responseMessage.put("responseMsg",  "Resource Message Error: Non-existent resource");
            }
            else {
            	responseMessage.put("responseCode", "500");
            	responseMessage.put("responseMsg",  "Resource Message Success");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            
            responseMessage.put("responseCode", "501");
            responseMessage.put("responseMsg",  "Resource Message Error: Incorrect Description");
        }
        
        responseJson = UsdmUtils.convertObjToJson(responseMessage);
        UsdmUtils.writeLog("/resource/update", filterJSON, responseJson);
        
        return responseJson;
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/resource/delete", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonResourceDelete(@RequestBody String filterJSON) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		int result = 0;
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			ArrayList<String> addressList = (ArrayList<String>)filter.get("addressList");

			/******************************/
			/* message validation : start */
			/******************************/
			
			if (addressList.isEmpty())	throw new Exception();
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String gwID 	= null;
			String panID 	= null;
			String snID 	= null;
			String tdID		= null;
			
			for (int i=0; i<addressList.size(); i++) {
				gwID	= UsdmUtils.getGwIDFromAddress(addressList.get(i));
	            panID	= UsdmUtils.getPanIDFromAddress(addressList.get(i));
	            snID 	= UsdmUtils.getSnIDFromAddress(addressList.get(i));
	            tdID 	= UsdmUtils.getTdIDFromAddress(addressList.get(i));
				
				if (gwID.equals("")) throw new Exception();
				
				// gateway 삭제
				if (panID.equals("")) {
					SNGatewayVO gatewayVO = new SNGatewayVO();
					gatewayVO.setGwID(gwID);
					
					//result = mwsInterfaceService.deleteGateway(gatewayVO);
					result = mwsInterfaceService.deleteGatewayDescent(gatewayVO);
				}
				/*
				// gateway 삭제(하위포함)
				else if (panID.equals("FF")) {
					SNGatewayVO gatewayVO = new SNGatewayVO();
					gatewayVO.setGwID(gwID);
					
					result = mwsInterfaceService.deleteGatewayDescent(gatewayVO);
				}
				*/
				// pan 삭제
				else if (snID.equals("")) {
					SNPanVO panVO = new SNPanVO();
					
					panVO.setGwID(gwID);
					panVO.setPanID(panID);
					
					//result = mwsInterfaceService.deletePan(panVO);
					result = mwsInterfaceService.deletePanDescent(panVO);
				}
				/*
				// pan 삭제(하위포함)
				else if (snID.equals("FF")) {
					SNPanVO panVO = new SNPanVO();
					
					panVO.setGwID(gwID);
					panVO.setPanID(panID);
					
					result = mwsInterfaceService.deletePanDescent(panVO);
				}
				*/
				// node 삭제
				else if (tdID.equals("")) {
					SNNodeVO nodeVO = new SNNodeVO();
					
					nodeVO.setGwID(gwID);
					nodeVO.setPanID(panID);
					nodeVO.setSnID(snID);
					
					//result = mwsInterfaceService.deleteNode(nodeVO);
					result = mwsInterfaceService.deleteNodeDescent(nodeVO);
				}
				/*
				// node 삭제(하위포함)
				else if (tdID.equals("FF")) {
					SNNodeVO nodeVO = new SNNodeVO();
					
					nodeVO.setGwID(gwID);
					nodeVO.setPanID(panID);
					nodeVO.setSnID(snID);
					
					result = mwsInterfaceService.deleteNodeDescent(nodeVO);
				}
				*/
				// transducer 삭제
				else {
					SNTransducerVO transducerVO = new SNTransducerVO();
					
					transducerVO.setGwID(gwID);
					transducerVO.setPanID(panID);
					transducerVO.setSnID(snID);
					transducerVO.setTdID(tdID);
					
					result = mwsInterfaceService.deleteTransducer(transducerVO);
				}
			}
			
			if (result < 0) {
				responseMessage.put("responseCode", "502");
				responseMessage.put("responseMsg",  "Resource Message Error: Non-existent resource");
			}
			else {
				responseMessage.put("responseCode", "500");
				responseMessage.put("responseMsg",  "Resource Message Success");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "501");
			responseMessage.put("responseMsg",  "Resource Message Error: Incorrect Description");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/resource/delete", filterJSON, responseJson);
		
		return responseJson;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/report/sensingValue", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonReportSensingValue(@RequestBody String filterJSON) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		int result = 0;
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			//long reqID = (long)filter.get("reqID");
			
			ArrayList<Map<String, Object>> sensingNodeList  = (ArrayList<Map<String, Object>>)filter.get("sensingNodeList");
			Map<String, Object> sensingNodeMap  = new HashMap<String, Object>();
			String 	snAddress		= "";
			String 	gwID			= "";
			String 	panID			= "";
			String 	snID			= "";
			long 	reportTime		= 0;
			
			ArrayList<Map<String, Object>> sensingValueList = new ArrayList<Map<String, Object>>();
			Map<String, Object> sensingValueMap = new HashMap<String, Object>();
			String 	tdID			= "";
			long 	sensingTime  	= 0;
			String 	sensorType 		= null;
			double 	sensingValue 	= 0;			

			/******************************/
			/* message validation : start */
			/******************************/
			
			if (sensingNodeList.isEmpty())	throw new Exception();
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			for (int i=0; i<sensingNodeList.size(); i++) {
				sensingNodeMap = (HashMap<String, Object>)sensingNodeList.get(i);
				
				snAddress	= (String)sensingNodeMap.get("snAddress");
				gwID		= UsdmUtils.getGwIDFromAddress(snAddress);
				panID		= UsdmUtils.getPanIDFromAddress(snAddress);
				snID		= UsdmUtils.getSnIDFromAddress(snAddress);
				reportTime	= (long)sensingNodeMap.get("reportTime");
				
				sensingValueList = (ArrayList<Map<String, Object>>)sensingNodeMap.get("sensingValueList");
				
				/******************************/
				/* message validation : start */
				/******************************/
				
				if (gwID.equals(""))		throw new Exception();
				if (panID.equals(""))		throw new Exception();
				if (snID.equals(""))		throw new Exception();
				
				/****************************/
				/* message validation : end */
				/****************************/
				
				for (int j=0; j<sensingValueList.size(); j++) {
					sensingValueMap = (HashMap<String, Object>)sensingValueList.get(j);
					
					tdID 			= (String)sensingValueMap.get("tdID");
					sensingTime 	= (long)sensingValueMap.get("sensingTime");
					sensorType 		= (String)sensingValueMap.get("sensorType");
					sensingValue	= Double.parseDouble((String)sensingValueMap.get("sensingValue"));
					
		            /******************************/
					/* message validation : start */
					/******************************/
					
					if (tdID.equals(""))		throw new Exception();
					if (sensorType.equals(""))	throw new Exception();
					
					/****************************/
					/* message validation : end */
					/****************************/
					
					SNSensingValueVO sensingValueVO = new SNSensingValueVO();
					
					sensingValueVO.setGwID(gwID);
					sensingValueVO.setPanID(panID);
					sensingValueVO.setSnID(snID);
					sensingValueVO.setTdID(tdID);
					sensingValueVO.setReportTime(reportTime);
					sensingValueVO.setSensingTime(sensingTime);
					sensingValueVO.setSensorType(sensorType);
					sensingValueVO.setSensingValue(sensingValue);
						
					result = mwsInterfaceService.insertSensingValue(sensingValueVO);
					
					/*
					if (result < 0) throw new Exception();
					*/
				}
			}
			
			responseMessage.put("responseCode", "600");
			responseMessage.put("responseMsg",  "Report Success");
			
		} catch (Exception e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "601");
			responseMessage.put("responseMsg",  "Report Error: Incorrect Message");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/report/sensingValue", filterJSON, responseJson);
		
		return responseJson;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/report/actuationResult", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonReportActuationResult(@RequestBody String filterJSON) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			//long reqID = (long)filter.get("reqID");
			ArrayList<Map<String, Object>> valueList = (ArrayList<Map<String, Object>>)filter.get("actuationValueList");
			
			Map<String, Object> valueMap = new HashMap<String, Object>();
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (valueList.isEmpty())	throw new Exception();
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			String tdAddress		= "";
			String gwID 			= "";
			String panID 			= "";
			String snID 			= "";
			String tdID				= "";
			String actuationResult	= "";
			
			for (int i=0; i<valueList.size(); i++) {
				valueMap = (HashMap<String, Object>)valueList.get(i);
				
				tdAddress 		= (String)valueMap.get("tdAddress");
				actuationResult = (String)valueMap.get("actuationResult");
				
				gwID	= UsdmUtils.getGwIDFromAddress(tdAddress);
				panID	= UsdmUtils.getPanIDFromAddress(tdAddress);
				snID 	= UsdmUtils.getSnIDFromAddress(tdAddress);
				tdID 	= UsdmUtils.getTdIDFromAddress(tdAddress);
				
				/******************************/
				/* message validation : start */
				/******************************/
				
				if (tdAddress.equals(""))		throw new Exception();
				if (gwID.equals(""))			throw new Exception();
				if (panID.equals(""))			throw new Exception();
				if (snID.equals(""))			throw new Exception();
				if (tdID.equals(""))			throw new Exception();
				if (actuationResult.equals(""))	throw new Exception();
				
				/****************************/
				/* message validation : end */
				/****************************/
				
				SNTransducerVO transducerVO = new SNTransducerVO();
				
				transducerVO.setGwID(gwID);
				transducerVO.setPanID(panID);
				transducerVO.setSnID(snID);
				transducerVO.setTdID(tdID);
				transducerVO.setActuationResult(actuationResult);
				
				mwsInterfaceService.updateActuationResult(transducerVO);
			}
			
			responseMessage.put("responseCode", "600");
			responseMessage.put("responseMsg",  "Report Success");
			
		} catch (Exception e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "601");
			responseMessage.put("responseMsg",  "Report Error: Incorrect Message");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/report/actuationResult", filterJSON, responseJson);
		
		return responseJson;
	}
	
	@RequestMapping(value="/notice/statusResource", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonNoticeStatusResource(@RequestBody String filterJSON) {
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		String responseJson = "";
		int result = 0;
		
		try {
			// convert JSON to map
			Map<String, Object> filter = UsdmUtils.convertJsonToMap(filterJSON);
			
			// read JSON contents from map
			String targetAddress 	= (String)filter.get("targetAddress");
			String statusCode 		= (String)filter.get("statusCode");
			
			String gwID 	= UsdmUtils.getGwIDFromAddress(targetAddress);
			String panID 	= UsdmUtils.getPanIDFromAddress(targetAddress);
			String snID 	= UsdmUtils.getSnIDFromAddress(targetAddress);
			String tdID		= UsdmUtils.getTdIDFromAddress(targetAddress);
			
			/******************************/
			/* message validation : start */
			/******************************/
			
			if (targetAddress.equals(""))	throw new Exception();
			if (statusCode.equals(""))		throw new Exception();
			
			/****************************/
			/* message validation : end */
			/****************************/
			
			// gateway status 변경
			if (panID.equals("")) {
				SNGatewayVO gatewayVO = new SNGatewayVO();
				
				gatewayVO.setGwID(gwID);
				gatewayVO.setStatusCode(statusCode);
				
				result = mwsInterfaceService.updateGatewayStatus(gatewayVO);
				
			}
			// pan status 변경
			else if (snID.equals("")) {
				SNPanVO panVO = new SNPanVO();
				
				panVO.setGwID(gwID);
				panVO.setPanID(panID);
				panVO.setStatusCode(statusCode);
				
				result = mwsInterfaceService.updatePanStatus(panVO);
			}
			// node status 변경
			else if (tdID.equals("")) {
				SNNodeVO nodeVO = new SNNodeVO();
				
				nodeVO.setGwID(gwID);
				nodeVO.setPanID(panID);
				nodeVO.setSnID(snID);
				nodeVO.setStatusCode(statusCode);
				
				result = mwsInterfaceService.updateNodeStatus(nodeVO);
			}
			// transducer status 변경
			else {
				SNTransducerVO transducerVO = new SNTransducerVO();
				
				transducerVO.setGwID(gwID);
				transducerVO.setPanID(panID);
				transducerVO.setSnID(snID);
				transducerVO.setTdID(tdID);
				transducerVO.setStatusCode(statusCode);
				
				result = mwsInterfaceService.updateTransducerStatus(transducerVO);
			}
			
			if (result < 0)
				throw new Exception();
			
			responseMessage.put("responseCode", "700");
			responseMessage.put("responseMsg",  "Notice Success");
			
		} catch (Exception e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "701");
			responseMessage.put("responseMsg",  "Notice Error: Incorrect Message");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/notice/statusResource", filterJSON, responseJson);
		
		return responseJson;
	}
	
	@RequestMapping(value="/notice/reboot", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String jsonNoticeReboot(@RequestBody String filterJSON) {
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
			
			if (mwsInterfaceService.validateSessionKey(loginSessionVO) < 0) {
				throw new LoginSessionException();
			}
			*/
			
			responseMessage.put("responseCode", "700");
			responseMessage.put("responseMsg",  "Notice Success");
			
		} catch (LoginSessionException e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "703");
			responseMessage.put("responseMsg",  "Notice Error: Invalid SessionKey");
		
		} catch (Exception e) {
			e.printStackTrace();
			
			responseMessage.put("responseCode", "701");
			responseMessage.put("responseMsg",  "Notice Error: Incorrect Message");
		}
		
		responseJson = UsdmUtils.convertObjToJson(responseMessage);
		UsdmUtils.writeLog("/notice/reboot", filterJSON, responseJson);
		
		return responseJson;
	}
	
}