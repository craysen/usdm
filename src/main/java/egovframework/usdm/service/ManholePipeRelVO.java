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

public class ManholePipeRelVO {

	private String	manholeFtrCde;
	private int		manholeFtrIdn;
	private String	pipeFtrCde;
	private int		pipeFtrIdn;
	
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
	public String getPipeFtrCde() {
		return pipeFtrCde;
	}
	public void setPipeFtrCde(String pipeFtrCde) {
		this.pipeFtrCde = pipeFtrCde;
	}
	public int getPipeFtrIdn() {
		return pipeFtrIdn;
	}
	public void setPipeFtrIdn(int pipeFtrIdn) {
		this.pipeFtrIdn = pipeFtrIdn;
	}
}