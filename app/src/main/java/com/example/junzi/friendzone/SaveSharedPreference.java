package com.example.junzi.friendzone;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by rhakanjin on 10/03/2017.
 */

public class SaveSharedPreference {

	static final String PREF_USER_NAME= "username";
	static final String PREF_USER_ID= "";
	//static final int ID_USER_CO = 0;

	static SharedPreferences getSharedPreferences(Context ctx) {
		return PreferenceManager.getDefaultSharedPreferences(ctx);
	}

	/* Partie username */
	public static void setUserName(Context ctx, String userName)
	{
		SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
		editor.putString(PREF_USER_NAME, userName);
		editor.commit();
		//editor.apply();
	}

	public static String getUserName(Context ctx)
	{
		return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
	}

	/* Partie user id co */
	public static void setUserId(Context ctx, String userId)
	{
		SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
		editor.putString(PREF_USER_ID, userId);
		editor.commit();
		//editor.apply();
	}

	public static String getUserId(Context ctx)
	{
		return getSharedPreferences(ctx).getString(PREF_USER_ID, "");
	}

	/* Déconnexion */
	public static void removepreferences(Context ctx) {
		SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
		System.out.println("123456789");
		editor.clear();
		editor.commit();

	}

}
