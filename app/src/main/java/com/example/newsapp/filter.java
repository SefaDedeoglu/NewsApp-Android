package com.example.newsapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link filter#newInstance} factory method to
 * create an instance of this fragment.
 */
public class filter extends Fragment {
    List<String> ids = new ArrayList<String>();
    HashMap hm;
    FirebaseFirestore db;
    FirebaseFirestoreSettings settings;
    List<HashMap<String,String>> liste=new ArrayList<>();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public filter() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment filter.
     */
    // TODO: Rename and change types and number of parameters
    public static filter newInstance(String param1, String param2) {
        filter fragment = new filter();
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

    public void getData(View v){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_filter, container, false);

        //get the spinner from the xml.
        Spinner dropdown = v.findViewById(R.id.spinner);
        List<String> items = new ArrayList<String>();
        db = FirebaseFirestore.getInstance();
        settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        db.collection("Categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                items.add(document.get("kategori").toString());
                            }
                            customDropDown customDropDown = new customDropDown(v.getContext(),items);
                           // ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(),android.R.layout.simple_dropdown_item_1line, items);
                            dropdown.setAdapter(customDropDown);

                            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
                                    Log.v("item", (String) parent.getItemAtPosition(position));
                                    liste.clear();
                                    ids.clear();
                                    db.collection("News")
                                            .whereEqualTo("kategori", (String) parent.getItemAtPosition(position))
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            Log.d("TAG356", document.getId() + " => " + document.getData());
                                                            hm=new HashMap();
                                                            hm.put("ImagePath",document.get("haberFoto").toString());
                                                            hm.put("Title",document.get("haberBaslik").toString());
                                                            hm.put("Description", document.get("haberAciklama").toString());
                                                            ids.add(document.getId());

                                                            liste.add(hm);
                                                        }
                                                        ListView lv = v.findViewById(R.id.layoutFilter);
                                                        String[] custom={"ImagePath","Title","Description"};
                                                        int[] customId={R.id.newImage,R.id.newTitle,R.id.newDescription};
                                                        CustomAdapter adapter = new CustomAdapter(v.getContext(),liste);
                                                        // SimpleAdapter adapter=new SimpleAdapter( getContext() ,list,R.layout.custom_list_filter,custom,customId);
                                                        lv.setAdapter(adapter);
                                                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                            @Override
                                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                Intent readNews = new Intent(v.getContext(), readnews.class);
                                                                readNews.putExtra("id",ids.get(position));
                                                                startActivity(readNews);
                                                                HashMap hm= liste.get(position);
                                                            }
                                                        });

                                                    } else {

                                                        Log.d("TAG356", "Error getting documents: ", task.getException());
                                                    }
                                                }
                                            });

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    // TODO Auto-generated method stub
                                }
                            });
                        } else {

                            // Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });




        return v;
    }
}