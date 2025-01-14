/*
 * Copyright 1999-2023 Percussion Software, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.percussion.pso.restservice.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;


/**
 */
@XmlRootElement(name="Value")
public class StringValue implements Value {

	/**
	 * Field stringValue.
	 */
	private String stringValue;
	
	
	/**
	 * Field type.
	 */
	public static final int TYPE=0;  
	
	/**
	 * Method getStringValue.
	 * @return String
	 * @see Value#getStringValue()
	 */
	@XmlValue
	public String getStringValue() {
		return stringValue;
	}
	/**
	 * Method setStringValue.
	 * @param stringValue String
	 * @see Value#setStringValue(String)
	 */
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

}
