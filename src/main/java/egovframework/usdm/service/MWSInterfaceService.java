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

public interface MWSInterfaceService {

	// connection/login
	int selectLoginSession(LoginSessionVO vo) throws Exception;
	void updateSessionKeyLogin(LoginSessionVO vo) throws Exception;

	// connection/logout
	int selectLogoutSession(LoginSessionVO vo) throws Exception;
	void updateSessionKeyLogout(LoginSessionVO vo) throws Exception;
	
	// resource/gateway
	void insertGateway(SNGatewayVO vo) throws Exception;
	// resource/pan
	void insertPan(SNPanVO vo) throws Exception;
	// resource/node
	void insertNode(SNNodeVO vo) throws Exception;
	// resource/transducer
	void insertTransducer(SNTransducerVO vo) throws Exception;
		
	// resource/gatewayByID(2)
	void insertGatewayByID(SNGatewayVO vo) throws Exception;
	// resource/panByID(2)
	void insertPanByID(SNPanVO vo) throws Exception;
	// resource/nodeByID(2)
	void insertNodeByID(SNNodeVO vo) throws Exception;
	
	// resource/update
	int updateGatewayAddress(SNGatewayVO vo) throws Exception;
	int updatePanAddress(SNPanVO vo) throws Exception;
	int updateNodeAddress(SNNodeVO vo) throws Exception;
	int updateTransducerAddress(SNTransducerVO vo) throws Exception;
	
	// resource/updateByGID(2)
	int updateGatewayAddressByGID(SNGatewayVO vo) throws Exception;
	int updatePanAddressByGID(SNPanVO vo) throws Exception;
	int updateNodeAddressByGID(SNNodeVO vo) throws Exception;
	
	// resource/delete
	int deleteGateway(SNGatewayVO vo) throws Exception;
	int deletePan(SNPanVO vo) throws Exception;
	int deleteNode(SNNodeVO vo) throws Exception;
	int deleteTransducer(SNTransducerVO vo) throws Exception;
	int deleteGatewayDescent(SNGatewayVO vo) throws Exception;
	int deletePanDescent(SNPanVO vo) throws Exception;
	int deleteNodeDescent(SNNodeVO vo) throws Exception;
	
	// resource/deleteByGID(2)
	int deleteGatewayByGID(SNGatewayVO vo) throws Exception;
	int deletePanByGID(SNPanVO vo) throws Exception;
	int deleteNodeByGID(SNNodeVO vo) throws Exception;
	
	// report/sensingValue
	int insertSensingValue(SNSensingValueVO vo) throws Exception;
	
	// report/sensingValueByGID(2)
	int insertSensingValueByGID(SNSensingValueVO vo) throws Exception;
	void checkSensingValueThreshold(SNSensingValueVO vo) throws Exception;
	void checkLeakageThreshold(SNSensingValueVO vo) throws Exception;
	
	// report/binaryValue(2)
	void insertBinaryValue(SNSensingValueVO vo) throws Exception;
	
	// report/binaryValueByGID(2)
	void insertBinaryValueByGID(SNSensingValueVO vo) throws Exception;
	
	// report/actuationResult
	int updateActuationResult(SNTransducerVO vo) throws Exception;
	
	// report/actuationResultByGID(2)
	int updateActuationResultByGID(SNTransducerVO vo) throws Exception;
	
	// notice/statusResource
	int updateGatewayStatus(SNGatewayVO vo) throws Exception;
	int updatePanStatus(SNPanVO vo) throws Exception;
	int updateNodeStatus(SNNodeVO vo) throws Exception;
	int updateTransducerStatus(SNTransducerVO vo) throws Exception;
	
	// notice/reboot
	int validateSessionKey(LoginSessionVO vo) throws Exception;

	// notice/statusResourceByGID(2)
	int updateGatewayStatusByGID(SNGatewayVO vo) throws Exception;
	int updatePanStatusByGID(SNPanVO vo) throws Exception;
	int updateNodeStatusByGID(SNNodeVO vo) throws Exception;
}
