package kr.co.raonnetworks.cityhall.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MoonJongRak on 2016. 2. 20..
 */
public class WorkerModel {

    private String workerId;
    private String workerName;
    private String workerPart;
    private String workerDivision;
    private long workerCard;
    private int workerStatus;


    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
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

    public long getWorkerCard() {
        return workerCard;
    }

    public void setWorkerCard(long workerCard) {
        this.workerCard = workerCard;
    }

    public int getWorkerStatus() {
        return workerStatus;
    }

    public void setWorkerStatus(int workerStatus) {
        this.workerStatus = workerStatus;
    }

    @Override
    public String toString() {
        Map<String, String> tmp = new HashMap<>();
        tmp.put("workerId", this.workerId);
        tmp.put("workerName", this.workerName);
        tmp.put("workerPart", this.workerPart);
        tmp.put("workerCard", Long.toString(this.workerCard));
        tmp.put("workerStatus", Integer.toString(this.workerStatus));
        tmp.put("workerDivision", this.workerDivision);
        return tmp.toString();
    }

    public Object[] toObjectArray() {
        return new Object[]{this.workerId, this.workerPart, this.workerName, this.workerCard, this.workerDivision,
                this.workerStatus};
    }

    public String getWorkerDivision() {
        return workerDivision;
    }

    public void setWorkerDivision(String workerDivision) {
        this.workerDivision = workerDivision;
    }
}
