package com.stevenpalomino.accountably;


import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MembersFragment extends ListFragment {

    public ListView expensesListview;
    public TextView totalExp;
    public List<Expense>objects = new ArrayList<Expense>();
    public List<Expense>tempObjects = new ArrayList<Expense>();
    public mySimpleAdapter adapter;
    final Handler handler = new Handler();
    public Float tempTotalFinal;

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



        Timer timer = new Timer();
        TimerTask runTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                        Log.d("tag", "HERE'S THE TASK");
                        getDataWithoutUpdatingUI();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timer.schedule(runTask, 5000, 5000); //execute every 20 seconds

        expensesListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int j, long l) {
                Log.d("TAG", "BOOOOM!");

                removeItem(j);

                return true;
            }
        });
        adapter = new mySimpleAdapter(getActivity(), objects);
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
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Expense");
                        query.whereEqualTo("user", ParseUser.getCurrentUser());
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> scoreList, ParseException e) {
                                if (e == null) {
                                    for (int i = 0; i < scoreList.size(); i++){
                                        if (scoreList.get(i).getString("expenseName").equals(objects.get(delPos).mName)){
                                            scoreList.get(i).deleteInBackground();
                                            break;
                                        }
                                    }
                                    getData();
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

    public void getData() {
        Connectivity conn = new Connectivity();
        if (conn.isOnline(getActivity())){
            if (ParseUser.getCurrentUser() != null) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Expense");
                query.whereEqualTo("user", ParseUser.getCurrentUser());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        if (e == null) {
                            Float totalAmount = 0.0f;
                            objects.clear();
                            for (int i = 0; i < parseObjects.size(); i++) {
                                Expense exp = new Expense();
                                Number amount = parseObjects.get(i).getNumber("expenseAmount");
                                exp.mName = parseObjects.get(i).getString("expenseName");
                                exp.mAmount = parseObjects.get(i).getNumber("expenseAmount");
                                exp.mPriority = parseObjects.get(i).getNumber("expensePriority");
                                exp.objectID = parseObjects.get(i).getObjectId();

                                objects.add(exp);
                                totalAmount += amount.floatValue();
                            }
                            totalExp.setText(String.format("$%.2f", totalAmount));
                            adapter.notifyDataSetChanged();
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }else{
            Toast.makeText(getActivity(), "No network connection detected.", Toast.LENGTH_LONG).show();

        }
    }


    public void getDataWithoutUpdatingUI() {
        Connectivity conn = new Connectivity();
        if (conn.isOnline(getActivity())){
            if (ParseUser.getCurrentUser() != null) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Expense");
                query.whereEqualTo("user", ParseUser.getCurrentUser());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        Log.d("tag", "Running");
                        if (e == null) {
                            tempTotalFinal = 0.0f;
                            tempObjects.clear();
                            for (int i = 0; i < parseObjects.size(); i++) {
                                Expense exp = new Expense();
                                Number amount = parseObjects.get(i).getNumber("expenseAmount");
                                exp.mName = parseObjects.get(i).getString("expenseName");
                                exp.mAmount = parseObjects.get(i).getNumber("expenseAmount");
                                exp.mPriority = parseObjects.get(i).getNumber("expensePriority");
                                exp.objectID = parseObjects.get(i).getObjectId();

                                tempObjects.add(exp);
                                tempTotalFinal += amount.floatValue();
                            }

                            Boolean isEqual = checkForEquality(objects, tempObjects);
                            if (!isEqual){
                                Log.d("tag", "IT'S EQUAL!!");
                                totalExp.setText(String.format("$%.2f", tempTotalFinal));
                                adapter = new mySimpleAdapter(getActivity(), tempObjects);
                                expensesListview.setAdapter(adapter);
                                setListAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                            //totalExp.setText(String.format("$%.2f", totalAmount));

                            //Boolean arraysEqual = (Arrays.equals(objects, tempObjects) ? true: false);
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }else{
            //Toast.makeText(getActivity(), "No network connection detected.", Toast.LENGTH_LONG).show();

        }
    }

    public Boolean checkForEquality(List<Expense> a, List<Expense> b){
        if (a.size() != b.size()){
            return false;
        }
        for (int i = 0; i < a.size(); i++){
            if (a.get(i).mName.equals(b.get(i).mName)){
                if (a.get(i).mAmount.equals(b.get(i).mAmount)){
                    if (a.get(i).mPriority.equals(b.get(i).mPriority)){
                        return true;
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }
        return true;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(getActivity(), UpdateActivity.class);
        intent.putExtra("currentObject", objects.get(position));
        startActivity(intent);
        Log.d("TAG", "Clicked ID: " + objects.get(position).objectID);
    }

    public class mySimpleAdapter extends ArrayAdapter<Expense> {
        private final Context context;
        private final List<Expense> objs;

        public mySimpleAdapter(Context context,  List<Expense> objs) {
            super(context, R.layout.row_layout, objs);
            this.context = context;
            this.objs = objs;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.row_layout, parent, false);
            TextView title = (TextView) rowView.findViewById(R.id.expTitle);
            TextView amount = (TextView) rowView.findViewById(R.id.expAmount);
            TextView priorityText = (TextView) rowView.findViewById(R.id.priorityText);
            title.setText(objs.get(position).mName);
            amount.setText(String.format("$%.2f",objs.get(position).mAmount.floatValue()));
            priorityText.setText(getPriority(objs.get(position).mPriority));



            return rowView;
        }

        public String getPriority (Number priority){
            String mPrio = "Priority: ";
            if (priority == 1){
                mPrio += "Low";
            }else if (priority == 2){
                mPrio += "Medium";
            }else if (priority == 3){
                mPrio += "High";
            }else{
                mPrio += "Unknown";
            }
            return mPrio;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_rotate){

            getData();
            return true;
        }


        return super.onOptionsItemSelected(item);



    }
}
