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
package egovframework.usdm.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;
//import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.usdm.service.*;
import egovframework.usdm.web.util.InvalidParameterException;
import egovframework.usdm.web.util.NoResourceException;
import egovframework.usdm.web.util.UsdmUtils;

import org.springframework.stereotype.Repository;

@Repository("apiInterfaceDAO")
public class APIInterfaceDAO extends EgovAbstractDAO {

	public int validateSessionKey(LoginSessionVO vo) throws Exception {
		/*
		List<EgovMap> existYn = (List<EgovMap>) list("apiInterfaceDAO.validateSessionKey", vo);
		
		if (existYn.get(0).get("validyn").equals("N"))
			return -1;
		
		return 0;
		*/
		
		return 1;
	}
	
	// query/latestValue
	public List<?> selectSensingValueQuery(SNSensingValueVO vo) throws Exception {
		return list("apiInterfaceDAO.selectSensingValueQuery", vo);
	}
	public List<?> selectSensingValueLatest(SNSensingValueVO vo) throws Exception {
		return list("apiInterfaceDAO.selectSensingValueLatest", vo);
	}
	
	// query/latestValueByID(2)
	public List<?> selectSensingValueQueryByID(SNSensingValueVO vo) throws Exception {
		return list("apiInterfaceDAO.selectSensingValueQueryByID", vo);
	}
	public List<?> selectSensingValueLatestByID(SNSensingValueVO vo) throws Exception {
		return list("apiInterfaceDAO.selectSensingValueLatestByID", vo);
	}
	
	// query/spatioTemporal
	// query/spatioTemporalXY(2)
	public List<?> selectSensingValueSpatioTemporal(SNSensingValueVO vo) throws Exception {
		return list("apiInterfaceDAO.selectSensingValueSpatioTemporal", vo);
	}
	
	// query/spatioTemporalByID(2)
	// query/spatioTemporalXYByID(2)
	public List<?> selectSensingValueSpatioTemporalByID(SNSensingValueVO vo) throws Exception {
		return list("apiInterfaceDAO.selectSensingValueSpatioTemporalByID", vo);
	}
	
	// query/accidentByGeoID(2)
	// query/accidentByGeoID2(2)
	public List<?> selectAccident(SNAccidentVO vo) throws Exception {
		return list("apiInterfaceDAO.selectAccident", vo);
	}
	
	// query/accidentByRegion(2)
	public List<?> selectAccidentByRegion(SNAccidentVO vo) throws Exception {
		return list("apiInterfaceDAO.selectAccidentByRegion", vo);
	}
	
	// query/movingObject(2)
	public List<?> selectSewerMovingObject(GeoVideoRelVO vo) throws Exception {
		return list("apiInterfaceDAO.selectSewerMovingObject", vo);
	}
	public List<?> selectSewerVideoGeoID(GeoVideoRelVO vo) throws Exception {
		return list("apiInterfaceDAO.selectSewerVideoGeoID", vo);
	}
	public List<?> selectSubwayMovingObject(GeoVideoRelVO vo) throws Exception {
		return list("apiInterfaceDAO.selectSubwayMovingObject", vo);
	}
	public List<?> selectSubwayVideoGeoID(GeoVideoRelVO vo) throws Exception {
		return list("apiInterfaceDAO.selectSubwayVideoGeoID", vo);
	}
	
	// query/valueByGeoObject(2)
	public List<?> selectValueByGeoObject(SNGeoRelationVO vo) throws Exception {
		return list("apiInterfaceDAO.selectValueByGeoObject", vo);
	}
		
	// query/sriGrid(2)
	public List<?> selectSriGrid(SNSriGridVO vo) throws Exception {
		return list("apiInterfaceDAO.selectSriGrid", vo);
	}
	
	// query/xSriGrid(2)
	public List<?> selectXSriGrid(SNSriGridVO vo) throws Exception {
		return list("apiInterfaceDAO.selectXSriGrid", vo);
	}
	
	// query/getGridByColor(3)
	public List<?> selectGridByColor(SNSriGridVO vo) throws Exception {
		return list("apiInterfaceDAO.selectGridByColor", vo);
	}
	
	// query/getGridBySRI(3)
	public List<?> selectGridBySRI(SNSriGridVO vo) throws Exception {
		return list("apiInterfaceDAO.selectGridBySRI", vo);
	}
	
	// query/getInfraByColor(3)
	public List<?> selectInfraByColor(SNSriVO vo) throws Exception {
		return list("apiInterfaceDAO.selectInfraByColor", vo);
	}
	
	// query/getInfraBySRI(3)
	public List<?> selectInfraBySRI(SNSriVO vo) throws Exception {
		return list("apiInterfaceDAO.selectInfraBySRI", vo);
	}
	
	// query/getInfraByAttribute(3)
	public List<?> selectInfraByAttribute(SNSriVO vo) throws Exception {
		List<?> result = null;
		
		switch (vo.getGeoType()) {
		case "water":
			result = list("apiInterfaceDAO.selectWaterByAttribute", vo);
			break;
		case "sewer":
			result = list("apiInterfaceDAO.selectSewerByAttribute", vo);
			break;
		case "subway":
			result = list("apiInterfaceDAO.selectSubwayByAttribute", vo);
			break;
		case "subway_s":
			result = list("apiInterfaceDAO.selectStationByAttribute", vo);
			break;
		}
		
		return result;
	}
	
	// query/getInfraInGrid(3)
	// query/getInfraInGridBySRI(3)
	public List<?> selectInfraInGrid(SNSriGridVO vo) throws Exception {
		return list("apiInterfaceDAO.selectInfraInGrid", vo);
	}
	
	// query/getSensingValueByID(3)
	public List<?> selectSensingValueByID(SNSensingValueVO vo) throws Exception {
		return list("apiInterfaceDAO.selectSensingValueByID", vo);
	}
	
	// query/repairHistory(3)
	public List<?> selectInfraRepair(InfraRepairVO vo) throws Exception {
		return list("apiInterfaceDAO.selectInfraRepair", vo);
	}
	
	// query/repairHistorySubsidence(3)
	public List<?> selectSubsidenceRepair(SubsidenceRepairVO vo) throws Exception {
		return list("apiInterfaceDAO.selectSubsidenceRepair", vo);
	}
	
	// query/getEvent(3)
	public List<?> selectEvent(EventVO vo) throws Exception {
		return list("apiInterfaceDAO.selectEvent", vo);
	}
	
	// query/getWaterManhole(2)
	public List<?> selectWaterManholePipeRel(ManholePipeRelVO vo) throws Exception {
		return list("apiInterfaceDAO.selectWaterManholePipeRel", vo);
	}
		
	public List<?> selectGatewayIDList() throws Exception {
		return list("apiInterfaceDAO.selectGatewayIDList");
	}
	
	public List<?> selectGatewayIDList2() throws Exception {
		return list("apiInterfaceDAO.selectGatewayIDList2");
	}
	
	public List<?> selectGatewayDescription(SNGatewayVO vo) throws Exception {
		return list("apiInterfaceDAO.selectGatewayDescription", vo);
	}
	public List<?> selectPanDescriptionByGwID(SNGatewayVO vo) throws Exception {
		return list("apiInterfaceDAO.selectPanDescriptionByGwID", vo);
	}
	public List<?> selectNodeDescriptionByGwID(SNGatewayVO vo) throws Exception {
		return list("apiInterfaceDAO.selectNodeDescriptionByGwID", vo);
	}
	public List<?> selectTransducerDescriptionByGwID(SNGatewayVO vo) throws Exception {
		return list("apiInterfaceDAO.selectTransducerDescriptionByGwID", vo);
	}
	
	public List<?> selectPanDescription(SNPanVO vo) throws Exception {
		return list("apiInterfaceDAO.selectPanDescription", vo);
	}
	public List<?> selectNodeDescriptionByPanID(SNPanVO vo) throws Exception {
		return list("apiInterfaceDAO.selectNodeDescriptionByPanID", vo);
	}
	public List<?> selectTransducerDescriptionByPanID(SNPanVO vo) throws Exception {
		return list("apiInterfaceDAO.selectTransducerDescriptionByPanID", vo);
	}
	
	public List<?> selectNodeDescription(SNNodeVO vo) throws Exception {
		return list("apiInterfaceDAO.selectNodeDescription", vo);
	}
	public List<?> selectTransducerDescriptionBySnID(SNNodeVO vo) throws Exception {
		return list("apiInterfaceDAO.selectTransducerDescriptionBySnID", vo);
	}
	
	public List<?> selectTransducerDescription(SNTransducerVO vo) throws Exception {
		return list("apiInterfaceDAO.selectTransducerDescription", vo);
	}
	
	public List<?> selectGatewayDescriptionByGID(SNGatewayVO vo) throws Exception {
		return list("apiInterfaceDAO.selectGatewayDescriptionByGID", vo);
	}
	public List<?> selectPanDescriptionByGwGID(SNGatewayVO vo) throws Exception {
		return list("apiInterfaceDAO.selectPanDescriptionByGwGID", vo);
	}
	public List<?> selectNodeDescriptionByGwGID(SNGatewayVO vo) throws Exception {
		return list("apiInterfaceDAO.selectNodeDescriptionByGwGID", vo);
	}
	public List<?> selectTransducerDescriptionByGwGID(SNGatewayVO vo) throws Exception {
		return list("apiInterfaceDAO.selectTransducerDescriptionByGwGID", vo);
	}
	
	public List<?> selectPanDescriptionByGID(SNPanVO vo) throws Exception {
		return list("apiInterfaceDAO.selectPanDescriptionByGID", vo);
	}
	public List<?> selectNodeDescriptionByPanGID(SNPanVO vo) throws Exception {
		return list("apiInterfaceDAO.selectNodeDescriptionByPanGID", vo);
	}
	public List<?> selectTransducerDescriptionByPanGID(SNPanVO vo) throws Exception {
		return list("apiInterfaceDAO.selectTransducerDescriptionByPanGID", vo);
	}
	
	public List<?> selectNodeDescriptionByGID(SNNodeVO vo) throws Exception {
		return list("apiInterfaceDAO.selectNodeDescriptionByGID", vo);
	}
	public List<?> selectTransducerDescriptionBySnGID(SNNodeVO vo) throws Exception {
		return list("apiInterfaceDAO.selectTransducerDescriptionBySnGID", vo);
	}
	
	public List<?> selectTransducerDescriptionByGID(SNTransducerVO vo) throws Exception {
		return list("apiInterfaceDAO.selectTransducerDescriptionByGID", vo);
	}
	
	// information/resourceStatus
	public List<?> selectGatewayStatus(SNGatewayVO vo) throws Exception {
		return list("apiInterfaceDAO.selectGatewayStatus", vo);
	}
	public List<?> selectGatewayStatusDescent(SNGatewayVO vo) throws Exception {
		return list("apiInterfaceDAO.selectGatewayStatusDescent", vo);
	}
	public List<?> selectPanStatus(SNPanVO vo) throws Exception {
		return list("apiInterfaceDAO.selectPanStatus", vo);
	}
	public List<?> selectPanStatusDescent(SNPanVO vo) throws Exception {
		return list("apiInterfaceDAO.selectPanStatusDescent", vo);
	}
	public List<?> selectNodeStatus(SNNodeVO vo) throws Exception {
		return list("apiInterfaceDAO.selectNodeStatus", vo);
	}
	public List<?> selectNodeStatusDescent(SNNodeVO vo) throws Exception {
		return list("apiInterfaceDAO.selectNodeStatusDescent", vo);
	}
	public List<?> selectTransducerStatus(SNTransducerVO vo) throws Exception {
		return list("apiInterfaceDAO.selectTransducerStatus", vo);
	}
	
	// information/resourceStatusByID(2)
	public List<?> selectGatewayStatusByGID(SNGatewayVO vo) throws Exception {
		return list("apiInterfaceDAO.selectGatewayStatusByGID", vo);
	}
	public List<?> selectPanStatusByGwGID(SNGatewayVO vo) throws Exception {
		return list("apiInterfaceDAO.selectPanStatusByGwGID", vo);
	}
	public List<?> selectNodeStatusByGwGID(SNGatewayVO vo) throws Exception {
		return list("apiInterfaceDAO.selectNodeStatusByGwGID", vo);
	}
	public List<?> selectTransducerStatusByGwGID(SNGatewayVO vo) throws Exception {
		return list("apiInterfaceDAO.selectTransducerStatusByGwGID", vo);
	}
	
	public List<?> selectPanStatusByGID(SNPanVO vo) throws Exception {
		return list("apiInterfaceDAO.selectPanStatusByGID", vo);
	}
	public List<?> selectNodeStatusByPanGID(SNPanVO vo) throws Exception {
		return list("apiInterfaceDAO.selectNodeStatusByPanGID", vo);
	}
	public List<?> selectTransducerStatusByPanGID(SNPanVO vo) throws Exception {
		return list("apiInterfaceDAO.selectTransducerStatusByPanGID", vo);
	}
	
	public List<?> selectNodeStatusByGID(SNNodeVO vo) throws Exception {
		return list("apiInterfaceDAO.selectNodeStatusByGID", vo);
	}
	public List<?> selectTransducerStatusBySnGID(SNNodeVO vo) throws Exception {
		return list("apiInterfaceDAO.selectTransducerStatusBySnGID", vo);
	}
	
	public List<?> selectTransducerStatusByGID(SNTransducerVO vo) throws Exception {
		return list("apiInterfaceDAO.selectTransducerStatusByGID", vo);
	}
	
	// information/connectivity(2)
	public List<?> selectConnectivity(SNGeoRelationVO vo) throws Exception {
		return list("apiInterfaceDAO.selectConnectivity", vo);
	}
		
	// information/connectivityUpdate(2)
	public int insertNodeGeoRelationByGeoID(SNGeoRelationVO vo) throws Exception {
		// 1. 노드의 존재여부 조회
		EgovMap nodeExistYn = (EgovMap) select("apiInterfaceDAO.selectExistingNodeByRel", vo);
		// 2. geo-object의 존재여부 조회
		EgovMap geoExistYn  = (EgovMap) select("apiInterfaceDAO.selectExistingGeoByRel", vo);
		
		if (nodeExistYn.get("existyn").equals("N") || geoExistYn.get("existyn").equals("N"))
			return -1;
		
		// 3. geo-object의 좌표조회
		EgovMap coordinate = (EgovMap) select("apiInterfaceDAO.selectGeoPosition", vo);
		
		double tmX = (double)coordinate.get("x");
		double tmY = (double)coordinate.get("y");
		
		vo.setX(tmX);
		vo.setY(tmY);
		vo.setLongitude(UsdmUtils.TMEastToLongitude(tmY, tmX));
		vo.setLatitude(UsdmUtils.TMNorthToLatitude(tmY, tmX));
		
		// 4. geo-object ID로 nodegeorel 존재조회
		EgovMap relExistYnList = (EgovMap) select("apiInterfaceDAO.selectExistingNodeGeoRelByGeoID", vo);
		String relExistYn = (String)relExistYnList.get("existyn");
					
		// 5. 사유: 오류수정
		if (vo.getReason().equals("1")) {
			// 5.1 관계가 존재하면
			if (relExistYn.equals("Y")) {
				// 5.1.1 nodegeorel update: nodeGID, gwID, panID, snID, position
				insert("apiInterfaceDAO.updateNodeGeoRelByGeoID", vo);
			}
			// 5.2 관계가 존재하지 않으면
			else {
				// 5.2.1 nodegeorel insert
				insert("apiInterfaceDAO.insertNodeGeoRelByGeoID", vo);
			}
		}
		// 6. 사유: 위치이동
		else {
			// 6.1 관계가 존재하면
			if (relExistYn.equals("Y"))
				// 6.1.1 nodegeorel update: endtime
				update("apiInterfaceDAO.updateOldNodeGeoRelByGeoID", vo);
			
			// 6.2 nodegeorel insert
			insert("apiInterfaceDAO.insertNodeGeoRelByGeoID", vo);
		}
		
		// 7. node와 transducer 주소 update
		update("apiInterfaceDAO.updateSnAddressByRel", vo);
		update("apiInterfaceDAO.updateTdAddressByRel", vo);
		
		return 0;
	}
	public int insertNodeGeoRelationBySnGID(SNGeoRelationVO vo) throws Exception {
		// 1. 노드의 존재여부 조회
		EgovMap nodeExistYn = (EgovMap) select("apiInterfaceDAO.selectExistingNodeByRel", vo);
		// 2. geo-object의 존재여부 조회
		EgovMap geoExistYn  = (EgovMap) select("apiInterfaceDAO.selectExistingGeoByRel", vo);
		
		if (nodeExistYn.get("existyn").equals("N") || geoExistYn.get("existyn").equals("N"))
			return -1;
				
		// 3. node의 좌표조회
		EgovMap coordinate = (EgovMap) select("apiInterfaceDAO.selectGeoPosition", vo);
		
		double tmX = (double)coordinate.get("x");
		double tmY = (double)coordinate.get("y");
		
		vo.setX(tmX);
		vo.setY(tmY);
		vo.setLongitude(UsdmUtils.TMEastToLongitude(tmY, tmX));
		vo.setLatitude(UsdmUtils.TMNorthToLatitude(tmY, tmX));
		
		// 4. 센서 GID로 nodegeorel 존재조회
		EgovMap relExistYnList = (EgovMap) select("apiInterfaceDAO.selectExistingNodeGeoRelBySnGID", vo);
		String relExistYn = (String)relExistYnList.get("existyn");
					
		// 5. 사유: 오류수정
		if (vo.getReason().equals("1")) {
			// 5.1 관계가 존재하면
			if (relExistYn.equals("Y")) {
				// 5.1.1 nodegeorel update: ftr_cde, ftr_idn (ftr_cde를 알 수 없음)
				insert("apiInterfaceDAO.updateNodeGeoRelBySnGID", vo);
			}
			// 5.2 관계가 존재하지 않으면
			else {
				// 5.2.1 nodegeorel insert (ftr_cde를 알 수 없음)
				insert("apiInterfaceDAO.insertNodeGeoRelBySnGID", vo);
			}
		}
		// 6. 사유: 위치이동
		else {
			// 6.1 관계가 존재하면
			if (relExistYn.equals("Y"))
				// 6.1.1 nodegeorel update: endtime
				update("apiInterfaceDAO.updateOldNodeGeoRelBySnGID", vo);
			
			// 6.2 nodegeorel insert (ftr_cde를 알 수 없음)
			insert("apiInterfaceDAO.insertNodeGeoRelBySnGID", vo);
		}
		
		// 7. node와 transducer 주소 update
		update("apiInterfaceDAO.updateSnAddressByRel", vo);
		update("apiInterfaceDAO.updateTdAddressByRel", vo);
		
		return 0;
	}
	
	// information/updateDesc(2)
	public int updateGwDescription(SNGatewayVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("apiInterfaceDAO.selectExistingGateway", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("apiInterfaceDAO.updateGwDescription", vo);
		update("apiInterfaceDAO.updateTransducerGwID", vo);
		update("apiInterfaceDAO.updateNodeGwID", vo);
		update("apiInterfaceDAO.updatePanGwID", vo);
		
		return 0;
	}
	public int updatePanDescription(SNPanVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("apiInterfaceDAO.selectExistingPan", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("apiInterfaceDAO.updatePanDescription", vo);
		update("apiInterfaceDAO.updateTransducerPanID", vo);
		update("apiInterfaceDAO.updateNodePanID", vo);
		
		return 0;
	}
	public int updateSnDescription(SNNodeVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("apiInterfaceDAO.selectExistingNode", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("apiInterfaceDAO.updateSnDescription", vo);
		update("apiInterfaceDAO.updateTransducerSnID", vo);
		
		return 0;
	}
	public int updateTdDescription(SNTransducerVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("apiInterfaceDAO.selectExistingTransducer", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("apiInterfaceDAO.updateTdDescription", vo);
		
		return 0;
	}
	
	// information/updateDescByID(2)
	public int updateGwDescriptionByID(SNGatewayVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("apiInterfaceDAO.selectExistingGatewayByID", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("apiInterfaceDAO.updateGwDescriptionByID", vo);
		update("apiInterfaceDAO.updateTransducerGwIDByID", vo);
		update("apiInterfaceDAO.updateNodeGwIDByID", vo);
		update("apiInterfaceDAO.updatePanGwIDByID", vo);
		
		return 0;
	}
	public int updatePanDescriptionByID(SNPanVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("apiInterfaceDAO.selectExistingPanByID", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("apiInterfaceDAO.updatePanDescriptionByID", vo);
		update("apiInterfaceDAO.updateTransducerPanIDByID", vo);
		update("apiInterfaceDAO.updateNodePanIDByID", vo);
		
		return 0;
	}
	public int updateSnDescriptionByID(SNNodeVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("apiInterfaceDAO.selectExistingNodeByID", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("apiInterfaceDAO.updateSnDescriptionByID", vo);
		update("apiInterfaceDAO.updateTransducerSnIDByID", vo);
		
		// 생성된 노드와 geo-object연결성 정보 생성
		int result = updateSnConnectivity(vo);
		if (result < 0) return -1;
		
		return 0;
	}
	public int updateTdDescriptionByID(SNTransducerVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("apiInterfaceDAO.selectExistingTransducerByID", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("apiInterfaceDAO.updateTdDescriptionByID", vo);
		
		return 0;
	}
	
	// information/registerAccident(2)
	public int insertAccident(SNAccidentVO vo) throws Exception {
		// 실존하는 geo-object인지 조회
		EgovMap geoExistYn = (EgovMap) select ("apiInterfaceDAO.selectExistingAccidentGeo", vo);
		
		if (geoExistYn.get("existyn").equals("N"))
			return -1;
		
		// 중복되는 사고정보인지 조회
		EgovMap accidentExistYn = (EgovMap) select ("apiInterfaceDAO.selectExistingAccident", vo);
		
		if (accidentExistYn.get("existyn").equals("Y"))
			return -2;
		
		insert("apiInterfaceDAO.insertAccident", vo);
		
		// ==================
		//   MQ 메세지 전송 
		// ==================
		MessageQueueVO messageVO = new MessageQueueVO();
		messageVO.setEventName(UsdmUtils.MQ_ACCIDENTOCCURED);
		messageVO.setResourceID(vo.getFtrIdn());
		messageVO.setValue(vo.getGeoType());
		
		UsdmUtils.sendMessageMQ(messageVO);
		
		// Event 기록 저장
		insertEvent(messageVO);
		
		return 0;
	}
	
	// information/setThreshold(2)
	public void insertThreshold(SNSensingValueVO vo) throws Exception {
		// 기존의 threshold 존재여부 조회 
		EgovMap existYn = (EgovMap) select ("apiInterfaceDAO.selectExistingThreshold", vo);
		
		if (existYn.get("existyn").equals("N"))
			insert("apiInterfaceDAO.insertThreshold", vo);
		else
			update("apiInterfaceDAO.updateThreshold", vo);
	}
	
	// information/mapWaterManhole(2)
	public void insertWaterManholePipeRel(ManholePipeRelVO vo) throws Exception {
		// 기존의 manhole-pipe 관계 존재여부 조회 
		EgovMap existYn = (EgovMap) select ("apiInterfaceDAO.selectExistingWaterManholePipeRel", vo);
		
		if (existYn.get("existyn").equals("N"))
			insert("apiInterfaceDAO.insertWaterManholePipeRel", vo);
	}
	
	// information/setLeakThreshold(2)
	public void insertLeakThreshold(SNSensingValueVO vo) throws Exception {
		// 기존의 leak threshold 존재여부 조회 
		EgovMap existYn = (EgovMap) select ("apiInterfaceDAO.selectExistingLeakThreshold", vo);
		
		if (existYn.get("existyn").equals("N"))
			insert("apiInterfaceDAO.insertLeakThreshold", vo);
		else
			update("apiInterfaceDAO.updateLeakThreshold", vo);
	}
	
	// information/repairInfra(3)
	public String insertInfraRepair(InfraRepairVO vo) throws Exception {
		String cellIDList = "";
		
		SNSriVO sriVO = new SNSriVO();
		sriVO.setGeoID(String.valueOf(vo.getFtrIdn()));
		
		// 매설물 및 SRI 데이터 존재여부 조회
		EgovMap existYn = (EgovMap) select("apiInterfaceDAO.selectExistingInfra", vo);
		
		if (existYn.get("existyn").equals("N"))
			return "";
		
		// ==================================================================================
		//  1. 복구정보 생성
		// ==================================================================================
		insert("apiInterfaceDAO.insertInfraRepair", vo);
		
		// 새로 생성된 복구ID 조회
		EgovMap newRepairID = (EgovMap) select ("apiInterfaceDAO.selectNewestInfraRepairID", vo);
		vo.setRepairID((int)newRepairID.get("repairid"));
		
		// ==================================================================================
		//  2. 사고정보에 복구ID update
		// ==================================================================================
		if (vo.getAccidentIDList() != null && !vo.getAccidentIDList().equals(""))
			update ("apiInterfaceDAO.updateAccidentRepairID", vo);
		
		// ==================================================================================
		//  3. SRI update 및 재계산
		// ==================================================================================
		switch (vo.getCategory()) {
		// 교체
		case 1:
			// 매설년도와 사용기간 update
			update ("apiInterfaceDAO.updateInfraReplace", vo);
			
			// SRI 재계산
			switch (vo.getGeoType()) {
			case "water":
				calculateWaterBSRI(sriVO);
				break;
			case "sewer":
				calculateSewerBSRI(sriVO);
				break;
			case "subway":
				calculateSubwayBSRI(sriVO);
				break;
			case "subway_s":
				calculateStationBSRI(sriVO);
				break;
			case "geology": // TBD
				break;
			}
			
			break;

		// 전체보수
		case 2:
		// 부분보수
		case 3:
			// SRI값을 음수로, 등급을 'R'로 update
			// SRI 재계산은 하지 않는다
			switch (vo.getGeoType()) {
			case "water":
				update ("apiInterfaceDAO.updateWaterPipeRepaired", vo);
				break;
			case "sewer":
				update ("apiInterfaceDAO.updateDrainPipeRepaired", vo);
				break;
			case "subway":
				update ("apiInterfaceDAO.updateSubwayLineRepaired", vo);
				break;
			case "subway_s":
				update ("apiInterfaceDAO.updateSubwayStationRepaired", vo);
				break;
			case "geology": // TBD
				break;
			}
			
			break;
			
		// 취소
		case 9:
			switch (vo.getGeoType()) {
			// 상수도인 경우 누수음을 0으로 update
			case "water":
				update ("apiInterfaceDAO.updateWaterPipeLekSigToZero", vo);
				
				// SRI 재계산
				calculateWaterBSRI(sriVO);
				
				break;
				
			// TBD
			case "sewer":
			case "subway":
			case "subway_s":
			case "geology":
				break;
			}
			
			break;
		}
		
		// ==================================================================================
		//  4. 그리드 재계산
		// ==================================================================================
		cellIDList = updateGridSRI(vo.getGeoType(), sriVO);
		
		return cellIDList;
	}
	public EgovMap selectInfraRepairGeoChange(InfraRepairVO vo) throws Exception {
		EgovMap result = null;
		
		switch (vo.getGeoType()) {
		case "water":
			result = (EgovMap) select("apiInterfaceDAO.selectInfraRepairWaterChange", vo);
			break;
		case "sewer":
			result = (EgovMap) select("apiInterfaceDAO.selectInfraRepairSewerChange", vo);
			break;
		case "subway":
			result = (EgovMap) select("apiInterfaceDAO.selectInfraRepairSubwayChange", vo);
			break;
		case "subway_s":
			result = (EgovMap) select("apiInterfaceDAO.selectInfraRepairStationChange", vo);
			break;
		case "geology": // TBD
			break;
		}

		return result;
	}
	public List<?> selectInfraRepairGridChanged(InfraRepairVO vo) throws Exception {
		return (List<EgovMap>) list("apiInterfaceDAO.selectInfraRepairGridChanged", vo);
	}
	
	// information/repairSubsidence(3)
	public String insertSubsidenceRepair(SubsidenceRepairVO vo) throws Exception {
		String cellIDList          = "";
		String cellIDListBySewer   = "";
		String cellIDListByStation = "";
		String cellIDListBySubway  = "";
		
		String geoType = "";
		
		List<String> sewerPipeList = vo.getSewerPipeList();
		List<String> subwayList    = vo.getSubwayList();
		List<String> stationList   = vo.getStationList();
		
		// 매설물 및 SRI 데이터 존재여부 조회
		if (sewerPipeList != null) {
			for (int i=0; i<sewerPipeList.size(); i++) {
				geoType = "sewer";
				
				InfraRepairVO infraVO = new InfraRepairVO();
				infraVO.setFtrIdn(Integer.parseInt(sewerPipeList.get(i)));
				infraVO.setGeoTable(UsdmUtils.getGeoTableName(geoType));
				infraVO.setSriTable(UsdmUtils.getSRITableName(geoType));
				
				EgovMap existYn = (EgovMap) select("apiInterfaceDAO.selectExistingInfra", infraVO);
				
				if (existYn.get("existyn").equals("N"))
					return "";
			}
		}
		if (subwayList != null) {
			for (int i=0; i<subwayList.size(); i++) {
				geoType = "subway";
				
				InfraRepairVO infraVO = new InfraRepairVO();
				infraVO.setFtrIdn(Integer.parseInt(subwayList.get(i)));
				infraVO.setGeoTable(UsdmUtils.getGeoTableName(geoType));
				infraVO.setSriTable(UsdmUtils.getSRITableName(geoType));
				
				EgovMap existYn = (EgovMap) select("apiInterfaceDAO.selectExistingInfra", infraVO);
				
				if (existYn.get("existyn").equals("N"))
					return "";
			}
		}
		if (stationList != null) {
			for (int i=0; i<stationList.size(); i++) {
				geoType = "subway_s";
				
				InfraRepairVO infraVO = new InfraRepairVO();
				infraVO.setFtrIdn(Integer.parseInt(stationList.get(i)));
				infraVO.setGeoTable(UsdmUtils.getGeoTableName(geoType));
				infraVO.setSriTable(UsdmUtils.getSRITableName(geoType));
				
				EgovMap existYn = (EgovMap) select("apiInterfaceDAO.selectExistingInfra", infraVO);
				
				if (existYn.get("existyn").equals("N"))
					return "";
			}
		}
		
		// ==================================================================================
		//  1. 복구정보 생성
		// ==================================================================================
		insert("apiInterfaceDAO.insertSubsidenceRepair", vo);
		
		// 새로 생성된 복구ID 조회
		EgovMap newRepairID = (EgovMap) select ("apiInterfaceDAO.selectNewestSubsidenceRepairID", vo);
		vo.setRepairID((int)newRepairID.get("repairid"));
		
		// ==================================================================================
		//  2. 사고정보에 복구ID update
		// ==================================================================================
		if (vo.getAccidentIDList() != null && !vo.getAccidentIDList().equals("")) {
			InfraRepairVO infraVO = new InfraRepairVO();
			infraVO.setRepairID(vo.getRepairID());
			infraVO.setAccidentIDList(vo.getAccidentIDList());
			
			update ("apiInterfaceDAO.updateAccidentRepairID", infraVO);
		}
		
		// ==================================================================================
		//  3. SRI update 및 SRI 재계산, 그리드 재계산
		// ==================================================================================
		double sewerDistance   = vo.getSewerDistance();
		double subwayDistance  = vo.getSubwayDistance();
		double stationDistance = vo.getStationDistance();
		
		int ftrIdn;
		
		String updateColumns = "";
		
		SubsidenceRepairRelVO relVO = new SubsidenceRepairRelVO();
		
		// ==================================================================================
		//  3-1. 복구가 영향을 미치는 하수관이 존재하면
		// ==================================================================================
		if (sewerPipeList != null) {
			geoType = "sewer";
			
			for (int i=0; i<sewerPipeList.size(); i++) {
				ftrIdn = Integer.parseInt(sewerPipeList.get(i));
				
				// 복구-매설물 관계정보 생성
				relVO.setRepairID(vo.getRepairID());
				relVO.setGeoType(geoType);
				relVO.setFtrIdn(ftrIdn);
				relVO.setDistance(sewerDistance);
				relVO.setGeoTable(UsdmUtils.getGeoTableName(geoType));
				
				insert ("apiInterfaceDAO.insertSubsidenceRepairRel", relVO);

				// 하수도 SRI 계산을 위한 측정값 변경 (GPR측정값 update)
				//relVO.setUpdateColumns(updateColumns);
				
				//update ("apiInterfaceDAO.updateSubsidenceRepairStationSri", relVO);
				
				// SRI 재계산
				SNSriVO sriVO = new SNSriVO();
				sriVO.setGeoID(String.valueOf(ftrIdn));
				
				calculateSewerBSRI(sriVO);
				
				// 그리드 재계산
				cellIDListBySewer = updateGridSRI(geoType, sriVO);
			}
		}
		
		// ==================================================================================
		//  3-2. 복구가 영향을 미치는 철도역사가 존재하면
		// ==================================================================================
		if (stationList != null) {
			geoType = "subway_s";
			
			for (int i=0; i<stationList.size(); i++) {
				ftrIdn = Integer.parseInt(stationList.get(i));

				// 복구-매설물 관계정보 생성
				relVO.setRepairID(vo.getRepairID());
				relVO.setGeoType(geoType);
				relVO.setFtrIdn(ftrIdn);
				relVO.setDistance(stationDistance);
				relVO.setGeoTable(UsdmUtils.getGeoTableName(geoType));
				
				insert ("apiInterfaceDAO.insertSubsidenceRepairRel", relVO);
				
				// 역사 SRI 계산을 위한 측정값 변경
				// 복구대상 = 지반침하
				switch (vo.getTarget()) {
				case 11: // 복구대상 = 지반침하
				case 12: // 복구대상 = 동공/공동
					if (stationDistance <= 50) {
						updateColumns  = "grd_200 = CASE WHEN grd_200 = 0 THEN 0 ELSE grd_200-1 END,";
						updateColumns += "grd_100 = CASE WHEN grd_100 = 0 THEN 0 ELSE grd_100-1 END,";
						updateColumns += "grd_50  = CASE WHEN grd_50  = 0 THEN 0 ELSE grd_50-1  END";
					}
					else if (stationDistance <= 100) {
						updateColumns  = "grd_200 = CASE WHEN grd_200 = 0 THEN 0 ELSE grd_200-1 END,";
						updateColumns += "grd_100 = CASE WHEN grd_100 = 0 THEN 0 ELSE grd_100-1 END";
					}
					else if (stationDistance <= 200) {
						updateColumns  = "grd_200 = CASE WHEN grd_200 = 0 THEN 0 ELSE grd_200-1 END";
					}
					
					break;
					
				case 13: // 복구대상 = 도로함몰
					if (stationDistance <= 50) {
						updateColumns  = "rod_200 = CASE WHEN rod_200 = 0 THEN 0 ELSE rod_200-1 END,";
						updateColumns += "rod_100 = CASE WHEN rod_100 = 0 THEN 0 ELSE rod_100-1 END,";
						updateColumns += "rod_50  = CASE WHEN rod_50  = 0 THEN 0 ELSE rod_50-1  END";
					}
					else if (stationDistance <= 100) {
						updateColumns  = "rod_200 = CASE WHEN rod_200 = 0 THEN 0 ELSE rod_200-1 END,";
						updateColumns += "rod_100 = CASE WHEN rod_100 = 0 THEN 0 ELSE rod_100-1 END";
					}
					else if (stationDistance <= 200) {
						updateColumns  = "rod_200 = CASE WHEN rod_200 = 0 THEN 0 ELSE rod_200-1 END";
					}
				}
				
				relVO.setUpdateColumns(updateColumns);
				
				update ("apiInterfaceDAO.updateSubsidenceRepairStationSri", relVO);
				
				// SRI 재계산
				SNSriVO sriVO = new SNSriVO();
				sriVO.setGeoID(String.valueOf(ftrIdn));
				
				calculateStationBSRI(sriVO);
				
				// 그리드 재계산
				cellIDListByStation = updateGridSRI(geoType, sriVO);
			}
		}
				
		// ==================================================================================
		//  3-3. 복구가 영향을 미치는 철도선로가 존재하면
		// ==================================================================================
		if (subwayList != null) {
			geoType = "subway";
			
			for (int i=0; i<subwayList.size(); i++) {
				ftrIdn = Integer.parseInt(subwayList.get(i));
				
				// 복구-매설물 관계정보 생성
				relVO.setRepairID(vo.getRepairID());
				relVO.setGeoType(geoType);
				relVO.setFtrIdn(ftrIdn);
				relVO.setDistance(subwayDistance);
				relVO.setGeoTable(UsdmUtils.getGeoTableName(geoType));
				
				insert ("apiInterfaceDAO.insertSubsidenceRepairRel", relVO);

				// 선로 SRI 계산을 위한 측정값 변경
				switch (vo.getTarget()) {
				case 11: // 복구대상 = 지반침하
				case 12: // 복구대상 = 동공/공동
					if (subwayDistance <= 50) {
						updateColumns  = "grd_200 = CASE WHEN grd_200 = 0 THEN 0 ELSE grd_200-1 END,";
						updateColumns += "grd_100 = CASE WHEN grd_100 = 0 THEN 0 ELSE grd_100-1 END,";
						updateColumns += "grd_50  = CASE WHEN grd_50  = 0 THEN 0 ELSE grd_50-1  END";
					}
					else if (subwayDistance <= 100) {
						updateColumns  = "grd_200 = CASE WHEN grd_200 = 0 THEN 0 ELSE grd_200-1 END,";
						updateColumns += "grd_100 = CASE WHEN grd_100 = 0 THEN 0 ELSE grd_100-1 END";
					}
					else if (subwayDistance <= 200) {
						updateColumns  = "grd_200 = CASE WHEN grd_200 = 0 THEN 0 ELSE grd_200-1 END";
					}
					
					break;
				
				case 13: // 복구대상 = 도로함몰
					if (subwayDistance <= 50) {
						updateColumns  = "rod_200 = CASE WHEN rod_200 = 0 THEN 0 ELSE rod_200-1 END,";
						updateColumns += "rod_100 = CASE WHEN rod_100 = 0 THEN 0 ELSE rod_100-1 END,";
						updateColumns += "rod_50  = CASE WHEN rod_50  = 0 THEN 0 ELSE rod_50-1  END";
					}
					else if (subwayDistance <= 100) {
						updateColumns  = "rod_200 = CASE WHEN rod_200 = 0 THEN 0 ELSE rod_200-1 END,";
						updateColumns += "rod_100 = CASE WHEN rod_100 = 0 THEN 0 ELSE rod_100-1 END";
					}
					else if (subwayDistance <= 200) {
						updateColumns  = "rod_200 = CASE WHEN rod_200 = 0 THEN 0 ELSE rod_200-1 END";
					}
					
					break;
				}
				
				relVO.setUpdateColumns(updateColumns);
				
				update ("apiInterfaceDAO.updateSubsidenceRepairSubwaySri", relVO);
				
				// SRI 재계산
				SNSriVO sriVO = new SNSriVO();
				sriVO.setGeoID(String.valueOf(ftrIdn));
				
				calculateSubwayBSRI(sriVO);
				
				// 그리드 재계산
				cellIDListBySubway = updateGridSRI(geoType, sriVO);
			}
		}
		
		cellIDList += cellIDListBySewer;
		
		if (!cellIDListByStation.equals("")) {
			if (!cellIDList.equals("")) cellIDList += ",";
			cellIDList += cellIDListByStation;
		}
		
		if (!cellIDListBySubway.equals("")) {
			if (!cellIDList.equals("")) cellIDList += ",";
			cellIDList += cellIDListBySubway;
		}
		
		return cellIDList;
	}
	public List<?> selectSubsidenceRepairSewerChange(SubsidenceRepairVO vo) throws Exception {
		return list ("apiInterfaceDAO.selectSubsidenceRepairSewerChange", vo);
	}
	public List<?> selectSubsidenceRepairSubwayChange(SubsidenceRepairVO vo) throws Exception {
		return list ("apiInterfaceDAO.selectSubsidenceRepairSubwayChange", vo);
	}
	public List<?> selectSubsidenceRepairStationChange(SubsidenceRepairVO vo) throws Exception {
		return list ("apiInterfaceDAO.selectSubsidenceRepairStationChange", vo);
	}
	
	// information/geoobjectListRequest(2)
	public List<?> selectGeoobjectList(SNSensingValueVO vo) throws Exception {
		List<EgovMap> result = new ArrayList<EgovMap>();
		String objectType = vo.getGeoObjectType();
		
		switch (objectType) {
		case "w_manhole":
			result = (List<EgovMap>) list("apiInterfaceDAO.selectWaterManholeList", vo);
			break;
		default:
			result = (List<EgovMap>) list("apiInterfaceDAO.selectGeoobjectList", vo);
		}
		
		return result;
	}
	
	// information/firmwareDataStore(2)
	public String insertFirmwareData(SNFirmwareVO vo) throws Exception {
		insert("apiInterfaceDAO.insertFirmwareData", vo);
		
		EgovMap firmwareID = (EgovMap) select("apiInterfaceDAO.selectLatestFirmwareID", vo);
		
		return Integer.toString((int)firmwareID.get("firmwareid"));
	}
	
	// information/firmwareListRequest(2)
	public List<?> selectFirmwareList(SNFirmwareVO vo) throws Exception {
		return list("apiInterfaceDAO.selectFirmwareList", vo);
	}
	
	// information/firmwareDataRequest(2)
	public EgovMap selectFirmwareData(SNFirmwareVO vo) throws Exception {
		return (EgovMap)select("apiInterfaceDAO.selectFirmwareData", vo);
	}
	
	// information/firmwareDataDelete(2)
	public int deleteFirmwareData(SNFirmwareVO vo) throws Exception {
		EgovMap existYn = (EgovMap) select("apiInterfaceDAO.selectExistingFirmwareData", vo);
		
		if (existYn.get("existyn").equals("N"))
			return -1;
		
		insert("apiInterfaceDAO.deleteFirmwareData", vo);
		
		return 0;
	}
	
	// information/firmwareUpdateHistoryStore(2)
	public boolean selectExistingFirmwareData(SNFirmwareVO vo) throws Exception {
		boolean result = false;
		
		EgovMap existYn = (EgovMap) select("apiInterfaceDAO.selectExistingFirmwareData", vo);
		
		if (existYn.get("existyn").equals("Y"))
			result = true;;
		
		return result;
	}
	public int insertFirmwareUpdateHistory(SNFirmwareUpdateHistoryVO vo) throws Exception {
		int result = 0;
		
		EgovMap existYn = (EgovMap) select("apiInterfaceDAO.selectExistingFirmwareUpdateHistory", vo);
		
		if (existYn.get("existyn").equals("Y"))
			result = -1;
		else
			insert("apiInterfaceDAO.insertFirmwareUpdateHistory", vo);
		
		return result;
	}
	
	// information/firmwareUpdateHistoryRequest(2)
	public List<?> selectFirmwareUpdateHistory(SNFirmwareUpdateHistoryVO vo) throws Exception {
		return list("apiInterfaceDAO.selectFirmwareUpdateHistory", vo);
	}
	public List<?> selectFirmwareUpdateHistoryTargetList(SNFirmwareUpdateHistoryVO vo) throws Exception {
		return list("apiInterfaceDAO.selectFirmwareUpdateHistoryTargetList", vo);
	}
		
	// information/transducerDefaultDescription(2)
	public int insertTdDefault(SNTransducerVO vo) throws Exception {
		EgovMap existYn = (EgovMap) select("apiInterfaceDAO.selectExistingTdDefault", vo);
		
		if (existYn.get("existyn").equals("Y"))
			return -1;
		
		insert("apiInterfaceDAO.insertTdDefault", vo);
		
		return 0;
	}
	
	// information/transducerDefaultRequest(2)
	public List<?> selectTdDefault(SNTransducerVO vo) throws Exception {
		return list("apiInterfaceDAO.selectTdDefault", vo);
	}
	
	// information/transducerDefaultUpdate(2)
	public int updateTdDefault(SNTransducerVO vo) throws Exception {
		EgovMap existYn = (EgovMap) select("apiInterfaceDAO.selectExistingTdDefault", vo);
		
		if (existYn.get("existyn").equals("N"))
			return -1;
		
		insert("apiInterfaceDAO.updateTdDefault", vo);
		
		return 0;
	}
	
	// information/transducerDefaultDelete(2)
	public int deleteTdDefault(SNTransducerVO vo) throws Exception {
		EgovMap existYn = (EgovMap) select("apiInterfaceDAO.selectExistingTdDefault", vo);
		
		if (existYn.get("existyn").equals("N"))
			return -1;
		
		insert("apiInterfaceDAO.deleteTdDefault", vo);
		
		return 0;
	}
	
	// information/userDefinedMessageStore(2)
	public int insertUserDefinedMessage(UserDefinedMessageVO vo) throws Exception {
		int result = 0;
		
		EgovMap existYn = (EgovMap) select("apiInterfaceDAO.selectExistingUserDefinedMessage", vo);
		
		if (existYn.get("existyn").equals("Y"))
			result = -1;
		else {
			// UDM의 binary data 내용 분석
			// Binary data는 hex-string이며 little-endian 방식으로 구성되어 있다
			// 이 String은 hex-string임을 나타내기 위해 "0x~"로 시작하므로 이 두 글자는 잘라낸다
			String message = vo.getMessageBinaryData().substring(2);
			
			// 이를 byte 배열로 변환 -> ByteBuffer에 저장
			ByteBuffer bb = ByteBuffer.wrap(new BigInteger(message,16).toByteArray());
			bb.order(ByteOrder.LITTLE_ENDIAN);
			
			// UDM 식별자(2 bytes)를 ByteBuffer에서 읽어들인다
			short id = bb.getShort();
			
			// UDM의 식별자가 9(lowUartActivity) 또는 17(lowBattery)인 경우 MQ메세지 전송
			if (id == 9 || id == 17) {
				long   	timestamp 	= (long)bb.getInt()*1000;		// timestamp (4 bytes)
				short	gid 		= bb.getShort();				// 노드의 GID (2 bytes)
				double	battery 	= (double)bb.getShort()/1000;	// 배터리 잔량 (2 bytes)
				/*
				double	temperature	= (double)bb.getShort()/1000;	// 온도 (2 bytes)
				double	humidity	= (double)bb.getShort()/1000;	// 습도 (2 bytes)
				*/
				
				// ==================
				//   MQ 메세지 전송 
				// ==================
				MessageQueueVO messageVO = new MessageQueueVO();
				messageVO.setEventName(id==9 ? UsdmUtils.MQ_LOWUARTACTIVITY : UsdmUtils.MQ_LOWBATTERY);
				messageVO.setResourceID(String.valueOf(gid));
				messageVO.setValue(String.valueOf(battery));
				messageVO.setTimestamp(UsdmUtils.convertDateToStr(timestamp, "yyyyMMdd'T'HHmmss"));
				
				UsdmUtils.sendMessageMQ(messageVO);
				
				// Event 기록 저장
				insertEvent(messageVO);
			}
			
			insert("apiInterfaceDAO.insertUserDefinedMessage", vo);
		}
		
		return result;
	}
	
	// information/userDefinedMessageRequest(2)
	public List<?> selectUserDefinedMessage(UserDefinedMessageVO vo) throws Exception {
		return list("apiInterfaceDAO.selectUserDefinedMessage", vo);
	}
	
	// sri/getWaterBSRIByID(2)
	public List<?> getWaterBSRIByID(SNSriVO vo) throws Exception {
		return list("apiInterfaceDAO.getWaterBSRIByID", vo);
	}
	
	// sri/getWaterBSRIByRegion(2)
	public List<?> getWaterBSRIByRegion(SNSriVO vo) throws Exception {
		return list("apiInterfaceDAO.getWaterBSRIByRegion", vo);
	}
	
	// sri/getSewerBSRIByID(2)
	public List<?> getSewerBSRIByID(SNSriVO vo) throws Exception {
		return list("apiInterfaceDAO.getSewerBSRIByID", vo);
	}
	
	// sri/getSewerBSRIByRegion(2)
	public List<?> getSewerBSRIByRegion(SNSriVO vo) throws Exception {
		return list("apiInterfaceDAO.getSewerBSRIByRegion", vo);
	}
	
	// sri/getMLineBSRIByID(2)
	public List<?> getSubwayBSRIByID(SNSriVO vo) throws Exception {
		return list("apiInterfaceDAO.getSubwayBSRIByID", vo);
	}
	
	// sri/getMLineBSRIByRegion(2)
	public List<?> getSubwayBSRIByRegion(SNSriVO vo) throws Exception {
		return list("apiInterfaceDAO.getSubwayBSRIByRegion", vo);
	}
	
	// sri/getMStationBSRIByID(2)
	public List<?> getStationBSRIByID(SNSriVO vo) throws Exception {
		return list("apiInterfaceDAO.getStationBSRIByID", vo);
	}
	
	// sri/getMStationBSRIByRegion(2)
	public List<?> getStationBSRIByRegion(SNSriVO vo) throws Exception {
		return list("apiInterfaceDAO.getStationBSRIByRegion", vo);
	}
	
	// sri/updateWaterBSRIByID(2)
	public void updateWaterBSRIByID(SNSriVO vo) throws Exception {
		EgovMap existYn = (EgovMap) select("apiInterfaceDAO.selectExistingWaterSri", vo);

		if (existYn.get("existyn").equals("Y")) {
			// 필드 업데이트
			update("apiInterfaceDAO.updateWaterBSRIByID", vo);
			
			// BSRI 재계산
			calculateWaterBSRI(vo);
			
			// SRI grid update
			updateGridSRI("water", vo);
		}
	}
	
	// sri/updateSewerBSRIByID(2)
	public void updateSewerBSRIByID(SNSriVO vo) throws Exception {
		EgovMap existYn = (EgovMap) select("apiInterfaceDAO.selectExistingSewerSri", vo);
		
		if (existYn.get("existyn").equals("Y")) {
			// 필드 업데이트
			update("apiInterfaceDAO.updateSewerBSRIByID", vo);
			
			// BSRI 재계산
			calculateSewerBSRI(vo);
			
			// SRI grid update
			updateGridSRI("sewer", vo);
		}
	}
	
	// sri/updateMLineBSRIByID(2)
	public void updateSubwayBSRIByID(SNSriVO vo) throws Exception {
		EgovMap existYn = (EgovMap) select("apiInterfaceDAO.selectExistingSubwaySri", vo);
		
		if (existYn.get("existyn").equals("Y")) {
			// 필드 업데이트
			update("apiInterfaceDAO.updateSubwayBSRIByID", vo);
			
			// BSRI 재계산
			calculateSubwayBSRI(vo);
			
			// SRI grid update
			updateGridSRI("subway", vo);
		}
	}
	
	// sri/updateMStationBSRIByID(2)
	public void updateStationBSRIByID(SNSriVO vo) throws Exception {
		EgovMap existYn = (EgovMap) select("apiInterfaceDAO.selectExistingStationSri", vo);
		
		if (existYn.get("existyn").equals("Y")) {
			// 필드 업데이트
			update("apiInterfaceDAO.updateStationBSRIByID", vo);
			
			// BSRI 재계산
			calculateStationBSRI(vo);
			
			// SRI grid update
			updateGridSRI("subway_s", vo);
		}
	}
		
	public void insertAssessValues(SNAssessValueVO vo) throws Exception {
		insert("apiInterfaceDAO.insertAssessValues", vo);
	}
	
	public List<?> retrieveAssessValues(SNAssessValueVO vo) throws Exception {
		return list("apiInterfaceDAO.retrieveAssessValues", vo);
	}
	
	public List<?> retrieveRiskValues(SNAssessValueVO vo) throws Exception {
		return list("apiInterfaceDAO.retrieveRiskValues", vo);
	}
	
	// rfid/initializeRFID(2)
	public void insertRfid(RfidVO vo) throws Exception {
		EgovMap existYn = (EgovMap) select("apiInterfaceDAO.selectExistingRfid", vo);
		
		// 기존레코드가 존재하지 않으면
		if (existYn.get("existyn").equals("N")) {
			insert("apiInterfaceDAO.insertRfid", vo);
		}
		else {
			update("apiInterfaceDAO.updateRfid", vo);
		}
	}
	
	// rfid/updateRFID(2)
	public void updateRfid(RfidVO vo) throws Exception {
		// 기존레코드의 state 조회
		EgovMap stateMap = (EgovMap)select("apiInterfaceDAO.selectRfidState", vo);
		
		if (stateMap != null) {
			String state = (String)stateMap.get("state");
			
			// 기존레코드의 state가 update하려는 state와 다른 경우
			if (!state.equals(vo.getState())) {
				
				// ==================
				//   MQ 메세지 전송 
				// ==================
				MessageQueueVO messageVO = new MessageQueueVO();
				messageVO.setEventName(UsdmUtils.MQ_RFIDSTATECHANGED);
				messageVO.setResourceID(String.valueOf(vo.getRfid()));
				messageVO.setValue(vo.getState());
				
				UsdmUtils.sendMessageMQ(messageVO);
				
				// Event 기록 저장
				insertEvent(messageVO);
			}
			
			update("apiInterfaceDAO.updateRfidState", vo);
		}
	}
	
	// rfid/getRFID(2)
	public List<?> selectRfid(RfidVO vo) throws Exception {
		return list("apiInterfaceDAO.selectRfid", vo);
	}
	
	// 노드와 geo-object의 연결성 정보를 생성하고, 노드의 위치를 geo-object와 동일하게 변경
	public int updateSnConnectivity(SNNodeVO vo) throws Exception {
		// 가장 가까운 geo-object 탐색(ftr_cde, ftr_idn, 좌표)
		EgovMap nearestGeoObject = (EgovMap) select("apiInterfaceDAO.selectNearestGeoObject", vo);
		
		// 탐색된 geo-object가 없으면 연결성 정보를 생성하지 않고 종료
		if (nearestGeoObject.isEmpty()) return 0;
		
		double tmX = (double)nearestGeoObject.get("x");
		double tmY = (double)nearestGeoObject.get("y");
		
		vo.setFtr_cde((String)nearestGeoObject.get("ftrcde"));
		vo.setFtr_idn(Integer.toString((int)nearestGeoObject.get("ftridn")));
		vo.setX(String.valueOf(tmX));
		vo.setY(String.valueOf(tmY));
		vo.setLongitude(String.valueOf(UsdmUtils.TMEastToLongitude(tmY, tmX)));
		vo.setLatitude(String.valueOf(UsdmUtils.TMNorthToLatitude(tmY, tmX)));
				
		// node와 geo-object의 현재관계 조회
		EgovMap relExistYn = (EgovMap) select("apiInterfaceDAO.selectExistingNodeGeoRel", vo);
		
		// 탐색된 geo-object와의 현재관계가 존재하지 않으면
		if (relExistYn.get("existyn").equals("N"))
		{
			// 기존의 다른 geo-object와의 관계를 update
			update("apiInterfaceDAO.updateOldNodeGeoRel", vo);
			
			// 새로운 관계정보 생성
			insert("apiInterfaceDAO.insertNodeGeoRel", vo);
		}
		// 현재관계가 존재하면 관계정보는 수정하지 않는다
		
		// node의 geo-object ID와 좌표 update
		update("apiInterfaceDAO.updateNodeGeoID", vo);
		
		return 0;
	}
	
	// 상수도 BSRI를 계산하여 update한다.
	public int calculateWaterBSRI(SNSriVO vo) throws Exception {
		Calendar cal = Calendar.getInstance();
		
		// [A] 상태평가값 계산에 사용하는 변수
		double lek_cnt_value = 0;				// A1.누수건수 조건값
		double lek_prb_value = 0;				// A2.누수가능성 조건값
		double ist_dur_value = 0;				// A3.매설년수 조건값
		double prt_fac_value = 0;				// A4.전식방지 조건값
		double rod_typ_value = 0;				// A5.도로현황 조건값
		double out_cot_value = 0;				// A6.외부피복 조건값
		double ins_cot_value = 0;				// A7.내부피복 조건값
		double rct_cnt_value = 0;				// A8.민원건수 조건값
		double pip_mat_value = 0;				// A9.이력정보 조건값
		
		final double lek_cnt_weight = 22;		// A1.누수건수 가중치
		final double lek_prb_weight = 9;		// A2.누수가능성 가중치
		final double ist_dur_weight = 15;		// A3.매설년수 가중치
		final double prt_fac_weight = 9;		// A4.전식방지 가중치
		final double rod_typ_weight = 6;		// A5.도로현황 가중치
		final double out_cot_weight = 1;		// A6.외부피복 가중치
		final double ins_cot_weight = 12;		// A7.내부피복 가중치
		final double rct_cnt_weight = 9;		// A8.민원건수 가중치
		final double pip_mat_weight = 8;		// A9.이력정보 가중치
		
		double wtl_sta;							// A10.상태평가(A의 결과)
		
		// [B] 위험점수,등급 계산에 사용하는 변수
		double wtl_sta_value = 0;				// B1.상태평가 조건값
		double lek_sig_value = 0;				// B2.누수신호 조건값
		double pip_mov_value = 0;				// B3.위치변화 조건값
		
		final double wtl_sta_weight = 20;		// B1.상태평가 가중치
		final double lek_sig_weight = 30;		// B2.누수신호 가중치
		final double pip_mov_weight = 50;		// B3.위치변화 가중치
		
		double wtl_sri;							// B4.위험점수(B의 결과)
		String wtl_lev;							// B5.위험등급
		
		// 상수도 SRI테이블 레코드 조회
		EgovMap waterSRIMap = (EgovMap)select("apiInterfaceDAO.selectWaterPipeSRI", vo);

		// 컬럼값 변수설정
		char 	lekCnt = (char)waterSRIMap.get("lekCnt");							// 누수건수
		double 	lekPrb = ((BigDecimal)waterSRIMap.get("lekPrb")).doubleValue();		// 누수가능성
		String 	mopCde = (String)waterSRIMap.get("mopCde");							// 관재질
		int 	istDur = cal.get(cal.YEAR)-((BigDecimal)waterSRIMap.get("istYea")).intValue()+1; // 매설년수
		String 	prtFac = (String)waterSRIMap.get("prtFac");							// 전식방지
		char 	rodTyp = Character.toLowerCase((char)waterSRIMap.get("rodTyp"));	// 도로현황
		String	outCot = (String)waterSRIMap.get("outCot");							// 외부피복
		String	insCot = (String)waterSRIMap.get("insCot");							// 내부피복
		int 	rctCnt = ((BigDecimal)waterSRIMap.get("rctCnt")).intValue();		// 민원건수
		int 	pipMat = ((BigDecimal)waterSRIMap.get("pipMat")).intValue();		// 이력정보
		double	lekSig = ((BigDecimal)waterSRIMap.get("lekSig")).doubleValue();		// 누수신호
		double 	pipMov = ((BigDecimal)waterSRIMap.get("pipMov")).doubleValue();		// 직관부이동거리
		
		/*****************************
		 * [A] 상태평가값 계산 START *
		 *****************************/
		
		// A1.누수건수 조건값 계산
		if      (lekCnt == 'Y')	lek_cnt_value = 0;
		else if (lekCnt == 'N')	lek_cnt_value = 1;
		
		// A2.누수가능성 조건값 계산
		if      (lekPrb >= 300)	lek_prb_value = 1;
		else if (lekPrb <= 50)	lek_prb_value = 0;
		else					lek_prb_value = 0.5;
		
		// A3.매설년수 조건값 계산
		switch (mopCde) {
		// 관 재질이 '금속관'인 경우
		case "MOP001": case "MOP002": case "MOP010":
		case "MOP019": case "MOP844": case "MOP934":
			if      (istDur < 15)	ist_dur_value = 1;
			else if (istDur >= 30)	ist_dur_value = 0;
			else					ist_dur_value = 0.5;
			break;
		// 관 재질이 '비금속관'인 경우
		case "MOP004": case "MOP005": case "MOP030":
		case "MOP909": case "MOP929":
			if      (istDur < 10)	ist_dur_value = 1;
			else if (istDur >= 20)	ist_dur_value = 0;
			else					ist_dur_value = 0.5;
			break;
		// 관 재질이 '금속관','비금속관'에 속하지 않는 경우
		default:
			ist_dur_value = 0.5;
		}
		
		// A4.전식방지 조건값 계산
		if      (prtFac.equals("N"))	prt_fac_value = 1;
		else if (prtFac.equals("Y"))	prt_fac_value = 0;
		else if (prtFac.equals("UN"))	prt_fac_value = 0.5;
		
		// A5.도로현황 조건값 계산
		if      (rodTyp=='c' || rodTyp=='d') rod_typ_value = 1;
		else if (rodTyp=='a') rod_typ_value = 0;
		else if (rodTyp=='b') rod_typ_value = 0.5;
		else                  rod_typ_value = 1;
		
		// A6.외부피복 조건값 계산
		if (outCot.equals("UN")) out_cot_value = 0.5;
		else {
			switch (mopCde) {
			// 관 재질이 '금속관'인 경우
			case "MOP001": case "MOP002": case "MOP010":
			case "MOP019": case "MOP844": case "MOP934":
				if      (outCot.equals("Y")) out_cot_value = 1;
				else if (outCot.equals("N")) out_cot_value = 0;
				break;
			// 관 재질이 '비금속관'인 경우
			case "MOP004": case "MOP005": case "MOP030":
			case "MOP909": case "MOP929":
				out_cot_value = 1;
				break;
			}
		}
		
		// A7.내부피복 조건값 계산
		if (insCot.equals("UN")) ins_cot_value = 0.5;
		else {
			switch (mopCde) {
			// 관 재질이 '금속관'인 경우
			case "MOP001": case "MOP002": case "MOP010":
			case "MOP019": case "MOP844": case "MOP934":
				if      (insCot.equals("Y")) ins_cot_value = 1;
				else if (insCot.equals("N")) ins_cot_value = 0;
				break;
			// 관 재질이 '비금속관'인 경우
			case "MOP004": case "MOP005": case "MOP030":
			case "MOP909": case "MOP929":
				ins_cot_value = 1;
				break;
			}
		}
		
		// A8.민원건수 조건값 계산
		if      (rctCnt == -1)	rct_cnt_value = 0.5;
		else if (rctCnt < 0.7)	rct_cnt_value = 1;
		else if (rctCnt >= 2.8)	rct_cnt_value = 0;
		else					rct_cnt_value = 0.5;
		
		// A9.이력정보 조건값 계산
		if      (pipMat == 999)	pip_mat_value = 0.5;
		else if (pipMat >= 10)	pip_mat_value = 1;
		else if (pipMat == 0)	pip_mat_value = 0;
		else					pip_mat_value = 0.5;
		
		// A10.상태평가값 계산 (A1~A9 조건값*가중치의 합)
		wtl_sta = lek_cnt_value * lek_cnt_weight	// A1.누수건수
				+ lek_prb_value * lek_prb_weight	// A2.누수가능성
				+ ist_dur_value * ist_dur_weight	// A3.매설년수
	            + prt_fac_value * prt_fac_weight	// A4.전식방지
	            + rod_typ_value * rod_typ_weight	// A5.도로현황
	            + out_cot_value * out_cot_weight	// A6.외부피복
	            + ins_cot_value * ins_cot_weight	// A7.내부피복
	            + rct_cnt_value * rct_cnt_weight	// A8.민원건수
	            + pip_mat_value * pip_mat_weight;	// A9.이력정보
		
		/***************************
		 * [A] 상태평가값 계산 END *
		 ***************************/
		
		/********************************
		 * [B] 위험점수,등급 계산 START *
		 ********************************/
		
		// B1.상태평가 조건값 계산
		if      (wtl_sta >= 75)	wtl_sta_value = 1;
		else if (wtl_sta < 50)	wtl_sta_value = 0;
		else					wtl_sta_value = 0.5;
		
		// B2.누수신호 조건값 계산
		if      (lekSig <= 30)	lek_sig_value = 1;
		else if (lekSig >= 50)	lek_sig_value = 0;
		else					lek_sig_value = 0.5;
		
		// B3.위치변화 조건값 계산
		if      (pipMov < 1)	pip_mov_value = 1;
		else if (pipMov >= 2)	pip_mov_value = 0;
		else					pip_mov_value = 0.5;
		
		// B4.위험점수 계산 (B1~B3 조건값*가중치의 합)
		wtl_sri = wtl_sta_value * wtl_sta_weight	// B1.상태평가
				+ lek_sig_value * lek_sig_weight	// B2.누수신호
				+ pip_mov_value * pip_mov_weight;	// B3.위치변화
		
		wtl_sri = 100 - wtl_sri;
		
		// B5.위험등급 설정
		/*
		if      (wtl_sri >= 55) wtl_lev = "A";
		else if (wtl_sri >= 25)	wtl_lev = "B";
		else 					wtl_lev = "C";
		*/
		
		/******************************
		 * [B] 위험점수,등급 계산 END *
		 ******************************/
				
		// 계산결과 update
		vo.setAssessValue(Double.toString(wtl_sta));
		vo.setSriValue(Double.toString(wtl_sri));
		vo.setSriGrade(UsdmUtils.getWaterSRIGrade(wtl_sri));
		
		update("apiInterfaceDAO.updateWaterPipeSRI", vo);
		
		return 0;
	}
	
	// 하수도 BSRI를 계산하여 update한다.
	public int calculateSewerBSRI(SNSriVO vo) throws Exception {
		Calendar cal = Calendar.getInstance();
		
		// [A] 상태평가값 계산에 사용하는 변수
		double mop_cde_value = 0;				// A1.관종 조건값
		double std_dip_value = 0;				// A2.관경 조건값
		double ist_dur_value = 0;				// A3.매설년수 조건값
		double dra_typ_value = 0;				// A4.배수형식 조건값
		double rod_typ_value = 0;				// A5.도로현황 조건값
		double pip_mat_value = 0;				// A6.이력정보 조건값
		
		final double mop_cde_weight = 16.48;	// A1.관종 가중치
		final double std_dip_weight = 13.62;	// A2.관경 가중치
		final double ist_dur_weight = 26.64;	// A3.매설년수 가중치
		final double dra_typ_weight = 13.84;	// A4.배수형식 가중치
		final double rod_typ_weight = 13.59;	// A5.도로현황 가중치
		final double pip_mat_weight = 15.83;	// A6.이력정보 가중치
		
		double sew_sta;							// A7.상태평가(A의 결과)
		
		// [B] 위험점수,등급 계산에 사용하는 변수
		double sew_sta_value = 0;				// B1.상태평가 조건값
		double sew_ctv_value = 0;				// B2.내부상태 조건값
		double sew_gpr_value = 0;				// B3.동공상태 조건값
		
		final double sew_sta_weight = 11.77;	// B1.상태평가 가중치
		final double sew_ctv_weight = 53.24;	// B2.내부상태 가중치
		final double sew_gpr_weight = 34.99;	// B3.동공상태 가중치
		
		double sew_sri;							// B4.위험점수(B의 결과)
		String sew_lev;							// B5.위험등급
		
		// 하수도 SRI테이블 레코드 조회
		EgovMap drainSRIMap = (EgovMap)select("apiInterfaceDAO.selectDrainPipeSRI", vo);
		
		// 컬럼값 변수설정
		String 	mopCde = (String)drainSRIMap.get("mopCde");							// 관종
		double	stdDip = ((BigDecimal)drainSRIMap.get("stdDip")).doubleValue();		// 관경
		int 	istDur = cal.get(cal.YEAR)-((BigDecimal)drainSRIMap.get("istYea")).intValue()+1; // 매설년수
		String 	draTyp = (String)drainSRIMap.get("draTyp");							// 배수형식
		char 	rodTyp = (char)drainSRIMap.get("rodTyp");							// 도로현황
		int 	pipMat = ((BigDecimal)drainSRIMap.get("pipMat")).intValue();		// 이력정보
		//double	sewCtv = ((BigDecimal)drainSRIMap.get("sewCtv")).doubleValue();		// 내부상태
		//double	sewGpr = ((BigDecimal)drainSRIMap.get("sewGpr")).doubleValue();		// 동공상태
		
		/*****************************
		 * [A] 상태평가값 계산 START *
		 *****************************/
		
		// A1.관종 조건값 계산
		switch (mopCde) {
		// 관 재질이 '다중PE관'인 경우
		case "MOP800": case "MOP801":
			mop_cde_value = 1;
			break;
		// 관 재질이 'PE관'인 경우
		case "MOP005":
			mop_cde_value = 0.5;
			break;
		// 관 재질이 '하수BOX 및 흄관'인 경우
		case "MOP012": case "MOP020":
			mop_cde_value = 0;
			break;
		default:
			mop_cde_value = 0.5;
		}
		
		// A2.관경 조건값 계산
		if      (stdDip <= 300)	std_dip_value = 1;
		else if (stdDip >= 450)	std_dip_value = 0;
		else					std_dip_value = 0.5;
		
		// A3.매설년수 조건값 계산
		switch (mopCde) {
		// 관 재질이 '흄관'인 경우
		case "MOP012": case "MOP033":
			if      (istDur < 15)	ist_dur_value = 1;
			else if (istDur >= 20)	ist_dur_value = 0;
			else					ist_dur_value = 0.5;
			break;
		// 관 재질이 '비콘크리트관'인 경우
		case "MOP004": case "MOP005": case "MOP015":
		case "MOP041": case "MOP868":
			if      (istDur < 10)	ist_dur_value = 1;
			else if (istDur >= 30)	ist_dur_value = 0;
			else					ist_dur_value = 0.5;
			break;
		// 관 재질이 '흄관','비콘크리트관'에 속하지 않는 경우
		default:
			ist_dur_value = 0.5;
		}
		
		// A4.배수형식 조건값 계산
		if      (draTyp.equals("SBA003")) dra_typ_value = 1;
		else if (draTyp.equals("SBA001")) dra_typ_value = 0;
		else if (draTyp.equals("SBA004")) dra_typ_value = 0.5;
		
		// A5.도로현황 조건값 계산
		if      (rodTyp=='c' || rodTyp=='d') rod_typ_value = 1;
		else if (rodTyp=='a') rod_typ_value = 0;
		else if (rodTyp=='b') rod_typ_value = 0.5;
		
		// A6.이력정보 조건값 계산
		if      (pipMat >= 10)	pip_mat_value = 1;
		else if (pipMat == 0)	pip_mat_value = 0;
		else					pip_mat_value = 0.5;
		
		// A10.상태평가값 계산 (A1~A6 조건값*가중치의 합)
		sew_sta = mop_cde_value * mop_cde_weight	// A1.관종
				+ std_dip_value * std_dip_weight	// A2.관경
				+ ist_dur_value * ist_dur_weight	// A3.매설년수
				+ dra_typ_value * dra_typ_weight	// A4.배수형식
				+ rod_typ_value * rod_typ_weight	// A5.도로현황
				+ pip_mat_value * pip_mat_weight;	// A6.이력정보
		
		/***************************
		 * [A] 상태평가값 계산 END *
		 ***************************/
		
		/********************************
		 * [B] 위험점수,등급 계산 START *
		 ********************************/
		
		/*
		// B1.상태평가 조건값 계산
		if      (sew_sta >= 75)	sew_sta_value = 1;
		else if (sew_sta < 50)	sew_sta_value = 0;
		else					sew_sta_value = 0.5;
		
		// B2.내부상태 조건값 계산
		if      (sewCtv >= 75)	sew_ctv_value = 1;
		else if (sewCtv < 50)	sew_ctv_value = 0;
		else					sew_ctv_value = 0.5;
		
		// B3.동공상태 조건값 계산
		if      (sewGpr >= 50)	sew_gpr_value = 0;
		else if (sewGpr >= 30)	sew_gpr_value = 0.5;
		else					sew_gpr_value = 1;
		
		// B4.위험점수 계산 (B1~B3 조건값*가중치의 합)
		sew_sri = sew_sta_value * sew_sta_weight	// B1.상태평가
				+ sew_ctv_value * sew_ctv_weight	// B2.내부상태
				+ sew_gpr_value * sew_gpr_weight;	// B3.동공상태
		
		// B5.위험등급 설정
		if      (sew_sri > 75) 	sew_lev = "A";
		else if (sew_sri < 45) 	sew_lev = "C";
		else 					sew_lev = "B";
		*/
		
		sew_sri = 100 - sew_sta;
		
		/*
		if      (sew_sri >= 55) sew_lev = "A";
		else if (sew_sri >= 25)	sew_lev = "B";
		else 					sew_lev = "C";
		*/
		
		/******************************
		 * [B] 위험점수,등급 계산 END *
		 ******************************/
		
		// 계산결과 update
		vo.setAssessValue(Double.toString(sew_sta));
		vo.setSriValue(Double.toString(sew_sri));
		vo.setSriGrade(UsdmUtils.getSewerSRIGrade(sew_sri));
		
		update("apiInterfaceDAO.updateDrainPipeSRI", vo);
		
		return 0;
	}
	
	// 지하철선로 BSRI를 계산하여 update한다.
	public int calculateSubwayBSRI(SNSriVO vo) throws Exception {
		Calendar cal = Calendar.getInstance();
		
		// [A] 위험점수 계산에 사용하는 변수
		double grd_cde_value = 0;				// A1.구조물등급 조건값
		double des_typ_value = 0;				// A2.설계형태 조건값
		double max_dep_value = 0;				// A3.최대매설깊이 조건값
		double ist_dur_value = 0;				// A4.공용기간 조건값
		double grd_sub_value = 0;				// A5.지반함몰 조건값 (A5-1~3의 결과)
		double rod_sub_value = 0;				// A6.도로함몰 조건값 (A6-1~3의 결과)
		double nea_dig_value = 0;				// A7.인접굴착 조건값 (A7-1~2의 결과)
		double col_wel_value = 0;				// A8.집수정 조건값
		
		final double grd_cde_weight = 10;		// A1.구조물등급 가중치
		final double des_typ_weight = 3;		// A2.설계형태 가중치
		final double max_dep_weight = 3;		// A3.최대매설깊이 가중치
		final double ist_dur_weight = 4;		// A4.공용기간 가중치
		final double grd_sub_weight = 50;		// A5.지반함몰 가중치
		final double rod_sub_weight = 15;		// A6.도로함몰 가중치
		final double nea_dig_weight = 10;		// A7.인접굴착 가중치
		final double col_wel_weight = 5;		// A8.집수정 가중치
		
		// [A5] 지반함몰 조건값 계산에 사용하는 변수
		double grd_50_value  = 0;				// A5-1.지반함몰 조건값(50m)
		double grd_100_value = 0;				// A5-2.지반함몰 조건값(100m)
		double grd_200_value = 0;				// A5-3.지반함몰 조건값(200m)
		
		final double grd_50_weight  = 60;		// A5-1.지반함몰 가중치(50m)
		final double grd_100_weight = 30;		// A5-2.지반함몰 가중치(100m)
		final double grd_200_weight = 10;		// A5-3.지반함몰 가중치(200m)
		
		// [A6] 도로함몰 조건값 계산에 사용하는 변수
		double rod_50_value  = 0;				// A6-1.도로함몰 조건값(50m)
		double rod_100_value = 0;				// A6-2.도로함몰 조건값(100m)
		double rod_200_value = 0;				// A6-3.도로함몰 조건값(200m)
		
		final double rod_50_weight  = 60;		// A6-1.도로함몰 가중치(50m)
		final double rod_100_weight = 30;		// A6-2.도로함몰 가중치(100m)
		final double rod_200_weight = 10;		// A6-3.도로함몰 가중치(200m)
		
		// [A7] 인접굴착 조건값 계산에 사용하는 변수
		double nea_dis_value = 0;				// A7-1.이격거리 조건값
		double nea_dep_value = 0;				// A7-2.굴착심도 조건값
		
		final double nea_dis_weight = 50;		// A7-1.이격거리 가중치
		final double nea_dep_weight = 50;		// A7-2.굴착심도 가중치
		
		double met_sri;							// A9.위험점수(A의 결과)
		String met_lev;							// A10.위험등급
		
		// 도시철도 SRI테이블 레코드 조회
		EgovMap subwaySRIMap = (EgovMap)select("apiInterfaceDAO.selectSubwayLineSRI", vo);
		
		// 컬럼값 변수설정
		String 	grdCde = subwaySRIMap.get("grdCde").toString();							// 구조물등급
		String	desTyp = subwaySRIMap.get("desTyp").toString();							// 설계형태
		double  maxDep = ((BigDecimal)subwaySRIMap.get("maxDep")).doubleValue();		// 최대매설깊이
		int 	istDur = cal.get(cal.YEAR)-((BigDecimal)subwaySRIMap.get("cmpYmd")).intValue()+1; // 공용기간
		int		grd50  = ((BigDecimal)subwaySRIMap.get("grd50")).intValue();			// 지반함몰(50m)
		int		grd100 = ((BigDecimal)subwaySRIMap.get("grd100")).intValue();			// 지반함몰(100m)
		int		grd200 = ((BigDecimal)subwaySRIMap.get("grd200")).intValue();			// 지반함몰(200m)
		int		rod50  = ((BigDecimal)subwaySRIMap.get("rod50")).intValue();			// 도로함몰(50m)
		int		rod100 = ((BigDecimal)subwaySRIMap.get("rod100")).intValue();			// 도로함몰(100m)
		int		rod200 = ((BigDecimal)subwaySRIMap.get("rod200")).intValue();			// 도로함몰(200m)
		double	neaDis = ((BigDecimal)subwaySRIMap.get("neaDis")).doubleValue();		// 이격거리
		double	neaDep = ((BigDecimal)subwaySRIMap.get("neaDep")).doubleValue();		// 굴착심도
		int		colWel = ((BigDecimal)subwaySRIMap.get("colWel")).intValue();			// 집수정
		
		//=============================
		// [A1] 구조물등급 조건값 계산 
		//=============================
		if      (grdCde.equalsIgnoreCase("A")) grd_cde_value = 0;
		else if (grdCde.equalsIgnoreCase("B")) grd_cde_value = 1.5;
		else if (grdCde.equalsIgnoreCase("C")) grd_cde_value = 3;
		else                                   grd_cde_value = 5;
		
		//===========================
		// [A2] 설계형태 조건값 계산 
		//===========================
		if      (desTyp.equals("비배수")) des_typ_value = 1.5;
		else if (desTyp.equals("배수"))   des_typ_value = 3;
		
		//===============================
		// [A3] 최대매설깊이 조건값 계산 
		//===============================
		if      (maxDep < 3)  max_dep_value = 0;
		else if (maxDep < 10) max_dep_value = 1.5;
		else if (maxDep > 30) max_dep_value = 5;
		else                  max_dep_value = 3;
		
		//===========================
		// [A4] 공용기간 조건값 계산 
		//===========================
		if      (istDur < 3)  ist_dur_value = 0;
		else if (istDur < 10) ist_dur_value = 1.5;
		else if (istDur > 30) ist_dur_value = 5;
		else                  ist_dur_value = 3;
		
		//===========================
		// [A5] 지반함몰 조건값 계산 
		//===========================
		// A5-1 지반함몰(50m) 조건값 계산
		if      (grd50 == 0) grd_50_value = 0;
		else if (grd50 <  2) grd_50_value = 1.5;
		else if (grd50 >  5) grd_50_value = 5;
		else                 grd_50_value = 3;
		
		// A5-2 지반함몰(100m) 조건값 계산
		if      (grd100-grd50 == 0) grd_100_value = 0;
		else if (grd100-grd50 <  3) grd_100_value = 1.5;
		else if (grd100-grd50 >  7) grd_100_value = 5;
		else                        grd_100_value = 3;
		
		// A5-3 지반함몰(200m) 조건값 계산
		if      (grd200-grd100 == 0) grd_200_value = 0;
		else if (grd200-grd100 <  8) grd_200_value = 1.5;
		else if (grd200-grd100 > 20) grd_200_value = 5;
		else                         grd_200_value = 3;
		
		// A5 지반함몰 조건값 계산
		grd_sub_value = grd_50_value /  100 * grd_50_weight
				      + grd_100_value / 100 * grd_100_weight
				      + grd_200_value / 100 * grd_200_weight;
		
		//===========================
		// [A6] 도로함몰 조건값 계산 
		//===========================
		// A6-1 도로함몰(50m) 조건값 계산
		if      (rod50 == 0) rod_50_value = 0;
		else if (rod50 <  1) rod_50_value = 1.5;
		else if (rod50 >  2) rod_50_value = 5;
		else                 rod_50_value = 3;
		
		// A6-2 도로함몰(100m) 조건값 계산
		if      (rod100-rod50 == 0) rod_100_value = 0;
		else if (rod100-rod50 <  2) rod_100_value = 1.5;
		else if (rod100-rod50 >  3) rod_100_value = 5;
		else                        rod_100_value = 3;
		
		// A6-3 도로함몰(200m) 조건값 계산
		if      (rod200-rod100 == 0) rod_200_value = 0;
		else if (rod200-rod100 <  3) rod_200_value = 1.5;
		else if (rod200-rod100 >  4) rod_200_value = 5;
		else                         rod_200_value = 3;
		
		// A6 도로함몰 조건값 계산
		rod_sub_value = rod_50_value /  100 * rod_50_weight
				      + rod_100_value / 100 * rod_100_weight
				      + rod_200_value / 100 * rod_200_weight;
		
		//===========================
		// [A7] 인접굴착 조건값 계산 
		//===========================
		// A7-1 이격거리 조건값 계산
		if      (neaDep == 0) nea_dis_value = 0;
		else if (neaDis > 30) nea_dis_value = 1.5;
		else if (neaDis < 10) nea_dis_value = 5;
		else                  nea_dis_value = 3;
		
		// A7-2 굴착심도 조건값 계산
		if      (neaDep == 0) nea_dep_value = 0;
		else if (neaDep <  3) nea_dep_value = 1.5;
		else if (neaDep > 10) nea_dep_value = 5;
		else                  nea_dep_value = 3;
		
		// A7 인접굴착 조건값 계산
		nea_dig_value = nea_dis_value / 100 * nea_dis_weight
				      + nea_dep_value / 100 * nea_dep_weight;
		
		//=========================
		// [A8] 집수정 조건값 계산 
		//=========================
		if      (colWel < 10)   col_wel_value = 0;
		else if (colWel < 300)  col_wel_value = 1.5;
		else if (colWel > 1000) col_wel_value = 5;
		else                    col_wel_value = 3;
		
		//===============================================
		// [A9] 위험점수 계산 (A1~A8 조건값*가중치의 합)
		//===============================================
		met_sri = grd_cde_value * grd_cde_weight	// A1.구조물등급
				+ des_typ_value * des_typ_weight	// A2.설계형태
				+ max_dep_value * max_dep_weight	// A3.최대매설깊이
				+ ist_dur_value * ist_dur_weight	// A4.공용기간
				+ grd_sub_value * grd_sub_weight	// A5.지반함몰
				+ rod_sub_value * rod_sub_weight	// A6.도로함몰
		        + nea_dig_value * nea_dig_weight	// A7.인접굴착
		        + col_wel_value * col_wel_weight;	// A8.집수정
		
		// 지하철선로 BSRI는 500점 만점
		met_sri = met_sri / 5;
		
		//=====================
		// [A10] 위험등급 계산 
		//=====================
		/*
		if      (met_sri > 60)	met_lev = "A";
		else if (met_sri > 30)	met_lev = "B";
		else 					met_lev = "C";
		*/
		
		// 계산결과 update
		vo.setSriValue(Double.toString(met_sri));
		vo.setSriGrade(UsdmUtils.getSubwayLineSRIGrade(met_sri));
		
		update("apiInterfaceDAO.updateSubwayLineSRI", vo);
		
		return 0;
	}
	
	// 지하철역사 BSRI를 계산하여 update한다.
	public int calculateStationBSRI(SNSriVO vo) throws Exception {
		Calendar cal = Calendar.getInstance();
		
		// [A] 위험점수 계산에 사용하는 변수
		double max_dep_value = 0;				// A1.최대매설깊이 조건값
		double ist_dur_value = 0;				// A2.공용기간 조건값
		double grd_sub_value = 0;				// A3.지반함몰 조건값 (A3-1~3의 결과)
		double rod_sub_value = 0;				// A4.도로함몰 조건값 (A4-1~3의 결과)
		double nea_dig_value = 0;				// A5.인접굴착 조건값 (A5-1~2의 결과)
		double col_wel_value = 0;				// A6.집수정 조건값
		
		final double max_dep_weight = 3;		// A1.최대매설깊이 가중치
		final double ist_dur_weight = 4;		// A2.공용기간 가중치
		final double grd_sub_weight = 50;		// A3.지반함몰 가중치
		final double rod_sub_weight = 15;		// A4.도로함몰 가중치
		final double nea_dig_weight = 10;		// A5.인접굴착 가중치
		final double col_wel_weight = 5;		// A6.집수정 가중치
		
		// [A3] 지반함몰 조건값 계산에 사용하는 변수
		double grd_50_value  = 0;				// A3-1.지반함몰 조건값(50m)
		double grd_100_value = 0;				// A3-2.지반함몰 조건값(100m)
		double grd_200_value = 0;				// A3-3.지반함몰 조건값(200m)
		
		final double grd_50_weight  = 60;		// A3-1.지반함몰 가중치(50m)
		final double grd_100_weight = 30;		// A3-2.지반함몰 가중치(100m)
		final double grd_200_weight = 10;		// A3-3.지반함몰 가중치(200m)
		
		// [A4] 도로함몰 조건값 계산에 사용하는 변수
		double rod_50_value  = 0;				// A4-1.도로함몰 조건값(50m)
		double rod_100_value = 0;				// A4-2.도로함몰 조건값(100m)
		double rod_200_value = 0;				// A4-3.도로함몰 조건값(200m)
		
		final double rod_50_weight  = 60;		// A4-1.도로함몰 가중치(50m)
		final double rod_100_weight = 30;		// A4-2.도로함몰 가중치(100m)
		final double rod_200_weight = 10;		// A4-3.도로함몰 가중치(200m)
		
		// [A5] 인접굴착 조건값 계산에 사용하는 변수
		double nea_dis_value = 0;				// A5-1.이격거리 조건값
		double nea_dep_value = 0;				// A5-2.굴착심도 조건값
		
		final double nea_dis_weight = 50;		// A5-1.이격거리 가중치
		final double nea_dep_weight = 50;		// A5-2.굴착심도 가중치
		
		double sta_sri;							// A7.위험점수(A의 결과)
		String sta_lev;							// A8.위험등급
		
		// 지하철역사 SRI테이블 레코드 조회
		EgovMap stationSRIMap = (EgovMap)select("apiInterfaceDAO.selectSubwayStationSRI", vo);
		
		// 컬럼값 변수설정
		double  maxDep = ((BigDecimal)stationSRIMap.get("maxDep")).doubleValue();		// 최대매설깊이
		int 	istDur = cal.get(cal.YEAR)-((BigDecimal)stationSRIMap.get("cmpYmd")).intValue()+1; // 공용기간
		int		grd50  = ((BigDecimal)stationSRIMap.get("grd50")).intValue();			// 지반함몰(50m)
		int		grd100 = ((BigDecimal)stationSRIMap.get("grd100")).intValue();			// 지반함몰(100m)
		int		grd200 = ((BigDecimal)stationSRIMap.get("grd200")).intValue();			// 지반함몰(200m)
		int		rod50  = ((BigDecimal)stationSRIMap.get("rod50")).intValue();			// 도로함몰(50m)
		int		rod100 = ((BigDecimal)stationSRIMap.get("rod100")).intValue();			// 도로함몰(100m)
		int		rod200 = ((BigDecimal)stationSRIMap.get("rod200")).intValue();			// 도로함몰(200m)
		double	neaDis = ((BigDecimal)stationSRIMap.get("neaDis")).doubleValue();		// 이격거리
		double	neaDep = ((BigDecimal)stationSRIMap.get("neaDep")).doubleValue();		// 굴착심도
		int		colWel = ((BigDecimal)stationSRIMap.get("colWel")).intValue();			// 집수정
		
		//===============================
		// [A1] 최대매설깊이 조건값 계산 
		//===============================
		if      (maxDep < 3)  max_dep_value = 0;
		else if (maxDep < 10) max_dep_value = 1.5;
		else if (maxDep > 30) max_dep_value = 5;
		else                  max_dep_value = 3;
		
		//===========================
		// [A2] 공용기간 조건값 계산 
		//===========================
		if      (istDur < 3)  ist_dur_value = 0;
		else if (istDur < 10) ist_dur_value = 1.5;
		else if (istDur > 30) ist_dur_value = 5;
		else                  ist_dur_value = 3;
		
		//===========================
		// [A3] 지반함몰 조건값 계산 
		//===========================
		// A3-1 지반함몰(50m) 조건값 계산
		if      (grd50 == 0) grd_50_value = 0;
		else if (grd50 <  2) grd_50_value = 1.5;
		else if (grd50 >  5) grd_50_value = 5;
		else                 grd_50_value = 3;
		
		// A3-2 지반함몰(100m) 조건값 계산
		if      (grd100-grd50 == 0) grd_100_value = 0;
		else if (grd100-grd50 <  3) grd_100_value = 1.5;
		else if (grd100-grd50 >  7) grd_100_value = 5;
		else                        grd_100_value = 3;
		
		// A3-3 지반함몰(200m) 조건값 계산
		if      (grd200-grd100 == 0) grd_200_value = 0;
		else if (grd200-grd100 <  8) grd_200_value = 1.5;
		else if (grd200-grd100 > 20) grd_200_value = 5;
		else                         grd_200_value = 3;
		
		// A3 지반함몰 조건값 계산
		grd_sub_value = grd_50_value /  100 * grd_50_weight
				      + grd_100_value / 100 * grd_100_weight
				      + grd_200_value / 100 * grd_200_weight;
		
		//===========================
		// [A4] 도로함몰 조건값 계산 
		//===========================
		// A4-1 도로함몰(50m) 조건값 계산
		if      (rod50 == 0) rod_50_value = 0;
		else if (rod50 <  1) rod_50_value = 1.5;
		else if (rod50 >  2) rod_50_value = 5;
		else                 rod_50_value = 3;
		
		// A4-2 도로함몰(100m) 조건값 계산
		if      (rod100-rod50 == 0) rod_100_value = 0;
		else if (rod100-rod50 <  2) rod_100_value = 1.5;
		else if (rod100-rod50 >  3) rod_100_value = 5;
		else                        rod_100_value = 3;
		
		// A4-3 도로함몰(200m) 조건값 계산
		if      (rod200-rod100 == 0) rod_200_value = 0;
		else if (rod200-rod100 <  3) rod_200_value = 1.5;
		else if (rod200-rod100 >  4) rod_200_value = 5;
		else                         rod_200_value = 3;
		
		// A4 도로함몰 조건값 계산
		rod_sub_value = rod_50_value /  100 * rod_50_weight
				      + rod_100_value / 100 * rod_100_weight
				      + rod_200_value / 100 * rod_200_weight;
		
		//===========================
		// [A5] 인접굴착 조건값 계산 
		//===========================
		// A5-1 이격거리 조건값 계산
		if      (neaDep == 0) nea_dis_value = 0;
		else if (neaDis > 30) nea_dis_value = 1.5;
		else if (neaDis < 10) nea_dis_value = 5;
		else                  nea_dis_value = 3;
		
		// A5-2 굴착심도 조건값 계산
		if      (neaDep == 0) nea_dep_value = 0;
		else if (neaDep <  3) nea_dep_value = 1.5;
		else if (neaDep > 10) nea_dep_value = 5;
		else                  nea_dep_value = 3;
		
		// A5 인접굴착 조건값 계산
		nea_dig_value = nea_dis_value / 100 * nea_dis_weight
				      + nea_dep_value / 100 * nea_dep_weight;
		
		//=========================
		// [A6] 집수정 조건값 계산 
		//=========================
		if      (colWel < 10)   col_wel_value = 0;
		else if (colWel < 300)  col_wel_value = 1.5;
		else if (colWel > 1000) col_wel_value = 5;
		else                    col_wel_value = 3;
		
		//===============================================
		// [A7] 위험점수 계산 (A1~A6 조건값*가중치의 합)
		//===============================================
		sta_sri = max_dep_value * max_dep_weight	// A1.최대매설깊이
				+ ist_dur_value * ist_dur_weight	// A2.공용기간
				+ grd_sub_value * grd_sub_weight	// A3.지반함몰
				+ rod_sub_value * rod_sub_weight	// A4.도로함몰
				+ nea_dig_value * nea_dig_weight	// A5.인접굴착
				+ col_wel_value * col_wel_weight;	// A6.집수정
		
		// 지하철역사 BSRI는 500점 만점
		sta_sri = sta_sri / 5;
		
		//====================
		// [A8] 위험등급 계산 
		//====================
		/*
		if      (sta_sri > 60) 	sta_lev = "A";
		else if (sta_sri > 30) 	sta_lev = "B";
		else 					sta_lev = "C";
		*/
		
		// 계산결과 update
		vo.setSriValue(Double.toString(sta_sri));
		vo.setSriGrade(UsdmUtils.getSubwayStationSRIGrade(sta_sri));
		
		update("apiInterfaceDAO.updateSubwayStationSRI", vo);
		
		return 0;
	}
	
	// SRI Grid 값을 update한다.
	public String updateGridSRI(String geoType, SNSriVO sriVo) throws Exception {
		String selectSqlID;
		String updateSqlID;
		
		switch (geoType) {
		case "water": 	// 상수도
			selectSqlID = "apiInterfaceDAO.selectGridWaterSRI";
			updateSqlID = "apiInterfaceDAO.updateGridWaterSRI";
			break;
			
		case "sewer": 	// 하수도
			selectSqlID = "apiInterfaceDAO.selectGridDrainSRI";
			updateSqlID = "apiInterfaceDAO.updateGridDrainSRI";
			break;
			
		case "subway":	// 지하철
			selectSqlID = "apiInterfaceDAO.selectGridSubwaySRI";
			updateSqlID = "apiInterfaceDAO.updateGridSubwaySRI";
			break;
			
		case "subway_s":	// 지하철역사
			selectSqlID = "apiInterfaceDAO.selectGridStationSRI";
			updateSqlID = "apiInterfaceDAO.updateGridStationSRI";
			break;
			
		case "geology":	// 지질정보(TBD)
		default:
			throw new InvalidParameterException("unsupported geo-object type");
		}
		
		// geo-object가 포함되는 grid cell의 ID와 최대 BSRI값 조회
		List<EgovMap> BSRIList = (List<EgovMap>) list(selectSqlID, sriVo);
		
		int cellID;
		double sriValue;
		String sriGrade = "";
		
		// MQ 메세지에 전송할 값
		String cellIDList    = "";
		String cellValueList = "";
		
		// grid의 SRI값 update
		for (int i=0; i<BSRIList.size(); i++) {
			cellID   = (int)BSRIList.get(i).get("cellid");
			sriValue = ((BigDecimal)BSRIList.get(i).get("sri")).doubleValue();
			
			switch (geoType) {
			case "water": 		// 상수도
				sriGrade = UsdmUtils.getWaterSRIGrade(sriValue);
				break;
			case "sewer": 		// 하수도
				sriGrade = UsdmUtils.getSewerSRIGrade(sriValue);
				break;
			case "subway":		// 지하철
				sriGrade = UsdmUtils.getSubwayLineSRIGrade(sriValue);
				break;
			case "subway_s":	// 지하철역사
				sriGrade = UsdmUtils.getSubwayStationSRIGrade(sriValue);				
				break;
			case "geology":	// 지질정보(TBD)
			default:
				sriGrade = "";
			}
			
			SNSriGridVO gridVo = new SNSriGridVO();
			
			gridVo.setCellID(cellID);
			gridVo.setSri(sriValue);
			gridVo.setGrade(sriGrade);
			gridVo.setLastUpdate(System.currentTimeMillis());
			
			update(updateSqlID, gridVo);
			
			// 그리드의 대표SRI값과 등급 update
			updateSriGridCellGrade(cellID);
			
			cellIDList    += cellID;
			cellValueList += sriValue;
			
			if (i < BSRIList.size()-1) {
				cellIDList    += ",";
				cellValueList += ",";
			}
		}
		
		// ==================
		//   MQ 메세지 전송 
		// ==================
		MessageQueueVO messageVO = new MessageQueueVO();
		messageVO.setEventName(UsdmUtils.MQ_SRICHANGED);
		messageVO.setResourceID(cellIDList);
		messageVO.setValue(cellValueList);
		
		UsdmUtils.sendMessageMQ(messageVO);
		
		// Event 기록 저장
		insertEvent(messageVO);
		
		return cellIDList;
	}
	
	// 그리드 cell의 대표SRI와 등급을 update한다.
	public void updateSriGridCellGrade(int cellID) throws Exception {
		List<EgovMap> queryResult = null; 
		
		// cellID가 0이 아닌 경우(하나의 그리드만 update)
		if (cellID > 0) {
			SNSriGridVO gridVo = new SNSriGridVO();
			gridVo.setCellID(cellID);

			// 지정한 그리드의 geotype별 SRI값과 등급 조회
			queryResult = (List<EgovMap>) list("apiInterfaceDAO.selectSriGridByID", gridVo);
		}
		// cellID가 0인 경우(모든 그리드를 update)
		// 최초 시스템 설치 후 그리드 데이터 초기화에 사용 (query/updateSriGridAll) 
		else {
			// 모든 그리드의 geotype별 SRI값과 등급 조회
			queryResult = (List<EgovMap>) list("apiInterfaceDAO.selectSriGridAll", null);
		}
		
		int sourceIndex;
		
		// layer의 index
		// 이 값은 정렬순서에 따른 결과값 추출에 영향을 미침
		final int drainIndex   = 0;
		final int waterIndex   = 1;
		final int subwayIndex  = 2;
		final int stationIndex = 3;
		final int geologyIndex = 4;
		
		// 각 layer의 명칭
		// List에 추가하는 순서는 index와 동일해야 함
		List<String> sourceLayer = new ArrayList<String>();
		sourceLayer.add("sewer");
		sourceLayer.add("water");
		sourceLayer.add("subway");
		sourceLayer.add("subway_s");
		sourceLayer.add("geology");

		EgovMap queryMap = new EgovMap();
		
		// grid의 SRI값 update
		for (int i=0; i<queryResult.size(); i++) {
			queryMap = (EgovMap)queryResult.get(i);
			
			// 각 layer의 SRI값
			// array에 추가하는 순서는 index와 동일해야 함
			double[] sriArray = new double[5];
			sriArray[drainIndex]   = (double)queryMap.get("drainsri");
			sriArray[waterIndex]   = (double)queryMap.get("watersri");
			sriArray[subwayIndex]  = (double)queryMap.get("subwaysri");
			sriArray[stationIndex] = (double)queryMap.get("stationsri");
			sriArray[geologyIndex] = (double)queryMap.get("geologysri");
			
			String[] gradeArray = new String[5];
			gradeArray[drainIndex]   = (String)queryResult.get(i).get("draingrade");
			gradeArray[waterIndex]   = (String)queryResult.get(i).get("watergrade");
			gradeArray[subwayIndex]  = (String)queryResult.get(i).get("subwaygrade");
			gradeArray[stationIndex] = (String)queryResult.get(i).get("stationgrade");
			gradeArray[geologyIndex] = (String)queryResult.get(i).get("geologygrade");
			
			// 그리드의 대표SRI값 선정
			// 1순위는 등급, 2순위는 점수
			// 1. 동일등급일 경우 상/하수도가 지하철선로/역사에 우선한다. (상/하수도에 우선순위 부여(5),지하철선로/역사(3),지질정보(1))
			// 2. 상/하수도가 동일한 등급일 경우 높은 점수가 대표값이 된다.
			//    지하철선로/역사가 동일한 등급일 경우 높은 점수가 대표값이 된다.
			// 3. 상/하수도가 동일한 등급과 동일한 점수를 갖는 경우 하수도가 대표값이 된다.
			//    지하철선로/역사가 동일한 등급과 동일한 점수를 갖는 경우 선로가 대표값이 된다.
			
			// 선정 방식은 2가지에 의존한다.
			// 1. Java의 문자열 정렬방식에 의해 등급, 우선순위, 000.00으로 formatting된 SRI값을 연결한 문자열 정렬
			// 2. 동일한 등급, 우선순위, SRI값을 갖는 경우 List의 index가 작은 항목이 선택됨
			//    하수도가 상수도에 우선하므로 gradeList 내에서 더 작은 index를 갖는다.(지하철선로/역사도 마찬가지)
			
			// List에 추가하는 순서는 index와 동일해야 함
			List<String> gradeList = new ArrayList<String>();
			gradeList.add(UsdmUtils.getAlignedSriStr((String)queryMap.get("draingrade"),   sriArray[drainIndex],   5));
			gradeList.add(UsdmUtils.getAlignedSriStr((String)queryMap.get("watergrade"),   sriArray[waterIndex],   5));
			gradeList.add(UsdmUtils.getAlignedSriStr((String)queryMap.get("subwaygrade"),  sriArray[subwayIndex],  3));
			gradeList.add(UsdmUtils.getAlignedSriStr((String)queryMap.get("stationgrade"), sriArray[stationIndex], 3));
			gradeList.add(UsdmUtils.getAlignedSriStr((String)queryMap.get("geologygrade"), sriArray[geologyIndex], 1));
			
			// 대표BSRI의 index
			sourceIndex = gradeList.indexOf(Collections.max(gradeList));
			
			// grid의 대표SRI와 등급 update
			SNSriGridVO updateGridVo = new SNSriGridVO();
			
			updateGridVo.setCellID((int)queryMap.get("cellid"));
			updateGridVo.setSri(sriArray[sourceIndex]);
			updateGridVo.setGrade(gradeArray[sourceIndex]);
			updateGridVo.setLastUpdate(System.currentTimeMillis());
			
			update("apiInterfaceDAO.updateSriGridAll", updateGridVo);
		}
	}
	
	// query/waterPipe2WGS
	public List<?> selectWaterPipeGeometry() throws Exception {
		return list("apiInterfaceDAO.selectWaterPipeGeometry", null);
	}
	
	// query/drainPipe2WGS
	public List<?> selectDrainPipeGeometry() throws Exception {
		return list("apiInterfaceDAO.selectDrainPipeGeometry", null);
	}
	// query/drainManhole2WGS
	public List<?> selectDrainManholeGeometry() throws Exception {
		return list("apiInterfaceDAO.selectDrainManholeGeometry", null);
	}
	
	// query/updateSriGridAll
	public void updateSriGridAll() throws Exception {
		updateSriGridCellGrade(0);
	}

	// RabbitMQ 메세지가 전송될 때 event 기록을 저장한다
	public void insertEvent(MessageQueueVO messageVO) throws Exception {
		long eventTime = 0;
		
		if (messageVO.getTimestamp() != null) {
			eventTime = UsdmUtils.convertStrToDate(messageVO.getTimestamp(), "yyyyMMdd'T'HHmmss");
		}
		else {
			eventTime = System.currentTimeMillis();
		}
		
		EventVO vo = new EventVO();
		vo.setEventName(messageVO.getEventName());
		vo.setResourceID(messageVO.getResourceID());
		vo.setEventValue(messageVO.getValue());
		vo.setEventTime(eventTime);
		
		insert("apiInterfaceDAO.insertEvent", vo);
	}
		
}
