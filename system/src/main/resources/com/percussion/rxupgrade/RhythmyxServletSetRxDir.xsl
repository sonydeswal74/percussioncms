<?xml version="1.0" encoding="UTF-8"?>
<!--
  ~     Percussion CMS
  ~     Copyright (C) 1999-2020 Percussion Software, Inc.
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU Affero General Public License for more details.
  ~
  ~     Mailing Address:
  ~
  ~      Percussion Software, Inc.
  ~      PO Box 767
  ~      Burlington, MA 01803, USA
  ~      +01-781-438-9900
  ~      support@percussion.com
  ~      https://www.percussion.com
  ~
  ~     You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <https://www.gnu.org/licenses/>
  -->

<xsl:stylesheet version="1.1" xmlns:j2ee="http://java.sun.com/xml/ns/j2ee"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!--
   This stylesheet is used to set the Rhythmyx directory in the "Rhythmyx"
   web application's (installed in the "AppServer/server/rx/deploy/rxapp.ear/rxapp.war/WEB-INF" directory)
   configuration file "web.xml". This web application contains the Rhythmyx
   Servlet.  If the user has selected to install the server, then he enters the value of
   the Rhythmyx installation directory in the Installshield panel whose bean id is
   "RxNewInstallDestinationPanelBeanId".
   The contents of this stylesheet is resolved during installation prior to
   applying it to the "AppServer/server/rx/deploy/rxapp.ear/rxapp.war/WEB-INF/web.xml" file.
   During resolution "$P(absoluteInstallLocation)" changes to the installation directory
   entered by the user. The stylesheet is then applied to the Xml file,
   thus setting the Rhythmyx directory appropriately.
   
   -->
	<xsl:template match="/">
		<xsl:apply-templates select="." mode="copy"/>
	</xsl:template>
	<!-- copy any attribute or template -->
	<xsl:template match="@*|*|comment()" mode="copy">
		<xsl:copy>
			<xsl:apply-templates select="@*" mode="copy"/>
			<xsl:apply-templates mode="copy"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="init-param[param-name='rxDir']/param-value | context-param[param-name='rxDir']/param-value | j2ee:param[param-name='rxDir']/param-value" mode="copy">
		<param-value>$P(absoluteInstallLocation)</param-value>
	</xsl:template>
</xsl:stylesheet>