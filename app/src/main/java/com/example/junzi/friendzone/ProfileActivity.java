package com.example.junzi.friendzone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sindy on 31/01/2017.
 */

public class ProfileActivity extends AppCompatActivity{
    private String id, id_connexion;
    private EditText editTextNom, editTextPrenom, editTextPseudo, editTextEmail, editTextPhoneNumber;
    private String nom, prenom, pseudo, email, phoneNumber, JSON_STRING;
    private Button updateBtn;
    private ImageView imageView;
    private Boolean success = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id_connexion = (String) getIntent().getSerializableExtra("value_user");
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();

        id = intent.getStringExtra(Config.KEY_EMP_ID);
        editTextNom = (EditText) findViewById(R.id.nom);
        editTextPrenom = (EditText) findViewById(R.id.prenom);
        editTextPseudo = (EditText) findViewById(R.id.pseudo);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPhoneNumber = (EditText) findViewById(R.id.phone_number);

        updateBtn = (Button) findViewById(R.id.updateBtn);

        getJSON();
    }

    private void showProfil(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String nom = c.getString(Config.TAG_NAME_USER);
            String prenom = c.getString(Config.TAG_FIRST_NAME_USER);
            String pseudo = c.getString(Config.TAG_PSEUDO);
            String email = c.getString(Config.TAG_MAIL);
            String phone_number = c.getString(Config.TAG_TELEPHONE);

            editTextNom.setText(nom);
            editTextPrenom.setText(prenom);
            editTextPseudo.setText(pseudo);
            editTextEmail.setText(email);
            editTextPhoneNumber.setText(phone_number);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ProfileActivity.this,"Fetching Data","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showProfil(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                //Changer avec l'id de l'user co une fois que la vue est OK
                String url = "http://"+Config.ip+"" +
                        "/projet/friendzoneapi/api/api.php" +
                        "/?fichier=users" +
                        "&action=user_profil" +
                        "&values[id]="+Config.id_user_co;
                String s = rh.sendGetRequest(url);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void update(){
        class GetJSON extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ProfileActivity.this,"Fetching Data","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showProfil(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                //Changer avec l'id de l'user co une fois que la vue est OK
                String url = "http://"+Config.ip+"" +
                        "/projet/friendzoneapi/api/api.php" +
                        "/?fichier=users" +
                        "&action=update_profil" +
                        "&values[id]="+Config.id_user_co+""+
                        "&values[nom]="+ nom +
                        "&values[prenom]="+ prenom +
                        "&values[tel]=" + phoneNumber +
                        "&values[pseudo]=" + pseudo +
                        "&values[mail]="+ email;
                String s = rh.sendGetRequest(url);
                if(s.contains("success_update")){
                    success = true;
                }
                System.out.println("heeeeeey");
                System.out.println(success);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    public void onClick(View view){
        if(updateBtn.getText().equals("Modifier")){
            editTextNom.setEnabled(true);
            editTextPrenom.setEnabled(true);
            editTextPseudo.setEnabled(true);
            editTextPhoneNumber.setEnabled(true);
            editTextEmail.setEnabled(true);
            //Faire appelle au json ici pour réafficher les données
            updateBtn.setText("Enregistrer");
        }else if(updateBtn.getText().equals("Enregistrer")){
            nom = editTextNom.getText().toString();
            prenom = editTextPrenom.getText().toString();
            pseudo = editTextPseudo.getText().toString();
            email = editTextEmail.getText().toString();
            phoneNumber = editTextPhoneNumber.getText().toString();

            editTextNom.setText(nom);
            editTextPrenom.setText(prenom);
            editTextPseudo.setText(pseudo);
            editTextEmail.setText(email);
            editTextPhoneNumber.setText(phoneNumber);

            update();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(success){
                Toast.makeText(this, "Vos informations ont bien été mis à jour", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Echec, vos informations n'ont pas pu être mis à jour, Contacter l'administrateur", Toast.LENGTH_LONG).show();
            }

            updateBtn.setText("trololo");
        }
    }

}
