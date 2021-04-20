package com.example.usc_film.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.usc_film.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.MyViewHolder> {
    private ArrayList<Cast> data;

    public CastAdapter(ArrayList<Cast> data) {
        this.data = data;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private CircleImageView imageView;
        private TextView castName;

        public MyViewHolder(final View view) {
            super(view);
            imageView = view.findViewById(R.id.recycler_detail_cast);
            castName = view.findViewById(R.id.recycler_detail_cast_name);
            this.itemView = view;
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_cast, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Cast cast = data.get(position);
        holder.castName.setText(cast.getName());
        Glide.with(holder.itemView)
                .load(cast.getPath())
                .fitCenter()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
