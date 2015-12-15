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
package egovframework.usdm.service;

import java.util.List;

public interface APIInterfaceService {

	int validateSessionKey(LoginSessionVO vo) throws Exception;
	
	// query/latestValue
	List<?> selectSensingValueQuery(SNSensingValueVO vo) throws Exception;
	
	// query/spatioTemporal
	List<?> selectSensingValueSpatioTemporal(SNSensingValueVO vo) throws Exception;
	
	// information/gatewayIDList
	List<?> selectGatewayIDList() throws Exception;
	
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
	
	// information/resourceStatus
	List<?> selectGatewayStatus(SNGatewayVO vo) throws Exception;
	List<?> selectGatewayStatusDescent(SNGatewayVO vo) throws Exception;
	
	List<?> selectPanStatus(SNPanVO vo) throws Exception;
	List<?> selectPanStatusDescent(SNPanVO vo) throws Exception;
	
	List<?> selectNodeStatus(SNNodeVO vo) throws Exception;
	List<?> selectNodeStatusDescent(SNNodeVO vo) throws Exception;
	
	List<?> selectTransducerStatus(SNTransducerVO vo) throws Exception;
	
	// sri/insertAssessValues
	void insertAssessValues(SNAssessValueVO vo) throws Exception;
	
	// sri/retrieveAssessValues
	List<?> retrieveAssessValues(SNAssessValueVO vo) throws Exception;
	
	// sri/assessUtilities
	
	// sri/assessSRI
	
	// sri/retrieveRiskValues
	List<?> retrieveRiskValues(SNAssessValueVO vo) throws Exception;
	
}
