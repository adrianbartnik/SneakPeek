package de.sneak.sneakpeek.sync;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class MovieSyncService extends Service {

    private static final String TAG = MovieSyncService.class.getSimpleName();

    private static final Object sSyncAdapterLock = new Object();
    private static MovieSyncAdapter movieSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate - MovieSyncService");
        synchronized (sSyncAdapterLock) {
            if (movieSyncAdapter == null) {
                movieSyncAdapter = new MovieSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return movieSyncAdapter.getSyncAdapterBinder();
    }
}