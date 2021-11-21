package com.andreeanita.lvlup.loginAndRegister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.andreeanita.lvlup.Database.DatabaseHelper;
import com.andreeanita.lvlup.R;
import com.andreeanita.lvlup.gpsTracking.GPSActivity;
import com.andreeanita.lvlup.gpsTracking.MapsActivity;
import com.andreeanita.lvlup.home.HomeActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Login extends AppCompatActivity {
    TextView register;
    EditText etEmail, etPassword;
    Button login;
    static String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DatabaseHelper databaseHelper;

        databaseHelper = new DatabaseHelper(this);

        etEmail = (EditText) findViewById(R.id.editTextLoginEmail);
        etPassword = (EditText) findViewById(R.id.editTextLoginPassword);

        login = (Button) findViewById(R.id.buttonLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                Boolean checklogin = databaseHelper.CheckLogin(email, password);
                if (checklogin == true) {
                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();

                    saveEmail(email);

                    openHomeActivity();
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        register = (TextView) findViewById(R.id.textViewRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });

    }

    public void openRegister() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    public void openHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void saveEmail(String string) {

        PreferenceManager.getDefaultSharedPreferences(this)
                .edit().putString("email", string)
                .apply();

    }


}