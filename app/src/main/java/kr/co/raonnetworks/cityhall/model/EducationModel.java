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

import kr.co.raonnetworks.cityhall.R;
import kr.co.raonnetworks.cityhall.libs.CheckableButton;

/**
 * Created by MoonJongRak on 2016. 2. 17..
 */
public class EducationModel implements Serializable {

    private SimpleDateFormat mSimpleDateFormat;

    private String eduName;
    private String eduLocation;
    private String eduPart;
    private String eduTime;
    private String eduType;
    private Date eduEnd, eduStart;

    private boolean[] isTarget;

    public EducationModel() {
        mSimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
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


    public String getEduTime() {
        return eduTime;
    }

    public void setEduTime(String eduTime) {
        this.eduTime = eduTime;
    }

    public void setEduTime(View eduTime) {
        this.eduTime = getStringFromView(eduTime);
    }

    public Date getEduEnd() {
        return eduEnd;
    }

    public void setEduEnd(Date eduEnd) {
        this.eduEnd = eduEnd;
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

    public void setEduStart(Date eduStart) {
        this.eduStart = eduStart;
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

    public boolean[] getIsTarget() {
        return isTarget;
    }

    public void setIsTarget(CheckableButton[] buttons) {
        this.isTarget = new boolean[buttons.length];
        for (int i = 0; i < buttons.length; i++) {
            this.isTarget[i] = buttons[i].isChecked();
        }
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

    public boolean checkSum() {
        return this.eduName != null && this.eduLocation != null && this.eduPart != null && this.eduTime != null && this.eduType != null && this.eduEnd != null && this.isTarget != null;
    }


    @Override
    public String toString() {
        Map<String, String> tmp = new HashMap<>();
        tmp.put("eduName", this.eduName);
        tmp.put("eduLocation", this.eduLocation);
        tmp.put("eduPart", this.eduPart);
        tmp.put("eduTime", this.eduTime);
        tmp.put("eduStart", this.eduStart != null ? Long.toString(this.eduStart.getTime()) : null);
        tmp.put("eduEnd", this.eduEnd != null ? Long.toString(this.eduEnd.getTime()) : null);
        tmp.put("eduType", this.eduType);
        tmp.put("isTarget", Arrays.toString(isTarget));

        return tmp.toString();
    }


}
