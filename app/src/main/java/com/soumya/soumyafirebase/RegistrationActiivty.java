package com.soumya.soumyafirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.soumya.soumyafirebase.models.firebasemodels.SampleUserData;

public class RegistrationActiivty extends AppCompatActivity {

    private static final String TAGNAME = RegistrationActiivty.class.getName();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


 /*   @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
          //  reload();
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_actiivty);
        // Enable the home button (back button) in the ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // Initialize Firebase Authentication
         mAuth = FirebaseAuth.getInstance();

        // Initialize EditText and Button references
        EditText editTextName = findViewById(R.id.editTextName);
        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        EditText editTextAge = findViewById(R.id.editTextAge);
        EditText editTextCity = findViewById(R.id.editTextCity);
        Button buttonRegister = findViewById(R.id.buttonRegister);


        // Set a click listener for the registration button to capture user input and register the user
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInternetAvailable()) {
                    // Retrieve user input from the EditText fields
                    String sname = editTextName.getText().toString();
                    String email = editTextEmail.getText().toString();
                    String password = editTextPassword.getText().toString();
                    String sage = editTextAge.getText().toString();
                    String scity = editTextCity.getText().toString();

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegistrationActiivty.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAGNAME, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user, sname, password, sage, scity);
                                    } else {
                                        Log.w(TAGNAME, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegistrationActiivty.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null, sname, password, sage, scity);
                                    }
                                }
                            });
                }
                else {
                    performAction();
                }
            }
        });

    }

    private void updateUI(FirebaseUser user, String sname, String password, String sage, String scity) {

        if(user != null){
            String email_name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            SampleUserData userData = new SampleUserData(uid,sname, email,password,sage,scity,email_name);

            mDatabase.child("SampleUserData").child(uid).setValue(userData);

            Toast.makeText(RegistrationActiivty.this, "Registered Successed.",
                    Toast.LENGTH_SHORT).show();
            finish();

        }

    }

    // Method to check if the internet is available
    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    // Method to perform your action when internet is available
    private void performAction() {
        // Your code to perform the action goes here
        Toast.makeText(this, "Action performed with internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // Handle the back button click here (e.g., go back to the previous activity)
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}