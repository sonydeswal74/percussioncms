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

package com.percussion.pagemanagement.web.service;

import static org.apache.commons.lang.Validate.*;

import java.util.List;

import com.percussion.pagemanagement.data.PSTemplate;
import com.percussion.pagemanagement.data.PSHtmlMetadata;
import com.percussion.pagemanagement.data.PSTemplateSummary;
import com.percussion.share.IPSSitemanageConstants;
import com.percussion.share.test.PSObjectRestClient;

/**
 * The class used for unit test on REST layer.
 * 
 * @author adamgent
 * @author YuBingChen
 */
public class PSTemplateServiceClient extends PSObjectRestClient
{
    private String path = "/Rhythmyx/services/pagemanagement/template/";

    public PSTemplateServiceClient(String baseUrl)
    {
        super(baseUrl);
    }

    public List<PSTemplateSummary> findAll()
    {
        return getObjectsFromPath( concatPath(getPath(), "/summary/all"), PSTemplateSummary.class);
    }

    public PSTemplate save(PSTemplate template) {
        return postObjectToPath(getPath(), template, PSTemplate.class);
    }
    
    public PSTemplate save(PSTemplate template, String pageId) {
        return postObjectToPath(concatPath(getPath(), "/page/", pageId), template, PSTemplate.class);
    }
    
    public PSTemplate createTemplate(String name, String srcId)
    {
        PSTemplate summ = getObjectFromPath(concatPath(getPath(), "/create/", name, srcId),
                PSTemplate.class);
        return summ;
    }

    public PSTemplate loadTemplate(String id)
    {
        PSTemplate summ = getObjectFromPath(concatPath(getPath(), id), PSTemplate.class);
        return summ;
    }
    
    public PSHtmlMetadata loadHtmlMetadata(String id)
    {
        PSHtmlMetadata metadata = 
            getObjectFromPath(concatPath(getPath(), "/loadTemplateMetadata/", id), 
                    PSHtmlMetadata.class);
        return metadata;
    }
    
    public void saveHtmlMetadata(PSHtmlMetadata metadata) {
        postObjectToPath(concatPath(getPath(), "/saveTemplateMetadata"), metadata);
    }

    public void deleteTemplate(String id)
    {
        notEmpty(id, "id");
        DELETE(concatPath(getPath(), id));
        
    }

    public PSTemplateSummary findTemplate(String id)
    {
        return getObjectFromPath(concatPath(getPath(), "summary/", id), PSTemplateSummary.class);
    }

    public List<PSTemplateSummary> findAllReadOnly()
    {
        return getObjectsFromPath(concatPath(getPath(), "/summary/all/readonly"),
                PSTemplateSummary.class);
    }

    public PSTemplate saveTemplate(PSTemplate template)
    {
        return postObjectToPath(getPath(), template, PSTemplate.class);
    }
    
    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }
    
    public String getContentOnlyTemplateId()
    {
        // get the template source ID
        List<PSTemplateSummary> summs = findAll();
        String srcId = null;
        for (PSTemplateSummary sum : summs)
        {
            if (sum.getName().indexOf(IPSSitemanageConstants.PLAIN_BASE_TEMPLATE_NAME) >= 0)
            {
                srcId = sum.getId();
                break;
            }
        }
        if (srcId == null)
            throw new RuntimeException("Cannot find \"plain\" template.");

        // create a template item
        PSTemplateSummary item = createTemplate("test.template.plain7", srcId);
        String id = item.getId();
   
        return id;
    }
}
