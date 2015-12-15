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

@Service("railroadService")
public class RailroadServiceImpl extends EgovAbstractServiceImpl implements RailroadService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RailroadServiceImpl.class);

	@Resource(name = "railroadDAO")
	private RailroadDAO railroadDAO;

	@Override
	public void insertRailroadVideo(RailroadVideoVO vo) throws Exception {
		try {
			LOGGER.debug(vo.toString());
			railroadDAO.insertRailroadVideo(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void updateRailroadVideo(RailroadVideoVO vo) throws Exception {
		try {
			LOGGER.debug(vo.toString());
			railroadDAO.updateRailroadVideo(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteRailroadVideo(int videoId) throws Exception {
		try {
			railroadDAO.deleteRailroadVideo(videoId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public RailroadVideoVO selectRailroadVideoDetail(RailroadVideoVO drainPipeVideoVO) throws Exception {
		RailroadVideoVO resultVO = new RailroadVideoVO();
		
		try {
			resultVO = railroadDAO.selectRailroadVideoDetail(drainPipeVideoVO);
			
			if (resultVO == null)
				throw processException("info.nodata.msg");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultVO;
	}
	
	@Override
	public List<?> selectRailroadFileList(UsdmDefaultVO usdmSearchVO) throws Exception {
		return railroadDAO.selectRailroadFileList(usdmSearchVO);
	}
	
	@Override
	public List<?> selectRailroadVideoList(UsdmDefaultVO usdmSearchVO) throws Exception {
		return railroadDAO.selectRailroadVideoList(usdmSearchVO);
	}
	
	@Override
	public List<?> selectMeasureTypeCombo() throws Exception {
		return railroadDAO.selectMeasureTypeCombo();
	}
	
}
