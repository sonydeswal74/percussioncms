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
import com.percussion.util.PSCharSets;
import com.percussion.util.PSStringOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * This class is used to POST parameters in the multi-part form.
 *
 * @author DavidBenua
 */
class PSInternalRequestMultiPart
   extends PSInternalRequest
   implements HttpServletRequest
{

   private static final Logger log = LogManager.getLogger(PSInternalRequestMultiPart.class);

   /**
    * Constructs an instance from a given servlet request.
    *
    * @param req The original servlet request object.
    */
   public PSInternalRequestMultiPart(HttpServletRequest req)
   {
      super(req);
      this.setMethod("POST");
   }

   /**
    * Empty the body. It is used when forwarding the request instance to
    * another servlet within the same container where we don't have to
    * re-package the same set of parameters into the body, and to avoid the 
    * target servlet receiving the same set of parameters twice.
    */
   public void emptyBody()
   {
      m_bos = new ByteArrayOutputStream();
      m_prepared = true;
      setContentLength(); // has to call this last
   }
   
   /**
    * Prepare the body and/or header of the current parameters. This method
    * must be called before pass this object to another servlet, for example
    * through {@link javax.servlet.RequestDispatcher}.
    */
   public void prepareBody()
   {
      PSMultipartWriter httpWriter = null;
      try
      {
         boolean hasContent = false;
         try(ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            m_bos = bos;
            httpWriter = new PSMultipartWriter(m_bos, getBodyEncoding());

            Enumeration pNames = this.getParameterNames();
            if (pNames.hasMoreElements())
               hasContent = true;
            while (pNames.hasMoreElements()) {
               String pName = (String) pNames.nextElement();
               ArrayList pValues =
                       new ArrayList(Arrays.asList(this.getParameterValues(pName)));
               Iterator pIter = pValues.iterator();
               while (pIter.hasNext()) {
                  String pValue = (String) pIter.next();
                  httpWriter.addField(pName, pValue);
               }
            }
            Iterator bodyParts = m_bodyParts.iterator();
            if (bodyParts.hasNext())
               hasContent = true;
            while (bodyParts.hasNext()) {
               PSHttpBodyPart bPart = (PSHttpBodyPart) bodyParts.next();
               httpWriter.addBytes(
                       bPart.getFieldName(),
                       bPart.getFileName(),
                       bPart.getMimeType(),
                       bPart.getEncoding(),
                       bPart.getBytes());
            }
            if (hasContent)
               httpWriter.addEndMarker();
         }
      }
      catch (UnsupportedEncodingException e)
      {
         log.error(PSExceptionUtils.getMessageForLog(e));
         log.debug(PSExceptionUtils.getDebugMessageForLog(e));
      }
      catch (IOException ioe)
      {
         log.error(ioe.getMessage());
         log.debug(ioe.getMessage(), ioe);
      }
      m_prepared = true;
      // set header for "Content-Type" and "content-length"
      // this has to be called after set "m_prepared"
      setContentLength();
      String contentType =
         "multipart/form-data; charset="
            + PSCharSets.getStdName(getBodyEncoding())
            + "; boundary="
            + httpWriter.getSeparator();
      setContentType(contentType);
   }

   // see javax.servlet.ServletRequest#getInputStream()
   public ServletInputStream getInputStream()
   {
      if (!m_prepared)
      {
         prepareBody();
      }
      if (m_reader)
      {
         throw new IllegalStateException();
      }
      m_stream = true;
        try(ServletInputStream res =
            (ServletInputStream) new InternalInputStream(new ByteArrayInputStream(m_bos
               .toByteArray()))){
         return res;
      } catch (IOException e) {
           throw new IllegalStateException();
        }
   }

   /**
    * Get the encoding of the body.
    *
    * @return character encoding, it is default to UTF8 if the encoding
    *    is not defined.
    */
   private String getBodyEncoding()
   {
      String encoding = getCharacterEncoding();
      if (encoding == null)
      {
         encoding = PSCharSets.rxJavaEnc();
      }
      else
      {
         // strip double quote if exist. Some app server such as
         // WebSphere 5.1 will have "\"" at the beginning and end.
         encoding = PSStringOperation.replace(encoding, "\"", "");
         encoding = PSCharSets.getJavaName(encoding);
      }
      return encoding;
   }

   /**
    * Set the "content-type" header to the given value.
    *
    * @param contentType The to be set value of the content type, assume
    *    it is not <code>null</code> or empty.
    */
   private void setContentType(String contentType)
   {
      m_contentType = contentType;
      // has to set with lower case, CANNOT BE "Content-Type";
      // otherwise, RhythmyxServlet will send out both headers below, which
      // will confuse Rhythmyx Server:
      //
      //       content-type: text/xml
      //       Content-Type: XXXX
      //
      setHeader("content-type", contentType);
   }

   /**
    * Set the "content-length" header to be the length of the "prepared" body.
    */
   private void setContentLength()
   {
      // has to set with lower case, CANNOT BE "Content-Length";
      // otherwise, RhythmyxServlet will send out both headers below, which
      // will confuse Rhythmyx Server:
      //
      //       content-length: 0
      //       Content-Length: XXXX
      //
      setHeader("content-length", String.valueOf(getContentLength()));
   }

   // see javax.servlet.ServletRequest#getContentType()
   public String getContentType()
   {
      if (m_contentType == null)
         return super.getContentType();
      else
         return m_contentType;
   }

   // see javax.servlet.ServletRequest#getContentLength()
   public int getContentLength()
   {
      if (!m_prepared)
      {
         prepareBody();
      }
      return m_bos.size();
   }

   // see javax.servlet.ServletRequest#getReader()
   public BufferedReader getReader() throws IOException
   {
      if (!m_prepared)
      {
         prepareBody();
      }
      if (m_stream)
      {
         throw new IllegalStateException("It is operated in stream mode, not in text mode");
      }
      m_reader = true;
      try(InputStream is = new ByteArrayInputStream(m_bos.toByteArray())) {
         InputStreamReader ir =
                 new InputStreamReader(is, getBodyEncoding());
         BufferedReader br = new BufferedReader(ir);
         return br;
      }
   }

   /**
    * Add the given body part.
    *
    * @param bPart the body part, it may not be <code>null</code>.
    */
   public void addBodyPart(PSHttpBodyPart bPart)
   {
      if (bPart == null)
         throw new IllegalArgumentException("bPart may not be null");
      m_bodyParts.add(bPart);
   }

   /**
    * The output byte array which contains the POST content. It is initialized
    * by {@link #prepareBody()}. It is <code>null</code> if not initialized
    * yet.
    */
   private ByteArrayOutputStream m_bos = null;

   /**
    * The content type is set by {@link #setContentType(String)}. It is
    * <code>null</code> if has not set yet.
    */
   private String m_contentType = null;

   /**
    * <code>true</code> if getting the response info it operated in stream mode.
    * It is set by the {@link #getInputStream()}.
    */
   private boolean m_stream = false;

   /**
    * <code>true</code> if getting the response info it operated in text mode.
    * It is set by the {@link @getReader()}.
    */
   private boolean m_reader = false;

   /**
    * <code>true</code> if the POST body has been prepared for send. It is
    * set by {@link #prepareBody()}.
    */
   private boolean m_prepared = false;

   /**
    * Holds a list of <code>PSHttpBodyPart</code> objects, never
    * <code>null</code>, but may be empty.
    */
   private List m_bodyParts = new ArrayList();


   /**
    * A concrete instance of ServletInputStream. This class is need to
    * fully implement the HttpServletResponse interface.
    *
    * @author DavidBenua
    */
private class InternalInputStream extends ServletInputStream
   {
      /**
      * create our stream from an existing byte array
      *
      * @param bis The byte array, may not be <code>null</code>.
      */
      public InternalInputStream(ByteArrayInputStream bis)
      {
        super();
        if (bis == null)
          throw new IllegalArgumentException("bis may not be null");
        m_bis = bis;
      }

      @Override
      public int available() {
         return m_bis.available();
      }

      /**
      * read the input stream
      *
      * @see java.io.InputStream#read()
      */
      public int read() throws IOException
      {
        return m_bis.read();
      }

      /**
      * The stream that backs our stream.
      */
      private ByteArrayInputStream m_bis;

      /**
       * Returns true when all the data from the stream has been read else
       * it returns false.
       *
       * @return <code>true</code> when all data for this particular request
       * has been read, otherwise returns <code>false</code>.
       * @since Servlet 3.1
       */
      @Override
      public boolean isFinished() {
         return false;
      }

      /**
       * Returns true if data can be read without blocking else returns
       * false.
       *
       * @return <code>true</code> if data can be obtained without blocking,
       * otherwise returns <code>false</code>.
       * @since Servlet 3.1
       */
      @Override
      public boolean isReady() {
         return false;
      }

      /**
       * Instructs the <code>ServletInputStream</code> to invoke the provided
       * {@link ReadListener} when it is possible to read
       *
       * @param readListener the {@link ReadListener} that should be notified
       *                     when it's possible to read.
       * @throws IllegalStateException if one of the following conditions is true
       *                               <ul>
       *                               <li>the associated request is neither upgraded nor the async started
       *                               <li>setReadListener is called more than once within the scope of the same request.
       *                               </ul>
       * @throws NullPointerException  if readListener is null
       * @since Servlet 3.1
       */
      @Override
      public void setReadListener(ReadListener readListener) {
         //TODO: Implement me
         throw new RuntimeException("Not yet implemented");
      }
   }


}
