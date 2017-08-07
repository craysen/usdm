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

public class SNFirmwareUpdateHistoryVO extends UsdmDefaultVO {

	private String	gwID;
	private String	targetID;
	private long	transmissionTime;
	private String	transmissionResult;
	private int		firmwareID;
	private int 	updateType;
	private long 	updateTime;
	private int		recommended1BlockSize;
	private String	gwIDCondition;
	private String	targetCondition;
	private String	temporalCondition;

	public String getGwID() {
		return gwID;
	}
	public void setGwID(String gwID) {
		this.gwID = gwID;
	}
	public String getTargetID() {
		return targetID;
	}
	public void setTargetID(String targetID) {
		this.targetID = targetID;
	}
	public long getTransmissionTime() {
		return transmissionTime;
	}
	public void setTransmissionTime(long transmissionTime) {
		this.transmissionTime = transmissionTime;
	}
	public String getTransmissionResult() {
		return transmissionResult;
	}
	public void setTransmissionResult(String transmissionResult) {
		this.transmissionResult = transmissionResult;
	}
	public int getFirmwareID() {
		return firmwareID;
	}
	public void setFirmwareID(int firmwareID) {
		this.firmwareID = firmwareID;
	}
	public int getUpdateType() {
		return updateType;
	}
	public void setUpdateType(int updateType) {
		this.updateType = updateType;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	public int getRecommended1BlockSize() {
		return recommended1BlockSize;
	}
	public void setRecommended1BlockSize(int recommended1BlockSize) {
		this.recommended1BlockSize = recommended1BlockSize;
	}
	public String getGwIDCondition() {
		return gwIDCondition;
	}
	public void setGwIDCondition(String gwIDCondition) {
		this.gwIDCondition = gwIDCondition;
	}
	public String getTargetCondition() {
		return targetCondition;
	}
	public void setTargetCondition(String targetCondition) {
		this.targetCondition = targetCondition;
	}
	public String getTemporalCondition() {
		return temporalCondition;
	}
	public void setTemporalCondition(String temporalCondition) {
		this.temporalCondition = temporalCondition;
	}

}