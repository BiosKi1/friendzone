package com.example.junzi.friendzone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by junzi on 02/02/2017.
 */

public class ListeAmisActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    private ListView listView;

    private String JSON_STRING;

	private Button partager_position;

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
                String id_user = jo.getString(Config.TAG_ID_USER);
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
                String url = "http://192.168.56.1/friendzoneapi" +
                        "/api/api.php" +
                        "/?fichier=users&action=amis_liste&values[id]=4";
                String s = rh.sendGetRequest(url);

                System.out.println(s);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		System.out.print("FSDGFDGFDGFD AAYYOOO");
		System.out.print("FSDGFDGFDGFD AAYYOOO");
		System.out.print("FSDGFDGFDGFD AAYYOOO");

		partager_position = (Button) findViewById(R.id.connexion);
		partager_position.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				/*Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
				startActivity(myIntent);*/
				System.out.print("FSDGFDGFDGFD AAYYOOO");
				System.out.print("FSDGFDGFDGFD AAYYOOO");
			}
		});
        Intent intent = new Intent(this, ListeAmisActivity.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String empId = map.get(Config.TAG_ID_USER).toString();
		System.out.println(empId);
        intent.putExtra(Config.EMP_ID,empId);
        startActivity(intent);
    }
}