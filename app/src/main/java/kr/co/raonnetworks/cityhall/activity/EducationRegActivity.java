package kr.co.raonnetworks.cityhall.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import kr.co.raonnetworks.cityhall.R;
import kr.co.raonnetworks.cityhall.libs.CheckableButton;
import kr.co.raonnetworks.cityhall.libs.ConfigManager;
import kr.co.raonnetworks.cityhall.libs.DBManager;
import kr.co.raonnetworks.cityhall.model.EducationModel;

/**
 * Created by MoonJongRak on 2016. 2. 17..
 */
public class EducationRegActivity extends ActionBarActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private ConfigManager mConfigManager;

    private int[] mButtonTargetIds = {R.id.buttonTarget0, R.id.buttonTarget1, R.id.buttonTarget2
            , R.id.buttonTarget3, R.id.buttonTarget4, R.id.buttonTarget5, R.id.buttonTarget6
            , R.id.buttonTarget7,};
    private int[] mEditTextIds = {R.id.editTextEduName, R.id.editTextEduLocation, R.id.editTextEduPart, R.id.editTextEduTime, R.id.editTextEduStart, R.id.editTextEduEnd,};


    private CheckableButton[] mCheckableButtonTargets = new CheckableButton[mButtonTargetIds.length];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_reg);
        setActionBar();
        mConfigManager = ConfigManager.getInstance(getContext());

        findViewById(R.id.buttonReg).setOnClickListener(this);

        for (int i = 0; i < mButtonTargetIds.length; i++) {
            mCheckableButtonTargets[i] = (CheckableButton) findViewById(mButtonTargetIds[i]);
        }

        for (int id : mEditTextIds) {
            findViewById(id).setOnFocusChangeListener(this);
            findViewById(id).setOnClickListener(this);
        }
    }

    private void setActionBar() {
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException ignore) {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(final View v) {

        switch (v.getId()) {
            case R.id.editTextEduStart:
            case R.id.editTextEduEnd:
                setDate(v);
                break;
            case R.id.buttonReg:

                EducationModel mEducationModel = new EducationModel();
                mEducationModel.setEduName(findViewById(R.id.editTextEduName));
                mEducationModel.setEduLocation(findViewById(R.id.editTextEduLocation));
                mEducationModel.setEduPart(findViewById(R.id.editTextEduPart));
                mEducationModel.setEduStart(findViewById(R.id.editTextEduStart));
                mEducationModel.setEduEnd(findViewById(R.id.editTextEduEnd));

                mEducationModel.setEduTarget(mCheckableButtonTargets);
                mEducationModel.setEduType((RadioButton) findViewById(((RadioGroup) findViewById(R.id.radioGroupType)).getCheckedRadioButtonId()));

                if (!mEducationModel.checkSum()) {
                    return;
                }

                DBManager.addEdu(mEducationModel);
                setResult(RESULT_OK);
                finish();
                break;
        }

    }

    @Override
    public void onFocusChange(final View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
                case R.id.editTextEduStart:
                case R.id.editTextEduEnd:
                    setDate(v);
                    break;
            }
        } else {
            if (v instanceof EditText) {
                ((EditText) v).setError(null);
            }
        }
    }

    private void setDate(final View v) {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
                if (view.isShown()) {
                    new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            if (view.isShown()) {
                                String date = year + "/" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1) + "") + "/" + dayOfMonth + " " + (hourOfDay < 10 ? "0" + hourOfDay : hourOfDay + "") + ":" + (minute < 10 ? "0" + minute : minute + "");
                                ((EditText) v).setText(date);
                                String startTimeTmp = ((EditText) findViewById(R.id.editTextEduStart)).getText().toString();
                                String endTimeTmp = ((EditText) findViewById(R.id.editTextEduEnd)).getText().toString();

                                if (!startTimeTmp.equals("") && !endTimeTmp.equals("")) {
                                    String eduTime = EducationModel.getEduTime(startTimeTmp, endTimeTmp);
                                    if (eduTime == null) {
                                        ((EditText) v).setText("");
                                        ((EditText) v).setError("일정 종료시간이 일정 시작시간 보다 같거나 빠를 수 없습니다.");
                                    } else {
                                        ((EditText) findViewById(R.id.editTextEduTime)).setText(eduTime);
                                        ((EditText) v).setError(null);
                                    }
                                }
                            }
                        }
                    }, mConfigManager.getHour(), mConfigManager.getMinute(), false).show();
                }
            }
        }, mConfigManager.getYear(), mConfigManager.getMonth(), mConfigManager.getDay()).show();
    }


    private Context getContext() {
        return this;
    }
}
