package se.ade.httptunnel;

import com.google.common.eventbus.EventBus;

public class Events extends EventBus {
    static Events instance;
    public static Events get() {
        if(instance == null) {
            instance = new Events();
        }

        return instance;
    }
}
