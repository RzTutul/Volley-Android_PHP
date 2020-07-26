package com.rztechtunes.vollyphp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllImagesVIewActivity extends AppCompatActivity {
    RecyclerView imageRV;
    String url = "https://techtunes999.000webhostapp.com/retrieveImage.php";
    List<ImageDataPojo> imageDataPojoList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_images_v_iew);

        imageRV = findViewById(R.id.imageRV);

        RequestQueue requestQueue  = MySingleton.getInstance(this).getRequestQueue();
        
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Toast.makeText(AllImagesVIewActivity.this, ""+success, Toast.LENGTH_SHORT).show();

                    if (success.equals("1"))
                    {
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String id = object.getString("id");
                            String imageUrl = object.getString("images");
                            String url = "https://techtunes999.000webhostapp.com/Images/"+imageUrl;

                            ImageDataPojo imageDataPojo = new ImageDataPojo(id,url);

                            imageDataPojoList.add(imageDataPojo);

                        }
                        ImageAdapterRV imageAdapterRV = new ImageAdapterRV(AllImagesVIewActivity.this,imageDataPojoList);
                        LinearLayoutManager llm = new LinearLayoutManager(AllImagesVIewActivity.this);
                        imageRV.setLayoutManager(llm);
                        imageRV.setAdapter(imageAdapterRV);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AllImagesVIewActivity.this, ""+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
}