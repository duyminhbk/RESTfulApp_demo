package com.app.restfulapp.reports;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.app.restfulapp.R;
import com.app.restfulapp.models.Customer;
import com.app.restfulapp.models.Member;
import com.app.restfulapp.ultis.Parser;
import com.app.restfulapp.ultis.ReportLayout;
import com.app.restfulapp.ultis.Define;
import com.app.restfulapp.ultis.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by minhpham on 1/18/16.
 */
public class FrgSLTVReport extends FrgReport {

    private Member kind;

    @Override
    protected void initView() {
        super.initView();
        ((TextView)findViewById(R.id.tx_area)).setText(String.format(getString(R.string.area_label), customer.getCustName()));
        TextView txDate = (TextView) findViewById(R.id.txdate);
        txDate.setText(fromDate + " - " + toDate);
    }

    @Override
    protected int defineLayout() {
        return R.layout.frg_report_sltv;
    }

    @Override
    protected void requestData() {
        if(customer == null){
            Toast.makeText(mActivity,"customer area not define",Toast.LENGTH_SHORT).show();
            return;
        }
        if(kind == null){
            Toast.makeText(mActivity,"flag not define",Toast.LENGTH_SHORT).show();
            return;
        }
//        if(member == null || "Alert".equalsIgnoreCase(member.getCode())){
//            Toast.makeText(mActivity,"member not define",Toast.LENGTH_SHORT).show();
//            return;
//        }
        mActivity.showLoading(true);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setCookieStore(mActivity.getCookieStore());
        // {chief_no: "6073", cust_type: "1", label_flag: "1", tc_date: "2015-01-01"}
        client.get(String.format(Define.SLTV_URL,member != null ? member.getCode():Utility.getString(mActivity,"saleNo"),customer.getCustName(),kind.getCode(), "2015-02-27"), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    mActivity.showLoading(false);
                    Log.d("minh", response.toString());
                    //success request
                    if(Parser.isSuccess(response)){
                        try {
                            reportLayout.setDataAndLayout(Parser.parseSLTV(response.optJSONObject("Result")));
                        }catch (ReportLayout.DataFormatException e){
                            e.printStackTrace();
                        }
                    }else{
                        // show error
                        Toast.makeText(mActivity, Parser.getError(response), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    mActivity.showLoading(false);
                    Toast.makeText(mActivity,responseString,Toast.LENGTH_SHORT).show();
                }
            }
        );
    }

    public FrgReport setData(Customer customer,Member member,Member kind, String fromDate, String toDate) {
        super.setData(customer, member, fromDate, toDate);
        this.kind = kind;
        return this;
    }
}
