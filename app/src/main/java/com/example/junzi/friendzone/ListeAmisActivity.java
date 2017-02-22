package com.example.junzi.friendzone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class ListeAmisActivity extends AppCompatActivity implements ListView.OnItemClickListener {

	private ListView listView;

	private String JSON_STRING;
	private String id_of_user;
	private String id_of_friend;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.exemple_liste_membre);
		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		getJSON();
	}


	private void showEmployee(){
		JSONObject jsonObject = null;
		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
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
				list.add(employees);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		ListAdapter adapter = new SimpleAdapter(
				ListeAmisActivity.this, list, R.layout.list_item,
				new String[]{Config.TAG_ID_USER,Config.TAG_ID_AMI ,Config.TAG_NAME_USER},
				new int[]{R.id.id_user, R.id.id_ami, R.id.name_user});

		listView.setAdapter(adapter);
	}

	private void getJSON(){
		class GetJSON extends AsyncTask<Void,Void,String>{


			ProgressDialog loading;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				loading = ProgressDialog.show(ListeAmisActivity.this,"Fetching Data","Wait...",false,false);
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
				String url = "http://"+Config.ip+"/projet/friendzoneapi" +
						"/api/api.php" +
						"/?fichier=users&action=amis_liste&values[id]="+id_of_user;
				String s = rh.sendGetRequest(url);

				System.out.println(s);
				return s;
			}
		}
		GetJSON gj = new GetJSON();
		gj.execute();
	}

	private void partager_position(){
		class partage_pos extends AsyncTask<Void,Void,String>{

			ProgressDialog loading;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				loading = ProgressDialog.show(ListeAmisActivity.this,"Updating...","Wait...",false,false);
			}

			@Override
			protected void onPostExecute(String s) {
				super.onPostExecute(s);
				loading.dismiss();
				Toast.makeText(ListeAmisActivity.this,s,Toast.LENGTH_LONG).show();
			}

			@Override
			protected String doInBackground(Void... params) {
				HashMap<String,String> hashMap = new HashMap<>();
				hashMap.put(Config.id_user_co,Config.id_user_co);
				hashMap.put(Config.KEY_EMP_ID_AMI,id_of_friend);

				RequestHandler rh = new RequestHandler();
				String url = "http://"+Config.ip+"/projet/friendzoneapi/api/api.php" +
						"/?fichier=users&action=partage_position" +
						"&values[id_user]=" +Config.id_user_co+
						"&values[id_ami]=" +id_of_friend+
						"&values[partage_pos]=1";
				String s = rh.sendPostRequest(url,hashMap);

				return s;
			}
		}
		partage_pos pp = new partage_pos();
		pp.execute();

		Toast.makeText(ListeAmisActivity.this, "Partage de la position activé",
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
		String empId = map.get(Config.TAG_ID_AMI).toString();
		String userId = map.get(Config.TAG_ID_USER).toString();

		id_of_user = userId;
		id_of_friend = empId;

		/* Passer la position en partager ou en ne plus partager */
		partager_position();

		/*Intent intent = new Intent(this, ListeAmisActivity.class);
		intent.putExtra(Config.EMP_ID,empId);
		startActivity(intent);*/
	}
}