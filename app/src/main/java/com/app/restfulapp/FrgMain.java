package com.app.restfulapp;

import android.app.DatePickerDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.restfulapp.models.Customer;
import com.app.restfulapp.models.Member;
import com.app.restfulapp.models.Product;
import com.app.restfulapp.reports.FrgSLGDReport;
import com.app.restfulapp.reports.FrgSLKHReport;
import com.app.restfulapp.reports.FrgSLTTReport;
import com.app.restfulapp.reports.FrgSLTVReport;
import com.app.restfulapp.ultis.Define;
import com.app.restfulapp.ultis.Parser;
import com.app.restfulapp.ultis.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

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
    private Spinner spinnerKind;
    private Reports reportType = Reports.NONE;

    private AdapMember mAdapMember;
    private AdapMember mAdapKind;
    private AdapCustomer mAdapCus;
    private AdapProduct mAdapProduct;
    private AdapMember mAdapP1;
    private AdapMember mAdapP2;

    private Member mMember;
    private Customer mCustomer;
    private Member mKind;
    private MainActivity.Role role;
    private Spinner spinnerP1;
    private Spinner spinnerP2;
    private Spinner spinnerProduct;

    private Product mProduct;
    private Member mP2;
    private Member mP1;
    private View lnDate;

    public String[] getGDArg() {
        //{ cust_type, label_flag, p_1, p_2, product_no, tc_date, PeriodType }
        String[] result = new String[7];
        result[0] = mCustomer.getCustName();
        result[1] = mMember.getCode();
        result[2] = mP1.getCode();
        result[3] = mP2.getCode().equalsIgnoreCase("alert")?"":mP2.getCode();
        result[4] = mProduct.getCode().equalsIgnoreCase("alert")?"":mProduct.getCode();
        result[5] = txDateFrom.getText() + "";
        result[6] = mKind.getName();
        return result;
    }

    public void setReport(String kind) {
        if (getString(R.string.slkh_title).equals(kind)) {
            reportType = Reports.SLKH;
        } else if (getString(R.string.sltv_title).equals(kind)) {
            reportType = Reports.SLTV;
        } else if (getString(R.string.sltt_title).equals(kind)) {
            reportType = Reports.SLTT;
        }else if (getString(R.string.slgd_title).equals(kind)) {
            reportType = Reports.SLGD;
        }else{
            reportType = Reports.NONE;
        }

    }

    @Override
    protected void initView() {
        role = mActivity.getRole();
        // find view
        txDateFrom = (TextView) rootView.findViewById(R.id.ed_date_from);
        txDateTo = (TextView) rootView.findViewById(R.id.ed_date_to);
        lnDate = findViewById(R.id.ln_date);
        etName = (EditText) findViewById(R.id.ed_member_name);

        spinnerReport = (Spinner) findViewById(R.id.sp_report);
        spinnerMember = (Spinner) findViewById(R.id.sp_member);
        spinnerCustomer = (Spinner) findViewById(R.id.sp_customer);
        spinnerKind = (Spinner) findViewById(R.id.sp_kind);
        spinnerP1 = (Spinner) findViewById(R.id.sp_p1);
        spinnerP2 = (Spinner) findViewById(R.id.sp_p2);
        spinnerProduct = (Spinner) findViewById(R.id.sp_product);

        txtSaleName = (TextView) rootView.findViewById(R.id.txtSaleName);
        txtSaleName.setText(Utility.getString(mActivity, "saleName"));

        btnSubmit = (Button) rootView.findViewById(R.id.btnSubmit);

//        etSal = (EditText) rootView.findViewById(R.id.ed_sal);
//        txtTotal = (TextView) rootView.findViewById(R.id.txtTotal);
//        lvContent = (ListView) rootView.findViewById(R.id.lv_content);
//        txEmpty = (TextView) rootView.findViewById(R.id.tv_empty);

//        initList();
        initDatePicker();
        initSpinner();

        handleSubmit();
    }

    private void getCustomer() {
        mActivity.showLoading(true);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setCookieStore(mActivity.getCookieStore());
        client.get(Define.GET_CUSTOMERS_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                mActivity.showLoading(false);
                Log.d("minh", "GET_CUSTOMERS_URL: " + response);
                if (Parser.isSuccess(response)) {

                    mAdapCus.setData(Parser.parseCustomers(response.optJSONArray("Result"))).notifyDataSetChanged();
                } else {
                    // show error
                    Toast.makeText(mActivity, Parser.getError(response), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                mActivity.showLoading(false);
                Toast.makeText(mActivity, responseString, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getLabelFlags(final AdapMember adapter) {
        mActivity.showLoading(true);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setCookieStore(mActivity.getCookieStore());
        client.get(Define.GET_LABEL_FLAGS, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                mActivity.showLoading(false);
                Log.d("minh", "GET_CUSTOMERS_URL: " + response);
                if (Parser.isSuccess(response)) {

                    adapter.setData(Parser.parseLabelFlag(response.optJSONObject("Result"))).notifyDataSetChanged();
                } else {
                    // show error
                    Toast.makeText(mActivity, Parser.getError(response), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                mActivity.showLoading(false);
                Toast.makeText(mActivity, responseString, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initSpinner() {
        // init spinner report
        int reportKinds;
        switch (role) {
            case DIR:
                reportKinds = R.array.report_type_dir;
                break;
            case CHIEF:
                reportKinds = R.array.report_type_chief;
                break;
            case SALE:
                reportKinds = R.array.report_type_sale;
                break;
            default:
                reportKinds = R.array.report_type_default;
                break;
        }
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mActivity, reportKinds, android.R.layout.simple_spinner_dropdown_item);
        spinnerReport.setAdapter(adapter);

        spinnerReport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setReport((String) parent.getItemAtPosition(position));
                updateSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerReport.setSelection(0);

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

        //init spinner kind
        mAdapKind = new AdapMember(mActivity);
        spinnerKind.setAdapter(mAdapKind);
        spinnerKind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mKind = (Member) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //init spinner p1
        mAdapP1 = new AdapMember(mActivity);
        spinnerP1.setAdapter(mAdapP1);
        spinnerP1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mP1 = (Member) parent.getSelectedItem();
                if("Alert".equalsIgnoreCase(mP1.getCode())||"".equalsIgnoreCase(mP1.getCode())) return;
                updateP2();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //init spinner p2
        mAdapP2 = new AdapMember(mActivity);
        spinnerP2.setAdapter(mAdapP2);
        spinnerP2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mP2 = (Member) parent.getSelectedItem();
                if("Alert".equalsIgnoreCase(mP2.getCode())||"".equalsIgnoreCase(mP2.getCode())) return;
                updateProduct();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //init spinner product
        mAdapProduct = new AdapProduct(mActivity);
        spinnerProduct.setAdapter(mAdapProduct);
        spinnerProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getSelectedItem() instanceof Product) {
                    mProduct = (Product) parent.getSelectedItem();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void updateP2() {
        mActivity.showLoading(true);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setCookieStore(mActivity.getCookieStore());
        client.get(String.format(Define.GET_P2, mP1.getCode()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("minh", "GET_P2: " + response);
                if (Parser.isSuccess(response)) {
                    mAdapP2.setData(Parser.parseP2(response.optJSONArray("Result"))).notifyDataSetChanged();
                    if(mAdapP2.getCount()>1) {
                        visibleSpinner(true,spinnerP2);
                        mP2 = (Member) spinnerP2.getSelectedItem();
                        updateProduct();
                    }else{
                        spinnerP2.setVisibility(View.GONE);
                    }
                } else {
                    // show error
                    Toast.makeText(mActivity, Parser.getError(response), Toast.LENGTH_SHORT).show();
                    spinnerP2.setVisibility(View.GONE);
                }
                mActivity.showLoading(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                mActivity.showLoading(false);
                spinnerP2.setVisibility(View.GONE);
                Toast.makeText(mActivity, responseString, Toast.LENGTH_SHORT).show();
                mAdapP2.setData(null);
            }
        });
    }

    private void updateProduct() {
        mActivity.showLoading(true);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setCookieStore(mActivity.getCookieStore());
        client.get(String.format(Define.GET_PRODUCT, mP2.getCode()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("minh", "GET_PRODUCT: " + response);
                if (Parser.isSuccess(response)) {
                    mAdapProduct.setData(Parser.parseProduct(response.optJSONArray("Result"))).notifyDataSetChanged();
                    if(mAdapProduct.getCount()>1){
                        visibleSpinner(true,spinnerProduct);
                    }else{
                        spinnerProduct.setVisibility(View.GONE);
                    }
                } else {
                    // show error
                    spinnerProduct.setVisibility(View.GONE);
                    Toast.makeText(mActivity, Parser.getError(response), Toast.LENGTH_SHORT).show();
                }
                mProduct = (Product) mAdapProduct.getItem(0);
                mActivity.showLoading(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                mActivity.showLoading(false);
                Toast.makeText(mActivity, responseString, Toast.LENGTH_SHORT).show();
                mAdapProduct.setData(null);
                spinnerProduct.setVisibility(View.GONE);
            }
        });
    }

    private void visibleSpinner(boolean flag,Spinner... spinners){
        if(spinners == null || spinners.length == 0) return;
        for(Spinner temp : spinners){
            temp.setVisibility(flag?View.VISIBLE:View.GONE);
            temp.setSelection(0);
        }
    }

    private void notifyDataChange(BaseAdapter... adaps){
        if(adaps == null || adaps.length == 0) return;
        for(BaseAdapter adap : adaps){
            adap.notifyDataSetChanged();
        }
    }

    private void updateSpinner() {
        mMember = null;
        mKind = null;
        visibleSpinner(false, spinnerCustomer, spinnerMember, spinnerKind, spinnerProduct, spinnerP1, spinnerP2);
        lnDate.setVisibility(View.VISIBLE);
        switch (reportType) {
            // reuse spinner customer and member to define cust_type and label_flag
            case SLGD: {
                visibleSpinner(true, spinnerCustomer, spinnerMember, spinnerKind,spinnerP1);
                mAdapCus.setData(Utility.genCustType());
                getLabelFlags(mAdapMember);
                mAdapKind.setData(Utility.genPeriodType());
                //update P1
                mActivity.showLoading(true);
                AsyncHttpClient client = new AsyncHttpClient();
                client.setCookieStore(mActivity.getCookieStore());
                client.get(Define.GET_P1, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("minh", "GET_P1: " + response);
                        if (Parser.isSuccess(response)) {
                            mAdapP1.setData(Parser.parseP1(response.optJSONObject("Result"))).notifyDataSetChanged();
                            spinnerP1.setSelection(0);
                            mActivity.showLoading(false);
                            updateP2();
                        } else {
                            // show error
                            Toast.makeText(mActivity, Parser.getError(response), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        mActivity.showLoading(false);
                        Toast.makeText(mActivity, responseString, Toast.LENGTH_SHORT).show();
                        mAdapP1.setData(null);
                        mAdapP1.notifyDataSetChanged();
                    }
                });

                notifyDataChange(mAdapMember, mAdapCus, mAdapKind);

                spinnerKind.setSelection(1);
                mMember = (Member) mAdapMember.getItem(0);
                mCustomer = (Customer) mAdapCus.getItem(0);
                mKind = (Member) mAdapKind.getItem(0);
                mActivity.showLoading(false);
            }
            break;
            case SLTT: {
                if (role != MainActivity.Role.SALE) {
                    visibleSpinner(true, spinnerMember);
                    mActivity.showLoading(true);
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.setCookieStore(mActivity.getCookieStore());
                    client.get(Define.SALEMAN_LIST_URL, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            mActivity.showLoading(false);
                            Log.d("minh", "SALEMAN_LIST_URL: " + response);
                            if (Parser.isSuccess(response)) {
                                mAdapMember.setData(Parser.parseMember(response.optJSONArray("Result"))).notifyDataSetChanged();
                                mMember = (Member) mAdapMember.getItem(0);
                            } else {
                                // show error
                                Toast.makeText(mActivity, Parser.getError(response), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            mActivity.showLoading(false);
                            Toast.makeText(mActivity, responseString, Toast.LENGTH_SHORT).show();
                            mAdapMember.setData(null);
                            mAdapMember.notifyDataSetChanged();
                        }
                    });
                } else {
                    mActivity.showLoading(false);
                    mMember = null;
                }
                visibleSpinner(true, spinnerKind);
                mAdapKind.setData(Utility.genPartKind());
                mAdapKind.notifyDataSetChanged();
                mKind = (Member) mAdapKind.getItem(0);
            }
            break;
            case SLKH:
                visibleSpinner(true, spinnerCustomer, spinnerKind);
                getCustomer();
                mAdapKind.setData(Utility.genPartKind());
                notifyDataChange(mAdapKind);
                mKind =(Member)mAdapKind.getItem(0);
                mActivity.showLoading(false);
                break;
            case SLTV: {
                lnDate.setVisibility(View.GONE);
                visibleSpinner(true, spinnerMember);
                if (role != MainActivity.Role.CHIEF) {
                    mActivity.showLoading(true);
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.setCookieStore(mActivity.getCookieStore());
                    client.get(Define.CHIEF_LIST_URL, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            mActivity.showLoading(false);
                            Log.d("minh", "CHIEF_LIST_URL: " + response);
                            if (Parser.isSuccess(response)) {
                                mAdapMember.setData(Parser.parseMember(response.optJSONArray("Result"))).notifyDataSetChanged();
                                mMember = (Member) mAdapMember.getItem(0);
                            } else {
                                // show error
                                Toast.makeText(mActivity, Parser.getError(response), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            mActivity.showLoading(false);
                            Log.d("minh", "CHIEF_LIST_URL- error: " + responseString);
                            mAdapMember.setData(null);
                            mAdapMember.notifyDataSetChanged();
                        }
                    });
                } else {
                    ArrayList<Member> temp = new ArrayList<>();
                    temp.add(new Member("Me",Utility.getString(mActivity,"saleNo")));
                    mAdapMember.setData(temp).notifyDataSetChanged();
                    mMember = (Member) mAdapMember.getItem(0);
                }
                visibleSpinner(true, spinnerCustomer, spinnerKind);
                mAdapCus.setData(Utility.genCustType());
                getLabelFlags(mAdapKind);
                notifyDataChange(mAdapKind, mAdapCus);
                mCustomer = (Customer) mAdapCus.getItem(0);
                mKind = (Member) mAdapKind.getItem(0);
            }
            break;
            default:{
                mMember = null;
                mKind = null;
                visibleSpinner(false, spinnerCustomer, spinnerMember, spinnerKind, spinnerProduct, spinnerP1, spinnerP2);
                break;
            }
        }
    }

    private void handleSubmit() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Utility.isOnline(mActivity)){
                    Toast.makeText(mActivity, "Please connect Internet to submit data.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((TextUtils.isEmpty(txDateFrom.getText()) || TextUtils.isEmpty(txDateTo.getText())) && reportType != Reports.SLTV) {
                    Toast.makeText(mActivity, "Date field not empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!Utility.isGreater(txDateTo.getText()+"",txDateFrom.getText()+"")){
                    Toast.makeText(mActivity, "To date should greater than From date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(reportType == Reports.NONE){
                    Toast.makeText(mActivity, "Please choose kind of report", Toast.LENGTH_SHORT).show();
                    return;
                }
                switch (reportType) {
                    case SLGD:
                        mActivity.addFragment(new FrgSLGDReport().setData(getGDArg()), true);
                        break;
                    case SLKH:
                        mActivity.addFragment(new FrgSLKHReport().setData(mCustomer, mKind, txDateFrom.getText() + "", txDateTo.getText() + ""), true);
                        break;
                    case SLTT:
                        mActivity.addFragment(new FrgSLTTReport().setData(mMember, mKind, txDateFrom.getText() + "", txDateTo.getText() + ""), true);
                        break;
                    case SLTV:
                        mActivity.addFragment(new FrgSLTVReport().setData(mCustomer, mMember, mKind, txDateFrom.getText() + "", txDateTo.getText() + ""), true);
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
                txDateFrom.setText(Utility.convertDate(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        // init onClick

        txDateFrom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    toDatePickerDialog.show();
                }
                return false;
            }
        });

        toDatePickerDialog2 = new DatePickerDialog(mActivity, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                txDateTo.setText(Utility.convertDate(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        // init onClick

        txDateTo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    toDatePickerDialog2.show();
                }
                return false;
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

    public enum Reports {
        NONE,SLKH, SLTT, SLTV, SLGD
    }
}
