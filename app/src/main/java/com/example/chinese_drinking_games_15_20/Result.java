package com.example.chinese_drinking_games_15_20;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Result extends AppCompatActivity implements View.OnClickListener {

    TextView tvResult, tvName, tvFinalGuess, tvOpponentName;
    ImageView ivLeftHand, ivRightHand, ivOpponentLeftHand, ivOpponentRightHand;
    Button btnContinue;

    int winRound, leftHand, rightHand, finalGuess, opponentLeftHand, opponentRightHand;
    String name, opponentName;
    JSONObject obj;
    Boolean myTurn;

    //@RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //UI for textView
        tvResult = findViewById(R.id.tvResult);
        tvFinalGuess = findViewById(R.id.tvFinalGuess);
        tvName = findViewById(R.id.tvName);
        tvOpponentName = findViewById(R.id.tvOpponentName);
        //UI for imageView
        ivLeftHand = findViewById(R.id.ivLeftHand);
        ivRightHand = findViewById(R.id.ivRightHand);
        ivOpponentLeftHand = findViewById(R.id.ivOpponentLeftHand);
        ivOpponentRightHand = findViewById(R.id.ivOpponentRightHand);
        //Button blinding and set onClickListener
        btnContinue = findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(this);
        //get intent
        myTurn = getIntent().getBooleanExtra("myTurn", false);

        //get json and set value
        //disable thread policy that force AsyncTask to go online
        //because the app have to get json from internet first, then performing tasks
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //get json string
        String result = getStringFromURL();
        //convert to json object, then set variable
        jsonToVariable(result);

        //get shared preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        name = prefs.getString("name", "Unknown");
        leftHand = prefs.getInt("leftHand", 0);
        rightHand = prefs.getInt("rightHand", 0);
        winRound = prefs.getInt("winRound", 0);
        setFinalGuess(prefs, obj);
        setImageView();
        result();
    }

    //set imageView by result
    //for easy to see left and right hand from the user perspective, the opponent's left and right hand switch
    private void setImageView() {
        tvOpponentName.setText(opponentName);
        if (leftHand == 0) {
            ivLeftHand.setImageResource(R.drawable.hand_close);
        } else {
            ivLeftHand.setImageResource(R.drawable.hand_open);
        }
        if (rightHand == 0) {
            ivRightHand.setImageResource(R.drawable.hand_close);
        } else {
            ivRightHand.setImageResource(R.drawable.hand_open);
        }
        if (opponentLeftHand == 0) {
            ivOpponentLeftHand.setImageResource(R.drawable.hand_close);
            ivOpponentLeftHand.setRotation(180); // rotate 90 degree
        } else {
            ivOpponentLeftHand.setImageResource(R.drawable.hand_open);
            ivOpponentLeftHand.setRotation(180); // rotate 90 degree
        }
        if (opponentRightHand == 0) {
            ivOpponentRightHand.setImageResource(R.drawable.hand_close);
            ivOpponentRightHand.setRotation(180); // rotate 90 degree
        } else {
            ivOpponentRightHand.setImageResource(R.drawable.hand_open);
            ivOpponentRightHand.setRotation(180); // rotate 90 degree
        }
    }

    //get opponent json string from url
    public String getStringFromURL() {
        InputStream inputStream = null;
        String result = "";
        URL url = null;
        try {
            //get JSON from the server
            //for example {"name": "Taiman", "left": 5, "right": 5, "guess": 15}
            url = new URL("https://4qm49vppc3.execute-api.us-east-1.amazonaws.com/Prod/itp4501_api/opponent/1");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            // Make GET request
            // May omit this line since "GET" is the default.
            con.setRequestMethod("GET");
            con.connect();

            // Get response string from inputStream of connection
            inputStream = con.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = bufferedReader.readLine()) != null)
                result += line;
            inputStream.close();
            jsonToVariable(result);
        } catch (Exception e) {
            Context context = getApplicationContext();
            CharSequence text = "Can not connect to internet";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        return result;
    }

    //string converts to json and set variables
    public void jsonToVariable(String result) {
        try {
            String json = result;
            obj = new JSONObject(json);
            opponentName = obj.getString("name");
            opponentLeftHand = obj.getInt("left");
            opponentRightHand = obj.getInt("right");
        } catch (Exception e) {
            Log.d("json", e.toString());
        }
    }

    //set finalGuess
    public void setFinalGuess(SharedPreferences prefs, JSONObject obj) {
        Log.d("myturn", "setFinalGuess: myturn" + myTurn);
        if (myTurn == true) {
            finalGuess = prefs.getInt("guess", 0);
            tvFinalGuess.setText("My Guess: " + finalGuess);
        } else {
            try {
                finalGuess = obj.getInt("guess");
                tvFinalGuess.setText(opponentName + "'s Guess: " + finalGuess);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //determine win or lost
    public boolean deterWin() {
        if (finalGuess == leftHand + rightHand + opponentLeftHand + opponentRightHand) {
            return true;
        } else {
            return false;
        }
    }

    //get result of this turn
    public void result() {
        //you guess right
        if (myTurn == true && deterWin() == true) {
            tvResult.setText("You win");
            winRound++;
            myTurn = true;
            //you guess wrong or opponent guess wrong
        } else if ((myTurn == true && deterWin() == false) || (myTurn == false && deterWin() == false)) {
            tvResult.setText("No One Win");
            myTurn = !myTurn;
            winRound = 0;
            //opponent guess right
        } else {
            tvResult.setText("You Lost");
            winRound++;
            myTurn = false;
        }
    }

    @Override
    public void onClick(View v) {
        Log.d("final win round", "onClick: win round " + winRound);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("winRound", winRound);
        editor.commit();
        //if win two times continuously, go to FinalResult page
        if (winRound == 2) {
            winRound = 0;
            Intent intent = new Intent(this, FinalResult.class);
            intent.putExtra("myTurn", myTurn);
            intent.putExtra("opponentName", opponentName);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, GameRevealHand.class);
            intent.putExtra("myTurn", myTurn);
            intent.putExtra("opponentName", opponentName);
            startActivity(intent);
        }
        finish();
    }
}
