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

package com.percussion.ant.install;

import com.percussion.install.PSLogger;
import com.percussion.util.PSOsTool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * This task can be used to create the sample scheduled publishing script.
 *
 * <br>
 * Example Usage:
 * <br>
 * <pre>
 *
 * First set the taskdef:
 *
 *  <code>
 *  &lt;taskdef name="createScheduledPublishingScript"
 *              class="com.percussion.ant.install.PSCreateScheduledPublishingScript"
 *              classpathref="INSTALL.CLASSPATH"/&gt;
 *  </code>
 *
 * Now use the task to copy the properties.
 *
 *  <code>
 *  &lt;createScheduledPublishingScript scheduledPublishingFile="ScheduledPublication"/&gt;
 *  </code>
 *
 * </pre>
 *
 */
public class PSCreateScheduledPublishingScript extends PSAction
{
   // see base class
   @Override
   public void execute()
   {
      createScriptFile();
   }
 
   /**************************************************************************
   * private function
   **************************************************************************/
  /**
   * Creates either a batch or a shell script on the fly for scheduled publishing
   */
  
  private void createScriptFile()
  {
     String rootDir = getRootDir();
     String fileName = rootDir + "/AppServer/bin/" + m_scheduledPublishingFile;
     boolean isWinOS = PSOsTool.isWindowsPlatform();
     if ( isWinOS == true )
        fileName += BAT;
     else
        fileName += SH;
           
     File f = new File(fileName);
     
     /**
      * Make sure the directory exists
      */
     if ( f.getParentFile().exists() == false )
     {
        f.getParentFile().mkdirs();
        PSLogger.logWarn("CreateScheduledPublishing: [" + f.getParentFile()
              + "] does not exist. Cannot create file:"+fileName);
        
     }
     
     /**
      * Dont overwrite the existing file: e.g. during UPGRADE
      */
     if ( f.exists() )
     {
        PSLogger.logInfo("CreateScheduledPublishing: [" + fileName + "] " +
               "already exists");
     }
     else
     {
        try
        {
           /**
            * Create the file
            */
           f.createNewFile();
        }
        catch (IOException e1)
        {
           PSLogger.logInfo("CreateScheduledPublishing: Failed to create " +
                              "file:[" + fileName + "]. ");
        }
        StringBuilder cpBuf = new StringBuilder(MAX_PATH_LENGTH);
        /**
         * First add the windows and unix's common path
         */
        for (int i = 0; i < ms_CommonClassPath.length; i++)
        {
           cpBuf.append(ms_CommonClassPath[i]);
           cpBuf.append((isWinOS == true) ? ";" : ":");
        }

        BufferedWriter out = null;
        FileInputStream propIS = null;
    
        try
        {
           out = new BufferedWriter(new FileWriter(fileName));
           if (isWinOS)
           {
              for (int i = 0; i < ms_Usage.length; i++)
                 out.write("::" + ms_Usage[i]);
              out.write("\n\ncmd /C ..\\..\\JRE\\bin\\java -cp ");
           }
           else
           {
              out.write("#!/bin/sh\n");
              for (int i = 0; i < ms_Usage.length; i++)
                 out.write("::" + ms_Usage[i]);

              out.write("\n\n../../JRE/bin/java -cp ");
           }
           out.write(cpBuf.toString()
                       + " com.percussion.publisher.runner.PSRemotePublisher localhost ");
           String httpPort = "9992";
           try
           {
              propIS = new FileInputStream(getRootDir() + File.separator
                    + PSConfigurePort.getServerPropsLocation());
              Properties props = new Properties();
              props.load(propIS);
              String port = props.getProperty(SERVER_PORT);
              if (port != null)
                 httpPort = port;
           }
           catch (IOException ioe)
           {
              PSLogger.logError("PSCreateScheduledPublishingScript : Error "
                    + "loading http port from server properties, using default");
           }
           out.write(httpPort);
           out.write(" 301");
        }
        catch (IOException e)
        {
           PSLogger.logError("Could not write to file: " + fileName);
        }
        finally
        {
           try
           {
              if (out != null)
                 out.close();
              if (propIS != null)
                 propIS.close();
           }
           catch (IOException ioe)
           {
           }
        }
     }
     return;
  }
 
  /**
   * Sets the publishing script file name.
   * 
   * @param file see {@link #m_scheduledPublishingFile}.
   */
  public void setScheduledPublishingFile(String file)
  {
     m_scheduledPublishingFile = file;
  }
  
 /**************************************************************************
 * Bean properties
 **************************************************************************/
  
  /**
   * A string value for the scheduled publishing file name. An extension
   * is tagged on later based on the OS: if WinOS, then .bat else .sh.
   */
  private String m_scheduledPublishingFile="ScheduledPublication";
  
  /**
   * Extension for windows script file
   */
  private static final String SH=".sh";
  
  /**
   * Extension for any UNIX script file
   */
  private static final String BAT=".bat";
  
  /**
   * CLASSPATH is different for windows and unixes
   */
  private static final String[] ms_CommonClassPath=
                       {
                          "../server/rx/deploy/rxapp.ear/rxapp.war/WEB-INF/lib/rxpublisher.jar",
                          "../server/rx/deploy/rxapp.ear/rxapp.war/WEB-INF/lib/rxmisctools.jar"
                       };
  
  /**
   * Usage info
   */
  private static final String ms_Usage[] = {
     "A sample ScheduledPublication script with hostname, port and editionID\n",
     "is generated. \n",
     "You should be able to modify this file for your convenience. \n\n\n",
     "Usage: \n",
     "\t\tAt the end of the launch command below, (after com.percussion.publisher.runner.PSRemotePublisher),\n",
     "\t\tare the following arguments:\n\n",
     "\t\t<server> <port> <editionid>\n\n", 
     "\t\tModify these arguments as needed.  The last two arguments are optional."
  };

  /**
   * Maximum classpath length, currently very small ( currently: <256 )
   */
  private static final int MAX_PATH_LENGTH = 1024;
   
  /**
   * the server properties file(server.properties) holds the port info
   */
  private static final String SERVER_PORT = "bindPort";
}






