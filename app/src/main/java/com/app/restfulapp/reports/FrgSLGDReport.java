package com.app.restfulapp.reports;

import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.restfulapp.R;
import com.app.restfulapp.models.Customer;
import com.app.restfulapp.models.Member;
import com.app.restfulapp.ultis.AppClientRequest;
import com.app.restfulapp.ultis.Define;
import com.app.restfulapp.ultis.Parser;
import com.app.restfulapp.ultis.ReportLayout;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.Date;

import cz.msebera.android.httpclient.Header;

/**
 * Created by minhpham on 1/18/16.
 */
public class FrgSLGDReport extends FrgReport {

    private String[] args;

    @Override
    protected void requestData() {
//        if(customer == null){
//            Toast.makeText(mActivity,"customer not define",Toast.LENGTH_SHORT).show();
//            return;
//        }
        //{ cust_type, label_flag, p_1, p_2, product_no, tc_date, PeriodType }
        mActivity.showLoading(true);
        String sale_no = args[1];

        //{ cust_type, label_flag, p_1, p_2, product_no, tc_date, PeriodType }
        AppClientRequest.get(mActivity, String.format(Define.SLGD_URL, args[0], args[1], args[2], args[3], args[4],
                        args[5], args[6], sale_no), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        mActivity.showLoading(false);
                        Log.d("minh", response.toString());
                        //success request
                        if (Parser.isSuccess(response)) {
                            try {
                                JSONObject result = Parser.parseSLGD(response.optJSONObject("Result"));
                                reportLayout.setDataAndLayout(result);

                                // Get from_date, to_date
                                String fromDate = result.getString("from_date");
                                String toDate = result.getString("to_date");

                                setTimeLabel(fromDate, toDate);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            // show error
                            Toast.makeText(mActivity, Parser.getError(response), Toast.LENGTH_SHORT).show();
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        mActivity.showLoading(false);

                        if (statusCode == 401 || statusCode == 404)
                            Toast.makeText(mActivity, "Session timeout. Please re-login and try again.", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(mActivity, "status :" + statusCode + " error: " + responseString + "", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }


    @Override
    protected int defineLayout() {
        return R.layout.frg_report_slgd;
    }

    public FrgReport setData(String[] args) {
        this.args = args;
        return this;
    }

    @Override
    protected void initView() {
        super.initView();

        if (args == null || args.length < 6) return;

//        ((TextView) findViewById(R.id.tx_time)).setText(mActivity.getString(R.string.time_label) + " " + args[5]);
    }

    protected void setTimeLabel(String fromDate, String toDate) {
        //((TextView) findViewById(R.id.tx_time)).setText("Từ ngày " + fromDate.toString() + " đến ngày " + toDate.toString());
        ((TextView) findViewById(R.id.tx_time)).setText(fromDate.toString());
    }

    public enum PeriodType {
        // None,
        Daily,
        // Weekly,
        Monthly,
        Quarterly,
        Yearly // Annualy
    }
}
