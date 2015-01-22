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

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.NumberFormat;


public class AddFragment extends Fragment {

    public EditText expenseName;
    public EditText expenseAmount;
    public Button addExpense;
    public Float mAmount;

    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add, container, false);

        expenseName = (EditText)v.findViewById(R.id.updateName);
        expenseAmount = (EditText)v.findViewById(R.id.updateAmount);
        addExpense = (Button)v.findViewById(R.id.addButton);
        final Spinner spinner = (Spinner)v.findViewById(R.id.prioritySpinner);

        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Connectivity conn = new Connectivity();
                //Check if connection is available
                if (conn.isOnline(getActivity())){
                    //check if name is not blank
                    if (validateName(expenseName.getText().toString())) {
                        //check if amount is valid
                        if (validateAmount(expenseAmount.getText().toString())) {
                            //check if priority is set
                            if ((validatePriority(spinner.getSelectedItemPosition()))) {
                                ParseUser user = ParseUser.getCurrentUser();
                                ParseObject expense = new ParseObject("Expense");
                                expense.put("expenseName", expenseName.getText().toString());
                                String amount = NumberFormat.getCurrencyInstance().format(Float.parseFloat(expenseAmount.getText().toString()));
                                Log.d("tag", "Number: " + amount);
                                expense.put("expenseAmount", Float.valueOf(String.format("%.2f", mAmount)));
                                expense.put("expensePriority", getNumberForPriority(spinner.getSelectedItem().toString()));
                                expense.put("user", user);
                                expense.saveInBackground();
                                Toast.makeText(getActivity(), "Expense added successfully", Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                            }else{
                                Toast.makeText(getActivity(), "Priority must be set", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            //Toast.makeText(getActivity(), "Expense amount cannot be blank", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Expense name cannot be blank", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "No network connection detected.", Toast.LENGTH_LONG).show();
                }
            }
        });

        return v;
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

















}
