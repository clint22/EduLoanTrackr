package com.example.appathon.eduloantracker.Views;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appathon.eduloantracker.R;
import com.example.appathon.eduloantracker.UserSessionManager;
import com.example.appathon.eduloantracker.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LoginActivity extends BaseActivity {


    @BindView(R.id.userNameLogin)
    EditText edttxt_username;
    @BindView(R.id.passwordLogin)
    EditText edttxt_password;
    @BindView(R.id.buttonLogin)
    Button btn_login;

    UserSessionManager session;
    private String username, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        session = new UserSessionManager(getApplicationContext());

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = Utils.getString(edttxt_username);
                password = Utils.getString(edttxt_password);

                // Validate if username, password is filled
                if (username.trim().length() > 0 && password.trim().length() > 0) {

                    // For testing puspose username, password is checked with static data
                    // username = admin
                    // password = admin

                    if (username.equals("admin") && password.equals("admin")) {

                        // Creating user login session
                        // Statically storing name="Android Example"
                        // and email="androidexample84@gmail.com"
                        session.createUserLoginSession(username,
                                password);

                        // Starting MainActivity
                        Intent i = new Intent(getApplicationContext(), LandingActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        // Add new Flag to start new Activity
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

                        finish();

                    } else {

                        // username / password doesn't match&
                        Toast.makeText(getApplicationContext(),
                                "Username/Password is incorrect",
                                Toast.LENGTH_LONG).show();

                    }
                } else {

                    // user didn't entered username or password
                    Toast.makeText(getApplicationContext(),
                            "Please enter username and password",
                            Toast.LENGTH_LONG).show();

                }

            }
        });


    }


}
