package com.app.restfulapp.reports;

import android.util.Log;
import android.widget.Toast;

import com.app.restfulapp.R;
import com.app.restfulapp.ultis.Parser;
import com.app.restfulapp.ultis.ReportLayout;
import com.app.restfulapp.ultis.Define;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by minhpham on 1/18/16.
 */
public class FrgSLGDReport extends FrgReport {

    @Override
    protected void requestData() {
        if(customer == null){
            Toast.makeText(mActivity,"customer not define",Toast.LENGTH_SHORT).show();
            return;
        }
        mActivity.showLoading(true);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setCookieStore(mActivity.getCookieStore());
        client.get(String.format(Define.SLGD_URL, "C51442", "1970-01-01", "2020-01-01"), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        mActivity.showLoading(false);
                        Log.d("minh", response.toString());
                        //success request
                        if(Parser.isSuccess(response)){
                            try {
                                reportLayout.setDataAndLayout(Parser.parseSLGD(response.optJSONObject("Result")));
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

    @Override
    protected int defineLayout() {
        return R.layout.frg_report_slgd;
    }
}
