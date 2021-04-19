package com.example.usc_film.home.slider;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.usc_film.DetailActivity;
import com.example.usc_film.MainActivity;
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
        private TextView popup;

        public MyViewHolder(final View view) {
            super(view);
            image = view.findViewById(R.id.recycler_imageview);
            popup = view.findViewById(R.id.recycler_popup);
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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final MediaData media_data = data.get(position);
        Glide.with(holder.itemView)
                .load(media_data.getImgUrl())
                .centerCrop()
                .into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                intent.putExtra("id", media_data.getId());
                intent.putExtra("title", media_data.getTitle());
                view.getContext().startActivity(intent);
                // System.out.println("clicked");
                // System.out.println(media_data.getTitle());
            }
        });

        holder.popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), holder.popup);

                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String url = "";
                        Intent browserIntent; //  = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                        switch (menuItem.getItemId()) {
                            case R.id.open_tmdb_btn:
                                url = "https://www.themoviedb.org/" + media_data.getType() + "/" + media_data.getId();
                                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                view.getContext().startActivity(browserIntent);
                                break;
                            case R.id.share_facebook_btn:
                                url = "https://www.facebook.com/sharer/sharer.php?u=https://www.themoviedb.org/" + media_data.getType() + "/" + media_data.getId();
                                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                view.getContext().startActivity(browserIntent);
                                break;
                            case R.id.share_twitter_btn:
                                url = "https://twitter.com/intent/tweet?text=" + Uri.encode("Check this out!\nhttps://www.themoviedb.org/") + media_data.getType() + "/" + media_data.getId();
                                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                view.getContext().startActivity(browserIntent);
                                break;
                            default:
                                Toast.makeText(view.getContext(), media_data.getTitle() + " was added to Watchlist", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });

                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
