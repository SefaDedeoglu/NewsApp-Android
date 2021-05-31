package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
    public void gobackApp(View v){
        super.onBackPressed();
    }
    public void privacy(View v){ Toast.makeText(this,"We dont have any privacy policy",Toast.LENGTH_SHORT).show();}
}