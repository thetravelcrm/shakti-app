package com.Shakti.Shakti.ReportAll.Activity;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Helpernew {
        public static void getListViewSize(ListView myListView) {
        ListAdapter myListAdapter=myListView.getAdapter();
        if (myListAdapter==null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight=0;
        for (int size=0; size < myListAdapter.getCount(); size++) {
            View listItem=myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight+=listItem.getMeasuredHeight();
        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params=myListView.getLayoutParams();
        params.height=totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
        // print height of adapter on log
        Log.i("height of listItem:", String.valueOf(totalHeight));
    }

        public static void setListViewWidth(ListView listview, LinearLayout Li, int MinWidth)
        {
            ListAdapter la=listview.getAdapter();
            Log.e("ListWidth","null");
            if(la==null){return;}
            int maxWidth=0;
            for (int size=0;size<listview.getCount();size++)
            {
                View listItem=la.getView(size, null, listview);
                listItem.measure(0,0);
                Log.e("ListWidth",String.valueOf(listItem.getMeasuredWidth()));
                if(listItem.getMeasuredWidth()>maxWidth)
                maxWidth=listItem.getMeasuredWidth();
            }
            if(maxWidth<MinWidth)
            {
                maxWidth=MinWidth;
            }

            ViewGroup.LayoutParams params=Li.getLayoutParams();
            params.width=maxWidth;
            Li.setLayoutParams(params);
            Log.e("ListWidth",String.valueOf(maxWidth));
        }

        public static void setLinearLayoutStaticWidth(LinearLayout Li,int Width)
        {
            ViewGroup.LayoutParams params=Li.getLayoutParams();
            params.width=Width;
            Li.setLayoutParams(params);
        }


    }


