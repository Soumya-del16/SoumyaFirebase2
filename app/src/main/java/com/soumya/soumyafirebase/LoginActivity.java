package com.soumya.soumyafirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.soumya.soumyafirebase.apputilities.AppUtilites;

public class LoginActivity extends AppCompatActivity {
    private static  final String TAG_NAME = LoginActivity.class.getName();
    private Button buttonCreate,buttonRegistered;
    private EditText email , password;
    private TextView password_error_mesg;
    private FirebaseAuth mAuth;
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
            //  reload();
        }
    }
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

                if(isInternetAvailable()) {
                    String semail = email.getText().toString();
                    String spass = password.getText().toString();

                    if (!semail.isEmpty() && !spass.isEmpty()) {

                        if (AppUtilites.isvalidEmailId(semail) && AppUtilites.isValidPassword(spass)) {
                            signInwithEmailPassword(semail, spass);
                        } else if (AppUtilites.isValidPassword(spass)) {
                            if (semail.equals("Fininfocom") && spass.equals("Fin@123")) {
                                signInwithEmailPassword("soumyarelli516@gmail.com", "Test@123");
                            } else {
                                password_error_mesg.setText("Not Valid Credentails");
                            }

                        } else {
                            if (!AppUtilites.isvalidEmailId(semail)) {
                                email.setError(getString(R.string.entervalidemail));
                            }
                            if (!AppUtilites.isValidPassword(spass)) {
                                password.setError(getString(R.string.entervalidpassword));
                                password_error_mesg.setText("* Password must contain : \n" +
                                        "Atleast one capital alphbet,number" +
                                        "Atlest one Special character" +
                                        "Max letters between 8 to 12");
                            }

                            //Toast.makeText(LoginActivity.this, "Please Enter Valid Email or Password", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        email.setError("Empty");
                        password.setError("Empty");
                        password_error_mesg.setText("* Please Enter Provide Credentials");
                    }
                }else {
                    performAction();
                }
            }
        });

    }

    private void signInwithEmailPassword(String semail, String spass) {
        mAuth.signInWithEmailAndPassword(semail, spass)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Log.d(TAG_NAME, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user,semail,spass);

                        } else {
                            Log.w(TAG_NAME, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null, semail, spass);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user, String semail, String spass) {

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            boolean emailVerified = user.isEmailVerified();

            String uid = user.getUid();

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
            Toast.makeText(LoginActivity.this, "Login Successed.",
                    Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(getApplicationContext(), "Not created", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    private void performAction() {
        Toast.makeText(this, "Action performed with internet", Toast.LENGTH_SHORT).show();
    }

}