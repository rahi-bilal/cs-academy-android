package com.ansaridroid.csacademy.util.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ansaridroid.csacademy.R;

import java.util.List;

public class NavListAdapter extends ArrayAdapter<String> {
    private String data[];
    private Context context;
    private int layoutResourceId;


    public NavListAdapter(Context context, int layoutResourceId, String[] data) {
        super(context, layoutResourceId, data);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        TextView textView = listItem.findViewById(R.id.lesson_title_list_text_view);
        textView.setText(data[position]);

        return listItem;
    }
}
