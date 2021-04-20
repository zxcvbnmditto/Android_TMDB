package com.example.usc_film;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ReviewActivity extends AppCompatActivity {
    private String ratingText;
    private String headerText;
    private String contextText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ratingText = extras.getString("rating");
            headerText = extras.getString("header");
            contextText = extras.getString("context");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        TextView rating = findViewById(R.id.review_rating);
        TextView header = findViewById(R.id.review_header);
        TextView context = findViewById(R.id.review_context);

        rating.setText(ratingText);
        header.setText(headerText);
        context.setText(contextText);
    }
}
