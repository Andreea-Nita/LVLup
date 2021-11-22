package com.andreeanita.lvlup.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.andreeanita.lvlup.Database.DatabaseHelper;
import com.andreeanita.lvlup.R;
import com.andreeanita.lvlup.gpsTracking.MapsActivity;
import com.andreeanita.lvlup.loginAndRegister.Login;

public class HomeActivity extends AppCompatActivity {
    Button startRunning;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    public static String userEmail;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        startRunning = (Button) findViewById(R.id.startRunningButton);
        startRunning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapsActivity();
            }
        });

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getWritableDatabase();

        //fetch user id
        userEmail = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this)
                .getString("email", "No user found");

        String query = "SELECT rowid _id,* from user_activity WHERE  email=?";
        Cursor cursor = db.rawQuery(query, new String[]{userEmail});

        ListView activityItems=(ListView) findViewById(R.id.activityList);

        ListViewAdapter listViewAdapter=new ListViewAdapter(this,cursor);
        activityItems.setAdapter(listViewAdapter);
        listViewAdapter.changeCursor(cursor);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profileItem:
                Toast.makeText(this, "Profile selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.logoutItem:
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