package com.movideo.moclient.moclient;

/**
 * Created by narendranathshanbhag on 6/04/2015.
 */
public interface Communicator {

    //The Communicator interface which connects the MainActivity, the MainFragment and the MediaFragment


    // The function call from the MainFragment to invoke the MediaFragment from within the MainActivity
    public void changeToMediaFragment();
}
