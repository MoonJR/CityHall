package kr.co.raonnetworks.cityhall.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import kr.co.raonnetworks.cityhall.R;
import kr.co.raonnetworks.cityhall.libs.ConfigManager;

public class LoginActivity extends Activity implements View.OnClickListener, TextView.OnEditorActionListener {

    ConfigManager mConfigManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mConfigManager = ConfigManager.getInstance(getContext());

        //변경해야할것
        setTitle(mConfigManager.getLoginTitle());
        ((TextView) findViewById(R.id.textViewTitle)).setText(mConfigManager.getCityTitle());
        ((EditText) findViewById(R.id.editTextId)).setText(mConfigManager.getId());
        if (!ConfigManager.LOGO_FILE.exists()) {
            Toast.makeText(getContext(), "로고 파일이 존재하지 않습니다.", Toast.LENGTH_LONG).show();
        } else {
            ((ImageView) findViewById(R.id.imageViewLogo)).setImageBitmap(BitmapFactory.decodeFile(ConfigManager.LOGO_FILE.getAbsolutePath()));
        }
        findViewById(R.id.buttonLogin).setOnClickListener(this);
        ((EditText) findViewById(R.id.editTextPasswd)).setOnEditorActionListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                String inputPasswd = ((EditText) findViewById(R.id.editTextPasswd)).getText().toString();
                if (inputPasswd.equals(mConfigManager.getPasswd())) {
                    startEducationListActivity();
                } else {
                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private Context getContext() {
        return this;
    }

    private void startEducationListActivity() {
        startActivity(new Intent(getContext(), EducationListActivity.class));
        finish();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (v.getId()) {
            case R.id.editTextPasswd:
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    findViewById(R.id.buttonLogin).callOnClick();
                }
                break;
        }
        return false;
    }
}
