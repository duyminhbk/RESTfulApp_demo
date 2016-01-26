package com.app.restfulapp.ultis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by minhpham on 1/18/16.
 */
public class Parser {

    public static boolean isSuccess(JSONObject json){
        if(json == null || json.isNull("ResultCode")){
            return false;
        }
        return json.optString("ResultCode").equalsIgnoreCase("success");
    }

    public static String getError(JSONObject json) {
        if(json == null || json.isNull("ResultMessages")) return "";
        JSONArray arr = json.optJSONArray("ResultMessages");
        String err = "";
        for(int i=0; i<arr.length();i++){
            err = err+arr.optString(i)+"\n";
        }
        return err;
    }
    public static JSONObject parseSLKH(JSONObject json) {
        return json;
    }

    public static JSONObject parseSLGD(JSONObject response) {
        return response;
    }


    public static JSONObject parseSLTT(JSONObject json) {
        return json;
    }

   public static JSONObject parseSLTV(JSONObject response) {
        return response;
    }
}
