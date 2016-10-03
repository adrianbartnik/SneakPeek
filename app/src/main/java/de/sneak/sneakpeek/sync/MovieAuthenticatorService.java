package de.sneak.sneakpeek.sync;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class MovieAuthenticatorService extends Service {

    private MovieAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        Log.d("Serivce", "Created!!!");
        mAuthenticator = new MovieAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("Serivce", "Bound!!!");

        return mAuthenticator.getIBinder();
    }
}

