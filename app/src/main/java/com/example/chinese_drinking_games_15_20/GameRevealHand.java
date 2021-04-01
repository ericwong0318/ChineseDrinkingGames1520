package com.example.chinese_drinking_games_15_20;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GameRevealHand extends AppCompatActivity implements View.OnClickListener {
    ViewPager mViewPager;
    CustomPagerAdapter mCustomPagerAdapter;
    Button btnNext;
    Intent intent;
    Boolean isRegister;
    int winRound;

    int[] mResources = {
            R.drawable.hand0and0,
            R.drawable.hand0and5,
            R.drawable.hand5and0,
            R.drawable.hand5and5,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_reveal_hand);
        //view pager
        mCustomPagerAdapter = new CustomPagerAdapter(this);
        mViewPager = findViewById(R.id.pagerHand);
        mViewPager.setAdapter(mCustomPagerAdapter);
        //set button
        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
        //SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //set winRound = 0 because you win the game and press btnContinue to replay but winRound is 2
        SharedPreferences.Editor editor = prefs.edit();
        winRound = prefs.getInt("winRound",0);
        if (winRound >= 2) {
            editor.putInt("winRound", 0);
            editor.commit();
        }

        //check user is register or not (isRegistered exists in SharedPreferences?)
        //if can not find isRegister in SharedPreferences,
        // means the user is not registered,
        // the app will go to register page
        isRegister = prefs.getBoolean("isRegister", false);
        if (isRegister == false) {
            intent = new Intent(this, UpdateInform.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        //choose different picture lead to different hand
        int selection = mViewPager.getCurrentItem();
        int leftHand, rightHand;
        switch (selection) {
            case 1:
                leftHand = 0;
                rightHand = 5;
                break;
            case 2:
                leftHand = 5;
                rightHand = 0;
                break;
            case 3:
                leftHand = 5;
                rightHand = 5;
                break;
            default:
                leftHand = 0;
                rightHand = 0;
        }
        //save hand choose
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("leftHand", leftHand);
        editor.putInt("rightHand", rightHand);
        editor.commit();

        //intent
        Boolean myTurn = getIntent().getBooleanExtra("myTurn", true);
        //if myTurn is true, I have to guess
        if (myTurn == true) {
            intent = new Intent(this, GameGuessing.class);
            //if myTurn is false, I don't have to guess
        } else {
            intent = new Intent(this, Result.class);
        }
        startActivity(intent);
        finish();
    }

    //adapter for pager
    class CustomPagerAdapter extends PagerAdapter {
        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mResources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

            ImageView imageView = itemView.findViewById(R.id.imageView);
            imageView.setImageResource(mResources[position]);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}