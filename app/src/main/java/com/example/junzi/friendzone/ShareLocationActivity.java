package com.example.junzi.friendzone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

/**
 * Created by rhakanjin on 16/03/2017.
 */

public class ShareLocationActivity extends AppCompatActivity {

	//Initialisation of the EditText values
	private EditText editTextName;
	private EditText editTextAddress;
	private EditText editTextCity;
	private EditText editTextPostal;

	//Declaration of values
	private String libelle;
	private String address_place;
	private String city;
	private String postal;
	private Double longi_lieu;
	private Double lat_lieu;
	private String id_user = Config.id_user_co;
	private String address_fin;
	private String JSON_STRING;
	private Boolean success = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_location);

		//Attribution of the button with the EditText
		editTextName = (EditText) findViewById(R.id.libelle_lieu);
		editTextAddress = (EditText) findViewById(R.id.adresse_lieu);
		editTextCity = (EditText) findViewById(R.id.ville_lieu);
		editTextPostal = (EditText) findViewById(R.id.cp_lieu);
	}


	public void shareLocation(View view){
		shareLocationJson();
	}

	private void initilisation(){
		//Recuperation of values to String format
		libelle = editTextName.getText().toString();
		address_place = editTextAddress.getText().toString();
		city = editTextCity.getText().toString();
		postal = editTextPostal.getText().toString();

		/*Recuperation of the address and the coordinates
		 					according to the address enter by the user */
		Geocoder gc = new Geocoder(this);
		List<Address> list = null;
		try {
			address_fin = (address_place+", "+city+", "+postal);
			list = gc.getFromLocationName(address_fin, 1);

			//Replace the space by %20 so it can be insert in DB
			address_fin = address_fin.replaceAll(" ", "%20");

			try{
				Address add = list.get(0);
				String locality = add.getLocality();

				//Recuperation of the city latitude and longitude
				lat_lieu = add.getLatitude();
				longi_lieu = add.getLongitude();
				Toast.makeText(this, locality, Toast.LENGTH_LONG).show();
			}catch (Exception e) {
				System.out.println("Problème adresse !");
				e.printStackTrace();
			}


		} catch (IOException e) {
			System.out.println("Problème ajout en base !");
			e.printStackTrace();
		}
	}

	private void shareLocationJson(){
		class GetJSON extends AsyncTask<Void,Void,String> {

			ProgressDialog loading;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				initilisation();
				loading = ProgressDialog.show(ShareLocationActivity.this,"Chargement","Patientez...",false,false);

			}

			@Override
			protected void onPostExecute(String s) {
				super.onPostExecute(s);
				loading.dismiss();
				JSON_STRING = s;

				if (s.contains("ok")){
					success = true;
					Toast.makeText(ShareLocationActivity.this, "Ce lieu viens d'être partagé avec vos amis !", Toast.LENGTH_LONG).show();
				}
				else if (s.contains("error_insert_lieu")){
					success = false;
					Toast.makeText(ShareLocationActivity.this, "Erreur d'insertion table lieu", Toast.LENGTH_LONG).show();
				}
				else if (s.contains("error_insert_appartient")){
					success = false;
					Toast.makeText(ShareLocationActivity.this, "Erreur d'insertion table appartient", Toast.LENGTH_LONG).show();
				}
				else {
					Toast.makeText(ShareLocationActivity.this, "Erreur de saisie, lieu inconnu!", Toast.LENGTH_LONG).show();
					success = false;
				}


			}

			@Override
			protected String doInBackground(Void... params) {
				RequestHandler rh = new RequestHandler();
				//Changer avec l'id de l'user co une fois que la vue est OK
				String url = Config.url+"api.php/?fichier=users&action=share_location" +
						"&values[id_user]=" +id_user+
						"&values[libelle]=" +libelle+
						"&values[adresse]="+address_fin+
						"&values[longi]="+longi_lieu+
						"&values[lat]="+lat_lieu;

				String s = rh.sendGetRequest(url);
				return s;
			}
		}
		GetJSON gj = new GetJSON();
		gj.execute();
	}
}
