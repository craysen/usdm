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

import org.springframework.web.multipart.MultipartFile;

public class UnderWaterVO extends UsdmDefaultVO {

	private String 	gwID;
	private String 	panID;
	private String 	snID;
	private String 	tdID;
	private MultipartFile file;
	private String 	measureDate;
	private String 	measureTime;
	private double 	waterLevel;
	private double 	waterTemp;
	private double 	waterConduct;
	private double 	waterTurb;
	private double 	upperSoilMoist;
	private double 	upperSoilConduct;
	private double 	upperSoilTemp;
	private double 	lowerSoilMoist;
	private double 	lowerSoilConduct;
	private double 	lowerSoilTemp;

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
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String getMeasureDate() {
		return measureDate;
	}
	public void setMeasureDate(String measureDate) {
		this.measureDate = measureDate;
	}
	public String getMeasureTime() {
		return measureTime;
	}
	public void setMeasureTime(String measureTime) {
		this.measureTime = measureTime;
	}
	public double getWaterLevel() {
		return waterLevel;
	}
	public void setWaterLevel(double waterLevel) {
		this.waterLevel = waterLevel;
	}
	public double getWaterTemp() {
		return waterTemp;
	}
	public void setWaterTemp(double waterTemp) {
		this.waterTemp = waterTemp;
	}
	public double getWaterConduct() {
		return waterConduct;
	}
	public void setWaterConduct(double waterConduct) {
		this.waterConduct = waterConduct;
	}
	public double getWaterTurb() {
		return waterTurb;
	}
	public void setWaterTurb(double waterTurb) {
		this.waterTurb = waterTurb;
	}
	public double getUpperSoilMoist() {
		return upperSoilMoist;
	}
	public void setUpperSoilMoist(double upperSoilMoist) {
		this.upperSoilMoist = upperSoilMoist;
	}
	public double getUpperSoilConduct() {
		return upperSoilConduct;
	}
	public void setUpperSoilConduct(double upperSoilConduct) {
		this.upperSoilConduct = upperSoilConduct;
	}
	public double getUpperSoilTemp() {
		return upperSoilTemp;
	}
	public void setUpperSoilTemp(double upperSoilTemp) {
		this.upperSoilTemp = upperSoilTemp;
	}
	public double getLowerSoilMoist() {
		return lowerSoilMoist;
	}
	public void setLowerSoilMoist(double lowerSoilMoist) {
		this.lowerSoilMoist = lowerSoilMoist;
	}
	public double getLowerSoilConduct() {
		return lowerSoilConduct;
	}
	public void setLowerSoilConduct(double lowerSoilConduct) {
		this.lowerSoilConduct = lowerSoilConduct;
	}
	public double getLowerSoilTemp() {
		return lowerSoilTemp;
	}
	public void setLowerSoilTemp(double lowerSoilTemp) {
		this.lowerSoilTemp = lowerSoilTemp;
	}

}
