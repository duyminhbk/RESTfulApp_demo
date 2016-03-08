package com.app.restfulapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.restfulapp.models.Customer;
import com.app.restfulapp.models.Member;
import com.app.restfulapp.ultis.Utility;

/**
 * Created by minhpham on 12/16/15.
 */
public abstract class BaseFrg extends Fragment {
    protected View rootView;
    protected MainActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(defineLayout(), container, false);
        mActivity = (MainActivity) getActivity();
        initView();
        return rootView;
    }

    protected View findViewById(int id){
        return rootView==null?null:rootView.findViewById(id);
    }

    protected abstract void initView();

    public String getName(){
        return getClass().getName();
    }

    protected abstract int defineLayout();

    @Override
    public void onPause() {
        super.onPause();
//        Utility.hideKeyboard(mActivity);
    }

}
