package com.example.junzi.friendzone;

import android.app.Activity;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;


public class InscriptionActivity extends Activity implements View.OnClickListener{
    private EditText pseudo, phone, email, password;
    private Button signinBtn;
    private AwesomeValidation validation;
    //Expression régulière utiliser pour contrôler la validité des informations saisies
    /*private String regexPseudo = "^(?=.*[a-z])(?=.*[A-Z])(?=\\S+$)$";*/
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
        /*validation.addValidation(this, R.id.pseudo, regexPseudo, R.string.pseudoError);*/
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
        if(validation.validate()){
            Toast.makeText(this, "Inscription réussie", Toast.LENGTH_LONG).show();
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
}
