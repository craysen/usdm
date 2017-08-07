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
import java.lang.String;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;
import java.security.*;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import egovframework.usdm.service.MessageQueueVO;
import egovframework.com.cmm.EgovProperties;
import egovframework.rte.psl.dataaccess.util.EgovMap;

public final class UsdmUtils {

	// 동영상 파일 다운로드 경로
	static final String sewerVideoDownloadPath  = EgovProperties.getProperty("downloadPath.sewerVideo");
	static final String subwayVideoDownloadPath = EgovProperties.getProperty("downloadPath.subwayVideo");
	
	// 테이블에 저장할 '시간(double)'컬럼의 최대값
	static final public long maxTime = 0;
	
	// TM-WGS 좌표계변환에 사용하는 상수
	static final double a = 6378137; // 장축 길이
	static final double f = 1/298.257223563; // 편명도
	static final double b = a*(1.0-f); // 단축 길이
	static final double e2 = 2*f-f*f; // 이심율
	
	static final double A0 = 1-(e2/4)-((3*e2*e2)/64)-((5*e2*e2*e2)/256);	
	static final double A2 = 3.0/8.0*(e2+(e2*e2)/4.0 + 15.0 * (e2*e2*e2)/128.0);
	static final double A4 = (15.0/256.0)*(e2*e2+((3*e2*e2*e2)/4.0));		
	static final double A6 = (35.0*e2*e2*e2)/3072.0;
		
	static final double E0 = 200000.0;
	static final double N0 = 600000.0;
	static final double k0 = 1;
	
	static final double PROJ_LAT = Double.parseDouble(EgovProperties.getProperty("proj.latitude"));
	static final double PROJ_LON = Double.parseDouble(EgovProperties.getProperty("proj.longitude"));
	
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
	
			BufferedOutputStream stream = null;
			
			if (!file.isEmpty()) {
			    try {
			        byte[] bytes = file.getBytes();
			        stream = new BufferedOutputStream(new FileOutputStream(new File(fullPath)));
			        stream.write(bytes);
			        model.addAttribute("resultMsg", "파일을 업로드 성공!");
			        
			    } catch (Exception e) {
			        model.addAttribute("resultMsg", "파일을 업로드하는 데에 실패했습니다.");
			        
			    } finally {
			    	if (stream != null) {
			    		try {
		                    stream.close();
		                } catch (Exception e) {
		                    e.printStackTrace();
		                }
		            }
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
	
	// JSON->Map 변환
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
	
	// Object->JSON 변환
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
		
		} catch (ParseException e) {
			throw new InvalidParameterException("Invalid date format");
		
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
	
	// 메세지 로그 생성(interface 메세지)
	public static void writeLog(String uri, String request, String response) {
		SimpleDateFormat loggingTime    = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		SimpleDateFormat dateInFileName = new SimpleDateFormat("yyyyMMdd");

		long currentTime = System.currentTimeMillis();
		
		String time		= loggingTime.format(currentTime);
		String fileName	= "usp_sdm_" + dateInFileName.format(currentTime) + ".log";

		File logPath = new File(EgovProperties.getProperty("log.path"));
		
		if (!logPath.exists())
			logPath.mkdirs();
		
		try {
			PrintStream fileStream = new PrintStream(new FileOutputStream(logPath.getCanonicalFile().getPath() + System.getProperty("file.separator") + fileName, true));
			
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

	// 메세지 로그 생성(message queue 전송메세지)
	public static void writeLog(String uri, String message) {
		SimpleDateFormat loggingTime    = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		SimpleDateFormat dateInFileName = new SimpleDateFormat("yyyyMMdd");
		
		long currentTime = System.currentTimeMillis();
		
		String time		= loggingTime.format(currentTime);
		String fileName	= "usp_sdm_" + dateInFileName.format(currentTime) + ".log";
		
		File logPath = new File(EgovProperties.getProperty("log.path"));
		
		if (!logPath.exists())
			logPath.mkdirs();
		
		try {
			PrintStream fileStream = new PrintStream(new FileOutputStream(logPath.getCanonicalFile().getPath() + System.getProperty("file.separator") + fileName, true));
			
			fileStream.println("=====================================================================");
			fileStream.println(uri  + " (" + time + ")");
			fileStream.println("=====================================================================");
			fileStream.println("<Notice>");
			fileStream.println(message);
			fileStream.println("");
			
			fileStream.flush();
			fileStream.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 무작위 문자열 생성 (session key 생성에 사용)
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
	
	// 문자열을 hash문자열로 변환
	public String generateHashSHA256(String str) {
		String SHA = ""; 

		try {
			MessageDigest sh = MessageDigest.getInstance("SHA-256");
			StringBuffer sb = new StringBuffer();
			
			sh.update(str.getBytes()); 

			byte byteData[] = sh.digest();

			for(int i = 0 ; i < byteData.length ; i++) {
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}

			SHA = sb.toString();

		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace(); 
			SHA = null; 
		}

		return SHA;
	}
	
	// 위도(WGS)를 Y좌표(TM)로 변환한다.
	public static double WGSLatitudeToNorth(double latitude, double longitude)
	{
		double lat = latitude*Math.PI/180.0;
		double lat0 = PROJ_LAT*Math.PI/180.0;
		
		double lon =  longitude*Math.PI/180.0;
		double lon0 = PROJ_LON*Math.PI/180.0;

		double m = a*(A0*lat-A2*(Math.sin(2.0*lat)) + A4*(Math.sin(4.0*lat)) - A6*(Math.sin(6.0*lat)));		
		double m0 = a*(A0*lat0 - A2*(Math.sin(2.0*lat0)) + A4*(Math.sin(4.0*lat0)) - A6*(Math.sin(6.0*lat0)));	
		
		double M = (a*(1.0-e2))/Math.pow(1.0-e2*(Math.sin(lat))*(Math.sin(lat)), 1.5);		
		double N = a/Math.sqrt(1.0-e2*(Math.sin(lat))*(Math.sin(lat)));		
		double psi = N/M;
		double t = Math.tan(lat);
		double dlon = lon - lon0;
		
		double Y_T1 = (Math.pow(dlon, 2.0)/2.0)*N*(Math.sin(lat))*(Math.cos(lat));		
		double Y_T2 = (Math.pow(dlon, 4.0)/24.0)*N*(Math.sin(lat))*(Math.pow(Math.cos(lat), 3.0))*(4*psi*psi+psi-t*t);				
		double Y_T3 = (Math.pow(dlon, 6.0)/720.0)*N*(Math.sin(lat))*(Math.pow(Math.cos(lat), 5.0))*(8*(Math.pow(psi, 4.0))*(11.0-24.0*t*t)-28.0*(Math.pow(psi,3.0))*(1.0-6.0*t*t)+(psi*psi)*(1.0-32.0*t*t)-psi*(2.0*t*t)+(Math.pow(t, 4.0)));					
		double Y_T4 = (Math.pow(dlon, 8.0)/40320.0)*(N*Math.sin(lat))*(Math.pow(Math.cos(lat), 7.0))*(1385.0-3111.0*t*t+543.0*(Math.pow(t,4.0)) - (Math.pow(t, 6.0)));
		
		return (N0+k0*(m - m0 + Y_T1 + Y_T2 + Y_T3 + Y_T4));
	}
	
	// 경도(WGS)를 X좌표(TM)로 변환한다.
	public static double WGSLongitudeToEast(double latitude, double longitude)
	{
		double lat = latitude*((Math.PI)/180.0);
		
		double lon =  longitude*((Math.PI)/180.0);
		double lon0 = PROJ_LON*((Math.PI)/180.0);
		
		double M = (a*(1-e2))/Math.pow(1-e2*Math.sin(lat)*Math.sin(lat), 1.5);
		double N = a/Math.sqrt(1-e2*Math.sin(lat)*Math.sin(lat));
		double psi = N/M;
		double t = Math.tan(lat);
		double dlon = lon - lon0;
		
		double X_T1 = ((dlon*dlon)/6.0) * (Math.pow(Math.cos(lat), 2.0)) * (psi-t*t);
		double X_T2 = (Math.pow(dlon, 4.0)/120.0) * (Math.pow(Math.cos(lat), 4.0)) * (4.0*Math.pow(psi,3.0)*(1.0-6.0*t*t)+(psi*psi)*(1.0+8.0*t*t)-psi*2.0*t*t+Math.pow(t, 4.0));
		double X_T3 = (Math.pow(dlon, 6.0)/5040.0)*Math.pow(Math.cos(lat), 6.0)*(61.0-479.0*t*t+179.0*Math.pow(t, 4.0)-Math.pow(t, 6.0));
		
		
		return (E0+k0*N*dlon*Math.cos(lat)*(1+X_T1+X_T2+X_T3));
	}
	
	// WGS좌표를 TM좌표로 변환한다.
	public static SDMPoint WGSToTM(double srcX, double srcY)
	{
		SDMPoint res = new SDMPoint();
		
		double lat = srcY*Math.PI/180.0;
		double lat0 = PROJ_LAT*Math.PI/180.0;
		
		double lon =  srcX*Math.PI/180.0;
		double lon0 = PROJ_LON*Math.PI/180.0;

		double m = a*(A0*lat-A2*(Math.sin(2.0*lat)) + A4*(Math.sin(4.0*lat)) - A6*(Math.sin(6.0*lat)));		
		double m0 = a*(A0*lat0 - A2*(Math.sin(2.0*lat0)) + A4*(Math.sin(4.0*lat0)) - A6*(Math.sin(6.0*lat0)));	
		
		double M = (a*(1.0-e2))/Math.pow(1.0-e2*(Math.sin(lat))*(Math.sin(lat)), 1.5);		
		double N = a/Math.sqrt(1.0-e2*(Math.sin(lat))*(Math.sin(lat)));		
		double psi = N/M;
		double t = Math.tan(lat);
		double dlon = lon - lon0;
		
		double Y_T1 = (Math.pow(dlon, 2.0)/2.0)*N*(Math.sin(lat))*(Math.cos(lat));		
		double Y_T2 = (Math.pow(dlon, 4.0)/24.0)*N*(Math.sin(lat))*(Math.pow(Math.cos(lat), 3.0))*(4*psi*psi+psi-t*t);				
		double Y_T3 = (Math.pow(dlon, 6.0)/720.0)*N*(Math.sin(lat))*(Math.pow(Math.cos(lat), 5.0))*(8*(Math.pow(psi, 4.0))*(11.0-24.0*t*t)-28.0*(Math.pow(psi,3.0))*(1.0-6.0*t*t)+(psi*psi)*(1.0-32.0*t*t)-psi*(2.0*t*t)+(Math.pow(t, 4.0)));					
		double Y_T4 = (Math.pow(dlon, 8.0)/40320.0)*(N*Math.sin(lat))*(Math.pow(Math.cos(lat), 7.0))*(1385.0-3111.0*t*t+543.0*(Math.pow(t,4.0)) - (Math.pow(t, 6.0)));
		
		double X_T1 = ((dlon*dlon)/6.0) * (Math.pow(Math.cos(lat), 2.0)) * (psi-t*t);
		double X_T2 = (Math.pow(dlon, 4.0)/120.0) * (Math.pow(Math.cos(lat), 4.0)) * (4.0*Math.pow(psi,3.0)*(1.0-6.0*t*t)+(psi*psi)*(1.0+8.0*t*t)-psi*2.0*t*t+Math.pow(t, 4.0));
		double X_T3 = (Math.pow(dlon, 6.0)/5040.0)*Math.pow(Math.cos(lat), 6.0)*(61.0-479.0*t*t+179.0*Math.pow(t, 4.0)-Math.pow(t, 6.0));
		
		res.setY(N0+k0*(m - m0 + Y_T1 + Y_T2 + Y_T3 + Y_T4));
		res.setX(E0+k0*N*dlon*Math.cos(lat)*(1+X_T1+X_T2+X_T3));
		
		return res;
	}
	
	// Y좌표(TM)를 위도(WGS)로 변환한다.
	public static double TMEastToLongitude(double north, double east)
	{
		double X1 = north - N0;
		double lat0 = PROJ_LAT*Math.PI/180.0;
		double lon0 = PROJ_LON*Math.PI/180.0;
		double m0 = a*(A0*lat0 - A2*(Math.sin(2.0*lat0)) + A4*(Math.sin(4.0*lat0)) - A6*(Math.sin(6.0*lat0)));	
		double m1 = m0+X1/k0;
		double n = (a-b)/(a+b);
		double G = a*(1.0-n)*(1.0-n*n)*(1.0+(9.0*n*n)/4.0+(225.0*n*n*n*n)/64.0)*(Math.PI/180.0);
		double d = (m1/G)*(Math.PI/180.0);
		double c = d+((3.0*n)/2.0-(27.0*n*n*n)/32.0)*Math.sin(2.0*d)+((21.0*n*n)/16.0-(55.0*n*n*n*n)/32.0)*Math.sin(4.0*d)+((151.0*n*n*n)/96.0)*Math.sin(6.0*d)+((1097.0*n*n*n*n)/512.0)*Math.sin(8.0*d);
		double M1 = (a*(1.0-e2))/Math.pow(1-e2*Math.sin(c)*Math.sin(c), 1.5);
		double N1 = a/Math.sqrt(1.0-e2*Math.sin(c)*Math.sin(c));
		double H = N1/M1;
		double t1 = Math.tan(c);
		double Y1 = east - E0;
		double K = Y1/(k0*N1);
		double T1, T2, T3, T4;
		
		double sec = 1.0/Math.cos(c);
		
		T1 = K*sec;
		T2 = ((K*K*K*sec)/6.0)*(H+2.0*t1*t1);
		T3 = ((Math.pow(K, 5.0)*sec)/120.0)*(-4.0*H*H*H*(1.0-6.0*t1*t1)+H*H*(9.0*68.0*t1*t1)+72.0*H*t1*t1+24.0*Math.pow(t1,4.0));
		T4 = ((Math.pow(K, 7.0)*sec)/5040.0)*(61.0*662.0*t1*t1+1320.0*Math.pow(t1, 4.0)+720.0*Math.pow(t1, 6.0));
		
		return (lon0+T1-T2+T3-T4)*(180.0/Math.PI);
	}
		
	// X좌표(TM)를 경도(WGS)로 변환한다.
	public static double TMNorthToLatitude(double north, double east)
	{
		double X1 = north - N0;
		double lat0 = PROJ_LAT*Math.PI/180.0;
		double m0 = a*(A0*lat0 - A2*(Math.sin(2.0*lat0)) + A4*(Math.sin(4.0*lat0)) - A6*(Math.sin(6.0*lat0)));	
		
		double m1 = m0+X1/k0;
		double n = (a-b)/(a+b);
		double G = a*(1.0-n)*(1.0-n*n)*(1.0+((9.0*n*n)/4.0)+((225.0*n*n*n*n)/64.0))*(Math.PI/180.0);		
		double d = (m1/G)*(Math.PI/180.0);
		double c = d + ((3.0*n)/2.0-(27.0*n*n*n)/32.0)*Math.sin(2.0*d)+(((21.0*n*n)/16.0)-((55.0*n*n*n*n)/32.0))*Math.sin(4.0*d)+((151.0*n*n*n)/96.0)*Math.sin(6.0*d)+((1097.0*n*n*n*n)/512.0)*Math.sin(8.0*d);		
		double M1 = (a*(1.0-e2))/Math.pow(1-e2*Math.sin(c)*Math.sin(c), 1.5);		
		double N1 = a/Math.sqrt(1.0-e2*Math.sin(c)*Math.sin(c));		
		double H = N1/M1;
		double t1 = Math.tan(c);		
		double Y1 = east - E0;
		double K = Y1/(k0*N1);
		
		double T1, T2, T3, T4;
		
		T1 = (t1/(k0*M1))*((Y1*K)/2.0);
		T2 = (t1/(k0*M1))*((Y1*K*K*K)/24.0)*(-4.0*H*H + 9.0*H*(1.0-t1*t1)+12.0*t1*t1);		
		T3 = (t1/(k0*M1))*((Y1*Math.pow(K, 5.0))/720.0)*(8.0*Math.pow(H, 4.0)*(11.0-24.0*t1*t1) - 12.0*Math.pow(H, 3.0)*(21.0-71.0*t1*t1)+15.0*H*H*(15.0-98.0*t1*t1+15.0*Math.pow(t1, 4.0))+180.0*H*(5.0*t1*t1-3.0*Math.pow(t1, 4.0))+360.0*Math.pow(t1, 4.0));
		T4 = (t1/(k0*M1))*((Y1*Math.pow(K, 7.0))/40320.0)*(1385.0+3633.0*t1*t1+4095.0*Math.pow(t1, 4.0)+1575.0*Math.pow(t1, 6.0)); 
		
		return (c-T1+T2-T3+T4)*(180.0/Math.PI);
	}	
	
	// TM좌표를 WGS좌표로 변환한다.
	public static SDMPoint TMToWGS(double srcX, double srcY)
	{
		SDMPoint res = new SDMPoint();
		
		double X1 = srcY - N0;
		double lat0 = PROJ_LAT*Math.PI/180.0;
		double lon0 = PROJ_LON*Math.PI/180.0;
		double m0 = a*(A0*lat0 - A2*(Math.sin(2.0*lat0)) + A4*(Math.sin(4.0*lat0)) - A6*(Math.sin(6.0*lat0)));	
		
		double m1 = m0+X1/k0;
		double n = (a-b)/(a+b);
		double G = a*(1.0-n)*(1.0-n*n)*(1.0+((9.0*n*n)/4.0)+((225.0*n*n*n*n)/64.0))*(Math.PI/180.0);		
		double d = (m1/G)*(Math.PI/180.0);
		double c = d + ((3.0*n)/2.0-(27.0*n*n*n)/32.0)*Math.sin(2.0*d)+(((21.0*n*n)/16.0)-((55.0*n*n*n*n)/32.0))*Math.sin(4.0*d)+((151.0*n*n*n)/96.0)*Math.sin(6.0*d)+((1097.0*n*n*n*n)/512.0)*Math.sin(8.0*d);		
		double M1 = (a*(1.0-e2))/Math.pow(1-e2*Math.sin(c)*Math.sin(c), 1.5);		
		double N1 = a/Math.sqrt(1.0-e2*Math.sin(c)*Math.sin(c));		
		double H = N1/M1;
		double t1 = Math.tan(c);		
		double Y1 = srcX - E0;
		double K = Y1/(k0*N1);
		double sec = 1.0/Math.cos(c);
		
		double T1, T2, T3, T4;
		
		T1 = (t1/(k0*M1))*((Y1*K)/2.0);
		T2 = (t1/(k0*M1))*((Y1*K*K*K)/24.0)*(-4.0*H*H + 9.0*H*(1.0-t1*t1)+12.0*t1*t1);		
		T3 = (t1/(k0*M1))*((Y1*Math.pow(K, 5.0))/720.0)*(8.0*Math.pow(H, 4.0)*(11.0-24.0*t1*t1) - 12.0*Math.pow(H, 3.0)*(21.0-71.0*t1*t1)+15.0*H*H*(15.0-98.0*t1*t1+15.0*Math.pow(t1, 4.0))+180.0*H*(5.0*t1*t1-3.0*Math.pow(t1, 4.0))+360.0*Math.pow(t1, 4.0));
		T4 = (t1/(k0*M1))*((Y1*Math.pow(K, 7.0))/40320.0)*(1385.0+3633.0*t1*t1+4095.0*Math.pow(t1, 4.0)+1575.0*Math.pow(t1, 6.0)); 
		
		res.setY((c-T1+T2-T3+T4)*(180.0/Math.PI));

		T1 = K*sec;
		T2 = ((K*K*K*sec)/6.0)*(H+2.0*t1*t1);
		T3 = ((Math.pow(K, 5.0)*sec)/120.0)*(-4.0*H*H*H*(1.0-6.0*t1*t1)+H*H*(9.0*68.0*t1*t1)+72.0*H*t1*t1+24.0*Math.pow(t1,4.0));
		T4 = ((Math.pow(K, 7.0)*sec)/5040.0)*(61.0*662.0*t1*t1+1320.0*Math.pow(t1, 4.0)+720.0*Math.pow(t1, 6.0));
		
		res.setX((lon0+T1-T2+T3-T4)*(180.0/Math.PI));

		return res;
	}
	
	// geo-object 유형을 입력받아 geo-object 테이블명을 반환한다.
	public static String getGeoTableName(String type) throws InvalidParameterException {
		String tableName;
		
		if (type.equals("")) return "";
		
		switch (type) {
		case "water":		// 상수관
			tableName = "sdm_waterpipe";
			break;
		case "w_manhole":	// 상수맨홀
			tableName = "sdm_watermanhole";
			break;
		case "sewer":		// 하수관
			tableName = "sdm_drainpipe";
			break;
		case "s_manhole":	// 하수맨홀
			tableName = "sdm_drainmanhole";
			break;
		case "subway":		// 지하철로
			tableName = "sdm_subwayline";
			break;
		case "subway_s":	// 지하철역사
			tableName = "sdm_subwaystation";
			break;
		case "geology":		// 지반(TBD)
		case "groundwater":	// 지하수(TBD)
		default:
			//throw new InvalidParameterException("unsupported geometry type");
			tableName = "";
		}
		
		return tableName;
	}
	
	// geo-object 유형을 입력받아 사고정보 테이블명을 반환한다.
	public static String getAccidentTableName(String type) throws InvalidParameterException {
		String tableName;
		
		if (type.equals("")) return "";
		
		switch (type) {
		case "water":		// 상수관
			tableName = "sdm_wateraccident";
			break;
		case "sewer":		// 하수관
			tableName = "sdm_drainaccident";
			break;
		case "w_manhole":	// 상수맨홀(TBD)
		case "s_manhole":	// 하수맨홀(TBD)
		case "subway":		// 지하철로(TBD)
		case "subway_s":	// 지하철역사(TBD)
		case "geology":		// 지반(TBD)
		case "groundwater":	// 지하수(TBD)
		default:
			throw new InvalidParameterException("unsupported accident type");
		}
		
		return tableName;
	}
	
	// 문자열 List를 SQL의 IN 절에 사용가능한 문자열로 변환한다.
	// ["a","b","c"] => "'a','b','c'"
	public static String getInOperatorString(List<String> strList)
	{
		String str = "";
		
		if (!strList.isEmpty()) {
			for (int i=0; i<strList.size(); i++) {
				str += "'" + strList.get(i) + "'";
				
				if (i<strList.size()-1) str += ",";
			}
		}
		else str = "''";
		
		return str;
	}
	public static String getInOperatorString(List<EgovMap> mapList, String name)
	{
		String str = "";
		
		if (!mapList.isEmpty()) {
			for (int i=0; i<mapList.size(); i++) {
				str += "'" + mapList.get(i).get(name) + "'";
				
				if (i<mapList.size()-1) str += ",";
			}
		}
		else str = "''";
		
		return str;
	}
	
	// MQ에 메세지를 전송한다.
	public static void sendMessageMQ(MessageQueueVO messageVo) throws Exception {
		try {
			messageVo.setTimestamp(convertDateToStr(System.currentTimeMillis(), "yyyyMMdd'T'hhmmss"));

			String message = convertObjToJson(messageVo);
			
			// MQ에 전송
			RabbitMQManager.sendMessage(message);
			// 로그생성
			writeLog("Notice", message);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 동영상 파일을 다운로드하기 위한 경로를 반환한다.
	public static String getFileDownloadPath(String fileName, String type) throws Exception {
		String downloadPath = "";
		
		// 하수도 동영상
		if (type.equals("sewer"))
			downloadPath = sewerVideoDownloadPath + fileName;
		// 지하철 동영상
		else if (type.equals("subway"))
			downloadPath = subwayVideoDownloadPath + fileName;
		
		return downloadPath;
	}
	
	// operator 문자열을 operator로 변환한다.
	public static String getOperator(String operation) throws InvalidParameterException {
		String operator = "";
		
		if (operation.equals("GT")) {
			operator = ">";
		}
		else if (operation.equals("GE")) {
			operator = ">=";
		}
		else if (operation.equals("LT")) {
			operator = "<";
		}
		else if (operation.equals("LE")) {
			operator = "<=";
		}
		else if (operation.equals("EQ")) {
			operator = "=";
		}
		else if (operation.equals("NE")) {
			operator = "!=";
		}
		else {
			throw new InvalidParameterException("unsupported operator");
		}
		
		return operator;
	}
	
	// operator 문자열과 operand 두 개를 입력받아 operation 결과를 반환한다
	public static boolean getOperationResult(String operation, double operand1, double operand2) throws Exception {
		boolean result = false;
		
		if (operation.equals("GT")) {
			result = operand1 > operand2;
		}
		else if (operation.equals("GE")) {
			result = operand1 >= operand2;
		}
		else if (operation.equals("LT")) {
			result = operand1 < operand2;
		}
		else if (operation.equals("LE")) {
			result = operand1 <= operand2;
		}
		else if (operation.equals("EQ")) {
			result = operand1 == operand2;
		}
		else if (operation.equals("NE")) {
			result = operand1 != operand2;
		}
		else {
			throw new InvalidParameterException("unsupported operator");
		}
		
		return result;
	}
	
	public static String getAccidentTypeName(String accidentType) {
		String accidentTypeName;
		
		switch (accidentType) {
		case "burst":
			accidentTypeName = "파열";
			break;
    	case "hole":
    		accidentTypeName = "구멍";
			break;
    	case "subsidence":
    		accidentTypeName = "침하";
			break;
    	case "cavity":
    		accidentTypeName = "동공";
			break;
    	case "crack":
    		accidentTypeName = "균열";
			break;
    	case "pollution":
    		accidentTypeName = "오염";
			break;
		default:
			accidentTypeName = "기타";
		}
		
		return accidentTypeName;
	}
	
	public static String getAccidentShapeName(String accidentShape) {
		String accidentShapeName;
		
		switch (accidentShape) {
		case "circle":
			accidentShapeName = "원형";
    		break;
    	case "ellipse":
    		accidentShapeName = "타원형";
    		break;
    	case "rectangle":
    		accidentShapeName = "사각형";
    		break;
    	case "line":
    		accidentShapeName = "직선";
    		break;
    	case "zigzag":
    		accidentShapeName = "갈지자";
    		break;
		default:
			accidentShapeName = "기타";
		}
		
		return accidentShapeName;
	}
	
	public static String getAccidentDescName(String accidentDesc) {
		
		String accidentDescName = "";
		String descName;
		String descValue;
		
		String[] accidentDescElement = accidentDesc.replaceAll(" ", "").split(",");
		
		for (int i=0; i<accidentDescElement.length; i++) {
			switch (accidentDescElement[i].split(":")[0]) {
			case "diameter":
				descName = "지름";
				break;
			case "majorAxis":
				descName = "장축";
				break;
			case "minorAxis":
				descName = "단축";
				break;
			case "width":
				descName = "가로";
				break;
			case "height":
				descName = "세로";
				break;
			case "depth":
				descName = "지표깊이";
				break;
			case "length":
				descName = "길이";
				break;
			case "degree":
				descName = "각도";
				break;
			case "direction":
				descName = "방향";
				break;
			case "place":
				descName = "위치";
				break;
			default:
				continue;
			}
			
			switch (accidentDescElement[i].split(":")[1]) {
			case "horizontality":
				descValue = "수평";
				break;
			case "verticality":
				descValue = "수직";
				break;
			case "ceiling":
				descValue = "천정";
				break;
			case "floor":
				descValue = "바닥";
				break;
			case "wall":
				descValue = "벽";
				break;
			default:
				descValue = accidentDescElement[i].split(":")[1];
			}
			
			if (!accidentDescName.equals("")) accidentDescName += ", ";
			
			accidentDescName += descName + ":" + descValue;
		}
		
		return accidentDescName;
	}
	
	/*
	public static String getConfig(String configName) throws Exception
	{
		String configValue = "";

		try {
			System.out.println(REAL_PATH + CONFIG_FILE);
			
			File file = new File(REAL_PATH + CONFIG_FILE);

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			 
			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName(configName);
			Element	configElement = (Element)nodeList.item(0);
			
			configValue = getCharacterDataFromElement(configElement);

		} catch (Exception e) {
			e.printStackTrace();
		}
			
		return configValue;
	}

	private static String getCharacterDataFromElement(Element e)
	{
		NodeList list = e.getChildNodes();
		String data;
		
		for (int index = 0; index < list.getLength(); index++) {
			if (list.item(index) instanceof CharacterData) {
				CharacterData child = (CharacterData) list.item(index);
				data = child.getData();
				
				if (data != null && data.trim().length() > 0)
					return child.getData();
			}
		}
		
		return "";
	}
	*/
}