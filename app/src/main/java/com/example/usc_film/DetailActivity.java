package com.example.usc_film;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    private String id;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");
            title = extras.getString("title");
        }

        setContentView(R.layout.activity_detail);
        TextView titleText = findViewById(R.id.detail_title);
        titleText.setText(title);
    }
}
