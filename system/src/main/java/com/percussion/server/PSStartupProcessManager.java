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
package com.percussion.server;

import com.percussion.error.PSExceptionUtils;
import com.percussion.services.notification.IPSNotificationListener;
import com.percussion.services.notification.IPSNotificationService;
import com.percussion.services.notification.PSNotificationEvent;
import com.percussion.services.notification.PSNotificationEvent.EventType;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author JaySeletz
 *
 */
public class PSStartupProcessManager implements IPSNotificationListener, IPSStartupProcessManager
{
   private static final Logger log = LogManager.getLogger(PSStartupProcessManager.class);
   
   List<IPSStartupProcess> m_startupProcesses = new ArrayList<IPSStartupProcess>();
   
   private Properties m_startupProperties;
   
   private File m_propFile = null;

   public void setNotificationService(IPSNotificationService notificationService)
   {
      notificationService.addListener(EventType.CORE_SERVER_INITIALIZED, this);
   }
   
   public void addStartupProcess(IPSStartupProcess startupProc)
   {
      Validate.notNull(startupProc);
      m_startupProcesses.add(startupProc);
   }

   public void notifyEvent(PSNotificationEvent notification)
   {
      if (!EventType.CORE_SERVER_INITIALIZED.equals(notification.getType()))
         return;
      log.info("Loading startup properties");
      Properties startupProps = getStartupProperties();
      log.info("Running startup processes");
      try
      {
         runStartupProcesses(startupProps);
      }
      finally
      {
         // write out props we have
         log.info("Saving startup properties");
         saveStartupProperties(startupProps);
         log.info("Finished running startup processes");
      }
   }

   protected void runStartupProcesses(Properties startupProps)
   {
      for (IPSStartupProcess proc : m_startupProcesses)
      {
         try
         {
            proc.doStartupWork(startupProps);
         }
         catch (Exception e)
         {
            // log it, and throw a runtime exception to halt the server init
            String msg = "Error running startup process: " + proc.getClass().getName() + ": ";
            log.error(msg, PSExceptionUtils.getMessageForLog(e));
            log.debug(msg, e);
            throw new RuntimeException(msg);
         }
      }
   }

   public void removeStartupProcess(String process){
      Properties startupProps = getStartupProperties();
      if(startupProps != null && !StringUtils.isEmpty(process)){
         String rxFix = startupProps.getProperty("RXFIX");
         rxFix = rxFix.replace(process,"");
         startupProps.put("RXFIX",rxFix);
         saveStartupProperties(startupProps);
      }
   }

   private void saveStartupProperties(Properties startupProps)
   {
      if (m_propFile == null)
         throw new RuntimeException("Startup properties have not been initialized, cannot save");
      
      Writer writer = null;
      try
      {
         writer = new FileWriter(m_propFile);
         startupProps.store(writer, null);
      }
      catch (IOException e)
      {
         throw new RuntimeException("Failed to save startup properties: " + m_propFile, e);
      }
      finally
      {
         IOUtils.closeQuietly(writer);
      }
   }

   private Properties getStartupProperties()
   {
      if (m_startupProperties == null)
      {
         if (m_propFile != null)
            loadStartupProperties();
         else
            m_startupProperties = new Properties();
      }
      
      return m_startupProperties;
   }

   public void setStartupProperties(Properties startupProperties)
   {
      Validate.notNull(startupProperties);
      m_startupProperties = startupProperties;
   }

   /**
    * Set the path to the startup properties and read them in.
    * 
    * @param propFilePath The path, relative to the install root, not <code>null</code>, must exist and
    * reference a valid properties file.
    */
   public void setPropFilePath(String propFilePath)
   {
      m_propFile = new File(PSServer.getRxDir(), propFilePath);
   }

   protected void loadStartupProperties()
   {
      Reader reader = null;
      try
      {
         reader = new FileReader(m_propFile);
         m_startupProperties = new Properties();
         m_startupProperties.load(reader);
      }
      catch (FileNotFoundException e)
      {
         String msg = "Failed to read startup prop file: " + m_propFile.getAbsolutePath();
         throw new IllegalArgumentException(e);
      }
      catch (IOException e)
      {
         String msg = "Failed to load startup properties: " + m_propFile.getAbsolutePath();
         log.error(msg);
         throw new RuntimeException(msg, e);
      }
      finally
      {
         IOUtils.closeQuietly(reader);
      }
   }
}
