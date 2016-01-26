package com.app.restfulapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.loopj.android.http.PersistentCookieStore;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog prgDialog;
    private PersistentCookieStore cookieStore;

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
        addFragment(new FrgLogin(), false);

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
