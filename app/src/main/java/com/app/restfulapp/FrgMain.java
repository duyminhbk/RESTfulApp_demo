package com.app.restfulapp;

import android.app.DatePickerDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.restfulapp.models.Customer;
import com.app.restfulapp.models.Member;
import com.app.restfulapp.reports.FrgSLGDReport;
import com.app.restfulapp.reports.FrgSLKHReport;
import com.app.restfulapp.reports.FrgSLTTReport;
import com.app.restfulapp.reports.FrgSLTVReport;
import com.app.restfulapp.ultis.Define;
import com.app.restfulapp.ultis.Parser;
import com.app.restfulapp.ultis.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

/**
 * Created by minhpham on 12/16/15.
 */
public class FrgMain extends BaseFrg {
    private String params = "?SaleNo=%s&Date=%s";
    private TextView txDateFrom;
    private TextView txDateTo;
    private Button btnSubmit;

    private DatePickerDialog toDatePickerDialog;
    private DatePickerDialog toDatePickerDialog2;

    private ListView lvContent;
    private EditText etSal;
    private ArrayAdapter<String> listAdapter;
    private ArrayList<String> mData;
    private TextView txEmpty;
    private TextView txtSaleName;
    private TextView txtTotal;
    private EditText etName;
    private Spinner spinnerReport;
    private Spinner spinnerMember;
    private Spinner spinnerCustomer;
    private Reports reportType = Reports.NONE;
    private AdapMember mAdapMember;
    private Member mMember;
    private AdapCustomer mAdapCus;
    private Customer mCustomer;

    public enum Reports{
        NONE,SLTV,SLGD,SLKH,SLTT
    }

    @Override
    protected void initView() {

        // find view
        txDateFrom = (TextView) rootView.findViewById(R.id.ed_date_from);
        txDateTo = (TextView) rootView.findViewById(R.id.ed_date_to);
        etName=(EditText)findViewById(R.id.ed_member_name);
        spinnerReport = (Spinner)findViewById(R.id.sp_report);
        spinnerMember = (Spinner)findViewById(R.id.sp_member);
        spinnerCustomer = (Spinner)findViewById(R.id.sp_customer);
//        etSal = (EditText) rootView.findViewById(R.id.ed_sal);

        txtSaleName = (TextView) rootView.findViewById(R.id.txtSaleName);
        txtSaleName.setText(Utility.getString(mActivity, "saleName"));

//        txtTotal = (TextView) rootView.findViewById(R.id.txtTotal);

        btnSubmit = (Button) rootView.findViewById(R.id.btnSubmit);
//        lvContent = (ListView) rootView.findViewById(R.id.lv_content);
//        txEmpty = (TextView) rootView.findViewById(R.id.tv_empty);

//        initList();
        initDatePicker();
        initSpinner();
        getCustomer();
        handleSubmit();
    }

    private void getCustomer(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.setCookieStore(mActivity.getCookieStore());
        client.get(Define.GET_CUSTOMERS_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                mActivity.showLoading(false);
                Log.d("minh", "GET_CUSTOMERS_URL: " + response);
                if(Parser.isSuccess(response)){

                    mAdapCus.setData(Parser.parseCustomers(response.optJSONArray("Result"))).notifyDataSetChanged();
                }else{
                    // show error
                    Toast.makeText(mActivity,Parser.getError(response),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                mActivity.showLoading(false);
                Toast.makeText(mActivity,responseString,Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initSpinner() {
        // init spinner report
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mActivity, R.array.report_type, android.R.layout.simple_spinner_dropdown_item);
        spinnerReport.setAdapter(adapter);

        spinnerReport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reportType = Reports.values()[position];
                updateMember();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //init spinner member
        mAdapMember = new AdapMember(mActivity);
        spinnerMember.setAdapter(mAdapMember);
        spinnerMember.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMember = (Member) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //init spinner customer
        mAdapCus = new AdapCustomer(mActivity);
        spinnerCustomer.setAdapter(mAdapCus);
        spinnerCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCustomer = (Customer) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void updateMember() {
        mMember = null;
        switch (reportType){
            case SLGD:
                break;
            case SLTT:{
                AsyncHttpClient client = new AsyncHttpClient();
                client.setCookieStore(mActivity.getCookieStore());
                client.get(Define.SALEMAN_LIST_URL, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        mActivity.showLoading(false);
                        Log.d("minh", "SALEMAN_LIST_URL: " + response);
                        if(Parser.isSuccess(response)){
                            mAdapMember.setData(Parser.parseMember(response.optJSONArray("Result"))).notifyDataSetChanged();
                        }else{
                            // show error
                            Toast.makeText(mActivity,Parser.getError(response),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        mActivity.showLoading(false);
                        Toast.makeText(mActivity,responseString,Toast.LENGTH_SHORT).show();
                    }
                });
                }
                break;
            case SLKH:
                break;
            case SLTV:{
                AsyncHttpClient client = new AsyncHttpClient();
                client.setCookieStore(mActivity.getCookieStore());
                client.get(Define.CHIEF_LIST_URL, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        mActivity.showLoading(false);
                        Log.d("minh", "CHIEF_LIST_URL: " + response);
                        if(Parser.isSuccess(response)){
                            mAdapMember.setData(Parser.parseMember(response.optJSONArray("Result"))).notifyDataSetChanged();
                        }else{
                            // show error
                            Toast.makeText(mActivity,Parser.getError(response),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        mActivity.showLoading(false);
                        Log.d("minh", "CHIEF_LIST_URL- error: " + responseString);
                    }
                });
            }
                break;
        }
    }

    private void handleSubmit() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(txDateFrom.getText()) || TextUtils.isEmpty(txDateTo.getText())) {
                    Toast.makeText(mActivity,"Date field not empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                switch (reportType){
                    case SLGD:
                        mActivity.addFragment(new FrgSLGDReport().setData(mCustomer,mMember,txDateFrom.getText()+"",txDateTo.getText()+""),true);
                        break;
                    case SLKH:
                        mActivity.addFragment(new FrgSLKHReport().setData(mCustomer,mMember,txDateFrom.getText()+"",txDateTo.getText()+""),true);
                        break;
                    case SLTT:
                        mActivity.addFragment(new FrgSLTTReport().setData(mCustomer,mMember,txDateFrom.getText()+"",txDateTo.getText()+""),true);
                        break;
                    case SLTV:
                        mActivity.addFragment(new FrgSLTVReport().setData(mCustomer,mMember,txDateFrom.getText()+"",txDateTo.getText()+""),true);
                        break;
                }
            }
        });
    }

    private void initDatePicker() {
        //init date picker
        Calendar newCalendar = Calendar.getInstance();

        toDatePickerDialog = new DatePickerDialog(mActivity, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                txDateFrom.setText(Utility.convertFullDate(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        // init onClick
        txDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDatePickerDialog.show();
            }
        });

        toDatePickerDialog2 = new DatePickerDialog(mActivity, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                txDateTo.setText(Utility.convertFullDate(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        // init onClick
        txDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDatePickerDialog2.show();
            }
        });
    }

    private void initList() {
        mData = new ArrayList<String>();
        listAdapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1, mData);
        lvContent.setAdapter(listAdapter);
        lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                switch (reportType){
//                    case SLGD:
//                        mActivity.addFragment(new FrgSLGDReport().setData(Parser.getID(listAdapter.getItem(position)),txDateFrom.getText()+"",txDateTo.getText()+""),true);
//                        break;
//                    case SLKH:
//                        mActivity.addFragment(new FrgSLKHReport().setData(Parser.getID(listAdapter.getItem(position)),txDateFrom.getText()+"",txDateTo.getText()+""),true);
//                        break;
//                    case SLTT:
//                        mActivity.addFragment(new FrgSLTTReport().setData(mMember.getCode(),txDateFrom.getText()+"",txDateTo.getText()+""),true);
//                        break;
//                    case SLTV:
//                        mActivity.addFragment(new FrgSLTVReport().setData(Parser.getID(listAdapter.getItem(position)),txDateFrom.getText()+"",txDateTo.getText()+""),true);
//                        break;
//                }
            }
        });
    }

    @Override
    public String getName() {
        return "FrgMain";
    }

    @Override
    protected int defineLayout() {
        return R.layout.frg_main;
    }
}
