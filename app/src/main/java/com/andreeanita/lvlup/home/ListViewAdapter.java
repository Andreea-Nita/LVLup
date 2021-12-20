package com.andreeanita.lvlup.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.andreeanita.lvlup.R;
import com.andreeanita.lvlup.gpsTracking.GPSActivity;
import com.andreeanita.lvlup.gpsTracking.RunningSession;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<RunningSession> {
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public ListViewAdapter(@NonNull Context context, ArrayList<RunningSession> runningSessions) {
        super(context, 0, runningSessions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        RunningSession runningSession = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item, parent, false);
        }
        TextView activityName = convertView.findViewById(R.id.textViewName);
        TextView activityDateTime = convertView.findViewById(R.id.textViewDateTime);
        TextView activityPace = convertView.findViewById(R.id.textViewPace);
        TextView activityDuration = convertView.findViewById(R.id.textViewDuration);
        TextView activityDistance = convertView.findViewById(R.id.textViewDistance);
        ImageView activityImage = convertView.findViewById(R.id.imageViewMap);

        Bitmap bImage = GPSActivity.base64ToBitmap(runningSession.getImage());

        activityName.setText(userId + "   " + runningSession.getTipe());
        activityDateTime.setText(runningSession.getDate() + "   " + runningSession.getActivityTime());
        activityPace.setText("Pace: " + runningSession.getPace());
        activityDuration.setText("Time: " + runningSession.getTimeElapsed());
        activityDistance.setText("Distance: " + runningSession.getFinalDistance());
        activityImage.setImageBitmap(bImage);

        return convertView;
    }


}