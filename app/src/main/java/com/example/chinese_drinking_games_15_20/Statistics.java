package com.example.chinese_drinking_games_15_20;

import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.File;

/**
 * The type Statistics.
 */
public class Statistics extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new Panel(this));
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    /**
     * The type Panel.
     */
    class Panel extends View {
        private Context context;

        /**
         * Instantiates a new Panel.
         *
         * @param context the context
         */
        public Panel(Context context) {
            super(context);
        }

        /**
         * The Title.
         */
        String title = "Win Rate";
        /**
         * The Items.
         */
        String items[] = {"Win", "Lost"};

        /**
         * The R color.
         */
        int rColor[] = {0xff85C1E9, 0xff2ECC71};
        /**
         * The Data.
         */
        long data[] = new long[2];
        /**
         * The Win.
         */
        long win, /**
         * The Lost.
         */
        lost;

        /**
         * Draw the pie chart and bar chart of win and lost ratio. The data comes rom database
         *
         * @param c
         */
        @Override
        public void onDraw(Canvas c) {
            super.onDraw(c);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);

            // Make the entire canvas in white
            paint.setColor(Color.WHITE);
            c.drawPaint(paint);

            //open database
            SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.example.chinese_drinking_games_15_20/databases/mydb.db", null, SQLiteDatabase.OPEN_READONLY);
            //find number of records of winOrLost = 'win' and 'lost'
            win = DatabaseUtils.queryNumEntries(db, "GamesLog", "winOrLost='win'");
            lost = DatabaseUtils.queryNumEntries(db, "GamesLog", "winOrLost='lost'");
            db.close();
            //set data of win rate chart
            data[0] = win;
            data[1] = lost;
            db.close();

            /**
             *  Draw pie chart of win rate
             */
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            float startAngle = 0;
            int y = getHeight() - 80;
            for (int i = 0; i < data.length; i++) {
                float oval = (float) (data[i] * 360 / (win + lost));
                int diameter = Math.min(this.getWidth(), this.getHeight()) - 500;
                //set different
                paint.setColor(rColor[i]);
                //draw rectangle
                RectF rec = new RectF(5, 100, diameter, diameter + 80);
                //draw arc
                c.drawArc(rec, startAngle, oval, true, paint);
                startAngle += oval;
                y += 20;
            }

            /** Draw the title
             *
             */
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(70);
            paint.setTypeface(Typeface.SERIF);
            c.drawText(title, getWidth() / 2 - 450, 60, paint);

            int y2 = getHeight() - 1500;
            paint.setTextSize(60);
            for (int i = items.length - 1; i >= 0; i--) {
                //set different color
                paint.setColor(rColor[i]);
                // Draw legend rectangles
                c.drawRect(20, y2 - 30, 50, y2, paint);
                // Draw labels
                c.drawText(items[i], 70, y2, paint);
                y2 += 70;
            }

            /**
             *  Draw the bar chart of win rate
             */
            int y3 = getHeight() - 80;
            paint.setColor(Color.BLACK);
            c.drawLine(20, y3 - 200, 800, y3 - 200, paint);
            paint.setColor(rColor[0]);
            int y5 = y3 - 200;
            for (int i = 0; i < win; i++) {
                c.drawRect(60, y5, 110, y5 - 50, paint);
                y5 -= 50;
            }
            c.drawText(String.valueOf(win), 50, y3 - 100, paint);
            c.drawText("win", 50, y3 - 20, paint);
            paint.setColor(rColor[1]);
            int y4 = y3 - 200;
            for (int i = 0; i < lost; i++) {
                c.drawRect(510, y4, 560, y4 - 50, paint);
                y4 -= 50;
            }
            c.drawText(String.valueOf(lost), 500, y3 - 100, paint);
            c.drawText("lost", 500, y3 - 20, paint);
        }
    }
}
