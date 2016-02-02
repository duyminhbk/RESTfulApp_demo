package com.app.restfulapp.ultis;

import com.app.restfulapp.models.Customer;
import com.app.restfulapp.models.Member;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    /*
     json model
       {
          "sale_no":"6104",
          "chief_ename":"aa"
       }
     */
    public static List<Member> parseMember(JSONArray arr) {
        ArrayList<Member> result = new ArrayList<Member>();
        if(arr == null || arr.length() == 0) return null;
        for(int i =0;i<arr.length();i++){
            JSONObject temp = arr.optJSONObject(i);
            result.add(new Member(temp.optString("chief_ename"),temp.optString("sale_no")));
        }
        return result;
    }

    public static String getID(String item) {
        String id="";
        try {
            JSONObject json = new JSONObject(item);
            id = json.optString("CustNo");
        }catch (Exception e){

        }
        return id;
    }
    /*
    json format
        {
            "cust_no": "C50097",
            "cust_vname": "Phạm Văn Quân.",
            "address_name": null,
            "sale_no": "6066",
            "label_flag": "5",
            "cust_type": "3",
            "cust_kind": null
        }
     */
    public static List<Customer> parseCustomers(JSONArray arr){
        if(arr ==null || arr.length() ==0) return null;
        List<Customer> result = new ArrayList<Customer>();
        for(int i=0;i<arr.length();i++){
            JSONObject json = arr.optJSONObject(i);
            Customer temp = new Customer();
            temp.setCustNo(json.optString("cust_no"));
            temp.setCustName(json.optString("cust_vname"));
            temp.setAddress(json.optString("address_name"));
            temp.setSaleNo(json.optString("sale_no"));
            temp.setLabelFlag(json.optString("label_flag"));
            temp.setCustType(json.optString("cust_type"));
            temp.setCustKind(json.optString("cust_kind"));
            result.add(temp);
        }
        return result;
    }

}
