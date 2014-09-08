package se.ade.adbtunnel;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import se.ade.httptunnel.AbstractLogger;
import se.ade.httptunnel.MultiLog;

public class MainActivity extends ActionBarActivity {
    private TextView logtext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MultiLog.setLogger(new AbstractLogger() {
            @Override
            public void v(String s, String s2) {
                log(s2);
            }

            @Override
            public void i(String s, String s2) {
                log(s2);
            }

            @Override
            public void d(String s, String s2) {
                log(s2);
            }

            @Override
            public void e(String s, String s2) {
                log(s2);
            }

            @Override
            public void w(String s, String s2) {
                log(s2);
            }
        });

        logtext = (TextView)findViewById(R.id.logtext);

        MultiLog.setLogLevel(MultiLog.LOG_LEVEL_7_NETWORK);

        final Button button = (Button)findViewById(R.id.start_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, TunnelService.class);
                startService(i);
                button.setText("Started");
                button.setEnabled(false);
            }
        });
    }

    private void log(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logtext.append(s + "\n");
            }
        });

    }
}
