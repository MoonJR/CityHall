package kr.co.raonnetworks.cityhall.test;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.Arrays;

import kr.co.raonnetworks.cityhall.EducationRegActivity;
import kr.co.raonnetworks.cityhall.R;

/**
 * Created by MoonJongRak on 2016. 2. 17..
 */
public class TestActivity extends EducationRegActivity {
//    public static final String TAG = "NfcDemo";
//
//    private TextView mTextView;
//    private NfcAdapter mNfcAdapter;
//    private PendingIntent mPendingIntent;
//    private IntentFilter[] mIntentFilters;
//    private String[][] mNFCTechLists;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_nfc_test);
//
//        mTextView = (TextView) findViewById(R.id.textView_explanation);
//
//        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
//
//        if (mNfcAdapter == null) {
//            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
//            finish();
//            return;
//        }
//
//        if (!mNfcAdapter.isEnabled()) {
//            mTextView.setText("NFC is disabled.");
//        } else {
//            mTextView.setText("시작");
//        }
//
//        setupNFCDispacher();
//    }
//
//    private void setupNFCDispacher() {
//        mPendingIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
//
//        // set an intent filter for all MIME data
//        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
//        try {
//            ndefIntent.addDataType("*/*");
//            mIntentFilters = new IntentFilter[]{ndefIntent};
//        } catch (Exception e) {
//            Log.e("TagDispatch", e.toString());
//        }
//
//        mNFCTechLists = new String[][]{new String[]{NfcF.class.getName()}};
//
//
//    }
//
//
//    @Override
//    public void onNewIntent(Intent intent) {
//        String action = intent.getAction();
//        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//
//        String s = action + "\n\n" + tag.toString();
//
//        // parse through all NDEF messages and their records and pick text type only
//        Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//        if (data != null) {
//            try {
//                for (int i = 0; i < data.length; i++) {
//                    NdefRecord[] recs = ((NdefMessage) data[i]).getRecords();
//                    for (int j = 0; j < recs.length; j++) {
//                        if (recs[j].getTnf() == NdefRecord.TNF_WELL_KNOWN &&
//                                Arrays.equals(recs[j].getType(), NdefRecord.RTD_TEXT)) {
//                            byte[] payload = recs[j].getPayload();
//
//                            String textEncoding;
//                            if ((payload[0] & 0200) == 0) {
//                                textEncoding = "UTF-8";
//                            } else {
//                                textEncoding = "UTF-16";
//                            }
//                            int langCodeLen = payload[0] & 0077;
//
//                            s += ("\n\nNdefMessage[" + i + "], NdefRecord[" + j + "]:\n\"" +
//                                    new String(payload, langCodeLen + 1, payload.length - langCodeLen - 1,
//                                            textEncoding) + "\"");
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                Log.e("TagDispatch", e.toString());
//            }
//        }
//
//        mTextView.setText(s);
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        if (mNfcAdapter != null)
//            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//
//        if (mNfcAdapter != null)
//            mNfcAdapter.disableForegroundDispatch(this);
//    }
//
//    private Context getContext() {
//        return this;
//    }
}
