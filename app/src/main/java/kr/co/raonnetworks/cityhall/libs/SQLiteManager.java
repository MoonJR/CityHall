package kr.co.raonnetworks.cityhall.libs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Created by MoonJongRak on 2015. 11. 27..
 */
public class SQLiteManager {
    public static final String SQLITE_FILE_NAME = "CityHall.sqlite";


    private static SQLiteManager instance;
    private SQLiteDatabase mSqLiteDatabase;


    private SQLiteManager(Context mContext) {
        File dbFile = new File(mContext.getExternalFilesDir(null), SQLITE_FILE_NAME);

        mSqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);

    }

    public static SQLiteManager getInstance(Context mContext) {
        if (instance == null || !instance.mSqLiteDatabase.isOpen()) {
            instance = new SQLiteManager(mContext);
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
