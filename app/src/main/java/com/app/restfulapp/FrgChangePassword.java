package com.app.restfulapp;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.restfulapp.ultis.AppClientRequest;
import com.app.restfulapp.ultis.Define;
import com.app.restfulapp.ultis.Parser;
import com.app.restfulapp.ultis.Utility;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by minhpham on 3/9/16.
 */
public class FrgChangePassword extends BaseFrg {
    private TextView txPass;
    private TextView txNewPass;
    private TextView txConfirmPass;
    private Button btSubmit;

    @Override
    protected void initView() {
        txPass = (TextView) findViewById(R.id.et_pass);
        txNewPass = (TextView) findViewById(R.id.et_new_pass);
        txConfirmPass = (TextView) findViewById(R.id.et_confirm_pass);
        btSubmit = (Button) findViewById(R.id.bt_submit);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateAndAlert()){
                    if(Utility.isOnline(mActivity)){
                        doSubmit(txPass.getText()+"",txNewPass.getText()+"");
                    }else{
                        Toast.makeText(mActivity,R.string.connect_warning, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean validateAndAlert() {
        String pass = Utility.getString(mActivity, "pass");
        if(!pass.equals(txPass.getText()+"")){
            Toast.makeText(mActivity,"Please input the right current password",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!(txNewPass.getText()+"").equals(txConfirmPass.getText()+"")){
            Toast.makeText(mActivity,"New password and Confirm password must be the same",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void doSubmit(String oldpass,String newpass) {
        mActivity.showLoading(true);
        AppClientRequest.get(mActivity,String.format(Define.CHANGE_PASS, oldpass, newpass), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("minh", "GET_CUSTOMERS_URL: " + response);

                if (Parser.isSuccess(response)) {
                    Utility.saveString(mActivity, "pass", txNewPass.getText() + "");
                    Toast.makeText(mActivity, "Update password sucessfully", Toast.LENGTH_SHORT).show();
                } else {
                    // show error
                    if(statusCode == 401 || statusCode == 404)
                        Toast.makeText(mActivity, "Session timeout. Please re-login and try again.", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(mActivity, Parser.getError(response), Toast.LENGTH_SHORT).show();
                }
                mActivity.showLoading(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                mActivity.showLoading(false);
                Toast.makeText(mActivity, "status :" + statusCode + " error: " + errorResponse + "", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    protected int defineLayout() {
        return R.layout.frg_change_pass;
    }
}
