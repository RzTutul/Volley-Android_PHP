package com.rztechtunes.vollyphp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends AppCompatActivity {

    EditText  emailET,passET;
    Button loginBtn;
    TextView signupBtn;
    String url = "https://techtunes999.000webhostapp.com/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_fragment);
        emailET = findViewById(R.id.emailET);
        passET = findViewById(R.id.passET);
        loginBtn = findViewById(R.id.login);
        signupBtn = findViewById(R.id.singup);
        setTitle("Login Page");

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailET.getText().toString();
                String pass = passET.getText().toString();

                loginWithSql(email,pass);
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginFragment.this,InsertActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginWithSql(final String email, final String pass) {
        Toast.makeText(this, ""+email+" "+pass, Toast.LENGTH_SHORT).show();
        RequestQueue requestQueue = MySingleton.getInstance(this).getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equalsIgnoreCase("login Successfully"))
                {
                    Toast.makeText(LoginFragment.this, ""+response, Toast.LENGTH_SHORT).show();
                    Intent intent  = new Intent(LoginFragment.this,HomePageActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(LoginFragment.this,""+ response, Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginFragment.this, ""+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()  {
                Map<String,String> params = new HashMap<>();
                params.put("email",email);
                params.put("pass",pass);
                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(request);

    }
}