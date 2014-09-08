package se.ade.adbtunnel;

import android.app.IntentService;
import android.content.Intent;

import se.ade.httptunnel.MultiLog;
import se.ade.httptunnel.fakehttp.client.MeetingPointToRemoteServiceClient;

public class TunnelService extends IntentService {
    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public TunnelService() {
        super("TunnelService");
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        MultiLog.v(this, "Starting...");
        MeetingPointToRemoteServiceClient client = new MeetingPointToRemoteServiceClient("127.0.0.1", 5555, "ade.se", 7575, "adb");
        try {
            client.start();
        } catch (Exception e) {
            MultiLog.e(this, "Error: " + e.getMessage());
        }

    }
}