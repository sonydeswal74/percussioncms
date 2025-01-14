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
package com.percussion.design.objectstore.legacy;

import com.percussion.cms.objectstore.PSCmsObject;
import com.percussion.design.objectstore.IPSWorkflowInfoValueAccessor;
import com.percussion.design.objectstore.PSComponent;
import com.percussion.design.objectstore.PSContentEditor;
import com.percussion.design.objectstore.PSWorkflowInfo;
import com.percussion.security.IPSTypedPrincipal;
import com.percussion.security.IPSTypedPrincipal.PrincipalTypes;
import com.percussion.server.PSServer;
import com.percussion.services.catalog.data.PSObjectSummary;
import com.percussion.services.security.IPSAcl;
import com.percussion.services.security.IPSAclService;
import com.percussion.services.security.PSAclServiceLocator;
import com.percussion.services.security.PSSecurityException;
import com.percussion.services.security.PSTypedPrincipal;
import com.percussion.services.workflow.IPSWorkflowService;
import com.percussion.services.workflow.PSWorkflowServiceLocator;
import com.percussion.utils.guid.IPSGuid;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Updates the content editors workflow info object. For CM1, all workflows
 * should be automatically allowed by all content types. This updater sets an
 * {@link IPSWorkflowInfoValueAccessor} on the {@link PSWorkflowInfo} that will
 * dynamically return all current workflows as allowed.
 * 
 * @author JaySeletz
 */
public class PSAllowAllCtypeWorkflowsUpdater implements IPSComponentUpdater
{
   /**
    * Empty constructor.
    */
   public PSAllowAllCtypeWorkflowsUpdater() 
   {

   }
   
   /**
    * Sets an {@link IPSWorkflowInfoValueAccessor} on the {@link PSWorkflowInfo} of
    * the supplied {@link PSContentEditor}.
    */
   public void updateComponent(PSComponent comp)
   {
      PSContentEditor editor = (PSContentEditor) (comp);

      // do nothing if the editor is not workflowable
      PSCmsObject cmsObject = PSServer.getCmsObjectRequired(editor
            .getObjectType());
      if (!cmsObject.isWorkflowable())
         return;

      PSWorkflowInfo wfInfo = editor.getWorkflowInfo();
      if (wfInfo != null)
      {
         updateInfo(wfInfo);
         @SuppressWarnings("unchecked")
         List<Integer> allowedIds = IteratorUtils.toList(wfInfo.getValues());
         if (!allowedIds.isEmpty() && !IteratorUtils.toList(wfInfo.getValues()).contains(editor.getWorkflowId()))
         {
            editor.setWorkflowId(allowedIds.get(0));
         }
      }
   }

   /**
    * Updates the supplied {@link PSWorkflowInfo} object to allow
    * all workflows in the system.  Exposed with package default
    * access to allow for unit testing.
    * 
    * @param wfInfo The info to update, may not be <code>null</code>.
    */
   void updateInfo(PSWorkflowInfo wfInfo)
   {
      Validate.notNull(wfInfo);
      
      wfInfo.setValueAccessor(new IPSWorkflowInfoValueAccessor()
      {
         
         public boolean isExclusionary()
         {
            return false;
         }
         
         public List<Integer> getValues()
         {
            // load all workflow summaries
            List<Integer> wfIds = new ArrayList<>();
            List<IPSGuid> wfGuids = new ArrayList<>();
            IPSWorkflowService wfSvc = PSWorkflowServiceLocator.getWorkflowService();
            List<PSObjectSummary> sums = wfSvc.findWorkflowSummariesByName(null);
            for (PSObjectSummary sum : sums)
            {
               wfIds.add(sum.getGUID().getUUID());
               wfGuids.add(sum.getGUID());
            }
            
            // ensure at least a default acl exists for each workflow
            IPSAclService aclSvc = PSAclServiceLocator.getAclService();
            List<IPSAcl> acls =  aclSvc.loadAclsForObjects(wfGuids);
            for (int i = 0; i < acls.size(); i++)
            {
               IPSAcl acl = acls.get(i);
               
               if (acl == null)
               {
                  IPSTypedPrincipal owner = new PSTypedPrincipal(
                        PSTypedPrincipal.DEFAULT_USER_ENTRY, PrincipalTypes.USER);
                  IPSGuid wfGuid = wfGuids.get(i);
                  IPSAcl newAcl = aclSvc.createAcl(wfGuid, owner);
                  try
                  {
                     aclSvc.saveAcls(Collections.singletonList(newAcl));
                  }
                  catch (PSSecurityException e)
                  {
                     Logger logger = LogManager.getLogger(this.getClass());
                     logger.error("Unable to save default acl for workflow {}" , wfGuid.getUUID());
                  }                  
               }
            }
            
            // PSContentTypeWorkflowsUpdater sorts the results, so we'll do that here too
            Collections.sort(wfIds);
            
            return wfIds;
         }
      });
   }

   /**
    * @return <code>true</code> if the supplied type is {@link PSContentEditor}. 
    */
   public boolean canUpdateComponent(Class type)
   {
      return type.getName().equals(PSContentEditor.class.getName());
   }

}
