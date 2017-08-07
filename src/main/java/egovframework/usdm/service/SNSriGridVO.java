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

public class SNSriGridVO extends UsdmDefaultVO {

	private int 	cellID;
	private String	minX;
	private String	minY;
	private String	maxX;
	private String	maxY;
	private double 	sri;
	private String	grade;
	private double 	waterSri;
	private double 	drainSri;
	private double 	subwaySri;
	private double 	geologySri;
	private long 	lastUpdate;
	private double 	minValue;
	private String	cellIDList;
	
	public int getCellID() {
		return cellID;
	}
	public void setCellID(int cellID) {
		this.cellID = cellID;
	}
	public String getMinX() {
		return minX;
	}
	public void setMinX(String minX) {
		this.minX = minX;
	}
	public String getMinY() {
		return minY;
	}
	public void setMinY(String minY) {
		this.minY = minY;
	}
	public String getMaxX() {
		return maxX;
	}
	public void setMaxX(String maxX) {
		this.maxX = maxX;
	}
	public String getMaxY() {
		return maxY;
	}
	public void setMaxY(String maxY) {
		this.maxY = maxY;
	}
	public double getSri() {
		return sri;
	}
	public void setSri(double sri) {
		this.sri = sri;
	}
	public double getWaterSri() {
		return waterSri;
	}
	public void setWaterSri(double waterSri) {
		this.waterSri = waterSri;
	}
	public double getDrainSri() {
		return drainSri;
	}
	public void setDrainSri(double drainSri) {
		this.drainSri = drainSri;
	}
	public double getSubwaySri() {
		return subwaySri;
	}
	public void setSubwaySri(double subwaySri) {
		this.subwaySri = subwaySri;
	}
	public double getGeologySri() {
		return geologySri;
	}
	public void setGeologySri(double geologySri) {
		this.geologySri = geologySri;
	}
	public long getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public double getMinValue() {
		return minValue;
	}
	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}
	public String getCellIDList() {
		return cellIDList;
	}
	public void setCellIDList(String cellIDList) {
		this.cellIDList = cellIDList;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}

}