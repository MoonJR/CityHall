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
        return tmp.toString();
    }

    public Object[] toObjectArray() {
        Object[] tmp = new Object[5];
        tmp[0] = this.workerId;
        tmp[1] = this.workerPart;
        tmp[2] = this.workerName;
        tmp[3] = this.workerCard;
        tmp[4] = this.workerStatus;
        return tmp;
    }
}
