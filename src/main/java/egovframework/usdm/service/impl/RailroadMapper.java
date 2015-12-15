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
import egovframework.usdm.service.RailroadVideoVO;
import egovframework.usdm.service.UsdmDefaultVO;

@Mapper("railroadMapper")
public interface RailroadMapper {

	void insertRailroadVideo(RailroadVideoVO vo) throws Exception;
	
	void updateRailroadVideo(RailroadVideoVO vo) throws Exception;
	
	void deleteRailroadVideo(int videoId) throws Exception;
	
	RailroadVideoVO selectRailroadVideoDetail(RailroadVideoVO railroadVideoVO) throws Exception;
	
	List<?> selectRailroadFileList(UsdmDefaultVO usdmSearchVO) throws Exception;
	
	List<?> selectRailroadVideoList(UsdmDefaultVO usdmSearchVO) throws Exception;
	
	List<?> selectMeasureTypeCombo() throws Exception;

}
