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
 *      https://www.percusssion.com
 *
 *     You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <https://www.gnu.org/licenses/>
 */

package com.percussion.delivery.jaxrs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class PSExceptionMapper implements ExceptionMapper<Exception>{

    //public static Log log = LogFactory.getLog(PSMetadataRestService.class);
    private static final Logger log = LogManager.getLogger(PSExceptionMapper.class);
    
    @Context
    private HttpServletRequest request;

    @Override
    public Response toResponse(Exception e)
    {
       
        String clientMsg = null;
        Status status = Response.Status.BAD_REQUEST;
        boolean includeStack = false;
        String serverMsg = "JAX-RS Exception with request "+request.getRequestURI()+" host="+request.getRemoteAddr()+": ";
        if (e instanceof JsonMappingException)
        {
            clientMsg = "Invalid request. JSON property is not of an expected type";
            serverMsg+=e.getMessage();
            status = Response.Status.BAD_REQUEST;
        } else if (e instanceof JsonParseException)
        {
            clientMsg = "Invalid request.  Invalid JSON object";
            serverMsg+=e.getMessage();
            status = Response.Status.BAD_REQUEST;
        }
        else if (e instanceof UnrecognizedPropertyException) 
        {
            clientMsg = "Invalid request. The JSON contains an unexpected property";
            serverMsg+=e.getMessage();
            status = Response.Status.BAD_REQUEST;
        }

        else if (e instanceof WebApplicationException)
        {
            log.debug("WebApplicationException:",e);
            return ((WebApplicationException) e).getResponse();
        }
        else
        {
            clientMsg = "An unexpected error occurred processing the request on the DTS server";
            status = Response.Status.INTERNAL_SERVER_ERROR;
            
        }
        
        log.error(serverMsg,includeStack ? e : null);
        
        
        return Response
                .status(status)
                .entity(clientMsg)
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
    
}