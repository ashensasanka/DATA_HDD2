package com.example.datahdd2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.datahdd2.databinding.BaseActivityBinding;

public class BaseActivity extends AppCompatActivity {
    Activity activity;
    BaseActivityBinding baseActivityBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = BaseActivity.this;
        baseActivityBinding = BaseActivityBinding.inflate(getLayoutInflater());
        setContentView(baseActivityBinding.getRoot());


        baseActivityBinding.xlsExport.setOnClickListener(view -> {
            Intent xlsIntent = new Intent(activity, Exportdata.class);
            startActivity(xlsIntent);
        });
    }
}