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

public class SNNodeVO extends UsdmDefaultVO {

	private String 	gwGID;
	private String 	panGID;
	private String 	GID;
	private String 	gwID;
	private String 	panID;
	private String 	snID;
	private String	ftr_cde;
	private String	ftr_idn;
	private double	deployTime;
	private String 	globalID;
	private String 	manufacturer;
	private String 	productNo;
	private String 	location;
	private String 	longitude;
	private String 	latitude;
	private String 	altitude;
	private String	role;
	private String	roleList;
	private String	parentNodeIDList;
	private String	supportedOperationList;
	private String	supportedAttributeList;
	private String	tdIDList;
	private String	monitoringMode;
	private int		monitoringPeriod;
	private String	statusCode;
	private int 	deleteCode;
	private String	nickName;
	private String 	newGwID;
	private String 	newPanID;
	private String 	newSnID;
	private String 	X;
	private String 	Y;
	private String 	geoTable;
	private long	currentTime;
	private long	endTime;
	
	public String getGwGID() {
		return gwGID;
	}
	public void setGwGID(String gwGID) {
		this.gwGID = gwGID;
	}
	public String getPanGID() {
		return panGID;
	}
	public void setPanGID(String panGID) {
		this.panGID = panGID;
	}
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
	public String getPanID() {
		return panID;
	}
	public void setPanID(String panID) {
		this.panID = panID;
	}
	public String getSnID() {
		return snID;
	}
	public void setSnID(String snID) {
		this.snID = snID;
	}
	public String getFtr_cde() {
		return ftr_cde;
	}
	public void setFtr_cde(String ftr_cde) {
		this.ftr_cde = ftr_cde;
	}
	public String getFtr_idn() {
		return ftr_idn;
	}
	public void setFtr_idn(String ftr_idn) {
		this.ftr_idn = ftr_idn;
	}
	public double getDeployTime() {
		return deployTime;
	}
	public void setDeployTime(double deployTime) {
		this.deployTime = deployTime;
	}
	public String getGlobalID() {
		return globalID;
	}
	public void setGlobalID(String globalID) {
		this.globalID = globalID;
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getRoleList() {
		return roleList;
	}
	public void setRoleList(String roleList) {
		this.roleList = roleList;
	}
	public String getParentNodeIDList() {
		return parentNodeIDList;
	}
	public void setParentNodeIDList(String parentNodeIDList) {
		this.parentNodeIDList = parentNodeIDList;
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
	public String getTdIDList() {
		return tdIDList;
	}
	public void setTdIDList(String tdIDList) {
		this.tdIDList = tdIDList;
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
	public String getNewPanID() {
		return newPanID;
	}
	public void setNewPanID(String newPanID) {
		this.newPanID = newPanID;
	}
	public String getNewSnID() {
		return newSnID;
	}
	public void setNewSnID(String newSnID) {
		this.newSnID = newSnID;
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
	public String getGeoTable() {
		return geoTable;
	}
	public void setGeoTable(String geoTable) {
		this.geoTable = geoTable;
	}
	public long getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
}