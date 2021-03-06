package com.app.restfulapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.restfulapp.ultis.AppClientRequest;
import com.app.restfulapp.ultis.Define;
import com.app.restfulapp.ultis.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by minhpham on 12/16/15.
 */
public class FrgLogin extends BaseFrg implements View.OnClickListener {

    //private static final String LOGIN_URL = "http://192.168.1.103:83/Home/Login";
    private TextView errorMsg;
    private EditText emailET;
    private EditText pwdET;
    private EditText serverAddressET;
    private Button btnLogin;
    private CheckBox cbAuto;
    private TextView txDeviceID;

    @Override
    protected void initView() {
        // Find Error Msg Text View control by ID
        errorMsg = (TextView) rootView.findViewById(R.id.login_error);
        txDeviceID = (TextView) rootView.findViewById(R.id.tx_deviceId);
        // Find Email Edit View control by ID
        emailET = (EditText) rootView.findViewById(R.id.loginEmail);
        // Find Password Edit View control by ID
        pwdET = (EditText) rootView.findViewById(R.id.loginPassword);
        // Find Server Address View control by ID
        serverAddressET = (EditText) rootView.findViewById(R.id.serverAddress);
        // Find login button
        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        txDeviceID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mActivity)
                        .setTitle("Your device ID")
                        .setMessage(Utility.getDeviceId(mActivity))
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }

                        })
                        .show();
            }
        });

        mActivity.getSupportActionBar().setTitle(R.string.app_name);
        cbAuto = (CheckBox)findViewById(R.id.cb_auto);

        String serverAddress = Utility.getString(mActivity, "serverAddress");
        serverAddressET.setText(serverAddress);

        // TODO_: DEBUG
//        emailET.setText("3188");
//        pwdET.setText("Wh4tTh3Heo");
    }

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param email
     * @param pass
     */
    public void invokeWS(String email,String pass) {
        // Show Progress Dialog
        mActivity.showLoading(true);
        // clear cookie to make the fresh cookie, to ensure the newest cookie is being send
        mActivity.getCookieStore().clear();
        AppClientRequest.get(mActivity,String.format(Define.LOGIN_URL, email, pass, Utility.getDeviceId(mActivity)), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                // Hide Progress Dialog
                mActivity.showLoading(false);

                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    String saleName = obj.optString("sale_ename");
                    Utility.saveSecurity(mActivity, new String(headers[0].getValue()));
                    Utility.saveString(mActivity, "saleNo", emailET.getText().toString());
                    Utility.saveString(mActivity, "saleName", saleName);
                    mActivity.setRole(obj.optString("role"));
                    Utility.saveString(mActivity, "email", emailET.getText().toString());
                    Utility.saveString(mActivity, "pass", pwdET.getText().toString());
                    Utility.saveString(mActivity, "serverAddress", serverAddressET.getText().toString());
                    Utility.saveBool(mActivity, "auto", cbAuto.isChecked());

                    goHomeScreen();

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(mActivity, "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // Hide Progress Dialog
                mActivity.showLoading(false);
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(mActivity, "Username or password is incorrect", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(mActivity, "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(mActivity, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public String getName() {
        return "FrgLogin";
    }

    @Override
    protected int defineLayout() {
        return R.layout.frg_login;
    }

    private void goHomeScreen() {
        mActivity.addFragment(new FrgMain(), false);
    }

    @Override
    public void onClick(View v) {
        Utility.hideKeyboard(mActivity);
//        goHomeScreen();
        // Get Email Edit View Value
        String email = emailET.getText().toString();

        // Get Password Edit View Value
        String password = pwdET.getText().toString();

        // Get Password Edit View Value
        String serverAddress = serverAddressET.getText().toString();

        // Save server address to Util
        Define.AssignServerAddress(serverAddress);

        if(!Utility.isOnline(mActivity)){
            Toast.makeText(mActivity, R.string.connect_warning, Toast.LENGTH_SHORT).show();
            return;
        }
        // When Email Edit View and Password Edit View have values other than Null
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            invokeWS(email, password);
        } else {
            Toast.makeText(mActivity, "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.logout).setVisible(false);
        menu.findItem(R.id.setting).setVisible(false);
    }
}
