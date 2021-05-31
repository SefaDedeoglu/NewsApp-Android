package com.example.newsapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class notifyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    FirebaseFirestore db;
    FirebaseFirestoreSettings settings;
    final int[] sayac = {0};
    Boolean open = true;
    @Override
    public void onCreate() {

        super.onCreate();
       // Toast.makeText(this,"Started",Toast.LENGTH_LONG).show();
        open=true;


        db = FirebaseFirestore.getInstance();
        settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        DocumentReference docRef = db.collection("Notify").document("first");





                docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@com.google.firebase.database.annotations.Nullable DocumentSnapshot snapshot,
                                        @com.google.firebase.database.annotations.Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAGnot", "Listen failed.", e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            if (sayac[0] != 0&&open==true) {
                                int delay = Integer.parseInt(snapshot.get("delay").toString()) * 1000;
                                scheduleNotification(getNotification(snapshot.get("text").toString()), delay, snapshot.get("itson").toString());
                                Log.d("TAGnot", "Current data: null");
                            } else {
                                sayac[0]++;
                            }


                        }
                    }

                });
            }


    private void scheduleNotification (Notification notification , int delay, String itsOn) {
        if(itsOn.equals("on")) {
            Intent notificationIntent = new Intent(this, NotifyApp.class);
            notificationIntent.putExtra(NotifyApp.NOTIFICATION_ID, 1);
            notificationIntent.putExtra(NotifyApp.NOTIFICATION, notification);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            long futureInMillis = SystemClock.elapsedRealtime() + delay;
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            assert alarmManager != null;
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
        }else{
            //do nothing
        }
    }
    private Notification getNotification (String content) {
        Uri sound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, "default" ) ;
        builder.setContentTitle( "NewsApp" ) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( "10001" ) ;
        builder.setSound(sound);
        return builder.build() ;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sayac[0]=0;
        open=false;
      //  Toast.makeText(this,"Stop",Toast.LENGTH_LONG).show();
    }
}
