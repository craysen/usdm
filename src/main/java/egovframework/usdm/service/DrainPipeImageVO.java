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

public class DrainPipeImageVO extends UsdmDefaultVO {

	private int				imageId;
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
	private int				pipeId;
	private int				manholeId;
	private double 			directionAngle;
	private double 			offset;
	private String			addr1;
	private String			addr2;
	private String			addr3;
	private String			recDate;
	private String			recTime;
	private int				videoId;
	private String			damage;
	private String			POI;
	
	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
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
	public int getPipeId() {
		return pipeId;
	}
	public void setPipeId(int pipeId) {
		this.pipeId = pipeId;
	}
	public int getManholeId() {
		return manholeId;
	}
	public void setManholeId(int manholeId) {
		this.manholeId = manholeId;
	}
	public double getDirectionAngle() {
		return directionAngle;
	}
	public void setDirectionAngle(double directionAngle) {
		this.directionAngle = directionAngle;
	}
	public double getOffset() {
		return offset;
	}
	public void setOffset(double offset) {
		this.offset = offset;
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
	public String getRecDate() {
		return recDate;
	}
	public void setRecDate(String recDate) {
		this.recDate = recDate;
	}
	public String getRecTime() {
		return recTime;
	}
	public void setRecTime(String recTime) {
		this.recTime = recTime;
	}
	public int getVideoId() {
		return videoId;
	}
	public void setVideoId(int videoId) {
		this.videoId = videoId;
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