package kr.co.raonnetworks.cityhall;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import kr.co.raonnetworks.cityhall.libs.ConfigManager;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    ConfigManager mConfigManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mConfigManager = ConfigManager.getInstance(getContext());
        //변경해야할것
        ((EditText) findViewById(R.id.editTextId)).setText(mConfigManager.getId());

        findViewById(R.id.buttonLogin).setOnClickListener(this);

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
}
