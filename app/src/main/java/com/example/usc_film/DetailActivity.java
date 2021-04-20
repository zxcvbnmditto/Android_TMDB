package com.example.usc_film;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.example.usc_film.detail.Cast;
import com.example.usc_film.detail.CastAdapter;
import com.example.usc_film.detail.DetailData;
import com.example.usc_film.detail.RecommendAdapter;
import com.example.usc_film.detail.Review;
import com.example.usc_film.detail.ReviewAdapter;
import com.example.usc_film.home.MediaData;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

enum Type {
    VIDEO(0),
    DETAIL(1),
    CAST(2),
    REVIEW(3),
    RECOMMEND(4);
    private final int value;
    private Type(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

public class DetailActivity extends AppCompatActivity {
    private String id;
    private String type;
    private DetailData detailData;
    private Boolean[] loaded = new Boolean[5];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");
            type = extras.getString("type");
            detailData = new DetailData(id, type);
        }
        setContentView(R.layout.activity_detail);

        final String baseUrl = "http://10.0.2.2:8080/";
        final String query = "?id=" + id + "&type=" + type;
        getVideoData(baseUrl + "Video" + query);
        getDetailData(baseUrl + "Detail" + query);
        getCastData(baseUrl + "Cast" + query);
        getReviewData(baseUrl + "Review" + query);
        getRecommendData(baseUrl + "Recommend" + query);
        setShareBtns();
    }

    private void setVisibility(Type type) {
        loaded[type.getValue()] = true;
        if (!Arrays.asList(loaded).contains(false)) {
            findViewById(R.id.detail_progressBar).setVisibility(View.INVISIBLE);
            findViewById(R.id.detail_loading_view).setVisibility(View.INVISIBLE);
            findViewById(R.id.detail_scroll).setVisibility(View.VISIBLE);
        }
    }

    private void getRecommendData(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            ArrayList<MediaData> mediaDataArrayList = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);

                                String id = obj.getString("id");
                                String title = obj.getString("title");
                                String path = obj.getString("path");
                                String type = obj.getString("type");

                                mediaDataArrayList.add(new MediaData(id, title, path, type));
                            }
                            setSliderAdapter(mediaDataArrayList);
                            setVisibility(Type.RECOMMEND);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }

    private void getReviewData(String url) {
        System.out.println("#####################################3");
        System.out.println(url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            ArrayList<Review> reviewArrayList = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);

                                String name = obj.getString("author");
                                String rating = obj.getString("rating");
                                String context = obj.getString("content");
                                String date = obj.getString("created_at");

                                reviewArrayList.add(new Review(name, rating, context, date));
                            }

                            detailData.setReviews(reviewArrayList);
                            setReviewView();
                            setVisibility(Type.REVIEW);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }

    private void getCastData(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            ArrayList<Cast> castArrayList = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);

                                String name = obj.getString("name");
                                String path = obj.getString("profile_path");
                                castArrayList.add(new Cast(path, name));
                            }

                            detailData.setCasts(castArrayList);
                            setCastView();
                            setVisibility(Type.CAST);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }

    private void getDetailData(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            detailData.setTitle(response.getString("title"));
                            detailData.setGenres(response.getString("genres"));
                            detailData.setOverview(response.getString("overview"));
                            detailData.setBackdrop_path(response.getString("path"));
                            detailData.setYear(response.getString("date"));
                            setDetailView();
                            setVisibility(Type.DETAIL);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {}
                });

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void getVideoData(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            detailData.setVideo_key(response.getString("key"));
                            setYTPlayer();
                            setVisibility(Type.VIDEO);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {}
                });

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void setSliderAdapter(ArrayList<MediaData> mediaDataArrayList) {
        RecommendAdapter adapter = new RecommendAdapter(mediaDataArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);

        RecyclerView view = findViewById(R.id.detail_recommend);
        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
        view.setAdapter(adapter);
    }

    private void setReviewView() {
        RecyclerView view = findViewById(R.id.detail_review);
        if (detailData.getReviews().size() > 0) {
            ReviewAdapter adapter = new ReviewAdapter(detailData.getReviews());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            view.setLayoutManager(layoutManager);
            view.setItemAnimator(new DefaultItemAnimator());
            view.setAdapter(adapter);
        } else {
            TextView title = findViewById(R.id.detail_review_title);
            title.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }
    }

    private void setCastView() {
        RecyclerView view = findViewById(R.id.detail_cast);
        if (detailData.getCasts().size() > 0) {
            CastAdapter adapter = new CastAdapter(detailData.getCasts());
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
            view.setLayoutManager(layoutManager);
            view.setItemAnimator(new DefaultItemAnimator());
            view.setAdapter(adapter);
        } else {
            TextView title = findViewById(R.id.detail_cast_title);
            title.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }
    }

    private void setDetailView() {
        TextView title =  findViewById(R.id.detail_title);
        title.setText(detailData.getTitle());
        TextView overview = findViewById(R.id.detail_overview);
        overview.setText(detailData.getOverview());
        TextView genres = findViewById(R.id.detail_genres);
        genres.setText(detailData.getGenres());
        TextView year = findViewById(R.id.detail_year);
        year.setText(detailData.getYear());

        ImageView imageView = findViewById(R.id.detail_backup_image);
        Glide.with(imageView)
                .load(detailData.getBackdrop_path())
                .centerCrop()
                .into(imageView);
    }

    private void setYTPlayer() {
        final YouTubePlayerView youTubePlayerView = findViewById(R.id.detail_youtube_player);
        if (!detailData.getVideo_key().equals("")) {
            getLifecycle().addObserver(youTubePlayerView);
            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    youTubePlayer.loadVideo(detailData.getVideo_key(), 0);
                }
            });
        }
        else {
            youTubePlayerView.setVisibility(View.GONE);
            ImageView imageView = findViewById(R.id.detail_backup_image);
            imageView.setVisibility(View.VISIBLE);
        }
    }

    private void setShareBtns() {
        TextView facebookShare = findViewById(R.id.detail_share_facebook);
        facebookShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.facebook.com/sharer/sharer.php?u=https://www.themoviedb.org/" + detailData.getType() + "/" + detailData.getId();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(browserIntent);
            }
        });
        TextView twitterShare = findViewById(R.id.detail_share_twitter);
        twitterShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://twitter.com/intent/tweet?text=" + Uri.encode("Check this out!\nhttps://www.themoviedb.org/") + detailData.getType() + "/" + detailData.getId();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(browserIntent);
            }
        });
    }
}
