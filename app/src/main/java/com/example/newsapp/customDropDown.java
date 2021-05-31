package com.example.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;


public class customDropDown extends ArrayAdapter<String> {

    public customDropDown(Context context, List<String> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (convertView == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_list, parent, false);

        }
        String currentItem = getItem(position);
        TextView tv = v.findViewById(R.id.dropdown_menu_item);
        tv.setText(currentItem);


        return v;
    }
}