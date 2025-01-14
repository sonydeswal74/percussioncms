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

(function($)
{
    $.PercWebResourcesService = {
        	deleteFile : deleteFile,
        	validateFileUpload : validateFileUpload
    };

    /**
     * Validate folder delete success callback, shows the warning message and makes a call to the server if user clicks ok. 
     */
    function cbVdfSuccess(data, path, name, type, callback)
    {
        var shouldPurge = path.indexOf($.perc_paths.RECYCLING_ROOT_NO_SLASH) !== -1;

        var title;
        if (type === "section")
        {
            title = I18N.message( "perc.ui.finder.section.delete@Title" );
        }
        else
        {
            if (shouldPurge) {
                title = I18N.message("perc.ui.finder.folder.purge@Title");
            } else {
                title = I18N.message("perc.ui.finder.folder.delete@Title");
            }
        }

        $.perc_utils.confirm_dialog({ 
              id: 'perc-finder-delete-folder',
              title: title,
              question: createDelWarning(data, name, type),
              success: function(){ 
                  var skipItems = "EMPTY";
                  if($("#perc_delete_folder_force").length > 0)
                  {
                    skipItems=$("#perc_delete_folder_force").get(0).checked?"NO":"YES";
                  }
                  var guid = $('a.perc-listing-category-FOLDER.perc_last_selected').attr("id").split("perc-finder-listing-")[1];
                  var delCriteria  = {"DeleteFolderCriteria":{"path":path,"skipItems":skipItems, "shouldPurge": shouldPurge, "guid":guid}};
                  //var delCriteria  = {"DeleteFolderCriteria":{"path":path,"skipItems":skipItems, "shouldPurge": shouldPurge}};
                  $.PercBlockUI($.PercBlockUIMode.CURSORONLY);
                  $.ajax({
                        url: $.perc_paths.PATH_DELETE_FOLDER, 
                        type: 'POST',
                        dataType:"json", 
                        contentType:"application/json", 
                        data:JSON.stringify(delCriteria),
                        success: callback,
                        error: cbDfErrors });
                   $.unblockUI(); 
              },
              width:500});
    }

    /**
	 * Delete file error callback, shows the error message to the user.
	 */
	function cbDfileErrors(errors) {
		var errorMsg = $.PercServiceUtils.extractDefaultErrorMessage(errors);
		var defMessage = I18N.message("perc.ui.web.resources.service@Could Not Delete File");
		$.perc_utils.alert_dialog({
			id : 'perc-finder-delete-error',
			title : I18N.message("perc.ui.web.resources.service@Delete File Error"),
			content : (errorMsg !== "") ? errorMsg : defMessage
		});
	}

	/**
	 * Creates a custom delete warning message based on the supplied file name.
	 */
	function createDelWarning(name) {
		var confirm;
		var middle;

		// type === fsfile
		middle = "perc.ui.finder.fsfile.delete@Filename";
		confirm = "perc.ui.finder.fsfile.delete@Confirm";

		var first;
		first = I18N.message(middle, [ name ]);
		first = first + "<br /><br />" + I18N.message(confirm, [ name ]);

		return first;
	}
    
    function deleteFile(path, name, callback)
    {
        var title = I18N.message( "perc.ui.finder.fsfile.delete@Title" );

        $.perc_utils.confirm_dialog({ 
              id: 'perc-finder-delete-fsfile',
              title: title,
              question: createDelWarning(name),
              success: function(){ 
                  $.PercBlockUI($.PercBlockUIMode.CURSORONLY);
                  $.PercServiceUtils.makeDeleteRequest(
                      $.perc_paths.WEBRESOURCESMGT + path,
                      false,
                      function(status, result) {
                          if(status === $.PercServiceUtils.STATUS_SUCCESS) {
                              callback($.PercServiceUtils.STATUS_SUCCESS, result.data);
                          } else {
                              cbDfileErrors(result.request);
                          }
                      }
                    );
                  $.unblockUI();
              },
              width:500});
    }
    
    /**
     * Checks if file exist given a name and path from the finder, under the Design node.
     * 
     * @param path
     * @param fileName
     * @param callback
     */
    function validateFileUpload(path, fileName, callback)
    {
        // Take the path from the finder, and joint everything (except "Web resources") with '/'. 
        // Append the filename to that.
        path = '/' + path.slice(3).join('/') + '/' + fileName;
                
        $.PercServiceUtils.makeRequest(
            $.perc_paths.WEBRESOURCESMGT_VALIDATE_FILE_UPLOAD + path,
            $.PercServiceUtils.TYPE_GET,
            false,
            function(status, result)
            {
                callback(status, result);
            }
        );
    }
})(jQuery);
