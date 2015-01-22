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

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignupFragment extends Fragment {

    public EditText mUsername;
    public EditText mPassword;
    public Button mSignup;
    public SharedPreferences prefs;
    public final String TAG = "~~~ Debug: ";

    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signup, container, false);

        prefs = getActivity().getSharedPreferences("com.stevenpalomino.accountably", Context.MODE_PRIVATE);

        mUsername = (EditText)v.findViewById(R.id.updateName);
        mPassword = (EditText)v.findViewById(R.id.SpasswordField);
        mSignup = (Button)v.findViewById(R.id.SsignupButton);



        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!mUsername.getText().toString().equals("")) && (!mPassword.getText().toString().equals(""))) {
                    ParseUser sUser = new ParseUser();
                    sUser.setUsername(mUsername.getText().toString());
                    sUser.setPassword(mPassword.getText().toString());
                    sUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                // Sign Up Successful, push main member's area
                                //Callback success to main view and push members area
                                prefs.edit().putString("user", mUsername.getText().toString()).apply();
                                prefs.edit().putString("pass", mPassword.getText().toString()).apply();
                                prefs.edit().putBoolean("signedIn", true).apply();
                                getActivity().finish();
                                Log.d(TAG, "success");

                            } else {
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


        // Inflate the layout for this fragment
        return v;
    }


}
