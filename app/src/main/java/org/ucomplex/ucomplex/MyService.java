package org.ucomplex.ucomplex;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Sermilion on 03/12/2015.
 */
public class MyService extends IntentService {

    Context context = this;
    OnTaskCompleteListener listener;

    public MyService() {
        super("Message update");
        listener = (OnTaskCompleteListener) getBaseContext();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;

    }

    @Override
    protected void onHandleIntent(final Intent intent) {
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Common.fetchMyNews(context);
                }
            }, 0, 5000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}