package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static androidx.constraintlayout.solver.widgets.ConstraintWidget.VISIBLE;


public class readnews extends AppCompatActivity {


    FirebaseFirestore db;
    FirebaseFirestoreSettings settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readnews);
        Intent intent = getIntent();
        String id =  intent.getStringExtra("id");
        db = FirebaseFirestore.getInstance();
        settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        TextView title = findViewById(R.id.idNewTitle);
        TextView text = findViewById(R.id.idNewtxt);
        ImageView img = findViewById(R.id.idNewimg);
        final String[] url = {""};
        View v =findViewById(android.R.id.content).getRootView();
        DocumentReference docRef = db.collection("News").document(id);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                   // Log.w("TAG12", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    title.setText(snapshot.get("haberBaslik").toString());
                    text.setText(snapshot.get("haberIcerik").toString());
                    url[0] = snapshot.get("haberFoto").toString();
                    if( url[0].length() >5){
                        Picasso.get().load(url[0]).into(img);
                    }
                    else{
                        img.setVisibility(v.GONE);
                    }
                    //Log.d("TAG12", "Current data: " + snapshot.getData());
                } else {
                    //Log.d("TAG12", "Current data: null");
                }
            }
        });



        dbHandler mydb = new dbHandler(v.getContext());
        ArrayList<String> idDown;
        idDown = new ArrayList<String>();
        Cursor cursor = mydb.readAllData();
        if(cursor.getCount() ==0){
        }
        else{
            while(cursor.moveToNext()){
                idDown.add(cursor.getString(1));
            }
        }

        Button btn = findViewById(R.id.downbutton);
        btn.setText("Download");
        btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downico, 0);

        for(int i=0;i<idDown.size();i++){
            if(idDown.get(i).equals(id)){
                btn.setText("Downloaded");
                btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.doneico, 0);
            }
        }

    }
    public void goback(View v){
        super.onBackPressed();
    }
    public void downbutton(View v){

        Button btn = findViewById(R.id.downbutton);


        dbHandler myDB = new dbHandler(v.getContext());
        if(btn.getText().equals("Downloaded")){
            //do nothing
        }
        else {
            Intent intent = getIntent();
            String id =  intent.getStringExtra("id");
            TextView title = findViewById(R.id.idNewTitle);
            TextView text = findViewById(R.id.idNewtxt);
            ImageView image = findViewById(R.id.idNewimg);
            if (image.getVisibility() == v.VISIBLE) {
                Bitmap finalBitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();

                SaveImageToGallery(finalBitmap, id);
                String fotoYol = Environment.getExternalStorageDirectory() + "/Pictures/NewsApp/" + id + ".jpg";
                myDB.addNew(id, fotoYol, title.getText().toString(), text.getText().toString());


                btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.doneico, 0);
                btn.setText("Downloaded");
            } else {
                myDB.addNew(id, "0", title.getText().toString(), text.getText().toString());
            }
        }
    }


    public void SaveImageToGallery(Bitmap bitmap,String ids){



/*


        OutputStream output;
        // Find the SD Card path
        File filepath = Environment.getExternalStoragePublicDirectory("");

        // Create a new folder in SD Card
        File dir = new File(filepath.getAbsolutePath()
                + "/NewsApp/");
        dir.mkdirs();


Log.i("laylaylo","write"+dir.canWrite()+"read"+dir.canRead());

        // Create a name for the saved image
        File file = new File(dir, ids+".jpg" );

        try {

            output = new FileOutputStream(file);

            // Compress into png format image from 0% - 100%
          //  bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();

        }

        catch (Exception e) {
            Toast.makeText(this,"dont saved \n"+e.getMessage(),Toast.LENGTH_SHORT).show();
            Log.e("laylaylom","dont saved \n"+e.getMessage());
            // TODO Auto-generated catch block
            e.printStackTrace();
        }





*/






        FileOutputStream fos;
        try {
                ContentResolver resolver = getContentResolver();
                ContentValues cv = new ContentValues();
                cv.put(MediaStore.MediaColumns.DISPLAY_NAME,ids+".jpg");
                cv.put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg");
                cv.put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_PICTURES+File.separator+"NewsApp");
                Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,cv);
                fos = (FileOutputStream) resolver.openOutputStream(Objects.requireNonNull(imageUri));
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
                //Toast.makeText(this,"saved to" + imageUri.getPath(),Toast.LENGTH_SHORT).show();
            File dir = new File(imageUri.getPath());
           // Log.i("laylaylo","write"+dir.canWrite()+"read"+dir.canRead());

        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this,"dont saved \n"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }



    /*
    private void saveMap(Map<String, String> inputMap) {
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences("MyVariables",
                Context.MODE_PRIVATE);
        if (pSharedPref != null) {
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove(mapKey).apply();
            editor.putString(mapKey, jsonString);
            editor.commit();
        }

    }
    */


}