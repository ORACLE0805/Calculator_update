package com.example.ylmusic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class MusicService extends Service {

    private MediaPlayer player;
    private String url;
    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        player=new MediaPlayer();

        return super.onStartCommand(intent, flags, startId);
    }
    public Mybinder binder=new Mybinder();
    public class Mybinder extends Binder{
        MusicService getService(){
            return MusicService.this;
        }

    }
}
