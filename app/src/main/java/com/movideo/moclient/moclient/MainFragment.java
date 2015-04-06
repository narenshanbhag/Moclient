package com.movideo.moclient.moclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by narendranathshanbhag on 6/04/2015.
 */
public class MainFragment extends Fragment {

    // The Mainfragment which appears when the app starts

    Button buttonJGetMedia;

    // Implementation of the Inter-fragment communication design pattern
    // An instance of the Communicator interface is instanciated
    Communicator c;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // The communicator is initialized
        c = (Communicator) getActivity();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        buttonJGetMedia = (Button)rootView.findViewById(R.id.buttonGetMedia);

        buttonJGetMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Setting the MediaFragment
                c.changeToMediaFragment();
            }
        });




        return rootView;
    }


}
