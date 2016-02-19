package kr.co.raonnetworks.cityhall;

import android.app.Application;

import java.io.File;

import kr.co.raonnetworks.cityhall.libs.FileManager;
import kr.co.raonnetworks.cityhall.libs.SQLiteManager;

/**
 * Created by MoonJongRak on 2016. 2. 18..
 */
public class CityHallApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        File mFileCityHallSqlite = new File(getFilesDir(), SQLiteManager.SQLITE_FILE_NAME);

        if (!mFileCityHallSqlite.exists()) {
            FileManager.copyFile(getResources().openRawResource(R.raw.city_hall), mFileCityHallSqlite);
        }
    }
}
