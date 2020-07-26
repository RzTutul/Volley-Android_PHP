package com.rztechtunes.vollyphp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserInfoAdapterRV extends RecyclerView.Adapter<UserInfoAdapterRV.userViewHolder> {

    Context context;
   static List<UserInfoPojo> list;
    String url = "https://techtunes999.000webhostapp.com/deleteData.php";


    public UserInfoAdapterRV(Context context, List<UserInfoPojo> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public userViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_row_layout, parent, false);


        return new userViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull userViewHolder holder, final int position) {

        holder.email.setText(list.get(position).getEmail());
        holder.id.setText(list.get(position).getId());
        holder.pass.setText(list.get(position).getPass());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                CharSequence[] dialogItem = {"View Data", "Edit data", "Delete Data"};

                builder.setTitle(list.get(position).getEmail());

                builder.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                Intent intent1 = new Intent(context,AllImagesVIewActivity.class);
                                context.startActivity(intent1);
                                break;
                            case 1:
                                Intent intent = new Intent(context,InsertActivity.class);
                                intent.putExtra("id",position);
                                context.startActivity(intent);
                                break;
                            case 2:
                                deleteData(list.get(position).getId());
                                notifyDataSetChanged();
                                break;


                        }
                    }
                });
                builder.create().show();
            }
        });

    }

    private void deleteData(final String position) {
        Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("data deleted"))
                {
                    Toast.makeText(context, "Data Deleted", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(context, ""+response, Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                    params.put("u_id", String.valueOf(position));
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class userViewHolder extends RecyclerView.ViewHolder {
        TextView id, email, pass;

        public userViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.idTV);
            email = itemView.findViewById(R.id.emailTV);
            pass = itemView.findViewById(R.id.passwordTV);

        }
    }
}
