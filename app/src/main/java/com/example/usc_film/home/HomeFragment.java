package com.example.usc_film.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.usc_film.MySingleton;
import com.example.usc_film.R;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


enum Type {
    MV_CAROUSEL(0),
    MV_TOP_RATED(1),
    MV_POPULAR(2),
    TV_CAROUSEL(3),
    TV_TOP_RATED(4),
    TV_POPULAR(5);
    private final int value;
    private Type(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

public class HomeFragment extends Fragment {

    private static boolean isMovie = true;
    private ProgressBar progressBar;
    private TextView loadingView;
    private ScrollView mvScrollView;
    private ScrollView tvScrollView;
    private Boolean[] loaded = new Boolean[6];
    private TextView mv_btn;
    private TextView tv_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        progressBar = view.findViewById(R.id.home_progressBar);
        loadingView = view.findViewById(R.id.home_loading_view);
        mvScrollView = view.findViewById(R.id.home_mv_scrollview);
        tvScrollView = view.findViewById(R.id.home_tv_scrollview);
        mv_btn = view.findViewById(R.id.movies_btn);
        tv_btn = view.findViewById(R.id.tv_btn);
        Arrays.fill(loaded, false);

        // Load Data from backend
        loadData();

        // Button Listeners
        mv_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!isMovie) {
                    isMovie = !isMovie;
                    setMainContentVisibility();
                }
                setTabBtnColor();
            }
        });

        tv_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (isMovie) {
                    isMovie = !isMovie;
                    setMainContentVisibility();
                }
                setTabBtnColor();
            }
        });

        // Footer Listener
        view.findViewById(R.id.footer_tmdb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.themoviedb.org/";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(browserIntent);
            }
        });

        setTabBtnColor();
        return view;
    }

    private void setCarouselAdapter(Type type, ArrayList<MediaData> sliderDataArrayList) {
        // initializing the slider view.
        SliderView carouselView;
        if (type == Type.MV_CAROUSEL)
            carouselView = mvScrollView.findViewById(R.id.carousel_slider);
        else
            carouselView = tvScrollView.findViewById(R.id.carousel_slider);
        carouselView.stopAutoCycle();

        // passing this array list inside our adapter class.
        CarouselAdapter adapter = new CarouselAdapter(getActivity().getApplicationContext(), sliderDataArrayList);

        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        carouselView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        // below method is used to
        // setadapter to sliderview.
        carouselView.setSliderAdapter(adapter);

        // below method is use to set
        // scroll time in seconds.
        carouselView.setScrollTimeInSec(3);

        // to set it scrollable automatically
        // we use below method.
        carouselView.setAutoCycle(true);

        // to start autocycle below method is used.
        carouselView.startAutoCycle();
    }

    private void setSliderAdapter(Type type, ArrayList<MediaData> mediaDataArrayList) {
        SliderAdapter adapter = new SliderAdapter(mediaDataArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),
                                                                            LinearLayoutManager.HORIZONTAL, false);

        RecyclerView view;
        switch (type) {
            case MV_TOP_RATED:
                view = mvScrollView.findViewById(R.id.top_rated);
                break;
            case MV_POPULAR:
                view = mvScrollView.findViewById(R.id.popular);
                break;
            case TV_TOP_RATED:
                view = tvScrollView.findViewById(R.id.top_rated);
                break;
            case TV_POPULAR:
                view = tvScrollView.findViewById(R.id.popular);
                break;
            default:
                view = mvScrollView.findViewById(R.id.top_rated);
                break;
        }

        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
        view.setAdapter(adapter);
    }

    private void loadData() {
        getDataFromBackend("http://10.0.2.2:8080/NowPlayingMovie", Type.MV_CAROUSEL);
        getDataFromBackend("http://10.0.2.2:8080/NowPlayingTv", Type.TV_CAROUSEL);
        getDataFromBackend("http://10.0.2.2:8080/TopRatedMovies", Type.MV_TOP_RATED);
        getDataFromBackend("http://10.0.2.2:8080/PopularMovies", Type.MV_POPULAR);
        getDataFromBackend("http://10.0.2.2:8080/TopRatedTvs", Type.TV_TOP_RATED);
        getDataFromBackend("http://10.0.2.2:8080/PopularTvs", Type.TV_POPULAR);
    }

    private void getDataFromBackend(String url, final Type type) {
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
                            if (type == Type.TV_CAROUSEL || type == Type.MV_CAROUSEL)
                                setCarouselAdapter(type, mediaDataArrayList);
                            else
                                setSliderAdapter(type, mediaDataArrayList);
                            changeLoadingVisibility(type);
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

        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }

    private void changeLoadingVisibility(Type type) {
        loaded[type.getValue()] = true;

        if (!Arrays.asList(loaded).contains(false)) {
            progressBar.setVisibility(View.INVISIBLE);
            loadingView.setVisibility(View.INVISIBLE);
            setMainContentVisibility();
        }
    }

    private void setMainContentVisibility() {
        if (isMovie) {
            mvScrollView.setVisibility(View.VISIBLE);
            tvScrollView.setVisibility(View.INVISIBLE);
        } else {
            mvScrollView.setVisibility(View.INVISIBLE);
            tvScrollView.setVisibility(View.VISIBLE);
        }
    }

    private void setTabBtnColor() {
        mv_btn.setTextColor(ContextCompat.getColorStateList(getActivity().getApplicationContext(), (isMovie) ? R.color.colorSecondary : R.color.colorPrimary));
        tv_btn.setTextColor(ContextCompat.getColorStateList(getActivity().getApplicationContext(), (isMovie) ? R.color.colorPrimary : R.color.colorSecondary));
    }
}
