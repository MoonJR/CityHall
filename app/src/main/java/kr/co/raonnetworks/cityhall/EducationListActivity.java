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

import java.util.ArrayList;

import kr.co.raonnetworks.cityhall.libs.DBManager;
import kr.co.raonnetworks.cityhall.model.EducationModel;

/**
 * Created by MoonJongRak on 2016. 2. 17..
 */
public class EducationListActivity extends AppCompatActivity implements View.OnClickListener {

    private final int RESULT_CODE = 123;

    private RecyclerEducationListAdapter mRecyclerEducationListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_list);

        RecyclerView mRecyclerViewEducationList = (RecyclerView) findViewById(R.id.recyclerViewEducationList);
        mRecyclerViewEducationList.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerEducationListAdapter = new RecyclerEducationListAdapter(getContext(), new ArrayList<EducationModel>());
        mRecyclerViewEducationList.setAdapter(mRecyclerEducationListAdapter);

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
                startActivityForResult(new Intent(getContext(), EducationRegActivity.class), RESULT_CODE);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_CODE) {
                mRecyclerEducationListAdapter.notifyUpdate();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
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

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecyclerEducationListAdapter.notifyUpdate();
    }

}
