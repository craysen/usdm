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
	
	// query/spatioTemporal
	public List<?> selectSensingValueSpatioTemporal(SNSensingValueVO vo) throws Exception {
		return apiInterfaceDAO.selectSensingValueSpatioTemporal(vo);
	}
	
	// information/gatewayIDList
	@Override
	public List<?> selectGatewayIDList() throws Exception {
		return apiInterfaceDAO.selectGatewayIDList();
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
	
}