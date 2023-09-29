package com.soumya.soumyafirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.utilities.Utilities;
import com.soumya.soumyafirebase.apputilities.AppUtilites;
import com.soumya.soumyafirebase.models.firebasemodels.UserData;

public class LoginActivity extends AppCompatActivity {
    private static  final String TAG_NAME = LoginActivity.class.getName();
    private Button buttonCreate,buttonRegistered;
    private EditText email , password;
    private TextView password_error_mesg;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.editTextLoginEmail);
        password = findViewById(R.id.editTextLoginPassword);
        password_error_mesg = findViewById(R.id.password_error_mesg);
        buttonCreate = findViewById(R.id.buttonCreate);
        buttonRegistered = findViewById(R.id.buttonRegistered);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        buttonRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RegistrationActiivty.class);
                startActivity(i);
            }
        });

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                String semail = email.getText().toString();
                String spass = password.getText().toString();

                if(AppUtilites.isvalidEmailId(semail) && AppUtilites.isValidPassword(spass)){

                    mAuth.signInWithEmailAndPassword(semail, spass)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG_NAME, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG_NAME, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }
                                }
                            });

                }
                else {
                    if(!AppUtilites.isvalidEmailId(semail)){
                        email.setError(getString(R.string.entervalidemail));
                    }
                    if (!AppUtilites.isValidPassword(spass)){
                        password.setError(getString(R.string.entervalidpassword));
                        password_error_mesg.setText("* Password must contain : \n" +
                                "Atleast one capital alphbet,number" +
                                "Atlest one Special character");
                    }

                    //Toast.makeText(LoginActivity.this, "Please Enter Valid Email or Password", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void updateUI(FirebaseUser user) {

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();

        }
        else {
            Toast.makeText(getApplicationContext(), "Not created", Toast.LENGTH_SHORT).show();
        }

    }
}