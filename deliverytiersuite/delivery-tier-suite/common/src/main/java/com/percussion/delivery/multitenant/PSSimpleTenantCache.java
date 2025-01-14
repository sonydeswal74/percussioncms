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
package com.percussion.delivery.multitenant;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.percussion.delivery.multitenant.IPSTenantAuthorization.Status;

/**
 * Provides a very simple in memory cache for tenant information
 * , usage, and authorizations.
 * 
 * 
 * @author natechadwick
 *
 */
public class PSSimpleTenantCache implements IPSTenantCache {

	/***
	 * Thread safe Hash map to hold the cache
	 */
	private ConcurrentHashMap<String, IPSTenantInfo> cache = new ConcurrentHashMap<>();
	
	/***
	 * Minutes cache entries have before needing re-authorization
	 */
	private long ttl;
	
	  /**
     * Log for this class.
     */
    private static final Logger log = LogManager.getLogger(PSSimpleTenantCache.class);
    
    private boolean authorizeExpiredTTL;
    private IPSTenantAuthorization auth;
    
	/** 
	 * @see com.percussion.delivery.multitenant.IPSTenantCache#setMaxTTL(int)
	 */
	@Override
	public void setMaxTTL(long minutes) {
		this.ttl = minutes;
	}
	
	/*** 
	 * @see com.percussion.delivery.multitenant.IPSTenantCache#getMaxTTL()
	 */
	@Override
	public long getMaxTTL() {
		return this.ttl;
	}

	/** 
	 * @see com.percussion.delivery.multitenant.IPSTenantCache#get(java.lang.String)
	 */
	@Override
	public IPSTenantInfo get(String id, ServletRequest req) {

			IPSTenantInfo t = cache.get(id);
			
			//Record overall calls
			if(t!=null){
				t.addAPIUsage(1);
				cache.put(id, t);
			}
			
			if(t!=null && ttl<checkTTLAge(t.getLastAuthorizationCheckDate())){
				log.debug("Cached Authorization expired for Tenant " + id);
				if(authorizeExpiredTTL)
				{
					reauthorize(t,req);
					return cache.get(id);
				}
			}	
	
		return t;

	}

	/** 
	 * @see com.percussion.delivery.multitenant.IPSTenantCache#put(com.percussion.delivery.multitenant.IPSTenantInfo)
	 */
	@Override
	public void put(IPSTenantInfo tenant) {
		if(cache.replace(tenant.getTenantId(), tenant)==null)
			cache.put(tenant.getTenantId(), tenant);
	}

	/** 
	 * @see com.percussion.delivery.multitenant.IPSTenantCache#remove(java.lang.String)
	 */
	@Override
	public void remove(String id) {
		cache.remove(id);
	}

	/** 
	 * @see com.percussion.delivery.multitenant.IPSTenantCache#clear()
	 */
	@Override
	public void clear() {
		cache.clear();	
	}

	/** 
	 * @see com.percussion.delivery.multitenant.IPSTenantCache#scavenge()
	 */
	@Override
	public void scavenge(ServletRequest req) {
		
		log.debug("Initiating scavenge for expired entries...");
		
		Iterator<Entry<String, IPSTenantInfo>> it = cache.entrySet().iterator();
	    IPSTenantInfo t;
	    
		while (it.hasNext()) {
			Map.Entry<String, IPSTenantInfo> pairs = it.next();
	    
	        t = (IPSTenantInfo)pairs.getValue();

	   	    if(ttl<checkTTLAge(t.getLastAuthorizationCheckDate())){
	   	    	log.debug("Authorization expired for tenant " + t.getTenantId() + " reauthorizing");	
	   	    	reauthorize(t,req);
	   	    }
		}
		
	}	

	/***
	 * Helper method to determine if a TTL date has expired. 
	 * 
	 * @param last
	 * @return
	 */
	private long checkTTLAge(Date last){	     
		return ((new Date().getTime() - last.getTime())/1000)/60;
	}

	/** 
	 * @see com.percussion.delivery.multitenant.IPSTenantCache#getAuthorizeExpiredTTL()
	 */
	@Override
	public boolean getAuthorizeExpiredTTL() {
		return this.authorizeExpiredTTL;
	}

	/** 
	 * @see com.percussion.delivery.multitenant.IPSTenantCache#setAuthorizeExpiredTTL(boolean)
	 */
	@Override
	public void setAuthorizeExpiredTTL(boolean ret) {
		this.authorizeExpiredTTL = ret;
	}

	/** 
	 * @see com.percussion.delivery.multitenant.IPSTenantCache#getAuthorizationProvider()
	 */
	@Override
	public IPSTenantAuthorization getAuthorizationProvider() {
		return this.auth;
	}

	/*** 
	 * @see com.percussion.delivery.multitenant.IPSTenantCache#setAuthorizationProvider(com.percussion.delivery.multitenant.IPSTenantAuthorization)
	 */
	@Override
	public void setAuthorizationProvider(IPSTenantAuthorization auth) {
		this.auth = auth;
	}

	/***
	 * Re-authorizes the specified tenant with the authorization provider 
	 * if configured. 
	 * 
	 * @param t
	 * @return true if the tenant has been authorized and refreshed, false if not.
	 */
	private boolean reauthorize(IPSTenantInfo t, ServletRequest req){
		boolean ret = false;
		
		if(this.auth!=null){
			log.warn("Tenant Authorization service not initialized.");
		}else{
			log.debug("Reauthorizing tenant " + t.getTenantId());
			
			PSLicenseStatus s = auth.authorize(t.getTenantId(),t.getAPIUsage(),null);
			
			if(s.getStatusCode() == Status.SUCCESS){
				t.setLastAuthorizationCheckDate(new Date());
				cache.put(t.getTenantId(), t);
				ret = true;
			}else{
				log.debug("Tenanant " + t.getTenantId() + "Not authorized");
				cache.remove(t.getTenantId());
				ret = false;
			}
		}
		
		return ret;
	}
	
}
