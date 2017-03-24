package com.example.junzi.friendzone;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.junzi.friendzone.R.drawable.location;

public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 0;
    private GoogleMap mMap;
    private String JSON_STRING;
    private String JSON_STRING_partage;
    private ArrayList<LatLng> locations = new ArrayList();
    private ArrayList<String> names = new ArrayList();
    private ArrayList<String> tel_friends = new ArrayList();
    private Button partager_lieu;
    private LocationManager locationManager;
    private Location location;
    private Double share_longi;
    private Double share_lat;
    private String libelle_lieu;
    private Boolean success = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getPositionCurrentUser();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        partager_lieu = (Button) findViewById(R.id.share_loc);
        partager_lieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareLocationOnMap();
            }
        });

        getJSON();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Profil) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            // Handle the camera action
        }
        else if (id == R.id.share_location) {
            Intent intent = new Intent(this, ShareLocationActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.liste_location) {
            Intent intent = new Intent(this, ListeLocationActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.listeContact) {
            Intent intent = new Intent(this, ListeAmisActivity.class);
            startActivity(intent);
			finish();
        }
        else if (id == R.id.addAmi) {
            Intent intent = new Intent(this, ListFriendActivity.class);
            startActivity(intent);
			finish();
        }
        else if (id == R.id.Deconnexion) {
            /*SharedPreferences preferences =getSharedPreferences("PREF_USER_ID",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            editor.apply();*/
            SaveSharedPreference.removepreferences(this);
            finish();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            Context context = getApplicationContext();
            CharSequence text = "Déconnexion réussie";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }
        }

        mMap.setMyLocationEnabled(true);


        String svcName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(svcName);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);

        String provider1 = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider1);

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        int i = 0;

		LatLng pos_user = new LatLng(location.getLatitude(), location.getLongitude());

        System.out.println(latitude);
        System.out.println(longitude);

        /* Afficher les utilisateurs ainsi que les numéros de tel, avec photo */
        for(LatLng locationz : locations){


            /*Gestion de la photo des contact sur la map*/
            Bitmap.Config conf = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = Bitmap.createBitmap(80, 80, conf);
            Bitmap marker = BitmapFactory.decodeResource(getResources(), R.drawable.imgdefault2);
            Bitmap newMarker = marker.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(newMarker);
            // Offset the drawing by 25x25
           /* canvas.drawBitmap(bitmap, 25, 25, null);*/

            // paint defines the text color, stroke width and size
            Paint color = new Paint();
            color.setTextSize(35);
            color.setColor(Color.BLACK);

            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
                    R.drawable.imgdefault2), 0,0, color);

            /*Ajout du markeur avec les paramètre sur la map*/
            mMap.addMarker(new MarkerOptions()
                    .position(locationz)
                    .title(names.get(i))
                    .snippet(""+tel_friends.get(i))
                    // Pour avoir les markers avec une couleur choisi random pour chaque friend
                    //.icon(BitmapDescriptorFactory.defaultMarker(new Random().nextInt(360)))

                    .icon(BitmapDescriptorFactory.fromBitmap(newMarker))


            );
            i++;

            /*Permet de mettre le trajet sur la map , geodesic(true) est supposé mettre
             l'itinéraire avec des virages mais il fait que une ligne droite*/
            Polyline line = mMap.addPolyline(new PolylineOptions()
                    .add(pos_user, locationz)
                    .width(5)
                    .color(Color.RED)
					.geodesic(true));

            /*Faire vibrer le téléphone si l'utilisateur a des amis sur la map*/
            if(locations.size() > 0)
            {
                Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(500);
            }
        }
   }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.print("grant");
                } else {
                    System.out.print("denied");
                }
                return;
            }
        }
    }

    private void getPositionCurrentUser(){
        class GetJSON extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MapActivity.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                setPosUser(s);
            }

            @Override
            public String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();

                String s = rh.sendGetRequest(Config.url+"api.php" +
                        "?fichier=users&action=user_position" +
                        "&values[id]="+Config.id_user_co);
                return s;
            }
        }

        GetJSON gj = new GetJSON();
        gj.execute();

    }

    private void setPosUser(String s){
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            /*Récupération du JSON et assignement des infos utilisateur et friends*/
            for(int i = 0; i<result.length(); i++){
                JSONObject c = result.getJSONObject(i);

                /*Ajoute les noms et prénoms des amis dans le arrayList*/
                names.add(c.getString(Config.TAG_NAME_AMI)+" "+c.getString(Config.TAG_PRENOM_AMI));

                /*Ajoute le tel des freinds dans le arrayList*/
                tel_friends.add(c.getString(Config.TAG_TELEPHONE));

                /*Ajouter la position des friends dans le arrayList*/
                locations.add(new LatLng(c.getDouble(Config.TAG_LAT_AMI), c.getDouble(Config.TAG_LONG_AMI)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendPartagePos(final String partage){
        class GetJSON extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MapActivity.this,"Envoie Position","...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING_partage = s;

            }

            @Override
            public String doInBackground(Void... params) {

                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.url+"api.php" +
                        "/?fichier=users&action=Update_Share_Pos" +
                        "&values[par]="+partage+"&values[id]=" +Config.id_user_co);

                return s;
            }
        }

        GetJSON gj = new GetJSON();
        gj.execute();

        finish();
        startActivity(getIntent());

    }

    private String showEmployee(){
        JSONObject jsonObject = null;
        String part = "1";

        try {
            jsonObject = new JSONObject(JSON_STRING_partage);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);



                if(jo.getString("par").equalsIgnoreCase("0")){

                    part = "0";
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return part;

    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String>{


            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MapActivity.this,"Veuillez patienter","Mise à jour...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING_partage = s;

                String rest = showEmployee();

                if( rest.equalsIgnoreCase("0")){
                    Button clickButton = (Button) findViewById(R.id.partage_Pos);
                    clickButton.setText("Position");
                    clickButton.setOnClickListener( new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            sendPartagePos("1");
                        }
                    });
                }else{
                    Button clickButton = (Button) findViewById(R.id.partage_Pos);
                    clickButton.setText("Cacher position");
                    clickButton.setOnClickListener( new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            sendPartagePos("0");
                        }
                    });
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();

                String url = Config.url +"api.php/?fichier=users&action=amis_liste&values[id]="+Config.id_user_co;
                String s = rh.sendGetRequest(url);

                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void shareLocationOnMap(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		final EditText et = new EditText(this);

		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(et);

		// set dialog message
		alertDialogBuilder.setMessage("Saisir le nom du lieu").setCancelable(true).setPositiveButton("PARTAGER", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
                if (!et.getText().toString().equals("")) {
                    System.out.println(et.getText().toString());
                    libelle_lieu = et.getText().toString();
                    shareLocationOnMapJson();
                }
                else{
                    Toast.makeText(MapActivity.this, "Saisir un nom pour le lieu", Toast.LENGTH_LONG).show();
                }
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
    }

    private void shareLocationOnMapJson(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MapActivity.this,"Chargement","Patientez...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;

                if (s.contains("ok")){
                    success = true;
                    Toast.makeText(MapActivity.this, "Ce lieu viens d'être partagé avec vos amis !", Toast.LENGTH_LONG).show();
                }
                else if (s.contains("error_insert_lieu")){
                    success = false;
                    Toast.makeText(MapActivity.this, "Erreur d'insertion table lieu", Toast.LENGTH_LONG).show();
                }
                else if (s.contains("error_insert_appartient")){
                    success = false;
                    Toast.makeText(MapActivity.this, "Erreur d'insertion table appartient", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(MapActivity.this, "Erreur de saisie, lieu inconnu!", Toast.LENGTH_LONG).show();
                    success = false;
                }


            }

            @Override
            protected String doInBackground(Void... params) {
                String locality="";

                if (ContextCompat.checkSelfPermission(MapActivity.this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(MapActivity.this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                    } else {

                        ActivityCompat.requestPermissions(MapActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
                    }
                }

                String svcName = Context.LOCATION_SERVICE;
                locationManager = (LocationManager) getSystemService(svcName);

                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setPowerRequirement(Criteria.POWER_LOW);

                criteria.setAltitudeRequired(false);
                criteria.setBearingRequired(false);
                criteria.setSpeedRequired(false);
                criteria.setCostAllowed(true);

                String provider1 = locationManager.getBestProvider(criteria, false);
                if (ActivityCompat.checkSelfPermission(MapActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return "";
                }
                Location location_s = locationManager.getLastKnownLocation(provider1);

                share_lat =location_s.getLatitude();
                share_longi = location_s.getLongitude();

                RequestHandler rh = new RequestHandler();
                Geocoder gc = new Geocoder(MapActivity.this);
                List<Address> list = null;

                try {
                    list = gc.getFromLocation(share_lat, share_longi, 1);
                    try{
                        Address add = list.get(0);
                        locality = add.getLocality();
                        System.out.println(locality);

                    }catch (Exception e) {
                        System.out.println("Problème adresse !");
                        e.printStackTrace();
                    }


                } catch (IOException e) {
                    System.out.println("Problème ajout en base !");
                    e.printStackTrace();
                }

                if (locality == null){
                    locality = "Adresse non disponible";
                    locality = locality.replaceAll(" ", "%20");
                }

                //Changer avec l'id de l'user co une fois que la vue est OK
                String url = Config.url+"api.php/?fichier=users&action=share_location" +
                        "&values[id_user]=" +Config.id_user_co+
                        "&values[libelle]="+libelle_lieu+
                        "&values[longi]="+share_longi+
                        "&values[lat]="+share_lat+
                        "&values[adresse]="+locality;

                String s = rh.sendGetRequest(url);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

}
