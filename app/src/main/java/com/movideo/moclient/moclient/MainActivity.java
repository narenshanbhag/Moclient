package com.movideo.moclient.moclient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements Communicator{


    // The declarations for all the relevant fragments in the MainActivity
    MainFragment mainFragment;
    SearchFragment searchFragment;
    MediaFragment mediaFragment;

    // The searchKeyword which will be used as an argument to send to the
    String searchKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    public void init()
    {
        mainFragment = new MainFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.rlContainerActivity, mainFragment, "Main page");
        fragmentTransaction.commit();

    }

    public void setSearchFragment(String keyWord)
    {

        searchFragment = new SearchFragment();
        searchFragment.setSearchKeyWord(keyWord);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rlContainerActivity, searchFragment, "Search List");
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.commit();

    }

    public void setMediaFragment()
    {

        mediaFragment = new MediaFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rlContainerActivity, mediaFragment, "Media List");
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.commit();

    }

    public void setMainFragment()
    {
        mainFragment = new MainFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rlContainerActivity, mainFragment, "Main page");
        fragmentTransaction.commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            // Alert Dialog to get keyword for the search function
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Search Title");
            alert.setMessage("Enter keyword to search for.");

            final EditText inputSearchTerm = new EditText(this);
            alert.setView(inputSearchTerm);

            alert.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    searchKeyword = new String();

                    // The final search term
                    searchKeyword = inputSearchTerm.getText().toString().trim();

                    if(searchKeyword.length()==0)
                    {
                        // If the search keyword is submitted blank
                        Toast.makeText(getApplicationContext(), "Empty search keyword. \nWant to try again with a different one?",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // Using the keyword to search & calling the search function
                        Toast.makeText(getApplicationContext(), "Searching for "+searchKeyword+"..", Toast.LENGTH_SHORT).show();
                        setSearchFragment(searchKeyword);

                    }
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            alert.show();

            return true;
        }
        else if(id == R.id.action_about)
        {
            // Alert Dialog for the about function
            // Just a brief about the app and the developer
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("About MoClient");
            alert.setMessage("Version 1.0. \n\nApp built by Naren Shanbhag for the Movideo Android Test.");

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            alert.show();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void changeToMediaFragment() {
        setMediaFragment();

    }
}
