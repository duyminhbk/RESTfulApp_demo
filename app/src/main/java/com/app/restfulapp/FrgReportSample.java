package com.app.restfulapp;

/**
 * Created by minhpham on 1/1/16.
 */
public class FrgReportSample extends BaseFrg {
    @Override
    protected void initView() {

    }

    @Override
    public String getName() {
        return getClass().getName();
    }

    @Override
    protected int defineLayout() {
        return R.layout.frg_report_detail_sample;
    }
}
