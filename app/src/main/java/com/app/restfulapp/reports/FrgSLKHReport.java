package com.app.restfulapp.reports;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.app.restfulapp.R;
import com.app.restfulapp.models.Customer;
import com.app.restfulapp.models.Member;
import com.app.restfulapp.ultis.AppClientRequest;
import com.app.restfulapp.ultis.Parser;
import com.app.restfulapp.ultis.ReportLayout;
import com.app.restfulapp.ultis.Define;
import com.app.restfulapp.ultis.Utility;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by minhpham on 1/18/16.
 */
public class FrgSLKHReport extends FrgReport {

    private Member kind;

    @Override
    protected void initView() {
        super.initView();
        int unit = Utility.getMaxScreen(mActivity)/11;
//        reportLayout.setColumnWidth(new int[]{unit * 2, unit * 3, unit * 2, unit * 2, unit * 2});

        TextView txDate = (TextView) findViewById(R.id.txdate);
        txDate.setText(fromDate+" - "+ toDate);
        if(kind ==null) return;
        ((TextView) findViewById(R.id.tx_kind)).setText(kind.getName());
    }

    @Override
    protected void requestData() {
        if(customer == null){
            Toast.makeText(mActivity,"customer not define",Toast.LENGTH_SHORT).show();
            return;
        }if(kind == null){
            Toast.makeText(mActivity,"part kind not define",Toast.LENGTH_SHORT).show();
            return;
        }
        mActivity.showLoading(true);
//        { cust_no, part_kind, tc_date1, tc_date2 }
        AppClientRequest.get(mActivity,String.format(Define.SLKH_URL, customer.getCustNo(), fromDate,
                        toDate, kind.getCode()), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("SLKH_URL", response + "");
                        mActivity.showLoading(false);
                        if (Parser.isSuccess(response)) {
                            try {
                                TextView txName = (TextView) findViewById(R.id.tx_name);
                                txName.setText(response.optJSONObject("Result").optString("cust_vname"));
                                reportLayout.setDataAndLayout(Parser.parseSLKH(response.optJSONObject("Result")));
                            } catch (ReportLayout.DataFormatException e) {
                                e.printStackTrace();
                            }
                        } else {
                            // show error
                            Toast.makeText(mActivity, Parser.getError(response), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
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

    @Override
    public FrgReport setData(Customer customer, Member member, String fromDate, String toDate) {
        super.setData(customer,member,fromDate,toDate);
        this.kind = member;
        return this;
    }

    @Override
    protected int defineLayout() {
        return R.layout.frg_report_slkh;
    }
}
