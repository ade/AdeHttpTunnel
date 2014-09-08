package se.ade.httptunnel;

/**
 * Created by adrnil on 07/09/14.
 */
public class Events {
    static com.google.common.eventbus.EventBus instance;
    public static Events get() {
        if(instance == null) {
            instance = new Events();
        }
        return instance;
    }

    public void publish (Object e) {

    }
}
