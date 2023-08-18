package com.example.datahdd2;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.datahdd2.databinding.ActivityEditProfileBinding;
import com.example.datahdd2.databinding.ActivityProfileBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView textView_name, textView_email, textView_regnumber, textView_mobile, textView_address;
    SharedPreferences sharedPreferences;
    TextView name,email, textView;
    Button signOutBtn,edit_profile_btn,button,export;
    EditText editText;



    // Press Back Again to Exit
    private long backPressTime;
    private Toast toast;

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
        setContentView(R.layout.activity_profile);


        // auto rename the profile name and email when google signing
        name = findViewById(R.id.name1);
        email =  findViewById(R.id.email);

        signOutBtn = findViewById(R.id.signoutbtn);                //NOT INCLUDED TO CODE

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            name.setText(personName);
            email.setText(personEmail);
        }

        textView_name = findViewById(R.id.view_name);
        textView_email = findViewById(R.id.text_email);
        textView_regnumber = findViewById(R.id.text_regnumber);
        textView_mobile = findViewById(R.id.text_mobile);
        textView_address = findViewById(R.id.text_address);
        button = findViewById(R.id.button);
        export = findViewById(R.id.button3);




        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        String name = sharedPreferences.getString(KEY_NAME,null);
        String email = sharedPreferences.getString(KEY_EMAIL,null);
        String regnumber = sharedPreferences.getString(KEY_REGNUMBER,null);
        String mobile = sharedPreferences.getString(KEY_MOBILE,null);
        String address1 = sharedPreferences.getString(KEY_ADDRESS,null);

        if (name!=null || email!= null || regnumber!= null || mobile!= null || address1!= null){
            textView_name.setText(name);
            textView_email.setText(email);
            textView_regnumber.setText(regnumber);
            textView_mobile.setText(mobile);
            textView_address.setText(address1);
        }

        // SEND DATA to DATA BASE
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });

        // EXPORT as EXCEL sheet
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,BaseActivity.class));
                //exportData();
            }
        });



        // sign out from profile
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                signout();
                Toast.makeText(ProfileActivity.this,"Log Out",Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        //Go to edit profile by button
        edit_profile_btn = (Button) findViewById(R.id.edit_profile_btn);
        edit_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,EditProfileActivity.class));
            }
        });

    }

    private void exportData() {
        String data = editText.getText().toString();

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
                                textView.append("ID = " +idr+ "Mobile = "+mobiler+ "\n");
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

    // SEND DATA to DATA BASE DECLARE insertData
    private void insertData() {

        String namedatabase = textView_name.getText().toString().trim();
        String emaildatabase = textView_email.getText().toString().trim();
        String regnumberdatabase = textView_regnumber.getText().toString().trim();
        String mobiledatabase = textView_mobile.getText().toString().trim();
        String addressdatabase = textView_address.getText().toString().trim();

        ProgressDialog prograssDialog = new ProgressDialog(this);
        prograssDialog.setMessage("Loading...");

        if (namedatabase.isEmpty()){
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (emaildatabase.isEmpty()){
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (regnumberdatabase.isEmpty()){
            Toast.makeText(this, "Enter Registation Number", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (mobiledatabase.isEmpty()){
            Toast.makeText(this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (addressdatabase.isEmpty()){
            Toast.makeText(this, "Enter Address", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            prograssDialog.show();
                StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.43.251/crudandroid2/create3.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equalsIgnoreCase("Data Inserted")){
                                    Toast.makeText(ProfileActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                                    prograssDialog.dismiss();
                                }
                                else {
                                    Toast.makeText(ProfileActivity.this, response, Toast.LENGTH_SHORT).show();
                                    prograssDialog.dismiss();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        prograssDialog.dismiss();
                    }
                }

                ){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String,String> params = new HashMap<String,String>();


                        params.put("Name",namedatabase);
                        params.put("email",emaildatabase);
                        params.put("Reg_Number",regnumberdatabase);
                        params.put("Mobile",mobiledatabase);
                        params.put("Address",addressdatabase);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
                requestQueue.add(request);
        }
    }

    // Press Back Again to Exit
    @Override
    public void onBackPressed(){

        if (backPressTime +2000 > System.currentTimeMillis()){
            super.onBackPressed();
            toast.cancel();
            return;
        }
        else {
            toast = Toast.makeText(this,"Press Back Again to Exit!",Toast.LENGTH_SHORT);
            toast.show();
        }
        backPressTime = System.currentTimeMillis();
    }

    //Sign out variable in line 62
    void signout() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                finish();
                startActivity(new Intent(ProfileActivity.this, SignInPageGoogle.class));
            }
        });
    }
}