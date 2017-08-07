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

@Repository("waterPipeDAO")
public class WaterPipeDAO extends EgovAbstractDAO {

	public List<?> selectWaterPipeGeometry(UsdmDefaultVO usdmSearchVO) throws Exception {
		return list("waterPipeDAO.selectWaterPipeGeometry", usdmSearchVO);
	}
	
	public String insertWaterAccident(WaterPipeAccidentVO vo) throws Exception {
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
		
		return (String) insert("waterPipeDAO.insertWaterAccident", vo);
	}
	
	public List<?> selectWaterAccidentList(UsdmDefaultVO usdmSearchVO) throws Exception {
		List<EgovMap> accidentList = (List<EgovMap>) list("waterPipeDAO.selectWaterAccidentList", usdmSearchVO);

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

	public WaterPipeAccidentVO selectWaterAccidentDetail(WaterPipeAccidentVO accidentVO) throws Exception {
		WaterPipeAccidentVO resultVO = (WaterPipeAccidentVO) select("waterPipeDAO.selectWaterAccidentDetail", accidentVO); 
		
		// 발생시간과 사고유형, 형태를 문자열로 변환
		resultVO.setAccidentTimeStr(UsdmUtils.convertDateToStr(accidentVO.getAccidentTime(), "yyyy-MM-dd HH:mm:ss"));
		resultVO.setAccidentType(UsdmUtils.getAccidentTypeName(resultVO.getAccidentType()));
		resultVO.setAccidentShape(UsdmUtils.getAccidentShapeName(resultVO.getAccidentShape()));
		resultVO.setAccidentDesc(UsdmUtils.getAccidentDescName(resultVO.getAccidentDesc()));
		
		return resultVO;
	}
}