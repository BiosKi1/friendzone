package com.example.junzi.friendzone;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.junzi.friendzone.Config;
import com.example.junzi.friendzone.MapActivity;
import com.example.junzi.friendzone.R;
import com.example.junzi.friendzone.RequestHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends ActionBarActivity {

    private EditText editTextUserName;
    private EditText editTextPassword;

    private static final String PREF_USER_NAME = "PREF_USER_NAME";

    String username;
    String password;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUserName = (EditText) findViewById(R.id.pseudo);
        editTextPassword = (EditText) findViewById(R.id.password);

        /* Listener pour le bouton inscription */
        signup = (Button) findViewById(R.id.sign_up_button1);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), InscriptionActivity.class);
                startActivity(myIntent);
                finish();
            }
        });
    }

    public void invokeLogin(View view){
        username = editTextUserName.getText().toString();
        password = editTextPassword.getText().toString();

        login(username,password);

    }

    private void login(final String username, String password) {

        class LoginAsync extends AsyncTask<String, Void, String>{

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(LoginActivity.this, "Patientez", "Chargement...");
            }

            @Override
            protected String doInBackground(String... params) {
                String uname = params[0];
                String pass = params[1];

                String s = null;
                try {
                    RequestHandler rh = new RequestHandler();
                    String req = Config.url + "api.php/?" +
                            "fichier=users&action=connexion&values" +
                            "[pseudo]=" + uname + "&values[mdp]=" + pass;
                    s = rh.sendGetRequest(req);

                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
                        JSONObject c = result.getJSONObject(0);
                        Config.id_user_co = c.getString(Config.TAG_ID);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return s;
            }

            @Override
            protected void onPostExecute(String result){
                loadingDialog.dismiss();

                if(result.contains("ok")){
                    /*SaveSharedPreference.setUserName(LoginActivity.this, PREF_USER_NAME);*/
                    SaveSharedPreference.setUserId(LoginActivity.this, Config.id_user_co);

                    Intent intent = new Intent(LoginActivity.this, MapActivity.class);

                    finish();
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(), "Pseudo ou mot de passe incorrect", Toast.LENGTH_LONG).show();
                }
            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(username, password);
    }
}