/*
 *     Percussion CMS
 *     Copyright (C) 1999-2021 Percussion Software, Inc.
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
 *      https://www.percusssion.com
 *
 *     You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <https://www.gnu.org/licenses/>
 */

package com.percussion.taxonomy.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.percussion.taxonomy.TaxonomySecurityHelper;
import com.percussion.taxonomy.domain.Node;
import com.percussion.taxonomy.service.NodeService;

import java.util.Collection;


import java.util.Map;
import java.util.HashMap;

@Controller
public class NodeController {

    protected final Log logger = LogFactory.getLog(getClass());
    private NodeService nodeService;

    public NodeController() {
        //TODO: Fix me
       /* setCommandClass(Node.class);
        setCommandName("node");
        */
    }

    protected ModelAndView handle(HttpServletRequest request, HttpServletResponse response,
            Object command, BindException errors) throws Exception {
 
    	TaxonomySecurityHelper.raise_error_if_cannot_admin();
    	Collection all = nodeService.getAllNodes(1,1);
        Map<String, Object> myModel = new HashMap<String, Object>();
        myModel.put("all", all);
        myModel.put("node", nodeService.getNode(1,1));
        return new ModelAndView("node", "model", myModel);
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }
}