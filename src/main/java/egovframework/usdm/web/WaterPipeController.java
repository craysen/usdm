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
package egovframework.usdm.web;

import java.util.List;
import java.io.File;

import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.usdm.service.*;
import egovframework.usdm.web.util.UsdmUtils;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springmodules.validation.commons.DefaultBeanValidator;

@Controller
public class WaterPipeController {

	/** WaterPipeService */
	@Resource(name = "waterPipeService")
	private WaterPipeService waterPipeService;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** Validator */
	@Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;
	
	@Autowired
    private ServletContext servletContext;

	/********************************************
	 * 상수도 사고정보 등록 및 조회 (UI-120)    * 
	 ********************************************/
	
	// 상수도 사고정보 입력화면 호출
	@RequestMapping(value = "/waterpipe/insertWaterAccident.do")
	public String insertWaterPipeAccident(@ModelAttribute("usdmSearchVO") UsdmDefaultVO usdmSearchVO, Model model) throws Exception {
		model.addAttribute("accidentVO", new WaterPipeAccidentVO());
		return "usp_sdm/UI-120_InsertWaterAccident";
	}
	
	// 상수관선택 팝업 호출
	@RequestMapping(value="/waterpipe/selectAccidentPipePopup.do")
	public String selectAccidentPipePopup (@ModelAttribute("usdmSearchVO") UsdmDefaultVO usdmSearchVO, Model model) throws Exception {
		List<?> pipeList = waterPipeService.selectWaterPipeGeometry(usdmSearchVO);
		
		model.addAttribute("pipeList", pipeList);
		
		return "/usp_sdm/UI-121_SelectWaterPopup";
	}
		
	// 상수도 사고정보 저장
	@RequestMapping(value = "/waterpipe/saveWaterAccident.do", method = RequestMethod.POST)
	public String saveWaterPipeAccident(@ModelAttribute("usdmSearchVO") UsdmDefaultVO usdmSearchVO, WaterPipeAccidentVO accidentVO, BindingResult bindingResult, Model model, SessionStatus status)
			throws Exception {
		
		// Server-Side Validation
		beanValidator.validate(accidentVO, bindingResult);
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("accidentVO", accidentVO);
			return "forward:/waterpipe/insertWaterAccident.do";
		}
		
		waterPipeService.insertWaterAccident(accidentVO);
		status.setComplete();
		
		return "forward:/waterpipe/selectWaterAccidentList.do";
	}
	
	// 사고정보 목록화면 호출 및 조회
	@RequestMapping(value = "/waterpipe/selectWaterAccidentList.do")
	public String selectWaterPipeAccidentList(@ModelAttribute("usdmSearchVO") UsdmDefaultVO usdmSearchVO, ModelMap model) throws Exception {
		if (usdmSearchVO.getFtrIdn() == null) usdmSearchVO.setFtrIdn("");
		
		double centerX 	= usdmSearchVO.getCenterX();
		double centerY 	= usdmSearchVO.getCenterY();
		double radius 	= usdmSearchVO.getRadius();
		
		String spatialCondition = "";
		
		if (centerX != 0 && centerY != 0) {
			double tmX = UsdmUtils.WGSLongitudeToEast(centerY, centerX);
            double tmY = UsdmUtils.WGSLatitudeToNorth(centerY, centerX);
			
			double minX = tmX - radius;
			double minY = tmY - radius;
			double maxX = tmX + radius;
			double maxY = tmY + radius;
			
			String mbrPolygon  = minX + " " + minY + ","
							   + maxX + " " + minY + ","
							   + maxX + " " + maxY + ","
							   + minX + " " + maxY + ","
							   + minX + " " + minY;
			
			spatialCondition += " AND ST_INTERSECTS(ST_GEOMFROMTEXT('POLYGON((" + mbrPolygon + "))',0), coordinate) = 1";
			spatialCondition += " AND SQRT(POWER(ABS(ST_X(coordinate)-" + tmX + "),2) + POWER(ABS(ST_Y(coordinate)-" + tmY + "),2)) <= " + radius;
		}
		
		usdmSearchVO.setSpatialCondition(spatialCondition);
		
		model.addAttribute("resultList", waterPipeService.selectWaterAccidentList(usdmSearchVO));

		return "usp_sdm/UI-122_SelectWaterAccidentList";
	}
	
	// 사고정보 상세화면 호출 및 조회
	@RequestMapping(value = "/waterpipe/selectWaterAccidentDetail.do")
	public String selectWaterPipeAccidentDetail(
			  @RequestParam(value="pipeFtrCde") String pipeFtrCde
			, @RequestParam(value="pipeFtrIdn") String pipeFtrIdn 
			, @RequestParam(value="longitude") String longitude 
			, @RequestParam(value="latitude") String latitude 
			, @RequestParam(value="accidentTime") String accidentTime 
			, ModelMap model) throws Exception {
		
		WaterPipeAccidentVO accidentVO = new WaterPipeAccidentVO();
		accidentVO.setPipeFtrCde(pipeFtrCde);
		accidentVO.setPipeFtrIdn(pipeFtrIdn);
		accidentVO.setLongitude(longitude);
		accidentVO.setLatitude(latitude);
		accidentVO.setAccidentTime(Long.parseLong(accidentTime));
		
		model.addAttribute("accidentVO", waterPipeService.selectWaterAccidentDetail(accidentVO));
		
		return "usp_sdm/UI-123_SelectWaterAccidentDetail";
	}

}