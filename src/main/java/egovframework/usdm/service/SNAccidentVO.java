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

public class SNAccidentVO {

	private String 	ftrCde;
	private String 	ftrIdn;
	private String 	longitude;
	private String 	latitude;
	private String 	altitude;
	private String 	address1;
	private String 	address2;
	private String 	address3;
	private String 	address4;
	private double	accidentTime;
	private String	accidentType;
	private String	accidentShape;
	private String	accidentDesc;
	private String 	X;
	private String 	Y;
	private String	geoType;
	private String	geoTable;
	private String	spatialCondition;
	
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
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getAddress3() {
		return address3;
	}
	public void setAddress3(String address3) {
		this.address3 = address3;
	}
	public String getAddress4() {
		return address4;
	}
	public void setAddress4(String address4) {
		this.address4 = address4;
	}
	public double getAccidentTime() {
		return accidentTime;
	}
	public void setAccidentTime(double accidentTime) {
		this.accidentTime = accidentTime;
	}
	public String getAccidentType() {
		return accidentType;
	}
	public void setAccidentType(String accidentType) {
		this.accidentType = accidentType;
	}
	public String getAccidentShape() {
		return accidentShape;
	}
	public void setAccidentShape(String accidentShape) {
		this.accidentShape = accidentShape;
	}
	public String getAccidentDesc() {
		return accidentDesc;
	}
	public void setAccidentDesc(String accidentDesc) {
		this.accidentDesc = accidentDesc;
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
	public String getGeoType() {
		return geoType;
	}
	public void setGeoType(String geoType) {
		this.geoType = geoType;
	}
	public String getGeoTable() {
		return geoTable;
	}
	public void setGeoTable(String geoTable) {
		this.geoTable = geoTable;
	}
	public String getSpatialCondition() {
		return spatialCondition;
	}
	public void setSpatialCondition(String spatialCondition) {
		this.spatialCondition = spatialCondition;
	}
	
}