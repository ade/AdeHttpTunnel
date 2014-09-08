package se.ade.httptunnel.server;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by adrnil on 08/09/14.
 */
public class DirectWriter extends DataOutputStream {
    public DirectWriter(OutputStream out) {
        super(out);
    }

    public void writeJsonObject(JSONObject obj) throws IOException {
        String jsonStr = obj.toString();
        byte[] jsonBytes = jsonStr.getBytes();
        this.writeInt(jsonBytes.length);
        this.write(jsonBytes);
    }
}
