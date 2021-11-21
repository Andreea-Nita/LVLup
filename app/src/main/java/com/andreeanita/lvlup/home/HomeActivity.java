package com.andreeanita.lvlup.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.andreeanita.lvlup.R;
import com.andreeanita.lvlup.gpsTracking.MapsActivity;
import com.andreeanita.lvlup.loginAndRegister.Login;

public class HomeActivity extends AppCompatActivity {
    Button startRunning;

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