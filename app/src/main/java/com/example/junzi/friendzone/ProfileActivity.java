package com.example.junzi.friendzone;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import android.util.Patterns;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by Sindy on 31/01/2017.
 */

public class ProfileActivity extends AppCompatActivity{
    private String id, id_connexion;
    private EditText editTextNom, editTextPrenom, editTextPseudo, editTextEmail, editTextPhoneNumber;
    private String nom, prenom, pseudo, email, phoneNumber, JSON_STRING;
    private Button updateBtn;
    private Boolean success = false;
    private AwesomeValidation validation;
    private String regexName = "^[a-zA-Z]+([ \\-']?[a-zA-Z]+[ \\-']?[a-zA-Z]+[ \\-']?)[a-zA-Z]+$";
    private String regexPseudo = "^[a-zA-Z0-9]+([ \\-'_]?[a-zA-Z0-9]+[ \\-'_]?[a-zA-Z0-9]+[ \\-'_]?)[a-zA-Z0-9]+$";
    private String regexPhone= "^[0-9]{2}[0-9]{8}$";

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

        editTextNom.setTextColor(Color.parseColor("#000000"));
        editTextPrenom.setTextColor(Color.parseColor("#000000"));
        editTextPseudo.setTextColor(Color.parseColor("#000000"));
        editTextEmail.setTextColor(Color.parseColor("#000000"));
        editTextPhoneNumber.setTextColor(Color.parseColor("#000000"));

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

        /*View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }*/

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
                String url = Config.ip+
                        "api.php" +
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
                String url = Config.ip+
                        "api.php" +
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
            updateBtn.setText("Enregistrer");
        }else if(updateBtn.getText().equals("Enregistrer")){
            nom = editTextNom.getText().toString();
            prenom = editTextPrenom.getText().toString();
            pseudo = editTextPseudo.getText().toString();
            email = editTextEmail.getText().toString();
            phoneNumber = editTextPhoneNumber.getText().toString();

            validation = new AwesomeValidation(ValidationStyle.BASIC);
            validation.addValidation(this, R.id.nom, regexName, R.string.nameError);
            validation.addValidation(this, R.id.prenom, regexName, R.string.firstNameError);
            validation.addValidation(this, R.id.pseudo, regexPseudo, R.string.pseudoError);
            validation.addValidation(this, R.id.email, Patterns.EMAIL_ADDRESS, R.string.emailError);
            validation.addValidation(this, R.id.phone_number, regexPhone, R.string.phoneError);

            findViewById(R.id.updateBtn).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    validation.validate();
                }
            });

            if(validation.validate()){
                editTextNom.setText(nom);
                editTextPrenom.setText(prenom);
                editTextPseudo.setText(pseudo);
                editTextEmail.setText(email);
                editTextPhoneNumber.setText(phoneNumber);
                update();

                editTextNom.setEnabled(false);
                editTextPrenom.setEnabled(false);
                editTextPseudo.setEnabled(false);
                editTextPhoneNumber.setEnabled(false);
                editTextEmail.setEnabled(false);

                /*try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                if(success){
                    Toast.makeText(this, "Vos informations ont bien été mis à jour", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this, "Echec, vos informations n'ont pas pu être mis à jour, Contacter l'administrateur", Toast.LENGTH_LONG).show();
                }
                updateBtn.setText("Modifier");
            }
        }
    }
}
