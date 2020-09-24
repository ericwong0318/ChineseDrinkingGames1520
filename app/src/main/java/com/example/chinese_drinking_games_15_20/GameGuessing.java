package com.example.chinese_drinking_games_15_20;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

/**
 * The type Game guessing.
 *  The player guess the opponent's hand, by select  a picture from a gallery
 */
public class GameGuessing extends AppCompatActivity implements View.OnClickListener, NumberPicker.OnValueChangeListener {
    /**
     * The Intent.
     */
    Intent intent;
    /**
     * The Guess.
     */
    int guess = 0;
    /**
     * The Btn next 2.
     */
    Button btnNext2;
    /**
     * The Np hand.
     */
    NumberPicker npHand;
    /**
     * The Guess choices.
     */
    String[] guessChoices = {"0", "5", "10", "15", "20"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_guessing);

        //button
        btnNext2 = findViewById(R.id.btnNext2);
        btnNext2.setOnClickListener(this);

        //number picker
        npHand = findViewById(R.id.numberPicker);
        npHand.setMinValue(0);
        npHand.setMaxValue(4);
        npHand.setDisplayedValues(guessChoices);

        npHand.setWrapSelectorWheel(true);
        npHand.setOnValueChangedListener(this);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        guess = Integer.parseInt(guessChoices[newVal]);
    }

    @Override
    public void onClick(View v) {
        //save hand choose
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("guess", guess);
        editor.commit();
        //intent
        intent = new Intent(this, Result.class);
        //Because of the user can guess, it means that myTurn = true
        intent.putExtra("myTurn", true);
        startActivity(intent);
        finish();
    }
}
