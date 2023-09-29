package com.soumya.soumyafirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.soumya.soumyafirebase.models.firebasemodels.UserData;

public class RegistrationActiivty extends AppCompatActivity {

    private static final String TAGNAME = RegistrationActiivty.class.getName();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
          //  reload();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_actiivty);
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
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAGNAME, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user,sname,password,sage,scity);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAGNAME, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegistrationActiivty.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null, sname, password, sage, scity);
                                }
                            }
                        });
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
            UserData userData = new UserData(uid,sname, email,password,sage,scity,email_name);

            mDatabase.child("UserData").child(uid).setValue(user);

            Toast.makeText(RegistrationActiivty.this, "Authentication Successed.",
                    Toast.LENGTH_SHORT).show();

        }

    }
}