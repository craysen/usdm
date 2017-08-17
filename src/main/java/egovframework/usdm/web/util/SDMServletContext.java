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

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import egovframework.usdm.service.MessageQueueVO;
import egovframework.usdm.service.impl.APIInterfaceDAO;

@WebListener
public class SDMServletContext implements ServletContextListener{
 
	@Override
    public void contextInitialized(ServletContextEvent sce) {
    	try {
    		// ==================
			//   MQ 메세지 전송 
			// ==================
			MessageQueueVO messageVO = new MessageQueueVO();
			messageVO.setEventName(UsdmUtils.MQ_REBOOT);
			messageVO.setResourceID("1");
			messageVO.setValue("1");
			
			UsdmUtils.sendMessageMQ(messageVO);
			
			// Event 기록 저장
			/*
			APIInterfaceDAO apiInterface = new APIInterfaceDAO();
			apiInterface.insertEvent(messageVO);
			*/
    	
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
 
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    	/*
    	try {
    		// ==================
			//   MQ 메세지 전송 
			// ==================
			MessageQueueVO messageVO = new MessageQueueVO();
			messageVO.setEventName(UsdmUtils.MQ_SHUTDOWN);
			messageVO.setResourceID("1");
			messageVO.setValue("1");
			
			UsdmUtils.sendMessageMQ(messageVO);
    	
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	*/
    }
 
}