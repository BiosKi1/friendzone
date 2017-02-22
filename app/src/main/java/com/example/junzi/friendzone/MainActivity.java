package com.example.junzi.friendzone;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
                Intent myIntent = new Intent(view.getContext(), InscriptionActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        friend = (Button) findViewById(R.id.friend);
        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_PICK);
                myIntent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                startActivity(myIntent);
                finish();
            }
        });

         /*
        * Au clique sur le bouton Connexion,
        * On passe sur l'activité de login
        */
        signup = (Button) findViewById(R.id.connexion);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        friend = (Button) findViewById(R.id.friend);
        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), ListFriendActivity.class);
                startActivity(myIntent);
                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (2):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    }
                }
                break;
        }
    }
}