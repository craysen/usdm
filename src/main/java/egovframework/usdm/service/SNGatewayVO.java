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

public class SNGatewayVO extends UsdmDefaultVO {

	private String 	GID;
	private String 	gwID;
	private String 	url;
	private String 	manufacturer;
	private String 	productNo;
	private String 	location;
	private String 	longitude;
	private String 	latitude;
	private String 	altitude;
	private long 	dateTime;
	private String 	supportedTransportProtocolList;
	private String 	supportedTransportConnectionControlList;
	private String 	supportedTransportDirectionList;
	private String 	supportedOperationList;
	private String 	supportedAttributeList;
	private String 	supportedSensorServiceCenterList;
	private String 	panIDList;
	private String 	monitoringMode;
	private int		monitoringPeriod;
	private String 	statusCode;
	private int		deleteCode;
	private String	nickName;
	private String 	newGwID;
	private String 	X;
	private String 	Y;

	public String getGID() {
		return GID;
	}
	public void setGID(String gID) {
		GID = gID;
	}
	public String getGwID() {
		return gwID;
	}
	public void setGwID(String gwID) {
		this.gwID = gwID;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getAltitude() {
		return altitude;
	}
	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}
	public long getDateTime() {
		return dateTime;
	}
	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}
	public String getSupportedTransportProtocolList() {
		return supportedTransportProtocolList;
	}
	public void setSupportedTransportProtocolList(
			String supportedTransportProtocolList) {
		this.supportedTransportProtocolList = supportedTransportProtocolList;
	}
	public String getSupportedTransportConnectionControlList() {
		return supportedTransportConnectionControlList;
	}
	public void setSupportedTransportConnectionControlList(
			String supportedTransportConnectionControlList) {
		this.supportedTransportConnectionControlList = supportedTransportConnectionControlList;
	}
	public String getSupportedTransportDirectionList() {
		return supportedTransportDirectionList;
	}
	public void setSupportedTransportDirectionList(
			String supportedTransportDirectionList) {
		this.supportedTransportDirectionList = supportedTransportDirectionList;
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
	public String getSupportedSensorServiceCenterList() {
		return supportedSensorServiceCenterList;
	}
	public void setSupportedSensorServiceCenterList(
			String supportedSensorServiceCenterList) {
		this.supportedSensorServiceCenterList = supportedSensorServiceCenterList;
	}
	public String getPanIDList() {
		return panIDList;
	}
	public void setPanIDList(String panIDList) {
		this.panIDList = panIDList;
	}
	public String getMonitoringMode() {
		return monitoringMode;
	}
	public void setMonitoringMode(String monitoringMode) {
		this.monitoringMode = monitoringMode;
	}
	public int getMonitoringPeriod() {
		return monitoringPeriod;
	}
	public void setMonitoringPeriod(int monitoringPeriod) {
		this.monitoringPeriod = monitoringPeriod;
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
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getNewGwID() {
		return newGwID;
	}
	public void setNewGwID(String newGwID) {
		this.newGwID = newGwID;
	}
	public String getX() {
		return X;
	}
	public void setX(String x) {
		X = x;
	}
	public String getY() {
		return Y;
	}
	public void setY(String y) {
		Y = y;
	}
	
}
