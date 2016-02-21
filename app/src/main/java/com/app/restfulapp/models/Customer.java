package com.app.restfulapp.models;

import java.io.Serializable;

/**
 * Created by minhpham on 2/2/16.
 * {
 "cust_no": "C50097",
 "cust_vname": "Phạm Văn Quân.",
 "address_name": null,
 "sale_no": "6066",
 "label_flag": "5",
 "cust_type": "3",
 "cust_kind": null
 }
 */
public class Customer implements Serializable{


    private String custNo;
    private String custName;
    private String address;
    private String saleNo;
    private String labelFlag;
    private String custType;
    private String custKind;

    public Customer(){

    }

    public  Customer(String custNo,String custName){
        this.custNo = custNo;
        this.custName = custName;
    }

    public String getCustNo() {
        return custNo;
    }

    public String getCustName() {
        return custName;
    }

    public String getAddress() {
        return address;
    }

    public String getSaleNo() {
        return saleNo;
    }

    public String getLabelFlag() {
        return labelFlag;
    }

    public String getCustType() {
        return custType;

    }

    public String getCustKind() {
        return custKind;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSaleNo(String saleNo) {
        this.saleNo = saleNo;
    }

    public void setLabelFlag(String labelFlag) {
        this.labelFlag = labelFlag;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    public void setCustKind(String custKind) {
        this.custKind = custKind;
    }


}
