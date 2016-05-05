package com.app.restfulapp.reports;

import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.app.restfulapp.R;
import com.app.restfulapp.models.Member;
import com.app.restfulapp.ultis.AppClientRequest;
import com.app.restfulapp.ultis.Define;
import com.app.restfulapp.ultis.Parser;
import com.app.restfulapp.ultis.ReportLayout;
import com.app.restfulapp.ultis.Utility;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by minhpham on 1/18/16.
 */
public class FrgSLTTReport extends FrgReport {

    private Member kind;

    @Override
    protected void initView() {
        super.initView();
        int unit = Utility.getMaxScreen(mActivity)/9;
//        reportLayout.setColumnWidth(new int[]{unit * 3, unit * 2, unit * 2, unit * 2});

        TextView txDate = (TextView) findViewById(R.id.txdate);

        txDate.setText(fromDate + " - " + toDate);
        if(kind != null) {
            ((TextView) findViewById(R.id.tx_kind)).setText(kind.getName());
        }
    }

    @Override
    protected int defineLayout() {
        return R.layout.frg_report_sltt;
    }

    @Override
    protected void requestData() {
//        if(member == null){
//            Toast.makeText(mActivity,"sale man not define",Toast.LENGTH_SHORT).show();
//            return;
//        }
        if(kind == null){
            Toast.makeText(mActivity,"part kind not define",Toast.LENGTH_SHORT).show();
            return;
        }
        mActivity.showLoading(true);
        ////sale_no: "6073", part_kind: "A", tc_date1: "2012-09-01", tc_date2: "2015-09-01"}
        AppClientRequest.get(mActivity,String.format(Define.SLTT_URL, member == null ? "" : member.getCode(), kind.getCode(),
                        fromDate, toDate), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                        super.onSuccess(statusCode, headers, response);
                        mActivity.showLoading(false);
                        Log.d("minh", response.toString());
                        //success request
                        if (Parser.isSuccess(response)) {
                            try {
                                TextView txName = (TextView) findViewById(R.id.tx_name);
                                if (!TextUtils.isEmpty(response.optJSONObject("Result").optString("sale_ename"))) {
                                    txName.setText(response.optJSONObject("Result").optString("sale_ename"));
                                }
                                reportLayout.setDataAndLayout(Parser.parseSLTT(response.optJSONObject("Result")));
                            } catch (ReportLayout.DataFormatException e) {
                                e.printStackTrace();
                            }
                        } else {
                            // show error
                            Toast.makeText(mActivity, Parser.getError(response), Toast.LENGTH_SHORT).show();
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        mActivity.showLoading(false);

                        if(statusCode == 401 || statusCode == 404)
                            Toast.makeText(mActivity, "Session timeout. Please re-login and try again.", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(mActivity, "status :" + statusCode + " error: " + responseString + "", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    public FrgReport setData(Member member,Member kind, String fromDate, String toDate) {
        super.setData(null, member, fromDate, toDate);
        this.kind = kind;
        return this;
    }
}
