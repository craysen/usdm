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

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.usdm.service.*;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("mwsInterfaceService")
public class MWSInterfaceServiceImpl extends EgovAbstractServiceImpl implements MWSInterfaceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MWSInterfaceServiceImpl.class);

	@Resource(name = "mwsInterfaceDAO")
	private MWSInterfaceDAO mwsInterfaceDAO;

	@Override
	public int selectLoginSession(LoginSessionVO vo) throws Exception {
		int result = 0;
		
		try {
			LOGGER.debug(vo.toString());
			result = mwsInterfaceDAO.selectLoginSession(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public void updateSessionKeyLogin(LoginSessionVO vo) throws Exception {
		try {
			LOGGER.debug(vo.toString());
			mwsInterfaceDAO.updateSessionKeyLogin(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int selectLogoutSession(LoginSessionVO vo) throws Exception {
		int result = 0;
		
		try {
			LOGGER.debug(vo.toString());
			result = mwsInterfaceDAO.selectLogoutSession(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public void updateSessionKeyLogout(LoginSessionVO vo) throws Exception {
		try {
			LOGGER.debug(vo.toString());
			mwsInterfaceDAO.updateSessionKeyLogout(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void insertGateway(SNGatewayVO vo) throws Exception {
		try {
			LOGGER.debug(vo.toString());
			mwsInterfaceDAO.insertGateway(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void insertGatewayByID(SNGatewayVO vo) throws Exception {
		try {
			LOGGER.debug(vo.toString());
			mwsInterfaceDAO.insertGatewayByID(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void insertPan(SNPanVO vo) throws Exception {
		try {
			LOGGER.debug(vo.toString());
			mwsInterfaceDAO.insertPan(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void insertPanByID(SNPanVO vo) throws Exception {
		try {
			LOGGER.debug(vo.toString());
			mwsInterfaceDAO.insertPanByID(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void insertNode(SNNodeVO vo) throws Exception {
		try {
			LOGGER.debug(vo.toString());
			mwsInterfaceDAO.insertNode(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void insertNodeByID(SNNodeVO vo) throws Exception {
		try {
			LOGGER.debug(vo.toString());
			mwsInterfaceDAO.insertNodeByID(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void insertTransducer(SNTransducerVO vo) throws Exception {
		try {
			LOGGER.debug(vo.toString());
			mwsInterfaceDAO.insertTransducer(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int updateGatewayAddress(SNGatewayVO vo) throws Exception {
		int result = 0;
		
		try {
			result = mwsInterfaceDAO.updateGatewayAddress(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public int updatePanAddress(SNPanVO vo) throws Exception {
		int result = 0;
		
		try {
			result = mwsInterfaceDAO.updatePanAddress(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public int updateNodeAddress(SNNodeVO vo) throws Exception {
		int result = 0;
		
		try {
			result = mwsInterfaceDAO.updateNodeAddress(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public int updateTransducerAddress(SNTransducerVO vo) throws Exception {
		int result = 0;
		
		try {
			result = mwsInterfaceDAO.updateTransducerAddress(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public int updateGatewayAddressByGID(SNGatewayVO vo) throws Exception {
		int result = 0;
		
		try {
			result = mwsInterfaceDAO.updateGatewayAddressByGID(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public int updatePanAddressByGID(SNPanVO vo) throws Exception {
		int result = 0;
		
		try {
			result = mwsInterfaceDAO.updatePanAddressByGID(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public int updateNodeAddressByGID(SNNodeVO vo) throws Exception {
		int result = 0;
		
		try {
			result = mwsInterfaceDAO.updateNodeAddressByGID(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public int deleteGateway(SNGatewayVO vo) throws Exception {
		int result = 0;
		
		try {
			result = mwsInterfaceDAO.deleteGateway(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public int deletePan(SNPanVO vo) throws Exception {
		int result = 0;
		
		try {
			result = mwsInterfaceDAO.deletePan(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public int deleteNode(SNNodeVO vo) throws Exception {
		int result = 0;
		
		try {
			result = mwsInterfaceDAO.deleteNode(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public int deleteTransducer(SNTransducerVO vo) throws Exception {
		int result = 0;
		
		try {
			result = mwsInterfaceDAO.deleteTransducer(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public int deleteGatewayDescent(SNGatewayVO vo) throws Exception {
		int result = 0;
		
		try {
			result = mwsInterfaceDAO.deleteGatewayDescent(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public int deletePanDescent(SNPanVO vo) throws Exception {
		int result = 0;
		
		try {
			result = mwsInterfaceDAO.deletePanDescent(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public int deleteNodeDescent(SNNodeVO vo) throws Exception {
		int result = 0;
		
		try {
			result = mwsInterfaceDAO.deleteNodeDescent(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public int deleteGatewayByGID(SNGatewayVO vo) throws Exception {
		int result = 0;
		
		try {
			result = mwsInterfaceDAO.deleteGatewayByGID(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public int deletePanByGID(SNPanVO vo) throws Exception {
		int result = 0;
		
		try {
			result = mwsInterfaceDAO.deletePanByGID(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public int deleteNodeByGID(SNNodeVO vo) throws Exception {
		int result = 0;
		
		try {
			result = mwsInterfaceDAO.deleteNodeByGID(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public int insertSensingValue(SNSensingValueVO vo) throws Exception {
		int result = 0;
		
		try {
			LOGGER.debug(vo.toString());
			result = mwsInterfaceDAO.insertSensingValue(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public int insertSensingValueByGID(SNSensingValueVO vo) throws Exception {
		int result = 0;
		
		try {
			LOGGER.debug(vo.toString());
			result = mwsInterfaceDAO.insertSensingValueByGID(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public void checkSensingValueThreshold(SNSensingValueVO vo) throws Exception {
		try {
			LOGGER.debug(vo.toString());
			mwsInterfaceDAO.checkSensingValueThreshold(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void checkLeakageThreshold(SNSensingValueVO vo) throws Exception {
		try {
			LOGGER.debug(vo.toString());
			mwsInterfaceDAO.checkLeakageThreshold(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void insertBinaryValue(SNSensingValueVO vo) throws Exception {
		try {
			LOGGER.debug(vo.toString());
			mwsInterfaceDAO.insertBinaryValue(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void insertBinaryValueByGID(SNSensingValueVO vo) throws Exception {
		try {
			LOGGER.debug(vo.toString());
			mwsInterfaceDAO.insertBinaryValueByGID(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int updateActuationResult(SNTransducerVO vo) throws Exception {
		int result = 0;
		
		try {
			result = mwsInterfaceDAO.updateActuationResult(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public int updateActuationResultByGID(SNTransducerVO vo) throws Exception {
		int result = 0;
		
		try {
			result = mwsInterfaceDAO.updateActuationResultByGID(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public int updateGatewayStatus(SNGatewayVO vo) throws Exception {
		int result = 0;
		
		try {
			result = mwsInterfaceDAO.updateGatewayStatus(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	@Override
	public int updatePanStatus(SNPanVO vo) throws Exception {
		int result = 0;
		
		try {
			result = mwsInterfaceDAO.updatePanStatus(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	@Override
	public int updateNodeStatus(SNNodeVO vo) throws Exception {
		int result = 0;
		
		try {
			result = mwsInterfaceDAO.updateNodeStatus(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	@Override
	public int updateTransducerStatus(SNTransducerVO vo) throws Exception {
		int result = 0;
		
		try {
			result = mwsInterfaceDAO.updateTransducerStatus(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public int validateSessionKey(LoginSessionVO vo) throws Exception {
		return mwsInterfaceDAO.validateSessionKey(vo);
	}
	
	@Override
	public int updateGatewayStatusByGID(SNGatewayVO vo) throws Exception {
		int result = 0;
		
		try {
			result = mwsInterfaceDAO.updateGatewayStatusByGID(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	@Override
	public int updatePanStatusByGID(SNPanVO vo) throws Exception {
		int result = 0;
		
		try {
			result = mwsInterfaceDAO.updatePanStatusByGID(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	@Override
	public int updateNodeStatusByGID(SNNodeVO vo) throws Exception {
		int result = 0;
		
		try {
			result = mwsInterfaceDAO.updateNodeStatusByGID(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

}
