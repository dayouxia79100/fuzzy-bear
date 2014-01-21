package com.wiley.fordummies.androidsdk.tictactoe;



import com.wiley.fordummies.androidsdk.tictactoe.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class Images extends Activity implements OnClickListener{
	public int flag=0;
	ImageView imageView=null;
	public static int IMAGE_CAPTURED = 1;
	static Uri imageFileURI=null;
	String imageFilePath="/mnt/sdcard/SampleImage.jpg";
	Bitmap imageBitmap=null;
	static final String TAGIMAGE="Image";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.images);
		imageView = (ImageView) findViewById(R.id.imageView);
		
		Button buttonShow = (Button) findViewById(R.id.buttonImageShow);
		buttonShow.setOnClickListener(this);
		Button buttonCapture = (Button) findViewById(R.id.buttonImageCapture);
		buttonCapture.setOnClickListener(this);
		Button buttonExit = (Button) findViewById(R.id.buttonImageExit);
		buttonExit.setOnClickListener(this);
		imageBitmap = BitmapFactory.decodeFile(imageFilePath);
	}
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.buttonImageShow:
			// Use BitmapFactory to create a bitmap
			imageView.setImageBitmap(imageBitmap);
			// imageBitmap=null;
			break;
		case R.id.buttonImageCapture:
			Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(cameraIntent, IMAGE_CAPTURED);
    	    break;
	    case R.id.buttonImageExit:
		   	finish();
		   	break;
		}
		
	}
	protected void onActivityResult (int requestCode, int resultCode, Intent cameraIntent) { 
        if (resultCode == RESULT_OK && requestCode == IMAGE_CAPTURED) {
        	Bundle extras = cameraIntent.getExtras() ; 
        	imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
			// imageBitmap=null;
        } 
	}
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		Log.d(TAGIMAGE, "Entering onPause");
		super.onPause();
		System.gc();
	}
}