/******************************************************************************
 *
 * [ PSSiteFolderAssembly.java ]
 *
 * COPYRIGHT (c) 1999 - 2006 by Percussion Software, Inc., Woburn, MA USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Percussion.
 *
 *****************************************************************************/
package com.percussion.fastforward.sfp;

import com.percussion.cas.PSGetSiteBaseUrl;
import com.percussion.cms.PSCmsException;
import com.percussion.cms.objectstore.PSFolder;
import com.percussion.data.PSConversionException;
import com.percussion.design.objectstore.PSLocator;
import com.percussion.design.objectstore.PSUnknownNodeTypeException;
import com.percussion.extension.IPSAssemblyLocation;
import com.percussion.extension.IPSUdfProcessor;
import com.percussion.extension.PSDefaultExtension;
import com.percussion.extension.PSExtensionException;
import com.percussion.extension.PSExtensionProcessingException;
import com.percussion.fastforward.utils.PSRelationshipHelper;
import com.percussion.fastforward.utils.PSUtils;
import com.percussion.server.IPSRequestContext;
import com.percussion.server.webservices.PSServerFolderProcessor;
import com.percussion.util.IPSHtmlParameters;

import java.util.List;

import org.apache.logging.log4j.Logger;

/**
 * Extension that can be used a location scheme generator as well as a UDF to
 * generate the publish or assembly location path based on the location of the
 * item in the site folder tree. The path is generated by concatenating the
 * names of all site folders in the tree starting from root to leaf. The base
 * for the path could differ based on whether the location being is generated
 * for an item that is from the same site as the current site or a different
 * one. Three site combinations exist and the generated location for each
 * combination follows the following pattern:
 * <p>
 * <ol>
 * <li><i>Single Site with Multiple Sections: </i>An item has link to an item
 * from a different section of the same site folder tree (originating siteid and
 * current siteid are the same) then the link generated will be relative link,
 * e.g. /Marketing/page301.html</li>
 * <li><i>Two Independent Sites: </i>An item has link to an item from a section
 * of a different site folder tree (originating siteid and current siteid are
 * different) then the link generated will be absolute link, e.g.
 * http://server:port/root/Marketing/page301.html</li>
 * <li><i>Multiple Sites with a Common Root: </i>An item has a link to an item
 * from a section of a different site however, that site originated from the
 * same site folder root, or the registered base URLs for both sites have common
 * base then the link generated will be a relative link starting after the
 * common part the base URLs of two sited, e.g. consider the original site is
 * registered with a base URL of http://server:port/commonRoot/siteAroot and
 * that for teh current site is http://server:port/commonRoot/siteBroot. The
 * location generated will start with /commonRoot/...</li>
 * </ol>
 */
public class PSSiteFolderAssembly extends PSDefaultExtension
      implements
         IPSAssemblyLocation,
         IPSUdfProcessor
{
   /**
    * Implementation of the UDF interface method. Generates a publishing
    * location as described in the class description. The request context must
    * have valid {@link IPSHtmlParameters#SYS_CONTENTID contentid},
    * {@link IPSHtmlParameters#SYS_REVISION revision}and
    * {@link IPSHtmlParameters#SYS_SITEID publishing siteid}HTML parameters.
    * 
    * @param params parameters for the extension as registered by teh extension.
    * @param request request context object, must not be <code>null</code>.
    * @return location path for the item in the request context, not
    *         <code>null</code>, may be empty.
    * @throws PSConversionException
    */
   public Object processUdf(Object[] params, IPSRequestContext request)
         throws PSConversionException
   {

      String contentId = request.getParameter(IPSHtmlParameters.SYS_CONTENTID);
      String revision = request.getParameter(IPSHtmlParameters.SYS_REVISION);
      String siteId = request.getParameter(IPSHtmlParameters.SYS_SITEID);
      log.debug("Site Folder Assembly - process UDF " + contentId + " "
            + revision + " " + siteId);

      if (contentId == null || revision == null)
      {
         // no content item specified -- return an empty location
         return "";
      }

      try
      {
         return buildSiteFolderPath(contentId, revision, siteId, request);
      }
      catch (Exception e)
      {
         // the relationship API failed -- return an empty location
         log.error(this.getClass(), e);
         throw new PSConversionException(0, e.getLocalizedMessage());
      }
   }

   /**
    * Generates a publishing location by concatenating the names of all site
    * folders that contain the content item identified by the contentId and
    * revision in the current request.
    * 
    * @param params Each parameter will be concatenated to the end of the site
    *           folder path, to form the filename
    * @param request The current request, never null.
    * 
    * @return A publishing location consisting of all site folders' names that
    *         contain the specified item, plus any additional parameters to this
    *         exit. The location will be an empty string if the specified item
    *         is not contained by a site folder.
    * @throws PSExtensionException if the current request does not define a
    *            contentid and revision, or if any exception occurs working with
    *            the relationship processor or the database proxy.
    */
   public String createLocation(Object[] params, IPSRequestContext request)
         throws PSExtensionException
   {

      String contentId = request.getParameter(IPSHtmlParameters.SYS_CONTENTID);
      String revision = request.getParameter(IPSHtmlParameters.SYS_REVISION);
      if (contentId == null || revision == null)
      {
         throw new PSExtensionException(DEBUG_HDR,
               "request must supply content id and revision");
      }
      String siteId = request.getParameter(IPSHtmlParameters.SYS_SITEID, "");
      String originalSiteId = request.getParameter(
            IPSHtmlParameters.SYS_ORIGINALSITEID, "");

      StringBuffer siteFolderPath = new StringBuffer();
      log.debug("Site Folder Assembly - create location");
      try
      {
         String siteBaseUrl = "";
         //Check if it is a cross site link
         if (siteId.length() > 0 && originalSiteId.length() > 0
               && !siteId.equals(originalSiteId))
         {
            String urlCurrBase = PSGetSiteBaseUrl.getbaseUrl(siteId, request);
            String urlOrigBase = PSGetSiteBaseUrl.getbaseUrl(originalSiteId,
                  request);
            //Normalize both to end with "/"
            if (!urlCurrBase.endsWith("/"))
               urlCurrBase += "/";
            if (!urlOrigBase.endsWith("/"))
               urlOrigBase += "/";
            //Default site base url is current site base url
            siteBaseUrl = urlCurrBase;
            //If both sites' base URLs have a common host:port then we have to 
            //take  absolute root.
            int indexCurr = urlCurrBase.indexOf("//");
            int indexOrig = urlOrigBase.indexOf("//");
            if (indexCurr > -1 && indexCurr == indexOrig)
            {
               //Both have protocol hence let us check for 
               urlCurrBase = urlCurrBase.substring(indexCurr + 2);
               urlOrigBase = urlOrigBase.substring(indexCurr + 2);
               indexCurr = urlCurrBase.indexOf('/');
               indexOrig = urlOrigBase.indexOf('/');
               if (indexCurr == indexOrig)
               {
                  String hostPortCurr = urlCurrBase;
                  String hostPortOrig = urlOrigBase;
                  if (indexCurr > -1)
                  {
                     hostPortCurr = urlCurrBase.substring(0, indexCurr);
                     hostPortOrig = urlOrigBase.substring(0, indexCurr);
                  }
                  if (hostPortCurr.equals(hostPortOrig))
                  {
                     siteBaseUrl = urlCurrBase.substring(indexCurr);
                  }
               }
            }
         }
         String path = buildSiteFolderPath(contentId, revision, siteId, request);
         //Make sure to keep only one path separator
         path = (siteBaseUrl.endsWith("/") && path.startsWith("/")) ? path
               .substring(1) : path;
         siteFolderPath.append(siteBaseUrl + path);
      }
      catch (Exception e)
      {
         // the relationship API failed
         log.error(e.getMessage());
         throw new PSExtensionException(this.getClass().getName(), e
               .getLocalizedMessage());
      }

      // add the additional scheme parameters (filename)
      for (int i = 0; i < params.length; i++)
      {
         String param = PSUtils.getParameter(params, i, "");
         siteFolderPath.append(param);
      }
      return siteFolderPath.toString();
   }

   /**
    * Finds all folders that contain the specified item, pick one that is a
    * desecendent of the site's root folder, and build that folder's path.
    * 
    * @param contentId
    * @param revision
    * @param siteId
    * @param request
    * @return the path name. Never <code>null</code>, may be
    *         <code>empty</code>
    * @throws PSUnknownNodeTypeException
    * @throws PSCmsException if an error occurs while fetching the folders that
    *            contain the specified item
    * @throws PSExtensionProcessingException
    */
   private String buildSiteFolderPath(String contentId, String revision,
         String siteId, IPSRequestContext request)
         throws PSUnknownNodeTypeException, PSCmsException,
         PSExtensionProcessingException
   {

      PSLocator currentItem = new PSLocator(contentId, revision);

      log.debug("building site folder path");
      PSServerFolderProcessor folderProcessor = PSServerFolderProcessor.getInstance();
      Object pathSpec = request.getPrivateObject(PSSite.SITE_PATH_NAME);
      if (pathSpec != null)
      {
         log.debug("Using specified path " + pathSpec.toString());
         return pathSpec.toString();
      }

      if (request.getPrivateObject(PSSite.SUPPRESS_SITE_PATH_KEY) != null)
      {
         log.debug("path suppressed ");
         return "";
      }

      if (siteId == null)
      {
         log.error(DEBUG_HDR + "a site id parameter is "
               + "required, so a location was not generated");
         return "";
      }

      String rootpath = PSSite.lookupFolderRootForSite(siteId, request);
      if (rootpath == null || rootpath.trim().length() == 0)
      {
         log.error(DEBUG_HDR + "a site folder root for site id " + siteId
               + " could not be found, so a location was not generated");
         return "";
      }
      int fid = folderProcessor.getIdByPath(rootpath);
     
      if (fid <=0)
      {
         log.error("Root Folder path not found " + rootpath);
         return "";
      }

      PSLocator siteFolderLoc = getSpecifiedFolder(request);
      PSFolder siteFolder = null;
      List path = null;
      if (siteFolderLoc != null)
      { // a folder specified in the parameters,
         log.debug("folder specified " + siteFolderLoc.getId());
         
         if (fid == siteFolderLoc.getId())
         {
            // item is in the site root folder
            log.debug("item in site root");
            return "/"; // path for root is always "/"
         }
            
         path = PSSite.buildFolderPathList(fid, siteFolderLoc,
               true);
         
         if (path.isEmpty())
         {
            log.error("Specified folder, id=" + siteFolderLoc.getId()
                  + ", not in Site Tree ");
            return "";
         }
      }
      else
      {
         path = PSSite.buildFolderPathList(fid, currentItem,
               false);
         
         if (path == null)
         {
            log.error("No folders in this site contain this item, (id="
                  + currentItem.getId() + ", rev=" + currentItem.getRevision()
                  + "). Path not generated");
            return "";
         }
      }
      
      return PSSite.renderSiteFolderPathLocators(path);
   }
   
   /**
    * get the locator for the specified folder from the request context. Looks
    * for the folderid as HTML parameter {@link IPSHtmlParameters#SYS_FOLDERID}
    * first; if does not find, looks for the parameter named "rx_folder" (for
    * backward compatibility). If a non <code>null</code> and non-empty
    * folderid exists in the request a locator is constructed with revision=1
    * and returned. Otherwise, returns <code>null</code>.
    * 
    * @param req request context object, must not be <code>null</code>.
    * @return Loctaor for the folderid found in the request context object, will
    *         be <code>null</code> if a valid folderid not found.
    */
   private PSLocator getSpecifiedFolder(IPSRequestContext req)
   {
      String rxFolder = req.getParameter(IPSHtmlParameters.SYS_FOLDERID);
      if (rxFolder == null || rxFolder.trim().length() == 0
            || rxFolder.equals("-1"))
      {
         rxFolder = req.getParameter("rx_folder");
      }
      if (rxFolder != null && rxFolder.trim().length() > 0)
      {
         return new PSLocator(rxFolder, "1");
      }
      return null;
   }

   /**
    * Name of this exit, used in debugging messages.
    */
   private static final String DEBUG_HDR = "SiteFolderAssembly:: ";

   /**
    * Reference to Log4j singleton object used to log any errors or debug info.
    */
   Logger log = LogManager.getLogger(this.getClass());
}
