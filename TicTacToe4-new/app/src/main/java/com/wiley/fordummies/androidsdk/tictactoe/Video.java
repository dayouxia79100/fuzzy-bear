package com.wiley.fordummies.androidsdk.tictactoe;

import java.io.File;

import com.wiley.fordummies.androidsdk.tictactoe.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.VideoView;


public class Video extends Activity implements OnClickListener{
	Button buttonStart, buttonStop, buttonRecord;
	VideoView videoView=null;
	static Uri videoFileURI=null;
	public static int VIDEO_CAPTURED = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video);
		videoView = (VideoView) findViewById(R.id.videoView);
		
		buttonStart = (Button) findViewById(R.id.buttonVideoStart);
	    buttonStart.setOnClickListener(this);
	    buttonStop = (Button) findViewById(R.id.buttonVideoStop);
	    buttonStop.setOnClickListener(this);
	    buttonRecord = (Button) findViewById(R.id.buttonVideoRecord);
	    buttonRecord.setOnClickListener(this);
	    
		Button btnExit = (Button) findViewById(R.id.buttonVideoExit);
		btnExit.setOnClickListener(this);
		File videoFile = new File("/mnt/sdcard/samplevideo.3gp");
		videoFileURI = Uri.fromFile(videoFile);
	}
	

	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.buttonVideoStart:
			// Load and start the movie
		    videoView.setVideoURI(videoFileURI);
			videoView.start();
    	    break;
		case R.id.buttonVideoRecord:
			Intent intent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
			startActivityForResult(intent, VIDEO_CAPTURED);
    	    break;
		case R.id.buttonVideoStop:
	    	videoView.stopPlayback();
    	    break;
	    case R.id.buttonVideoExit:
		   	finish();
		   	break;
		}
	} 
	protected void onActivityResult (int requestCode, int resultCode, Intent data) { 
        if (resultCode == RESULT_OK && requestCode == VIDEO_CAPTURED) { 
                videoFileURI = data.getData(); 
        } 
	}
}