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
public class DrainPipeController {

	/** DrainPipeService */
	@Resource(name = "drainPipeService")
	private DrainPipeService drainPipeService;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** Validator */
	@Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;
	
	@Autowired
    private ServletContext servletContext;

	/********************************************
	 * 하수도 센싱데이터 입력 (UI-040)          * 
	 ********************************************/

	// 하수도 이미지입력화면 호출
	@RequestMapping(value = "/drainpipe/insertDrainPipeImage.do")
	public String insertDrainPipeImage(@ModelAttribute("usdmSearchVO") UsdmDefaultVO usdmSearchVO, Model model) throws Exception {
		model.addAttribute("drainPipeImageVO", new DrainPipeImageVO());
		return "usp_sdm/UI-041_InsertDrainPipeImage";
	}
	
	// 하수도 이미지파일정보 저장
	@RequestMapping(value = "/drainpipe/uploadImageFile.do", method = RequestMethod.POST)
	public String insertDrainPipeImageFile(@ModelAttribute("usdmSearchVO") UsdmDefaultVO usdmSearchVO, DrainPipeImageVO drainPipeImageVO, BindingResult bindingResult, Model model, SessionStatus status)
			throws Exception {

		String realPath = servletContext.getRealPath("");
		String tempPath = realPath + "upload" + File.separator;
		String savePath = tempPath.replace(File.separator, "/");
		String realFileName = "";
		
		savePath = "C:/sdm_uploadedFiles/drainpipe_image/";
		File uploadPath = new File(savePath);
		
		if(!uploadPath.exists()) {
			uploadPath.mkdirs();
		}
		
		// Server-Side Validation
		beanValidator.validate(drainPipeImageVO, bindingResult);

		if (bindingResult.hasErrors()) {
			model.addAttribute("drainPipeImageVO", drainPipeImageVO);
			return "forward:/drainpipe/insertDrainPipeImage.do";
		}

		// upload file first
		MultipartFile file = drainPipeImageVO.getFile();
		realFileName = UsdmUtils.uploadFile(file, savePath, model);
		
		drainPipeImageVO.setFileName(file.getOriginalFilename());
		drainPipeImageVO.setFilePath(savePath);
		drainPipeImageVO.setFileRealName(realFileName);
		
		drainPipeService.insertDrainPipeImage(drainPipeImageVO);
		status.setComplete();

		return "forward:/drainpipe/selectDrainPipeFileList.do";
	}
	
	// 하수도 동영상입력화면 호출
	@RequestMapping(value = "/drainpipe/insertDrainPipeVideo.do")
	public String insertDrainPipeVideo(@ModelAttribute("usdmSearchVO") UsdmDefaultVO usdmSearchVO, Model model) throws Exception {
		model.addAttribute("drainPipeVideoVO", new DrainPipeVideoVO());
		return "usp_sdm/UI-042_InsertDrainPipeVideo";
	}
	
	// 하수도 동영상파일정보 저장
	@RequestMapping(value = "/drainpipe/uploadVideoFile.do", method = RequestMethod.POST)
	public String insertDrainPipeVideoFile(@ModelAttribute("usdmSearchVO") UsdmDefaultVO usdmSearchVO, DrainPipeVideoVO drainPipeVideoVO, BindingResult bindingResult, Model model, SessionStatus status)
			throws Exception {
		String realPath = servletContext.getRealPath("/");
		String tempPath = realPath + "upload" + File.separator;
		String savePath = tempPath.replace(File.separator, "/");
		String realFileName = "";
		
		savePath = "C:/sdm_uploadedFiles/drainpipe_video/";
		File uploadPath = new File(savePath);
		
		if(!uploadPath.exists()) {
			uploadPath.mkdirs();
		}
		
		// Server-Side Validation
		beanValidator.validate(drainPipeVideoVO, bindingResult);
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("drainPipeVideoVO", drainPipeVideoVO);
			return "forward:/drainpipe/insertDrainPipeVideo.do";
		}
		
		// upload file first
		MultipartFile file = drainPipeVideoVO.getFile();
		realFileName = UsdmUtils.uploadFile(file, savePath, model);
		
		drainPipeVideoVO.setFileName(file.getOriginalFilename());
		drainPipeVideoVO.setFilePath(savePath);
		drainPipeVideoVO.setFileRealName(realFileName);
		
		drainPipeService.insertDrainPipeVideo(drainPipeVideoVO);
		status.setComplete();
		
		return "forward:/drainpipe/selectDrainPipeFileList.do";
	}
	
	// 하수맨홀, 하수관선택 팝업 호출
	@RequestMapping(value="/drainpipe/selectPipePopup.do")
	public String selectPipePopup (@ModelAttribute("usdmSearchVO") UsdmDefaultVO usdmSearchVO, Model model) throws Exception {
		List<?> manholeList = drainPipeService.selectDrainManholeGeometry(usdmSearchVO);
		List<?> pipeList    = drainPipeService.selectDrainPipeGeometry(usdmSearchVO);
		
		model.addAttribute("manholeList", manholeList);
		model.addAttribute("pipeList",    pipeList);
		
		//return "/usp_sdm/UI-043_SelectPipePopup_naver";	// 네이버지도
		return "/usp_sdm/UI-043_SelectPipePopup_daum";	// 다음지도
	}

	/********************************************
	 * 하수도 센싱데이터 조회 (UI-070)          * 
	 ********************************************/
	
	/********************************************
	 * 하수도 파일목록 조회 (UI-080)            * 
	 ********************************************/
	
	// 파일목록화면 호출 및 조회
	@RequestMapping(value = "/drainpipe/selectDrainPipeFileList.do")
	public String selectFileInfoList(@ModelAttribute("usdmSearchVO") UsdmDefaultVO usdmSearchVO, ModelMap model) throws Exception {
		List<?> fileList = drainPipeService.selectDrainPipeFileList(usdmSearchVO);
		model.addAttribute("resultList", fileList);

		return "usp_sdm/UI-080_SelectDrainPipeFileList";
	}

	// 이미지파일 상세화면 호출 및 조회
	@RequestMapping(value = "/drainpipe/selectDrainPipeImageDetail.do")
	public String selectImageInfo(@RequestParam(value="fileId") String fileId, ModelMap model) throws Exception {
		DrainPipeImageVO drainPipeImageVO = new DrainPipeImageVO();
		drainPipeImageVO.setImageId(Integer.parseInt(fileId));
		
		model.addAttribute(drainPipeService.selectDrainPipeImageDetail(drainPipeImageVO));
		
		return "usp_sdm/UI-081_SelectDrainPipeImageDetail";
	}
		
	// 동영상파일 상세화면 호출 및 조회
	@RequestMapping(value = "/drainpipe/selectDrainPipeVideoDetail.do")
	public String selectVideoInfo(@RequestParam(value="fileId") String fileId, ModelMap model) throws Exception {
		DrainPipeVideoVO drainPipeVideoVO = new DrainPipeVideoVO();
		drainPipeVideoVO.setVideoId(Integer.parseInt(fileId));
		
		model.addAttribute(drainPipeService.selectDrainPipeVideoDetail(drainPipeVideoVO));
		
		return "usp_sdm/UI-082_SelectDrainPipeVideoDetail";
	}
	
	/********************************************
	 * 하수도 사고정보 등록 및 조회 (UI-110)    * 
	 ********************************************/
	
	// 하수도 사고정보 입력화면 호출
	@RequestMapping(value = "/drainpipe/insertDrainAccident.do")
	public String insertDrainPipeAccident(@ModelAttribute("usdmSearchVO") UsdmDefaultVO usdmSearchVO, Model model) throws Exception {
		model.addAttribute("accidentVO", new DrainPipeAccidentVO());
		return "usp_sdm/UI-110_InsertDrainAccident";
	}
	
	// 하수관선택 팝업 호출
	@RequestMapping(value="/drainpipe/selectAccidentPipePopup.do")
	public String selectAccidentPipePopup (@ModelAttribute("usdmSearchVO") UsdmDefaultVO usdmSearchVO, Model model) throws Exception {
		List<?> pipeList = drainPipeService.selectDrainPipeGeometry(usdmSearchVO);
		
		model.addAttribute("pipeList", pipeList);
		
		return "/usp_sdm/UI-111_SelectDrainPopup";
	}
		
	// 하수도 사고정보 저장
	@RequestMapping(value = "/drainpipe/saveDrainAccident.do", method = RequestMethod.POST)
	public String saveDrainPipeAccident(@ModelAttribute("usdmSearchVO") UsdmDefaultVO usdmSearchVO, DrainPipeAccidentVO accidentVO, BindingResult bindingResult, Model model, SessionStatus status)
			throws Exception {
		
		// Server-Side Validation
		beanValidator.validate(accidentVO, bindingResult);
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("accidentVO", accidentVO);
			return "forward:/drainpipe/insertDrainAccident.do";
		}
		
		drainPipeService.insertDrainAccident(accidentVO);
		status.setComplete();
		
		return "forward:/drainpipe/selectDrainAccidentList.do";
	}
	
	// 사고정보 목록화면 호출 및 조회
	@RequestMapping(value = "/drainpipe/selectDrainAccidentList.do")
	public String selectDrainPipeAccidentList(@ModelAttribute("usdmSearchVO") UsdmDefaultVO usdmSearchVO, ModelMap model) throws Exception {
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
		
		model.addAttribute("resultList", drainPipeService.selectDrainAccidentList(usdmSearchVO));

		return "usp_sdm/UI-112_SelectDrainAccidentList";
	}
	
	// 사고정보 상세화면 호출 및 조회
	@RequestMapping(value = "/drainpipe/selectDrainAccidentDetail.do")
	public String selectDrainPipeAccidentDetail(
			  @RequestParam(value="pipeFtrCde") String pipeFtrCde
			, @RequestParam(value="pipeFtrIdn") String pipeFtrIdn 
			, @RequestParam(value="longitude") String longitude 
			, @RequestParam(value="latitude") String latitude 
			, @RequestParam(value="accidentTime") String accidentTime 
			, ModelMap model) throws Exception {
		
		DrainPipeAccidentVO accidentVO = new DrainPipeAccidentVO();
		accidentVO.setPipeFtrCde(pipeFtrCde);
		accidentVO.setPipeFtrIdn(pipeFtrIdn);
		accidentVO.setLongitude(longitude);
		accidentVO.setLatitude(latitude);
		accidentVO.setAccidentTime(Long.parseLong(accidentTime));
		
		model.addAttribute("accidentVO", drainPipeService.selectDrainAccidentDetail(accidentVO));
		
		return "usp_sdm/UI-113_SelectDrainAccidentDetail";
	}
	
	/********************************************
	 * 공통                                     * 
	 ********************************************/

}