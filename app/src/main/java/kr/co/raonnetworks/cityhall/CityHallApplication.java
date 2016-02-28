package kr.co.raonnetworks.cityhall;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.os.EnvironmentCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import kr.co.raonnetworks.cityhall.libs.ConfigManager;
import kr.co.raonnetworks.cityhall.libs.FileManager;
import kr.co.raonnetworks.cityhall.libs.SQLiteManager;

/**
 * Created by MoonJongRak on 2016. 2. 18..
 */
public class CityHallApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        File mFileCityHallSqlite = new File(getExternalFilesDir(null), SQLiteManager.SQLITE_FILE_NAME);
        File mFileConfigJson = new File(getExternalFilesDir(null), ConfigManager.CONFIG_FILE_NAME);
        File mFileLogo = new File(getExternalFilesDir(null), ConfigManager.LOGO_FILE_NAME);

        if (!mFileCityHallSqlite.exists()) {
            FileManager.copyFile(getResources().openRawResource(R.raw.city_hall), mFileCityHallSqlite);
        }

        if (!mFileConfigJson.exists()) {
            FileManager.copyFile(getResources().openRawResource(R.raw.config), mFileConfigJson);
        }

        if (!mFileLogo.exists()) {
            try {
                OutputStream out = new FileOutputStream(mFileLogo);
                Bitmap tmp = BitmapFactory.decodeResource(getResources(), R.drawable.ch_logo);
                tmp.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(mFileLogo));
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.e("moon", getExternalFilesDir(null).getAbsolutePath());
        if (ConfigManager.getInstance(this) == null) {
            Toast.makeText(this, "컨피그 파일이 잘못 되었습니다.", Toast.LENGTH_LONG).show();
            // System.exit(0);
        }


    }
}
