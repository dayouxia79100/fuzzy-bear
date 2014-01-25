package com.example.app;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by dayouxia on 1/24/14.
 */
public class PollService extends IntentService {
    private static final  String TAG ="PollService";
    private static final int POLL_INTERVAL = 1000 * 60 * 5; //15 seconds
    public static final String PREF_IS_ALARM_ON = "isAlarmOn";

    public PollService(){
        super(TAG);
    }

    public static void setServiceAlar(Context context, boolean isOn){
        Intent i = new Intent(context,PollService.class);
        PendingIntent pi = PendingIntent.getService(context,0,i,0);

        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        if(isOn){
            alarmManager.setRepeating(AlarmManager.RTC,
                    System.currentTimeMillis(), POLL_INTERVAL,pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PollService.PREF_IS_ALARM_ON, isOn)
                .commit();

    }

    public static boolean isServiceAlarmOn(Context context){
        Intent i = new Intent(context,PollService.class);
        PendingIntent pi = PendingIntent.getService(
                context,0,i,PendingIntent.FLAG_NO_CREATE
        );
        return pi != null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        @SuppressWarnings("deprecation")
        boolean isNetworkAvailable = cm.getBackgroundDataSetting() &&
                cm.getActiveNetworkInfo() != null;
        if(!isNetworkAvailable) return;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String query = prefs.getString(FlickrFetchr.PREF_SEARCH_QUERY,null);
        String lastResultId = prefs.getString(FlickrFetchr.PREF_RESULT_ID,null);

        ArrayList<GalleryItem> items;
        if(query != null){
            items = new FlickrFetchr().search(query);
        } else {
            items = new FlickrFetchr().fetchItems();
        }

        if(items.size() == 0) return;

        String resultId = items.get(0).getId();

        if(!resultId.equals(lastResultId)){
            Log.i(TAG,"Got a new result: "+ resultId);

            Resources r = getResources();
            PendingIntent pi = PendingIntent
                    .getActivity(this,0,new Intent(this,PhotoGalleryFragment.class),0);

            Notification notification = new NotificationCompat.Builder(this)
                    .setTicker(r.getString(R.string.new_pictures_title))
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle(r.getString(R.string.new_pictures_title))
                    .setContentText(r.getString(R.string.new_pictures_text))
                    .setAutoCancel(true)
                    .build();

            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);

            notificationManager.notify(0,notification);



        } else{
            Log.i(TAG,"Got an old result: "+resultId);
        }

        prefs.edit()
                .putString(FlickrFetchr.PREF_RESULT_ID,resultId)
                .commit();



        Log.i(TAG,"Received an intent: "+intent);
    }
}
