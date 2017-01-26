package com.example.junzi.friendzone;

import android.app.AlertDialog;
import android.app.ListActivity;


import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.provider.ContactsContract;

import java.io.IOException;
import java.io.InputStream;




public class listfriend extends ListActivity {

    private static final String TAG = listfriend.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;


    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        String contactName = null;

        // querying contact data store

        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.


            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String  name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

            Toast.makeText(this, "Contect LIST  =  "+name, Toast.LENGTH_LONG).show();

            cursor.moveToNext();

        }

        cursor.close();


        String[] values = new String[]{"Paul", "Jacque", "Pierre"};


        // use your custom layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.listfriend, R.id.firstLine, values);
        setListAdapter(adapter);
    }
}
