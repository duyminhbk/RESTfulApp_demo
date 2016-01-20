package com.app.restfulapp;

import android.app.DatePickerDialog;
import android.text.TextUtils;
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

import com.app.restfulapp.reports.FrgSLGDReport;
import com.app.restfulapp.reports.FrgSLKHReport;
import com.app.restfulapp.reports.FrgSLTTReport;
import com.app.restfulapp.reports.FrgSLTVReport;
import com.app.restfulapp.ultis.Define;
import com.app.restfulapp.ultis.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

/**
 * Created by minhpham on 12/16/15.
 */
public class FrgMain extends BaseFrg {
    private String params = "?SaleNo=%s&Date1=%s&Date2=%s";
    private TextView txDateFrom;
    private TextView txDateTo;
    private Button btnSubmit;
    private SimpleDateFormat dateFormatter;

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
    private Reports report_type = Reports.NONE;

    public String getID(String item) {
        String id="";
        try {
            JSONObject json = new JSONObject(item);
            id = json.optString("CustNo");
        }catch (Exception e){

        }
        return id;
    }

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
//        etSal = (EditText) rootView.findViewById(R.id.ed_sal);

        txtSaleName = (TextView) rootView.findViewById(R.id.txtSaleName);
        txtSaleName.setText(Utility.getString(mActivity, "saleName"));

        txtTotal = (TextView) rootView.findViewById(R.id.txtTotal);

        btnSubmit = (Button) rootView.findViewById(R.id.btnSubmit);
        lvContent = (ListView) rootView.findViewById(R.id.lv_content);
        txEmpty = (TextView) rootView.findViewById(R.id.tv_empty);

        initList();
        initDatePicker();
        initSpinner();
        handleSubmit();
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mActivity, R.array.report_type, android.R.layout.simple_spinner_dropdown_item);
        spinnerReport.setAdapter(adapter);

        spinnerReport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                report_type = Reports.values()[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void handleSubmit() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(txDateFrom.getText()) || TextUtils.isEmpty(txDateTo.getText())) {
                    return;
                }

                mActivity.showLoading(true);

                String saleNo = Utility.getString(mActivity, "saleNo");
                String url = Define.SUBMIT_DATA_URL + String.format(params, saleNo, txDateFrom.getText().toString(), txDateTo.getText().toString());

                // Make RESTful webservice call using AsyncHttpClient object
                AsyncHttpClient client = new AsyncHttpClient();
                client.setCookieStore(mActivity.getCookieStore());
                client.get(url, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        mActivity.showLoading(false);
                        try {
                            JSONArray arr = new JSONArray(new String(responseBody));

                            mData.clear();

                            if (arr.length()>0){
                                lvContent.setVisibility(View.VISIBLE);
                                txEmpty.setVisibility(View.GONE);
                                for(int i = 0;i<arr.length();i++){

                                    if(i < arr.length() - 1) {
                                        mData.add(arr.optJSONObject(i).toString());
                                    } else {
                                        txtTotal.setText("Total: " + arr.optJSONObject(i).optString("SumTotalQty", "_"));
                                    }
                                }
                            }else{
                                lvContent.setVisibility(View.GONE);
                                txEmpty.setVisibility(View.VISIBLE);
                                txtTotal.setText("Total: 0");
                            }
                            listAdapter.notifyDataSetChanged();
                        }catch (Exception e){
                            Toast.makeText(mActivity, "can't parse data !!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        mActivity.showLoading(false);
                        Toast.makeText(mActivity, "get data fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void initDatePicker() {
        //init date picker
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        Calendar newCalendar = Calendar.getInstance();

        toDatePickerDialog = new DatePickerDialog(mActivity, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                txDateFrom.setText(dateFormatter.format(newDate.getTime()));
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
                txDateTo.setText(dateFormatter.format(newDate.getTime()));
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
                switch (report_type){
                    case SLGD:
                        mActivity.addFragment(new FrgSLGDReport().setData(getID(listAdapter.getItem(position)),txDateFrom.getText()+"",txDateTo.getText()+""),true);
                        break;
                    case SLKH:
                        mActivity.addFragment(new FrgSLKHReport().setData(getID(listAdapter.getItem(position)),txDateFrom.getText()+"",txDateTo.getText()+""),true);
                        break;
                    case SLTT:
                        mActivity.addFragment(new FrgSLTTReport().setData(getID(listAdapter.getItem(position)),txDateFrom.getText()+"",txDateTo.getText()+""),true);
                        break;
                    case SLTV:
                        mActivity.addFragment(new FrgSLTVReport().setData(getID(listAdapter.getItem(position)),txDateFrom.getText()+"",txDateTo.getText()+""),true);
                        break;
                }
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
