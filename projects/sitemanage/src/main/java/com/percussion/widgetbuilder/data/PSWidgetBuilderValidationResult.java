/*
 *     Percussion CMS
 *     Copyright (C) 1999-2020 Percussion Software, Inc.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     Mailing Address:
 *
 *      Percussion Software, Inc.
 *      PO Box 767
 *      Burlington, MA 01803, USA
 *      +01-781-438-9900
 *      support@percussion.com
 *      https://www.percusssion.com
 *
 *     You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <https://www.gnu.org/licenses/>
 */
package com.percussion.widgetbuilder.data;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.Validate;

/**
 * @author JaySeletz
 *
 */
@XmlRootElement(name="WidgetBuilderValidationResult")
public class PSWidgetBuilderValidationResult
{
    String category;
    String name;
    String message;
    
    public PSWidgetBuilderValidationResult()
    {
        
    }
    
    /**
     * @param category
     * @param name
     * @param message
     */
    public PSWidgetBuilderValidationResult(String category, String name, String message)
    {
        Validate.notEmpty(category);
        Validate.notEmpty(name);
        Validate.notEmpty(message);
        
        this.category = category;
        this.name = name;
        this.message = message;
    }

    public String getCategory()
    {
        return category;
    }

    public String getName()
    {
        return name;
    }

    public String getMessage()
    {
        return message;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public enum ValidationCategory
    {
        GENERAL,
        CONTENT,
        RESOURCES,
        DISPLAY
    }
}