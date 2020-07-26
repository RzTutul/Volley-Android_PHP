package com.rztechtunes.vollyphp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageAdapterRV extends RecyclerView.Adapter<ImageAdapterRV.ImageViewHolder> {
    Context context;
    List<ImageDataPojo> list;

    public ImageAdapterRV(Context context, List<ImageDataPojo> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.image_row_layout,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImages()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class  ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.row_images);
        }
    }
}
