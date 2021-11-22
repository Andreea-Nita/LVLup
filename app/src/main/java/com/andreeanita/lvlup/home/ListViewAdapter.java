package com.andreeanita.lvlup.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cursoradapter.widget.CursorAdapter;

import com.andreeanita.lvlup.R;

public class ListViewAdapter extends CursorAdapter {

    public ListViewAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }


    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.row_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView activityName = (TextView) view.findViewById(R.id.textViewName);
        TextView activityDateTime = (TextView) view.findViewById(R.id.textViewDateTime);
        TextView activityPace = (TextView) view.findViewById(R.id.textViewPace);
        TextView activityDuration = (TextView) view.findViewById(R.id.textViewDuration);
        TextView activityDistance = (TextView) view.findViewById(R.id.textViewDistance);
        ImageView activityImage=(ImageView) view.findViewById(R.id.imageViewMap);

        // Extract properties from cursor
        @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("email"));//TODO: replace with user name
        @SuppressLint("Range") String dateTime = cursor.getString(cursor.getColumnIndex("date"))
                +"   "+cursor.getString(cursor.getColumnIndex("time"));
        @SuppressLint("Range") String pace = cursor.getString(cursor.getColumnIndex("pace"));
        @SuppressLint("Range") String durtion = cursor.getString(cursor.getColumnIndex("time_elapsed"));
        @SuppressLint("Range") String distance = cursor.getString(cursor.getColumnIndex("distance"));
        @SuppressLint("Range") Bitmap image = BitmapFactory.decodeByteArray(cursor.getBlob(cursor.getColumnIndex("image")),
                0, (cursor.getBlob(cursor.getColumnIndex("image"))).length);


        // Populate fields with extracted properties
        activityName.setText(name);
        activityDateTime.setText(dateTime);
        activityPace.setText(pace);
        activityDuration.setText(durtion);
        activityDistance.setText(distance);
        activityImage.setImageBitmap(image);



    }
}
