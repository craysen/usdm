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

import java.math.BigDecimal;
import java.util.List;

import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.usdm.service.*;
import egovframework.usdm.web.util.UsdmUtils;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springmodules.validation.commons.DefaultBeanValidator;

@Controller
public class CommonController {

	/** CommonService */
	@Resource(name = "commonService")
	private CommonService commonService;
	
	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** Validator */
	@Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;

	@Autowired
    private ServletContext servletContext;
	
	/********************************************
	 * 로그인 (UI-010)                          *
	 ********************************************/
	
	// 로그인화면 호출
	@RequestMapping(value = "/login.do")
	public String userLogin() throws Exception {
		return "usp_sdm/UI-010_Login";
	}
	
	// 로그인
	@RequestMapping(value = "/main.do", method = RequestMethod.POST)
	public String loginAction(UserLoginInfo user, HttpSession session) throws Exception {
		
		String returnStr = "usp_sdm/mainframe";
		
		/*
		UserLoginInfo loginUser = loginBO.findByUserIdAndPassword(user.getUserId(), user.getPassword());
		
		if (loginUser != null) {
        
        	UserLoginInfo loginUser = new UserLoginInfo();
        	
	        loginUser.setUserId("admin");
	        loginUser.setUserName("관리자");
	        loginUser.setAuthGroupCd("A");
	        
	        session.setAttribute("userLoginInfo", loginUser);
	        
	        returnStr = "usp_sdm/mainframe";
        }
        else {
        	returnStr = "redirect:/loginerror.do";
        }
        */

		UserLoginInfo loginUser = new UserLoginInfo();
		
        if (user.getUserId().equals("admin")) {
        	
        	loginUser.setUserId(user.getUserId());
	        loginUser.setUserName("관리자");
	        loginUser.setAuthGroupCd("ADMIN");
        }
        else if (user.getUserId().equals("etri")) {
        	
        	loginUser.setUserId(user.getUserId());
        	loginUser.setUserName("ETRI");
        	loginUser.setAuthGroupCd("ETRI");
        }
        else if (user.getUserId().equals("sewer")) {
        	
        	loginUser.setUserId(user.getUserId());
        	loginUser.setUserName("건기연");
        	loginUser.setAuthGroupCd("KICT");
        }
        else if (user.getUserId().equals("metro")) {
        	
        	loginUser.setUserId(user.getUserId());
        	loginUser.setUserName("철도연");
        	loginUser.setAuthGroupCd("KRRI");
        }
        else if (user.getUserId().equals("water")) {
        	
        	loginUser.setUserId(user.getUserId());
        	loginUser.setUserName("지자원");
        	loginUser.setAuthGroupCd("KIGAM");
        }
        else {
        	returnStr = "redirect:/loginError.do";
        }
        
        session.setAttribute("userLoginInfo", loginUser);
        	
        return returnStr;
	}
	
	// 로그인
	// login.jsp를 통하지 않고 바로 관리자로 로그인하여 메인화면으로 진입
	@RequestMapping(value = "/loginMain.do", method = RequestMethod.GET)
	public String loginMainAction(UserLoginInfo user, HttpSession session) throws Exception {
		
		UserLoginInfo loginUser = new UserLoginInfo();
		
		loginUser.setUserId("admin");
		loginUser.setUserName("관리자");
		loginUser.setAuthGroupCd("ADMIN");
		
		session.setAttribute("userLoginInfo", loginUser);
		
		return "usp_sdm/mainframe";
	}
	
	// 로그인 실패
	@RequestMapping(value = "/loginError.do")
	public String userLoginError() throws Exception {
		return "usp_sdm/UI-010_Login";
	}
	
	// 로그아웃
	@RequestMapping(value = "/logout.do")
	public String userLogout(HttpSession session) throws Exception {
		session.invalidate();
		return "redirect:/login.do";
	}
	
	/********************************************
	 * 메인 (UI-020)                            * 
	 ********************************************/
	
	// 메인화면 호출
	@RequestMapping(value = "/mainframe.do")
	public String callMain (@ModelAttribute("usdmSearchVO") UsdmDefaultVO usdmSearchVO, Model model) throws Exception {
		List<?> sensorList = commonService.selectSensorNodeGeometry(usdmSearchVO);
		List<?> valueList  = commonService.selectSensorNodeSensingValue(usdmSearchVO);
		
		model.addAttribute("sensorList", sensorList);
		model.addAttribute("valueList",  valueList);
		
		return "usp_sdm/UI-020_Main";
	}
	
	@RequestMapping(value = "/topmenu.do")
	public String callTopMenu() throws Exception {
		return "usp_sdm/topmenu";
	}
	
	@RequestMapping(value = "/leftmenu.do")
	public String callLeftMenu() throws Exception {
		return "usp_sdm/leftmenu";
	}
	
	/********************************************
	 * 센서등록 (UI-030)                        * 
	 ********************************************/
	
	/********************************************
	 * 공통                                     *
	 ********************************************/
	
}