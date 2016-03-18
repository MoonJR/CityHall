package kr.co.raonnetworks.cityhall.activity;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import kr.co.raonnetworks.cityhall.R;
import kr.co.raonnetworks.cityhall.adapter.RecyclerAttendanceListAdapter;
import kr.co.raonnetworks.cityhall.dialog.AttendanceCheckDialog;
import kr.co.raonnetworks.cityhall.libs.DBManager;
import kr.co.raonnetworks.cityhall.libs.SerialManager;
import kr.co.raonnetworks.cityhall.model.EducationModel;

/**
 * Created by MoonJongRak on 2016. 2. 20..
 */
public class EducationDetailActivity extends ActionBarActivity implements SerialManager.OnSerialFinishedListener, OnClickListener {
    private static EducationModel mEducationModel;

    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntentNfc;
    private RecyclerAttendanceListAdapter mRecyclerAttendanceListAdapter;
    private SerialManager mSerialManager;
    private Timer mTimerCardNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_detail);
        setActionBar();
        findViewById(R.id.buttonSelfAttendance).setOnClickListener(this);

        initNfc();
        updateEduData();

        mRecyclerAttendanceListAdapter = new RecyclerAttendanceListAdapter(mEducationModel);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewAttendanceList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mRecyclerAttendanceListAdapter);

        TimerTask mTimerTaskCardNotify = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView mTextView = ((TextView) findViewById(R.id.textViewCardNotify));

                        if (mTextView.getCurrentTextColor() == getResources().getColor(R.color.colorCardNotify0)) {
                            mTextView.setTextColor(getResources().getColor(R.color.colorCardNotify1));
                        } else {
                            mTextView.setTextColor(getResources().getColor(R.color.colorCardNotify0));
                        }
                    }
                });
            }
        };
        mTimerCardNotify = new Timer();
        mTimerCardNotify.schedule(mTimerTaskCardNotify, 0, 800);

    }


    private void setActionBar() {
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException ignore) {

        }
    }

    private void initNfc() {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(getContext());

        if (mNfcAdapter == null) {
            Toast.makeText(getContext(), "해당 기기는 NFC를 이용할 수 없어 출석체크가 불가능 합니다.", Toast.LENGTH_LONG).show();
        } else {
            mPendingIntentNfc = PendingIntent.getActivity(getContext(), 0, new Intent(getContext(), AttendanceCheckDialog.class), PendingIntent.FLAG_UPDATE_CURRENT);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_education_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("알림");
                if (mRecyclerAttendanceListAdapter.getItemCount() != 0) {
                    builder.setMessage("출석 내역이 있는 데이터는 삭제할 수 없습니다.")
                            .setPositiveButton("확인", null);
                } else {
                    builder.setMessage("현재의 교육 데이터를 삭제하시겠습니까?")
                            .setNegativeButton("취소", null)
                            .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DBManager.deleteEdu(mEducationModel);
                                    Toast.makeText(getContext(), "교육을 삭제하였습니다.", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });
                }
                builder.show();
                break;
            case R.id.action_upload:
                builder = new AlertDialog.Builder(getContext())
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("알림")
                        .setNegativeButton("취소", null)
                        .setPositiveButton("업로드", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mSerialManager.startEducationUpLoad(mEducationModel);
                            }
                        });
                if (mEducationModel.getEduUploaded() == EducationModel.FLAG_EDUCATION_UPLOADED) {
                    builder.setMessage("이미 업로드 된 교육입니다.\n다시 업로드 하시겠습니까?");
                } else {
                    builder.setMessage("교육정보를 업로드 합니다.\n업로드 하시겠습니까?");
                }
                builder.show();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            if (!mNfcAdapter.isEnabled()) {
                new AlertDialog.Builder(this)
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("알림")
                        .setMessage("해당 디바이스의 NFC읽기 모드를 켜야 출석체크가 가능합니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
                                } else {
                                    startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                                }
                            }
                        }).show();
            } else {
                mNfcAdapter.enableForegroundDispatch(this, mPendingIntentNfc, null, null);
            }
        }

        mRecyclerAttendanceListAdapter.notifyUpdate();
        ((TextView) findViewById(R.id.textViewAttendanceCount)).setText(Integer.toString(mRecyclerAttendanceListAdapter.getItemCount()));
        mSerialManager = SerialManager.getInstance(getContext());
        mSerialManager.setOnSerialFinishedListener(this);
        updateEduData();

    }

    @Override
    protected void onPause() {
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }
        super.onPause();
    }

    private Context getContext() {
        return this;
    }

    private void updateEduData() {
        String eduId = getIntent().getStringExtra("eduId");
        mEducationModel = DBManager.getEdu(eduId);
        if (mEducationModel == null) {
            Toast.makeText(getContext(), "잘못된 접근입니다.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        ((TextView) findViewById(R.id.textViewName)).setText(mEducationModel.getEduName());
        ((TextView) findViewById(R.id.textViewLocation)).setText(mEducationModel.getEduLocation());
        ((TextView) findViewById(R.id.textViewPart)).setText(mEducationModel.getEduPart());
        ((TextView) findViewById(R.id.textViewStart)).setText(mEducationModel.getEduStartString());
        ((TextView) findViewById(R.id.textViewEnd)).setText(mEducationModel.getEduEndString());
        ((TextView) findViewById(R.id.textViewTarget)).setText(mEducationModel.getEduTargetString());
        ((TextView) findViewById(R.id.textViewType)).setText(mEducationModel.getEduType());

    }

    public static EducationModel getEducationModel() {
        return mEducationModel;
    }


    @Override
    public void onFinished(boolean isSuccess, boolean isDataSend, Exception e) {
        if (isSuccess) {
            if (isDataSend) {
                DBManager.uploadEducation(mEducationModel);
                Toast.makeText(getContext(), "교육정보 업로드 성공.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "교육정보 업로드 실패.", Toast.LENGTH_LONG).show();
        }


        updateEduData();
        dismissProgressDialogDataProcess();
    }

    @Override
    public void onStart(boolean isDataSend) {
        showProgressDialogDataProcess(isDataSend);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSelfAttendance:
                startActivity(new Intent(getContext(), EducationSelfAttendanceActivity.class));
                break;
        }

    }

    @Override
    protected void onDestroy() {
        mTimerCardNotify.cancel();
        super.onDestroy();
    }
}
