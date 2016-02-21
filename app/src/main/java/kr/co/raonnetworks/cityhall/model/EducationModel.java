package kr.co.raonnetworks.cityhall.model;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import kr.co.raonnetworks.cityhall.R;
import kr.co.raonnetworks.cityhall.libs.CheckableButton;

/**
 * Created by MoonJongRak on 2016. 2. 17..
 */
public class EducationModel implements Serializable {


    public static final String EDU_DATA_PATTERN = "yyyy/MM/dd hh:mm";
    public static final int[] EDU_TARGET = {0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01,};
    private final String[] EDU_TARGET_NAME = {"전직원", "5급 이상", "6급", "7급", "8급", "9급", "기능직", "기타"};

    private SimpleDateFormat mSimpleDateFormat;

    private String eduId;
    private String eduName;
    private String eduLocation;
    private String eduPart;
    private String eduType;
    private int eduAttendanceNumber;
    private Date eduEnd, eduStart;

    private int eduTarget;


    public EducationModel() {
        mSimpleDateFormat = new SimpleDateFormat(EDU_DATA_PATTERN);
        this.eduId = UUID.randomUUID().toString();
    }

    public String getEduName() {
        return eduName;
    }

    public void setEduName(String eduName) {
        this.eduName = eduName;
    }

    public void setEduName(View eduName) {
        this.eduName = getStringFromView(eduName);
    }

    public String getEduLocation() {
        return eduLocation;
    }

    public void setEduLocation(String eduLocation) {
        this.eduLocation = eduLocation;
    }

    public void setEduLocation(View eduLocation) {
        this.eduLocation = getStringFromView(eduLocation);
    }

    public String getEduPart() {
        return eduPart;
    }

    public void setEduPart(String eduPart) {
        this.eduPart = eduPart;
    }

    public void setEduPart(View eduPart) {
        this.eduPart = getStringFromView(eduPart);
    }


    public Date getEduEnd() {
        return eduEnd;
    }

    public void setEduEnd(Date eduEnd) {
        this.eduEnd = eduEnd;
    }

    public void setEduEnd(long eduEnd) {
        this.eduEnd = new Date(eduEnd);
    }

    public void setEduEnd(View eduEnd) {
        try {
            this.eduEnd = mSimpleDateFormat.parse(getStringFromView(eduEnd));
        } catch (ParseException | NullPointerException e) {
            this.eduEnd = null;
        }
    }

    public Date getEduStart() {
        return eduStart;
    }

    public String getEduStartString() {
        return mSimpleDateFormat.format(this.eduStart);
    }

    public void setEduStart(Date eduStart) {
        this.eduStart = eduStart;
    }


    public void setEduStart(long eduStart) {
        this.eduStart = new Date(eduStart);
    }

    public String getEduEndString() {
        return mSimpleDateFormat.format(this.eduEnd);
    }

    public void setEduStart(View eduStart) {
        try {
            this.eduStart = mSimpleDateFormat.parse(getStringFromView(eduStart));
        } catch (ParseException | NullPointerException e) {
            this.eduStart = null;
        }
    }

    public String getEduType() {
        return eduType;
    }

    public void setEduType(String eduType) {
        this.eduType = eduType;
    }

    public void setEduType(RadioButton eduType) {
        switch (eduType.getId()) {
            case R.id.radioButtonType0:
                this.eduType = "시책";
                break;
            case R.id.radioButtonType1:
                this.eduType = "직무";
                break;
            case R.id.radioButtonType2:
                this.eduType = "소양";
                break;
            default:
                this.eduType = null;
                break;
        }
    }

    public int getEduTarget() {
        return eduTarget;
    }


    public String getEduTargetString() {
        String result = "";
        for (int i = 0; i < EDU_TARGET.length; i++) {
            if ((this.eduTarget & EDU_TARGET[i]) > 0) {
                result += EDU_TARGET_NAME[i] + ",";
            }
        }

        if (result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        } else if (result.equals("")) {
            result = "미지정";
        }

        return result;
    }

    public boolean isEduTarget(WorkerModel workerModel) {
        return (this.eduTarget & workerModel.getWorkerPosition()) > 0;
    }

    public void setEduTarget(CheckableButton[] buttons) {

        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].isChecked()) {
                this.eduTarget |= EDU_TARGET[i];
            }
        }
    }

    public void setEduTarget(int eduTarget) {
        this.eduTarget = eduTarget;
    }

    private String getStringFromView(View v) {
        if (v instanceof EditText) {
            EditText mEditText = (EditText) v;
            String tmp = mEditText.getText().toString().trim();
            if (tmp.equals("")) {
                mEditText.setError("해당 내용을 입력해 주세요.");
                return null;
            } else {
                return ((EditText) v).getText().toString();
            }
        } else {
            return null;
        }
    }

    public void setEduId(String eduId) {
        this.eduId = eduId;
    }

    public String getEduId() {
        return this.eduId;
    }

    public boolean checkSum() {
        return this.eduName != null && this.eduLocation != null && this.eduPart != null && this.eduType != null && this.eduEnd != null;
    }


    @Override
    public String toString() {
        Map<String, String> tmp = new HashMap<>();
        tmp.put("eduName", this.eduName);
        tmp.put("eduLocation", this.eduLocation);
        tmp.put("eduPart", this.eduPart);
        tmp.put("eduStart", this.eduStart != null ? Long.toString(this.eduStart.getTime()) : null);
        tmp.put("eduEnd", this.eduEnd != null ? Long.toString(this.eduEnd.getTime()) : null);
        tmp.put("eduType", this.eduType);
        tmp.put("eduTarget", Integer.toBinaryString(eduTarget));
        return tmp.toString();
    }

    public Object[] toObjectArray() {
        return new Object[]{eduId, eduName, eduLocation, eduPart, eduStart, eduEnd, eduTarget, eduType};
    }


    public int getEduAttendanceNumber() {
        return eduAttendanceNumber;
    }

    public void setEduAttendanceNumber(int eduAttendanceNumber) {
        this.eduAttendanceNumber = eduAttendanceNumber;
    }
}
