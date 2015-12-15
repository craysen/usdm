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

public class SNSensingValueVO extends UsdmDefaultVO {

	private String 	gwID;
	private String 	panID;
	private String 	snID;
	private String 	tdID;
	private long 	reportTime;
	private long  	sensingTime;
	private String  sensorType;
	private double	sensingValue;
	private String  additionalCondition;
	private String  temporalCondition;
	private String  spatialCondition;
	
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
	public String getTdID() {
		return tdID;
	}
	public void setTdID(String tdID) {
		this.tdID = tdID;
	}
	public long getReportTime() {
		return reportTime;
	}
	public void setReportTime(long reportTime) {
		this.reportTime = reportTime;
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
	public String getSpatialCondition() {
		return spatialCondition;
	}
	public void setSpatialCondition(String spatialCondition) {
		this.spatialCondition = spatialCondition;
	}
		
}