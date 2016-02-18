package kr.co.raonnetworks.cityhall;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final String ROOT_ID = "mox7050";
    private final String ROOT_PASSWD = "123";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //변경해야할것
        ((EditText) findViewById(R.id.editTextId)).setText(ROOT_ID);

        findViewById(R.id.buttonLogin).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:

                String inputPasswd = ((EditText) findViewById(R.id.editTextPasswd)).getText().toString();
                if (inputPasswd.equals(ROOT_PASSWD)) {
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
