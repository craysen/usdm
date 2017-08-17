/*
 * Copyright 2011 MOPAS(Ministry of Public Administration and Security).
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

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.usdm.service.EventVO;
import egovframework.usdm.service.InfraRepairVO;
import egovframework.usdm.service.LoginSessionVO;
import egovframework.usdm.service.ManholePipeRelVO;
import egovframework.usdm.service.RfidVO;
import egovframework.usdm.service.SNAccidentVO;
import egovframework.usdm.service.SNAssessValueVO;
import egovframework.usdm.service.SNFirmwareUpdateHistoryVO;
import egovframework.usdm.service.SNFirmwareVO;
import egovframework.usdm.service.SNGatewayVO;
import egovframework.usdm.service.SNGeoRelationVO;
import egovframework.usdm.service.SNNodeVO;
import egovframework.usdm.service.SNPanVO;
import egovframework.usdm.service.SNSensingValueVO;
import egovframework.usdm.service.SNSriGridVO;
import egovframework.usdm.service.SNSriVO;
import egovframework.usdm.service.SNTransducerVO;
import egovframework.usdm.service.GeoVideoRelVO;
import egovframework.usdm.service.SubsidenceRepairVO;
import egovframework.usdm.service.UserDefinedMessageVO;

@Mapper("apiInterfaceMapper")
public interface APIInterfaceMapper {

	int validateSessionKey(LoginSessionVO vo) throws Exception;
	
	// query/latestValue
	List<?> selectSensingValueQuery(SNSensingValueVO vo) throws Exception;
	List<?> selectSensingValueLatest(SNSensingValueVO vo) throws Exception;
	
	// query/latestValueByID(2)
	List<?> selectSensingValueQueryByID(SNSensingValueVO vo) throws Exception;
	List<?> selectSensingValueLatestByID(SNSensingValueVO vo) throws Exception;
	
	// query/spatioTemporal
	// query/spatioTemporalXY(2)
	List<?> selectSensingValueSpatioTemporal(SNSensingValueVO vo) throws Exception;
	
	// query/spatioTemporalByID(2)
	// query/spatioTemporalXYByID(2)
	List<?> selectSensingValueSpatioTemporalByID(SNSensingValueVO vo) throws Exception;
	
	// query/accidentByGeoID(2)
	// query/accidentByGeoID2(2)
	List<?> selectAccident(SNAccidentVO vo) throws Exception;
	
	// query/accidentByGeoIDByRegion(2)
	List<?> selectAccidentByRegion(SNAccidentVO vo) throws Exception;
	
	// query/valueByGeoObject(2)
	List<?> selectValueByGeoObject(SNGeoRelationVO vo) throws Exception;
	
	// query/movingObject(2)
	List<?> selectSewerMovingObject(GeoVideoRelVO vo) throws Exception;
	List<?> selectSewerVideoGeoID(GeoVideoRelVO vo) throws Exception;
	List<?> selectSubwayMovingObject(GeoVideoRelVO vo) throws Exception;
	List<?> selectSubwayVideoGeoID(GeoVideoRelVO vo) throws Exception;
		
	// query/sriGrid(2)
	List<?> selectSriGrid(SNSriGridVO vo) throws Exception;
	
	// query/xSriGrid(2)
	List<?> selectXSriGrid(SNSriGridVO vo) throws Exception;
	
	// query/getGridByColor(3)
	List<?> selectGridByColor(SNSriGridVO vo) throws Exception;
		
	// query/getGridBySRI(3)
	List<?> selectGridBySRI(SNSriGridVO vo) throws Exception;
	
	// query/getInfraByColor(3)
	List<?> selectInfraByColor(SNSriVO vo) throws Exception;
	
	// query/getInfraBySRI(3)
	List<?> selectInfraBySRI(SNSriVO vo) throws Exception;
	
	// query/getInfraByAttribute(3)
	List<?> selectInfraByAttribute(SNSriVO vo) throws Exception;
	
	// query/getInfraInGrid(3)
	// query/getInfraInGridBySRI(3)
	List<?> selectInfraInGrid(SNSriGridVO vo) throws Exception;
	
	// query/getSensingValueByID(3)
	List<?> selectSensingValueByID(SNSensingValueVO vo) throws Exception;
		
	// query/repairHistory(3)
	List<?> selectInfraRepair(InfraRepairVO vo) throws Exception;
	
	// query/repairHistorySubsidence(3)
	List<?> selectSubsidenceRepair(SubsidenceRepairVO vo) throws Exception;
	
	// query/getEvent(3)
	List<?> selectEvent(EventVO vo) throws Exception;
	
	// query/getWaterManhole(2)
	List<?> selectWaterManholePipeRel(ManholePipeRelVO vo) throws Exception;
	
	// information/gatewayIDList
	List<?> selectGatewayIDList() throws Exception;
	
	// information/gatewayIDList2(2)
	List<?> selectGatewayIDList2() throws Exception;
	
	// information/resourceDescription
	List<?> selectGatewayDescription(SNGatewayVO vo) throws Exception;
	List<?> selectPanDescriptionByGwID(SNGatewayVO vo) throws Exception;
	List<?> selectNodeDescriptionByGwID(SNGatewayVO vo) throws Exception;
	List<?> selectTransducerDescriptionByGwID(SNGatewayVO vo) throws Exception;
	
	List<?> selectPanDescription(SNPanVO vo) throws Exception;
	List<?> selectNodeDescriptionByPanID(SNPanVO vo) throws Exception;
	List<?> selectTransducerDescriptionByPanID(SNPanVO vo) throws Exception;
	
	List<?> selectNodeDescription(SNNodeVO vo) throws Exception;
	List<?> selectTransducerDescriptionBySnID(SNNodeVO vo) throws Exception;
	
	List<?> selectTransducerDescription(SNTransducerVO vo) throws Exception;
	
	// information/resourceDescriptionByID(2)
	List<?> selectGatewayDescriptionByGID(SNGatewayVO vo) throws Exception;
	List<?> selectPanDescriptionByGwGID(SNGatewayVO vo) throws Exception;
	List<?> selectNodeDescriptionByGwGID(SNGatewayVO vo) throws Exception;
	List<?> selectTransducerDescriptionByGwGID(SNGatewayVO vo) throws Exception;
	
	List<?> selectPanDescriptionByGID(SNPanVO vo) throws Exception;
	List<?> selectNodeDescriptionByPanGID(SNPanVO vo) throws Exception;
	List<?> selectTransducerDescriptionByPanGID(SNPanVO vo) throws Exception;
	
	List<?> selectNodeDescriptionByGID(SNNodeVO vo) throws Exception;
	List<?> selectTransducerDescriptionBySnGID(SNNodeVO vo) throws Exception;
	
	List<?> selectTransducerDescriptionByGID(SNTransducerVO vo) throws Exception;

	// information/resourceStatus
	List<?> selectGatewayStatus(SNGatewayVO vo) throws Exception;
	List<?> selectGatewayStatusDescent(SNGatewayVO vo) throws Exception;
	
	List<?> selectPanStatus(SNPanVO vo) throws Exception;
	List<?> selectPanStatusDescent(SNPanVO vo) throws Exception;
	
	List<?> selectNodeStatus(SNNodeVO vo) throws Exception;
	List<?> selectNodeStatusDescent(SNNodeVO vo) throws Exception;
	
	List<?> selectTransducerStatus(SNTransducerVO vo) throws Exception;
	
	// information/resourceStatusByID(2)
	List<?> selectGatewayStatusByGID(SNGatewayVO vo) throws Exception;
	List<?> selectPanStatusByGwGID(SNGatewayVO vo) throws Exception;
	List<?> selectNodeStatusByGwGID(SNGatewayVO vo) throws Exception;
	List<?> selectTransducerStatusByGwGID(SNGatewayVO vo) throws Exception;
	
	List<?> selectPanStatusByGID(SNPanVO vo) throws Exception;
	List<?> selectNodeStatusByPanGID(SNPanVO vo) throws Exception;
	List<?> selectTransducerStatusByPanGID(SNPanVO vo) throws Exception;
	
	List<?> selectNodeStatusByGID(SNNodeVO vo) throws Exception;
	List<?> selectTransducerStatusBySnGID(SNNodeVO vo) throws Exception;
	
	List<?> selectTransducerStatusByGID(SNTransducerVO vo) throws Exception;

	// information/connectivity(2)
	List<?> selectConnectivity(SNGeoRelationVO vo) throws Exception;
	
	// information/connectivityUpdate(2)
	int insertNodeGeoRelationByGeoID(SNGeoRelationVO vo) throws Exception;
	int insertNodeGeoRelationBySnGID(SNGeoRelationVO vo) throws Exception;
		
	// information/updateDesc(2)
	int updateGwDescription(SNGatewayVO vo) throws Exception;
	int updatePanDescription(SNPanVO vo) throws Exception;
	int updateSnDescription(SNNodeVO vo) throws Exception;
	int updateTdDescription(SNTransducerVO vo) throws Exception;
	
	// information/updateDescByID(2)
	int updateGwDescriptionByID(SNGatewayVO vo) throws Exception;
	int updatePanDescriptionByID(SNPanVO vo) throws Exception;
	int updateSnDescriptionByID(SNNodeVO vo) throws Exception;
	int updateTdDescriptionByID(SNTransducerVO vo) throws Exception;
	
	// information/registerAccident(2)
	int insertAccident(SNAccidentVO vo) throws Exception;
	
	// information/setThreshold(2)
	void insertThreshold(SNSensingValueVO vo) throws Exception;
	
	// information/mapWaterManhole(2)
	void insertWaterManholePipeRel(ManholePipeRelVO vo) throws Exception;
		
	// information/setLeakThreshold(2)
	void insertLeakThreshold(SNSensingValueVO vo) throws Exception;
	
	// information/repairInfra(3)
	String insertInfraRepair(InfraRepairVO vo) throws Exception;
	EgovMap selectInfraRepairGeoChange(InfraRepairVO vo) throws Exception;
	List<?> selectInfraRepairGridChanged(InfraRepairVO vo) throws Exception;
	
	// information/repairSubsidence(3)
	String insertSubsidenceRepair(SubsidenceRepairVO vo) throws Exception;
	List<?> selectSubsidenceRepairSewerChange(SubsidenceRepairVO vo) throws Exception;
	List<?> selectSubsidenceRepairSubwayChange(SubsidenceRepairVO vo) throws Exception;
	List<?> selectSubsidenceRepairStationChange(SubsidenceRepairVO vo) throws Exception;
	
	// information/geoobjectListRequest(2)
	List<?> selectGeoobjectList(SNSensingValueVO vo) throws Exception;
	
	// information/firmwareDataStore(2)
	String insertFirmwareData(SNFirmwareVO vo) throws Exception;
		
	// information/firmwareListRequest(2)
	List<?> selectFirmwareList(SNFirmwareVO vo) throws Exception;
	
	// information/firmwareDataRequest(2)
	EgovMap selectFirmwareData(SNFirmwareVO vo) throws Exception;
	
	// information/firmwareDataDelete(2)
	int deleteFirmwareData(SNFirmwareVO vo) throws Exception;
	
	// information/firmwareUpdateHistoryStore(2)
	boolean selectExistingFirmwareData(SNFirmwareVO vo) throws Exception;
	int insertFirmwareUpdateHistory(SNFirmwareUpdateHistoryVO vo) throws Exception;
	
	// information/firmwareUpdateHistoryRequest(2)
	List<?> selectFirmwareUpdateHistory(SNFirmwareUpdateHistoryVO vo) throws Exception;
	List<?> selectFirmwareUpdateHistoryTargetList(SNFirmwareUpdateHistoryVO vo) throws Exception;
		
	// information/transducerDefaultDescription(2)
	int insertTdDefault(SNTransducerVO vo) throws Exception;
	
	// information/transducerDefaultRequest(2)
	List<?> selectTdDefault(SNTransducerVO vo) throws Exception;
	
	// information/transducerDefaultUpdate(2)
	int updateTdDefault(SNTransducerVO vo) throws Exception;
	
	// information/transducerDefaultDelete(2)
	int deleteTdDefault(SNTransducerVO vo) throws Exception;
	
	// information/userDefinedMessageStore(2)
	int insertUserDefinedMessage(UserDefinedMessageVO vo) throws Exception;
	
	// information/userDefinedMessageRequest(2)
	List<?> selectUserDefinedMessage(UserDefinedMessageVO vo) throws Exception;
	
	// sri/getWaterBSRIByID(2)
	List<?> getWaterBSRIByID(SNSriVO vo) throws Exception;
	
	// sri/getWaterBSRIByRegion(2)
	List<?> getWaterBSRIByRegion(SNSriVO vo) throws Exception;
	
	// sri/getSewerBSRIByID(2)
	List<?> getSewerBSRIByID(SNSriVO vo) throws Exception;
	
	// sri/getSewerBSRIByRegion(2)
	List<?> getSewerBSRIByRegion(SNSriVO vo) throws Exception;
	
	// sri/getMLineBSRIByID(2)
	List<?> getSubwayBSRIByID(SNSriVO vo) throws Exception;
	
	// sri/getMLineBSRIByRegion(2)
	List<?> getSubwayBSRIByRegion(SNSriVO vo) throws Exception;
	
	// sri/getMStationBSRIByID(2)
	List<?> getStationBSRIByID(SNSriVO vo) throws Exception;
	
	// sri/getMStationBSRIByRegion(2)
	List<?> getStationBSRIByRegion(SNSriVO vo) throws Exception;
		
	// sri/updateWaterBSRIByID(2)
	void updateWaterBSRIByID(SNSriVO vo) throws Exception;
	
	// sri/updateSewerBSRIByID(2)
	void updateSewerBSRIByID(SNSriVO vo) throws Exception;
	
	// sri/updateMLineBSRIByID(2)
	void updateSubwayBSRIByID(SNSriVO vo) throws Exception;
	
	// sri/updateMStationBSRIByID(2)
	void updateStationBSRIByID(SNSriVO vo) throws Exception;
		
	// sri/insertAssessValues
	void insertAssessValues(SNAssessValueVO vo) throws Exception;
	
	// sri/retrieveAssessValues
	List<?> retrieveAssessValues(SNAssessValueVO vo) throws Exception;
	
	// sri/assessUtilities
	
	// sri/assessSRI
	
	// sri/retrieveRiskValues
	List<?> retrieveRiskValues(SNAssessValueVO vo) throws Exception;

	// rfid/initializeRFID(2)
	void insertRfid(RfidVO vo) throws Exception;
	
	// rfid/updateRFID(2)
	void updateRfid(RfidVO vo) throws Exception;
	
	// rfid/getRFID(2)
	List<?> selectRfid(RfidVO vo) throws Exception;
		
	// query/waterPipe2WGS
	List<?> selectWaterPipeGeometry() throws Exception;
	
	// query/drainPipe2WGS
	List<?> selectDrainPipeGeometry() throws Exception;
	
	// query/drainManhole2WGS
	List<?> selectDrainManholeGeometry() throws Exception;

	// query/updateSriGridAll
	void updateSriGridAll() throws Exception;
}
