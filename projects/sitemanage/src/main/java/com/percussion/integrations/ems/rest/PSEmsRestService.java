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

package com.percussion.integrations.ems.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.percussion.delivery.client.PSDeliveryClient;
import com.percussion.delivery.data.PSDeliveryInfo;
import com.percussion.delivery.service.IPSDeliveryInfoService;
import com.percussion.error.PSExceptionUtils;
import com.percussion.util.PSSiteManageBean;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import static org.apache.commons.lang.Validate.notNull;

/***
 * Functions as a proxy on the CMS side for calling the remote DTS EMS Api client. 
 * Used from within content editors and content type fields.
 * 
 * @author natechadwick
 *
 */
@Path("/ems")
@PSSiteManageBean("emsAPIService")
public class PSEmsRestService {
	
	
	 private static final String BUILDINGS_PATH = "/perc-integrations/integrations/ems/buildings";
	 private static final String GROUPS_PATH = "/perc-integrations/integrations/ems/groups";
	 private static final String EVENTTYPES_PATH = "/perc-integrations/integrations/ems/eventtypes";
	 private static final String BOOKINGS_PATH = "/perc-integrations/integrations/ems/bookings";
	 private static final String MC_CALENDARS_PATH = "/perc-integrations/integrations/ems/mc/calendars";
	 private static final String MC_LOCATIONS_PATH = "/perc-integrations/integrations/ems/mc/locations";
	 private static final String MC_GROUPINGS_PATH = "/perc-integrations/integrations/ems/mc/groupings";
	 private static final String MC_EVENTTYPES_PATH = "/perc-integrations/integrations/ems/mc/eventtypes";
	 private static final String MC_EVENTS_PATH = "/perc-integrations/integrations/ems/mc/events";
	 private static final String MC_FEATUREDEVENTS_PATH = "/perc-integrations/integrations/ems/mc/featuredevents";

	 /**
     * The delivery service initialized by constructor, never <code>null</code>.
     */
    private IPSDeliveryInfoService deliveryService;
	private static final Logger log = LogManager.getLogger(PSEmsRestService.class);
    /***
     * The license Override if any
     */
    private String licenseId="";

  	@Autowired
    public PSEmsRestService(IPSDeliveryInfoService deliveryService){
        notNull(deliveryService);
    	this.deliveryService = deliveryService;
    }
    
    
    public void setLicenseOverride(String licenseId) {
  		this.licenseId = licenseId;
  	}

  	public String getLicenseOverride() {
  		return this.licenseId;
  	}
    
	@GET
	@Path("/buildings")
	@Produces(MediaType.APPLICATION_XML)
	public Response getBuildings(){
		String ret = "";
		 try
	        {
	            PSDeliveryInfo server = deliveryService.findByService(PSDeliveryInfo.SERVICE_INTEGRATIONS);
	            if(server == null){
	            	String msg ="Unable to locate delivery server for perc-integration service.  Verify the service is registered in the delivery-servers.xml and try again"; 
	            	log.error(msg);
	            	return Response.serverError().entity(msg).build();
	            }
	            PSDeliveryClient deliveryClient = new PSDeliveryClient();
	            
	            deliveryClient.setLicenseOverride(licenseId);
	            
	            HttpMethod method = new GetMethod();
	            method.setPath(BUILDINGS_PATH);
	            method.setRequestHeader("Content-Type", MediaType.APPLICATION_XML);
	            method.setRequestHeader("Accept", MediaType.APPLICATION_XML);
	            
	            URI uri = new URI(new URI(server.getUrl(),true),BUILDINGS_PATH,true);
	            method.setURI(uri);
	          
	            int code = deliveryClient.executeMethod(method);
	            if(code == 200){
	            	ret = IOUtils.toString(method.getResponseBodyAsStream());
	            }else{
	            	throw new Exception("Invalid response code:" + code + " was received when listing buildings.");
	            }
	            
	        }catch(Exception e){
	        	log.error("Error pulling buildings from DTS. Error: {}",
						PSExceptionUtils.getMessageForLog(e));
	        	return Response.serverError().entity(e.getMessage()).build();
	        }
		 return Response.status(Status.OK).entity(ret).build();
		 
	}
	
	@GET
	@Path("/eventtypes")
	@Produces(MediaType.APPLICATION_XML)
	public Response getEventTypes(){
		String ret = "";
		try{
			
			  PSDeliveryInfo server = deliveryService.findByService(PSDeliveryInfo.SERVICE_INTEGRATIONS);
	            if(server == null){
	            	String msg ="Unable to locate delivery server for perc-integration service.  Verify the service is registered in the delivery-servers.xml and try again"; 
	            	log.error(msg);
	            	return Response.serverError().entity(msg).build();
	            }
	            PSDeliveryClient deliveryClient = new PSDeliveryClient();
	            deliveryClient.setLicenseOverride(licenseId);
	            
	            HttpMethod method = new GetMethod();
	            method.setPath(EVENTTYPES_PATH);
	            method.setRequestHeader("Content-Type", MediaType.APPLICATION_XML);
	            method.setRequestHeader("Accept", MediaType.APPLICATION_XML);
	            
	            URI uri = new URI(new URI(server.getUrl(),true),EVENTTYPES_PATH,true);
	            method.setURI(uri);
	          
	            int code = deliveryClient.executeMethod(method);
	            if(code == 200){
	            	ret = IOUtils.toString(method.getResponseBodyAsStream());
	            }else{
	            	throw new Exception("Invalid response code:" + code + " was received when listing eventtypes.");
	            }
			
		}
		catch(Exception e){
			log.error("Error pulling Event Types from DTS. Error: {}",
					PSExceptionUtils.getMessageForLog(e));
			return Response.serverError().entity(e.getMessage()).build();
		}
		
		return Response.status(Status.OK).entity(ret).build();
	}
	
	@GET
	@Path("/groups")
	@Produces(MediaType.APPLICATION_XML)
	public Response getGroups(){
		String ret = "";
		try{
			  PSDeliveryInfo server = deliveryService.findByService(PSDeliveryInfo.SERVICE_INTEGRATIONS);
	            if(server == null){
	            	String msg ="Unable to locate delivery server for perc-integration service.  Verify the service is registered in the delivery-servers.xml and try again"; 
	            	log.error(msg);
	            	return Response.serverError().entity(msg).build();
	            }
	            PSDeliveryClient deliveryClient = new PSDeliveryClient();
	            deliveryClient.setLicenseOverride(licenseId);
	            
	            HttpMethod method = new GetMethod();
	            method.setPath(GROUPS_PATH);
	            method.setRequestHeader("Content-Type", MediaType.APPLICATION_XML);
	            method.setRequestHeader("Accept", MediaType.APPLICATION_XML);
	            
	            URI uri = new URI(new URI(server.getUrl(),true),GROUPS_PATH,true);
	            method.setURI(uri);
	          
	            int code = deliveryClient.executeMethod(method);
	            if(code == 200){
	            	ret = IOUtils.toString(method.getResponseBodyAsStream());
	            }else{
	            	throw new Exception("Invalid response code:" + code + " was received when listing groups.");
	            }
		}
		catch(Exception e){
			log.error("Error pulling Groups from DTS. Error: {}",
					PSExceptionUtils.getMessageForLog(e));
			return Response.serverError().entity(e.getMessage()).build();	
		}
		
		return Response.status(Status.OK).entity(ret).build();
	}
	
	@POST
	@Path("/bookings")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getBookings(PSBookingsQuery query){
		String ret = "";
		try{
			log.debug("Entering getBookings...");
			PSDeliveryInfo server = deliveryService.findByService(PSDeliveryInfo.SERVICE_INTEGRATIONS);
	         if(server == null){
	         	String msg ="Unable to locate delivery server for perc-integration service.  Verify the service is registered in the delivery-servers.xml and try again"; 
	         	log.error(msg);
	         	return Response.serverError().entity(msg).build();
	         }
	         
	         PSDeliveryClient deliveryClient = new PSDeliveryClient();
	         deliveryClient.setLicenseOverride(licenseId);
	         ObjectMapper mapper = new ObjectMapper();
	         log.debug("Building Request Entity...");
	         StringRequestEntity entity = new StringRequestEntity(mapper.writeValueAsString(query),MediaType.APPLICATION_JSON, "UTF-8");
	         log.debug("Built Request Entity");
	         PostMethod method = new PostMethod();
	         method.setPath(BOOKINGS_PATH);
	         method.setRequestHeader("Content-Type", MediaType.APPLICATION_JSON);
	         method.setRequestHeader("Accept", MediaType.APPLICATION_JSON);
	         method.setRequestEntity(entity);
	         
	         URI uri = new URI(new URI(server.getUrl(),true),BOOKINGS_PATH,true);
	         method.setURI(uri);
	       
	         int code = deliveryClient.executeMethod(method);
	         if(code == 200){
	         	ret = IOUtils.toString(method.getResponseBodyAsStream());
	         }else{
	         	throw new Exception("Invalid response code:" + code + " was received when listing bookings.");
	         }
		}
		catch(Exception e){
			log.error("Error pulling Groups from DTS. Error: {}",
					PSExceptionUtils.getMessageForLog(e));
			return Response.serverError().entity(e.getMessage()).build();	
		}
	
	return Response.status(Status.OK).entity(ret).build();
         
	}
	
	@GET
	@Path("/mc/groupings")
	@Produces(MediaType.APPLICATION_XML)
	public Response getMCGroupings(){
		String ret = "";
		try{
			  PSDeliveryInfo server = deliveryService.findByService(PSDeliveryInfo.SERVICE_INTEGRATIONS);
	            if(server == null){
	            	String msg ="Unable to locate delivery server for perc-integration service.  Verify the service is registered in the delivery-servers.xml and try again"; 
	            	log.error(msg);
	            	return Response.serverError().entity(msg).build();
	            }
	            PSDeliveryClient deliveryClient = new PSDeliveryClient();
	            deliveryClient.setLicenseOverride(licenseId);
	            
	            HttpMethod method = new GetMethod();
	            method.setPath(MC_GROUPINGS_PATH);
	            method.setRequestHeader("Content-Type", MediaType.APPLICATION_XML);
	            method.setRequestHeader("Accept", MediaType.APPLICATION_XML);
	            
	            URI uri = new URI(new URI(server.getUrl(),true),MC_GROUPINGS_PATH,true);
	            method.setURI(uri);
	          
	            int code = deliveryClient.executeMethod(method);
	            if(code == 200){
	            	ret = IOUtils.toString(method.getResponseBodyAsStream());
	            }else{
	            	throw new Exception("Invalid response code:" + code + " was received when listing MC groupings.");
	            }
		}
		catch(Exception e){
			log.error("Error pulling Groups from DTS. Error: {}",
					PSExceptionUtils.getMessageForLog(e));
			return Response.serverError().entity(e.getMessage()).build();	
		}
		
		return Response.status(Status.OK).entity(ret).build();
	}
	
	
	@GET
	@Path("/mc/locations")
	@Produces(MediaType.APPLICATION_XML)
	public Response getMCLocations(){
		String ret = "";
		try{
			  PSDeliveryInfo server = deliveryService.findByService(PSDeliveryInfo.SERVICE_INTEGRATIONS);
	            if(server == null){
	            	String msg ="Unable to locate delivery server for perc-integration service.  Verify the service is registered in the delivery-servers.xml and try again"; 
	            	log.error(msg);
	            	return Response.serverError().entity(msg).build();
	            }
	            PSDeliveryClient deliveryClient = new PSDeliveryClient();
	            deliveryClient.setLicenseOverride(licenseId);
	            
	            HttpMethod method = new GetMethod();
	            method.setPath(MC_LOCATIONS_PATH);
	            method.setRequestHeader("Content-Type", MediaType.APPLICATION_XML);
	            method.setRequestHeader("Accept", MediaType.APPLICATION_XML);
	            
	            URI uri = new URI(new URI(server.getUrl(),true),MC_LOCATIONS_PATH,true);
	            method.setURI(uri);
	          
	            int code = deliveryClient.executeMethod(method);
	            if(code == 200){
	            	ret = IOUtils.toString(method.getResponseBodyAsStream());
	            }else{
	            	throw new Exception("Invalid response code:" + code + " was received when listing MC locations.");
	            }
		}
		catch(Exception e){
			log.error("Error pulling MC Locations from DTS. Error: {}",
					PSExceptionUtils.getMessageForLog(e));
			return Response.serverError().entity(e.getMessage()).build();	
		}
		
		return Response.status(Status.OK).entity(ret).build();
	}
	
	@GET
	@Path("/mc/eventtypes")
	@Produces(MediaType.APPLICATION_XML)
	public Response getMCEventTypes(){
		String ret = "";
		try{
			  PSDeliveryInfo server = deliveryService.findByService(PSDeliveryInfo.SERVICE_INTEGRATIONS);
	            if(server == null){
	            	String msg ="Unable to locate delivery server for perc-integration service.  Verify the service is registered in the delivery-servers.xml and try again"; 
	            	log.error(msg);
	            	return Response.serverError().entity(msg).build();
	            }
	            PSDeliveryClient deliveryClient = new PSDeliveryClient();
	            deliveryClient.setLicenseOverride(licenseId);
	            
	            HttpMethod method = new GetMethod();
	            method.setPath(MC_EVENTTYPES_PATH);
	            method.setRequestHeader("Content-Type", MediaType.APPLICATION_XML);
	            method.setRequestHeader("Accept", MediaType.APPLICATION_XML);
	            
	            URI uri = new URI(new URI(server.getUrl(),true),MC_EVENTTYPES_PATH,true);
	            method.setURI(uri);
	          
	            int code = deliveryClient.executeMethod(method);
	            if(code == 200){
	            	ret = IOUtils.toString(method.getResponseBodyAsStream());
	            }else{
	            	throw new Exception("Invalid response code:" + code + " was received when listing MC eventtypes.");
	            }
		}
		catch(Exception e){
			log.error("Error pulling Event Types from DTS. Error: {}",
					PSExceptionUtils.getMessageForLog(e));
			return Response.serverError().entity(e.getMessage()).build();	
		}
		
		return Response.status(Status.OK).entity(ret).build();
	}
	
	@GET
	@Path("/mc/calendars")
	@Produces(MediaType.APPLICATION_XML)
	public Response getMCCalendars(){
		String ret = "";
		try{
			  PSDeliveryInfo server = deliveryService.findByService(PSDeliveryInfo.SERVICE_INTEGRATIONS);
	            if(server == null){
	            	String msg ="Unable to locate delivery server for perc-integration service.  Verify the service is registered in the delivery-servers.xml and try again"; 
	            	log.error(msg);
	            	return Response.serverError().entity(msg).build();
	            }
	            PSDeliveryClient deliveryClient = new PSDeliveryClient();
	            deliveryClient.setLicenseOverride(licenseId);
	            
	            HttpMethod method = new GetMethod();
	            method.setPath(MC_CALENDARS_PATH);
	            method.setRequestHeader("Content-Type", MediaType.APPLICATION_XML);
	            method.setRequestHeader("Accept", MediaType.APPLICATION_XML);
	            
	            URI uri = new URI(new URI(server.getUrl(),true),MC_CALENDARS_PATH,true);
	            method.setURI(uri);
	          
	            int code = deliveryClient.executeMethod(method);
	            if(code == 200){
	            	ret = IOUtils.toString(method.getResponseBodyAsStream());
	            }else{
	            	throw new Exception("Invalid response code:" + code + " was received when listing MC Calendars.");
	            }
		}
		catch(Exception e){
			log.error("Error pulling MC Calendars from DTS. Error: {}",
					PSExceptionUtils.getMessageForLog(e));
			return Response.serverError().entity(e.getMessage()).build();	
		}
		
		return Response.status(Status.OK).entity(ret).build();
	}
	
	@POST
	@Path("/mc/events")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getMCEvents(PSEventQuery query){
		String ret = "";
		try{
			log.debug("Entering getMCEvents...");
			PSDeliveryInfo server = deliveryService.findByService(PSDeliveryInfo.SERVICE_INTEGRATIONS);
	         if(server == null){
	         	String msg ="Unable to locate delivery server for perc-integration service.  Verify the service is registered in the delivery-servers.xml and try again"; 
	         	log.error(msg);
	         	return Response.serverError().entity(msg).build();
	         }
	         
	         PSDeliveryClient deliveryClient = new PSDeliveryClient();
	         deliveryClient.setLicenseOverride(licenseId);
	         ObjectMapper mapper = new ObjectMapper();
	         log.debug("Building Request Entity...");
	         StringRequestEntity entity = new StringRequestEntity(mapper.writeValueAsString(query),MediaType.APPLICATION_JSON, "UTF-8");
	         log.debug("Built Request Entity");
	         PostMethod method = new PostMethod();
	         method.setPath(MC_EVENTS_PATH);
	         method.setRequestHeader("Content-Type", MediaType.APPLICATION_JSON);
	         method.setRequestHeader("Accept", MediaType.APPLICATION_JSON);
	         method.setRequestEntity(entity);
	         
	         URI uri = new URI(new URI(server.getUrl(),true),MC_EVENTS_PATH,true);
	         method.setURI(uri);
	       
	         int code = deliveryClient.executeMethod(method);
	         if(code == 200){
	         	ret = IOUtils.toString(method.getResponseBodyAsStream());
	         }else{
	         	throw new Exception("Invalid response code:" + code + " was received when listing MC Events.");
	         }
		}
		catch(Exception e){
			log.error("Error pulling MC Events from DTS. Error: {}",
					PSExceptionUtils.getMessageForLog(e));
			return Response.serverError().entity(e.getMessage()).build();	
		}
	
	return Response.ok(ret,MediaType.APPLICATION_JSON).build();
         
	}

	@POST
	@Path("/mc/featuredevents")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getMCFutureEvents(PSFeaturedEventsQuery query){
		String ret = "";
		try{
			log.debug("Entering getMCFeaturedEvents...");
			PSDeliveryInfo server = deliveryService.findByService(PSDeliveryInfo.SERVICE_INTEGRATIONS);
	         if(server == null){
	         	String msg ="Unable to locate delivery server for perc-integration service.  Verify the service is registered in the delivery-servers.xml and try again"; 
	         	log.error(msg);
	         	return Response.serverError().entity(msg).build();
	         }
	         
	         PSDeliveryClient deliveryClient = new PSDeliveryClient();
	         deliveryClient.setLicenseOverride(licenseId);
	         ObjectMapper mapper = new ObjectMapper();
	         log.debug("Building Request Entity...");
	         StringRequestEntity entity = new StringRequestEntity(mapper.writeValueAsString(query),MediaType.APPLICATION_JSON, "UTF-8");
	         log.debug("Built Request Entity");
	         PostMethod method = new PostMethod();
	         method.setPath(MC_FEATUREDEVENTS_PATH);
	         method.setRequestHeader("Content-Type", MediaType.APPLICATION_JSON);
	         method.setRequestHeader("Accept", MediaType.APPLICATION_JSON);
	         method.setRequestEntity(entity);
	         
	         URI uri = new URI(new URI(server.getUrl(),true),MC_FEATUREDEVENTS_PATH,true);
	         method.setURI(uri);
	       
	         int code = deliveryClient.executeMethod(method);
	         if(code == 200){
	         	ret = IOUtils.toString(method.getResponseBodyAsStream());
	         }else{
	         	throw new Exception("Invalid response code:" + code + " was received when listing MC Featured Events.");
	         }
		}
		catch(Exception e){
			log.error("Error pulling MC Featured Events from DTS. Error: {}",
					PSExceptionUtils.getMessageForLog(e));
			return Response.serverError().entity(e.getMessage()).build();	
		}
	
	return Response.status(Status.OK).entity(ret).build();
         
	}

}
