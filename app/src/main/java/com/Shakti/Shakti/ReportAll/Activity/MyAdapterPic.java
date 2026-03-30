package com.Shakti.Shakti.ReportAll.Activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.Shakti.Shakti.Dashbord.ui.VerifySiteActivity;
import com.Shakti.Shakti.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class MyAdapterPic extends BaseAdapter {
    private Context context;
    private ArrayList<String> arrayList;
    private CircleImageView imageView1;
    String type;
    public MyAdapterPic(Context context, ArrayList<String> arrayList,String type) {
        this.context = context;
        this.arrayList = arrayList;
        this.type = type;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.rowpic, parent, false);
        imageView1 = convertView.findViewById(R.id.imageView1);
         Log.e("arrayList","111:   "+arrayList.get(position));



        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.customer_support);
        requestOptions.error(R.drawable.customer_support);


        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(arrayList.get(position))
                .into(imageView1);


        return convertView;


    }
}
