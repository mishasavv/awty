package edu.washington.mikhail3.arewethereyet;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
    Button button;
    TextView message;
    TextView number;
    TextView time;
    AlarmManager am;
    PendingIntent alarmIntent = null;
    BroadcastReceiver alarmReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent i) {
            //Toast.makeText(MainActivity.this, number.getText() + ": " + message.getText(), Toast.LENGTH_SHORT).show();
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(number.getText().toString(), null, message.getText().toString(), null, null);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "SMS FAILED", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        message = (TextView) findViewById(R.id.message);
        number = (TextView) findViewById(R.id.phone);
        time = (TextView) findViewById(R.id.interval);
        button = (Button) findViewById(R.id.button);
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Main", "Click");
                if (alarmIntent != null) {
                    am.cancel(alarmIntent);
                    alarmIntent.cancel();
                    alarmIntent = null;
                    button.setText("START");
                } else if (message.getText().toString().length() > 0 && number.getText().toString().length() >= 7 && Integer.parseInt(time.getText().toString()) > 0) {
                    Intent i = new Intent();
                    int repeatTime = Integer.parseInt(time.getText().toString());
                    i.setAction("edu.washington.mikhail3.arewethereyet");
                    registerReceiver(alarmReceiver, new IntentFilter("edu.washington.mikhail3.arewethereyet"));
                    alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, i, 0);
                    am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), (repeatTime * 60 * 1000), alarmIntent);
                    button.setText("STOP");
                }
            }
        });
    }
}


