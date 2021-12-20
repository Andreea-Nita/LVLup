package com.andreeanita.lvlup.gpsTracking;

import static com.andreeanita.lvlup.gpsTracking.MapsActivity.image;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.andreeanita.lvlup.R;
import com.andreeanita.lvlup.home.HomeActivity;

public class GPSActivity extends AppCompatActivity implements LocationListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        ImageButton btnHome = findViewById(R.id.homeGPSbutton);
        btnHome.setOnClickListener(view -> openHome());

        ImageButton btnMusic = findViewById(R.id.musicGPSbutton);
        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: openSpotify;
            }
        });

        Button btnStart = findViewById(R.id.startGPSButton);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMapsActivity();
            }
        });

        TextView paceTextView = findViewById(R.id.averagePaceTextView);
        if (MapsActivity.pace != null) {
            paceTextView.setText("Pace: " + MapsActivity.pace + " m/km");
        } else paceTextView.setText("Pace: 0:0 m/km");

        TextView distanceTextView = findViewById(R.id.distanceTextView);
        if (MapsActivity.finalDistance != null) {
            distanceTextView.setText("Distance: " + MapsActivity.finalDistance + " km");
        } else distanceTextView.setText("Distance: 0.0 km");

        TextView timeTextView = findViewById(R.id.timeTextView);
        if (MapsActivity.timeElapsed != null) {
            timeTextView.setText("Time: " + MapsActivity.timeElapsed);
        } else timeTextView.setText("Time: 0m");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 5;
        Bitmap bitmap = base64ToBitmap(image);
        ImageView imageView = findViewById(R.id.imageViewMapResult);
        imageView.setImageBitmap(bitmap);

    }

    public static Bitmap base64ToBitmap(String image) {
        byte[] decodedBytes = Base64.decode(
                image.substring(image.indexOf(",") + 1),
                Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


    public void openHome() {
        MapsActivity.pace = null;
        MapsActivity.timeElapsed = null;
        MapsActivity.finalDistance = null;
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        GPSActivity.this.finish();
    }

    public void openMapsActivity() {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        GPSActivity.this.finish();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

}