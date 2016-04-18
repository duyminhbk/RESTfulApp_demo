package com.app.restfulapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.app.restfulapp.ultis.AppClientRequest;
import com.app.restfulapp.ultis.Define;
import com.app.restfulapp.ultis.Utility;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog prgDialog;
    private PersistentCookieStore cookieStore;

    public enum Role {GEN, DIR, CHIEF, LEAD, SALE};

    private Role role;
    private FrgMain frgMain;

    public void setRole(String txRole){
        if(TextUtils.isEmpty(txRole)) role = Role.SALE;
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
        // Boolean isAutoLogin = Utility.getBoolean(this, "auto");
        Boolean isAutoLogin = false;

        if(!isAutoLogin) {
            addFragment(new FrgLogin(), false);
        }else{
            DoAutoLogin();
        }
    }

    private void DoAutoLogin() {
        if(!Utility.isOnline(this)){
            Toast.makeText(this, R.string.connect_warning, Toast.LENGTH_SHORT).show();
            addFragment(new FrgLogin(), false);
        }
        String email = Utility.getString(this, "email");
        String pass = Utility.getString(this, "pass");
        showLoading(true);
        // Make RESTful webservice call using AsyncHttpClient object
        PersistentCookieStore myCookieStore = this.getCookieStore();
        // clear cookie to make the fresh cookie, to ensure the newest cookie is being send
        myCookieStore.clear();
        // set the new cookie
        AppClientRequest.get(this,String.format(Define.LOGIN_URL, email, pass, email), new AsyncHttpResponseHandler() {
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
                    frgMain = new FrgMain();
                    addFragment(frgMain, false);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.logout:
                Utility.saveBool(this,"auto",false);
                addFragment(new FrgLogin(), false);
                return true;
            case R.id.setting:
                addFragment(new FrgChangePassword(), true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    //rest of app
    }

    public void showLoading(boolean flag) {
        if (flag ) {
            prgDialog.show();
        } else {
            prgDialog.hide();
        }
    }

    public void addFragment(BaseFrg frg, boolean shouldAdd) {
        if(frg instanceof FrgLogin){
            FragmentManager fm =getSupportFragmentManager();
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
        }
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
