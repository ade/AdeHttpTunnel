package se.ade.httptunnel;

/**
 * Created by adrnil on 2013-08-29.
 */
public interface AbstractLogger {
    public void v(String tag, String msg);
    public void i(String tag, String msg);
    public void d(String tag, String msg);
    public void e(String tag, String msg);
    public void w(String tag, String msg);
}
