package com.example.junzi.friendzone;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.attr.data;

/**
 * Created by junzi on 02/02/2017.
 */

public class ListeAmisActivity extends AppCompatActivity {

	ListView listView;
	final ListeAmisActivity activiter = this ;
	private String JSON_STRING;
	private String id_of_user;

	Button partage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println(Config.id_user_co);
		System.out.println("ID USER CO ICI 1");
		setContentView(R.layout.exemple_liste_membre);
		listView = (ListView) findViewById(R.id.listViewplus);

		System.out.println(Config.id_user_co);
		System.out.println("ID USER CO ICI 2");
		getJSON();
	}


	private void showEmployee(){
		JSONObject jsonObject = null;
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> imgList  = new ArrayList<String>() ;
		ArrayList<String> listId  = new ArrayList<String>() ;
		ArrayList<String> part  = new ArrayList<String>() ;

		try {
			jsonObject = new JSONObject(JSON_STRING);
			JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

			for(int i = 0; i<result.length(); i++){
				JSONObject jo = result.getJSONObject(i);
				String id_user = Config.id_user_co;
				String id_ami = jo.getString(Config.TAG_ID_AMI);
				String name = jo.getString(Config.TAG_NAME_USER);

				HashMap<String,String> employees = new HashMap<>();
				employees.put(Config.TAG_ID_USER,id_user);
				employees.put(Config.TAG_ID_AMI,id_ami);
				employees.put(Config.TAG_NAME_USER,name);

				String  img = getImgFromContact(jo.getString("tel"));
				imgList.add(img);
				listId.add(jo.getString("id_ami"));
				part.add(jo.getString("par"));
				System.out.print(img+" "+name);
				list.add(name+ " (Mobile : "  + jo.getString("tel")+")");
			}



		} catch (JSONException e) {
			e.printStackTrace();
		}


		CustomAdapterListAmis adapter = new CustomAdapterListAmis(activiter, list, imgList,listId,part);
		listView = (ListView)findViewById(R.id.listViewplus);
		listView.setAdapter(adapter);

	}

	private void getJSON(){
		class GetJSON extends AsyncTask<Void,Void,String>{


			ProgressDialog loading;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				loading = ProgressDialog.show(ListeAmisActivity.this,"Veuillez patienter","Mise à jour...",false,false);
			}

			@Override
			protected void onPostExecute(String s) {
				super.onPostExecute(s);
				loading.dismiss();
				JSON_STRING = s;
				showEmployee();
			}

			@Override
			protected String doInBackground(Void... params) {
				RequestHandler rh = new RequestHandler();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Config.id_user_co);
				System.out.println("ID USER CO ICI");

				String url = Config.ip +"api.php/?fichier=users&action=amis_liste&values[id]="+Config.id_user_co;
				String s = rh.sendGetRequest(url);

				return s;
			}
		}
		GetJSON gj = new GetJSON();
		gj.execute();
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
							phoneNumber = phoneNumber.replaceAll("\\s", "");

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
}