package kr.co.raonnetworks.cityhall.libs;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import kr.co.raonnetworks.cityhall.R;

/**
 * Created by MoonJongRak on 2016. 2. 22..
 */
public class ConfigManager {

    private static ConfigManager instance;

    private String id;
    private String passwd;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private boolean isTagReverse;

    private ConfigManager(Context mContext) throws JSONException, IOException {
        JSONObject mJsonObject = new JSONObject(getJsonString(mContext));
        this.id = mJsonObject.getString("id");
        this.passwd = mJsonObject.getString("passwd");
        this.year = mJsonObject.getInt("year");
        this.month = mJsonObject.getInt("month") - 1;
        this.day = mJsonObject.getInt("day");
        this.hour = mJsonObject.getInt("hour");
        this.minute = mJsonObject.getInt("minute");
        this.isTagReverse = mJsonObject.getBoolean("isTagReverse");

    }

    public static ConfigManager getInstance(Context mContext) {
        if (instance == null) {
            try {
                instance = new ConfigManager(mContext);
            } catch (JSONException | IOException e) {
                instance = null;
                e.printStackTrace();
            }
        }
        return instance;
    }

    private String getJsonString(Context mContext) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(mContext.getResources().openRawResource(R.raw.config)));

        String tmp;
        StringBuilder buffer = new StringBuilder();
        if ((tmp = reader.readLine()) != null) {
            buffer.append(tmp);
        }

        return buffer.toString();
    }


    public String getId() {
        return id;
    }

    public String getPasswd() {
        return passwd;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public boolean isTagReverse() {
        return isTagReverse;
    }

    public void setIsTagReverse(boolean isTagReverse) {
        this.isTagReverse = isTagReverse;
    }
}
