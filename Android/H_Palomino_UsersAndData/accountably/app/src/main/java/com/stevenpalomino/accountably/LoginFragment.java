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
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


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
        mUsername = (EditText)v.findViewById(R.id.expenseName);
        mPassword = (EditText)v.findViewById(R.id.passwordField);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "username: " + mUsername.getText().toString());

            if ((!mUsername.getText().toString().equals("")) && (!mPassword.getText().toString().equals("")))
                {
                    ParseUser.logInInBackground(mUsername.getText().toString(), mPassword.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            if (parseUser != null) {
                                prefs.edit().putString("user", mUsername.getText().toString()).apply();
                                prefs.edit().putString("pass", mPassword.getText().toString()).apply();
                                prefs.edit().putBoolean("signedIn", true).apply();
                                getActivity().finish();
                                Log.d(TAG, "success");
                            }else{
                                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });
                }else{
                Toast.makeText(getActivity(), "Username or Password cannot be empty", Toast.LENGTH_SHORT).show();
            }
            }
        });


        return v;
    }


}
