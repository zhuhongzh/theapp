package com.example.administrator.lifeapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Selection;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.administrator.lifeapp.Adapter.HistoryAdapter;
import com.example.administrator.lifeapp.Adapter.JockAdapter;
import com.example.administrator.lifeapp.Adapter.MsgAdapter;
import com.example.administrator.lifeapp.db.CalendarChinese;
import com.example.administrator.lifeapp.db.Constellation;
import com.example.administrator.lifeapp.db.History;
import com.example.administrator.lifeapp.db.Idiom;
import com.example.administrator.lifeapp.db.Jock;
import com.example.administrator.lifeapp.db.Msg;
import com.example.administrator.lifeapp.util.HttpUtil;
import com.example.administrator.lifeapp.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 更新内容2
 */
public class MainActivity extends AppCompatActivity {
    private NavigationView naView;
    private List<Jock> jockList = new ArrayList<>();
    private List<String> thereNo = new ArrayList<>();
    private List<Jock> jocks;

    private List<History> todayHistory = new ArrayList<>();
    private List<History> histories;
    private List<History> historyList;

    private String address = "http://v.juhe.cn/joke/randJoke.php?key=8899f9b377d33409cc548e1f06a45fb4";
    private String address2 = "http://v.juhe.cn/chengyu/query?key=67bdce5b4e868dea8a3a5dde475d76bb&word=";
    private String address3;
    private String adress4 = "http://api.jisuapi.com/iqa/query?appkey=7553d1c47bc84d9d&question=";

    private List<Msg> msgList = new ArrayList<>();

    private JockAdapter adapterJock;
    private HistoryAdapter historyAdapter;
    private MsgAdapter msgAdapter;

    private SwipeRefreshLayout jockLayout;
    private LinearLayout bookLayout;
    private LinearLayout historyLayout;
    private LinearLayout chatLayout;
    private Idiom idiom;
    private Button find;
    private EditText editText;
    private TextView txtToday;

    private TextView txtChinese;
    private TextView txtPinyin;
    private TextView txtGrammer;
    private TextView txtFrom;
    private TextView txtExample;
    private TextView txtExplain;
    private ListView listViewSynonyms;
    private ListView listViewAntonyms;

    private EditText edtInput;
    private Button sendBtn;
    private RecyclerView chatContent;
    private LinearLayout headerLayout;
    private List<String> listconstellation = new ArrayList<>(Arrays.asList("白羊座", "金牛座", "双子座",
            "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座", "水瓶座", "双鱼座"));
    private List<Constellation> constellationAll;
    private List<CalendarChinese> calendarChineseAll;

    private int year;
    private int month;
    private int day;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        naView = findViewById(R.id.nav_view);
        jockLayout = findViewById(R.id.jock_layout);
        bookLayout = findViewById(R.id.book_layout);
        historyLayout = findViewById(R.id.history_layout);
        chatLayout = findViewById(R.id.chat_layout);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        initJock();
        initbook();
        initHistory();
        inintChat();
        naView.setCheckedItem(R.id.nav_historyToday);
        headerLayout = (LinearLayout) naView.inflateHeaderView(R.layout.nav_header);
        initheader();
        naView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_historyToday:
                        historyLayout.setVisibility(View.VISIBLE);
                        jockLayout.setVisibility(View.GONE);
                        bookLayout.setVisibility(View.GONE);
                        chatLayout.setVisibility(View.GONE);
                        break;
                    case R.id.nav_book:
                        bookLayout.setVisibility(View.VISIBLE);
                        historyLayout.setVisibility(View.GONE);
                        jockLayout.setVisibility(View.GONE);
                        chatLayout.setVisibility(View.GONE);
                        break;
                    case R.id.nav_smiling:
                        if (jocks == null) {
                            sendJock();
                        }
                        jockLayout.setVisibility(View.VISIBLE);
                        bookLayout.setVisibility(View.GONE);
                        historyLayout.setVisibility(View.GONE);
                        chatLayout.setVisibility(View.GONE);
                        break;
                    case R.id.nav_robot:
                        chatLayout.setVisibility(View.VISIBLE);
                        jockLayout.setVisibility(View.GONE);
                        bookLayout.setVisibility(View.GONE);
                        historyLayout.setVisibility(View.GONE);
                        break;
                }
                return true;
            }
        });
    }

    private void initheader() {
        final TextView date = headerLayout.findViewById(R.id.header_date);
        final TextView lunar = headerLayout.findViewById(R.id.header_lunar);
        final TextView suit = headerLayout.findViewById(R.id.header_suit);
        final TextView avoid = headerLayout.findViewById(R.id.header_avoid);
        final TextView yeart = headerLayout.findViewById(R.id.header_year);
        final TextView all = headerLayout.findViewById(R.id.header_all);
        final TextView color = headerLayout.findViewById(R.id.header_color);
        final TextView QFriend = headerLayout.findViewById(R.id.header_QFriend);
        final TextView number = headerLayout.findViewById(R.id.header_number);
        final TextView health = headerLayout.findViewById(R.id.header_health);
        final TextView work = headerLayout.findViewById(R.id.header_work);
        final TextView money = headerLayout.findViewById(R.id.header_money);
        final TextView love = headerLayout.findViewById(R.id.header_love);
        final TextView summary = headerLayout.findViewById(R.id.header_summary);
        Spinner spinner = headerLayout.findViewById(R.id.header_spinner);
        constellationAll = DataSupport.findAll(Constellation.class);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                final Constellation constellation0;
                boolean result = true;
                final String con = listconstellation.get(position);
                for (int i = 0; i < constellationAll.size(); i++) {
                    if (con.equals(constellationAll.get(i).getName()) && constellationAll.get(i).getDate().equals(year + "-" + month + "-" + day)) {
                        constellation0 = constellationAll.get(i);
                        result = false;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                all.setText("综合指数：" + constellation0.getAll());
                                health.setText("健康指数：" + constellation0.getHealth());
                                money.setText("财运指数：" + constellation0.getMoney());
                                love.setText("爱情指数：" + constellation0.getLove());
                                work.setText("工作指数：" + constellation0.getWork());
                                number.setText("幸运数字：" + constellation0.getNumber() + "");
                                color.setText("幸运颜色：" + constellation0.getColor());
                                QFriend.setText("速配星座：" + constellation0.getQFriend());
                                summary.setText("今日概述：" + constellation0.getSummary());
                            }
                        });
                        break;
                    }
                }
                if (result == true) {
                    String address = "http://web.juhe.cn:8080/constellation/getAll?consName=" + con + "&type=today&key=40a9c0716a1f44e625edf0d28314da55";
                    HttpUtil.sendOkHttpRequest(address, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseText = response.body().string();
                            final Constellation constellation1 = Utility.handleConstellation(responseText);
                            constellation1.setName(listconstellation.get(position));
                            constellation1.setDate(year + "-" + month + "-" + day);
                            constellation1.save();
                            if (constellation1.getAll().equals("无")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        all.setText("综合指数：" + "暂无法查询");
                                        health.setText("健康指数：" + "暂无法查询");
                                        money.setText("财运指数：" + "暂无法查询");
                                        love.setText("爱情指数：" + "暂无法查询");
                                        work.setText("工作指数：" + "暂无法查询");
                                        number.setText("幸运数字：" + "暂无法查询");
                                        color.setText("幸运颜色：" + "暂无法查询");
                                        QFriend.setText("速配星座：" + "暂无法查询");
                                        summary.setText("今日概述：" + "暂无法查询");
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        all.setText("综合指数：" + constellation1.getAll());
                                        health.setText("健康指数：" + constellation1.getHealth());
                                        money.setText("财运指数：" + constellation1.getMoney());
                                        love.setText("爱情指数：" + constellation1.getLove());
                                        work.setText("工作指数：" + constellation1.getWork());
                                        number.setText("幸运数字：" + constellation1.getNumber() + "");
                                        color.setText("幸运颜色：" + constellation1.getColor());
                                        QFriend.setText("速配星座：" + constellation1.getQFriend());
                                        summary.setText("今日概述：" + constellation1.getSummary());
                                    }
                                });
                            }
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        boolean reason = false;
        calendarChineseAll = DataSupport.findAll(CalendarChinese.class);
        for (int i = 0; i < calendarChineseAll.size(); i++) {
            if (calendarChineseAll.get(i).getDate().equals(year + "-" + month + "-" + day)) {
                final CalendarChinese calendarChinese = calendarChineseAll.get(i);
                reason = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        yeart.setText(calendarChinese.getYear() + "年大吉      ");
                        lunar.setText(calendarChinese.getLunarYear() + calendarChinese.getLunar());
                        date.setText(calendarChinese.getDate() + "      " + calendarChinese.getWeekday());
                        avoid.setText("今日宜：" + calendarChinese.getSuit());
                        suit.setText("今日不宜：" + calendarChinese.getAvoid());
                    }
                });
                break;
            }
        }
        if (reason == false) {
            String address = "http://v.juhe.cn/calendar/day?date=" + year + "-" + month + "-" + day + "&key=ed5c43068b922b29d206ec79fea2a99a";
            HttpUtil.sendOkHttpRequest(address, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    final CalendarChinese calendarChinese = Utility.handleCalendar(responseText);
                    if (calendarChinese.getAvoid().equals("无")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                yeart.setText("");
                                lunar.setText("");
                                date.setText("");
                                avoid.setText("");
                                suit.setText("");
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                yeart.setText(calendarChinese.getYear() + "年大吉      ");
                                lunar.setText(calendarChinese.getLunarYear() + calendarChinese.getLunar());
                                date.setText(calendarChinese.getDate() + "        " + calendarChinese.getWeekday());
                                avoid.setText("今日宜：" + calendarChinese.getSuit());
                                suit.setText("今日不宜：" + calendarChinese.getAvoid());
                            }
                        });
                    }
                }
            });
        }
    }

    private void inintChat() {
        edtInput = chatLayout.findViewById(R.id.input_edt);
        sendBtn = chatLayout.findViewById(R.id.send_btn);
        chatContent = chatLayout.findViewById(R.id.chat_recyclerview);
        Button contentBtn = findViewById(R.id.content_btn);
        contentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChatContent.class);
                startActivity(intent);
            }
        });
//        android.support.v7.widget.Toolbar toolbar = chatLayout.findViewById(R.id.toolbar);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(MainActivity.this, ChatContent.class);
//                    startActivity(intent);
//                }
//            });
//        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        Msg msg = new Msg();
        msg.setType(Msg.TYPE_RECEIVED);
        msg.setContent("您好，我是小i，很荣幸为您服务");
        msgList.add(msg);
        msgAdapter = new MsgAdapter(msgList);
        chatContent.setLayoutManager(layoutManager);
        chatContent.setAdapter(msgAdapter);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtInput.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "请输入你想询问小智的类容", Toast.LENGTH_SHORT).show();
                } else {
                    sendChat();
                }
            }
        });
    }

    private void sendChat() {
        final Msg msg = new Msg();
        msg.setType(Msg.TYPE_SENT);
        msg.setContent(edtInput.getText().toString());
        String address = adress4 + edtInput.getText().toString();
        Log.d("123456", edtInput.getText().toString());
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(MainActivity.this, "您的网络未连接，无法查询，请尽快连接网络", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                Msg msg1 = Utility.handleMsg(responseText);
                msgList.add(msg);
                msg.save();
                msg1.save();
                msgList.add(msg1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        msgAdapter.notifyDataSetChanged();
                        chatContent.scrollToPosition(msgList.size() - 1);
                        edtInput.setText("");
                    }
                });
            }
        });
    }

    private void initHistory() {
        txtToday = findViewById(R.id.txt_today);
        txtToday.setTypeface(Typeface.createFromAsset(getAssets(), "font/hwxk.ttf"));
        address3 = "http://api.juheapi.com/japi/toh?key=dee300bf3ba4fd4ffbc64af72c228150&v=1.0&month=" + month + "&day=" + day;
        final RecyclerView recyclerView = historyLayout.findViewById(R.id.history_recyclerview);
        historyAdapter = new HistoryAdapter(todayHistory);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(historyAdapter);
        sendHistory();
    }

    private void sendHistory() {
        historyList = DataSupport.findAll(History.class);
        todayHistory.clear();
        boolean reason = false;
        for (int i = 0; i < historyList.size(); i++) {
            if ((historyList.get(i).getMonth() + "-" + historyList.get(i).getDay()).equals(month + "-" + day)) {
                todayHistory.add(historyList.get(i));
                reason = true;
            }
        }
        if (reason == true) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    historyAdapter.notifyDataSetChanged();
                }
            });
        }
        if (reason == false) {
            HttpUtil.sendOkHttpRequest(address3, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(MainActivity.this, "您的网络未连接，无法查询，请尽快连接网络", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    histories = Utility.handleHistory(responseText);
                    if (histories.get(0).getId().equals("0")) {
                        if (historyList.size() != 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    histories = DataSupport.findAll(History.class);
                                    todayHistory.clear();
                                    for (int i = 0; i < 20; i++) {
                                        todayHistory.add(histories.get(i));
                                    }
                                    historyAdapter.notifyDataSetChanged();
                                    txtToday.setText("峥嵘岁月");
                                }
                            });
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                todayHistory.clear();
                                for (int i = 0; i < histories.size(); i++) {
                                    todayHistory.add(histories.get(i));
                                }
                                historyAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            });
        }
    }

    private void initJock() {
        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapterJock = new JockAdapter(jockList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        TextView jockTitle = jockLayout.findViewById(R.id.title);
        jockTitle.setTypeface(Typeface.createFromAsset(getAssets(), "font/hwxk.ttf"));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterJock);
        jockLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendJock();
            }
        });
    }

    private void initbook() {
        find = bookLayout.findViewById(R.id.btn_find);
        editText = bookLayout.findViewById(R.id.edt_input);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Animation animation = new AlphaAnimation(1.0f, 0.0f);
//                animation.setDuration(300);
//                Animation animation;
//                animation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.my_anim);
//                find.startAnimation(animation);
                boolean result = true;
                String content = editText.getText().toString();
                if (content.equals("")) {
                    Toast.makeText(MainActivity.this, "请输入您要查询的成语", Toast.LENGTH_SHORT).show();
                } else {
                    char[] contentChar = content.toCharArray();
                    for (int i = 0; i < contentChar.length; i++) {
                        if (!isChinese(contentChar[i])) {
                            result = false;
                            break;
                        }
                    }
                    if (result == true) {
                        List<Idiom> idiomList = DataSupport.findAll(Idiom.class);
                        for (int i = 0; i < idiomList.size(); i++) {
                            final Idiom idiom2 = idiomList.get(i);
                            if (editText.getText().toString().equals(idiom2.getIdiom())) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        txtChinese.setText(idiom2.getIdiom());
                                        txtPinyin.setText(idiom2.getChinesePhoneticize());
                                        txtFrom.setText(idiom2.getFrom());
                                        txtGrammer.setText((idiom2.getGrammar()));
                                        txtExample.setText(idiom2.getExample());
                                        txtExplain.setText(idiom2.getExplain());
                                        if (idiom2.getAntonyms() != null) {
                                            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                                    (MainActivity.this, android.R.layout.simple_list_item_1, idiom.getAntonyms());
                                            adapter.notifyDataSetChanged();
                                            listViewAntonyms.setAdapter(adapter);
                                        }
                                        if (idiom2.getAntonyms() == null) {
                                            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                                    (MainActivity.this, android.R.layout.simple_list_item_1, thereNo);
                                            adapter.notifyDataSetChanged();
                                            listViewAntonyms.setAdapter(adapter);
                                        }
                                        if (idiom2.getSynonyms() != null) {
                                            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                                    (MainActivity.this, android.R.layout.simple_list_item_1, idiom.getSynonyms());
                                            adapter.notifyDataSetChanged();
                                            listViewSynonyms.setAdapter(adapter);
                                        }
                                        if (idiom2.getSynonyms() == null) {
                                            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                                    (MainActivity.this, android.R.layout.simple_list_item_1, thereNo);
                                            adapter.notifyDataSetChanged();
                                            listViewSynonyms.setAdapter(adapter);
                                        }
                                    }
                                });
                                result = false;
                            }
                        }
                        if (result == true) {
                            sendBook();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "您输入的不是成语，查询失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        txtChinese = bookLayout.findViewById(R.id.txt_chinese);
        txtPinyin = bookLayout.findViewById(R.id.txt_pinyin);
        txtFrom = bookLayout.findViewById(R.id.txt_from);
        txtGrammer = bookLayout.findViewById(R.id.txt_grammar);
        txtExample = bookLayout.findViewById(R.id.txt_example);
        txtExplain = bookLayout.findViewById(R.id.txt_explain);
        listViewAntonyms = bookLayout.findViewById(R.id.antonyms_listview);
        listViewSynonyms = bookLayout.findViewById(R.id.synonyms_listview);
    }

    private void sendBook() {
        String address;
        address = address2 + editText.getText();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(MainActivity.this, "您的网络未连接，无法查询，请尽快连接网络", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                idiom = Utility.handleIdiom(responseText);
                if (idiom.getReason().equals("success")) {
                    idiom.setIdiom(editText.getText().toString());
                    idiom.save();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtChinese.setText(editText.getText());
                            txtPinyin.setText(idiom.getChinesePhoneticize());
                            txtFrom.setText(idiom.getFrom());
                            txtGrammer.setText((idiom.getGrammar()));
                            txtExample.setText(idiom.getExample());
                            txtExplain.setText(idiom.getExplain());
                            if (idiom.getAntonyms() != null) {
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                        (MainActivity.this, android.R.layout.simple_list_item_1, idiom.getAntonyms());
                                adapter.notifyDataSetChanged();
                                listViewAntonyms.setAdapter(adapter);
                            }
                            if (idiom.getAntonyms() == null) {
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                        (MainActivity.this, android.R.layout.simple_list_item_1, thereNo);
                                adapter.notifyDataSetChanged();
                                listViewAntonyms.setAdapter(adapter);
                            }
                            if (idiom.getSynonyms() != null) {
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                        (MainActivity.this, android.R.layout.simple_list_item_1, idiom.getSynonyms());
                                adapter.notifyDataSetChanged();
                                listViewSynonyms.setAdapter(adapter);
                            }
                            if (idiom.getSynonyms() == null) {
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                        (MainActivity.this, android.R.layout.simple_list_item_1, thereNo);
                                adapter.notifyDataSetChanged();
                                listViewSynonyms.setAdapter(adapter);
                            }
                        }
                    });
                } else {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "没有查询到这个成语或者您查询的次数已经达到上限", Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        });
    }


    public void sendJock() {
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(MainActivity.this, "您的网络未连接，无法查询，请尽快连接网络", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                jocks = Utility.handleJocks(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        jockList.clear();
                        if (jocks.get(0).getTitle() == "无") {
                            jocks = DataSupport.findAll(Jock.class);
                            for (int i = 0; i < 10; i++) {
                                int random = new Random().nextInt(10000);
                                jockList.add(jocks.get(random));
                            }
                        } else {
                            for (int i = 0; i < jocks.size(); i++) {
                                jockList.add(jocks.get(i));
                            }
                        }
                        adapterJock.notifyDataSetChanged();
                        jockLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    public boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
}
