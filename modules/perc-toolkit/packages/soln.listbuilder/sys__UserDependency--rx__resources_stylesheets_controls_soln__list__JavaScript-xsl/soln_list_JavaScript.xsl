<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE xsl:stylesheet [
		<!ENTITY % HTMLlat1 SYSTEM "../../DTD/HTMLlat1x.ent">
		%HTMLlat1;
		<!ENTITY % HTMLsymbol SYSTEM "../../DTD/HTMLsymbolx.ent">
		%HTMLsymbol;
		<!ENTITY % HTMLspecial SYSTEM "../../DTD/HTMLspecialx.ent">
		%HTMLspecial;
]>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:psxctl="URN:percussion.com/control" xmlns="http://www.w3.org/1999/xhtml" exclude-result-prefixes="psxi18n" xmlns:psxi18n="urn:www.percussion.com/i18n" >
<xsl:template match="/" />
	<!--
     sys_FileWord. 
     Do not modify this control directly. This control, word template file and cab files need to be modified together.
     Please see read me or help for upgrading the word controls. 
 -->
	<psxctl:ControlMeta name="soln_list_JavaScript" dimension="single" choiceset="none">
		<psxctl:Description>Executes custom javascript</psxctl:Description>
		<psxctl:ParamList>
		</psxctl:ParamList>
	</psxctl:ControlMeta>
	<xsl:template match="Control[@name='soln_list_JavaScript']" mode="psxcontrol">
		<xsl:variable name="calendar_id" select="concat('perc-content-edit-', @paramName)" />
		<xsl:variable name="value" select="Value" />
		<script type="text/javascript">
			(function($) {
			$(function() {
			var autoControls = [
						'soln_list_dateRangeEnd',
						'soln_list_dateRangeStart',
						'soln_list_jcrQuery',
						'soln_list_folderPaths',
						'soln_list_contentTypes',
						'soln_list_titleContains',
						'soln_list_childSnippet'
						];
			function findControls(controls) {
				controls = $.makeArray(controls);
				return $('tr > td[class=controlname]')
					.find('label')
					.filter(function(i) {
						var n = $(this).attr('for');
						return -1 != $.inArray(n, controls);
					}).parent().parent();
			}
			function doToggle() {
				var slot = $('#soln_list_slot_select').val();
				if (slot == 'soln_list_ManualSlot') {
					findControls(autoControls).hide();
				}
				else {
					findControls(autoControls).show();
				}
			}
			findControls('soln_list_js').hide();
			doToggle();
			$('#soln_list_slot_select').change(function() {
				doToggle();
			});
			
			});
			})(jQuery);
		</script>

	</xsl:template>
</xsl:stylesheet>