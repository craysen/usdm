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

public interface DrainPipeService {

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
	
	List<?> selectDrainManholeGeometry(UsdmDefaultVO usdmSearchVO) throws Exception;
	
	List<?> selectDrainPipeGeometry(UsdmDefaultVO usdmSearchVO) throws Exception;

	void insertDrainAccident(DrainPipeAccidentVO vo) throws Exception;
	
	List<?> selectDrainAccidentList(UsdmDefaultVO usdmSearchVO) throws Exception;

	DrainPipeAccidentVO selectDrainAccidentDetail(DrainPipeAccidentVO drainPipeVideoVO) throws Exception;
}
