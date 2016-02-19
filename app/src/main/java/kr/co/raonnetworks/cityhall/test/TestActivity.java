package kr.co.raonnetworks.cityhall.test;

import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

import java.nio.charset.Charset;

import kr.co.raonnetworks.cityhall.LoginActivity;
import kr.co.raonnetworks.cityhall.R;

/**
 * Created by MoonJongRak on 2016. 2. 17..
 */
public class TestActivity extends LoginActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_nfc_test);
//
//    }
//
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(getContext());
//
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//
//        Tag myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//
//        Ndef ndefTag = Ndef.get(myTag);
//
//        int size = ndefTag.getMaxSize();
//        boolean writable = ndefTag.isWritable();
//        String type = ndefTag.getType();
//        String id = byteArrayToHexString(myTag.getId());
//
//
//        super.onNewIntent(intent);
//    }
//
//    public String byteArrayToHexString(byte[] b) {
//        int len = b.length;
//        String data = "";
//
//        for (int i = 0; i < len; i++) {
//            data += Integer.toHexString((b[i] >> 4) & 0xf);
//            data += Integer.toHexString(b[i] & 0xf);
//        }
//        return data;
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//
//        if (messages == null) return;
//
//        for (int i = 0; i < messages.length; i++)
//            setReadTagData((NdefMessage)messages[0]);
//    }
//
//    public void setReadTagData(NdefMessage ndefmsg) {
//        if(ndefmsg == null ) {
//            return ;
//        }
//        String msgs = "";
//        msgs += ndefmsg.toString() + "\n";
//        NdefRecord [] records = ndefmsg.getRecords() ;
//        for(NdefRecord rec : records) {
//            byte [] payload = rec.getPayload() ;
//            String textEncoding = "UTF-8" ;
//            if(payload.length > 0)
//                textEncoding = ( payload[0] & 0200 ) == 0 ? "UTF-8" : "UTF-16";
//
//            Short tnf = rec.getTnf();
//            String type = String.valueOf(rec.getType());
//            String payloadStr = new String(rec.getPayload(), Charset.forName(textEncoding));
//        }
//    }
//
//    private Context getContext() {
//        return this;
//    }
}
