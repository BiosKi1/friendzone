package com.example.junzi.friendzone;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by rhakanjin on 16/03/2017.
 */
public class ListeLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

	private GoogleMap mMap;
	private String JSON_STRING;
	private ArrayList<LatLng> locations = new ArrayList();
	private ArrayList<String> names = new ArrayList();
	private ArrayList<String> address = new ArrayList();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getLocationOnMapJson();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_liste_location);

		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;

	}

	private void getLocationOnMapJson(){
		class GetJSON extends AsyncTask<Void,Void,String> {

			ProgressDialog loading;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				loading = ProgressDialog.show(ListeLocationActivity.this,"Chargement","Patientez...",false,false);
			}

			@Override
			protected void onPostExecute(String s) {
				super.onPostExecute(s);
				loading.dismiss();
				JSON_STRING = s;

				JSONObject jsonObject = null;

				try {
					jsonObject = new JSONObject(JSON_STRING);
					JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            	/*Récupération du JSON et assignement des infos utilisateur et friends*/
					for (int i = 0; i < result.length(); i++) {
						JSONObject c = result.getJSONObject(i);

                /*Ajoute les adresses dans le arrayList*/
						address.add(c.getString(Config.TAG_ADRESSE_LIEU));

				/*Ajoute les noms des lieux dans le arrayList*/
						names.add(c.getString(Config.TAG_ADRESSE_NOM));

                /*Ajouter la position des lieux dans le arrayList*/
						locations.add(new LatLng(c.getDouble(Config.TAG_LAT_LIEU), c.getDouble(Config.TAG_LONG_LIEU)));
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

				int i = 0;

				/* Afficher les utilisateurs ainsi que les numéros de tel, avec photo */
				for(LatLng locationz : locations) {
            		/*Ajout du markeur avec les paramètre sur la map*/
					mMap.addMarker(new MarkerOptions()
							.position(locationz)
							.title(names.get(i))
							.snippet(address.get(i))
							// Pour avoir les markers avec une couleur choisi random pour chaque friend
							.icon(BitmapDescriptorFactory.defaultMarker(new Random().nextInt(360)))
					);
					i++;
				}

			}

			@Override
			protected String doInBackground(Void... params) {
				RequestHandler rh = new RequestHandler();


				//Changer avec l'id de l'user co une fois que la vue est OK
				String url = Config.url+"api.php/?fichier=users" +
						"&action=liste_location" +
						"&values[id_user]=" +Config.id_user_co;

				String s = rh.sendGetRequest(url);
				return s;
			}
		}
		GetJSON gj = new GetJSON();
		gj.execute();
	}

}