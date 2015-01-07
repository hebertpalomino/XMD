package com.stevenpalomino.accountably;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseUser;


public class MembersActivity extends ActionBarActivity {

    public SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        prefs = this.getSharedPreferences("com.stevenpalomino.accountably", Context.MODE_PRIVATE);

        MembersFragment mFrag = new MembersFragment();
        getFragmentManager().beginTransaction().replace(R.id.membersContainer, mFrag).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_members, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            ParseUser.logOut();
            prefs.edit().putBoolean("signedIn", false).apply();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
