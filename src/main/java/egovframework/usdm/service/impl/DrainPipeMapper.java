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
import egovframework.usdm.service.DrainPipeImageVO;
import egovframework.usdm.service.DrainPipeVideoVO;
import egovframework.usdm.service.UsdmDefaultVO;

@Mapper("drainPipeMapper")
public interface DrainPipeMapper {

	void insertDrainPipeVideo(DrainPipeVideoVO vo) throws Exception;
	
	void insertDrainPipeImage(DrainPipeImageVO vo) throws Exception;
	
	void updateDrainPipeVideo(DrainPipeVideoVO vo) throws Exception;
	
	void updateDrainPipeImage(DrainPipeImageVO vo) throws Exception;
		
	void deleteDrainPipeVideo(int videoId) throws Exception;
	
	void deleteDrainPipeImage(int imageId) throws Exception;

	DrainPipeVideoVO selectDrainPipeVideoDetail(DrainPipeVideoVO drainPipeVideoVO) throws Exception;
	
	DrainPipeImageVO selectDrainPipeImageDetail(DrainPipeImageVO drainPipeImageVO) throws Exception;
	
	List<?> selectDrainPipeFileList(UsdmDefaultVO usdmSearchVO) throws Exception;
	
	List<?> selectDrainPipeVideoList(UsdmDefaultVO usdmSearchVO) throws Exception;
	
	List<?> selectDrainPipeImageList(UsdmDefaultVO usdmSearchVO) throws Exception;

}
