package com.example.newsapp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<HashMap<String,String>> {

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();

            Drawable d = Drawable.createFromStream(is, "any_image_name");
            return d;
        } catch (Exception e) {

            return null;
        }
    }


        public CustomAdapter(Context context, List<HashMap<String,String>> books) {
            super(context, 0, books);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if(row == null) {
                row = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_filter, parent, false);
            }

            TextView txt = row.findViewById(R.id.newTitle);
            TextView txt2 = row.findViewById(R.id.newDescription);
            ImageView photo = (ImageView)row.findViewById(R.id.newImage);
            final HashMap<String,String> hm = getItem(position);

            String url="";
                url = hm.get("ImagePath");


/*
            try {
                URL newurl = new URL(url);
                photo.setImageBitmap( BitmapFactory.decodeStream(newurl.openConnection().getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            */


            //   photo.setImageResource(R.drawable.deneme);
/*
                Drawable drawable = LoadImageFromWebOperations(url);
                Log.i("TAG!", "" + drawable);
                if (drawable != null) {
                    photo.setImageDrawable(drawable);
                }
                */
            LinearLayout.LayoutParams paramWithoutPic = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    5
            );

            LinearLayout.LayoutParams paramWithPic = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    3
            );


            LinearLayout ly = row.findViewById(R.id.nonimglayout);
                if(!url.equals("0")) {
                    ly.setLayoutParams(paramWithPic);
                    photo.setVisibility(row.VISIBLE);
                    Picasso.get().load(url).into(photo);
                }
                else{
                    ly.setLayoutParams(paramWithoutPic);
                    photo.setVisibility(row.GONE);
                }


            txt.setText(hm.get("Title"));
            txt2.setText(hm.get("Description"));

            return row;
        }

}
