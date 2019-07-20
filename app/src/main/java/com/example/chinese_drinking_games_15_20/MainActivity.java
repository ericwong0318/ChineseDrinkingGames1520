package com.example.chinese_drinking_games_15_20;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnStart, btnUpdate, btnStat;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.btnStart);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnStat = findViewById(R.id.btnStat);

        btnStart.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnStat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btnStart:
                    //set winRound = 0 because the game is started
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                    //if can not find isRegister, means the user is not registered
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("winRound", 0);
                    editor.commit();

                    intent = new Intent(this, GameRevealHand.class);
                    startActivity(intent);
                    finish();
                    break;

                case R.id.btnUpdate:
                    intent = new Intent(this, UpdateInform.class);
                    startActivity(intent);
                    break;

                case R.id.btnStat:
                    //open database
                    try {
                        //check past history
                        //if database isn't existed, mean no past statistics
                        Context ctx = this; // for Activity, or Service. Otherwise simply get the context.
                        String dbName = "mydb.db";
                        File dbPath = ctx.getDatabasePath(dbName);
                        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath.toString(), null, SQLiteDatabase.OPEN_READONLY);
                        db.close();
                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), R.string.databaseException, Toast.LENGTH_LONG).show();
                        break;
                    }
                    intent = new Intent(this, Statistics.class);
                    startActivity(intent);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
