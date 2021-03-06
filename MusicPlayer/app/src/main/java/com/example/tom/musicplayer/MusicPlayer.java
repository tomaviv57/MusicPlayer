package com.example.tom.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Chen on 13/10/2016.
 */

public class MusicPlayer extends AppCompatActivity {
    private Intent previousIntent, playIntent, nextIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        Button prev = (Button)findViewById(R.id.prev);
        Button st_ps = (Button)findViewById(R.id.st_ps);
        Button nxt = (Button)findViewById(R.id.nxt);
        TextView title = (TextView)findViewById(R.id.textView);

        previousIntent = new Intent(this, MusicService.class);
        previousIntent.setAction(MusicService.PREV);

        playIntent = new Intent(this, MusicService.class);
        playIntent.setAction(MusicService.PLAY);

        nextIntent = new Intent(this, MusicService.class);
        nextIntent.setAction(MusicService.NEXT);

        // checks if the app was opened from explorer
        if(getIntent().getData() != null) {
            // the app was open from explorer, to access it permission is needed
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != android.content.pm.PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            else {
                Intent serviceIntent = new Intent(getApplicationContext(), MusicService.class);
                serviceIntent.putExtra(MusicService.URI_DATA,getIntent().getData());
                serviceIntent.setAction(MusicService.URI);
                startService(serviceIntent);
            }
        }

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(previousIntent);
            }
        });

        st_ps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(playIntent);
            }
        });

        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(nextIntent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent serviceIntent = new Intent(getApplicationContext(), MusicService.class);
                    serviceIntent.putExtra(MusicService.URI_DATA, getIntent().getData());
                    serviceIntent.setAction(MusicService.URI);
                    startService(serviceIntent);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(this,"I need this permission to play songs from your file explorer",Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}