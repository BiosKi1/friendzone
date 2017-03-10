package com.example.junzi.friendzone;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by rhakanjin on 10/03/2017.
 */

public class MainActivity extends AppCompatActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if(SaveSharedPreference.getUserId(MainActivity.this).length() != 0)
		{
			Config.id_user_co = SaveSharedPreference.getUserId(MainActivity.this);


			// Stay at the current activity.
			 /*
			* Au clique sur le bouton Map,
			* On passe sur l'activité de la map
			*/
			Intent myIntent = new Intent(this, MapActivity.class);
			startActivity(myIntent);
			finish();
		}
		else
		{
			createNotification();
			// call Login Activity
			/*
			* Au clique sur le bouton Login,
			* On passe sur l'activité de connexion
			*/
			Intent myIntent = new Intent(this, LoginActivity.class);
			startActivity(myIntent);
			finish();
		}
	}

	private void createNotification(){
		final NotificationManager mNotification = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		Notification.Builder builder = new Notification.Builder(this)
				.setWhen(System.currentTimeMillis())
				.setSmallIcon(R.drawable.profil)
				.setContentTitle("Friend Zone")
				.setContentText("Welcome... Into the Friend Zone !");

		mNotification.notify(1, builder.build());
	}

}
