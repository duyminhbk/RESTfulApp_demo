package com.app.restfulapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.app.restfulapp.models.Member;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by minhpham on 1/28/16.
 */
public class AdapMember extends BaseAdapter {

    private final Context mContext;
    private List<Member> mData;

    public AdapMember(Context context) {
        this.mContext = context;
        genNoData();
    }

    private void genNoData() {
        mData = new ArrayList<Member>();
        mData.add(new Member("Alert - No Data",""));
    }

    public AdapMember setData(List<Member> data){
        if(data == null){
            genNoData();
            return this;
        }
        this.mData = data;
        return this;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class Holder{

        public TextView name;
        public TextView code;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        Member member = mData.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_member_list,parent,false);
            holder = new Holder();
            holder.name = (TextView)convertView.findViewById(R.id.tx_name);
            holder.code = (TextView)convertView.findViewById(R.id.tx_code);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        holder.name.setText(member.getName());
        holder.code.setText(member.getCode());
        return convertView;
    }
}
