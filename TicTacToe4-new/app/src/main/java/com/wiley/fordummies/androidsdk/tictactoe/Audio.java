package com.wiley.fordummies.androidsdk.tictactoe;

import java.io.File;

import com.wiley.fordummies.androidsdk.tictactoe.R;
import com.wiley.fordummies.androidsdk.tictactoe.MyPlaybackService;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Audio extends Activity implements OnClickListener{
	private boolean started=false;
	String audioFilePath="/mnt/sdcard/SampleAudio.mp3";
	public static int AUDIO_CAPTURED = 1;
	Uri audioFileURI=null;
	static final String TAGACTIVITYAUDIO="Audio";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audio);
		
		Button buttonStart = (Button) findViewById(R.id.buttonAudioStart);
	    buttonStart.setOnClickListener(this);
	    Button buttonStop = (Button) findViewById(R.id.buttonAudioStop);
	    buttonStop.setOnClickListener(this);
	    Button buttonRecord = (Button) findViewById(R.id.buttonAudioRecord);
	    buttonRecord.setOnClickListener(this);

		Button btnExit = (Button) findViewById(R.id.buttonAudioExit);
		btnExit.setOnClickListener(this);
		audioFileURI = Uri.fromFile(new File(audioFilePath));
	}
	

	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.buttonAudioStart:
			if(!started){
				Intent musicIntent = new Intent(this, MyPlaybackService.class);
				musicIntent.putExtra("URIString", audioFileURI.toString());
				startService(musicIntent);
				started=true;
			}
    	    break;
	    case R.id.buttonAudioStop:
	        stopService(new Intent(this, MyPlaybackService.class));
	        started=false;
	        break;
	    case R.id.buttonAudioRecord:
	    	 Intent audioRecordIntent =  new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
	    	 startActivityForResult(audioRecordIntent,AUDIO_CAPTURED); 
	    	 break;
	    case R.id.buttonAudioExit:
		   	finish();
		   	break;
		}
		
	}
	protected void onActivityResult (int requestCode, int resultCode, Intent data) { 
        if (resultCode == RESULT_OK && requestCode == AUDIO_CAPTURED) { 
                audioFileURI = data.getData(); 
                Log.v(TAGACTIVITYAUDIO, "Audio File URI: >" + audioFileURI + "<");
        } 
	}
}