package com.example.administrator.lifeapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.lifeapp.db.History;
import com.example.administrator.lifeapp.util.HttpUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private String address = "http://api.juheapi.com/japi/tohdet?key=dee300bf3ba4fd4ffbc64af72c228150&v=1.0&id=";
    private List<History> historyList;
    private Context context;
    private String historyInformation;

    public HistoryAdapter(List<History> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        History history = historyList.get(i);
        viewHolder.title.setText(history.getTitle());
        viewHolder.content.setText("        " + history.getContent());
        viewHolder.lunar.setText(history.getLunar());
        viewHolder.day.setText(history.getYear() + "-" + history.getMonth() + "-" + history.getDay());
        String id = history.getId();
        final String address1 = address + id;
        viewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtil.sendOkHttpRequest(address1, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        historyInformation = response.body().string();
                        Intent intent = new Intent(context,InformationActivity.class);
                        intent.putExtra("data",historyInformation);
                        context.startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView day;
        TextView content;
        TextView lunar;
        ImageView picture;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_title);
            day = itemView.findViewById(R.id.txt_date);
            content = itemView.findViewById(R.id.txt_content);
            lunar = itemView.findViewById(R.id.txt_lunar);
        }
    }

    public String getHistoryInformation() {
        return historyInformation;
    }
}
