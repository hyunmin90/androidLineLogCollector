package com.lineplus.uploader;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.content.Context;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    SharedPreferences prefs = null;
    private boolean startedFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DateFormat dateForm = new SimpleDateFormat("HH:mm:ss");


        //Toast.makeText(MainActivity.this, "Its been done", Toast.LENGTH_SHORT).show();
        if(startedFlag==false)
        {

            try {
                TelephonyManager tManager1 = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String uuid1 = tManager1.getDeviceId();
                if(!new uploader().run(uuid1)==false)
                {
                    Toast.makeText(MainActivity.this, "There is no file to upload. Try again later", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Files sent", Toast.LENGTH_SHORT).show();
                }
                startedFlag=true;
            } catch (Exception e) {
                startedFlag=true;
                e.printStackTrace();
            }
        }
        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String uuid = tManager.getDeviceId();

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 23);
        today.set(Calendar.MINUTE, 30);
        today.set(Calendar.SECOND, 0);
        MyTimeTask task = new MyTimeTask();
        task.uuid=uuid;
        Timer timer = new Timer();
        timer.schedule(task, today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));



        prefs = getSharedPreferences("com.lineplus.uploader", MODE_PRIVATE);

        Button button = (Button) findViewById(R.id.uploadBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String uuid = tManager.getDeviceId();
                    if(new uploader().run(uuid)==false)
                    {
                        Toast.makeText(MainActivity.this, "There is no file to upload. Try again later", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(MainActivity.this, "Files sent!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            startedFlag = false;
        }
    }

    private static class MyTimeTask extends TimerTask
    {
        String uuid;
        public void run()
        {
            System.out.println("Code Executed at time"+new Date().getTime());
            try {
                new uploader().run(uuid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
