package com.app.restfulapp.models;

/**
 * Created by minhpham on 2/23/16.
 */
public class Product extends Member {

    private String p1;
    private String p2;
    private String partKind;

    public Product(String name, String code) {
        super(name, code);
    }

    public void setP1(String p1) {
        this.p1 = p1;
    }

    public void setP2(String p2) {
        this.p2 = p2;
    }

    public void setPartKind(String partKind) {
        this.partKind = partKind;
    }

    public String getP1() {
        return p1;
    }

    public String getP2() {
        return p2;
    }

    public String getPartKind() {
        return partKind;
    }
}
