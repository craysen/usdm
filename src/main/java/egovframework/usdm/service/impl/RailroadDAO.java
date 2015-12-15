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

@Repository("railroadDAO")
public class RailroadDAO extends EgovAbstractDAO {

	public String insertRailroadVideo(RailroadVideoVO vo) throws Exception {
		return (String) insert("railroadDAO.insertRailroadVideo", vo);
	}
	
	public void updateRailroadVideo(RailroadVideoVO vo) throws Exception {
		update("railroadDAO.updateRailroadVideo", vo);
	}
	
	public void deleteRailroadVideo(int videoId) throws Exception {
		delete("railroadDAO.deleteRailroadVideo", videoId);
	}
	
	public RailroadVideoVO selectRailroadVideoDetail(RailroadVideoVO railroadVideoVO) throws Exception {
		return (RailroadVideoVO) select("railroadDAO.selectRailroadVideoDetail", railroadVideoVO);
	}
	
	public List<?> selectRailroadFileList(UsdmDefaultVO usdmSearchVO) throws Exception {
		return list("railroadDAO.selectRailroadFileList", usdmSearchVO);
	}
	
	public List<?> selectRailroadVideoList(UsdmDefaultVO usdmSearchVO) throws Exception {
		return list("railroadDAO.selectRailroadVideoList", usdmSearchVO);
	}
	
	public List<?> selectMeasureTypeCombo() throws Exception {
		return list("railroadDAO.selectMeasureTypeCombo", null);
	}
	
}
