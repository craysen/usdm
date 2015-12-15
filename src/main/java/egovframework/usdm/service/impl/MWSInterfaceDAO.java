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

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.usdm.service.*;

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
	
	public String insertPan(SNPanVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingPan", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			insert("mwsInterfaceDAO.insertPan", vo);
		else
			update("mwsInterfaceDAO.updatePan", vo);
		
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
	
	public int insertSensingValue(SNSensingValueVO vo) throws Exception {
		List<EgovMap> range = (List<EgovMap>) list("mwsInterfaceDAO.selectSensingValueRange", vo);
		
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
			
		insert("mwsInterfaceDAO.insertSensingValue", vo);
		return 0;
	}
	
	public int updateActuationResult(SNTransducerVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("mwsInterfaceDAO.selectExistingTransducer", vo);
		
		if (existYn.get(0).get("existyn").equals("N"))
			return -1;
		
		update("mwsInterfaceDAO.updateActuationResult", vo);
		
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

}
