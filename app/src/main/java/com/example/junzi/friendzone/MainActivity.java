package com.example.junzi.friendzone;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    Button signin;
    Button signup;
    Button friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        * Au clique sur le bouton Signin,
        * On passe sur l'activité d'inscription
        */
        signin = (Button) findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), signin.class);
                startActivity(myIntent);
            }
        });

        friend = (Button) findViewById(R.id.friend);
        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_PICK);
                myIntent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                startActivity(myIntent);
            }
        });

         /*
        * Au clique sur le bouton Connexion,
        * On passe sur l'activité de login
        */
        signin = (Button) findViewById(R.id.connexion);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(myIntent);
            }
        });

         /*
        * Au clique sur le bouton Map,
        * On passe sur l'activité map
        */
        signin = (Button) findViewById(R.id.map);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), MapsActivity.class);
                startActivity(myIntent);
            }
        });
    }
}