/*
 *     Percussion CMS
 *     Copyright (C) 1999-2020 Percussion Software, Inc.
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

package com.percussion.ant.install;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * See super class
 */
public class PSUpdateTomcatPortInTables extends PSExecSQLStmt
{
   // see base class
   @Override
   public void execute()
   {
      String sqlStr = getSql();
      String patternStr = "CATALINA_PORT";
      Pattern pattern = Pattern.compile(patternStr);
      Matcher matcher = pattern.matcher(sqlStr);
      sqlStr = matcher.replaceAll(tomcatPort);
      setSql(sqlStr);
      super.execute();   
   }
   
   /**
    * @return Returns the tomcatPort.
    */
   public String getTomcatPort()
   {
      return tomcatPort;
   }
   /**
    * @param tokens The tomcatPort to set.
    */
   public void setTomcatPort(String token)
   {
      this.tomcatPort = token;
   }
   
   /**
    * Tomcat port from the tomcat panel
    */
   protected String tomcatPort = "9992";
}

