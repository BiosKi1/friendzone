package com.example.junzi.friendzone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import static com.example.junzi.friendzone.R.id.sign_in_button;
import static com.example.junzi.friendzone.R.id.signin;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView PseudoView;
    private EditText PasswordView;
    private View ProgressView;
    private View LoginFormView;
    private String Pseudo;
    private String Mdp;
    private String JSON_STRING;
    private Boolean identification = false;
	private Button connexion;
    private String id_user_co;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        PseudoView = (AutoCompleteTextView) findViewById(R.id.pseudo);

        PasswordView = (EditText) findViewById(R.id.password);
        PasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button SignInButton = (Button) findViewById(sign_in_button);
        SignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        LoginFormView = findViewById(R.id.login_form);
        ProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        PseudoView.setError(null);
        PasswordView.setError(null);

        // Store values at the time of the login attempt.
        String pseudo = PseudoView.getText().toString();
        String password = PasswordView.getText().toString();

        boolean cancel;
        View focusView = null;

        // Check for a valid password, if the user entered one.
       /* if (TextUtils.isEmpty(password)) {
            PasswordView.setError(getString(R.string.error_field_required));
            focusView = PasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            PasswordView.setError(getString(R.string.error_invalid_password));
            focusView = PasswordView;
            cancel = true;
        }

        // Check for a valid pseudo address.
        if (TextUtils.isEmpty(pseudo)) {
            PseudoView.setError(getString(R.string.error_field_required));
            focusView = PseudoView;
            cancel = true;
        } else if (!isPseudoValid(pseudo)) {
            PseudoView.setError(getString(R.string.error_invalid_pseudo));
            focusView = PseudoView;
            cancel = true;
        }*/

        if (isSessionValid(pseudo, password)) {
            cancel = false;
        }
        else {
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
                PasswordView.setError(getString(R.string.error_incorrect_session));
                PasswordView.requestFocus();
                /*focusView.requestFocus();*/
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            /*showProgress(true);*/
            mAuthTask = new UserLoginTask(pseudo, password);
            mAuthTask.execute((Void) null);
        }
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginActivity.this,"Fetching Data","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                /*String s = rh.sendGetRequest(Config.URL_CONNECT);*/
                String s = rh.sendGetRequest("http://192.168.150.1/projet/friendzoneapi/api/api.php/?" +
                        "fichier=users&action=connexion&values" +
                        "[pseudo]="+Pseudo+"&values[mdp]="+Mdp);

                if (s.contains("ok")){
                    id_user_co = s.substring(0, 1);
                    identification = true;
                }
                else if (s.contains("no_match")){
                    identification = false;
                }

                System.out.println(s);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private boolean isSessionValid(String pseudo, String password) {
        Pseudo = pseudo;
        Mdp = password;
        getJSON();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(identification);

        if (identification){
            return true;
        }
        return false;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mPseudo;
        private final String mPassword;

        UserLoginTask(String pseudo, String password) {
            mPseudo = pseudo;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mPseudo)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
				connexion = (Button) findViewById(sign_in_button);
				connexion.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent myIntent = new Intent(view.getContext(), ListeAmisActivity.class);
						myIntent.putExtra("value_user",id_user_co);
						startActivity(myIntent);

					}
				});
            } else {
                PasswordView.setError(getString(R.string.error_incorrect_password));
                PasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            /*showProgress(false);*/
        }
    }
}

