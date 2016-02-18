package com.app.restfulapp.models;

/**
 * Created by minhpham on 1/28/16.
 */
public class Member {
    private String code;
    private String name;

    public Member(String name,String code){
        this.name = name;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
