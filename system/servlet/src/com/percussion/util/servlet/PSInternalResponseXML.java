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
package com.percussion.util.servlet;

import com.percussion.error.PSExceptionUtils;
import com.percussion.security.xml.PSSecureXMLUtils;
import com.percussion.security.xml.PSXmlSecurityOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
/**
 * Extends {@link PSInternalResponse} to support requests that return
 * XML documents. This can be particularly useful if the target of
 * the request is the Rhythmyx server.
 *
 * This class assumes that a valid JAXP parser has been configured
 * by the container environment, and that the XML document will be
 * parsed <b>without</b> validation.
 *
 * @author DavidBenua
 */
class PSInternalResponseXML
   extends PSInternalResponse
   implements HttpServletResponse
{
   /**
    * Constructs a new response object based on an original response.
    *
    * @param response the original response to wrap.
    */
   public PSInternalResponseXML(HttpServletResponse response)
   {
      super(response);
   }

   /**
    * Gets the result buffer as an XML DOM object.
    *
    * @return the XML document as a DOM object, never <code>null</code>.
    *
    * @throws ParserConfigurationException when there is no JAXP
    * Parser configured in the environment.
    *
    * @throws SAXException when the XML document cannot be parsed.
    * This may happen because the called servlet returned an error
    * document, or because of missing entity definitions, or other XML
    * problems. The buffer can still be obtained with
    * <code>getString</code>.
    *
    * @throws IOException when the XML Parser cannot read the document.
    * This should never happen on the main document (it's in memory), but
    * if there are entity or document type definitions that cannot be
    * resolved, an IOException may result.
    */
   public Document getDocument()
      throws ParserConfigurationException, SAXException, IOException
   {
      InputSource source = null;
      if (isStreamUsed())
      {
         source = new InputSource(this.getInputStream());
         log.debug("using input stream");
      }
      if (isWriterUsed())
      {
         Reader sr = new StringReader(this.getString());
         source = new InputSource(sr);
         log.debug("using string reader");
      }
      DocumentBuilder builder = getBuilder(log);
      try
      {
         return builder.parse(source);
      }
      catch (IOException e)
      {
         throw e;
      }
      catch (SAXException e)
      {
         throw new SAXException(e.getLocalizedMessage() + " Source String: "
                  + this.getString());
      }
   }

   /**
    * Obtains a document builder for the default parser.
    * This method is synchronized to provide a separate builder for
    * each thread. The <code>DocumentBuilderFactory</code> class
    * is not thread-safe.
    *
    * @param myLogger the caller's logger.
    * @return the document builder
    * @throws ParserConfigurationException when there is no XML
    * parser
    */
   private synchronized static DocumentBuilder getBuilder(Logger myLogger)
      throws ParserConfigurationException
   {
      try
      {
         return ms_dbf.newDocumentBuilder();
      }
      catch (ParserConfigurationException e)
      {
         myLogger.error("Invalid XML Parser {}", PSExceptionUtils.getMessageForLog(e));
         log.error(PSExceptionUtils.getMessageForLog(e));
         log.debug(PSExceptionUtils.getDebugMessageForLog(e));
         throw (ParserConfigurationException) e.fillInStackTrace();
      }
   }

   /**
    * our private logger. Never <code>null</code>.
    */
   private static final Logger log = LogManager.getLogger(PSInternalResponseXML.class);

   /**
    * The factory instance for the XML parser. There is only one
    * factory instance across all instances of this class.
    */
   private static DocumentBuilderFactory ms_dbf;

   /**
    * retrieves the document builder factory instance.
    */
   static {
      ms_dbf = PSSecureXMLUtils.getSecuredDocumentBuilderFactory(
              new PSXmlSecurityOptions(
                      true,
                      true,
                      true,
                      false,
                      true,
                      false
              ));

      ms_dbf.setNamespaceAware(true);
      ms_dbf.setValidating(false);
   }
}
