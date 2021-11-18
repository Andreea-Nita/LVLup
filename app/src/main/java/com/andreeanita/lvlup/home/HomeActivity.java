package com.andreeanita.lvlup.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andreeanita.lvlup.R;
import com.andreeanita.lvlup.gpsTracking.GPSActivity;
import com.andreeanita.lvlup.loginAndRegister.Login;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HomeActivity extends AppCompatActivity {
    Button startRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        startRunning = (Button)findViewById(R.id.startRunningButton);

        startRunning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGPSActivity();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.profileItem:
                Toast.makeText(this, "Profile selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.logoutItem:
                deleteEmail();
                openLogin();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteEmail() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try (BufferedWriter br = Files.newBufferedWriter(Paths.get("src/main/res/raw/login_email.csv"),
                    Charset.forName("UTF8"))){
                br.write("");
                br.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void openGPSActivity() {
        Intent intent = new Intent(this, GPSActivity.class);
        startActivity(intent);
    }

    public void openLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}