
package com.example.junzi.friendzone;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by francis on 17/02/2017.
 */

public class ListFriendActivity extends AppCompatActivity {

    ListView mListView;
    private String JSON_STRING;
    private boolean addAmis;
    public String res;
    final Context context = this;
    final ArrayList<String> idContact = new ArrayList<String>() ;
    final ArrayList<String> imgList = new ArrayList<String>() ;
    final ListFriendActivity activiter = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_friend);

        mListView = (ListView) findViewById(R.id.listViewFriend);

        //android.R.layout.simple_list_item_1 est une vue disponible de base dans le SDK android,
        //Contenant une TextView avec comme identifiant "@android:id/text1"

        ArrayList<String>  nb = fetchContactsNb();



        if (!nb.isEmpty()){
            getJSON(nb);
        }
        else{
            Toast.makeText(getApplicationContext(), "Aucun ami disponible", Toast.LENGTH_LONG).show();
        }
        if (!nb.isEmpty()) {
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                    Object o = mListView.getItemAtPosition(position);
                    String str = (String) o;
                    String mydata = str;
                    Pattern pattern = Pattern.compile("(.*?)\\(");
                    Matcher matcher = pattern.matcher(mydata);
                    if (matcher.find()) {
                        str = matcher.group(1);
                    }
                    final int nb_id = Integer.parseInt(idContact.get(position));

                    // set title
                    alertDialogBuilder.setTitle("Retrouver des amis ");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Ajouter " + str + "en amis ?")
                            .setCancelable(false)
                            .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    getJSONAdd(nb_id);

                                }
                            })
                            .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }
            });
        }
    }



    private void getJSON(final ArrayList<String> nb){
        class GetJSON extends AsyncTask<Void,Void,String> {


            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ListFriendActivity.this,"Récupération","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;

                ArrayList<String> arr = new ArrayList<String>() ;

                try {
                    JSONObject jsnobject = new JSONObject(JSON_STRING);
                    JSONArray jsonArray = jsnobject.getJSONArray("result");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsnobject2 = jsonArray.getJSONObject(i);
                        String user = jsnobject2.getString("nom_user") + " (Mobile : "  + jsnobject2.getString("tel")+")";
                        arr.add(user);
                        String  img = getImgFromContact(jsnobject2.getString("tel"));

                        imgList.add(img);
                        idContact.add(jsnobject2.getString("id_user"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListFriendActivity.this,
                        android.R.layout.simple_list_item_1, arr);*/
                CustomAdapterListFriend adapter =new CustomAdapterListFriend(activiter, arr, imgList);
                mListView =(ListView)findViewById(R.id.listViewFriend);
                mListView.setAdapter(adapter);

            }

            @Override
            public String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();

                String requete = "values[array][]="+nb.get(0);

                for (int i = 1; i < nb.size(); i++)
                {
                    requete = requete+"&values[array][]="+nb.get(i);

                }

                String s = rh.sendGetRequest(Config.url+"api.php/?" +
                        "fichier=users&action=non_friend&"+requete+"&values[id_user]="+Config.id_user_co);

                return s;
            }
        }

        GetJSON gj = new GetJSON();
        gj.execute();

    }


    private void getJSONAdd(final int nb){

        class GetJSONAdd extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ListFriendActivity.this,"Ajout de l'ami","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;

                if(s.contains("ok")){


                    AlertDialog alertDialog = new AlertDialog.Builder(ListFriendActivity.this).create();
                    alertDialog.setTitle("Vous avez ajouté un ami.");

                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    startActivity(getIntent());
                                }
                            });
                    alertDialog.show();

                } else{

                    AlertDialog alertDialog = new AlertDialog.Builder(ListFriendActivity.this).create();
                    alertDialog.setTitle("Une erreure est survenue.");

                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }



            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                /*String s = rh.sendGetRequest(Config.URL_CONNECT);*/

                String s = rh.sendGetRequest(Config.url+"api.php/?fichier=users&action=add_friend&values[id_amis]="+nb+"&values[id_co]="+Config.id_user_co);



                return s;
            }
        }
        GetJSONAdd gj = new GetJSONAdd();
        gj.execute();
    }


    public ArrayList<String> fetchContactsNames() {

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;

        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;

        ArrayList<String> output = new ArrayList<String>();

        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);

        // Loop for every contact in the phone
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));
                if (name != "") {
                    output.add(name);
                }else{
                    output.add(" ");
                }
            }
        }

        return output;

    }

    public  ArrayList<String>  fetchContactsNb() {

        String phoneNumber = null;
        String email = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String PRENOM = ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        ArrayList<String> output = new ArrayList<String>();

        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);

        // Loop for every contact in the phone
        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));

                if (hasPhoneNumber > 0) {


                    // Query and loop for every phone number of the contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);

                    while (phoneCursor.moveToNext()) {
                        int phoneType = phoneCursor.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        String photo = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));
                        String name = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));




                        if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                        {
                            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
                            phoneNumber = phoneNumber.replaceAll(" ", "");
                            output.add(phoneNumber);
                        }


                    }

                    phoneCursor.close();

                }


            }

        }

        return output;

    }

    public String getImgFromContact(String tel){
        String imgString = "";
        String phoneNumber = "";
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;

        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);

        // Loop for every contact in the phone
        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));

                if (hasPhoneNumber > 0) {


                    // Query and loop for every phone number of the contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);

                    while (phoneCursor.moveToNext()) {
                        int phoneType = phoneCursor.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        String photo = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));
                        String name = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));




                        if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                        {
                            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
                            phoneNumber = phoneNumber.replaceAll(" ", "");

                            if( tel.equals(phoneNumber.toString()) ){
                                return photo;
                            }else {
                                Uri path = Uri.parse("android.resource://com.example.junzi.friendzone/" + R.drawable.imgdefault);
                                imgString = path.toString();
                            }

                        }


                    }

                    phoneCursor.close();

                }


            }

        }

        return imgString;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
            finish();
        }

        return true;
    }
}