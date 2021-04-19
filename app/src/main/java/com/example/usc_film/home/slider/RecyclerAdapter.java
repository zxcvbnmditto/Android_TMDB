package com.example.usc_film.home.slider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.usc_film.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{
    private ArrayList<MediaData> data;

    public RecyclerAdapter(ArrayList<MediaData> data) {
        this.data = data;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private ImageView image;

        public MyViewHolder(final View view) {
            super(view);
            image = view.findViewById(R.id.recycler_imageview);
            this.itemView = view;
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_recycler, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MediaData media_data = data.get(position);
        Glide.with(holder.itemView)
                .load(media_data.getImgUrl())
                .centerCrop()
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
