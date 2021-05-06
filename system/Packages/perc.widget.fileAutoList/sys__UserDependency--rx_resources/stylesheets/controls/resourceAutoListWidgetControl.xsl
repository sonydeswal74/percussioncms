<?xml version="1.0" encoding="UTF-8" ?>
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
  ~      https://www.percusssion.com
  ~
  ~     You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <https://www.gnu.org/licenses/>
  -->

<!DOCTYPE xsl:stylesheet [
		<!ENTITY % HTMLlat1 SYSTEM "../../../DTD/HTMLlat1x.ent">
		%HTMLlat1;
		<!ENTITY % HTMLsymbol SYSTEM "../../../DTD/HTMLsymbolx.ent">
		%HTMLsymbol;
		<!ENTITY % HTMLspecial SYSTEM "../../../DTD/HTMLspecialx.ent">
		%HTMLspecial;
		]>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:psxctl="URN:percussion.com/control" xmlns="http://www.w3.org/1999/xhtml" exclude-result-prefixes="psxi18n" xmlns:psxi18n="urn:www.percussion.com/i18n" >
	<xsl:template match="/" />
	<!--
         resourceAutoListWidgetControl
     -->
	<psxctl:ControlMeta name="resourceAutoListWidgetControl" dimension="single" choiceset="none">
		<psxctl:Description>The control for building the JCR query for the Resource Auto List Widget:</psxctl:Description>
		<psxctl:ParamList>
		</psxctl:ParamList>
		<!-- Add Extension here
		<psxctl:Dependencies>
			<psxctl:Dependency status="readyToGo" occurrence="single">
				<psxctl:Default>
					<PSXExtensionCall id="0">
						<name>Java/global/percussion/generic/sys_FileInfo</name>
					</PSXExtensionCall>
				</psxctl:Default>
			</psxctl:Dependency>
		</psxctl:Dependencies>
		-->
		<!-- CSS perc_webmgt.packed.css contains all css for jquery datepicker and Site/Save As control-->
		<psxctl:AssociatedFileList>
			<psxctl:FileDescriptor name="jquery-ui-1.8.9.custom.css" type="css" mimetype="text/css">
				<psxctl:FileLocation>../../cm/themes/smoothness/jquery-ui-1.8.9.custom.css     </psxctl:FileLocation>
				<psxctl:Timestamp/>
			</psxctl:FileDescriptor>
			<psxctl:FileDescriptor name="ui.datepicker.css" type="css" mimetype="text/css">
				<psxctl:FileLocation>../../cm/themes/smoothness/ui.datepicker.css</psxctl:FileLocation>
				<psxctl:Timestamp/>
			</psxctl:FileDescriptor>
			<psxctl:FileDescriptor name="ui.dynatree.css" type="css" mimetype="text/css">
				<psxctl:FileLocation>../../cm/css/dynatree/skin/ui.dynatree.css</psxctl:FileLocation>
				<psxctl:Timestamp/>
			</psxctl:FileDescriptor>
			<psxctl:FileDescriptor name="jquery.resourceAutoList.css" type="css" mimetype="text/css">
				<psxctl:FileLocation>../rx_resources/controls/percResourceAutoListControl/css/jquery.resourceAutoList.css</psxctl:FileLocation>
				<psxctl:Timestamp/>
			</psxctl:FileDescriptor>

			<!-- JavaScript -->
			<psxctl:FileDescriptor name="jquery-ui.js" type="script" mimetype="text/javascript">
				<psxctl:FileLocation>../../cm/jslib/jquery-ui.js</psxctl:FileLocation>
				<psxctl:Timestamp/>
			</psxctl:FileDescriptor>
			<psxctl:FileDescriptor name="PSJSUtils.js" type="script" mimetype="text/javascript">
				<psxctl:FileLocation>../../cm/jslib/PSJSUtils.js</psxctl:FileLocation>
				<psxctl:Timestamp/>
			</psxctl:FileDescriptor>
			<psxctl:FileDescriptor name="perc_path_constants.js" type="script" mimetype="text/javascript">
				<psxctl:FileLocation>../../cm/plugins/perc_path_constants.js</psxctl:FileLocation>
				<psxctl:Timestamp/>
			</psxctl:FileDescriptor>
			<psxctl:FileDescriptor name="perc_utils.js" type="script" mimetype="text/javascript">
				<psxctl:FileLocation>../../cm/plugins/perc_utils.js</psxctl:FileLocation>
				<psxctl:Timestamp/>
			</psxctl:FileDescriptor>
			<psxctl:FileDescriptor name="jquery.layout.js" type="script" mimetype="text/javascript">
				<psxctl:FileLocation>../../cm/jslib/jquery.layout.js</psxctl:FileLocation>
				<psxctl:Timestamp/>
			</psxctl:FileDescriptor>
			<psxctl:FileDescriptor name="jquery.metadata.js" type="script" mimetype="text/javascript">
				<psxctl:FileLocation>../../cm/jslib/jquery.metadata.js</psxctl:FileLocation>
				<psxctl:Timestamp/>
			</psxctl:FileDescriptor>
			<psxctl:FileDescriptor name="tools.scrollable-1.1.2.js" type="script" mimetype="text/javascript">
				<psxctl:FileLocation>../../cm/jslib/tools.scrollable-1.1.2.js</psxctl:FileLocation>
				<psxctl:Timestamp/>
			</psxctl:FileDescriptor>
			<psxctl:FileDescriptor name="tools.scrollable.mousewheel-1.0.1.js" type="script" mimetype="text/javascript">
				<psxctl:FileLocation>../../cm/jslib/tools.scrollable.mousewheel-1.0.1.js</psxctl:FileLocation>
				<psxctl:Timestamp/>
			</psxctl:FileDescriptor>
			<psxctl:FileDescriptor name="jquery.validate.js" type="script" mimetype="text/javascript">
				<psxctl:FileLocation>../../cm/jslib/jquery.validate.js</psxctl:FileLocation>
				<psxctl:Timestamp/>
			</psxctl:FileDescriptor>
			<!-- SimpleDateFormat JS-->
			<psxctl:FileDescriptor name="date.js" type="script" mimetype="text/javascript">
				<psxctl:FileLocation>../../cm/jslib/date.js</psxctl:FileLocation>
				<psxctl:Timestamp/>
			</psxctl:FileDescriptor>
			<psxctl:FileDescriptor name="perc_extend_jQueryValidate.js" type="script" mimetype="text/javascript">
				<psxctl:FileLocation>../../cm/plugins/perc_extend_jQueryValidate.js</psxctl:FileLocation>
				<psxctl:Timestamp/>
			</psxctl:FileDescriptor>

			<!-- Site Picker Control JS-->
			<psxctl:FileDescriptor name="perc_path_manager.js" type="script" mimetype="text/javascript">
				<psxctl:FileLocation>../../cm/plugins/perc_path_manager.js</psxctl:FileLocation>
				<psxctl:Timestamp/>
			</psxctl:FileDescriptor>
			<psxctl:FileDescriptor name="jquery.dynatree.js" type="script" mimetype="text/javascript">
				<psxctl:FileLocation>../../cm/jslib/jquery.dynatree.js</psxctl:FileLocation>
				<psxctl:Timestamp/>
			</psxctl:FileDescriptor>
			<psxctl:FileDescriptor name="jquery.text-overflow.js" type="script" mimetype="text/javascript">
				<psxctl:FileLocation>../../cm/jslib/jquery.text-overflow.js</psxctl:FileLocation>
				<psxctl:Timestamp/>
			</psxctl:FileDescriptor>
			<psxctl:FileDescriptor name="Jeditable.js" type="script" mimetype="text/javascript">
				<psxctl:FileLocation>../../cm/jslib/Jeditable.js</psxctl:FileLocation>
				<psxctl:Timestamp/>
			</psxctl:FileDescriptor>
			<psxctl:FileDescriptor name="PercServiceUtils.js" type="script" mimetype="text/javascript">
				<psxctl:FileLocation>../../cm/services/PercServiceUtils.js</psxctl:FileLocation>
				<psxctl:Timestamp/>
			</psxctl:FileDescriptor>
			<psxctl:FileDescriptor name="PercFinderTree.js" type="script" mimetype="text/javascript">
				<psxctl:FileLocation>../../cm/widgets/PercFinderTree.js</psxctl:FileLocation>
				<psxctl:Timestamp/>
			</psxctl:FileDescriptor>
			<psxctl:FileDescriptor name="PercExtendUiDialog.js" type="script" mimetype="text/javascript">
				<psxctl:FileLocation>../../cm/plugins/PercExtendUiDialog.js</psxctl:FileLocation>
				<psxctl:Timestamp/>
			</psxctl:FileDescriptor>
			<!-- Image Auto List Control JS-->
			<psxctl:FileDescriptor name="jquery.resource.js" type="script" mimetype="text/javascript">
				<psxctl:FileLocation>../rx_resources/controls/percResourceAutoListControl/js/jquery.resourceAutoList.js</psxctl:FileLocation>
				<psxctl:Timestamp/>
			</psxctl:FileDescriptor>
		</psxctl:AssociatedFileList>
		<psxctl:ParamList>
			<psxctl:Param name="contentTypeName" datatype="String" paramtype="generic">
				<psxctl:Description>Content type name.</psxctl:Description>
				<psxctl:DefaultValue></psxctl:DefaultValue>
			</psxctl:Param>
		</psxctl:ParamList>
	</psxctl:ControlMeta>

	<xsl:template match="Control[@name='resourceAutoListWidgetControl']" mode="psxcontrol">
		<xsl:variable name="contentTypeName">
			<xsl:choose>
				<xsl:when test="ParamList/Param[@name='contentTypeName']">
					<xsl:value-of select="ParamList/Param[@name='contentTypeName']"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select=""/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<script >

			$=$j;
			var percAssetContentTypeName = '<xsl:value-of select="$contentTypeName"/>';
			$(document).ready(function() {

			$('#display_start_date').datepicker({
			onSelect:
			function(value, date) {
			// *********************************************************************
			//This will be set from the widget config $date_format (set by velocity)
			// *********************************************************************
			$(this).focus();
			var p_start_date = new Date(value);
			var p_end_date = new Date($('[name="end_date"]').val());
			<xsl:text disable-output-escaping="yes">
				if(p_start_date > p_end_date)
                {
                    $.perc_utils.alert_dialog({title:"Error", content:"Created on or after date must be less than Created before date.", okCallBack:function(){
                        setDisplayDate($('[name="start_date"]').val(),"display_start_date");
                        return false;
                    }});
                }
                else
                {
                    $('[name="start_date"]').val(value);

                    setDisplayDate(value,"display_start_date");

                    buildQuery();
                }
								</xsl:text>
			// if the top most jquery is defined
			if($.topFrameJQuery != undefined)
			// mark the asset as dirty
			$.topFrameJQuery.PercDirtyController.setDirty(true, "asset");
			},
			showOn: 'button', buttonImage: '../rx_resources/controls/percResourceAutoListControl/images/calendar.gif', buttonImageOnly: true, altFormat: 'yy-mm-dd'
			});

			$('#display_end_date').datepicker({
			onSelect:
			function(value, date) {
			$(this).focus();
			var p_start_date = new Date($('[name="start_date"]').val());
			var p_end_date = new Date(value);
			<xsl:text disable-output-escaping="yes">
				if(p_start_date >= p_end_date)
                {
                    $j.perc_utils.alert_dialog({title:"Error", content:"Created before date must be greater than Created on or after date.", okCallBack:function(){
                        setDisplayDate($('[name="end_date"]').val(),"display_end_date");
                        return false;
                    }});
                }
                else
                {
                    $('[name="end_date"]').val(value);
                    setDisplayDate(value,"display_end_date");
                    buildQuery();
                }
								</xsl:text>
			// if the top most jquery is defined
			if($.topFrameJQuery != undefined)
			// mark the asset as dirty
			$.topFrameJQuery.PercDirtyController.setDirty(true, "asset");
			},
			showOn: 'button', buttonImage: '../rx_resources/controls/percResourceAutoListControl/images/calendar.gif', buttonImageOnly: true, altFormat: 'yy-mm-dd'
			});

			$('#perc-content-form').resourceAutoListControl({});
			});


		</script>

		<script >
			$j(document).ready(function(){
				$j("#autolist-title").click(function () {
					$j("#criteria_for_list").toggle();
					$j("#autolist-title").toggleClass("autolist-expand-image autolist-close-image");
				});
			});
		</script>
		<div id = "perc-autolist-wrapper">
			<div id = "autolist-title-wrapper">
				<div id = "autolist-title" class = "autolist-close-image"> Criteria for List Creation </div>
			</div>
			<div id="criteria_for_list">
				<div class="ui-perc-ctl_daterange">

					<table style = "line-height:17px;">
						<tr>
							<td> <label for="display_title_contains">Title contains:<br /></label>
								<input style = "width:300px" type="text" name="display_title_contains" id="display_title_contains"/></td>
							<td width = "30px"></td>
							<td> <label for="display_start_date">Created on or after:<br /></label>
								<input size = "16" type="text" name="display_start_date" id="display_start_date" class="date-pick"/></td>
							<td width = "10px"></td>
							<td><label for="display_end_date">Created before: <br /></label>
								<input size = "16" type="text" name="display_end_date" id="display_end_date" class="date-pick"/></td>
						</tr>
					</table>
				</div>
				<p></p>
				<div id = "perc-resourceautolist-detail">
					<table style = "line-height:17px;">
						<tr>
							<td>
								<span class = "autolistlabel"><label for="perc-folder-selector" style="font-weight:bold">Asset library location:</label></span>
							</td>
						</tr>
						<tr>
							<td height = "265px" valign = "top">
								<div id="perc-folder-selector">
								</div>
							</td>
						</tr>
					</table>
				</div>
				<input type="hidden" aarenderer="NONE" class="datadisplay" id="perc-content-edit-query" name="query"/>
			</div>
		</div>

	</xsl:template>
	<xsl:template match="Control[@name='resourceAutoListWidgetControl' and @isReadOnly='yes']" priority='10' mode="psxcontrol">
		<script >
	<![CDATA[
			$j(document).ready(function(){
				$j("#autolist-title").click(function () {
					$j("#criteria_for_list").toggle();
					$j("#autolist-title").toggleClass("autolist-expand-image autolist-close-image");
				});

				// Put asset library value in appropriate field
				var assetlibrarypath = $j("#perc_asset_library_path").val().substring(26);
				var splitPath = assetlibrarypath.split("/");

				$j("#perc_display_asset_library_path").text(assetlibrarypath);
			});
			]]>
   </script>
		<div id = "perc-autolist-wrapper" >
			<div id = "autolist-title-wrapper">
				<div id = "autolist-title" class = "autolist-close-image"> Criteria for List Creation </div>
			</div>
			<div id="criteria_for_list">
				<label for="display_title_contains">Title contains:</label>
				<br/>
				<div class="datadisplay" id="perc_display_title_contains"><xsl:value-of select="//DisplayField/Control[@paramName='title_contains']/Value"/></div>
				<br/>
				<label for="display_start_date">Created on or after:</label>
				<br/>
				<div class="datadisplay" id="perc_display_start_date"><xsl:value-of select="//DisplayField/Control[@paramName='start_date']/Value"/></div>
				<br/>
				<label for="display_end_date">Created before:</label>
				<br/>
				<div class="datadisplay" id="perc_display_end_date"><xsl:value-of select="//DisplayField/Control[@paramName='end_date']/Value"/></div>
				<br/>
				<label for="display_asset_library_path">Asset library location:</label>
				<br/>
				<input type="hidden" id="perc_asset_library_path">
					<xsl:attribute name="value"><xsl:value-of select="//DisplayField/Control[@paramName='asset_library_path']/Value"/></xsl:attribute>
				</input>
				<div class="datadisplay" id="perc_display_asset_library_path"></div>
			</div>
		</div>
	</xsl:template>
</xsl:stylesheet>
