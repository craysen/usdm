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

@Service("waterPipeService")
public class WaterPipeServiceImpl extends EgovAbstractServiceImpl implements WaterPipeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(WaterPipeServiceImpl.class);

	@Resource(name = "waterPipeDAO")
	private WaterPipeDAO waterPipeDAO;

	@Override
	public List<?> selectWaterPipeGeometry(UsdmDefaultVO usdmSearchVO) throws Exception {
		return waterPipeDAO.selectWaterPipeGeometry(usdmSearchVO);
	}
	
	@Override
	public void insertWaterAccident(WaterPipeAccidentVO vo) throws Exception {
		try {
			LOGGER.debug(vo.toString());
			waterPipeDAO.insertWaterAccident(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public List<?> selectWaterAccidentList(UsdmDefaultVO usdmSearchVO) throws Exception {
		return waterPipeDAO.selectWaterAccidentList(usdmSearchVO);
	}

	@Override
	public WaterPipeAccidentVO selectWaterAccidentDetail(WaterPipeAccidentVO accidentVO) throws Exception {
		WaterPipeAccidentVO resultVO = new WaterPipeAccidentVO();
		
		try {
			resultVO = waterPipeDAO.selectWaterAccidentDetail(accidentVO);
			
			if (resultVO == null)
				throw processException("info.nodata.msg");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultVO;
	}

}
