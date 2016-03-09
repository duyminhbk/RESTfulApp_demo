package com.app.restfulapp.ultis;

/**
 * Created by minhpham on 1/20/16.
 */
public final class Define {
    private static String SERVICE = "http://visitme.cloudapp.net:83";
//    private static String SERVICE = "http://113.161.145.161:80";
    public static final String SUBMIT_DATA_URL = SERVICE+"/Home/GetSaleData?SaleNo=%s&Date=%s";
    public static final String SALEMAN_LIST_URL = SERVICE+"/Home/GetSalemanList";
    public static final String CHIEF_LIST_URL = SERVICE+"/Home/GetChiefList";
    public static final String GET_CUSTOMERS_URL = SERVICE+"/Home/GetCustomerList";
    public static final String LOGIN_URL = SERVICE+"/Home/Login?Email=%s&Password=%s";
    public static final String GET_LABEL_FLAGS = SERVICE+"/Home/GetLabelFlags";
    public static final String GET_P1 = SERVICE+"/Home/GetP1List";
    public static final String GET_P2 = SERVICE+"/Home/GetP2List?p1=%s";
    public static final String GET_PRODUCT = SERVICE+"/Home/GetProductList?p2=%s";
    public static final String CHANGE_PASS = SERVICE+"/Home/ChangePassword?model.OldPassword=%s&model.NewPassword=%s";
    public static final String SLKH_URL = SERVICE+"/Home/GetCustomerReport?" +
            "model.cust_no=%s" +
            "&model.tc_date1=%s" +
            "&model.tc_date2=%s" +
            "&model.part_kind=%s";
    //{ cust_type, label_flag, p_1, p_2, product_no, tc_date, PeriodType }
    public static final String SLGD_URL = SERVICE+"/Home/GetDirectorReport?" +
            "model.cust_type=%s" +
            "&model.label_flag=%s" +
            "&model.p_1=%s" +
            "&model.p_2=%s" +
            "&model.product_no=%s" +
            "&model.tc_date=%s" +
            "&model.PeriodType=%s";
    //// {chief_no: "6073", cust_type: "1", label_flag: "1", tc_date: "2015-01-01"}
    public static final String SLTV_URL = SERVICE+"/Home/GetChiefReport?" +
            "model.chief_no=%s"+
            "&model.cust_type=%s" +
            "&model.label_flag=%s"+
            "&model.tc_date=%s" ;
    //sale_no: "6073", part_kind: "A", tc_date1: "2012-09-01", tc_date2: "2015-09-01"}
    public static final String SLTT_URL = SERVICE+"/Home/GetSaleManReport?" +
            "model.sale_no=%s" +
            "&model.part_kind=%s" +
            "&model.tc_date1=%s" +
            "&model.tc_date2=%s" ;
}
