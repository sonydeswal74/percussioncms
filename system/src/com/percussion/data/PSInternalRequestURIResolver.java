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
 *      https://www.percussion.com
 *
 *     You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <https://www.gnu.org/licenses/>
 */

package com.percussion.data;

import com.percussion.design.objectstore.PSDataSet;
import com.percussion.design.objectstore.PSRequestor;
import com.percussion.security.PSAuthenticationFailedException;
import com.percussion.security.PSAuthorizationException;
import com.percussion.security.xml.IPSInternalRequestURIResolver;
import com.percussion.server.PSInternalRequest;
import com.percussion.server.PSRequest;
import com.percussion.server.PSRequestStatistics;
import com.percussion.server.PSServer;
import com.percussion.server.PSUserSession;
import com.percussion.utils.request.PSRequestInfo;
import com.percussion.xml.PSXmlDocumentBuilder;
import org.w3c.dom.Document;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PSInternalRequestURIResolver implements IPSInternalRequestURIResolver {


    /**
     * Called by the processor when it encounters
     * an xsl:include, xsl:import, or document() function.
     *
     * @param href An href attribute, which may be relative or absolute.
     * @param base The base URI against which the first argument will be made
     *             absolute if the absolute URI is required.
     * @return A Source object, or null if the href cannot be resolved,
     * and the processor should try to resolve the URI itself.
     * @throws TransformerException if an error occurs when trying to
     *                              resolve the URI.
     */
    @Override
    public Source resolve(String href, String base) throws TransformerException {
        //Deal with the internal requests here itself to avoid external requests
        //by XSLT processor
        int port = PSServer.getListenerPort();
        String baseUrl = "http://127.0.0.1";
        if(port != 80)
            baseUrl += ":" + port;

        if (href.toLowerCase().startsWith(baseUrl))
        {
            try
            {
                boolean domResult = false;

                PSInternalRequest iReq = null;
                PSRequest req = (PSRequest) PSRequestInfo
                        .getRequestInfo(PSRequestInfo.KEY_PSREQUEST);
                if(req == null)
                    throw new IllegalStateException("req must not be null");
                iReq =  PSServer.getInternalRequest(href,
                        req, null, false);
                //Internal request might be null for example, in the case of a
                //request to loadable handler.
                if (iReq == null)
                {
                    // In such a case deal the href normally
                    PSUserSession sess = req.getUserSession();
                    href = href + (href.contains("?") ? "&pssessionid=" : "?pssessionid=")
                            + sess.getId();
                    return new StreamSource(href);
                }
                String pageExt = iReq.getRequest().getRequestPageExtension();
                IPSInternalRequestHandler handler = iReq
                        .getInternalRequestHandler();
                if (pageExt.toLowerCase().equals(".xml")
                        || pageExt.toLowerCase().equals(".txt"))
                {
                    //make sure these extensions are not mapped to other mime types
                    //explicitly

                    if (handler instanceof PSQueryHandler)
                    {
                        PSQueryHandler qh = (PSQueryHandler) handler;
                        PSDataSet dset = qh.getDataSet();
                        if (dset != null)
                        {
                            PSRequestor rqr = dset.getRequestor();
                            if (rqr.getMimeType(pageExt) == null)
                                domResult = true;
                        }
                    }
                }

                if (domResult)
                {
                    Document doc = iReq.getResultDoc();

                    return new DOMSource(doc, href);
                }
                //Else return merged result
                else
                {
                    ByteArrayInputStream is;

                    try
                    {
                        if (handler instanceof PSQueryHandler)
                        {
                            try(ByteArrayOutputStream bos = iReq.getMergedResult()) {
                                is = new ByteArrayInputStream(bos.toByteArray());
                            }
                        }
                        else
                        {
                            PSRequest areq = iReq.getRequest();
                            areq.setAllowsCloning(false);
                            iReq.makeRequest();
                            PSRequestStatistics stats = areq.getStatistics();
                            Document doc = PSXmlDocumentBuilder.createXmlDocument();
                            stats.toXml(doc);
                            String output = PSXmlDocumentBuilder.toString(doc);
                            is = new ByteArrayInputStream(output.getBytes());
                        }
                        return new StreamSource(is, href);
                    }
                    catch (PSAuthorizationException | PSAuthenticationFailedException | IOException e)
                    {
                        throw new TransformerException(e);
                    }
                }
            }
            catch (PSInternalRequestCallException e)
            {
                throw new TransformerException(e);
            }
        }

        return null;
    }

    /**
     * Constant to indicate http protocol. If a reference URL string starts
     * with this, it is a HTTP url stream.
     */
    public static final String HTTP_PROTOCOL  = "http:";

    /**
     * Constant to indicate file protocol. If a reference URL string starts
     * with this, it is a file stream.
     */
    public static final String FILE_PROTOCOL = "file:";
}