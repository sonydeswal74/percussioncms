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
package com.percussion.server.command;

import com.percussion.error.PSErrorManager;
import com.percussion.error.PSException;
import com.percussion.i18n.PSTmxResourceBundle;
import com.percussion.server.IPSServerErrors;
import com.percussion.server.PSRemoteConsoleHandler;
import com.percussion.server.PSRequest;
import com.percussion.xml.PSXmlDocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Locale;


/**
 * The PSConsoleCommandReloadI18nResources class implements processing of the
 * "reload i18nresources" console command.
 *
 * @see PSRemoteConsoleHandler
 */
public class PSConsoleCommandReloadI18nResources 
   extends PSConsoleCommand
{
   /**
    * The constructor for this class. The command arguments are ignored for this
    * command.
    *
    * @param cmdArgs   the argument string to use when executing   this command, may
    * be <code>null</code> or empty.
    */
   public PSConsoleCommandReloadI18nResources(String cmdArgs)
   {
      super(cmdArgs);
   }

   /**
    * Execute the command specified by this object. The results are returned
    * as an XML document of the appropriate structure for the command.
    *   <P>
    * The execution of this command results in the following XML document
    * structure:
    * <PRE><CODE>
    *      &lt;ELEMENT PSXConsoleCommandResults   (command, resultCode, resultText)&gt;
    *      &lt;--
    *         the command that was executed
    *      --&gt;
    *      &lt;ELEMENT command (#PCDATA)&gt;
    *      &lt;--
    *         the result code for the command execution
    *      --&gt;
    *      &lt;ELEMENT resultCode (#PCDATA)&gt;
    *      &lt;--
    *         the message text associated with the result code
    *      --&gt;
    *      &lt;ELEMENT resultText (#PCDATA)&gt;
    * </CODE></PRE>
    *
    * @param request the requestor object, may be <code>null</code>
    * @return the result document, never <code>null</code>
    * @throws PSConsoleCommandException if an error occurs during execution
    */
   public Document execute(PSRequest request)
      throws PSConsoleCommandException
   {
      Document respDoc = PSXmlDocumentBuilder.createXmlDocument();
      Element root = PSXmlDocumentBuilder.createRoot(
         respDoc, "PSXConsoleCommandResults");
      PSXmlDocumentBuilder.addElement(
         respDoc, root, "command", ms_cmdName + " " + m_cmdArgs);

      Locale loc;
      if (request != null)
         loc = request.getPreferredLocale();
      else
         loc = Locale.getDefault();

      try
      {
         // reload the TMX resources
         PSTmxResourceBundle.getInstance().loadResources();
         PSXmlDocumentBuilder.addElement(respDoc, root, "resultCode",
            String.valueOf(IPSServerErrors.RCONSOLE_I18NRESOURCES_RELOADED));
         Object[] args = { m_cmdArgs };
         String termMsg = PSErrorManager.createMessage(
            IPSServerErrors.RCONSOLE_I18NRESOURCES_RELOADED, args, loc);
         PSXmlDocumentBuilder.addElement(respDoc, root, "resultText", termMsg);
      } catch (Exception e) {
         String msg;
         if (e instanceof com.percussion.error.PSException)
            msg = ((PSException)e).getLocalizedMessage(loc);
         else
            msg = e.getMessage();

         Object[] args = { (ms_cmdName + " " + m_cmdArgs), msg };
         throw new PSConsoleCommandException(
            IPSServerErrors.RCONSOLE_EXEC_EXCEPTION, args);
      }

      return respDoc;
   }


   /**
    * The command executed by this class.
    */
   final static String   ms_cmdName = "reload i18nresources";
}

