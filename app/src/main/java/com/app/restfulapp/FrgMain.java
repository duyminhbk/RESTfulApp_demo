package com.app.restfulapp;

import android.app.DatePickerDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.restfulapp.ultis.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

/**
 * Created by minhpham on 12/16/15.
 */
public class FrgMain extends BaseFrg {
    private static final String SUBMIT_DATA_URL = "http://visitme.cloudapp.net:83/Home/GetSaleData";
    private String params = "?SaleNo=%s&Date=%s";
    private EditText etDate;
    private Button btnSubmit;
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog toDatePickerDialog;
    private TextView txContent;

    @Override
    protected void initView() {
        // find view
        etDate = (EditText) rootView.findViewById(R.id.ed_date);
        btnSubmit = (Button) rootView.findViewById(R.id.btnSubmit);
        txContent = (TextView) rootView.findViewById(R.id.tx_content);

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

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etDate.getText())) {
                    return;
                }
                mActivity.showLoading(true);
                String url = SUBMIT_DATA_URL + String.format(params, Utility.getString(mActivity, "code"), etDate.getText().toString());


                // Make RESTful webservice call using AsyncHttpClient object
                AsyncHttpClient client = new AsyncHttpClient();

                client.get(url, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        txContent.setText(new String(responseBody));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
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
