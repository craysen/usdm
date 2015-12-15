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

public class SNTransducerVO extends UsdmDefaultVO {

	private String 	gwID;
	private String 	panID;
	private String 	snID;
	private String 	tdID;
	private String 	manufacturer;
	private String 	productNo;
	private String 	transducerCategory;
	private String 	transducerType;
	private String 	unit;
	private String 	dataType;
	private double	rangeMin;
	private double	rangeMax;
	private double	rangeOffset;
	private String	level;
	private String	supportedOperationList;
	private String	supportedAttributeList;
	private String	statusCode;
	private String	actuationResult;
	private int 	deleteCode;
	private String 	newGwID;
	private String 	newPanID;
	private String 	newSnID;
	private String 	newTdID;
	
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
	public String getTransducerCategory() {
		return transducerCategory;
	}
	public void setTransducerCategory(String transducerCategory) {
		this.transducerCategory = transducerCategory;
	}
	public String getTransducerType() {
		return transducerType;
	}
	public void setTransducerType(String transducerType) {
		this.transducerType = transducerType;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public double getRangeMin() {
		return rangeMin;
	}
	public void setRangeMin(double rangeMin) {
		this.rangeMin = rangeMin;
	}
	public double getRangeMax() {
		return rangeMax;
	}
	public void setRangeMax(double rangeMax) {
		this.rangeMax = rangeMax;
	}
	public double getRangeOffset() {
		return rangeOffset;
	}
	public void setRangeOffset(double rangeOffset) {
		this.rangeOffset = rangeOffset;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
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
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getActuationResult() {
		return actuationResult;
	}
	public void setActuationResult(String actuationResult) {
		this.actuationResult = actuationResult;
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
	public String getNewSnID() {
		return newSnID;
	}
	public void setNewSnID(String newSnID) {
		this.newSnID = newSnID;
	}
	public String getNewTdID() {
		return newTdID;
	}
	public void setNewTdID(String newTdID) {
		this.newTdID = newTdID;
	}
	
}