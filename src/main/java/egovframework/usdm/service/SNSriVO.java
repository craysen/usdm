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

public class SNSriVO extends UsdmDefaultVO {

	private String geoID;
	
	// 상,하수도
	private String roadType;
	private String complaintCount;
	private String recordPeriod;
	private String leakSignal;
	private String pipeMove;
	private String internalState;
	private String emptySpace;
	private String minX;
	private String minY;
	private String maxX;
	private String maxY;
	private String assessValue;
	
	// 지하철선로,역사
	private String structureGrade;
	private String groundCount50;
	private String groundCount100;
	private String groundCount200;
	private String roadCount50;
	private String roadCount100;
	private String roadCount200;
	private String evaDistance;
	private String evaDepth;
	private String collectingWell;
	
	private String sriValue;
	private String sriGrade;
	
	public String getGeoID() {
		return geoID;
	}
	public void setGeoID(String geoID) {
		this.geoID = geoID;
	}
	public String getRoadType() {
		return roadType;
	}
	public void setRoadType(String roadType) {
		this.roadType = roadType;
	}
	public String getComplaintCount() {
		return complaintCount;
	}
	public void setComplaintCount(String complaintCount) {
		this.complaintCount = complaintCount;
	}
	public String getRecordPeriod() {
		return recordPeriod;
	}
	public void setRecordPeriod(String recordPeriod) {
		this.recordPeriod = recordPeriod;
	}
	public String getLeakSignal() {
		return leakSignal;
	}
	public void setLeakSignal(String leakSignal) {
		this.leakSignal = leakSignal;
	}
	public String getPipeMove() {
		return pipeMove;
	}
	public void setPipeMove(String pipeMove) {
		this.pipeMove = pipeMove;
	}
	public String getInternalState() {
		return internalState;
	}
	public void setInternalState(String internalState) {
		this.internalState = internalState;
	}
	public String getEmptySpace() {
		return emptySpace;
	}
	public void setEmptySpace(String emptySpace) {
		this.emptySpace = emptySpace;
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
	public String getAssessValue() {
		return assessValue;
	}
	public void setAssessValue(String assessValue) {
		this.assessValue = assessValue;
	}
	public String getStructureGrade() {
		return structureGrade;
	}
	public void setStructureGrade(String structureGrade) {
		this.structureGrade = structureGrade;
	}
	public String getEvaDistance() {
		return evaDistance;
	}
	public void setEvaDistance(String evaDistance) {
		this.evaDistance = evaDistance;
	}
	public String getEvaDepth() {
		return evaDepth;
	}
	public void setEvaDepth(String evaDepth) {
		this.evaDepth = evaDepth;
	}
	public String getCollectingWell() {
		return collectingWell;
	}
	public void setCollectingWell(String collectingWell) {
		this.collectingWell = collectingWell;
	}
	public String getSriValue() {
		return sriValue;
	}
	public void setSriValue(String sriValue) {
		this.sriValue = sriValue;
	}
	public String getSriGrade() {
		return sriGrade;
	}
	public void setSriGrade(String sriGrade) {
		this.sriGrade = sriGrade;
	}
	public String getGroundCount50() {
		return groundCount50;
	}
	public void setGroundCount50(String groundCount50) {
		this.groundCount50 = groundCount50;
	}
	public String getGroundCount100() {
		return groundCount100;
	}
	public void setGroundCount100(String groundCount100) {
		this.groundCount100 = groundCount100;
	}
	public String getGroundCount200() {
		return groundCount200;
	}
	public void setGroundCount200(String groundCount200) {
		this.groundCount200 = groundCount200;
	}
	public String getRoadCount50() {
		return roadCount50;
	}
	public void setRoadCount50(String roadCount50) {
		this.roadCount50 = roadCount50;
	}
	public String getRoadCount100() {
		return roadCount100;
	}
	public void setRoadCount100(String roadCount100) {
		this.roadCount100 = roadCount100;
	}
	public String getRoadCount200() {
		return roadCount200;
	}
	public void setRoadCount200(String roadCount200) {
		this.roadCount200 = roadCount200;
	}
	
}