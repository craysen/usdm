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
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.usdm.service.*;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springmodules.validation.commons.DefaultBeanValidator;

@Controller
public class UnderWaterController {

	/** UnderWaterService */
	@Resource(name = "underWaterService")
	private UnderWaterService underWaterService;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** Validator */
	@Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;

	/********************************************
	 * 지하수 센싱데이터 입력 (UI-060)          * 
	 ********************************************/

	// 지하수 파일입력 화면 호출
	@RequestMapping(value = "/underwater/insertUnderWaterFile.do")
	public String insertUnderWaterFile() throws Exception {
		return "usp_sdm/UI-060_InsertUnderWaterFile";
	}
	
	// 지하수 텍스트파일 저장
	@RequestMapping(value = "/underwater/insertUnderWaterText.do", method = RequestMethod.POST)
	public String insertUnderWaterText(String gwID, String panID, String snID, String tdID, MultipartFile file) throws Exception {
		try {
			// 파일 읽기
			List<UnderWaterVO> underWaterVOList = getUnderWaterVOFromText(file);
		
			// 파일내용 콘솔출력 (테스트용)
			/*
			for (int i=0; i<underWaterVOList.size(); i++) {
				System.out.print(underWaterVOList.get(i).getMeasureDate() + " ");
				System.out.print(underWaterVOList.get(i).getMeasureTime() + " ");
				System.out.print(underWaterVOList.get(i).getWaterLevel() + " ");
				System.out.print(underWaterVOList.get(i).getWaterTemp() + " ");
				System.out.print(underWaterVOList.get(i).getWaterConduct() + " ");
				System.out.print(underWaterVOList.get(i).getWaterTurb() + " ");
				System.out.print(underWaterVOList.get(i).getUpperSoilMoist() + " ");
				System.out.print(underWaterVOList.get(i).getUpperSoilConduct() + " ");
				System.out.print(underWaterVOList.get(i).getUpperSoilTemp() + " ");
				System.out.print(underWaterVOList.get(i).getLowerSoilMoist() + " ");
				System.out.print(underWaterVOList.get(i).getLowerSoilConduct() + " ");
				System.out.print(underWaterVOList.get(i).getLowerSoilTemp() + " ");
				System.out.print("\n");
			}
			*/
		
			// 테이블에 저장
			if (underWaterVOList != null) {
				for (int i=0; i<underWaterVOList.size(); i++) {
					UnderWaterVO underWaterVO = underWaterVOList.get(i);
					underWaterVO.setGwID(gwID);
					underWaterVO.setPanID(panID);
					underWaterVO.setSnID(snID);
					underWaterVO.setTdID(tdID);
					
					underWaterService.insertUnderWaterData(underWaterVO);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "forward:/underwater/selectUnderWaterData.do";
	}
	
	// 지하수 엑셀파일 저장
	@RequestMapping(value = "/underwater/insertUnderWaterExcel.do", method = RequestMethod.POST)
	public String insertUnderWaterExcel(String gwID, String panID, String snID, String tdID, MultipartFile file) throws Exception {
		try {
			// 파일 읽기
			List<UnderWaterVO> underWaterVOList = getUnderWaterVOFromExcel(file);
			
			// 테이블에 저장
			if (underWaterVOList != null) {
				for (int i=0; i<underWaterVOList.size(); i++) {
					UnderWaterVO underWaterVO = underWaterVOList.get(i);
					underWaterVO.setGwID(gwID);
					underWaterVO.setPanID(panID);
					underWaterVO.setSnID(snID);
					underWaterVO.setTdID(tdID);
					
					underWaterService.insertUnderWaterData(underWaterVO);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "forward:/underwater/selectUnderWaterData.do";
	}
	
	/********************************************
	 * 지하수 센싱데이터 조회 (UI-072)          * 
	 ********************************************/
	
	// 지하수 센싱데이터 조회화면 호출
	@RequestMapping(value = "/underwater/selectUnderWaterData.do")
	public String selectUnderWaterData(@ModelAttribute("usdmSearchVO") UsdmDefaultVO usdmSearchVO, ModelMap model) throws Exception {
		List<?> list = underWaterService.selectUnderWaterData(usdmSearchVO);
		model.addAttribute("resultList", list);

		return "usp_sdm/UI-072_SelectUnderWaterData";
	}
		
	/********************************************
	 * 지하수 파일목록 조회 (UI-100)            * 
	 ********************************************/

	/********************************************
	 * 공통                                     * 
	 ********************************************/

	// 지하수 센싱데이터 텍스트파일 읽기
	/*
	 * 파일 내의 모든 행을 읽으며, 모든 행은 다음과 같은 데이터를 포함해야 한다.
	 * 파일 포맷 및 내용에 대한 오류처리 없음 (2015.10.16)
	 * 
	 * 필드1:  측정일자(measureDate)
	 * 필드2:  측정시간(measureTime)
	 * 필드3:  지하수 수위(waterLevel)
	 * 필드4:  지하수 수온(waterTemp)
	 * 필드5:  지하수 전도도(waterConduct)
	 * 필드6:  지하수 탁도(waterTurb)
	 * 필드7:  상층토양 습도(upperSoilMoist)
	 * 필드8:  상층토양 전도도(upperSoilConduct)
	 * 필드9:  상층토양 온도(upperSoilTemp)
	 * 필드10: 하층토양 습도(lowerSoilMoist)
	 * 필드11: 하층토양 전도도(lowerSoilConduct)
	 * 필드12: 하층토양 온도(lowerSoilTemp) 
	 *  
	 */
	public List<UnderWaterVO> getUnderWaterVOFromText(MultipartFile file) throws Exception {
		List<UnderWaterVO> underWaterVOList = new ArrayList<UnderWaterVO>();
		
		try {
			InputStream inputStream = file.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
						
			String line;
			String delimiter = "\t";		// 필드 구분자
			
			while ((line = bufferedReader.readLine()) != null)
			{
				UnderWaterVO underWaterVO = new UnderWaterVO();
				String[] tokens = line.split(delimiter);
	            
	            underWaterVO.setMeasureDate			(tokens[0]);
	            underWaterVO.setMeasureTime			(tokens[1]);
	            underWaterVO.setWaterLevel			(Double.parseDouble(tokens[2]));
	            underWaterVO.setWaterTemp			(Double.parseDouble(tokens[3]));
	            underWaterVO.setWaterConduct		(Double.parseDouble(tokens[4]));
	            underWaterVO.setWaterTurb			(Double.parseDouble(tokens[5]));
	            underWaterVO.setUpperSoilMoist		(Double.parseDouble(tokens[6]));
	            underWaterVO.setUpperSoilConduct	(Double.parseDouble(tokens[7]));
	            underWaterVO.setUpperSoilTemp		(Double.parseDouble(tokens[8]));
	            underWaterVO.setLowerSoilMoist		(Double.parseDouble(tokens[9]));
	            underWaterVO.setLowerSoilConduct	(Double.parseDouble(tokens[10]));
	            underWaterVO.setLowerSoilTemp		(Double.parseDouble(tokens[11]));
	            
	            underWaterVOList.add(underWaterVO);
			}
			
			bufferedReader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return underWaterVOList;
	}
	
	// 지하수 센싱데이터 엑셀파일 읽기
	/*
	 * 파일 내의 모든 sheet를 읽으며, 모든 행은 다음과 같은 셀로 이루어져야 한다.
	 * 셀 내용에 대한 오류처리 없음 (2015.10.16)
	 * 
	 * Cell 0:  측정일자(measureDate)
	 * Cell 1:  측정시간(measureTime)
	 * Cell 2:  지하수 수위(waterLevel)
	 * Cell 3:  지하수 수온(waterTemp)
	 * Cell 4:  지하수 전도도(waterConduct)
	 * Cell 5:  지하수 탁도(waterTurb)
	 * Cell 6:  상층토양 습도(upperSoilMoist)
	 * Cell 7:  상층토양 전도도(upperSoilConduct)
	 * Cell 8:  상층토양 온도(upperSoilTemp)
	 * Cell 9:  하층토양 습도(lowerSoilMoist)
	 * Cell 10: 하층토양 전도도(lowerSoilConduct)
	 * Cell 11: 하층토양 온도(lowerSoilTemp) 
	 *  
	 */
	public List<UnderWaterVO> getUnderWaterVOFromExcel(MultipartFile file) throws Exception {
		List<UnderWaterVO> underWaterVOList = new ArrayList<UnderWaterVO>();
		
		try {
			String 	fileName  	= file.getOriginalFilename();
			int 	lastIndex	= fileName.lastIndexOf(".");
			String 	extension 	= fileName.substring(lastIndex);
			
			// 파일 확장자가 'xls'인 경우
			if (extension.equals(".xls")) {
				HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
				int sheetNum = workbook.getNumberOfSheets();
				
				// 파일의 모든 sheet에 대해 반복
				for (int loop=0; loop<sheetNum; loop++) {
					HSSFSheet sheet = workbook.getSheetAt(loop);
					
					int rows = sheet.getPhysicalNumberOfRows();
					
					// 모든 행에 대해 반복
					for (int rowindex=0; rowindex<rows; rowindex++) {
						HSSFRow row = sheet.getRow(rowindex);
						
						if (row != null) {
							UnderWaterVO underWaterVO = new UnderWaterVO();
				            
				            underWaterVO.setMeasureDate			(row.getCell(0).getStringCellValue() + "");
				            underWaterVO.setMeasureTime			(row.getCell(1).getStringCellValue() + "");
				            underWaterVO.setWaterLevel			(row.getCell(2).getNumericCellValue());
				            underWaterVO.setWaterTemp			(row.getCell(3).getNumericCellValue());
				            underWaterVO.setWaterConduct		(row.getCell(4).getNumericCellValue());
				            underWaterVO.setWaterTurb			(row.getCell(5).getNumericCellValue());
				            underWaterVO.setUpperSoilMoist		(row.getCell(6).getNumericCellValue());
				            underWaterVO.setUpperSoilConduct	(row.getCell(7).getNumericCellValue());
				            underWaterVO.setUpperSoilTemp		(row.getCell(8).getNumericCellValue());
				            underWaterVO.setLowerSoilMoist		(row.getCell(9).getNumericCellValue());
				            underWaterVO.setLowerSoilConduct	(row.getCell(10).getNumericCellValue());
				            underWaterVO.setLowerSoilTemp		(row.getCell(11).getNumericCellValue());
				            
				            underWaterVOList.add(underWaterVO);
						}
					}
				}
				
				workbook.close();
			}
			// 파일 확장자가 'xlsx'인 경우
			else if (extension.equals(".xlsx")) {
				XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
				int sheetNum = workbook.getNumberOfSheets();
				
				// 파일의 모든 sheet에 대해 반복
				for (int loop=0; loop<sheetNum; loop++) {
					XSSFSheet sheet = workbook.getSheetAt(loop);
					
					int rows = sheet.getPhysicalNumberOfRows();
					
					// 모든 행에 대해 반복
					for (int rowindex=0; rowindex<rows; rowindex++) {
						XSSFRow row = sheet.getRow(rowindex);
						
						if (row != null) {
							UnderWaterVO underWaterVO = new UnderWaterVO();
				            
				            underWaterVO.setMeasureDate			(row.getCell(0).getStringCellValue() + "");
				            underWaterVO.setMeasureTime			(row.getCell(1).getStringCellValue() + "");
				            underWaterVO.setWaterLevel			(row.getCell(2).getNumericCellValue());
				            underWaterVO.setWaterTemp			(row.getCell(3).getNumericCellValue());
				            underWaterVO.setWaterConduct		(row.getCell(4).getNumericCellValue());
				            underWaterVO.setWaterTurb			(row.getCell(5).getNumericCellValue());
				            underWaterVO.setUpperSoilMoist		(row.getCell(6).getNumericCellValue());
				            underWaterVO.setUpperSoilConduct	(row.getCell(7).getNumericCellValue());
				            underWaterVO.setUpperSoilTemp		(row.getCell(8).getNumericCellValue());
				            underWaterVO.setLowerSoilMoist		(row.getCell(9).getNumericCellValue());
				            underWaterVO.setLowerSoilConduct	(row.getCell(10).getNumericCellValue());
				            underWaterVO.setLowerSoilTemp		(row.getCell(11).getNumericCellValue());
				            
				            underWaterVOList.add(underWaterVO);
						}
					}
				}
				
				workbook.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        return underWaterVOList;
	}
}