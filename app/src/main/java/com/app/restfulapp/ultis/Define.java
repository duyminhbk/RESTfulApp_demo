package com.app.restfulapp.ultis;

/**
 * Created by minhpham on 1/20/16.
 */
public final class Define {
    private static String SERVICE = "http://visitme.cloudapp.net:83";
    public static final String SUBMIT_DATA_URL = SERVICE+"/Home/GetSaleData?SaleNo=%s&Date=%s";
    public static final String SALEMAN_LIST_URL = SERVICE+"/Home/GetSalemanList";
    public static final String CHIEF_LIST_URL = SERVICE+"/Home/GetChiefList";
    public static final String GET_CUSTOMERS_URL = SERVICE+"/Home/GetCustomerList";
    public static final String LOGIN_URL = SERVICE+"/Home/Login";
    public static final String SLKH_URL = SERVICE+"/Home/GetCustomerReport?model.cust_no=%s&model.tc_date1=%s&model.tc_date2=%s";
    public static final String SLGD_URL = SERVICE+"/Home/GetDirectorReport?model.tc_date=%s&model.PeriodType=Monthly&model.cust_type=%s&model.label_flag=%s";
    public static final String SLTV_URL = SERVICE+"/Home/GetChiefReport?model.tc_date=%s&model.cust_type=%s&model.label_flag=%s&model.chief_no=%s";
    public static final String SLTT_URL = SERVICE+"/Home/GetSaleManReport?model.sale_no=%s&model.tc_date1=%s&model.tc_date2=%s" ;
}
