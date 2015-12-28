package com.app.restfulapp;

import android.app.DatePickerDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.restfulapp.ultis.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

/**
 * Created by minhpham on 12/16/15.
 */
public class FrgMain extends BaseFrg {
    private static final String SUBMIT_DATA_URL = "http://visitme.cloudapp.net:83/Home/GetSaleData";
    // private static final String SUBMIT_DATA_URL = "http://192.168.1.103:83/Home/GetSaleData";
    private String params = "?SaleNo=%s&Date1=%s&Date2=%s";
    private EditText etDate;
    private EditText etDate2;
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

    @Override
    protected void initView() {

        // find view
        etDate = (EditText) rootView.findViewById(R.id.ed_date);
        etDate2 = (EditText) rootView.findViewById(R.id.ed_date2);
//        etSal = (EditText) rootView.findViewById(R.id.ed_sal);

        txtSaleName = (TextView) rootView.findViewById(R.id.txtSaleName);
        txtSaleName.setText(Utility.getString(mActivity, "saleName"));

        txtTotal = (TextView) rootView.findViewById(R.id.txtTotal);

        btnSubmit = (Button) rootView.findViewById(R.id.btnSubmit);
        lvContent = (ListView) rootView.findViewById(R.id.lv_content);
        txEmpty = (TextView) rootView.findViewById(R.id.tv_empty);

        mData = new ArrayList<String>();
        listAdapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1, mData);
        lvContent.setAdapter(listAdapter);
        //init date picker
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        Calendar newCalendar = Calendar.getInstance();

        toDatePickerDialog = new DatePickerDialog(mActivity, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        // init onClick
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDatePickerDialog.show();
            }
        });

        toDatePickerDialog2 = new DatePickerDialog(mActivity, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etDate2.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        // init onClick
        etDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDatePickerDialog2.show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etDate.getText()) || TextUtils.isEmpty(etDate2.getText())) {
                    return;
                }

                mActivity.showLoading(true);

                String saleNo = Utility.getString(mActivity, "saleNo");
                // String url = SUBMIT_DATA_URL + String.format(params, etSal.getText().toString(), etDate.getText().toString());
                String url = SUBMIT_DATA_URL + String.format(params, saleNo, etDate.getText().toString(), etDate2.getText().toString());

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
                            Toast.makeText(mActivity,"can't parse data !!!",Toast.LENGTH_SHORT).show();
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

    @Override
    public String getName() {
        return "FrgMain";
    }

    @Override
    protected int defineLayout() {
        return R.layout.frg_main;
    }
}
