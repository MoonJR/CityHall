package kr.co.raonnetworks.cityhall.libs;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.Date;

import kr.co.raonnetworks.cityhall.model.EducationModel;
import kr.co.raonnetworks.cityhall.model.WorkerModel;

/**
 * Created by MoonJongRak on 2015. 11. 27..
 */
public class DBManager {
    public static final byte FLAG_SUCCESS_ATTENDANCE = 0x00;
    public static final byte FLAG_ALREADLY_ATTENDANCE = 0x01;
    public static final byte FLAG_NOT_TARGET_ATTENDANCE = 0x02;

    public static void addEdu(Context context, EducationModel educationModel) {
        if (!educationModel.checkSum()) {
            throw new CityHallDBException();
        }
        SQLiteDatabase db = SQLiteManager.getInstance(context).getDataBase();
        String query = makeQuery("INSERT INTO EDUCATION (EDU_ID, EDU_NAME, EDU_LOCATION, EDU_PART, EDU_START, EDU_END, EDU_TARGET, EDU_TYPE) VALUES(?,?,?,?,?,?,?,?)", educationModel.toObjectArray());
        db.execSQL(query);
    }

    public static ArrayList<EducationModel> getEdu(Context context) {
        SQLiteDatabase db = SQLiteManager.getInstance(context).getDataBase();
        String query = "SELECT EDU_ID, EDU_NAME, EDU_LOCATION, EDU_PART, EDU_START, EDU_END, EDU_TARGET, EDU_TYPE FROM EDUCATION";

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
                modelList.add(mEducationModelTmp);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return modelList;
    }

    public static EducationModel getEdu(Context context, String eduId) {
        SQLiteDatabase db = SQLiteManager.getInstance(context).getDataBase();
        String query = makeQuery("SELECT EDU_ID, EDU_NAME, EDU_LOCATION, EDU_PART, EDU_START, EDU_END, EDU_TARGET, EDU_TYPE FROM EDUCATION WHERE EDU_ID=?", new Object[]{eduId});

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
        }
        cursor.close();
        return mEducationModelTmp;
    }


    //경우의수 이미 출석이 완료 되었거나 or 출석 해당자가 아닐경우
    public static byte addAttendance(Context context, EducationModel eduModel, WorkerModel workerModel) {

        if (eduModel.isEduTarget(workerModel)) {
            return FLAG_NOT_TARGET_ATTENDANCE;
        }

        SQLiteDatabase db = SQLiteManager.getInstance(context).getDataBase();
        long attendanceId = (eduModel.getEduId() + workerModel.getWorkerId()).hashCode();
        String query = makeQuery("INSERT INTO ATTENDANCE (ATTENDANCE_ID, EDU_ID, WORKER_ID) VALUES (?,?,?)", new Object[]{attendanceId, eduModel.getEduId(), workerModel.getWorkerId()});
        try {
            db.execSQL(query);
        } catch (SQLiteConstraintException e) {
            return FLAG_ALREADLY_ATTENDANCE;
        }
        return FLAG_SUCCESS_ATTENDANCE;
    }


    private static String makeQuery(String query, Object[] datas) throws CityHallDBException {
        for (Object data : datas) {
            if (data instanceof String) {
                query = query.replaceFirst("\\?", "'" + data + "'");
            } else if (data instanceof Number) {
                query = query.replaceFirst("\\?", data.toString());
            } else if (data instanceof Date) {
                query = query.replaceFirst("\\?", Long.toString(((Date) data).getTime()));
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
