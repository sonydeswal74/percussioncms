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
package com.percussion.pagemanagement.assembler.impl;

import com.percussion.cms.objectstore.PSInvalidContentTypeException;
import com.percussion.cms.objectstore.server.PSItemDefManager;
import com.percussion.design.objectstore.PSLocator;
import com.percussion.error.PSException;
import com.percussion.extension.IPSAssemblyLocation;
import com.percussion.extension.IPSExtensionDef;
import com.percussion.extension.PSExtensionException;
import com.percussion.server.IPSRequestContext;
import com.percussion.services.assembly.IPSAssemblyService;
import com.percussion.services.assembly.IPSAssemblyTemplate;
import com.percussion.services.catalog.PSTypeEnum;
import com.percussion.services.guidmgr.IPSGuidManager;
import com.percussion.share.rx.PSLegacyExtensionUtils;
import com.percussion.share.service.exception.PSBeanValidationUtils;
import com.percussion.share.service.exception.PSDataServiceException;
import com.percussion.util.IPSHtmlParameters;
import com.percussion.utils.guid.IPSGuid;
import net.sf.oval.constraint.NotNull;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.percussion.share.rx.PSLegacyExtensionUtils.addParameters;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.Validate.notNull;

/**
 * An adapter for legacy location scheme generators.
 * The location scheme parameters are wrapped in more friendly
 * object: {@link PSAssemblyLocationRequest}.
 * 
 * @author adamgent
 *
 */
public abstract class PSAbstractAssemblyLocationAdapter implements IPSAssemblyLocation
{

    private IPSGuidManager guidManager;
    private IPSAssemblyService assemblyService;
    private PSItemDefManager itemDefManager;
    
    private List<String> parameterNames = new ArrayList<>();
    
    @Override
    public String createLocation(Object[] parameters, IPSRequestContext request) throws PSExtensionException {
        PSAssemblyLocationRequest lr = new PSAssemblyLocationRequest();
        Map<String, String> paramMap = new HashMap<>();
        addParameters(paramMap, getParameterNames(), parameters);
        addParameters(paramMap, request);
        lr.setParameters(paramMap);
        
        lr.setItemId(getItemGuid(lr));
        lr.setTemplateId(getTemplateGuid(lr));
        lr.setSiteId(getSiteGuid(lr));
        lr.setPage(getPageNumber(lr));
        lr.setFolderId(getFolderGuid(lr));
        lr.setItemFilter(getItemFilter(lr));
        lr.setContext(getContext(lr));
        lr.setAssemblyContext(getAssemblyContext(lr));
        lr.setDeliveryContext(getDeliveryContext(lr));
        

        log.debug("Validating location request: {}", lr);
        try {
            PSBeanValidationUtils.validate(lr).throwIfInvalid();

        return createLocation(lr);
        } catch (PSDataServiceException | PSException e) {
            throw new PSExtensionException(e.getMessage(),e);
        }
    }
    
    /**
     * Creates a url from a location request.
     * @param locationRequest never <code>null</code>.
     * @return never <code>null</code>.
     */
    protected abstract String createLocation(PSAssemblyLocationRequest locationRequest) throws PSDataServiceException, PSException;
    
    /**
     * Gets the assembly template.
     * @param locationRequest never <code>null</code>.
     * @return never <code>null</code>.
     */
    protected IPSAssemblyTemplate getTemplate(PSAssemblyLocationRequest locationRequest) {
        IPSGuid templateId = locationRequest.getTemplateId();
        notNull(templateId);
        return assemblyService.findTemplate(templateId);
    }
    
    protected Number getAssemblyContext(PSAssemblyLocationRequest locationRequest) {
        String ac = locationRequest.getParameter(IPSHtmlParameters.SYS_ASSEMBLY_CONTEXT);
        if (isNotBlank(ac)) {
            return Integer.parseInt(ac);
        }
        return null;
    }
    
    protected Number getDeliveryContext(PSAssemblyLocationRequest locationRequest) {
        String ac = locationRequest.getParameter(IPSHtmlParameters.SYS_DELIVERY_CONTEXT);
        if (isNotBlank(ac)) {
            return Integer.parseInt(ac);
        }
        return null;
    }
    /**
     * Gets the content type.
     * @param locationRequest never <code>null</code>.
     * @return never <code>null</code>.
     */
    protected String getContentTypeName(PSAssemblyLocationRequest locationRequest) throws PSException {
        PSLocator locator = guidManager.makeLocator(locationRequest.getItemId());
        long contentTypeId = itemDefManager.getItemContentType(locator);
        try
        {
            return itemDefManager.contentTypeIdToName(contentTypeId);
        }
        catch (PSInvalidContentTypeException e)
        {
            throw new PSException("Cannot get content type for location request", e);
        }
    }
    
    protected String getAuthtype(PSAssemblyLocationRequest request) {
        return request.getParameter(IPSHtmlParameters.SYS_AUTHTYPE);
    }
    
    protected String getItemFilter(PSAssemblyLocationRequest request) {
        return request.getParameter(IPSHtmlParameters.SYS_ITEMFILTER);
    }
    
    protected Integer getPageNumber(PSAssemblyLocationRequest request) {
        String pagestr = request.getParameter("sys_page");
        if (isNotBlank(pagestr)) {
            return Integer.parseInt(pagestr);
        }
        return null;
    }
    protected Integer getContext(PSAssemblyLocationRequest request) {
        String contextstr = request.getParameter(IPSHtmlParameters.SYS_CONTEXT);
        return Integer.parseInt(contextstr);
    }
    
    protected IPSGuid getFolderGuid(PSAssemblyLocationRequest request) {
        String fidstr = request.getParameter(IPSHtmlParameters.SYS_FOLDERID);
        if(isNotBlank(fidstr)) {
            return getGuidManager().makeGuid(new PSLocator(fidstr, "0"));
        }
        return null;
        
    }
    protected IPSGuid getItemGuid(PSAssemblyLocationRequest request) {
        String cidstr = request.getParameter(IPSHtmlParameters.SYS_CONTENTID);
        String revstr = request.getParameter(IPSHtmlParameters.SYS_REVISION);
        return getGuidManager().makeGuid(new PSLocator(cidstr,revstr));
    }
    
    protected IPSGuid getTemplateGuid(PSAssemblyLocationRequest request) {
        String variantid = request.getParameter(IPSHtmlParameters.SYS_VARIANTID);
        return getGuidManager().makeGuid(variantid, PSTypeEnum.TEMPLATE);
    }
    
    protected IPSGuid getSiteGuid(PSAssemblyLocationRequest request) {
        String sitestr = request.getParameter(IPSHtmlParameters.SYS_SITEID);
        if (isNotBlank(sitestr)) {
            return getGuidManager().makeGuid(sitestr, PSTypeEnum.SITE);
        }
        return null;
    }

    @Override
    public void init(IPSExtensionDef extensionDef, @SuppressWarnings("unused") File file)
    {
        setParameterNames(PSLegacyExtensionUtils.getParameterNames(extensionDef));
    }
    
    
    
    public List<String> getParameterNames()
    {
        return parameterNames;
    }

    public void setParameterNames(List<String> parameterNames)
    {
        this.parameterNames = parameterNames;
    }

    public IPSGuidManager getGuidManager()
    {
        return guidManager;
    }

    public void setGuidManager(IPSGuidManager guidManager)
    {
        this.guidManager = guidManager;
    }

    public IPSAssemblyService getAssemblyService()
    {
        return assemblyService;
    }

    public void setAssemblyService(IPSAssemblyService assemblyService)
    {
        this.assemblyService = assemblyService;
    }
    
    public PSItemDefManager getItemDefManager()
    {
        return itemDefManager;
    }

    public void setItemDefManager(PSItemDefManager itemDefManager)
    {
        this.itemDefManager = itemDefManager;
    }

    /**
     * Wraps all the parameters passed into a location
     * scheme generator. Most of the ids have been
     * converted to guids.
     * 
     * @author adamgent
     *
     */
    public static class PSAssemblyLocationRequest {

        @NotNull
        private IPSGuid itemId;
        @NotNull
        private IPSGuid templateId;
        @NotNull
        private IPSGuid siteId;
        private IPSGuid folderId;
        @NotNull
        private Number context;
        
        private Number assemblyContext;
        
        private Number deliveryContext;
        
        private String itemFilter;
        
        private Integer page;
        private Map<String, String> parameters = new HashMap<>();
        
        public String getParameter(String name) {
            return parameters.get(name);
        }

        public IPSGuid getItemId()
        {
            return itemId;
        }
        public void setItemId(IPSGuid itemId)
        {
            this.itemId = itemId;
        }
        public IPSGuid getFolderId()
        {
            return folderId;
        }
        public void setFolderId(IPSGuid folderId)
        {
            this.folderId = folderId;
        }
        public IPSGuid getTemplateId()
        {
            return templateId;
        }
        public void setTemplateId(IPSGuid templateId)
        {
            this.templateId = templateId;
        }
        public IPSGuid getSiteId()
        {
            return siteId;
        }
        public void setSiteId(IPSGuid siteId)
        {
            this.siteId = siteId;
        }
        public Map<String, String> getParameters()
        {
            return parameters;
        }
        public void setParameters(Map<String, String> parameters)
        {
            this.parameters = parameters;
        }
        public Integer getPage()
        {
            return page;
        }
        public void setPage(Integer page)
        {
            this.page = page;
        }
        
        
        public String getItemFilter()
        {
            return itemFilter;
        }
        public void setItemFilter(String itemFilter)
        {
            this.itemFilter = itemFilter;
        }
        
        
        public Number getContext()
        {
            return context;
        }
        public void setContext(Number context)
        {
            this.context = context;
        }
        
        
        
        public Number getAssemblyContext()
        {
            return assemblyContext;
        }

        public void setAssemblyContext(Number assemblyContext)
        {
            this.assemblyContext = assemblyContext;
        }

        public Number getDeliveryContext()
        {
            return deliveryContext;
        }

        public void setDeliveryContext(Number deliveryContext)
        {
            this.deliveryContext = deliveryContext;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PSAssemblyLocationRequest)) return false;
            PSAssemblyLocationRequest that = (PSAssemblyLocationRequest) o;
            return Objects.equals(getItemId(), that.getItemId()) && Objects.equals(getTemplateId(), that.getTemplateId()) && Objects.equals(getSiteId(), that.getSiteId()) && Objects.equals(getFolderId(), that.getFolderId()) && Objects.equals(getContext(), that.getContext()) && Objects.equals(getAssemblyContext(), that.getAssemblyContext()) && Objects.equals(getDeliveryContext(), that.getDeliveryContext()) && Objects.equals(getItemFilter(), that.getItemFilter()) && Objects.equals(getPage(), that.getPage()) && Objects.equals(getParameters(), that.getParameters());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getItemId(), getTemplateId(), getSiteId(), getFolderId(), getContext(), getAssemblyContext(), getDeliveryContext(), getItemFilter(), getPage(), getParameters());
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("PSAssemblyLocationRequest{");
            sb.append("itemId=").append(itemId);
            sb.append(", templateId=").append(templateId);
            sb.append(", siteId=").append(siteId);
            sb.append(", folderId=").append(folderId);
            sb.append(", context=").append(context);
            sb.append(", assemblyContext=").append(assemblyContext);
            sb.append(", deliveryContext=").append(deliveryContext);
            sb.append(", itemFilter='").append(itemFilter).append('\'');
            sb.append(", page=").append(page);
            sb.append(", parameters=").append(parameters);
            sb.append('}');
            return sb.toString();
        }

        @Override
        public PSAbstractAssemblyLocationAdapter clone()
        {
            try
            {
                return (PSAbstractAssemblyLocationAdapter) BeanUtils.cloneBean(this);
            }
            catch (Exception e)
            {
              throw new RuntimeException(e);
            }
        }
    }
    
    
    /**
     * The log instance to use for this class, never <code>null</code>.
     */
    protected final Logger log = LogManager.getLogger(getClass());

}

