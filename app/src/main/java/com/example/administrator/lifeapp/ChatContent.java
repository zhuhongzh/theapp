package com.example.administrator.lifeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.administrator.lifeapp.Adapter.MsgAdapter;
import com.example.administrator.lifeapp.db.Msg;

import org.litepal.crud.DataSupport;

import java.util.List;

public class ChatContent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_content);
        RecyclerView recyclerView = findViewById(R.id.chatcontent_recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        List<Msg> msgList = DataSupport.findAll(Msg.class);
        MsgAdapter msgAdapter = new MsgAdapter(msgList);
        recyclerView.setAdapter(msgAdapter);
        Toolbar toolbar = findViewById(R.id.toolbar_chatcontent);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
