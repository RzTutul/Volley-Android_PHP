package com.rztechtunes.vollyphp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {
    TextView dataTV, dataTV2,dataSTV;
    String url = "http://192.168.0.108/VolleyCRUD/data.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataTV = findViewById(R.id.dataTV);
        dataTV2 = findViewById(R.id.data2TV);
        dataSTV = findViewById(R.id.dataSTV);
        setTitle("DashBoard");
        fetchData();

        fetchNewRequestQue();

        fethcDataBySingleTonPattern();
    }

    private void fethcDataBySingleTonPattern() {
        RequestQueue requestQueue  = MySingleton.getInstance(this).getRequestQueue();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataSTV.setText(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dataSTV.setText(error.getLocalizedMessage());
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    private void fetchData() {

        //By Default
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataTV.setText(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dataTV.setText(error.getLocalizedMessage());
            }
        });

        queue.add(stringRequest);

    }


    private void fetchNewRequestQue() {
        //This is working for when we use cache and http client
        RequestQueue mRequestQueue;

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); //1mb
        Network network = new BasicNetwork(new HurlStack());

        mRequestQueue = new RequestQueue(cache, network);

        mRequestQueue.start();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                dataTV2.setText("Request with caches  --" + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dataTV2.setText(error.getLocalizedMessage());
            }
        });

        mRequestQueue.add(stringRequest);
    }


}