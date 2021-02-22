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
package com.percussion.share.service;

import com.percussion.share.dao.IPSGenericDao;
import com.percussion.share.data.IPSItemSummary;
import com.percussion.share.data.PSItemSummaryUtils;
import com.percussion.share.service.exception.PSDataServiceException;

public abstract class PSAbstractFullDataService<FULL,SUM extends IPSItemSummary> 
    extends PSAbstractDataService<FULL, SUM, String> implements IPSDataService<FULL, SUM, String>
{

    protected IPSDataItemSummaryService itemSummaryService;
    
    public PSAbstractFullDataService(IPSDataItemSummaryService itemSummaryService, IPSGenericDao<FULL, String> dao)
    {
        super(dao);
        this.itemSummaryService = itemSummaryService;
    }

    public SUM find(String id) throws PSDataServiceException {

        validateIdParameter("find", id);
        IPSItemSummary itemSummary = itemSummaryService.find(id);
        SUM sum = createSummary(id);
        PSItemSummaryUtils.copyProperties(itemSummary, sum);
        return sum;

    }
    
    protected abstract SUM createSummary(String id);

}