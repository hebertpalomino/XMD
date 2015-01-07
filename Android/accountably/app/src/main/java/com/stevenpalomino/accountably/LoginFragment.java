package com.stevenpalomino.accountably;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class LoginFragment extends Fragment {

    public final String TAG = "~~~ spdebug";
    public Button mLoginButton;
    public EditText mUsername;
    public EditText mPassword;
    public SharedPreferences prefs;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        prefs = getActivity().getSharedPreferences("com.stevenpalomino.accountably", Context.MODE_PRIVATE);
        mLoginButton = (Button)v.findViewById(R.id.loginButton);
        mUsername = (EditText)v.findViewById(R.id.usernameField);
        mPassword = (EditText)v.findViewById(R.id.passwordField);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "username: " + mUsername.getText().toString());
                ParseUser user = new ParseUser();
                user.setUsername(mUsername.getText().toString());
                user.setPassword(mPassword.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            // Sign Up Successful, push main member's area
                            //Callback success to main view and push members area
                            prefs.edit().putBoolean("signedIn", true).apply();
                            getActivity().finish();
                            Log.d(TAG, "success");

                        }else{
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


        return v;
    }


}
