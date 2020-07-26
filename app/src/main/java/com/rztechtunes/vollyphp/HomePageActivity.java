package com.rztechtunes.vollyphp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    List<UserInfoPojo> list = new ArrayList<>();
    UserInfoAdapterRV userInfoAdapterRV;
    RecyclerView userRV;

    String url = "https://techtunes999.000webhostapp.com/dataRetrive.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        userRV = findViewById(R.id.userRV);
        setTitle("DashBoard");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1"))
                    {
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject object =jsonArray.getJSONObject(i);
                            String id = object.getString("u_id");
                            String email = object.getString("email");
                            String pass = object.getString("pass");

                            UserInfoPojo userInfoPojo = new UserInfoPojo(id,email,pass);
                            list.add(userInfoPojo);

                        }

                        UserInfoAdapterRV userInfoAdapterRV = new UserInfoAdapterRV(HomePageActivity.this,list);
                        LinearLayoutManager llm = new LinearLayoutManager(HomePageActivity.this);
                        userRV.setLayoutManager(llm);
                        userRV.setAdapter(userInfoAdapterRV);

                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}