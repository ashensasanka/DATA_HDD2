package com.example.datahdd2;

import static com.example.datahdd2.R.id.progressbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInPageGoogle extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView googleBtn;


    EditText emailEditText,passwordEditText;
    Button signinBtn,signupBtn;
    ProgressBar prograssBar;




    //Skip the MainActivity page when put the user name
    public static String PREFS_NAME = "MyPrefsFile";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_page_google);

        emailEditText =findViewById(R.id.email_edit_text);
        passwordEditText =findViewById(R.id.password_edit_text);
        signinBtn = findViewById(R.id.signinbtn);
        signupBtn = findViewById(R.id.signupbtn);
        prograssBar =findViewById(progressbar);

        signinBtn.setOnClickListener((v)->loginUser());

        //Skip the MainActivity page when put the user name
        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(SignInPageGoogle.PREFS_NAME,0);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putBoolean("hasLoggedIn",true);
                editor.commit();
                startActivity(new Intent(SignInPageGoogle.this,ProfileActivity.class));
                finish();
            }
        });


        //Press Signup Button go to Create Account page
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInPageGoogle.this,CreateAccountActivity.class));
            }
        });


        //Google login
        googleBtn = findViewById(R.id.google_btn);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }


    void loginUser(){
        //line 54
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();


        boolean isValidated = validateData(email,password);
        if (!isValidated){
            return;
        }

        signinAccountInFirebase(email, password);
    }

    void signinAccountInFirebase(String email, String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInPrograss(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInPrograss(false);
                if (task.isSuccessful()){
                    //login is success
                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                        //go to profile

                        startActivity(new Intent(SignInPageGoogle.this,ProfileActivity.class));
                    }
                    else {
                        Utility.showToast(SignInPageGoogle.this,"Email not verified, Please verify your email.");
                    }
                }
                else {
                    //login failed
                    Utility.showToast(SignInPageGoogle.this,task.getException().getLocalizedMessage());
                }
            }
        });
    }

    void changeInPrograss(boolean inPrograss){
        if (inPrograss){
            prograssBar.setVisibility(View.VISIBLE);
            signinBtn.setVisibility(View.GONE);
        }
        else {
            prograssBar.setVisibility(View.GONE);
            signinBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData (String email, String password){

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is invalid");
            return false;
        }
        if (password.length()<6){
            passwordEditText.setError("Password length is invalid");
            return false;
        }
        return true;
    }


    //Google Signing in line 96
    void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                navigateToProfileActivity();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(),"Something Want Wrong",Toast.LENGTH_SHORT).show();
            }
        }
    }
    void navigateToProfileActivity(){

        Intent intent = new Intent(SignInPageGoogle.this,ProfileActivity.class);
        startActivity(intent);
    }
}