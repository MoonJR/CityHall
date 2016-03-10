package kr.co.raonnetworks.cityhall.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;

import kr.co.raonnetworks.cityhall.R;
import kr.co.raonnetworks.cityhall.adapter.RecyclerSelfAttendanceListAdapter;

/**
 * Created by MoonJongRak on 2016. 3. 8..
 */
public class EducationSelfAttendanceActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerSelfAttendanceListAdapter mRecyclerSelfAttendanceListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_self_attendance);

        RecyclerView mRecyclerViewSelfAttendance = (RecyclerView) findViewById(R.id.recyclerViewSelfAttendanceList);
        mRecyclerViewSelfAttendance.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerSelfAttendanceListAdapter = new RecyclerSelfAttendanceListAdapter(getContext(), EducationDetailActivity.getEducationModel(), "");
        mRecyclerViewSelfAttendance.setAdapter(mRecyclerSelfAttendanceListAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_education_self_attendance, menu);
        SearchView mSearchView = (SearchView) menu.findItem(R.id.searchViewActionBar).getActionView();
        mSearchView.setOnQueryTextListener(this);

        return true;
    }

    private Context getContext() {
        return this;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mRecyclerSelfAttendanceListAdapter.notifyUpdate(newText);
        return false;
    }
}
