package com.stevenpalomino.accountably;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;


public class AddFragment extends Fragment {

    public EditText expenseName;
    public EditText expenseAmount;
    public Button addExpense;

    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add, container, false);

        expenseName = (EditText)v.findViewById(R.id.expenseName);
        expenseAmount = (EditText)v.findViewById(R.id.expenseAmount);
        addExpense = (Button)v.findViewById(R.id.addButton);

        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!expenseName.getText().toString().equals("")) && (!expenseAmount.getText().toString().equals(""))) {
                    ParseUser user = ParseUser.getCurrentUser();
                    ParseObject expense = new ParseObject("expense");
                    expense.put("expenseName", expenseName.getText().toString());
                    expense.put("expenseAmount", Float.valueOf(String.format("%.2f", Float.parseFloat(expenseAmount.getText().toString()))));
                    expense.put("user", user);
                    expense.saveInBackground();
                    Toast.makeText(getActivity(), "Expense added successfully", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }else{
                    Toast.makeText(getActivity(), "Expense name or amount cannot be blank", Toast.LENGTH_SHORT).show();
                }
            }
        });



        return v;
    }


}
