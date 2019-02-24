package com.example.administrator.lifeapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.lifeapp.R;
import com.example.administrator.lifeapp.db.Jock;

import java.util.List;

public class JockAdapter extends RecyclerView.Adapter<JockAdapter.ViewHolder> {
    private List<Jock> jockList;

    public JockAdapter(List<Jock> jockList) {
        this.jockList = jockList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.jock_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Jock jock = jockList.get(i);
//        viewHolder.title.setText(jock.getTitle());
        viewHolder.content.setText(i + 1 + "、"+jock.getContent());
    }

    @Override
    public int getItemCount() {
        return jockList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
//            TextView title;
            TextView content;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            title = itemView.findViewById(R.id.jock_title);
            content = itemView.findViewById(R.id.jock_content);
        }
    }
}
