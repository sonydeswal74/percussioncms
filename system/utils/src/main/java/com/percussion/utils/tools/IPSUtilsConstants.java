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
package com.percussion.utils.tools;

/**
 * General constants referenced by Utils classes
 */
public interface IPSUtilsConstants
{
   /**
    * The standard name of the preferred encoding for Rhythmyx. This encoding is
    * guaranteed to be acceptable for XML parsers and HTTP servers, and should
    * be some kind of Unicode so that we can be sure all characters are
    * representable. This is the standard name for {@link #RX_JAVA_ENC}.
    */
   public static final String RX_STANDARD_ENC = "UTF-8";


   /**
    * The standard name of the preferred encoding for Rhythmyx. This encoding is
    * guaranteed to be acceptable for Sun's Java methods which take a character
    * encoding, and should be some kind of Unicode so that we can be sure all
    * characters are representable.This is the Java name for 
    * {@link #RX_STANDARD_ENC}.
    */
   public static final String RX_JAVA_ENC = "UTF-8";
}

