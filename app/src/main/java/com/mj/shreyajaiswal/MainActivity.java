package com.mj.shreyajaiswal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;
    public  static  ArrayList<String> playername;
    public  static String captain;
    // URL to get contacts JSON
    private static String url = "https://test.oye.direct/players.json";

    ArrayList<String> contactList;
HashMap<String,HashMap<String,Boolean>> players;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactList = new ArrayList<>();
        players = new HashMap<>();
        playername = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listView);

        new GetContacts().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    //JSONObject jsonObject = new JSONObject(s);

                    Iterator x = jsonObj.keys();
                    while (x.hasNext()){
                        String key = (String) x.next();
                        contactList.add(key);
                        JSONArray js =jsonObj.getJSONArray(key);
                            HashMap<String, Boolean> hm = new HashMap<>();
                            for (int i = 0; i < js.length(); i++) {
                              JSONObject jp=  js.getJSONObject(i);
                              if(jp.has("captain")){
                                  hm.put(jp.getString("name"),jp.getBoolean("captain"));
                              }
                             else{
                                  hm.put(jp.getString("name"),false);
                              }

                             // Log.i("+++",jp.getString("name"));

                            }
                               players.put(key,hm);
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            Collections.sort(contactList);
            ArrayAdapter<String>adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,contactList);
           // listView.setAdapter(adapter);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    playername.clear();
                    String selectedItem = (String) parent.getItemAtPosition(position);
                       HashMap<String,Boolean> hm1 =players.get(selectedItem);

                    for (String i : hm1.keySet()) {


                       if(hm1.get(i)==true){
                           i= i+" - CAPTAIN";
                       }
                        playername.add(i);
                    }
                  //  Collections.sort(playername);
                    startActivity(new Intent(MainActivity.this,MainActivity2.class));

                }
            });
        }

    }
}

