package kr.co.raonnetworks.cityhall;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * Created by MoonJongRak on 2016. 2. 17..
 */
public class EducationListActivity extends AppCompatActivity implements View.OnClickListener {

    private final int RESULT_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_list);

        RecyclerView mRecyclerViewEducationList = (RecyclerView) findViewById(R.id.recyclerViewEducationList);
        mRecyclerViewEducationList.setLayoutManager(new LinearLayoutManager(this));


        findViewById(R.id.buttonUploadData).setOnClickListener(this);
        findViewById(R.id.buttonUpdateWork).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_education_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_reg:
                startActivity(new Intent(getContext(), EducationRegActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonUpdateWork:
                break;
            case R.id.buttonUploadData:
                break;
        }
    }

    private Context getContext() {
        return this;
    }
}
