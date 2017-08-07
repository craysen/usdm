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

import javax.servlet.http.HttpServletResponse;

public final class InvalidParameterException extends Exception {
	
	static final int 	responseStatus 	= HttpServletResponse.SC_BAD_REQUEST;
	static final int 	responseCode 	= 4000;
	static final String	responseMsg 	= "Error : Invalid Request Parameter.";
	
	String detailMsg = "unknown cause";
	
	public InvalidParameterException() {
		return;
	}
	
	public InvalidParameterException(String msg) {
		detailMsg = " (" + msg + ")";
		return;
	}
	
	public int getStatus() {
		return responseStatus;
	}
	
	public String getCode() {
		return String.valueOf(responseCode);
	}
	
	@Override
	public String getMessage() {
		return String.valueOf(responseMsg + detailMsg);
	}
	
}