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
package com.percussion.dashboardmanagement.data;

import com.percussion.share.data.PSAbstractPersistantObject;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;

@XmlRootElement(name = "Dashboard")
public class PSDashboard extends PSAbstractPersistantObject {

    @NotNull
    @NotBlank
    private List<PSGadget> gadgets;
    private PSDashboardConfiguration config;
    
    @NotNull
    @NotBlank
    private String id;
    
    public List<PSGadget> getGadgets() {
        return gadgets;
    }

    public void setGadgets(List<PSGadget> gadgets) {
        this.gadgets = gadgets;
    }

    public PSDashboardConfiguration getDashboardConfiguration() {
    	return this.config;
    }
    
    public void setDashboardConfiguration(PSDashboardConfiguration config) {
    	this.config = config;
    }
    
	@Override
    public String getId() {
		return id;
	}

	@Override
    public void setId(String id) {
		this.id = id;
	}
    
    private static final long serialVersionUID = -6627409151209959037L;


    
}
