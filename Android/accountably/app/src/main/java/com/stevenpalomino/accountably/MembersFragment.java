package com.stevenpalomino.accountably;


import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MembersFragment extends ListFragment {

    public ListView expensesListview;
    public MembersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_members, container, false);

        expensesListview = (ListView)v.findViewById(android.R.id.list);


        String[] s1 = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu"};

        String[] s2 = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu"};


        mySimpleAdapter adapter = new mySimpleAdapter(getActivity(), s1, s2);
        expensesListview.setAdapter(adapter);
        setListAdapter(adapter);



        return v;
    }


    public class mySimpleAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final String[] values;
        private final String[] amounts;

        public mySimpleAdapter(Context context, String[] values, String[] amounts) {
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
            title.setText(values[position]);
            amount.setText(amounts[position]);



            return rowView;
        }
    }




}
