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
package com.percussion.server;

import com.percussion.data.PSConditionalEvaluator;
import com.percussion.data.PSExecutionData;
import com.percussion.design.objectstore.IPSObjectStoreErrors;
import com.percussion.design.objectstore.PSDataSet;
import com.percussion.design.objectstore.PSRequestor;
import com.percussion.design.objectstore.PSResultPage;
import com.percussion.design.objectstore.PSResultPageSet;
import com.percussion.error.PSIllegalArgumentException;
import com.percussion.util.PSCollection;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


/**
 * The PSRequestPageMap class is used to map a request page to its
 * associated data set. The same request page name can be used to map to
 * many different data sets. The selection parameters must be checked for
 * the data set to determine which data set is a match. A hash table is
 * built in the PSApplicationHandler object which contains the name of
 * the request page as the key and an object of this class as the value.
 * The PSRequestPageMap object is then invoked to determine which
 * data set handler should be used.
 *
 * @author     Tas Giakouminakis
 * @version    1.0
 * @since      1.0
 */
public class PSRequestPageMap
{
   /**
    * Construct a request page mapping for the specified requestor object.
    *
    * @param      requestor      the requestor object definined the map info
    *
    * @param      rh               the request handler to use
    *
    * @exception   PSIllegalArgumentException
    *                              if <code>requestor</code> is
    *                              <code>null</code>
    */
   public PSRequestPageMap(PSDataSet dataSet,
                           IPSRequestHandler rh)
      throws PSIllegalArgumentException
   {
      super();

      PSRequestor requestor = dataSet.getRequestor();

      if (requestor == null)
         throw new PSIllegalArgumentException(
            IPSObjectStoreErrors.DATASET_REQUESTOR_NULL);

      m_reqPage      = requestor.getRequestPage();
      m_reqHandler   = rh;
      
      PSCollection conditionals = requestor.getSelectionCriteria();
      if ((conditionals == null) || (conditionals.size() == 0))
         m_SelectionCriteriaChecker = null;
      else
         m_SelectionCriteriaChecker =
            new PSConditionalEvaluator(conditionals);

      /* Need to get the accepted extensions for the specified requestor */
      m_extensionsSupported = new HashSet();
      
      HashMap mimeMap = requestor.getMimeProperties();

      if (mimeMap != null)
      {
         Iterator i = mimeMap.keySet().iterator();
         while (i.hasNext())
            m_extensionsSupported.add(i.next());
      }

      PSResultPageSet pageSet = dataSet.getOutputResultPages();
      if (pageSet != null)
      {
         PSCollection pageCollection = pageSet.getResultPages();
         if ((pageCollection != null) && (pageCollection.size() > 0))
         {
            for (int i = 0; i < pageCollection.size(); i++)
            {
               PSResultPage page = (PSResultPage) pageCollection.get(i);
               Collection pageExtensions = page.getExtensions();
                              
               if (pageExtensions != null)
               {
                  Iterator pageIt = pageExtensions.iterator();
                  while (pageIt.hasNext())
                  {
                     Object o = pageIt.next();
                     if (!m_extensionsSupported.contains(o))
                        m_extensionsSupported.add(o);
                  }
               }
            }
         }
      }
   }

   private Collection                  m_extensionsSupported;

   /**
    * Get the request page associated with this map.
    *
    * @return               the name of the request page
    */
   public java.lang.String getRequestPage()
   {
      return m_reqPage;
   }

   /**
    * Get the request handler associated with this map.
    *
    * @return               the request handler
    */
   public IPSRequestHandler getRequestHandler()
   {
      return m_reqHandler;
   }

   /**
    * Is this request page a match based upon the request page name and
    * the selection parameters defined in the request?
    *
    * @param   ah            the application handler performing the test
    *
    * @param   request      the request to check
    *
    * @return               <code>true</code> if it is
    */
   public boolean isMatch(PSApplicationHandler ah, PSRequest request)
   {
      if (   !m_reqPage.equalsIgnoreCase(request.getRequestPage()) &&
            !m_reqPage.equalsIgnoreCase(request.getRequestPage(false)) )
         return false;

      if (m_extensionsSupported != null)
      {
         String reqUrl = request.getRequestFileURL().toLowerCase();
         /* Check the request's extension */
         int slashIndex = reqUrl.lastIndexOf('/');
         if (slashIndex > -1)
         {
            String resourcePortion = reqUrl.substring(slashIndex + 1);
            int dotIndex = resourcePortion.lastIndexOf('.');
            if (dotIndex > -1)
            {
               String extension = resourcePortion.substring(dotIndex + 1);
               if (!m_extensionsSupported.contains(extension))
               {
                  if (!extension.equals("txt") && !extension.equals("xml"))
                     return false;
               }
            }
         }
      }

      if (m_SelectionCriteriaChecker == null)
         return true;

      return m_SelectionCriteriaChecker.isMatch(
         new PSExecutionData(ah, null, request));
   }


   private IPSRequestHandler            m_reqHandler;
   private java.lang.String            m_reqPage;
   private PSConditionalEvaluator      m_SelectionCriteriaChecker;
}

