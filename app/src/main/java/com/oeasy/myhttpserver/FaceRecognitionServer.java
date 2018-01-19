package com.oeasy.myhttpserver;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.google.gson.JsonObject;
import com.oeasy.myhttpserver.nanohttpd.protocols.http.IHTTPSession;
import com.oeasy.myhttpserver.nanohttpd.protocols.http.NanoHTTPD;
import com.oeasy.myhttpserver.nanohttpd.protocols.http.response.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : Created IT_RealMan.J.W
 * @date :  2018/1/9.
 * Des :
 */

public class FaceRecognitionServer extends NanoHTTPD {

    public static final int DEFAULT_SERVER_PORT = 5058;
    public static final String TAG = FaceRecognitionServer.class.getSimpleName();

    private static final String REQUEST_ROOT = "/";
    private static final String REQUEST_KNOCK = "/knock";
    private static final String REQUEST_PDNS = "/pdns";
    private Context mContext;


    public FaceRecognitionServer(Context context) {
        super(DEFAULT_SERVER_PORT);
        mContext = context;
    }


    @Override
    protected Response serve(IHTTPSession session) {
        String strUri = session.getUri();
        String method = session.getMethod().name();
        Map<String, String> files = new HashMap<>();
        try {
            session.parseBody(files);
            String body = session.getQueryParameterString();
            Log.d(TAG, "serve:body  " + body );
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ResponseException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Response serve uri = " + strUri + ", method = " + method);
        if (REQUEST_ROOT.equals(strUri)) {
            return responseRootPage(session);
        } else if (REQUEST_KNOCK.equals(strUri)) {
            return responseKnockJson();
        } else if (REQUEST_PDNS.equals(strUri)) {
            return responsePDNSJson();
        }
        return response404(session);
    }


    private Response responseRootPage(IHTTPSession session) {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html><html><body>");
        builder.append("当前KR3399Http服务已启动 \n");
        builder.append("</body></html>\n");
        //return Response.newFixedLengthResponse(Status.OK, "application/octet-stream", builder.toString());
        return Response.newFixedLengthResponse(builder.toString());
    }


    private Response responseKnockJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("ResultCode", "true");
        jsonObject.addProperty("ResultDesc", "调用成功");
        jsonObject.addProperty("Name", "PhoneTalk");
        jsonObject.addProperty("Host", getLocalIpStr(mContext));
        return Response.newFixedLengthResponse(jsonObject.toString());
    }

    private static String getLocalIpStr(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return intToIpAddr(wifiInfo.getIpAddress());
    }

    private static String intToIpAddr(int ip) {
        return (ip & 0xff) + "." + ((ip >> 8) & 0xff) + "." + ((ip >> 16) & 0xff) + "." + ((ip >> 24) & 0xff);
    }

    private Response responsePDNSJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("ResultCode", "true");
        jsonObject.addProperty("ResultDesc", "调用成功");
        jsonObject.addProperty("PID", "1eed1901108c");
        return Response.newFixedLengthResponse(jsonObject.toString());
    }

    private Response response404(IHTTPSession session) {
        String url = session.getUri();
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html><html><body>");
        builder.append("Sorry, Can't Found " + url + " !");
        builder.append("</body></html>\n");
        return Response.newFixedLengthResponse(builder.toString());
    }
}
