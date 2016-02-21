package kr.co.raonnetworks.cityhall.test;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import kr.co.raonnetworks.cityhall.EducationListActivity;
import kr.co.raonnetworks.cityhall.LoginActivity;
import kr.co.raonnetworks.cityhall.R;


/**
 * Created by MoonJongRak on 2016. 2. 17..
 */
public class TestActivity extends LoginActivity {
//
//    TextView mTextView;
//    NfcAdapter mNfcAdapter; // NFC 어댑터
//    PendingIntent mPendingIntent; // 수신받은 데이터가 저장된 인텐트
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_nfc_test);
//        mTextView = (TextView) findViewById(R.id.textView_explanation);
//        // NFC 어댑터를 구한다
//        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
//        // NFC 어댑터가 null 이라면 칩이 존재하지 않는 것으로 간주
//        if (mNfcAdapter == null) {
//            mTextView.setText("This phone is not NFC enable.");
//            return;
//        }
//
//
//        mTextView.setText("Scan a NFC tag");
//
//        // NFC 데이터 활성화에 필요한 인텐트를 생성
//        Intent intent = new Intent(this, EducationListActivity.class);
//        mPendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//    }
//
//    public void onResume() {
//        super.onResume();
//        // 앱이 실행될때 NFC 어댑터를 활성화 한다
//        Log.d("moon", "onResume");
//        if (mNfcAdapter != null) {
//            if (!mNfcAdapter.isEnabled()) {
//                new AlertDialog.Builder(this)
//                        .setIcon(R.mipmap.ic_launcher)
//                        .setTitle("알림")
//                        .setMessage("해당 디바이스의 NFC읽기 모드를 켜야 출석체크가 가능합니다.")
//                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                                    startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
//                                } else {
//                                    startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
//                                }
//                            }
//                        }).show();
//            } else {
//                mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
//            }
//        }
//
//
//    }
//
//    public void onPause() {
//        super.onPause();
//        Log.d("moon", "onPause");
//
//        // 앱이 종료될때 NFC 어댑터를 비활성화 한다
//        if (mNfcAdapter != null)
//            mNfcAdapter.disableForegroundDispatch(this);
//    }
//
//
//    // NFC 태그 정보 수신 함수. 인텐트에 포함된 정보를 분석해서 화면에 표시
//    @Override
//    public void onNewIntent(Intent intent) {
//        // 인텐트에서 액션을 추출
//        String action = intent.getAction();
//        // 인텐트에서 태그 정보 추출
//        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//        mTextView.setText(byteArrayToHexString(tag.getId()));
//    }


}
