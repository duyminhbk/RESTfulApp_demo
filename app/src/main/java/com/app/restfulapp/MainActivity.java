package com.app.restfulapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.app.restfulapp.ultis.Define;
import com.app.restfulapp.ultis.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog prgDialog;
    private PersistentCookieStore cookieStore;

    public enum Role {UNKNOW,DIR,CHIEF,LEAD,SALE};

    private Role role;

    public void setRole(String txRole){
        if(TextUtils.isEmpty(txRole)) role = Role.UNKNOW;
        role = Role.values()[Integer.parseInt(txRole)];
    }

    public Role getRole(){
        return role;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        cookieStore = new PersistentCookieStore(this);
        Boolean isAutoLogin = Utility.getBoolean(this, "auto");
        if(!isAutoLogin) {
            addFragment(new FrgLogin(), false);
        }else{
            DoAutoLogin();
        }

    }

    private void DoAutoLogin() {
        String email = Utility.getString(this, "email");
        String pass = Utility.getString(this, "pass");
        String para = String.format("?Email=%s&Password=%s", email, pass);
        showLoading(true);
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        PersistentCookieStore myCookieStore = this.getCookieStore();
        // clear cookie to make the fresh cookie, to ensure the newest cookie is being send
        myCookieStore.clear();
        // set the new cookie
        client.setCookieStore(myCookieStore);
        client.get(Define.LOGIN_URL + para, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                // Hide Progress Dialog
                showLoading(false);
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    String saleName = obj.optString("sale_ename");
                    Utility.saveSecurity(MainActivity.this, new String(headers[0].getValue()));
                    Utility.saveString(MainActivity.this, "saleName", saleName);
                    setRole(obj.optString("role"));
                    // jump to home screen
                    addFragment(new FrgMain(), false);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(MainActivity.this, "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // Hide Progress Dialog
                showLoading(false);
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(MainActivity.this, "Username or password is incorrect", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(MainActivity.this, "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(MainActivity.this, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void showLoading(boolean flag) {
        if (flag ) {
            prgDialog.show();
        } else {
            prgDialog.hide();
        }
    }

    public void addFragment(BaseFrg frg, boolean shouldAdd) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_container, frg, frg.getTag());
        if (shouldAdd) {
            ft.addToBackStack(frg.getName());
        }
        ft.commit();
    }

    public PersistentCookieStore getCookieStore() {
        return cookieStore;
    }
}
