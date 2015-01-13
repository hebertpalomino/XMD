package com.stevenpalomino.accountably;


import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class MembersFragment extends ListFragment {

    public ListView expensesListview;
    public TextView totalExp;
    static List<String>titlesList = new ArrayList<String>();
    static List<String>amountsList = new ArrayList<String>();
    public mySimpleAdapter adapter;
    public MembersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_members, container, false);
        totalExp = (TextView)v.findViewById(R.id.totalExp);

        expensesListview = (ListView)v.findViewById(android.R.id.list);

        getData();

        expensesListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int j, long l) {
                Log.d("TAG", "BOOOOM!");

                removeItem(j);

                return false;
            }
        });
        adapter = new mySimpleAdapter(getActivity(), titlesList, amountsList);
        expensesListview.setAdapter(adapter);
        setListAdapter(adapter);



        return v;
    }

    public void removeItem(int position){
        final int delPos = position;

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Are you sure?")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //delete from Parse
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("expense");
                        query.whereEqualTo("user", ParseUser.getCurrentUser());
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> scoreList, ParseException e) {
                                if (e == null) {
                                    for (int i = 0; i < scoreList.size(); i++){
                                        if (scoreList.get(i).getString("expenseName").equals(titlesList.get(delPos))){
                                            scoreList.get(i).deleteInBackground();
                                            getData();
                                        }
                                    }
                                } else {

                                }
                            }
                        });

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("tag", "onResume");
        getData();
    }

    public void getData(){
        if (ParseUser.getCurrentUser() != null){
            ParseQuery<ParseObject> query = ParseQuery.getQuery("expense");
            query.whereEqualTo("user", ParseUser.getCurrentUser());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    if (e == null){
                        Log.d("tag", "Objects: " + parseObjects);
                        Float totalAmount = 0.0f;
                        titlesList.clear();
                        amountsList.clear();
                        for (int i = 0; i < parseObjects.size(); i++ ){
                            Number amount = parseObjects.get(i).getNumber("expenseAmount");
                            String title = parseObjects.get(i).getString("expenseName");
                            titlesList.add(title);
                            amountsList.add(amount.toString());
                            totalAmount += amount.floatValue();
                            Log.d("TAG", "title: " + title);
                        }
                        totalExp.setText(String.format("$%.2f", totalAmount));
                        Log.d("TAG", "Amount: " + totalAmount);
                        adapter.notifyDataSetChanged();
                    }else{
                        e.printStackTrace();
                    }
                }
            });
        }
    }



    public class mySimpleAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final List<String> values;
        private final List<String> amounts;

        public mySimpleAdapter(Context context, List<String> values, List<String> amounts) {
            super(context, R.layout.row_layout, values);
            this.context = context;
            this.amounts = amounts;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.row_layout, parent, false);
            TextView title = (TextView) rowView.findViewById(R.id.expTitle);
            TextView amount = (TextView) rowView.findViewById(R.id.expAmount);
            title.setText(values.get(position));
            amount.setText(amounts.get(position));



            return rowView;
        }
    }




}
