package org.ucomplex.ucomplex.Model;

import android.app.Application;

/**
 * Created by Sermilion on 15/11/2015.
 * Singleton class for sharing large data between activities.
 */
public class Globals extends Application {

    private static Object shareInstance;
    private static Globals instance;

    static {
        instance = new Globals();
    }

    public static Globals getInstance() {
        return Globals.instance;
    }

    public Globals(){

    }
    public Globals(Object object){
        shareInstance = object;
    }

    public Object getShareInstance() {
        return shareInstance;
    }

    public void setShareInstance(Object shareInstance) {
        Globals.shareInstance = shareInstance;
    }

    public void clear(){
        shareInstance = null;
    }
}
