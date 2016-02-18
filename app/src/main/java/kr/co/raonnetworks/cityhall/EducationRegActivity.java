package kr.co.raonnetworks.cityhall;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import kr.co.raonnetworks.cityhall.libs.CheckableButton;
import kr.co.raonnetworks.cityhall.model.EducationModel;

/**
 * Created by MoonJongRak on 2016. 2. 17..
 */
public class EducationRegActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {


    int[] mButtonTargetIds = {R.id.buttonTarget0, R.id.buttonTarget1, R.id.buttonTarget2
            , R.id.buttonTarget3, R.id.buttonTarget4, R.id.buttonTarget5, R.id.buttonTarget6
            , R.id.buttonTarget7,};
    int[] mEditTextIds = {R.id.editTextEduName, R.id.editTextEduLocation, R.id.editTextEduPart, R.id.editTextEduTime, R.id.editTextEduStart, R.id.editTextEduEnd,};


    CheckableButton[] mCheckableButtonTargets = new CheckableButton[mButtonTargetIds.length];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_reg);

        findViewById(R.id.buttonReg).setOnClickListener(this);

        for (int i = 0; i < mButtonTargetIds.length; i++) {
            mCheckableButtonTargets[i] = (CheckableButton) findViewById(mButtonTargetIds[i]);
        }

        for (int id : mEditTextIds) {
            findViewById(id).setOnFocusChangeListener(this);
        }


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
                mEducationModel.setEduTime(findViewById(R.id.editTextEduTime));
                mEducationModel.setEduStart(findViewById(R.id.editTextEduStart));
                mEducationModel.setEduEnd(findViewById(R.id.editTextEduEnd));

                mEducationModel.setIsTarget(mCheckableButtonTargets);
                mEducationModel.setEduType((RadioButton) findViewById(((RadioGroup) findViewById(R.id.radioGroupType)).getCheckedRadioButtonId()));

                if (!mEducationModel.checkSum()) {
                    return;
                }

                Log.d("test", mEducationModel.toString());
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
                                String date = year + "/" + monthOfYear + "/" + dayOfMonth + " " + (hourOfDay < 10 ? "0" + hourOfDay : hourOfDay + "") + ":" + (minute < 10 ? "0" + minute : minute + "");
                                ((EditText) v).setText(date);
                            }
                        }
                    }, 9, 0, false).show();
                }
            }
        }, 2016, 1, 24).show();
    }


    private Context getContext() {
        return this;
    }
}
