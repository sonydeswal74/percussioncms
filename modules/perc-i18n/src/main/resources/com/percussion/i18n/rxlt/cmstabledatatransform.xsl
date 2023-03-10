<?xml version="1.0" encoding="UTF-8"?>


<!-- This stylesheet generates the translation keys out of all CMS tables. The input
XML document for this stylesheet will be generated by JDBC Table factory -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:psx="urn:www.percussion.com/i18n/cmstables">
   <!-- List of all CMS tables to be processed. Every table listed here must have
	a template implemented, otherwise keys will not be generated. The template implementation
	follows the schem to generated the translation keys and descriptions for corresponding table -->
   <psx:tables>
      <psx:table name="WORKFLOWAPPS"/>
      <psx:table name="TRANSITIONS"/>
      <psx:table name="STATES"/>
      <psx:table name="RXSYSCOMPONENT"/>
      <psx:table name="RXSLOTTYPE"/>
      <psx:table name="RXMENUACTION"/>
      <psx:table name="RXLOOKUP"/>
      <psx:table name="RXCOMMUNITY"/>
      <psx:table name="RXLOCALE"/>
      <psx:table name="PSX_TEMPLATE"/>
      <psx:table name="CONTENTTYPES"/>
      <psx:table name="PSX_ROLES"/>
      <psx:table name="RXSITES"/>
      <psx:table name="PSX_SEARCHFIELDS"/>
      <psx:table name="PSX_SEARCHES"/>
      <psx:table name="PSX_DISPLAYFORMATS"/>
      <psx:table name="PSX_DISPLAYFORMATCOLUMNS"/>
      <psx:table name="PSX_RXCONFIGURATIONS"/>
   </psx:tables>
   <!-- Main template. Entry point -->
   <xsl:template match="/">
      <keys>
         <xsl:apply-templates select="//table" mode="generatekeys"/>
      </keys>
   </xsl:template>
   <!-- Ignore any other table in the document -->
   <xsl:template match="table" mode="generatekeys"/>
   <!-- WORKFLOWAPPS-->
   <xsl:template match="table[@name='WORKFLOWAPPS']" mode="generatekeys" priority="10">
      <xsl:for-each select="row">
         <key>
            <xsl:attribute name="name"><xsl:value-of select="concat('psx.workflow.workflow.', column[@name='WORKFLOWAPPID'], '@', column[@name='WORKFLOWAPPNAME'])"/></xsl:attribute>
            <xsl:value-of select="column[@name='WORKFLOWAPPDESC']"/>
         </key>
      </xsl:for-each>
   </xsl:template>
   <!-- TRANSITIONS-->
   <xsl:template match="table[@name='TRANSITIONS']" mode="generatekeys" priority="10">
      <xsl:for-each select="row">
         <key>
            <xsl:attribute name="name"><xsl:value-of select="concat('psx.workflow.transition.', column[@name='WORKFLOWAPPID'], '.', column[@name='TRANSITIONID'], '@', column[@name='TRANSITIONLABEL'])"/></xsl:attribute>
            <xsl:value-of select="column[@name='TRANSITIONDESC']"/>
         </key>
      </xsl:for-each>
   </xsl:template>
   <!-- STATES-->
   <xsl:template match="table[@name='STATES']" mode="generatekeys" priority="10">
      <xsl:for-each select="row">
         <key>
            <xsl:attribute name="name"><xsl:value-of select="concat('psx.workflow.state', '@', column[@name='STATENAME'])"/></xsl:attribute>
            <!-- We do not use STATDESC as note since State Name is like a keyword not specific to a worklfow and/or state -->
            <xsl:value-of select="'Name of a state in the workflow'"/>
         </key>
      </xsl:for-each>
   </xsl:template>
   <!-- RXSYSCOMPONENT -->
   <xsl:template match="table[@name='RXSYSCOMPONENT']" mode="generatekeys" priority="10">
      <xsl:for-each select="row">
         <key>
            <xsl:attribute name="name"><xsl:value-of select="concat('psx.component.', column[@name='NAME'], '@', column[@name='DISPLAYNAME'])"/></xsl:attribute>
            <xsl:value-of select="column[@name='DESCRIPTION']"/>
         </key>
      </xsl:for-each>
   </xsl:template>
   <!-- RXSLOTTYPE-->
   <xsl:template match="table[@name='RXSLOTTYPE']" mode="generatekeys" priority="10">
      <xsl:for-each select="row">
         <key>
            <xsl:attribute name="name"><xsl:value-of select="concat('psx.slot@', column[@name='SLOTNAME'])"/></xsl:attribute>
            <xsl:value-of select="column[@name='SLOTDESC']"/>
         </key>
      </xsl:for-each>
   </xsl:template>
   <!-- RXMENUACTION-->
   <xsl:template match="table[@name='RXMENUACTION']" mode="generatekeys" priority="10">
      <xsl:for-each select="row">
         <key>
            <xsl:attribute name="name"><xsl:value-of select="concat('psx.menuitem.', column[@name='NAME'], '@', column[@name='DISPLAYNAME'])"/></xsl:attribute>
            <xsl:value-of select="column[@name='DESCRIPTION']"/>
         </key>
      </xsl:for-each>
   </xsl:template>
   <!-- RXLOOKUP-->
   <xsl:template match="table[@name='RXLOOKUP']" mode="generatekeys" priority="10">
      <xsl:for-each select="row">
         <key>
            <xsl:attribute name="name"><xsl:value-of select="concat('psx.keyword.', column[@name='LOOKUPID'], '@', column[@name='LOOKUPDISPLAY'])"/></xsl:attribute>
            <xsl:value-of select="column[@name='DESCRIPTION']"/>
         </key>
      </xsl:for-each>
   </xsl:template>
   <!-- RXCOMMUNITY -->
   <xsl:template match="table[@name='RXCOMMUNITY']" mode="generatekeys" priority="10">
      <xsl:for-each select="row">
         <key>
            <xsl:attribute name="name"><xsl:value-of select="concat('psx.community@', column[@name='NAME'])"/></xsl:attribute>
            <xsl:value-of select="column[@name='DESCRIPTION']"/>
         </key>
      </xsl:for-each>
   </xsl:template>
   <!-- ROLES -->
   <xsl:template match="table[@name='ROLES']" mode="generatekeys" priority="10">
      <xsl:for-each select="row">
         <key>
            <xsl:attribute name="name"><xsl:value-of select="concat('psx.role@', column[@name='ROLENAME'])"/></xsl:attribute>
            <xsl:value-of select="column[@name='ROLEDESC']"/>
         </key>
      </xsl:for-each>
   </xsl:template>
   <!-- RXLOCALE -->
   <xsl:template match="table[@name='RXLOCALE']" mode="generatekeys" priority="10">
      <xsl:for-each select="row">
         <key>
            <xsl:attribute name="name"><xsl:value-of select="concat('psx.locale.', column[@name='LANGUAGESTRING'], '@', column[@name='DISPLAYNAME'])"/></xsl:attribute>
            <xsl:value-of select="column[@name='DESCRIPTION']"/>
         </key>
      </xsl:for-each>
   </xsl:template>
   <!-- CONTENTVARIANTS -->
   <xsl:template match="table[@name='PSX_TEMPLATE']" mode="generatekeys" priority="10">
      <xsl:for-each select="row">
         <key>
            <xsl:attribute name="name"><xsl:value-of select="concat('psx.variant.', column[@name='TEMPLATE_ID'], '@', column[@name='LABEL'])"/></xsl:attribute>
            <xsl:value-of select="column[@name='DESCRIPTION']"/>
         </key>
      </xsl:for-each>
   </xsl:template>
   <!-- CONTENTTYPES -->
   <xsl:template match="table[@name='CONTENTTYPES']" mode="generatekeys" priority="10">
      <xsl:for-each select="row">
         <key>
            <xsl:attribute name="name"><xsl:value-of select="concat('psx.contenttype.', column[@name='CONTENTTYPEID'], '@', column[@name='CONTENTTYPENAME'])"/></xsl:attribute>
            <xsl:value-of select="column[@name='CONTENTTYPEDESC']"/>
         </key>
      </xsl:for-each>
   </xsl:template>
   <!-- RXSITES -->
   <xsl:template match="table[@name='RXSITES']" mode="generatekeys" priority="10">
      <xsl:for-each select="row">
         <key>
            <xsl:attribute name="name"><xsl:value-of select="concat('psx.site.name.', column[@name='SITEID'], '@', column[@name='SITENAME'])"/></xsl:attribute>
            <xsl:value-of select="column[@name='SITEDESC']"/>
         </key>
         <key>
            <xsl:attribute name="name"><xsl:value-of select="concat('psx.site.description.', column[@name='SITEID'], '@', column[@name='SITEDESC'])"/></xsl:attribute>
            <xsl:value-of select="'Description of the site that is displayed in a view'"/>
         </key>
      </xsl:for-each>
   </xsl:template>
   <!-- PSX_ROLES -->
   <xsl:template match="table[@name='PSX_ROLES']" mode="generatekeys" priority="10">
      <xsl:for-each select="row">
         <key>
            <xsl:attribute name="name"><xsl:value-of select="concat('psx.role@', column[@name='NAME'])"/></xsl:attribute>
            <xsl:value-of select="'Server Role Name'"/>
         </key>
      </xsl:for-each>
   </xsl:template>
   <!-- CONTENTEXPLORER -->
   <xsl:template match="table[@name='PSX_SEARCHFIELDS']" mode="generatekeys" priority="10">
      <xsl:for-each select="row">
         <key>
            <xsl:attribute name="name"><xsl:value-of select="concat('psx.contentexplorer.searchfields.', column[@name='FIELDNAME'], '@', column[@name='FIELDLABEL'])"/></xsl:attribute>
            <xsl:value-of select="column[@name='FIELDESCRIPTION']"/>
         </key>
      </xsl:for-each>
   </xsl:template>
   <xsl:template match="table[@name='PSX_SEARCHES']" mode="generatekeys" priority="10">
      <xsl:for-each select="row">
         <key>
            <xsl:attribute name="name"><xsl:value-of select="concat('psx.contentexplorer.searches.', column[@name='INTERNALNAME'], '@', column[@name='DISPLAYNAME'])"/></xsl:attribute>
            <xsl:value-of select="column[@name='DESCRIPTION']"/>
         </key>
      </xsl:for-each>
   </xsl:template>
   <xsl:template match="table[@name='PSX_DISPLAYFORMATS']" mode="generatekeys" priority="10">
      <xsl:for-each select="row">
         <key>
            <xsl:attribute name="name"><xsl:value-of select="concat('psx.contentexplorer.displayformats.', column[@name='INTERNALNAME'], '@', column[@name='DISPLAYNAME'])"/></xsl:attribute>
            <xsl:value-of select="column[@name='DESCRIPTION']"/>
         </key>
      </xsl:for-each>
   </xsl:template>
   <xsl:template match="table[@name='PSX_DISPLAYFORMATCOLUMNS']" mode="generatekeys" priority="10">
      <xsl:for-each select="row">
         <key>
            <xsl:attribute name="name"><xsl:value-of select="concat('psx.contentexplorer.displayformatcolumns.', column[@name='SOURCE'], '@', column[@name='DISPLAYNAME'])"/></xsl:attribute>
            <xsl:value-of select="column[@name='DESCRIPTION']"/>
         </key>
      </xsl:for-each>
   </xsl:template>
   <xsl:template match="table[@name='PSX_RXCONFIGURATIONS']" mode="generatekeys" priority="10">
      <xsl:for-each select="//PSXRelationshipConfigSet">
         <xsl:variable name="relationshiptype" select="@name"/>
         <xsl:for-each select="PSXRelationshipConfig">
            <key>
               <xsl:attribute name="name"><xsl:value-of select="concat('psx.', $relationshiptype, '.', @name, '@', @label)"/></xsl:attribute>
               <xsl:text>One of the relationship types configured in the System</xsl:text>
            </key>
         </xsl:for-each>
      </xsl:for-each>
   </xsl:template>
</xsl:stylesheet>