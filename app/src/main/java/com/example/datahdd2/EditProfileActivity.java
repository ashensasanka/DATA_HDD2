package com.example.datahdd2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.datahdd2.databinding.ActivityEditProfileBinding;

public class EditProfileActivity extends AppCompatActivity {

    EditText editText_name, editText_email, editText_regnumber, editText_mobile, editText_address ;
    Button submit_btn;
    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_REGNUMBER = "regnumber";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_ADDRESS = "address1";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editText_name = findViewById(R.id.editTextName);
        editText_email = findViewById(R.id.editTextEmail);
        editText_regnumber = findViewById(R.id.editTextRegNumb);
        editText_mobile = findViewById(R.id.editTextMobile);
        editText_address = findViewById(R.id.editTextAddress);

        submit_btn = findViewById(R.id.submit);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        String name = sharedPreferences.getString(KEY_NAME,null);
        String email = sharedPreferences.getString(KEY_EMAIL,null);
        String regnumber = sharedPreferences.getString(KEY_REGNUMBER,null);
        String mobile = sharedPreferences.getString(KEY_MOBILE,null);
        String address1 = sharedPreferences.getString(KEY_ADDRESS,null);


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_NAME,editText_name.getText().toString());
                editor.putString(KEY_EMAIL,editText_email.getText().toString());
                editor.putString(KEY_REGNUMBER,editText_regnumber.getText().toString());
                editor.putString(KEY_MOBILE,editText_mobile.getText().toString());
                editor.putString(KEY_ADDRESS,editText_address.getText().toString());
                editor.apply();
                Intent intent = new Intent(EditProfileActivity.this,ProfileActivity.class);
                startActivity(intent);
                Toast.makeText(EditProfileActivity.this,"Edit Success",Toast.LENGTH_SHORT).show();
            }
        });

    }
}