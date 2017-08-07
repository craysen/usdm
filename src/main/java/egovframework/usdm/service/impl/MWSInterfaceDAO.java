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
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.usdm.service.*;
import egovframework.usdm.web.util.InvalidParameterException;
import egovframework.usdm.web.util.UsdmUtils;

import org.springframework.stereotype.Repository;

@Repository("mwsInterfaceDAO")
public class MWSInterfaceDAO extends EgovAbstractDAO {

	public int selectLoginSession(LoginSessionVO vo) throws Exception {
		List<EgovMap> userList = (List<EgovMap>) list("mwsInterfaceDAO.selectLoginSession", vo);
		
		// 존재하지 않는 사용자 (-1)
		if (userList.size() == 0)
			return -1;
		
		// 패스워드 불일치 (1)
		if (!userList.get(0).get("userpw").equals(vo.getUserPW()))
			return 1;
		
		// 정상적으로 로그인 (0)
		return 0;
	}
	
	public void updateSessionKeyLogin(LoginSessionVO vo) throws Exception {
		update("mwsInterfaceDAO.updateSessionKeyLogin", vo);
	}
	
	public int selectLogoutSession(LoginSessionVO vo) throws Exception {
		List<EgovMap> userList = (List<EgovMap>) list("mwsInterfaceDAO.selectLoginSession", vo);
		
		// 로그인하지 않은 사용자 (-1)
		if (userList.size() == 0 || userList.get(0).get("sessionkey").equals(""))
			return -1;
		
		// 세션키 불일치 (1)
		if (!userList.get(0).get("sessionkey").equals(vo.getSessionKey()))
			return 1;
		
		// 정상적으로 로그아웃 (0)
		return 0;
	}
	
	public void updateSessionKeyLogout(LoginSessionVO vo) throws Exception {
		update("mwsInterfaceDAO.updateSessionKeyLogout", vo);
	}
	
	public String insertGateway(SNGatewayVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingGateway", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			insert("mwsInterfaceDAO.insertGateway", vo);
		else
			update("mwsInterfaceDAO.updateGateway", vo);
		
		return "";
	}
	
	public String insertGatewayByID(SNGatewayVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingGatewayByID", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			insert("mwsInterfaceDAO.insertGatewayByID", vo);
		else
			update("mwsInterfaceDAO.updateGatewayByID", vo);
		
		return "";
	}
	
	public String insertPan(SNPanVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingPan", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			insert("mwsInterfaceDAO.insertPan", vo);
		else
			update("mwsInterfaceDAO.updatePan", vo);
		
		return "";
	}
	
	public String insertPanByID(SNPanVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingPanByID", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			insert("mwsInterfaceDAO.insertPanByID", vo);
		else
			update("mwsInterfaceDAO.updatePanByID", vo);
		
		return "";
	}
	
	public String insertNode(SNNodeVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingNode", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			insert("mwsInterfaceDAO.insertNode", vo);
		else
			update("mwsInterfaceDAO.updateNode", vo);
		
		return "";
	}
	
	public String insertNodeByID(SNNodeVO vo) throws Exception {
		
		// 주소가 동일한 노드가 존재하면 해당 레코드를 삭제한다 (2016.11.23)
		delete("mwsInterfaceDAO.deleteNode", vo);

		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingNodeByID", vo);
		
		// GID가 동일한 노드가 존재하면 해당 레코드를 update한다
		if (existYn.get(0).get("existyn").equals("Y"))
			update("mwsInterfaceDAO.updateNodeByID", vo);			
		else
			insert("mwsInterfaceDAO.insertNodeByID", vo);
		
		// 생성된 노드와 geo-object연결성 정보 생성
		if (!vo.getGeoTable().equals("")) {
			int result = updateSnConnectivity(vo);
			if (result < 0) throw new Exception();
		}
		
		return "";
	}
	
	public String insertTransducer(SNTransducerVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingTransducer", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			insert("mwsInterfaceDAO.insertTransducer", vo);
		else
			update("mwsInterfaceDAO.updateTransducer", vo);
		
		return "";
	}
	
	public int updateGatewayAddress(SNGatewayVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingGateway", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("mwsInterfaceDAO.updateTransducerGwID", vo);
		update("mwsInterfaceDAO.updateNodeGwID", vo);
		update("mwsInterfaceDAO.updatePanGwID", vo);
		update("mwsInterfaceDAO.updateGatewayGwID", vo);
		//select("mwsInterfaceDAO.updateGatewayAddress", vo);
		
		return 0;
	}
	
	public int updatePanAddress(SNPanVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingPan", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("mwsInterfaceDAO.updateTransducerPanID", vo);
		update("mwsInterfaceDAO.updateNodePanID", vo);
		update("mwsInterfaceDAO.updatePanPanID", vo);
		//select("mwsInterfaceDAO.updatePanAddress", vo);
		
		return 0;
	}
	
	public int updateNodeAddress(SNNodeVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingNode", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("mwsInterfaceDAO.updateTransducerSnID", vo);
		update("mwsInterfaceDAO.updateNodeSnID", vo);
		//select("mwsInterfaceDAO.updateNodeAddress", vo);
		
		return 0;
	}
	
	public int updateTransducerAddress(SNTransducerVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingTransducer", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("mwsInterfaceDAO.updateTransducerTdID", vo);
		//select("mwsInterfaceDAO.updateTransducerAddress", vo);
		
		return 0;
	}
	
	public int updateGatewayAddressByGID(SNGatewayVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingGatewayByID", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("mwsInterfaceDAO.updateTransducerGwIDByGID", vo);
		update("mwsInterfaceDAO.updateNodeGwIDByGID", vo);
		update("mwsInterfaceDAO.updatePanGwIDByGID", vo);
		update("mwsInterfaceDAO.updateGatewayGwIDByGID", vo);
		
		return 0;
	}
	
	public int updatePanAddressByGID(SNPanVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingPanByID", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("mwsInterfaceDAO.updateTransducerPanIDByGID", vo);
		update("mwsInterfaceDAO.updateNodePanIDByGID", vo);
		update("mwsInterfaceDAO.updatePanPanIDByGID", vo);
		
		return 0;
	}
	
	public int updateNodeAddressByGID(SNNodeVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingNodeByID", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("mwsInterfaceDAO.updateTransducerSnIDByGID", vo);
		update("mwsInterfaceDAO.updateNodeSnIDByGID", vo);
		
		return 0;
	}
	
	public int deleteGateway(SNGatewayVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingGateway", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("mwsInterfaceDAO.updateGatewayDeleteCode", vo);
		update("mwsInterfaceDAO.updatePanDeleteCodeGw", vo);
		update("mwsInterfaceDAO.updateNodeDeleteCodeGw", vo);
		update("mwsInterfaceDAO.updateTransducerDeleteCodeGw", vo);
		
		return 0;
	}
	public int deletePan(SNPanVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingPan", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("mwsInterfaceDAO.updatePanDeleteCode", vo);
		update("mwsInterfaceDAO.updateNodeDeleteCodePan", vo);
		update("mwsInterfaceDAO.updateTransducerDeleteCodePan", vo);
		
		return 0;
	}
	public int deleteNode(SNNodeVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingNode", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("mwsInterfaceDAO.updateNodeDeleteCode", vo);
		update("mwsInterfaceDAO.updateTransducerDeleteCodeSn", vo);
		
		return 0;
	}
	public int deleteTransducer(SNTransducerVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingTransducer", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("mwsInterfaceDAO.updateTransducerDeleteCode", vo);
		
		return 0;
	}

	public int deleteGatewayDescent(SNGatewayVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingGateway", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("mwsInterfaceDAO.updateGatewayDeleteCode", vo);
		update("mwsInterfaceDAO.updatePanDeleteCodeGw", vo);
		update("mwsInterfaceDAO.updateNodeDeleteCodeGw", vo);
		update("mwsInterfaceDAO.updateTransducerDeleteCodeGw", vo);
		
		return 0;
	}
	public int deletePanDescent(SNPanVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingPan", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("mwsInterfaceDAO.updatePanDeleteCode", vo);
		update("mwsInterfaceDAO.updateNodeDeleteCodePan", vo);
		update("mwsInterfaceDAO.updateTransducerDeleteCodePan", vo);
		
		return 0;
	}
	public int deleteNodeDescent(SNNodeVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingNode", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("mwsInterfaceDAO.updateNodeDeleteCode", vo);
		update("mwsInterfaceDAO.updateTransducerDeleteCodeSn", vo);
		
		return 0;
	}
	
	public int deleteGatewayByGID(SNGatewayVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingGatewayByID", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("mwsInterfaceDAO.updateGatewayDeleteCodeByGID", vo);
		update("mwsInterfaceDAO.updatePanDeleteCodeGwByGID", vo);
		update("mwsInterfaceDAO.updateNodeDeleteCodeGwByGID", vo);
		update("mwsInterfaceDAO.updateTransducerDeleteCodeGwByGID", vo);
		
		return 0;
	}
	public int deletePanByGID(SNPanVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingPanByID", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("mwsInterfaceDAO.updatePanDeleteCodeByGID", vo);
		update("mwsInterfaceDAO.updateNodeDeleteCodePanByGID", vo);
		update("mwsInterfaceDAO.updateTransducerDeleteCodePanByGID", vo);
		
		return 0;
	}
	public int deleteNodeByGID(SNNodeVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingNodeByID", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("mwsInterfaceDAO.updateNodeDeleteCodeByGID", vo);
		update("mwsInterfaceDAO.updateTransducerDeleteCodeSnByGID", vo);
		
		return 0;
	}
	
	public int insertSensingValue(SNSensingValueVO vo) throws Exception {
		// 먼저 노드의 존재여부를 조회한다.
		SNNodeVO nodeVO = new SNNodeVO();
		nodeVO.setGwID(vo.getGwID());
		nodeVO.setPanID(vo.getPanID());
		nodeVO.setSnID(vo.getSnID());
		
		EgovMap nodeExist = (EgovMap) select("mwsInterfaceDAO.selectExistingNode", nodeVO);
		
		// 존재하지 않는 노드인 경우 invalid 데이터에 저장
		if (nodeExist.get("existyn").equals("N")) {
			insert("mwsInterfaceDAO.insertSensingValueInvalid", vo);
			return 1;
		}
		
		// 센싱값 범위 조회
		List<EgovMap> range = (List<EgovMap>) list("mwsInterfaceDAO.selectSensingValueRange", vo);
		
		// 범위 조회결과가 없으면(td가 없으면) invalid 데이터에 저장
		if (range.isEmpty()) {
			insert("mwsInterfaceDAO.insertSensingValueInvalid", vo);
			return -1;
		}
		
		EgovMap rangeElement = range.get(0);
		
		double rangeMin = (double)rangeElement.get("rangemin");
		double rangeMax = (double)rangeElement.get("rangemax");
		double value	= vo.getSensingValue();
		
		if (value < rangeMin || value > rangeMax) {
			insert("mwsInterfaceDAO.insertSensingValueInvalid", vo);
			return -1;
		}
		
		// 센싱값 저장
		try {
			insert("mwsInterfaceDAO.insertSensingValue", vo);
		
		} catch (Exception e) {
			return 2;
		}
		
		// 기존 최신센싱값 삭제 후 새로 생성
		delete("mwsInterfaceDAO.deleteSensingValueLatest", vo);
		insert("mwsInterfaceDAO.insertSensingValueLatest", vo);
		
		return 0;
	}
	
	public int insertSensingValueByGID(SNSensingValueVO vo) throws Exception {
		// 먼저 노드의 존재여부를 조회한다.
		SNNodeVO nodeVO = new SNNodeVO();
		nodeVO.setGID(vo.getSnGID());
		
		EgovMap nodeExist = (EgovMap) select("mwsInterfaceDAO.selectExistingNodeByID", nodeVO);
		
		// 존재하지 않는 노드인 경우 invalid 데이터에 저장
		if (nodeExist.get("existyn").equals("N")) {
			insert("mwsInterfaceDAO.insertSensingValueInvalidByGID", vo);
			return 1;
		}
		
		// 센싱값 범위 조회
		List<EgovMap> range = (List<EgovMap>) list("mwsInterfaceDAO.selectSensingValueRangeByGID", vo);
		
		// 범위 조회결과가 없으면(td가 없으면) invalid 데이터에 저장
		if (range==null || range.isEmpty()) {
			insert("mwsInterfaceDAO.insertSensingValueInvalidByGID", vo);
			return -1;
		}

		// 범위에 포함되지 않으면 invalid 데이터에 저장
		EgovMap rangeElement = range.get(0);
		double rangeMin = (double)rangeElement.get("rangemin");
		double rangeMax = (double)rangeElement.get("rangemax");
		double value	= vo.getSensingValue();
		
		if (value < rangeMin || value > rangeMax) {
			insert("mwsInterfaceDAO.insertSensingValueInvalidByGID", vo);
			return -1;
		}
		
		// 센싱값 저장
		try {
			insert("mwsInterfaceDAO.insertSensingValueByGID", vo);
		
		} catch (Exception e) {
			return 2;
		}
		
		// 기존 최신센싱값 삭제 후 새로 생성
		delete("mwsInterfaceDAO.deleteSensingValueLatestByGID", vo);
		insert("mwsInterfaceDAO.insertSensingValueLatestByGID", vo);
		
		return 0;
	}
	
	// 센싱값의 sensorType이 threshold를 초과하는 경우 alert발생
	public void checkSensingValueThreshold(SNSensingValueVO vo) throws Exception {
		// node와 연결된 맨홀-상수관의 ID를 조회
		List<EgovMap> waterPipeIDList = new ArrayList<EgovMap>();
		
		// report/sensingValue에서 호출한 경우
		if (vo.getGwID() != null)
			waterPipeIDList = (List<EgovMap>) list("mwsInterfaceDAO.selectNodeRelatedWaterPipe", vo);
		// report/sensingValueByGID에서 호출한 경우
		else
			waterPipeIDList = (List<EgovMap>) list("mwsInterfaceDAO.selectNodeRelatedWaterPipeByGID", vo);
		
		vo.setGeoIDList(UsdmUtils.getInOperatorString(waterPipeIDList, "ftrIdn"));
		
		// 상수관의 lek_sig값 update
		update("mwsInterfaceDAO.updateWaterPipeLekSig", vo);

		for (int i=0; i<waterPipeIDList.size(); i++) {
			SNSriVO sriVO = new SNSriVO();
			sriVO.setGeoID(String.valueOf((int)waterPipeIDList.get(i).get("ftrIdn")));

			// 상수관 BSRI 재계산
			calculateWaterBSRI(sriVO);
			
			// 그리드 SRI 재계산
			updateGridSRI("water", sriVO);	
		}
					
		// 센싱값의 threshold 조회
		EgovMap thresholdResult = (EgovMap)select("mwsInterfaceDAO.selectSensingValueThreshold", vo);
		
		// threshold가 존재하지 않으면 return
		if (thresholdResult == null) return;
		
		String operator1 = (String)thresholdResult.get("operator1");
		double operand1  = (double)thresholdResult.get("operand1");
		String logicalOp = (String)thresholdResult.get("logicalop");
		String operator2 = (String)thresholdResult.get("operator2");
		double operand2  = (double)thresholdResult.get("operand2");
		
		boolean operation  = false;
		boolean operation1 = false;
		boolean operation2 = false;
		
		if (logicalOp == null) {
			operation = UsdmUtils.getOperationResult(operator1, vo.getSensingValue(), operand1);
		}
		else {
			operation1 = UsdmUtils.getOperationResult(operator1, vo.getSensingValue(), operand1);
			operation2 = UsdmUtils.getOperationResult(operator2, vo.getSensingValue(), operand2);
			
			if (logicalOp.equals("AND")) {
				operation = operation1 && operation2;
			}
			else if (logicalOp.equals("OR")) {
				operation = operation1 || operation2;
			}
		}
		
		// 센싱값이 threshold를 벗어나면
		if (operation) {
			// ==================
			//   MQ 메세지 전송 
			// ==================
			MessageQueueVO messageVO = new MessageQueueVO();
			messageVO.setEventName("leakOccurred");
			messageVO.setValue(String.valueOf(vo.getSensingValue()));
			
			if (vo.getGwID() != null)
				messageVO.setResourceID(vo.getGwID() + ":" + vo.getPanID() + ":" + vo.getSnID());
			else
				messageVO.setResourceID(vo.getGID());
			
			UsdmUtils.sendMessageMQ(messageVO);
		}
	}
	
	// 상수도 누수 센서 센싱값이 threshold를 초과하는 경우 alert발생
	public void checkLeakageThreshold(SNSensingValueVO vo) throws Exception {

		// ADC->mV 변환 레코드 생성
		SNSensingValueVO mvVO = new SNSensingValueVO();
		
		mvVO.setSnGID(vo.getSnGID());
		mvVO.setGID(vo.getGID());
		mvVO.setTdID(vo.getTdID());
		mvVO.setReportTime(vo.getReportTime());
		mvVO.setSensingTime(vo.getSensingTime());
		mvVO.setSensorType("WSP_SOUND_MV");
		mvVO.setSensingValue(convertADCtoMV((int)vo.getSensingValue()));
		
		// DB변환에 필요한 기준값 조회
		EgovMap mvResult = (EgovMap)select("mwsInterfaceDAO.selectLeakageThreshold", mvVO);
				
		// ADC->dB 변환 레코드 생성
		SNSensingValueVO dbVO = new SNSensingValueVO();
		
		dbVO.setSnGID(vo.getSnGID());
		dbVO.setGID(vo.getGID());
		dbVO.setTdID(vo.getTdID());
		dbVO.setReportTime(vo.getReportTime());
		dbVO.setSensingTime(vo.getSensingTime());
		dbVO.setSensorType("WSP_SOUND_DB");
		dbVO.setSensingValue(convertADCtoDB((int)vo.getSensingValue(), (double)mvResult.get("operand")));

		// mV와 dB로 변환한 센싱값 레코드 저장
		insert("mwsInterfaceDAO.insertSensingValueByGID", mvVO);
		insert("mwsInterfaceDAO.insertSensingValueLatestByGID", mvVO);
		
		insert("mwsInterfaceDAO.insertSensingValueByGID", dbVO);
		insert("mwsInterfaceDAO.insertSensingValueLatestByGID", dbVO);
		
		// node와 연결된 맨홀-상수관의 ID를 조회
		List<EgovMap> waterPipeIDList = (List<EgovMap>) list("mwsInterfaceDAO.selectNodeRelatedWaterPipeByGID", dbVO);

		// 연결된 상수관이 있는 경우 BSRI 및 그리드 update
		//if (waterPipeIDList.size() > 0) {
		if (waterPipeIDList != null) {
			vo.setGeoIDList(UsdmUtils.getInOperatorString(waterPipeIDList, "ftrIdn"));
			
			// 상수관의 lek_sig값 update
			update("mwsInterfaceDAO.updateWaterPipeLekSig", vo);
		
			for (int i=0; i<waterPipeIDList.size(); i++) {
				SNSriVO sriVO = new SNSriVO();
				sriVO.setGeoID(String.valueOf((int)waterPipeIDList.get(i).get("ftrIdn")));
				
				// 상수관 BSRI 재계산
				int result = calculateWaterBSRI(sriVO);
				
				// 그리드 SRI 재계산
				if (result==0) updateGridSRI("water", sriVO);	
			}
		}
		
		// 센싱값의 threshold 조회
		EgovMap thresholdResult = (EgovMap)select("mwsInterfaceDAO.selectLeakageThreshold", dbVO);
		
		// threshold가 존재하면
		if (thresholdResult != null) {
			String	operator 		= (String)thresholdResult.get("operator");
			double	operand  		= (double)thresholdResult.get("operand");
			int 	counter      	= (int)thresholdResult.get("counter");
			int 	counterLimit	= (int)thresholdResult.get("counterlimit");
			
			// threshold의 operator가 GT인 경우
			if (operator.equalsIgnoreCase("GT")) {
				// 센싱값이 threshold를 벗어나면
				if (UsdmUtils.getOperationResult(operator, dbVO.getSensingValue(), operand)) {
					
					// 누수counter가 limit에 도달한 경우 alert
					if (++counter >= counterLimit) {
						// ==================
						//   MQ 메세지 전송 
						// ==================
						MessageQueueVO messageVO = new MessageQueueVO();
						messageVO.setEventName("leakOccurred");
						messageVO.setValue(String.valueOf(dbVO.getSensingValue()));
						messageVO.setResourceID(dbVO.getGID());
						
						UsdmUtils.sendMessageMQ(messageVO);
					}
					
					// counter update
					dbVO.setCounter(counter);
					update("mwsInterfaceDAO.updateLeakageThresholdCounter", dbVO);
				}
				// 센싱값이 threshold를 벗어나지 않으면
				else {
					// threshold를 현재 센싱값으로 변경
					// operand와 counter update
					dbVO.setCounter(0);
					update("mwsInterfaceDAO.updateLeakageThresholdCounter2", dbVO);
				}
			}
		}
	}
	
	// ADC 값을 mV로 변환한다.
	private double convertADCtoMV(int adcValue) {
		return 1.65 * adcValue / 4194304 + 1.65;
	}
	
	// ADC 값을 dB로 변환한다.
	private double convertADCtoDB(int adcValue, double mvValue2) {
		return 20 * Math.log10(convertADCtoMV(adcValue) / mvValue2);
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
		EgovMap waterSRIMap = (EgovMap)select("mwsInterfaceDAO.selectWaterPipeSRI", vo);

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
		if      (wtl_sri >= 55) wtl_lev = "C";
		else if (wtl_sri >= 24)	wtl_lev = "B";
		else 					wtl_lev = "A";
		
		/******************************
		 * [B] 위험점수,등급 계산 END *
		 ******************************/
				
		// 계산결과 update
		vo.setAssessValue(Double.toString(wtl_sta));
		vo.setSriValue(Double.toString(wtl_sri));
		vo.setSriGrade(wtl_lev);
		
		update("mwsInterfaceDAO.updateWaterPipeSRI", vo);
		
		return 0;
	}
	
	// SRI Grid 값을 update한다.
	public int updateGridSRI(String geoType, SNSriVO sriVo) throws Exception {
		String selectSqlID;
		String updateSqlID;
		
		switch (geoType) {
		case "water": 	// 상수도
			selectSqlID = "mwsInterfaceDAO.selectGridWaterSRI";
			updateSqlID = "mwsInterfaceDAO.updateGridWaterSRI";
			break;
			
		case "sewer": 	// 하수도
			selectSqlID = "mwsInterfaceDAO.selectGridDrainSRI";
			updateSqlID = "mwsInterfaceDAO.updateGridDrainSRI";
			break;
			
		case "subway":	// 지하철
			selectSqlID = "mwsInterfaceDAO.selectGridSubwaySRI";
			updateSqlID = "mwsInterfaceDAO.updateGridSubwaySRI";
			break;
			
		case "subway_s":	// 지하철역사
			selectSqlID = "mwsInterfaceDAO.selectGridStationSRI";
			updateSqlID = "mwsInterfaceDAO.updateGridStationSRI";
			break;
			
		case "geology":	// 지질정보(TBD)
		default:
			throw new InvalidParameterException("unsupported geo-object type");
		}
		
		// geo-object가 포함되는 grid cell의 ID와 최소BSRI값 조회
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
			case "sewer": 		// 하수도
				if      (sriValue >= 55) 	sriGrade = "C";
				else if (sriValue >= 24) 	sriGrade = "B";
				else						sriGrade = "A";
				
				break;
				
			case "subway":		// 지하철
			case "subway_s":	// 지하철역사
				if      (sriValue >= 60) 	sriGrade = "C";
				else if (sriValue >= 30) 	sriGrade = "B";
				else						sriGrade = "A";
				
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
		messageVO.setEventName("sriValueChanged");
		messageVO.setResourceID(cellIDList);
		messageVO.setValue(cellValueList);
		
		UsdmUtils.sendMessageMQ(messageVO);
		
		return 0;
	}
	
	public void insertBinaryValue(SNSensingValueVO vo) throws Exception {
		insert("mwsInterfaceDAO.insertBinaryValue", vo);
	}
	
	public void insertBinaryValueByGID(SNSensingValueVO vo) throws Exception {
		insert("mwsInterfaceDAO.insertBinaryValueByGID", vo);
	}
	
	public int updateActuationResult(SNTransducerVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingTransducer", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("mwsInterfaceDAO.updateActuationResult", vo);
		
		return 0;
	}
	
	public int updateActuationResultByGID(SNTransducerVO vo) throws Exception {
		update("mwsInterfaceDAO.updateActuationResultByGID", vo);
		
		return 0;
	}
	
	public int updateGatewayStatus(SNGatewayVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingGateway", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("mwsInterfaceDAO.updateGatewayStatusCode", vo);
		
		return 0;
	}
	
	public int updatePanStatus(SNPanVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingPan", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("mwsInterfaceDAO.updatePanStatusCode", vo);
		
		return 0;
	}
	
	public int updateNodeStatus(SNNodeVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingNode", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("mwsInterfaceDAO.updateNodeStatusCode", vo);
		
		return 0;
	}
	
	public int updateTransducerStatus(SNTransducerVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingTransducer", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("mwsInterfaceDAO.updateTransducerStatusCode", vo);
		
		return 0;
	}
	
	public int validateSessionKey(LoginSessionVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.validateSessionKey", vo);
		
		if (existYn.get(0).get("validyn").equals("N"))
			return -1;
		
		return 0;
	}
	
	public int updateGatewayStatusByGID(SNGatewayVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingGatewayByID", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("mwsInterfaceDAO.updateGatewayStatusCodeByGID", vo);
		
		return 0;
	}
	
	public int updatePanStatusByGID(SNPanVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingPanByID", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("mwsInterfaceDAO.updatePanStatusCodeByGID", vo);
		
		return 0;
	}
	
	public int updateNodeStatusByGID(SNNodeVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingNodeByID", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("mwsInterfaceDAO.updateNodeStatusCodeByGID", vo);
		
		return 0;
	}
	
	// 노드와 geo-object의 연결성 정보를 생성하고, 노드의 위치를 geo-object와 동일하게 변경
	public int updateSnConnectivity(SNNodeVO vo) throws Exception {
		// 가장 가까운 geo-object 탐색(ftr_cde, ftr_idn, 좌표)
		EgovMap nearestGeoObject = (EgovMap) select("mwsInterfaceDAO.selectNearestGeoObject", vo);
		
		// 탐색된 geo-object가 없으면 연결성 정보를 생성하지 않고 종료
		if (nearestGeoObject == null) return 0;
		
		double tmX = (double)nearestGeoObject.get("x");
		double tmY = (double)nearestGeoObject.get("y");
		
		vo.setFtr_cde((String)nearestGeoObject.get("ftrcde"));
		vo.setFtr_idn(Integer.toString((int)nearestGeoObject.get("ftridn")));
		vo.setX(String.valueOf(tmX));
		vo.setY(String.valueOf(tmY));
		vo.setLongitude(String.valueOf(UsdmUtils.TMEastToLongitude(tmY, tmX)));
		vo.setLatitude(String.valueOf(UsdmUtils.TMNorthToLatitude(tmY, tmX)));
				
		// node와 geo-object의 현재관계 조회
		EgovMap relExistYn = (EgovMap) select("mwsInterfaceDAO.selectExistingNodeGeoRel", vo);
		
		// 탐색된 geo-object와의 현재관계가 존재하지 않으면
		if (relExistYn.get("existyn").equals("N"))
		{
			// 기존의 다른 geo-object와의 관계를 update
			update("mwsInterfaceDAO.updateOldNodeGeoRel", vo);
			
			// 새로운 관계정보 생성
			insert("mwsInterfaceDAO.insertNodeGeoRel", vo);
		}
		// 현재관계가 존재하면 관계정보는 수정하지 않는다
		
		// node의 geo-object ID와 좌표 update
		update("mwsInterfaceDAO.updateNodeGeoID", vo);
		
		return 0;
	}

}
