package kr.co.raonnetworks.cityhall.libs;

import android.database.sqlite.SQLiteDatabase;

import java.io.File;

import kr.co.raonnetworks.cityhall.CityHallApplication;

/**
 * Created by MoonJongRak on 2015. 11. 27..
 */
public class SQLiteManager {
    public static final String SQLITE_FILE_NAME = "CityHall.sqlite";
    public static final File SQLITE_FILE = new File(CityHallApplication.APP_DIR, SQLITE_FILE_NAME);

    private static SQLiteManager instance;
    private SQLiteDatabase mSqLiteDatabase;


    private SQLiteManager() {
        mSqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(SQLITE_FILE, null);
    }

    public static SQLiteManager getInstance() {
        if (instance == null || !instance.mSqLiteDatabase.isOpen()) {
            instance = new SQLiteManager();
        }
        return instance;
    }

    public SQLiteDatabase getDataBase() {
        return mSqLiteDatabase;
    }


    public void close() {
        if (mSqLiteDatabase.isOpen()) {
            mSqLiteDatabase.close();
        }
    }


}
