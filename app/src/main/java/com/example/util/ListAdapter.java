package com.example.util;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    ArrayList<ListData> myList = new ArrayList<ListData>();
    Context context;

    public ListAdapter(Context context, ArrayList<ListData> myList) {
        this.myList = myList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public ListData getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewHolder view = (ListViewHolder) convertView;
        if (view == null) {
            view = new ListViewHolder(context);
        }
        ListData log = getItem(position);
        view.setLog(log);
        return view;
    }
}
