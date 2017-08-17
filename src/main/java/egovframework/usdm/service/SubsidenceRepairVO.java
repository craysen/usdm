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

import java.util.List;

public class SubsidenceRepairVO {

	private int		repairID;
	private long	repairDate;
	private int		target;
	private String 	contents;
	private String 	place;
	private double	longitude;
	private double	latitude;
	private String 	wkt;
	
	private List<String> sewerPipeList;
	private List<String> subwayList;
	private List<String> stationList;
	private double	sewerDistance;
	private double	subwayDistance;
	private double	stationDistance;
	private String	sewerPipeIDList;
	private String	subwayIDList;
	private String	stationIDList;
	
	private String 	cellIDList;
	private String 	accidentIDList;
	private String 	temporalCondition;
	private String 	spatialCondition;
	
	public int getRepairID() {
		return repairID;
	}
	public void setRepairID(int repairID) {
		this.repairID = repairID;
	}
	public long getRepairDate() {
		return repairDate;
	}
	public void setRepairDate(long repairDate) {
		this.repairDate = repairDate;
	}
	public int getTarget() {
		return target;
	}
	public void setTarget(int target) {
		this.target = target;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
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
	public String getCellIDList() {
		return cellIDList;
	}
	public void setCellIDList(String cellIDList) {
		this.cellIDList = cellIDList;
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
	public List<String> getSewerPipeList() {
		return sewerPipeList;
	}
	public void setSewerPipeList(List<String> sewerPipeList) {
		this.sewerPipeList = sewerPipeList;
	}
	public List<String> getSubwayList() {
		return subwayList;
	}
	public void setSubwayList(List<String> subwayList) {
		this.subwayList = subwayList;
	}
	public List<String> getStationList() {
		return stationList;
	}
	public void setStationList(List<String> stationList) {
		this.stationList = stationList;
	}
	public double getSewerDistance() {
		return sewerDistance;
	}
	public void setSewerDistance(double sewerDistance) {
		this.sewerDistance = sewerDistance;
	}
	public double getSubwayDistance() {
		return subwayDistance;
	}
	public void setSubwayDistance(double subwayDistance) {
		this.subwayDistance = subwayDistance;
	}
	public double getStationDistance() {
		return stationDistance;
	}
	public void setStationDistance(double stationDistance) {
		this.stationDistance = stationDistance;
	}
	public String getSewerPipeIDList() {
		return sewerPipeIDList;
	}
	public void setSewerPipeIDList(String sewerPipeIDList) {
		this.sewerPipeIDList = sewerPipeIDList;
	}
	public String getSubwayIDList() {
		return subwayIDList;
	}
	public void setSubwayIDList(String subwayIDList) {
		this.subwayIDList = subwayIDList;
	}
	public String getStationIDList() {
		return stationIDList;
	}
	public void setStationIDList(String stationIDList) {
		this.stationIDList = stationIDList;
	}
	public String getSpatialCondition() {
		return spatialCondition;
	}
	public void setSpatialCondition(String spatialCondition) {
		this.spatialCondition = spatialCondition;
	}

}