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

public class SNGeoRelationVO {

	private String 	GID;
	private long	startTime;
	private long	endTime;
	private String 	ftrCde;
	private String 	ftrIdn;
	private String	gwID;
	private String	panID;
	private String	snID;
	private String	reason;
	private double	currentTime;
	private String	geoTable;
	private double	X;
	private double	Y;
	private double	longitude;
	private double	latitude;
	private long  	sensingTime;
	private String  sensorType;
	private double	sensingValue;
	private String  temporalCondition;
	private String  additionalCondition;
	
	public String getGID() {
		return GID;
	}
	public void setGID(String gID) {
		GID = gID;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public String getFtrCde() {
		return ftrCde;
	}
	public void setFtrCde(String ftrCde) {
		this.ftrCde = ftrCde;
	}
	public String getFtrIdn() {
		return ftrIdn;
	}
	public void setFtrIdn(String ftrIdn) {
		this.ftrIdn = ftrIdn;
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
	public double getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(double currentTime) {
		this.currentTime = currentTime;
	}
	public String getGeoTable() {
		return geoTable;
	}
	public void setGeoTable(String geoTable) {
		this.geoTable = geoTable;
	}
	public double getX() {
		return X;
	}
	public void setX(double x) {
		X = x;
	}
	public double getY() {
		return Y;
	}
	public void setY(double y) {
		Y = y;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public long getSensingTime() {
		return sensingTime;
	}
	public void setSensingTime(long sensingTime) {
		this.sensingTime = sensingTime;
	}
	public String getSensorType() {
		return sensorType;
	}
	public void setSensorType(String sensorType) {
		this.sensorType = sensorType;
	}
	public double getSensingValue() {
		return sensingValue;
	}
	public void setSensingValue(double sensingValue) {
		this.sensingValue = sensingValue;
	}
	public String getAdditionalCondition() {
		return additionalCondition;
	}
	public void setAdditionalCondition(String additionalCondition) {
		this.additionalCondition = additionalCondition;
	}
	public String getTemporalCondition() {
		return temporalCondition;
	}
	public void setTemporalCondition(String temporalCondition) {
		this.temporalCondition = temporalCondition;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
}