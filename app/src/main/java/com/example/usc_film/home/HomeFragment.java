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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.usc_film.MySingleton;
import com.example.usc_film.R;
import com.example.usc_film.home.carousel.SliderAdapter;
import com.example.usc_film.home.carousel.SliderData;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private static boolean isMovie = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
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

    private void setSliderExample(View view, ArrayList<SliderData> sliderDataArrayList) {
        // initializing the slider view.
        SliderView sliderView = view.findViewById(R.id.carousel_slider);
        sliderView.stopAutoCycle();

        // passing this array list inside our adapter class.
        SliderAdapter adapter = new SliderAdapter(getActivity().getApplicationContext(), sliderDataArrayList);

        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        // below method is used to
        // setadapter to sliderview.
        sliderView.setSliderAdapter(adapter);

        // below method is use to set
        // scroll time in seconds.
        sliderView.setScrollTimeInSec(3);

        // to set it scrollable automatically
        // we use below method.
        sliderView.setAutoCycle(true);

        // to start autocycle below method is used.
        sliderView.startAutoCycle();
    }

    private void loadData(View view) {
        System.out.println("Load Data");
        getCarousel(view);
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
                            setSliderExample(view, sliderDataArrayList);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        textView.setText("That didn't work!");
                    }
                });

        // Add the request to the RequestQueue.
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }
}
