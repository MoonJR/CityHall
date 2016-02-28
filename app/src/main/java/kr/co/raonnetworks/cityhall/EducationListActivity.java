package kr.co.raonnetworks.cityhall;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import kr.co.raonnetworks.cityhall.libs.ConfigManager;
import kr.co.raonnetworks.cityhall.libs.DBManager;
import kr.co.raonnetworks.cityhall.model.AttendanceModel;
import kr.co.raonnetworks.cityhall.model.EducationModel;
import kr.co.raonnetworks.cityhall.model.WorkerModel;
import kr.co.raonnetworks.cityhall.test.FT311UARTInterface;

/**
 * Created by MoonJongRak on 2016. 2. 17..
 */
public class EducationListActivity extends AppCompatActivity implements View.OnClickListener {

    private final int RESULT_CODE = 123;
    private FT311UARTInterface serialInterface;
    private byte[] writeBuffer;
    private byte[] readBuffer;
    private int[] actualNumBytes;

    private RecyclerEducationListAdapter mRecyclerEducationListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_list);

        RecyclerView mRecyclerViewEducationList = (RecyclerView) findViewById(R.id.recyclerViewEducationList);
        mRecyclerViewEducationList.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerEducationListAdapter = new RecyclerEducationListAdapter(getContext());
        mRecyclerViewEducationList.setAdapter(mRecyclerEducationListAdapter);

        findViewById(R.id.buttonUploadData).setOnClickListener(this);
        findViewById(R.id.buttonUpdateWork).setOnClickListener(this);


        this.writeBuffer = new byte[64];
        this.readBuffer = new byte[4096];
        this.actualNumBytes = new int[1];
        this.serialInterface = new FT311UARTInterface(getContext(), null);
        this.serialInterface.ResumeAccessory();

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
                new WorkerUpdateThread("1=|0=|s=|\r", "1=|0=|e=|\r").start();
                break;
            case R.id.buttonUploadData:
                new EducationUpLoadThread().start();
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

    private void writeData(FT311UARTInterface serial, String query) {

        for (int i = 0; i < query.length(); ) {
            int offset = i + 50;
            if (offset > query.length()) {
                offset = query.length();
            }
            String queryTmp = query.substring(i, offset);
            Log.d("moon", "test:" + queryTmp);

            int numBytes = queryTmp.length();
            for (int j = 0; j < numBytes; j++) {
                writeBuffer[j] = (byte) queryTmp.charAt(j);
            }
            serial.SendData(numBytes, writeBuffer);

            i = offset;
        }

    }

    private String readData(FT311UARTInterface serial) throws TimeoutException {

        long startTime = System.currentTimeMillis();
        StringBuilder builder = new StringBuilder();
        while (true) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {
            }
            int status = serial.ReadData(4096, readBuffer, actualNumBytes);
            if (status == 0x00 && actualNumBytes[0] > 0) {
                startTime = System.currentTimeMillis();
                final String tmp = new String(readBuffer, 0, actualNumBytes[0]);
                builder.append(tmp);
                if (builder.toString().contains("e=|")) {
                    return builder.toString().trim();
                }
            }

            if (System.currentTimeMillis() - startTime > 10000) {
                throw new TimeoutException("수신 시간 초과");
            }
        }
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

    private void upDateWorker(String data) {
        String[] dataTmp = data.split("\r");

        for (int i = 0; i < dataTmp.length; i++) {
            String[] dataTmp2 = dataTmp[i].split("=\\|");
            if (i == 0) {
                ConfigManager.getInstance(getContext()).setTime(dataTmp2[3]);
            } else if (i == dataTmp.length - 1) {
                break;
            } else {
                for (int j = 0; j < dataTmp2.length; j++) {
                    try {
                        dataTmp2[j] = URLDecoder.decode(dataTmp2[j], "EUC-KR");
                    } catch (UnsupportedEncodingException ignored) {
                    }
                }

                WorkerModel mWorkerModelTmp = new WorkerModel();
                mWorkerModelTmp.setWorkerId(dataTmp2[0]);
                mWorkerModelTmp.setWorkerPart(dataTmp2[1]);
                mWorkerModelTmp.setWorkerName(dataTmp2[2]);
                if (dataTmp2[3].equals("-")) {
                    mWorkerModelTmp.setWorkerCard(-1);
                } else {
                    mWorkerModelTmp.setWorkerCard(Long.parseLong(dataTmp2[3]));
                }

                if (dataTmp2[4].equals("y")) {
                    mWorkerModelTmp.setWorkerStatus(1);
                } else {
                    mWorkerModelTmp.setWorkerStatus(0);
                }
                DBManager.addWorker(getContext(), mWorkerModelTmp);
            }
        }
    }

    @Override
    protected void onDestroy() {
        serialInterface.DestroyAccessory(false);
        super.onDestroy();
    }

    private class WorkerUpdateThread extends Thread {

        private String startQuery, endQuery;

        private WorkerUpdateThread(String startQuery, String endQuery) {
            this.startQuery = startQuery;
            this.endQuery = endQuery;
        }

        @Override
        public void run() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showProgressDialogDataProcess(false);
                }
            });

            writeData(serialInterface, startQuery);
            try {
                String updateData = readData(serialInterface);
                upDateWorker(updateData);
            } catch (TimeoutException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "수신 시간 초과", Toast.LENGTH_LONG).show();
                    }
                });
                e.printStackTrace();
            }
            writeData(serialInterface, endQuery);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissProgressDialogDataProcess();
                }
            });

        }
    }

    private class EducationUpLoadThread extends Thread {


        ArrayList<EducationModel> mEducationModels;

        private EducationUpLoadThread() {
            mEducationModels = DBManager.getEdu(getContext());
        }

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showProgressDialogDataProcess(true);
                }
            });

            for (EducationModel mEducationModel : mEducationModels) {
                ArrayList<AttendanceModel> mAttendanceModels = DBManager.getAttendance(getContext(), mEducationModel);

                StringBuilder builder = new StringBuilder();
                builder.append(getEducationStartQuery(mEducationModel));
                for (AttendanceModel mAttendanceModel : mAttendanceModels) {
                    builder.append(getAttendanceQuery(mAttendanceModel));
                }
                builder.append(getEducationEndQuery(mEducationModel));
                writeData(serialInterface, builder.toString());
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissProgressDialogDataProcess();
                }
            });


        }

        private String getEducationStartQuery(EducationModel mEducationModel) {
            try {
                return "2=|" + mEducationModel.getEduAttendanceCount() + "=|s=|" + URLEncoder.encode(mEducationModel.getEduName(), "EUC-KR") + "=|" + URLEncoder.encode(mEducationModel.getEduLocation(), "EUC-KR") + "=|" + URLEncoder.encode(mEducationModel.getEduPart(), "EUC-KR") + "=|" + mEducationModel.getEduStart().getTime() + "=|" + mEducationModel.getEduEnd().getTime() + "=|" + (mEducationModel.getEduEnd().getTime() - mEducationModel.getEduStart().getTime()) + "=|" + URLEncoder.encode(mEducationModel.getEduTargetString(), "EUC-KR") + "=|" + URLEncoder.encode(mEducationModel.getEduType(), "EUC-KR") + "=|\r";
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }

        private String getEducationEndQuery(EducationModel mEducationModel) {
            return "2=|" + mEducationModel.getEduAttendanceCount() + "=|e=|\r";
        }

        private String getAttendanceQuery(AttendanceModel mAttendanceModel) {
            if (mAttendanceModel.getWorkerId() != null) {
                return "mem=|" + mAttendanceModel.getWorkerId() + "=|" + mAttendanceModel.getAttendanceTime().getTime() + "=|\r";
            } else {
                return "card=|" + mAttendanceModel.getWorkerCard() + "=|" + mAttendanceModel.getAttendanceTime().getTime() + "=|\r";
            }
        }


    }
}
