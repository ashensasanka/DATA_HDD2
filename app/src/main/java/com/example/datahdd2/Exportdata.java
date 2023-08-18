package com.example.datahdd2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.datahdd2.databinding.ActivityExportdataBinding;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Exportdata extends AppCompatActivity implements ExportInterface{


    private ActivityExportdataBinding activityExportdataBinding;
    private Connection connection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        activityExportdataBinding = ActivityExportdataBinding.inflate(getLayoutInflater());
        setContentView(activityExportdataBinding.getRoot());

        activityExportdataBinding.button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewData();
            }
        });

        activityExportdataBinding.button4.setOnClickListener(view -> {

            exportData();

        });
    }

    private void exportData() {
        List<XlsMode> xlsModeList = new ArrayList<>();

        xlsModeList.add(new XlsMode("1","Ashen", "ashensasanka22gmail.com","AS95473","0785667435","102/15,Kohombana","a"));
        xlsModeList.add(new XlsMode("2","Buddhi", "buddhinirosha22@gmail","AS95948","0716524717","36/2, Pasalmawatha, Gangodaila","a"));
        xlsModeList.add(new XlsMode("3","Nilupul", "nilupulpiyuman22@gmail","AS95678","0774563234","37,santhanna , Kurunagala","a"));
        xlsModeList.add(new XlsMode("4","Chandima", "chandimaweerathunga45@gmail.com","AS95347","0775678234","02,heenmatiya , amapata","a"));
        xlsModeList.add(new XlsMode("5","Adeesha", "adeeshatharaka78@gmail.com","AS95769","078445635","67, gall road , balmabapitiya","a"));


        Gson gson = new Gson();
        var jsonArray = gson.toJsonTree(xlsModeList).getAsJsonArray();

        String[] titles = new String[]{"ID", "Name", "Class", "Bench", "Age", "Gender","Grade"};
        String[] indexName = new String[]{"studentId", "studentName", "studentClass", "studentBench", "studentAge", "studentGender","studentGrade"};

        HashMap<String, String> otherValue  = new HashMap<>();
        otherValue.put("Record", "Student Record");
        otherValue.put("Place", "Campus City");
        otherValue.put("City", "Toranto");


        var file = generateXlsFile(this, titles,indexName,jsonArray,otherValue,"Student Record","students",1);


    }

    private void ViewData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.43.251/crudandroid2/read2.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i =0; i <jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String idr = jsonObject.getString("id");
                                String mobiler = jsonObject.getString("Mobile");
                                String tellr = jsonObject.getString("Tell");
                                String addressr = jsonObject.getString("Address");
                                activityExportdataBinding.textView7.append("ID = " +idr+ "Mobile = "+mobiler+ "\n");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error",error.getLocalizedMessage());
            }
        });
        queue.add(stringRequest);
    }

}


