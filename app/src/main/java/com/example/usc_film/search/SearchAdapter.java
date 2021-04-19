package com.example.usc_film.search;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.example.usc_film.DetailActivity;
import com.example.usc_film.R;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder>{
    private ArrayList<SearchData> data;

    public SearchAdapter(ArrayList<SearchData> data) {
        this.data = data;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        ImageView imageView;
        TextView titleView;
        TextView mediaTypeView;
        TextView ratingView;

        public MyViewHolder(final View view) {
            super(view);
            imageView = view.findViewById(R.id.recycler_search_image);
            titleView = view.findViewById(R.id.recycler_search_title);
            mediaTypeView = view.findViewById(R.id.recycler_search_mediatype);
            ratingView = view.findViewById(R.id.recycler_search_rating);
            this.itemView = view;
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_recycler, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final SearchData searchData = data.get(position);

        Glide.with(holder.itemView)
                .load(searchData.getImgUrl())
                .centerCrop()
                .into(holder.imageView);

        holder.titleView.setText(searchData.getTitle());
        String tmp = searchData.getType() + " (" +  searchData.getYear() + ")";
        holder.mediaTypeView.setText(tmp);
        holder.ratingView.append(searchData.getRating());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                intent.putExtra("id", searchData.getId());
                intent.putExtra("title", searchData.getTitle());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
