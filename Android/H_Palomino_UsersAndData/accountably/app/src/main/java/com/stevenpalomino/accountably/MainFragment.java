package com.stevenpalomino.accountably;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MainFragment extends Fragment {


    public Button loginBtn;
    public Button signupBtn;
    public SharedPreferences prefs;
    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        Boolean isSignedIn = prefs.getBoolean("signedIn", false);
        Log.d("boolean", "boolean: " + isSignedIn);
        if (isSignedIn == false){
            //do nothing
        }else{
            Intent membersActivity = new Intent(getActivity(), MembersActivity.class);
            getActivity().startActivity(membersActivity);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        prefs = getActivity().getSharedPreferences("com.stevenpalomino.accountably", Context.MODE_PRIVATE);

        View v = inflater.inflate(R.layout.fragment_main, container, false);
        // Enable Local Datastore.



        loginBtn = (Button)v.findViewById(R.id.loginButton);
        signupBtn = (Button)v.findViewById(R.id.SsignupButton);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(getActivity(), SignupActivity.class);
                getActivity().startActivity(signupIntent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(loginIntent);
            }
        });

        // Inflate the layout for this fragment
        return v;
    }


}
