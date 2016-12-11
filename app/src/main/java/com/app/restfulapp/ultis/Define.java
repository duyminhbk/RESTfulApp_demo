package com.app.restfulapp.ultis;

/**
 * Created by minhpham on 1/20/16.
 */
public final class Define {
//    private static String SERVICE = "http://192.168.1.105:83";
//    private static String SERVICE = "http://visitme.cloudapp.net:83";
    private static String SERVICE = "http://antvn.vn";

    public static void AssignServerAddress(String serverAddress)
    {
        SERVICE = "http://" + serverAddress;

        SALEMAN_LIST_URL = SERVICE+"/Home/GetSalemanList";
        CHIEF_LIST_URL = SERVICE+"/Home/GetChiefList";
        DIR_LIST_URL = SERVICE+"/Home/GetDirList";
        GET_CUSTOMERS_URL = SERVICE+"/Home/GetCustomerList";
        LOGIN_URL = SERVICE+"/Home/Login?Email=%s&Password=%s&Imei=%s";
        GET_LABEL_FLAGS = SERVICE+"/Home/GetLabelFlags";
        GET_P1 = SERVICE+"/Home/GetP1List";
        GET_P2 = SERVICE+"/Home/GetP2List?p1=%s";
        GET_PRODUCT = SERVICE+"/Home/GetProductList?p2=%s";
        CHANGE_PASS = SERVICE+"/Home/ChangePassword?model.OldPassword=%s&model.NewPassword=%s";
        SLKH_URL = SERVICE+"/Home/GetCustomerReport?" +
                "model.cust_no=%s" +
                "&model.tc_date1=%s" +
                "&model.tc_date2=%s" +
                "&model.part_kind=%s";
        //{ cust_type, label_flag, p_1, p_2, product_no, tc_date, PeriodType }
        SLGD_URL = SERVICE+"/Home/GetDirectorReport?" +
                "model.cust_type=%s" +
                "&model.label_flag=%s" +
                "&model.p_1=%s" +
                "&model.p_2=%s" +
                "&model.product_no=%s" +
                "&model.tc_date=%s" +
                "&model.PeriodType=%s" +
                "&model.sale_no=%s"
                ;
        //// {chief_no: "6073", cust_type: "1", label_flag: "1", tc_date: "2015-01-01"}
        SLTV_URL = SERVICE+"/Home/GetChiefReport?" +
                "model.chief_no=%s"+
                "&model.cust_type=%s" +
                "&model.label_flag=%s"+
                "&model.tc_date=%s" ;
        //sale_no: "6073", part_kind: "A", tc_date1: "2012-09-01", tc_date2: "2015-09-01"}
        SLTT_URL = SERVICE+"/Home/GetSaleManReport?" +
                "model.sale_no=%s" +
                "&model.part_kind=%s" +
                "&model.tc_date1=%s" +
                "&model.tc_date2=%s" ;
    }

//    public static final String SUBMIT_DATA_URL = SERVICE+"/Home/GetSaleData?SaleNo=%s&Date=%s";
    public static String SALEMAN_LIST_URL = SERVICE+"/Home/GetSalemanList";
    public static String CHIEF_LIST_URL = SERVICE+"/Home/GetChiefList";
    public static String DIR_LIST_URL = SERVICE+"/Home/GetDirList";
    public static String GET_CUSTOMERS_URL = SERVICE+"/Home/GetCustomerList";
    public static String LOGIN_URL = SERVICE+"/Home/Login?Email=%s&Password=%s&Imei=%s";
    public static String GET_LABEL_FLAGS = SERVICE+"/Home/GetLabelFlags";
    public static String GET_P1 = SERVICE+"/Home/GetP1List";
    public static String GET_P2 = SERVICE+"/Home/GetP2List?p1=%s";
    public static String GET_PRODUCT = SERVICE+"/Home/GetProductList?p2=%s";
    public static String CHANGE_PASS = SERVICE+"/Home/ChangePassword?model.OldPassword=%s&model.NewPassword=%s";
    public static String SLKH_URL = SERVICE+"/Home/GetCustomerReport?" +
            "model.cust_no=%s" +
            "&model.tc_date1=%s" +
            "&model.tc_date2=%s" +
            "&model.part_kind=%s";
    //{ cust_type, label_flag, p_1, p_2, product_no, tc_date, PeriodType }
    public static String SLGD_URL = SERVICE+"/Home/GetDirectorReport?" +
            "model.cust_type=%s" +
            "&model.label_flag=%s" +
            "&model.p_1=%s" +
            "&model.p_2=%s" +
            "&model.product_no=%s" +
            "&model.tc_date=%s" +
            "&model.PeriodType=%s" +
            "&model.sale_no=%s"
            ;
    //// {chief_no: "6073", cust_type: "1", label_flag: "1", tc_date: "2015-01-01"}
    public static String SLTV_URL = SERVICE+"/Home/GetChiefReport?" +
            "model.chief_no=%s"+
            "&model.cust_type=%s" +
            "&model.label_flag=%s"+
            "&model.tc_date=%s" ;
    //sale_no: "6073", part_kind: "A", tc_date1: "2012-09-01", tc_date2: "2015-09-01"}
    public static String SLTT_URL = SERVICE+"/Home/GetSaleManReport?" +
            "model.sale_no=%s" +
            "&model.part_kind=%s" +
            "&model.tc_date1=%s" +
            "&model.tc_date2=%s" ;

//    public static String AuthenticationCookieName = ".AspNet.ApplicationCookie";
}
