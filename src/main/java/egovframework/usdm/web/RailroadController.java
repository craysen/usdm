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
public class RailroadController {

	/** RailroadService */
	@Resource(name = "railroadService")
	private RailroadService railroadService;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** Validator */
	@Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;
	
	@Autowired
    private ServletContext servletContext;

	/********************************************
	 * 도시철도 센싱데이터 입력 (UI-050)        * 
	 ********************************************/

	// 도시철도 동영상입력화면 호출
	@RequestMapping(value = "/railroad/insertRailroadVideo.do")
	public String insertRailroadVideo(@ModelAttribute("usdmSearchVO") UsdmDefaultVO usdmSearchVO, Model model) throws Exception {
		model.addAttribute("railroadVideoVO", new RailroadVideoVO());
		
		List<?> comboList = railroadService.selectMeasureTypeCombo();
		model.addAttribute("comboList", comboList);
		
		return "usp_sdm/UI-052_InsertRailroadVideo";
	}
	
	// 도시철도 동영상파일정보 저장
	@RequestMapping(value = "/railroad/uploadVideoFile.do", method = RequestMethod.POST)
	public String insertRailroadVideoFile(@ModelAttribute("usdmSearchVO") UsdmDefaultVO usdmSearchVO, RailroadVideoVO railroadVideoVO, BindingResult bindingResult, Model model, SessionStatus status)
			throws Exception {
		String realPath = servletContext.getRealPath("/");
		String tempPath = realPath + "upload" + File.separator;
		String savePath = tempPath.replace(File.separator, "/");
		String realFileName = "";
		
		savePath = "C:/sdm_uploadedFiles/railroad_video/";
		File uploadPath = new File(savePath);
		
		if(!uploadPath.exists()) {
			uploadPath.mkdirs();
		}
			
		// Server-Side Validation
		beanValidator.validate(railroadVideoVO, bindingResult);
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("railroadVideoVO", railroadVideoVO);
			return "forward:/railroad/insertRailroadVideo.do";
		}
		
		// upload file first
		MultipartFile file = railroadVideoVO.getFile();
		realFileName = UsdmUtils.uploadFile(file, savePath, model);
		
		railroadVideoVO.setFileName(file.getOriginalFilename());
		railroadVideoVO.setFilePath(savePath);
		railroadVideoVO.setFileRealName(realFileName);
		
		railroadService.insertRailroadVideo(railroadVideoVO);
		status.setComplete();
		
		return "forward:/railroad/selectRailroadFileList.do";
	}

	/********************************************
	 * 도시철도 센싱데이터 조회 (UI-071)        * 
	 ********************************************/
	
	/********************************************
	 * 도시철도 파일목록 조회 (UI-090)          * 
	 ********************************************/
	
	// 파일목록화면 호출 및 조회
	@RequestMapping(value = "/railroad/selectRailroadFileList.do")
	public String selectFileInfoList(@ModelAttribute("usdmSearchVO") UsdmDefaultVO usdmSearchVO, ModelMap model) throws Exception {
		List<?> fileList = railroadService.selectRailroadVideoList(usdmSearchVO);
		model.addAttribute("resultList", fileList);
		
		return "usp_sdm/UI-090_SelectRailroadFileList";
	}
		
	// 동영상파일 상세화면 호출 및 조회
	@RequestMapping(value = "/railroad/selectRailroadVideoDetail.do")
	public String selectVideoInfo(@RequestParam(value="fileId") String fileId, ModelMap model) throws Exception {
		RailroadVideoVO railroadVideoVO = new RailroadVideoVO();
		railroadVideoVO.setVideoId(Integer.parseInt(fileId));
		
		model.addAttribute(railroadService.selectRailroadVideoDetail(railroadVideoVO));
		
		return "usp_sdm/UI-092_SelectRailroadVideoDetail";
	}
	
	/********************************************
	 * 공통                                     * 
	 ********************************************/

}