package com.stevenpalomino.accountably;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.Parse;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Enable Local Datastore.

        Parse.enableLocalDatastore(getActivity());
        Parse.initialize(getActivity(), "Tp7eC66zJGYEoV2o2w5sf5uzuazYg1MgnKmxUx7z", "oWYycuBamJR6spbdmHab6Fe0QQtZThmnKcWwQBTp");



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }


}
