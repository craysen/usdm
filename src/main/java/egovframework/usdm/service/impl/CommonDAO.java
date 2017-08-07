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

import java.math.BigDecimal;
import java.util.List;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.usdm.service.*;
import egovframework.usdm.web.util.UsdmUtils;

import org.springframework.stereotype.Repository;

@Repository("commonDAO")
public class CommonDAO extends EgovAbstractDAO {

	public List<?> selectSensorNodeGeometry(UsdmDefaultVO usdmSearchVO) throws Exception {
		return list("commonDAO.selectSensorNodeGeometry", usdmSearchVO);
	}
	
	public List<?> selectSensorNodeSensingValue(UsdmDefaultVO usdmSearchVO) throws Exception {
		List<?> valueList = list("commonDAO.selectSensorNodeSensingValue", usdmSearchVO);
		
		for (int i=0; i<valueList.size(); i++) {
			EgovMap valueMap = (EgovMap)valueList.get(i);
			
			valueMap.put("timestr", UsdmUtils.convertDateToStr((double)valueMap.get("sensingtime"), "yyyy-MM-dd HH:mm:ss.SSS"));
		}
		
		return valueList;
	}

}
