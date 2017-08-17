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

public class SubsidenceRepairRelVO {

	private int		repairID;
	private String 	geoType;
	private int		ftrIdn;
	private	double	distance;
	
	private String 	geoTable;
	private String 	updateColumns;
	
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
	public int getFtrIdn() {
		return ftrIdn;
	}
	public void setFtrIdn(int ftrIdn) {
		this.ftrIdn = ftrIdn;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public String getGeoTable() {
		return geoTable;
	}
	public void setGeoTable(String geoTable) {
		this.geoTable = geoTable;
	}
	public String getUpdateColumns() {
		return updateColumns;
	}
	public void setUpdateColumns(String updateColumns) {
		this.updateColumns = updateColumns;
	}
	
}