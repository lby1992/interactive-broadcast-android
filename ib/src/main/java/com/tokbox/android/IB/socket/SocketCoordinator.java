package com.tokbox.android.IB.socket;

import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.tokbox.android.IB.config.IBConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;


public class SocketCoordinator {

    private static final String LOG_TAG = SocketCoordinator.class.getSimpleName();
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(IBConfig.BACKEND_BASE_URL);
        } catch (URISyntaxException e) {
            Log.i(LOG_TAG, e.getMessage());
        }
    }

    public void connect() {
        try {
            mSocket.connect();
            Log.i(LOG_TAG, "connected");
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
        }

    }

    public void emitJoinRoom(String backstageSessionId) {
        if(mSocket.connected()) {
            mSocket.emit("joinRoom", backstageSessionId);
            Log.i(LOG_TAG, "joinRoom emitted");
        } else {
            Log.i(LOG_TAG, "joinRoom not emitted");
        }
    }

    public void emitJoinInteractive(JSONObject event) {
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("fanUrl", event.getString("fanUrl"));
            jsonData.put("adminId", event.getString("adminId"));
        } catch (JSONException ex) {
            Log.d(LOG_TAG, ex.getMessage());
        }
        mSocket.emit("joinInteractive", jsonData);
        if(mSocket.connected()) {
            Log.i(LOG_TAG, "joinInteractive emitted");
        } else {
            Log.i(LOG_TAG, "joinInteractive not emitted");
        }
    }

    public void authenticate() {
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("token", IBConfig.AUTH_TOKEN);
        } catch(JSONException ex) {
          Log.d(LOG_TAG, ex.getMessage());
        }
        mSocket.emit("authenticate", jsonData);
        if(mSocket.connected()) {
            Log.i(LOG_TAG, "authenticate emitted");
        } else {
            Log.i(LOG_TAG, "authenticate not emitted");
        }
    }

    public void emitJoinBroadcast(String room) {
        if(mSocket.connected()) {
            mSocket.emit("joinBroadcast", room);
            Log.i(LOG_TAG, "JoinBroadcast emitted " + room);
        } else {
            Log.i(LOG_TAG, "JoinBroadcast not emitted");
        }
    }



    public Socket getSocket() {
        return mSocket;
    }

    /*public void on(String sessionIdProducer) {
        if(mSocket.connected()) {
            mSocket.emit("joinRoom", sessionIdProducer);
            Log.i(LOG_TAG, "joinRoom emitted");
        } else {
            Log.i(LOG_TAG, "joinRoom not emitted");
        }
    }*/
    public void SendSnapShot(JSONObject data) {
        if(mSocket.connected()) {
            mSocket.emit("mySnapshot", data);
            Log.i(LOG_TAG, "mySnapshot emitted");
        } else {
            Log.i(LOG_TAG, "mySnapshot not emitted");
        }

    }

    public void disconnect() {
        if(mSocket.connected()) {
            Log.i(LOG_TAG, "socket disconnected");
            mSocket.disconnect();
        }
    }

}

