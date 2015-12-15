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
package egovframework.usdm.web.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

public final class UsdmUtils {
	
	// 파일 업로드
	public static String uploadFile(MultipartFile file, String savePath, Model model) throws Exception {
		String rename = "";
		
		try {
			long time = System.currentTimeMillis();
			SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.KOREA);
			
			File dir = new File(savePath);
			if (!dir.exists()) dir.mkdir();
			
			// 원본파일명
			String originalFilename = file.getOriginalFilename();
			// 파일명
			String onlyFileName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
			// 확장자
			String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
			
			// 실제 저장될 파일명 (원본파일명 + 연월일시분초)
			rename = onlyFileName.replace(" ", "_") + "_" + dayTime.format(new Date(time)) + extension;
			String fullPath = savePath + rename;
	
			if (!file.isEmpty()) {
			    try {
			        byte[] bytes = file.getBytes();
			        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(fullPath)));
			        stream.write(bytes);
			        stream.close();
			        model.addAttribute("resultMsg", "파일을 업로드 성공!");
			    } catch (Exception e) {
			        model.addAttribute("resultMsg", "파일을 업로드하는 데에 실패했습니다.");
			    }
			}
			else {
			    model.addAttribute("resultMsg", "업로드할 파일을 선택해주시기 바랍니다.");
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rename;
	}
	
	// JSON 변환 (JSON -> Map)
	public static Map<String, Object> convertJsonToMap(String json) throws JsonMappingException, JsonParseException, Exception {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			map = mapper.readValue(json, new TypeReference<HashMap<String, Object>>() {});
		
		} catch (JsonMappingException e) {
			e.printStackTrace();

		} catch (JsonParseException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
    }
	
	// JSON 변환 (Map -> JSON)
	public static String convertObjToJson(Object obj) {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = "";
		
		try {
			json = ow.writeValueAsString(obj);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	// 주소에서 자원ID 추출
	public static String getGwIDFromAddress (String addr) { return addr.split(":",4)[0]; }
	public static String getPanIDFromAddress(String addr) { return addr.split(":",4)[1]; }
	public static String getSnIDFromAddress (String addr) { return addr.split(":",4)[2]; }
	public static String getTdIDFromAddress (String addr) { return addr.split(":",4)[3]; }
	
	// 날짜 변환 (문자열->Date)
	public static long convertStrToDate(String originStr, String format) throws Exception {
		String convertedStr = "";
		long date = 0;
		
		try {
			SimpleDateFormat inputFormat  = new SimpleDateFormat(format);
			SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        
			convertedStr = outputFormat.format(inputFormat.parse(originStr));
			
			Date dt = outputFormat.parse(convertedStr);
			date = dt.getTime();
					
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return date;
	}
	
	// 날짜 변환 (Date->문자열)
	public static String convertDateToStr(double originDate, String format) throws Exception {
		String convertedStr = "";
		
		try {
			Date dt = new Date((long)originDate);
			SimpleDateFormat sf = new SimpleDateFormat(format);
        
			convertedStr = sf.format(dt);
					
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return convertedStr;
	}
	
	// 메세지 로그 생성
	public static void writeLog(String uri, String request, String response) {
		SimpleDateFormat loggingTime    = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		SimpleDateFormat dateInFileName = new SimpleDateFormat("yyyyMMdd");

		long currentTime = System.currentTimeMillis();
		
		String time        = loggingTime.format(currentTime);
		String logFilePath = "C:\\sdm_log\\";
		String logFileName = "usp_sdm_" + dateInFileName.format(currentTime) + ".log";
		
		File logPath = new File(logFilePath);
		
		if(!logPath.exists()) {
			logPath.mkdirs();
		}
		
		try {
			PrintStream fileStream = new PrintStream(new FileOutputStream(logFilePath + logFileName, true));
			
			fileStream.println("=====================================================================");
			fileStream.println(uri  + " (" + time + ")");
			fileStream.println("=====================================================================");
			fileStream.println("<Request>");
			fileStream.println(request);
			fileStream.println("");
			fileStream.println("<Response>");
			fileStream.println(response);
			fileStream.println("");
			
			fileStream.flush();
			fileStream.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 무작위 문자열 생성
	public static String getRandomString() {
		Random rnd = new Random();
		StringBuffer buf = new StringBuffer();

		for (int i=0; i<20; i++) {
			if(rnd.nextBoolean()) {
				buf.append((char)((int)(rnd.nextInt(26)) + 97));
		    }
			else {
		        buf.append((rnd.nextInt(10))); 
		    }
		}
		
		return buf.toString();
	}

}