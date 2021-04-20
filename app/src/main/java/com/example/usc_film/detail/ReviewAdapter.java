package com.example.usc_film.detail;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usc_film.R;
import com.example.usc_film.ReviewActivity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {
    private ArrayList<Review> reviews;

    public ReviewAdapter(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView headView;
        private TextView ratingView;
        private TextView contextView;

        public MyViewHolder(final View view) {
            super(view);
            headView = view.findViewById(R.id.recycler_detail_review_header);
            ratingView = view.findViewById(R.id.recycler_detail_review_rating);
            contextView = view.findViewById(R.id.recycler_detail_review_context);
            this.itemView = view;
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_review, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Review review = reviews.get(position);

        String headerText = "by " + review.getName() + " on ";
        // 2017-11-11T15:09:34.114Z
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(review.getDate());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            headerText += calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);
            headerText += ", " + calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
            headerText += " " + review.getDate().substring(8, 10);
            headerText += " " + review.getDate().substring(0, 4);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.headView.setText(headerText);
        final String ratingText = review.getRating() + "/5";
        holder.ratingView.setText(ratingText);
        holder.contextView.setText(review.getContext());

        final String finalHeaderText = headerText;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ReviewActivity.class);
                intent.putExtra("rating", ratingText);
                intent.putExtra("header", finalHeaderText);
                intent.putExtra("context", review.getContext());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}
