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
package com.percussion.content.ui.aa.actions.impl;

import com.percussion.cms.objectstore.PSItemDefSummary;
import com.percussion.cms.objectstore.server.PSItemDefManager;
import com.percussion.content.ui.aa.PSAAObjectId;
import com.percussion.content.ui.aa.actions.PSAAClientActionException;
import com.percussion.content.ui.aa.actions.PSActionResponse;
import com.percussion.services.assembly.PSAssemblyException;
import com.percussion.services.contentmgr.IPSNodeDefinition;
import com.percussion.services.error.PSNotFoundException;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Retrieves a listing of all allowed content types for a particular slot.
 * Expects an objectId for the slot. Returns a Json Array with a Json object for
 * each template entry.
 * 
 * <pre>
 *      Each Json object that represents a content type has the following
 *      parameters:
 *      
 *      contenttypeid
 *      name
 *      description
 * </pre>
 * 
 */
public class PSGetAllowedContentTypeForSlotAction extends PSAAActionBase
{

   /*
    * (non-Javadoc)
    * 
    * @see com.percussion.content.ui.aa.actions.IPSAAClientAction#execute(java.util.Map)
    */
   @SuppressWarnings("unchecked")
   public PSActionResponse execute(Map<String, Object> params)
         throws PSAAClientActionException
   {
      String objectIdStr = (String) getParameter(params, OBJECT_ID_PARAM);
      String results = null;
      try
      {
         JSONArray array = new JSONArray();
         if (StringUtils.isBlank(objectIdStr))
         {
            PSItemDefManager defMgr = PSItemDefManager.getInstance();
            Collection summ = defMgr.getSummaries(getRequestContext()
                  .getSecurityToken());
            List<PSItemDefSummary> summLst = new ArrayList<PSItemDefSummary>();
            for (Object sum : summ)
            {
               summLst.add((PSItemDefSummary)sum);
            }            
            Collections.sort(summLst, new Comparator()
            {
               public int compare(Object obj1, Object obj2)
               {
                  PSItemDefSummary temp1 = (PSItemDefSummary) obj1;
                  PSItemDefSummary temp2 = (PSItemDefSummary) obj2;
         
                  return temp1.getLabel().compareTo(temp2.getLabel());
               }
            });

            for (Object sum : summLst)
            {
               PSItemDefSummary def = (PSItemDefSummary) sum;
               array.put(getCtypeObject(
                     String.valueOf(def.getGUID().getUUID()), String
                           .valueOf(def.getLabel()), String.valueOf(def
                           .getDescription())));
            }
         }
         else
         {
            PSAAObjectId objectId = new PSAAObjectId(objectIdStr);
            Collection<IPSNodeDefinition> types = getAssociatedCTypes(objectId);
            for (IPSNodeDefinition def : types)
            {
               array.put(getCtypeObject(
                     String.valueOf(def.getGUID().getUUID()), String
                           .valueOf(def.getLabel()), String.valueOf(def
                           .getDescription())));
            }
         }
         results = array.toString();
      }
      catch (Exception e)
      {
         throw new PSAAClientActionException(e);
      }
      return new PSActionResponse(results, PSActionResponse.RESPONSE_TYPE_JSON);
   }

   /**
    * Retrieves the associated content types for the slot indicated in the
    * object id passed in.
    * 
    * @param objectId cannot be <code>null</code> or empty. Must have the slot
    *           id defined.
    * @return collection of node def object, never <code>null</code>, may be
    *         empty.
    * @throws PSAssemblyException if an error occurs when retriving the slot
    *            from the assembly service.
    */
   protected static Collection<IPSNodeDefinition> getAssociatedCTypes(
         PSAAObjectId objectId) throws PSAssemblyException, PSNotFoundException {
      if (objectId == null)
         throw new IllegalArgumentException("objectId cannot be null.");
      if (StringUtils.isBlank(objectId.getSlotId()))
         throw new IllegalArgumentException(
               "The slotid in the objectId cannot be null or empty.");
      String slotid = objectId.getSlotId();
      List<IPSNodeDefinition> defs = PSActionUtil
            .getAllowedNodeDefsForSlot(slotid);
      return defs;

   }

   /**
    * Helper method to create JSONObject of the given content type id, name and description
    * @param id content type id assumed not <code>null</code>.
    * @param name content type name assumed not <code>null</code>.
    * @param desc content type description assumed not <code>null</code>.
    * @return JSONObject of the above values.
    * @throws JSONException
    */
   private JSONObject getCtypeObject(String id, String name, String desc)
         throws JSONException
   {
      JSONObject obj = new JSONObject();
      obj.append("contenttypeid", id);
      obj.append("name", name);
      obj.append("description", desc);
      return obj;
   }

}
