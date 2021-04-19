package com.example.usc_film.home;

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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.usc_film.MySingleton;
import com.example.usc_film.R;
import com.example.usc_film.home.carousel.SliderAdapter;
import com.example.usc_film.home.carousel.SliderData;
import com.example.usc_film.home.slider.MediaData;
import com.example.usc_film.home.slider.RecyclerAdapter;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


enum Type {
    CAROUSEL,
    TOP_RATED,
    POPULAR
}

public class HomeFragment extends Fragment {

    private static boolean isMovie = true;
    private ProgressBar progressBar;
    private TextView loadingView;
    private ScrollView scrollView;
    private SliderView carouselView;
    private RecyclerView topRatedView;
    private RecyclerView popularView;
    private Boolean loadCarousel;
    private Boolean loadTopRated;
    private Boolean loadPopular;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        progressBar = view.findViewById(R.id.home_progressBar);
        loadingView = view.findViewById(R.id.home_loading_view);
        scrollView = view.findViewById(R.id.home_scrollview);
        carouselView = view.findViewById(R.id.carousel_slider);
        topRatedView = view.findViewById(R.id.top_rated);
        popularView = view.findViewById(R.id.popular);
        loadCarousel = false;
        loadTopRated = false;
        loadPopular = false;
        loadData(view);

        // Button Listeners
        final Button mv_btn = view.findViewById(R.id.movies_btn);
        mv_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!isMovie) {
                    isMovie = !isMovie;
                    getFragmentManager().beginTransaction().replace(R.id.main_fragment_container, new HomeFragment()).commit();
                }
            }
        });

        final Button tv_btn = view.findViewById(R.id.tv_btn);
        tv_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (isMovie) {
                    isMovie = !isMovie;
                    getFragmentManager().beginTransaction().replace(R.id.main_fragment_container, new HomeFragment()).commit();
                }
            }
        });

        mv_btn.setTextColor(ContextCompat.getColorStateList(getActivity().getApplicationContext(), (isMovie) ? R.color.colorPrimary : R.color.colorSecondary));
        tv_btn.setTextColor(ContextCompat.getColorStateList(getActivity().getApplicationContext(), (isMovie) ? R.color.colorSecondary : R.color.colorPrimary));
        return view;
    }

    private void setSlider(ArrayList<SliderData> sliderDataArrayList) {
        // initializing the slider view.
//        SliderView sliderView = view.findViewById(R.id.carousel_slider);
        carouselView.stopAutoCycle();

        // passing this array list inside our adapter class.
        SliderAdapter adapter = new SliderAdapter(getActivity().getApplicationContext(), sliderDataArrayList);

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

    private void setRecyclerAdapter(RecyclerView view, ArrayList<MediaData> mediaDataArrayList) {
        RecyclerAdapter adapter = new RecyclerAdapter(mediaDataArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),
                                                                            LinearLayoutManager.HORIZONTAL, false);
        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
        view.setAdapter(adapter);
    }

    private void loadData(View view) {
        System.out.println("Load Data");
        getCarousel(Type.CAROUSEL);
        String topRatedUrl = (isMovie) ? "http://10.0.2.2:8080/TopRatedMovies" : "http://10.0.2.2:8080/TopRatedTvs";
        String popularUrl = (isMovie) ? "http://10.0.2.2:8080/PopularMovies" : "http://10.0.2.2:8080/PopularTvs";
        getTopRatedAndPopular(topRatedView, Type.TOP_RATED, topRatedUrl);
        getTopRatedAndPopular(popularView, Type.POPULAR, popularUrl);
    }

    private void getTopRatedAndPopular(final RecyclerView view, final Type type, String url) {
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
                                System.out.println(title);
                                System.out.println(path);

                                mediaDataArrayList.add(new MediaData(id, title, path));
                            }

                            setRecyclerAdapter(view, mediaDataArrayList);
                            changeVisibility(type);
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

    private void getCarousel(final Type type) {
        String url = (isMovie) ? "http://10.0.2.2:8080/NowPlayingMovie" : "http://10.0.2.2:8080/NowPlayingTv";
        // Request a string response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);

                                String id = obj.getString("id");
                                String title = obj.getString("title");
                                String path = obj.getString("path");

                                sliderDataArrayList.add(new SliderData(path));
                            }
                            setSlider(sliderDataArrayList);
                            changeVisibility(type);
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

        // Add the request to the RequestQueue.
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }

    private void changeVisibility(Type type) {
        switch (type) {
            case CAROUSEL:
                loadCarousel = true;
                break;
            case POPULAR:
                loadPopular = true;
                break;
            case TOP_RATED:
                loadTopRated = true;
                break;
            default:
                break;
        }

        if (loadCarousel && loadTopRated && loadPopular) {
            progressBar.setVisibility(View.INVISIBLE);
            loadingView.setVisibility(View.INVISIBLE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }
}
