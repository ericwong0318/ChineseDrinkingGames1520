package com.example.chinese_drinking_games_15_20;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class FinalResult extends AppCompatActivity implements View.OnClickListener {
    Button btnContinue, btnQuit;
    TextView tvFinalResult;
    ImageView ivGif;
    SQLiteDatabase db;
    String opponentName;
    private Boolean myTurn;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_result);
        //UI
        tvFinalResult = findViewById(R.id.tvFinalResult);
        btnContinue = findViewById(R.id.btnContinue);
        btnQuit = findViewById(R.id.btnQuit);
        ivGif = findViewById(R.id.ivGif);
        final MediaPlayer mp;
        //set onClickListener
        btnContinue.setOnClickListener(this);
        btnQuit.setOnClickListener(this);

        Intent intent = getIntent();
        opponentName = intent.getStringExtra("opponentName");
        myTurn = intent.getBooleanExtra("myTurn", false);

        if (myTurn == true) {
            tvFinalResult.setTextColor(Color.parseColor("#FAAC58"));
            tvFinalResult.setText("You Win");
            ivGif.setImageResource(R.drawable.award);
            mp = MediaPlayer.create(this, R.raw.win);
        } else {
            tvFinalResult.setTextColor(Color.parseColor("#808080"));
            tvFinalResult.setText("You Lost");
            ivGif.setImageResource(R.drawable.thumbsdown);
            mp = MediaPlayer.create(this, R.raw.lost);
        }
        mp.start();


        try {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            //
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            1);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // Permission has already been granted
            }

            //make directory
            File directory = new File("/data/data/com.example.chinese_drinking_games_15_20/databases");
            directory.mkdirs();

            //open database
            Context ctx = this; // for Activity, or Service. Otherwise simply get the context.
            String dbName = "mydb.db";
            File dbPath = ctx.getDatabasePath(dbName);

            db = SQLiteDatabase.openDatabase(dbPath.toString(),
                    null, SQLiteDatabase.CREATE_IF_NECESSARY);
            //set data to insert
            Date today = new Date();
            String date, time;
            DateFormat dateFormatter, timeFormatter;
            Locale currentLocale = Locale.getDefault();
            //get date
            dateFormatter = DateFormat.getDateInstance(DateFormat.DEFAULT, currentLocale);
            date = dateFormatter.format(today);
            //get time
            timeFormatter = DateFormat.getTimeInstance(DateFormat.DEFAULT, currentLocale);
            time = timeFormatter.format(today);

            //pair value
            ContentValues pairValue = new ContentValues();
            pairValue.put("gameDate", date);
            pairValue.put("gameTime", time);
            pairValue.put("opponentName", opponentName);
            if (myTurn == true) {
                pairValue.put("WinOrLost", "win");
            } else {
                pairValue.put("WinOrLost", "lost");
            }
            String sql;
            //create table "GamesLog"
            /*sql = "DROP TABLE if exists GamesLog;";
            db.execSQL(sql);*/
            sql = "CREATE TABLE IF NOT EXISTS\"GamesLog\" (\"gameDate\"	TEXT NOT NULL,\"gameTime\"	TEXT NOT NULL,\"opponentName\"	TEXT NOT NULL,\"winOrLost\"	TEXT NOT NULL)";
            db.execSQL(sql);
            long rowPosition = db.insert("GamesLog", null, pairValue);
            Log.d("database", "onCreate: rowPosition " + rowPosition);

            db.close();
        } catch (SQLiteException e) {
            Log.d("database", "onCreate: " + e.toString());
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnContinue:
                intent = new Intent(this, GameRevealHand.class);
                startActivity(intent);
                break;
            case R.id.btnQuit:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
        finish();
    }
}
