package kr.co.raonnetworks.cityhall.model;

import java.util.Date;

/**
 * Created by MoonJongRak on 2016. 2. 25..
 */
public class AttendanceModel {

    private long attendanceId;
    private long workerCard;
    private String eduId;
    private String workerId;
    private String workerName;
    private Date attendanceTime;

    public long getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(long attendanceId) {
        this.attendanceId = attendanceId;
    }

    public String getEduId() {
        return eduId;
    }

    public void setEduId(String eduId) {
        this.eduId = eduId;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public Date getAttendanceTime() {
        return attendanceTime;
    }

    public void setAttendanceTime(Date attendanceTime) {
        this.attendanceTime = attendanceTime;
    }

    public void setAttendanceTime(long attendanceTime) {
        this.attendanceTime = new Date(attendanceTime);
    }

    public long getWorkerCard() {
        return workerCard;
    }

    public void setWorkerCard(long workerCard) {
        this.workerCard = workerCard;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }
}
