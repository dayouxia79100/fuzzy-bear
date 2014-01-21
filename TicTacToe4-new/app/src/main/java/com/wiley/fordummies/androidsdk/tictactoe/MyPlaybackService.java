package com.wiley.fordummies.androidsdk.tictactoe;

import com.wiley.fordummies.androidsdk.tictactoe.R;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;

public class MyPlaybackService extends Service {
	MediaPlayer player;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		player = MediaPlayer.create(this, R.raw.sampleaudio);
		player.setLooping(true);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Bundle extras = intent.getExtras();
		if(extras !=null){
			String audioFileURIString = extras.getString("URIString");
			Uri audioFileURI=Uri.parse(audioFileURIString);
			try {
				player.reset();
				player.setDataSource(this.getApplicationContext(), audioFileURI);
				player.prepare();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		player.start();
		return START_STICKY;
	}
	@Override
	public void onDestroy() {
		player.stop();
	}

}