package com.stevenpalomino.accountably;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class UpdateFragment extends Fragment {


    public Float mAmount;
    public UpdateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_update, container, false);

        final Expense currentExpense = (Expense)getActivity().getIntent().getSerializableExtra("currentObject");

        Log.d("TAG", "update objectid: " + currentExpense.objectID);
        final EditText updateName = (EditText)v.findViewById(R.id.updateName);
        final EditText updateAmount = (EditText)v.findViewById(R.id.updateAmount);
        final Spinner spinner = (Spinner)v.findViewById(R.id.prioritySpinner);
        Button update = (Button)v.findViewById(R.id.updateButton);

        updateName.setText(currentExpense.mName);
        updateAmount.setText(String.format("%.2f", currentExpense.mAmount.floatValue()));
        spinner.setSelection(currentExpense.mPriority.intValue());
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Connectivity conn = new Connectivity();
                if (conn.isOnline(getActivity())) {
                    //validate name
                    if (validateName(updateName.getText().toString())) {
                        //validate amount
                        if (validateAmount(updateAmount.getText().toString())) {
                            //validate priority
                            if ((validatePriority(spinner.getSelectedItemPosition()))) {

                                ParseQuery<ParseObject> query = ParseQuery.getQuery("Expense");

                                query.getInBackground(currentExpense.objectID, new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject expense, ParseException e) {
                                        if (e == null) {
                                            ParseUser user = ParseUser.getCurrentUser();
                                            expense.put("expenseName", updateName.getText().toString());
                                            expense.put("expenseAmount", Float.valueOf(String.format("%.2f", mAmount)));
                                            expense.put("expensePriority", getNumberForPriority(spinner.getSelectedItem().toString()));
                                            expense.put("user", user);
                                            expense.saveInBackground();
                                            Toast.makeText(getActivity(), "Expense updated successfully", Toast.LENGTH_SHORT).show();
                                            getActivity().finish();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getActivity(), "No network connection detected.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }else{
                    Toast.makeText(getActivity(), "No network connection detected.", Toast.LENGTH_LONG).show();
                }
            }
        });




        return v;
    }


    public int getNumberForPriority(String stringPriority){
        int priority = 0;
        if (stringPriority.equals("1 - Low Priority")){
            priority = 1;
        }else if (stringPriority.equals("2 - Medium Priority")){
            priority = 2;
        }else if (stringPriority.equals("3 - High Priority")){
            priority = 3;
        }

        return priority;
    }


    public Boolean validateName (String name){
        if (name.equals("")){
            return false;
        }else{
            return true;
        }
    }


    public Boolean validatePriority(int position){
        //pattern validation
        if (position == 1 || position == 2 || position == 3){
            return true;
        }else{
            return false;
        }
    }


    public Boolean validateAmount(String amount){
        Log.d("tag", "AMOUNT: " + amount);
        int decimalTwoSpaces = amount.indexOf(".") + 3;
        if (amount.equals("")){
            Toast.makeText(getActivity(), "Expense amount cannot be blank", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (decimalTwoSpaces != -1){
            amount = amount.substring(0, decimalTwoSpaces);
            Log.d("tag", "INDEX: " + decimalTwoSpaces);
            Log.d("tag", "AMOUNT: " + amount);
            mAmount = Float.parseFloat(amount);
            Log.d("tag", "AMOUNT: " + mAmount);
        }
        if (mAmount > 0){
            return true;
        }else{
            Toast.makeText(getActivity(), "Amount must be positive", Toast.LENGTH_SHORT).show();
            return false;
        }
    }













}
