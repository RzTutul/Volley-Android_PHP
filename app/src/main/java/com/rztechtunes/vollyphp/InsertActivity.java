package com.rztechtunes.vollyphp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class InsertActivity extends AppCompatActivity {

    private static final String TAG = InsertActivity.class.getSimpleName();
    ImageView selectImage;
    EditText nameET, passET;
    Button saveBtn, updateBtn,saveImgBtn;
    TextView userTV;
    String url = "https://techtunes999.000webhostapp.com/insert.php";
    String updateUrl = "https://techtunes999.000webhostapp.com/updateData.php";
    String imageUploadUrl = "https://techtunes999.000webhostapp.com/uploadImages.php";
    int position;
    String encodedImage;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        nameET = findViewById(R.id.nameET);
        passET = findViewById(R.id.passET);
        saveBtn = findViewById(R.id.saveBtn);
        userTV = findViewById(R.id.userTV);
        updateBtn = findViewById(R.id.updateBtn);
        selectImage = findViewById(R.id.imageViewBtn);
        saveImgBtn = findViewById(R.id.saveImageBtn);

        setTitle("SignUP");


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            position = bundle.getInt("id");
            updateBtn.setVisibility(View.VISIBLE);
            saveBtn.setVisibility(View.GONE);
            nameET.setText(UserInfoAdapterRV.list.get(position).getEmail());
            passET.setText(UserInfoAdapterRV.list.get(position).getPass());

        } else {
            updateBtn.setVisibility(View.GONE);
        }

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(InsertActivity.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
            }
        });


saveImgBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        uploadImage();
    }
});

updateBtn.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick (View view){
        String name = nameET.getText().toString();
        String pass = passET.getText().toString();
        String id = UserInfoAdapterRV.list.get(position).getId();
        Toast.makeText(InsertActivity.this, ""+id, Toast.LENGTH_SHORT).show();
        updateDtaa(id, name, pass);
    }
    });

        saveBtn.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View view){
        String name = nameET.getText().toString();
        String pass = passET.getText().toString();
        insertData(name, pass);
    }
    });


}

    private void uploadImage() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Uploading");
        dialog.show();
        Log.d(TAG, "uploadImage: "+encodedImage);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, imageUploadUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Toast.makeText(InsertActivity.this,response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(InsertActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("images",encodedImage );
                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data !=null)
        {
            Uri filePath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                 bitmap = BitmapFactory.decodeStream(inputStream);
                selectImage.setImageBitmap(bitmap);
                encodedImage = imageStore(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
    }

    private String imageStore(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] imageBytes = stream.toByteArray();
       return Base64.encodeToString(imageBytes,Base64.DEFAULT);
    }
    private void updateDtaa(final String id, final String name, final String pass) {
        RequestQueue requestQueue = MySingleton.getInstance(this).getRequestQueue();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("Updated")) {
                    Toast.makeText(InsertActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(InsertActivity.this, HomePageActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(InsertActivity.this, "Updated Fail", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InsertActivity.this, "" + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("email",name);
                params.put("pass", pass);
                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
    private void insertData(final String name, final String pass) {
        RequestQueue requestQueue = MySingleton.getInstance(this).getRequestQueue();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(InsertActivity.this, "" + response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InsertActivity.this, "" + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", name);
                params.put("pass", pass);
                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}