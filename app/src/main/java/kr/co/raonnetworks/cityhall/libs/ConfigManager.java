package kr.co.raonnetworks.cityhall.libs;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kr.co.raonnetworks.cityhall.R;

/**
 * Created by MoonJongRak on 2016. 2. 22..
 */
public class ConfigManager {

    public static String CONFIG_FILE_NAME = "config.json";
    public static String LOGO_FILE_NAME = "logo.png";

    private static ConfigManager instance;
    private Context mContext;

    private String loginTitle;
    private String cityTitle;
    private String id;
    private String passwd;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private boolean isTagReverse;

    private ConfigManager(Context mContext) throws JSONException, IOException {
        this.mContext = mContext;
        JSONObject mJsonObject = new JSONObject(getJsonString(mContext));
        this.loginTitle = mJsonObject.getString("loginTitle");
        this.cityTitle = mJsonObject.getString("cityTitle");
        this.id = mJsonObject.getString("id");
        this.passwd = mJsonObject.getString("passwd");
        this.isTagReverse = mJsonObject.getBoolean("isTagReverse");

        //시간 설정
        long time = mContext.getSharedPreferences("CONFIG", Context.MODE_PRIVATE).getLong("time", System.currentTimeMillis());
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTime(new Date(time));

        this.year = mCalendar.get(Calendar.YEAR);
        this.month = mCalendar.get(Calendar.MONTH);
        this.day = mCalendar.get(Calendar.DAY_OF_MONTH);
        this.hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        this.minute = mCalendar.get(Calendar.MINUTE);

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

    public void setTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            mContext.getSharedPreferences("CONFIG", Context.MODE_PRIVATE).edit().putLong("time", format.parse(time).getTime()).apply();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private String getJsonString(Context mContext) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(mContext.getExternalFilesDir(null), CONFIG_FILE_NAME))));
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

    public String getLoginTitle() {
        return loginTitle;
    }

    public void setLoginTitle(String loginTitle) {
        this.loginTitle = loginTitle;
    }

    public String getCityTitle() {
        return cityTitle;
    }

    public void setCityTitle(String cityTitle) {
        this.cityTitle = cityTitle;
    }
}
