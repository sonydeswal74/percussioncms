<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE xsl:stylesheet [
        <!ENTITY % HTMLlat1 PUBLIC "-//W3C//ENTITIES_Latin_1_for_XHTML//EN" "https://www.percussion.com/DTD/HTMLlat1x.ent">
        %HTMLlat1;
        <!ENTITY % HTMLsymbol PUBLIC "-//W3C//ENTITIES_Symbols_for_XHTML//EN" "https://www.percussion.com/DTD/HTMLsymbolx.ent">
        %HTMLsymbol;
        <!ENTITY % HTMLspecial PUBLIC "-//W3C//ENTITIES_Special_for_XHTML//EN" "https://www.percussion.com/DTD/HTMLspecialx.ent">
        %HTMLspecial;
        ]>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:psxctl="urn:percussion.com/control"
                xmlns="http://www.w3.org/1999/xhtml" xmlns:psxi18n="com.percussion.i18n"
                extension-element-prefixes="psxi18n" exclude-result-prefixes="psxi18n">

    <xsl:template match="/" />
    <!--
         pageAutoListWidgetControl
     -->
    <psxctl:ControlMeta name="pageAutoListWidgetControl" dimension="single" choiceset="none">
        <psxctl:Description>The control for building the JCR query for the Page Auto List Widget:</psxctl:Description>
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
            <psxctl:FileDescriptor name="PercDatetimePicker.css" type="css" mimetype="text/css">
                <psxctl:FileLocation>/cm/css/PercDatetimePicker.css</psxctl:FileLocation>
                <psxctl:Timestamp/>
            </psxctl:FileDescriptor>
            <psxctl:FileDescriptor name="jquery-ui-1.8.9.custom.css     " type="css" mimetype="text/css">
                <psxctl:FileLocation>/cm/themes/smoothness/jquery-ui-1.8.9.custom.css     </psxctl:FileLocation>
                <psxctl:Timestamp/>
            </psxctl:FileDescriptor>
            <psxctl:FileDescriptor name="ui.datepicker.css" type="css" mimetype="text/css">
                <psxctl:FileLocation>/cm/themes/smoothness/ui.datepicker.css</psxctl:FileLocation>
                <psxctl:Timestamp/>
            </psxctl:FileDescriptor>

            <psxctl:FileDescriptor name="perc_webmgt.packed.css" type="css" mimetype="text/css">
                <psxctl:FileLocation>/cm/css/dynatree/skin/ui.dynatree.css</psxctl:FileLocation>
                <psxctl:Timestamp/>
            </psxctl:FileDescriptor>

            <psxctl:FileDescriptor name="perc_webmgt.packed.css" type="css" mimetype="text/css">
                <psxctl:FileLocation>/rx_resources/widgets/pageAutoList/css/jquery.pageAutoList.css</psxctl:FileLocation>
                <psxctl:Timestamp/>
            </psxctl:FileDescriptor>

            <psxctl:FileDescriptor name="tree.css" type="css" mimetype="text/css">
                <psxctl:FileLocation>/sys_resources/css/checkboxTree/tree.css</psxctl:FileLocation>
                <psxctl:Timestamp/>
            </psxctl:FileDescriptor>

            <!-- JavaScript -->
            <psxctl:FileDescriptor name="jquery-ui.js" type="script" mimetype="text/javascript">
                <psxctl:FileLocation>/cm/jslib/profiles/3x/jquery/libraries/jquery-ui/jquery-ui.js</psxctl:FileLocation>
                <psxctl:Timestamp/>
            </psxctl:FileDescriptor>
            <psxctl:FileDescriptor name="jquery.percutils.js" type="script" mimetype="text/javascript">
                <psxctl:FileLocation>/cm/jslib/profiles/3x/jquery/plugins/jquery-percutils/jquery.percutils.js</psxctl:FileLocation>
                <psxctl:Timestamp/>
            </psxctl:FileDescriptor>
            <psxctl:FileDescriptor name="perc_path_constants.js" type="script" mimetype="text/javascript">
                <psxctl:FileLocation>/cm/plugins/perc_path_constants.js</psxctl:FileLocation>
                <psxctl:Timestamp/>
            </psxctl:FileDescriptor>
            <psxctl:FileDescriptor name="perc_utils.js" type="script" mimetype="text/javascript">
                <psxctl:FileLocation>/cm/plugins/perc_utils.js</psxctl:FileLocation>
                <psxctl:Timestamp/>
            </psxctl:FileDescriptor>
            <psxctl:FileDescriptor name="jquery.layout.js" type="script" mimetype="text/javascript">
                <psxctl:FileLocation>/cm/jslib/profiles/3x/jquery/plugins/jquery-layout/jquery.layout_and_plugins.js</psxctl:FileLocation>
                <psxctl:Timestamp/>
            </psxctl:FileDescriptor>
            <psxctl:FileDescriptor name="jquery.metadata.js" type="script" mimetype="text/javascript">
                <psxctl:FileLocation>/cm/jslib/profiles/3x/jquery/plugins/jquery-perc-retiredjs/jquery.metadata.js</psxctl:FileLocation>
                <psxctl:Timestamp/>
            </psxctl:FileDescriptor>
            <psxctl:FileDescriptor name="tools.scrollable-1.1.2.js" type="script" mimetype="text/javascript">
                <psxctl:FileLocation>/cm/jslib/profiles/3x/jquery/plugins/jquery-perc-retiredjs/tools.scrollable-1.1.2.js</psxctl:FileLocation>
                <psxctl:Timestamp/>
            </psxctl:FileDescriptor>
            <psxctl:FileDescriptor name="tools.scrollable.mousewheel-1.0.1.js" type="script" mimetype="text/javascript">
                <psxctl:FileLocation>/cm/jslib/profiles/3x/jquery/plugins/jquery-perc-retiredjs/tools.scrollable.mousewheel-1.0.1.js</psxctl:FileLocation>
                <psxctl:Timestamp/>
            </psxctl:FileDescriptor>
            <psxctl:FileDescriptor name="jquery.validate.js" type="script" mimetype="text/javascript">
                <psxctl:FileLocation>/cm/jslib/profiles/3x/jquery/plugins/jquery-validation/jquery.validate.js</psxctl:FileLocation>
                <psxctl:Timestamp/>
            </psxctl:FileDescriptor>
            <!-- SimpleDateFormat JS-->
            <psxctl:FileDescriptor name="date.js" type="script" mimetype="text/javascript">
                <psxctl:FileLocation>/cm/jslib/profiles/3x/libraries/perc-retiredjs/date.js</psxctl:FileLocation>
                <psxctl:Timestamp/>
            </psxctl:FileDescriptor>
            <psxctl:FileDescriptor name="perc_extend_jQueryValidate.js" type="script" mimetype="text/javascript">
                <psxctl:FileLocation>/cm/plugins/perc_extend_jQueryValidate.js</psxctl:FileLocation>
                <psxctl:Timestamp/>
            </psxctl:FileDescriptor>
            <!-- Site Picker Control JS-->
            <psxctl:FileDescriptor name="perc_path_manager.js" type="script" mimetype="text/javascript">
                <psxctl:FileLocation>/cm/plugins/perc_path_manager.js</psxctl:FileLocation>
                <psxctl:Timestamp/>
            </psxctl:FileDescriptor>
            <psxctl:FileDescriptor name="jquery.dynatree.js" type="script" mimetype="text/javascript">
                <psxctl:FileLocation>/cm/jslib/profiles/3x/jquery/plugins/jquery-dynatree/jquery.dynatree.js</psxctl:FileLocation>
                <psxctl:Timestamp/>
            </psxctl:FileDescriptor>
            <psxctl:FileDescriptor name="checkboxTree.js" type="script" mimetype="text/javascript">
                <psxctl:FileLocation>/sys_resources/js/checkboxTree.js</psxctl:FileLocation>
                <psxctl:Timestamp/>
            </psxctl:FileDescriptor>
            <psxctl:FileDescriptor name="checkboxTreeReadonly.js" type="script" mimetype="text/javascript">
                <psxctl:FileLocation>/sys_resources/js/checkboxTreeReadonly.js</psxctl:FileLocation>
                <psxctl:Timestamp/>
            </psxctl:FileDescriptor>
            <psxctl:FileDescriptor name="jquery.text-overflow.js" type="script" mimetype="text/javascript">
                <psxctl:FileLocation>/cm/jslib/profiles/3x/jquery/plugins/jquery-perc-retiredjs/jquery.text-overflow.js</psxctl:FileLocation>
                <psxctl:Timestamp/>
            </psxctl:FileDescriptor>
            <psxctl:FileDescriptor name="jquery.jeditable.js" type="script" mimetype="text/javascript">
                <psxctl:FileLocation>/cm/jslib/profiles/3x/jquery/plugins/jquery-jeditable/jquery.jeditable.js</psxctl:FileLocation>
                <psxctl:Timestamp/>
            </psxctl:FileDescriptor>
            <psxctl:FileDescriptor name="PercServiceUtils.js" type="script" mimetype="text/javascript">
                <psxctl:FileLocation>/cm/services/PercServiceUtils.js</psxctl:FileLocation>
                <psxctl:Timestamp/>
            </psxctl:FileDescriptor>
            <psxctl:FileDescriptor name="PercFinderTree.js" type="script" mimetype="text/javascript">
                <psxctl:FileLocation>/cm/widgets/PercFinderTree.js</psxctl:FileLocation>
                <psxctl:Timestamp/>
            </psxctl:FileDescriptor>
            <psxctl:FileDescriptor name="PercExtendUiDialog.js" type="script" mimetype="text/javascript">
                <psxctl:FileLocation>/cm/plugins/PercExtendUiDialog.js</psxctl:FileLocation>
                <psxctl:Timestamp/>
            </psxctl:FileDescriptor>
            <!-- Page Auto List Control JS-->
            <psxctl:FileDescriptor name="jquery.pageAutoListControl.js" type="script" mimetype="text/javascript">
                <psxctl:FileLocation>/rx_resources/widgets/pageAutoList/js/jquery.pageAutoList.js</psxctl:FileLocation>
                <psxctl:Timestamp/>
            </psxctl:FileDescriptor>
        </psxctl:AssociatedFileList>
    </psxctl:ControlMeta>

    <xsl:template match="Control[@name='pageAutoListWidgetControl']" mode="psxcontrol">

        <script >
            $(function() {
            $('#display_start_date').datepicker({
            onSelect:
            function(value, date) {
            // *********************************************************************
            //This will be set from the widget config $date_format (set by velocity)
            // *********************************************************************
            $(this).trigger("focus");
            var p_start_date = new Date(value);
            var p_end_date = new Date($('[name="end_date"]').val());
            <xsl:text disable-output-escaping="yes">
                if(p_start_date > p_end_date)
                {
                    $.perc_utils.alert_dialog({title:"Error", content:"First published on or after date must be less than First published before date.", okCallBack:function(){
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
            showOn: 'button', buttonImage: '/rx_resources/widgets/pageAutoList/images/calendar.gif', buttonImageOnly: true, altFormat: 'yy-mm-dd', buttonText: ''
            });

            $('#display_end_date').datepicker({
            onSelect:
            function(value, date) {
            $(this).trigger("focus");
            var p_start_date = new Date($('[name="start_date"]').val());
            var p_end_date = new Date(value);
            <xsl:text disable-output-escaping="yes">
                if(p_start_date >= p_end_date)
                {
                    $.perc_utils.alert_dialog({title:"Error", content:"First published before date must be greater than First published on or after date.", okCallBack:function(){
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
            showOn: 'button', buttonImage: '/rx_resources/widgets/pageAutoList/images/calendar.gif', buttonImageOnly: true, altFormat: 'yy-mm-dd', buttonText: ''
            });

            $('#perc-content-form').pageAutoListControl({});

            $("#ui-datepicker-div").hide();
            });


        </script>

        <script >
            $(function(){
                $("#autolist-title").on("click",function () {
                    $("#criteria_for_list").toggle();
                    $("#autolist-title").toggleClass("autolist-expand-image autolist-close-image");
                });
            });
        </script>
        <div id = "perc-autolist-wrapper" >
            <div id = "autolist-title-wrapper">
                <div id = "autolist-title" class = "autolist-close-image">Results Criteria</div>
            </div>
            <div id="criteria_for_list">
                <div class="ui-perc-ctl_daterange">
                    <label for="display_title_contains">Title contains:<br /></label>
                    <input size = "50" type="text" class = "datadisplay" name="display_title_contains" id="display_title_contains"/>
                    <table style = "line-height:17px;">
                        <tr>
                            <td> <label for="display_start_date">First published on or after:<br /></label>
                                <input size = "16" type="text" name="display_start_date" id="display_start_date" class="datadisplay date-pick"/></td>
                            <td width = "18px"></td>
                            <td><label for="display_end_date">First published before: <br /></label>
                                <input size = "16" type="text" name="display_end_date" id="display_end_date" class="datadisplay date-pick"/></td>
                        </tr>
                        <tr>
                            <td colspan="3">
                                <input id="perc-pageautolist-hide-past-results" name="hidePastResults" type="checkbox"/>
                                <label id="perc-pageautolist-hide-past-results-label" for="hidePastResults">Hide historical results by</label>
                                <input type="radio" id="perc-pal-hide-past-results-button1" name="perc-pageautolist-hide-past-results-date-type" value="rx:sys_contentpostdate" />
                                <label for="perc-pal-hide-past-results-button1">Content publish date</label>
                                <input type="radio" id="perc-pal-hide-past-results-button2" name="perc-pageautolist-hide-past-results-date-type" value="rx:page_end_date"/>
                                <label for="perc-pal-hide-past-results-button2">Calendar end date</label>
                            </td>
                        </tr>
                    </table>
                </div>
                <p></p>
                <div id = "perc-pageautolist-detail">
                    <table style = "line-height:17px;">
                        <tr>
                            <td>
                                <span class = "autolistlabel"><label for="perc-folder-selector" style="font-weight:bold">Website location:</label></span>
                            </td>
                            <td>
                                <span class = "autolistlabel"><label for="display_pagetemplates_list" style="font-weight:bold">Template:</label></span>
                            </td>
                        </tr>
                        <tr>
                            <td height = "265px" valign = "top">
                                <div id="perc-folder-selector">
                                </div>
                            </td>
                            <td height = "265px" valign = "top">
                                <div class="ui-perc-pageautolist-pagetypes">
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <span class = "autolistlabel"><label for="perc-category-selector" style="font-weight:bold">Categories:</label></span>
                            </td>
                            <td>
                                <span class = "autolistlabel"><label for="perc-tags-selector" style="display:none;font-weight:bold">Tags:</label></span>
                            </td>
                        </tr>
                        <tr>
                            <td height = "265px" valign="top">
                                <div id="perc-category-selector" style="width:300px; height:250px; overflow:auto;" class="tree-datadisplay" role="tree">
                                </div>
                            </td>
                            <td valign="top">
                                <div height = "265px" id="perc-tags-selector">
                                    <div id="perc-tags-selector-container">
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>
                <input type="hidden" aarenderer="NONE" class="datadisplay" id="perc-content-edit-query" name="query"/>
            </div>
        </div>

    </xsl:template>
    <xsl:template match="Control[@name='pageAutoListWidgetControl' and @isReadOnly='yes']" priority='10' mode="psxcontrol">
        <script >
    <![CDATA[
            $(function(){
                $("#autolist-title").on("click", function () {
                    $("#criteria_for_list").toggle();
                    $("#autolist-title").toggleClass("autolist-expand-image autolist-close-image");
                });

                // Put site value in website location field
                var sitepath = $("#perc_site_path").val().substring(8);
                var splitPath = sitepath.split("/");

                $("#perc_display_site_path").text(sitepath);

                // Fill templates field

                if (typeof splitPath[0] !== "undefined" && splitPath[0] != "")
                {
                    $.PercServiceUtils.makeJsonRequest(
                            $.perc_paths.TEMPLATES_BY_SITE + "/" + splitPath[0],
                            $.PercServiceUtils.TYPE_GET,
                            false,
                            function(status, result){
                                if(status === $.PercServiceUtils.STATUS_SUCCESS)
                                {
                                    var summaries = result.data.TemplateSummary;
                                    var temps = {};
                                    var tempsArray = [];
                                    var tempIds = ($("#perc_template_list").val() !== "") ? $("#perc_template_list").val().split(',') : "";
                                    for(i = 0; i < summaries.length; i++)
                                    {
                                        temps[summaries[i].id] = summaries[i].name;
                                    }
                                    for(i = 0; i < tempIds.length; i++)
                                    {
                                        tempsArray[i] = temps[tempIds[i]];
                                    }
                                    tempsArray.sort();
                                    var buff = "";
                                    for(i = 0; i < tempsArray.length; i++)
                                    {
                                        if(i > 0)
                                            buff += "<br/>";
                                        buff += tempsArray[i];
                                    }
                                    $("#perc_display_template_list").append(buff);
                                }
                                else
                                {
                                    var defaultMsg =
                                            $.PercServiceUtils.extractDefaultErrorMessage(result.request);
                                    $.perc_utils.alert_dialog({title: 'Error', content: defaultMsg});
                                }
                            }
                    );
                }
            });
            ]]>
                </script>
        <div id = "perc-autolist-wrapper">
            <div id = "autolist-title-wrapper">
                <div id = "autolist-title" class = "autolist-close-image"> Criteria for List Creation </div>
            </div>
            <div id="criteria_for_list">
                <label for="display_title_contains">Title contains:</label>
                <br/>
                <div class="datadisplay" id="perc_display_title_contains"><xsl:value-of select="//DisplayField/Control[@paramName='title_contains']/Value"/></div>
                <br/>
                <label for="display_start_date">Posted on or after:</label>
                <br/>
                <div class="datadisplay" id="perc_display_start_date"><xsl:value-of select="//DisplayField/Control[@paramName='start_date']/Value"/></div>
                <br/>
                <label for="display_end_date">Posted before:</label>
                <br/>
                <div class="datadisplay" id="perc_display_end_date"><xsl:value-of select="//DisplayField/Control[@paramName='end_date']/Value"/></div>
                <br/>
                <label for="display_site_path">Website location:</label>
                <br/>
                <input type="hidden" id="perc_site_path">
                    <xsl:attribute name="value"><xsl:value-of select="//DisplayField/Control[@paramName='site_path']/Value"/></xsl:attribute>
                </input>
                <div class="datadisplay" id="perc_display_site_path"></div>
                <br/>
                <label for="perc_template_list">Template:</label>
                <br/>
                <input type="hidden" id="perc_template_list">
                    <xsl:attribute name="value"><xsl:value-of select="//DisplayField/Control[@paramName='page_templates_list']/Value"/></xsl:attribute>
                </input>
                <div class="datadisplay" id="perc_display_template_list">
                </div>
            </div>
        </div>
    </xsl:template>
</xsl:stylesheet>
