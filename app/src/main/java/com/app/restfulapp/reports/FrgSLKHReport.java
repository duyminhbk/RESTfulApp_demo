package com.app.restfulapp.reports;

import android.widget.TextView;
import android.widget.Toast;

import com.app.restfulapp.R;
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
public class FrgSLKHReport extends FrgReport {

    @Override
    protected void initView() {
        super.initView();
        int unit = Utility.getMaxScreen(mActivity)/11;
        reportLayout.setColumnWidth(new int[]{unit * 2, unit * 3, unit * 2, unit * 2, unit * 2});

        TextView txDate = (TextView) findViewById(R.id.txdate);
        txDate.setText(Utility.convertSimpleDate(fromDate)+" - "+ Utility.convertSimpleDate(toDate));

    }

    @Override
    protected void requestData() {
        if(customer == null){
            Toast.makeText(mActivity,"customer not define",Toast.LENGTH_SHORT).show();
            return;
        }
        mActivity.showLoading(true);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setCookieStore(mActivity.getCookieStore());

        client.get(String.format(Define.SLKH_URL, customer.getCustNo(), Utility.convertSimpleDate(fromDate),
                        Utility.convertSimpleDate(toDate)), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        mActivity.showLoading(false);
                        if(Parser.isSuccess(response)){
                            try {
                                TextView txName = (TextView) findViewById(R.id.tx_name);
                                txName.setText(response.optJSONObject("Result").optString("cust_vname"));
                                reportLayout.setDataAndLayout(Parser.parseSLKH(response.optJSONObject("Result")));
                            }catch (ReportLayout.DataFormatException e){
                                e.printStackTrace();
                            }
                        }else{
                             // show error
                            Toast.makeText(mActivity,Parser.getError(response),Toast.LENGTH_SHORT).show();
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

    @Override
    protected int defineLayout() {
        return R.layout.frg_report_slkh;
    }
}
