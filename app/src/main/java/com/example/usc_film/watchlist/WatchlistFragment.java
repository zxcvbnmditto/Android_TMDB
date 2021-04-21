package com.example.usc_film.watchlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.usc_film.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

import static android.widget.GridView.STRETCH_SPACING_UNIFORM;

public class WatchlistFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watchlist, container, false);
        RecyclerView watchlistView = view.findViewById(R.id.watchlist_content);
        TextView emptyTextView = view.findViewById(R.id.watchlist_empty);
        ScrollView scrollView = view.findViewById(R.id.watchlist_scrollview);
        ArrayList<WatchlistData> watchlistDataArrayList = new ArrayList<>();

        loadWatchlistData(watchlistDataArrayList);

        if (watchlistDataArrayList.size() == 0) {
            emptyTextView.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.INVISIBLE);
        } else {
            emptyTextView.setVisibility(View.INVISIBLE);
            scrollView.setVisibility(View.VISIBLE);
        }
        setAdapter(watchlistView, watchlistDataArrayList);
        return view;
    }

    private void loadWatchlistData(ArrayList<WatchlistData> watchlistDataArrayList) {
        // Read Shared Preference
        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        if (sharedPref.contains("order")) {
            String order_raw = sharedPref.getString("order", "");
            System.out.println(order_raw);
            String[] order = order_raw.split("\\|");
            for (int i = 1; i < order.length; i++) {
//                System.out.println("###############################");
//                System.out.println(order[i]);
                String[] parts = order[i].split("-");
                String id = parts[0];
                String media_type = parts[1];
                String imgUrl = sharedPref.getString(order[i], "");
                watchlistDataArrayList.add(new WatchlistData(imgUrl, media_type, id));
            }
        }
    }

    private void setAdapter(RecyclerView view, ArrayList<WatchlistData> watchlistDataArrayList) {
        WatchlistAdapter adapter = new WatchlistAdapter(watchlistDataArrayList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(),
                3);

        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
        view.setAdapter(adapter);
    }
}
