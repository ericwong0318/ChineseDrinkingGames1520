package com.example.chinese_drinking_games_15_20;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UpdateInform extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private final static String MY_PREFS_NAME = "com.example.chinese_drinking_games_15_20_preferences";
    public SharedPreferences sp;
    private EditText name, DoB, phoneNo, email;
    private Button save, clearAll;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // bind GUI elements with local controls
        name = findViewById(R.id.etName);
        DoB = findViewById(R.id.etDoB);
        phoneNo = findViewById(R.id.etPhoneNo);
        email = findViewById(R.id.etEmail);
        save = findViewById(R.id.btnSave);
        clearAll = findViewById(R.id.btnClearAll);

        //set onClickListener
        DoB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(UpdateInform.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        save.setOnClickListener(this);
        clearAll.setOnClickListener(this);
        //set addTextChangedListener
        name.addTextChangedListener(this);
        DoB.addTextChangedListener(this);
        phoneNo.addTextChangedListener(this);
        email.addTextChangedListener(this);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
                //getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);

        //get data
        if (sp.contains("name")) {
            name.setText(sp.getString("name", ""));
        }
        if (sp.contains("DoB")) {
            DoB.setText(sp.getString("DoB", ""));
        }
        if (sp.contains("phoneNo")) {
            phoneNo.setText(sp.getString("phoneNo", ""));
        }
        if (sp.contains("email")) {
            email.setText(sp.getString("email", ""));
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    //input validation
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(name.getText().toString().equals("")){
            name.setError("Input something");
        }
        if(DoB.getText().toString().equals("")){
            DoB.setError("Input something");
        }
        if(email.getText().toString().equals("")){
            email.setError("Input something");
        }
        if(phoneNo.getText().toString().equals("")){
            phoneNo.setError("Input something");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {}

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    //set format of DoB
    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        DoB.setText(sdf.format(myCalendar.getTime()));
    }

    //onClick event
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave: //call save()
                save(v);
                break;
            case R.id.btnClearAll: //call clear()
                clear(v);
                break;
        }
    }

    //save data to sharedPreference
    public void save(View view) {
        //editText can not be null
        if (!name.getText().toString().equals("")&&!DoB.getText().toString().equals("")&&!phoneNo.getText().toString().equals("")&&!email.getText().toString().equals("")) {
            try {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("name", name.getText().toString());
                //showDialog(1);
                editor.putString("DoB", DoB.getText().toString());
                editor.putString("phoneNo", phoneNo.getText().toString());
                editor.putString("email", email.getText().toString());
                editor.putBoolean("isRegister", true);
                editor.commit();
                finish();
            } catch (Exception e){
                Log.d("pref", "save: "+e);
            }
        } else{
            Toast.makeText(getApplicationContext(), "Can not save because some information is missing",Toast.LENGTH_LONG).show();
            return;
        }
    }

    //set all text to null
    public void clear(View view) {
        name.setText("");
        email.setText("");
        DoB.setText("");
        phoneNo.setText("");
    }
}
