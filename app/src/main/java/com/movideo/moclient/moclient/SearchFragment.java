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
public class SearchFragment extends Fragment {

    // Lists, ArrayList and ArrayAdapter for capturing the list of results to be displayed
    ListView listOfVideos;

    ArrayList<String> videoArray;

    ArrayAdapter<String> videoAdapter;

    // The final token from the authentication url (Setters and getters below)
    static String token;

    public String getSearchKeyWord() {
        return searchKeyWord;
    }

    public void setSearchKeyWord(String searchKeyWord) {
        this.searchKeyWord = new String();
        this.searchKeyWord = searchKeyWord;
    }

    // The Search keyword
    String searchKeyWord;


    public static String getToken() {
        return token;
    }

    public void setToken(String token) {
        SearchFragment.token = new String();
        SearchFragment.token = token;
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


        // Using an AsyncTask Class to get the Media Search Query up and running
        new MediaSearch().execute();



        return rootView;
    }


    class MediaSearch extends AsyncTask<Void, String, Void>
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
                    String media_search_Url = "http://api.movideo.com/rest/media/search?token="+token+"&keywords=['"+getSearchKeyWord()+"']&output=json";

                    // The Object request for the media list starts here
                        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, media_search_Url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {

                                try {

                                    // Getting to the results
                                    JSONObject pager = jsonObject.getJSONObject("pager");
                                    JSONArray listOfVideos = pager.getJSONArray("list");

                                    int totalItems = pager.getInt("totalItems");

                                    JSONObject media = listOfVideos.getJSONObject(0);

                                    // If there is just one result for the search query
                                    if (totalItems == 1) {

                                        JSONObject video = media.getJSONObject("media");

                                        // Populating the array
                                        videoArray.add(video.getString("title"));

                                    } else if (totalItems > 1) { // If there is more than one result for the search query

                                        JSONArray videos = media.getJSONArray("media");

                                        // Populating the array
                                        int i;
                                        int size = videos.length();

                                        for (i = 0; i < size; i++) {
                                            videoArray.add(videos.getJSONObject(i).getString("title"));
                                        }


                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(), "Sorry! Can't find any media with this search keyword. \nWant to try again with a different one?",
                                            Toast.LENGTH_LONG).show();

                                } finally {
                                }

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
