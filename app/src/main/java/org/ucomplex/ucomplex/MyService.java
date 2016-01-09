package org.ucomplex.ucomplex;

            import android.app.Activity;
            import android.app.IntentService;
            import android.content.Intent;
            import android.os.IBinder;

            import org.ucomplex.ucomplex.Activities.Tasks.FetchMessagesTask;
            import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;


/**
 * Created by Sermilion on 03/12/2015.
 */
            public class MyService extends IntentService {

                Activity context;
                OnTaskCompleteListener listener;

                public MyService() {
                    super("Message update");
                    context = (Activity) getBaseContext();
                    listener = (OnTaskCompleteListener) getBaseContext();
                }

                @Override
                public IBinder onBind(Intent arg0) {
                    return null;

                }

                @Override
                protected void onHandleIntent(Intent intent) {
                    while(true)
                    {
                        FetchMessagesTask fetchMessagesTask = new FetchMessagesTask(context, listener);
                        fetchMessagesTask.setType(2);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent,flags,startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}