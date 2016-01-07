package org.ucomplex.ucomplex;

            import android.app.Activity;
            import android.app.IntentService;
            import android.app.Service;
            import android.content.Intent;
            import android.os.Handler;
            import android.os.IBinder;
            import android.widget.Toast;

            import org.ucomplex.ucomplex.Activities.Tasks.FetchMessagesTask;
            import org.ucomplex.ucomplex.Activities.Tasks.OnTaskCompleteListener;


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
                    Toast.makeText(this, "onhandleintent", Toast.LENGTH_SHORT).show();
                    while(true)
                    {
                        FetchMessagesTask fetchMessagesTask = new FetchMessagesTask(context, listener);
                        fetchMessagesTask.setType(2);
                Toast.makeText(getApplicationContext(), "getting app count", Toast.LENGTH_LONG).show();
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

/**
 * logged
 * loggedUser
 * student
 * profilePhoto
 * tempProfilePhoto
 */
