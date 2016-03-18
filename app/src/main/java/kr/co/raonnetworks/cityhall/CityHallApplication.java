package kr.co.raonnetworks.cityhall;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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

    public static File APP_DIR = new File(Environment.getExternalStorageDirectory(), "CityHall");

    @Override
    public void onCreate() {
        super.onCreate();

        //버전 관리하기 위해 기존 데이터 제거
        if (!getSharedPreferences("SETTING", MODE_PRIVATE).getBoolean(getVersion(), false)) {
            if (SQLiteManager.SQLITE_FILE.exists()) {
                SQLiteManager.SQLITE_FILE.renameTo(new File(SQLiteManager.SQLITE_FILE.getParentFile(), SQLiteManager.SQLITE_FILE_NAME + "_" + getVersion() + ".bak"));
            }
            getSharedPreferences("SETTING", MODE_PRIVATE).edit().putBoolean(getVersion(), true).apply();
        }

        if (!APP_DIR.exists()) {
            APP_DIR.mkdir();
        }

        if (!SQLiteManager.SQLITE_FILE.exists()) {
            FileManager.copyFile(getResources().openRawResource(R.raw.city_hall), SQLiteManager.SQLITE_FILE);
        }

        if (!ConfigManager.CONFIG_FILE.exists()) {
            FileManager.copyFile(getResources().openRawResource(R.raw.config), ConfigManager.CONFIG_FILE);
        }

        if (!ConfigManager.LOGO_FILE.exists()) {
            try {
                OutputStream out = new FileOutputStream(ConfigManager.LOGO_FILE);
                Bitmap tmp = BitmapFactory.decodeResource(getResources(), R.drawable.ch_logo);
                tmp.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(ConfigManager.LOGO_FILE));
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (ConfigManager.getInstance(this) == null) {
            Toast.makeText(this, "컨피그 파일이 잘못 되었습니다.", Toast.LENGTH_LONG).show();
            // System.exit(0);
        }
    }

    private String getVersion() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }


}
