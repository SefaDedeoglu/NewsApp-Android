package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class viewdownload_new extends AppCompatActivity {


    String imgpath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewdownload_new);

        dbHandler mydb = new dbHandler(this);
        ArrayList<String> id,foto,baslik,icerik;
        id = new ArrayList<String>();
        foto = new ArrayList<String>();
        baslik = new ArrayList<String>();
        icerik = new ArrayList<String>();

        Cursor cursor = mydb.readAllData();
        if(cursor.getCount() ==0){
            Toast.makeText(this,"No Downloaded News",Toast.LENGTH_SHORT);
        }
        else{
            while(cursor.moveToNext()){
                id.add(cursor.getString(1));
                foto.add(cursor.getString(2));
                baslik.add(cursor.getString(3));
                icerik.add(cursor.getString(4));

            }
        }
        HashMap hm;
        Intent intent = getIntent();
        String idDown =  intent.getStringExtra("idDown");
        int indis = -1;
        List<HashMap<String,String>> list=new ArrayList<>();
        for(int i=0;i<id.size();i++){
            if(id.get(i).equals(idDown)){
                indis=i;
                break;
            }
        }

        if(indis!=-1){
            TextView title = findViewById(R.id.iddownTitle);
            TextView text = findViewById(R.id.iddowntxt);
          //  ImageView img = (ImageView)findViewById(R.id.downimg);
            title.setText(baslik.get(indis));
            text.setText(icerik.get(indis));
            imgpath =foto.get(indis).toString();
            File imgFile = new  File(foto.get(indis).toString());

            if(imgFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                ImageView myImage = (ImageView)findViewById(R.id.downloadedimg);


                myImage.setImageBitmap(myBitmap);
            }
        }



    }
    public void backbutton(View v){
        super.onBackPressed();
    }
    public void cancelbutton(View v){

        dbHandler mydb = new dbHandler(this);

        Intent intent = getIntent();
        String idDown =  intent.getStringExtra("idDown");
        mydb.deleteRow(idDown);
        File fdelete = new File(imgpath);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                System.out.println("file Deleted :");
            } else {
                System.out.println("file not Deleted :");
            }
        }

        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
    }
}
