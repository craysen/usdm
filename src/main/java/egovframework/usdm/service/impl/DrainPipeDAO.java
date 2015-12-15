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
import egovframework.usdm.service.*;

import org.springframework.stereotype.Repository;

@Repository("drainPipeDAO")
public class DrainPipeDAO extends EgovAbstractDAO {

	public String insertDrainPipeVideo(DrainPipeVideoVO vo) throws Exception {
		return (String) insert("drainPipeDAO.insertDrainPipeVideo", vo);
	}
	
	public String insertDrainPipeImage(DrainPipeImageVO vo) throws Exception {
		return (String) insert("drainPipeDAO.insertDrainPipeImage", vo);
	}
	
	public void updateDrainPipeVideo(DrainPipeVideoVO vo) throws Exception {
		update("drainPipeDAO.updateDrainPipeVideo", vo);
	}
	
	public void updateDrainPipeImage(DrainPipeImageVO vo) throws Exception {
		update("drainPipeDAO.updateDrainPipeImage", vo);
	}
	
	public void deleteDrainPipeVideo(int videoId) throws Exception {
		delete("drainPipeDAO.deleteDrainPipeVideo", videoId);
	}
	
	public void deleteDrainPipeImage(int imageId) throws Exception {
		delete("drainPipeDAO.deleteDrainPipeImage", imageId);
	}
	
	public DrainPipeVideoVO selectDrainPipeVideoDetail(DrainPipeVideoVO drainPipeVideoVO) throws Exception {
		return (DrainPipeVideoVO) select("drainPipeDAO.selectDrainPipeVideoDetail", drainPipeVideoVO);
	}
	
	public DrainPipeImageVO selectDrainPipeImageDetail(DrainPipeImageVO drainPipeImageVO) throws Exception {
		return (DrainPipeImageVO) select("drainPipeDAO.selectDrainPipeImageDetail", drainPipeImageVO);
	}

	public List<?> selectDrainPipeFileList(UsdmDefaultVO usdmSearchVO) throws Exception {
		return list("drainPipeDAO.selectDrainPipeFileList", usdmSearchVO);
	}
	
	public List<?> selectDrainPipeVideoList(UsdmDefaultVO usdmSearchVO) throws Exception {
		return list("drainPipeDAO.selectDrainPipeVideoList", usdmSearchVO);
	}
	
	public List<?> selectDrainPipeImageList(UsdmDefaultVO usdmSearchVO) throws Exception {
		return list("drainPipeDAO.selectDrainPipeImageList", usdmSearchVO);
	}

}
