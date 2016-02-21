package kr.co.raonnetworks.cityhall;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by MoonJongRak on 2016. 2. 21..
 */
public class AttendanceCheckDialog extends AppCompatActivity {

    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_attendance_check);

        Tag mTag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);

        TextView mTextViewTitle = (TextView) findViewById(R.id.textViewTitle);
        TextView mTextViewName = (TextView) findViewById(R.id.textViewName);
        TextView mTextViewPosition = (TextView) findViewById(R.id.textViewPosition);
        TextView mTextViewPart = (TextView) findViewById(R.id.textViewPart);


        if (mTag != null) {
            findViewById(R.id.layoutFail).setVisibility(View.VISIBLE);
            findViewById(R.id.layoutOK).setVisibility(View.GONE);
        } else {

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


}
