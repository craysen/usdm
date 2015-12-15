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
package egovframework.usdm.service;

public class SNPanVO extends UsdmDefaultVO {

	private String 	gwID;
	private String 	panID;
	private String 	topology;
	private String 	protocolStack;
	private int 	panChannel;
	private String 	supportedChannelList;
	private String 	supportedTopologyList;
	private String 	supportedProtocolStackList;
	private String 	supportedOperationList;
	private String 	supportedAttributeList;
	private String 	snIDList;
	private String	statusCode;
	private int		deleteCode;
	private String 	newGwID;
	private String 	newPanID;
	
	public String getGwID() {
		return gwID;
	}
	public void setGwID(String gwID) {
		this.gwID = gwID;
	}
	public String getPanID() {
		return panID;
	}
	public void setPanID(String panID) {
		this.panID = panID;
	}
	public String getTopology() {
		return topology;
	}
	public void setTopology(String topology) {
		this.topology = topology;
	}
	public String getProtocolStack() {
		return protocolStack;
	}
	public void setProtocolStack(String protocolStack) {
		this.protocolStack = protocolStack;
	}
	public int getPanChannel() {
		return panChannel;
	}
	public void setPanChannel(int panChannel) {
		this.panChannel = panChannel;
	}
	public String getSupportedChannelList() {
		return supportedChannelList;
	}
	public void setSupportedChannelList(String supportedChannelList) {
		this.supportedChannelList = supportedChannelList;
	}
	public String getSupportedTopologyList() {
		return supportedTopologyList;
	}
	public void setSupportedTopologyList(String supportedTopologyList) {
		this.supportedTopologyList = supportedTopologyList;
	}
	public String getSupportedProtocolStackList() {
		return supportedProtocolStackList;
	}
	public void setSupportedProtocolStackList(String supportedProtocolStackList) {
		this.supportedProtocolStackList = supportedProtocolStackList;
	}
	public String getSupportedOperationList() {
		return supportedOperationList;
	}
	public void setSupportedOperationList(String supportedOperationList) {
		this.supportedOperationList = supportedOperationList;
	}
	public String getSupportedAttributeList() {
		return supportedAttributeList;
	}
	public void setSupportedAttributeList(String supportedAttributeList) {
		this.supportedAttributeList = supportedAttributeList;
	}
	public String getSnIDList() {
		return snIDList;
	}
	public void setSnIDList(String snIDList) {
		this.snIDList = snIDList;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public int getDeleteCode() {
		return deleteCode;
	}
	public void setDeleteCode(int deleteCode) {
		this.deleteCode = deleteCode;
	}
	public String getNewGwID() {
		return newGwID;
	}
	public void setNewGwID(String newGwID) {
		this.newGwID = newGwID;
	}
	public String getNewPanID() {
		return newPanID;
	}
	public void setNewPanID(String newPanID) {
		this.newPanID = newPanID;
	}
	
}