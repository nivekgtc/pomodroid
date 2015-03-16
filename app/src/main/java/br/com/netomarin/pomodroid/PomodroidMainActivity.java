package br.com.netomarin.pomodroid;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class PomodroidMainActivity extends Activity {

    private long mTimeLeft;

    private Button pomodoroButton;
    private TextView timerTextView;

    private TimerTicReceiver timerTicReceiver;
    private Intent timerService;
    private int currentState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomodroid_main);

        timerTextView = (TextView) findViewById(R.id.timerTextView);
        pomodoroButton = (Button) findViewById(R.id.startPomodoroButton);
        pomodoroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPomodoro();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timerTicReceiver == null) {
            timerTicReceiver = new TimerTicReceiver();
            registerReceiver(timerTicReceiver, new IntentFilter(PomodoroTimerService.
                    TIC_BROADCAST_MESSAGE));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        if (timerTicReceiver != null) {
            unregisterReceiver(timerTicReceiver);
            timerTicReceiver = null;
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void startPomodoro() {
        startTimerService();
    }

    private void startTimerService() {
        timerService = new Intent(this, PomodoroTimerService.class);
        startService(timerService);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pomodroid_main, menu);
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

    private class TimerTicReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            long timeLeft = intent.getLongExtra(PomodoroTimerService.TIC_BROADCAST_PAYLOAD, 0);
            int state = intent.getIntExtra(PomodoroTimerService.TIC_BROADCAST_STATE, 0);
            timerTextView.setText(getString(R.string.txt_time_remaining) + " " +
                    Commons.getRemainingTimeString(timeLeft));
        }
    }
}
