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
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.usdm.service.*;
import egovframework.usdm.web.util.UsdmUtils;

import org.springframework.stereotype.Repository;

@Repository("drainPipeDAO")
public class DrainPipeDAO extends EgovAbstractDAO {

	public String insertDrainPipeVideo(DrainPipeVideoVO vo) throws Exception {
		insert("drainPipeDAO.insertDrainPipeVideo", vo);
		
		EgovMap videoIdMap = (EgovMap)select("drainPipeDAO.selectMaxDrainPipeVideoID", vo);
		vo.setVideoId((int)videoIdMap.get("videoid"));
		
		String pipeId = (String)vo.getPipeFtrIdn();
		String[] pipeIdList = pipeId.split(",");
		
		for (int i=0; i<pipeIdList.length; i++) {
			vo.setPipeFtrIdn(pipeIdList[i]);
			
			insert("drainPipeDAO.insertDrainPipeVideoRel", vo);
		}
		
		return null; 
	}
	
	public String insertDrainPipeImage(DrainPipeImageVO vo) throws Exception {
		return (String) insert("drainPipeDAO.insertDrainPipeImage", vo);
	}
	
	public void updateDrainPipeVideo(DrainPipeVideoVO vo) throws Exception {
		update("drainPipeDAO.updateDrainPipeVideo", vo);
		
		String pipeId = (String)vo.getPipeFtrIdn();
		String[] pipeIdList = pipeId.split(",");
		
		delete("drainPipeDAO.deleteDrainPipeVideoRel", vo);
		
		for (int i=0; i<pipeIdList.length; i++) {
			vo.setPipeFtrIdn(pipeIdList[i]);
			
			insert("drainPipeDAO.insertDrainPipeVideoRel", vo);
		}
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
		DrainPipeVideoVO vo = (DrainPipeVideoVO) select("drainPipeDAO.selectDrainPipeVideoDetail", drainPipeVideoVO);
		
		List<EgovMap> pipeIdList = (List<EgovMap>) list("drainPipeDAO.selectDrainPipeVideoRel", drainPipeVideoVO);
		
		String pipeFtrIdn = "";
		
		if (pipeIdList != null) {
			for (int i=0; i<pipeIdList.size(); i++) {
				pipeFtrIdn += pipeIdList.get(i).get("pipeftridn").toString();
				
				if (i<pipeIdList.size()-1) pipeFtrIdn += ","; 
			}
		}
		
		vo.setPipeFtrIdn(pipeFtrIdn);
		
		return vo;
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
	
	public List<?> selectDrainManholeGeometry(UsdmDefaultVO usdmSearchVO) throws Exception {
		return list("drainPipeDAO.selectDrainManholeGeometry", usdmSearchVO);
	}
	public List<?> selectDrainPipeGeometry(UsdmDefaultVO usdmSearchVO) throws Exception {
		return list("drainPipeDAO.selectDrainPipeGeometry", usdmSearchVO);
	}
	
	public String insertDrainAccident(DrainPipeAccidentVO vo) throws Exception {
		// 경위도좌표를 TM좌표로 변환
		double tmX = UsdmUtils.WGSLongitudeToEast(Double.parseDouble(vo.getLatitude()), Double.parseDouble(vo.getLongitude()));
		double tmY = UsdmUtils.WGSLatitudeToNorth(Double.parseDouble(vo.getLatitude()), Double.parseDouble(vo.getLongitude()));
		
		// 시간문자열을 long 타입으로 변환
		long accidentTime = UsdmUtils.convertStrToDate(vo.getAccidentTimeStr(), "yyyy-MM-dd HH:mm:ss.SSS");
		
		// 사고명세 생성
		String diameter 	= vo.getDiameter();
		String majorAxis 	= vo.getMajorAxis();
		String minorAxis 	= vo.getMinorAxis();
		String width 		= vo.getWidth();
		String height 		= vo.getHeight();
		String depth 		= vo.getDepth();
		String length 		= vo.getLength();
		String degree 		= vo.getDegree();
		String direction 	= vo.getDirection();
		String place 		= vo.getPlace();
		
		String accidentDesc	= "";
		
		if (diameter != null && !diameter.equals("")) {
			accidentDesc += "diameter:" + diameter;
		}
		if (majorAxis != null && !majorAxis.equals("")) {
			if (!accidentDesc.equals("")) accidentDesc += ",";
			accidentDesc += "majorAxis:" + majorAxis;
		}
		if (minorAxis != null && !minorAxis.equals("")) {
			if (!accidentDesc.equals("")) accidentDesc += ",";
			accidentDesc += "minorAxis:" + minorAxis;
		}
		if (width != null && !width.equals("")) {
			if (!accidentDesc.equals("")) accidentDesc += ",";
			accidentDesc += "width:" + width;
		}
		if (height != null && !height.equals("")) {
			if (!accidentDesc.equals("")) accidentDesc += ",";
			accidentDesc += "height:" + height;
		}
		if (depth != null && !depth.equals("")) {
			if (!accidentDesc.equals("")) accidentDesc += ",";
			accidentDesc += "depth:" + depth;
		}
		if (length != null && !length.equals("")) {
			if (!accidentDesc.equals("")) accidentDesc += ",";
			accidentDesc += "length:" + length;
		}
		if (degree != null && !degree.equals("")) {
			if (!accidentDesc.equals("")) accidentDesc += ",";
			accidentDesc += "degree:" + degree;
		}
		if (direction != null && !direction.equals("")) {
			if (!accidentDesc.equals("")) accidentDesc += ",";
			accidentDesc += "direction:" + direction;
		}
		if (place != null && !place.equals("")) {
			if (!accidentDesc.equals("")) accidentDesc += ",";
			accidentDesc += "place:" + place;
		}
		
		// 사고정보 레코드 저장
		vo.setX(tmX);
		vo.setY(tmY);
		vo.setAccidentTime(accidentTime);
		vo.setAccidentDesc(accidentDesc);
		
		return (String) insert("drainPipeDAO.insertDrainAccident", vo);
	}
	
	public List<?> selectDrainAccidentList(UsdmDefaultVO usdmSearchVO) throws Exception {
		List<EgovMap> accidentList = (List<EgovMap>) list("drainPipeDAO.selectDrainAccidentList", usdmSearchVO);

		// 발생시간과 사고유형, 형태를 문자열로 변환
		for (int i=0; i<accidentList.size(); i++) {
			EgovMap accidentMap = accidentList.get(i);
			
			accidentMap.put("accidentdatestr",  UsdmUtils.convertDateToStr((double)accidentMap.get("accidenttimelong"), "yyyy년 MM월 dd일"));
			accidentMap.put("accidenttimestr",  UsdmUtils.convertDateToStr((double)accidentMap.get("accidenttimelong"), "HH시 mm분 ss초"));
			accidentMap.put("accidenttypestr",  UsdmUtils.getAccidentTypeName((String)accidentMap.get("accidenttype")));
			accidentMap.put("accidentshapestr", UsdmUtils.getAccidentShapeName((String)accidentMap.get("accidentshape")));
		}
		
		return accidentList;
	}

	public DrainPipeAccidentVO selectDrainAccidentDetail(DrainPipeAccidentVO accidentVO) throws Exception {
		DrainPipeAccidentVO resultVO = (DrainPipeAccidentVO) select("drainPipeDAO.selectDrainAccidentDetail", accidentVO); 
		
		// 발생시간과 사고유형, 형태를 문자열로 변환
		resultVO.setAccidentTimeStr(UsdmUtils.convertDateToStr(accidentVO.getAccidentTime(), "yyyy-MM-dd HH:mm:ss"));
		resultVO.setAccidentType(UsdmUtils.getAccidentTypeName(resultVO.getAccidentType()));
		resultVO.setAccidentShape(UsdmUtils.getAccidentShapeName(resultVO.getAccidentShape()));
		resultVO.setAccidentDesc(UsdmUtils.getAccidentDescName(resultVO.getAccidentDesc()));
		
		return resultVO;
	}
}