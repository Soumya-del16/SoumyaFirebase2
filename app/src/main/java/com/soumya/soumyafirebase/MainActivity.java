package com.soumya.soumyafirebase;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.soumya.soumyafirebase.adapters.UserRecyclerAdapter;
import com.soumya.soumyafirebase.models.SampleModel;
import com.soumya.soumyafirebase.models.firebasemodels.SampleUserData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG_NAME = MainActivity.class.getName();
    private DatabaseReference mDatabase;
    private UserRecyclerAdapter userRecyclerAdapter;
    private LinearLayoutManager llm;
    private RecyclerView user_recycler_view;
    private List<SampleUserData> sampleUserData;
    private TextView no_data_found;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        sampleUserData = new ArrayList<>();

        user_recycler_view = findViewById(R.id.user_recycler_view);
        no_data_found = findViewById(R.id.no_data_found);
        if(isInternetAvailable()) {

            mDatabase = FirebaseDatabase.getInstance().getReference("SampleUserData"); // Replace with your database reference

            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG_NAME, "onDataChange");

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        SampleUserData user = snapshot.getValue(SampleUserData.class);
                        sampleUserData.add(user);
                        Log.d(TAG_NAME, sampleUserData.get(0).getName());
                    }
                    llm = new LinearLayoutManager(getApplicationContext());

                    userRecyclerAdapter = new UserRecyclerAdapter(getApplicationContext(), sampleUserData);
                    user_recycler_view.setLayoutManager(llm);
                    user_recycler_view.setAdapter(userRecyclerAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG_NAME, "Failed to read value.", databaseError.toException());
                }
            };

            mDatabase.addListenerForSingleValueEvent(valueEventListener);
        }
        else {
            performAction();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.menu_filter) {

        } else if (id == R.id.menu_name) {
            Collections.sort(sampleUserData, new Comparator<SampleUserData>() {
                @Override
                public int compare(SampleUserData user1, SampleUserData user2) {
                    return user1.getName().compareTo(user2.getName());
                }
            });
            userRecyclerAdapter.setData(sampleUserData);
            userRecyclerAdapter.notifyDataSetChanged();

        } else if (id == R.id.menu_age) {
            Collections.sort(sampleUserData, new Comparator<SampleUserData>() {
                @Override
                public int compare(SampleUserData user1, SampleUserData user2) {
                    return Integer.compare(Integer.parseInt(user1.getAge()), Integer.parseInt(user2.getAge()));
                }
            });
            userRecyclerAdapter.setData(sampleUserData);
            userRecyclerAdapter.notifyDataSetChanged();


        } else if (id == R.id.menu_city) {
            Collections.sort(sampleUserData, new Comparator<SampleUserData>() {
                @Override
                public int compare(SampleUserData user1, SampleUserData user2) {
                    return user1.getCity().compareTo(user2.getCity());
                }
            });
            userRecyclerAdapter.setData(sampleUserData);
            userRecyclerAdapter.notifyDataSetChanged();


        } else if (id == R.id.menu_logout) {
            // To log out the current user
            FirebaseAuth.getInstance().signOut();
            Intent j = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(j);
            finish();
        }
        return super.onOptionsItemSelected(item);
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
        no_data_found.setVisibility(View.VISIBLE);
        Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
    }

}