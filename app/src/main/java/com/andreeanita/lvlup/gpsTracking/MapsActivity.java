package com.andreeanita.lvlup.gpsTracking;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.andreeanita.lvlup.R;
import com.andreeanita.lvlup.databinding.ActivityMapsBinding;
import com.andreeanita.lvlup.home.HomeActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    protected GoogleMap mMap;
    protected ActivityMapsBinding binding;
    protected FusedLocationProviderClient fusedLocationProviderClient;
    protected static final int REQUEST_CODE = 200;
    private Location startLocation;
    protected Location currentLocation;
    protected Location prevLocation;
    private long finishDateTime;
    private long startDateTime;
    protected long time;
    public static String timeElapsed;
    public static String image;
    float distance = 0.0F;
    public static String finalDistance;
    public static String pace;
    public static String userEmail;
    public static String userUid;
    public static String type;

    private List<LatLng> coordonates = new ArrayList<>();

    private LocationManager locationManager;

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location == null)
                return;
            fetchLocation();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Criteria crit = new Criteria();
        crit.setAccuracy(Criteria.ACCURACY_FINE);
        crit.setAccuracy(Criteria.ACCURACY_COARSE);
        crit.setPowerRequirement(Criteria.POWER_LOW);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String best = locationManager.getBestProvider(crit, false);
        locationManager.requestLocationUpdates(best, 0, 1, locationListener);

        startDateTime = Calendar.getInstance().getTimeInMillis();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://lvlup-330812-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReferenceFromUrl("https://lvlup-330812-default-rtdb.europe-west1.firebasedatabase.app/");

        Button stop = findViewById(R.id.stopButton);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopLocationUpdates();
                mapShowRoute();

                //save map screenshot
                image = saveMapPhoto();

                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setMessage("Do you want to save this activity?");
                builder.setCancelable(true);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @SuppressLint("Range")
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();


                        finalDistance = String.format("%.2f", (getFinalDistance(distance)));

                        finishDateTime = Calendar.getInstance().getTimeInMillis();
                        time = finishDateTime - startDateTime;
                        timeElapsed = calculateTime(time);

                        pace = calculatePace(distance, time);

                        //fetch user id
                        userEmail = PreferenceManager.getDefaultSharedPreferences(MapsActivity.this)
                                .getString("email", "No user found");

                        String date = String.valueOf(LocalDate.now());
                        String activityTime = LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));

                        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();


                        if (LocalTime.now().isAfter(LocalTime.parse("00:00:00")) && LocalTime.now().isBefore(LocalTime.parse("03:59:00"))) {
                            type = "Night run";//map.put("Night run", runningSession);
                        } else if (LocalTime.now().isAfter(LocalTime.parse("04:00:00")) && LocalTime.now().isBefore(LocalTime.parse("11:59:00"))) {
                            type = "Morning run";//map.put("Morning run", runningSession);
                        } else if (LocalTime.now().isAfter(LocalTime.parse("12:00:00")) && LocalTime.now().isBefore(LocalTime.parse("17:59:00"))) {
                            type = "Day run";// map.put("Day run", runningSession);
                        } else {
                            type = "Evening run";//map.put("Evening run", runningSession);
                        }

                        DatabaseReference reference = databaseReference.child("users").child(userUid).child("activities");
                        String pushedId = reference.push().getKey();
                        RunningSession runningSession = new RunningSession(pushedId, date, activityTime, pace, timeElapsed,
                                finalDistance, image, type);

                        reference.child(pushedId).setValue(runningSession);

                        openGPSActivity();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        openHomeActivity();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // get initial location when map loads
        fetchLocation();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        fetchLocation();
    }

    public void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                if (startLocation == null) {
                    startLocation = currentLocation;
                }
                prevLocation = currentLocation;
                currentLocation = location;
                //finishLocation = location;

                LatLng current = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                coordonates.add(current);

                //calculate distance from first location to current location
                if (prevLocation != null && currentLocation != null) {
                    distance = distance + prevLocation.distanceTo(currentLocation);//(double) (((int) (prevLocation.distanceTo(currentLocation) * 100)) / 100);
                } else {
                    distance = 0;
                }

                //follow the pin
                mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 20));
                mMap.setMyLocationEnabled(true);

                //add blue line between previous location and current location
                if (prevLocation != null) {
                    mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(prevLocation.getLatitude(), prevLocation.getLongitude()),
                                    new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                            .width(6).color(Color.BLUE)
                            .visible(true));
                }

            }
        });

    }

    private void stopLocationUpdates() {
        locationManager.removeUpdates(locationListener);
    }

    public String saveMapPhoto() {
        mMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(@Nullable Bitmap bitmap) {
                image = getBitmapAsBase64(bitmap);
            }
        });
        return image;
    }

    public static String getBitmapAsBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        String encoded = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
        return encoded;
    }

    public void openHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        MapsActivity.this.finish();
    }

    public void openGPSActivity() {
        Intent intent = new Intent(this, GPSActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        MapsActivity.this.finish();
    }

    public String calculateTime(long time) {
        int seconds = (int) (time / 1000) % 60;
        int minutes = (int) ((time / (1000 * 60)) % 60);
        int hours = (int) ((time / (1000 * 60 * 60)) % 24);
        StringBuilder finalTime = new StringBuilder();
        if (hours == 0) {
            finalTime.append(minutes + "m " + seconds + "s");
        } else {
            finalTime.append(hours + "h " + minutes + "m " + seconds + "s");
        }
        return finalTime.toString();
    }

    //pace=min/km
    public String calculatePace(float distance, long time) {
        int pace;
        String finalPace;
        double dist = getFinalDistance(distance);
        if (dist != 0 && time != 0) {
            pace = (int) ((int) (time / 1000) / dist);

        } else {
            pace = 0;
        }
        int min = pace / 60;
        int sec = pace % 60;
        if (sec < 10) {
            finalPace = min + ":" + sec + "0";
        } else {
            finalPace = min + ":" + sec;
        }

        return finalPace;
    }

    //distance will only be displayed if it is greater than 9m
    public double getFinalDistance(float distance) {
        double finalDistance = 0.0;
        double doubleDistance = (int) distance;
        if (doubleDistance > 9) {
            finalDistance = doubleDistance / 1000;
        }
        return finalDistance;
    }

    public void mapShowRoute() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng c : coordonates) {
            builder.include(c);

            //initialize the padding for map boundary
            int padding = 10;

            //create the bounds from latlngBuilder to set into map camera
            LatLngBounds bounds = builder.build();

            // the camera with bounds and padding to set into map
            final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

            //call the map call back to know map is loaded or not
            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    //set animated zoom camera into map
                    mMap.animateCamera(cu);
                }
            });
        }
    }


}
