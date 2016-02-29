package kr.co.raonnetworks.cityhall;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import kr.co.raonnetworks.cityhall.libs.DBManager;
import kr.co.raonnetworks.cityhall.model.EducationModel;

/**
 * Created by MoonJongRak on 2016. 2. 20..
 */
public class EducationDetailActivity extends AppCompatActivity {

    private static EducationModel mEducationModel;

    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntentNfc;
    private RecyclerAttendanceListAdapter mRecyclerAttendanceListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_detail);

        initNfc();
        updateEduData();

        mRecyclerAttendanceListAdapter = new RecyclerAttendanceListAdapter(getContext(), mEducationModel);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewAttendanceList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mRecyclerAttendanceListAdapter);


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
        mEducationModel = DBManager.getEdu(getContext(), eduId);
        if (mEducationModel == null) {
            Toast.makeText(getContext(), "잘못된 접근입니다.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        ((TextView) findViewById(R.id.textViewName)).setText(mEducationModel.getEduName());
        ((TextView) findViewById(R.id.textViewDataName)).setText("null");
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


}
