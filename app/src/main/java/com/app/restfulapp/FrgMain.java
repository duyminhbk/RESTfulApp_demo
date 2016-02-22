package com.app.restfulapp;

import android.app.DatePickerDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
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
    private AdapMember mAdapProduct;
    private AdapMember mAdapP1;
    private AdapMember mAdapP2;

    private Member mMember;
    private Customer mCustomer;
    private Member mKind;
    private MainActivity.Role role;
    private Spinner spinnerP1;
    private Spinner spinnerP2;
    private Spinner spinnerProduct;

    private Member mProduct;
    private Member mP2;
    private Member mP1;

    public String[] getGDArg() {
        //{ cust_type, label_flag, p_1, p_2, product_no, tc_date, PeriodType }
        String[] result = new String[8];
        mAdapCus.setData(Utility.genCustType());
        mAdapMember.setData(Utility.genFlag());
        mAdapKind.setData(Utility.genPeriodType());
        mAdapProduct.setData(Utility.genProduct());
        mAdapP1.setData(Utility.genP1());
        mAdapP2.setData(Utility.genP2());

        result[0] = mCustomer.getCustName();
        result[1] = mMember.getCode();
        result[2] = mP1.getCode();
        result[3] = mP2.getCode();
        result[4] = mProduct.getName();
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
        }
        if (getString(R.string.slgd_title).equals(kind)) {
            reportType = Reports.SLGD;
        }
    }

    @Override
    protected void initView() {
        role = mActivity.getRole();
        // find view
        txDateFrom = (TextView) rootView.findViewById(R.id.ed_date_from);
        txDateTo = (TextView) rootView.findViewById(R.id.ed_date_to);
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
        getCustomer();
        handleSubmit();
    }

    private void getCustomer() {
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //init spinner kind
        mAdapP2 = new AdapMember(mActivity);
        spinnerP2.setAdapter(mAdapP2);
        spinnerP2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mP2 = (Member) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //init spinner kind
        mAdapProduct = new AdapMember(mActivity);
        spinnerProduct.setAdapter(mAdapProduct);
        spinnerProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mProduct = (Member) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void updateMember() {
        mMember = null;
        mKind = null;
        spinnerCustomer.setVisibility(View.GONE);
        spinnerMember.setVisibility(View.GONE);
        spinnerKind.setVisibility(View.GONE);
        spinnerProduct.setVisibility(View.GONE);
        spinnerP1.setVisibility(View.GONE);
        spinnerP2.setVisibility(View.GONE);
        mActivity.showLoading(true);
        switch (reportType) {
            // reuse spinner customer and member to define cust_type and label_flag
            case SLGD:
                spinnerCustomer.setVisibility(View.VISIBLE);
                spinnerMember.setVisibility(View.VISIBLE);
                spinnerKind.setVisibility(View.VISIBLE);
                spinnerProduct.setVisibility(View.VISIBLE);
                spinnerP1.setVisibility(View.VISIBLE);
                spinnerP2.setVisibility(View.VISIBLE);

                mAdapCus.setData(Utility.genCustType());
                mAdapMember.setData(Utility.genFlag());
                mAdapKind.setData(Utility.genPeriodType());
                mAdapProduct.setData(Utility.genProduct());
                mAdapP1.setData(Utility.genP1());
                mAdapP2.setData(Utility.genP2());

                mAdapMember.notifyDataSetChanged();
                mAdapCus.notifyDataSetChanged();
                mAdapKind.notifyDataSetChanged();
                mAdapProduct.notifyDataSetChanged();
                mAdapP1.notifyDataSetChanged();
                mAdapP2.notifyDataSetChanged();

                mMember = (Member) spinnerMember.getSelectedItem();
                mCustomer = (Customer) spinnerCustomer.getSelectedItem();
                mKind = (Member) spinnerKind.getSelectedItem();
                mProduct = (Member) spinnerProduct.getSelectedItem();
                mP1 = (Member) spinnerP1.getSelectedItem();
                mP2 = (Member) spinnerP2.getSelectedItem();

                mActivity.showLoading(false);
                break;
            case SLTT: {
                if (role != MainActivity.Role.SALE) {
                    spinnerMember.setVisibility(View.VISIBLE);
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.setCookieStore(mActivity.getCookieStore());
                    client.get(Define.SALEMAN_LIST_URL, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            mActivity.showLoading(false);
                            Log.d("minh", "SALEMAN_LIST_URL: " + response);
                            if (Parser.isSuccess(response)) {
                                mAdapMember.setData(Parser.parseMember(response.optJSONArray("Result"))).notifyDataSetChanged();
                            } else {
                                // show error
                                Toast.makeText(mActivity, Parser.getError(response), Toast.LENGTH_SHORT).show();
                            }
                            mMember = (Member) spinnerMember.getSelectedItem();
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
                spinnerKind.setVisibility(View.VISIBLE);
                mAdapKind.setData(Utility.genPartKind());
                mAdapKind.notifyDataSetChanged();
                mKind = (Member) spinnerKind.getSelectedItem();
            }
            break;
            case SLKH:
                spinnerCustomer.setVisibility(View.VISIBLE);
                spinnerKind.setVisibility(View.VISIBLE);
                mAdapKind.setData(Utility.genPartKind());
                mAdapKind.notifyDataSetChanged();
                mKind = (Member) spinnerMember.getSelectedItem();
                mActivity.showLoading(false);
                break;
            case SLTV: {
                if (role != MainActivity.Role.CHIEF) {
                    spinnerMember.setVisibility(View.VISIBLE);
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.setCookieStore(mActivity.getCookieStore());
                    client.get(Define.CHIEF_LIST_URL, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            mActivity.showLoading(false);
                            Log.d("minh", "CHIEF_LIST_URL: " + response);
                            if (Parser.isSuccess(response)) {
                                mAdapMember.setData(Parser.parseMember(response.optJSONArray("Result"))).notifyDataSetChanged();
                            } else {
                                // show error
                                Toast.makeText(mActivity, Parser.getError(response), Toast.LENGTH_SHORT).show();
                            }
                            mMember = (Member) spinnerMember.getSelectedItem();
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
                    mActivity.showLoading(false);
                    mMember = null;
                }
                spinnerCustomer.setVisibility(View.VISIBLE);
                mAdapCus.setData(Utility.genCustType());
                spinnerKind.setVisibility(View.VISIBLE);
                mAdapKind.setData(Utility.genFlag());
                mAdapKind.notifyDataSetChanged();
                mAdapCus.notifyDataSetChanged();
                mCustomer = (Customer) spinnerCustomer.getSelectedItem();
                mKind = (Member) spinnerKind.getSelectedItem();
            }
            break;
        }
    }

    private void handleSubmit() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(txDateFrom.getText()) || TextUtils.isEmpty(txDateTo.getText())) {
                    Toast.makeText(mActivity, "Date field not empty", Toast.LENGTH_SHORT).show();
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
        NONE, SLTV, SLGD, SLKH, SLTT
    }
}
