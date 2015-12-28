package com.app.restfulapp;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by minhpham on 12/28/15.
 */
public class FrgReportOption extends BaseFrg {

    @Override
    protected void initView() {
        findViewById(R.id.btn_slkh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mActivity,mActivity.getString(R.string.slkh_title),Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btn_slgd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mActivity,mActivity.getString(R.string.slgd_title),Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btn_sltt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mActivity,mActivity.getString(R.string.sltt_title),Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btn_sltv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mActivity,mActivity.getString(R.string.sltv_title),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public String getName() {
        return getClass().getName();
    }

    @Override
    protected int defineLayout() {
        return R.layout.frg_report_option;
    }
}
