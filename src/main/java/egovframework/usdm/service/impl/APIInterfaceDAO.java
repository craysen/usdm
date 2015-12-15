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

@Repository("apiInterfaceDAO")
public class APIInterfaceDAO extends EgovAbstractDAO {

	public int validateSessionKey(LoginSessionVO vo) throws Exception {
		List<EgovMap> existYn = (List<EgovMap>) list("apiInterfaceDAO.validateSessionKey", vo);
		
		if (existYn.get(0).get("validyn").equals("N"))
			return -1;
		
		return 0;
	}
	
	public List<?> selectSensingValueQuery(SNSensingValueVO vo) throws Exception {
		return list("apiInterfaceDAO.selectSensingValueQuery", vo);
	}
	
	public List<?> selectSensingValueSpatioTemporal(SNSensingValueVO vo) throws Exception {
		return list("apiInterfaceDAO.selectSensingValueSpatioTemporal", vo);
	}
	
	public List<?> selectGatewayIDList() throws Exception {
		return list("apiInterfaceDAO.selectGatewayIDList");
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
	
	public void insertAssessValues(SNAssessValueVO vo) throws Exception {
		insert("apiInterfaceDAO.insertAssessValues", vo);
	}
	
	public List<?> retrieveAssessValues(SNAssessValueVO vo) throws Exception {
		return list("apiInterfaceDAO.retrieveAssessValues", vo);
	}
	
	public List<?> retrieveRiskValues(SNAssessValueVO vo) throws Exception {
		return list("apiInterfaceDAO.retrieveRiskValues", vo);
	}
}
