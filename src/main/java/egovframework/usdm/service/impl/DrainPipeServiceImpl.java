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

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.usdm.service.*;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("drainPipeService")
public class DrainPipeServiceImpl extends EgovAbstractServiceImpl implements DrainPipeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DrainPipeServiceImpl.class);

	@Resource(name = "drainPipeDAO")
	private DrainPipeDAO drainPipeDAO;

	@Override
	public void insertDrainPipeVideo(DrainPipeVideoVO vo) throws Exception {
		try {
			LOGGER.debug(vo.toString());
			drainPipeDAO.insertDrainPipeVideo(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void insertDrainPipeImage(DrainPipeImageVO vo) throws Exception {
		try {
			LOGGER.debug(vo.toString());
			drainPipeDAO.insertDrainPipeImage(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateDrainPipeVideo(DrainPipeVideoVO vo) throws Exception {
		try {
			LOGGER.debug(vo.toString());
			drainPipeDAO.updateDrainPipeVideo(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void updateDrainPipeImage(DrainPipeImageVO vo) throws Exception {
		try {
			LOGGER.debug(vo.toString());
			drainPipeDAO.updateDrainPipeImage(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteDrainPipeVideo(int videoId) throws Exception {
		try {
			drainPipeDAO.deleteDrainPipeVideo(videoId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void deleteDrainPipeImage(int imageId) throws Exception {
		try {
			drainPipeDAO.deleteDrainPipeImage(imageId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public DrainPipeVideoVO selectDrainPipeVideoDetail(DrainPipeVideoVO drainPipeVideoVO) throws Exception {
		DrainPipeVideoVO resultVO = new DrainPipeVideoVO();
		
		try {
			resultVO = drainPipeDAO.selectDrainPipeVideoDetail(drainPipeVideoVO);
			
			if (resultVO == null)
				throw processException("info.nodata.msg");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultVO;
	}
	
	@Override
	public DrainPipeImageVO selectDrainPipeImageDetail(DrainPipeImageVO drainPipeImageVO) throws Exception {
		DrainPipeImageVO resultVO = new DrainPipeImageVO();
		
		try {
			resultVO = drainPipeDAO.selectDrainPipeImageDetail(drainPipeImageVO);
			
			if (resultVO == null)
				throw processException("info.nodata.msg");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultVO;
	}
	
	@Override
	public List<?> selectDrainPipeFileList(UsdmDefaultVO usdmSearchVO) throws Exception {
		return drainPipeDAO.selectDrainPipeFileList(usdmSearchVO);
	}
	
	@Override
	public List<?> selectDrainPipeVideoList(UsdmDefaultVO usdmSearchVO) throws Exception {
		return drainPipeDAO.selectDrainPipeVideoList(usdmSearchVO);
	}
	
	@Override
	public List<?> selectDrainPipeImageList(UsdmDefaultVO usdmSearchVO) throws Exception {
		return drainPipeDAO.selectDrainPipeImageList(usdmSearchVO);
	}

}
