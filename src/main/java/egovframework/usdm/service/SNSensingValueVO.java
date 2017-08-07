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

	private String 	snGID;
	private String 	gwID;
	private String 	panID;
	private String 	snID;
	private String 	tdID;
	private long 	reportTime;
	private long  	sensingTime;
	private String  sensorType;
	private double	sensingValue;
	private String	binaryValue;
	private String  additionalCondition;
	private String  temporalCondition;
	private String  spatialCondition;
	private String 	GID;
	private String 	geoObjectType;
	private String 	geoTable;

	private String 	operator1;
	private double	operand1;
	private String 	logicalOp;
	private String 	operator2;
	private double	operand2;
	private int		counter;
	
	private String	geoIDList;
	
	public String getSnGID() {
		return snGID;
	}
	public void setSnGID(String snGID) {
		this.snGID = snGID;
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
	public String getBinaryValue() {
		return binaryValue;
	}
	public void setBinaryValue(String binaryValue) {
		this.binaryValue = binaryValue;
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
	public String getGID() {
		return GID;
	}
	public void setGID(String gID) {
		GID = gID;
	}
	public String getGeoObjectType() {
		return geoObjectType;
	}
	public void setGeoObjectType(String geoObjectType) {
		this.geoObjectType = geoObjectType;
	}
	public String getGeoTable() {
		return geoTable;
	}
	public void setGeoTable(String geoTable) {
		this.geoTable = geoTable;
	}
	public String getOperator1() {
		return operator1;
	}
	public void setOperator1(String operator1) {
		this.operator1 = operator1;
	}
	public double getOperand1() {
		return operand1;
	}
	public void setOperand1(double operand1) {
		this.operand1 = operand1;
	}
	public String getLogicalOp() {
		return logicalOp;
	}
	public void setLogicalOp(String logicalOp) {
		this.logicalOp = logicalOp;
	}
	public String getOperator2() {
		return operator2;
	}
	public void setOperator2(String operator2) {
		this.operator2 = operator2;
	}
	public double getOperand2() {
		return operand2;
	}
	public void setOperand2(double operand2) {
		this.operand2 = operand2;
	}
	public String getGeoIDList() {
		return geoIDList;
	}
	public void setGeoIDList(String geoIDList) {
		this.geoIDList = geoIDList;
	}
	public int getCounter() {
		return counter;
	}
	public void setCounter(int counter) {
		this.counter = counter;
	}
	
}