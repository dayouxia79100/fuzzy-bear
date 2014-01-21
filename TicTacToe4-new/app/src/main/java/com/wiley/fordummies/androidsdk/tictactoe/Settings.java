package com.wiley.fordummies.androidsdk.tictactoe;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class Settings extends PreferenceActivity {

	private final static String OPT_NAME = "name";
	private final static String OPT_NAME_DEF = "Player";
	private final static String OPT_PLAY_FIRST = "human_starts";
	private final static boolean OPT_PLAY_FIRST_DEF = true;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Display the fragment as the main content.
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new SettingsFragment()).commit();
	}

	public static String getName(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(OPT_NAME, OPT_NAME_DEF);
	}

	public static boolean doesHumanPlayFirst(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_PLAY_FIRST, OPT_PLAY_FIRST_DEF);
	}

	/**
	 * This fragment shows the preferences for the first header.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class SettingsFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.settings);
		}
	}
}
