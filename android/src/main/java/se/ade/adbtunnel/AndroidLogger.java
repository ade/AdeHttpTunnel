package se.ade.adbtunnel;

import android.util.Log;

import se.ade.httptunnel.AbstractLogger;

/**
 * Created by adrnil on 2013-08-29.
 */
public class AndroidLogger implements AbstractLogger {
    @Override
    public void v(String tag, String msg) {
        Log.v(tag, msg);
    }

    @Override
    public void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    @Override
    public void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    @Override
    public void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    @Override
    public void w(String tag, String msg) {
        Log.w(tag, msg);
    }
}
