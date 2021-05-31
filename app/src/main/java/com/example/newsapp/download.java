package com.example.newsapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link download#newInstance} factory method to
 * create an instance of this fragment.
 */
public class download extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public download() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment download.
     */
    // TODO: Rename and change types and number of parameters
    public static download newInstance(String param1, String param2) {
        download fragment = new download();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_download, container, false);

        dbHandler mydb = new dbHandler(v.getContext());
        ArrayList<String> id,foto,baslik;
        id = new ArrayList<String>();
        foto = new ArrayList<String>();
        baslik = new ArrayList<String>();
        Cursor cursor = mydb.readAllData();
        if(cursor.getCount() ==0){
            Toast.makeText(v.getContext(),"No Downloaded News",Toast.LENGTH_SHORT);
        }
        else{
            while(cursor.moveToNext()){
                id.add(cursor.getString(1));
                foto.add(cursor.getString(2));
                baslik.add(cursor.getString(3));

            }
        }
        HashMap hm;
        List<HashMap<String,String>> list=new ArrayList<>();
        for(int i=0;i<id.size();i++){
            hm=new HashMap();
            hm.put("id",id.get(i));
            hm.put("img",foto.get(i));
            hm.put("title", baslik.get(i));
            list.add(hm);
        }



        ListView lv = (ListView)v.findViewById(R.id.layoutDown);
        downloadAdapter adapter = new downloadAdapter(v.getContext(),list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap hm= list.get(position);
                Intent readNews = new Intent(v.getContext(), viewdownload_new.class);
                readNews.putExtra("idDown",hm.get("id").toString());
                startActivity(readNews);
            }
        });


        return v;
    }
}