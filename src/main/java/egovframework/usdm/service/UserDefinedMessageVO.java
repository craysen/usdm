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

public class UserDefinedMessageVO {

	private String	targetID;
	private long	transmissionTime;
	private int		commandID;
	private String	transmissionDirection;
	private String	transmissionResult;
	private int		messageDataSize;
	private String	messageBinaryData;
	private String	temporalCondition;
	
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
	public int getCommandID() {
		return commandID;
	}
	public void setCommandID(int commandID) {
		this.commandID = commandID;
	}
	public String getTransmissionDirection() {
		return transmissionDirection;
	}
	public void setTransmissionDirection(String transmissionDirection) {
		this.transmissionDirection = transmissionDirection;
	}
	public String getTransmissionResult() {
		return transmissionResult;
	}
	public void setTransmissionResult(String transmissionResult) {
		this.transmissionResult = transmissionResult;
	}
	public int getMessageDataSize() {
		return messageDataSize;
	}
	public void setMessageDataSize(int messageDataSize) {
		this.messageDataSize = messageDataSize;
	}
	public String getMessageBinaryData() {
		return messageBinaryData;
	}
	public void setMessageBinaryData(String messageBinaryData) {
		this.messageBinaryData = messageBinaryData;
	}
	public String getTemporalCondition() {
		return temporalCondition;
	}
	public void setTemporalCondition(String temporalCondition) {
		this.temporalCondition = temporalCondition;
	}
	
}