package kr.co.raonnetworks.cityhall;

import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import kr.co.raonnetworks.cityhall.libs.ConfigManager;
import kr.co.raonnetworks.cityhall.libs.DBManager;
import kr.co.raonnetworks.cityhall.model.EducationModel;
import kr.co.raonnetworks.cityhall.model.WorkerModel;

/**
 * Created by MoonJongRak on 2016. 2. 21..
 */
public class AttendanceCheckDialog extends AppCompatActivity {

    private Handler mHandler = new Handler();
    private ConfigManager mConfigManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_attendance_check);


        mConfigManager = ConfigManager.getInstance(getContext());

        Tag mTag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);

        TextView mTextViewTitle = (TextView) findViewById(R.id.textViewTitle);
        TextView mTextViewName = (TextView) findViewById(R.id.textViewName);
        TextView mTextViewPart = (TextView) findViewById(R.id.textViewPart);


        if (mTag == null) {
            findViewById(R.id.layoutFail).setVisibility(View.VISIBLE);
            findViewById(R.id.layoutOK).setVisibility(View.GONE);
        } else {
            findViewById(R.id.layoutFail).setVisibility(View.GONE);
            findViewById(R.id.layoutOK).setVisibility(View.VISIBLE);
            byte[] tagIdTmp = mTag.getId();
            if (mConfigManager.isTagReverse()) {
                for (int i = 0; i < tagIdTmp.length / 2; i++) {
                    byte temp = tagIdTmp[i];
                    tagIdTmp[i] = tagIdTmp[tagIdTmp.length - i - 1];
                    tagIdTmp[tagIdTmp.length - i - 1] = temp;
                }
            }

            long tagId = byteArrayToLong(tagIdTmp);

            EducationModel mEducationModel = EducationDetailActivity.getEducationModel();
            WorkerModel mWorkerModel = DBManager.getWorker(getContext(), tagId);
            if (mWorkerModel == null) {
                mWorkerModel = new WorkerModel();
                mWorkerModel.setWorkerCard(tagId);
                mWorkerModel.setWorkerId(null);
            }

            if (mWorkerModel.getWorkerId() == null) {
                mTextViewName.setText("등록되지 않은 사용자.");
            } else {
                mTextViewName.setText(mWorkerModel.getWorkerName());
            }
            mTextViewPart.setText(mWorkerModel.getWorkerPart());

            if (DBManager.addAttendance(getContext(), mEducationModel, mWorkerModel)) {
                mTextViewTitle.setText("카드 인식 완료.");
            } else {
                mTextViewTitle.setText("이미 출석처리 되었습니다.");
            }
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2000);
    }


    public static String byteArrayToHexString(byte[] b) {
        String data = "";
        for (byte aB : b) {
            data += Integer.toHexString((aB >> 4) & 0xf);
            data += Integer.toHexString(aB & 0xf);
        }
        return data;
    }

    public static long byteArrayToLong(byte[] bytes) {

        final byte[] change = new byte[8];
        for (int i = 0; i < 8; i++) {
            change[i] = (byte) 0x00;
        }

        for (int i = 0; i < bytes.length; i++) {
            change[7 - i] = bytes[bytes.length - 1 - i];
        }
        ByteBuffer byte_buf = ByteBuffer.wrap(change);
        byte_buf.order(ByteOrder.BIG_ENDIAN);
        return byte_buf.getLong();
    }

    private Context getContext() {
        return this;
    }


}
