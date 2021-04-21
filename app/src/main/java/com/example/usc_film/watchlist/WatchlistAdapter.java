package com.example.usc_film.watchlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.usc_film.DetailActivity;
import com.example.usc_film.R;

import java.util.ArrayList;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.MyViewHolder> {
    private ArrayList<WatchlistData> data;

    public WatchlistAdapter(ArrayList<WatchlistData> data) {
        this.data = data;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        ImageView imageView;
        TextView textView;
        TextView btn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recycler_watchlist_image);
            textView = itemView.findViewById(R.id.recycler_watchlist_type);
            btn = itemView.findViewById(R.id.recycler_watchlist_btn);
            this.itemView = itemView;
        }
    }

    @NonNull
    @Override
    public WatchlistAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_watchlist, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchlistAdapter.MyViewHolder holder, final int position) {
        final WatchlistData d = data.get(position);
        Glide.with(holder.itemView)
                .load(d.getImgUrl())
                .centerCrop()
                .into(holder.imageView);

        String mediaTypeText = (d.getType().equals( "movie")) ? "Movie" : "TV";
        holder.textView.setText(mediaTypeText);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                intent.putExtra("id", d.getId());
                intent.putExtra("type", d.getType());
                view.getContext().startActivity(intent);
            }
        });


        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Activity myActivity = (Activity) view.getContext();
                SharedPreferences prefs = myActivity.getSharedPreferences(view.getContext().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                String key = d.getId() + "-" + d.getType();
                if (prefs.contains("order")) {  // FIXME: If maybe redundant
                    String order = prefs.getString("order", "");
                    String new_order = order.replaceFirst("\\|" + key, "");
                    editor.putString("order", new_order);
                }
                editor.remove(key);
                editor.apply();

                SharedPreferences title_prefs = myActivity.getSharedPreferences(view.getContext().getString(R.string.preference_title_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor title_editor = title_prefs.edit();
                title_editor.remove(key);
                title_editor.apply();

                String toastMsg = d.getTitle() + " was removed from watchlist";
                Toast.makeText(view.getContext(), toastMsg, Toast.LENGTH_SHORT).show();

                data.remove(position);
                notifyItemChanged(position);
                notifyItemRangeRemoved(position, 1);
                notifyDataSetChanged();  // Not sure if it is the best
                if (getItemCount() == 0) {
                    TextView emptyText = myActivity.findViewById(R.id.watchlist_empty);
                    emptyText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
