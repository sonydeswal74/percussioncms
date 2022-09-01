package com.percussion.soln.p13n.delivery.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsDateJsonValueProcessor;

import com.percussion.soln.p13n.delivery.DeliveryRequest;
import com.percussion.soln.p13n.delivery.DeliveryResponse;
import com.percussion.soln.p13n.delivery.data.DeliverySnippetItem;


/**
 * 
 * Useful Web utils for the delivery service.
 * @author adamgent
 *
 */
public class DeliveryWebUtils {

    private static final JsonConfig JSON_REQUEST_CONFIG = new JsonConfig();
    static {
        JSON_REQUEST_CONFIG.setRootClass(DeliveryRequest.class);
        JSON_REQUEST_CONFIG.registerJsonValueProcessor(Date.class, new JsDateJsonValueProcessor());
    }
    
    @SuppressWarnings("unchecked")
    public static DeliveryRequest jsonToRequest(String json) {

        JSONObject jsonObj = JSONObject.fromObject(json);
        JSONObject jsonList = jsonObj.optJSONObject("listItem");
        JSONArray jsonSnippets = 
            jsonList != null ? jsonList.optJSONArray("snippets") : null;
        Collection<DeliverySnippetItem> snippetCollection = 
            jsonSnippets != null ? JSONArray.toCollection(jsonSnippets, DeliverySnippetItem.class) : null;
        List<DeliverySnippetItem> snippets = snippetCollection != null ? new ArrayList<DeliverySnippetItem>(snippetCollection) : null;
        DeliveryRequest dr = (DeliveryRequest) JSONObject.toBean(jsonObj, JSON_REQUEST_CONFIG);
        if (dr.getListItem() != null && snippets != null) {
            dr.getListItem().setSnippets(snippets);
        }
        return dr;
    }
    
    
    
    public static String requestToJson(DeliveryRequest request) {
        return JSONObject.fromObject(request).toString();
    }
    
    public static String responseToJson(DeliveryResponse response) {
        JSONObject obj = JSONObject.fromObject(response);
        return obj.toString();
    }
    
    public static DeliveryResponse jsonToResponse(String json) {
        return (DeliveryResponse) JSONObject.toBean(JSONObject.fromObject(json), DeliveryResponse.class);
    
    }

}
