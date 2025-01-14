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

package com.percussion.services.integrations.siteimprove;

import com.percussion.error.PSExceptionUtils;
import com.percussion.security.TLSSocketFactory;
import com.percussion.server.PSServer;
import com.percussion.services.integrations.IPSIntegrationProviderService;
import com.percussion.util.PSURLEncoder;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Services for the REST endpoint for our Siteimprove integration.
 */
public class PSSiteImproveProviderService implements IPSIntegrationProviderService
{

   // The api endpoints for the Siteimprove api.
   private static final String NEW_SITEIMPROVE_BASE_URL = "https://api-gateway.siteimprove.com/cms-recheck";

   private static final String PERCUSSION_CM1_VERSION = "Percussion CMS " + PSServer.getVersion();
   private static final String SITEIMPROVE_TOKEN_URL = "https://my2.siteimprove.com/auth/token?cms=";
   private static final String SITEIMPROVE_RECRAWL_SITE = "recrawl";
   private static final String SITEIMPROVE_RECHECK_PAGE = "recheck";
   private static final String SITEIMPROVE_TOKEN = "token";

   // Header strings
   private static final String ACCEPTS = "Accept";
   private static final String APPLICATION_JSON = "application/json";
   private static final String CONTENT_TYPE = "Content-Type";
   private static final String UTF_8 = "UTF-8";
   private static ExecutorService pool = Executors.newFixedThreadPool(1);
   private static final Logger logger = LogManager.getLogger(PSSiteImproveProviderService.class);
   
   private static final int HTTPS_PORT = 443;
   private static final String DEFAULT_PROTOCOL = "http";

   /**
    * Gets a new Siteimprove token for the site. It is to be saved/persisted in
    * PSMetadata object unless the feature is disabled/re-enabled for a site.
    * 
    * @return the token. empty string if not set
    */
   public String getNewSiteImproveToken()
   {

    try{
       registerSslProtocol();
    }catch(Exception e){
       logger.error("Error initilizing SSL Engine: {}" , PSExceptionUtils.getMessageForLog(e));
       return "";
    }

      String SITEIMPROVE_TOKEN_QUERY_PARAM = PSURLEncoder.encodeQuery(PERCUSSION_CM1_VERSION);
      GetMethod getMethod = new GetMethod(SITEIMPROVE_TOKEN_URL + SITEIMPROVE_TOKEN_QUERY_PARAM);
      try
      {
         executeMethod(getMethod);
      }
      catch (Exception e1)
      {
         logger.error("Unable to get new Siteimprove token with message: {}", PSExceptionUtils.getMessageForLog(e1));
         return "";
      }
      String token = "";

      try
      {
         JSONObject jsonObjectItems = new JSONObject(getMethod.getResponseBodyAsString());
         token = jsonObjectItems.getString(SITEIMPROVE_TOKEN);
      }
      catch (IOException | JSONException e)
      {
         logger.error("Failed to get new Siteimprove token with message: {}" ,
                 PSExceptionUtils.getMessageForLog(e));
         logger.debug(e);
      }

      return token;

   }

   /**
    * Request a site check from siteimprove. Site is determined by id.
    *
    * @param siteId Site id of the site we wish to check.
    * @param credentials The credentials allowing us to access the siteimprove
    *           api.
    * @throws Exception Siteimprove rejected our request or the site id was bad.
    */
   @Override
   public void updateSiteInfo(final String siteId, final Map<String, String> credentials) throws Exception
   {

      if (siteId == null || siteId.isEmpty())
      {
         throw new NullPointerException("siteURL cannot be null or empty.");
      }

      pool.submit(() -> {
         try
         {

            registerSslProtocol();

            PostMethod postMethod = new PostMethod(NEW_SITEIMPROVE_BASE_URL);

            JSONObject object = new JSONObject();
            object.accumulate("url", siteId);
            object.accumulate(SITEIMPROVE_TOKEN, credentials.get(SITEIMPROVE_TOKEN));
            object.accumulate("type", SITEIMPROVE_RECRAWL_SITE);

            logger.debug("JSON Object body: {}" ,object);

            StringRequestEntity requestEntity = new StringRequestEntity(object.toString(), APPLICATION_JSON, UTF_8);

            postMethod.setRequestEntity(requestEntity);

            boolean responseStatus = executeMethod(postMethod);

            if (!responseStatus)
            {
               throw new PSSiteImproveProviderException("Failed to request a page check from siteimprove with id:  " + siteId);
            }

         }
         catch (Exception e)
         {
            logger.error(e);
         }
      });
   }

   /**
    * Request a page check from siteimprove.
    *
    * @param siteId Id of our site that the page lives on
    * @param pageURL The live URL of a page, must pre-exist from a site crawl on
    *           siteimprove's side. Otherwise do a new site crawl.
    * @param credentials The sitename/token allowing us to access the siteimprove
    *           api.
    */
   @Override
   public void updatePageInfo(final String siteId, final String pageURL, final Map<String, String> credentials)
   {

      if (pageURL == null || pageURL.isEmpty())
      {
         throw new NullPointerException("pageUrl cannot be null or empty.");
      }

      pool.submit(() -> {
         try
         {
            int retries = 0;
            while (retries < 4)
            {
               registerSslProtocol();

               PostMethod postMethod = new PostMethod(NEW_SITEIMPROVE_BASE_URL);

               JSONObject object = new JSONObject();

               String finalURL = pageURL;

               logger.debug("canonicalDist: {}" , credentials.get("canonicalDist"));
               logger.debug("siteProtocol: {}" , credentials.get("siteProtocol"));
               logger.debug("defaultDocument: {}" , credentials.get("defaultDocument"));
               logger.debug("token: {}" , credentials.get(SITEIMPROVE_TOKEN));
               logger.debug("siteName: {}" , credentials.get("sitename"));

               if("sections".equals(credentials.get("canonicalDist")))
                  finalURL = StringUtils.replace(pageURL, credentials.get("defaultDocument"), "");

               object.accumulate("url", finalURL);
               object.accumulate(SITEIMPROVE_TOKEN, credentials.get(SITEIMPROVE_TOKEN));
               object.accumulate("type", SITEIMPROVE_RECHECK_PAGE);

               logger.debug("JSON Object body: {}" , object);

               StringRequestEntity requestEntity = null;

               requestEntity = new StringRequestEntity(object.toString(), APPLICATION_JSON, UTF_8);

               postMethod.setRequestEntity(requestEntity);

               boolean responseStatus = executeMethod(postMethod);
               if (responseStatus)
               {
                  return;
               }
               Thread.sleep(3000);
               retries++;
            }
            throw new PSSiteImproveProviderException("Failed to notify siteimprove to check page with url: " + pageURL
                  + " .  Site id is: " + siteId + " .  Exceeded retry count.");
         } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException | PSSiteImproveProviderException | IOException e) {
            logger.error(PSExceptionUtils.getMessageForLog(e));
         }catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
         }
      });
   }

   /**
    * Parse the credentials, set up the headers for the request such as auth and
    * accepts JSON. Then send request.
    *
    * @param httpMethod The http method we wish to execute, make sure it has the
    *           URI already.
    * @throws Exception The httpclient failed to execute the method.
    */
   private Boolean executeMethod(HttpMethod httpMethod) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {

      HttpClient httpClient = new HttpClient();
      registerSslProtocol();
      Header acceptsHeader = new Header(ACCEPTS, APPLICATION_JSON);
      Header contentType = new Header(CONTENT_TYPE, APPLICATION_JSON);
      httpMethod.addRequestHeader(acceptsHeader);
      httpMethod.addRequestHeader(contentType);

      int statusCode = httpClient.executeMethod(httpMethod);

      return statusCode >= 200 && statusCode <= 300;
   }

   private void registerSslProtocol() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException
   {
      String scheme = "https";
      Protocol baseHttps = Protocol.getProtocol(scheme);
      int defaultPort = HTTPS_PORT;

      ProtocolSocketFactory customFactory = new TLSSocketFactory();

      Protocol customHttps = new Protocol(scheme, customFactory, defaultPort);
      Protocol.registerProtocol(scheme, customHttps);
   }

   @Override
   public Boolean validateCredentials(Map<String, String> credentials) throws Exception
   {
      if("".equals(credentials.get(SITEIMPROVE_TOKEN)))
         return false;
      else if("".equals(credentials.get("sitename")))
         return false;
      
      if("".equals(credentials.get("siteProtocol")) || null == credentials.get("siteProtocol"))
         credentials.put("siteProtocol", DEFAULT_PROTOCOL); // default protocol to http if empty
      if("".equals(credentials.get("defaultDocument")) || null == credentials.get("defaultDocument"))
         credentials.put("defaultDocument", "index.html");
      if("".equals(credentials.get("canonicalDist")) || null == credentials.get("canonicalDist"))
         credentials.put("canonicalDist", "pages");
      return true;
   }
   
   /**
    * No need to implement as we don't need to retreive site info from backend.  It's all done 
    * from the front end Siteimprove plugin.
    */
   @Override
   public String retrieveSiteInfo(String siteName, Map<String, String> credentials) throws Exception
   {
      throw new NotImplementedException();
   }

   /**
    * No need to implement as we don't need to retreive site info from backend.  It's all done 
    * from the front end Siteimprove plugin.
    */
   @Override
   public String retrievePageInfo(String siteName, String pageURL, Map<String, String> credentials)
   {
      throw new NotImplementedException();
   }

}
