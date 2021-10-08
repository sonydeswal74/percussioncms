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
package com.percussion.xsl.encoding;

import java.io.IOException;

/**
 * Defines the EUC_TW character encoding for the Saxon XSLT processor.
 */
public class PSEUC_TWCharacterSet extends PSGenericCharacterSet
{
   /**
    * Initializes a newly created <code>PSEUC_TWCharacterSet</code> object by
    * delegating to {@link PSGenericCharacterSet#PSGenericCharacterSet(String,
    * String) <code>super("EUC_TW", "java-EUC_TW.xml")</code>}
    * 
    * @throws IOException if there are problems reading the resource file.
    */
   public PSEUC_TWCharacterSet() throws IOException
   {
      super("EUC_TW", "java-EUC_TW.xml");
   }
}