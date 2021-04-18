package com.example.usc_film.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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


public class HomeFragment extends Fragment {

    private static boolean isMovie = true;
    private SliderView carouselView;
    private RecyclerView topRatedView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        carouselView = view.findViewById(R.id.carousel_slider);
        topRatedView = view.findViewById(R.id.top_rated);
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

    private void setSliderExample(ArrayList<SliderData> sliderDataArrayList) {
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

    private void setRecyclerAdapter(ArrayList<MediaData> mediaDataArrayList) {
        RecyclerAdapter adapter = new RecyclerAdapter(mediaDataArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),
                                                                            LinearLayoutManager.HORIZONTAL, false);
        topRatedView.setLayoutManager(layoutManager);
        topRatedView.setItemAnimator(new DefaultItemAnimator());
        topRatedView.setAdapter(adapter);
    }

    private void loadData(View view) {
        System.out.println("Load Data");
        getCarousel(view);
        String topRatedUrl = (isMovie) ? "http://10.0.2.2:8080/TopRatedMovies" : "http://10.0.2.2:8080/TopRatedTvs";
        String popularUrl = (isMovie) ? "http://10.0.2.2:8080/PopularMovies" : "http://10.0.2.2:8080/PopularTvs";
        getTopRatedAndPopular(topRatedUrl);
//        getTopRatedAndPopular(view, popularUrl);
    }

    private void getTopRatedAndPopular(String url) {
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

                                mediaDataArrayList.add(new MediaData(id, title, path));
                            }

                            setRecyclerAdapter(mediaDataArrayList);
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

    private void getCarousel(final View view) {
//        final ImageView imageView = (ImageView) view.findViewById(R.id.test_img);

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
                            setSliderExample(sliderDataArrayList);
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
}
