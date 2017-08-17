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

public class InfraRepairVO {

	private int		repairID;
	private String	geoType;
	private String 	ftrCde;
	private int		ftrIdn;
	private long	repairDate;
	private int		category;
	private String 	contents;
	private double	longitude;
	private double	latitude;
	private String 	wkt;
	
	private String 	geoTable;
	private String 	sriTable;
	private String 	cellIDList;
	private String 	accidentIDList;
	private String 	temporalCondition;

	public int getRepairID() {
		return repairID;
	}
	public void setRepairID(int repairID) {
		this.repairID = repairID;
	}
	public String getGeoType() {
		return geoType;
	}
	public void setGeoType(String geoType) {
		this.geoType = geoType;
	}
	public String getFtrCde() {
		return ftrCde;
	}
	public void setFtrCde(String ftrCde) {
		this.ftrCde = ftrCde;
	}
	public int getFtrIdn() {
		return ftrIdn;
	}
	public void setFtrIdn(int ftrIdn) {
		this.ftrIdn = ftrIdn;
	}
	public long getRepairDate() {
		return repairDate;
	}
	public void setRepairDate(long repairDate) {
		this.repairDate = repairDate;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
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
	public String getWkt() {
		return wkt;
	}
	public void setWkt(String wkt) {
		this.wkt = wkt;
	}
	public String getGeoTable() {
		return geoTable;
	}
	public void setGeoTable(String geoTable) {
		this.geoTable = geoTable;
	}
	public String getAccidentIDList() {
		return accidentIDList;
	}
	public void setAccidentIDList(String accidentIDList) {
		this.accidentIDList = accidentIDList;
	}
	public String getTemporalCondition() {
		return temporalCondition;
	}
	public void setTemporalCondition(String temporalCondition) {
		this.temporalCondition = temporalCondition;
	}
	public String getSriTable() {
		return sriTable;
	}
	public void setSriTable(String sriTable) {
		this.sriTable = sriTable;
	}
	public String getCellIDList() {
		return cellIDList;
	}
	public void setCellIDList(String cellIDList) {
		this.cellIDList = cellIDList;
	}
	
}