package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static boolean bool = true;
    List<String> ids = new ArrayList<String>();
    List<HashMap<String,String>> list=new ArrayList<>();
    HashMap hm;
    FirebaseFirestore db;
    FirebaseFirestoreSettings settings;
    Context cx;
    TextView txt;
    Fragment curr;
    Fragment frghome = new homepage();
    Fragment frgFilter = new filter();
    Fragment frgdown = new download();
    Fragment frgsettings = new settings();

    public void getData(){
        ListView lv = (ListView)findViewById(R.id.ScrollLayout);
        String[] custom={"ImagePath","Title","Description"};
        int[] customId={R.id.newImage,R.id.newTitle,R.id.newDescription};
        CustomAdapter adapter = new CustomAdapter(cx,list);
        // SimpleAdapter adapter=new SimpleAdapter( getContext() ,list,R.layout.custom_list_filter,custom,customId);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap hm= list.get(position);

                Intent readNews = new Intent(getApplicationContext(), readnews.class);
                readNews.putExtra("id",ids.get(position));
                startActivity(readNews);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION},1);
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE},1);

        startService(new Intent(this,notifyService.class));


        super.onCreate(savedInstanceState);

        cx=this;
        setContentView(R.layout.activity_main);
        list.clear();
        db = FirebaseFirestore.getInstance();
        settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        db.collection("News")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                hm=new HashMap();
                                hm.put("ImagePath",document.get("haberFoto").toString());
                                hm.put("Title",document.get("haberBaslik").toString());
                                hm.put("Description", document.get("haberAciklama").toString());
                                ids.add(document.getId());
                                list.add(hm);
                            }
                            getData();
                        } else {
                            txt.setText("Connection Problem !");
                            // Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
        curr=frghome;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.allLayout, frghome);
        transaction.addToBackStack(null);
        transaction.commit();
        txt=(TextView)findViewById(R.id.txt1);

    }


    public void clickHome(View view){
        if(curr!=frghome){
            txt.setText("Home");
            list.clear();
            db = FirebaseFirestore.getInstance();
            settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .build();
            db.setFirestoreSettings(settings);
            db.collection("News")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    hm=new HashMap();
                                    hm.put("ImagePath",document.get("haberFoto").toString());
                                    hm.put("Title",document.get("haberBaslik").toString());
                                    hm.put("Description", document.get("haberAciklama").toString());
                                    ids.add(document.getId());
                                    list.add(hm);
                                }
                                getData();
                            } else {
                                txt.setText("Connection Problem !");
                                // Log.d("TAG", "Error getting documents: ", task.getException());
                            }
                        }
                    });
            curr=frghome;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.allLayout, frghome);
            transaction.addToBackStack(null);
            transaction.commit();
        }

    }

    public void clickFilter(View view){
        if(curr!=frgFilter) {
            txt.setText("Filter Page");
            curr = frgFilter;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.allLayout, frgFilter);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public void clickDown(View view){
        if(curr!=frgdown) {
            txt.setText("Downloaded News");
            curr = frgdown;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.allLayout, frgdown);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public void clickSettings(View view) {
        if (curr != frgsettings) {
            txt.setText("Settings");
            curr = frgsettings;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.allLayout, frgsettings);

            transaction.addToBackStack(null);
            transaction.commit();
        }
    }




}