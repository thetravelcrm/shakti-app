package com.Shakti.Shakti.Dashbord.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.Shakti.Shakti.Dashbord.Activity.ViewsitedetailsActivity;
import com.Shakti.Shakti.R;

import java.util.ArrayList;


public class MyAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> arrayList;
    private TextView  name,veryfiside;
    String type;
    public MyAdapter(Context context, ArrayList<String> arrayList,String type) {
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
        convertView = LayoutInflater.from(context).inflate(R.layout.row, parent, false);
         name = convertView.findViewById(R.id.textView);
        veryfiside = convertView.findViewById(R.id.veryfiside);
         Log.e("arrayList","111:   "+arrayList.get(position));

         if(type.equalsIgnoreCase("1")){

             veryfiside.setVisibility(View.VISIBLE);
             name.setVisibility(View.GONE);

             veryfiside.setText(R.string.View);
             veryfiside.setTextColor(context.getResources().getColorStateList(R.color.white));
             veryfiside.setBackground(context.getResources().getDrawable(R.drawable.rect));
             veryfiside.setBackgroundTintList(context.getResources().getColorStateList(R.color.green));

             veryfiside.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {

                     //Intent i=new Intent(context, VerifySiteActivity.class);
                     Intent i=new Intent(context, ViewsitedetailsActivity.class);
                     i.putExtra("id",""+arrayList.get(position));
                     context.startActivity(i);

                 }
             });



         }
         else if(type.equalsIgnoreCase("2"))         {
             veryfiside.setVisibility(View.VISIBLE);
             name.setVisibility(View.GONE);

             veryfiside.setText(R.string.Verify);
             veryfiside.setTextColor(context.getResources().getColorStateList(R.color.white));
             veryfiside.setBackground(context.getResources().getDrawable(R.drawable.rect));
             veryfiside.setBackgroundTintList(context.getResources().getColorStateList(R.color.green));
             veryfiside.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {

                     Intent i=new Intent(context, VarifypurchaseDealerActivity.class);
                     i.putExtra("id",""+arrayList.get(position));
                     i.putExtra("position",""+position);
                     context.startActivity(i);

                 }
             });
         }
         else {

            if(arrayList!=null) {
                Log.e("SiteReportres", "Statuslist : " + arrayList.size());
                Log.e("SiteReportres", "position : " + position);
                Log.e("SiteReportres", "position  get: " + arrayList.get(position));
                name.setText(arrayList.get(position));

                if (arrayList.get(position).equalsIgnoreCase("pending")) {
                    name.setTextColor(context.getResources().getColorStateList(R.color.black));
                    name.setText(R.string.Pending);
                    name.setBackgroundTintList(context.getResources().getColorStateList(R.color.yallow));


                } else if (arrayList.get(position).equalsIgnoreCase("active")) {
                    name.setText(R.string.Approved);
                    name.setTextColor(context.getResources().getColorStateList(R.color.white));
                    name.setBackgroundTintList(context.getResources().getColorStateList(R.color.green_jungel));
                } else if (arrayList.get(position).equalsIgnoreCase("rejected")) {
                    name.setText(R.string.Rejected);
                    name.setTextColor(context.getResources().getColorStateList(R.color.white));
                    name.setBackgroundTintList(context.getResources().getColorStateList(R.color.red_Imperial));
                }
                else if (arrayList.get(position).equalsIgnoreCase("delivered")) {
                    name.setText(R.string.Delevered);
                    name.setTextColor(context.getResources().getColorStateList(R.color.white));
                    name.setBackgroundTintList(context.getResources().getColorStateList(R.color.blue));
                }
                else if (arrayList.get(position).equalsIgnoreCase("approved")) {
                    name.setText(R.string.Approved);
                    name.setTextColor(context.getResources().getColorStateList(R.color.white));
                    name.setBackgroundTintList(context.getResources().getColorStateList(R.color.green_jungel));
                }
            }

         }


        return convertView;


    }
}