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

public class RailroadVideoVO extends UsdmDefaultVO {

	private int				videoId;
	private String			gwID;
	private String			panID;
	private String			snID;
	private String			tdID;
	private String			videoType;
	private String			videoTypeName;
	private MultipartFile 	file;
	private String 			filePath;
	private String 			fileName;
	private String 			fileRealName;
	private int 			resolutionW;
	private int 			resolutionH;
	private int 			numPixel;
	private double 			startPosX;
	private double 			startPosY;
	private double 			startPosZ;
	private double 			endPosX;
	private double 			endPosY;
	private double 			endPosZ;
	private String			recStartTime;
	private String			recEndTime;
	private String			workSectionStart;
	private String			workSectionEnd;
	private int				measureType;
	private String			measureTypeName;
	private String			damage;
	private String			POI;
	
	public int getVideoId() {
		return videoId;
	}
	public void setVideoId(int videoId) {
		this.videoId = videoId;
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
	public String getVideoType() {
		return videoType;
	}
	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}
	public String getVideoTypeName() {
		return videoTypeName;
	}
	public void setVideoTypeName(String videoTypeName) {
		this.videoTypeName = videoTypeName;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileRealName() {
		return fileRealName;
	}
	public void setFileRealName(String fileRealName) {
		this.fileRealName = fileRealName;
	}
	public int getResolutionW() {
		return resolutionW;
	}
	public void setResolutionW(int resolutionW) {
		this.resolutionW = resolutionW;
	}
	public int getResolutionH() {
		return resolutionH;
	}
	public void setResolutionH(int resolutionH) {
		this.resolutionH = resolutionH;
	}
	public int getNumPixel() {
		return numPixel;
	}
	public void setNumPixel(int numPixel) {
		this.numPixel = numPixel;
	}
	public double getStartPosX() {
		return startPosX;
	}
	public void setStartPosX(double startPosX) {
		this.startPosX = startPosX;
	}
	public double getStartPosY() {
		return startPosY;
	}
	public void setStartPosY(double startPosY) {
		this.startPosY = startPosY;
	}
	public double getStartPosZ() {
		return startPosZ;
	}
	public void setStartPosZ(double startPosZ) {
		this.startPosZ = startPosZ;
	}
	public double getEndPosX() {
		return endPosX;
	}
	public void setEndPosX(double endPosX) {
		this.endPosX = endPosX;
	}
	public double getEndPosY() {
		return endPosY;
	}
	public void setEndPosY(double endPosY) {
		this.endPosY = endPosY;
	}
	public double getEndPosZ() {
		return endPosZ;
	}
	public void setEndPosZ(double endPosZ) {
		this.endPosZ = endPosZ;
	}
	public String getRecStartTime() {
		return recStartTime;
	}
	public void setRecStartTime(String recStartTime) {
		this.recStartTime = recStartTime;
	}
	public String getRecEndTime() {
		return recEndTime;
	}
	public void setRecEndTime(String recEndTime) {
		this.recEndTime = recEndTime;
	}
	public String getWorkSectionStart() {
		return workSectionStart;
	}
	public void setWorkSectionStart(String workSectionStart) {
		this.workSectionStart = workSectionStart;
	}
	public String getWorkSectionEnd() {
		return workSectionEnd;
	}
	public void setWorkSectionEnd(String workSectionEnd) {
		this.workSectionEnd = workSectionEnd;
	}
	public int getMeasureType() {
		return measureType;
	}
	public void setMeasureType(int measureType) {
		this.measureType = measureType;
	}
	public String getMeasureTypeName() {
		return measureTypeName;
	}
	public void setMeasureTypeName(String measureTypeName) {
		this.measureTypeName = measureTypeName;
	}
	public String getDamage() {
		return damage;
	}
	public void setDamage(String damage) {
		this.damage = damage;
	}
	public String getPOI() {
		return POI;
	}
	public void setPOI(String pOI) {
		POI = pOI;
	}
		
}
