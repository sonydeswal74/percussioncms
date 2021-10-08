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
 *      https://www.percussion.com
 *
 *     You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <https://www.gnu.org/licenses/>
 */

package com.percussion.data;

import com.percussion.design.objectstore.IPSReplacementValue;


/**
 * The IPSDataExtractor interface must be implemented by any classes
 * capable of pulling data from the PSExecutionData object. This is then
 * used as input into a UDF call, a SQL statement parameter or XML field
 * value.
 * 
 * @author     Tas Giakouminakis
 * @version    1.0
 * @since      1.0
 */
public interface IPSDataExtractor
{
   /**
    * Extract a data value using the run-time data.
    *
    * @param   execData    the execution data associated with this request.
    *                      This includes all context data, result sets, etc.
    *
    * @return               the associated value; <code>null</code> if a
    *                        value is not found
    *
    * @exception   PSDataExtractionException
    *                        if an error condition causes the extraction to
    *                        fail. This is not thrown if the requested data
    *                        does not exist.
    */
   public Object extract(PSExecutionData data)
      throws PSDataExtractionException;

   /**
    * Extract a data value using the run-time data.
    *
    * @param   execData    the execution data associated with this request.
    *                      This includes all context data, result sets, etc.
    *
    * @param   defValue      the default value to use if a value is not found
    *
    * @return               the associated value; <code>defValue</code> if a
    *                        value is not found
    *
    * @exception   PSDataExtractionException
    *                        if an error condition causes the extraction to
    *                        fail. This is not thrown if the requested data
    *                        does not exist.
    */
   public Object extract(PSExecutionData data, Object defValue)
      throws PSDataExtractionException;

   /**
    * Get the source IPSReplacementValue object(s) used to create this
    * extractor.
    *
    * @return               the source object(s) (may be <code>null</code>)
    */
   public IPSReplacementValue[] getSource();
}
