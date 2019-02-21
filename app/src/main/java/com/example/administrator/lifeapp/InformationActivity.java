package com.example.administrator.lifeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.lifeapp.db.HistoryDetial;
import com.example.administrator.lifeapp.util.Utility;

public class InformationActivity extends AppCompatActivity {
    String informationHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        Intent intent = getIntent();
        informationHistory = intent.getStringExtra("data");
        initHistoryDetial();
    }

    private void initHistoryDetial() {
        HistoryDetial historyDetial = Utility.handleHistoryDetial(informationHistory);
        TextView title = findViewById(R.id.title_detial);
        TextView content = findViewById(R.id.content_detial);
        ImageView image = findViewById(R.id.picture_detial);
        title.setText(historyDetial.getTitle());
        content.setText(historyDetial.getContent());
        Glide.with(InformationActivity.this).load(historyDetial.getPicture()).into(image);
    }
}
