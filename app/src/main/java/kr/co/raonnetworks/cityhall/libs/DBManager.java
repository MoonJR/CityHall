package kr.co.raonnetworks.cityhall.libs;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import kr.co.raonnetworks.cityhall.model.AttendanceModel;
import kr.co.raonnetworks.cityhall.model.EducationModel;
import kr.co.raonnetworks.cityhall.model.WorkerModel;

/**
 * Created by MoonJongRak on 2015. 11. 27..
 */
public class DBManager {
    public static void addEdu(Context context, EducationModel educationModel) {
        if (!educationModel.checkSum()) {
            throw new CityHallDBException();
        }
        SQLiteDatabase db = SQLiteManager.getInstance(context).getDataBase();
        String query = makeQuery("INSERT INTO EDUCATION (EDU_ID, EDU_NAME, EDU_LOCATION, EDU_PART, EDU_START, EDU_END, EDU_TARGET, EDU_TYPE, EDU_UPLOADED) VALUES(?,?,?,?,?,?,?,?,?)", educationModel.toObjectArray());
        db.execSQL(query);
    }

    public static ArrayList<EducationModel> getEdu(Context context) {
        SQLiteDatabase db = SQLiteManager.getInstance(context).getDataBase();
        String query = "SELECT EDU_ID, EDU_NAME, EDU_LOCATION, EDU_PART, EDU_START, EDU_END, EDU_TARGET, EDU_TYPE, EDU_UPLOADED, (SELECT COUNT(*) FROM ATTENDANCE WHERE ATTENDANCE.EDU_ID = EDUCATION.EDU_ID) AS COUNT FROM EDUCATION ORDER BY EDU_START DESC";

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<EducationModel> modelList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                EducationModel mEducationModelTmp = new EducationModel();
                mEducationModelTmp.setEduId(cursor.getString(cursor.getColumnIndex("EDU_ID")));
                mEducationModelTmp.setEduName(cursor.getString(cursor.getColumnIndex("EDU_NAME")));
                mEducationModelTmp.setEduLocation(cursor.getString(cursor.getColumnIndex("EDU_LOCATION")));
                mEducationModelTmp.setEduPart(cursor.getString(cursor.getColumnIndex("EDU_PART")));
                mEducationModelTmp.setEduStart(cursor.getLong(cursor.getColumnIndex("EDU_START")));
                mEducationModelTmp.setEduEnd(cursor.getLong(cursor.getColumnIndex("EDU_END")));
                mEducationModelTmp.setEduTarget(cursor.getInt(cursor.getColumnIndex("EDU_TARGET")));
                mEducationModelTmp.setEduType(cursor.getString(cursor.getColumnIndex("EDU_TYPE")));
                mEducationModelTmp.setEduUploaded(cursor.getInt(cursor.getColumnIndex("EDU_UPLOADED")));
                mEducationModelTmp.setEduAttendanceCount(cursor.getInt(cursor.getColumnIndex("COUNT")));
                modelList.add(mEducationModelTmp);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return modelList;
    }

    public static EducationModel getEdu(Context context, String eduId) {
        SQLiteDatabase db = SQLiteManager.getInstance(context).getDataBase();
        String query = makeQuery("SELECT EDU_ID, EDU_NAME, EDU_LOCATION, EDU_PART, EDU_START, EDU_END, EDU_TARGET, EDU_TYPE, EDU_UPLOADED, (SELECT COUNT(*) FROM ATTENDANCE WHERE ATTENDANCE.EDU_ID = EDUCATION.EDU_ID) AS COUNT FROM EDUCATION WHERE EDU_ID=?", new Object[]{eduId});

        Cursor cursor = db.rawQuery(query, null);

        EducationModel mEducationModelTmp = null;
        if (cursor.moveToFirst()) {
            mEducationModelTmp = new EducationModel();
            mEducationModelTmp.setEduId(cursor.getString(cursor.getColumnIndex("EDU_ID")));
            mEducationModelTmp.setEduName(cursor.getString(cursor.getColumnIndex("EDU_NAME")));
            mEducationModelTmp.setEduLocation(cursor.getString(cursor.getColumnIndex("EDU_LOCATION")));
            mEducationModelTmp.setEduPart(cursor.getString(cursor.getColumnIndex("EDU_PART")));
            mEducationModelTmp.setEduStart(cursor.getLong(cursor.getColumnIndex("EDU_START")));
            mEducationModelTmp.setEduEnd(cursor.getLong(cursor.getColumnIndex("EDU_END")));
            mEducationModelTmp.setEduTarget(cursor.getInt(cursor.getColumnIndex("EDU_TARGET")));
            mEducationModelTmp.setEduType(cursor.getString(cursor.getColumnIndex("EDU_TYPE")));
            mEducationModelTmp.setEduUploaded(cursor.getInt(cursor.getColumnIndex("EDU_UPLOADED")));
            mEducationModelTmp.setEduAttendanceCount(cursor.getInt(cursor.getColumnIndex("COUNT")));

        }
        cursor.close();
        return mEducationModelTmp;
    }


    //경우의수 이미 출석이 완료 되었거나 or 출석 해당자가 아닐경우
    public static boolean addAttendance(Context context, EducationModel eduModel, WorkerModel workerModel) {

        SQLiteDatabase db = SQLiteManager.getInstance(context).getDataBase();
        long attendanceId = (eduModel.getEduId() + workerModel.getWorkerCard()).hashCode();
        String query = makeQuery("INSERT INTO ATTENDANCE (ATTENDANCE_ID, EDU_ID, WORKER_ID, WORKER_CARD, ATTENDANCE_TIME) VALUES (?,?,?,?,?)", new Object[]{attendanceId, eduModel.getEduId(), workerModel.getWorkerId(), workerModel.getWorkerCard(), System.currentTimeMillis()});
        try {
            db.execSQL(query);
        } catch (SQLiteConstraintException e) {
            return false;
        }
        return true;
    }

    public static ArrayList<AttendanceModel> getAttendance(Context context, EducationModel eduModel) {

        SQLiteDatabase db = SQLiteManager.getInstance(context).getDataBase();
        String query = makeQuery("SELECT ATTENDANCE.ATTENDANCE_ID, ATTENDANCE.EDU_ID, ATTENDANCE.WORKER_ID, ATTENDANCE.WORKER_CARD, ATTENDANCE.ATTENDANCE_TIME, WORKER.WORKER_NAME FROM ATTENDANCE LEFT OUTER JOIN WORKER ON ATTENDANCE.WORKER_ID=WORKER.WORKER_ID WHERE EDU_ID=?", new Object[]{eduModel.getEduId()});
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<AttendanceModel> modelList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                AttendanceModel mAttAttendanceModel = new AttendanceModel();
                mAttAttendanceModel.setAttendanceId(cursor.getInt(cursor.getColumnIndex("ATTENDANCE_ID")));
                mAttAttendanceModel.setEduId(cursor.getString(cursor.getColumnIndex("EDU_ID")));
                mAttAttendanceModel.setWorkerId(cursor.getString(cursor.getColumnIndex("WORKER_ID")));
                mAttAttendanceModel.setAttendanceTime(cursor.getLong(cursor.getColumnIndex("ATTENDANCE_TIME")));
                mAttAttendanceModel.setWorkerCard(cursor.getLong(cursor.getColumnIndex("WORKER_CARD")));
                mAttAttendanceModel.setWorkerName(cursor.getString(cursor.getColumnIndex("WORKER_NAME")));
                modelList.add(mAttAttendanceModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return modelList;
    }


    public static void addWorker(Context context, WorkerModel workerModel) {
        SQLiteDatabase db = SQLiteManager.getInstance(context).getDataBase();
        String query = makeQuery("INSERT OR REPLACE INTO WORKER VALUES(?,?,?,?,?)", workerModel.toObjectArray());
        db.execSQL(query);
        query = makeQuery("UPDATE ATTENDANCE SET WORKER_ID = ? WHERE WORKER_CARD = ?", new Object[]{workerModel.getWorkerId(), workerModel.getWorkerCard()});
        db.execSQL(query);
    }

    public static ArrayList<WorkerModel> getWorker(Context context) {
        SQLiteDatabase db = SQLiteManager.getInstance(context).getDataBase();
        String query = "SELECT WORKER_ID, WORKER_PART, WORKER_NAME, WORKER_CARD, WORKER_STATUS FROM WORKER";

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<WorkerModel> modelList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                WorkerModel mWorkerModelTmp = new WorkerModel();
                mWorkerModelTmp.setWorkerId(cursor.getString(cursor.getColumnIndex("WORKER_ID")));
                mWorkerModelTmp.setWorkerPart(cursor.getString(cursor.getColumnIndex("WORKER_PART")));
                mWorkerModelTmp.setWorkerName(cursor.getString(cursor.getColumnIndex("WORKER_NAME")));
                mWorkerModelTmp.setWorkerCard(cursor.getInt(cursor.getColumnIndex("WORKER_CARD")));
                mWorkerModelTmp.setWorkerStatus(cursor.getInt(cursor.getColumnIndex("WORKER_STATUS")));
                modelList.add(mWorkerModelTmp);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return modelList;
    }

    public static WorkerModel getWorker(Context context, long workerCard) {
        SQLiteDatabase db = SQLiteManager.getInstance(context).getDataBase();
        String query = makeQuery("SELECT WORKER_ID, WORKER_PART, WORKER_NAME, WORKER_CARD, WORKER_STATUS FROM WORKER WHERE WORKER_CARD = ?", new Object[]{workerCard});

        Cursor cursor = db.rawQuery(query, null);

        WorkerModel mWorkerModelTmp = null;
        if (cursor.moveToFirst()) {
            mWorkerModelTmp = new WorkerModel();
            mWorkerModelTmp.setWorkerId(cursor.getString(cursor.getColumnIndex("WORKER_ID")));
            mWorkerModelTmp.setWorkerPart(cursor.getString(cursor.getColumnIndex("WORKER_PART")));
            mWorkerModelTmp.setWorkerName(cursor.getString(cursor.getColumnIndex("WORKER_NAME")));
            mWorkerModelTmp.setWorkerCard(cursor.getInt(cursor.getColumnIndex("WORKER_CARD")));
            mWorkerModelTmp.setWorkerStatus(cursor.getInt(cursor.getColumnIndex("WORKER_STATUS")));
        }
        cursor.close();

        return mWorkerModelTmp;
    }

    public static void resetWorker(Context context) {
        SQLiteDatabase db = SQLiteManager.getInstance(context).getDataBase();
        String query = "DELETE FROM WORKER";
        db.execSQL(query);
    }

    private static String makeQuery(String query, Object[] datas) throws CityHallDBException {
        for (Object data : datas) {
            if (data instanceof String) {
                query = query.replaceFirst("\\?", "'" + data + "'");
            } else if (data instanceof Number) {
                query = query.replaceFirst("\\?", data.toString());
            } else if (data instanceof Date) {
                query = query.replaceFirst("\\?", Long.toString(((Date) data).getTime()));
            } else if (data == null) {
                query = query.replaceFirst("\\?", "NULL");
            } else {
                throw new CityHallDBException();
            }
        }
        return query;
    }

    public static class CityHallDBException extends SQLiteException {
        public CityHallDBException() {
            super();
            System.err.println("쿼리를 확인하세요.");
        }
    }
}
