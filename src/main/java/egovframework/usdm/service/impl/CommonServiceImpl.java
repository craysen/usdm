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

@Service("commonService")
public class CommonServiceImpl extends EgovAbstractServiceImpl implements CommonService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonServiceImpl.class);
	
	@Resource(name = "commonDAO")
	private CommonDAO commonDAO;

	@Override
	public List<?> selectSensorNodeGeometry(UsdmDefaultVO usdmSearchVO) throws Exception {
		return commonDAO.selectSensorNodeGeometry(usdmSearchVO);
	}

	@Override
	public List<?> selectSensorNodeSensingValue(UsdmDefaultVO usdmSearchVO) throws Exception {
		return commonDAO.selectSensorNodeSensingValue(usdmSearchVO);
	}

}
