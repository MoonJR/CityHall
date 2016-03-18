package kr.co.raonnetworks.cityhall.dialog;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.List;

import kr.co.raonnetworks.cityhall.activity.LoginActivity;
import kr.co.raonnetworks.cityhall.libs.SerialManager;
import kr.co.raonnetworks.cityhall.test.TestActivity_Publisher;

/**
 * Created by MoonJongRak on 2016. 3. 12..
 */
public class SerialCheckDialog extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> Info = am.getRunningTasks(1);
        ComponentName baseActivity = Info.get(0).baseActivity;
        String baseActivityName = baseActivity.getClassName();
        if (baseActivityName.equals(getClass().getName())) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            if (!baseActivityName.equals(TestActivity_Publisher.class.getName())) {
                SerialManager.getInstance(this).stopSerial();
                SerialManager.getInstance(this).startSerial();
            }
            finish();
        }
    }
}
