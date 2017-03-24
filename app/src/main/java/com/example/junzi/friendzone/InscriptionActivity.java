package com.example.junzi.friendzone;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class InscriptionActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText pseudo, phone, email, password;
    private Button signinBtn;
    private AwesomeValidation validation;
    private String JSON_STRING;
    private Boolean inscription = false, existMail = false, existPseudo = false;
    private String PseudoS, PhoneS, EmailS, PasswordS;

    //Expression régulière utiliser pour contrôler la validité des informations saisies
    private String regexPseudo="^[a-zA-Z0-9]+([ \\-'_]?[a-zA-Z0-9]+[ \\-'_]?[a-zA-Z0-9]+[ \\-'_]?)[a-zA-Z0-9]+$";
    private String regexPhone= "^[0-9]{2}[0-9]{8}$";
    private String regexPassword="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        validation = new AwesomeValidation(ValidationStyle.BASIC);
        //Récupération des informations
        pseudo = (EditText) findViewById(R.id.pseudo);
        phone = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.mail);
        password = (EditText) findViewById(R.id.password);
        signinBtn = (Button) findViewById(R.id.signinBtn);

        //Validation des champs à l'aide du Plugin Android
        validation.addValidation(this, R.id.pseudo, regexPseudo, R.string.pseudoError);
        validation.addValidation(this, R.id.phone, regexPhone, R.string.phoneError);
        validation.addValidation(this, R.id.mail, Patterns.EMAIL_ADDRESS, R.string.emailError);
        validation.addValidation(this, R.id.password, regexPassword, R.string.passwordError);
        signinBtn.setOnClickListener(this);
    }

    /*
    * Fonction d'envoie du formulaire
    * Affiche un message de confirmation quand les informations envoyés sont valides
    */
    private void submitForm(){
		PseudoS =  pseudo.getText().toString();
		PhoneS = phone.getText().toString();
		EmailS = email.getText().toString();
		PasswordS = password.getText().toString();

        /* Call l'api pour inscription */
        if(validation.validate()){
            getJSON();
        }
    }

    /*
    * Fonction d'envoie des informations au clique sur le bouton
    */
    public void onClick(View view){
        if(view == signinBtn){
            submitForm();
        }
    }

    public void redirectionForm(){
        if (existMail){
            Toast.makeText(this, "Cette adresse e-mail est déjà utlisée", Toast.LENGTH_LONG).show();
            inscription = false;
            existMail = false;
            existPseudo = false;
        }else if(inscription){
            Toast.makeText(this, "Inscription prise en compte", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(this, MapActivity.class);
            startActivity(myIntent);
        }
        else if(existPseudo) {
            Toast.makeText(this, "Ce pseudo est déjà utilisé", Toast.LENGTH_LONG).show();
            inscription = false;
            existMail = false;
            existPseudo = false;
        }
        else{
            Toast.makeText(this, "Informations valides", Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Echec de l'inscription, contact a admin", Toast.LENGTH_LONG).show();
        }
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(InscriptionActivity.this,"Fetching Data","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(JSON_STRING);

                    /*On recup' l'id insert*/
                    SaveSharedPreference.setUserId(InscriptionActivity.this, jsonObject.getString(Config.TAG_JSON_ARRAY));
                    Config.id_user_co = jsonObject.getString(Config.TAG_JSON_ARRAY);
                } catch (JSONException e) {
                    System.out.println("Erreur récupération id de la personne inscr.");
                    e.printStackTrace();
                }
                redirectionForm();

            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                /*String s = rh.sendGetRequest(Config.URL_CONNECT);*/

                String s = rh.sendGetRequest(Config.url+"api.php/" +
                        "?fichier=users&action=inscription" +
                        "&values[nom]=" +
                        "&values[prenom]=" +
                        "&values[mdp]=" + PasswordS +
                        "&values[tel]=" + PhoneS +
                        "&values[pseudo]=" + PseudoS +
                        "&values[mail]="+ EmailS);

				if(s.contains("error_mail")){
					existMail = true;
					inscription = false;
				}
                else if(s.contains("error_pseudo")){
                    existPseudo = true;
                    inscription = false;
                }
                else if(s.contains("error")){
                    inscription = false;
                }
                else if(s.isEmpty()){
                    inscription = false;
                }
                else{
                    inscription = true;
                }

                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();

    }
}
