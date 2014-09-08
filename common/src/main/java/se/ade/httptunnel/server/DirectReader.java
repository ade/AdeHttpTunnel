package se.ade.httptunnel.server;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by adrnil on 08/09/14.
 */
public class DirectReader extends DataInputStream {
    public DirectReader(InputStream in) {
        super(in);
    }

    public JSONObject readJsonObject() throws IOException {
        int headerLength = 32;//this.readInt();
        byte[] headerBytes = new byte[headerLength];
        this.readFully(headerBytes);
        String headerString = new String(headerBytes);
        JSONObject jsonObject = new JSONObject(headerString);
        return jsonObject;
    }
}
