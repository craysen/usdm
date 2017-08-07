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

public class DrainPipeAccidentVO {

	private String 	pipeFtrCde;
	private String 	pipeFtrIdn;
	private String 	longitude;
	private String 	latitude;
	private String 	altitude;
	private double 	x;
	private double 	y;
	private long	accidentTime;
	private String	accidentTimeStr;
	private String	accidentType;
	private String	accidentShape;
	private String	accidentDesc;

	private String 	diameter;
	private String 	majorAxis;
	private String 	minorAxis;
	private String 	width;
	private String 	height;
	private String 	depth;
	private String 	length;
	private String	degree;
	private String 	direction;
	private String 	place;
	
	public String getPipeFtrCde() {
		return pipeFtrCde;
	}
	public void setPipeFtrCde(String pipeFtrCde) {
		this.pipeFtrCde = pipeFtrCde;
	}
	public String getPipeFtrIdn() {
		return pipeFtrIdn;
	}
	public void setPipeFtrIdn(String pipeFtrIdn) {
		this.pipeFtrIdn = pipeFtrIdn;
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
	public long getAccidentTime() {
		return accidentTime;
	}
	public void setAccidentTime(long accidentTime) {
		this.accidentTime = accidentTime;
	}
	public String getAccidentTimeStr() {
		return accidentTimeStr;
	}
	public void setAccidentTimeStr(String accidentTimeStr) {
		this.accidentTimeStr = accidentTimeStr;
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
	public String getDiameter() {
		return diameter;
	}
	public void setDiameter(String diameter) {
		this.diameter = diameter;
	}
	public String getMajorAxis() {
		return majorAxis;
	}
	public void setMajorAxis(String majorAxis) {
		this.majorAxis = majorAxis;
	}
	public String getMinorAxis() {
		return minorAxis;
	}
	public void setMinorAxis(String minorAxis) {
		this.minorAxis = minorAxis;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getDepth() {
		return depth;
	}
	public void setDepth(String depth) {
		this.depth = depth;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	
}