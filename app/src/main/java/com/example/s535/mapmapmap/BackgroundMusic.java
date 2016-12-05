package com.example.s535.mapmapmap;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

/**
 * Created by MinJun on 2016-12-06.
 */

public class BackgroundMusic extends Service{
    private MediaPlayer music=null;

    public IBinder onBind(Intent intent){return null;}
    public int onStartCommand(Intent intent, int flags, int startId){
        super.onStartCommand(intent, flags, startId);
        music=MediaPlayer.create(this, R.raw.harry_potter);
        music.setLooping(true);
        music.start();
        return START_NOT_STICKY;
    }
    public void onDesroy(){
        music.stop();
        super.onDestroy();
    }
}