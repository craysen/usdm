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

public class DrainPipeVideoVO extends UsdmDefaultVO {

	private int				videoId;
	private String			gwID;
	private String			panID;
	private String			snID;
	private String			tdID;
	private MultipartFile 	file;
	private String 			filePath;
	private String 			fileName;
	private String 			fileRealName;
	private int 			resolutionW;
	private int 			resolutionH;
	private int 			numPixel;
	private double 			positionX;
	private double 			positionY;
	private double 			positionZ;
	private String			manholeFtrCde;
	private int				manholeFtrIdn;
	private String			pipeFtrIdn;
	private double 			directionAngle;
	private String			addr1;
	private String			addr2;
	private String			addr3;
	private String			recStartTime;
	private String			recEndTime;
	private String			damage;
	private String			POI;
	private double			distance;
	
	public int getvideoId() {
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
	public double getPositionX() {
		return positionX;
	}
	public void setPositionX(double positionX) {
		this.positionX = positionX;
	}
	public double getPositionY() {
		return positionY;
	}
	public void setPositionY(double positionY) {
		this.positionY = positionY;
	}
	public double getPositionZ() {
		return positionZ;
	}
	public void setPositionZ(double positionZ) {
		this.positionZ = positionZ;
	}
	public String getManholeFtrCde() {
		return manholeFtrCde;
	}
	public void setManholeFtrCde(String manholeFtrCde) {
		this.manholeFtrCde = manholeFtrCde;
	}
	public int getManholeFtrIdn() {
		return manholeFtrIdn;
	}
	public void setManholeFtrIdn(int manholeFtrIdn) {
		this.manholeFtrIdn = manholeFtrIdn;
	}
	public double getDirectionAngle() {
		return directionAngle;
	}
	public void setDirectionAngle(double directionAngle) {
		this.directionAngle = directionAngle;
	}
	public String getAddr1() {
		return addr1;
	}
	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}
	public String getAddr2() {
		return addr2;
	}
	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}
	public String getAddr3() {
		return addr3;
	}
	public void setAddr3(String addr3) {
		this.addr3 = addr3;
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
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public String getPipeFtrIdn() {
		return pipeFtrIdn;
	}
	public void setPipeFtrIdn(String pipeFtrIdn) {
		this.pipeFtrIdn = pipeFtrIdn;
	}
	public int getVideoId() {
		return videoId;
	}
	
}