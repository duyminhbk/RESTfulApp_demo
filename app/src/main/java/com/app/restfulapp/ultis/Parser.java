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
    //{"index":["firstname","lastname","phone"],"data":{"1":["minh","pham","0123"],"2":["ha","nguyen",""],"3":["ngoc","","112"]}}
    public static JSONObject parseSLKH(JSONObject json) {
        if(json == null || json.isNull("P1List")) return null;
        JSONObject result = new JSONObject();
        try {
            //define header
            JSONArray index = new JSONArray();
            index.put("Product Name");
            index.put("Packing type");
            index.put("Weight (Kgs)");
            index.put("Amount (VND)");

            result.put("index",index);
            // process data
            JSONObject data = new JSONObject();
            JSONObject list = json.optJSONArray("P1List").getJSONObject(0);
            String p1Total = list.optString("Total");
            JSONArray arr = list.optJSONArray("P2List");
            // process data on rows
            for(int i=0;i<arr.length();i++ ){
                JSONObject temp =  arr.getJSONObject(i);
                JSONArray sum = new JSONArray();
                sum.put(temp.optString("P2")+" "+temp.optString("P2Name"));
                sum.put(" ");
                sum.put(" ");
                sum.put(temp.optString("Total"));
                data.put("sum"+i,sum);
                parseProductSLKH(i, temp.optJSONArray("Products"), data);
            }
            //add total
            JSONArray total = new JSONArray();
            total.put(" ");
            total.put("<b>TOTAL OF AGENT</b>");
            total.put(" ");
            total.put("<b>"+p1Total+"</b>");
            data.put("total",total);
            // finish add data
            result.put("data",data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static void parseProductSLKH(int index, JSONArray arr, JSONObject output) throws JSONException {
        if(arr == null || arr.length() ==0) return;
        for(int i =0;i<arr.length();i++){
            JSONObject json = arr.getJSONObject(i);
            JSONArray temp = new JSONArray();
            temp.put(json.optString("p_no")+" "+json.optString("p_vname"));
            temp.put(json.optString("packing_type"));
            temp.put(json.optString("weight"));
            temp.put(json.optString("amount"));
            output.put(index+""+i,temp);
        }

    }

    public static JSONObject parseSLGD(JSONObject response) {
        return null;
    }

    public static JSONObject parseSLTT(JSONObject response) {
        return null;
    }

    public static JSONObject parseSLTV(JSONObject response) {
        return null;
    }
}
