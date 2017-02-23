package com.example.junzi.friendzone;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.Manifest;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.junzi.friendzone.R.id.listView;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    /*private Map<Marker, Class> allMarkersMap = new HashMap<Marker, Class>;*/
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 0;
    private GoogleMap mMap;
    private double Long_user;
    private double Lat_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getPositionCurrentUser();
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }
        }

        mMap.setMyLocationEnabled(true);

        LocationManager locationManager;
        String svcName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(svcName);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);

        String provider1 = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider1);
        /*double lat = location.getLatitude();
        double lng = location.getLongitude();
        double alt = location.getAltitude();*/
/*
        System.out.println(lat);
        System.out.println(lng);*/

        LatLng position = new LatLng(Lat_user, Long_user);
        mMap.addMarker(new MarkerOptions().position(position).title("Votre dernière position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
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
                loading = ProgressDialog.show(MapsActivity.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                setPosUser(s);
            }

            @Override
            public String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();

                /*String s = rh.sendGetRequest(Config.URL_CONNECT);*/
                String s = rh.sendGetRequest("http://"+Config.ip+"/projet/friendzoneapi/api/api.php" +
                        "?fichier=users&action=user_position" +
                        "&values[id]="+Config.id_user_co);

                return s;
            }
        }

        GetJSON gj = new GetJSON();
        gj.execute();

    }

    private void setPosUser(String s){
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            Long_user = c.getDouble(Config.TAG_LONG_USER);
            Lat_user = c.getDouble(Config.TAG_LAT_USER);

            System.out.println(Long_user);
            System.out.println(Lat_user);
            System.out.println("GIJFDIGJDFIGJODIFJGOIDFJGIODFJGIJDFIOGJD 6564465354654436");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void listeContact(View view){
        Intent intent = new Intent(this, ListeAmisActivity.class);
        startActivity(intent);
    }

    public void addAmi(View view){
        Intent intent = new Intent(this, ListFriendActivity.class);
        startActivity(intent);
    }

    public void Profil(View view){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }


}
