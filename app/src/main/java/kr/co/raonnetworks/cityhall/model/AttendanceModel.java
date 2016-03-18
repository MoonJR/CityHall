package kr.co.raonnetworks.cityhall.model;

import java.util.Date;

/**
 * Created by MoonJongRak on 2016. 2. 25..
 */
public class AttendanceModel {

    private long attendanceId;
    private String workerCard;
    private String eduId;
    private String workerId;
    private String workerName;
    private String workerPart;
    private String workerDivision;
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

    public String getWorkerCard() {
        return workerCard;
    }

    public void setWorkerCard(String workerCard) {
        this.workerCard = workerCard;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getWorkerPart() {
        return workerPart;
    }

    public void setWorkerPart(String workerPart) {
        this.workerPart = workerPart;
    }


    public static long makeAttendanceId(EducationModel eduModel, WorkerModel workerModel) {
        return workerModel.getWorkerId() == null ? eduModel.getEduId().hashCode() + workerModel.getWorkerCard().hashCode() : eduModel.getEduId().hashCode() + workerModel.getWorkerId().hashCode();
    }

    public String getWorkerDivision() {
        return workerDivision;
    }

    public void setWorkerDivision(String workerDivision) {
        this.workerDivision = workerDivision;
    }
}
