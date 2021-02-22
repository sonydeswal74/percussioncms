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

package com.percussion.pagemanagement.service;

import com.percussion.pagemanagement.data.PSPage;

/**
 * The REST layer for page service.
 *   
 * @author YuBingChen
 */
public interface IPSPageRestService
{
   
   /**
    * Creates a page.
    *
    * @param page the new page info, not <code>null</code>.
    * 
    * @return the created page ID, not blank.
    */
   String create(PSPage page);
   
   /**
    * Loads the specified page.
    * 
    * @param id the ID of the page, not blank.
    * 
    * @return the loaded page, not <code>null</code>.
    */
   PSPage load(String id);
   
   void delete(String id);
}