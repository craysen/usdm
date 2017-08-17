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

import java.util.List;

import javax.annotation.Resource;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.usdm.service.*;
import egovframework.rte.psl.dataaccess.util.EgovMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("apiInterfaceService")
public class APIInterfaceServiceImpl extends EgovAbstractServiceImpl implements APIInterfaceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(APIInterfaceServiceImpl.class);

	@Resource(name = "apiInterfaceDAO")
	private APIInterfaceDAO apiInterfaceDAO;

	public int validateSessionKey(LoginSessionVO vo) throws Exception {
		return apiInterfaceDAO.validateSessionKey(vo);
	}
	
	// query/latestValue
	public List<?> selectSensingValueQuery(SNSensingValueVO vo) throws Exception {
		return apiInterfaceDAO.selectSensingValueQuery(vo);
	}
	public List<?> selectSensingValueLatest(SNSensingValueVO vo) throws Exception {
		return apiInterfaceDAO.selectSensingValueLatest(vo);
	}
	
	// query/latestValueByID(2)
	public List<?> selectSensingValueQueryByID(SNSensingValueVO vo) throws Exception {
		return apiInterfaceDAO.selectSensingValueQueryByID(vo);
	}
	public List<?> selectSensingValueLatestByID(SNSensingValueVO vo) throws Exception {
		return apiInterfaceDAO.selectSensingValueLatestByID(vo);
	}
	
	// query/spatioTemporal
	// query/spatioTemporalXY(2)
	public List<?> selectSensingValueSpatioTemporal(SNSensingValueVO vo) throws Exception {
		return apiInterfaceDAO.selectSensingValueSpatioTemporal(vo);
	}
	
	// query/spatioTemporalByID(2)
	// query/spatioTemporalXYByID(2)
	public List<?> selectSensingValueSpatioTemporalByID(SNSensingValueVO vo) throws Exception {
		return apiInterfaceDAO.selectSensingValueSpatioTemporalByID(vo);
	}
	
	// query/accidentByGeoID(2)
	// query/accidentByGeoID2(2)
	public List<?> selectAccident(SNAccidentVO vo) throws Exception {
		return apiInterfaceDAO.selectAccident(vo);
	}
	
	// query/accidentByRegion(2)
	public List<?> selectAccidentByRegion(SNAccidentVO vo) throws Exception {
		return apiInterfaceDAO.selectAccidentByRegion(vo);
	}
	
	// query/valueByGeoObject(2)
	public List<?> selectValueByGeoObject(SNGeoRelationVO vo) throws Exception {
		return apiInterfaceDAO.selectValueByGeoObject(vo);
	}
	
	// query/movingObject(2)
	public List<?> selectSewerMovingObject(GeoVideoRelVO vo) throws Exception {
		return apiInterfaceDAO.selectSewerMovingObject(vo);
	}
	public List<?> selectSewerVideoGeoID(GeoVideoRelVO vo) throws Exception {
		return apiInterfaceDAO.selectSewerVideoGeoID(vo);
	}
	public List<?> selectSubwayMovingObject(GeoVideoRelVO vo) throws Exception {
		return apiInterfaceDAO.selectSubwayMovingObject(vo);
	}
	public List<?> selectSubwayVideoGeoID(GeoVideoRelVO vo) throws Exception {
		return apiInterfaceDAO.selectSubwayVideoGeoID(vo);
	}
	
	// query/sriGrid(2)
	public List<?> selectSriGrid(SNSriGridVO vo) throws Exception {
		return apiInterfaceDAO.selectSriGrid(vo);
	}
	
	// query/xSriGrid(2)
	public List<?> selectXSriGrid(SNSriGridVO vo) throws Exception {
		return apiInterfaceDAO.selectXSriGrid(vo);
	}
	
	// query/getGridByColor(3)
	public List<?> selectGridByColor(SNSriGridVO vo) throws Exception {
		return apiInterfaceDAO.selectGridByColor(vo);
	}
	
	// query/getGridBySRI(3)
	public List<?> selectGridBySRI(SNSriGridVO vo) throws Exception {
		return apiInterfaceDAO.selectGridBySRI(vo);
	}
	
	// query/getInfraByColor(3)
	public List<?> selectInfraByColor(SNSriVO vo) throws Exception {
		return apiInterfaceDAO.selectInfraByColor(vo);
	}
		
	// query/getInfraBySRI(3)
	public List<?> selectInfraBySRI(SNSriVO vo) throws Exception {
		return apiInterfaceDAO.selectInfraBySRI(vo);
	}
	
	// query/getInfraByAttribute(3)
	public List<?> selectInfraByAttribute(SNSriVO vo) throws Exception {
		return apiInterfaceDAO.selectInfraByAttribute(vo);
	}
	
	// query/getInfraInGrid(3)
	// query/getInfraInGridBySRI(3)
	public List<?> selectInfraInGrid(SNSriGridVO vo) throws Exception {
		return apiInterfaceDAO.selectInfraInGrid(vo);
	}
	
	// query/getSensingValueByID(3)
	public List<?> selectSensingValueByID(SNSensingValueVO vo) throws Exception {
		return apiInterfaceDAO.selectSensingValueByID(vo);
	}
	
	// query/repairHistory(3)
	public List<?> selectInfraRepair(InfraRepairVO vo) throws Exception {
		return apiInterfaceDAO.selectInfraRepair(vo);
	}
	
	// query/repairHistorySubsidence(3)
	public List<?> selectSubsidenceRepair(SubsidenceRepairVO vo) throws Exception {
		return apiInterfaceDAO.selectSubsidenceRepair(vo);
	}
	
	// query/getEvent(3)
	public List<?> selectEvent(EventVO vo) throws Exception {
		return apiInterfaceDAO.selectEvent(vo);
	}
		
	// query/getWaterManhole(2)
	public List<?> selectWaterManholePipeRel(ManholePipeRelVO vo) throws Exception {
		return apiInterfaceDAO.selectWaterManholePipeRel(vo);
	}
	
	// information/gatewayIDList
	@Override
	public List<?> selectGatewayIDList() throws Exception {
		return apiInterfaceDAO.selectGatewayIDList();
	}
	
	// information/gatewayIDList2(2)
	@Override
	public List<?> selectGatewayIDList2() throws Exception {
		return apiInterfaceDAO.selectGatewayIDList2();
	}

	// information/resourceDescription
	@Override
	public List<?> selectGatewayDescription(SNGatewayVO vo) throws Exception {
		return apiInterfaceDAO.selectGatewayDescription(vo);
	}
	@Override
	public List<?> selectPanDescriptionByGwID(SNGatewayVO vo) throws Exception {
		return apiInterfaceDAO.selectPanDescriptionByGwID(vo);
	}
	@Override
	public List<?> selectNodeDescriptionByGwID(SNGatewayVO vo) throws Exception {
		return apiInterfaceDAO.selectNodeDescriptionByGwID(vo);
	}
	@Override
	public List<?> selectTransducerDescriptionByGwID(SNGatewayVO vo) throws Exception {
		return apiInterfaceDAO.selectTransducerDescriptionByGwID(vo);
	}
	
	@Override
	public List<?> selectPanDescription(SNPanVO vo) throws Exception {
		return apiInterfaceDAO.selectPanDescription(vo);
	}
	@Override
	public List<?> selectNodeDescriptionByPanID(SNPanVO vo) throws Exception {
		return apiInterfaceDAO.selectNodeDescriptionByPanID(vo);
	}
	@Override
	public List<?> selectTransducerDescriptionByPanID(SNPanVO vo) throws Exception {
		return apiInterfaceDAO.selectTransducerDescriptionByPanID(vo);
	}
	
	@Override
	public List<?> selectNodeDescription(SNNodeVO vo) throws Exception {
		return apiInterfaceDAO.selectNodeDescription(vo);
	}
	@Override
	public List<?> selectTransducerDescriptionBySnID(SNNodeVO vo) throws Exception {
		return apiInterfaceDAO.selectTransducerDescriptionBySnID(vo);
	}
	
	@Override
	public List<?> selectTransducerDescription(SNTransducerVO vo) throws Exception {
		return apiInterfaceDAO.selectTransducerDescription(vo);
	}
	
	// information/resourceDescriptionByID(2)
	@Override
	public List<?> selectGatewayDescriptionByGID(SNGatewayVO vo) throws Exception {
		return apiInterfaceDAO.selectGatewayDescriptionByGID(vo);
	}
	@Override
	public List<?> selectPanDescriptionByGwGID(SNGatewayVO vo) throws Exception {
		return apiInterfaceDAO.selectPanDescriptionByGwGID(vo);
	}
	@Override
	public List<?> selectNodeDescriptionByGwGID(SNGatewayVO vo) throws Exception {
		return apiInterfaceDAO.selectNodeDescriptionByGwGID(vo);
	}
	@Override
	public List<?> selectTransducerDescriptionByGwGID(SNGatewayVO vo) throws Exception {
		return apiInterfaceDAO.selectTransducerDescriptionByGwGID(vo);
	}
	
	@Override
	public List<?> selectPanDescriptionByGID(SNPanVO vo) throws Exception {
		return apiInterfaceDAO.selectPanDescriptionByGID(vo);
	}
	@Override
	public List<?> selectNodeDescriptionByPanGID(SNPanVO vo) throws Exception {
		return apiInterfaceDAO.selectNodeDescriptionByPanGID(vo);
	}
	@Override
	public List<?> selectTransducerDescriptionByPanGID(SNPanVO vo) throws Exception {
		return apiInterfaceDAO.selectTransducerDescriptionByPanGID(vo);
	}
	
	@Override
	public List<?> selectNodeDescriptionByGID(SNNodeVO vo) throws Exception {
		return apiInterfaceDAO.selectNodeDescriptionByGID(vo);
	}
	@Override
	public List<?> selectTransducerDescriptionBySnGID(SNNodeVO vo) throws Exception {
		return apiInterfaceDAO.selectTransducerDescriptionBySnGID(vo);
	}
	
	@Override
	public List<?> selectTransducerDescriptionByGID(SNTransducerVO vo) throws Exception {
		return apiInterfaceDAO.selectTransducerDescriptionByGID(vo);
	}
	
	// information/resourceStatus
	@Override
	public List<?> selectGatewayStatus(SNGatewayVO vo) throws Exception {
		return apiInterfaceDAO.selectGatewayStatus(vo);
	}
	@Override
	public List<?> selectGatewayStatusDescent(SNGatewayVO vo) throws Exception {
		return apiInterfaceDAO.selectGatewayStatusDescent(vo);
	}

	@Override
	public List<?> selectPanStatus(SNPanVO vo) throws Exception {
		return apiInterfaceDAO.selectPanStatus(vo);
	}
	@Override
	public List<?> selectPanStatusDescent(SNPanVO vo) throws Exception {
		return apiInterfaceDAO.selectPanStatusDescent(vo);
	}
	
	@Override
	public List<?> selectNodeStatus(SNNodeVO vo) throws Exception {
		return apiInterfaceDAO.selectNodeStatus(vo);
	}
	@Override
	public List<?> selectNodeStatusDescent(SNNodeVO vo) throws Exception {
		return apiInterfaceDAO.selectNodeStatusDescent(vo);
	}
	
	@Override
	public List<?> selectTransducerStatus(SNTransducerVO vo) throws Exception {
		return apiInterfaceDAO.selectTransducerStatus(vo);
	}
	
	// information/resourceStatusByID(2)
	@Override
	public List<?> selectGatewayStatusByGID(SNGatewayVO vo) throws Exception {
		return apiInterfaceDAO.selectGatewayStatusByGID(vo);
	}
	@Override
	public List<?> selectPanStatusByGwGID(SNGatewayVO vo) throws Exception {
		return apiInterfaceDAO.selectPanStatusByGwGID(vo);
	}
	@Override
	public List<?> selectNodeStatusByGwGID(SNGatewayVO vo) throws Exception {
		return apiInterfaceDAO.selectNodeStatusByGwGID(vo);
	}
	@Override
	public List<?> selectTransducerStatusByGwGID(SNGatewayVO vo) throws Exception {
		return apiInterfaceDAO.selectTransducerStatusByGwGID(vo);
	}
	
	@Override
	public List<?> selectPanStatusByGID(SNPanVO vo) throws Exception {
		return apiInterfaceDAO.selectPanStatusByGID(vo);
	}
	@Override
	public List<?> selectNodeStatusByPanGID(SNPanVO vo) throws Exception {
		return apiInterfaceDAO.selectNodeStatusByPanGID(vo);
	}
	@Override
	public List<?> selectTransducerStatusByPanGID(SNPanVO vo) throws Exception {
		return apiInterfaceDAO.selectTransducerStatusByPanGID(vo);
	}
	
	@Override
	public List<?> selectNodeStatusByGID(SNNodeVO vo) throws Exception {
		return apiInterfaceDAO.selectNodeStatusByGID(vo);
	}
	@Override
	public List<?> selectTransducerStatusBySnGID(SNNodeVO vo) throws Exception {
		return apiInterfaceDAO.selectTransducerStatusBySnGID(vo);
	}
	
	@Override
	public List<?> selectTransducerStatusByGID(SNTransducerVO vo) throws Exception {
		return apiInterfaceDAO.selectTransducerStatusByGID(vo);
	}
	
	// information/connectivity(2)
	public List<?> selectConnectivity(SNGeoRelationVO vo) throws Exception {
		return apiInterfaceDAO.selectConnectivity(vo);
	}
		
	// information/connectivityUpdate(2)
	public int insertNodeGeoRelationByGeoID(SNGeoRelationVO vo) throws Exception {
		int result = 0;
		
		try {
			LOGGER.debug(vo.toString());
			result = apiInterfaceDAO.insertNodeGeoRelationByGeoID(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	public int insertNodeGeoRelationBySnGID(SNGeoRelationVO vo) throws Exception {
		int result = 0;
		
		try {
			LOGGER.debug(vo.toString());
			result = apiInterfaceDAO.insertNodeGeoRelationBySnGID(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
		
	// information/updateDesc(2)
	public int updateGwDescription(SNGatewayVO vo) throws Exception {
		int result = 0;
		
		try {
			result = apiInterfaceDAO.updateGwDescription(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	public int updatePanDescription(SNPanVO vo) throws Exception {
		int result = 0;
		
		try {
			result = apiInterfaceDAO.updatePanDescription(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	public int updateSnDescription(SNNodeVO vo) throws Exception {
		int result = 0;
		
		try {
			result = apiInterfaceDAO.updateSnDescription(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	public int updateTdDescription(SNTransducerVO vo) throws Exception {
		int result = 0;
		
		try {
			result = apiInterfaceDAO.updateTdDescription(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	// information/updateDescByID(2)
	public int updateGwDescriptionByID(SNGatewayVO vo) throws Exception {
		int result = 0;
		
		try {
			result = apiInterfaceDAO.updateGwDescriptionByID(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	public int updatePanDescriptionByID(SNPanVO vo) throws Exception {
		int result = 0;
		
		try {
			result = apiInterfaceDAO.updatePanDescriptionByID(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	public int updateSnDescriptionByID(SNNodeVO vo) throws Exception {
		int result = 0;
		
		try {
			result = apiInterfaceDAO.updateSnDescriptionByID(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	public int updateTdDescriptionByID(SNTransducerVO vo) throws Exception {
		int result = 0;
		
		try {
			result = apiInterfaceDAO.updateTdDescriptionByID(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	// information/registerAccident(2)
	@Override
	public int insertAccident(SNAccidentVO vo) throws Exception {
		int result = 0;
		
		try {
			result = apiInterfaceDAO.insertAccident(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	// information/setThreshold(2)
	public void insertThreshold(SNSensingValueVO vo) throws Exception {
		try {
			apiInterfaceDAO.insertThreshold(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// information/mapWaterManhole(2)
	public void insertWaterManholePipeRel(ManholePipeRelVO vo) throws Exception {
		try {
			apiInterfaceDAO.insertWaterManholePipeRel(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// information/setLeakThreshold(2)
	public void insertLeakThreshold(SNSensingValueVO vo) throws Exception {
		try {
			apiInterfaceDAO.insertLeakThreshold(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// information/repairInfra(3)
	public String insertInfraRepair(InfraRepairVO vo) throws Exception {
		String result = "";
		
		try {
			result = apiInterfaceDAO.insertInfraRepair(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	public EgovMap selectInfraRepairGeoChange(InfraRepairVO vo) throws Exception {
		return apiInterfaceDAO.selectInfraRepairGeoChange(vo);
	}
	public List<?> selectInfraRepairGridChanged(InfraRepairVO vo) throws Exception {
		return apiInterfaceDAO.selectInfraRepairGridChanged(vo);
	}
	
	// information/repairSubsidence(3)
	public String insertSubsidenceRepair(SubsidenceRepairVO vo) throws Exception {
		String result = "";
		
		try {
			result = apiInterfaceDAO.insertSubsidenceRepair(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	public List<?> selectSubsidenceRepairSewerChange(SubsidenceRepairVO vo) throws Exception {
		return apiInterfaceDAO.selectSubsidenceRepairSewerChange(vo);
	}
	public List<?> selectSubsidenceRepairSubwayChange(SubsidenceRepairVO vo) throws Exception {
		return apiInterfaceDAO.selectSubsidenceRepairSubwayChange(vo);
	}
	public List<?> selectSubsidenceRepairStationChange(SubsidenceRepairVO vo) throws Exception {
		return apiInterfaceDAO.selectSubsidenceRepairStationChange(vo);
	}
		
	// information/geoobjectListRequest(2)
	public List<?> selectGeoobjectList(SNSensingValueVO vo) throws Exception {
		return apiInterfaceDAO.selectGeoobjectList(vo);
	}
	
	// information/firmwareDataStore(2)
	public String insertFirmwareData(SNFirmwareVO vo) throws Exception {
		String firmwareID = "";
		
		try {
			LOGGER.debug(vo.toString());
			firmwareID = apiInterfaceDAO.insertFirmwareData(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return firmwareID;
	}
	
	// information/firmwareListRequest(2)
	public List<?> selectFirmwareList(SNFirmwareVO vo) throws Exception {
		return apiInterfaceDAO.selectFirmwareList(vo);
	}
	
	// information/firmwareDataRequest(2)
	public EgovMap selectFirmwareData(SNFirmwareVO vo) throws Exception {
		return apiInterfaceDAO.selectFirmwareData(vo);
	}
		
	// information/firmwareDataDelete(2)
	public int deleteFirmwareData(SNFirmwareVO vo) throws Exception {
		int result = 0;
		
		try {
			result = apiInterfaceDAO.deleteFirmwareData(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	// information/firmwareUpdateHistoryStore(2)
	public boolean selectExistingFirmwareData(SNFirmwareVO vo) throws Exception {
		return apiInterfaceDAO.selectExistingFirmwareData(vo);
	}
	public int insertFirmwareUpdateHistory(SNFirmwareUpdateHistoryVO vo) throws Exception {
		int result = 0;
		
		try {
			LOGGER.debug(vo.toString());
			result = apiInterfaceDAO.insertFirmwareUpdateHistory(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	// information/firmwareUpdateHistoryRequest(2)
	public List<?> selectFirmwareUpdateHistory(SNFirmwareUpdateHistoryVO vo) throws Exception {
		return apiInterfaceDAO.selectFirmwareUpdateHistory(vo);
	}
	public List<?> selectFirmwareUpdateHistoryTargetList(SNFirmwareUpdateHistoryVO vo) throws Exception {
		return apiInterfaceDAO.selectFirmwareUpdateHistoryTargetList(vo);
	}
	
	// information/transducerDefaultDescription(2)
	public int insertTdDefault(SNTransducerVO vo) throws Exception {
		int result = 0;
		
		try {
			result = apiInterfaceDAO.insertTdDefault(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	// information/transducerDefaultRequest(2)
	public List<?> selectTdDefault(SNTransducerVO vo) throws Exception {
		return apiInterfaceDAO.selectTdDefault(vo);
	}
	
	// information/transducerDefaultUpdate(2)
	public int updateTdDefault(SNTransducerVO vo) throws Exception {
		int result = 0;
		
		try {
			result = apiInterfaceDAO.updateTdDefault(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	// information/transducerDefaultDelete(2)
	public int deleteTdDefault(SNTransducerVO vo) throws Exception {
		int result = 0;
		
		try {
			result = apiInterfaceDAO.deleteTdDefault(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	// information/userDefinedMessageStore(2)
	public int insertUserDefinedMessage(UserDefinedMessageVO vo) throws Exception {
		int result = 0;
		
		try {
			LOGGER.debug(vo.toString());
			result = apiInterfaceDAO.insertUserDefinedMessage(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	// information/userDefinedMessageRequest(2)
	public List<?> selectUserDefinedMessage(UserDefinedMessageVO vo) throws Exception {
		return apiInterfaceDAO.selectUserDefinedMessage(vo);
	}
	
	// sri/getWaterBSRIByID(2)
	public List<?> getWaterBSRIByID(SNSriVO vo) throws Exception {
		return apiInterfaceDAO.getWaterBSRIByID(vo);
	}
	
	// sri/getWaterBSRIByRegion(2)
	public List<?> getWaterBSRIByRegion(SNSriVO vo) throws Exception {
		return apiInterfaceDAO.getWaterBSRIByRegion(vo);
	}
	
	// sri/getSewerBSRIByID(2)
	public List<?> getSewerBSRIByID(SNSriVO vo) throws Exception {
		return apiInterfaceDAO.getSewerBSRIByID(vo);
	}
	
	// sri/getSewerBSRIByRegion(2)
	public List<?> getSewerBSRIByRegion(SNSriVO vo) throws Exception {
		return apiInterfaceDAO.getSewerBSRIByRegion(vo);
	}
	
	// sri/getMLineBSRIByID(2)
	public List<?> getSubwayBSRIByID(SNSriVO vo) throws Exception {
		return apiInterfaceDAO.getSubwayBSRIByID(vo);
	}
	
	// sri/getMLineBSRIByRegion(2)
	public List<?> getSubwayBSRIByRegion(SNSriVO vo) throws Exception {
		return apiInterfaceDAO.getSubwayBSRIByRegion(vo);
	}
	
	// sri/getMStationBSRIByID(2)
	public List<?> getStationBSRIByID(SNSriVO vo) throws Exception {
		return apiInterfaceDAO.getStationBSRIByID(vo);
	}
	
	// sri/getMStationBSRIByRegion(2)
	public List<?> getStationBSRIByRegion(SNSriVO vo) throws Exception {
		return apiInterfaceDAO.getStationBSRIByRegion(vo);
	}
	
	// sri/updateWaterBSRIByID(2)
	public void updateWaterBSRIByID(SNSriVO vo) throws Exception {
		apiInterfaceDAO.updateWaterBSRIByID(vo);
	}
	
	// sri/updateSewerBSRIByID(2)
	public void updateSewerBSRIByID(SNSriVO vo) throws Exception {
		apiInterfaceDAO.updateSewerBSRIByID(vo);
	}
	
	// sri/updateMLineBSRIByID(2)
	public void updateSubwayBSRIByID(SNSriVO vo) throws Exception {
		apiInterfaceDAO.updateSubwayBSRIByID(vo);
	}
	
	// sri/updateMStationBSRIByID(2)
	public void updateStationBSRIByID(SNSriVO vo) throws Exception {
		apiInterfaceDAO.updateStationBSRIByID(vo);
	}
		
	// sri/insertAssessValues
	@Override
	public void insertAssessValues(SNAssessValueVO vo) throws Exception {
		try {
			LOGGER.debug(vo.toString());
			apiInterfaceDAO.insertAssessValues(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// sri/retrieveAssessValues
	@Override
	public List<?> retrieveAssessValues(SNAssessValueVO vo) throws Exception {
		return apiInterfaceDAO.retrieveAssessValues(vo);
	}
	
	// sri/assessUtilities
	
	// sri/assessSRI
		
	// sri/retrieveRiskValues
	@Override
	public List<?> retrieveRiskValues(SNAssessValueVO vo) throws Exception {
		return apiInterfaceDAO.retrieveRiskValues(vo);
	}
	
	// rfid/initializeRFID(2)
	public void insertRfid(RfidVO vo) throws Exception {
		apiInterfaceDAO.insertRfid(vo);
	}
	
	// rfid/updateRFID(2)
	public void updateRfid(RfidVO vo) throws Exception {
		apiInterfaceDAO.updateRfid(vo);
	}
	
	// rfid/getRFID(2)
	public List<?> selectRfid(RfidVO vo) throws Exception {
		return apiInterfaceDAO.selectRfid(vo);
	}
	
	// query/waterPipe2WGS
	@Override
	public List<?> selectWaterPipeGeometry() throws Exception {
		return apiInterfaceDAO.selectWaterPipeGeometry();
	}
	
	// query/drainPipe2WGS
	@Override
	public List<?> selectDrainPipeGeometry() throws Exception {
		return apiInterfaceDAO.selectDrainPipeGeometry();
	}
	// query/drainManhole2WGS
	@Override
	public List<?> selectDrainManholeGeometry() throws Exception {
		return apiInterfaceDAO.selectDrainManholeGeometry();
	}

	// query/updateSriGridAll
	public void updateSriGridAll() throws Exception {
		apiInterfaceDAO.updateSriGridAll();
	}
		
}