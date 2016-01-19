package com.app.restfulapp.reports;

import android.widget.Toast;

import com.app.restfulapp.BaseFrg;
import com.app.restfulapp.R;
import com.app.restfulapp.ultis.ReportLayout;

/**
 * Created by minhpham on 1/18/16.
 */
public abstract class FrgReport extends BaseFrg {
    protected ReportLayout reportLayout;
    protected String reportID;
    protected String fromDate;
    protected String toDate;

    @Override
    protected void initView() {
        reportLayout = (ReportLayout)findViewById(R.id.rp_sample);
        requestData();
    }

    protected abstract void requestData();

    public FrgReport setData(String id,String fromDate,String toDate){
        this.reportID = id;
        this.fromDate = fromDate;
        this.toDate = toDate;
        return this;
    }

}
