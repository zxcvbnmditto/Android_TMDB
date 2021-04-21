package com.example.usc_film.search;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private RecyclerView searchContentView;
    private TextView searchNoReseultFoundView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        SearchView searchView = view.findViewById(R.id.search_bar);
        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchContentView = view.findViewById(R.id.search_content);
        searchNoReseultFoundView = view.findViewById(R.id.search_no_result_found);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String url = "http://10.0.2.2:8080/Search?query=" + query;
                search(url);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    String url = "http://10.0.2.2:8080/Search?query=" + newText;
                    search(url);
                }
                return false;
            }
        });

        return view;
    }


    private void search(String url) {
        // Request a string response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            ArrayList<SearchData> searchDataArrayList = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);

                                String id = obj.getString("id");
                                String title = obj.getString("name");
                                String type = obj.getString("type");
                                String year = obj.getString("year");
                                String path = obj.getString("path");
                                String rating = obj.getString("rating");

                                searchDataArrayList.add(new SearchData(id, path, type, title, year, rating));
                            }

                            setSearchAdapter(searchDataArrayList);

                            if (response.length() > 0) {
                                searchContentView.setVisibility(View.VISIBLE);
                                searchNoReseultFoundView.setVisibility(View.INVISIBLE);
                            }
                            else {
                                searchContentView.setVisibility(View.INVISIBLE);
                                searchNoReseultFoundView.setVisibility(View.VISIBLE);
                            }

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

    private void setSearchAdapter(ArrayList<SearchData> searchDataArrayList) {
        SearchAdapter adapter = new SearchAdapter(searchDataArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        searchContentView.setLayoutManager(layoutManager);
        searchContentView.setItemAnimator(new DefaultItemAnimator());
        searchContentView.setAdapter(adapter);
    }
}
