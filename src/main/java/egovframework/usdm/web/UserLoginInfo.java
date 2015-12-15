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

import java.security.MessageDigest;

public class UserLoginInfo {
	private String userId;
	private String password;
	private String userName;
	private String authGroupCd;

	public void setUserId(String user_id) {
		userId = user_id;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setPassword(String passwd) {
		password = passwd;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setUserName(String user_name) {
		userName = user_name;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setAuthGroupCd(String auth_group_cd) {
		authGroupCd = auth_group_cd;
	}
	
	public String getAuthGroupCd() {
		return authGroupCd;
	}
	
	public String testSHA256(String str) throws Exception {

		String SHA = ""; 

		try{

			MessageDigest sh = MessageDigest.getInstance("SHA-256"); 
			sh.update(str.getBytes()); 

			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer(); 

			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}

			SHA = sb.toString();

		} catch(Exception e) {
			e.printStackTrace(); 
			SHA = null; 
		}

		return SHA;
	}
	
}