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
package com.percussion.deployer.server.dependencies;

import com.percussion.deployer.client.IPSDeployConstants;
import com.percussion.deployer.objectstore.PSApplicationIDTypeMapping;
import com.percussion.deployer.objectstore.PSApplicationIDTypes;
import com.percussion.deployer.objectstore.PSDependency;
import com.percussion.deployer.objectstore.PSDependencyFile;
import com.percussion.deployer.objectstore.PSDeployComponentUtils;
import com.percussion.deployer.objectstore.PSIdMap;
import com.percussion.deployer.server.IPSIdTypeHandler;
import com.percussion.deployer.server.PSAppTransformer;
import com.percussion.deployer.server.PSArchiveHandler;
import com.percussion.deployer.server.PSDependencyDef;
import com.percussion.deployer.server.PSDependencyMap;
import com.percussion.deployer.server.PSImportCtx;
import com.percussion.deployer.services.IPSDeployService;
import com.percussion.deployer.services.PSDeployServiceException;
import com.percussion.deployer.services.PSDeployServiceLocator;
import com.percussion.design.objectstore.PSParam;
import com.percussion.error.IPSDeploymentErrors;
import com.percussion.error.PSDeployException;
import com.percussion.extension.PSExtensionRef;
import com.percussion.security.PSSecurityToken;
import com.percussion.services.catalog.PSTypeEnum;
import com.percussion.services.error.PSNotFoundException;
import com.percussion.services.filter.IPSFilterService;
import com.percussion.services.filter.IPSItemFilter;
import com.percussion.services.filter.IPSItemFilterRuleDef;
import com.percussion.services.filter.PSFilterException;
import com.percussion.services.filter.PSFilterServiceLocator;
import com.percussion.services.filter.data.PSItemFilter;
import com.percussion.services.filter.data.PSItemFilterRuleDef;
import com.percussion.services.guidmgr.data.PSGuid;
import com.percussion.utils.guid.IPSGuid;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class to handle packaging and deploying a Filter definition.
 * @author vamsinukala
 */

public class PSFilterDefDependencyHandler extends PSDependencyHandler implements
        IPSIdTypeHandler
{
   /**
    * Construct the dependency handler.
    *
    * @param def The def for the type supported by this handler.  May not be
    * <code>null</code> and must be of the type supported by this class.  See
    * {@link #getType()} for more info.
    * @param dependencyMap The full dependency map.  May not be
    * <code>null</code>.
    *
    * @throws IllegalArgumentException if any param is invalid.
    */

   public PSFilterDefDependencyHandler(PSDependencyDef def,
         PSDependencyMap dependencyMap) 
   {
      super(def, dependencyMap);
   }

   /**
    * Helper method to 
    * 1. locate all the filters
    * 2. generate a map m_namedFiltersMap and m_guidFiltersMap
    */
   private void init()
   {
      if ( m_filterSvc == null )
         m_filterSvc = PSFilterServiceLocator.getFilterService();
      List<IPSItemFilter> filters = m_filterSvc.findAllFilters();
      Iterator<IPSItemFilter> it  = filters.iterator();
      m_namedFiltersMap.clear();
      m_guidFiltersMap.clear();
      
      while (it.hasNext())
      {
         IPSItemFilter filter = it.next();
         m_namedFiltersMap.put(filter.getName(), filter);
         m_guidFiltersMap.put(filter.getGUID(), filter);
      }
   }

   // see base class
   // id is a PSGuid for a Filter def
   @Override
   public PSDependency getDependency(PSSecurityToken tok, String id)
      throws PSDeployException
   {
      if (tok == null)
         throw new IllegalArgumentException("tok may not be null");

      if (id == null || id.trim().length() == 0)
         throw new IllegalArgumentException("id may not be null or empty");
      init();
      IPSItemFilter filter = findFilterByDependencyID(id);
      PSDependency dep = null;
      if ( filter != null)
      {
         dep = createDependency(m_def, ""
               + filter.getGUID().longValue(), filter.getName());
      }
      return dep;
   }

   //see base class
   @Override
   public Iterator getChildDependencies(PSSecurityToken tok, PSDependency dep)
           throws PSDeployException, PSNotFoundException {
      if (tok == null)
         throw new IllegalArgumentException("tok may not be null");

      if (dep == null)
         throw new IllegalArgumentException("dep may not be null");

      if (! dep.getObjectType().equals(DEPENDENCY_TYPE))
         throw new IllegalArgumentException("dep wrong type");
      init();
      
      PSDependencyHandler handler = null;
      IPSItemFilter filter = findFilterByDependencyID(dep.getDependencyId());
      Set<PSDependency> childDeps = new HashSet<>();
      
      // Add any parent filters..
      IPSItemFilter pfilter = filter.getParentFilter();
      if ( pfilter != null )
      {
         PSDependencyHandler fHandler = getDependencyHandler(
               PSFilterDefDependencyHandler.DEPENDENCY_TYPE);
         PSDependency pDep = fHandler.getDependency(tok,String.valueOf(pfilter
               .getGUID().longValue()));
         if ( pDep != null )
         {
            if (pDep.getDependencyType() == PSDependency.TYPE_SHARED)
            {
               pDep.setIsAssociation(false);             
            }
            childDeps.add(pDep);
         }
      }
      // Add Rule's Exit deps
      handler = getDependencyHandler(
            PSExitDefDependencyHandler.DEPENDENCY_TYPE);
      
      // Add any exits from Item Filters
      Set<IPSItemFilterRuleDef> rules =  filter.getRuleDefs();
      for (IPSItemFilterRuleDef rule : rules) {
         try {
            PSExtensionRef eRef = PSPublisherServiceHelper
                    .getItemFilterRuleExtensionRef(rule.getRuleName());
            if (eRef == null)
               continue;
            PSDependency ruleDep = handler.getDependency(tok, eRef.toString());
            if (ruleDep != null && !childDeps.contains(ruleDep)) {
               if (ruleDep.getDependencyType() == PSDependency.TYPE_SHARED) {
                  ruleDep.setIsAssociation(false);
               }
               childDeps.add(ruleDep);
            }
         } catch (PSFilterException e) {
            throw new PSDeployException(IPSDeploymentErrors.UNEXPECTED_ERROR,
                    "While creating the Filter dependency, " +
                            "a FilterException occurred: " + e.getLocalizedMessage());
         }
      }
      
      //Acl deps
      addAclDependency(tok, PSTypeEnum.ITEM_FILTER, dep, childDeps);
      
      //    Dont forget the stupid idTypes...
      childDeps.addAll(PSIdTypeDependencyHandler.getIdTypeDependencies(tok, 
            dep, this));

      return childDeps.iterator();
   }
   
   /**
    * From the contentList map, return all the contentList names
    * @return iterator on a set of names
    */
   public Iterator getFilterNames()
   {
      init();
      Set names = m_namedFiltersMap.keySet();
      return names.iterator();
   }

   
   // see base class
   // Load all the filters and return dependencies
   @Override
   public Iterator<PSDependency> getDependencies(PSSecurityToken tok)
   {
      init();   
      Iterator names   = getFilterNames(); 
      List<PSDependency> deps = new ArrayList<>();
      PSDependency dep;
      while (names.hasNext())
      {
         String name = (String) names.next();
         IPSItemFilter f = m_namedFiltersMap.get(name);
         dep = createDeployableElement(m_def, ""
               + f.getGUID().longValue(), name);
         deps.add(dep);
      }
      return deps.iterator();
   }

   
   // see base class
   @Override
   public Iterator getDependencyFiles(PSSecurityToken tok, PSDependency dep)
      throws PSDeployException
   {
      if (tok == null)
         throw new IllegalArgumentException("tok may not be null");
      if (dep == null)
         throw new IllegalArgumentException("dep may not be null");
      if (!dep.getObjectType().equals(DEPENDENCY_TYPE))
         throw new IllegalArgumentException("dep wrong type");
      init();

      // pack the data into the files
      List<PSDependencyFile> files = new ArrayList<>();

      IPSItemFilter f = m_namedFiltersMap.get(dep.getDisplayName());
      files.add(getDepFileFromFilter(f));
      return files.iterator();
   }

   /**
    * Creates a dependency file from a given dependency data object.
    * @param f the actual filter never <code>null</code>
    * @return The dependency file object, it will never be <code>null</code>.
    *
    * @throws IllegalArgumentException if any param is invalid.
    * @throws PSDeployException if any other error occurs.
    */
   protected PSDependencyFile getDepFileFromFilter(IPSItemFilter f)
      throws PSDeployException
   {
      if (f == null)
         throw new IllegalArgumentException("depData may not be null");
      String str;
      try
      {
         str = f.toXML();
      }
      catch (Exception e)
      {
         throw new PSDeployException(IPSDeploymentErrors.UNEXPECTED_ERROR,
               "Unable to generate a dependency file for filter:"
                     + f.getName());
      }
      
      return new PSDependencyFile(PSDependencyFile.TYPE_SERVICEGENERATED_XML,
            createXmlFile(XML_HDR_STR + str));
   }
   
   
   @Override
   public void installDependencyFiles(PSSecurityToken tok,
         PSArchiveHandler archive, PSDependency dep, PSImportCtx ctx)
   throws PSDeployException
   {
      Iterator files = getFilterDependecyFilesFromArchive(archive, dep);
      PSDependencyFile depFile = (PSDependencyFile) files.next();

      IPSDeployService depSvc = PSDeployServiceLocator.getDeployService();
      try
      {
         depSvc.deserializeAndSaveFilter(tok, archive, dep, depFile, ctx, this);
      }
      catch (PSDeployServiceException e)
      {
         throw new PSDeployException(IPSDeploymentErrors.UNEXPECTED_ERROR,
               "error occurred while installing site: "+e.getLocalizedMessage());
      }  

      // add transaction log
      addTransactionLogEntryByGuidType(dep, ctx, PSTypeEnum.ITEM_FILTER, true);

   }

   /**
    * TROLL thru the object and restore the versions of child-lings ;).
    * @param f the actual filter never <code>null</code> 
    * @param ver the version of filter
    * @throws PSDeployException
    */
  @Transactional(propagation = Propagation.REQUIRED)
   public void saveFilter(IPSItemFilter f, Integer ver)
         throws PSDeployException
   {
      // nullify and set it to the passed version of the Filter, can be null
      ((PSItemFilter) f).setVersion(null);
      ((PSItemFilter) f).setVersion(ver);
      try
      {
         m_filterSvc.saveFilter(f);
      }
      catch (Exception e1)
      {
         throw new PSDeployException(IPSDeploymentErrors.UNEXPECTED_ERROR,
               "Could not save or update the filter:" + f.getName() + "\n"
                     + e1.getLocalizedMessage());
      }
   }

   
   /**
    * See {@link PSDependencyHandler#shouldDeferInstallation()} for more info.
    * PSItemFilterRuleDef that has an extensionRef needs the extension
    * in the system else it CHOKES
    * @return <code>true</code>, since child items must be installed first.
    */
   @Override
   public boolean shouldDeferInstallation()
   {
      return true;
   }

   /**
    * Deserialize the filter and alter its properties so as to be unique.
    * @param tok 
    * @param archive the ArchiveHandler to use to retrieve the files from the 
    * archive, may not be <code>null</code> 
    * @param dep 
    * @param depFile the PSDependencyFile that was retrieved from the archive
    * may not be <code>null</code>
    * @param ctx 
    * @param filter if any, pre-loaded 
    * @return the deserialized filter
    * @throws PSDeployException
    */
   public IPSItemFilter generateFilterFromFile(PSSecurityToken tok,
         PSArchiveHandler archive, PSDependency dep, PSDependencyFile depFile,
         PSImportCtx ctx, IPSItemFilter filter)
         throws PSDeployException
   {
      
      String tmpStr = PSDependencyUtils.getFileContentAsString(
            archive, depFile);
      if ( filter == null )
         filter = new PSItemFilter();
      
      try
      {
         // clear the rule defs :-), later on after deserialization and
         // before save, make sure to wipe out the version info, since
         // new RULE Defs i.e. with new IDs are being generated
         filter.setRuleDefs(null);
         filter.fromXML(tmpStr);

         // nullify the versioning on ruledefs
         Set<IPSItemFilterRuleDef> rules = filter.getRuleDefs();
         for (IPSItemFilterRuleDef def : rules)
            ((PSItemFilterRuleDef)def).setVersion(null);
      }
      catch (Exception e)
      {
         throw new PSDeployException(IPSDeploymentErrors.UNEXPECTED_ERROR,
               "Could not deserialize the ItemFilter");
      }
      return filter;
   }
   

   
   /**
    * Return an iterator for dependency files in the archive
    * @param archive The archive handler to retrieve the dependency files from,
    *           may not be <code>null</code>.
    * @param dep The dependency object, may not be <code>null</code>.
    * 
    * @return An iterator one or more <code>PSDependencyFile</code> objects.
    *         It will never be <code>null</code> or empty.
    * 
    * @throws IllegalArgumentException if any param is invalid.
    * @throws PSDeployException if there is no dependency file in the archive
    *            for the specified dependency object, or any other error occurs.
    */
   protected Iterator getFilterDependecyFilesFromArchive(
         PSArchiveHandler archive, PSDependency dep) throws PSDeployException
   {
      if (archive == null)
         throw new IllegalArgumentException("archive may not be null");
      if (dep == null)
         throw new IllegalArgumentException("dep may not be null");

      Iterator files = archive.getFiles(dep);

      if (!files.hasNext())
      {
         Object[] args =
         {PSDependencyFile.TYPE_ENUM[PSDependencyFile.TYPE_SERVICEGENERATED_XML],
               dep.getObjectType(), dep.getDependencyId(), dep.getDisplayName()};
         throw new PSDeployException(
               IPSDeploymentErrors.MISSING_DEPENDENCY_FILE, args);
      }
      return files;
   }

   
   
   // see base class
   public Iterator getChildTypes()
   {
      return ms_childTypes.iterator();
   }


   // see base class
   @Override
   public String getType()
   {
      return DEPENDENCY_TYPE;
   }
   /**
    * Utility method to find the slot by a given guid(as a STRINGGGGGG)
    * @param depId the guid
    * @return <code>null</code> if slot is not found else get DA SLOT
    * @throws PSDeployException
    */
   public IPSItemFilter findFilterByDependencyID(String depId)
         throws PSDeployException
   {
      if (depId == null || depId.trim().length() == 0)
         throw new IllegalArgumentException(
               "dependency ID may not be null or empty");
   
      IPSItemFilter f = null;
      try
      {
         f = m_filterSvc.findFilterByID(new PSGuid(PSTypeEnum.ITEM_FILTER,
               PSDependencyUtils.getGuidValFromString(depId, m_def
                     .getObjectTypeName())));
      }
      catch (PSNotFoundException e)
      { }
      return f;
   }

   // see base class
   public PSApplicationIDTypes getIdTypes(PSSecurityToken tok, PSDependency dep)
         throws PSDeployException
   {
      if (tok == null)
         throw new IllegalArgumentException("tok may not be null");
      if (dep == null)
         throw new IllegalArgumentException("dep may not be null");
      if (!dep.getObjectType().equals(getType()))
         throw new IllegalArgumentException("dep wrong type");
      IPSItemFilter f = findFilterByDependencyID(dep.getDependencyId());
      PSApplicationIDTypes idTypes = new PSApplicationIDTypes(dep);
      try
      {
         if ( f != null )
         {
            // ADD ANY RULES's params that need mapping
            Set<IPSItemFilterRuleDef> ruleDefSet = f.getRuleDefs();
            for (IPSItemFilterRuleDef ruleDef : ruleDefSet) {
               Map<String, String> paramMap = ruleDef.getParams();

               List mappings = new ArrayList<>();
               // check each param for idtypes
               Iterator entries = paramMap.entrySet().iterator();
               while (entries.hasNext()) {
                  Map.Entry entry = (Map.Entry) entries.next();

                  // convert to PSParam to leverage existing transformer code
                  Iterator params = PSDeployComponentUtils.convertToParams(
                          entry).iterator();
                  while (params.hasNext()) {
                     PSParam param = (PSParam) params.next();
                     PSAppTransformer.checkParam(mappings, param, null);
                  }
               }
               idTypes.addMappings("" + RULEDEF_ARGS + ":" + ruleDef.getRuleName(),
                       IPSDeployConstants.ID_TYPE_ELEMENT_RULEDEF_PARAMS,
                       mappings.iterator());
            }
         }
      }
      catch(PSFilterException e)
      {
        throw new PSDeployException(IPSDeploymentErrors.UNEXPECTED_ERROR,
              "while looking for ID types, ruleDef did not provide a proper name");
      }
      return idTypes;
   }


   private void addTransformedParams(IPSItemFilterRuleDef ruleDef,
         Map<String, String> params)
   {
      Set<String> keys = params.keySet();
      Iterator<String> it = keys.iterator();
      while (it.hasNext())
      {
         String key = it.next();
         ruleDef.setParam(key, params.get(key));
      }
   }
   
   /**
    * Transform any IdTypes and ids in Bindings .
    * @param filter the actual filter, never <code>null</code>
    * @param dep the dependency
    * @param ctx import context never <code>null</code>
    * @param isNew if <code>true</code>, then don't transform the idType 
    * @return the filter with tranforms performed
    * @throws PSDeployException
    */
   public IPSItemFilter doTransforms(IPSItemFilter filter,
         PSDependency dep, PSImportCtx ctx, boolean isNew)
         throws PSDeployException
   {    
      if ( filter == null )
         throw new IllegalArgumentException(
               "Filter cannot be null for idtype mapping");     
      return transformIdsInParams(filter, ctx);
   }

   
   
   /**
    * Recurse thru all the rules and their params and see if they are idmapped, 
    * if so, transform them
    * @param filter the actual filter, never <code>null</code>
    * @param ctx import context never <code>null</code>
    * @return the filter with transforms performed
    * @throws PSDeployException
    */
   private IPSItemFilter transformIdsInParams(IPSItemFilter filter,
         PSImportCtx ctx) throws PSDeployException
   {
      PSIdMap idMap = ctx.getCurrentIdMap();
      
      //no need to xform
      if ( idMap == null )
         return filter;
      
      Set<IPSItemFilterRuleDef> rules =  filter.getRuleDefs();
      Iterator<IPSItemFilterRuleDef> it = rules.iterator();
      // troll thru all the RuleDefs
      while(it.hasNext())
      {
         IPSItemFilterRuleDef rule = it.next();
         Map<String, String> params = rule.getParams();
         Map<String, String> xformedParams = PSDependencyUtils.cloneMap(params);

         if(isIdTypeMappingEnabled()) {
            transformIds(xformedParams, ctx.getIdTypes(), idMap);
         }
         //rule.getParams().clear();
         addTransformedParams(rule, xformedParams);
      }
      return filter;
   }
   // see base class
   public void transformIds(Object object, PSApplicationIDTypes idTypes,
         PSIdMap idMap) throws PSDeployException
   {
      if (object == null)
         throw new IllegalArgumentException("object may not be null");

      if (idTypes == null)
         throw new IllegalArgumentException("idTypes may not be null");

      if (idMap == null)
         throw new IllegalArgumentException("idMap may not be null");
      if (!(object instanceof Map))
      {
         throw new IllegalArgumentException("invalid object type");
      }

      Map paramMap = (Map)object;
      // walk id types and perform any transforms
      Iterator resources = idTypes.getResourceList(false);
      while (resources.hasNext())
      {
         String resource = (String)resources.next();
         Iterator elements = idTypes.getElementList(resource, false);
         while (elements.hasNext())
         {
            String element = (String)elements.next();
            Iterator mappings = idTypes.getIdTypeMappings(
                  resource, element, false);
            while (mappings.hasNext())
            {
               PSApplicationIDTypeMapping mapping =
                  (PSApplicationIDTypeMapping)mappings.next();

               if (mapping.getType().equals(
                  PSApplicationIDTypeMapping.TYPE_NONE))
                  continue;

               if (element
                     .equals(IPSDeployConstants.ID_TYPE_ELEMENT_RULEDEF_PARAMS))
               {
                  // transform the params
                  Iterator entries = paramMap.entrySet().iterator();
                  while (entries.hasNext())
                  {
                     // convert to PSParam(s) to leverage existing code
                     List<String> valList = new ArrayList<>();
                     Map.Entry entry = (Map.Entry)entries.next();
                     List paramList = PSDeployComponentUtils.convertToParams(
                        entry);
                     for (Object o : paramList) {
                        PSParam param = (PSParam) o;

                        // transform
                        PSAppTransformer.transformParam(param, mapping, idMap);
                        valList.add(param.getValue().getValueText());
                     }
                     Object newVal;
                     if (valList.size() > 1)
                        newVal = valList;
                     else
                        newVal = valList.get(0);
                     entry.setValue(newVal);
                  }
               }
            }
         }
      }

   }

   // see base class
   @Override
   public boolean doesDependencyExist(PSSecurityToken tok, String id)
         throws PSDeployException
   {
      if (tok == null)
         throw new IllegalArgumentException("tok may not be null");

      if (StringUtils.isBlank(id))
         throw new IllegalArgumentException("id may not be null or empty");

      if ( !PSGuid.isValid(PSTypeEnum.ITEM_FILTER, id) )
         return false;
      
      IPSItemFilter c = findFilterByDependencyID(id);
      return (c != null) ? true : false;
   }

   
   /**
    * A util header for filters for handling serialization 
    */
   private static final String XML_HDR_STR = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
   
   /**
    * Constant for this handler's supported type
    */
   public static final String DEPENDENCY_TYPE = "FilterDef";

   /**
    * Filter Svc 
    */
   private static IPSFilterService m_filterSvc = PSFilterServiceLocator
         .getFilterService();
   
   private HashMap<String, IPSItemFilter> m_namedFiltersMap = 
                        new HashMap<>();
   
   private HashMap<IPSGuid, IPSItemFilter> m_guidFiltersMap = 
                        new HashMap<>();


   /**
    * the rule def params name
    */
   public static String RULEDEF_ARGS  = "RuleDefParams";
   
   /**
    * List of child types supported by this handler, it will never be
    * <code>null</code> or empty.
    */
   private static List<String> ms_childTypes = new ArrayList<>();
   
   static
   {
      ms_childTypes.add(PSExitDefDependencyHandler.DEPENDENCY_TYPE);
      ms_childTypes.add(PSAclDefDependencyHandler.DEPENDENCY_TYPE);
   }
}
