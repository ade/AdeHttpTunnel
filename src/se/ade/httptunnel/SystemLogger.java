package se.ade.httptunnel;

/**
 * Created by adrnil on 2013-08-29.
 */
public class SystemLogger implements AbstractLogger {
    @Override
    public void v(String tag, String msg) {
        System.out.println("[VERBOSE, " + tag + "]: " + msg);
    }

    @Override
    public void i(String tag, String msg) {
        System.out.println("[INFO, " + tag + "]: " + msg);
    }

    @Override
    public void d(String tag, String msg) {
        System.out.println("[DEBUG, " + tag + "]: " + msg);
    }

    @Override
    public void e(String tag, String msg) {
        System.out.println("[ERROR, " + tag + "]: " + msg);
    }

    @Override
    public void w(String tag, String msg) {
        System.out.println("[WARN, " + tag + "]: " + msg);
    }
}
