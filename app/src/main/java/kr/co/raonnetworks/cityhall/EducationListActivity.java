package kr.co.raonnetworks.cityhall;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import kr.co.raonnetworks.cityhall.libs.ConfigManager;
import kr.co.raonnetworks.cityhall.libs.DBManager;
import kr.co.raonnetworks.cityhall.libs.SerialManager;

/**
 * Created by MoonJongRak on 2016. 2. 17..
 */
public class EducationListActivity extends AppCompatActivity implements View.OnClickListener, SerialManager.OnSerialFinishedListener {


    private final int RESULT_CODE = 123;
    private RecyclerEducationListAdapter mRecyclerEducationListAdapter;
    private SerialManager mSerialManager;
    private ConfigManager mConfigManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_list);

        RecyclerView mRecyclerViewEducationList = (RecyclerView) findViewById(R.id.recyclerViewEducationList);
        mRecyclerViewEducationList.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerEducationListAdapter = new RecyclerEducationListAdapter(getContext());
        mRecyclerViewEducationList.setAdapter(mRecyclerEducationListAdapter);

        findViewById(R.id.buttonResetWorker).setOnClickListener(this);
        findViewById(R.id.buttonUpdateWork).setOnClickListener(this);
        findViewById(R.id.buttonUploadEdu).setOnClickListener(this);

        mSerialManager = SerialManager.getInstance(getContext());
        mSerialManager.setTimeout(5000);
        mSerialManager.startSerial();

        mConfigManager = ConfigManager.getInstance(getContext());

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
                mSerialManager.startWorkerUpdate("1=|" + mConfigManager.getTime() + "=|s=|\r", "1=|" + mConfigManager.getTime() + "=|e=|\r");
                break;
            case R.id.buttonResetWorker:
                new AlertDialog.Builder(getContext())
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("알림")
                        .setMessage("직원 정보를 초기화 합니다.\n초기화를 진행하시겠습니까?")
                        .setNegativeButton("취소", null)
                        .setPositiveButton("초기화", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBManager.resetWorker();
                                ConfigManager.getInstance(getContext()).setTime(0); //업데이트 시간 초기화
                                Toast.makeText(getContext(), "직원 정보 초기화 완료!", Toast.LENGTH_LONG).show();
                            }
                        }).show();
                break;
            case R.id.buttonUploadEdu:
                new AlertDialog.Builder(getContext())
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("알림")
                        .setMessage("모든 교육정보를 업로드 합니다..\n업로드를 진행하시겠습니까?")
                        .setNegativeButton("취소", null)
                        .setPositiveButton("업로드", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mSerialManager.startEducationsUpLoad();
                            }
                        }).show();
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
        mSerialManager.setOnSerialFinishedListener(this);
        mRecyclerEducationListAdapter.notifyUpdate();

    }


    private ProgressDialog mProgressDialogDataProcess;

    private void showProgressDialogDataProcess(boolean isDataSend) {
        if (mProgressDialogDataProcess == null) {
            mProgressDialogDataProcess = new ProgressDialog(getContext());
        }
        mProgressDialogDataProcess.setTitle("알림");
        mProgressDialogDataProcess.setIcon(R.mipmap.ic_launcher);
        if (isDataSend) {
            mProgressDialogDataProcess.setMessage("데이터 송신중...");
        } else {
            mProgressDialogDataProcess.setMessage("데이터 수신중...");
        }
        mProgressDialogDataProcess.setCancelable(false);
        mProgressDialogDataProcess.show();
    }

    private void dismissProgressDialogDataProcess() {
        if (mProgressDialogDataProcess != null) {
            mProgressDialogDataProcess.dismiss();
        }
    }


    @Override
    public void onFinished(boolean isSuccess, Exception e) {
        dismissProgressDialogDataProcess();
        if (isSuccess) {
            Toast.makeText(getContext(), "직원정보 업데이트 성공", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "서버 접속시간 초과.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        mSerialManager.stopSerial();
        super.onDestroy();
    }

    @Override
    public void onStart(boolean isDataSend) {
        showProgressDialogDataProcess(isDataSend);
    }
}


