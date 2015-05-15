package edu.washington.mikhail3.arewethereyet;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
            Toast.makeText(MainActivity.this, number.getText() + ": " + message.getText(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        message = (TextView) findViewById(R.id.message);
        number = (TextView) findViewById(R.id.phone);
        time = (TextView) findViewById(R.id.time);
        button = (Button) findViewById(R.id.button);
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alarmIntent != null) {
                    am.cancel(alarmIntent);
                    alarmIntent.cancel();
                    alarmIntent = null;
                    button.setText("start");
                } else if (message.getText().length() > 0 && number.getText().length() >= 10 && Integer.parseInt(time.getText().toString()) > 0 && time.length() > 0) {
                    Intent i = new Intent();
                    int repeatTime = Integer.parseInt((String) time.getText());
                    i.setAction("edu.washington.mikhail3.arewethereyet");
                    registerReceiver(alarmReceiver, new IntentFilter("edu.washington.mikhail3.arewethereyet"));
                    alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, i, 0);
                    am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), (repeatTime * 60 * 1000), alarmIntent);
                    button.setText("stop");
                }
            }
        });
    }
}


