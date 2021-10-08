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
package com.percussion.search.lucene.textconverter;

import com.percussion.extension.IPSExtensionDef;
import com.percussion.extension.PSExtensionException;
import com.percussion.extension.PSExtensionProcessingException;
import com.percussion.search.lucene.IPSLuceneConstants;
import org.apache.poi.extractor.POITextExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.extractor.ExcelExtractor;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Extracts the text from input stream corresponding to Microsoft Excel file
 * using POI api. Gets the plain text from the cells that represent the String
 * from all the sheets. Appends the cell text with a blank space.
 * 
 */
public class PSTextConverterMsExcel implements IPSLuceneTextConverter
{

   /*
    * (non-Javadoc)
    * 
    * @see com.percussion.search.lucene.textconverter.IPSLuceneTextConverter#getConvertedText(java.io.InputStream,
    * java.lang.String)
    */
   public String getConvertedText(InputStream is, String mimetype)
      throws PSExtensionProcessingException
   {
      if (is == null)
         throw new IllegalArgumentException("is must not be null");

      String resultText = "";
      try
      {
         POITextExtractor ext = getTextExtractor(mimetype, is);
         resultText = ext.getText();
      }
      catch (IOException e)
      {
         throw new PSExtensionProcessingException(m_className, e);
      }
      return resultText;
   }


   private POITextExtractor getTextExtractor(String mimetype, InputStream is) throws IOException
   {
      ExcelExtractor extractor;
      if (IPSLuceneConstants.MIME_TYPE_APPLICATION_BY_EXCEL.equalsIgnoreCase(mimetype) || 
            IPSLuceneConstants.MIME_TYPE_APPLICATION_BY_VNDMSEXCEL.equalsIgnoreCase(mimetype))
      {
         extractor = new org.apache.poi.hssf.extractor.ExcelExtractor(new HSSFWorkbook(is));
      }
      else
      {
         extractor = new XSSFExcelExtractor(new XSSFWorkbook(is));
      }
      
      extractor.setIncludeSheetNames(false);
      extractor.setIncludeCellComments(false);
      
      return (POITextExtractor) extractor;
   }



   /*
    * (non-Javadoc)
    * 
    * @see com.percussion.extension.IPSExtension#init(com.percussion.extension.IPSExtensionDef,
    * java.io.File)
    */
   public void init(IPSExtensionDef def, File codeRoot)
      throws PSExtensionException
   {

   }

   /**
    * A memeber variable to hold the name of this class.
    */
   private String m_className = getClass().getName();

}