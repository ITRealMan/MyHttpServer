package com.oeasy.myhttpserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

/**
 * @author : Created IT_RealMan.J.W
 * @date :  2018/1/9.
 * Des :
 */

public class FaceRecognitionService extends Service {

    public static final String TAG = FaceRecognitionService.class.getSimpleName();

    private FaceRecognitionServer faceRecognitionServer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        faceRecognitionServer = new FaceRecognitionServer(this);
        try {
            faceRecognitionServer.start();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "onCreate: " + e);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        faceRecognitionServer.stop();
        Log.d(TAG, "onDestroy: ");
    }
}
