package com.example.newsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class dbHandler extends SQLiteOpenHelper {
private Context context;
    public dbHandler(@Nullable Context context) {
        super(context, "News", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    String query = "CREATE TABLE NEWSdb (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "haber_id TEXT, " +
            "haber_foto TEXT, " +
            "haber_baslik TEXT, " +
            "haber_icerik TEXT); ";
    db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS NEWSdb");
        onCreate(db);
    }
    void addNew(String haber_id,String foto,String baslik,String icerik){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("haber_id",haber_id);
        cv.put("haber_foto",foto);
        cv.put("haber_baslik",baslik);
        cv.put("haber_icerik",icerik);
        long result = db.insert("NEWSdb",null,cv);
        if(result==-1){
            Toast.makeText(context,"Failed download",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(context,"Success download",Toast.LENGTH_LONG).show();
        }
    }
    Cursor readAllData(){
        String query = "SELECT * FROM NEWSdb";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db!=null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }
    void deleteRow(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete("NEWSdb","haber_id=?",new String[]{id});
        if(result==-1){
            Toast.makeText(context,"Failed delete",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(context,"Success delete",Toast.LENGTH_LONG).show();
        }
    }
}
