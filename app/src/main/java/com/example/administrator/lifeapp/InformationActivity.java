package com.example.administrator.lifeapp;

import android.content.Intent;
import android.graphics.Typeface;
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
        title.setTypeface(Typeface.createFromAsset(getAssets(),"font/hwxk.ttf"));
        content.setTypeface(Typeface.createFromAsset(getAssets(),"font/hwxk.ttf"));
        title.setText(historyDetial.getTitle());
        char[] chars = historyDetial.getContent().toCharArray();
        StringBuilder builder = new StringBuilder();
        builder.append("        ");
        for (int i = 0; i < chars.length; i++) {
            builder.append(chars[i]);
            if(chars[i] == '\n'){
                builder.append("        ");
            }
        }
        content.setText(builder.toString());
        Glide.with(InformationActivity.this).load(historyDetial.getPicture()).into(image);
    }
}
