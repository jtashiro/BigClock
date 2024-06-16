package com.fiospace.bigclock;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView textViewTime;
    private TextView textViewDate;

    private Handler handler = new Handler();
    private Runnable runnable;
    private boolean showColon = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        // Keep the screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        textViewTime = findViewById(R.id.textViewTime);
        textViewDate = findViewById(R.id.textViewDate);

        runnable = new Runnable() {
            @Override
            public void run() {
                updateTime();
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);
    }

    private void updateTime() {
        String currentDate = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(new Date());
        textViewDate.setText(currentDate);

        // Format the time to show only hours and minutes
        String currentTime = new SimpleDateFormat(showColon ? "HH:mm" : "HH mm", Locale.getDefault()).format(new Date());
        textViewTime.setText(currentTime);

        // Toggle the colon every second
        showColon = !showColon;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

}