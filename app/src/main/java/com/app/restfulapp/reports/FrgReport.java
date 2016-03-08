package com.app.restfulapp.reports;

import android.content.res.Configuration;
import android.util.Log;
import android.widget.Toast;

import com.app.restfulapp.BaseFrg;
import com.app.restfulapp.R;
import com.app.restfulapp.models.Customer;
import com.app.restfulapp.models.Member;
import com.app.restfulapp.ultis.ReportLayout;
import com.app.restfulapp.ultis.Utility;

/**
 * Created by minhpham on 1/18/16.
 */
public abstract class FrgReport extends BaseFrg {
    protected ReportLayout reportLayout;
    protected String fromDate;
    protected String toDate;
    protected Customer customer;
    protected Member member;

    @Override
    protected void initView() {
        reportLayout = (ReportLayout)findViewById(R.id.rp_sample);
        requestData();
    }

    protected abstract void requestData();

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            reportLayout.reLayout();
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            reportLayout.reLayout();
        }
    }

    public FrgReport setData(Customer customer,Member member, String fromDate,String toDate){
        this.customer = customer;
        this.member = member;
        this.fromDate = fromDate;
        this.toDate = toDate;
        return this;
    }

}
