package com.movideo.moclient.moclient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by narendranathshanbhag on 6/04/2015.
 */
public class MediaFragment extends Fragment {


    // Lists, ArrayList and ArrayAdapter for capturing the list of results to be displayed
    ListView listOfVideos;

    ArrayList<String> videoArray;

    ArrayAdapter<String> videoAdapter;


    // The final token from the authentication url (Setters and getters below)
    static String token;

    public static String getToken() {
        return token;
    }

    public void setToken(String token) {
        MediaFragment.token = new String();
        MediaFragment.token = token;
    }

    // The authentication String containing  the Application Alias and the key
    final String auth_Url = "http://api.movideo.com/rest/session?applicationalias=flash_test_app&key=dev&output=json";


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_media, container, false);

        listOfVideos = (ListView)rootView.findViewById(R.id.listViewMainList);

        videoArray = new ArrayList<String>();
        videoAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, videoArray);
        //Setting the Adapter
        listOfVideos.setAdapter(videoAdapter);



        // Using an AsyncTask Class to get the MediaQuery up and running
        new MediaQuery().execute();



        return rootView;
    }


    class MediaQuery extends AsyncTask<Void, String, Void>
    {

        //The AsyncTask class

        // The request queue from the Google Volley library
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());


        @Override
        protected void onPreExecute() {


            // The Object request starts here
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, auth_Url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {

                    try
                    {
                        // Getting to the token
                        JSONObject tokenContent = jsonObject.getJSONObject("session");
                        String tokenText = tokenContent.getString("token");

                        // Capture and set the token
                        setToken(tokenText);

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    finally {   }


                    // Setting the media URL
                    String media_items_Url = "http://api.movideo.com/rest/media?token="+token+"&output=json";

                    // The Object request for the media list starts here
                    JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, media_items_Url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {

                            try
                            {
                                // Getting to the results
                                JSONArray listOfVideos = jsonObject.getJSONObject("pager").getJSONArray("list");
                                JSONObject media = listOfVideos.getJSONObject(0);
                                JSONArray videos = media.getJSONArray("media");

                                // Populating the array
                                int i;
                                for(i=0;i<videos.length();i++)
                                {
                                    videoArray.add(videos.getJSONObject(i).getString("title"));
                                }

                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();

                            }
                            finally {   }

                            // Refresh and notify the adapter of the changes
                            videoAdapter.notifyDataSetChanged();



                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(getActivity(), volleyError.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    requestQueue.add(jsonObjectRequest1);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getActivity(), volleyError.toString(), Toast.LENGTH_SHORT).show();
                }
            });

            requestQueue.add(jsonObjectRequest);

        }

        @Override
        protected Void doInBackground(Void... params) {

            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {

        }

        @Override
        protected void onPostExecute(Void aVoid) {


        }
    }
}
