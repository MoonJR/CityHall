package kr.co.raonnetworks.cityhall.libs;

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
    public static void addEdu(EducationModel educationModel) {
        if (!educationModel.checkSum()) {
            throw new CityHallDBException(CityHallDBException.FLAG_DB_ERROR);
        }
        SQLiteDatabase db = SQLiteManager.getInstance().getDataBase();
        String query = makeQuery("INSERT INTO EDUCATION (EDU_ID, EDU_NAME, EDU_LOCATION, EDU_PART, EDU_START, EDU_END, EDU_TARGET, EDU_TYPE, EDU_UPLOADED) VALUES(?,?,?,?,?,?,?,?,?)", educationModel.toObjectArray());
        db.execSQL(query);
    }

    public static ArrayList<EducationModel> getEdu() {
        SQLiteDatabase db = SQLiteManager.getInstance().getDataBase();
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

    public static EducationModel getEdu(String eduId) {
        SQLiteDatabase db = SQLiteManager.getInstance().getDataBase();
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

    public static ArrayList<EducationModel> getEduForUpload() {
        SQLiteDatabase db = SQLiteManager.getInstance().getDataBase();
        String query = makeQuery("SELECT EDU_ID, EDU_NAME, EDU_LOCATION, EDU_PART, EDU_START, EDU_END, EDU_TARGET, EDU_TYPE, EDU_UPLOADED, (SELECT COUNT(*) FROM ATTENDANCE WHERE ATTENDANCE.EDU_ID = EDUCATION.EDU_ID) AS COUNT FROM EDUCATION WHERE EDUCATION.EDU_UPLOADED = ? ORDER BY EDU_START DESC", new Object[]{EducationModel.FLAG_EDUCATION_NOT_UPLOADED});

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

    public static boolean haveNewAttendanceList() {
        SQLiteDatabase db = SQLiteManager.getInstance().getDataBase();
        String query = makeQuery("SELECT COUNT(*) AS COUNT FROM EDUCATION WHERE EDU_UPLOADED = ?", new Object[]{EducationModel.FLAG_EDUCATION_NOT_UPLOADED});

        Cursor cursor = db.rawQuery(query, null);

        int count = 0;

        if (cursor.moveToFirst()) {
            do {
                count = cursor.getInt(cursor.getColumnIndex("COUNT"));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return count != 0;

    }


    public static void deleteEdu(EducationModel educationModel) {
        SQLiteDatabase db = SQLiteManager.getInstance().getDataBase();
        String query = makeQuery("DELETE FROM EDUCATION WHERE EDU_ID = ?", new Object[]{educationModel.getEduId()});
        db.execSQL(query);
    }

    public static boolean addAttendance(EducationModel eduModel, WorkerModel workerModel) {

        SQLiteDatabase db = SQLiteManager.getInstance().getDataBase();

        long attendanceId = AttendanceModel.makeAttendanceId(eduModel, workerModel);

        String query = makeQuery("INSERT INTO ATTENDANCE (ATTENDANCE_ID, EDU_ID, WORKER_ID, WORKER_CARD, ATTENDANCE_TIME) VALUES (?,?,?,?,?)", new Object[]{attendanceId, eduModel.getEduId(), workerModel.getWorkerId(), workerModel.getWorkerCard(), System.currentTimeMillis()});
        try {
            db.execSQL(query);
            query = makeQuery("UPDATE EDUCATION SET EDU_UPLOADED = ? WHERE EDU_ID = ?", new Object[]{EducationModel.FLAG_EDUCATION_NOT_UPLOADED, eduModel.getEduId()});
            db.execSQL(query);
        } catch (SQLiteConstraintException e) {
            query = makeQuery("UPDATE ATTENDANCE SET WORKER_CARD = ? WHERE ATTENDANCE_ID = ?", new Object[]{workerModel.getWorkerCard(), attendanceId});
            db.execSQL(query);
            return false;
        }
        return true;
    }

    public static void deleteAttendance(EducationModel eduModel, WorkerModel workerModel) {
        SQLiteDatabase db = SQLiteManager.getInstance().getDataBase();

        long attendanceId = AttendanceModel.makeAttendanceId(eduModel, workerModel);

        String query = makeQuery("DELETE FROM ATTENDANCE WHERE ATTENDANCE_ID = ?", new Object[]{attendanceId});
        db.execSQL(query);
    }

    public static ArrayList<AttendanceModel> getAttendance(EducationModel eduModel) {

        SQLiteDatabase db = SQLiteManager.getInstance().getDataBase();
        String query = makeQuery("SELECT ATTENDANCE.ATTENDANCE_ID, ATTENDANCE.WORKER_ID, ATTENDANCE.WORKER_CARD, ATTENDANCE.ATTENDANCE_TIME, WORKER.WORKER_NAME, WORKER.WORKER_PART, WORKER.WORKER_DIVISION FROM ATTENDANCE LEFT OUTER JOIN WORKER ON ATTENDANCE.WORKER_ID=WORKER.WORKER_ID WHERE EDU_ID=? ORDER BY ATTENDANCE.ATTENDANCE_TIME DESC ", new Object[]{eduModel.getEduId()});
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<AttendanceModel> modelList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                AttendanceModel mAttAttendanceModel = new AttendanceModel();
                mAttAttendanceModel.setAttendanceId(cursor.getInt(cursor.getColumnIndex("ATTENDANCE_ID")));
                mAttAttendanceModel.setEduId(eduModel.getEduId());
                mAttAttendanceModel.setWorkerId(cursor.getString(cursor.getColumnIndex("WORKER_ID")));
                mAttAttendanceModel.setAttendanceTime(cursor.getLong(cursor.getColumnIndex("ATTENDANCE_TIME")));
                mAttAttendanceModel.setWorkerCard(cursor.getString(cursor.getColumnIndex("WORKER_CARD")));
                mAttAttendanceModel.setWorkerName(cursor.getString(cursor.getColumnIndex("WORKER_NAME")));
                mAttAttendanceModel.setWorkerPart(cursor.getString(cursor.getColumnIndex("WORKER_PART")));
                mAttAttendanceModel.setWorkerDivision(cursor.getString(cursor.getColumnIndex("WORKER_DIVISION")));
                modelList.add(mAttAttendanceModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return modelList;
    }

    public static ArrayList<AttendanceModel> getAttendanceStatus(EducationModel eduModel, String workerName) {

        SQLiteDatabase db = SQLiteManager.getInstance().getDataBase();
        workerName = "%" + workerName + "%";
        String query = makeQuery("SELECT WORKER.WORKER_ID, WORKER.WORKER_CARD, WORKER.WORKER_NAME, WORKER.WORKER_PART, WORKER.WORKER_DIVISION, ATTENDANCE.ATTENDANCE_ID, ATTENDANCE.ATTENDANCE_TIME FROM WORKER LEFT OUTER JOIN ATTENDANCE ON WORKER.WORKER_ID = ATTENDANCE.WORKER_ID AND ATTENDANCE.EDU_ID=? WHERE WORKER.WORKER_NAME LIKE ? ORDER BY WORKER.WORKER_NAME", new Object[]{eduModel.getEduId(), workerName});

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<AttendanceModel> modelList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                AttendanceModel mAttAttendanceModel = new AttendanceModel();
                mAttAttendanceModel.setAttendanceId(cursor.getInt(cursor.getColumnIndex("ATTENDANCE_ID")));
                mAttAttendanceModel.setEduId(eduModel.getEduId());
                mAttAttendanceModel.setWorkerId(cursor.getString(cursor.getColumnIndex("WORKER_ID")));
                mAttAttendanceModel.setAttendanceTime(cursor.getLong(cursor.getColumnIndex("ATTENDANCE_TIME")));
                mAttAttendanceModel.setWorkerCard(cursor.getString(cursor.getColumnIndex("WORKER_CARD")));
                mAttAttendanceModel.setWorkerName(cursor.getString(cursor.getColumnIndex("WORKER_NAME")));
                mAttAttendanceModel.setWorkerPart(cursor.getString(cursor.getColumnIndex("WORKER_PART")));
                mAttAttendanceModel.setWorkerDivision(cursor.getString(cursor.getColumnIndex("WORKER_DIVISION")));
                modelList.add(mAttAttendanceModel);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return modelList;
    }

    public static void addWorker(WorkerModel workerModel) {
        SQLiteDatabase db = SQLiteManager.getInstance().getDataBase();
        String query;
        if (workerModel.getWorkerStatus() == WorkerModel.FLAG_ACTIVE_WORKER) {
            try {
                query = makeQuery("INSERT OR REPLACE INTO WORKER VALUES(?,?,?,?,?,?)", workerModel.toObjectArray());
                db.execSQL(query);
            } catch (SQLiteConstraintException ignored) {

            }
            long attendanceId = workerModel.getWorkerId().hashCode() - workerModel.getWorkerCard().hashCode();
            query = makeQuery("UPDATE ATTENDANCE SET ATTENDANCE_ID = ATTENDANCE_ID + ?, WORKER_ID = ? WHERE WORKER_ID IS NULL AND WORKER_CARD = ?", new Object[]{attendanceId, workerModel.getWorkerId(), workerModel.getWorkerCard()});
            db.execSQL(query);
        } else {
            deleteWorker(workerModel);
        }

    }

    private static void deleteWorker(WorkerModel workerModel) {
        SQLiteDatabase db = SQLiteManager.getInstance().getDataBase();
        String query = makeQuery("DELETE FROM WORKER WHERE WORKER_ID = ?", new Object[]{workerModel.getWorkerId()});
        db.execSQL(query);
    }

    public static WorkerModel getWorker(String workerCard) {
        SQLiteDatabase db = SQLiteManager.getInstance().getDataBase();
        String query = makeQuery("SELECT WORKER_ID, WORKER_PART, WORKER_NAME, WORKER_CARD, WORKER_DIVISION, WORKER_STATUS FROM WORKER WHERE WORKER_CARD = ?", new Object[]{workerCard});

        Cursor cursor = db.rawQuery(query, null);

        WorkerModel mWorkerModelTmp = null;
        if (cursor.moveToFirst()) {
            mWorkerModelTmp = new WorkerModel();
            mWorkerModelTmp.setWorkerId(cursor.getString(cursor.getColumnIndex("WORKER_ID")));
            mWorkerModelTmp.setWorkerPart(cursor.getString(cursor.getColumnIndex("WORKER_PART")));
            mWorkerModelTmp.setWorkerName(cursor.getString(cursor.getColumnIndex("WORKER_NAME")));
            mWorkerModelTmp.setWorkerCard(cursor.getString(cursor.getColumnIndex("WORKER_CARD")));
            mWorkerModelTmp.setWorkerDivision(cursor.getString(cursor.getColumnIndex("WORKER_DIVISION")));
            mWorkerModelTmp.setWorkerStatus(cursor.getInt(cursor.getColumnIndex("WORKER_STATUS")));
        }
        cursor.close();

        return mWorkerModelTmp;
    }

    public static void resetWorker() {
        SQLiteDatabase db = SQLiteManager.getInstance().getDataBase();
        String query = "DELETE FROM WORKER";
        db.execSQL(query);
    }

    public static void uploadEducation(EducationModel eduModel) {
        SQLiteDatabase db = SQLiteManager.getInstance().getDataBase();
        String query = makeQuery("UPDATE EDUCATION SET EDU_UPLOADED = ? WHERE EDU_ID = ?", new Object[]{EducationModel.FLAG_EDUCATION_UPLOADED, eduModel.getEduId()});
        db.execSQL(query);
    }

    public static void uploadEducation() {
        SQLiteDatabase db = SQLiteManager.getInstance().getDataBase();
        String query = makeQuery("UPDATE EDUCATION SET EDU_UPLOADED = ?", new Object[]{EducationModel.FLAG_EDUCATION_UPLOADED});
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
                throw new CityHallDBException(CityHallDBException.FLAG_DB_ERROR);
            }
        }
        return query;
    }

    public static class CityHallDBException extends SQLiteException {

        public static final int FLAG_NO_UPLOAD_DATA = 1;
        public static final int FLAG_DB_ERROR = 2;

        private int flag;

        public CityHallDBException(int flag) {
            super();
            if (flag == FLAG_DB_ERROR) {
                System.err.println("쿼리를 확인하세요.");
            } else if (flag == FLAG_NO_UPLOAD_DATA) {
                System.err.println("업로드할 데이터가 없습니다.");
            }
            this.flag = flag;
        }

        public int getFlag() {
            return flag;
        }
    }
}
