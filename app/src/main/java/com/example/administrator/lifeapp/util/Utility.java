package com.example.administrator.lifeapp.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.administrator.lifeapp.db.CalendarChinese;
import com.example.administrator.lifeapp.db.Constellation;
import com.example.administrator.lifeapp.db.History;
import com.example.administrator.lifeapp.db.HistoryDetial;
import com.example.administrator.lifeapp.db.Idiom;
import com.example.administrator.lifeapp.db.Jock;
import com.example.administrator.lifeapp.db.Msg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utility {
    public static List<Jock> handleJocks(String response) {
        List<Jock> jockList = new ArrayList<>();
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.getString("reason").equals("success")){
                    JSONArray allJockes = jsonObject.getJSONArray("result");
                    for (int i = 0; i < allJockes.length(); i++) {
                        JSONObject object = allJockes.getJSONObject(i);
                        Jock jock = new Jock();
                        jock.setTitle(i + 1 + "");
                        jock.setContent(object.getString("content"));
                        jockList.add(jock);
                        jock.save();
                    }
                    return jockList;
                }
                else {
                    Jock jock = new Jock();
                    jock.setTitle("无");
                    jockList.add(jock);
                    return jockList;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Idiom handleIdiom(String response) {
        try {
            JSONObject object1 = new JSONObject(response);
            String reason = object1.getString("reason");
            if (reason.equals("success")) {
                Log.d("123456", "成功");
                JSONObject object2 = object1.getJSONObject("result");
                Idiom idiom = new Idiom();
                idiom.setReason(reason);
                idiom.setChinesePhoneticize(object2.getString("pinyin"));
                idiom.setExample(object2.getString("example"));
                idiom.setExplain(object2.getString("chengyujs"));
                idiom.setGrammar(object2.getString("yufa"));
                idiom.setFrom(object2.getString("from_"));
                Object object4 = object2.get("tongyi");
                if (object4 instanceof JSONArray) {
                    JSONArray array1 = (JSONArray) object4;
                    List<String> synonyms = new ArrayList<>();
                    for (int i = 0; i < array1.length(); i++) {
                        synonyms.add(array1.getString(i));
                    }
                    idiom.setSynonyms(synonyms);
                } else {
                    idiom.setSynonyms(null);
                }
                Object object5 = object2.get("fanyi");
                if (object5 instanceof JSONArray) {
                    JSONArray array2 = (JSONArray) object5;
                    List<String> antonyms = new ArrayList<>();
                    for (int i = 0; i < array2.length(); i++) {
                        antonyms.add(array2.getString(i));
                    }
                    idiom.setAntonyms(antonyms);
                } else {
                    idiom.setAntonyms(null);
                }
                return idiom;
            } else {
                Idiom idiom = new Idiom();
                idiom.setReason("no");
                return idiom;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static List<History> handleHistory(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray array = jsonObject.getJSONArray("result");
            List<History> historyList = new ArrayList<>();
            if(jsonObject.getString("reason").equals("请求成功！")){
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    History history = new History();
                    history.setId(object.getString("_id"));
                    history.setTitle(object.getString("title"));
                    history.setLunar(object.getString("lunar"));
                    history.setContent(object.getString("des"));
                    history.setDay(object.getInt("day"));
                    history.setMonth(object.getInt("month"));
                    history.setYear(object.getInt("year"));
                    history.setPicture(object.getString("pic"));
                    historyList.add(history);
                    history.save();
                }
                return historyList;
            }else {
                History history = new History();
                history.setId("0");
                historyList.add(history);
                return historyList;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }
    public static HistoryDetial handleHistoryDetial(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray array = jsonObject.getJSONArray("result");
            JSONObject object = array.getJSONObject(0);
            HistoryDetial historyDetial = new HistoryDetial();
            historyDetial.setTitle(object.getString("title"));
            historyDetial.setContent(object.getString("content"));
            historyDetial.setPicture(object.getString("pic"));
            return historyDetial;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }
    public static Msg handleMsg(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            Msg msg = new Msg();
            List<Msg> msgs = new ArrayList<>();
            if(jsonObject.getString("status").equals("104")){
                msgs.add(new Msg("啊，主人，我今天身体不适，不能和您正常聊天呀了！",Msg.TYPE_RECEIVED));
                msgs.add(new Msg("主人，我没时间了,今天不说话好吗？",Msg.TYPE_RECEIVED));
                msgs.add(new Msg("先生您贵姓，我现在脑子不清楚，我们改日再聊好吗？",Msg.TYPE_RECEIVED));
                msgs.add(new Msg("求求主人了，我们明天再聊好吗",Msg.TYPE_RECEIVED));
                int random = new Random().nextInt(4);
                msg = msgs.get(random);
            }else {
                JSONObject object = jsonObject.getJSONObject("result");
                msg.setContent(object.getString("content"));
                msg.setType(Msg.TYPE_RECEIVED);
            }
            return msg;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }
    public static CalendarChinese handleCalendar(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getString("reason").equals("Success")){
                JSONObject object = jsonObject.getJSONObject("result");
                JSONObject object1 = object.getJSONObject("data");
                CalendarChinese calendar = new CalendarChinese();
                calendar.setAvoid(object1.getString("avoid"));
                calendar.setSuit(object1.getString("suit"));
                calendar.setDate(object1.getString("date"));
                calendar.setLunar(object1.getString("lunar"));
                calendar.setLunarYear(object1.getString("lunarYear"));
                calendar.setWeekday(object1.getString("weekday"));
                calendar.setYear(object1.getString("animalsYear"));
                return calendar;
            }else {
                CalendarChinese calendarChinese = new CalendarChinese();
                calendarChinese.setAvoid("无");
                return calendarChinese;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }
    public static Constellation handleConstellation(String response){
        try{
            JSONObject object = new JSONObject(response);
            Constellation constellation = new Constellation();
            if(object.getInt("error_code") == 0){
                constellation.setAll(object.getString("all"));
                constellation.setHealth(object.getString("health"));
                constellation.setLove(object.getString("love"));
                constellation.setMoney(object.getString("money"));
                constellation.setWork(object.getString("work"));
                constellation.setColor(object.getString("color"));
                constellation.setNumber(object.getInt("number"));
                constellation.setQFriend(object.getString("QFriend"));
                constellation.setSummary(object.getString("summary"));
            }else {
                constellation.setAll("无");
            }
            return constellation;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }
}
