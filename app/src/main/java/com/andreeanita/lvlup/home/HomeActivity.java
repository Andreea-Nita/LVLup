package com.andreeanita.lvlup.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.andreeanita.lvlup.R;
import com.andreeanita.lvlup.gpsTracking.MapsActivity;
import com.andreeanita.lvlup.gpsTracking.RunningSession;
import com.andreeanita.lvlup.loginAndRegister.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    Button startRunning;

    ArrayList<RunningSession> list = new ArrayList<>();
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://lvlup-330812-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReferenceFromUrl("https://lvlup-330812-default-rtdb.europe-west1.firebasedatabase.app/");

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        startRunning = findViewById(R.id.startRunningButton);
        startRunning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapsActivity();
            }
        });

        ProgressBar pb = (ProgressBar) findViewById(R.id.pbLoading);
        pb.setVisibility(ProgressBar.VISIBLE);


        ListView activityItems = findViewById(R.id.activityList);

        try {
            DatabaseReference reference = databaseReference.child("users").child(userId).child("activities");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for (DataSnapshot snpshot : snapshot.getChildren()) {
                        RunningSession run = snpshot.getValue(RunningSession.class);
                        list.add(0,run);
                    }
                    ListViewAdapter adapter = new ListViewAdapter(HomeActivity.this, list);
                    activityItems.setAdapter(adapter);
                    pb.setVisibility(ProgressBar.INVISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

        activityItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RunningSession item = (RunningSession) activityItems.getItemAtPosition(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage("Do you want to delete this activity?");
                builder.setCancelable(true);

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference reference = databaseReference.child("users").child(userId).child("activities");
                        reference.child(item.getKey()).removeValue();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profileItem:
                Toast.makeText(this, "Profile selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.logoutItem:
                FirebaseAuth.getInstance().signOut();
                openLogin();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void openMapsActivity() {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        HomeActivity.this.finish();
    }

    public void openLogin() {
        Intent intent = new Intent(this, Login.class);
        //clear the back stack
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        HomeActivity.this.finish();
    }


}