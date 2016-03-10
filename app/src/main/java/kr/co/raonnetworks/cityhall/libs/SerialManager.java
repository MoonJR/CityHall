package kr.co.raonnetworks.cityhall.libs;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import kr.co.raonnetworks.cityhall.model.AttendanceModel;
import kr.co.raonnetworks.cityhall.model.EducationModel;
import kr.co.raonnetworks.cityhall.model.WorkerModel;

/**
 * Created by MoonJongRak on 2016. 2. 28..
 */
public class SerialManager extends FT311UARTInterface {
    private final String FLAG_CHUNK_MIDDLE = "[end]";
    private final String FLAG_CHUNK_END = "[[end]]";


    private static SerialManager manager;

    private OnSerialFinishedListener listener;
    private Context context;
    private long timeout;

    private byte[] writeBuffer;
    private byte[] readBuffer;
    private int[] actualNumBytes;

    public static SerialManager getInstance(Context context) {
        if (manager == null) {
            manager = new SerialManager(context);
        }
        return manager;
    }

    private SerialManager(Context context) {
        super(context, null);
        this.writeBuffer = new byte[64];
        this.readBuffer = new byte[4096];
        this.actualNumBytes = new int[1];
        this.timeout = 10000;
        this.context = context;

    }

    public void startSerial() {
        this.ResumeAccessory();
    }

    public void stopSerial() {
        this.DestroyAccessory(false);
        manager = null;
    }

    public void setOnSerialFinishedListener(OnSerialFinishedListener listener) {
        this.listener = listener;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getTimeout() {
        return this.timeout;
    }


    public void startWorkerUpdate(String startQuery, String endQuery) {
        new WorkerUpdateThread(startQuery, endQuery).start();
    }

    public void startEducationUpLoad(EducationModel mEducationModel) {
        new EducationUpLoadThread(mEducationModel).start();
    }

    public void startEducationsUpLoad() {
        new EducationsUpLoadThread().start();
    }

    private void writeData(String query) {

        for (int i = 0; i < query.length(); ) {
            int offset = i + 50;
            if (offset > query.length()) {
                offset = query.length();
            }
            String queryTmp = query.substring(i, offset);

            int numBytes = queryTmp.length();
            for (int j = 0; j < numBytes; j++) {
                writeBuffer[j] = (byte) queryTmp.charAt(j);
            }
            this.SendData(numBytes, writeBuffer);
            i = offset;
        }
    }

    private String readData() throws TimeoutException {

        long startTime = System.currentTimeMillis();
        StringBuilder builder = new StringBuilder();
        while (true) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {
            }
            int status = this.ReadData(4096, readBuffer, actualNumBytes);
            if (status == 0x00 && actualNumBytes[0] > 0) {
                startTime = System.currentTimeMillis();
                final String tmp = new String(readBuffer, 0, actualNumBytes[0]);
                builder.append(tmp);
                if (builder.toString().contains("e=|")) {
                    return builder.toString().trim();
                }
            }

            if (System.currentTimeMillis() - startTime > getTimeout()) {
                throw new TimeoutException("수신 시간 초과");
            }
        }
    }

    private void upDateWorker(String data) {
        String[] dataTmp = data.split("\r");

        //직원코드=|부서명=|이름=|카드번호=|구분번호=|상태 + cr
        for (int i = 0; i < dataTmp.length; i++) {
            String[] dataTmp2 = dataTmp[i].split("=\\|");
            if (i == 0) {
                ConfigManager.getInstance(context).setTime(dataTmp2[3]);
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


                mWorkerModelTmp.setWorkerDivision(dataTmp2[4]);

                if (dataTmp2[5].equals("y")) {
                    mWorkerModelTmp.setWorkerStatus(1);
                } else {
                    mWorkerModelTmp.setWorkerStatus(0);
                }
                DBManager.addWorker(mWorkerModelTmp);
            }
        }
    }

    private class WorkerUpdateThread extends Thread {

        private String startQuery, endQuery;

        private WorkerUpdateThread(String startQuery, String endQuery) {
            this.startQuery = startQuery;
            this.endQuery = endQuery;
        }

        @Override
        public void run() {
            if (listener != null) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onStart(false);
                    }
                });
            }
            writeData(startQuery);
            try {
                String updateData = readData();
                upDateWorker(updateData);
            } catch (final TimeoutException e) {
                if (listener != null) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFinished(false, e);
                        }
                    });
                }
            }
            writeData(endQuery);
            if (listener != null) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFinished(true, null);
                    }
                });
            }
        }

    }

    private class EducationUpLoadThread extends Thread {
        private EducationModel mEducationModel;
        private SimpleDateFormat mSimpleDateFormat;

        private EducationUpLoadThread(EducationModel mEducationModel) {
            this.mSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            this.mEducationModel = mEducationModel;
        }

        @Override
        public void run() {

            if (listener != null) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onStart(true);
                    }
                });
            }

            ArrayList<AttendanceModel> mAttendanceModels = DBManager.getAttendance(mEducationModel);

            StringBuilder builder = new StringBuilder();
            builder.append(getEducationStartQuery(mEducationModel));
            for (AttendanceModel mAttendanceModel : mAttendanceModels) {
                builder.append(getAttendanceQuery(mAttendanceModel));
            }
            builder.append(getEducationEndQuery(mEducationModel));
            writeData(builder.toString());
            try {
                readData();
            } catch (final TimeoutException e) {
                e.printStackTrace();
                if (listener != null) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFinished(false, e);
                        }
                    });
                }
            }

            if (listener != null) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFinished(true, null);
                    }
                });
            }
        }

        private String getEducationStartQuery(EducationModel mEducationModel) {
            try {
                return "2=|" + mEducationModel.getEduAttendanceCount() + "=|s=|" + URLEncoder.encode(mEducationModel.getEduName(), "EUC-KR") + "=|" + URLEncoder.encode(mEducationModel.getEduLocation(), "EUC-KR") + "=|" + URLEncoder.encode(mEducationModel.getEduPart(), "EUC-KR") + "=|" + mSimpleDateFormat.format(mEducationModel.getEduStart()) + "=|" + mSimpleDateFormat.format(mEducationModel.getEduEnd()) + "=|" + URLEncoder.encode(mEducationModel.getEduTime(), "EUC-KR") + "=|" + URLEncoder.encode(mEducationModel.getEduTargetString(), "EUC-KR") + "=|" + URLEncoder.encode(mEducationModel.getEduType(), "EUC-KR") + "=|" + FLAG_CHUNK_MIDDLE;
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }

        private String getEducationEndQuery(EducationModel mEducationModel) {
            return "2=|" + mEducationModel.getEduAttendanceCount() + "=|e=|" + FLAG_CHUNK_END;
        }

        private String getAttendanceQuery(AttendanceModel mAttendanceModel) {
            if (mAttendanceModel.getWorkerId() != null) {
                return "mem=|" + mAttendanceModel.getWorkerId() + "=|" + mSimpleDateFormat.format(mAttendanceModel.getAttendanceTime()) + "=|" + FLAG_CHUNK_MIDDLE;
            } else {
                return "card=|" + mAttendanceModel.getWorkerCard() + "=|" + mSimpleDateFormat.format(mAttendanceModel.getAttendanceTime()) + "=|" + FLAG_CHUNK_MIDDLE;
            }
        }


    }

    private class EducationsUpLoadThread extends Thread {


        ArrayList<EducationModel> mEducationModels;

        private EducationsUpLoadThread() {
            mEducationModels = DBManager.getEdu();
        }

        @Override
        public void run() {

            if (listener != null) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onStart(true);
                    }
                });
            }
            for (int i = 0; i < mEducationModels.size(); i++) {
                StringBuilder builder = new StringBuilder();
                EducationModel mEducationModel = mEducationModels.get(i);
                ArrayList<AttendanceModel> mAttendanceModels = DBManager.getAttendance(mEducationModel);

                builder.append(getEducationStartQuery(mEducationModel));
                for (AttendanceModel mAttendanceModel : mAttendanceModels) {
                    builder.append(getAttendanceQuery(mAttendanceModel));
                }
                if (mEducationModels.size() - 1 <= i) {
                    builder.append(getEducationEndQuery(mEducationModel, true));
                } else {
                    builder.append(getEducationEndQuery(mEducationModel, false));
                }

                writeData(builder.toString());
            }

            try {
                readData();
            } catch (final TimeoutException e) {
                e.printStackTrace();
                if (listener != null) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFinished(false, e);
                        }
                    });
                }
            }

            if (listener != null) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFinished(true, null);
                    }
                });
            }
        }

        private String getEducationStartQuery(EducationModel mEducationModel) {
            try {
                return "2=|" + mEducationModel.getEduAttendanceCount() + "=|s=|" + URLEncoder.encode(mEducationModel.getEduName(), "EUC-KR") + "=|" + URLEncoder.encode(mEducationModel.getEduLocation(), "EUC-KR") + "=|" + URLEncoder.encode(mEducationModel.getEduPart(), "EUC-KR") + "=|" + mEducationModel.getEduStart().getTime() + "=|" + mEducationModel.getEduEnd().getTime() + "=|" + (mEducationModel.getEduEnd().getTime() - mEducationModel.getEduStart().getTime()) + "=|" + URLEncoder.encode(mEducationModel.getEduTargetString(), "EUC-KR") + "=|" + URLEncoder.encode(mEducationModel.getEduType(), "EUC-KR") + "=|" + FLAG_CHUNK_MIDDLE;
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }

        private String getEducationEndQuery(EducationModel mEducationModel, boolean isEnd) {
            String tmp = "2=|" + mEducationModel.getEduAttendanceCount() + "=|e=|";
            return isEnd ? tmp + FLAG_CHUNK_END : tmp + FLAG_CHUNK_MIDDLE;
        }

        private String getAttendanceQuery(AttendanceModel mAttendanceModel) {
            if (mAttendanceModel.getWorkerId() != null) {
                return "mem=|" + mAttendanceModel.getWorkerId() + "=|" + mAttendanceModel.getAttendanceTime().getTime() + "=|" + FLAG_CHUNK_MIDDLE;
            } else {
                return "card=|" + mAttendanceModel.getWorkerCard() + "=|" + mAttendanceModel.getAttendanceTime().getTime() + "=|" + FLAG_CHUNK_MIDDLE;
            }
        }


    }


    public interface OnSerialFinishedListener {
        void onFinished(boolean isSuccess, Exception e);

        void onStart(boolean isDataSend);

    }
}







